package org.jxpfw.jsp;

/**
 * Default, empty implementation for the class {@link AbstractJSPBean}.
 * <br>Extend this class when you only want to implement some of the abstract
 * methods in {@link AbstractJSPBean}.<br>
 * Maybe this class is not an Adapter in the strict GoF sense but it does
 * implement abstract methods with an empty implementation.
 * @author Kees Schotanus
 * @version 1.0 $Revision: 1.7 $
 */
public class JSPBeanAdapter extends AbstractJSPBean {

    /**
     * Universal version identifier for this serializable class.
     */
    private static final long serialVersionUID = 4083836135055712644L;

    /**
     * Empty implementation.
     * @throws Exception Only to allow a subclass implementation to throw an
     *  Exception.
     */
    @Override
    protected void processFirstPass() throws Exception {
    }

    /**
     * Empty implementation.
     * @throws Exception Only to allow a subclass implementation to throw an
     *  Exception.
     */
    @Override
    protected void processSubsequentPass() throws Exception {
    }

    /**
     * Empty implementation.
     * @throws Exception Only to allow a subclass implementation to throw an
     *  Exception.
     */
    @Override
    protected void processFooter() throws Exception {
    }
}
