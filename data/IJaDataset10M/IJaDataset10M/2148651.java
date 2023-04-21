package com.aptana.ide.editor.js.runtime;

import com.aptana.ide.metadata.IDocumentation;
import com.aptana.ide.metadata.IDocumentationContainer;

/**
 * @author Kevin Lindsey
 */
public class Property implements IDocumentationContainer {

    private IDocumentation _documentation;

    /**
	 * None
	 */
    public static final int NONE = 0;

    /**
	 * Read-only
	 */
    public static final int READ_ONLY = 1;

    /**
	 * Don't enumerate
	 */
    public static final int DONT_ENUM = 2;

    /**
	 * Don't delete
	 */
    public static final int DONT_DELETE = 4;

    /**
	 * Internal
	 */
    public static final int INTERNAL = 8;

    /**
	 * Not visible
	 */
    public static final int NOT_VISIBLE = 16;

    private OrderedObjectCollection _assignments;

    private int _attributes;

    private int _referenceCount;

    /**
	 * Retrieve the assignment at the given index.
	 * 
	 * @param index
	 *            The assignment index to retrieve
	 * @return Returns the assignment at the given index
	 */
    public IObject getAssignment(int index) {
        return this._assignments.get(index).object;
    }

    /**
	 * Get the total number of references to this property in all of the source code
	 * 
	 * @return The number of references to this property
	 */
    public int getReferenceCount() {
        return this._referenceCount;
    }

    /**
	 * Get the file index associated with this property
	 * 
	 * @param fileIndex
	 *            The file index of the value
	 * @param offset
	 *            The offset of the value
	 * @return Returns the file index (in ordered array) at the specified file index and offset
	 */
    public int getSourceFileIndex(int fileIndex, int offset) {
        return this._assignments.getFloorFileIndex(fileIndex, offset);
    }

    /**
	 * Get the value associated with this property
	 * 
	 * @param fileIndex
	 *            The file index of the value
	 * @param offset
	 *            The offset of the value
	 * @return Returns the object at the specified file index and offset
	 */
    public IObject getValue(int fileIndex, int offset) {
        return this._assignments.getFloorObject(fileIndex, offset);
    }

    /**
	 * Set the value associated with this property
	 * 
	 * @param value
	 *            The value to assign to this property
	 * @param fileIndex
	 *            The file index of the value
	 */
    public void setValue(IObject value, int fileIndex) {
        if (value != ObjectBase.UNDEFINED) {
            this._assignments.add(value, fileIndex);
        }
    }

    /**
	 * Unset the value associated with this property
	 * 
	 * @param fileIndex
	 *            The file index of the value to remove
	 * @param offset
	 *            The file offset of the value to remove
	 */
    public void unsetValue(int fileIndex, int offset) {
        this._assignments.remove(fileIndex, offset);
    }

    /**
	 * Determines if this property has any references in the source code
	 * 
	 * @return Returns true if there are references to this property
	 */
    public boolean hasReferences() {
        return this._referenceCount > 0;
    }

    /**
	 * Retrieve the collection of all assignments for this property
	 * 
	 * @return Returns an OrderedObjectCollection that contains all assignments for this property
	 */
    public OrderedObjectCollection getAssignments() {
        return this._assignments;
    }

    /**
	 * Determines if this property has any assignments. If a property is undefined, it will exist (reference count > 0),
	 * but it will have no values
	 * 
	 * @return Returns true if this property contains one or more assignment values
	 */
    public boolean hasAssignments() {
        return this._assignments.size() > 0;
    }

    /**
	 * Determines if this property is enumerable
	 * 
	 * @return Returns true if this property is enumerable
	 */
    public boolean isEnumerable() {
        return (this._attributes & DONT_ENUM) != DONT_ENUM;
    }

    /**
	 * Determines if this property is internal
	 * 
	 * @return Returns true if this property is internal
	 */
    public boolean isInternal() {
        return (this._attributes & INTERNAL) == INTERNAL;
    }

    /**
	 * Determines if this property is permanent
	 * 
	 * @return Returns true if this property is permanent
	 */
    public boolean isPermanent() {
        return (this._attributes & DONT_DELETE) == DONT_DELETE;
    }

    /**
	 * Determines if this property is read-only
	 * 
	 * @return Returns true if this property is read-only
	 */
    public boolean isReadOnly() {
        return (this._attributes & READ_ONLY) == READ_ONLY;
    }

    /**
	 * Determines if this property is read-only
	 * 
	 * @return Returns true if this property is read-only
	 */
    public boolean isVisible() {
        return (this._attributes & NOT_VISIBLE) != NOT_VISIBLE;
    }

    /**
	 * Create a new instance of Property
	 * 
	 * @param value
	 *            The value of the property
	 * @param fileIndex
	 *            The index of the file where the property value is located
	 * @param attributes
	 *            The attributes of the property
	 */
    public Property(IObject value, int fileIndex, int attributes) {
        this._assignments = new OrderedObjectCollection();
        this._attributes = attributes;
        this.setValue(value, fileIndex);
    }

    /**
	 * Increase the number of references to this property
	 * 
	 * @return Returns the total number of references on this property after adding this reference
	 */
    public int addReference() {
        return ++this._referenceCount;
    }

    /**
	 * Decrease the number of references to this property
	 * 
	 * @return Returns the total number of references on this property after removing this reference.
	 */
    public int removeReference() {
        if (this._referenceCount == 0) {
            throw new IllegalStateException(Messages.Property_RefCountBelowZero);
        }
        return --this._referenceCount;
    }

    /**
	 * Return a String representation of this object
	 * 
	 * @return The string representation of this object
	 */
    public String toString() {
        return this._assignments.toString();
    }

    /**
	 * @see com.aptana.ide.metadata.IDocumentationContainer#getDocumentation()
	 */
    public IDocumentation getDocumentation() {
        return this._documentation;
    }

    /**
	 * @see com.aptana.ide.metadata.IDocumentationContainer#hasDocumentation()
	 */
    public boolean hasDocumentation() {
        return this._documentation != null;
    }

    /**
	 * @see com.aptana.ide.metadata.IDocumentationContainer#setDocumentation(com.aptana.ide.metadata.IDocumentation)
	 */
    public void setDocumentation(IDocumentation documentation) {
        this._documentation = documentation;
    }

    /**
	 * Gets the last valid documentation object based on an offset. This is used when a property is hidden by reassignment.
	 * This will prefer the last declared doc (before cursor position) but will take forward declared docs as well (user code
	 * may doc things in prototype assignments but reassign in ctors for example).
	 * @param sourceFileIndex
	 * @param beginOffset
	 * @return Returns a valid documentation object based on an offset.
	 */
    public IDocumentation getAnyValidDocumentation(int sourceFileIndex, int beginOffset) {
        IDocumentation result = null;
        int index = this._assignments.size() - 1;
        while (index > 0) {
            OrderedObject oo = this._assignments.get(index);
            IObject obj = oo.object;
            if (obj.hasDocumentation()) {
                result = obj.getDocumentation();
                if (sourceFileIndex > oo.fileIndex) {
                    break;
                } else if (sourceFileIndex == oo.fileIndex && beginOffset > obj.getStartingOffset()) {
                    break;
                }
            }
            index--;
        }
        return result;
    }
}
