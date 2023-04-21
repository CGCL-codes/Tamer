package net.sf.s34j.defines;

/**
 *  Static constants useful for working with S3.
 */
public class S3Constants {

    /**
     *  The namespace for metadata returned from S3 requests such as "list keys".
     */
    public static final String S3_NAMESPACE = "http://s3.amazonaws.com/doc/2006-03-01/";

    /** The object's key */
    public static final String BUCKETLIST_KEY = "Key";

    /** The object's last modification timestamp */
    public static final String BUCKETLIST_LASTMOD = "LastModified";

    /** The object's Content-Length */
    public static final String BUCKETLIST_SIZE = "Size";

    /** The object's ETag */
    public static final String BUCKETLIST_ETAG = "ETag";
}
