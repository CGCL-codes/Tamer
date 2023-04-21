package agentgui.simulationService.distribution;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class enables the download from a given URL to a local destination folder.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class Download {

    private String srcFileURL;

    private String destFileLocale;

    private Integer downloadProgress;

    private boolean downloadSuccessful = false;

    private boolean downloadFinished = false;

    /**
	 * Instantiates a new download.
	 *
	 * @param sourceFileURL the URL of the source file 
	 * @param destinationFileLocal the local destination file 
	 */
    public Download(String sourceFileURL, String destinationFileLocal) {
        this.srcFileURL = sourceFileURL;
        this.destFileLocale = destinationFileLocal;
        this.downloadSuccessful = this.downloadFile();
        this.downloadFinished = true;
    }

    /**
	 * Will download the configured file.
	 *
	 * @return true, if successful
	 */
    private boolean downloadFile() {
        URL url;
        HttpURLConnection huc;
        byte[] buffer = new byte[4096];
        int totBytes, bytes, sumBytes = 0;
        try {
            url = new URL(srcFileURL);
            huc = (HttpURLConnection) url.openConnection();
            huc.connect();
            InputStream is = huc.getInputStream();
            int code = huc.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                File output = new File(destFileLocale);
                FileOutputStream outputStream = new FileOutputStream(output);
                totBytes = huc.getContentLength();
                while ((bytes = is.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, bytes);
                    sumBytes += bytes;
                    downloadProgress = (Math.round((((float) sumBytes / (float) totBytes) * 100)));
                }
                huc.disconnect();
                return true;
            }
            huc.disconnect();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
	 * Informs about a finished download.
	 * @return true, if the download is finished
	 */
    public boolean isFinished() {
        return this.downloadFinished;
    }

    /**
	 * Informs if the download was successful.
	 * @return true, if the download was successful 
	 */
    public boolean wasSuccessful() {
        return this.downloadSuccessful;
    }

    /**
	 * Informs about the current download progress.
	 * @return the downloadProgress
	 */
    public Integer getDownloadProgress() {
        return downloadProgress;
    }
}
