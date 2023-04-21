package com.meterware.httpunit.dom;

import org.mozilla.javascript.Function;
import org.mozilla.javascript.Context;

/**
 * the handler for HTML events
 * @author <a href="mailto:russgold@httpunit.org">Russell Gold</a>
 */
class HTMLEventHandler {

    private HTMLElementImpl _baseElement;

    private String _handlerName;

    private Function _handler;

    /**
     * create a handler for the given HTML Event
     * @param baseElement
     * @param handlerName
     */
    public HTMLEventHandler(HTMLElementImpl baseElement, String handlerName) {
        _baseElement = baseElement;
        _handlerName = handlerName;
    }

    /**
     * set the handler Function for this event Handler
     * @param handler
     */
    void setHandler(Function handler) {
        _handler = handler;
    }

    /**
     * get the (cached) handler Function for this event Handler
     * on first access compile the function
     * @return
     */
    Function getHandler() {
        if (_handler == null) {
            String attribute = _baseElement.getAttributeWithNoDefault(_handlerName);
            if (attribute != null && Context.getCurrentContext() != null) {
                _handler = Context.getCurrentContext().compileFunction(_baseElement, "function " + AbstractDomComponent.createAnonymousFunctionName() + "() { " + attribute + "}", "httpunit", 0, null);
            }
        }
        return _handler;
    }
}
