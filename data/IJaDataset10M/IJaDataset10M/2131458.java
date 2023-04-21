package org.merlin.xmlv;

import java.util.*;
import org.jdom.*;
import org.merlin.util.Logger;

public class XMLValidator {

    private StringBuffer errorBuf = new StringBuffer();

    private ElementValidator elementValidator;

    private DocumentValidator documentValidator;

    private Logger logger;

    public XMLValidator() {
        logger = new Logger();
        elementValidator = new ElementValidator(logger);
        documentValidator = new DocumentValidator(logger);
    }

    public ElementValidator getElementValidator() {
        return elementValidator;
    }

    public void setElementValidator(ElementValidator elementValidator) {
        this.elementValidator = elementValidator;
    }

    public DocumentValidator getDocumentValidator() {
        return documentValidator;
    }

    public void setDocumentValidator(DocumentValidator documentValidator) {
        this.documentValidator = documentValidator;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public List getErrors() {
        return logger.getErrors();
    }

    public List getWarnings() {
        return logger.getWarnings();
    }

    public boolean validate(Document doc, Document descriptor) {
        logger.reset();
        documentValidator.validate(doc, descriptor);
        elementValidator.validate(doc.getRootElement(), descriptor.getRootElement());
        return !logger.hasErrors();
    }
}
