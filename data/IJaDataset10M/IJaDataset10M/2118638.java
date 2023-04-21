package neembuu.directuploader;

/**
 * We have some additional properties associated with UploadableContent
 * like destinationFileOffset, that is why we have this extra class.
 * We might like to remove this, let 's ignore this for now.
 * @author Shashank Tulsyan
 */
public class UploadOperation {

    protected UploadableContent contentBeingUploaded;

    protected String uploadFileName;

    protected long byteAlreadyUploaded;

    protected long destinationFileOffset;

    public UploadOperation(UploadableContent contentBeingUploaded) {
        this(contentBeingUploaded, null, 0, 0);
    }

    public UploadOperation(UploadableContent contentBeingUploaded, String uploadFileName, long byteAlreadyUploaded, long destinationFileOffset) {
        this.contentBeingUploaded = contentBeingUploaded;
        this.uploadFileName = uploadFileName;
        this.byteAlreadyUploaded = byteAlreadyUploaded;
        this.destinationFileOffset = destinationFileOffset;
    }

    private UploadOperation(Builder b) {
        this.contentBeingUploaded = b.contentBeingUploaded;
        this.uploadFileName = b.uploadFileName;
        this.byteAlreadyUploaded = b.byteAlreadyUploaded;
        this.destinationFileOffset = b.destinationFileOffset;
    }

    public UploadableContent getContentBeingUploaded() {
        return contentBeingUploaded;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    /**
     * The filename that the uploaded file should have
     * This can be different from the original name of the file
     * Also, somtimes the UploadOperation may be derived from
     * non-File sources, in such case.
     * @return The name that the uploaded file should have
     */
    public String getUploadFileName() {
        if (uploadFileName == null) return getContentBeingUploaded().getFileName();
        return uploadFileName;
    }

    public long getBytesAlreadyUploaded() {
        return byteAlreadyUploaded;
    }

    public long getDestinationFileOffset() {
        return destinationFileOffset;
    }

    public static class Builder {

        private UploadableContent contentBeingUploaded;

        private String uploadFileName;

        private long byteAlreadyUploaded;

        private long destinationFileOffset;

        public Builder(UploadableContent contentBeingUploaded) {
            this.contentBeingUploaded = contentBeingUploaded;
        }

        public Builder setByteAlreadyUploaded(long byteAlreadyUploaded) {
            this.byteAlreadyUploaded = byteAlreadyUploaded;
            return this;
        }

        public Builder setDestinationFileOffset(long destinationFileOffset) {
            this.destinationFileOffset = destinationFileOffset;
            return this;
        }

        public Builder setUploadFileName(String uploadFileName) {
            this.uploadFileName = uploadFileName;
            return this;
        }

        public UploadOperation build() {
            return new UploadOperation(this);
        }
    }
}
