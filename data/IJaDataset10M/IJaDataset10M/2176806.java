package channel.master;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 *
 * @author Michael Hanns
 *
 */
public class UpdateChannelList {

    public static boolean ipUpdated = false;

    public static boolean update(String user, String pass, String channelString, String globalIP) {
        FTPClient ftp = new FTPClient();
        int reply;
        try {
            ftp.connect("witna.co.uk", 21);
            ftp.login(user, pass);
            reply = ftp.getReplyCode();
            if (FTPReply.isPositiveCompletion(reply)) {
                updateChannelList(ftp, channelString);
                if (!ipUpdated) {
                    ipUpdated = updateMasterChannelIP(ftp, globalIP);
                }
                ftp.disconnect();
                return true;
            } else {
                ftp.disconnect();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private static void updateChannelList(FTPClient ftp, String chanDets) {
        String filename = "channelList.html";
        File chanList = new File(filename);
        FileOutputStream out;
        PrintStream p;
        try {
            out = new FileOutputStream(chanList);
            p = new PrintStream(out);
            p.println(chanDets);
            p.close();
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory("/chanData");
            ftp.setFileType(FTPClient.ASCII_FILE_TYPE);
            InputStream fis = new FileInputStream(chanList);
            OutputStream os = ftp.storeFileStream("channelList.html");
            byte buf[] = new byte[8192];
            int bytesRead = fis.read(buf);
            while (bytesRead != -1) {
                os.write(buf, 0, bytesRead);
                bytesRead = fis.read(buf);
            }
            fis.close();
            os.close();
            ftp.completePendingCommand();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean updateMasterChannelIP(FTPClient ftp, String chanDets) {
        String filename = "officialIP.html";
        File chanList = new File(filename);
        FileOutputStream out;
        PrintStream p;
        try {
            out = new FileOutputStream(chanList);
            p = new PrintStream(out);
            p.println(chanDets);
            p.close();
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory("/chanData");
            ftp.setFileType(FTPClient.ASCII_FILE_TYPE);
            InputStream fis = new FileInputStream(chanList);
            OutputStream os = ftp.storeFileStream("officialIP.html");
            byte buf[] = new byte[8192];
            int bytesRead = fis.read(buf);
            while (bytesRead != -1) {
                os.write(buf, 0, bytesRead);
                bytesRead = fis.read(buf);
            }
            fis.close();
            os.close();
            ftp.completePendingCommand();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
