package net.zehrer.vse.model.jaxws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import net.zehrer.vse.common.Authentication;
import net.zehrer.vse.common.ICredential;
import net.zehrer.vse.common.TimeStampFactory;
import net.zehrer.vse.model.IStorageService;
import net.zehrer.vse.model.StorageException;
import com.amazonaws.s3.doc._2006_03_01.AmazonS3;
import com.amazonaws.s3.doc._2006_03_01.AmazonS3_Service;
import com.amazonaws.s3.doc._2006_03_01.ListAllMyBucketsEntry;
import com.amazonaws.s3.doc._2006_03_01.ListAllMyBucketsList;
import com.amazonaws.s3.doc._2006_03_01.ListAllMyBucketsResult;

public class S3Service implements IStorageService {

    private final String AMAZONS3 = "AmazonS3";

    private TimeStampFactory timeStampFactory = null;

    private Authentication securityProvider = null;

    private ICredential credentialProvider = null;

    private AmazonS3 service = null;

    public S3Service() {
        this.service = new AmazonS3_Service().getAmazonS3();
    }

    private List<String> getBucketList() {
        List<String> result = new ArrayList<String>();
        XMLGregorianCalendar timestamp = timeStampFactory.createTimeStamp();
        String signature = getSignature("ListAllMyBuckets", timestamp.toString());
        ListAllMyBucketsResult serviceResult = this.service.listAllMyBuckets(this.credentialProvider.getUserID(), timestamp, signature);
        ListAllMyBucketsList bucketList = serviceResult.getBuckets();
        for (ListAllMyBucketsEntry entry : bucketList.getBucket()) {
            result.add(entry.getName());
        }
        return result;
    }

    private String getSignature(String operation, String timeStamp) {
        StringBuffer result = new StringBuffer();
        result.append(AMAZONS3);
        result.append(operation);
        result.append(timeStamp);
        securityProvider.setKey(this.credentialProvider.getPass());
        return securityProvider.sign(result.toString());
    }

    public String testConnection() {
        try {
            getBucketList();
        } catch (Exception e) {
            return e.getMessage();
        }
        return null;
    }

    public List<String> getStorageList() throws StorageException {
        try {
            return getBucketList();
        } catch (Exception e) {
            throw new StorageException(e.getMessage());
        }
    }

    public void setTimeStampFactory(TimeStampFactory timeStampFactory) {
        this.timeStampFactory = timeStampFactory;
    }

    public void setSecurityProvider(Authentication securityProvider) {
        this.securityProvider = securityProvider;
    }

    public void setCredentialProvider(ICredential credentialProvider) {
        this.credentialProvider = credentialProvider;
    }
}
