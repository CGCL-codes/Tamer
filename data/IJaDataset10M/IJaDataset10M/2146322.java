package com.globant.google.mendoza.malbec;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

/** Validates an xml with the Google Checkout schema.
 */
public final class SchemaValidator {

    /** The class logger.
   */
    private static Log log = LogFactory.getLog(SchemaValidator.class);

    /** Validates the given xml with the Google Checkout schema.
   *
   * @param xml The xml to validate against the schema.
   *
   * @return The schema validation result.
   */
    public SchemaValidationResult validate(final String xml) {
        log.trace("Entering validate");
        if (xml == null) {
            log.trace("Leaving validate");
            throw new RuntimeException("The xml to validate cannot be null");
        }
        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        URL url = getClass().getResource("/xsd/apiv2.xsd");
        Schema schema = null;
        try {
            schema = factory.newSchema(url);
        } catch (SAXException e) {
            log.trace("Leaving validate");
            throw new RuntimeException(e);
        }
        Validator validator = schema.newValidator();
        StringReader reader = new StringReader(xml);
        Source source = new StreamSource(reader);
        SchemaValidationResult result = null;
        try {
            validator.validate(source);
            result = new SchemaValidationResult(true, "The xml is valid.");
        } catch (SAXException ex) {
            result = new SchemaValidationResult(false, ex.getMessage());
            log.debug("Validation: " + result);
            log.debug("Cause: " + result.getMessage());
            log.trace("Leaving validate");
        } catch (IOException e) {
            log.trace("Leaving validate");
            throw new RuntimeException(e);
        }
        log.trace("Leaving validate");
        return result;
    }

    /** The schema validation result.
   */
    public final class SchemaValidationResult {

        /** The validation message. */
        private String message;

        /** True if the xml is valid. */
        private boolean valid;

        /** Constructor. Creates an instance of SchemaValidationResult.
     *
     * @param isValid True if the xml is valid.
     *
     * @param validationMessage The validation result message.
     */
        public SchemaValidationResult(final boolean isValid, final String validationMessage) {
            valid = isValid;
            message = validationMessage;
        }

        /** True if the xml is valid.
     *
     * @return True if the xml is valid.
     */
        public boolean isXmlValid() {
            return valid;
        }

        /** Gets the validation message.
     *
     * @return The validation message.
     */
        public String getMessage() {
            return message;
        }
    }
}
