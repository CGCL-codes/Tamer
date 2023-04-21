package ee.webmedia.xtee.client.service.configuration;

/**
 * Delegates calls to embedded configuration.
 * 
 * @author Tanel Käär (tanelk@webmedia.ee)
 */
public class DelegatingXTeeServiceConfiguration implements XTeeServiceConfiguration {

    protected XTeeServiceConfiguration configuration;

    public DelegatingXTeeServiceConfiguration(XTeeServiceConfiguration configuration) {
        this.configuration = configuration;
    }

    public String getDatabase() {
        return configuration.getDatabase();
    }

    public String getFile() {
        return configuration.getFile();
    }

    public String getIdCode() {
        return configuration.getIdCode();
    }

    public String getInstitution() {
        return configuration.getInstitution();
    }

    public String getMethod() {
        return configuration.getMethod();
    }

    public String getSecurityServer() {
        return configuration.getSecurityServer();
    }

    public String getVersion() {
        return configuration.getVersion();
    }

    public String getWsdlDatabase() {
        return configuration.getWsdlDatabase();
    }

    public boolean getForceDatabaseNamespace() {
        return configuration.getForceDatabaseNamespace();
    }

    public void forceDatabaseNamespace() {
        configuration.forceDatabaseNamespace();
    }

    public boolean useDeprecatedApi() {
        return configuration.useDeprecatedApi();
    }
}
