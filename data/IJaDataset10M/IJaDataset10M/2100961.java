package jazzlib;

import huf.io.FileUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class Main extends Task {

    private String srcDir = null;

    public void setSrcdir(String srcDir) {
        this.srcDir = srcDir;
    }

    private String dstFile = null;

    public void setDstfile(String dstFile) {
        this.dstFile = dstFile;
    }

    public void execute() {
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(dstFile));
            out.setLevel(9);
            for (File f : new File(srcDir).listFiles()) {
                out.putNextEntry(new ZipEntry(f.getName()));
                FileUtils.copy(f, out);
                out.closeEntry();
            }
            out.close();
        } catch (IOException ioe) {
            throw new BuildException(ioe);
        }
    }
}
