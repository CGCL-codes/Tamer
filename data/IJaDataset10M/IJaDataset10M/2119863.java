package org.pachyderm.foundation;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.log4j.Logger;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSError;

public class PXCompileZipArchive extends PXBuildPhase {

    private static Logger LOG = Logger.getLogger(PXCompileZipArchive.class.getName());

    public PXCompileZipArchive(NSDictionary<String, String> archive) {
        super(archive);
        LOG.info("[CONSTRUCT]");
    }

    public void executeInContext(PXBuildContext context) {
        LOG.info("executeInContext: executing");
        URL bundleURL = context.getBundle().bundleURL();
        if (bundleURL.getProtocol().equals("file")) {
            try {
                String presentationDirectory = bundleURL.toString();
                presentationDirectory = presentationDirectory.substring(5, presentationDirectory.length() - 1);
                File presentationDirectoryFile = new File(presentationDirectory);
                String zipFilename = presentationDirectory + ".zip";
                FileOutputStream dest = new FileOutputStream(zipFilename);
                BufferedOutputStream bos = new BufferedOutputStream(dest);
                ZipOutputStream out = new ZipOutputStream(bos);
                zipDir(presentationDirectoryFile, out, presentationDirectoryFile.getPath(), context);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
                NSDictionary info = new NSDictionary(new Object[] { e.getMessage() }, new String[] { NSError.LocalizedDescriptionKey });
                NSError message = new NSError("Pachyderm Build Domain", -1, info);
                context.appendBuildMessage(message);
            }
        }
    }

    private void zipDir(File zipDir, ZipOutputStream zos, String startingPath, PXBuildContext context) {
        try {
            String[] dirList = zipDir.list();
            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;
            for (int i = 0; i < dirList.length; i++) {
                File f = new File(zipDir, dirList[i]);
                String entryPath = f.getPath();
                if (entryPath.startsWith(startingPath)) {
                    entryPath = entryPath.substring(startingPath.length(), entryPath.length());
                }
                if (f.isDirectory()) {
                    @SuppressWarnings("unused") String filePath = entryPath;
                    zipDir(f, zos, startingPath, context);
                    continue;
                }
                FileInputStream fis = new FileInputStream(f);
                ZipEntry anEntry = new ZipEntry(entryPath);
                zos.putNextEntry(anEntry);
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            NSDictionary info = new NSDictionary(new Object[] { e.getMessage() }, new String[] { NSError.LocalizedDescriptionKey });
            NSError message = new NSError("Pachyderm Build Domain", -1, info);
            context.appendBuildMessage(message);
        }
    }
}
