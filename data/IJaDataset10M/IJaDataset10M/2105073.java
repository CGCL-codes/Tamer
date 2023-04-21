package assays.com;

import java.util.List;
import java.util.Enumeration;
import java.util.zip.ZipFile;
import java.io.File;
import java.util.zip.ZipInputStream;
import java.io.FileInputStream;
import java.io.*;
import java.util.HashMap;
import java.util.zip.ZipEntry;

public class ZipProcessor {

    ZipFile zipFile = null;

    HashMap entries = null;

    public ZipProcessor(File myFile) {
        int num = 0;
        ZipInputStream zipinpstr = null;
        entries = new HashMap();
        try {
            zipinpstr = new ZipInputStream(new FileInputStream(myFile));
        } catch (FileNotFoundException ex) {
            myFile = null;
        }
        ZipEntry entry;
        if (zipinpstr != null) try {
            while ((entry = zipinpstr.getNextEntry()) != null) {
                entries.put(entry.getName(), new Integer(++num));
                zipinpstr.closeEntry();
            }
            zipinpstr.close();
        } catch (IOException ex1) {
        }
    }

    public static boolean getFile(File myFile, String inputFile, File outputFile) {
        ZipInputStream zipinpstr = null;
        boolean result = false;
        int l, bufSize = 8192;
        byte buffer[] = new byte[bufSize];
        try {
            zipinpstr = new ZipInputStream(new FileInputStream(myFile));
        } catch (FileNotFoundException ex) {
        }
        ZipEntry entry;
        if (zipinpstr != null) try {
            while ((entry = zipinpstr.getNextEntry()) != null) {
                if (!entry.getName().equals(inputFile)) zipinpstr.closeEntry(); else {
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    l = zipinpstr.read(buffer, 0, bufSize);
                    while (l > 0) {
                        fos.write(buffer, 0, l);
                        l = zipinpstr.read(buffer, 0, bufSize);
                    }
                    fos.close();
                    zipinpstr.closeEntry();
                    result = true;
                    break;
                }
            }
            zipinpstr.close();
        } catch (IOException ex1) {
        }
        return result;
    }

    public boolean isZipOK() {
        return (zipFile == null) ? false : true;
    }

    public Integer existsFile(String fileName) {
        return (Integer) entries.get(fileName);
    }

    public void closeFile() {
        try {
            if (zipFile != null) zipFile.close();
        } catch (Exception ex) {
        }
        ;
    }
}
