package org.opencms.setup.comptest;

import org.opencms.setup.CmsSetupBean;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;

/**
 * Tests the permission on the target installation folders.<p>
 * 
 * @author Michael Moossen
 * 
 * @version $Revision: 1.2 $ 
 * 
 * @since 6.1.8 
 */
public class CmsSetupTestFolderPermissions implements I_CmsSetupTest {

    /** The test name. */
    public static final String TEST_NAME = "Folder Permissions";

    /**
     * @see org.opencms.setup.comptest.I_CmsSetupTest#getName()
     */
    public String getName() {
        return TEST_NAME;
    }

    /**
     * @see org.opencms.setup.comptest.I_CmsSetupTest#execute(org.opencms.setup.CmsSetupBean)
     */
    public CmsSetupTestResult execute(CmsSetupBean setupBean) {
        CmsSetupTestResult testResult = new CmsSetupTestResult(this);
        String basePath = setupBean.getWebAppRfsPath();
        if (!basePath.endsWith(File.separator)) {
            basePath += File.separator;
        }
        File file1;
        Random rnd = new Random();
        do {
            file1 = new File(basePath + "test" + rnd.nextInt(1000));
        } while (file1.exists());
        boolean success = false;
        try {
            file1.createNewFile();
            FileWriter fw = new FileWriter(file1);
            fw.write("aA1");
            fw.close();
            success = true;
            FileReader fr = new FileReader(file1);
            success = success && (fr.read() == 'a');
            success = success && (fr.read() == 'A');
            success = success && (fr.read() == '1');
            success = success && (fr.read() == -1);
            fr.close();
            success = file1.delete();
            success = !file1.exists();
        } catch (Exception e) {
            success = false;
        }
        if (!success) {
            testResult.setRed();
            testResult.setInfo("OpenCms cannot be installed without read and write privileges for path " + basePath + "! Please check you are running your servlet container with the right user and privileges.");
            testResult.setHelp("Not enough permissions to create/read/write a file");
            testResult.setResult(RESULT_FAILED);
        } else {
            testResult.setGreen();
            testResult.setResult(RESULT_PASSED);
        }
        return testResult;
    }
}
