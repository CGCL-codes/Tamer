package uk.ac.roslin.ensemblconfig;

import java.io.Serializable;
import java.util.Properties;
import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 *
 * @author paterson
 */
public class SchemaVersion {

    public static enum Source implements Serializable {

        ENSEMBLDB("EnsemblDB Source Configuration"), ENSEMBLGENOMES("EnsemblGenomes Source Configuration"), LOCAL("Local Source Configuration");

        private String label;

        Source(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private Properties schemaVersionProperties = null;

    private Properties ensembldbProperties = null;

    private Properties ensemblgenomesProperties = null;

    private Properties localdbProperties = null;

    private String currentEnsemblVersion = null;

    private String currentGenomesVersion = null;

    private String[] registeredEnsemblVersions = null;

    public SchemaVersion() {
        schemaVersionProperties = this.readResource("uk.ac.roslin.ensemblconfig.schema_version_mappings");
        ensembldbProperties = this.readResource("uk.ac.roslin.ensemblconfig.ensembldb");
        ensemblgenomesProperties = this.readResource("uk.ac.roslin.ensemblconfig.ensemblgenomes");
        currentEnsemblVersion = (schemaVersionProperties != null) ? schemaVersionProperties.getProperty("current_ensembl_release") : null;
        currentGenomesVersion = (schemaVersionProperties != null) ? schemaVersionProperties.getProperty("current_genomes_release") : null;
        registeredEnsemblVersions = (schemaVersionProperties != null) ? schemaVersionProperties.getProperty("ensembl_releases").split(" ") : null;
    }

    public SchemaVersion(Properties localProperties) {
        this();
        this.localdbProperties = localProperties;
    }

    private Properties readResource(String id) {
        Properties p = null;
        try {
            ResourceBundle rb = ResourceBundle.getBundle(id);
            p = new Properties();
            for (Enumeration keys = rb.getKeys(); keys.hasMoreElements(); ) {
                final String key = (String) keys.nextElement();
                final String value = rb.getString(key);
                p.put(key, value);
            }
        } catch (Exception ex) {
            System.out.println("System can't read the configuration file: " + id);
        }
        return p;
    }

    public String getMybatisSchemaPath(String database, String version) {
        String out = null;
        String location = null;
        String sc = null;
        location = (schemaVersionProperties != null) ? schemaVersionProperties.getProperty("schema_location") : null;
        sc = (schemaVersionProperties != null) ? schemaVersionProperties.getProperty(database + "_" + version + "_schema") : null;
        out = (location != null && sc != null) ? location + sc + "Configuration.xml" : null;
        return out;
    }

    public Properties getConfiguration(Source registrySource) {
        if (registrySource == Source.ENSEMBLDB) {
            return ensembldbProperties;
        } else if (registrySource == Source.ENSEMBLGENOMES) {
            return ensemblgenomesProperties;
        } else if (registrySource == Source.LOCAL) {
            return localdbProperties;
        }
        return null;
    }

    public String getCurrentVersion(Source registrySource) {
        return currentEnsemblVersion;
    }

    public String getCurrentGenomesVersion(Source registrySource) {
        return currentGenomesVersion;
    }

    public String[] getRegisteredVersions(Source registrySource) {
        return registeredEnsemblVersions;
    }

    public String getBaseMybatis() {
        return schemaVersionProperties.getProperty("base_mybatis");
    }
}
