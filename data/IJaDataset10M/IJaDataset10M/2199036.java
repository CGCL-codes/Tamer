package com.koutra.dist.proc.faucet;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import com.koutra.dist.proc.model.ContentType;
import com.koutra.dist.proc.model.ISink;
import com.koutra.dist.proc.model.XformationException;

/**
 * A faucet implementation that can be used to drive an XML transformation pipeline.
 * When using the constructor that supplies a java.io.InputStream, the caller has
 * the responsibility of closing the stream once the transformation has completed.
 * 
 * @author Pafsanias Ftakas
 */
public class XMLFaucet extends AbstractFileOrStreamFaucet {

    private static final Logger logger = Logger.getLogger(XMLFaucet.class);

    protected InputStream inputStream;

    protected InputSource inputSource;

    /**
	 * @deprecated Use any of the initializing constructors instead.
	 */
    public XMLFaucet() {
    }

    /**
	 * Initializing constructor for the Stream type.
	 * @param id the ID of the faucet.
	 * @param is the input stream that the faucet is to use.
	 */
    public XMLFaucet(String id, InputStream is) {
        super(id);
        this.inputStream = is;
        this.inputSource = null;
    }

    /**
	 * Initializing constructor for the File type.
	 * @param id the ID of the faucet.
	 * @param path the path to the file to read from.
	 */
    public XMLFaucet(String id, String path) {
        super(id, path);
        this.inputStream = null;
        this.inputSource = null;
    }

    /**
	 * Implementation of the <code>IFaucet</code> interface.
	 * 
	 * @param contentType the type that we want this faucet to support.
	 * @return true iff this faucet supports the content type argument.
	 */
    @Override
    public boolean supportsOutput(ContentType contentType) {
        switch(contentType) {
            case XML:
                return true;
            case ByteStream:
            case CharStream:
            case ResultSet:
            default:
                return false;
        }
    }

    /**
	 * Override the implementation in the abstract faucet to add a check that the sink
	 * supports the proper content type.
	 */
    @Override
    protected void checkSinkValidity(ISink sink) {
        super.checkSinkValidity(sink);
        if (!sink.supportsInput(ContentType.XML)) throw new IllegalArgumentException("Sink '" + sink.getId() + "' must support the XML content type.");
    }

    /**
	 * Implementation of the <code>IFaucet</code> interface.
	 * 
	 * @param contentType the type that we want to get the source for.
	 * @return the proper source object for the content type requested.
	 */
    @Override
    public Object getSource(ContentType contentType) {
        switch(contentType) {
            case ByteStream:
            case CharStream:
            case ResultSet:
                return null;
        }
        if (inputSource != null) return inputSource;
        switch(type) {
            case File:
                try {
                    FileInputStream is = new FileInputStream(path);
                    inputStream = new BufferedInputStream(is);
                    if (logger.isTraceEnabled()) logger.trace("Using input stream " + inputStream + " wrapping input stream " + is + " reading from path " + path);
                } catch (FileNotFoundException e) {
                    throw new XformationException("Unable to create input stream", e);
                }
                break;
            case Stream:
                break;
        }
        this.inputSource = new InputSource(inputStream);
        return inputSource;
    }

    /**
	 * Implementation of the <code>IFaucet</code> interface.
	 */
    @Override
    public void dispose() {
        switch(type) {
            case File:
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new XformationException("Unable to close input stream", e);
                }
                break;
            case Stream:
                break;
        }
    }
}
