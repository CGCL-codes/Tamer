package org.dcm4chex.archive.hsm.spi;

import org.dcm4chex.archive.ejb.jdbc.FileInfo;
import org.dcm4chex.archive.hsm.spi.utils.HsmUtils;
import org.dcm4che.dict.UIDs;
import java.io.IOException;
import java.io.File;

/**
 * @author Fuad Ibrahimov
 * @since Apr 8, 2007
 */
public class TestUtils {

    public static FileInfo newFileInfo(int pk, String name, String baseDir, String seriesIUID, long fileLength, String sopIUID, String md5, int status) throws IOException {
        return new FileInfo(pk, "123", "Peter Mustermann", null, "1.2.3.4.5", seriesIUID, null, null, null, sopIUID, "1.44.33.22", "TEST_AET", "TEST_AET", 0, baseDir, name, UIDs.ExplicitVRBigEndian, md5, fileLength, status);
    }

    public static void cleanup(File tarFile, String tempDir) throws IOException {
        if (tarFile != null) {
            tarFile.delete();
            if (tempDir != null) HsmUtils.deleteParentsTill(tarFile, tempDir);
        }
    }
}
