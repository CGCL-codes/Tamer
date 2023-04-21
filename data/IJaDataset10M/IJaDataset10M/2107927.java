package org.perfectjpattern.core.api.behavioral.visitor;

/**
 * <b>Visitor Design Pattern</b>: Represent an operation to be performed on the
 * elements of an object structure. Visitor lets you define a new operation
 * without changing the classes of the elements on which it operates.<br/>
 * <br/>
 * 
 * <b>Responsibility</b> Abstract definition of "Element"
 * <ul>
 * <li>Marker interface for any Element type.</li>
 * </ul>
 * 
 * <br/>
 * Example usage:
 * <pre><code>
 *    //
 *    // Abstract Base Node for a simple Element hierarchy.
 *    //  
 *    abstract
 *    class AbstractNode 
 *    implements IElement 
 *    {
 *        //--------------------------------------------------------------------
 *        public 
 *        BaseNode(String aName) 
 *        {
 *            theName = aName;
 *        }
 *                
 *        //--------------------------------------------------------------------
 *        public final String 
 *        getName() 
 *        {
 *            return theName;
 *        }        
 *
 *        //--------------------------------------------------------------------
 *        // members
 *        //--------------------------------------------------------------------
 *        private final String theName;
 *    }
 *
 *    //
 *    // RedNode
 *    //  
 *    class RedNode 
 *    extends AbstractNode 
 *    {
 *        //--------------------------------------------------------------------
 *        public 
 *        RedNode() 
 *        {
 *            super("Red");
 *        }
 *    }
 *    
 *    //
 *    // BlackNode
 *    //  
 *    class BlackNode 
 *    extends AbstractNode 
 *    {
 *        //--------------------------------------------------------------------
 *        public 
 *        BlackNode() 
 *        {
 *            super("Black");
 *        }
 *    }
 *    
 *    //
 *    // Simple Visitor
 *    // 
 *    class NodeVisitor
 *    extends AbstractVisitor&lt;AbstractNode&gt;
 *    {
 *        //--------------------------------------------------------------------
 *        public void
 *        visitRedNode(RedNode aNode)
 *        {
 *             // do RedNode visiting
 *             System.out.println(aNode.getName());    
 *        }
 *        
 *        //--------------------------------------------------------------------
 *        public void
 *        visitBlackNode(BlackNode aNode)
 *        {
 *             // do BlackNode visiting
 *             System.out.println(aNode.getName());    
 *        }
 *    }
 *    
 *    //
 *    // Usage example
 *    //
 *    IVisitor&lt;AbstractNode&gt; myVisitor = new NodeVisitor();
 *    myVisitor.visit(new RedNode());
 *    myVisitor.visit(new BlackNode());
 * </code></pre>
 * 
 * @author <a href="mailto:bravegag@hotmail.com">Giovanni Azua</a>
 * @version $Revision: 1.0 $ $Date: Jul 1, 2007 6:59:35 AM $
 */
public interface IElement {
}
