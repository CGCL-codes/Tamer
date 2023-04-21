package org.translationcomponent.api.impl.translator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.translationcomponent.api.Document;
import org.translationcomponent.api.DocumentFactory;
import org.translationcomponent.api.ResponseCode;
import org.translationcomponent.api.TranslationRequest;
import org.translationcomponent.api.TranslationRequestFactory;
import org.translationcomponent.api.TranslationResponse;
import org.translationcomponent.api.TranslationResponseFactory;
import org.translationcomponent.api.TranslatorService;
import org.translationcomponent.api.impl.response.ResponseStateException;

/**
 * Base class for other translators.
 * 
 * *
 * 
 * @author ROB
 * 
 */
public abstract class TranslatorServiceAbstract implements TranslatorService {

    protected final Log log = LogFactory.getLog(getClass());

    /**
	 * The translator to call to actually perform the translation. After this
	 * translator does its work it can pass the text or parts of the text to
	 * this translator
	 */
    private TranslatorService chainedTranslator;

    private DocumentFactory documentFactory;

    private TranslationRequestFactory requestFactory;

    private TranslationResponseFactory responseFactory;

    public void service(final TranslationRequest request, final TranslationResponse response) {
        Document document = documentFactory.create(request.getType(), request.getFullUrlWithQueryString(), request.getParameters(), request.getText(), response.getCharacterEncoding(), request);
        try {
            document.open();
            if (document.getStatus().getCode() == ResponseCode.OK) {
                this.serviceDocument(request, response, document);
            } else {
                response.setEndState(document.getStatus());
            }
        } catch (Exception e) {
            log.warn(request.getFullUrl(), e);
            response.setEndState(new ResponseStateException(e));
        } finally {
            document.close();
        }
    }

    protected abstract void serviceDocument(final TranslationRequest request, final TranslationResponse response, final Document document) throws Exception;

    public void setChainedTranslator(final TranslatorService chainedTranslator) {
        this.chainedTranslator = chainedTranslator;
    }

    public void setDocumentFactory(DocumentFactory documentFactory) {
        this.documentFactory = documentFactory;
    }

    public void setRequestFactory(TranslationRequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    public TranslatorService getChainedTranslator() {
        return chainedTranslator;
    }

    public TranslationRequestFactory getRequestFactory() {
        return requestFactory;
    }

    public DocumentFactory getDocumentFactory() {
        return documentFactory;
    }

    public TranslationResponseFactory getResponseFactory() {
        return responseFactory;
    }

    public void setResponseFactory(TranslationResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
    }
}
