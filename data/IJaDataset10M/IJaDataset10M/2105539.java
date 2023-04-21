package edu.ucdavis.genomics.metabolomics.binbase.collections;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.EnvironmentLockedException;
import edu.ucdavis.genomics.metabolomics.util.collection.factory.MapFactory;

/**
 * generates a map where all instances share one database connection. It might cause memory issues with very large objects.
 * 
 * @author wohlgemuth
 * 
 */
public class BerkleyDBIndependentMapFactoryImpl<K, V> extends MapFactory<K, V> {

    /**
	 * Initialized one temperaery directory at the first access to the factory
	 */
    private static File temporaryDirectory = generateDirectory();

    private static Database database = generateDatabase(temporaryDirectory);

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

    /**
	 * generates a single instance to the database
	 * 
	 * @param temporaryDirectory2
	 * @return
	 * @throws EnvironmentLockedException
	 * @throws DatabaseException
	 */
    private static Database generateDatabase(final File temporaryDirectory2) {
        try {
            EnvironmentConfig envConfig = new EnvironmentConfig();
            envConfig.setTransactional(false);
            envConfig.setAllowCreate(true);
            envConfig.setSharedCache(true);
            envConfig.setCachePercent(10);
            envConfig.setCacheSize(200000);
            Environment env = new Environment(temporaryDirectory2, envConfig);
            DatabaseConfig dbConfig = new DatabaseConfig();
            dbConfig.setTransactional(false);
            dbConfig.setAllowCreate(true);
            final Database database = env.openDatabase(null, "binbase_map_catalog", dbConfig);
            Runtime.getRuntime().addShutdownHook(new Thread() {

                @Override
                public void run() {
                    try {
                        database.close();
                        temporaryDirectory2.delete();
                    } catch (DatabaseException e) {
                    }
                }
            });
            return database;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<K, V> createMap() {
        try {
            return new BerkleyDBIndependentMap<K, V>(database);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
	 * deletes a file and all subfiles
	 * 
	 * @param file
	 */
    protected static void deleteFile(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                deleteFile(f);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("deleting file: " + file.getName() + " successfull: " + file.delete());
        }
    }
}
