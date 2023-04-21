package org.xaware.server.engine.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jdom.Attribute;
import org.jdom.Document;
import org.xaware.server.engine.IBizViewContext;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.XAwareException;

/**
 * 
 * @author hcurtis
 */
public class StreamingBizDocContext extends BizDocContext {

    private BizDocContext bdContext = null;

    private Map<String, Object> params = null;

    /**
     * Configure this context as a simple BizDocContext to execute the referenced instreaming BizDocument
     * 
     * @param bdContext
     *            the instreaming BizDocument or derivation of it
     * @throws XAwareException
     */
    public StreamingBizDocContext(final IBizViewContext bdContext) throws XAwareException {
        super("StreamingContext of " + bdContext.getBizViewName(), StreamingHelper.getInStreamJdom(bdContext), null, (Document) null, bdContext.getRegistry());
        if (bdContext instanceof BizDocContext) {
            this.bdContext = (BizDocContext) bdContext;
            this.params = new HashMap<String, Object>(bdContext.getBizParams());
        } else {
            throw new XAwareException("Invalid context class, " + bdContext.getClass().getName() + ", for streaming");
        }
        setOutStreamConfig(bdContext.getOutStreamConfig());
        if (getOutStreamConfig() != null) {
            bdContext.getScriptRoot().setAttribute(new Attribute(XAwareConstants.XAWARE_ATTR_REMOVE, XAwareConstants.XAWARE_YES, XAwareConstants.xaNamespace));
        }
        bdContext.setOutStreamConfig(null);
    }

    /**
     * Accessor for the original input parameters provided to the streaming BizDoc.
     * 
     * @return a Map of input parameters to the streaming BizDoc.
     */
    public Map<String, Object> getParams() {
        return Collections.unmodifiableMap(params);
    }

    /**
     * Accessor for the BizDocContext wrapped by this StreamingBizDocContext.
     * 
     * @return a reference to the BizDocContext wrapped by this StreamingBizDocContext.
     */
    public BizDocContext getBdContext() {
        return bdContext;
    }

    /**
     * Override so that we can add the results from this execution to the original bizdoc
     * context
     * @throws XAwareException
     */
    @Override
    public void endPassProcessing() throws XAwareException {
        super.endPassProcessing();
        this.getScriptRoot().removeNamespaceDeclaration(XAwareConstants.xaNamespace);
        bdContext.setScriptRoot(this.getScriptRoot());
    }
}
