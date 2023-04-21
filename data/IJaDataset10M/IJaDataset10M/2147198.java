package org.translationcomponent.service.document.parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.translationcomponent.api.impl.response.ResponseStateOk;
import org.translationcomponent.api.impl.test.EchoTranslator;
import org.translationcomponent.api.impl.test.RequestResponse;
import org.translationcomponent.api.impl.test.multithreaded.request.TestCallable;

public class ParserCallable implements TestCallable {

    private RequestResponse reqresp;

    private ParserFactory factory;

    private EchoTranslator echoTranslator;

    public RequestResponse call() throws Exception {
        Parser pt = factory.createParser();
        InputStream source = new ByteArrayInputStream(reqresp.getRequest().getText().getBytes(reqresp.getResponse().getCharacterEncoding()));
        try {
            pt.open(reqresp.getRequest(), reqresp.getResponse(), null, source, reqresp.getResponse().getCharacterEncoding(), echoTranslator);
            try {
                pt.parse();
            } finally {
                pt.close();
            }
        } finally {
            source.close();
        }
        reqresp.getResponse().setLastModified(reqresp.getExpectedResponse().getLastModified());
        reqresp.getResponse().setTranslationCount(reqresp.getExpectedResponse().getTranslationCount());
        reqresp.getResponse().setFailCount(reqresp.getExpectedResponse().getFailCount());
        if (!reqresp.getResponse().hasEnded()) {
            reqresp.getResponse().setEndState(ResponseStateOk.getInstance());
        }
        return reqresp;
    }

    public void init(RequestResponse reqresp, ParserFactory factory, EchoTranslator mockTranslator) {
        this.reqresp = reqresp;
        this.factory = factory;
        this.echoTranslator = mockTranslator;
    }
}
