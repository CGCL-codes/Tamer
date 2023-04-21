package org.dvb.si;

/**
 * This interface groups the common features of SIBouquet, SINetwork,
 * SITransportStream, SIService, PMTService, SIEvent, SITime and
 * PMTElementaryStream.
 * <p>
 * Each SIInformation instance represents a sub-table (part). Any method
 * accessing descriptors will retrieve descriptors from the same sub-table
 * version as the SIInformation instance. When this version is no longer
 * available, an SITableUpdatedEvent is returned.
 * @see SIBouquet
 * @see SINetwork
 * @see SITransportStream
 * @see SIService
 * @see PMTService
 * @see SIEvent
 * @see SITime
 * @see PMTElementaryStream
 */
public interface SIInformation {

    /**
	 * Constant for retrieve mode parameter of the retrieve methods. When
	 * FROM_CACHE_ONLY mode is specified, the data will be retrieved only if it
	 * is in the cache. Otherwise, SINotInCacheEvent will be delivered to the
	 * listener. No stream access is done in this case.
	 */
    public static final short FROM_CACHE_ONLY = 0;

    /**
	 * Constant for retrieve mode parameter of the retrieve methods. When
	 * FROM_CACHE_OR_STREAM mode is specified, the data will be retrieved from
	 * cache if it is present in the cache, otherwise it will be retrieved from
	 * the stream.
	 */
    public static final short FROM_CACHE_OR_STREAM = 1;

    /**
	 * Constant for retrieve mode parameter of the retrieve methods. When
	 * FROM_STREAM_ONLY mode is specified, the data will be retrieved directly
	 * from the stream and no cache access is tried first. This mode is
	 * meaningful only if the application knows that the information is not in
	 * the cache or that the information in the cache is no longer valid, but
	 * the implementation of the SI database may not be aware of the invalidity
	 * of the cached data. If the application has got the notification of the
	 * existence of an updated version through the listener mechanism in this
	 * API, the implementation of the SI database is aware of the version change
	 * and the application should specify the FROM_CACHE_OR_STREAM mode to be
	 * able to retrieve the data faster if the updated version has already been
	 * loaded to the cache by the SI database implementation.
	 */
    public static final short FROM_STREAM_ONLY = 2;

    /**
	 * This method retrieves all descriptors in the order the descriptors are
	 * broadcast.
	 * <p>
	 * This method is asynchronous and the completion of the method will be
	 * signalled by an <code>SISuccessfulRetrieveEvent</code> being sent to
	 * listener. Any retrieved descriptors are found in the SIIterator returned
	 * by the <code>getResult</code> method of that event. If descriptors are
	 * found then this iterator will contain Descriptor objects. If there are no
	 * matching descriptors, this iterator will contain no objects.
	 * @param retrieveMode Mode of retrieval indicating whether the data should
	 *            be retrieved only from the cache (FROM_CACHE_ONLY), from the
	 *            cache if available and if not from the stream
	 *            (FROM_CACHE_OR_STREAM), or always from the stream
	 *            (FROM_STREAM_ONLY).
	 * @param appData An object supplied by the application. This object will be
	 *            delivered to the listener when the request completes. The
	 *            application can use this objects for internal communication
	 *            purposes. If the application does not need any application
	 *            data, the parameter can be null.
	 * @param listener SIRetrievalListener that will receive the event informing
	 *            about the completion of the request.
	 * @return An SIRequest object
	 * @exception SIIllegalArgumentException thrown if the retrieveMode is
	 *                invalid
	 * @see SIRequest
	 * @see SIRetrievalListener
	 * @see Descriptor
	 * @see SIIterator
	 */
    public SIRequest retrieveDescriptors(short retrieveMode, Object appData, SIRetrievalListener listener) throws SIIllegalArgumentException;

    /**
	 * Retrieve a set of descriptors. This method retrieves all or a set of
	 * descriptors in the order the descriptors are broadcast.
	 * <p>
	 * The tag values included in the <code>someDescriptorTags</code>
	 * parameter are used for filtering the descriptors that are returned. Only
	 * those descriptors whose tag value is included in the
	 * <code>someDescriptorTags</code> array are retrieved, unless the
	 * <code>someDescriptorTags</code> array contains -1 as its one and only
	 * item in which case all descriptors related to this object are retrieved.
	 * <p>
	 * If the list of tags is a subset of the one hinted to the underlying
	 * implementation (in the request which created the object on which the
	 * method is called), this is likely to increase the efficiency of the
	 * (optional) caching mechanism
	 * <p>
	 * This method is asynchronous and the completion of the method will be
	 * signalled by an <code>SISuccessfulRetrieveEvent</code> being sent to
	 * listener. Any retrieved descriptors are found in the SIIterator returned
	 * by the <code>getResult</code> method of that event. If descriptors are
	 * found then this iterator will contain Descriptor objects. If there are no
	 * matching descriptors, this iterator will contain no objects.
	 * @param retrieveMode Mode of retrieval indicating whether the data should
	 *            be retrieved only from the cache (FROM_CACHE_ONLY), from the
	 *            cache if available and if not from the stream
	 *            (FROM_CACHE_OR_STREAM), or always from the stream
	 *            (FROM_STREAM_ONLY).
	 * @param appData An object supplied by the application. This object will be
	 *            delivered to the listener when the request completes. The
	 *            application can use this objects for internal communication
	 *            purposes. If the application does not need any application
	 *            data, the parameter can be null.
	 * @param listener SIRetrievalListener that will receive the event informing
	 *            about the completion of the request.
	 * @param someDescriptorTags Descriptor tag values of descriptors that are
	 *            used for filtering descriptors from the descriptors included
	 *            in the SI table item corresponding to this SIInformation
	 *            object. If the array contains -1 as its one and only element,
	 *            all descriptors related to this object are retrieved.
	 * @return An SIRequest object
	 * @exception SIIllegalArgumentException thrown if the retrieveMode is
	 *                invalid
	 * @see SIRequest
	 * @see SIRetrievalListener
	 * @see Descriptor
	 * @see SIIterator
	 * @see DescriptorTag
	 */
    public SIRequest retrieveDescriptors(short retrieveMode, Object appData, SIRetrievalListener listener, short[] someDescriptorTags) throws SIIllegalArgumentException;

    /**
	 * Get the tags of all descriptors that are part of this version of this
	 * object. The tags are returned in the same order as the descriptors are
	 * broadcast. This method returns also the tags of descriptors that were not
	 * hinted at and that are not necessarily present in the cache. If there are
	 * no descriptors associated with this SIInformation object, this method
	 * returns an empty array whose length is 0.
	 * @return The tags of the descriptors actually broadcast for the object
	 *         (identified by their tags).
	 * @see DescriptorTag
	 */
    public short[] getDescriptorTags();

    /**
	 * Return the root of the hierarchy the object that implements this
	 * interface belongs to.
	 * @return The root of the hierarchy.
	 */
    public SIDatabase getSIDatabase();

    /**
	 * Return the time when the information contained in the object that
	 * implements this interface was last updated.
	 * @return The date of the last update.
	 */
    public java.util.Date getUpdateTime();

    /**
	 * Return true when the information contained in the object that implements
	 * this interface was filtered from an 'actual' table or from a table with
	 * no 'actual/other' distinction.
	 * @return <code>true</code> if the information comes from an 'actual'
	 *         table or from a table with no 'actual/other' distiction,
	 *         otherwise returns <code>false</code>
	 */
    public boolean fromActual();

    /**
	 * Return the org.davic.mpeg.TransportStream object the information
	 * contained in the object that implements that interface was filtered from.
	 * @return The <code>org.davic.mpeg.TransportStream</code> object the
	 *         information was filtered from.
	 * @see org.davic.mpeg.TransportStream
	 */
    public org.davic.mpeg.TransportStream getDataSource();
}
