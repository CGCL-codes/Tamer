package org.ccnx.ccn.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import org.ccnx.ccn.CCNHandle;
import org.ccnx.ccn.ContentVerifier;
import org.ccnx.ccn.config.ConfigurationException;
import org.ccnx.ccn.impl.security.crypto.CCNDigestHelper;
import org.ccnx.ccn.impl.support.DataUtils;
import org.ccnx.ccn.impl.support.Log;
import org.ccnx.ccn.profiles.SegmentationProfile;
import org.ccnx.ccn.profiles.VersioningProfile;
import org.ccnx.ccn.profiles.nameenum.EnumeratedNameList;
import org.ccnx.ccn.protocol.ContentName;
import org.ccnx.ccn.protocol.ContentObject;
import org.ccnx.ccn.protocol.Interest;
import org.ccnx.ccn.protocol.PublisherPublicKeyDigest;
import org.ccnx.ccn.protocol.SignedInfo.ContentType;

/**
 * Miscellaneous helper functions to read data. Most clients will
 * prefer the higher-level interfaces offered by CCNInputStream and
 * its subclasses, or CCNNetworkObject and its subclasses.
 */
public class CCNReader {

    protected CCNHandle _handle;

    public CCNReader(CCNHandle handle) throws ConfigurationException, IOException {
        _handle = handle;
        if (null == _handle) _handle = CCNHandle.open();
    }

    /**
	 * Gets a ContentObject matching this name and publisher
	 * @param name desired name or prefix for data
	 * @param publisher desired publisher or null for any publisher
	 * @param timeout milliseconds to wait for data
	 * @return data matching the name and publisher or null
	 * @throws IOException
	 */
    public ContentObject get(ContentName name, PublisherPublicKeyDigest publisher, long timeout) throws IOException {
        return _handle.get(name, publisher, timeout);
    }

    /**
	 * Gets a ContentObject matching this interest, CURRENTLY UNVERIFIED.
	 * @param interest interest for desired object
	 * @param timeout milliseconds to wait for data
	 * @return data matching the interest or null
	 * @throws IOException
	 */
    public ContentObject get(Interest interest, long timeout) throws IOException {
        return _handle.get(interest, timeout);
    }

    /**
	 * Helper method to retrieve a set of segmented content blocks and rebuild them
	 * into a single buffer. Equivalent to CCNWriter's put. Does not do anything about
	 * versioning.
	 */
    public byte[] getData(ContentName name, PublisherPublicKeyDigest publisher, int timeout) throws IOException {
        CCNInputStream inputStream = new CCNInputStream(name, publisher, _handle);
        inputStream.setTimeout(timeout);
        byte[] data = DataUtils.getBytesFromStream(inputStream);
        return data;
    }

    /**
	 * Helper method to retrieve a set of segmented content blocks and rebuild them
	 * into a single buffer. Equivalent to CCNWriter's put. Does not do anything about
	 * versioning.
	 */
    public byte[] getVersionedData(ContentName name, PublisherPublicKeyDigest publisher, int timeout) throws IOException {
        CCNInputStream inputStream = new CCNVersionedInputStream(name, publisher, _handle);
        inputStream.setTimeout(timeout);
        byte[] data = DataUtils.getBytesFromStream(inputStream);
        return data;
    }

    /**
	 * Return data the specified number of levels below us in the
	 * hierarchy, with order preference of leftmost.
	 * 
	 * @param handle handle to use for requests
	 * @param name of content to get
	 * @param level number of levels below name in the hierarchy content should sit
	 * @param publisher the desired publisher of this content, or null for any publisher.
	 * @param timeout timeout for retrieval
	 * @return matching content, if found
	 * @throws IOException
	 */
    public ContentObject getLower(ContentName name, int level, PublisherPublicKeyDigest publisher, long timeout) throws IOException {
        return _handle.get(Interest.lower(name, level, publisher), timeout);
    }

    /**
	 * Enumerate matches below query name in the hierarchy, looking
	 * at raw content. For a higher-level enumeration protocol see the 
	 * name enumeration protocol.
	 * Note this method is also quite slow because it has to timeout requests at every
	 * search level
	 * @param query an Interest defining the highest level of the query
	 * @param timeout - milliseconds to wait for each individual get of data, default is 5 seconds
	 * @return a list of the content objects matching this query
	 * @throws IOException 
	 */
    public ArrayList<ContentObject> enumerate(Interest query, long timeout) throws IOException {
        ArrayList<ContentObject> result = new ArrayList<ContentObject>();
        int count = query.name().count();
        while (true) {
            ContentObject co = null;
            co = _handle.get(query, timeout);
            if (co == null) break;
            Log.info(Log.FAC_IO, "enumerate: retrieved " + co.fullName() + " on query: " + query.name());
            result.add(co);
            for (int i = co.name().count() - 1; i > count; i--) {
                result.addAll(enumerate(new Interest(new ContentName(i, co.name().components())), timeout));
            }
            query = Interest.next(co.name(), count, null);
        }
        Log.info(Log.FAC_IO, "enumerate: retrieved " + result.size() + " objects.");
        return result;
    }

    /**
	 * API to determine whether a piece of content exists in the repository. Currently uses
	 * name enumeration. Will change to alternative protocol whenever repository supports one.
	 * 
	 * Deprecated - instead use RepositoryControl.localRepoSync which will sync data if its not in the
	 * repository which is what we want to do if this returns false.
	 * 
	 * @param availableContent
	 * @param timeout
	 * @param handle
	 * @return
	 * @throws IOException
	 */
    @Deprecated
    public static ContentObject isContentInRepository(ContentObject availableContent, long timeout, CCNHandle handle) throws IOException {
        if (timeout == 0) {
            return null;
        }
        EnumeratedNameList enl = new EnumeratedNameList(availableContent.name(), handle);
        enl.waitForChildren(timeout);
        enl.stopEnumerating();
        if (enl.hasChild(availableContent.digest())) {
            return availableContent;
        }
        Log.info(Log.FAC_IO, "Repository does not contain expected child of {0}, has {1} children at that point", availableContent.name(), enl.childCount());
        return null;
    }

    /**
	 * API to determine whether a piece of content exists in the repository. Currently uses
	 * name enumeration. Will change to alternative protocol whenever repository supports one.
	 * 
	 * Deprecated - instead use RepositoryControl.localRepoSync which will sync data if its not in the
	 * repository which is what we want to do if this returns false.
	 * 
	 * @param contentName
	 * @param desiredType
	 * @param desiredContentDigest
	 * @param verifier
	 * @param timeout
	 * @param handle
	 * @return
	 * @throws IOException
	 */
    @Deprecated
    public static ContentObject isContentInRepository(ContentName contentName, ContentType desiredType, byte[] desiredContentDigest, PublisherPublicKeyDigest desiredPublisher, ContentVerifier verifier, long timeout, CCNHandle handle) throws IOException {
        ContentObject latestAvailableContent = isVersionedContentAvailable(contentName, desiredType, desiredContentDigest, desiredPublisher, verifier, timeout, handle);
        if (null == latestAvailableContent) {
            return null;
        }
        return CCNReader.isContentInRepository(latestAvailableContent, timeout, handle);
    }

    /**
	 * Checks to see if the named content or its latest version is available on the network; if it
	 * is, returns its first segment as a ContentObject. Actually should dereference links, as
	 * actual reads are done via input streams. Could remove our separate pull if we
	 * plumb content verifiers up through streams.
	 * @param contentName
	 * @param desiredType
	 * @param desiredContentDigest
	 * @param desiredPublisher 
	 * @param verifier
	 * @param timeout
	 * @return
	 * @throws IOException 
	 */
    public static ContentObject isVersionedContentAvailable(ContentName contentName, ContentType desiredType, byte[] desiredContentDigest, PublisherPublicKeyDigest desiredPublisher, ContentVerifier verifier, long timeout, CCNHandle handle) throws IOException {
        if (timeout == 0) {
            return null;
        }
        byte[] contentNameVersionComponent = VersioningProfile.cutTerminalVersion(contentName).second();
        ContentObject retrievedObject = null;
        if (null == contentNameVersionComponent) {
            retrievedObject = VersioningProfile.getFirstBlockOfLatestVersion(contentName, null, desiredPublisher, timeout, ((null != verifier) ? verifier : handle.keyManager().getDefaultVerifier()), handle);
        } else {
            retrievedObject = SegmentationProfile.getSegment(contentName, null, desiredPublisher, timeout, ((null != verifier) ? verifier : handle.keyManager().getDefaultVerifier()), handle);
        }
        if (null == retrievedObject) {
            Log.info(Log.FAC_IO, "isContentAvailable: no content available corresponding to {0}", contentName);
            return null;
        } else {
            if (Log.isLoggable(Log.FAC_IO, Level.FINER)) Log.finer(Log.FAC_IO, "isContentAvailable: found content {0} matching name {1}", retrievedObject.name(), contentName);
            if (null != desiredContentDigest) {
                CCNInputStream inputStream = new CCNInputStream(retrievedObject, null, handle);
                byte[] streamContent = CCNDigestHelper.digest(inputStream);
                if (!Arrays.equals(streamContent, desiredContentDigest)) {
                    Log.info(Log.FAC_IO, "Retrieved content {0} matching name {1}, but that stream's content is {2}, not expected {3}.", retrievedObject.name(), contentName, DataUtils.printBytes(streamContent), DataUtils.printBytes(desiredContentDigest));
                    return null;
                }
            }
            return retrievedObject;
        }
    }

    /**
	 * Is there anything available below this name that we can find in the available time?
	 * This really just wraps get, but puts a place for us to hang verification and other
	 * checks, and makes intent clear.
	 * @param contentPrefix Prefix content must start with.
	 * @param requiredPublisher Publisher that must have signed content, null for any.
	 * @param timeout How long to wait for content. If 0, returns immediately.
	 * @param handle
	 */
    public static ContentObject isAnyContentAvailable(ContentName contentPrefix, PublisherPublicKeyDigest requiredPublisher, long timeout, CCNHandle handle) throws IOException {
        if (timeout == 0) {
            return null;
        }
        ContentObject object = handle.get(contentPrefix, requiredPublisher, timeout);
        return object;
    }
}
