package com.iver.cit.jdwglib;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import junit.framework.TestCase;
import com.hardcode.gdbms.driver.exceptions.InitializeDriverException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.drivers.dwg.DwgMemoryDriver;

/**
 * Multiple test with dwg files of many versions (DWG 12, 13-14, 2000)
 * @author azabala
 *
 */
public class DwgFilesTestCase extends TestCase {

    private File baseDataPath;

    boolean TEST_ALL = true;

    protected void setUp() throws Exception {
        super.setUp();
        URL url = this.getClass().getResource("DwgFileTest_data");
        if (url == null) throw new Exception("Can't find 'DwgFileTest_data' dir");
        baseDataPath = new File(url.getFile());
        if (!baseDataPath.exists()) throw new Exception("Can't find 'DwgFileTest_data' dir");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        baseDataPath = null;
    }

    public void test1() {
        if (TEST_ALL) {
            File[] directories = baseDataPath.listFiles();
            for (int j = 0; j < directories.length; j++) {
                System.out.println("Probando dwg versi�n " + directories[j].getName());
                File[] files = directories[j].listFiles(new FilenameFilter() {

                    public boolean accept(File arg0, String fileName) {
                        if (fileName.endsWith("dwg")) {
                            return true;
                        }
                        return false;
                    }
                });
                for (int i = 0; i < files.length; i++) {
                    try {
                        DwgMemoryDriver driver = new DwgMemoryDriver();
                        driver.open(files[i]);
                        driver.initialize();
                        System.out.println("NumElements=" + driver.getShapeCount());
                        System.out.println("WithRoi=" + (driver.getRois().size() != 0));
                        driver.close();
                    } catch (Exception e) {
                        System.out.println("Error durante la lectura del fichero " + i + "," + files[i].getName());
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }
    }

    public void testLwPline() throws ReadDriverException {
        DwgMemoryDriver driver = new DwgMemoryDriver();
        String fileName = baseDataPath.getAbsolutePath() + "/2000/ORENSE-5.DWG";
        File file = new File(fileName);
        driver.open(file);
        driver.initialize();
        fileName = baseDataPath.getAbsolutePath() + "/14/ORENSE-5.DWG";
        file = new File(fileName);
        driver.open(file);
        driver.initialize();
    }
}
