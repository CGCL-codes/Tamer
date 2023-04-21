package PolyJen;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import com.beowurks.BeoCommon.Util;

public class ZipExtraction {

    private static final int DATA_BUFFER = 2048;

    protected final SimpleDateFormat foRegularDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

    private final String fcStartDirectory;

    private ZipFile foZipFile = null;

    private final String fcArchiveName;

    public ZipExtraction(final String tcArchiveName, final String tcStartDirectory) {
        this.fcArchiveName = tcArchiveName;
        this.fcStartDirectory = Util.replaceAll(Util.includeTrailingBackslash(tcStartDirectory), "\\", "/").toString();
    }

    public void extractFiles() throws Exception {
        if (!ZipExtraction.makeDirectory(this.fcStartDirectory)) {
            throw (new Exception("Unable to make the directory " + this.fcStartDirectory + "."));
        }
        Exception loError = null;
        try {
            this.foZipFile = new ZipFile(this.fcArchiveName);
            this.extractAll();
        } catch (final Exception loErr1) {
            loError = loErr1;
        } finally {
            if (this.foZipFile != null) {
                this.foZipFile.close();
            }
        }
        if (loError != null) {
            throw (loError);
        }
    }

    private void extractAll() throws Exception {
        Exception loError = null;
        BufferedOutputStream loDest = null;
        BufferedInputStream loInput = null;
        FileOutputStream loFileOutput = null;
        ZipEntry loEntry;
        final byte laData[] = new byte[ZipExtraction.DATA_BUFFER];
        final Enumeration<? extends ZipEntry> loEnum = this.foZipFile.entries();
        final int lnCount = this.foZipFile.size();
        for (int i = 0; i < lnCount; ++i) {
            loEntry = loEnum.nextElement();
            if (loEntry.isDirectory()) {
                continue;
            }
            try {
                loDest = null;
                loInput = null;
                loFileOutput = null;
                final String lcOutputFullName = this.fcStartDirectory + Util.extractFileName(loEntry.getName(), "/");
                loInput = new BufferedInputStream(this.foZipFile.getInputStream(loEntry));
                loFileOutput = new FileOutputStream(lcOutputFullName);
                loDest = new BufferedOutputStream(loFileOutput, ZipExtraction.DATA_BUFFER);
                double lnTotalRead = 0;
                int lnBytesRead;
                lnBytesRead = loInput.read(laData, 0, ZipExtraction.DATA_BUFFER);
                while (lnBytesRead != -1) {
                    loDest.write(laData, 0, lnBytesRead);
                    lnTotalRead += lnBytesRead;
                    lnBytesRead = loInput.read(laData, 0, ZipExtraction.DATA_BUFFER);
                }
                loDest.flush();
                loDest.close();
                loDest = null;
                loInput.close();
                loInput = null;
                final File loFile = new File(lcOutputFullName);
                try {
                    loFile.setLastModified(loEntry.getTime());
                } catch (final IllegalArgumentException loErr) {
                } catch (final SecurityException loErr) {
                }
                if (loFile.length() != loEntry.getSize()) {
                    throw (new Exception("The file size for " + loFile.getPath() + " does not match the file size of " + loEntry.getName() + " found in the archive file of " + this.fcArchiveName + "."));
                }
            } catch (final Exception loErr1) {
                loError = loErr1;
            } finally {
                if (loDest != null) {
                    loDest.flush();
                    loDest.close();
                }
                if (loInput != null) {
                    loInput.close();
                }
            }
        }
        if (loError != null) {
            throw (loError);
        }
    }

    private static boolean makeDirectory(final String tcDirectory) {
        final File loFile = new File(tcDirectory);
        boolean llOkay = true;
        if (!loFile.exists()) {
            if (!loFile.mkdirs()) {
                llOkay = false;
            }
        }
        return (llOkay);
    }
}
