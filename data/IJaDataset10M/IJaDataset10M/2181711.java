package self.util.zip;

import java.io.*;
import java.util.zip.*;

public class ZipDirectoryInflator {

    private byte[] buff;

    private String startFrom;

    private boolean inclSubs;

    private String outputZipName;

    private ZipOutputStream output;

    private int startFromLen;

    public ZipDirectoryInflator(int sz, String start, boolean inc, String outZipFile) {
        buff = new byte[sz];
        startFrom = start;
        startFromLen = startFrom.length();
        inclSubs = inc;
        outputZipName = outZipFile;
    }

    public void run(EntryConfigurationListener callback) throws IOException {
        FileOutputStream fos = new FileOutputStream(outputZipName);
        output = new ZipOutputStream(fos);
        try {
            File curr = new File(startFrom);
            zipDirectory(curr, callback);
        } finally {
            output.close();
        }
    }

    private void zipDirectory(File curr, EntryConfigurationListener callback) throws IOException {
        File[] files = curr.listFiles();
        for (int cntr = 0; cntr < files.length; cntr++) {
            if (files[cntr].isDirectory()) {
                if (inclSubs) zipDirectory(files[cntr], callback);
            } else addFileToZip(files[cntr], callback);
        }
    }

    private void addFileToZip(File file, EntryConfigurationListener callback) throws IOException {
        String abs = file.getAbsolutePath();
        abs = abs.substring(startFromLen + 1);
        ZipEntry entry = new ZipEntry(abs);
        if (callback != null) callback.configure(entry);
        output.putNextEntry(entry);
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis, 2048);
            try {
                int readIn;
                while ((readIn = bis.read(buff)) > 0) output.write(buff, 0, readIn);
            } finally {
                bis.close();
            }
        } finally {
            output.closeEntry();
        }
    }

    public static void main(String[] args) throws Exception {
        ZipDirectoryInflator test = new ZipDirectoryInflator(512000, "C:/localdata/cAID/gs.v4.testdata/will", true, "C:/localdata/cAID/test.zip");
        test.run(null);
    }
}
