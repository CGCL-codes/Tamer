package net.sourceforge.poi.serialization.poifs;

import net.sourceforge.poi.cocoon.serialization.elementprocessor.ElementProcessor;
import net.sourceforge.poi.cocoon.serialization.elementprocessor.ElementProcessorSerializer;
import org.apache.cocoon.serialization.Serializer;
import net.sourceforge.poi.cocoon.serialization.elementprocessor.ElementProcessorFactory.CannotCreateElementProcessorException;
import net.sourceforge.poi.poifs.filesystem.POIFSFileSystem;
import net.sourceforge.poi.util.POILogger;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * An extension of ElementProcessorSerializer with extensions for
 * dealing with the POIFS filesystem
 *
 * This is an abstract class. Concrete extensions need to implement
 * the following methods:
 * <ul>
 *    <li>String getMimeType()</li>
 *    <li>void doLocalPreEndDocument()</li>
 *    <li>void doLocalPostEndDocument()</li>
 *    <li>ElementProcessorFactory getElementProcessorFactory()</li>
 *    <li>POILogger doGetLoggerForElementProcessor(ElementProcessor
 *                                                 processor)</li>
 * </ul>
 *
 * @author Marc Johnson (marc_johnson27591@hotmail.com)
 */
public abstract class POIFSSerializer extends ElementProcessorSerializer {

    private POIFSFileSystem _filesystem;

    /**
     * Constructor
     */
    public POIFSSerializer() {
        super();
        _filesystem = new POIFSFileSystem();
    }

    /**
     * Extending classes should do whatever they need to do prior to
     * writing the filesystem out
     */
    protected abstract void doLocalPreEndDocument();

    /**
     * Extending classes should do whatever they need to do after
     * writing the filesystem out
     */
    protected abstract void doLocalPostEndDocument();

    /**
     * Provide a POILogger for the specified ElementProcessor
     *
     * @param processor the ElementProcessor
     *
     * @return a POILogger; must not be null
     */
    protected abstract POILogger doGetLoggerForElementProcessor(ElementProcessor processor);

    /**
     * Provide access to the filesystem for extending classes
     *
     * @return the filesystem
     */
    protected POIFSFileSystem getFilesystem() {
        return _filesystem;
    }

    /**
     * perform pre-initialization on an element processor
     *
     * @param processor the element processor to be iniitialized
     *
     * @exception SAXException on errors
     */
    protected void doPreInitialization(ElementProcessor processor) throws SAXException {
        try {
            ((POIFSElementProcessor) processor).setFilesystem(_filesystem);
            ((POIFSElementProcessor) processor).setLogger(doGetLoggerForElementProcessor(processor));
        } catch (ClassCastException e) {
            throw SAXExceptionFactory("could not pre-initialize processor", e);
        }
    }

    /**
     * Receive notification of the end of a document.
     *
     * @exception SAXException if there is an error writing the
     *            document to the output stream
     */
    public void endDocument() throws SAXException {
        doLocalPreEndDocument();
        OutputStream stream = getOutputStream();
        if (stream != null) {
            try {
                _filesystem.writeFilesystem(stream);
            } catch (IOException e) {
                throw SAXExceptionFactory("could not process endDocument event", e);
            }
        } else {
            throw SAXExceptionFactory("no outputstream for writing the document!!");
        }
        doLocalPostEndDocument();
    }
}
