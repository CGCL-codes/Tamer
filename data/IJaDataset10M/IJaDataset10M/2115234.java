package cx.ath.contribs.internal.xerces.xinclude;

import cx.ath.contribs.internal.xerces.xni.NamespaceContext;

/**
 * This is an implementation of NamespaceContext which is intended to be used for
 * XInclude processing.  It enables each context to be marked as invalid, if necessary,
 * to indicate that the namespaces recorded on those contexts won't be apparent in the
 * resulting infoset.
 * 
 * @author Peter McCracken, IBM
 * 
 * @version $Id: XIncludeNamespaceSupport.java,v 1.1 2007/06/02 09:58:58 paul Exp $
 */
public class XIncludeNamespaceSupport extends MultipleScopeNamespaceSupport {

    /**
     * This stores whether or not the context at the matching depth was valid.
     */
    private boolean[] fValidContext = new boolean[8];

    /**
     * 
     */
    public XIncludeNamespaceSupport() {
        super();
    }

    /**
     * @param context
     */
    public XIncludeNamespaceSupport(NamespaceContext context) {
        super(context);
    }

    /**
     * Pushes a new context onto the stack.
     */
    public void pushContext() {
        super.pushContext();
        if (fCurrentContext + 1 == fValidContext.length) {
            boolean[] contextarray = new boolean[fValidContext.length * 2];
            System.arraycopy(fValidContext, 0, contextarray, 0, fValidContext.length);
            fValidContext = contextarray;
        }
        fValidContext[fCurrentContext] = true;
    }

    /**
     * This method is used to set a context invalid for XInclude namespace processing.
     * Any context defined by an &lt;include&gt; or &lt;fallback&gt; element is not
     * valid for processing the include parent's [in-scope namespaces]. Thus, contexts
     * defined by these elements are set to invalid by the XInclude processor using
     * this method.
     */
    public void setContextInvalid() {
        fValidContext[fCurrentContext] = false;
    }

    /**
     * This returns the namespace URI which was associated with the given pretext, in
     * the context that existed at the include parent of the current element.  The
     * include parent is the last element, before the current one, which was not set
     * to an invalid context using setContextInvalid()
     * 
     * @param prefix the prefix of the desired URI
     * @return the URI corresponding to the prefix in the context of the include parent
     */
    public String getURIFromIncludeParent(String prefix) {
        int lastValidContext = fCurrentContext - 1;
        while (lastValidContext > 0 && !fValidContext[lastValidContext]) {
            lastValidContext--;
        }
        return getURI(prefix, lastValidContext);
    }
}
