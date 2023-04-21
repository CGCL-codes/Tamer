package update;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;

public class DownloadProcess {

    public static final int STEP_FACTOR = 100;

    private String urlString, outFile;

    private ListenerContainer<DownloadListener> downloadListeners;

    private int steps;

    public DownloadProcess(String urlString, String outFile) {
        this.urlString = urlString;
        this.outFile = outFile;
        downloadListeners = new ListenerContainer<DownloadListener>();
    }

    public void addDownloadListener(DownloadListener l) {
        downloadListeners.addListener(l);
    }

    public void removeDownloadListener(DownloadListener l) {
        downloadListeners.removeListener(l);
    }

    public void startDownload() {
        steps = 0;
        try {
            Method m = DownloadListener.class.getMethod("downloadStarted", DownloadProcess.class);
            downloadListeners.callMethod(m, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            URL url = new URL(urlString);
            is = url.openStream();
            fos = new FileOutputStream(outFile);
            int oneChar, counter = 0;
            while ((oneChar = is.read()) != -1) {
                fos.write(oneChar);
                counter++;
                if (counter == STEP_FACTOR) {
                    steps++;
                    counter = 0;
                    try {
                        Method m = DownloadListener.class.getMethod("downloadStatusChanged", DownloadProcess.class);
                        downloadListeners.callMethod(m, this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Method m = DownloadListener.class.getMethod("downloadFinished", DownloadProcess.class);
                downloadListeners.callMethod(m, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getSteps() {
        return steps;
    }
}
