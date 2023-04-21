package wotlas.utils;

/** A list that implements the same methods as a java.utils.Vector
 *  WITH the difference that you can only remove elements from the head
 *  or the tail ( We optimized these operations by implementing defered
 *  destruction ).
 * <br><p>
 * <b>Important</b>: The <i>List</i> methods are not synchronized.
 * <br><p>
 * <b>Known use</b>: A* pathfindig algorithm (see {@link wotlas.libs.pathfinding.AStar A*} )
 *
 * @author Aldiss, Petrus
 */
public class List {

    /** 
     *  Object Array
     */
    private Object array[];

    /**
     *  index of the last valid element of this array
     */
    private int last_element;

    /** index of the first valid element of this array
     */
    private int first_element;

    /**
     *  Array growth
     */
    private int growth;

    /**
     *  Initial capacity
     */
    private int initial_capacity;

    /** Constructor with a default initial list capacity of 10 and a
     * default growth of 10.
     */
    public List() {
        this.last_element = -1;
        this.first_element = -1;
        this.array = new Object[10];
        this.growth = 10;
        this.initial_capacity = 10;
    }

    /** Constructor with initial list capacity and a default growth of 10.
     *
     * @param initial_capacity initial list capacity
     */
    public List(int initial_capacity) {
        this.last_element = -1;
        this.first_element = -1;
        this.array = new Object[initial_capacity];
        this.growth = 10;
        this.initial_capacity = initial_capacity;
    }

    /** Constructor with initial list capacity and list growth.
     *
     * @param initial_capacity initial list capacity
     * @param growth array growth when the capacity is reached.
     */
    public List(int initial_capacity, int growth) {
        this.last_element = -1;
        this.first_element = -1;
        this.array = new Object[initial_capacity];
        this.initial_capacity = initial_capacity;
        this.growth = growth;
    }

    /** Adds this element at the end of the list.
     *
     * @param o object to add
     */
    public void addElement(Object o) {
        if (this.last_element == this.array.length - 1) {
            Object array_tmp[] = new Object[this.array.length + this.growth - this.first_element];
            System.arraycopy(this.array, this.first_element, array_tmp, 0, this.array.length - this.first_element);
            this.last_element = this.array.length - this.first_element - 1;
            this.first_element = 0;
            this.array = array_tmp;
        }
        this.last_element++;
        this.array[this.last_element] = o;
        if (this.first_element == -1) this.first_element = 0;
    }

    /** Removes the first element of the list.
     */
    public void removeFirstElement() {
        if (this.last_element == -1) return;
        if (this.first_element == this.last_element) {
            this.array = new Object[this.initial_capacity];
            this.first_element = -1;
            this.last_element = -1;
            return;
        }
        this.array[this.first_element] = null;
        this.first_element++;
        if (this.first_element >= 2 * this.growth) {
            Object array_tmp[] = new Object[this.array.length - this.growth];
            System.arraycopy(this.array, this.first_element, array_tmp, 0, this.last_element - this.first_element + 1);
            this.last_element = this.last_element - this.first_element;
            this.first_element = 0;
            this.array = array_tmp;
        }
    }

    /** Removes the last element of the list.
     */
    public void removeLastElement() {
        if (this.last_element == -1) return;
        if (this.last_element == this.first_element) {
            this.array = new Object[this.initial_capacity];
            this.first_element = -1;
            this.last_element = -1;
            return;
        }
        this.array[this.last_element] = null;
        this.last_element--;
        if (this.array.length - this.last_element > 2 * this.growth) {
            Object array_tmp[] = new Object[this.array.length - this.growth];
            System.arraycopy(this.array, this.first_element, array_tmp, 0, this.last_element - this.first_element + 1);
            this.last_element = this.last_element - this.first_element;
            this.first_element = 0;
            this.array = array_tmp;
        }
    }

    /** Removes all the elements of the list.
     */
    public void removeAllElements() {
        if (this.last_element == -1) return;
        this.array = new Object[this.initial_capacity];
        this.first_element = -1;
        this.last_element = -1;
    }

    /** To get the element at the specied index.
     *
     * @param index element index
     */
    public Object elementAt(int index) {
        return this.array[index + this.first_element];
    }

    /** Insert this element at the specified position of the list.
     *  The elements of the array beginning at "index" are shifted to
     *  the right.
     *
     * @param o object to add.
     * @param index index of insertion
     */
    public void insertElementAt(Object o, int index) {
        if (this.last_element == -1) {
            addElement(o);
            return;
        }
        if (index > this.last_element - this.first_element) {
            addElement(o);
            return;
        }
        Object array_tmp[];
        if (this.last_element - this.first_element + 1 == this.array.length) array_tmp = new Object[this.array.length + this.growth]; else array_tmp = new Object[this.array.length - this.first_element + 1];
        if (index > 0) System.arraycopy(this.array, this.first_element, array_tmp, 0, index);
        System.arraycopy(this.array, index + this.first_element, array_tmp, index + 1, this.last_element - this.first_element - index + 1);
        this.last_element = this.last_element - this.first_element + 1;
        this.first_element = 0;
        this.array = array_tmp;
        this.array[index] = o;
    }

    /** To get the size of the list.
     *
     * @return the list's size
     */
    public int size() {
        if (this.last_element == -1) return 0;
        return this.last_element - this.first_element + 1;
    }

    /** To test if the list is empty
     *
     * @return true if the list is empty
     */
    public boolean isEmpty() {
        return (this.last_element == -1);
    }

    /** To print the content of the list.
     *
     * @return a string representation of the list
     */
    @Override
    public String toString() {
        StringBuffer s = new StringBuffer("List:\n First> " + this.first_element + "\n  Last> " + this.last_element + "\n   Array Size> " + this.array.length + "    Growth> " + this.growth + "\n     Elements> ");
        if (this.last_element == -1) {
            s.append("none.");
            return s.toString();
        }
        s.append("{ ");
        for (int i = this.first_element; i <= this.last_element; i++) s.append("[" + (i - this.first_element) + "]:" + this.array[i].toString() + ", ");
        s.append("}");
        return s.toString();
    }
}
