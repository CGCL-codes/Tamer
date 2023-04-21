package component_interfaces.semanticmm4u.realization.user_profile_connector.provided;

import java.util.GregorianCalendar;
import java.util.Vector;
import de.offis.semanticmm4u.failures.user_profiles_connectors.MM4UCannotAddUserProfileNodeException;
import de.offis.semanticmm4u.failures.user_profiles_connectors.MM4UCannotRemoveUserProfileNodeException;
import de.offis.semanticmm4u.failures.user_profiles_connectors.MM4UCannotRemoveUserProfileNodeValueException;
import de.offis.semanticmm4u.failures.user_profiles_connectors.MM4UCannotSetOrAddUserProfileNodeValueException;

/**
 * An user profile node. Used to store user profile data hierarchically.
 * 
 * 
 */
public interface IUserProfileNode {

    /**
	 * Implements the interface <code>comparable</code>. Comparing nodes is
	 * required, e. g., to sort nodes in collections by their names. For
	 * comparison, the nodes' names are used.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
    public int compareTo(Object other);

    /**
	 * @see java.lang.Object#toString()
	 */
    public String toString();

    /**
	 * Returns the name of the node.
	 * 
	 * @return name of the node
	 */
    public String getName();

    /**
	 * Returns the parent node of this node.
	 * 
	 * @return the parent node
	 */
    public IUserProfileNode getParent();

    /**
	 * Indicates whether the node can carry/contain values.
	 * 
	 * @return <code>true</code>, if the node is allowed to store at least
	 *         one value
	 */
    public boolean isAllowedToStoreValues();

    /**
	 * Indicates whether the node is a container node, i. e., if this this node
	 * is allowed to carry multiple values.
	 * 
	 * @return <code>true</code>, if this node is a container node
	 */
    public boolean isContainer();

    /**
	 * Indicates whether the node records temporal data.
	 * 
	 * @return <code>true</code> if the node records temporal data.
	 */
    public boolean recordsTemporalData();

    /**
	 * @param recordTemporalData
	 *            Determines whether a history of the change of values of this
	 *            node is recorded.
	 */
    public void setHoldTemporalData(boolean recordTemporalData);

    /**
	 * Returns the children nodes of this node.
	 * 
	 * @return children nodes
	 */
    public Vector getChildren();

    /**
	 * Returns the first child node with the name <code>name</code> of this
	 * node. If there is no node with such a name, <code>null</code> is
	 * returned.
	 * 
	 * @param name
	 *            name of the child node searched for
	 * @return the child node or <code>null</code>
	 */
    public IUserProfileNode getNodeByName(String name);

    /**
	 * Returns the child node, starting with the current node, specified by the
	 * path <code>nodePath</code>. If there are multiple nodes corresponding
	 * to this node, it returs the first node.<br>
	 * A path looks like: <code>DemographicalData.Name.LastName</code>. The
	 * current node does not need to part of the path.
	 * 
	 * @param Path
	 *            to the node, seperated by dots (".")
	 * @return node the path points at or <code>null</code>, if the path
	 *         points at no node
	 */
    public IUserProfileNode getNodeByPath(String nodePath);

    /**
	 * Adds a user profile node as child node with name <code>childName</code>
	 * to this user profile node.
	 * 
	 * @param childName
	 *            name of the child node that is to be added
	 * @throws MM4UCannotAddUserProfileNodeException
	 *             Is thrown, if the user model does not define a user profile
	 *             extension with a node <code>childName</code> at this point
	 */
    public IUserProfileNode addChild(String childName) throws MM4UCannotAddUserProfileNodeException;

    /**
	 * Removes this user profile node from the user profile. This includes the
	 * sub-profile of this node.
	 * 
	 * @throws MM4UCannotRemoveUserProfileNodeException
	 *             Is thrown, if the user profile node cannot be removed,
	 *             because it is defined to be mandatory in the user model.
	 * @see #removeChild(UserProfileNode)
	 */
    public void remove() throws MM4UCannotRemoveUserProfileNodeException;

    /**
	 * Removes a user value from this node. <code>position</code> determines
	 * the position in the list of values, which shall be removed.
	 * 
	 * If the node is a non-container node, the values should be removed by
	 * using <code>removeValue()</code> since here a position is not needed to
	 * be provided.
	 * 
	 * @param position
	 *            the position of the value that shall be removed
	 * @throws MM4UCannotRemoveUserProfileNodeValueException
	 *             Is thrown, if there is no value at this position.
	 */
    public void removeValue(int position) throws MM4UCannotRemoveUserProfileNodeValueException;

    /**
	 * Removes the first user value at this node.
	 * 
	 * @throws MM4UCannotRemoveUserProfileNodeValueException
	 *             Is thrown, if the node does not carry any value
	 * @see #removeValue(int)
	 */
    public void removeValue() throws MM4UCannotRemoveUserProfileNodeValueException;

    /**
	 * Removes all user values from this node; indepentendly whether the node is
	 * a container node or not.
	 * 
	 */
    public void removeAllValues();

    /**
	 * Returns a vector with all (current) user values of this node. If the node
	 * is no container node, it contains one element at maximum.
	 * 
	 * @return a set of the current valid values of this node; if there is no
	 *         user value, the vector is empty
	 */
    public Vector getValues();

    /**
	 * Adds a user value to this node. For a container node, this is always
	 * allowed. For a non-container node, this is only allowed, if the node does
	 * not contain a value so far.
	 * 
	 * @param value
	 *            the new value at this node
	 * @param validStart
	 *            the point in time, when this value shall be valid
	 * @param creator
	 *            the creator of this value
	 * @param confidence
	 *            the trustiness of this value
	 * @param accuracy
	 *            the accurary of this value
	 * @param unit
	 *            the unit/measurement of this value
	 * 
	 * @throws MM4UCannotSetOrAddUserProfileNodeValueException
	 *             Is thrown, if ...
	 *             <ul>
	 *             <li> invalid values are given for <code>accuracy</code> or
	 *             <code>confidence</code> (e.g. >1).
	 *             <li> the node is not allowed to contain a user value.
	 *             <li> it is not allowed to add a user value to this node.
	 *             </ul>
	 * @see #editValue(String, GregorianCalendar, String, double, double,
	 *      String, int)
	 */
    public void addValue(String value, GregorianCalendar validStart, String creator, double confidence, double accuracy, String unit) throws MM4UCannotSetOrAddUserProfileNodeValueException;

    /**
	 * Sets the user value of this node to be <code>value</code>. This method
	 * simplifies the editing of user values. For the meta data it is assumed
	 * that
	 * <ul>
	 * <li> the validity starts with the current point in time,
	 * <li> the creator is unknown,
	 * <li> the confidence is 1,
	 * <li> the accuracy is 1,
	 * <li> the unit of this user value is unknown.
	 * </ul>
	 * 
	 * In addition: The position of this value is 0. For a container node, only
	 * the first user value is changed. For a non-container node, the only one
	 * user value that is allowed is changed. If there does not exist a user
	 * value at this node so far, it is created (for both container nodes and
	 * non-container nodes).
	 * 
	 * @param value
	 *            the value to be set
	 * @throws MM4UCannotSetOrAddUserProfileNodeValueException
	 *             Is thrown, if the value is not allowed to store user values.
	 * @see #setValue(String, GregorianCalendar, String, double, double, String,
	 *      int)
	 */
    public void setValue(String value) throws MM4UCannotSetOrAddUserProfileNodeValueException;

    public void setValue(boolean value) throws MM4UCannotSetOrAddUserProfileNodeValueException;

    public void setValue(int value) throws MM4UCannotSetOrAddUserProfileNodeValueException;

    /**
	 * Sets the user value of the value at position <code>position</code> in
	 * the list of user values of this node.
	 * <p>
	 * If the node records temporal data, a new user date is added and the
	 * corresponding values for transaction time and validity time are changed.
	 * If the node does not record temporal data, the current user value is
	 * overrided by the new one.
	 * 
	 * @param value
	 *            the new value
	 * @param validStart
	 *            the point in time when this new value shall be valid
	 * @param creator
	 *            the creator of this value
	 * @param confidence
	 *            the trustiness of this value
	 * @param accuracy
	 *            the accuracy of this value
	 * @param unit
	 *            the unit/measurement of this value
	 * @param position
	 *            the position of this value within the list of user values of
	 *            this node
	 * @throws MM4UCannotSetOrAddUserProfileNodeValueException
	 *             Is thrown if, ...
	 *             <ul>
	 *             <li> invalid values are given for <code>accuracy</code> or
	 *             <code>confidence</code> (e.g. >1).
	 *             <li> the node is not allowed to contain a user value.
	 *             <li> an invalid position for the user value is provided.
	 *             </ul>
	 * @see #editValue(String, GregorianCalendar, String, double, double,
	 *      String, int)
	 */
    public void setValue(String value, GregorianCalendar validStart, String creator, double confidence, double accuracy, String unit, int position) throws MM4UCannotSetOrAddUserProfileNodeValueException;

    /**
	 * Removes all user data of this node.
	 */
    public void clearAllUserDataEntry();
}
