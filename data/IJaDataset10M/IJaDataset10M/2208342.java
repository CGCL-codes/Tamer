package gc3d.test;

import gc3d.griddata.ZipUtils;
import java.io.File;

public class TestZipUtils {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        File zipfile = new File("/home/pierrick/megapov.zip");
        File unzipfolder = new File("/tmp/GridPovTemp/megapov");
        ZipUtils.unzipArchive(zipfile, unzipfolder);
    }
}
