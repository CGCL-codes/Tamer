package org.dicom4j.dicom.UniqueIdentifiers.support;

import java.util.TreeMap;
import org.dicom4j.dicom.CodedAttributes.support.CodedAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for list of {@link UniqueIdentifier UniqueIdentifier}
 *
 * @since 0.0.2
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte 
 *
 */
public abstract class UniqueIdentifiers {

    public UniqueIdentifiers() {
        fList = new TreeMap();
        addUIDs();
    }

    /**
	 * Return a {@link CodedAttribute CodedAttribute } 
	 * @param aIndex the index
	 * @return the CodedAttribute
	 */
    public UniqueIdentifier getByUID(String aUID) {
        fLogger.debug("getByUID: " + aUID);
        return (UniqueIdentifier) fList.get(aUID.trim());
    }

    /**
	 * return the Name associated to the UID
	 * @param aUID
	 * @return
	 */
    public String getName(String aUID) {
        UniqueIdentifier lUID = getByUID(aUID);
        if (lUID != null) {
            return lUID.getName();
        } else {
            return aUID;
        }
    }

    /**
	 * return true if an Object is associated to the UID
	 * @param aUID
	 * @return
	 */
    public boolean isSupported(String aUID) {
        if (getByUID(aUID) != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * add one {@link UniqueIdentifier}
	 * @param aUniqueIdentifier the UniqueIdentifier
	 * @return the added UniqueIdentifier
	 */
    public UniqueIdentifier addUID(UniqueIdentifier aUniqueIdentifier) {
        fList.put(aUniqueIdentifier.getUID(), aUniqueIdentifier);
        return aUniqueIdentifier;
    }

    public int count() {
        return fList.size();
    }

    protected abstract void addUIDs();

    private TreeMap fList;

    private static Logger fLogger = LoggerFactory.getLogger(UniqueIdentifiers.class);
}
