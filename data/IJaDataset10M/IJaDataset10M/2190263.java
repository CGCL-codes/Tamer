package issrg.ontology.importer;

import issrg.ontology.*;

public class PolicyImportException extends PolicyOntologyException {

    public PolicyImportException() {
        super();
    }

    public PolicyImportException(String message, Throwable cause) {
        super(message, cause);
    }

    public PolicyImportException(String message) {
        super(message);
    }

    public PolicyImportException(Throwable cause) {
        super(cause);
    }
}
