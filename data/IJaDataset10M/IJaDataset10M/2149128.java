package org.epoline.impexp.jsf.businesslogic.cl;

import java.util.Properties;
import org.apache.log4j.Logger;
import org.epo.utils.EPODate;
import org.epo.utils.ImageFormat;
import org.epo.wad.WAD_SubDocument;
import org.epoline.impexp.jsf.businesslogic.dl.*;
import org.epoline.jsf.client.FaultyServiceException;
import org.epoline.jsf.client.ServiceNotAvailableException;
import org.epoline.jsf.client.ServiceStatus;
import org.epoline.service.shared.BaseServiceDelegate;
import org.epoline.service.shared.DossierKey;

/**
 * Delegate class for BusinessLogicDBService that should be used by any client that wants to use the BusinessLogicDB Service
 */
public class BusinessLogicDBServiceDelegate extends BaseServiceDelegate {

    private int MAX_RETRY = 1;

    /**
	 * Constructor for BusinessLogicDBServiceDelegate withouth any attributes
	 * @param props Object containing all the properties required for JSF
	 * @param log A Logger object to be used for logging, otherwhise a own logger is created
	 * @exception ServiceNotAvailableException The delegate is not able to work with the JSF
	 */
    public BusinessLogicDBServiceDelegate(Properties props, Logger log) throws ServiceNotAvailableException {
        this(props, null, log);
    }

    /**
	 * Constructor for BusinessLogicDBServiceDelegate for BusinessLogicDBService with specific Attributes
	 * @param props Object containing all the properties required for JSF
	 * @param log A Logger object to be used for logging, otherwhise a own logger is created
	 * @param attributes The attributes the service should have.
	 * @exception ServiceNotAvailableException The delegate is not able to work with the JSF
	 */
    public BusinessLogicDBServiceDelegate(Properties props, String[] attributes, Logger log) throws ServiceNotAvailableException {
        super(props, attributes, log, BusinessLogicDBProxyInterface.NAME);
    }

    /**
	 * Create one document for a certain dossier. The dossier will be created if it does not exist. 
	 * If no package is specified, the package will be created.
	 * @param dossierKeys The keys that are valid for the dossier, required
	 * @param electronic Indicator if the dossier is (according to legacy) electronic, required
	 * @param docCode The Document Code for the document (may also bve a FormID), required
	 * @param totalPages The number of pages in the document, required
	 * @param legalDate The legal date for the package, required
	 * @param annotation Document annotation, optionally
	 * @param messageText Text for the Message to be created after loading, optionally
	 * @param targetTeam Team the message must be sent to after loading of dcoument, optionally
	 * @param targetUser User the message must be sent to after loading of dcoument, optionally
	 * @param source The Source the document is coming from, required
	 * @param destCountry The country the form must be sent to, optionally
	 * @param urgency The urgency of the package for loading, required
	 * @param pckStatus The initial packageStatus for the package to be created, required
	 * @param format The format of the imageData of the Document, required
	 * @param logPackage The logical packageNumber the package belongs to, if 0 not used.
	 * @param aPackage The PackageID the document belongs to, optionally
	 * @param aUser The user who is creating the document, required
	 * @param closedDoc Indicator if the document should be created in Closed status
	 * @param createMessage Indicator overruling all other parameters for message creation in case it is false, required
	 * @return object containing all relevant info required for loading the vreated document in Phoenix
	 * @exception BusinessLogicDBException There is a problem creating the document in DB
	 * @exception DMSDossierException There is a problem with the dossier in DB
	 * @exception ServiceNotAvailableException The service is not accessable at the moment.
	 */
    public DMSDocCreationData createDMSDocument(DossierKey[] dossierKeys, boolean electronic, String docCode, int totalPages, EPODate legalDate, String annotation, String messageText, String targetTeam, String targetUser, String source, String destCountry, int urgency, int pckStatus, ImageFormat format, int logPackage, String aPackage, String aUser, boolean closedDoc, boolean createMessage) throws ServiceNotAvailableException, DMSDossierException, BusinessLogicDBException {
        try {
            if (getLogger().isDebugEnabled()) getLogger().debug("+BusinessLogicDBServiceDelegate.createDMSDocument()");
            return createDMSDocument(dossierKeys, electronic, docCode, totalPages, legalDate, annotation, messageText, targetTeam, targetUser, source, destCountry, urgency, pckStatus, format, logPackage, aPackage, aUser, closedDoc, createMessage, 0);
        } catch (DMSDossierException e) {
            getLogger().warn(e);
            throw e;
        } catch (BusinessLogicDBException e) {
            getLogger().error(e);
            throw e;
        } finally {
            if (getLogger().isDebugEnabled()) getLogger().debug("-BusinessLogicDBServiceDelegate.createDMSDocument()");
        }
    }

    /**
	 * Create a set of documents for a certain dossier. The dossier will be created if it does not exist. 
	 * If no package is specified, the package will be created.
	 * @param dossierKeys The keys that are valid for the dossier, required
	 * @param electronic Indicator if the dossier is (according to legacy) electronic, required
	 * @param subDocs The list of documents that must be created, required
	 * @param legalDate The legal date for the package, required
	 * @param annotation Document annotation, optionally
	 * @param messageText Text for the Message to be created after loading, optionally
	 * @param targetTeam Team the message must be sent to after loading of dcoument, optionally
	 * @param targetUser User the message must be sent to after loading of dcoument, optionally
	 * @param source The Source the document is coming from, required
	 * @param destCountry The country the form must be sent to, optionally
	 * @param urgency The urgency of the package for loading, required
	 * @param pckStatus The initial packageStatus for the package to be created, required
	 * @param format The format of the imageData of the Document, required
	 * @param logPackage The logical packageNumber the package belongs to, if 0 not used.
	 * @param aPackage The PackageID the document belongs to, optionally
	 * @param aUser The user who is creating the document, required
	 * @param closedDoc Indicator if the document should be created in Closed status
	 * @param createMessage Indicator overruling all other parameters for message creation in case it is false, required
	 * @return object containing all relevant info required for loading the created document in Phoenix
	 * @exception BusinessLogicDBException There is a problem creating the document in DB
	 * @exception DMSDossierException There is a problem with the dossier in DB
	 * @exception ServiceNotAvailableException The service is not accessable at the moment.
	 */
    public DMSDocCreationData createDMSDocuments(DossierKey[] dossierKeys, boolean electronic, WAD_SubDocument[] subDocs, EPODate legalDate, String annotation, String messageText, String targetTeam, String targetUser, String source, String country, int urgency, int pckStatus, ImageFormat format, int logPackage, String aPackage, String aUser, boolean closedDoc, boolean createMessage) throws ServiceNotAvailableException, DMSDossierException, BusinessLogicDBException {
        try {
            if (getLogger().isDebugEnabled()) getLogger().debug("+BusinessLogicDBServiceDelegate.createDMSDocuments()");
            return createDMSDocuments(dossierKeys, electronic, subDocs, legalDate, annotation, messageText, targetTeam, targetUser, source, country, urgency, pckStatus, format, logPackage, aPackage, aUser, closedDoc, createMessage, 0);
        } catch (DMSDossierException e) {
            getLogger().warn(e);
            throw e;
        } catch (BusinessLogicDBException e) {
            getLogger().error(e);
            throw e;
        } finally {
            if (getLogger().isDebugEnabled()) getLogger().debug("-BusinessLogicDBServiceDelegate.createDMSDocuments()");
        }
    }

    /**
	 * Set the packagestatus for a specific package and record the change in PackageHistory
	 * @param aPackage The packageID (17 pos) for the package to change the status
	 * @param aValue The new package ID
	 * @param aUser The user who requests the change
	 * @exception BusinessLogicDBException The status can't be set
	 * @exception ServiceNotAvailableException The service is not accessable at the moment.
	 */
    public void setDMSPackageStatus(String aPackage, int aValue, String aUser) throws ServiceNotAvailableException, BusinessLogicDBException {
        try {
            if (getLogger().isDebugEnabled()) getLogger().debug("+BusinessLogicDBServiceDelegate.setDMSPackageStatus()");
            setDMSPackageStatus(aPackage, aValue, aUser, 0);
        } catch (BusinessLogicDBException e) {
            getLogger().error(e);
            throw e;
        } finally {
            if (getLogger().isDebugEnabled()) getLogger().debug("-BusinessLogicDBServiceDelegate.setDMSPackageStatus()");
        }
    }

    /**
     * Close the package (status = 99) and set also all documents in the package to "Deleted" 
     * and record the change in PackageHistory
     * @param aPackage The packageID (17 pos) to close
     * @param aUser The user who requests the change
	 * @exception BusinessLogicDBException The status can't be set
	 * @exception ServiceNotAvailableException The service is not accessable at the moment.
     */
    public void closeDMSPackage(String aPackage, String aUser) throws ServiceNotAvailableException, BusinessLogicDBException {
        try {
            if (getLogger().isDebugEnabled()) getLogger().debug("+BusinessLogicDBServiceDelegate.closeDMSPackage()");
            closeDMSPackage(aPackage, aUser, 0);
        } catch (BusinessLogicDBException e) {
            getLogger().error(e);
            throw e;
        } finally {
            if (getLogger().isDebugEnabled()) getLogger().debug("-BusinessLogicDBServiceDelegate.closeDMSPackage()");
        }
    }

    /**
     * Accept the package will set the Package status to 5 and will return the document details required during
     * the loading process (like with creatDocument())
     * @param aPackageID The packageID to accept
     * @param aUser The user who requests the change
	 * @return object containing all relevant info required for loading the package in Phoenix
	 * @exception BusinessLogicDBException The package can't be accepted
	 * @exception ServiceNotAvailableException The service is not accessable at the moment.
     */
    public DMSDocCreationData acceptPackage(String aPackageID, String aUser) throws ServiceNotAvailableException, BusinessLogicDBException {
        try {
            if (getLogger().isDebugEnabled()) getLogger().debug("+BusinessLogicDBServiceDelegate.acceptPackage()");
            return acceptPackage(aPackageID, aUser, 0);
        } catch (BusinessLogicDBException e) {
            getLogger().error(e);
            throw e;
        } finally {
            if (getLogger().isDebugEnabled()) getLogger().debug("-BusinessLogicDBServiceDelegate.acceptPackage()");
        }
    }

    /**
	 * Accept all packages that are stored on a CD. The CD will be created if not present, all packes will be linked 
	 * to the CD and the status of the packages will be 6.
	 * @param packageBatches a list of so-called PackageBatch strings (17 pos packageID + BatchID)
	 * @param cdrDate The date the CD was produced
	 * @param cdrLocation The location (2 pos) the CD was created
	 * @param cdrSeqNr The sequence for the CD for that date and location
	 * @return Details for each of the package, including if it is loadable.
	 * @exception BusinessLogicDBException The CD can't be accepted
	 * @exception ServiceNotAvailableException The service is not accessable at the moment.
 	 */
    public PackageStatus[] acceptPackages(String[] packageBatches, EPODate cdrDate, int cdrSeqNr, String cdrLocation) throws ServiceNotAvailableException, BusinessLogicDBException {
        try {
            if (getLogger().isDebugEnabled()) getLogger().debug("+BusinessLogicDBServiceDelegate.acceptPackages()");
            return acceptPackages(packageBatches, cdrDate, cdrSeqNr, cdrLocation, 0);
        } catch (BusinessLogicDBException e) {
            getLogger().error(e);
            throw e;
        } finally {
            if (getLogger().isDebugEnabled()) getLogger().debug("-BusinessLogicDBServiceDelegate.acceptPackages()");
        }
    }

    /**
 	 * Add an action to the actionlog for a specific dossier
 	 * @param dossierKeys The keys that identify the dossier
 	 * @param user The user that adds the action
 	 * @param name The name of the action
 	 * @param action The action text to add
	 * @exception BusinessLogicDBException The action can't be added
	 * @exception ServiceNotAvailableException The service is not accessable at the moment.
 	 */
    public void addActionLog(DossierKey[] dossierKeys, String user, String name, String action) throws ServiceNotAvailableException, BusinessLogicDBException {
        try {
            if (getLogger().isDebugEnabled()) getLogger().debug("+BusinessLogicDBServiceDelegate.addActionLog()");
            addActionLog(dossierKeys, user, name, action, 0);
        } catch (BusinessLogicDBException e) {
            getLogger().error(e);
            throw e;
        } finally {
            if (getLogger().isDebugEnabled()) getLogger().debug("-BusinessLogicDBServiceDelegate.addActionLog()");
        }
    }

    /**
 	 * Set the SendForAPubl date for the specified document to today.
 	 * @param docKey The key (database key) for the document to update
	 * @exception BusinessLogicDBException The Document can't be updated
	 * @exception ServiceNotAvailableException The service is not accessable at the moment.
 	 */
    public void setDocSendForAPubl(String docKey) throws ServiceNotAvailableException, BusinessLogicDBException {
        try {
            if (getLogger().isDebugEnabled()) getLogger().debug("+BusinessLogicDBServiceDelegate.setDocSendForAPubl()");
            setDocSendForAPubl(docKey, 0);
        } catch (BusinessLogicDBException e) {
            getLogger().error(e);
            throw e;
        } finally {
            if (getLogger().isDebugEnabled()) getLogger().debug("-BusinessLogicDBServiceDelegate.setDocSendForAPubl()");
        }
    }

    /**
 	 * Set the flag for APubl for the specified document to the specified value
 	 * @param docKey The key (database key) for the document to update
	 * @exception BusinessLogicDBException The Document can't be updated
	 * @exception ServiceNotAvailableException The service is not accessable at the moment.
 	 */
    public void setAPublFlag(String docKey, int aValue) throws ServiceNotAvailableException, BusinessLogicDBException {
        try {
            if (getLogger().isDebugEnabled()) getLogger().debug("+BusinessLogicDBServiceDelegate.setAPublFlag()");
            setAPublFlag(docKey, aValue, 0);
        } catch (BusinessLogicDBException e) {
            getLogger().error(e);
            throw e;
        } finally {
            if (getLogger().isDebugEnabled()) getLogger().debug("-BusinessLogicDBServiceDelegate.setAPublFlag()");
        }
    }

    /**
 	 * Create a Team Message for the specified team and Dossier (direct creation, without Distribution manager)
 	 * Call must probably be removed as soon as MessageService is accepted
 	 * @param dossierKeys the keys the dossier known by.
 	 * @param team The team that the message should be send to.
 	 * @param message The text of the message
	 * @exception BusinessLogicDBException The Document can't be updated
	 * @exception ServiceNotAvailableException The service is not accessable at the moment.
 	 */
    public void createMessage(DossierKey[] dossierKeys, String team, String message) throws ServiceNotAvailableException, BusinessLogicDBException {
        try {
            if (getLogger().isDebugEnabled()) getLogger().debug("+BusinessLogicDBServiceDelegate.createMessage()");
            createMessage(dossierKeys, team, message, 0);
        } catch (BusinessLogicDBException e) {
            getLogger().error(e);
            throw e;
        } finally {
            if (getLogger().isDebugEnabled()) getLogger().debug("-BusinessLogicDBServiceDelegate.createMessage()");
        }
    }

    /**
 	 * Get the details for the DocCode regarding creating mailitems in the Mailbox.
 	 * Call must probably be removed as soon as DMSLight classes are transferred
 	 * @param docCode The code for which the data must be retrieved
 	 * @return The MailcreationData that contains things like description in 3 languages, and how to send)
	 * @exception BusinessLogicDBException The data can't be retrieved
	 * @exception ServiceNotAvailableException The service is not accessable at the moment.
 	 */
    public MailCreationData getMailCreationData(String docCode) throws ServiceNotAvailableException, BusinessLogicDBException {
        try {
            if (getLogger().isDebugEnabled()) getLogger().debug("+BusinessLogicDBServiceDelegate.getMailCreationData()");
            return getMailCreationData(docCode, 0);
        } catch (BusinessLogicDBException e) {
            getLogger().error(e);
            throw e;
        } finally {
            if (getLogger().isDebugEnabled()) getLogger().debug("-BusinessLogicDBServiceDelegate.getMailCreationData()");
        }
    }

    /**
 	 * Get the statuses of a set of packages
 	 * @param packages the list of packagesIDs (17Pos) for which the packagestatus must be retrieved
 	 * @return the list of packageStati in the same order as the input list of packages
	 * @exception BusinessLogicDBException The data can't be retrieved
	 * @exception ServiceNotAvailableException The service is not accessable at the moment.
 	 */
    public int[] getPackageStatus(String[] packages) throws ServiceNotAvailableException, BusinessLogicDBException {
        try {
            if (getLogger().isDebugEnabled()) getLogger().debug("+BusinessLogicDBServiceDelegate.getPackageStatus()");
            return getPackageStatus(packages, 0);
        } catch (BusinessLogicDBException e) {
            getLogger().error(e);
            throw e;
        } finally {
            if (getLogger().isDebugEnabled()) getLogger().debug("-BusinessLogicDBServiceDelegate.getPackageStatus()");
        }
    }

    /**
 	 * Get the list of available docCodes (must be removed if DMsLight classes are used)
 	 * @return the list of available docCodes in DMS
	 * @exception BusinessLogicDBException The data can't be retrieved
	 * @exception ServiceNotAvailableException The service is not accessable at the moment.
 	 */
    public String[] getDocCodes() throws ServiceNotAvailableException, BusinessLogicDBException {
        try {
            if (getLogger().isDebugEnabled()) getLogger().debug("+BusinessLogicDBServiceDelegate.getDocCodes()");
            return getDocCodes(0);
        } catch (BusinessLogicDBException e) {
            getLogger().error(e);
            throw e;
        } finally {
            if (getLogger().isDebugEnabled()) getLogger().debug("-BusinessLogicDBServiceDelegate.getDocCodes()");
        }
    }

    /**
 	 * Get the list of teams that a user is defined in
 	 * @param aUser The user to look for
 	 * @return the list of teams
	 * @exception BusinessLogicDBException The data can't be retrieved
	 * @exception ServiceNotAvailableException The service is not accessable at the moment.
 	 */
    public DMSTeamView[] getTeamsForUser(String aUser) throws ServiceNotAvailableException, BusinessLogicDBException {
        try {
            if (getLogger().isDebugEnabled()) getLogger().debug("+BusinessLogicDBServiceDelegate.getTeamsForUser()");
            return getTeamsForUser(aUser, 0);
        } catch (BusinessLogicDBException e) {
            getLogger().error(e);
            throw e;
        } finally {
            if (getLogger().isDebugEnabled()) getLogger().debug("-BusinessLogicDBServiceDelegate.getTeamsForUser()");
        }
    }

    /**
 	 * Get the list of members that are in a team
 	 * @param aTeam The team to look for
 	 * @return the list of members
	 * @exception BusinessLogicDBException The data can't be retrieved
	 * @exception ServiceNotAvailableException The service is not accessable at the moment.
 	 */
    public DMSMemberView[] getMembers(DMSTeamView aTeam) throws ServiceNotAvailableException, BusinessLogicDBException {
        try {
            if (getLogger().isDebugEnabled()) getLogger().debug("+BusinessLogicDBServiceDelegate.getMembers()");
            return getMembers(aTeam, 0);
        } catch (BusinessLogicDBException e) {
            getLogger().error(e);
            throw e;
        } finally {
            if (getLogger().isDebugEnabled()) getLogger().debug("-BusinessLogicDBServiceDelegate.getMembers()");
        }
    }

    /**
 	 * Get the list of documents that are in a package
 	 * @param packageID The packageID (17pos) for which the documents must be retrieved
 	 * @return the list of documents
	 * @exception BusinessLogicDBException The data can't be retrieved
	 * @exception ServiceNotAvailableException The service is not accessable at the moment.
 	 */
    public DMSDocumentView[] getDocuments(String packageID) throws ServiceNotAvailableException, BusinessLogicDBException {
        try {
            if (getLogger().isDebugEnabled()) getLogger().debug("+BusinessLogicDBServiceDelegate.getDocuments()");
            return getDocuments(packageID, 0);
        } catch (BusinessLogicDBException e) {
            getLogger().error(e);
            throw e;
        } finally {
            if (getLogger().isDebugEnabled()) getLogger().debug("-BusinessLogicDBServiceDelegate.getDocuments()");
        }
    }

    /**
 	 * check if the specified dossier is present in DMS
 	 * @param keys the keys under which the dossier should be present
 	 * @return true, if present otherwhise false
	 * @exception BusinessLogicDBException The data can't be retrieved
	 * @exception ServiceNotAvailableException The service is not accessable at the moment.
 	 */
    public boolean isDossierPresent(DossierKey[] keys) throws ServiceNotAvailableException, BusinessLogicDBException {
        try {
            if (getLogger().isDebugEnabled()) getLogger().debug("+BusinessLogicDBServiceDelegate.isDossierPresent()");
            return isDossierPresent(keys, 0);
        } catch (BusinessLogicDBException e) {
            getLogger().error(e);
            throw e;
        } finally {
            if (getLogger().isDebugEnabled()) getLogger().debug("-BusinessLogicDBServiceDelegate.isDossierPresent()");
        }
    }

    /**
 	 * get the real Phoenix docCode for either a FormID or docCode
 	 * @param aCode the code to check, first it will be interpreted as FormID, if not found it will be interpreted as DocCode.
 	 * @return the DocCode
	 * @exception BusinessLogicDBException The data can't be retrieved
	 * @exception ServiceNotAvailableException The service is not accessable at the moment.
 	 */
    public String getDocCode(String aCode) throws ServiceNotAvailableException, BusinessLogicDBException {
        try {
            if (getLogger().isDebugEnabled()) getLogger().debug("+BusinessLogicDBServiceDelegate.getDocCode()");
            return getDocCode(aCode, 0);
        } catch (BusinessLogicDBException e) {
            getLogger().error(e);
            throw e;
        } finally {
            if (getLogger().isDebugEnabled()) getLogger().debug("-BusinessLogicDBServiceDelegate.getDocCode()");
        }
    }

    public String getMailroomPrinter(String destCountry, int totalPages, int sendMethod) throws ServiceNotAvailableException, BusinessLogicDBException {
        try {
            if (getLogger().isDebugEnabled()) getLogger().debug("+BusinessLogicDBServiceDelegate.getMailroomPrinter()");
            return getMailroomPrinter(destCountry, totalPages, sendMethod, 0);
        } catch (BusinessLogicDBException e) {
            getLogger().error(e);
            throw e;
        } finally {
            if (getLogger().isDebugEnabled()) getLogger().debug("-BusinessLogicDBServiceDelegate.getMailroomPrinter()");
        }
    }

    public String getBatchPrintingPrinter(String source, String docCode) throws ServiceNotAvailableException, BusinessLogicDBException {
        try {
            if (getLogger().isDebugEnabled()) getLogger().debug("+BusinessLogicDBServiceDelegate.getBatchPrintingPrinter()");
            return getBatchPrintingPrinter(source, docCode, 0);
        } catch (BusinessLogicDBException e) {
            getLogger().error(e);
            throw e;
        } finally {
            if (getLogger().isDebugEnabled()) getLogger().debug("-BusinessLogicDBServiceDelegate.getBatchPrintingPrinter()");
        }
    }

    /**
	 * get the current status of the service
	 * @return the status of the Service
	 * @exception ServiceNotAvailableException  The server can't be found.
	 */
    public ServiceStatus getStatus() throws ServiceNotAvailableException {
        try {
            if (getLogger().isDebugEnabled()) getLogger().debug("+BusinessLogicDBServiceDelegate.getStatus()");
            return getStatus(0);
        } finally {
            if (getLogger().isDebugEnabled()) getLogger().debug("-BusinessLogicDBServiceDelegate.getStatus()");
        }
    }

    private DMSDocCreationData createDMSDocument(DossierKey[] dossierKeys, boolean electronic, String docCode, int totalPages, EPODate legalDate, String annotation, String messageText, String targetTeam, String targetUser, String source, String destCountry, int urgency, int pckStatus, ImageFormat format, int logPackage, String aPackage, String aUser, boolean closedDoc, boolean createMessage, int nthTime) throws ServiceNotAvailableException, DMSDossierException, BusinessLogicDBException {
        BusinessLogicDBProxyInterface proxy = null;
        try {
            proxy = getProxy();
            return proxy.createDMSDocument(dossierKeys, electronic, docCode, totalPages, legalDate, annotation, messageText, targetTeam, targetUser, source, destCountry, urgency, pckStatus, format, logPackage, aPackage, aUser, closedDoc, createMessage);
        } catch (FaultyServiceException e) {
            discardService(proxy);
            if (nthTime < MAX_RETRY) return createDMSDocument(dossierKeys, electronic, docCode, totalPages, legalDate, annotation, messageText, targetTeam, targetUser, source, destCountry, urgency, pckStatus, format, logPackage, aPackage, aUser, closedDoc, createMessage, nthTime + 1); else {
                getLogger().error(e);
                throw new ServiceNotAvailableException(e.toString());
            }
        }
    }

    private DMSDocCreationData createDMSDocuments(DossierKey[] dossierKeys, boolean electronic, WAD_SubDocument[] subDocs, EPODate legalDate, String annotation, String messageText, String targetTeam, String targetUser, String source, String country, int urgency, int pckStatus, ImageFormat format, int logPackage, String aPackage, String aUser, boolean closedDoc, boolean createMessage, int nthTime) throws ServiceNotAvailableException, DMSDossierException, BusinessLogicDBException {
        BusinessLogicDBProxyInterface proxy = null;
        try {
            proxy = getProxy();
            return proxy.createDMSDocuments(dossierKeys, electronic, subDocs, legalDate, annotation, messageText, targetTeam, targetUser, source, country, urgency, pckStatus, format, logPackage, aPackage, aUser, closedDoc, createMessage);
        } catch (FaultyServiceException e) {
            discardService(proxy);
            if (nthTime < MAX_RETRY) return createDMSDocuments(dossierKeys, electronic, subDocs, legalDate, annotation, messageText, targetTeam, targetUser, source, country, urgency, pckStatus, format, logPackage, aPackage, aUser, closedDoc, createMessage, nthTime + 1); else {
                getLogger().error(e);
                throw new ServiceNotAvailableException(e.toString());
            }
        }
    }

    private void setDMSPackageStatus(String aPackage, int aValue, String aUser, int nthTime) throws ServiceNotAvailableException, BusinessLogicDBException {
        BusinessLogicDBProxyInterface proxy = null;
        try {
            proxy = getProxy();
            proxy.setDMSPackageStatus(aPackage, aValue, aUser);
        } catch (FaultyServiceException e) {
            discardService(proxy);
            if (nthTime < MAX_RETRY) setDMSPackageStatus(aPackage, aValue, aUser, nthTime + 1); else {
                getLogger().error(e);
                throw new ServiceNotAvailableException(e.toString());
            }
        }
    }

    private void closeDMSPackage(String aPackage, String aUser, int nthTime) throws ServiceNotAvailableException, BusinessLogicDBException {
        BusinessLogicDBProxyInterface proxy = null;
        try {
            proxy = getProxy();
            proxy.closeDMSPackage(aPackage, aUser);
        } catch (FaultyServiceException e) {
            discardService(proxy);
            if (nthTime < MAX_RETRY) closeDMSPackage(aPackage, aUser, nthTime + 1); else {
                getLogger().error(e);
                throw new ServiceNotAvailableException(e.toString());
            }
        }
    }

    private DMSDocCreationData acceptPackage(String aPackageID, String aUser, int nthTime) throws ServiceNotAvailableException, BusinessLogicDBException {
        BusinessLogicDBProxyInterface proxy = null;
        try {
            proxy = getProxy();
            return proxy.acceptPackage(aPackageID, aUser);
        } catch (FaultyServiceException e) {
            discardService(proxy);
            if (nthTime < MAX_RETRY) return acceptPackage(aPackageID, aUser, nthTime + 1); else {
                getLogger().error(e);
                throw new ServiceNotAvailableException(e.toString());
            }
        }
    }

    private PackageStatus[] acceptPackages(String[] packageBatches, EPODate cdrDate, int cdrSeqNr, String cdrLocation, int nthTime) throws ServiceNotAvailableException, BusinessLogicDBException {
        BusinessLogicDBProxyInterface proxy = null;
        try {
            proxy = getProxy();
            return proxy.acceptPackages(packageBatches, cdrDate, cdrSeqNr, cdrLocation);
        } catch (FaultyServiceException e) {
            discardService(proxy);
            if (nthTime < MAX_RETRY) return acceptPackages(packageBatches, cdrDate, cdrSeqNr, cdrLocation, nthTime + 1); else {
                getLogger().error(e);
                throw new ServiceNotAvailableException(e.toString());
            }
        }
    }

    private void addActionLog(DossierKey[] dossierKeys, String user, String name, String action, int nthTime) throws ServiceNotAvailableException, BusinessLogicDBException {
        BusinessLogicDBProxyInterface proxy = null;
        try {
            proxy = getProxy();
            proxy.addActionLog(dossierKeys, user, name, action);
        } catch (FaultyServiceException e) {
            discardService(proxy);
            if (nthTime < MAX_RETRY) addActionLog(dossierKeys, user, name, action, nthTime + 1); else {
                getLogger().error(e);
                throw new ServiceNotAvailableException(e.toString());
            }
        }
    }

    private void setDocSendForAPubl(String docKey, int nthTime) throws ServiceNotAvailableException, BusinessLogicDBException {
        BusinessLogicDBProxyInterface proxy = null;
        try {
            proxy = getProxy();
            proxy.setDocSendForAPubl(docKey);
        } catch (FaultyServiceException e) {
            discardService(proxy);
            if (nthTime < MAX_RETRY) setDocSendForAPubl(docKey, nthTime + 1); else {
                getLogger().error(e);
                throw new ServiceNotAvailableException(e.toString());
            }
        }
    }

    private void setAPublFlag(String docKey, int aValue, int nthTime) throws ServiceNotAvailableException, BusinessLogicDBException {
        BusinessLogicDBProxyInterface proxy = null;
        try {
            proxy = getProxy();
            proxy.setAPublFlag(docKey, aValue);
        } catch (FaultyServiceException e) {
            discardService(proxy);
            if (nthTime < MAX_RETRY) setAPublFlag(docKey, aValue, nthTime + 1); else {
                getLogger().error(e);
                throw new ServiceNotAvailableException(e.toString());
            }
        }
    }

    private void createMessage(DossierKey[] dossierKeys, String team, String message, int nthTime) throws ServiceNotAvailableException, BusinessLogicDBException {
        BusinessLogicDBProxyInterface proxy = null;
        try {
            proxy = getProxy();
            proxy.createMessage(dossierKeys, team, message);
        } catch (FaultyServiceException e) {
            discardService(proxy);
            if (nthTime < MAX_RETRY) createMessage(dossierKeys, team, message, nthTime + 1); else {
                getLogger().error(e);
                throw new ServiceNotAvailableException(e.toString());
            }
        }
    }

    private MailCreationData getMailCreationData(String docCode, int nthTime) throws ServiceNotAvailableException, BusinessLogicDBException {
        BusinessLogicDBProxyInterface proxy = null;
        try {
            proxy = getProxy();
            return proxy.getMailCreationData(docCode);
        } catch (FaultyServiceException e) {
            discardService(proxy);
            if (nthTime < MAX_RETRY) return getMailCreationData(docCode, nthTime + 1); else {
                getLogger().error(e);
                throw new ServiceNotAvailableException(e.toString());
            }
        }
    }

    private int[] getPackageStatus(String[] packages, int nthTime) throws ServiceNotAvailableException, BusinessLogicDBException {
        BusinessLogicDBProxyInterface proxy = null;
        try {
            proxy = getProxy();
            return proxy.getPackageStatus(packages);
        } catch (FaultyServiceException e) {
            discardService(proxy);
            if (nthTime < MAX_RETRY) return getPackageStatus(packages, nthTime + 1); else {
                getLogger().error(e);
                throw new ServiceNotAvailableException(e.toString());
            }
        }
    }

    private String[] getDocCodes(int nthTime) throws ServiceNotAvailableException, BusinessLogicDBException {
        BusinessLogicDBProxyInterface proxy = null;
        try {
            proxy = getProxy();
            return proxy.getDocCodes();
        } catch (FaultyServiceException e) {
            discardService(proxy);
            if (nthTime < MAX_RETRY) return getDocCodes(nthTime + 1); else {
                getLogger().error(e);
                throw new ServiceNotAvailableException(e.toString());
            }
        }
    }

    private DMSTeamView[] getTeamsForUser(String aUser, int nthTime) throws ServiceNotAvailableException, BusinessLogicDBException {
        BusinessLogicDBProxyInterface proxy = null;
        try {
            proxy = getProxy();
            return proxy.getTeamsForUser(aUser);
        } catch (FaultyServiceException e) {
            discardService(proxy);
            if (nthTime < MAX_RETRY) return getTeamsForUser(aUser, nthTime + 1); else {
                getLogger().error(e);
                throw new ServiceNotAvailableException(e.toString());
            }
        }
    }

    private DMSMemberView[] getMembers(DMSTeamView aTeam, int nthTime) throws ServiceNotAvailableException, BusinessLogicDBException {
        BusinessLogicDBProxyInterface proxy = null;
        try {
            proxy = getProxy();
            return proxy.getMembers(aTeam);
        } catch (FaultyServiceException e) {
            discardService(proxy);
            if (nthTime < MAX_RETRY) return getMembers(aTeam, nthTime + 1); else {
                getLogger().error(e);
                throw new ServiceNotAvailableException(e.toString());
            }
        }
    }

    private DMSDocumentView[] getDocuments(String aPackageID, int nthTime) throws ServiceNotAvailableException, BusinessLogicDBException {
        BusinessLogicDBProxyInterface proxy = null;
        try {
            proxy = getProxy();
            return proxy.getDocuments(aPackageID);
        } catch (FaultyServiceException e) {
            discardService(proxy);
            if (nthTime < MAX_RETRY) return getDocuments(aPackageID, nthTime + 1); else {
                getLogger().error(e);
                throw new ServiceNotAvailableException(e.toString());
            }
        }
    }

    private boolean isDossierPresent(DossierKey[] keys, int nthTime) throws ServiceNotAvailableException, BusinessLogicDBException {
        BusinessLogicDBProxyInterface proxy = null;
        try {
            proxy = getProxy();
            return proxy.isDossierPresent(keys);
        } catch (FaultyServiceException e) {
            discardService(proxy);
            if (nthTime < MAX_RETRY) return isDossierPresent(keys, nthTime + 1); else {
                getLogger().error(e);
                throw new ServiceNotAvailableException(e.toString());
            }
        }
    }

    private String getDocCode(String aCode, int nthTime) throws ServiceNotAvailableException, BusinessLogicDBException {
        BusinessLogicDBProxyInterface proxy = null;
        try {
            proxy = getProxy();
            return proxy.getDocCode(aCode);
        } catch (FaultyServiceException e) {
            discardService(proxy);
            if (nthTime < MAX_RETRY) return getDocCode(aCode, nthTime + 1); else {
                getLogger().error(e);
                throw new ServiceNotAvailableException(e.toString());
            }
        }
    }

    private String getMailroomPrinter(String destCountry, int totalPages, int sendMethod, int nthTime) throws ServiceNotAvailableException, BusinessLogicDBException {
        BusinessLogicDBProxyInterface proxy = null;
        try {
            proxy = getProxy();
            return proxy.getMailroomPrinter(destCountry, totalPages, sendMethod);
        } catch (FaultyServiceException e) {
            discardService(proxy);
            if (nthTime < MAX_RETRY) return getMailroomPrinter(destCountry, totalPages, sendMethod, nthTime + 1); else {
                getLogger().error(e);
                throw new ServiceNotAvailableException(e.toString());
            }
        }
    }

    private String getBatchPrintingPrinter(String source, String docCode, int nthTime) throws ServiceNotAvailableException, BusinessLogicDBException {
        BusinessLogicDBProxyInterface proxy = null;
        try {
            proxy = getProxy();
            return proxy.getBatchPrintingPrinter(source, docCode);
        } catch (FaultyServiceException e) {
            discardService(proxy);
            if (nthTime < MAX_RETRY) return getBatchPrintingPrinter(source, docCode, nthTime + 1); else {
                getLogger().error(e);
                throw new ServiceNotAvailableException(e.toString());
            }
        }
    }

    private BusinessLogicDBProxyInterface getProxy() throws ServiceNotAvailableException {
        return (BusinessLogicDBProxyInterface) getServiceProxy();
    }

    private ServiceStatus getStatus(int nthTime) throws ServiceNotAvailableException {
        BusinessLogicDBProxyInterface proxy = null;
        try {
            proxy = getProxy();
            return proxy.getStatus();
        } catch (FaultyServiceException e) {
            discardService(proxy);
            if (nthTime < MAX_RETRY) return getStatus(nthTime + 1); else {
                getLogger().error(e);
                throw new ServiceNotAvailableException(e.toString());
            }
        }
    }
}
