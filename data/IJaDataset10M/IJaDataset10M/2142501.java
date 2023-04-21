package forteresce.portprofile.profiles.util;

import java.io.File;
import forteresce.portprofile.config.enums.SystemPropertiesEnum;
import junit.framework.TestCase;

public class ZipUtilTest extends TestCase {

    public void test_zip_withafolder() throws Exception {
        File tempFolder = new File(SystemPropertiesEnum.JAVA_IO_TMPDIR.get() + SystemPropertiesEnum.FILE_SEPARATOR.get() + "folderToBeZipped");
        tempFolder.mkdir();
        File tempFile = new File(tempFolder, "tempFile");
        tempFile.createNewFile();
        boolean result = ZipUtil.zip(tempFolder.getAbsolutePath(), "test_folder.zip");
        assertTrue(result);
        File zipFile = new File(SystemPropertiesEnum.JAVA_IO_TMPDIR.get() + SystemPropertiesEnum.FILE_SEPARATOR.get() + "test_folder.zip");
        assertTrue(zipFile.exists());
        tempFolder.deleteOnExit();
        tempFile.deleteOnExit();
        zipFile.deleteOnExit();
    }

    public void test_zip_withafile() throws Exception {
        File tempFile = File.createTempFile("test", "tmp");
        boolean result = ZipUtil.zip(tempFile.getAbsolutePath(), "test_file.zip");
        assertTrue(result);
        File zipFile = new File(SystemPropertiesEnum.JAVA_IO_TMPDIR.get() + SystemPropertiesEnum.FILE_SEPARATOR.get() + "test_file.zip");
        assertTrue(zipFile.exists());
        tempFile.deleteOnExit();
        zipFile.deleteOnExit();
    }

    public void test_unzip_withafile() throws Exception {
        File tempFile = File.createTempFile("test", "tmp");
        ZipUtil.zip(tempFile.getAbsolutePath(), "test_file.zip");
        boolean result = ZipUtil.unzip(SystemPropertiesEnum.JAVA_IO_TMPDIR.get() + SystemPropertiesEnum.FILE_SEPARATOR.get() + "test_file.zip", SystemPropertiesEnum.USER_HOME.get());
        assertTrue(result);
        File file = new File(SystemPropertiesEnum.USER_HOME.get() + SystemPropertiesEnum.FILE_SEPARATOR.get() + tempFile.getName());
        assertTrue(file.exists());
        tempFile.deleteOnExit();
        file.deleteOnExit();
    }

    public void test_unzip_withafolder() throws Exception {
        File tempFolder = new File(SystemPropertiesEnum.JAVA_IO_TMPDIR.get() + SystemPropertiesEnum.FILE_SEPARATOR.get() + "folderToBeZipped");
        tempFolder.mkdir();
        File tempFile = new File(tempFolder, "tempFile");
        tempFile.createNewFile();
        ZipUtil.zip(tempFolder.getAbsolutePath(), "test_folder.zip");
        boolean result = ZipUtil.unzip(SystemPropertiesEnum.JAVA_IO_TMPDIR.get() + SystemPropertiesEnum.FILE_SEPARATOR.get() + "test_folder.zip", SystemPropertiesEnum.USER_HOME.get());
        assertTrue(result);
        File folder = new File(SystemPropertiesEnum.USER_HOME.get() + SystemPropertiesEnum.FILE_SEPARATOR.get() + "folderToBeZipped");
        assertTrue(folder.exists());
        tempFolder.deleteOnExit();
        tempFile.deleteOnExit();
        folder.deleteOnExit();
        folder.listFiles()[0].deleteOnExit();
    }
}
