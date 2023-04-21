package edu.ucdavis.genomics.metabolomics.binbase.algorythm.data.statistic.replacement.resolver;

import java.io.File;
import java.net.URI;
import org.jboss.logging.Logger;
import edu.ucdavis.genomics.metabolomics.binbase.EnviormentVariables;

/**
 * access an enviorement variable and trys to load
 * 
 * @author wohlgemuth
 */
public class EnvDirResolver extends AbstractFileResolver {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Override
    public File resolveNetcdfFile(String sampleName) {
        return new MyFile(getFile(System.getProperty(EnviormentVariables.BINBASE_LOCAL_NETCDF_DIR), sampleName).getAbsolutePath());
    }

    public String toString() {
        return getClass().getName() + " - variable:  " + EnviormentVariables.BINBASE_LOCAL_NETCDF_DIR + " dir: " + System.getProperty(EnviormentVariables.BINBASE_LOCAL_NETCDF_DIR);
    }

    class MyFile extends File {

        private Logger logger = Logger.getLogger(getClass());

        public MyFile(String parent, String child) {
            super(parent, child);
        }

        public MyFile(String pathname) {
            super(pathname);
        }

        public MyFile(URI uri) {
            super(uri);
        }

        public MyFile(File parent, String child) {
            super(parent, child);
        }

        @Override
        public boolean canWrite() {
            return false;
        }

        @Override
        public boolean delete() {
            logger.warn("sorry file can't be deleted!");
            return false;
        }

        @Override
        public void deleteOnExit() {
            logger.warn("sorry file can't be deleted!");
        }

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;
    }
}
