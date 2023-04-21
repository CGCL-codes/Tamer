package org.fcrepo.server.validation;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.fcrepo.server.Module;
import org.fcrepo.server.Server;
import org.fcrepo.server.errors.ModuleInitializationException;
import org.fcrepo.server.errors.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Module Wrapper for DOValidatorImpl.
 *
 * @author Sandy Payette
 */
public class DOValidatorModule extends Module implements DOValidator {

    private static final Logger logger = LoggerFactory.getLogger(DOValidatorModule.class);

    /**
     * An instance of the core implementation class for DOValidator. The
     * DOValidatorModule acts as a wrapper to this class.
     */
    private DOValidatorImpl dov = null;

    /**
     * <p>
     * Constructs a new DOValidatorModule
     * </p>
     *
     * @param moduleParameters
     *        The name/value pair map of module parameters.
     * @param server
     *        The server instance.
     * @param role
     *        The module role name.
     * @throws ModuleInitializationException
     *         If initialization values are invalid or initialization fails for
     *         some other reason.
     * @throws ServerException
     */
    public DOValidatorModule(Map<String, String> moduleParameters, Server server, String role) throws ModuleInitializationException, ServerException {
        super(moduleParameters, server, role);
    }

    @Override
    public void postInitModule() throws ModuleInitializationException {
        try {
            HashMap<String, String> xmlSchemaMap = new HashMap<String, String>();
            HashMap<String, String> ruleSchemaMap = new HashMap<String, String>();
            String tempDir = null;
            String schematronPreprocessorPath = null;
            Iterator<String> nameIter = parameterNames();
            while (nameIter.hasNext()) {
                String paramName = nameIter.next();
                if (paramName.startsWith("xsd_")) {
                    String xmlSchemaName = paramName.substring(4);
                    try {
                        String xmlSchemaPath = new File(getServer().getHomeDir(), getParameter(paramName)).getPath();
                        xmlSchemaMap.put(xmlSchemaName, xmlSchemaPath);
                        logger.debug("Initialized XML Schema " + "location: " + xmlSchemaPath);
                    } catch (Exception e) {
                        String msg = "Problem configuring XML Schema for format=" + xmlSchemaName;
                        logger.error(msg, e);
                        throw new ModuleInitializationException(msg + ": " + e.getClass().getName() + ": " + e.getMessage(), getRole());
                    }
                } else if (paramName.startsWith("rules_")) {
                    String ruleSchemaName = paramName.substring(6);
                    try {
                        String ruleSchemaPath = new File(getServer().getHomeDir(), getParameter(paramName)).getPath();
                        ruleSchemaMap.put(ruleSchemaName, ruleSchemaPath);
                        logger.debug("Initialized Schematron schema " + "location: " + ruleSchemaPath);
                    } catch (Exception e) {
                        String msg = "Problem configuring Schematron Schema for format=" + ruleSchemaName;
                        logger.error(msg, e);
                        throw new ModuleInitializationException(msg + ": " + e.getClass().getName() + ": " + e.getMessage(), getRole());
                    }
                } else if (paramName.equals("tempDir")) {
                    tempDir = new File(getServer().getHomeDir(), getParameter(paramName)).getPath();
                    logger.debug("tempDir set to: " + tempDir);
                } else if (paramName.equals("schtron_preprocessor")) {
                    schematronPreprocessorPath = new File(getServer().getHomeDir(), getParameter(paramName)).getPath();
                    logger.debug("Initialized Schematron " + "preprocessor location: " + schematronPreprocessorPath);
                }
            }
            dov = new DOValidatorImpl(tempDir, xmlSchemaMap, schematronPreprocessorPath, ruleSchemaMap);
        } catch (Exception e) {
            throw new ModuleInitializationException(e.getMessage(), "org.fcrepo.server.validation.DOValidatorModule");
        }
    }

    /**
     * <p>
     * Validates a digital object.
     * </p>
     *
     * @param objectAsStream
     *        The digital object provided as a bytestream.
     * @param validationType
     *        The level of validation to perform on the digital object. This is
     *        an integer from 0-2 with the following meanings: 0 = VALIDATE_ALL
     *        (do all validation levels) 1 = VALIDATE_XML_SCHEMA (perform only
     *        XML Schema validation) 2 = VALIDATE_SCHEMATRON (perform only
     *        Schematron Rules validation)
     * @param phase
     *        The stage in the work flow for which the validation should be
     *        contextualized. "ingest" = the object is in the submission format
     *        for the ingest stage phase "store" = the object is in the
     *        authoritative format for the final storage phase
     * @throws ServerException
     *         If validation fails for any reason.
     */
    public void validate(InputStream objectAsStream, String format, int validationType, String phase) throws ServerException {
        dov.validate(objectAsStream, format, validationType, phase);
    }

    /**
     * <p>
     * Validates a digital object.
     * </p>
     *
     * @param objectAsFile
     *        The digital object provided as a file.
     * @param validationType
     *        The level of validation to perform on the digital object. This is
     *        an integer from 0-2 with the following meanings: 0 = VALIDATE_ALL
     *        (do all validation levels) 1 = VALIDATE_XML_SCHEMA (perform only
     *        XML Schema validation) 2 = VALIDATE_SCHEMATRON (perform only
     *        Schematron Rules validation)
     * @param phase
     *        The stage in the work flow for which the validation should be
     *        contextualized. "ingest" = the object is in the submission format
     *        for the ingest stage phase "store" = the object is in the
     *        authoritative format for the final storage phase
     * @throws ServerException
     *         If validation fails for any reason.
     */
    public void validate(File objectAsFile, String format, int validationType, String phase) throws ServerException {
        dov.validate(objectAsFile, format, validationType, phase);
    }
}
