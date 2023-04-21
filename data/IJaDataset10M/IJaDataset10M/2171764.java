package net.sf.jftp.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Vector;
import net.sf.jftp.system.StringUtils;
import net.sf.jftp.system.logging.Log;

public class HttpTransfer extends Transfer implements Runnable {

    private String url;

    private String localPath;

    private String file;

    public boolean work = true;

    public boolean pause = false;

    public Thread runner;

    private Vector listeners;

    private int stat = 1;

    private ConnectionHandler handler = new ConnectionHandler();

    public HttpTransfer(String url, String localPath, Vector listeners, ConnectionHandler handler) {
        this.url = url;
        this.localPath = localPath;
        this.listeners = listeners;
        this.handler = handler;
        file = StringUtils.getFile(url);
        prepare();
    }

    public void prepare() {
        runner = new Thread(this);
        runner.setPriority(Thread.MIN_PRIORITY);
        runner.start();
    }

    public void run() {
        try {
            if (handler.getConnections().get(file) == null) {
                Log.out("download started: " + url);
                Log.out("connection handler present: " + handler + ", poll size: " + handler.getConnections().size());
                Log.out("local file: " + localPath + file);
                handler.addConnection(file, this);
            } else {
                Log.debug("Transfer already in progress: " + file);
                work = false;
                stat = 2;
                return;
            }
            URL u = new URL(url);
            BufferedOutputStream f = new BufferedOutputStream(new FileOutputStream(localPath + file));
            BufferedInputStream in = new BufferedInputStream(u.openStream());
            byte[] buf = new byte[4096];
            int len = 0;
            while ((stat > 0) && work) {
                stat = in.read(buf);
                if (stat == -1) {
                    break;
                }
                f.write(buf, 0, stat);
                len += stat;
                fireProgressUpdate(file, DataConnection.GET, len);
            }
            f.flush();
            f.close();
            in.close();
            fireProgressUpdate(file, DataConnection.FINISHED, len);
        } catch (Exception ex) {
            work = false;
            Log.debug("Download failed: " + ex.toString());
            File f = new File(localPath + file);
            f.delete();
            fireProgressUpdate(file, DataConnection.FAILED, -1);
            ex.printStackTrace();
            return;
        }
        if (!work) {
            File f = new File(localPath + file);
            f.delete();
            Log.out("download aborted: " + file);
        }
    }

    public void fireProgressUpdate(String file, String type, int bytes) {
        if (listeners == null) {
            return;
        }
        for (int i = 0; i < listeners.size(); i++) {
            ((ConnectionListener) listeners.elementAt(i)).updateProgress(file, type, bytes);
        }
    }

    public int getStatus() {
        return stat;
    }

    public boolean hasStarted() {
        return true;
    }

    public FtpConnection getFtpConnection() {
        return null;
    }

    public DataConnection getDataConnection() {
        return null;
    }
}
