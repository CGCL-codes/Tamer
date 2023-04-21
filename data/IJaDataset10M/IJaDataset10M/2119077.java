package icreate.comps;

import java.util.ArrayList;

/** Bean abstracting a Section
 */
public class Section extends Abstract_IC_Component implements icreate.mans.DataContainerObject, Prioritised {

    private int col_layoutID = 1;

    private byte collapse = 0;

    private int priority = 0;

    /** Holds value of property members
     */
    protected ArrayList members = new ArrayList();

    /** Creates a new instance of Object
     * @param parent New value for parent property
     * @throws compException thrown by addMember(..) of parent
     */
    public Section(Page parent) throws compException {
        setParent(parent);
    }

    /** Blank Constructor
     */
    public Section() {
    }

    /** Array of member types
     * @return Array of member types allowed to be added to container
     */
    public String[] memberTypes() {
        return null;
    }

    /** Add Member to Object
     * @param member Add member to object
     * @throws compException thrown if param member is of the wrong type
     */
    public boolean addMember(icreate.mans.DataObject member) {
        return false;
    }

    /** Remove a member
     * @param member Member to be removed
     */
    public boolean removeMember(icreate.mans.DataObject member) {
        return members.remove(member);
    }

    /** Getter for property col_layoutID
     * @return Value of property col_layoutID
     */
    public int getCol_layoutID() {
        return col_layoutID;
    }

    /** Getter for property collapse
     * @return Value of property collapse
     */
    public byte getCollapse() {
        return collapse;
    }

    /** Setter for property col_layoutID
     * @param col_layoutID New value for property col_layoutID
     */
    public void setCol_layoutID(int col_layoutID) {
        this.col_layoutID = col_layoutID;
    }

    /** Setter for property collapse
     * @param collapse New value for property collapse
     */
    public void setCollapse(byte collapse) {
        this.collapse = collapse;
    }

    /** Clear members in Object
     */
    public void clearMembers() {
        this.members.clear();
    }

    /** Getter for property members
     * @return Value of property members
     */
    public ArrayList getMembers() {
        return this.members;
    }

    /** Getter for property priority.
     * @return Value of property priority.
     */
    public int getPriority() {
        return this.priority;
    }

    /** Setter for property priority.
     * @param priority New value of property priority.
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /** Tests whether object is equal to instance, tested on property id and whether
     * Object is instance of Section
     * @param obj Object to be compared to instance
     * @return true if equal
     */
    public boolean equals(Object obj) {
        return (obj instanceof Section) && (this.getId() == ((Section) obj).getId());
    }

    /** Compares Object to instance based on property priority
     * @param obj Object to be compared to
     * @return relative ordering
     */
    public int compareTo(Object obj) {
        if (obj instanceof Section) {
            int i1 = this.priority;
            int i2 = ((Section) obj).getPriority();
            return (i2 < i1 ? -1 : (i2 == i1 ? 0 : 1));
        }
        return 1;
    }
}
