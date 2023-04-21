package com.funambol.google.engine.source;

import com.funambol.google.exception.GmailManagerException;
import java.io.ByteArrayInputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.TimeZone;
import com.funambol.common.pim.contact.Contact;
import com.funambol.common.pim.converter.ContactToSIFC;
import com.funambol.common.pim.converter.ContactToVcard;
import com.funambol.common.pim.sif.SIFCParser;
import com.funambol.common.pim.vcard.VcardParser;
import com.funambol.framework.core.AlertCode;
import com.funambol.framework.engine.SyncItem;
import com.funambol.framework.engine.SyncItemImpl;
import com.funambol.framework.engine.SyncItemKey;
import com.funambol.framework.engine.SyncItemState;
import com.funambol.framework.engine.source.ContentType;
import com.funambol.framework.engine.source.SyncContext;
import com.funambol.framework.engine.source.SyncSourceException;
import com.funambol.framework.engine.source.SyncSourceInfo;
import com.funambol.framework.security.Sync4jPrincipal;
import com.funambol.framework.tools.beans.BeanInitializationException;
import com.funambol.framework.tools.merge.MergeResult;
import com.funambol.google.admin.GoogleConnectorConfig;
import com.funambol.google.exception.GoogleConnectorConfigException;
import com.funambol.google.items.dao.SyncItemInfo;
import com.funambol.google.items.dao.SyncItemInfoContactDAO;
import com.funambol.google.items.manager.GmailContactManager;

/**
 * This class define <i>SyncSource</i> between
 * SyncServer and Gmail Service
 *
 * @see GoogleSyncSource
 * @author Tiago Conde
 * @version $Id: GmailContactSyncSource.java,v 1.0.0 2007/07/30 15:00:00 Tiago Conde Exp $
 *
 */
public class GmailContactSyncSource extends GoogleSyncSource {

    private static final long serialVersionUID = 6113705370737103834L;

    /**
     * Manager that makes the connection between the SyncSource and the GmailContactDAO
     */
    private GmailContactManager gmailManager = null;

    /**
     * Object responsible for caching sync items info (id and md5 hash)
     */
    private SyncItemInfoContactDAO syncItemInfoDAO = null;

    /**
     * List of id's of contacts to be removed in the commit command
     */
    private ArrayList<String> itemsToRemoveById = new ArrayList<String>();

    public GmailContactSyncSource() {
    }

    public void init() throws BeanInitializationException {
        if (log.isTraceEnabled()) {
            log.trace("Initializing sync source " + sourceURI + " (" + this.getClass().getName() + ")");
        }
        try {
            syncItemInfoDAO = new SyncItemInfoContactDAO(GoogleConnectorConfig.getConfigInstance().getDataSource());
        } catch (GoogleConnectorConfigException e) {
            if (log.isErrorEnabled()) log.error("Unable to initialize SyncItemInfoContactDAO: " + e.getMessage());
            throw new BeanInitializationException("Unable to create instance of SyncItemInfoContactDAO: " + e.getMessage(), e);
        }
        try {
            syncItemInfoDAO.init();
        } catch (Exception e) {
            if (log.isErrorEnabled()) log.error("Unable to initialize SyncItemInfoDAO: " + e.getMessage());
            throw new BeanInitializationException("Unable to initialize SyncItemInfoDAO: " + e.getMessage(), e);
        }
    }

    public void beginSync(SyncContext context) throws SyncSourceException {
        super.beginSync(context);
        if (log.isInfoEnabled()) {
            log.info("begining sync with sourceURI:" + sourceURI);
        }
        principal = context.getPrincipal();
        syncMode = context.getSyncMode();
        principalId = ((Sync4jPrincipal) principal).getId();
        try {
            gmailManager = new GmailContactManager(context.getPrincipal().getUsername(), context.getPrincipal().getUser().getPassword());
            gmailManager.login();
            gmailManager.getAllContactsFromGmail();
        } catch (GmailManagerException ex) {
            throw new SyncSourceException(ex.getMessage());
        }
        device = context.getPrincipal().getDevice();
        SyncSourceInfo info = getInfo();
        ContentType[] contentTypes = null;
        if (info != null) {
            contentTypes = info.getSupportedTypes();
            if (contentTypes != null && contentTypes.length > 0) {
                itemType = contentTypes[0].getType();
            }
        }
        String timezone = device.getTimeZone();
        if (device.getConvertDate()) {
            if (timezone != null && timezone.length() > 0) {
                deviceTimeZoneDescr = timezone;
                deviceTimeZone = TimeZone.getTimeZone(deviceTimeZoneDescr);
            }
        }
        if ((syncMode == AlertCode.SLOW) || (syncMode == AlertCode.REFRESH_FROM_SERVER)) {
            if (log.isInfoEnabled()) {
                log.info("refresh from server, sourceUri:" + sourceURI);
            }
            try {
                syncItemInfoDAO.deleteLocalItems(sourceURI, principalId);
            } catch (Exception e) {
                throw new SyncSourceException(e.getMessage());
            }
        } else {
            if (syncMode == AlertCode.REFRESH_FROM_CLIENT) {
                if (log.isInfoEnabled()) {
                    log.info("refresh from client, sourceUri:" + sourceURI);
                }
                try {
                    syncItemInfoDAO.deleteLocalItems(sourceURI, principalId);
                } catch (Exception e) {
                    throw new SyncSourceException(e.getMessage());
                }
                try {
                    gmailManager.removeAllContacts();
                } catch (GmailManagerException ex) {
                    throw new SyncSourceException(ex.getMessage());
                }
            }
            serverItems = gmailManager.getAllSyncItemInfo();
            try {
                localItems = syncItemInfoDAO.getLocalItems(sourceURI, principalId);
            } catch (Exception e) {
                throw new SyncSourceException(e.getMessage());
            }
            newItemKeys = findNewItemIds();
            updatedItemKeys = findUpdateItemIds();
            deletedItemKeys = findDeleteItemIds();
        }
        if (log.isInfoEnabled()) {
            log.info("sync ended with sourceURI:" + sourceURI);
        }
    }

    public void endSync() throws SyncSourceException {
        super.endSync();
        SyncItemInfo[] serverItems;
        try {
            gmailManager.getAllContactsFromGmail();
            serverItems = gmailManager.getAllSyncItemInfo();
            syncItemInfoDAO.updateLocalItems(serverItems, sourceURI, principalId);
        } catch (Exception e) {
            throw new SyncSourceException("error caching items in endSync:" + e.getMessage());
        }
        try {
            gmailManager.logout();
        } catch (GmailManagerException ex) {
            if (log.isInfoEnabled()) log.info("error logging out: " + ex.getMessage());
        }
        if (log.isInfoEnabled()) log.info("end sync with sourceUri: " + sourceURI);
    }

    public void commitSync() throws SyncSourceException {
        if (log.isInfoEnabled()) log.info("committing changes...");
        if (itemsToRemoveById.size() != 0) {
            try {
                gmailManager.removeContacts(itemsToRemoveById.toArray(new String[itemsToRemoveById.size()]));
            } catch (GmailManagerException ex) {
                throw new SyncSourceException(ex.getMessage());
            }
        }
    }

    public SyncItemKey[] getAllSyncItemKeys() throws SyncSourceException {
        if (log.isInfoEnabled()) log.info("getting all item keys");
        String[] keys;
        try {
            keys = gmailManager.getAllSyncItemKeys();
        } catch (GmailManagerException ex) {
            throw new SyncSourceException(ex.getMessage());
        }
        ArrayList allItems = new ArrayList();
        for (int i = 0; i < keys.length; i++) {
            allItems.add(new SyncItemKey(keys[i]));
        }
        return (SyncItemKey[]) allItems.toArray(new SyncItemKey[0]);
    }

    public SyncItem getSyncItemFromId(SyncItemKey syncItemKey) throws SyncSourceException {
        if (log.isInfoEnabled()) log.info("getting item with id: " + syncItemKey.getKeyAsString());
        Contact contact;
        try {
            contact = gmailManager.getContactFromId(syncItemKey.getKeyAsString());
        } catch (GmailManagerException ex) {
            throw new SyncSourceException(ex.getMessage());
        }
        return convertContactToSyncItem(contact, SyncItemState.NEW);
    }

    public SyncItem addSyncItem(SyncItem syncItem) throws SyncSourceException {
        if (log.isInfoEnabled()) log.info("inserting item on Gmail");
        Contact contact = convertSyncItemToContact(syncItem);
        try {
            contact = gmailManager.addContact(contact);
        } catch (GmailManagerException ex) {
            throw new SyncSourceException(ex.getMessage());
        }
        syncItem = convertContactToSyncItem(contact, SyncItemState.NEW);
        return syncItem;
    }

    public SyncItem updateSyncItem(SyncItem syncItem) throws SyncSourceException {
        if (log.isInfoEnabled()) log.info("updating item " + syncItem.getKey().getKeyAsString() + " on Gmail");
        Contact contact = convertSyncItemToContact(syncItem);
        try {
            contact = gmailManager.updateContact(contact);
        } catch (GmailManagerException ex) {
            throw new SyncSourceException(ex.getMessage());
        }
        syncItem = convertContactToSyncItem(contact, SyncItemState.UPDATED);
        return syncItem;
    }

    public void removeSyncItem(SyncItemKey syncItemKey, Timestamp time) throws SyncSourceException {
        if (log.isInfoEnabled()) log.info("adding item " + syncItemKey.getKeyAsString() + " on Gmail to the items to remove list");
        itemsToRemoveById.add(syncItemKey.getKeyAsString());
    }

    public void removeSyncItem(SyncItemKey syncItemKey, Timestamp time, boolean softDelete) throws SyncSourceException {
        if (softDelete) {
        } else {
            removeSyncItem(syncItemKey, time);
        }
    }

    public SyncItemKey[] getSyncItemKeysFromTwin(SyncItem syncItem) throws SyncSourceException {
        if (log.isInfoEnabled()) log.info("checking twins for item :" + syncItem.getKey().getKeyAsString());
        Contact contact = convertSyncItemToContact(syncItem);
        return gmailManager.getKeysFromTwins(contact);
    }

    public boolean mergeSyncItems(SyncItemKey serverKey, SyncItem clientItem) throws SyncSourceException {
        if (log.isInfoEnabled()) log.info("merging items " + serverKey.getKeyAsString() + " and " + clientItem.getKey().getKeyAsString());
        Contact clientContact = null;
        Contact serverContact = null;
        clientContact = convertSyncItemToContact(clientItem);
        try {
            serverContact = gmailManager.getContactFromId(serverKey.getKeyAsString());
        } catch (GmailManagerException ex) {
            throw new SyncSourceException(ex.getMessage());
        }
        MergeResult mergeResult = null;
        try {
            mergeResult = clientContact.merge(serverContact);
        } catch (Exception e) {
            throw new SyncSourceException("Error merging the contact with gui:" + serverKey.getKeyAsString(), e);
        }
        if (mergeResult.isSetBRequired()) {
            if (log.isInfoEnabled()) log.info("isSetBRequired");
            SyncItem serverSyncItem = convertContactToSyncItem(serverContact, SyncItemState.UPDATED);
            updateSyncItem(serverSyncItem);
        }
        if (mergeResult.isSetARequired()) {
            if (log.isInfoEnabled()) log.info("isSetARequired");
            clientItem = convertContactToSyncItem(clientContact, SyncItemState.UPDATED);
        }
        return mergeResult.isSetARequired();
    }

    /**
     * Convert a SyncItem into a Foundation Contact
     * @param syncItem
     * @return Contact
     * @throws SyncSourceException
     */
    private Contact convertSyncItemToContact(SyncItem syncItem) throws SyncSourceException {
        if (log.isInfoEnabled()) log.info("converting SyncItem with id: " + syncItem.getKey().getKeyAsString() + " to Contact");
        Contact contact = null;
        byte[] itemContent = syncItem.getContent();
        if (itemContent == null) {
            itemContent = new byte[0];
        }
        String content = new String(itemContent);
        if (content != null && content.length() > 0) {
            if (TYPE_VCARD.equals(itemType)) {
                contact = vcard2Contact(content);
            } else {
                contact = sifc2Contact(content);
            }
        }
        contact.setUid(syncItem.getKey().getKeyAsString());
        return contact;
    }

    /**
     * Convert a Foundation Contact into a SyncItem
     * @param contact
     * @param state
     * @return SyncItem
     * @throws SyncSourceException
     */
    private SyncItem convertContactToSyncItem(Contact contact, char state) throws SyncSourceException {
        if (log.isInfoEnabled()) log.info("converting Contact with id: " + contact.getUid() + " to SyncItem");
        SyncItem syncItem;
        String content;
        syncItem = new SyncItemImpl(this, contact.getUid(), state);
        if (TYPE_VCARD.equals(itemType)) {
            content = contact2vcard(contact);
        } else {
            content = contact2sifc(contact);
        }
        syncItem.setContent(content.getBytes());
        syncItem.setType(itemType);
        return syncItem;
    }

    /**
     * Convert a Foundation Contact into VCARD content
     * @param contact
     * @return vcard content
     * @throws SyncSourceException
     */
    private String contact2vcard(Contact contact) throws SyncSourceException {
        if (log.isInfoEnabled()) log.info("Converting: Contact => VCARD");
        String vcard = null;
        try {
            ContactToVcard c2vc = new ContactToVcard(deviceTimeZone, deviceCharset);
            vcard = c2vc.convert(contact);
            if (log.isInfoEnabled()) {
                log.info("OUTPUT = {" + vcard + "}. Conversion done.");
            }
        } catch (Exception e) {
            throw new SyncSourceException("Error converting Contact to VCARD. ", e);
        }
        return vcard;
    }

    /**
     * Convert a Foundation Contact into SIFC content
     * @param contact
     * @return sifc contect
     * @throws SyncSourceException
     */
    private String contact2sifc(Contact contact) throws SyncSourceException {
        if (log.isInfoEnabled()) log.info("Converting: Contact => SIF-C");
        String xml = null;
        try {
            ContactToSIFC c2xml = new ContactToSIFC(deviceTimeZone, deviceCharset);
            xml = c2xml.convert(contact);
            if (log.isInfoEnabled()) {
                log.info("OUTPUT = {" + xml + "}. Conversion done.");
            }
        } catch (Exception e) {
            throw new SyncSourceException("Error converting Contact to SIF-C. ", e);
        }
        return xml;
    }

    /**
     * Convert VCARD content into a Foundation Contact
     * @param content
     * @return Foundation Contact
     * @throws SyncSourceException
     */
    private Contact vcard2Contact(String content) throws SyncSourceException {
        if (log.isInfoEnabled()) log.info("Converting: VCARD => Contact");
        ByteArrayInputStream is = null;
        VcardParser parser = null;
        Contact contact = null;
        try {
            if ((content == null) || (content.length() == 0)) {
                return null;
            }
            content = content.replaceAll("=\r\n", "\r\n ");
            is = new ByteArrayInputStream(content.getBytes());
            if ((content.getBytes()).length > 0) {
                parser = new VcardParser(is, this.deviceTimeZoneDescr, this.deviceCharset);
                contact = (Contact) parser.vCard();
            }
        } catch (Exception e) {
            throw new SyncSourceException("Error converting VCARD to Contact. ", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ignore) {
                }
            }
        }
        return contact;
    }

    /**
     * Convert SIFC content into a Foundation Contact
     * @param content
     * @return Foundation Contact
     * @throws SyncSourceException
     */
    private Contact sifc2Contact(String content) throws SyncSourceException {
        if (log.isInfoEnabled()) log.info("Converting: SIF-C => Contact");
        ByteArrayInputStream is = null;
        SIFCParser parser = null;
        Contact contact = null;
        try {
            if ((content == null) || (content.length() == 0)) {
                return null;
            }
            contact = new Contact();
            is = new ByteArrayInputStream(content.getBytes());
            if ((content.getBytes()).length > 0) {
                parser = new SIFCParser(is);
                contact = (Contact) parser.parse();
            }
        } catch (Exception e) {
            throw new SyncSourceException("Error converting SIF-C to Contact. ", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ignore) {
                }
            }
        }
        return contact;
    }

    /**
     * Creates array of updated items
     * @return updated items id array
     */
    private SyncItemKey[] findUpdateItemIds() {
        ArrayList<SyncItemKey> updateRows = new ArrayList<SyncItemKey>();
        SyncItemInfo localItem;
        for (int i = 0; i < serverItems.length; i++) {
            localItem = getItemById(localItems, serverItems[i].getKey().getKeyAsString());
            if (localItem != null) {
                if (!serverItems[i].getMd5Hash().equals(localItem.getMd5Hash())) {
                    if (log.isTraceEnabled()) log.trace("updated item found with key: " + serverItems[i].getKey().getKeyAsString() + " Md5: " + serverItems[i].getMd5Hash());
                    updateRows.add(serverItems[i].getKey());
                }
            }
        }
        return updateRows.toArray(new SyncItemKey[updateRows.size()]);
    }
}
