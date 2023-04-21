package au.edu.uq.itee.eresearch.dimer.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepositoryFactory {

    private static final Logger log = LoggerFactory.getLogger(RepositoryFactory.class);

    private static final String REPOSITORY_CONFIG = "au/edu/uq/itee/eresearch/dimer/repository.properties";

    public static Repository createRepository() throws RepositoryException, IOException {
        return createRepository(null, null);
    }

    public static Repository createRepository(InputStream newRepositoryFileStream, InputStream newIndexingConfigurationFileStream) throws RepositoryException, IOException {
        try {
            log.info("Instantiating repository.");
            Properties properties = new Properties();
            properties.load(RepositoryFactory.class.getClassLoader().getResourceAsStream(REPOSITORY_CONFIG));
            File repositoryHome = new File(properties.getProperty("repository-home"));
            File repositoryFile = new File(repositoryHome, "repository.xml");
            File indexingConfigurationFile = new File(repositoryHome, "indexing-configuration.xml");
            if (newRepositoryFileStream != null && !repositoryFile.exists()) {
                OutputStream out = null;
                try {
                    repositoryFile.getParentFile().mkdirs();
                    out = new FileOutputStream(repositoryFile);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = newRepositoryFileStream.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                } catch (Exception e) {
                    log.error("Couldn't write repository file.", e);
                } finally {
                    if (out != null) try {
                        out.close();
                    } catch (Exception e) {
                    }
                }
            }
            if (newIndexingConfigurationFileStream != null && !indexingConfigurationFile.exists()) {
                OutputStream out = null;
                try {
                    indexingConfigurationFile.getParentFile().mkdirs();
                    out = new FileOutputStream(indexingConfigurationFile);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = newIndexingConfigurationFileStream.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                } catch (Exception e) {
                    log.error("Couldn't write indexing configuration file.", e);
                } finally {
                    if (out != null) try {
                        out.close();
                    } catch (Exception e) {
                    }
                }
            }
            RepositoryConfig repositoryConfig = RepositoryConfig.create(repositoryFile.getCanonicalPath(), repositoryHome.getCanonicalPath());
            return RepositoryImpl.create(repositoryConfig);
        } finally {
            try {
                newRepositoryFileStream.close();
            } catch (Exception e) {
            }
            try {
                newIndexingConfigurationFileStream.close();
            } catch (Exception e) {
            }
        }
    }
}
