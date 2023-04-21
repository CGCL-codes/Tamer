package org.personalsmartspace.pss_sm_discovery.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;
import org.personalsmartspace.log.impl.PSSLog;
import org.personalsmartspace.onm.api.pss3p.ICallbackListener;
import org.personalsmartspace.onm.api.pss3p.XMLConverter;
import org.personalsmartspace.pss_sm_api.impl.OWLSSparqlParser;
import org.personalsmartspace.pss_sm_api.impl.PssService;
import org.personalsmartspace.pss_sm_api.impl.ServiceMgmtConstants;
import org.personalsmartspace.pss_sm_api.impl.ServiceMgmtException;
import org.personalsmartspace.pss_sm_registry.impl.ServiceRegistry;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;

public class ExternalCallback implements ICallbackListener {

    private PSSLog logger = new PSSLog(this);

    private String owlsStoreDirectory = System.getProperty(ServiceMgmtConstants.JAVA_USER_DIRECTORY) + File.separator + ServiceMgmtConstants.REMOTE_OWLS_DIRECTORY;

    private IServiceIdentifier serviceId;

    public ExternalCallback(IServiceIdentifier serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public void handleCallbackObject(Object xmlObject) {
        logger.debug("Callback object : " + xmlObject);
    }

    @Override
    public synchronized void handleCallbackString(String xmlObject) {
        String owlsData = (String) XMLConverter.xmlToObject(xmlObject, String.class);
        logger.debug("Callback string : " + owlsData);
        File owlsStore = new File(owlsStoreDirectory);
        if (owlsStore.exists()) {
            File owlsFile = new File(owlsStoreDirectory + File.separator + "OWLSFile" + System.currentTimeMillis() + ServiceMgmtConstants.OWLS_FILE_SUFFIX);
            if (this.writeToFile(owlsFile, owlsData)) {
                try {
                    OWLSSparqlParser parser = new OWLSSparqlParser(owlsFile.toURI().toString());
                    File permOwlsFile = new File(owlsStoreDirectory + File.separator + parser.getServiceName() + ServiceMgmtConstants.OWLS_FILE_SUFFIX);
                    this.writeToFile(permOwlsFile, owlsData);
                    owlsFile.delete();
                    ServiceRegistry registry = ServiceRegistry.getInstance();
                    PssService existing = registry.findService(this.serviceId);
                    existing.setOntologyDescriptionURI(permOwlsFile.toURI().toString());
                    existing.setServiceQualifiers(parser.getServiceProfileProps());
                    existing.setVersionNumber(parser.getVersionNumber());
                    registry.updateForeignService(existing);
                    permOwlsFile.deleteOnExit();
                } catch (ServiceMgmtException e) {
                    logger.debug("ServiceMgmt exception", e);
                }
            }
        }
    }

    @Override
    public void handleErrorMessage(String errorMessage) {
        logger.debug("Error object : " + errorMessage);
    }

    /**
     * Create a new file with contents
     * 
     * @param file
     * @param contents
     * @return boolean
     */
    private boolean writeToFile(File file, String contents) {
        boolean retvalue = true;
        FileWriter out;
        try {
            out = new FileWriter(file);
            out.write(contents);
            out.close();
        } catch (IOException e) {
            logger.debug("IO exception", e);
            retvalue = false;
        }
        return retvalue;
    }
}
