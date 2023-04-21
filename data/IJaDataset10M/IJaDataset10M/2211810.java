package jm2pc.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;
import jm2pc.client.config.Config;
import jm2pc.client.config.ConfigRepository;
import jm2pc.client.devices.out.CanvasScreen;
import jm2pc.client.devices.out.ImageCapture;
import jm2pc.client.devices.out.ListScreen;
import jm2pc.client.devices.out.Zoom;
import jm2pc.client.filesystem.ListFiles;
import jm2pc.client.i18n.LanguageRepository;
import jm2pc.client.i18n.MyResourceBundle;
import jm2pc.client.plugins.CanvasPlugins;
import jm2pc.client.plugins.PlugIns;
import jm2pc.client.servers.ListServers;
import jm2pc.client.servers.Server;
import jm2pc.client.shell.FormShell;
import jm2pc.client.utils.CanvasImage;
import jm2pc.client.utils.CanvasAbout;
import jm2pc.client.utils.CanvasWait;
import jm2pc.client.utils.DisplayManager;
import jm2pc.client.utils.TextString;
import jm2pc.utils.Constants;

public class JM2PCMIDlet extends MIDlet {

    private Display display;

    private DisplayManager displayManager;

    private CanvasWait cvWait;

    private CanvasScreen cvScreen;

    private List lsServers;

    private Menu lsCommands;

    private TextString tbString;

    private CanvasImage cvImage;

    private Zoom zoomImg;

    private int serverScreenWidth;

    private int serverScreenHeight;

    private StreamConnection connection;

    private InputStream in;

    private OutputStream out;

    private DataInputStream dataIn;

    private DataOutputStream dataOut;

    private ImageCapture imCapture;

    private int screenX;

    private int screenY;

    private long totalBytes;

    private boolean cancelled;

    private boolean alertOk;

    private boolean connected;

    private String lastCommand;

    public MyResourceBundle messages;

    public JM2PCMIDlet() {
        messages = new MyResourceBundle();
        LanguageRepository repLang = new LanguageRepository();
        String locale = repLang.loadLanguage();
        messages.loadMessages(locale);
        cancelled = false;
        alertOk = false;
        zoomImg = new Zoom();
        display = Display.getDisplay(this);
        initComponents();
        totalBytes = 0;
        connected = false;
    }

    public void initComponents() {
        cvWait = new CanvasWait(this);
        lsServers = new ListServers(this);
        lsCommands = new Menu(this);
        imCapture = new ImageCapture(this);
        cvScreen = new CanvasScreen(this);
        cvImage = new CanvasImage(this);
        tbString = new TextString(this);
        displayManager = new DisplayManager(display, lsServers);
    }

    public synchronized void connect(final Server server) {
        cvWait.setMessage(messages.getMessage("connecting"));
        display.setCurrent(cvWait);
        cancelled = false;
        class ConnectThread extends Thread {

            public void run() {
                try {
                    PlugIns.removeAll();
                    String protocol = "socket";
                    if (server.getType() == Server.TYPE_BTSPP) {
                        protocol = "btspp";
                    } else if (server.getType() == Server.TYPE_SSL) protocol = "ssl";
                    StringBuffer sb = new StringBuffer(protocol);
                    sb.append("://");
                    sb.append(server.getAddress());
                    sb.append(":");
                    sb.append(server.getPort());
                    connected = false;
                    connection = (StreamConnection) Connector.open(sb.toString(), Connector.READ_WRITE, true);
                    if (cancelled) {
                        closeConnection();
                        cancelled = false;
                        return;
                    }
                    in = connection.openInputStream();
                    out = connection.openOutputStream();
                    dataIn = new DataInputStream(in);
                    dataOut = new DataOutputStream(out);
                    if (cancelled) {
                        closeConnection();
                        cancelled = false;
                        return;
                    }
                    cvWait.setMessage(messages.getMessage("logging"));
                    dataOut.writeUTF(server.getPassword());
                    dataOut.flush();
                    boolean authenticated = dataIn.readBoolean();
                    if (!authenticated) {
                        throw new Exception(messages.getMessage("invalidPassword"));
                    }
                    cvWait.setMessage(messages.getMessage("sendingData"));
                    ConfigRepository repCfg = new ConfigRepository();
                    Config cfg;
                    if (repCfg != null) cfg = repCfg.load(); else cfg = new Config();
                    alertOk = cfg.isAlertOk();
                    String platform = System.getProperty("microedition.platform");
                    if (platform == null || platform.length() == 0) platform = "J2ME-JM2PC";
                    if (cancelled) {
                        closeConnection();
                        cancelled = false;
                        return;
                    }
                    dataOut.writeInt(cvScreen.getWidth());
                    dataOut.writeInt(cvScreen.getHeight());
                    dataOut.writeUTF(platform);
                    dataOut.writeBoolean(cfg.isJpegImages());
                    dataOut.writeBoolean(cfg.isReceivePlugins());
                    dataOut.writeInt(cfg.getMaxBytesDownload());
                    dataOut.writeInt(cfg.getBitsColor());
                    dataOut.flush();
                    if (cfg.isReceivePlugins()) {
                        cvWait.setMessage(messages.getMessage("plugin"));
                        receivePlugins(dataIn);
                    }
                    cvWait.setMessage(messages.getMessage("serverInfo"));
                    serverScreenWidth = dataIn.readInt();
                    serverScreenHeight = dataIn.readInt();
                    totalBytes += 50;
                    if (cancelled) {
                        closeConnection();
                        cancelled = false;
                        return;
                    }
                    lsCommands.getPluginForm().updatePlugins();
                    cvScreen.getControlOption().updatePlugins();
                    connected = true;
                    display.setCurrent(lsCommands);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (!cancelled) {
                        StringBuffer sb = new StringBuffer(messages.getMessage("errorConnect"));
                        sb.append(' ');
                        sb.append(e.getMessage());
                        showAlert(sb.toString(), AlertType.ERROR, true, lsServers);
                    } else {
                        AlertType.ERROR.playSound(display);
                    }
                    closeConnection();
                } finally {
                    try {
                        System.gc();
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
        Thread t = new ConnectThread();
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }

    public void receivePlugins(DataInputStream dataIn) throws IOException {
        try {
            int qtd = dataIn.readInt();
            totalBytes += 4;
            for (int i = 0; i < qtd; i++) {
                String pluginName = dataIn.readUTF();
                totalBytes += pluginName.length();
                PlugIns.addPlugin(i, pluginName);
            }
        } catch (IOException ioe) {
            throw new IOException(messages.getMessage("errorGetPlugins"));
        }
    }

    public void startApp() {
        Displayable nextDisplayable;
        try {
            if (connected) {
                nextDisplayable = lsCommands;
                display.setCurrent(new CanvasAbout(this, nextDisplayable));
            } else {
                nextDisplayable = lsServers;
                display.setCurrent(new CanvasAbout(this, nextDisplayable));
            }
            Thread.sleep(2100);
            display.setCurrent(nextDisplayable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        if (!unconditional) {
            disconnect();
        }
        notifyDestroyed();
    }

    public void sendData(byte[] data) throws IOException {
        for (int i = 0; i < data.length; i++) out.write(data[i]);
        out.flush();
        totalBytes += data.length;
    }

    public void disconnect() {
        connected = false;
        displayManager.home();
        try {
            System.gc();
            Thread.sleep(200);
        } catch (InterruptedException e) {
        }
        class DisconnectRun implements Runnable {

            public void run() {
                try {
                    sendData("SAIR\n".getBytes());
                } catch (IOException ioe) {
                } finally {
                    closeConnection();
                }
            }
        }
        Thread t = new Thread(new DisconnectRun());
        t.start();
    }

    private boolean hasResponse(String command) {
        int j = command.indexOf(" ");
        String c;
        if (j == -1) c = command; else c = command.substring(0, j);
        if (c.equals(Constants.CMD_TELA) || c.equals(Constants.CMD_FS) || PlugIns.containsCommand(c)) return true;
        if (c.equals(Constants.CMD_PROCESSO)) {
            int i = command.indexOf(" ");
            String type = command.substring(i + 1, command.length() - 1);
            i = type.indexOf(" ");
            if (i != -1) type = type.substring(0, i);
            if (type.equals(Constants.CMD_PROCESSO_TP_CONSOLE)) return true;
        }
        return false;
    }

    public void executeCommand(final String command) {
        lastCommand = command;
        final boolean hasResponse = hasResponse(command);
        class RequestRun implements Runnable {

            public void run() {
                try {
                    sendData(command.getBytes());
                    if (alertOk) {
                        if (display.getCurrent() instanceof Canvas) AlertType.INFO.playSound(display); else {
                            showAlert(messages.getMessage("successCmdMessage"), AlertType.INFO, false, null);
                        }
                    }
                } catch (IOException e) {
                    StringBuffer sb = new StringBuffer(messages.getMessage("error"));
                    sb.append(": [");
                    sb.append(e.getMessage());
                    sb.append("] ");
                    sb.append(messages.getMessage("connectAgain"));
                    showAlert(sb.toString(), AlertType.ERROR, true, lsCommands);
                }
            }
        }
        class ResponseRun implements Runnable {

            public void run() {
                int i = command.indexOf(" ");
                String type = command.substring(i + 1, command.length() - 1);
                i = type.indexOf(" ");
                if (i != -1) type = type.substring(0, i);
                if (type.equals(Constants.CMD_TELA_TP_INT)) {
                    imCapture.createImageCapture(ListScreen.SCREEN_INT, true);
                } else if (type.equals(Constants.CMD_TELA_TP_XY)) {
                    imCapture.createImageCapture(ListScreen.SCREEN_XY, true);
                } else if (type.equals(Constants.CMD_PROCESSO_TP_CONSOLE)) {
                    FormShell fmShell = (FormShell) lsCommands.getFormShell();
                    try {
                        fmShell.update();
                    } catch (IOException e) {
                        showAlert(messages.getMessage("errorResponse"), AlertType.ERROR, true, lsCommands);
                    }
                } else if (type.equals(Constants.CMD_FS_TP_LIST)) {
                    cvWait.setMessage(messages.getMessage("fileSystem"));
                    displayManager.pushDisplayable(cvWait);
                    ListFiles lsFiles = (ListFiles) lsCommands.getListArquivos();
                    try {
                        lsFiles.update();
                        displayManager.popDisplayable();
                    } catch (IOException e) {
                        showAlert(messages.getMessage("errorResponse"), AlertType.ERROR, true, lsCommands);
                    }
                } else {
                    try {
                        cvWait.setMessage(messages.getMessage("plugin"));
                        if (display.getCurrent() instanceof CanvasScreen || display.getCurrent() instanceof CanvasPlugins) {
                            displayManager.pushDisplayable(cvWait, true);
                        } else displayManager.pushDisplayable(cvWait);
                        int tRet = dataIn.readInt();
                        totalBytes += 4;
                        if (tRet == Constants.Plugin_TP_IMAGE) {
                            imCapture.createImageCapture(ListScreen.SCREEN_PLUGIN, false);
                        } else if (tRet == Constants.Plugin_TP_TEXTO) {
                            tbString.updateText();
                            displayManager.pushDisplayable(tbString);
                        } else if (tRet == Constants.Plugin_TP_VOID) {
                            displayManager.popDisplayable();
                        }
                    } catch (IOException e) {
                        showAlert(messages.getMessage("errorResponse"), AlertType.ERROR, true, lsCommands);
                    }
                }
            }
        }
        Thread reqThread = new Thread(new RequestRun());
        reqThread.setPriority(Thread.NORM_PRIORITY);
        reqThread.start();
        if (hasResponse) {
            Thread resThread = new Thread(new ResponseRun());
            resThread.setPriority(Thread.NORM_PRIORITY);
            resThread.start();
        }
    }

    public void sendCommand(final String command, final String param) {
        StringBuffer sb = new StringBuffer(command);
        sb.append(' ');
        sb.append(param);
        sb.append('\n');
        executeCommand(sb.toString());
    }

    public DisplayManager getDisplayManager() {
        return displayManager;
    }

    public Zoom getImageZoom() {
        return zoomImg;
    }

    public void setImageZoom(int zoom) {
        zoomImg.setZoomImg(zoom);
    }

    public void setScreenXY(int x, int y) {
        screenX = x;
        screenY = y;
        int width = cvScreen.getWidth();
        int height = cvScreen.getHeight();
        int zoom = zoomImg.getZoomImg();
        int coverageX = x + (width * zoom);
        int coverageY = y + (height * zoom);
        if (x < 0) screenX = 0; else if (coverageX > serverScreenWidth) {
            screenX = serverScreenWidth - (width * zoom);
        }
        if (y < 0) screenY = 0; else if (coverageY > serverScreenHeight) {
            screenY = serverScreenHeight - (height * zoom);
        }
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    public void showScreen(Image im, int type) {
        if (im != null) {
            cvScreen.setImage(im);
            cvScreen.setType(type);
            display.setCurrent(cvScreen);
        } else {
            showAlert(messages.getMessage("errorDownload"), null, true, lsCommands);
        }
    }

    public void showImage(Image im) {
        if (im != null) {
            cvImage.updateImage(im);
            displayManager.pushDisplayable(cvImage);
        } else {
            showAlert(messages.getMessage("errorDownload"), null, true, lsCommands);
        }
    }

    public void showAlert(String msg, AlertType type, boolean modal, Displayable displayable) {
        displayManager.showAlert(msg, type, modal, displayable);
    }

    public void showTransferredBytes() {
        StringBuffer sb = new StringBuffer(messages.getMessage("transferredBytes"));
        sb.append(' ');
        sb.append(totalBytes);
        showAlert(sb.toString(), AlertType.INFO, true, null);
    }

    public int getServerScreenHeight() {
        return serverScreenHeight;
    }

    public int getServerScreenWidth() {
        return serverScreenWidth;
    }

    public long getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(long totalBytes) {
        this.totalBytes = totalBytes;
    }

    public void closeConnection() {
        cancelled = true;
        connected = false;
        try {
            if (in != null) in.close();
            if (out != null) out.close();
        } catch (IOException e) {
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (IOException e) {
            }
        }
    }

    public CanvasWait getCanvasWait() {
        return cvWait;
    }

    public void changeLanguage(String locale) {
        messages.loadMessages(locale);
        LanguageRepository repLanguage = new LanguageRepository();
        try {
            repLanguage.salvarLanguage(locale);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initComponents();
        System.gc();
        displayManager.home();
    }

    public InputStream getIn() {
        return in;
    }

    public OutputStream getOut() {
        return out;
    }

    public Displayable getMenu() {
        return lsCommands;
    }

    public String getLastCommand() {
        return lastCommand;
    }
}
