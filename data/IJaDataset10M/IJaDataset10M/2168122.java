package net.sourceforge.ondex.core.util;

import it.unimi.dsi.fastutil.ints.IntIterator;
import net.sourceforge.ondex.core.*;
import net.sourceforge.ondex.exception.type.AccessDeniedException;
import java.util.*;

public class ONDEXViewFunctions {

    /**
     * Return "and" of two given ONDEXView.
     *
     * @param view1 ONDEXView<T>
     * @param view2 ONDEXView<T>
     * @return ONDEXView<T>
     */
    public static <T extends ONDEXEntity> Set<T> and(Set<T> view1, Set<T> view2) {
        Set<T> newset = copy(view1);
        newset.retainAll(view2);
        return newset;
    }

    /**
     * Return "or" of two given ONDEXView.
     *
     * @param view1 ONDEXView<T>
     * @param view2 ONDEXView<T>
     * @return ONDEXView<T>
     */
    public static <T extends ONDEXEntity> Set<T> or(Set<T> view1, Set<T> view2) {
        Set<T> newset = copy(view1);
        newset.addAll(view2);
        return newset;
    }

    /**
     * Return new set containing "andNot" of two given ONDEXView.
     *
     * @param view1 ONDEXView<T>
     * @param view2 ONDEXView<T>
     * @return ONDEXView<T>
     */
    public static <T extends ONDEXEntity> Set<T> andNot(Set<T> view1, Set<T> view2) {
        Set<T> newset = copy(view1);
        newset.removeAll(view2);
        return newset;
    }

    @SuppressWarnings("unchecked")
    public static <AnyType extends ONDEXEntity> Set<AnyType> create(final ONDEXGraph aog, Class<AnyType> c, ONDEXBitSet set) {
        if (ONDEXConcept.class.equals(c)) {
            return (Set<AnyType>) (Set) new ONDEXViewImpl<ONDEXConcept>(set) {

                @Override
                protected ONDEXConcept reallyGetEntity(int i) {
                    return aog.getConcept(i);
                }

                @Override
                public Class<ONDEXConcept> getDataType() {
                    return ONDEXConcept.class;
                }
            };
        } else if (ONDEXRelation.class.equals(c)) {
            return (Set<AnyType>) (Set) new ONDEXViewImpl<ONDEXRelation>(set) {

                @Override
                protected ONDEXRelation reallyGetEntity(int i) {
                    return aog.getRelation(i);
                }

                @Override
                public Class<ONDEXRelation> getDataType() {
                    return ONDEXRelation.class;
                }
            };
        } else {
            throw new ClassCastException("Can't instantiate ONDEXViewImpl for: " + c);
        }
    }

    public static <E> Set<E> copy(Set<E> set) {
        if (set instanceof UnmodifiableSet) set = ((UnmodifiableSet<E>) set).set;
        if (set instanceof ONDEXViewImpl) {
            ONDEXViewImpl sat = (ONDEXViewImpl) set;
            return sat.clone();
        } else {
            return new HashSet<E>(set);
        }
    }

    public static <E> Set<E> unmodifiableSet(Set<E> set) {
        if (set instanceof UnmodifiableSet) return set;
        return new UnmodifiableSet<E>(set);
    }

    public static final class UnmodifiableSet<E> implements Set<E> {

        private final Set<E> set;

        private UnmodifiableSet(Set<E> set) {
            this.set = set;
        }

        @Override
        public int size() {
            return set.size();
        }

        @Override
        public boolean isEmpty() {
            return set.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return set.contains(o);
        }

        @Override
        public Object[] toArray() {
            return set.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return set.toArray(a);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return set.containsAll(c);
        }

        @Override
        public boolean equals(Object o) {
            return set.equals(o);
        }

        @Override
        public int hashCode() {
            return set.hashCode();
        }

        @Override
        public Iterator<E> iterator() {
            final Iterator<E> it = set.iterator();
            return new Iterator<E>() {

                @Override
                public boolean hasNext() {
                    return it.hasNext();
                }

                @Override
                public E next() {
                    return it.next();
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        @Override
        public boolean add(E e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Provides a iterator type view based on BitSets on all Objects which implement
     * Viewable (AbstractConcept and AbstractRelation).
     *
     * @author taubertj
     * @param <AnyType>
     */
    private abstract static class ONDEXViewImpl<AnyType extends ONDEXEntity> extends AbstractSet<AnyType> implements ONDEXView<AnyType>, Cloneable {

        /**
         * Represents view of participating AbstractConcept or AbstractRelation IDs.
         */
        private ONDEXBitSet set;

        /**
         * Constructors initialises all internal variables for a given class and
         * BitSet.
         *
         * @param set SparseBitSet
         */
        private ONDEXViewImpl(ONDEXBitSet set) {
            this.set = set;
            if (set == null) throw new NullPointerException("Null set used in ONDEXViewImpl constructor");
        }

        /**
         * Returns a deep clone of the set but clones only the reference to graph and Class (obviously)
         */
        @SuppressWarnings({ "unchecked" })
        @Override
        public ONDEXViewImpl<AnyType> clone() {
            try {
                ONDEXViewImpl<AnyType> newImpl = (ONDEXViewImpl<AnyType>) super.clone();
                newImpl.set = (ONDEXBitSet) set.clone();
                return newImpl;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }

        /**
         * returns false if the element is not contained in the view or if the user
         * has no permission to view the element.
         *
         * @throws net.sourceforge.ondex.exception.type.AccessDeniedException if the object parameter does not provide read permission.
         */
        @Override
        public boolean contains(Object o) throws AccessDeniedException {
            if (o != null && getDataType().isAssignableFrom(o.getClass())) {
                if (o instanceof ONDEXEntity) {
                    ONDEXEntity e = (ONDEXEntity) o;
                    return set.get(e.getId());
                }
            }
            return false;
        }

        /**
         * Checks for all objects of a given Collection to be contained in this
         * iterator.
         *
         * @param c Collection<?>
         * @return boolean
         * @throws net.sourceforge.ondex.exception.type.AccessDeniedException
         */
        @Override
        public boolean containsAll(Collection<?> c) throws AccessDeniedException {
            if (size() == 0) return false;
            for (Object aC : c) {
                if (!contains(aC)) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Returns true if this collection contains no elements.
         *
         * @return true if this collection contains no elements
         */
        @Override
        public boolean isEmpty() {
            return (size() == 0);
        }

        protected abstract AnyType reallyGetEntity(int i);

        protected abstract Class<AnyType> getDataType();

        @Override
        public int size() {
            return set.countSetBits();
        }

        @Override
        public Iterator<AnyType> iterator() {
            return new Iterator<AnyType>() {

                IntIterator i = set.iterator();

                @Override
                public boolean hasNext() {
                    return i.hasNext();
                }

                @Override
                public AnyType next() {
                    int i1 = i.nextInt();
                    if (set.get(i1)) {
                        return reallyGetEntity(i1);
                    } else {
                        return null;
                    }
                }

                @Override
                public void remove() {
                    i.remove();
                }
            };
        }

        public boolean remove(Object obj) {
            if (obj instanceof ONDEXEntity) {
                boolean exists = set.get(((ONDEXEntity) obj).getId());
                set.clear(((ONDEXEntity) obj).getId());
                return exists;
            }
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            if (c instanceof ONDEXViewImpl) {
                int size = set.countSetBits();
                set.andNot(((ONDEXViewImpl) c).set);
                return size != set.countSetBits();
            } else {
                return super.removeAll(c);
            }
        }

        @Override
        public boolean add(AnyType c) {
            boolean isSet = set.get(c.getId());
            set.set(c.getId());
            return isSet;
        }

        @Override
        public boolean addAll(Collection<? extends AnyType> c) {
            if (c instanceof ONDEXViewImpl) {
                int size = set.countSetBits();
                set.or(((ONDEXViewImpl) c).set);
                return size != set.countSetBits();
            } else {
                return super.addAll(c);
            }
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            if (c instanceof ONDEXViewImpl) {
                int size = set.countSetBits();
                set.and(((ONDEXViewImpl) c).set);
                return size != set.countSetBits();
            } else {
                return super.retainAll(c);
            }
        }
    }
}
