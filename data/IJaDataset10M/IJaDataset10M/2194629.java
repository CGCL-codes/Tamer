package org.jecars.wfplugin;

import java.util.ArrayList;
import java.util.List;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import org.jecars.tools.workflow.IWF_Context;

/**
 *
 * @author weert
 */
public class WFP_Context implements IWFP_Context {

    private final transient IWF_Context mContext;

    private final transient List<IWFP_Input> mInputs = new ArrayList<IWFP_Input>();

    /** WFP_Context
   * 
   * @param pContext 
   */
    public WFP_Context(final IWF_Context pContext) throws RepositoryException {
        mContext = pContext;
        for (final Node dn : mContext.getDataNodes()) {
            addInput(new WFP_Input(dn));
        }
        return;
    }

    private void addInput(final IWFP_Input pInput) {
        mInputs.add(pInput);
        return;
    }

    @Override
    public List<IWFP_Input> getInputs() throws WFP_Exception {
        return mInputs;
    }

    @Override
    public IWFP_Output addOutput(final String pName) throws WFP_Exception {
        try {
            final Node n = mContext.getNode().addNode(pName, "jecars:datafile");
            n.setProperty("jcr:mimeType", "text/plain");
            return new WFP_Output(mContext, n);
        } catch (RepositoryException re) {
            throw new WFP_Exception(re);
        }
    }
}
