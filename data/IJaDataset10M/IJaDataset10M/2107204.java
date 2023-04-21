package fi.cleancode.jfileoperator.fileoperator;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class BasicFileOperator implements FileOperator {

    private File fileToOperate;

    public BasicFileOperator(File file) {
        fileToOperate = file;
    }

    public void copyToFolder(File folder) throws IOException {
        FileUtils.copyFileToDirectory(fileToOperate, folder);
    }

    public void moveToFolder(File folder) {
        String toFileName = folder.getAbsolutePath() + File.separator + fileToOperate.getName();
        File toFile = new File(toFileName);
        fileToOperate.renameTo(toFile);
    }
}
