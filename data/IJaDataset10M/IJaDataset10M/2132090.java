package er.luceneadaptor;

import com.webobjects.eoaccess.EOGeneralAdaptorException;

public class ERLuceneAdaptorException extends EOGeneralAdaptorException {

    public ERLuceneAdaptorException(String message, Throwable throwable) {
        super(message);
    }

    public ERLuceneAdaptorException(String message) {
        super(message);
    }
}
