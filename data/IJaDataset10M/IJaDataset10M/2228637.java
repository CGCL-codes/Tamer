package edu.ucdavis.genomics.metabolomics.binbase.collections;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import edu.ucdavis.genomics.metabolomics.util.collection.factory.MapFactory;

/**
 * generates a map and each map has its own database connection and data storage
 * assigned. So this is good for maps which store very very large objects internally,
 * but has a huge overhead on the file system and is tremendelous slow for this reason
 * 
 * @author wohlgemuth
 * 
 */
public class BerkleySingleDirectoryMapFactoryImpl<K, V> extends MapFactory<K, V> {

    /**
	 * generates a temporaery directory and hopefully deletes it a the end of
	 * the run
	 * 
	 * @return
	 */
    protected static File generateDirectory() {
        try {
            File file = File.createTempFile("binbase", "temp");
            final File temporaryDirectory = new File(file.getAbsolutePath() + "_store/");
            if (temporaryDirectory.exists() == false) {
                temporaryDirectory.mkdirs();
            }
            file.delete();
            return temporaryDirectory;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<K, V> createMap() {
        try {
            return new BerkleyDBMap<K, V>(generateDirectory(), true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
