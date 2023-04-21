package proper.imp;

import proper.util.ProperVector;
import java.util.Vector;

/**
* Data is the common ancestor for Predicate and List.
*
* @author      FracPete
* @version $Revision: 1.2 $
*/
public abstract class Data implements Cloneable {

    protected static final boolean VERBOSE = false;

    private int id;

    private Vector data;

    private Data parent;

    /**
   * initializes the List
   */
    protected Data() {
        this(0);
    }

    /**
   * initializes the List
   */
    public Data(int id) {
        data = new ProperVector();
        parent = null;
        this.id = id;
    }

    /**
   * returns a copy of this object
   */
    public Object clone() throws CloneNotSupportedException {
        Data result;
        result = null;
        try {
            result = (Data) this.getClass().newInstance();
            result.initWith(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
   * initializes this object with the data from the given object
   */
    protected void initWith(Data d) {
        this.id = d.id;
        this.parent = d.parent;
        this.data = (Vector) d.data.clone();
    }

    /**
   * returns the name of the container
   */
    public abstract String getName();

    /**
   * sets the name of the container
   */
    protected abstract void setName(String name);

    /**
   * returns the ID of this predicate
   */
    public int getID() {
        return id;
    }

    /**
   * sets the ID if this predicate - ATTENTION: can damage the uniqueness!
   */
    public void setID(int id) {
        this.id = id;
    }

    /**
   * sets the parent of this object
   */
    public void setParent(Data parent) {
        this.parent = parent;
    }

    /**
   * returns the parent of this object
   */
    public Data getParent() {
        return parent;
    }

    /**
   * returns whether this data has a parent or not
   */
    public boolean hasParent() {
        return (getParent() != null);
    }

    /**
   * adds the given object to the list
   */
    public void add(Object o) {
        if (o instanceof Data) ((Data) o).setParent(this);
        data.add(o);
    }

    /**
   * adds the given object to the list at the specified index
   */
    public void add(int index, Object o) {
        if (o instanceof Data) ((Data) o).setParent(this);
        data.add(index, o);
    }

    /**
   * returns the specified object
   */
    public Object get(int index) {
        if ((index >= 0) && (index < size())) return getData().get(index); else return null;
    }

    /**
   * removes the specified object
   */
    public Object remove(int index) {
        return getData().remove(index);
    }

    /**
   * returns the data stored in a Vector
   */
    public Vector getData() {
        return data;
    }

    /**
   * returns the size of the stored data
   */
    public int size() {
        return getData().size();
    }

    /**
   * returns the index of the given data in its own children
   */
    public int getIndex(Data d) {
        int result;
        result = getData().indexOf(d);
        return result;
    }

    /**
   * returns the index of itself in the parents children
   */
    public int getParentIndex() {
        int result;
        result = -1;
        if (hasParent()) result = getParent().getIndex(this);
        return result;
    }

    /**
   * checks whether the given element is NULL
   * @param index          the index of the element to check
   * @return               whether the element is NULL
   */
    public boolean isNull(int index) {
        if ((index >= 0) && (index < size())) return isNull(get(index).toString()); else return false;
    }

    /**
   * checks whether the string is a NULL value
   */
    public static boolean isNull(String str) {
        boolean result;
        result = ((str.equals("n/a")) || (str.equals("'n/a'")) || (str.equals("?")) || (str.equals("_")) || (str.equals(Fingerprint.NULL)));
        return result;
    }

    /**
   * returns the object in a String representation
   */
    public abstract String toString();
}
