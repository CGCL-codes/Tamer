package org.brandao.brutos.web;

import org.brandao.brutos.MvcResponse;
import org.brandao.brutos.MvcResponseFactory;

/**
 *
 * @author Afonso Brandao
 */
public class WebMvcResponseFactory extends MvcResponseFactory {

    protected MvcResponse getNewResponse() {
        RequestInfo requestInfo = RequestInfo.getCurrentRequestInfo();
        return new WebMvcResponseImp(requestInfo.getResponse());
    }
}
