package com.jedox.etl.components.connection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.palo.api.*;
import com.jedox.etl.components.config.connection.OLAPGlobalConnectionConfigurator;
import com.jedox.etl.core.component.ConfigurationException;
import com.jedox.etl.core.component.InitializationException;
import com.jedox.etl.core.component.RuntimeException;
import com.jedox.etl.core.config.Settings;
import com.jedox.etl.core.connection.IOLAPConnection;
import com.jedox.etl.core.util.FileUtil;

public class OLAPGlobalConnection extends OLAPConnection implements IOLAPConnection {

    private static final Log log = LogFactory.getLog(OLAPGlobalConnection.class);

    private org.palo.api.Connection connection;

    private int timeout;

    private String globalReference;

    private String[] suiteConnectionInfo;

    public OLAPGlobalConnection() {
        setConfigurator(new OLAPGlobalConnectionConfigurator());
    }

    public OLAPGlobalConnectionConfigurator getConfigurator() {
        return (OLAPGlobalConnectionConfigurator) super.getConfigurator();
    }

    /**
	 * Find the Config file config.php of Palo Suite 
	 * @return File config.php of the palo database
	 */
    private File getConfigFile(String pathToConfig) throws ConfigurationException {
        File file;
        pathToConfig = pathToConfig.replace("/", File.separator);
        pathToConfig = pathToConfig.replace("\\", File.separator);
        if (FileUtil.isRelativ(pathToConfig)) {
            String globalPath = Settings.getRootDir() + File.separator + pathToConfig;
            file = new File(globalPath);
            if (!file.exists()) {
                globalPath = Settings.getRootDir() + File.separator + ".." + File.separator + ".." + File.separator + pathToConfig;
                file = new File(globalPath);
            }
        } else {
            file = new File(pathToConfig);
        }
        if (!file.exists()) {
            throw new ConfigurationException("Config file of Palo Suite not found in " + pathToConfig);
        }
        return file;
    }

    /**
	 * Determine the connection info of global Palo OLAP instance from config file of Palo Suite 
	 */
    private void setSuiteConnectionInfo(String pathToConfig) throws ConfigurationException {
        String[] confNames = new String[] { "CFG_PALO_HOST", "CFG_PALO_PORT", "CFG_PALO_USER", "CFG_PALO_PASS" };
        String[] tmpStrs;
        String line = null, confName;
        Pattern testPat = null;
        Matcher mchr = null;
        int i, countConfs = 4;
        suiteConnectionInfo = new String[] { "", "", "", "" };
        try {
            FileReader fr = new FileReader(getConfigFile(pathToConfig));
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                for (i = 0; i < confNames.length; i++) {
                    confName = confNames[i];
                    testPat = Pattern.compile(confName + "['\"](.)*?['\"]");
                    mchr = testPat.matcher(line);
                    if (mchr.find()) {
                        countConfs--;
                        tmpStrs = line.split(confName + "['\"](.)*?['\"]");
                        tmpStrs = tmpStrs[1].split("['\"]");
                        suiteConnectionInfo[i] = tmpStrs[0];
                        break;
                    }
                }
                if (countConfs == 0) break;
            }
        } catch (Exception e) {
            throw new ConfigurationException("Failed to determine connection parameters of global palo instance: " + e.getMessage());
        }
    }

    protected ConnectionConfiguration getSuiteConnectionConfiguration() {
        ConnectionConfiguration cc = new ConnectionConfiguration(suiteConnectionInfo[0], suiteConnectionInfo[1]);
        cc.setUser(suiteConnectionInfo[2]);
        cc.setPassword(suiteConnectionInfo[3]);
        cc.setLoadOnDemand(true);
        cc.setTimeout(timeout);
        return cc;
    }

    protected org.palo.api.Connection connect2OLAP() throws RuntimeException {
        try {
            log.debug("Opening connection " + getName());
            ConnectionFactory confact = ConnectionFactory.getInstance();
            org.palo.api.Connection palosuiteConnection = confact.newConnection(getSuiteConnectionConfiguration());
            log.debug("Connection to Palo Suite OLAP Server on " + suiteConnectionInfo[0] + " " + suiteConnectionInfo[0] + " is open.");
            Database configdb = palosuiteConnection.getDatabaseByName("Config");
            Hierarchy connectionHierarchy = configdb.getDimensionByName("connections").getDefaultHierarchy();
            Element connectionProperties = connectionHierarchy.getElementByName(globalReference);
            if (connectionProperties == null) throw new RuntimeException("Connection " + globalReference + " is not found.");
            String globalConnTypet = (String) connectionProperties.getAttributeValue(connectionHierarchy.getAttribute("host"));
            if (!globalConnTypet.equals("palo")) throw new RuntimeException("Connection " + getDatabase() + " is not a Palo Connection.");
            String globalConnHost = (String) connectionProperties.getAttributeValue(connectionHierarchy.getAttributeByName("host"));
            String globalConnPort = (String) connectionProperties.getAttributeValue(connectionHierarchy.getAttributeByName("port"));
            String globalConnUsername = (String) connectionProperties.getAttributeValue(connectionHierarchy.getAttributeByName("username"));
            String globalConnPassword = (String) connectionProperties.getAttributeValue(connectionHierarchy.getAttributeByName("password"));
            int globalConnActive = Integer.parseInt((String) connectionProperties.getAttributeValue(connectionHierarchy.getAttributeByName("active")));
            palosuiteConnection.disconnect();
            if (globalConnActive != 1) throw new RuntimeException("Connection " + getDatabase() + " is inactive and therefor can not be used.");
            ConnectionConfiguration cc = new ConnectionConfiguration(globalConnHost, globalConnPort);
            cc.setUser(globalConnUsername);
            cc.setPassword(globalConnPassword);
            cc.setLoadOnDemand(true);
            cc.setTimeout(timeout);
            connection = confact.newConnection(cc);
            log.debug("OLAP Connection Timeout set to " + timeout + " ms");
            return connection;
        } catch (Exception e) {
            throw new RuntimeException("Failed to open OLAP connection " + getName() + ". Please check your connection specification. " + e.getMessage());
        }
    }

    public void init() throws InitializationException {
        try {
            super.init();
            globalReference = getConfigurator().getGlobalReference();
            timeout = Integer.parseInt(getParameter("Timeout", String.valueOf(30000)));
            String configPath = getConfigurator().getConfigPath();
            setSuiteConnectionInfo(configPath);
        } catch (Exception e) {
            throw new InitializationException(e);
        }
    }
}
