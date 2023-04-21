package jsomap.map;

import jsomap.data.Pattern;

/** <p>A node in the map.  This interface provides a general way of representing a node in the map, without
 * tying it to any specific implementation.  It specifies methods for accessing its underyling pattern and
 * its corresponding model.</p>
 *
 * <p>Traditionally, the nodes in a self-organizing map are points in one or two dimensional space
 * arranged in some sort of lattice.  Additionally, each node has a corresponding model that exists in
 * the input space of the data.  This interface represents an abstract notion of a map node, in that it
 * is not tied to a one or two dimensional vector.</p>
 *
 * <p>Implementations should generally provide two constructors: one accepting a concrete <CODE>Pattern</CODE>
 * implementation and a second accepting a concrete <CODE>Pattern</CODE> implementation along with a model.  
 * The second constructor should attempt to set up the connection between the model and this node.</p>
 *
 * @author Zach Cox <zcox@iastate.edu>
 * @version $Revision: 1.2 $
 * @since JDK1.3
 */
public interface Node {

    /** Returns the corresponding model for this node.
	 *
	 * @return the corresponding model for this node.
	 */
    public Model getModel();

    /** Returns the pattern for this node.
	 *
	 * @return the pattern for this node.
	 */
    public Pattern getPattern();

    /** Sets the corresponding model for this node to the specified model.  This method should also set the
	 * specified model's node to this node.
	 *
	 * @param model the corresponding model for this node.
	 */
    public void setModel(Model model);

    /** Sets this node's pattern to the specified pattern.
	 *
	 * @param pattern the pattern.
	 * @throws NullPointerException if <CODE>pattern</CODE> is <CODE>null</CODE>.
	 */
    public void setPattern(Pattern pattern);
}
