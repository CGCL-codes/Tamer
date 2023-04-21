package com.volantis.mcs.accessors.xml.jibx;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.shared.content.CachingContentInput;
import com.volantis.shared.content.ContentInput;
import com.volantis.shared.jibx.ContentUnmarshaller;
import com.volantis.shared.throwable.ExtendedIOException;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.xml.schema.validator.SchemaValidator;
import org.jibx.runtime.JiBXException;
import org.xml.sax.SAXException;
import java.io.IOException;

/**
 * Used to read a repository object structure from a given Reader object.
 *
 * @mock.generate 
 */
public class JiBXReader {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer = LocalizationFactory.createExceptionLocalizer(JiBXReader.class);

    private final Class expectedClass;

    private SchemaValidator schemaValidator;

    /**
     * Initialise.
     * <p>
     * Note that no validation will be done by default.
     * <p>
     * JiBXReader needs an expected class to initiate the reading process and
     * to check the result of read. This class cannot be <code>null</code>.
     *
     * @param expectedClass the class of objects this reader will read
     */
    public JiBXReader(Class expectedClass) {
        this(expectedClass, null);
    }

    /**
     * Initialise.
     *
     * @param expectedClass     the class that will be read by this reader
     * @param validator         the SchemaValidator to use
     */
    public JiBXReader(Class expectedClass, SchemaValidator validator) {
        if (expectedClass == null) {
            throw new IllegalArgumentException("Expected class cannot be null.");
        }
        this.expectedClass = expectedClass;
        this.schemaValidator = validator;
    }

    /**
     * Unmarshall a Theme or ComponentContainer object structure using JIBX.
     * JIBX will expected to be able to read valid XML from the supplied
     * Reader. The reader should provide the complete XML document including
     * the header and namespace.
     *
     * @param content from which the XML will be read
     * @param name
     * @return a Theme or ComponentContainer object
     * @throws IOException
     */
    public Object read(ContentInput content, String name) throws IOException {
        try {
            if (schemaValidator != null) {
                CachingContentInput cachingContent = new CachingContentInput(content);
                schemaValidator.validate(cachingContent);
                content = cachingContent.getCachedContent();
            }
            Object readObject = null;
            ContentUnmarshaller contentUnmarshaller = new ContentUnmarshaller(expectedClass);
            readObject = contentUnmarshaller.unmarshallContent(content, name);
            return readObject;
        } catch (JiBXException e) {
            throw new ExtendedIOException(exceptionLocalizer.format("cannot-read-object", new Object[] { expectedClass.getName(), content }), e);
        } catch (SAXException e) {
            throw new ExtendedIOException(exceptionLocalizer.format("cannot-read-object", new Object[] { expectedClass.getName(), content }), e);
        }
    }
}
