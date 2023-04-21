package org.oclc.da.informationobject;

import java.io.Serializable;

/**
 * A class that defines the set of valid <code>InformationObject</code> types
 * available in the archive.
 * 
 * @author JCG
 *  
 */
public class InformationObjectType implements Serializable {

    private static final long serialVersionUID = 200509061629L;

    /** This type maps to an instance of <code>ContentObject</code> */
    public static final InformationObjectType CONTENT_OBJECT = new InformationObjectType("CONTENT_OBJECT");

    /**
     * This type maps to an instance of a <code>PDI</code> for a
     * <code>ContentObject</code>
     */
    public static final InformationObjectType CONTENT_PDI = new InformationObjectType("CONTENT_PDI");

    /**
     * This type maps to an instance of a raw image annotation. A raw image
     * annotation will contain all of the specific fields obtained from each of
     * the image formats, without any normalization. All fields will be
     * represented and named as they appear in the specific image format
     * description.
     */
    public static final InformationObjectType FILE_ANNOTATION_STATUS = new InformationObjectType("FILE_ANNOTATION_STATUS");

    /**
     * This type is an invalid type. It is mainly used for testing.
     */
    public static final InformationObjectType INVALID = new InformationObjectType("INVALID");

    /**
     * This type maps to an instance of a normalized image annotation. This type
     * of annotation will be a full set of all known image annotation metadata.
     * Any common elements between various image formats will be mapped into
     * common fields where possible.
     */
    public static final InformationObjectType NORMALIZED_IMAGE_ANNOTATION = new InformationObjectType("NORMALIZED_IMAGE_ANNOTATION");

    /**
     * This type maps to an instance of a primary text annotation. A primary
     * text annotation will contain all currently available fields obtained from
     * each of the text formats, without any normalization. All fields will be
     * represented and named as they appear in the specific text format
     * description.
     */
    public static final InformationObjectType PRIMARY_TEXT_ANNOTATION = new InformationObjectType("PRIMARY_TEXT_ANNOTATION");

    /**
     * This type maps to an instance of a primary PDF annotation. A primary PDF
     * annotation will contain all currently available fields obtained from each
     * of the PDF formats, without any normalization. All fields will be
     * represented and named as they appear in the specific PDF format
     * description.
     */
    public static final InformationObjectType PRIMARY_PDF_ANNOTATION = new InformationObjectType("PRIMARY_PDF_ANNOTATION");

    /**
     * This type maps to an instance of a primary image annotation. A primary
     * image annotation will contain all currently available fields obtained
     * from each of the image formats, without any normalization. All fields
     * will be represented and named as they appear in the specific image format
     * description.
     */
    public static final InformationObjectType PRIMARY_IMAGE_ANNOTATION = new InformationObjectType("PRIMARY_IMAGE_ANNOTATION");

    /**
     * This type maps to an instance of a raw image annotation. A raw image
     * annotation is one whose format is currently unknown to the archive. It
     * could be a non-standard or user supplied metadata file.
     */
    public static final InformationObjectType RAW_IMAGE_ANNOTATION = new InformationObjectType("RAW_IMAGE_ANNOTATION");

    /**
     * This type maps to an instance of the current standard image annotation. A
     * standard image annotation contains all attributes needed to represent the
     * current image metadata standard being implemented by the archive.
     * Currently this standard is MIX.
     */
    public static final InformationObjectType STANDARD_IMAGE_ANNOTATION = new InformationObjectType("STANDARD_IMAGE_ANNOTATION");

    /**
     * This type is a query object. It will contain search clauses, sorts,
     * record set size and any other state needed to perform queries.
     */
    public static final InformationObjectType QUERY = new InformationObjectType("QUERY");

    /**
     * This type is the entry point object for NDIIPP. 
     */
    public static final InformationObjectType ENTRY_POINT = new InformationObjectType("ENTRY_POINT");

    /**
     * This type is the domain object for NDIIPP. 
     */
    public static final InformationObjectType DOMAIN = new InformationObjectType("DOMAIN");

    /**
     * This type is the domain spider options object for NDIIPP.
     * This is an OLD type maintained for conversion purposes. 
     */
    public static final InformationObjectType DOMAIN_SPIDER_OPTIONS = new InformationObjectType("DOMAIN_SPIDER_OPTIONS");

    /**
     * This type is the spider settings object for NDIIPP. 
     */
    public static final InformationObjectType SPIDER_SETTINGS = new InformationObjectType("SPIDER_SETTINGS");

    /**
     * This type is the domain spider status object for NDIIPP. 
     */
    public static final InformationObjectType DOMAIN_SPIDER_STATUS = new InformationObjectType("DOMAIN_SPIDER_STATUS");

    /**
     * This type is the relationship object for NDIIPP. 
     */
    public static final InformationObjectType RELATIONSHIP = new InformationObjectType("RELATIONSHIP");

    /**
     * This type is the event object for NDIIPP. 
     */
    public static final InformationObjectType EVENT = new InformationObjectType("EVENT");

    /**
     * This type is the event object for NDIIPP. 
     */
    public static final InformationObjectType ENTITY = new InformationObjectType("ENTITY");

    /**
     * This type is the analysisdef object for NDIIPP. 
     */
    public static final InformationObjectType ANALYSIS_DEF = new InformationObjectType("ANALYSIS_DEF");

    /**
     * This type is the rundef object for NDIIPP. 
     */
    public static final InformationObjectType RUN_DEF = new InformationObjectType("RUN_DEF");

    /**
     * This type is the session object for NDIIPP. 
     */
    public static final InformationObjectType SESSION = new InformationObjectType("SESSION");

    /**
     * This type is the useraccount object for NDIIPP. 
     */
    public static final InformationObjectType USERACCOUNT = new InformationObjectType("USERACCOUNT");

    /**
     * This type is the institution object for NDIIPP. 
     */
    public static final InformationObjectType INSTITUTION = new InformationObjectType("INSTITUTION");

    /**
     * This type is the subject heading object for NDIIPP. 
     */
    public static final InformationObjectType SUBJECT_HEADING = new InformationObjectType("SUBJECT_HEADING");

    /**
     * This type is the contact information for NDIIPP. 
     */
    public static final InformationObjectType CONTACT = new InformationObjectType("CONTACT");

    /** This type is a lock for an object in the system. */
    public static final InformationObjectType LOCK = new InformationObjectType("LOCK");

    /** This type is the result for NDIIPP. */
    public static final InformationObjectType RESULT = new InformationObjectType("RESULT");

    /** This type is the exceptionobject for NDIIPP. */
    public static final InformationObjectType EXCEPTION_OBJECT = new InformationObjectType("EXCEPTION_OBJECT");

    /** This type is an object representing a report. */
    public static final InformationObjectType REPORT = new InformationObjectType("REPORT");

    /** This type is an object representing a harvest. */
    public static final InformationObjectType HARVEST = new InformationObjectType("HARVEST");

    /** This type is an object representing a package info. */
    public static final InformationObjectType PACKAGE_INFO = new InformationObjectType("PACKAGE_INFO");

    /** This type is an object representing series. */
    public static final InformationObjectType SERIES = new InformationObjectType("SERIES");

    /** This type is an object representing Dublin Core Metadata. */
    public static final InformationObjectType DC_METADATA = new InformationObjectType("DC_METADATA");

    /** This type is an object representing a Dublin Core Metadata element. */
    public static final InformationObjectType DC_ELEMENT = new InformationObjectType("DC_ELEMENT");

    /** This type is an object representing a schedule request. */
    public static final InformationObjectType SCHEDULE = new InformationObjectType("SCHEDULE");

    /** This type is an object representing a task request. */
    public static final InformationObjectType TASK = new InformationObjectType("TASK");

    /** This type is an object representing a resource request. */
    public static final InformationObjectType RESOURCE_REQUEST = new InformationObjectType("RESOURCE_REQUEST");

    /** This type is an object representing a package info. */
    public static final InformationObjectType PACKAGE_CONTAINER = new InformationObjectType("PACKAGE_CONTAINER");

    /** This type is an object representing series. */
    public static final InformationObjectType SERIES_HISTORY = new InformationObjectType("SERIES_HISTORY");

    /** This type is an object representing websites. */
    public static final InformationObjectType WEBSITE = new InformationObjectType("WEBSITE");

    /** This type is an object representing an entry in a package log. */
    public static final InformationObjectType PACKAGE_LOG = new InformationObjectType("PACKAGE_LOG");

    /** This type is an object represents a spider process.  It is not persisted to the database. */
    public static final InformationObjectType SPIDER_PROCESS = new InformationObjectType("SPIDER_PROCESS");

    /**
     * Construct an instance of an <code>InformationObjectType</code> from a
     * string representation. This is the inverse of <code>toString</code>.
     * 
     * @param type
     *            A string that uniquely identifies the type of an
     *            <code>InformationObject</code>
     * @return return the information object type
     */
    public static InformationObjectType parseString(String type) {
        return new InformationObjectType(type);
    }

    private String type = null;

    /**
     * Construct an instance of an <code>InformationObjectType</code>
     * 
     * @param type
     *            A string that uniquely identifies the type of an
     *            <code>InformationObject</code>
     */
    private InformationObjectType(String type) {
        this.type = type;
    }

    /**
     * (non-Javadoc)
     * @see  Serializable#equals(Object)
     */
    public boolean equals(Object object) {
        if ((object != null) && (object instanceof InformationObjectType)) {
            InformationObjectType otherType = (InformationObjectType) object;
            return type.equals(otherType.type);
        }
        return false;
    }

    /**
     * (non-Javadoc)
     * @see  Serializable#hashCode()
     */
    public int hashCode() {
        return type.hashCode();
    }

    /**
     * (non-Javadoc)
     * @see  Serializable#toString()
     */
    public String toString() {
        return type;
    }
}
