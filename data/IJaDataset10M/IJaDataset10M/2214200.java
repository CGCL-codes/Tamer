package ca.ucalgary.cpsc.agilePlanner.test.planner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.junit.Assert;

public class ResetFiles {

    public ResetFiles() {
        super();
    }

    /***************************************************************************
     * SETUP 
     * @throws IOException *
     **************************************************************************/
    protected static void resetProjectFiles() throws IOException {
        copyFile("OriginalProjectFile.xml", "ProjectFile.xml");
        copyFile("OriginalProjectWithBacklogIterationAndStoryCards.xml", "ProjectWithBacklogIterationAndStoryCards.xml");
        copyFile("OriginalEmptyProject.xml", "EmptyProject.xml");
    }

    private static void copyFile(String filenameToCopy, String targetFileName) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        File testdir = new File("." + File.separator + "TestDirectory");
        testdir.mkdir();
        try {
            in = new FileInputStream("." + File.separator + "OriginalTestProjects" + File.separator + filenameToCopy);
            out = new FileOutputStream(testdir.getAbsolutePath() + File.separator + targetFileName);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } finally {
            try {
                if (out != null) out.close();
                if (in != null) in.close();
            } catch (IOException e) {
                e.printStackTrace();
                Assert.fail(e.getMessage());
            }
        }
    }
}
