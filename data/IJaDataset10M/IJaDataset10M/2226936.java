package org.dmp.chillout.cpd.database.hibernate.base;

import java.io.Serializable;
import org.apache.log4j.Logger;
import org.dmp.chillout.cpd.management.user.UserRegisterAgency;

/**
 * This is an object that contains data related to the tcontentinfo table. Do
 * not modify this class because it will be overwritten if the configuration
 * file related to this class is modified.
 * 
 * @hibernate.class table="tcontentinfo"
 */
public abstract class BaseTcontentinfo implements Serializable {

    protected Logger log = Logger.getLogger(BaseTcontentinfo.class);

    ;

    public static String REF = "Tcontentinfo";

    public static String PROP_CONTRIBUTOR = "contributor";

    public static String PROP_TYPE = "type";

    public static String PROP_CONTENT_NAME = "contentName";

    public static String PROP_DESCRIPTION = "description";

    public static String PROP_AUTHORS = "authors";

    public static String PROP_IMAGEPATH = "imagepath";

    public static String PROP_FILEPATH = "filepath";

    public static String PROP_DCIPATH = "dcipath";

    public static String PROP_DCFPATH = "dcfpath";

    public static String PROP_CONTENTID = "contentid";

    public static String PROP_FORMAT = "format";

    public BaseTcontentinfo() {
        initialize();
    }

    /**
	 * Constructor for primary key
	 */
    public BaseTcontentinfo(java.lang.String contentid) {
        this.setContentid(contentid);
        initialize();
    }

    /**
	 * Constructor for required fields
	 */
    public BaseTcontentinfo(java.lang.String contentid, org.dmp.chillout.cpd.database.hibernate.Tuserinfo contributor, java.lang.String contentName) {
        this.setContentid(contentid);
        this.setContributor(contributor);
        this.setContentName(contentName);
        initialize();
    }

    protected void initialize() {
    }

    private int hashCode = Integer.MIN_VALUE;

    private java.lang.String contentid;

    private java.lang.String contentName;

    private java.lang.String description;

    private java.lang.Integer type;

    private java.lang.String format;

    private java.lang.String authors;

    private String filepath;

    private String imagepath;

    private String dcipath;

    private String dcfpath;

    private org.dmp.chillout.cpd.database.hibernate.Tuserinfo contributor;

    private java.util.Set<org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo> tlicenseinfos;

    /**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="assigned" column="ContentID"
	 */
    public java.lang.String getContentid() {
        return contentid;
    }

    /**
	 * Set the unique identifier of this class
	 * 
	 * @param contentid
	 *            the new ID
	 */
    public void setContentid(java.lang.String contentid) {
        this.contentid = contentid;
        this.hashCode = Integer.MIN_VALUE;
    }

    /**
	 * Return the value associated with the column: ContentName
	 */
    public java.lang.String getContentName() {
        return contentName;
    }

    /**
	 * Set the value related to the column: ContentName
	 * 
	 * @param contentName
	 *            the ContentName value
	 */
    public void setContentName(java.lang.String contentName) {
        this.contentName = contentName;
    }

    /**
	 * Return the value associated with the column: Description
	 */
    public java.lang.String getDescription() {
        return description;
    }

    /**
	 * Set the value related to the column: Description
	 * 
	 * @param description
	 *            the Description value
	 */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    /**
	 * Return the value associated with the column: Type
	 */
    public java.lang.Integer getType() {
        return type;
    }

    /**
	 * Set the value related to the column: Type
	 * 
	 * @param type
	 *            the Type value
	 */
    public void setType(java.lang.Integer type) {
        this.type = type;
    }

    /**
	 * Return the value associated with the column: Format
	 */
    public java.lang.String getFormat() {
        return format;
    }

    /**
	 * Set the value related to the column: Format
	 * 
	 * @param format
	 *            the Format value
	 */
    public void setFormat(java.lang.String format) {
        this.format = format;
    }

    /**
	 * Return the value associated with the column: Authors
	 */
    public java.lang.String getAuthors() {
        return authors;
    }

    /**
	 * Set the value related to the column: Authors
	 * 
	 * @param authors
	 *            the Authors value
	 */
    public void setAuthors(java.lang.String authors) {
        this.authors = authors;
    }

    /**
	 * Return the value associated with the column: Contributor
	 */
    public org.dmp.chillout.cpd.database.hibernate.Tuserinfo getContributor() {
        return contributor;
    }

    /**
	 * Set the value related to the column: Contributor
	 * 
	 * @param contributor
	 *            the Contributor value
	 */
    public void setContributor(org.dmp.chillout.cpd.database.hibernate.Tuserinfo contributor) {
        this.contributor = contributor;
    }

    /**
	 * Return the value associated with the column: tlicenseinfos
	 */
    public java.util.Set<org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo> getTlicenseinfos() {
        return tlicenseinfos;
    }

    /**
	 * Set the value related to the column: tlicenseinfos
	 * 
	 * @param tlicenseinfos
	 *            the tlicenseinfos value
	 */
    public void setTlicenseinfos(java.util.Set<org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo> tlicenseinfos) {
        this.tlicenseinfos = tlicenseinfos;
    }

    public void addTotlicenseinfos(org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo tlicenseinfo) {
        if (null == getTlicenseinfos()) setTlicenseinfos(new java.util.TreeSet<org.dmp.chillout.cpd.database.hibernate.Tlicenseinfo>());
        getTlicenseinfos().add(tlicenseinfo);
    }

    public boolean equals(Object obj) {
        if (null == obj) return false;
        if (!(obj instanceof org.dmp.chillout.cpd.database.hibernate.Tcontentinfo)) return false; else {
            org.dmp.chillout.cpd.database.hibernate.Tcontentinfo tcontentinfo = (org.dmp.chillout.cpd.database.hibernate.Tcontentinfo) obj;
            if (null == this.getContentid() || null == tcontentinfo.getContentid()) return false; else return (this.getContentid().equals(tcontentinfo.getContentid()));
        }
    }

    public int hashCode() {
        if (Integer.MIN_VALUE == this.hashCode) {
            if (null == this.getContentid()) return super.hashCode(); else {
                String hashStr = this.getClass().getName() + ":" + this.getContentid().hashCode();
                this.hashCode = hashStr.hashCode();
            }
        }
        return this.hashCode;
    }

    public String toString() {
        return super.toString();
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getDcipath() {
        return dcipath;
    }

    public void setDcipath(String dcipath) {
        this.dcipath = dcipath;
    }

    /**
	 * @return Returns the dcfpath.
	 */
    public String getDcfpath() {
        return dcfpath;
    }

    /**
	 * @param dcfpath The dcfpath to set.
	 */
    public void setDcfpath(String dcfpath) {
        this.dcfpath = dcfpath;
    }
}
