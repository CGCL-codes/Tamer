package org.tolk.util.extension.ipico;

/**
 * @author Werner van Rensburg
 *
 */
public class ReaderVo {

    private String readerId;

    private String readerName;

    public ReaderVo(String readerId, String readerName) {
        this.readerId = readerId;
        this.readerName = readerName;
    }

    public ReaderVo(String readerId) {
        this.readerId = readerId;
        this.readerName = "";
    }

    public ReaderVo() {
        this.readerId = "";
        this.readerName = "";
    }

    /**
     * ReaderID as per IRSP substring pos 2-3
     * 
     * @param readerId the readerID to set
     */
    public void setReaderId(String readerId) {
        this.readerId = readerId;
    }

    /**
     * @return the readerId
     */
    public String getReaderId() {
        return readerId;
    }

    /**
     * Name/Descriptor of the Reader
     * 
     * @param readerName the name to set
     */
    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    /**
     * @return the readerName
     */
    public String getReaderName() {
        return readerName;
    }
}
