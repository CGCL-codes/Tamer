package trees.jaxb;

/**
 * Class Node_type.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Node_type implements java.io.Serializable {

    /**
     * Field _cat.
     */
    private java.lang.String _cat;

    /**
     * Field _name.
     */
    private java.lang.String _name;

    /**
     * Field _type.
     */
    private trees.jaxb.types.Node_type_string_type _type;

    /**
     * Field _fs.
     */
    private Fs _fs;

    /**
     * Field _nodeList.
     */
    private java.util.Vector<Node> _nodeList;

    public Node_type() {
        super();
        this._nodeList = new java.util.Vector<Node>();
    }

    /**
     * 
     * 
     * @param vNode
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addNode(final Node vNode) throws java.lang.IndexOutOfBoundsException {
        this._nodeList.addElement(vNode);
    }

    /**
     * 
     * 
     * @param index
     * @param vNode
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void addNode(final int index, final Node vNode) throws java.lang.IndexOutOfBoundsException {
        this._nodeList.add(index, vNode);
    }

    /**
     * Method enumerateNode.
     * 
     * @return an Enumeration over all Node elements
     */
    public java.util.Enumeration<? extends Node> enumerateNode() {
        return this._nodeList.elements();
    }

    /**
     * Returns the value of field 'cat'.
     * 
     * @return the value of field 'Cat'.
     */
    public java.lang.String getCat() {
        return this._cat;
    }

    /**
     * Returns the value of field 'fs'.
     * 
     * @return the value of field 'Fs'.
     */
    public Fs getFs() {
        return this._fs;
    }

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'Name'.
     */
    public java.lang.String getName() {
        return this._name;
    }

    /**
     * Method getNode.
     * 
     * @param index
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     * @return the value of the Node at the given index
     */
    public Node getNode(final int index) throws java.lang.IndexOutOfBoundsException {
        if (index < 0 || index >= this._nodeList.size()) {
            throw new IndexOutOfBoundsException("getNode: Index value '" + index + "' not in range [0.." + (this._nodeList.size() - 1) + "]");
        }
        return (Node) _nodeList.get(index);
    }

    /**
     * Method getNode.Returns the contents of the collection in an
     * Array.  <p>Note:  Just in case the collection contents are
     * changing in another thread, we pass a 0-length Array of the
     * correct type into the API call.  This way we <i>know</i>
     * that the Array returned is of exactly the correct length.
     * 
     * @return this collection as an Array
     */
    public Node[] getNode() {
        Node[] array = new Node[0];
        return (Node[]) this._nodeList.toArray(array);
    }

    /**
     * Method getNodeCount.
     * 
     * @return the size of this collection
     */
    public int getNodeCount() {
        return this._nodeList.size();
    }

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'Type'.
     */
    public trees.jaxb.types.Node_type_string_type getType() {
        return this._type;
    }

    /**
     */
    public void removeAllNode() {
        this._nodeList.clear();
    }

    /**
     * Method removeNode.
     * 
     * @param vNode
     * @return true if the object was removed from the collection.
     */
    public boolean removeNode(final Node vNode) {
        boolean removed = _nodeList.remove(vNode);
        return removed;
    }

    /**
     * Method removeNodeAt.
     * 
     * @param index
     * @return the element removed from the collection
     */
    public Node removeNodeAt(final int index) {
        java.lang.Object obj = this._nodeList.remove(index);
        return (Node) obj;
    }

    /**
     * Sets the value of field 'cat'.
     * 
     * @param cat the value of field 'cat'.
     */
    public void setCat(final java.lang.String cat) {
        this._cat = cat;
    }

    /**
     * Sets the value of field 'fs'.
     * 
     * @param fs the value of field 'fs'.
     */
    public void setFs(final Fs fs) {
        this._fs = fs;
    }

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(final java.lang.String name) {
        this._name = name;
    }

    /**
     * 
     * 
     * @param index
     * @param vNode
     * @throws java.lang.IndexOutOfBoundsException if the index
     * given is outside the bounds of the collection
     */
    public void setNode(final int index, final Node vNode) throws java.lang.IndexOutOfBoundsException {
        if (index < 0 || index >= this._nodeList.size()) {
            throw new IndexOutOfBoundsException("setNode: Index value '" + index + "' not in range [0.." + (this._nodeList.size() - 1) + "]");
        }
        this._nodeList.set(index, vNode);
    }

    /**
     * 
     * 
     * @param vNodeArray
     */
    public void setNode(final Node[] vNodeArray) {
        _nodeList.clear();
        for (int i = 0; i < vNodeArray.length; i++) {
            this._nodeList.add(vNodeArray[i]);
        }
    }

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(final trees.jaxb.types.Node_type_string_type type) {
        this._type = type;
    }
}
