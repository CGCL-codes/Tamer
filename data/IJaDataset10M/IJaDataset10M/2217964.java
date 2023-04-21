package v201101;

import com.google.api.adwords.lib.AdWordsServiceLogger;
import com.google.api.adwords.lib.AdWordsUser;
import com.google.api.adwords.lib.AuthToken;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This example gets and downloads a report from a report definition.
 * To get a report definition, run AddKeywordsPerformanceReportDefinition.java.
 * Currently, there is only production support for report download.
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class DownloadReport {

    public static void main(String[] args) {
        try {
            AdWordsServiceLogger.log();
            AdWordsUser user = new AdWordsUser();
            user.setAuthToken(new AuthToken(user.getEmail(), user.getPassword()).getAuthToken());
            String fileName = "INSERT_OUTPUT_FILE_NAME_HERE";
            long reportDefintionId = Long.parseLong("INSERT_REPORT_DEFINITION_ID_HERE");
            String url = "https://adwords.google.com/api/adwords/reportdownload?__rd=" + reportDefintionId;
            HttpURLConnection urlConn = (HttpURLConnection) new URL(url).openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.setRequestProperty("Authorization", "GoogleLogin auth=" + user.getRegisteredAuthToken());
            if (user.getClientCustomerId() != null) {
                urlConn.setRequestProperty("clientCustomerId", user.getClientCustomerId());
            } else if (user.getClientEmail() != null) {
                urlConn.setRequestProperty("clientEmail", user.getClientEmail());
            } else {
                urlConn.setRequestProperty("clientEmail", user.getEmail());
            }
            urlConn.setRequestProperty("returnMoneyInMicros", Boolean.toString(user.isReportsReturnMoneyInMicros()));
            urlConn.connect();
            writeStreamToStream(urlConn.getInputStream(), new FileOutputStream(new File(fileName)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * Writes the contents of the {@link InputStream} inputStream to the
   * {@link OutputStream} outputStream.
   *
   * @param inputStream the {@code InputStream} to read from
   * @param outputStream the {@code OutputStream} to write to
   * @throws IOException if an I/O error occurs
   */
    private static void writeStreamToStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        BufferedOutputStream bos = new BufferedOutputStream(outputStream);
        try {
            int i = 0;
            while ((i = bis.read()) != -1) {
                bos.write(i);
            }
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
    }
}
