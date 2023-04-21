package ti.mcore.u;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import junit.framework.TestCase;
import ti.mcore.Routing;
import ti.mcore.tests.u.DataUtil;

/**
 * 
 * 
 * @author alex.k@ti.com
 */
public class FileUtilTest extends TestCase {

    private static final String CALLER_DIR = DataUtil.getCallerPath(FileUtilTest.class);

    private static final String INPUTS_DIR = CALLER_DIR + File.separator + "inputs";

    private static final Hashtable<String, Boolean> INPUT_FILE_NAMES = new Hashtable<String, Boolean>();

    static {
        INPUT_FILE_NAMES.put("FileIdx-034.txt", true);
        INPUT_FILE_NAMES.put("FileIdx-0x24.txt", true);
        INPUT_FILE_NAMES.put("FileIdx-junk12.txt", true);
        INPUT_FILE_NAMES.put("FileIdx-.txt", true);
        INPUT_FILE_NAMES.put("FileIdx.txt", true);
        INPUT_FILE_NAMES.put("FileIdx-1.txt", true);
        INPUT_FILE_NAMES.put("FileIdx-2.txt", true);
        INPUT_FILE_NAMES.put("FileIdx-13.txt", true);
        INPUT_FILE_NAMES.put("FileIdx-23.txt", true);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(FileUtilTest.class);
    }

    public void testExists() {
    }

    public void testIsValidDirectory() {
    }

    public void testGetValidDir() {
    }

    /**
	 * @see FileUtil#getIndexedFileName(String, String, String, String)
	 */
    public void testGetIndexedFileName() {
        String idxFileStr = FileUtil.getIndexedFileName(INPUTS_DIR, "FileIdx", null, ".txt");
        assertEquals(INPUTS_DIR + File.separator + "FileIdx-14.txt", idxFileStr);
        idxFileStr = FileUtil.getIndexedFileName(CALLER_DIR, "FileUtilTest", null, ".java");
        assertEquals(CALLER_DIR + File.separator + "FileUtilTest-1.java", idxFileStr);
        idxFileStr = FileUtil.getIndexedFileName(CALLER_DIR, "SomeNewFile", null, ".java");
        assertEquals(CALLER_DIR + File.separator + "SomeNewFile.java", idxFileStr);
    }

    /**
	 * @see FileUtil#getIndexedFileName(String, String)
	 */
    public void testGetIndexedFileName_01() {
        String idxFileStr = FileUtil.getIndexedFileName("SomeNewFile.txt", ".txt");
        assertEquals("SomeNewFile.txt", idxFileStr);
        idxFileStr = FileUtil.getIndexedFileName("build.properties", ".properties");
        assertEquals("build.properties", idxFileStr);
        final String baseName = INPUTS_DIR + File.separator + "FileIdx";
        idxFileStr = FileUtil.getIndexedFileName(baseName + ".txt", ".txt");
        assertEquals(baseName + "-14.txt", idxFileStr);
        idxFileStr = FileUtil.getIndexedFileName(CALLER_DIR + File.separator + "FileUtilTest" + ".java", ".java");
        assertEquals(CALLER_DIR + File.separator + "FileUtilTest-1.java", idxFileStr);
        idxFileStr = FileUtil.getIndexedFileName(CALLER_DIR + File.separator + "SomeNewFile" + ".java", ".java");
        assertEquals(CALLER_DIR + File.separator + "SomeNewFile.java", idxFileStr);
    }

    public void testGetFileIdx() {
        tetGetFileIdx(0, "FileIdx-034", ".txt");
        tetGetFileIdx(0, "FileIdx-0x24", ".txt");
        tetGetFileIdx(0, "FileIdx-junk12", ".txt");
        tetGetFileIdx(0, "FileIdx-", ".txt");
        tetGetFileIdx(0, "FileIdx", ".txt");
        tetGetFileIdx(1, "FileIdx-1", ".txt");
        tetGetFileIdx(2, "FileIdx-2", ".txt");
        tetGetFileIdx(13, "FileIdx-13", ".txt");
        tetGetFileIdx(23, "FileIdx-23", ".txt");
    }

    /**
	 * @param testIdx
	 * @param fileName
	 * @param suffix
	 * 
	 * @author alex.k@ti.com
	 */
    private void tetGetFileIdx(int testIdx, String fileName, String suffix) {
        File file = new File(INPUTS_DIR, fileName + suffix);
        int idx = FileUtil.getFileIdx(file, null, suffix);
        assertEquals(testIdx, idx);
    }

    /**
	 * @see FileUtil#getFiles(String, String)
	 * 
	 * @author alex.k@ti.com
	 */
    public void testGetFiles() {
        File[] files = FileUtil.getFiles(INPUTS_DIR, "*.txt");
        for (int i = 0; i < files.length; i++) {
            if (!INPUT_FILE_NAMES.get(files[i].getName())) {
                assertEquals("", "not found file[" + i + "] :\"" + files[i] + "\"");
            }
        }
        files = FileUtil.getFiles(INPUTS_DIR, "*.html");
        assertNull(files);
        files = FileUtil.getFiles(CALLER_DIR, "File.*.java");
        assertEquals(this.getClass().getSimpleName() + ".java", files[0].getName());
        assertEquals(1, files.length);
    }

    /**
	 * @see FileUtil#getCurrentDatFiles()
	 *
	 * @author alex.k@ti.com
	 * @throws IOException 
	 */
    public void testGetCurrentDatFiles() throws IOException {
        String currentEdsFileName = Routing.getRouting().getSeries().getName();
        System.err.println(currentEdsFileName);
        File currEdsFile = new File(currentEdsFileName);
        currEdsFile.deleteOnExit();
        String baseDatFile = StringUtil.replaceSuffix(currEdsFile.getAbsolutePath(), IPlatoFileConsts.EDS_FILE_SUFFIX, IPlatoFileConsts.DAT_FILE_SUFFIX);
        Hashtable<String, Boolean> datFilesControls = new Hashtable<String, Boolean>();
        final int controlCount = 27;
        for (int i = 0; i < controlCount; i++) {
            String controlName = FileUtil.getIndexedFileName(baseDatFile, IPlatoFileConsts.DAT_FILE_SUFFIX);
            File controlFile = new File(controlName);
            controlFile.createNewFile();
            controlFile.deleteOnExit();
            datFilesControls.put(controlName, true);
        }
        File[] files = FileUtil.getCurrentDatFiles(currentEdsFileName);
        assertEquals(controlCount, files.length);
        for (int i = 0; i < files.length; i++) {
            if (!datFilesControls.get(files[i].getAbsolutePath())) {
                assertEquals("file[" + i + "] = " + files[i].getAbsolutePath(), "no such file");
            }
        }
        currEdsFile.delete();
    }
}
