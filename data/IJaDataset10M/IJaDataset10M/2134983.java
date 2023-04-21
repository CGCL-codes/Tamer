package com.bss.impactreplacer.result;

/**
 * impactreplacer exception
 * @author bearocean
 *
 */
public class ImpactReplacerException {

    private String absoluteFilePath = null;

    private String exceptionDes = null;

    public String getAbsoluteFilePath() {
        return absoluteFilePath;
    }

    public void setAbsoluteFilePath(String absoluteFilePath) {
        this.absoluteFilePath = absoluteFilePath;
    }

    public String getExceptionDes() {
        return exceptionDes;
    }

    public void setExceptionDes(String exceptionDes) {
        this.exceptionDes = exceptionDes;
    }

    /**
	 * constructor
	 * @param absoluteFilePath
	 * @param exceptionDes
	 */
    public ImpactReplacerException(String absoluteFilePath, String exceptionDes) {
        this.absoluteFilePath = absoluteFilePath;
        this.exceptionDes = exceptionDes;
    }

    /**
	 * convert ImpactReplacerException to string message
	 */
    public String toString() {
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append("File:");
        sbBuffer.append(this.absoluteFilePath);
        sbBuffer.append(" Failed because of ");
        sbBuffer.append(this.exceptionDes);
        return sbBuffer.toString();
    }
}
