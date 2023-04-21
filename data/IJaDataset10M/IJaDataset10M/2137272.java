package ftraq.db;

import ftraq.bookmarklibrary.exceptions.AddNodeFailure;
import ftraq.bookmarklibrary.exceptions.RemoveNodeFailure;
import java.util.Enumeration;

public interface DbBookmarkFolder extends ftraq.db.DbBookmarkEntry {

    /** defines the name of the object
     *  @param String name - the new name
     *  @throws UpdateNodeAttributeFailure - if the operation failed
     */
    boolean hasChildren();

    /** returns the collection of all child-Object
     *  @return Enumeration - the children
     */
    Enumeration getChildren();

    /** returns the child-Object at position X
     *  @return int i_index - the position
     */
    DbBookmarkEntry getChildAt(int i_index);

    /** returns the number of childs
     *  @return int - the number
     */
    int getChildCount();

    /** returns the index of the given DbBookmarkEntry-Object
     *  @return DbBookmarkEntry i_entry - the entry
     */
    int getIndex(DbBookmarkEntry i_entry);

    /** adds a new child
     *  @param DbBookmarkEntry i_entry - the child
     *  @throws UpdateNodeAttributeFailure - if the operation failed
     */
    void addChild(DbBookmarkEntry i_entry) throws AddNodeFailure;

    /** adds a new child at position i
     *  @param DbBookmarkEntry i_entry - the child
     *  @param int i - the position
     *  @throws AddNodeFailure - if the operation failed
     */
    void addChildAt(DbBookmarkEntry i_entry, int i) throws AddNodeFailure;

    /** removes a child
     *  @param DbBookmarkEntry i_entry - the child
     *  @throws RemoveNodeFailure - if the operation failed
     */
    void removeChild(DbBookmarkEntry i_entry) throws RemoveNodeFailure;

    /** removes a child at position i
     *  @param int i - the position
     *  @throws RemoveNodeFailure - if the operation failed
     */
    void removeChildAt(int i) throws RemoveNodeFailure;
}
