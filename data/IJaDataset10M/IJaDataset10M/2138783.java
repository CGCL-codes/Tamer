package il.ac.biu.cs.grossmm.api.data;

/**
 * Any Java interface (or class) which extends (implements) this interface
 * defines a node type. Such interface are called node type interfaces.
 * <p>
 * Node type consists of a tuple of properties and a tuple of node-sets. A
 * property is defined by the java type of values which can be stored in this
 * property. A node-set is defined by type of data nodes which can be contained
 * in this node-set. Node type interface provides a simple way to map node types
 * to Java type constructs. Properties and node-sets are defined as static constants
 * in node type interfaces. These constants are called property constants and
 * node-set contsants respectively.   
 * <p>
 * Node type interface should adhere a contract defined by this interface as
 * follows:
 * <ol>
 * <li> Class hierarchy rules
 * <ol>
 * <li> Node type interface must be a subtype of <tt>NodeInterface</tt>
 * <li> Node type interface may be a subtype of other node type interface
 * <li> No other type (except <tt>lang.Object</tt>) may be a supertype of
 * node type interface
 * </ol>
 * <li> Node type interface must not define any methods or fields except the
 * following:
 * <ol>
 * <li> Node type interface may contain public static final fields of type
 * {@link Property} or {@link Unique} to define properties
 * <li> Node type interface may contain public static final fields of type
 * {@link Nodeset} to define types of nested nodes
 * </ol>
 * </ol>
 * 
 * <p>
 * <tt>Property</tt> fields may be initialized to values provided by
 * {@link NodeTypeByInterface#property(Class, Class)} and
 * {@link NodeTypeByInterface#unique(Class, Class)} method and <tt>Nodeset</tt>
 * field may be initialized to values provided by
 * {@link NodeTypeByInterface#nodeset(int)} method
 * 
 * <p>
 * The following example defines a node type which contains two properties: name
 * of type <tt>String</tt> and a phone number of type <tt>Long</tt>:
 * 
 * <pre>
 *     import static il.ac.biu.cs.grossmm.api.data.NodeTypeByInterface.*;
 *    
 *     interface NameNumber extends NodeInterface {
 *         Unique&lt;NameNumber, String&gt; NAME 
 *                  = unique(NameNumber.class, String.class); 
 *    	
 *    	    Property&lt;NameNumber, Long&gt; PHONE
 *                  =property(NameNumber.class, Long.class);
 *     } 
 * </pre>
 * 
 * <p>
 * The following example defines a node type which contains a node-set of nodes
 * defined the the previous example
 * 
 * <pre>
 *    import static il.ac.biu.cs.grossmm.api.data.NodeTypeByInterface.*;
 *    
 *    interface PhoneBook extends NodeInterface {
 *         Nodeset&lt;PhoneBook, NameNumber&gt; 
 *                  = nodeset(PhoneBook.class, NameNumber.class);
 *    }
 * </pre>
 * 
 * @author Mark Grossmann
 */
public interface NodeInterface {
}
