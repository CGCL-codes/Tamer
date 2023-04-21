package org.wam.style;

import org.wam.core.WamElement;
import prisms.util.ArrayUtils;

/**
 * A TypedStyleGroup is a group in WAM that holds members of a given type. This allows styles to be
 * applied not only to named groups ({@link NamedStyleGroup}) but also to specific sub-types within
 * the named group.
 * 
 * @param <E> The sub-type of WamElement that this group holds
 */
public class TypedStyleGroup<E extends WamElement> extends WamStyle {

    private TypedStyleGroup<? super E> theParent;

    private TypedStyleGroup<? extends E>[] theChildren;

    private final Class<E> theType;

    private E[] theMembers;

    /**
	 * Creates a typed style group
	 * 
	 * @param parent The parent style group that this group is a sub-type of
	 * @param type The type of elements that this group is to hold
	 */
    public TypedStyleGroup(TypedStyleGroup<? super E> parent, Class<E> type) {
        theParent = parent;
        theType = type;
        theChildren = new TypedStyleGroup[0];
        theMembers = (E[]) java.lang.reflect.Array.newInstance(type, 0);
    }

    @Override
    public TypedStyleGroup<? super E> getParent() {
        return theParent;
    }

    /**
	 * @return This group's type
	 */
    public Class<E> getType() {
        return theType;
    }

    /**
	 * @return The number of children groups in this group
	 */
    public int getChildCount() {
        return theChildren.length;
    }

    /**
	 * @param idx The index of the child group to get
	 * @return This group's child group at the given index
	 */
    public TypedStyleGroup<? extends E> getChild(int idx) {
        return theChildren[idx];
    }

    @SuppressWarnings("unchecked")
    public synchronized <S extends E> TypedStyleGroup<S> insertTypedGroup(Class<S> type) {
        if (type == theType) return (TypedStyleGroup<S>) this;
        if (!type.isAssignableFrom(getType())) throw new IllegalArgumentException("Type " + type.getName() + " is not a sub-type of this group (" + this + ")'s type (" + getType().getName() + ")");
        TypedStyleGroup[] children = theChildren;
        for (int c = 0; c < children.length; c++) if (children[c].getType().isAssignableFrom(type)) return children[c].insertTypedGroup(type.asSubclass(theChildren[c].getType()));
        TypedStyleGroup<S> ret = new TypedStyleGroup<S>(this, type);
        ret.addDependent(this);
        for (int c = 0; c < children.length; c++) if (type.isAssignableFrom(children[c].getType())) {
            ret.theChildren = ArrayUtils.add(ret.theChildren, children[c]);
            children[c].removeDependent(this);
            children[c].addDependent(ret);
            children[c].theParent = ret;
            children = ArrayUtils.remove(children, c);
            c--;
        }
        children = ArrayUtils.add(children, ret);
        theChildren = children;
        return ret;
    }

    @SuppressWarnings("unchecked")
    public <S extends E> TypedStyleGroup<? super S> getGroupForType(Class<S> type) {
        final TypedStyleGroup<? extends E>[] children = theChildren;
        for (TypedStyleGroup child : children) if (child.getType().isAssignableFrom(type)) return child.getGroupForType(type.asSubclass(child.getType()));
        return this;
    }

    @Override
    public void clear(StyleAttribute<?> attr) {
        super.clear(attr);
    }

    /**
	 * @return The number of members in this group (not including its sub-typed groups)
	 */
    public int getMemberCount() {
        return theMembers.length;
    }

    /**
	 * @param element The element to determine membership in this group
	 * @return Whether the given element is a member of this group
	 */
    public boolean isMember(E element) {
        TypedStyleGroup<?> group = getGroupForType((Class<? extends E>) element.getClass());
        return ArrayUtils.contains((E[]) group.theMembers, element);
    }

    void addMember(E member) {
        addMember(member, theMembers.length);
    }

    <T extends E, V extends T> void addMember(V member, int index) {
        TypedStyleGroup<T> group = (TypedStyleGroup<T>) getGroupForType((Class<V>) member.getClass());
        if (index < 0) index = group.theMembers.length;
        group.theMembers = prisms.util.ArrayUtils.add(group.theMembers, member, index);
    }

    void removeMember(E member) {
        TypedStyleGroup<?> group = getGroupForType((Class<? extends E>) member.getClass());
        theMembers = prisms.util.ArrayUtils.remove((E[]) group.theMembers, member);
    }

    /**
	 * @return An iterable to iterate through every member in this group and its sub-typed groups.
	 *         The iterator returned by this method is an immutable capture of the content of this
	 *         group and its sub-typed groups at the moment. Any changes made to this group or its
	 *         subgroups after the iterator is created are not reflected in the iterator's content,
	 *         and the iterator itself is immutable, so it cannot affect the content of this group.
	 */
    public Iterable<E> members() {
        return new MemberIterable<E, E>(this, theMembers, theChildren, theType);
    }

    /**
	 * Creates an iterator to iterate through every member in this group, but not its sub-typed
	 * groups. The iterator returned by this method is an immutable capture of the content of this
	 * group at the moment. Any changes made to this group after the iterator is created are not
	 * reflected in the iterator's content, and the iterator itself is immutable, so it cannot
	 * affect the content of this group.
	 * 
	 * @return An iterable that can return an iterator to iterate through this group's members
	 *         without descending into this group's sub-members
	 */
    public Iterable<E> shallowMembers() {
        return new MemberIterable<E, E>(this, theMembers, new TypedStyleGroup[0], theType);
    }

    /**
	 * Creates an iterator to iterate through every member in this group and its sub-typed groups
	 * that is an instance of the given type. The iterator returned by this method is an immutable
	 * capture of the content of this group and its sub-typed groups at the moment. Any changes made
	 * to this group or its subgroups after the iterator is created are not reflected in the
	 * iterator's content, and the iterator itself is immutable, so it cannot affect the content of
	 * this group.
	 * 
	 * The argument class may be an extension of {@link WamElement} or an arbitrary interface. If a
	 * class is given whose type is not compatible with WamElement, the iterator will of course be
	 * empty.
	 * 
	 * @param <T> The compile-time type of the members to return
	 * @param type The runtime type of the members to return
	 * @return An iterable that can return an iterator to iterate through every member of this group
	 *         and its subgroups that is also an instance of the given class.
	 */
    public <T> Iterable<T> members(Class<T> type) {
        return new MemberIterable<E, T>(this, theMembers, theChildren, type);
    }

    private static class MemberIterable<E extends WamElement, T> implements Iterable<T> {

        private final TypedStyleGroup<E> theGroup;

        private final E[] theMembers;

        private final TypedStyleGroup<? extends E>[] theChildren;

        private final Class<T> theType;

        MemberIterable(TypedStyleGroup<E> group, E[] members, TypedStyleGroup<? extends E>[] children, Class<T> type) {
            theGroup = group;
            theMembers = members;
            theChildren = children;
            theType = type;
        }

        public MemberIterator<E, T> iterator() {
            return new MemberIterator<E, T>(theGroup, theMembers, theChildren, theType);
        }
    }

    private static class MemberIterator<E extends WamElement, T> implements java.util.ListIterator<T> {

        private final TypedStyleGroup<E> theGroup;

        private final E[] members;

        private final TypedStyleGroup<? extends E>[] theChildren;

        private final Class<T> theType;

        private java.util.ListIterator<? extends T>[] childIters;

        private int memberIndex;

        private int childIndex;

        private int overallIndex;

        MemberIterator(TypedStyleGroup<E> group, E[] _members, TypedStyleGroup<? extends E>[] _children, Class<T> type) {
            theGroup = group;
            members = _members;
            theChildren = _children;
            theType = type;
            childIters = new java.util.ListIterator[theChildren.length];
            for (int c = 0; c < theChildren.length; c++) {
                if (theGroup.getType().equals(theType)) childIters[c] = (java.util.ListIterator<? extends T>) theChildren[c].iterator(); else if (type.isInterface() || theType.isAssignableFrom(theChildren[c].getType())) childIters[c] = (java.util.ListIterator<T>) theChildren[c].members(type).iterator();
            }
        }

        public boolean hasNext() {
            if (memberIndex < members.length) return true;
            if (childIndex < theChildren.length) {
                while ((childIters[childIndex] == null || !childIters[childIndex].hasNext()) && childIndex < theChildren.length) childIndex++;
                return childIndex < theChildren.length && childIters[childIndex] != null && childIters[childIndex].hasNext();
            }
            return false;
        }

        public T next() {
            T ret = null;
            do {
                if (theType.isInstance(members[memberIndex])) ret = theType.cast(members[memberIndex]);
                memberIndex++;
            } while (memberIndex < members.length && ret == null);
            if (ret != null) return ret;
            if (childIndex < theChildren.length) {
                while ((childIters[childIndex] == null || !childIters[childIndex].hasNext()) && childIndex < theChildren.length) childIndex++;
                if (childIndex == theChildren.length) throw new IndexOutOfBoundsException("No next member");
                ret = childIters[childIndex].next();
            } else throw new IndexOutOfBoundsException("No next member");
            overallIndex++;
            return ret;
        }

        public int nextIndex() {
            return overallIndex;
        }

        public boolean hasPrevious() {
            if (memberIndex > 0) return true;
            if (childIndex > 0) return true;
            if (childIters[childIndex] != null) return childIters[childIndex].hasPrevious();
            return false;
        }

        public T previous() {
            T ret = null;
            if (childIndex > 0 && (childIndex == theChildren.length || childIters[childIndex] == null)) childIndex--;
            while (childIndex > 0 && (childIters[childIndex] == null || !childIters[childIndex].hasPrevious())) childIndex--;
            if (childIndex < theChildren.length && childIters[childIndex] != null && childIters[childIndex].hasPrevious()) ret = childIters[childIndex].previous(); else if (memberIndex > 0) {
                do {
                    memberIndex--;
                    if (theType.isInstance(members[memberIndex])) ret = theType.cast(members[memberIndex]);
                } while (memberIndex > 0 && ret == null);
            }
            if (ret == null) throw new IndexOutOfBoundsException("No previous member");
            overallIndex--;
            return ret;
        }

        public int previousIndex() {
            return overallIndex - 1;
        }

        public void add(T e) {
            throw new UnsupportedOperationException("TypedStyleGroup iterators do not support modification");
        }

        public void remove() {
            throw new UnsupportedOperationException("TypedStyleGroup iterators do not support modification");
        }

        public void set(T e) {
            throw new UnsupportedOperationException("TypedStyleGroup iterators do not support modification");
        }
    }
}
