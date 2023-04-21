/*
 * RestrictedXList.java
 *
 * Created on April 28, 2002, 10:47 PM
 *
 * Copyright (c) Joerg Wassmer
 * This library is free software. You can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 2 or above
 * as published by the Free Software Foundation.
 * For more information please visit <http://jaxlib.sourceforge.net>.
 */
package jaxlib.col.restricted;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

import jaxlib.closure.Closure;
import jaxlib.closure.Filter;
import jaxlib.col.ArrayXList;
import jaxlib.col.ObjectArray;
import jaxlib.col.XCollections;
import jaxlib.col.XList;
import jaxlib.jaxlib_private.CheckArg;
import jaxlib.util.AccessType;
import jaxlib.util.AccessTypeSet;
import jaxlib.util.BiDi;
import jaxlib.util.CheckBounds;
import jaxlib.util.sorting.SortAlgorithm;


/**
 * Provides a restricted view of a <tt>XList</tt> for denying access to modifying operations like <tt>add</tt>,
 * <tt>remove</tt> and <tt>set</tt>. The most common usage is to create an unmodifiable view of a list.
 * <p>
 * Only modifying operations can be restricted - query operations like <tt>contains</tt> or <tt>get</tt> can not.
 * </p>
 *
 * @see #unmodifiable(XList)
 * @see #allow(XList, AccessTypeSet)
 * @see #deny(XList, AccessTypeSet)
 * @see java.util.Collections#unmodifiableList(java.util.List)
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: RestrictedXList.java 3029 2011-12-29 00:36:48Z joerg_wassmer $
 */
@SuppressWarnings("unchecked")
public class RestrictedXList<E> extends RestrictedXCollection<E> implements XList<E>, Serializable
{

  /**
   * @since JaXLib 1.0
   */
  private static final long serialVersionUID = 1L;


  /**
   * Returns an fixed-size view of the specified list.
   * <p>
   * All operations of the returned list, which would change its size, thus all <tt>add</tt> and <tt>remove</tt>
   * operations, will throw an <tt>UnsupportedOperationException</tt>.
   * </p><p>
   * If the specified list is instance of <tt>RandomAccess</tt>, the returned list also will be instance of.
   * </p>
   *
   * @return A fixed-size view of the specified list.
   *
   * @param delegate the list to restrict access for.
   *
   * @throws NullPointerException if specified list is <tt>null</tt>.
   *
   * @since JaXLib 1.0
   */
  public static <E> RestrictedXList<E> fixedSize(final XList<E> delegate)
  {
    return allow(delegate, AccessTypeSet.READ_SET);
  }



  /**
   * Returns an unmodifiable view of the specified list.
   * <p>
   * All modifying operations of the returned list, like <tt>add</tt>, <tt>remove</tt> and <tt>set</tt>, will throw an
   * <tt>UnsupportedOperationException</tt>.
   * </p><p>
   * If the specified list is instance of <tt>RandomAccess</tt>, the returned list also will be instance of.
   * </p>
   *
   * @return An unmodifiable view of the specified list.
   *
   * @param delegate the list to restrict access for.
   *
   * @throws NullPointerException if specified list is <tt>null</tt>.
   *
   * @see java.util.Collections#unmodifiableList(java.util.List)
   * @see java.lang.UnsupportedOperationException
   * @see jaxlib.col.XCollection#accessTypes()
   * @see java.util.RandomAccess
   *
   * @since JaXLib 1.0
   */
  public static <E> RestrictedXList<E> unmodifiable(final XList<E> delegate)
  {
    return allow(delegate, AccessTypeSet.READ_ONLY);
  }


  /**
   * Returns a view of the specified list which allows only specified types of operations.
   * <p>
   * All modifying operations of the returned list, which are not included in specified set of accesstypes, will throw
   * an <tt>UnsupportedOperationException</tt>.
   * </p><p>
   * If the specified list is instance of <tt>RandomAccess</tt>, the returned collection also will be instance of.
   * </p>
   *
   * @return A view of specified list which's operations are limited to specified set of accesstypes.
   *
   * @param delegate      the list to restrict access for.
   * @param allowedAccess the types of operations to allow the specified list to be accessed through.
   *
   * @throws NullPointerException if the specified list or the set of accesstypes is <tt>null</tt>.
   *
   * @see java.lang.UnsupportedOperationException
   * @see jaxlib.col.XCollection#accessTypes()
   * @see #unmodifiable(XList)
   * @see #deny(XList, AccessTypeSet)
   * @see java.util.RandomAccess
   *
   * @since JaXLib 1.0
   */
  public static <E> RestrictedXList<E> allow(final XList<E> delegate, final AccessTypeSet allowedAccess)
  {
    return deny(delegate, allowedAccess.denied());
  }


  /**
   * Returns a view of the specified list which denies the specified types of operations.
   * <p>
   * All modifying operations of the returned list, which are included in specified set of accesstypes, will throw an
   * <tt>UnsupportedOperationException</tt>.
   * </p><p>
   * If the specified list is instance of <tt>RandomAccess</tt>, the returned list also will be instance of.
   * </p>
   *
   * @return
   *  A view of the specified list which's operations are limited to those not contained in specified set of
   *  accesstypes.
   *
   * @param delegate      the list to restrict access for.
   * @param deniedAccess  the types of operations of the list to deny.
   *
   * @throws NullPointerException if the specified list or the set of accesstypes is <tt>null</tt>.
   *
   * @see jaxlib.col.XCollection#accessTypes()
   * @see #unmodifiable(XList)
   * @see #allow(XList, AccessTypeSet)
   * @see java.util.RandomAccess
   *
   * @since JaXLib 1.0
   */
  public static <E> RestrictedXList<E> deny(final XList<E> delegate, final AccessTypeSet deniedAccess)
  {
    if ((delegate instanceof RestrictedXList) && delegate.accessTypes().deniesAll(deniedAccess.except(AccessType.READ)))
      return (RestrictedXList<E>) delegate;
    else if (delegate instanceof RandomAccess)
      return new RestrictedRandomAccessXList<>(delegate, deniedAccess.denied().and(AccessType.READ));
    else
      return new RestrictedXList<>(delegate, deniedAccess.denied().and(AccessType.READ));
  }




  private static <E> Collection<E> secureSize(final Collection<E> src)
  {
    if (isSecureSize(src))
      return src;
    return (Collection<E>) ObjectArray.readOnly(src.toArray());
  }





  /**
   * @serial
   * @since JaXLib 1.0
   */
  private final XList<E> delegate;


  /**
   * Constructs a new <tt>RestrictedXList</tt>.
   * This constructor is for subclasses only. Use the static factory methods for access from outside.
   *
   * @param delegate      the list to restrict access to.
   * @param allowedAccess the set of operations to allow.
   *
   * @throws NullPointerException if the specified list or the set of accesstypes is <tt>null</tt>.
   *
   * @see #unmodifiable(XList)
   * @see #allow(XList, AccessTypeSet)
   * @see #deny(XList, AccessTypeSet)
   *
   * @since JaXLib 1.0
   */
  protected RestrictedXList(final XList<E> delegate, final AccessTypeSet allowedAccess)
  {
    super(delegate, allowedAccess);
    this.delegate = delegate;
  }



  @Override
  public final void allocate(final int index, final int count)
  {
    checkAdd();
    this.delegate.allocate(index, count);
  }



  @Override
  public final void add(final int index, final E e)
  {
    checkAdd();
    this.delegate.add(index, e);
  }



  @Override
  public boolean addAll(final int index, final Collection<? extends E> source)
  {
    //checkAdd();
    //return this.delegate.addAll(index, source(source));

    if ((source == this) || (source == (Object) this.delegate))
    {
      checkAdd();
      return this.delegate.addAll(index, this.delegate);
    }
    else
    {
      // use iterator to avoid unsecure addAll implementations
      if (XCollections.isSizeStable(source))
        return addNext(index, source.iterator(), source.size()) > 0;
      else
        return addNext(index, source.iterator(), -1) > 0;
    }
  }



  @Override
  public boolean addAll(final int index, final List<? extends E> source, final int fromIndex, final int toIndex)
  {
    //checkAdd();
    //return this.delegate.addAll(index, source(source), fromIndex, toIndex);
    if ((source == this) || (source == (Object) this.delegate))
    {
      checkAdd();
      return this.delegate.addAll(index, this.delegate, fromIndex, toIndex);
    }
    else
    {
      // use iterator to avoid unsecure addAll implementations
      return addNext(index, source.listIterator(fromIndex), toIndex - fromIndex) > 0;
    }
  }



  @Override
  public final boolean addAll(final int index, final E[] source)
  {
    checkAdd();
    return this.delegate.addAll(index, source);
  }



  @Override
  public final boolean addAll(final int index, final E[] source, final int fromIndex, final int toIndex)
  {
    checkAdd();
    return this.delegate.addAll(index, source, fromIndex, toIndex);
  }



  @Override
  public final void addCount(final int index, final E e, final int count)
  {
    checkAdd();
    this.delegate.addCount(index, e, count);
  }



  @Override
  public final void addFirst(final E e)
  {
    checkAdd();
    this.delegate.addFirst(e);
  }



  @Override
  public final int addNext(final int index, final Iterator<? extends E> source, final int remaining)
  {
    checkAdd();
    return this.delegate.addNext(index, source, remaining);
  }



  @Override
  public final int addRemaining(final Iterator<? extends E> source)
  {
    checkAdd();
    return this.delegate.addRemaining(source);
  }



  @Override
  public final int addRemaining(final int index, final Iterator<? extends E> source)
  {
    checkAdd();
    return this.delegate.addRemaining(index, source);
  }



  @Override
  public final void at(final int index, final E e)
  {
    checkSet();
    this.delegate.at(index, e);
  }



  @Override
  public final int copy(final int fromIndex, final int toIndex, final int destIndex)
  {
    int size = this.delegate.size();
    CheckArg.copyListRangeIntern(size, fromIndex, toIndex, destIndex);
    if ((destIndex + (toIndex - fromIndex)) > size)
      checkAdd();
    if (destIndex != size)
      checkSet();
    return this.delegate.copy(fromIndex, toIndex, destIndex);
  }



  @Override
  public int copy(final int index, final Collection<? extends E> source)
  {
    if ((source == this) || (source == this.delegate))
      return copy(0, size(), index);

    int size = this.delegate.size();
    CheckArg.rangeForAdding(size, index);

    // avoid eventually unsecure copy() method of delegate
    if (XCollections.isSizeStable(source))
      return copyNext(index, source.iterator(), source.size());
    else
      return copy(index, (E[]) source.toArray());
  }



  @Override
  public int copy(final int index, final List<? extends E> source, final int fromIndex, final int toIndex)
  {
    if ((source == this) || (source == this.delegate))
      return copy(fromIndex, toIndex, index);

    int size = this.delegate.size();
    CheckArg.rangeForAdding(size, index);
    CheckBounds.range(source.size(), fromIndex, toIndex);
    if (fromIndex == toIndex)
      return 0;


    // avoid eventually unsecure copy() method of delegate
    if (fromIndex == 0)
      return copyNext(index, source.iterator(), toIndex - fromIndex);
    else
      return copyNext(index, source.listIterator(fromIndex), toIndex - fromIndex);
  }



  @Override
  public final int copy(final int index, final E[] source)
  {
    int size = this.delegate.size();
    CheckArg.rangeForAdding(size, index);
    if ((index + source.length) > size)
      checkAdd();
    if (index != size)
      checkSet();
    return this.delegate.copy(index, source);
  }



  @Override
  public int copy(final int index, final E[] source, final int fromIndex, final int toIndex)
  {
    int size = this.delegate.size();
    CheckArg.rangeForAdding(size, index);
    CheckBounds.range(source.length, fromIndex, toIndex);
    if ((index + (toIndex - fromIndex)) > size)
      checkAdd();
    if (index != size)
      checkSet();
    return this.delegate.copy(index, source, fromIndex, toIndex);
  }



  @Override
  public int copyNext(final int index, final Iterator<? extends E> source, final int remaining)
  {
    if (remaining < 0)
    {
      final ArrayXList<E> l = new ArrayXList<>();
      l.addNext(source, remaining);
      return copy(index, l, 0, l.size());
    }
    else
    {
      final int size = this.delegate.size();
      CheckArg.rangeForAdding(size, index);
      if (index != size)
        checkSet();
      if ((index + remaining) > size)
        checkAdd();
      return this.delegate.copyNext(index, source, remaining);
    }
  }



  @Override
  public final void clear(final int index)
  {
    checkRemove();
    this.delegate.clear(index);
  }



  @Override
  public final void clear(final int fromIndex, final int toIndex)
  {
    if (fromIndex == toIndex)
      CheckBounds.range(size(), fromIndex, toIndex);
    else
    {
      checkRemove();
      this.delegate.clear(fromIndex, toIndex);
    }
  }



  @Override
  public final void clearFirst()
  {
    checkRemove();
    this.delegate.clearFirst();
  }



  @Override
  public final void clearLast()
  {
    checkRemove();
    this.delegate.clearLast();
  }



  @Override
  public Iterator<E> descendingIterator()
  {
    return new RestrictedIterator<>(this.delegate.descendingIterator(), this.allowedAccess);
  }



  @Override
  public void fill(final E e)
  {
    checkSet();
    this.delegate.fill(e);
  }



  @Override
  public final int forEach(int fromIndex, int toIndex, Closure<? super E> procedure)
  {
    return this.delegate.forEach(fromIndex, toIndex, procedure);
  }



  @Override
  public final int forEach(int fromIndex, int toIndex, BiDi dir, Closure<? super E> procedure)
  {
    return this.delegate.forEach(fromIndex, toIndex, dir, procedure);
  }



  @Override
  public Iterator<E> iterator()
  {
    return iterator(0);
  }



  @Override
  public Iterator<E> iterator(int fromIndex)
  {
    return new RestrictedIterator<>(this.delegate.iterator(fromIndex), this.allowedAccess);
  }



  @Override
  public ListIterator<E> listIterator()
  {
    return listIterator(0);
  }



  @Override
  public ListIterator<E> listIterator(int fromIndex)
  {
    return new RestrictedListIterator<>(this.delegate.listIterator(fromIndex), this.allowedAccess);
  }



  @Override
  public ListIterator<E> from(Object e)
  {
    return from(0, this.delegate.size(), e);
  }



  @Override
  public ListIterator<E> from(int fromIndex, int toIndex, Object e)
  {
    ListIterator<E> it = this.delegate.from(fromIndex, toIndex, e);
    return (it == null) ? null : new RestrictedListIterator<>(it, this.allowedAccess);
  }



  @Override
  public ListIterator<E> fromId(Object identity)
  {
    return fromId(0, this.delegate.size(), identity);
  }



  @Override
  public ListIterator<E> fromId(int fromIndex, int toIndex, Object identity)
  {
    ListIterator<E> it = this.delegate.fromId(fromIndex, toIndex, identity);
    return (it == null) ? null : new RestrictedListIterator<>(it, this.allowedAccess);
  }



  @Override
  public ListIterator<E> fromLast(Object e)
  {
    return fromLast(0, this.delegate.size(), e);
  }



  @Override
  public ListIterator<E> fromLast(int fromIndex, int toIndex, Object e)
  {
    ListIterator<E> it = this.delegate.fromLast(fromIndex, toIndex, e);
    return (it == null) ? null : new RestrictedListIterator<>(it, this.allowedAccess);
  }



  @Override
  public ListIterator<E> fromLastId(Object identity)
  {
    return fromLastId(0, this.delegate.size(), identity);
  }



  @Override
  public ListIterator<E> fromLastId(int fromIndex, int toIndex, Object identity)
  {
    ListIterator<E> it = this.delegate.fromLastId(fromIndex, toIndex, identity);
    return (it == null) ? null : new RestrictedListIterator<>(it, this.allowedAccess);
  }



  @Override
  public final void move(int fromIndex, int toIndex, int destIndex)
  {
    checkSet();
    this.delegate.move(fromIndex, toIndex, destIndex);
  }



  @Override
  public final E remove(int index)
  {
    checkRemove();
    return this.delegate.remove(index);
  }



  @Override
  public final boolean remove(int fromIndex, int toIndex, Object e)
  {
    checkRemove();
    return this.delegate.remove(fromIndex, toIndex, e);
  }



  @Override
  public final int removeAll(int fromIndex, int toIndex, Collection<?> source)
  {
    checkRemove();
    return this.delegate.removeAll(fromIndex, toIndex, source(source));
  }



  @Override
  public final int removeCount(int fromIndex, int toIndex, Object e, int max)
  {
    checkRemove();
    return this.delegate.removeCount(fromIndex, toIndex, e, max);
  }



  @Override
  public final int removeCountIdentical(int fromIndex, int toIndex, Object identity, int maxCount)
  {
    checkRemove();
    return this.delegate.removeCountIdentical(fromIndex, toIndex, identity, maxCount);
  }



  @Override
  public final int removeEach(int fromIndex, int toIndex, Object e)
  {
    checkRemove();
    return this.delegate.removeEach(fromIndex, toIndex, e);
  }



  @Override
  public final int removeEachIdentical(int fromIndex, int toIndex, Object identity)
  {
    checkRemove();
    return this.delegate.removeEachIdentical(fromIndex, toIndex, identity);
  }


  @Override
  public final int removeMatches(int fromIndex, int toIndex, Filter<? super E> condition, boolean iF)
  {
    checkRemove();
    return this.delegate.removeMatches(fromIndex, toIndex, condition, iF);
  }



  @Override
  public final int removeMatches(
    final int               fromIndex,
    final int               toIndex,
    final BiDi              dir,
    final Filter<? super E> condition,
    final boolean           iF,
    final int               maxCount,
    final boolean           stopOnDismatch
  )
  {
    checkRemove();
    return this.delegate.removeMatches(fromIndex, toIndex, dir, condition, iF, maxCount, stopOnDismatch);
  }



  @Override
  public final boolean removeId(int fromIndex, int toIndex, Object identity)
  {
    checkRemove();
    return this.delegate.removeId(fromIndex, toIndex, identity);
  }



  @Override
  public final E removeFirst()
  {
    checkRemove();
    return this.delegate.removeFirst();
  }



  @Override
  public final int removeFirst(Object e)
  {
    checkRemove();
    return this.delegate.removeFirst(e);
  }



  @Override
  public final int removeFirst(int fromIndex, int toIndex, Object e)
  {
    checkRemove();
    return this.delegate.removeFirst(fromIndex, toIndex, e);
  }



  @Override
  public final int removeFirstId(Object identity)
  {
    checkRemove();
    return this.delegate.removeFirstId(identity);
  }



  @Override
  public final int removeFirstId(int fromIndex, int toIndex, Object identity)
  {
    checkRemove();
    return this.delegate.removeFirstId(fromIndex, toIndex, identity);
  }



  @Override
  public final E removeLast()
  {
    checkRemove();
    return this.delegate.removeLast();
  }



  @Override
  public final int removeLast(Object e)
  {
    checkRemove();
    return this.delegate.removeLast(e);
  }



  @Override
  public final int removeLast(int fromIndex, int toIndex, Object e)
  {
    checkRemove();
    return this.delegate.removeLast(fromIndex, toIndex, e);
  }



  @Override
  public final int removeLastId(Object identity)
  {
    checkRemove();
    return this.delegate.removeLastId(identity);
  }



  @Override
  public final int removeLastId(int fromIndex, int toIndex, Object identity)
  {
    checkRemove();
    return this.delegate.removeLastId(fromIndex, toIndex, identity);
  }



  @Override
  public void replaceContent(Collection<? extends E> source)
  {
    if ((source == this) || (source == (Object) this.delegate))
      return;

    source = secureSize(source);
    int sourceSize = source.size();
    if (sourceSize == 0)
    {
      clear();
      return;
    }

    int size = size();
    if (size == 0)
      checkAdd();
    else
    {
      checkSet();
      if (sourceSize > size)
        checkAdd();
      else
        checkRemove();
    }

    this.delegate.replaceContent(source);
  }



  @Override
  public void replaceContent(int fromIndex, int toIndex, Collection<? extends E> source)
  {
    if ((source == this) || (source == (Object) this.delegate))
    {
      if ((fromIndex == 0) && (toIndex == size()))
        return;
      else
        source = (Collection<E>) ObjectArray.readOnly(source.toArray());
    }

    source = secureSize(source);
    int sourceSize = source.size();
    if (sourceSize == 0)
    {
      clear(fromIndex, toIndex);
      return;
    }

    int size = size();
    if (size == 0)
      checkAdd();
    else
    {
      checkSet();
      if (sourceSize > size)
        checkAdd();
      else
        checkRemove();
    }

    this.delegate.replaceContent(fromIndex, toIndex, source);
  }



  @Override
  public int replaceEach(Object oldElement, E newElement)
  {
    checkSet();
    return this.delegate.replaceEach(oldElement, newElement);
  }



  @Override
  public int replaceEachIdentical(Object oldElement, E newElement)
  {
    checkSet();
    return this.delegate.replaceEachIdentical(oldElement, newElement);
  }



  @Override
  public int replaceEach(int fromIndex, int toIndex, Object oldElement, E newElement)
  {
    checkSet();
    return this.delegate.replaceEach(fromIndex, toIndex, oldElement, newElement);
  }



  @Override
  public int replaceEachIdentical(int fromIndex, int toIndex, Object oldElement, E newElement)
  {
    checkSet();
    return this.delegate.replaceEachIdentical(fromIndex, toIndex, oldElement, newElement);
  }



  @Override
  public final int retainAll(int fromIndex, int toIndex, Collection<?> source)
  {
    checkRemove();
    return this.delegate.retainAll(fromIndex, toIndex, source(source));
  }



  @Override
  public final void reverse()
  {
    checkSet();
    this.delegate.reverse();
  }



  @Override
  public final void reverse(int fromIndex, int toIndex)
  {
    checkSet();
    this.delegate.reverse(fromIndex, toIndex);
  }



  @Override
  public final void rotate(int distance)
  {
    checkSet();
    this.delegate.rotate(distance);
  }



  @Override
  public final void rotate(int fromIndex, int toIndex, int distance)
  {
    checkSet();
    this.delegate.rotate(fromIndex, toIndex, distance);
  }



  @Override
  public final E set(int index, E e)
  {
    checkSet();
    return this.delegate.set(index, e);
  }



  @Override
  public void set(int fromIndex, int toIndex, E e)
  {
    checkSet();
    this.delegate.set(fromIndex, toIndex, e);
  }



  @Override
  public final void sort()
  {
    checkSet();
    this.delegate.sort();
  }



  @Override
  public final void sort(final Comparator<? super E> c)
  {
    checkSet();
    this.delegate.sort(c);
  }



  @Override
  public final void sort(int fromIndex, int toIndex)
  {
    checkSet();
    this.delegate.sort(fromIndex, toIndex);
  }



  @Override
  public final void sort(int fromIndex, int toIndex, SortAlgorithm algo)
  {
    checkSet();
    if (algo == null)
      algo = SortAlgorithm.getDefault();
    if (algo.isSecure())
      this.delegate.sort(fromIndex, toIndex, algo);
    else
      algo.apply(this, fromIndex, toIndex);
  }



  @Override
  public final void sort(SortAlgorithm algo)
  {
    checkSet();
    if (algo == null)
      algo = SortAlgorithm.getDefault();
    if (algo.isSecure())
      this.delegate.sort(algo);
    else
      algo.apply(this);
  }



  @Override
  public void sort(int fromIndex, int toIndex, Comparator<? super E> c, SortAlgorithm algo)
  {
    checkSet();
    if (algo == null)
      algo = SortAlgorithm.getDefault();
    if (algo.isSecure())
      this.delegate.sort(fromIndex, toIndex, c, algo);
    else
      algo.apply(this, fromIndex, toIndex, c);
  }



  @Override
  public XList<E> subList(int fromIndex, int toIndex)
  {
    return RestrictedXList.allow(this.delegate.subList(fromIndex, toIndex), this.allowedAccess);
  }




  @Override
  public final void swap(int index1, int index2)
  {
    checkSet();
    this.delegate.swap(index1, index2);
  }



  @Override
  public final void swapRanges(int index1, int index2, int count)
  {
    checkSet();
    this.delegate.swapRanges(index1, index2, count);
  }



  @Override
  public void swapRanges(int index1, List<E> b, int index2, int count)
  {
    checkSet();
    this.delegate.swapRanges(index1, b, index2, count);
  }



  @Override
  public final int binarySearch(final E key)
  {
    return this.delegate.binarySearch(key);
  }



  @Override
  public final int binarySearch(final E key, final Comparator<? super E> comp)
  {
    return this.delegate.binarySearch(key, comp);
  }



  @Override
  public final int binarySearch(int fromIndex, int toIndex, BiDi dir, E e, Comparator<? super E> c)
  {
    return this.delegate.binarySearch(fromIndex, toIndex, dir, e, c);
  }



  @Override
  public final boolean contains(int fromIndex, int toIndex, Object e)                     {
    return this.delegate.contains(fromIndex, toIndex, e);
  }



  @Override
  public final boolean containsAll(int fromIndex, int toIndex, Collection<?> source)
  {
    if (source == this)
      source = (Collection) this.delegate;
    return this.delegate.containsAll(fromIndex, toIndex, source(source));
  }



  @Override
  public final boolean containsId(int fromIndex, int toIndex, Object identity)
  {
    return this.delegate.containsId(fromIndex, toIndex, identity);
  }



  @Override
  public final int count(int fromIndex, int toIndex, Object e)
  {
    return this.delegate.count(fromIndex, toIndex, e);
  }



  @Override
  public final int countUp(int fromIndex, int toIndex, Object e, int maxCount)
  {
    return this.delegate.countUp(fromIndex, toIndex, e, maxCount);
  }



  @Override
  public final int countId(int fromIndex, int toIndex, Object identity)
  {
    return this.delegate.countId(fromIndex, toIndex, identity);
  }



  @Override
  public final int countIdUp(int fromIndex, int toIndex, Object identity, int maxCount)
  {
    return this.delegate.countIdUp(fromIndex, toIndex, identity, maxCount);
  }




  @Override
  public final boolean contentEquals(List<?> sequence)
  {
    if (sequence == this)
      sequence = (List) this.delegate;
    return this.delegate.contentEquals(sequence);
  }



  @Override
  public final boolean contentEquals(int fromIndex, int toIndex, List<?> sequence)
  {
    if (sequence == this)
      sequence = (List) this.delegate;
    return this.delegate.contentEquals(fromIndex, toIndex, sequence);
  }



  @Override
  public final boolean contentIdentical(List<?> sequence)
  {
    if (sequence == this)
      sequence = (List) this.delegate;
    return this.delegate.contentIdentical(sequence);
  }



  @Override
  public final boolean contentIdentical(int fromIndex, int toIndex, List<?> sequence)
  {
    if (sequence == this)
      sequence = (List) this.delegate;
    return this.delegate.contentIdentical(fromIndex, toIndex, sequence);
  }



  @Override
  public final E get(int index)
  {
    return this.delegate.get(index);
  }



  @Override
  public final E getEqual(int fromIndex, int toIndex, Object e)
  {
    return this.delegate.getEqual(fromIndex, toIndex, e);
  }



  @Override
  public final E getFirst()
  {
    return this.delegate.getFirst();
  }



  @Override
  public final E getLast()
  {
    return this.delegate.getLast();
  }



  @Override
  public E peekFirst()
  {
    return this.delegate.peekFirst();
  }



  @Override
  public E peekLast()
  {
    return this.delegate.peekLast();
  }



  @Override
  public final E pollFirst()
  {
    checkRemove();
    return this.delegate.pollFirst();
  }



  @Override
  public final E pollLast()
  {
    checkRemove();
    return this.delegate.pollLast();
  }



  @Override
  public final int indexOf(Object e)
  {
    return this.delegate.indexOf(e);
  }



  @Override
  public final int indexOf(int fromIndex, int toIndex, Object e)
  {
    return this.delegate.indexOf(fromIndex, toIndex, e);
  }



  @Override
  public final int indexOfId(Object identity)
  {
    return this.delegate.indexOfId(identity);
  }



  @Override
  public final int indexOfId(int fromIndex, int toIndex, Object identity)
  {
    return this.delegate.indexOfId(fromIndex, toIndex, identity);
  }



  @Override
  public final int indexOfSequence(List sequence)
  {
    if (sequence == this)
      sequence = (List) this.delegate;
    return this.delegate.indexOfSequence(sequence);
  }



  @Override
  public final int indexOfSequence(int fromIndex, int toIndex, List seq)
  {
    if (seq == this)
      seq = (List) this.delegate;
    return this.delegate.indexOfSequence(fromIndex, toIndex, seq);
  }



  @Override
  public final int indexOfIdSequence(List sequence)
  {
    if (sequence == this)
      sequence = (List) this.delegate;
    return this.delegate.indexOfIdSequence(sequence);
  }



  @Override
  public final int indexOfIdSequence(int fromIndex, int toIndex, List seq)
  {
    if (seq == this)
      seq = (List) this.delegate;
    return this.delegate.indexOfIdSequence(fromIndex, toIndex, seq);
  }



  @Override
  public final int lastIndexOf(Object e)
  {
    return this.delegate.lastIndexOf(e);
  }



  @Override
  public final int lastIndexOf(int fromIndex, int toIndex, Object e)
  {
    return this.delegate.lastIndexOf(fromIndex, toIndex, e);
  }



  @Override
  public final int lastIndexOfId(Object identity)
  {
    return this.delegate.lastIndexOfId(identity);
  }



  @Override
  public final int lastIndexOfId(int fromIndex, int toIndex, Object identity)
  {
    return this.delegate.lastIndexOfId(fromIndex, toIndex, identity);
  }



  @Override
  public final int lastIndexOfIdSequence(List<?> sequence)
  {
    if (sequence == this)
      sequence = (List) this.delegate;
    return this.delegate.lastIndexOfIdSequence(sequence);
  }



  @Override
  public final int lastIndexOfIdSequence(int fromIndex, int toIndex, List<?> seq)
  {
    if (seq == this)
      seq = (List) this.delegate;
    return this.delegate.lastIndexOfIdSequence(fromIndex, toIndex, seq);
  }



  @Override
  public final int lastIndexOfSequence(List<?> sequence)
  {
    if (sequence == this)
      sequence = (List) this.delegate;
    return this.delegate.lastIndexOfSequence(sequence);
  }



  @Override
  public final int lastIndexOfSequence(int fromIndex, int toIndex, List<?> seq)
  {
    if (seq == this)
      seq = (List) this.delegate;
    return this.delegate.lastIndexOfSequence(fromIndex, toIndex, seq);
  }



  @Override
  public final Object[] toArray(int fromIndex, int toIndex)
  {
    return this.delegate.toArray(fromIndex, toIndex);
  }




  @Override
  public final void toArray(int fromIndex, int toIndex, Object[] target)
  {
    this.delegate.toArray(fromIndex, toIndex, target);
  }



  @Override
  public final void toArray(int fromIndex, int toIndex, Object[] target, int targetIndex)
  {
    this.delegate.toArray(fromIndex, toIndex, target, targetIndex);
  }






}
