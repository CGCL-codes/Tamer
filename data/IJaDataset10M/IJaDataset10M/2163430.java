package com.photom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.zip.CRC32;

public class CheckSum {

    public static int BUFFERSIZE = 4 * 1024;

    private File file;

    public CheckSum(String filename) {
        this.file = new File(filename);
    }

    public CheckSum(File file) {
        this.file = file;
    }

    public static void main(String args[]) {
        if (args.length == 2) {
            if (args[0].equals("create")) {
                System.exit(new CheckSum(args[1]).createChkFile());
            } else if (args[0].equals("check")) {
                System.exit(new CheckSum(args[1]).checkChkFile());
            }
        } else {
            System.out.println("Usage : java Checksum create [filename]\n" + "        java Checksum check  [filename]");
        }
    }

    public int createChkFile() {
        try {
            byte[] chk = createChecksum();
            File f = new File(file.getAbsoluteFile() + ".chk");
            OutputStream os = new FileOutputStream(f);
            os.write(chk);
            os.close();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int checkChkFile() {
        int rc = 0;
        try {
            byte[] chk1 = createChecksum();
            byte[] chk2 = new byte[chk1.length];
            File f = new File(file.getAbsoluteFile() + ".chk");
            InputStream is = new FileInputStream(f);
            is.read(chk2);
            if (new String(chk2).equals(new String(chk1))) {
                System.out.println("Same!");
                rc = 1;
            } else {
                System.out.println("Different!");
                rc = 2;
            }
            is.close();
            return rc;
        } catch (Exception e) {
            e.printStackTrace();
            return rc;
        }
    }

    public StringBuilder createChecksumString() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        byte[] bs = createChecksum();
        for (Byte byte1 : bs) {
            stringBuilder.append(byte1.intValue());
        }
        return stringBuilder;
    }

    public Long createChecksumLong() throws IOException {
        Long ret = 0L;
        try {
            InputStream in = new FileInputStream(file);
            CRC32 checksum = new CRC32();
            checksum.reset();
            byte[] buffer = new byte[BUFFERSIZE];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) >= 0) {
                checksum.update(buffer, 0, bytesRead);
            }
            ret = checksum.getValue();
            in.close();
        } catch (IOException e) {
            System.out.println("Couldn't read file or file name (corrupted?) : " + file.getAbsolutePath());
            ret = 0L;
        }
        return ret;
    }

    private byte[] createChecksum() throws Exception {
        InputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance("MD5");
        int numRead;
        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);
        fis.close();
        return complete.digest();
    }
}
