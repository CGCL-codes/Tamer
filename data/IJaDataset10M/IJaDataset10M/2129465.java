package org.naftulin.configmgr.parsers;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.naftulin.configmgr.ConfigurationManagementEntry;
import org.naftulin.configmgr.ConfigurationManagementEntryImpl;
import org.naftulin.configmgr.ConfigurationManagerException;
import org.naftulin.configmgr.ConfigurationType;
import org.naftulin.configmgr.content.MasterRecordImpl;
import org.xml.sax.SAXException;

/**
 * Parses master configuration file to create a configuration entry.
 * 
 * @author Henry Naftulin
 * @version 1.0
 */
public class MasterRecordParser extends AbstractConfigEntryParser {

    private static final long serialVersionUID = 1L;

    private static final String MASTER_RECORD_RULES_XML = "master-record-rules.xml";

    private static final Logger log = Logger.getLogger(MasterRecordParser.class);

    /**
	 * Retrurns a configuration managment entry by reading the master record file passed in, and storing it's content.
	 * @param key the key configuration entry will be assigned
	 * @param fileUrl the file URL to be parsed.
	 * @return a configuration managment with content of master configuration file.
	 * @throws ConfigurationManagerException if an error occurs while parsing an entry.
	 */
    public ConfigurationManagementEntry getConfigurationManagementEntry(final String key, final URL fileUrl) throws ConfigurationManagerException {
        validateParameters(key, fileUrl);
        final String fileName = fileUrl.getFile();
        String content = null;
        ConfigurationManagementEntry entry = null;
        try {
            final InputStream stream = fileUrl.openStream();
            log.debug("reading master record configuration from file " + fileName);
            content = readStreamContentAsString(stream);
            log.debug("master record configuration is " + content);
        } catch (IOException e) {
            log.warn("Error while reading log4j file", e);
            throw new ConfigurationManagerException("Error while reading log4j file", e);
        }
        entry = new ConfigurationManagementEntryImpl(key, fileName, content, this, ConfigurationType.MASTER_RECORD);
        log.info("configured entry " + entry);
        return entry;
    }

    private void validateParameters(final String key, final URL fileUrl) throws ConfigurationManagerException {
        if (fileUrl == null) {
            throw new ConfigurationManagerException("file URL is null");
        }
        if (fileUrl.getFile() == null) {
            throw new ConfigurationManagerException("file name passed in the URL " + fileUrl + " is null");
        }
        if (key == null) {
            throw new ConfigurationManagerException("key is null");
        }
    }

    /**
	 * Returns a string representation of this parser.
	 * @return a string representation of this parser.
	 */
    public String toString() {
        return "master record parser";
    }

    /**
	 * Returns master record based on the configuration file passed in. Parses the passed in file based
	 * on the digester rules defined in MASTER_RECORD_RULES_XML and
	 * uses commons digester to create master record object based on it.
	 * @param fileName xml file that describe the configuration.
	 * @return master record.
	 * @exception ConfigurationManagerException if a master record cannot be parsed.
	 */
    public MasterRecordImpl digestMasterRecord(final String fileName) throws ConfigurationManagerException {
        MasterRecordImpl masterRecord = null;
        final Digester masterRecordDigester = getDigester();
        if (masterRecordDigester == null) {
            throw new ConfigurationManagerException("Major error: could not create a digester from  " + MASTER_RECORD_RULES_XML + " file with rules to parse master record. Make sure all jars that configuration manager depends on are present.");
        }
        log.debug("loaded rules to read master record");
        try {
            log.debug("loading master record file " + fileName);
            final URL masterRecordXml = MasterRecordParser.class.getClassLoader().getResource(fileName);
            if (masterRecordXml == null) {
                throw new ConfigurationManagerException("Could not read master record from file " + fileName + ". Please check whether this file exists and you have read permissions to read it.");
            }
            final Reader preprocessedStream = preprocessConfigurationFile(masterRecordXml);
            masterRecord = (MasterRecordImpl) masterRecordDigester.parse(preprocessedStream);
            if (masterRecord == null) {
                throw new ConfigurationManagerException("Could not parse master record from file " + fileName + " based on rules file " + MASTER_RECORD_RULES_XML + ". Master record is null. Please check these files to make sure they are valid");
            }
            masterRecord.setFileName(fileName);
        } catch (IOException e) {
            throw new ConfigurationManagerException("Could not read master record from file " + fileName + " and rules file " + MASTER_RECORD_RULES_XML + ". Please check whether these files exist and you have read permissions to read them.", e);
        } catch (SAXException e) {
            throw new ConfigurationManagerException("Could not parse master record from file " + fileName + " and rules file " + MASTER_RECORD_RULES_XML + ". Please check whether these files are valid xml files.", e);
        }
        log.info("master record parsed " + masterRecord);
        return masterRecord;
    }

    private Reader preprocessConfigurationFile(final URL masterRecordXml) throws IOException {
        final BufferedReader readStream = new BufferedReader(new InputStreamReader(masterRecordXml.openStream()));
        final StringBuffer sb = new StringBuffer();
        String line;
        while ((line = readStream.readLine()) != null) {
            sb.append(line);
        }
        final String fileContent = sb.toString();
        final String modifiedFileContent = doSubstitutions(fileContent);
        final Reader preprocessedFile = new StringReader(modifiedFileContent);
        return preprocessedFile;
    }

    String doSubstitutions(final String fileContent) {
        String modifiedFileContent = fileContent;
        final Pattern environmentVarPattern = Pattern.compile("\\?\\S+\\?");
        final Matcher environmentVarMatcher = environmentVarPattern.matcher(fileContent);
        String environmentVar = null;
        String value = null;
        while (environmentVarMatcher.find()) {
            String environmentVarWithMarkers = environmentVarMatcher.group();
            if (environmentVarWithMarkers.length() > 2) {
                environmentVar = environmentVarWithMarkers.substring(1, environmentVarWithMarkers.length() - 1);
                value = getEnvironmentVarValue(environmentVar);
                modifiedFileContent = StringUtils.replace(modifiedFileContent, environmentVarWithMarkers, value);
            }
        }
        return modifiedFileContent;
    }

    String getEnvironmentVarValue(final String environmentVar) {
        String value = System.getProperty(environmentVar);
        if (value == null) {
            value = System.getenv(environmentVar);
        }
        if (value == null) {
            value = "";
        }
        return value;
    }

    public Digester getDigester() throws ConfigurationManagerException {
        URL rules = MasterRecordParser.class.getResource(MASTER_RECORD_RULES_XML);
        if (rules == null) {
            throw new ConfigurationManagerException("Major error: could not find " + MASTER_RECORD_RULES_XML + " file with rules to parse master record. Please get a configuration manager distribution.");
        }
        log.debug("about to load rules to read master record");
        Digester masterRecordDigester = DigesterLoader.createDigester(rules);
        return masterRecordDigester;
    }
}
