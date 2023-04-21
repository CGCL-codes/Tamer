package tudresden.ocl20.pivot.tools.codegen.ocl2java.types.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import tudresden.ocl20.pivot.tools.codegen.ocl2java.types.OclInvalidException;

/**
 * <p>
 * Represents utility class to provide operations of OCL Collections in Java.
 * </p>
 * 
 * @author Claas Wilke
 */
public class OclCollections {

    /**
	 * <p>
	 * Returns the Bag that contains all the elements from self.
	 * </p>
	 * 
	 * @param self
	 *            The {@link Collection} representing self.
	 * @return The Bag that contains all the elements from self.
	 */
    public static <T extends Object> List<T> asBag(Collection<T> self) {
        if (self == null) {
            return new ArrayList<T>();
        }
        return new ArrayList<T>(self);
    }

    /**
	 * <p>
	 * Returns the Bag that contains all the elements from self.
	 * </p>
	 * 
	 * @param self
	 *            The array representing self.
	 * @return The Bag that contains all the elements from self.
	 */
    public static <T extends Object> List<T> asBag(T[] self) {
        if (self == null) {
            return new ArrayList<T>();
        }
        return Arrays.asList(self);
    }

    /**
	 * <p>
	 * Returns an OrderedSet that contains all the elements from self, with
	 * duplicates removed, in an order dependent on the particular concrete
	 * collection type.
	 * </p>
	 * 
	 * @param self
	 *            The {@link Collection} representing self.
	 * @return An OrderedSet that contains all the elements from self, with
	 *         duplicates removed, in an order dependent on the particular
	 *         concrete collection type.
	 */
    public static <T extends Object> List<T> asOrderedSet(Collection<T> self) {
        if (self == null) {
            return new ArrayList<T>();
        }
        List<T> result = new ArrayList<T>();
        for (T element : self) {
            if (!result.contains(element)) {
                result.add(element);
            }
        }
        return result;
    }

    /**
	 * <p>
	 * Returns an OrderedSet that contains all the elements from self, with
	 * duplicates removed, in an order dependent on the particular concrete
	 * collection type.
	 * </p>
	 * 
	 * @param self
	 *            The array representing self.
	 * @return An OrderedSet that contains all the elements from self, with
	 *         duplicates removed, in an order dependent on the particular
	 *         concrete collection type.
	 */
    public static <T extends Object> List<T> asOrderedSet(T[] self) {
        if (self == null) {
            return new ArrayList<T>();
        }
        return asOrderedSet(Arrays.asList(self));
    }

    /**
	 * <p>
	 * Returns the Sequence that contains all the elements from self, in an
	 * order dependent on the particular concrete collection type.
	 * </p>
	 * 
	 * @param self
	 *            The {@link Collection} representing self.
	 * @return The Sequence that contains all the elements from self, in an
	 *         order dependent on the particular concrete collection type.
	 */
    public static <T extends Object> List<T> asSequence(Collection<T> self) {
        if (self == null) {
            return new ArrayList<T>();
        }
        return new ArrayList<T>(self);
    }

    /**
	 * <p>
	 * Returns the Sequence that contains all the elements from self, in an
	 * order dependent on the particular concrete collection type.
	 * </p>
	 * 
	 * @param self
	 *            The array representing self.
	 * @return The Sequence that contains all the elements from self, in an
	 *         order dependent on the particular concrete collection type.
	 */
    public static <T extends Object> List<T> asSequence(T[] self) {
        if (self == null) {
            return new ArrayList<T>();
        }
        return Arrays.asList(self);
    }

    /**
	 * <p>
	 * Returns the Set containing all the elements from self, with duplicates
	 * removed.
	 * </p>
	 * 
	 * @param self
	 *            The {@link Collection} representing self.
	 * @return The Set containing all the elements from self, with duplicates
	 *         removed.
	 */
    public static <T extends Object> Set<T> asSet(Collection<T> self) {
        if (self == null) {
            return new HashSet<T>();
        }
        return new HashSet<T>(self);
    }

    /**
	 * <p>
	 * Returns the Set containing all the elements from self, with duplicates
	 * removed.
	 * </p>
	 * 
	 * @param self
	 *            The array representing self.
	 * @return The Set containing all the elements from self, with duplicates
	 *         removed.
	 */
    public static <T extends Object> Set<T> asSet(T[] self) {
        if (self == null) {
            return new HashSet<T>();
        }
        return new HashSet<T>(Arrays.asList(self));
    }

    /**
	 * <p>
	 * Returns the number of times that object occurs in the collection self.
	 * </p>
	 * 
	 * @param self
	 *            The {@link Collection} representing self.
	 * @param object
	 *            The {@link Object} to count.
	 * @return The number of times that object occurs in the collection self.
	 */
    public static <T extends Object> Integer count(Collection<T> self, T object) {
        assert (object != null);
        if (self == null) {
            self = new ArrayList<T>();
        }
        int result;
        result = 0;
        for (T element : self) {
            if (element.equals(object)) {
                result++;
            }
        }
        return result;
    }

    /**
	 * <p>
	 * Returns the number of times that object occurs in the collection self.
	 * </p>
	 * 
	 * @param self
	 *            The array representing self.
	 * @param object
	 *            The {@link Object} to count.
	 * @return The number of times that object occurs in the collection self.
	 */
    public static <T extends Object> Integer count(T[] self, T object) {
        assert (object != null);
        if (self == null) {
            return 0;
        }
        return count(Arrays.asList(self), object);
    }

    /**
	 * <p>
	 * Returns <code>true</code> if c is a collection of the same kind as self
	 * and contains the same elements in the same quantities and in the same
	 * order, in the case of an ordered collection type.
	 * </p>
	 * 
	 * @param self
	 *            The {@link Collection} to be checked.
	 * @param c2
	 *            The {@link Collection} which elements shall be checked.
	 * @return <code>true</code> if c is a collection of the same kind as self
	 *         and contains the same elements in the same quantities and in the
	 *         same order, in the case of an ordered collection type.
	 */
    public static <T1 extends Object, T2 extends Object> Boolean equals(Collection<T2> self, Collection<T1> c2) {
        if (c2 == null) {
            c2 = new ArrayList<T1>();
        }
        if (self == null) {
            self = new ArrayList<T2>();
        }
        return self.equals(c2);
    }

    /**
	 * <p>
	 * Returns <code>true</code> if c is a collection of the same kind as self
	 * and contains the same elements in the same quantities and in the same
	 * order, in the case of an ordered collection type.
	 * </p>
	 * 
	 * @param self
	 *            The array to be checked.
	 * @param c2
	 *            The {@link Collection} which elements shall be checked.
	 * @return <code>true</code> if c is a collection of the same kind as self
	 *         and contains the same elements in the same quantities and in the
	 *         same order, in the case of an ordered collection type.
	 */
    public static <T1 extends Object, T2 extends Object> Boolean equals(T2[] self, Collection<T1> c2) {
        if (c2 == null) {
            c2 = new ArrayList<T1>();
        }
        if (self == null) {
            return equals(new ArrayList<T2>(), c2);
        }
        return equals(Arrays.asList(self), c2);
    }

    /**
	 * <p>
	 * Returns <code>true</code> if c is a collection of the same kind as self
	 * and contains the same elements in the same quantities and in the same
	 * order, in the case of an ordered collection type.
	 * </p>
	 * 
	 * @param self
	 *            The {@link Collection} to be checked.
	 * @param c2
	 *            The array which elements shall be checked.
	 * @return <code>true</code> if c is a collection of the same kind as self
	 *         and contains the same elements in the same quantities and in the
	 *         same order, in the case of an ordered collection type.
	 */
    public static <T1 extends Object, T2 extends Object> Boolean equals(Collection<T2> self, T1[] c2) {
        if (c2 == null) {
            return equals(self, new ArrayList<T1>());
        }
        if (self == null) {
            self = new ArrayList<T2>();
        }
        return equals(self, Arrays.asList(c2));
    }

    /**
	 * <p>
	 * Returns <code>true</code> if c is a collection of the same kind as self
	 * and contains the same elements in the same quantities and in the same
	 * order, in the case of an ordered collection type.
	 * </p>
	 * 
	 * @param self
	 *            The array to be checked.
	 * @param c2
	 *            The array which elements shall be checked.
	 * @return <code>true</code> if c is a collection of the same kind as self
	 *         and contains the same elements in the same quantities and in the
	 *         same order, in the case of an ordered collection type.
	 */
    public static <T1 extends Object, T2 extends Object> Boolean equals(T2[] self, T1[] c2) {
        if (c2 == null) {
            return equals(self, new ArrayList<T1>());
        }
        if (self == null) {
            return equals(new ArrayList<T1>(), c2);
        }
        return equals(Arrays.asList(self), Arrays.asList(c2));
    }

    /**
	 * <p>
	 * Returns <code>true</code> if object is not an element of self, false
	 * otherwise.
	 * </p>
	 * 
	 * @param self
	 *            The {@link Collection} representing self.
	 * @param object
	 *            The {@link Object} which shall be searched for.
	 * @return <code>true</code> if object is not an element of self, false
	 *         otherwise.
	 */
    public static <T extends Object> Boolean excludes(Collection<T> self, T object) {
        return !includes(self, object);
    }

    /**
	 * <p>
	 * Returns <code>true</code> if object is not an element of self, false
	 * otherwise.
	 * </p>
	 * 
	 * @param self
	 *            The array representing self.
	 * @param object
	 *            The {@link Object} which shall be searched for.
	 * @return <code>true</code> if object is not an element of self, false
	 *         otherwise.
	 */
    public static <T extends Object> Boolean excludes(T[] self, T object) {
        return !includes(self, object);
    }

    /**
	 * <p>
	 * Returns <code>true</code> if self contains none of the elements of c2.
	 * </p>
	 * 
	 * @param self
	 *            The {@link Collection} to be checked.
	 * @param c2
	 *            The {@link Collection} which elements shall be checked.
	 * @return <code>true</code> if self contains none of the elements of c2.
	 */
    public static <T1 extends Object, T2 extends Object> Boolean excludesAll(Collection<T2> self, Collection<T1> c2) {
        if (c2 == null) {
            c2 = new ArrayList<T1>();
        }
        if (self == null) {
            self = new ArrayList<T2>();
        }
        boolean result;
        result = true;
        if (!self.isEmpty()) {
            for (Object element : c2) {
                result &= !self.contains(element);
            }
        }
        return result;
    }

    /**
	 * <p>
	 * Returns <code>true</code> if self contains none of the elements of c2.
	 * </p>
	 * 
	 * @param self
	 *            The array to be checked.
	 * @param c2
	 *            The {@link Collection} which elements shall be checked.
	 * @return <code>true</code> if self contains none of the elements of c2.
	 */
    public static <T1 extends Object, T2 extends Object> Boolean excludesAll(T2[] self, Collection<T1> c2) {
        if (self == null) {
            return excludesAll(new ArrayList<T2>(), c2);
        }
        return excludesAll(Arrays.asList(self), c2);
    }

    /**
	 * <p>
	 * Returns <code>true</code> if self contains none of the elements of c2.
	 * </p>
	 * 
	 * @param self
	 *            The {@link Collection} to be checked.
	 * @param c2
	 *            The array which elements shall be checked.
	 * @return <code>true</code> if self contains none of the elements of c2.
	 */
    public static <T1 extends Object, T2 extends Object> Boolean excludesAll(Collection<T2> self, T1[] c2) {
        if (c2 == null) {
            return excludesAll(self, new ArrayList<T2>());
        }
        return excludesAll(self, Arrays.asList(c2));
    }

    /**
	 * <p>
	 * Returns <code>true</code> if self contains none of the elements of c2.
	 * </p>
	 * 
	 * @param self
	 *            The array to be checked.
	 * @param c2
	 *            The array which elements shall be checked.
	 * @return <code>true</code> if self contains none of the elements of c2.
	 */
    public static <T1 extends Object, T2 extends Object> Boolean excludesAll(T2[] self, T1[] c2) {
        if (self == null) {
            return true;
        } else if (c2 == null) {
            return excludesAll(Arrays.asList(self), new ArrayList<T1>());
        }
        return excludesAll(Arrays.asList(self), Arrays.asList(c2));
    }

    /**
	 * <p>
	 * If the element type is not a collection type, this results in the same
	 * collection as self. If the element type is a collection type, the result
	 * is a collection containing all the elements of all the recursively
	 * flattened elements of self.
	 * </p>
	 * 
	 * @param self
	 *            The {@link Collection} representing self.
	 * @return The flattened {@link Collection}.
	 */
    @SuppressWarnings("unchecked")
    public static <T extends Object> Collection<?> flatten(Collection<T> self) {
        Collection<Object> result;
        result = new ArrayList<Object>();
        if (self == null) {
            return result;
        }
        for (T element : self) {
            if (element == null) {
                result.add(element);
            } else if (element instanceof Collection<?>) {
                result.addAll((Collection<?>) element);
            } else if (element.getClass().isArray()) {
                result.addAll(Arrays.asList((T[]) element));
            } else {
                result.add(element);
            }
        }
        return result;
    }

    /**
	 * <p>
	 * If the element type is not a collection type, this results in the same
	 * collection as self. If the element type is a collection type, the result
	 * is a collection containing all the elements of all the recursively
	 * flattened elements of self.
	 * </p>
	 * 
	 * @param self
	 *            The array representing self.
	 * @return The flattened {@link Collection}.
	 */
    public static <T extends Object> Collection<?> flatten(T[] self) {
        if (self == null) {
            return new ArrayList<Object>();
        }
        return flatten(Arrays.asList(self));
    }

    /**
	 * <p>
	 * Returns <code>true</code> if object is an element of self, false
	 * otherwise.
	 * </p>
	 * 
	 * @param self
	 *            The {@link Collection} to be checked.
	 * @param object
	 *            The {@link Object} which shall be searched for.
	 * @return <code>true</code> if object is an element of self, false
	 *         otherwise.
	 */
    public static <T extends Object> Boolean includes(Collection<T> self, T object) {
        if (self == null) {
            return false;
        } else if (object == null) {
            throw new OclInvalidException("Parameter 'object' must not be null.");
        }
        return self.contains(object);
    }

    /**
	 * <p>
	 * Returns <code>true</code> if object is an element of self, false
	 * otherwise.
	 * </p>
	 * 
	 * @param object
	 *            The {@link Object} which shall be searched for.
	 * @param self
	 *            The array to be checked.
	 * @return <code>true</code> if object is an element of self, false
	 *         otherwise.
	 */
    public static <T extends Object> Boolean includes(T[] self, T object) {
        if (self == null) {
            return false;
        } else if (object == null) {
            throw new OclInvalidException("Parameter 'object' must not be null.");
        }
        return includes(Arrays.asList(self), object);
    }

    /**
	 * <p>
	 * Returns <code>true</code> if self contains all the elements of c2.
	 * </p>
	 * 
	 * @param self
	 *            The {@link Collection} to be checked.
	 * @param c2
	 *            The {@link Collection} which elements shall be checked.
	 * @return <code>true</code> if self contains all the elements of c2.
	 */
    public static <T1 extends Object, T2 extends Object> Boolean includesAll(Collection<T2> self, Collection<T1> c2) {
        if (self == null) {
            self = new ArrayList<T2>();
        }
        if (c2 == null) {
            c2 = new ArrayList<T1>();
        }
        return self.containsAll(c2);
    }

    /**
	 * <p>
	 * Returns <code>true</code> if self contains all the elements of c2.
	 * </p>
	 * 
	 * @param self
	 *            The array to be checked.
	 * @param c2
	 *            The {@link Collection} which elements shall be checked.
	 * @return <code>true</code> if self contains all the elements of c2.
	 */
    public static <T1 extends Object, T2 extends Object> Boolean includesAll(T2[] self, Collection<?> c2) {
        if (self == null) {
            return includesAll(new ArrayList<T2>(), c2);
        }
        return includesAll(Arrays.asList(self), c2);
    }

    /**
	 * <p>
	 * Returns <code>true</code> if self contains all the elements of c2.
	 * </p>
	 * 
	 * @param self
	 *            The {@link Collection} to be checked.
	 * @param c2
	 *            The array which elements shall be checked.
	 * @return <code>true</code> if self contains all the elements of c2.
	 */
    public static <T1 extends Object, T2 extends Object> Boolean includesAll(Collection<T2> self, T1[] c2) {
        assert (c2 != null);
        if (c2 == null) {
            return includesAll(self, new ArrayList<T2>());
        }
        return includesAll(self, Arrays.asList(c2));
    }

    /**
	 * <p>
	 * Returns <code>true</code> if self contains all the elements of c2.
	 * </p>
	 * 
	 * @param self
	 *            The array to be checked.
	 * @param c2
	 *            The array which elements shall be checked.
	 * @return <code>true</code> if self contains all the elements of c2.
	 */
    public static <T1 extends Object, T2 extends Object> Boolean includesAll(T2[] self, T1[] c2) {
        if (c2 == null) {
            return true;
        } else if (self == null) {
            return includesAll(new ArrayList<T2>(), Arrays.asList(c2));
        }
        return includesAll(Arrays.asList(self), Arrays.asList(c2));
    }

    /**
	 * <p>
	 * Returns <code>true</code> if self is empty.
	 * </p>
	 * 
	 * @param self
	 *            The {@link Collection} representing self.
	 * @return <code>true</code> if self is empty.
	 */
    public static <T extends Object> Boolean isEmpty(Collection<T> self) {
        if (self == null) {
            return true;
        }
        return self.isEmpty();
    }

    /**
	 * <p>
	 * Returns <code>true</code> if self is empty.
	 * </p>
	 * 
	 * @param self
	 *            The array representing self.
	 * @return <code>true</code> if self is empty.
	 */
    public static <T extends Object> Boolean isEmpty(T[] self) {
        if (self == null) {
            return true;
        }
        return self.length == 0;
    }

    /**
	 * <p>
	 * Returns the element with the maximum value of all elements in self.
	 * Elements must be of a type implementing {@link Comparable}.
	 * </p>
	 * 
	 * @param self
	 *            The {@link Collection} representing self.
	 * @return The element with the maximum value of all elements in self.
	 *         Elements must be of a type implementing {@link Comparable}.
	 */
    public static <T extends Object> T max(Collection<T> self) {
        if (self == null || self.size() == 0) {
            return null;
        }
        T result;
        result = Collections.max(self, new Comparator<T>() {

            @SuppressWarnings("unchecked")
            public int compare(T first, T second) {
                if (first instanceof Comparable<?> && second instanceof Comparable<?>) {
                    Comparable<Object> firstComparable;
                    Comparable<Object> secondComparable;
                    firstComparable = (Comparable<Object>) first;
                    secondComparable = (Comparable<Object>) second;
                    return firstComparable.compareTo(secondComparable);
                } else {
                    throw new OclInvalidException("Cannot compare elements of collection.");
                }
            }
        });
        return result;
    }

    /**
	 * <p>
	 * Returns the element with the maximum value of all elements in self.
	 * Elements must be of a type implementing {@link Comparable}.
	 * </p>
	 * 
	 * @param self
	 *            The array representing self.
	 * @return The element with the maximum value of all elements in self.
	 *         Elements must be of a type implementing {@link Comparable}.
	 */
    public static <T extends Comparable<T>> T max(T[] self) {
        if (self == null || self.length == 0) {
            return null;
        }
        return max(Arrays.asList(self));
    }

    /**
	 * <p>
	 * Returns the element with the minimum value of all elements in self.
	 * Elements must be of a type implementing {@link Comparable}.
	 * </p>
	 * 
	 * @param self
	 *            The {@link Collection} representing self.
	 * @return The element with the minimum value of all elements in self.
	 *         Elements must be of a type implementing {@link Comparable}.
	 */
    public static <T extends Object> T min(Collection<T> self) {
        if (self == null || self.size() == 0) {
            return null;
        }
        T result;
        result = Collections.min(self, new Comparator<T>() {

            @SuppressWarnings("unchecked")
            public int compare(T first, T second) {
                if (first instanceof Comparable<?> && second instanceof Comparable<?>) {
                    Comparable<Object> firstComparable;
                    Comparable<Object> secondComparable;
                    firstComparable = (Comparable<Object>) first;
                    secondComparable = (Comparable<Object>) second;
                    return firstComparable.compareTo(secondComparable);
                } else {
                    throw new OclInvalidException("Cannot compare elements of collection.");
                }
            }
        });
        return result;
    }

    /**
	 * <p>
	 * Returns the element with the minimum value of all elements in self.
	 * Elements must be of a type implementing {@link Comparable}.
	 * </p>
	 * 
	 * @param self
	 *            The array representing self.
	 * @return The element with the minimum value of all elements in self.
	 *         Elements must be of a type implementing {@link Comparable}.
	 */
    public static <T extends Comparable<T>> T min(T[] self) {
        if (self == null || self.length == 0) {
            return null;
        }
        return min(Arrays.asList(self));
    }

    /**
	 * <p>
	 * Returns <code>true</code> if self is not empty.
	 * </p>
	 * 
	 * @param self
	 *            The {@link Collection} representing self.
	 * @return <code>true</code> if self is not empty.
	 */
    public static <T extends Object> Boolean notEmpty(Collection<T> self) {
        if (self == null) {
            return false;
        }
        return !self.isEmpty();
    }

    /**
	 * <p>
	 * Returns <code>true</code> if self is not empty.
	 * </p>
	 * 
	 * @param self
	 *            The array representing self.
	 * @return <code>true</code> if self is not empty.
	 */
    public static <T extends Object> Boolean notEmpty(T[] self) {
        if (self == null) {
            return false;
        }
        return self.length > 0;
    }

    /**
	 * <p>
	 * Returns <code>true</code> if c is not equal to self.
	 * </p>
	 * 
	 * @param self
	 *            The {@link Collection} to be checked.
	 * @param c2
	 *            The {@link Collection} which elements shall be checked.
	 * @return <code>true</code> if c is not equal to self.
	 */
    public static <T1 extends Object, T2 extends Object> Boolean notEquals(Collection<T2> self, Collection<T1> c2) {
        if (c2 == null) {
            c2 = new ArrayList<T1>();
        }
        if (self == null) {
            self = new ArrayList<T2>();
        }
        return !self.equals(c2);
    }

    /**
	 * <p>
	 * Returns <code>true</code> if c is not equal to self.
	 * </p>
	 * 
	 * @param self
	 *            The array to be checked.
	 * @param c2
	 *            The {@link Collection} which elements shall be checked.
	 * @return <code>true</code> if c is not equal to self.
	 */
    public static <T1 extends Object, T2 extends Object> Boolean notEquals(T2[] self, Collection<T1> c2) {
        if (c2 == null) {
            c2 = new ArrayList<T1>();
        }
        if (self == null) {
            return notEquals(new ArrayList<T2>(), c2);
        }
        return notEquals(Arrays.asList(self), c2);
    }

    /**
	 * <p>
	 * Returns <code>true</code> if c is not equal to self.
	 * </p>
	 * 
	 * @param self
	 *            The {@link Collection} to be checked.
	 * @param c2
	 *            The array which elements shall be checked.
	 * @return <code>true</code> if c is not equal to self.
	 */
    public static <T1 extends Object, T2 extends Object> Boolean notEquals(Collection<T2> self, T1[] c2) {
        if (c2 == null) {
            return notEquals(self, new ArrayList<T1>());
        }
        if (self == null) {
            self = new ArrayList<T2>();
        }
        return notEquals(self, Arrays.asList(c2));
    }

    /**
	 * <p>
	 * Returns <code>true</code> if c is not equal to self.
	 * </p>
	 * 
	 * @param self
	 *            The array to be checked.
	 * @param c2
	 *            The array which elements shall be checked.
	 * @return <code>true</code> if c is not equal to self.
	 */
    public static <T1 extends Object, T2 extends Object> Boolean notEquals(T2[] self, T1[] c2) {
        if (c2 == null) {
            return notEquals(self, new ArrayList<T1>());
        }
        if (self == null) {
            return notEquals(new ArrayList<T1>(), c2);
        }
        return notEquals(Arrays.asList(self), Arrays.asList(c2));
    }

    /**
	 * <p>
	 * Returns the cartesian product of self and c2.
	 * </p>
	 * 
	 * @param self
	 *            The {@link Collection} representing self.
	 * @param c2
	 *            The second {@link Collection}.
	 * @return The cartesian product of self and c2.
	 */
    public static <T1 extends Object, T2 extends Object> Set<Map<String, Object>> product(Collection<T1> self, Collection<T2> c2) {
        if (self == null) {
            self = new ArrayList<T1>();
        }
        if (c2 == null) {
            c2 = new ArrayList<T2>();
        }
        Set<Map<String, Object>> result;
        result = new HashSet<Map<String, Object>>();
        for (T1 element1 : self) {
            for (Object element2 : c2) {
                Map<String, Object> tuple;
                tuple = new HashMap<String, Object>();
                tuple.put("first", element1);
                tuple.put("second", element2);
                result.add(tuple);
            }
        }
        return result;
    }

    /**
	 * <p>
	 * Returns the cartesian product of self and c2.
	 * </p>
	 * 
	 * @param self
	 *            The array representing self.
	 * @param c2
	 *            The second {@link Collection}.
	 * @return The cartesian product of self and c2.
	 */
    public static <T1 extends Object, T2 extends Object> Set<Map<String, Object>> product(T1[] self, Collection<T2> c2) {
        if (self == null) {
            return product(new ArrayList<T1>(), c2);
        } else if (c2 == null) {
            c2 = new ArrayList<T2>();
        }
        return product(Arrays.asList(self), c2);
    }

    /**
	 * <p>
	 * Returns the cartesian product of self and c2.
	 * </p>
	 * 
	 * @param self
	 *            The {@link Collection} representing self.
	 * @param c2
	 *            The second array.
	 * @return The cartesian product of self and c2.
	 */
    public static <T1 extends Object, T2 extends Object> Set<Map<String, Object>> product(Collection<T1> self, T2[] c2) {
        if (self == null) {
            self = new ArrayList<T1>();
        }
        if (c2 == null) {
            return product(self, new ArrayList<T2>());
        }
        return product(self, Arrays.asList(c2));
    }

    /**
	 * <p>
	 * Returns the cartesian product of self and c2.
	 * </p>
	 * 
	 * @param self
	 *            The array representing self.
	 * @param c2
	 *            The second array.
	 * @return The cartesian product of self and c2.
	 */
    public static <T1 extends Object, T2 extends Object> Set<Map<String, Object>> product(T1[] self, T2[] c2) {
        if (self == null) {
            return product(new ArrayList<T1>(), c2);
        } else if (c2 == null) {
            return product(self, new ArrayList<T2>());
        }
        return product(Arrays.asList(self), Arrays.asList(c2));
    }

    /**
	 * <p>
	 * Returns the number of elements in self.
	 * </p>
	 * 
	 * @param self
	 *            The {@link Collection} representing self.
	 * @return The number of elements in self.
	 */
    public static <T extends Object> Integer size(Collection<T> self) {
        if (self == null) {
            return 0;
        }
        return self.size();
    }

    /**
	 * <p>
	 * Returns the number of elements in self.
	 * </p>
	 * 
	 * @param self
	 *            The array representing self.
	 * @return The number of elements in self.
	 */
    public static <T extends Object> Integer size(T[] self) {
        if (self == null) {
            return 0;
        }
        return self.length;
    }

    /**
	 * <p>
	 * Returns the addition of all elements in self.
	 * </p>
	 * 
	 * @param self
	 *            The {@link Collection} representing self.
	 * @return The addition of all elements in self.
	 */
    public static <T extends Number> Number sum(Collection<T> self) {
        Number result;
        result = new Double(0);
        if (self == null) {
            return new Double(0);
        }
        for (T element : self) {
            if (element instanceof Number) {
                result = result.doubleValue() + ((Number) element).doubleValue();
            } else {
                throw new OclInvalidException("Operation Collection.sum() is not defined for elements of the type " + element.getClass().getCanonicalName() + ".");
            }
        }
        return result;
    }

    /**
	 * <p>
	 * Returns the addition of all elements in self.
	 * </p>
	 * 
	 * @param self
	 *            The array representing self.
	 * @return The addition of all elements in self.
	 */
    public static Number sum(Integer[] self) {
        if (self == null) {
            return new Double(0);
        }
        return sum(Arrays.asList(self));
    }

    /**
	 * <p>
	 * Returns the addition of all elements in self.
	 * </p>
	 * 
	 * @param self
	 *            The array representing self.
	 * @return The addition of all elements in self.
	 */
    public static Number sum(Float[] self) {
        if (self == null) {
            return new Double(0);
        }
        return sum(Arrays.asList(self));
    }
}
