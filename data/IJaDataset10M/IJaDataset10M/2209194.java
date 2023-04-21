package com.acciente.induction.init.config.xmlconfigloader;

import com.acciente.commons.lang.Strings;
import com.acciente.induction.init.config.Config;
import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;

/**
 * Internal.
 * RequestInterceptorsRule
 *
 * @created Aug 30, 2009
 *
 * @author Adinath Raveendra Raj
 */
public class RequestInterceptorsRule extends Rule {

    private Config.RequestInterceptors _oRequestInterceptors;

    public RequestInterceptorsRule(Config.RequestInterceptors oRequestInterceptors) {
        _oRequestInterceptors = oRequestInterceptors;
    }

    public AddRequestInterceptorRule createAddRequestInterceptorRule() {
        return new AddRequestInterceptorRule();
    }

    public class AddRequestInterceptorRule extends Rule {

        private String _sClassName;

        public void begin(String sNamespace, String sName, Attributes oAttributes) {
            _sClassName = null;
        }

        public void end(String sNamespace, String sName) throws XMLConfigLoaderException {
            if (Strings.isEmpty(_sClassName)) {
                throw new XMLConfigLoaderException("config > request-interceptors > requestinterceptor: class is a required attribute");
            }
            _oRequestInterceptors.addRequestInterceptor(_sClassName);
        }

        public ParamClassRule createParamClassRule() {
            return new ParamClassRule();
        }

        private class ParamClassRule extends Rule {

            public void body(String sNamespace, String sName, String sText) {
                _sClassName = sText;
            }
        }
    }
}
