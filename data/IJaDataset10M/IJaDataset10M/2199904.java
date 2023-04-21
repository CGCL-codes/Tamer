package com.tenline.pinecone.platform.model;

import java.io.Serializable;

/**
 * @author Bill
 *
 */
@SuppressWarnings("serial")
public class Message implements Serializable {

    private String characterEncoding;

    private String contentType;

    private int contentLength;

    private byte[] contentBytes;

    /**
	 * 
	 */
    public Message() {
    }

    /**
	 * @param characterEncoding the characterEncoding to set
	 */
    public void setCharacterEncoding(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    /**
	 * @return the characterEncoding
	 */
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    /**
	 * @param contentType the contentType to set
	 */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
	 * @return the contentType
	 */
    public String getContentType() {
        return contentType;
    }

    /**
	 * @param contentLength the contentLength to set
	 */
    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    /**
	 * @return the contentLength
	 */
    public int getContentLength() {
        return contentLength;
    }

    /**
	 * @param contentBytes the contentBytes to set
	 */
    public void setContentBytes(byte[] contentBytes) {
        this.contentBytes = contentBytes;
    }

    /**
	 * @return the contentBytes
	 */
    public byte[] getContentBytes() {
        return contentBytes;
    }
}
