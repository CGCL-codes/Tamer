package com.nimbusinformatics.genomicstransfer.amazon.s3;

import java.io.File;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ProgressEvent;
import com.amazonaws.services.s3.model.ProgressListener;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.Upload;
import com.nimbusinformatics.genomicstransfer.Settings;
import com.nimbusinformatics.genomicstransfer.util.AbstractOperation;

/**
 * Operation for uploading data to S3 bucket.
 */
public class S3UploadOperation extends AbstractOperation {

    private final File sourceFile;

    private final String destinationName;

    /**
   * S3 upload structure.
   */
    private Upload upload;

    /**
   * Creates new operation.
   * 
   * @param sourceFile
   *          source file in local file system
   * @param destinationName
   *          destination file name to upload to
   */
    public S3UploadOperation(File sourceFile, String destinationName) {
        this.sourceFile = sourceFile;
        this.destinationName = destinationName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    @Override
    protected void execute() throws Exception {
        setTotal(sourceFile.length());
        ProgressListener progressListener = new ProgressListener() {

            public void progressChanged(ProgressEvent progressEvent) {
                if (upload == null) {
                    return;
                }
                setCompleted((int) upload.getProgress().getBytesTransfered());
            }
        };
        PutObjectRequest request = new PutObjectRequest(Settings.getInstance().getS3BucketName(), destinationName, sourceFile).withProgressListener(progressListener);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setServerSideEncryption(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
        request.setMetadata(objectMetadata);
        upload = S3Client.getTransferManager().upload(request);
        upload.waitForCompletion();
    }
}
