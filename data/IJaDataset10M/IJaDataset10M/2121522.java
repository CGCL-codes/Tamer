package com.funambol.google.items.model;

import com.funambol.common.pim.contact.Contact;
import com.funambol.common.pim.converter.ContactToSIFC;
import com.funambol.common.pim.converter.ContactToVcard;
import com.funambol.common.pim.converter.ConverterException;
import com.funambol.framework.logging.FunambolLogger;
import com.funambol.framework.logging.FunambolLoggerFactory;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.TimeZone;

/**
 *
 * @author sergio
 */
public class GoogleContactModel extends Contact {

    private FunambolLogger log = FunambolLoggerFactory.getLogger("funambol.google");

    private String id = "";

    private String md5Hash;

    public GoogleContactModel() {
    }

    public GoogleContactModel(String id, Contact c) {
        this.setBusinessDetail(c.getBusinessDetail());
        this.setCategories(c.getCategories());
        this.setFolder(c.getFolder());
        this.setImportance(c.getImportance());
        this.setLanguages(c.getLanguages());
        this.setMileage(c.getMileage());
        this.setName(c.getName());
        this.setNotes(c.getNotes());
        this.setPersonalDetail(c.getPersonalDetail());
        this.setRevision(c.getRevision());
        this.setSensitivity(c.getSensitivity());
        this.setSubject(c.getSubject());
        this.setTimezone(c.getTimezone());
        this.setUid(c.getUid());
        this.setXTags(c.getXTags());
        this.setId(id);
        this.createMd5Hash();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMd5Hash() {
        return md5Hash;
    }

    public void createMd5Hash() {
        try {
            String vcardObject = new ContactToVcard(TimeZone.getTimeZone("UTC"), "UTF-8").convert(this);
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(vcardObject.getBytes());
            this.md5Hash = new BigInteger(m.digest()).toString();
            if (log.isTraceEnabled()) {
                log.trace("Hash is:" + this.md5Hash);
            }
        } catch (ConverterException ex) {
            log.error("Error creating hash:" + ex.getMessage());
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            log.error("Error creating hash:" + noSuchAlgorithmException.getMessage());
        }
    }

    public String getContactAsString() {
        try {
            return "Item " + this.getId() + " -> " + new ContactToSIFC(TimeZone.getTimeZone("UTC"), "UTF-8").convert(this);
        } catch (ConverterException ex) {
            return "Error getContactAsString";
        }
    }
}
