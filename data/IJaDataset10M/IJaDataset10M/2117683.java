package com.koutra.dist.proc.sink;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import com.koutra.dist.proc.model.ContentType;
import com.koutra.dist.proc.model.IFaucet;
import com.koutra.dist.proc.model.IPipelineItem;
import com.koutra.dist.proc.model.XformationException;

/**
 * A sink implementation that drives the SAX XML transformation. When using the
 * constructor that supplies the output stream, the caller has the responsibility
 * of closing the output stream when the transformation is completed.
 * 
 * @author Pafsanias Ftakas
 */
public class XMLSink extends AbstractFileOrStreamSink {

    private static final Logger logger = Logger.getLogger(XMLSink.class);

    protected Object faucetObject;

    protected OutputStream outputStream;

    /**
	 * @deprecated Use any of the initializing constructors instead.
	 */
    public XMLSink() {
    }

    /**
	 * Initializing constructor for the Stream type.
	 * @param id the ID of the sink.
	 * @param os the output stream to write to.
	 */
    public XMLSink(String id, OutputStream os) {
        super(id);
        this.faucetObject = null;
        this.outputStream = os;
    }

    /**
	 * Initializing constructor for the File type.
	 * @param id the ID of the sink.
	 * @param path the path to the file to write to.
	 */
    public XMLSink(String id, String path) {
        super(id, path);
        this.faucetObject = null;
        this.outputStream = null;
    }

    /**
	 * Implementation of the <code>ISink</code> interface.
	 * 
	 * @param contentType the type that we want this sink to support.
	 * @return true iff this sink supports the content type argument.
	 */
    @Override
    public boolean supportsInput(ContentType contentType) {
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
	 * Override the implementation in the abstract sink to add a check that the faucet
	 * supports the proper content type.
	 */
    @Override
    protected void checkFaucetValidity(IFaucet faucet) {
        super.checkFaucetValidity(faucet);
        if (!faucet.supportsOutput(ContentType.XML)) throw new IllegalArgumentException("Faucet '" + faucet.getId() + "' must support the XML content type.");
    }

    /**
	 * Implementation of the <code>ISink</code> interface.
	 */
    @Override
    public void registerSource(Object source) {
    }

    /**
	 * Implementation of the <code>ISink</code> interface.
	 */
    @Override
    public String dumpPipeline() {
        return getClass().getName() + ": " + faucetObject + "->" + outputStream;
    }

    /**
	 * Implementation of the <code>ISink</code> interface.
	 */
    @Override
    public void dispose() {
        switch(type) {
            case File:
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new XformationException("Unable to close output stream", e);
                }
                break;
            case Stream:
                break;
        }
        faucet.dispose();
    }

    /**
	 * Implementation of the <code>ISink</code> interface.
	 */
    @Override
    public void consume() {
        if (!hookedUp) throw new XformationException("Sink has not been set up correctly: " + "faucet has not been set");
        switch(type) {
            case File:
                try {
                    outputStream = new BufferedOutputStream(new FileOutputStream(path));
                } catch (FileNotFoundException e) {
                    throw new XformationException("Unable to create output stream", e);
                }
                break;
            case Stream:
                break;
        }
        StreamResult result = new StreamResult(outputStream);
        InputSource input = null;
        if (faucet instanceof IPipelineItem) {
            input = (InputSource) ((IPipelineItem) faucet).consume(this);
        }
        try {
            faucetObject = faucet.getSource(ContentType.XML);
            if (logger.isTraceEnabled()) logger.trace("Sink is using reader: " + faucetObject);
            SAXTransformerFactory stf = (SAXTransformerFactory) TransformerFactory.newInstance();
            Transformer transformer = stf.newTransformer();
            SAXSource transformSource = new SAXSource((XMLReader) faucetObject, input);
            transformer.transform(transformSource, result);
        } catch (Exception e) {
            throw new XformationException("Unable to set up transform", e);
        }
    }
}
