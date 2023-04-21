package org.obe.runtime.evaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.obe.engine.EngineContext;
import org.w3c.dom.Document;

/**
 * Various functions accessible to scripted expressions.
 *
 * @author Adrian Price
 */
public class RuntimeFunctions {

    private static final Log _logger = LogFactory.getLog(RuntimeFunctions.class);

    public static Document document(Object src) {
        return EngineContext.peekContext().getServiceManager().getDataConverter().toDocument(src);
    }

    /**
     * N.B. Although the service methods are all static, JXPath expects a
     * public no-args constructor.
     */
    public RuntimeFunctions() {
    }
}
