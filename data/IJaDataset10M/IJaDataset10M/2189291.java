package netblend.slave.render;

import static netblend.NetBlendProtocol.BAD_PATH_ERROR;
import static netblend.NetBlendProtocol.BUSY_RENDERING_ERROR;
import static netblend.NetBlendProtocol.DISCONNECT_FROM_SERVER_COMMAND;
import static netblend.NetBlendProtocol.FILE_NOT_FOUND_ERROR;
import static netblend.NetBlendProtocol.GET_RENDER_FINSIHED_COMMAND;
import static netblend.NetBlendProtocol.NOT_RENDERING_ERROR;
import static netblend.NetBlendProtocol.OK_RESPONSE;
import static netblend.NetBlendProtocol.RENDERING_ERROR;
import static netblend.NetBlendProtocol.RENDERING_FINISHED_RESPONSE;
import static netblend.NetBlendProtocol.RENDER_SCENE_COMMAND;
import static netblend.NetBlendProtocol.SHUTDOWN_SERVER_COMMAND;
import static netblend.NetBlendProtocol.STILL_RENDERING_RESPONSE;
import static netblend.NetBlendProtocol.TERMINATE_RENDER_COMMAND;
import static netblend.NetBlendSystem.SOURCE_PATH;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import netblend.slave.SlaveMain;

public class RenderServerConnection extends Thread {

    protected static volatile RenderHandler render = null;

    protected Socket socket;

    protected DataInputStream dataIn;

    protected DataOutputStream dataOut;

    protected String renderExecutable;

    /**
	 * Creates a new connection handler.
	 * 
	 * @param socket
	 *            the socket on which this connection communicates.
	 * @param dataIn
	 *            the input stream for incoming data.
	 * @param dataOut
	 *            the output stream for outgoing data.
	 * @param renderExecutable
	 *            the path to the render executable.
	 */
    public RenderServerConnection(Socket socket, DataInputStream dataIn, DataOutputStream dataOut, String renderExecutable) {
        super("RenderServerConnection");
        this.socket = socket;
        this.dataIn = dataIn;
        this.dataOut = dataOut;
        this.renderExecutable = renderExecutable;
    }

    @Override
    public void run() {
        int command;
        try {
            do {
                command = dataIn.readInt();
                switch(command) {
                    case GET_RENDER_FINSIHED_COMMAND:
                        getRenderFinsihedCommand();
                        break;
                    case RENDER_SCENE_COMMAND:
                        renderSceneCommand();
                        break;
                    case TERMINATE_RENDER_COMMAND:
                        terminateRenderCommand();
                        break;
                    case DISCONNECT_FROM_SERVER_COMMAND:
                        dataIn.close();
                        dataOut.close();
                        socket.close();
                        System.out.println("Diconnected.");
                        return;
                    case SHUTDOWN_SERVER_COMMAND:
                        SlaveMain.shutdown();
                        return;
                    default:
                        System.err.println("Error: Bad request.");
                        socket.close();
                        return;
                }
            } while (command != DISCONNECT_FROM_SERVER_COMMAND);
        } catch (IOException e) {
        }
    }

    private void getRenderFinsihedCommand() throws IOException {
        if (render == null) dataOut.writeInt(NOT_RENDERING_ERROR); else if (render.isRendering()) dataOut.writeInt(STILL_RENDERING_RESPONSE); else {
            int exit = render.getExitCode();
            if (exit == 0) dataOut.writeInt(RENDERING_FINISHED_RESPONSE); else dataOut.writeInt(RENDERING_ERROR);
        }
    }

    private void terminateRenderCommand() {
        try {
            if (render == null || !render.isRendering()) {
                dataOut.writeInt(NOT_RENDERING_ERROR);
                return;
            }
            render.terminate();
            dataOut.writeInt(OK_RESPONSE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Renders the scene received as a file name.
	 * 
	 * @throws IOException
	 */
    private void renderSceneCommand() throws IOException {
        if (render != null && render.isRendering()) {
            dataOut.writeInt(BUSY_RENDERING_ERROR);
            return;
        }
        dataOut.writeInt(OK_RESPONSE);
        int dataLength = dataIn.readInt();
        byte[] bytes = new byte[dataLength];
        dataIn.readFully(bytes);
        String sourceFileName = new String(bytes);
        dataLength = dataIn.readInt();
        bytes = new byte[dataLength];
        dataIn.readFully(bytes);
        String outputFilePrefix = new String(bytes);
        if ((sourceFileName.indexOf("..") != -1) || (sourceFileName.indexOf(":") != -1) || sourceFileName.startsWith("/") || sourceFileName.startsWith("\\")) {
            dataOut.writeInt(BAD_PATH_ERROR);
            return;
        }
        if ((outputFilePrefix.indexOf("..") != -1) || (outputFilePrefix.indexOf(":") != -1) || outputFilePrefix.startsWith("/") || outputFilePrefix.startsWith("\\")) {
            dataOut.writeInt(BAD_PATH_ERROR);
            return;
        }
        File file = new File(SOURCE_PATH + File.separator + sourceFileName);
        if (!file.exists()) {
            dataOut.writeInt(FILE_NOT_FOUND_ERROR);
            return;
        }
        String fullPath = file.getAbsolutePath();
        dataOut.writeInt(OK_RESPONSE);
        int frame = dataIn.readInt();
        int stripCount = dataIn.readInt();
        int strip = dataIn.readInt();
        int outputFileType = dataIn.readInt();
        boolean terminateOnError = dataIn.readBoolean();
        dataOut.writeInt(OK_RESPONSE);
        System.out.println("Rendering: " + sourceFileName + " (frame " + frame + ", strip " + strip + " of " + stripCount + ")...");
        render = new RenderHandler(renderExecutable, fullPath, frame, stripCount, strip, outputFilePrefix, outputFileType, terminateOnError);
        render.start();
    }
}
