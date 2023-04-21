/*
 * XCollections.java
 *
 * Created on April 21, 2002, 7:53 PM
 *
 * Copyright (c) Joerg Wassmer
 * This library is free software. You can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 2 or above
 * as published by the Free Software Foundation.
 * For more information please visit <http://jaxlib.sourceforge.net>.
 */

package jaxlib.col;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import jaxlib.array.ObjectArrays;
import jaxlib.jaxlib_private.col.CollectionTools;
import jaxlib.jaxlib_private.col.IterableTools;
import jaxlib.lang.Objects;



/**
 * This class consists of static methods that operate on collections.
 * <p>
 * <b>The methods of this class should never be called from inside of instances of <tt>XCollection</tt> with themselves
 * as argument, except the method's documentation explicitly allows it.</b>
 * </p>
 *
 * @see XCollection
 * @see Lists
 * @see Maps
 * @see Iterators
 * @see java.util.Collection
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: XCollections.java 3051 2012-02-13 01:37:48Z joerg_wassmer $
 */
@SuppressWarnings({"unchecked", "element-type-mismatch"})
public abstract class XCollections extends Object
{


  protected XCollections() throws InstantiationException
  {
    throw new InstantiationException();
  }



  private static final ImmutableCompactHashSet<String> sizeStables;

  static
  {
    final HashSet<String> stables = new HashSet<>(64);

    stables.add("java.beans.beancontext.BeanContextServicesSupport");
    stables.add("java.beans.beancontext.BeanContextSupport");
    stables.add("java.util.ArrayDeque");
    stables.add("java.util.ArrayList");
    stables.add("java.util.Collections$CopiesList");
    stables.add("java.util.HashSet");
    stables.add("java.util.JumboEnumSet");
    stables.add("java.util.LinkedHashSet");
    stables.add("java.util.LinkedList");
    stables.add("java.util.PriorityQueue");
    stables.add("java.util.RegularEnumSet");
    stables.add("java.util.Stack");
    stables.add("java.util.TreeSet");
    stables.add("java.util.Vector");
    stables.add("java.util.concurrent.SynchronousQueue");
    stables.add("javax.management.AttributeList");
    stables.add("javax.management.relation.RoleList");
    stables.add("javax.management.relation.RoleUnresolvedList");
    stables.add("javax.print.attribute.standard.JobStateReasons");
    stables.add("org.hibernate.collection.internal.PersistentBag");
    stables.add("org.hibernate.collection.internal.PersistentIdentifierBag");
    stables.add("org.hibernate.collection.internal.PersistentList");
    stables.add("org.hibernate.collection.internal.PersistentSet");
    stables.add("org.hibernate.collection.internal.PersistentSortedSet");
    stables.add(Collections.EMPTY_LIST.getClass().getName());
    stables.add(Collections.EMPTY_SET.getClass().getName());
    stables.add(Collections.singleton(1).getClass().getName());
    stables.add(Collections.singletonList(1).getClass().getName());
    stables.add(Arrays.asList(1, 2L).getClass().getName());

    Map<?,?> map = new HashMap();
    stables.add(map.entrySet().getClass().getName());
    stables.add(map.keySet().getClass().getName());
    stables.add(map.values().getClass().getName());

    map = new LinkedHashMap();
    stables.add(map.entrySet().getClass().getName());
    stables.add(map.keySet().getClass().getName());
    stables.add(map.values().getClass().getName());

    map = new TreeMap();
    stables.add(map.entrySet().getClass().getName());
    stables.add(map.keySet().getClass().getName());
    stables.add(map.values().getClass().getName());

    map = Collections.singletonMap(1, 2);
    stables.add(map.entrySet().getClass().getName());
    stables.add(map.keySet().getClass().getName());
    stables.add(map.values().getClass().getName());

    map = new IdentityHashMap();
    stables.add(map.entrySet().getClass().getName());
    stables.add(map.keySet().getClass().getName());
    stables.add(map.values().getClass().getName());

    sizeStables = new ImmutableCompactHashSet<>(String.class, stables, null, (Void) null);
  }




  public static <E> boolean addAll(final Collection<E> dest, final Iterable<? extends E> src)
  {
    if (src instanceof Collection)
      return dest.addAll((Collection<E>) src);
    if (dest instanceof XCollection)
      return ((XCollection<E>) dest).addRemaining(src.iterator()) > 0;

    boolean modified = false;
    for (final E e : src)
      modified = dest.add(e) || modified;
    return modified;
  }



  public static <E> boolean addAll(
    final Collection<E>     dest,
    final List<? extends E> src,
    final int               fromIndex,
    final int               toIndex
  )
  {
    return (dest instanceof XCollection)
      ? (((XCollection<E>) dest).addAll(src, fromIndex, toIndex) > 0)
      : CollectionTools.addAll(dest, src, fromIndex, toIndex);
  }



  public static <E,S extends E> boolean addAll(final Collection<E> dest, final S[] src)
  {
    return (dest instanceof XCollection)
      ? (((XCollection<E>) dest).addAll(src) > 0)
      : CollectionTools.addAll(dest, src, 0, src.length);
  }



  public static <E,S extends E> boolean addAll(
    final Collection<E> dest,
    final S[]           src,
    final int           fromIndex,
    final int           toIndex
  )
  {
    return (dest instanceof XCollection)
      ? (((XCollection<E>) dest).addAll(src, fromIndex, toIndex) > 0)
      : CollectionTools.addAll(dest, src, fromIndex, toIndex);
  }




  public static <E> boolean addIfAbsent(final Collection<E> dest, final E e)
  {
    return
        (dest instanceof XCollection)
      ? ((XCollection<E>) dest).addIfAbsent(e)
      : (((dest instanceof Set) || !dest.contains(e)) && dest.add(e));
  }




  public static <E> int addNext(final Collection<E> dest, final Iterator<? extends E> source, int remaining)
  {
    if (remaining < 0)
      return addRemaining(dest, source);
    if (remaining == 0)
      return 0;
    if (remaining == 1)
      return dest.add(source.next()) ? 1 : 0;
    if (dest instanceof XCollection)
      return ((XCollection<E>) dest).addNext(source, remaining);

    if (dest instanceof RandomAccess)
    {
      final int size = dest.size();
      dest.addAll(new ArrayXList<>(source, remaining));  // cast because of compiler bug
      return Math.max(0, dest.size() - size);             // in case of not-sizestable collection.
    }

    int count = 0;
    while (remaining-- > 0)
    {
      if (dest.add(source.next()))
        count++;
    }
    return count;
  }



  public static <E> int addRemaining(final Collection<E> dest, final Iterator<? extends E> source)
  {
    if (dest instanceof XCollection)
      return ((XCollection<E>) dest).addRemaining(source);

    if (dest instanceof RandomAccess)
    {
      final int size = dest.size();
      dest.addAll(new ArrayXList<>(source));  // cast because of compiler bug
      return Math.max(0, dest.size() - size);             // in case of not-sizestable collection.
    }

    int count = 0;
    while (source.hasNext())
    {
      if (dest.add(source.next()))
        count++;
    }
    return count;
  }




  /**
   * Returns true if specified collection contains an element which is identical to specified one.
   *
   *
   * @param data     the collection where to search in.
   * @param identity the object to search for.
   * @return <tt>true</tt> if the collection contains an element, comparison <tt>element == identity</tt> is true for;
   *         <tt>false/tt> if the collection contains no such element.
   * @see XCollection#concontainsIdject)
   * @since JaXLib 1.0
   * @throws NullPointerExccontainsIdta == null</tt>.
   */
  public static boolean containsId(final Collection<?> data, final Object identity)
  {
    if (data instanceof XCollection)
      return ((XCollection) data).containsId(identity);
    else
      return CollectionTools.containsIdentical(data, identity);
  }




  /**
   * Returns the getFirst element of specified collection, as it would be returned by <tt>iterator().next()</tt>.
   *
   * @see XList#fgetFirst)
   * @since JaXLib 1.0
   */
  public static <E> E first(final Collection<E> source)
  {
    if (source instanceof XList)
      return ((XList<E>) source).getFirst();
    else if (source instanceof List)
    {
      final List<E> list = (List<E>) source;
      if (list.isEmpty())
        throw new NoSuchElementException();
      return list.get(0);
    }
    else if (source instanceof SortedSet)
      return ((SortedSet<E>) source).first();
    else
      return source.iterator().next();
  }



  public static <E> E getEqual(final Collection<E> source, final Object e)
  {
    if (e == null)
      throw new NullPointerException();

    if (source instanceof XCollection)
      return ((XCollection<E>) source).getEqual(e);

    if (source instanceof List)
    {
      final List<E> list = (List<E>) source;
      final int index = list.indexOf(e);
      return (index >= 0) ? list.get(index) : null;
    }

    if ((source instanceof Set) && !source.contains(e))
      return null;

    return IterableTools.getEqual(source, e);
  }




  public static <E> E internalize(final Collection<E> dest, final E e)
  {
    if (e == null)
    {
      addIfAbsent(dest, null);
      return null;
    }

    final E b = getEqual(dest, e);
    if (b != null)
      return b;
    dest.add(e);
    return e;
  }



  /**
   * Returns true if this class expects specified collection to be fast executing calls to
   * <tt>contains(Object element)</tt>.
   * Generally, the result will be <tt>true</tt> if the collection is instance of <tt>Set</tt>, or if it is an instance
   * of {@link GenericCollection} returning true for <tt>isFastSearching()</tt>.
   *
   * @see GenericCollection#isFastSearching()
   *
   * @since JaXLib 1.0
   */
  public static boolean isFastSearching(final Collection<?> c)
  {
    return (c instanceof GenericCollection)
      ? ((GenericCollection) c).isFastSearching()
      : (c instanceof Set);
  }



  /**
   * Returns true if this class expects that specified collection will not change its size inside a thread without
   * calls to a remove operation inside of the same thread.
   * <p>
   * Sometimes this method is usefull to avoid copying elements of a collection to another temporary
   * collection or to a temporary array.
   * <pre>
   * boolean askNext = XCollections.isSizeStable(collection);
   * int remaining   = collection.size();
   * for (Iterator it = collection.iterator(); askNext ? it.hasNext() : (remaining-- > 0);)
   * {
   *  doSomething(it.next())
   * }
   * </pre>
   * </p><p>
   * The result can also be used for micro-optimizing routines which are iterating over collections, by avoiding
   * calls to <tt>Iterator.hasNext()</tt>. Please note that such a construct should normally be avoided, because
   * it makes your code harder to read. The performance benefit is minimal to zero, depending on the jvm.
   * </p><p>
   * Classes known to be stable in size are for example <tt>java.util.HashSet</tt> and
   * <tt>java.util.TreeSet</tt>.
   * Classes known <b>not</b> to be stable in size are for example the collection views of
   * <tt>java.util.WeakHashMap</tt> and <tt>jaxlib.col.WeakKeyHashMap</tt>.
   * </p><p>
   * If the collection is instance of <tt>GenericCollection</tt>, this method simply returns
   * <tt>((GenericCollection) c).isSizeStable()</tt>.
   * </p>
   *
   * @return true if this class expects that the collection does not to change its size without calls to a
   *         remove operation.
   *
   * @throws NullPointerException if <tt>c == null</tt>.
   *
   * @see GenericCollection#isSizeStable()
   * @see jaxlib.col.ref
   * @see java.util.Iterator#hasNext()
   * @see java.util.WeakHashMap
   * @see java.util.List
   *
   * @since JaXLib 1.0
   */
  public static boolean isSizeStable(final Collection<?> c)
  {
    return (c instanceof GenericCollection)
      ? ((GenericCollection) c).isSizeStable()
      : XCollections.sizeStables.containsId(c.getClass().getName());
  }



  /**
   * Removes up to specified amount of specified element.
   * <p>
   * <i>The result is undefined</i> if the collection uses an inconsistent equivalence relation, such that the
   * collection handles two objects as equal even if <code>a.equals(b) == false</code>.
   * </p>
   *
   * @param data      the collection to remove elements from.
   * @param e         the element to remove.
   * @param maxCount  the maximum number of elements to remove, or <code>-1</code> to remove all.
   *
   * @throws IllegalArgumentException if <code>maxCount < -1</code>
   * @throws NullPointerException     if <code>data == null</code>
   *
   * @since JaXLib 1.0
   */
  public static int removeCount(final Collection<?> data, final Object e, final int maxCount)
  {
    if (data instanceof XCollection)
      return ((XCollection) data).removeCount(e, maxCount);
    return CollectionTools.removeCount(data, e, maxCount);
  }



  /**
   * Removes up to specified amount of specified identity.
   *
   * @param data      the collection to remove objects from.
   * @param identity  the object to remove.
   * @param maxCount  the maximum number of objects to remove, or <code>-1</code> to remove all.
   *
   * @throws IllegalArgumentException if <code>maxCount < -1</code>
   * @throws NullPointerException     if <code>data == null</code>
   *
   * @since JaXLib 1.0
   */
  public static int removeCountIdentical(final Collection<?> data, final Object identity, final int maxCount)
  {
    if (data instanceof XCollection)
      return ((XCollection) data).removeCountIdentical(identity, maxCount);
    return CollectionTools.removeCountIdentical(data, identity, maxCount);
  }



  /**
   * Removes all occurences of specified element.
   * <p>
   * <i>The result is undefined</i> if the collection uses an inconsistent equivalence relation, such that the
   * collection handles two objects as equal even if <code>a.equals(b) == false</code>.
   * </p>
   *
   * @param data      the collection to remove elements from.
   * @param e         the element to remove.
   *
   * @throws NullPointerException     if <code>data == null</code>
   *
   * @since JaXLib 1.0
   */
  public static int removeEach(final Collection<?> data, final Object e)
  {
    return removeCount(data, e, -1);
  }



  /**
   * Removes all occurences of specified object.
   *
   * @param data      the collection to remove objects from.
   * @param identity  the object to remove.
   *
   * @throws NullPointerException     if <code>data == null</code>
   *
   * @since JaXLib 1.0
   */
  public static int removeEachIdentical(final Collection<?> data, final Object identity)
  {
    return removeCountIdentical(data, identity, -1);
  }




  /**
   * Removes one object which is identical to specified one from specified collection.
   *
   * @return <tt>true</tt> if one element was found, comparison <tt>element == identity</tt> was true for.
   *
   * @param data      the collection where to remove one object from.
   * @param identity  the object to remove.
   *
   * @throws NullPointerException if <tt>data == null</tt>.
   *
   * @since JaXLib 1.0
   */
  public static boolean removeIdentical(final Collection<?> data, final Object identity)
  {
    return removeCountIdentical(data, identity, 1) != 0;
  }



  /**
   * Copies elements of specified collection to a new array of the specified type.
   * <p><i>
   * Implementation note: This method does, unlike <tt>Collection.toArray(dest)</tt>, <b>not</b> set the array element
   * at <tt>source.size()</tt> to <tt>null</tt>.
   * </i></p>
   *
   * @see #toArray(Collection source, Object[] dest, int destIndex)
   * @see Collection#toArray(Object[])
   *
   * @since JaXLib 1.0
   */
  @Nonnull
  public static <E> E[] toArray(final Collection<? extends E> source, @Nullable final Class<E> componentType)
  {
    if ((componentType == null) || (componentType == Object.class))
      return (E[]) source.toArray();
    else if (isSizeStable(source))
      return source.toArray((E[]) Array.newInstance(componentType, source.size()));
    else
      return IterableTools.toArray(source, componentType);
  }



  /**
   * Copies elements of specified collection to specified array.
   * <p><i>
   * Implementation note: This method does, unlike <tt>Collection.toArray(dest)</tt>, <b>not</b> set the array element
   * at <tt>source.size()</tt> to <tt>null</tt>.
   * </i></p>
   *
   * @throws IndexOutOfBoundsException  if <tt>dest.length < source.size()</tt>.
   * @throws NullPointerException       if <tt>(source == null) || (dest == null)</tt>.
   *
   * @see #toArray(Collection source, Object[] dest, int destIndex)
   * @see Collection#toArray(Object[])
   *
   * @since JaXLib 1.0
   */
  @Nonnull
  public static <E> E[] toArray(final Collection<? extends E> source, E[] dest)
  {
    if (dest == null)
      throw new NullPointerException("dest");
    if (dest.length == 0)
      return source.toArray(dest);

    if (source instanceof XCollection)
    {
      final XCollection col = (XCollection<? extends E>) source;
      final int        size = col.size();
      E[] a = (size <= dest.length) ? dest : ObjectArrays.newInstance(dest, size);
      final int count = col.toArray(a, 0);
      if ((a != dest) && (count <= dest.length))
      {
        ObjectArrays.copyFast(a, 0, count, dest, 0);
        a = dest;
      }
      return a;
    }

    if (source instanceof List)
      return Lists.toArray((List<? extends E>) source, dest);
    if (!isSizeStable(source))
      return IterableTools.toArray(source, dest);

    final int size = source.size();
    if (dest.length <= size)
      return source.toArray(dest);

    final E last = dest[size];
    final E[]  b = source.toArray(dest);
    if (dest == b)
      dest[size] = last;
    return b;
  }



  /**
   * Copies elements of specified collection to specified array at specified index.
   *
   * @throws IndexOutOfBoundsException  if <tt>(destIndex + source.size() > dest.length) || (destIndex < 0)</tt>.
   * @throws NullPointerException       if <tt>(source == null) || (dest == null)</tt>.
   *
   * @see XCollection#toArray(Object[],int)
   *
   * @since JaXLib 1.0
   */
  public static <E> int toArray(final Collection<? extends E> source, final E[] dest, final int destIndex)
  {
    return (source instanceof XCollection)
      ? ((XCollection<E>) source).toArray(dest, destIndex)
      : CollectionTools.toArray(source, dest, destIndex);
  }




  public static boolean[] toBooleanArray(final Collection<Boolean> source)
  {
    if (source instanceof List)
      return Lists.toBooleanArray((List<Boolean>) source);

    final int    size = source.size();
    final boolean[] a = new boolean[size];
    if (size == 0)
      return a;

    int i = 0;
    for (final Iterator<Boolean> it = source.iterator(); (i < size) && it.hasNext();)
      a[i++] = it.next();

    return (i == a.length)
      ? a
      : Arrays.copyOf(a, i);
  }



  public static byte[] toByteArray(final Collection<? extends Number> source)
  {
    if (source instanceof List)
      return Lists.toByteArray((List<? extends Number>) source);

    final int size = source.size();
    final byte[] a = new byte[size];
    if (size == 0)
      return a;

    int i = 0;
    for (final Iterator<? extends Number> it = source.iterator(); (i < size) && it.hasNext();)
      a[i++] = it.next().byteValue();

    return (i == a.length)
      ? a
      : Arrays.copyOf(a, i);
  }



  public static char[] toCharArray(final Collection<Character> source)
  {
    if (source instanceof List)
      return Lists.toCharArray((List<Character>) source);

    final int size = source.size();
    final char[] a = new char[size];
    if (size == 0)
      return a;

    int i = 0;
    for (final Iterator<Character> it = source.iterator(); (i < size) && it.hasNext();)
      a[i++] = it.next();

    return (i == a.length)
      ? a
      : Arrays.copyOf(a, i);
  }



  public static double[] toDoubleArray(final Collection<? extends Number> source)
  {
    if (source instanceof List)
      return Lists.toDoubleArray((List<? extends Number>) source);

    final int   size = source.size();
    final double[] a = new double[size];
    if (size == 0)
      return a;

    int i = 0;
    for (final Iterator<? extends Number> it = source.iterator(); (i < size) && it.hasNext();)
      a[i++] = it.next().doubleValue();

    return (i == a.length)
      ? a
      : Arrays.copyOf(a, i);
  }



  public static float[] toFloatArray(final Collection<? extends Number> source)
  {
    if (source instanceof List)
      return Lists.toFloatArray((List<? extends Number>) source);

    final int  size = source.size();
    final float[] a = new float[size];
    if (size == 0)
      return a;

    int i = 0;
    for (final Iterator<? extends Number> it = source.iterator(); (i < size) && it.hasNext();)
      a[i++] = it.next().floatValue();

    return (i == a.length)
      ? a
      : Arrays.copyOf(a, i);
  }



  public static int[] toIntArray(final Collection<? extends Number> source)
  {
    if (source instanceof List)
      return Lists.toIntArray((List<? extends Number>) source);

    final int size = source.size();
    final int[]  a = new int[size];
    if (size == 0)
      return a;

    int i = 0;
    for (final Iterator<? extends Number> it = source.iterator(); (i < size) && it.hasNext();)
      a[i++] = it.next().intValue();

    return (i == a.length)
      ? a
      : Arrays.copyOf(a, i);
  }



  public static long[] toLongArray(final Collection<? extends Number> source)
  {
    if (source instanceof List)
      return Lists.toLongArray((List<? extends Number>) source);

    final int size = source.size();
    final long[] a = new long[size];
    if (size == 0)
      return a;

    int i = 0;
    for (final Iterator<? extends Number> it = source.iterator(); (i < size) && it.hasNext();)
      a[i++] = it.next().longValue();

    return (i == a.length)
      ? a
      : Arrays.copyOf(a, i);
  }



  public static short[] toShortArray(final Collection<? extends Number> source)
  {
    if (source instanceof List)
      return Lists.toShortArray((List<? extends Number>) source);

    final int  size = source.size();
    final short[] a = new short[size];
    if (size == 0)
      return a;

    int i = 0;
    for (final Iterator<? extends Number> it = source.iterator(); (i < size) && it.hasNext();)
      a[i++] = it.next().shortValue();

    return (i == a.length)
      ? a
      : Arrays.copyOf(a, i);
  }



  static boolean shouldVerifyElementTypes(final Collection<?> src, final Class<?> componentType)
  {
    if (componentType == Object.class)
      return false;
    if (!(src instanceof AbstractXCollection))
      return true;

    final AbstractXCollection<?> x = (AbstractXCollection) src;
    return !x.isComponentTypeSecure() || !componentType.isAssignableFrom(x.getComponentType());
  }



  @CheckForNull
  static <E> E[] toArrayOrNull(final Collection<? extends E> source, @Nullable final Class<E> componentType)
  {
    if ((componentType == null) || (componentType == Object.class))
    {
      final E[] a = (E[]) source.toArray(Objects.EMPTY_ARRAY);
      return (a.length > 0) ? a : null;
    }
    else if (XCollections.isSizeStable(source))
    {
      final int size = source.size();
      return (size <= 0)
        ? null
        : source.toArray((E[]) Array.newInstance(componentType, size));
    }
    return IterableTools.toArrayOrNull(source, componentType);
  }



}
