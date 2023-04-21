package gov.sns.xal.model.gen.ptree;

import gov.sns.xal.model.gen.GenerationException;

/**
 * This is the interface to the visitor design pattern of a proxy tree.  
 * Specifically, any class implementing this interface can be used as
 * a "visitor" for a proxy tree.
 * 
 * @author Christopher K. Allen
 */
public interface IProxyVisitor {

    /**
     * Accept a proxy-tree node of type <code>ThinHardware</code> and perform
     * any visitor-specific processing of the node argument.
     * 
     * @param pxyNode                current proxy-tree node being visited
     * 
     * @throws GenerationException   unable to process node (visitor specific)
     */
    public void process(ThinHardware pxyNode) throws GenerationException;

    /**
     * Accept a proxy-tree node of type <code>ThickHardware</code> and perform
     * any visitor specific processing of the node argument.
     * 
     * @param pxyNode                current proxy-tree node being visited
     * 
     * @throws GenerationException   unable to process node (visitor specific)
     */
    public void process(ThickHardware pxyNode) throws GenerationException;

    /**
     * Accept a proxy-tree node of type <code>DriftSpace</code> and perform
     * any visitor specific processing of the node argument.
     * 
     * @param pxyNode                current proxy-tree node being visited
     * 
     * @throws GenerationException   unable to process node (visitor specific)
     */
    public void process(DriftSpace pxyNode) throws GenerationException;

    /**
     * Accept a proxy-tree node of type <code>ProxyTree</code> and perform
     * any visitor specific processing of the node argument.  Node that
     * <code>ProxyTree</code> nodes are somewhat special in that they 
     * representing the node of designated sub-trees.
     * 
     * @param pxyNode                current proxy-tree node being visited
     * 
     * @throws GenerationException   unable to process node (visitor specific)
     */
    public void process(ProxyTree pxyNode) throws GenerationException;

    /**
     * Event representing the completion of a proxy sub-tree indicated by
     * the argument.
     * 
     * @param pxyNode               proxy sub-tree completed processing
     * 
     * @throws GenerationException  visitor specific error in tree processing
     */
    public void leaving(ProxyTree pxyNode) throws GenerationException;
}
