package net.sf.liwenx.impl;

import java.io.InputStream;
import javax.activation.MimeType;

/**
 * 
 * @author Alejandro Guerra Cabrera
 * 
 */
public class BinaryResponse extends AbstractLiwenxResponse {

    private InputStream inputStream;

    private MimeType mimeType;

    /**
	 * Constructor
	 * 
	 * @param inputStream - intputStream from which read bytes of response.
	 * @param mimeType - mime type.
	 */
    BinaryResponse(InputStream inputStream, MimeType mimeType) {
        super();
        this.inputStream = inputStream;
        this.mimeType = mimeType;
    }

    /**
	 * Constructor
	 * 
	 * @param statusCode - status code.
	 * @param inputStream - intputStream from which read bytes of response.
	 * @param mimeType - mime type.
	 */
    BinaryResponse(int statusCode, InputStream inputStream, MimeType mimeType) {
        super(statusCode);
        this.inputStream = inputStream;
        this.mimeType = mimeType;
    }

    /**
	 * @return the inputStream
	 */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
	 * @return the mimeType
	 */
    public MimeType getMimeType() {
        return mimeType;
    }
}
