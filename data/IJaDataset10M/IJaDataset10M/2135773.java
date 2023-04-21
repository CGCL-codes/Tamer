package net.woodstock.nettool4j.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import net.woodstock.nettool4j.AbstractRunnable;

public class TCPRedirectHandler extends AbstractRunnable {

    private Socket socketInput;

    private Socket socketOutput;

    private long count;

    public TCPRedirectHandler(final Socket socketInput, final Socket socketOutput) {
        super();
        this.socketInput = socketInput;
        this.socketOutput = socketOutput;
        this.count = 0;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = this.socketInput.getInputStream();
            OutputStream outputStream = this.socketOutput.getOutputStream();
            int i = -1;
            do {
                if (!this.socketInput.isInputShutdown()) {
                    i = inputStream.read();
                    if (i != -1) {
                        this.count++;
                        if (!this.socketOutput.isOutputShutdown()) {
                            outputStream.write(i);
                        } else {
                            break;
                        }
                    }
                } else {
                    break;
                }
            } while (i != -1);
        } catch (IOException e) {
            this.getLogger().log(Level.FINEST, e.getMessage(), e);
        } finally {
            String addressInput = SocketUtils.toString(this.socketInput.getLocalSocketAddress());
            String addressOutput = SocketUtils.toString(this.socketInput.getRemoteSocketAddress());
            this.getLogger().log(Level.INFO, "Disconecting " + addressInput + ":" + " => " + addressOutput + " [" + this.count + " bytes]");
            try {
                this.socketInput.shutdownInput();
                this.socketInput.shutdownOutput();
                this.socketInput.close();
            } catch (IOException e) {
                this.getLogger().log(Level.FINEST, e.getMessage(), e);
            }
        }
    }
}
