package org.dcm4chex.archive.hsm.spi;

import org.dcm4chex.archive.ejb.jdbc.FileInfo;
import org.dcm4chex.archive.common.FileStatus;
import org.dcm4chex.archive.hsm.spi.utils.HsmUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import org.dcm4che.util.MD5Utils;
import java.util.*;
import java.io.File;
import java.io.IOException;

/**
 * @author Fuad Ibrahimov
 * @since Dec 1, 2006
 */
public class HsmUtilsTests {

    private static final String FILE2 = "org/dcm4chex/archive/hsm/spi/test2.txt";

    private static final String FILE1 = "org/dcm4chex/archive/hsm/spi/test1.txt";

    private Map<String, String> md5Sums;

    private static final String HSM_FS = "hsm:/home/hsm";

    private FileInfo[][] localFileInfos;

    private FileInfo[][] archivedFileInfos;

    private FileInfo[][] archivedAndLocalFiles;

    private FileInfo archivedFileInfo2;

    private FileInfo archivedFileInfo1;

    private FileInfo archivedFileInfo4;

    private FileInfo archivedFileInfo5;

    private FileInfo archivedFileInfo6;

    private FileInfo archivedFileInfo3;

    private static final String MD5_1 = "123456789012345678901234567890ad";

    private static final String MD5_2 = "123456789012345678901234567890ce";

    @Test
    public void doesntDeleteDirsIfGivenDirectoryIsNotParentOfTheFile() throws Exception {
        File temp = HsmUtils.classpathResource(".");
        File dir1 = new File(temp, "dir1");
        dir1.mkdir();
        File dir2 = new File(temp, "dir2");
        dir2.mkdir();
        File file = new File(dir1, "test.txt");
        file.createNewFile();
        try {
            HsmUtils.deleteParentsTill(file, dir2.getCanonicalPath());
            fail("Didn't throw expected IOException");
        } catch (IOException e) {
            assertEquals(e.getMessage(), dir2 + " is not a parent directory of " + file);
        } finally {
            file.delete();
            dir1.delete();
            dir2.delete();
        }
    }
}
