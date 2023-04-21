package com.funambol.toolbox.mime;

import com.funambol.common.pim.converter.ConverterException;
import com.funambol.framework.logging.FunambolLogger;
import java.util.TimeZone;

public abstract class MimeConverter extends MimeType {

    private String label;

    private String mimeType;

    private String version;

    public MimeConverter() {
    }

    public MimeConverter(String mimeType, String version) {
        this("", mimeType, version);
    }

    public MimeConverter(String label, String mimeType, String version) {
        this.label = label;
        this.mimeType = mimeType;
        this.version = version;
    }

    public String getLabel() {
        return label;
    }

    public String getVersion() {
        return version;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String toString() {
        return super.toString() + "[" + mimeType + "/" + version + "]";
    }

    /**
     * The timezone that is suggested to be used by the converter.
     */
    protected String tzString = null;

    protected TimeZone timezone = null;

    public void setTimezone(String tz) {
        if (tz != null) {
            this.tzString = tz;
            timezone = TimeZone.getTimeZone(tz);
        }
    }

    /**
     * The charset that is suggested to be used by the converter.
     */
    protected String charset = null;

    public void setCharset(String charset) {
        this.charset = charset;
    }

    /**
     * The encoding property representing the encoding that is used for the
     * item within the SyncML protocol.
     */
    protected String encoding = null;

    /**
     * getter for the encoding.
     */
    public String getEncoding() {
        return encoding;
    }

    public abstract String itemToDataStorage(byte[] in, FunambolLogger log) throws ConverterException;

    public abstract byte[] dataStorageToItem(byte[] in, FunambolLogger log) throws ConverterException;
}
