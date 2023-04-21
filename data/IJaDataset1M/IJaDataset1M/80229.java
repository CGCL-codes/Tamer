package net.entelijan.cobean.data.util.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileWriter {

    public void write(String outFileName, IOutputStramUser handler) {
        File outFile = new File(outFileName);
        File outDir = outFile.getParentFile();
        if (!outDir.exists()) {
            boolean ok = outDir.mkdirs();
            if (!ok) {
                throw new IllegalStateException("Could not create '" + outDir + "'");
            }
        }
        try {
            FileOutputStream fout = null;
            try {
                fout = new FileOutputStream(outFile);
                handler.use(fout);
            } finally {
                if (fout != null) {
                    fout.close();
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not write to " + outFile + " because: " + e.getMessage(), e);
        }
    }
}
