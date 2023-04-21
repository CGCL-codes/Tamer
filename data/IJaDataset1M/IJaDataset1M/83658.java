package net.sourceforge.ecldbtool.connect;

import java.sql.Driver;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import net.sourceforge.ecldbtool.model.io.IDDEGenerator2;
import net.sourceforge.ecldbtool.model.io.IMetadataReader;
import net.sourceforge.ecldbtool.model.io.standard.JDBCMetaDataMapper;

public class DriverDescriptor {

    private String driverClassName;

    private String defaultURL;

    private String displayName;

    private IConfigurationElement configElement;

    private Driver driver;

    private IMetadataReader metadataReader;

    private IDDEGenerator2 ddeGenerator;

    DriverDescriptor(String driverClassName, String defaultURL, String displayName, IConfigurationElement configElement) {
        this.driverClassName = driverClassName;
        this.defaultURL = defaultURL;
        this.displayName = displayName;
        this.configElement = configElement;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getDefaultURL() {
        return defaultURL;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Driver getDriver() throws CoreException {
        if (driver == null) try {
            driver = (Driver) configElement.createExecutableExtension("driverClassName");
        } catch (Throwable e) {
            try {
                driver = (Driver) Class.forName("COM.ibm.db2.jdbc.app.DB2Driver").newInstance();
            } catch (Exception e2) {
            }
        }
        return driver;
    }

    public IMetadataReader getMetadataReader() throws CoreException {
        if (metadataReader == null) try {
            metadataReader = (IMetadataReader) configElement.createExecutableExtension("metadataReaderClassName");
        } catch (Throwable e) {
            metadataReader = new JDBCMetaDataMapper();
        }
        return metadataReader;
    }

    public IDDEGenerator2 getDDEGenerator() throws CoreException {
        if (ddeGenerator == null) ddeGenerator = (IDDEGenerator2) configElement.createExecutableExtension("ddeGeneratorClassName");
        return ddeGenerator;
    }
}
