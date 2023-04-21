package org.pachyderm.okiosid;

import java.util.Date;
import java.util.StringTokenizer;
import org.pachyderm.apollo.core.UTType;
import org.pachyderm.apollo.data.CXManagedObject;
import org.pachyderm.apollo.data.CXManagedObjectMetadata;
import org.pachyderm.apollo.data.CXObjectStore;
import org.pachyderm.apollo.data.CXObjectStoreCoordinator;
import org.pachyderm.apollo.data.MD;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSPathUtilities;
import com.webobjects.foundation.NSSelector;
import com.webobjects.foundation.NSSet;
import com.webobjects.foundation.NSTimestamp;
import com.webobjects.foundation.NSURL;

/**
 * 
 * @author jarcher
 *
 */
public class OKIOSIDManagedObjectMetadata implements NSKeyValueCoding, CXManagedObjectMetadata {

    private CXManagedObject _managedObjectRef = null;

    private NSURL _OSIDStoreURL = null;

    private String _id = null;

    private NSMutableDictionary _cachedAttributes = null;

    private static final NSSet _InspectableAttributes = new NSSet(new Object[] { "ContentType", "contentType", "contributor", "dcmiContributor", "dcmiContributorFormatted", "Coverage", "coverage", "dcmiCoverage", "dcmiCoverageFormatted", "Creator", "creator", "dcmiCreator", "dcmiCreatorFormatted", "Authors", "authors", "date", "dcmiDate", "dcmiDateSubmitted", "dcmiDateModified", "dcmiDateFormatted", "AttributeChangeDate", "attributeChangeDate", "DateAdded", "dateAdded", "DateModified", "dateModified", "Description", "description", "dcmiDescription", "dcmiDescriptionFormatted", "format", "dcmiFormat", "dcmiFormatMime", "dcmiFormatFormatted", "resourceIdentifier", "dcmiIdentifier", "dcmiIdentifierFormatted", "Language", "language", "dcmiLanguage", "dcmiLanguageFormatted", "Publisher", "publisher", "dcmiPublisher", "dcmiPublisherFormatted", "Relation", "relation", "dcmiRelation", "dcmiRelationFormatted", "Rights", "rights", "dcmiRights", "dcmiRightsCopyright", "dcmiRightsAccesRights", "dcmiRightsFormatted", "Source", "source", "dcmiSource", "dcmiSourceFormatted", "Keywords", "keywords", "dcmiSubject", "dcmiSubjectKeyword", "dcmiSubjectFormatted", "Title", "title", "dcmiTitle", "dcmiTitleMain", "dcmiTitleFormatted", "titleAlternate", "DisplayName", "displayName", "Type", "type", "dcmiType", "dcmiTypeType", "dcmiTypeDCMIType", "dcmiTypeFormatted", "Tombstone", "tombstone", "tombstoneCreator", "tombstoneTitle", "tombstoneDate", "tombstoneMedium", "tombstoneDimensions", "tombstoneCredits", "tombstoneCopyright", "tombstoneFootnote", "assetURL", "assetLargeImageURL", "assetMediumImage", "assetThumbnailURL", "URL", "location", "assetIdentifier", "ResourceManagerID", "resourceManagerID", "accessibilityCompliance", "accessibilityComplianceFormatted", "accessibilityAltAssetRepresentation", "accessibilityAltText", "accessibilityLongDesc", "accessibilityAudioTranscript" });

    private static final NSSet _MutableAttributes = null;

    /**
	 * 
	 */
    public OKIOSIDManagedObjectMetadata() {
        super();
    }

    /**
	 * 
	 * @param mo
	 */
    public OKIOSIDManagedObjectMetadata(CXManagedObject mo) {
        super();
        _managedObjectRef = mo;
        _OSIDStoreURL = ((OKIOSIDManagedObject) mo).objectStoreURL();
    }

    /**
	 * 
	 */
    public CXManagedObject managedObject() {
        if (_managedObjectRef == null) {
            _managedObjectRef = OKIOSIDManagedObject.objectWithIdentifierAndObjectStoreURL(identifier(), _OSIDStoreURL);
            _managedObjectRef.attachMetadata(this);
        }
        return _managedObjectRef;
    }

    /**
	 * 
	 */
    private void _initMetadata() {
        String id = identifier();
        CXManagedObject mo = managedObject();
        if ((id != null) && (mo == null)) {
            mo = OKIOSIDManagedObject.objectWithIdentifierAndObjectStoreURL(id, objectStoreURL());
        }
        if (mo != null) {
            PachydermOSIDAssetMetadataPopulator localPopulator = ((OKIOSIDManagedObject) mo).populator();
            NSDictionary dict = _metadataRecordForPopulator(localPopulator);
            if (dict != null) {
                setCachedAttributes(dict);
            }
        }
    }

    /**
	 * 
	 * @return
	 */
    public CXObjectStore objectStore() {
        if (objectStoreURL() != null) {
            return CXObjectStoreCoordinator.getDefaultCoordinator().objectStoreForURL(objectStoreURL());
        }
        return null;
    }

    /**
	 * 
	 * @param dict
	 */
    public void setCachedAttributes(NSDictionary dict) {
        _cachedAttributes = dict.mutableClone();
    }

    /**
	 * 
	 * @return
	 */
    public NSURL objectStoreURL() {
        return _OSIDStoreURL;
    }

    /**
	 * 
	 * @param OSIDStoreURL
	 */
    public void setObjectStoreURL(NSURL OSIDStoreURL) {
        _OSIDStoreURL = OSIDStoreURL;
    }

    /**
	 * Implementations should return the URL of the object store that this metadata originated from.
	 *
	 * @see CXObjectStore#getUrl()
	 */
    public NSURL storeURL() {
        return _OSIDStoreURL;
    }

    /**
	 * Returns the unique identifier for this metadata.
	 */
    public String identifier() {
        if (_managedObjectRef != null) {
            _id = _managedObjectRef.identifier();
        }
        return _id;
    }

    /**
	 * Returns String value of DC:Identifier
	 * 
	 * @return String value of DC:Identifier
	 * @see http://dublincore.org/documents/usageguide/elements.shtml#identifier
	 */
    public String resourceIdentifier() {
        return (String) _cachedValueForKey("resourceIdentifier");
    }

    /**
	 * Returns an Integer object corresponding to the value of 
	 * DCTERMS:Valid attribute of the metadata record for the asset.
	 * 
	 * @return Integer object corresponding to DCTERMS:Valid attribute of metadata record for asset
	 */
    public Integer isDeleted() {
        return new Integer(0);
    }

    /**
	 * Returns a String corresponding to the value of 
	 * DCTERMS:Valid attribute of the metadata record for the asset.
	 * 
	 * @return String corresponding to DCTERMS:Valid attribute of metadata record for asset
	 * @see http://dublincore.org/documents/usageguide/qualifiers.shtml#valid
	 */
    public String valid() {
        return null;
    }

    /**
	 * Returns a set containing the attribute that the receiver contains. Implementations may return a null array 
	 * if this cannot be determined ahead of time or the receiver would like to receive all attribute requests whether 
	 * or not it is capable of handling them. However, an actual array of keys should be 
	 * returned whenever possible so that the containing managed object can accurately determine what attributes are 
	 * available for an object.
	 */
    public NSSet inspectableAttributes() {
        return _InspectableAttributes;
    }

    /**
	 * Returns a set containing the attributes that can be modified.
	 */
    public NSSet mutableAttributes() {
        return _MutableAttributes;
    }

    /**
	 * Returns a value for the requested attribute.
	 */
    public Object valueForAttribute(String attribute) {
        return _InspectableAttributes.containsObject(attribute) ? valueForKey(attribute) : null;
    }

    /**
	 * 
	 */
    public void takeValueForKey(Object o, String s) {
        if (_MutableAttributes.containsObject(s)) {
            NSSelector sel = new NSSelector("set" + s);
            try {
                sel.invoke(this);
            } catch (Exception e) {
            }
        }
    }

    /**
	 * 
	 */
    public Object valueForKey(String s) {
        NSSelector sel = new NSSelector(s);
        try {
            return sel.invoke(this);
        } catch (Exception e) {
        }
        return null;
    }

    /**
	 * 
	 * @param s
	 * @return
	 */
    private Object _cachedValueForKey(String s) {
        if (_cachedAttributes == null) {
            _initMetadata();
        }
        return _cachedAttributes.valueForKey(s);
    }

    /**
	 * Sets the value of the attribute to the specified value.
	 */
    public void setValueForAttribute(Object value, String attribute) {
        takeValueForKey(value, attribute);
    }

    /**
	 * Returns whether the receiver has a native binary representation of its data. For example, if this object represents a LOM record, then it should be able to provide the information as XML data.
	 */
    public boolean hasNativeDataRepresentation() {
        return false;
    }

    /**
	 * Returns a Uniform Type Identifier (UTI) for the type of data in the native representation. If no UTI is 
	 * available or known, implementations should use <code>public.data</code>.
	 */
    public String nativeDataRepresentationType() {
        return null;
    }

    /**
	 * Returns an NSData object containing the native data representation of this object's metadata.
	 */
    public NSData nativeDataRepresentation() {
        return null;
    }

    /**
	 * Returns String value corresponding to DCTEMS:AccessRights. Currently this defaults to returning the access 
	 * rights integer code. Automatically returns '0' for 'public'; the method assumes all items coming back from 
	 * this search from OSID for this user are public (accessible to user). However, it is preferrable to have this
	 * method return a real value if it exists in the metadata.
	 * @return String literal value '0' corresponding to public access.
	 */
    public String accessRights() {
        return "0";
    }

    /**
	 * Returns a String containing the value for DCTERMS:Alternative (Alternative Title)
	 * 
	 * @return String value of DCTERMS:Alternative
	 * @see http://dublincore.org/documents/usageguide/elements.shtml#alternative
	 */
    public String alternative() {
        return (String) _cachedValueForKey("titleAlternative");
    }

    /**
	 * Returns a String containing the value for the Alternative Text accessibility data
	 * @return String alt text value for asset
	 */
    public String altText() {
        return (String) _cachedValueForKey("accessibilityAltText");
    }

    /**
	 * Returns an NSTimestamp object containing the value acquired from dateModified()
	 * @return NSTimestamp value of dateModified()
	 */
    public NSTimestamp attributeChangeDate() {
        return dateModified();
    }

    /**
	 * Returns a String corresponding to the value for DTERMS:Audience. 
	 * @return String value of DCTERMS:Audience
	 */
    public String audience() {
        return null;
    }

    /**
	 * Returns an NSArray object filled by the values acquired from resourceManagerID()
	 * @return NSArray value of resourceManagerID()
	 */
    public NSArray authors() {
        return new NSArray(resourceManagerID());
    }

    /**
	 * Returns String value of UTI type, either pre-cached from retrieval, or if missing, tries to discern from file extension.
	 * @return String UTI type value
	 */
    public String contentType() {
        if (_cachedValueForKey(MD.ContentType) != null) {
            return (String) _cachedValueForKey(MD.ContentType);
        }
        String type = type();
        String uti = UTType.preferredIdentifierForTag(UTType.MIMETypeTagClass, type, null);
        if (uti == null) {
            String file = location();
            if (file != null) {
                uti = UTType.preferredIdentifierForTag(UTType.FilenameExtensionTagClass, (NSPathUtilities.pathExtension(file)).toLowerCase(), null);
            }
            if (uti == null) {
                uti = UTType.Item;
            }
        }
        _cachedAttributes.takeValueForKey(uti, MD.ContentType);
        return uti;
    }

    /**
	 * Returns a String value corresponding to the DC:Contributor 
	 * attribute of the metadata record for the asset.
	 * 
	 * @return DC:Contributor attribute of metadata record for asset.
	 * @see http://dublincore.org/documents/usageguide/elements.shtml#contributor
	 */
    public String contributor() {
        return (String) _cachedValueForKey("contributor");
    }

    /**
	 * Return String value of DCTERMS:DateCopyright for asset
	 * @return String value of DCTERMS:DateCopyright
	 */
    public String copyright() {
        return (String) _cachedValueForKey("copyright");
    }

    /**
	 * Returns String value of DC:Coverage for asset
	 * @return String value of DC:Coverage
	 */
    public String coverage() {
        return (String) _cachedValueForKey("coverage");
    }

    /**
	 * Returns the value of DCTERMS:Created attribute of the metadata record for the asset
	 * 
	 * @return String value of DCTERMS:Created attribute for asset
	 * @see http://dublincore.org/documents/usageguide/qualifiers.shtml#created
	 */
    public String created() {
        return null;
    }

    /**
	 * Returns the value of DC:Creator attribute of the metadata
	 * record for the asset.
	 * 
	 * @return DC:Creator attribute of metadata record for asset
	 * @see http://dublincore.org/documents/usageguide/elements.shtml#creator
	 */
    public String creator() {
        return (String) _cachedValueForKey("creator");
    }

    /**
	 * Returns the value of DC:Date attribute of the metadata record
	 * for the asset.
	 * 
	 * @return DC:Date attribute of metadata record for asset
	 * @see http://dublincore.org/documents/usageguide/elements.shtml#date
	 */
    public String date() {
        return ((Date) _cachedValueForKey("date")).toString();
    }

    /**
	 * Returns string value of DCTERMS:DateAccepted attribute of the metadata record for the asset.
	 * 
	 * @return String value of DCTERMS:DateAccepted attribute
	 * @see http://dublincore.org/documents/usageguide/qualifiers.shtml#dateAccepted
	 */
    public String dateAccepted() {
        return null;
    }

    /**
	 * Returns an NSTimestamp of the value of DCTERMS:DateSubmitted 
	 * attribute of the metadata record for the asset.
	 * 
	 * @return NSTimestamp of DCTERMS:DateSubmitted attribute of metadata record for asset
	 */
    public NSTimestamp dateAdded() {
        return new NSTimestamp((Date) _cachedValueForKey("date_added"));
    }

    /**
	 * Returns a String value of DCTERMS:DateCopyrighted attribute of the metadata record for the asset.
	 * 
	 * @return String value of DCTERMS:DateCopyrighted attribute
	 * @see http://dublincore.org/documents/usageguide/qualifiers.shtml#dateCopyrighted
	 */
    public String dateCopyrighted() {
        return copyright();
    }

    /**
	 * Returns a String value of DCTERMS:Issued attribute of the metadata record for the asset.
	 * 
	 * @return String value of DCTERMS:Issued attribute
	 * @see http://dublincore.org/documents/usageguide/qualifiers.shtml#issued
	 */
    public String dateIssued() {
        return null;
    }

    /**
	 * Returns an NSTimestamp of the value of DCTERMS:DateModified attribute of the
	 * metadata record for the asset.
	 * 
	 * @return NSTimestamp of DCTERMS:DateModified attribute of metadata record for asset
	 * @see http://dublincore.org/documents/usageguide/qualifiers.shtml#modified
	 */
    public NSTimestamp dateModified() {
        return new NSTimestamp((Date) _cachedValueForKey("date_modified"));
    }

    /**
	 * Returns an NSTimestamp of the value of DCTERMS:DateSubmitted 
	 * attribute of the metadata record for the asset.
	 * 
	 * @return NSTimestamp of DCTERMS:DateSubmitted attribute of metadata record for asset
	 * @see http://dublincore.org/documents/usageguide/qualifiers.shtml#dateSubmitted
	 */
    public NSTimestamp dateSubmitted() {
        return dateAdded();
    }

    /**
	 * Returns the value of DC:Description attribute of the metadata
	 * record for the asset.
	 * 
	 * @return DC:Description attrubite of metadata record for asset
	 * @see http://dublincore.org/documents/usageguide/elements.shtml#description
	 */
    public String description() {
        return (String) _cachedValueForKey("Description");
    }

    /**
	 * Returns the value of DC:Title attribute of the metadata
	 * record for the asset.
	 * 
	 * @return DC:Title attribute of metadata record for asset
	 * @see http://dublincore.org/documents/usageguide/elements.shtml#title
	 */
    public String displayName() {
        return (String) _cachedValueForKey(MD.Title);
    }

    /**
	 * Returns a String value of DC:Format attribute of the metadata record for the asset.
	 * 
	 * @return String DC:Format attribute of metadata record for asset.
	 */
    public String format() {
        return (String) _cachedValueForKey("format");
    }

    /**
	 * Returns a string value of DCTERMS:InstructionalMethod attribute of the metadata record
	 * for the asset. 
	 * 
	 * @return String DCTERMS:InstructionalMethod attribute of metadata record for asset.
	 */
    public String instructionalMethod() {
        return null;
    }

    /**
	 * 
	 * @return
	 */
    public NSArray keywords() {
        String keywords = (String) _cachedValueForKey("keywords");
        if (keywords == null) {
            return null;
        }
        StringTokenizer tokenizer = new StringTokenizer(keywords, ";,");
        int count = tokenizer.countTokens();
        if (count < 2) {
            tokenizer = new StringTokenizer(keywords);
            count = tokenizer.countTokens();
        }
        NSMutableArray tokens = new NSMutableArray(tokenizer.countTokens());
        while (tokenizer.hasMoreTokens()) {
            tokens.addObject(tokenizer.nextToken().trim());
        }
        return tokens;
    }

    /**
	 * Returns String value of DCTERMS:EducationLevel attribute
	 * 
	 * @return String value of DCTERMS:EducationLevel attribute
	 */
    public String educationLevel() {
        return null;
    }

    /**
	 * Returns String value of DCTERMS:Extent attribute
	 * 
	 * @return String value of DCTERMS:Extent attribute
	 * @see http://dublincore.org/documents/usageguide/qualifiers.shtml#extent
	 */
    public String extent() {
        return null;
    }

    /**
	 * Returns String value of DCTERMS:Mediator attribute
	 * 
	 * @return String value of DCTERMS:Mediator attribute
	 */
    public String mediator() {
        return null;
    }

    /**
	 * Returns String value of DCTERMS:Medium attribute
	 * 
	 * @return String value of DCTERMS:Medium attribute
	 * @see http://dublincore.org/documents/usageguide/qualifiers.shtml#medium
	 */
    public String medium() {
        return null;
    }

    /**
	 * Returns String value of DC:Langauge attribute
	 * 
	 * @return String value of DC:Language attribute
	 */
    public String language() {
        return (String) _cachedValueForKey("language");
    }

    /**
	 * Returns the String value of DCTERMS:License attribute of the metadata
	 * record for the asset.
	 * 
	 * @return String DCTERMS:License attribute of metadata record for asset
	 */
    public String license() {
        return null;
    }

    /**
	 * Returns String value of assetURL for asset
	 * 
	 * @return String value of assetURL for asset
	 */
    public String location() {
        String location = null;
        location = (String) _cachedValueForKey("assetURL");
        return location;
    }

    /**
	 * Returns String value of accessibility long description
	 * 
	 * @return String value of accessibility long description
	 */
    public String longDesc() {
        return null;
    }

    /**
	 * Returns the value of DCTERMS:RightsHolder attribute of the metadata
	 * record for the asset.
	 * 
	 * @return DCTERMS:RightsHolder attribute of metadata record for asset
	 */
    public String owner() {
        return (String) _cachedValueForKey("owner");
    }

    /**
	 * Returns the value of DCTERMS:RightsHolder attribute of the metadata
	 * record for the asset.
	 * 
	 * @return DCTERMS:RightsHolder attribute of metadata record for asset
	 */
    public String rightsHolder() {
        return null;
    }

    /**
	 * Returns the Integer value of DCTERMS:AccessRights attribute of the metadata record
	 * for the asset.
	 * 
	 * @return Integer value of DCTERMS:AccessRights attribute of the metadata record
	 * for the asset.
	 */
    public Integer permissions() {
        return new Integer(0);
    }

    /**
	 * Returns String value of DCTERMS:Provenance attribute 
	 * 
	 * @return String value of DCTERMS:Provenance attribute
	 */
    public String provenance() {
        return null;
    }

    /**
	 * Returns the value of DC:Publisher attribute of the metadata 
	 * record for the asset.
	 * 
	 * @return DC:Publisher attribute of metadata record for asset
	 * @see http://dublincore.org/documents/usageguide/elements.shtml#publisher
	 */
    public String publisher() {
        return (String) _cachedValueForKey("publisher");
    }

    /**
	 * Returns String value of DC:Relation attribute
	 * 
	 * @return String value of DC:Relation attribute
	 */
    public String relation() {
        return (String) _cachedValueForKey("relation");
    }

    /**
	 * Returns the String value of DC:Contributor-type data (what is this?)
	 * 
	 * @return String value of DC:Contributor attribute-type data
	 */
    public String resourceManagerID() {
        return (String) _cachedValueForKey("resourceManagerID");
    }

    /**
	 * Returns the value of DC:Rights attribute of the metadata
	 * record for the asset.
	 * 
	 * @return DC:Rights attribute of metadata record for asset
	 */
    public String rights() {
        return (String) _cachedValueForKey("rights");
    }

    /**
	 * Returns the value of DC:Source attribute of the metadata
	 * record for the asset.
	 * 
	 * @return DC:Source attribute of metadata record for asset
	 * @see http://dublincore.org/documents/usageguide/elements.shtml#source
	 */
    public String source() {
        return (String) _cachedValueForKey("source");
    }

    /**
	 * Returns the String value of DCTERMS:Spatial attribute of the metadata record
	 * for the asset.
	 * 
	 * @return String value of DCTERMS:Spatial attribute of the metadata record
	 */
    public String spatial() {
        return null;
    }

    /**
	 * Returns the value of DC:Subject attribute of the metadata
	 * record for the asset.
	 * 
	 * @return DC:Subject attribute of metadata record for asset
	 * @see http://dublincore.org/documents/usageguide/elements.shtml#subject
	 */
    public String subject() {
        return (String) _cachedValueForKey("subject");
    }

    /**
	 * Returns String value of accessibility synchronized caption attribute
	 * 
	 * @return String value of accessibility synchronized caption attribute
	 */
    public String syncCaption() {
        return null;
    }

    /**
	 * Returns String value of DCTERMS:Temporal attribute
	 * 
	 * @return String value of DCTERMS:Temporal attribute
	 */
    public String temporal() {
        return null;
    }

    /**
	 * Returns the value of DC:Title attribute of the metadata
	 * record for the asset.
	 * 
	 * @return DC:Title attribute of metadata record for asset
	 * @see http://dublincore.org/documents/usageguide/elements.shtml#title
	 */
    public String title() {
        return (String) _cachedValueForKey(MD.Title);
    }

    /**
	 * Returns the value of DC:Title attribute of the metadata
	 * record for the asset.
	 * 
	 * @return DC:Title attribute of metadata record for asset
	 * @see http://dublincore.org/documents/usageguide/elements.shtml#title
	 */
    public String Title() {
        return (String) _cachedValueForKey(MD.Title);
    }

    /**
	 * Returns String of media label (formerly tombstone) to be included as caption for asset
	 * 
	 * @return String media label value
	 */
    public String mediaLabel() {
        return (String) _cachedValueForKey("tombstone");
    }

    /**
	 * Returns the String value of accessibility audio transcript attribute
	 * 
	 * @return String value of accessibility audio transcript attribute
	 */
    public String transcript() {
        return null;
    }

    /**
	 * Returns String of media label (formerly tombstone) to be included as caption for asset
	 * 
	 * @return String media label value
	 */
    public String tombstone() {
        return mediaLabel();
    }

    /**
	 * 
	 * @return
	 */
    public String tombstoneCreator() {
        return (String) _cachedValueForKey("tombstoneCreator");
    }

    /**
	 * 
	 * @return
	 */
    public String tombstoneTitle() {
        return (String) _cachedValueForKey("tombstoneTitle");
    }

    /**
	 * 
	 * @return
	 */
    public String tombstoneDate() {
        return (String) _cachedValueForKey("tombstoneDate");
    }

    /**
	 * 
	 * @return
	 */
    public String tombstoneMedium() {
        return (String) _cachedValueForKey("tombstoneMedium");
    }

    /**
	 * 
	 * @return
	 */
    public String tombstoneDimensions() {
        return (String) _cachedValueForKey("tombstoneDimensions");
    }

    /**
	 * 
	 * @return
	 */
    public String tombstoneCredits() {
        return (String) _cachedValueForKey("tombstoneCredits");
    }

    /**
	 * 
	 * @return
	 */
    public String tombstoneCopyright() {
        return (String) _cachedValueForKey("tombstoneCopyright");
    }

    /**
	 * 
	 * @return
	 */
    public String tombstoneFootnote() {
        return (String) _cachedValueForKey("tombstoneFootnote");
    }

    /**
	 * Returns the value of DC:Type attribute of the metadata
	 * record for the asset.
	 * 
	 * @return DC:Type attribute of metadata record for asset
	 */
    public String type() {
        return (String) _cachedValueForKey("type");
    }

    /**
	 * 
	 * @param localPopulator
	 * @return
	 */
    private NSDictionary _metadataRecordForPopulator(PachydermOSIDAssetMetadataPopulator localPopulator) {
        NSMutableDictionary metadata = new NSMutableDictionary();
        metadata.takeValueForKey(localPopulator.dcmiResourceTypeDCMIType(), "AssetType");
        metadata.takeValueForKey(localPopulator.dcmiResourceDescriptionFormatted(), "Description");
        metadata.takeValueForKey(localPopulator.dcmiResourceDescriptionFormatted(), "description");
        metadata.takeValueForKey(localPopulator.dcmiResourceTitleMain(), MD.Title);
        metadata.takeValueForKey(localPopulator.dcmiResourceTitleMain(), "Title");
        metadata.takeValueForKey(localPopulator.dcmiResourceTitleMain(), "title");
        metadata.takeValueForKey(localPopulator.dcmiResourceTitleMain(), "DisplayName");
        metadata.takeValueForKey(localPopulator.dcmiResourceTitleMain(), "displayName");
        metadata.takeValueForKey(localPopulator.assetIdentifier(), "resourceIdentifier");
        metadata.takeValueForKey(localPopulator.dcmiResourceDateModified(), "AttributeChangeDate");
        metadata.takeValueForKey(localPopulator.dcmiResourceDateModified(), "attributeChangeDate");
        metadata.takeValueForKey(localPopulator.dcmiResourceCreator(), "Authors");
        metadata.takeValueForKey(localPopulator.dcmiResourceCreator(), "authors");
        metadata.takeValueForKey(localPopulator.dcmiResourceCoverageFormatted(), "Coverage");
        metadata.takeValueForKey(localPopulator.dcmiResourceCoverageFormatted(), "coverage");
        metadata.takeValueForKey(localPopulator.dcmiResourceFormatMIME(), "ContentType");
        metadata.takeValueForKey(localPopulator.dcmiResourceFormatMIME(), "contentType");
        metadata.takeValueForKey(localPopulator.dcmiResourceSubjectKeyword(), "Keywords");
        metadata.takeValueForKey(localPopulator.dcmiResourceSubjectKeyword(), "keywords");
        metadata.takeValueForKey(localPopulator.dcmiResourceRelationFormatted(), "Relation");
        metadata.takeValueForKey(localPopulator.dcmiResourceRelationFormatted(), "relation");
        metadata.takeValueForKey(localPopulator.dcmiResourceRightsAccessRights(), "ResourceManagerID");
        metadata.takeValueForKey(localPopulator.dcmiResourceRightsAccessRights(), "resourceManagerID");
        metadata.takeValueForKey(localPopulator.dcmiResourceRightsFormatted(), "Rights");
        metadata.takeValueForKey(localPopulator.dcmiResourceRightsFormatted(), "rights");
        metadata.takeValueForKey(localPopulator.dcmiResourceSourceFormatted(), "Source");
        metadata.takeValueForKey(localPopulator.dcmiResourceSourceFormatted(), "source");
        metadata.takeValueForKey(localPopulator.mediaLabel(), "MediaLabel");
        metadata.takeValueForKey(localPopulator.mediaLabel(), "mediaLabel");
        metadata.takeValueForKey(localPopulator.dcmiResourceLanguageFormatted(), "Language");
        metadata.takeValueForKey(localPopulator.dcmiResourceLanguageFormatted(), "language");
        metadata.takeValueForKey(localPopulator.dcmiResourceTypeDCMIType(), "Type");
        metadata.takeValueForKey(localPopulator.dcmiResourceTypeDCMIType(), "type");
        metadata.takeValueForKey(localPopulator.dcmiResourcePublisherFormatted(), "Publisher");
        metadata.takeValueForKey(localPopulator.dcmiResourcePublisherFormatted(), "publisher");
        metadata.takeValueForKey(localPopulator.dcmiResourceCreatorFormatted(), "Creator");
        metadata.takeValueForKey(localPopulator.dcmiResourceCreatorFormatted(), "creator");
        metadata.takeValueForKey(localPopulator.dcmiResourceDateSubmitted(), "DateAdded");
        metadata.takeValueForKey(localPopulator.dcmiResourceDateSubmitted(), "dateAdded");
        metadata.takeValueForKey(localPopulator.dcmiResourceDateModified(), "DateModified");
        metadata.takeValueForKey(localPopulator.dcmiResourceDateModified(), "dateModified");
        return metadata.immutableClone();
    }

    /**
	 * 
	 * @param asset
	 * @param mo
	 * @return
	 */
    @SuppressWarnings({ "unused", "unchecked" })
    private NSDictionary _metadataRecordForAsset(org.osid.repository.Asset asset, OKIOSIDManagedObject mo) {
        NSMutableDictionary metadata = new NSMutableDictionary();
        try {
            metadata.takeValueForKey(asset.getAssetType().toString(), "AssetType");
            metadata.takeValueForKey(asset.getDescription(), "Description");
            metadata.takeValueForKey(asset.getDisplayName(), MD.Title);
            metadata.takeValueForKey(asset.getDisplayName(), "title");
            metadata.takeValueForKey(asset.getId().getIdString(), "resourceIdentifier");
            org.osid.repository.RecordIterator RecordIterator = asset.getRecords();
            while (RecordIterator.hasNextRecord()) {
                org.osid.repository.Record sourceRecord = RecordIterator.nextRecord();
                org.osid.repository.PartIterator partIterator = sourceRecord.getParts();
                while (partIterator.hasNextPart()) {
                    org.osid.repository.Part part = partIterator.nextPart();
                    org.osid.shared.Type partStructureType = part.getPartStructure().getType();
                    if (PachydermOSIDAssetContext.getInstance().pachydermMetadataMapping().containsValue(partStructureType.getKeyword())) {
                        NSArray keys = PachydermOSIDAssetContext.getInstance().pachydermMetadataMapping().allKeysForObject(partStructureType.getKeyword());
                        java.util.Enumeration enumerator = keys.objectEnumerator();
                        while (enumerator.hasMoreElements()) {
                            String key = (String) enumerator.nextElement();
                            metadata.takeValueForKey(part.getValue(), key);
                        }
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return metadata.immutableClone();
    }
}
