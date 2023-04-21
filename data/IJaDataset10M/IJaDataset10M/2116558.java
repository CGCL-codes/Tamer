package ClientServerInreraction;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Класс для загрузки файлов с указанного сервера.
 * @author Иван
 */
public class Download {

    private URL url;

    public String strUrl;

    private int size;

    private int downloaded;

    private final int MAX_SIZE_OF_BUFFER = 1024;

    public boolean herrr;

    public Download() {
    }

    public Download(Boolean b) {
        herrr = b;
    }

    public Download(String strUrl) {
        this.strUrl = strUrl;
        size = -1;
    }

    public int getSize() {
        return size;
    }

    public String getUrl() {
        return url.toString();
    }

    public int getProgress() {
        return 1;
    }

    public String getNameOfFile() {
        String fileName = url.getFile();
        int pos = fileName.lastIndexOf('/') + 1;
        return fileName.substring(pos);
    }

    public void run() throws IOException {
        BufferedOutputStream outputFile = null;
        BufferedInputStream inputStream = null;
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        int connectLength = connection.getContentLength();
        if (size == -1) {
            size = connectLength;
        }
        outputFile = new BufferedOutputStream(new FileOutputStream(getNameOfFile()));
        inputStream = (BufferedInputStream) connection.getInputStream();
        byte[] bufer = new byte[MAX_SIZE_OF_BUFFER];
        while (inputStream.read(bufer) != -1) {
            outputFile.write(bufer);
        }
        if (outputFile != null) {
            outputFile.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }
}
