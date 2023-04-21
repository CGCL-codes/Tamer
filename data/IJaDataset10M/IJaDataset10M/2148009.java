package scribd;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UrlDownload {

    static final int size = 1024;

    public static void fileUrl(String fAddress, String localFileName, String destinationDir) {
        OutputStream outStream = null;
        URLConnection uCon = null;
        InputStream is = null;
        try {
            URL Url;
            byte[] buf;
            int ByteRead, ByteWritten = 0;
            Url = new URL(fAddress);
            outStream = new BufferedOutputStream(new FileOutputStream(destinationDir + "\\" + localFileName));
            uCon = Url.openConnection();
            is = uCon.getInputStream();
            buf = new byte[size];
            while ((ByteRead = is.read(buf)) != -1) {
                outStream.write(buf, 0, ByteRead);
                ByteWritten += ByteRead;
            }
            System.out.println("Downloaded Successfully.");
            System.out.println("File name:\"" + localFileName + "\"\nNo ofbytes :" + ByteWritten);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void fileDownload(String fAddress, String destinationDir) {
        int slashIndex = fAddress.lastIndexOf('/');
        int periodIndex = fAddress.lastIndexOf('.');
        String fileName = fAddress.substring(slashIndex + 1);
        URL url;
        try {
            url = new URL(fAddress);
            URLConnection uc = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            File file = new File(destinationDir + "/download.pdf");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            int inputLine;
            while ((inputLine = in.read()) != -1) out.write(inputLine);
            in.close();
        } catch (Exception ex) {
            Logger.getLogger(UrlDownload.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        if (args.length == 2) {
            for (int i = 1; i < args.length; i++) {
                fileDownload(args[i], args[0]);
            }
        } else {
        }
    }
}
