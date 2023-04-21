package uk.co.lakesidetech.springxmldb.util;

/**
 * <p>
 * A set of utils for dealing with XML database collection path strings
 * </p>
 * @author Stuart Eccles
 */
public class CollectionPathUtils {

    private static String COLLECTION_SEPERATOR = "/";

    private static String ROOT_COLLECTION = "db";

    /**
	 * given a collection full path return the actual name of the collection
	 * @param fullPath the collection path
	 * @return the collection name
	 */
    public static String findCollectionNameFromFullPath(String fullPath) {
        int begin = fullPath.lastIndexOf(COLLECTION_SEPERATOR);
        if (begin == -1) {
            begin = 0;
        } else {
            begin++;
        }
        int end = fullPath.length();
        String collectionName = fullPath.substring(begin, end);
        return collectionName;
    }

    /**
	 * find the containing collection of a collection given its full path
	 * @param fullPath the collection full path
	 * @return the containing collection path
	 */
    public static String findContainingCollectionFromFullPath(String fullPath) {
        int end = fullPath.lastIndexOf(COLLECTION_SEPERATOR);
        if (end == -1) {
            end = 0;
        }
        String collectionPath = fullPath.substring(0, end);
        return collectionPath;
    }

    /**
	 * return the collection path without the root database collection
	 * @param fullPath the colection path
	 * @return path without the root
	 */
    public static String findCollectionPathWithoutRootFromFullPath(String fullPath) {
        return fullPath.substring((COLLECTION_SEPERATOR + ROOT_COLLECTION).length());
    }
}
