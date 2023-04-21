package net.sf.storehaus.s3filesystem.amazon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import net.sf.storehaus.s3filesystem.S3Bucket;
import net.sf.storehaus.s3filesystem.S3FileSystem;
import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.security.AWSCredentials;

/**
 * An S3 file system that saves all objects in the Amazon S3 service.  
 * 
 * @author Ted Stockwell
 */
public class AmazonS3FileSystem implements S3FileSystem {

    RestS3Service _restS3Service;

    String _hostname;

    String _accessKey;

    String _secretKey;

    public AmazonS3FileSystem(String accessKey, String secretKey) throws S3ServiceException {
        AWSCredentials credentials = new AWSCredentials(accessKey, secretKey);
        _restS3Service = new RestS3Service(credentials);
    }

    public String getName() {
        return S3Service.getS3EndpointHost();
    }

    public Collection<S3Bucket> getAllBuckets() throws IOException {
        try {
            org.jets3t.service.model.S3Bucket[] jet3tBuckets = _restS3Service.listAllBuckets();
            ArrayList<S3Bucket> buckets = new ArrayList<S3Bucket>();
            for (int i = 0; i < jet3tBuckets.length; i++) {
                buckets.add(new AmazonS3Bucket(this, jet3tBuckets[i].getName()));
            }
            return buckets;
        } catch (S3ServiceException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    public S3Bucket getBucket(String bucketName) throws IOException {
        return new AmazonS3Bucket(this, bucketName);
    }

    public String getCanonicalName() throws IOException {
        return _hostname;
    }
}
