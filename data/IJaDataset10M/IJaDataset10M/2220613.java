package edu.mit.osidimpl.repository.cache;

import edu.mit.osidimpl.manager.*;
import edu.mit.osidimpl.repository.shared.*;

/**
 * Repository manages Assets of various Types and information about the Assets.
 * Assets are created, persisted, and validated by the Repository.  When
 * initially created, an Asset has an immutable Type and unique Id and its
 * validation status is false.  In this state, all methods can be called, but
 * integrity checks are not enforced.  When the Asset and its Records are
 * ready to be validated, the validateAsset method checks the Asset and sets
 * the validation status.  When working with a valid Asset, all methods
 * include integrity checks and an exception is thrown if the activity would
 * result in an inappropriate state.  Optionally, the invalidateAsset method
 * can be called to release the requirement for integrity checks, but the
 * Asset will not become valid again, until validateAsset is called and the
 * entire Asset is checked.
 * 
 * <p>
 * OSID Version: 2.0
 * </p>
 * 
 * <p>
 * Licensed under the {@link org.osid.SidImplementationLicenseMIT MIT
 * O.K.I&#46; OSID Definition License}.
 * </p>
 */
public class Repository implements org.osid.repository.Repository {

    java.util.HashMap<org.osid.shared.Id, org.osid.repository.Asset> cache = new java.util.HashMap<org.osid.shared.Id, org.osid.repository.Asset>();

    private org.osid.repository.Repository repository;

    protected OsidLogger logger;

    protected RepositoryManager mgr;

    /**
     *  Constructs a new <Repository</code>.
     *
     *  @param mgr the <code>RepositoryManager</code>
     *  @param repository
     */
    public Repository(RepositoryManager mgr, org.osid.repository.Repository repository) throws org.osid.repository.RepositoryException {
        if ((mgr == null) || (repository == null)) {
            throw new org.osid.repository.RepositoryException(org.osid.repository.RepositoryException.NULL_ARGUMENT);
        }
        this.mgr = mgr;
        this.repository = repository;
        this.logger = mgr.logger;
        AssetCacher cacher = new AssetCacher(this);
        cacher.start();
    }

    /**
     *  Update the display name for this Repository.
     *
     *  @param displayName
     *  @throws org.osid.repository.RepositoryException An exception with 
     *         one of the following messages defined in
     *         org.osid.repository.RepositoryException may be thrown: {@link
     *         org.osid.repository.RepositoryException#OPERATION_FAILED
     *         OPERATION_FAILED}, {@link
     *         org.osid.repository.RepositoryException#PERMISSION_DENIED
     *         PERMISSION_DENIED}, {@link
     *         org.osid.repository.RepositoryException#CONFIGURATION_ERROR
     *         CONFIGURATION_ERROR}, {@link
     *         org.osid.repository.RepositoryException#UNIMPLEMENTED
     *         UNIMPLEMENTED}, {@link
     *         org.osid.repository.RepositoryException#NULL_ARGUMENT
     *         NULL_ARGUMENT}
     */
    public void updateDisplayName(String displayName) throws org.osid.repository.RepositoryException {
        logger.logMethod(displayName);
        this.repository.updateDisplayName(displayName);
        return;
    }

    /**
     *  Get the display name for this Repository.
     *
     *  @return String the display name
     *  @throws org.osid.repository.RepositoryException
     */
    public String getDisplayName() throws org.osid.repository.RepositoryException {
        logger.logMethod();
        return (this.repository.getDisplayName());
    }

    /**
     *  Get the unique Id for this Repository.
     *
     *  @return org.osid.shared.Id A unique Id that is usually set by a create
     *          method's implementation.
     *  @throws org.osid.repository.RepositoryException
     */
    public org.osid.shared.Id getId() throws org.osid.repository.RepositoryException {
        logger.logMethod();
        return (this.repository.getId());
    }

    /**
     *  Get the RepositoryType of this Repository.
     *
     *  @return org.osid.shared.Type
     *  @throws org.osid.repository.RepositoryException
     */
    public org.osid.shared.Type getType() throws org.osid.repository.RepositoryException {
        logger.logMethod();
        return (this.repository.getType());
    }

    /**
     *  Get the description for this Repository.
     *
     *  @return String the description
     *  @throws org.osid.repository.RepositoryException
     */
    public String getDescription() throws org.osid.repository.RepositoryException {
        logger.logMethod();
        return (this.repository.getDescription());
    }

    /**
     *  Update the description for this Repository.
     *
     *  @param description
     *  @throws org.osid.repository.RepositoryException An exception with one
     *         of the following messages defined in
     *         org.osid.repository.RepositoryException may be thrown: {@link
     *         org.osid.repository.RepositoryException#NULL_ARGUMENT
     *         NULL_ARGUMENT}
     */
    public void updateDescription(String description) throws org.osid.repository.RepositoryException {
        logger.logMethod(description);
        this.repository.updateDescription(description);
        return;
    }

    /**
     *  Create a new Asset of this AssetType in this Repository.  The
     *  implementation of this method sets the Id for the new object.
     *
     *  @return Asset
     *  @throws org.osid.repository.RepositoryException An exception with one
     *          of the following messages defined in
     *          org.osid.repository.RepositoryException may be thrown: {@link
     *          org.osid.repository.RepositoryException#OPERATION_FAILED
     *          OPERATION_FAILED}, {@link
     *          org.osid.repository.RepositoryException#PERMISSION_DENIED
     *          PERMISSION_DENIED}, {@link
     *          org.osid.repository.RepositoryException#NULL_ARGUMENT
     *          NULL_ARGUMENT}, {@link
     *          org.osid.repository.RepositoryException#UNKNOWN_TYPE
     *          UNKNOWN_TYPE}
     */
    public org.osid.repository.Asset createAsset(String displayName, String description, org.osid.shared.Type assetType) throws org.osid.repository.RepositoryException {
        logger.logMethod(displayName, description, assetType);
        return (this.repository.createAsset(displayName, description, assetType));
    }

    /**
     *  Delete an Asset from this Repository.
     *
     *  @param assetId
     *  @throws org.osid.repository.RepositoryException An exception with one
     *          of the following messages defined in
     *          org.osid.repository.RepositoryException may be thrown: {@link
     *          org.osid.repository.RepositoryException#OPERATION_FAILED
     *          OPERATION_FAILED}, {@link
     *          org.osid.repository.RepositoryException#PERMISSION_DENIED
     *          PERMISSION_DENIED}, {@link
     *          org.osid.repository.RepositoryException#NULL_ARGUMENT
     *          NULL_ARGUMENT}, {@link
     *          org.osid.repository.RepositoryException#UNKNOWN_ID UNKNOWN_ID}
     */
    public void deleteAsset(org.osid.shared.Id assetId) throws org.osid.repository.RepositoryException {
        logger.logMethod(assetId);
        this.repository.deleteAsset(assetId);
        return;
    }

    /**
     *  Get all the Assets in this Repository.  Iterators return a set, one at
     *  a time.
     *
     *  @return AssetIterator  The order of the objects returned by the
     *          Iterator is not guaranteed.
     *  @throws org.osid.repository.RepositoryException An exception with one
     *          of the following messages defined in
     *          org.osid.repository.RepositoryException may be thrown: {@link
     *          org.osid.repository.RepositoryException#OPERATION_FAILED
     *          OPERATION_FAILED}, {@link
     *          org.osid.repository.RepositoryException#PERMISSION_DENIED
     *          PERMISSION_DENIED}
     */
    public org.osid.repository.AssetIterator getAssets() throws org.osid.repository.RepositoryException {
        logger.logMethod();
        java.util.ArrayList<org.osid.repository.Asset> al = new java.util.ArrayList<org.osid.repository.Asset>();
        return (new AssetIterator(this.cache.values()));
    }

    /**
     *  Get all the Assets of the specified AssetType in this Asset.  Iterators
     *  return a set, one at a time.
     *
     *  @return AssetIterator  The order of the objects returned by the 
     *          Iterator is not guaranteed.
     *  @throws org.osid.repository.RepositoryException An exception with one
     *          of the following messages defined in
     *          org.osid.repository.RepositoryException may be thrown: {@link
     *          org.osid.repository.RepositoryException#OPERATION_FAILED
     *          OPERATION_FAILED}, {@link
     *          org.osid.repository.RepositoryException#PERMISSION_DENIED
     *          PERMISSION_DENIED}, {@link
     *          org.osid.repository.RepositoryException#NULL_ARGUMENT
     *          NULL_ARGUMENT}, {@link
     *          org.osid.repository.RepositoryException#UNKNOWN_TYPE
     *          UNKNOWN_TYPE}
     */
    public org.osid.repository.AssetIterator getAssetsByType(org.osid.shared.Type assetType) throws org.osid.repository.RepositoryException {
        logger.logMethod(assetType);
        if (assetType == null) {
            throw new org.osid.repository.RepositoryException(org.osid.repository.RepositoryException.NULL_ARGUMENT);
        }
        java.util.ArrayList<org.osid.repository.Asset> al = new java.util.ArrayList<org.osid.repository.Asset>();
        for (org.osid.repository.Asset asset : this.cache.values()) {
            if (assetType.isEqual(asset.getAssetType())) {
                al.add(asset);
            }
        }
        return (new AssetIterator(al));
    }

    /**
     *  Get all the AssetTypes in this Repository.  AssetTypes are used to
     *  categorize Assets.  Iterators return a set, one at a time.
     *
     *  @return org.osid.shared.TypeIterator  The order of the objects returned
     *          by the Iterator is not guaranteed.
     *  @throws org.osid.repository.RepositoryException An exception with 
     *          one of the following messages defined in
     *          org.osid.repository.RepositoryException may be thrown: {@link
     *          org.osid.repository.RepositoryException#OPERATION_FAILED
     *          OPERATION_FAILED}, {@link
     *          org.osid.repository.RepositoryException#PERMISSION_DENIED
     *          PERMISSION_DENIED}
     */
    public org.osid.shared.TypeIterator getAssetTypes() throws org.osid.repository.RepositoryException {
        logger.logMethod();
        return (this.repository.getAssetTypes());
    }

    /**
     * Get the Properties of this Type associated with this Repository.
     *
     * @return org.osid.shared.Properties
     *
     * @throws org.osid.repository.RepositoryException An exception with one of
     *         the following messages defined in
     *         org.osid.repository.RepositoryException may be thrown: {@link
     *         org.osid.repository.RepositoryException#NULL_ARGUMENT
     *         NULL_ARGUMENT}, {@link
     *         org.osid.repository.RepositoryException#UNKNOWN_TYPE
     *         UNKNOWN_TYPE}
     */
    public org.osid.shared.Properties getPropertiesByType(org.osid.shared.Type propertiesType) throws org.osid.repository.RepositoryException {
        logger.logMethod(propertiesType);
        return (this.repository.getPropertiesByType(propertiesType));
    }

    /**
     *  Get all the Property Types for  Repository.
     *
     *  @return org.osid.shared.TypeIterator
     *  @throws org.osid.repository.RepositoryException
     */
    public org.osid.shared.TypeIterator getPropertyTypes() throws org.osid.repository.RepositoryException {
        logger.logMethod();
        return (this.repository.getPropertyTypes());
    }

    /**
     * Get the Properties associated with this Repository.
     *
     * @return org.osid.shared.PropertiesIterator
     *
     * @throws org.osid.repository.RepositoryException An exception with one of
     *         the following messages defined in
     *         org.osid.repository.RepositoryException may be thrown: {@link
     *         org.osid.repository.RepositoryException#OPERATION_FAILED
     *         OPERATION_FAILED}, {@link
     *         org.osid.repository.RepositoryException#PERMISSION_DENIED
     *         PERMISSION_DENIED}, {@link
     *         org.osid.repository.RepositoryException#CONFIGURATION_ERROR
     *         CONFIGURATION_ERROR}, {@link
     *         org.osid.repository.RepositoryException#UNIMPLEMENTED
     *         UNIMPLEMENTED}
     */
    public org.osid.shared.PropertiesIterator getProperties() throws org.osid.repository.RepositoryException {
        logger.logMethod();
        return (this.repository.getProperties());
    }

    /**
     *  Get all the RecordStructures in this Repository.  RecordStructures are
     *  used to categorize information about Assets.  Iterators return a set,
     *  one at a time.
     *
     *  @return RecordStructureIterator  The order of the objects returned by
     *          the Iterator is not guaranteed.
     *  @throws org.osid.repository.RepositoryException An exception with 
     *          one of the following messages defined in
     *          org.osid.repository.RepositoryException may be thrown: {@link
     *          org.osid.repository.RepositoryException#OPERATION_FAILED
     *          OPERATION_FAILED}, {@link
     *          org.osid.repository.RepositoryException#PERMISSION_DENIED
     *          PERMISSION_DENIED}
     */
    public org.osid.repository.RecordStructureIterator getRecordStructures() throws org.osid.repository.RepositoryException {
        logger.logMethod();
        return (this.repository.getRecordStructures());
    }

    /**
     * Get all the RecordStructures with the specified RecordStructureType in
     * this Repository.  RecordStructures are used to categorize information
     * about Assets.  Iterators return a set, one at a time.
     *
     * @param recordStructureType
     * @return RecordStructureIterator  The order of the objects returned by
     *         the Iterator is not guaranteed.
     * @throws org.osid.repository.RepositoryException An exception with one of
     *         the following messages defined in
     *         org.osid.repository.RepositoryException may be thrown: {@link
     *         org.osid.repository.RepositoryException#OPERATION_FAILED
     *         OPERATION_FAILED}, {@link
     *         org.osid.repository.RepositoryException#PERMISSION_DENIED
     *         PERMISSION_DENIED}, {@link
     *         org.osid.repository.RepositoryException#CONFIGURATION_ERROR
     *         CONFIGURATION_ERROR}, {@link
     *         org.osid.repository.RepositoryException#UNIMPLEMENTED
     *         UNIMPLEMENTED}
     */
    public org.osid.repository.RecordStructureIterator getRecordStructuresByType(org.osid.shared.Type recordStructureType) throws org.osid.repository.RepositoryException {
        logger.logMethod(recordStructureType);
        return (this.repository.getRecordStructuresByType(recordStructureType));
    }

    /**
     * Get the RecordStructures that this AssetType must support.
     * RecordStructures are used to categorize information about Assets.
     * Iterators return a set, one at a time.
     *
     * @param assetType
     *
     * @return RecordStructureIterator  The order of the objects returned by
     *         the Iterator is not guaranteed.
     *
     * @throws org.osid.repository.RepositoryException An exception with one of
     *         the following messages defined in
     *         org.osid.repository.RepositoryException may be thrown: {@link
     *         org.osid.repository.RepositoryException#OPERATION_FAILED
     *         OPERATION_FAILED}, {@link
     *         org.osid.repository.RepositoryException#PERMISSION_DENIED
     *         PERMISSION_DENIED}, {@link
     *         org.osid.repository.RepositoryException#CONFIGURATION_ERROR
     *         CONFIGURATION_ERROR}, {@link
     *         org.osid.repository.RepositoryException#UNIMPLEMENTED
     *         UNIMPLEMENTED}, {@link
     *         org.osid.repository.RepositoryException#NULL_ARGUMENT
     *         NULL_ARGUMENT}, {@link
     *         org.osid.repository.RepositoryException#UNKNOWN_TYPE
     *         UNKNOWN_TYPE}
     */
    public org.osid.repository.RecordStructureIterator getMandatoryRecordStructures(org.osid.shared.Type assetType) throws org.osid.repository.RepositoryException {
        logger.logMethod(assetType);
        return (this.repository.getMandatoryRecordStructures(assetType));
    }

    /**
     * Get all the SearchTypes supported by this Repository.  Iterators return
     * a set, one at a time.
     *
     * @return org.osid.shared.TypeIterator  The order of the objects returned
     *         by the Iterator is not guaranteed.
     *
     * @throws org.osid.repository.RepositoryException An exception with one of
     *         the following messages defined in
     *         org.osid.repository.RepositoryException may be thrown: {@link
     *         org.osid.repository.RepositoryException#OPERATION_FAILED
     *         OPERATION_FAILED}, {@link
     *         org.osid.repository.RepositoryException#PERMISSION_DENIED
     *         PERMISSION_DENIED}, {@link
     *         org.osid.repository.RepositoryException#CONFIGURATION_ERROR
     *         CONFIGURATION_ERROR}, {@link
     *         org.osid.repository.RepositoryException#UNIMPLEMENTED
     *         UNIMPLEMENTED}
     */
    public org.osid.shared.TypeIterator getSearchTypes() throws org.osid.repository.RepositoryException {
        logger.logMethod();
        return (this.repository.getSearchTypes());
    }

    /**
     *  Get all the StatusTypes supported by this Repository.  Iterators return
     *  a set, one at a time.
     *
     *  @return org.osid.shared.TypeIterator  The order of the objects returned
     *          by the Iterator is not guaranteed.
     *  @throws org.osid.repository.RepositoryException An exception with one
     *          of the following messages defined in
     *          org.osid.repository.RepositoryException may be thrown: {@link
     *          org.osid.repository.RepositoryException#UNIMPLEMENTED
     *          UNIMPLEMENTED}
     */
    public org.osid.shared.TypeIterator getStatusTypes() throws org.osid.repository.RepositoryException {
        logger.logMethod();
        return (this.repository.getStatusTypes());
    }

    /**
     *  Get the StatusType of the Asset with the specified unique Id.
     *
     *  @return org.osid.shared.Type
     *  @throws org.osid.repository.RepositoryException An exception with one
     *          of the following messages defined in
     *          org.osid.repository.RepositoryException may be thrown: {@link
     *          org.osid.repository.RepositoryException#UNIMPLEMENTED
     *          UNIMPLEMENTED}
     */
    public org.osid.shared.Type getStatus(org.osid.shared.Id assetId) throws org.osid.repository.RepositoryException {
        logger.logMethod(assetId);
        return (this.repository.getStatus(assetId));
    }

    /**
     *  Validate all the Records for an Asset and set its status Type
     *  accordingly.  If the Asset is valid, return true; otherwise return
     *  false.  The implementation may throw an Exception for any validation
     *  failures and use the Exception's message to identify specific causes.
     *
     *  @param assetId
     *  @return boolean
     *  @throws org.osid.repository.RepositoryException An exception with one
     *          of the following messages defined in
     *          org.osid.repository.RepositoryException may be thrown: {@link
     *          org.osid.repository.RepositoryException#UNIMPLEMENTED
     *          UNIMPLEMENTED}
     */
    public boolean validateAsset(org.osid.shared.Id assetId) throws org.osid.repository.RepositoryException {
        logger.logMethod(assetId);
        return (this.repository.validateAsset(assetId));
    }

    /**
     *  Set the Asset's status Type accordingly and relax validation checking
     *  when creating Records and Parts or updating Parts' values.
     *
     *  @param assetId
     *  @throws org.osid.repository.RepositoryException An exception with one
     *          of the following messages defined in
     *          org.osid.repository.RepositoryException may be thrown: {@link
     *          org.osid.repository.RepositoryException#UNIMPLEMENTED
     *          UNIMPLEMENTED}
     */
    public void invalidateAsset(org.osid.shared.Id assetId) throws org.osid.repository.RepositoryException {
        logger.logMethod(assetId);
        this.repository.invalidateAsset(assetId);
        return;
    }

    /**
     * Get the Asset with the specified unique Id.
     *
     * @param assetId
     *
     * @return Asset
     *
     * @throws org.osid.repository.RepositoryException An exception with one of
     *         the following messages defined in
     *         org.osid.repository.RepositoryException may be thrown: {@link
     *         org.osid.repository.RepositoryException#OPERATION_FAILED
     *         OPERATION_FAILED}, {@link
     *         org.osid.repository.RepositoryException#PERMISSION_DENIED
     *         PERMISSION_DENIED}, {@link
     *         org.osid.repository.RepositoryException#CONFIGURATION_ERROR
     *         CONFIGURATION_ERROR}, {@link
     *         org.osid.repository.RepositoryException#UNIMPLEMENTED
     *         UNIMPLEMENTED}, {@link
     *         org.osid.repository.RepositoryException#NULL_ARGUMENT
     *         NULL_ARGUMENT}, {@link
     *         org.osid.repository.RepositoryException#UNKNOWN_ID UNKNOWN_ID}
     */
    public org.osid.repository.Asset getAsset(org.osid.shared.Id assetId) throws org.osid.repository.RepositoryException {
        logger.logMethod(assetId);
        if (assetId == null) {
            throw new org.osid.repository.RepositoryException(org.osid.repository.RepositoryException.NULL_ARGUMENT);
        }
        org.osid.repository.Asset asset = this.cache.get(assetId);
        if (asset != null) {
            return (asset);
        } else {
            throw new org.osid.repository.RepositoryException(org.osid.repository.RepositoryException.UNKNOWN_ID);
        }
    }

    /**
     *  Get the Asset with the specified unique Id that is appropriate for the
     *  date specified.  The specified date allows a Repository implementation
     *  to support Asset versioning.
     *
     *  @param assetId
     *  @param date the number of milliseconds since January 1, 1970, 00:00:00
     *         GMT
     *  @return Asset
     *  @throws org.osid.repository.RepositoryException An exception with one
     *          of the following messages defined in
     *          org.osid.repository.RepositoryException may be thrown: {@link
     *          org.osid.repository.RepositoryException#UNIMPLEMENTED
     *          UNIMPLEMENTED}
     */
    public org.osid.repository.Asset getAssetByDate(org.osid.shared.Id assetId, long date) throws org.osid.repository.RepositoryException {
        logger.logMethod(assetId, date);
        return (this.repository.getAssetByDate(assetId, date));
    }

    /**
     *  Get all the dates for the Asset with the specified unique Id.  These
     *  dates allows a Repository implementation to support Asset versioning.
     *
     *  @param assetId
     *  @return org.osid.shared.LongValueIterator (a date is the number of
     *          milliseconds since January 1, 1970, 00:00:00 GMT)
     *  @throws org.osid.repository.RepositoryException An exception with one
     *          of the following messages defined in
     *          org.osid.repository.RepositoryException may be thrown: {@link
     *          org.osid.repository.RepositoryException#UNIMPLEMENTED
     *          UNIMPLEMENTED}
     */
    public org.osid.shared.LongValueIterator getAssetDates(org.osid.shared.Id assetId) throws org.osid.repository.RepositoryException {
        logger.logMethod(assetId);
        return (this.repository.getAssetDates(assetId));
    }

    /**
     * Perform a search of the specified Type and get all the Assets that
     * satisfy the SearchCriteria.  Iterators return a set, one at a time.
     *
     * @param searchCriteria
     * @param searchType
     * @param searchProperties
     *
     * @return AssetIterator  The order of the objects returned by the Iterator
     *         is not guaranteed.
     *
     * @throws org.osid.repository.RepositoryException An exception with one of
     *         the following messages defined in
     *         org.osid.repository.RepositoryException may be thrown: {@link
     *         org.osid.repository.RepositoryException#OPERATION_FAILED
     *         OPERATION_FAILED}, {@link
     *         org.osid.repository.RepositoryException#PERMISSION_DENIED
     *         PERMISSION_DENIED}, {@link
     *         org.osid.repository.RepositoryException#CONFIGURATION_ERROR
     *         CONFIGURATION_ERROR}, {@link
     *         org.osid.repository.RepositoryException#UNIMPLEMENTED
     *         UNIMPLEMENTED}, {@link
     *         org.osid.repository.RepositoryException#NULL_ARGUMENT
     *         NULL_ARGUMENT}, {@link
     *         org.osid.repository.RepositoryException#UNKNOWN_TYPE
     *         UNKNOWN_TYPE}
     */
    public org.osid.repository.AssetIterator getAssetsBySearch(java.io.Serializable searchCriteria, org.osid.shared.Type searchType, org.osid.shared.Properties searchProperties) throws org.osid.repository.RepositoryException {
        logger.logMethod(searchCriteria, searchType, searchProperties);
        return (this.repository.getAssetsBySearch(searchCriteria, searchType, searchProperties));
    }

    /**
     * Create a copy of an Asset.  The Id, AssetType, and Repository for the
     * new Asset is set by the implementation.  All Records are similarly
     * copied.
     *
     * @param asset
     *
     * @return org.osid.shared.Id
     *
     * @throws org.osid.repository.RepositoryException An exception with one of
     *         the following messages defined in
     *         org.osid.repository.RepositoryException may be thrown: {@link
     *         org.osid.repository.RepositoryException#OPERATION_FAILED
     *         OPERATION_FAILED}, {@link
     *         org.osid.repository.RepositoryException#PERMISSION_DENIED
     *         PERMISSION_DENIED}, {@link
     *         org.osid.repository.RepositoryException#CONFIGURATION_ERROR
     *         CONFIGURATION_ERROR}, {@link
     *         org.osid.repository.RepositoryException#UNIMPLEMENTED
     *         UNIMPLEMENTED}, {@link
     *         org.osid.repository.RepositoryException#NULL_ARGUMENT
     *         NULL_ARGUMENT}, {@link
     *         org.osid.repository.RepositoryException#UNKNOWN_ID UNKNOWN_ID}
     */
    public org.osid.shared.Id copyAsset(org.osid.repository.Asset asset) throws org.osid.repository.RepositoryException {
        logger.logMethod(asset);
        return (this.repository.copyAsset(asset));
    }

    /**
     *  This method indicates whether this implementation supports Repository
     *  methods getAssetsDates() and getAssetByDate()
     *
     *  @return boolean false indicates that these methods will throw {@link
     *          org.osid.repository.RepositoryException#UNIMPLEMENTED
     *          UNIMPLEMENTED}, true indicates this implementation supports
     *          Repository methods getAssetsDates() and getAssetByDate()
     *  @throws org.osid.repository.RepositoryException
     */
    public boolean supportsVersioning() throws org.osid.repository.RepositoryException {
        logger.logMethod();
        return (this.repository.supportsVersioning());
    }

    /**
     *  This method indicates whether this implementation supports Repository
     *  methods: copyAsset, deleteAsset, invalidateAsset, updateDescription,
     *  updateDisplayName. Asset methods: addAsset, copyRecordStructure,
     *  createRecord, deleteRecord, inheritRecordStructure, removeAsset,
     *  updateContent, updateDescription, updateDisplayName,
     *  updateEffectiveDate, updateExpirationDate. Part methods: createPart,
     *  deletePart, updateDisplayName, updateValue. PartStructure methods:
     *  updateDisplayName, validatePart. Record methods: createPart,
     *  deletePart, updateDisplayName. RecordStructure methods:
     *  updateDisplayName, validateRecord.
     *  @return boolean false indicates that these methods will throw {@link
     *          org.osid.repository.RepositoryException#UNIMPLEMENTED
     *          UNIMPLEMENTED}, true indicates this implementation supports
     *          Repository methods: copyAsset, deleteAsset, invalidateAsset,
     *          updateDescription, updateDisplayName. Asset methods: addAsset,
     *          copyRecordStructure, createRecord, deleteRecord,
     *          inheritRecordStructure, removeAsset, updateContent,
     *          updateDescription, updateDisplayName, updateEffectiveDate,
     *          updateExpirationDate. Part methods: createPart, deletePart,
     *          updateDisplayName, updateValue. PartStructure methods:
     *          updateDisplayName, validatePart. Record methods: createPart,
     *          deletePart, updateDisplayName. RecordStructure methods:
     *          updateDisplayName, validateRecord.
     *  @throws org.osid.repository.RepositoryException
     */
    public boolean supportsUpdate() throws org.osid.repository.RepositoryException {
        logger.logMethod();
        return (this.repository.supportsUpdate());
    }

    class AssetCacher extends Thread {

        private Repository repository;

        AssetCacher(Repository repository) {
            this.repository = repository;
        }

        public void run() {
            try {
                org.osid.repository.AssetIterator ai = this.repository.repository.getAssets();
                while (ai.hasNextAsset()) {
                    org.osid.repository.Asset asset = new Asset(this.repository, ai.nextAsset());
                    logger.logTrace("caching... " + asset.getDisplayName());
                    cache.put(asset.getId(), asset);
                    asset.getContent();
                }
            } catch (org.osid.repository.RepositoryException re) {
                logger.logError("cannot populate cache", re);
            }
            return;
        }
    }
}
