package org.webgraphlab.algimpl;

import org.webgraphlab.algorithm.*;

/**
 **  Implementation skeleton class for the ::org::webgraphlab::algorithm::AlgorithmComponent component.
 **  Business operations MUST be completed !!!
 **/
public class CriticalPathAlgorithm extends org.omg.CORBA.LocalObject implements CCM_AlgorithmComponent, org.omg.Components.SessionComponent {

    /**
     **  Context reference.
     **/
    private CCM_AlgorithmComponent_Context the_context_;

    /**
     **  The default constructor.
     **/
    public CriticalPathAlgorithm() {
        the_context_ = null;
    }

    /**
     **  To obtain the context.
     **
     **  @return The context that has been previously set by
     **          the set_session_context operation.
     **/
    public CCM_AlgorithmComponent_Context getContext() {
        return the_context_;
    }

    /**
     **  Completes the component configuration.
     **
     **  @throws org.omg.Components.InvalidConfiguration
     **          Thrown if the configuration is invalid.
     **/
    public void configuration_complete() throws org.omg.Components.InvalidConfiguration {
    }

    /**
     *  Called by the container when the component session context will be fixed.
     *
     *  @param context The session context.
     *
     *  @throws org.omg.Components.CCMException
     *          Thrown if a system level error occured.
     */
    public void set_session_context(org.omg.Components.SessionContext context) throws org.omg.Components.CCMException {
        the_context_ = (CCM_AlgorithmComponent_Context) context;
    }

    /**
     *
     * Called by the container when the component will be activated.
     *
     *  @throws org.omg.Components.CCMException
     *          Thrown if a system level error occured.
     */
    public void ccm_activate() throws org.omg.Components.CCMException {
    }

    /**
     *
     * Called by the container when the component will be passivated.
     *
     *  @throws org.omg.Components.CCMException
     *          Thrown if a system level error occured.
     */
    public void ccm_passivate() throws org.omg.Components.CCMException {
    }

    /**
     *
     * Called by the container when the component will be removed.
     *
     *  @throws org.omg.Components.CCMException
     *          Thrown if a system level error occured.
     */
    public void ccm_remove() throws org.omg.Components.CCMException {
    }

    /**
     **  Implementation of the ::org::webgraphlab::algorithm::GraphAlgorithm::loadGraph operation.
     **/
    public void loadGraph(java.lang.String graph) throws org.webgraphlab.algorithm.GraphParseException {
        System.out.println(">>>>>>>>>>>>>>>>GriticPathAlgorithm::loadGraph");
    }

    /**
     **  Implementation of the ::org::webgraphlab::algorithm::GraphAlgorithm::loadParameters operation.
     **/
    public void loadParameters(java.lang.String params) throws org.webgraphlab.algorithm.ParametersParseException {
        System.out.println(">>>>>>>>>>>>>>>>GriticPathAlgorithm::loadParameters:");
        System.out.println(params);
    }

    /**
     **  Implementation of the ::org::webgraphlab::algorithm::GraphAlgorithm::getGraphFormat operation.
     **/
    public java.lang.String getGraphFormat() {
        return "";
    }

    /**
     **  Implementation of the ::org::webgraphlab::algorithm::GraphAlgorithm::getParametersFormat operation.
     **/
    public java.lang.String getParametersFormat() {
        System.out.println(">>>>>>>>>>>>>>>>GriticPathAlgorithm::getParametersFormat:");
        return "";
    }

    /**
     **  Implementation of the ::org::webgraphlab::algorithm::GraphAlgorithm::getResultFormat operation.
     **/
    public java.lang.String getResultFormat() {
        System.out.println(">>>>>>>>>>>>>>>>GriticPathAlgorithm::getResultFormat:");
        return "";
    }

    /**
     **  Implementation of the ::org::webgraphlab::algorithm::AlgorithmEssence::hasNextIteration operation.
     **/
    public boolean hasNextIteration() {
        System.out.println(">>>>>>>>>>>>>>>>GriticPathAlgorithm::hasNextIteration:");
        return true;
    }

    /**
     **  Implementation of the ::org::webgraphlab::algorithm::AlgorithmEssence::runNextIteration operation.
     **/
    public java.lang.String runNextIteration() {
        System.out.println(">>>>>>>>>>>>>>>>GriticPathAlgorithm::runNextIteration:");
        return "?!!!!!!!!!!!!qqwdfasdfasdlkfjasdklfjasdklfjskldfjasdkljfklsdjflkasdjfl" + "sdfasdfasdfsafsdfsdfasf" + "askldjflsadkfjlaskjfl;asdjkfl;askdjfasdjkfl;asdkfjl;";
    }

    /**
     **  Implementation of the ::org::webgraphlab::algorithm::AlgorithmEssence::reset operation.
     **/
    public void reset() {
        System.out.println(">>>>>>>>>>>>>>>>GriticPathAlgorithm::reset:");
    }
}
