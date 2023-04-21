package ca.etsmtl.ihe.xdsitest.test;

import java.io.File;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import ca.etsmtl.ihe.xdsitest.util.Utilities;
import ca.etsmtl.ihe.xdsitest.util.FailedException;

public class RetrieveDcmWado {

    public static File retrieve(URL url, String studyUid, String seriesUid, String instanceUid, File outputFile) throws FailedException {
        URL requestUrl;
        try {
            requestUrl = new URL(url.toString() + "?requestType=WADO&contentType=application/dicom" + "&studyUID=" + studyUid + "&seriesUID=" + seriesUid + "&objectUID=" + instanceUid);
        } catch (MalformedURLException e) {
            throw new FailedException("WADO request to '" + url + "'", e);
        }
        try {
            HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setUseCaches(false);
            connection.setRequestProperty("accept", "application/dicom");
            connection.connect();
            try {
                int statusCode = connection.getResponseCode();
                switch(statusCode) {
                    case HttpURLConnection.HTTP_OK:
                        {
                            if (!"application/dicom".equals(connection.getContentType())) {
                                throw new FailedException("WADO request to '" + url + "'", "response content-type: " + connection.getContentType());
                            }
                            OutputStream ostream = new BufferedOutputStream(new FileOutputStream(outputFile));
                            try {
                                Utilities.copyStream(connection.getInputStream(), ostream);
                            } finally {
                                ostream.close();
                            }
                            return outputFile;
                        }
                    case HttpURLConnection.HTTP_NOT_FOUND:
                        {
                            throw new FailedException("Error 400 NOT FOUND.");
                        }
                    default:
                        throw new FailedException("WADO request to '" + url + "'", "GET response status: " + statusCode);
                }
            } finally {
                connection.disconnect();
            }
        } catch (IOException e) {
            throw new FailedException("WADO request to '" + url + "'", e);
        }
    }

    public static void main(String[] args) throws Exception {
        File outputFile = RetrieveDcmWado.retrieve(new URL(args[0]), args[1], args[2], args[3], new File(args[4]));
        System.out.println(outputFile);
    }
}
