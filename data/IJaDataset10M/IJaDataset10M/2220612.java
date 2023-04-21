/*
 * DefaultListModelList.java
 *
 * Created on 24 February 2003, 01:01
 *
 * Copyright (c) Joerg Wassmer
 * This library is free software. You can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 2 or above
 * as published by the Free Software Foundation.
 * For more information please visit <http://jaxlib.sourceforge.net>.
 */

package jaxlib.col.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.concurrent.Executor;

import jaxlib.closure.Closure;
import jaxlib.col.ArrayXList;
import jaxlib.col.SingletonList;
import jaxlib.col.XCollections;
import jaxlib.col.XList;
import jaxlib.jaxlib_private.CheckArg;
import jaxlib.text.Stringifiable;
import jaxlib.util.AccessTypeSet;
import jaxlib.util.BiDi;
import jaxlib.util.sorting.SortAlgorithm;


/**
 * Default implementation of a <tt>ListModelList</tt> which delegates to another list.
 * <p>
 * Subclasses implementing new methods which modify the list should call the appropriate one of following
 * methods after each modification, but if and only if the subclass is not delegating to superclass-methods
 * which already are firing events:<br>
 * <ul>
 * <li>{@link #intervalAdded(int,int)}</li>
 * <li>{@link #intervalRemoved(int,int)}</li>
 * <li>{@link #contentsChanged(int,int)}</li>
 * </ul>
 * </p><p>
 * <i>Implementation note:</i> The {@link #equals(Object)} method returns <tt>true</tt> if and only if the
 * object argument is identical to the receiving <tt>DefaultListModelList</tt> instance. The
 * {@link #hashCode()} method returns the identity hashcode of the receiving instance.
 * </p><p>
 * A <tt>DefaultListModelList</tt> is serializable if its underlying list is. On serialization all
 * not-serializable listeners are skipped silently.<br> The iterator is <i>fail-fast</i> if the iterator of
 * the underlying list is.
 * </p><p>
 * Like most collection classes, this class is not synchronized. If a <tt>DefaultListModelList</tt> gets
 * modified structurally by more than one thread, it has to be synchronized externally. What a structural
 * modification is is defined by the listmodel's underlying list. Please note that a
 * <tt>DefaultListModelList</tt> has to be synchronized also if its underlying list already is threadsafe.
 * </p><p>
 * The methods to manage event listeners, like {@link #addListDataListener} and
 * {@link #removeListDataListener}, are thread safe.
 * </p>
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: DefaultListModelList.java 3029 2011-12-29 00:36:48Z joerg_wassmer $
 */
@SuppressWarnings("unchecked")
public class DefaultListModelList<E> extends AbstractListModelList<E>
implements ListModelList<E>, RandomAccess, Serializable
{

  /**
   * @since JaXLib 1.0
   */
  private static final long serialVersionUID = 1L;


  /**
   * @deprecated Use constructor.
   *
   * Creates a new default listmodel.
   * The new listmodel is instance of {@link RandomAccess}.
   *
   * @since JaXLib 1.0
   */
  @Deprecated
  public static <E> DefaultListModelList<E> newInstance()
  {
    return new DefaultListModelList<>(new ArrayXList<E>(), null);
  }



  /**
   * @deprecated Use constructor.
   *
   * Creates a new listmodel which delegates to specified list.
   * The listmodel is {@link RandomAccess} if the specified list is.
   *
   * @param
   *  delegate the list to delegate to.
   *
   * @throws NullPointerException
   *  if {@code delegate == null}.
   *
   * @since JaXLib 1.0
   */
  @Deprecated
  public static <E> DefaultListModelList<E> newInstance(final XList<E> delegate)
  {
    return new DefaultListModelList<>(delegate, null);
  }



  /**
   * @deprecated Use constructor.
   *
   * Creates a new listmodel which delegates to specified list.
   * The listmodel is {@link RandomAccess} if the specified list is.
   *
   * @param
   *  delegate the list to delegate to.
   * @param eventDispatcher
   *  the executor to use to fire events (optional).
   *
   * @throws NullPointerException
   *  if {@code delegate == null}.
   *
   * @since JaXLib 1.0
   */
  @Deprecated
  public static <E> DefaultListModelList<E> newInstance(final XList<E> delegate, final Executor eventDispatcher)
  {
    return new DefaultListModelList<>(delegate, eventDispatcher);
  }



  /**
   * Create a new immutable list model containing only the specified element.
   * This mainly is useful to be used in UI designers, to set a default model for a component before the real model is
   * unavailable.
   *
   * @since JaXLib 1.0
   */
  public static <E> DefaultListModelList<E> newSingletonList(final E value)
  {
    return new DefaultListModelList<>(new SingletonList<>(value), null);
  }




  /**
   * The list this listmodel delegates to.
   * This field is never <tt>null</tt>.
   *
   * @serial
   * @since JaXLib 1.0
   */
  final XList<E> delegate;


  public DefaultListModelList()
  {
    this(new ArrayXList<E>());
  }



  /**
   * Creates a new <tt>DefaultListModelList</tt> delegating to specified list.
   * <p>
   * This constructor is for subclasses only, use one of the static factory methods in this class to create
   * a new instance.
   * Subclasses should implement the {@link RandomAccess} interface if the specified list does.
   * </p>
   *
   * @param delegate
   *  the list to delegate to.
   *
   * @throws NullPointerException
   *  if {@code delegate == null}.
   *
   * @see #newInstance(XList)
   *
   * @since JaXLib 1.0
   */
  public DefaultListModelList(final XList<E> delegate)
  {
    super();
    CheckArg.notNull(delegate, "delegate");
    this.delegate = delegate;
  }



  /**
   * Creates a new <tt>DefaultListModelList</tt> delegating to specified list.
   * <p>
   * This constructor is for subclasses only, use one of the static factory methods in this class to create
   * a new instance.
   * Subclasses should implements the {@link RandomAccess} interface if the specified list does.
   * </p>
   *
   * @param delegate
   *  the list to delegate to.
   * @param eventDispatcher
   *  the executor to use to fire events (optional).
   *
   * @throws NullPointerException
   *  if {@code delegate == null}.
   *
   * @see #newInstance(XList,Executor)
   *
   * @since JaXLib 1.0
   */
  public DefaultListModelList(final XList<E> delegate, final Executor eventDispatcher)
  {
    super(eventDispatcher);
    CheckArg.notNull(delegate, "delegate");
    this.delegate = delegate;
  }



  final <T> Collection<T> source(final Collection<T> source)
  {
    return (source == this) ? (Collection<T>) this.delegate : source;
  }



  final <T> List<T> source(final List<T> source)
  {
    return (source == this) ? (List<T>) this.delegate : source;
  }



  @Override
  public AccessTypeSet accessTypes()
  {
    return this.delegate.accessTypes();
  }



  @Override
  public void add(final int index, final E e)
  {
    this.delegate.add(index, e);
    intervalAdded(index, index);
  }



  @Override
  public boolean addIfAbsent(final E e)
  {
    if (!this.delegate.addIfAbsent(e))
      return false;

    final int i = this.delegate.size() - 1;
    intervalAdded(i, i);
    return true;
  }



  @Override
  public void addCount(final int index, final E e, final int count)
  {
    if (count != 0)
    {
      this.delegate.addCount(index, e, count);
      intervalAdded(index, (index + count) - 1);
    }
  }



  @Override
  public boolean addAll(final int index, final Collection<? extends E> source)
  {
    final int size = this.delegate.size();
    if (!this.delegate.addAll(index, source(source)))
      return false;
    intervalAdded(index, (index + ((this.delegate.size() - size) - 1)));
    return true;
  }



  @Override
  public boolean addAll(final int index, final List<? extends E> source, final int fromIndex, final int toIndex)
  {
    final int size = this.delegate.size();
    if (!this.delegate.addAll(index, source(source), fromIndex, toIndex))
      return false;
    intervalAdded(index, (index + ((this.delegate.size() - size) - 1)));
    return true;
  }



  @Override
  public boolean addAll(final int index, final E[] source, final int fromIndex, final int toIndex)
  {
    int size = this.delegate.size();
    if (!this.delegate.addAll(index, source, fromIndex, toIndex))
      return false;
    intervalAdded(index, (index + ((this.delegate.size() - size) - 1)));
    return true;
  }



  @Override
  public int addNext(final int index, final Iterator<? extends E> source, final int remaining)
  {
    final int added = this.delegate.addNext(index, source, remaining);
    if (added > 0)
      intervalAdded(index, (index + added) - 1);
    return added;
  }



  @Override
  public final void at(final int index, final E e)
  {
    this.delegate.at(index, e);
    contentsChanged(index, index);
  }



  @Override
  public int binarySearch(
    final int                   fromIndex,
    final int                   toIndex,
    final BiDi                  dir,
    final E                     key,
    final Comparator<? super E> c
  )
  {
    return this.delegate.binarySearch(fromIndex, toIndex, dir, key, c);
  }



  @Override
  public int capacity()
  {
    return this.delegate.capacity();
  }



  @Override
  public void clear(final int fromIndex, final int toIndex)
  {
    this.delegate.clear(fromIndex, toIndex);
    if (fromIndex != toIndex)
      intervalRemoved(fromIndex, toIndex - 1);
  }



  @Override
  public boolean contains(final int fromIndex, final int toIndex, final Object e)
  {
    return this.delegate.contains(fromIndex, toIndex, e);
  }



  @Override
  public boolean containsId(final int fromIndex, final int toIndex, final Object identity)
  {
    return this.delegate.contains(fromIndex, toIndex, identity);
  }



  @Override
  public boolean containsAll(final int fromIndex, final int toIndex, final Collection<?> source)
  {
    return this.delegate.containsAll(fromIndex, toIndex, source(source));
  }



  @Override
  public int copy(final int index, Collection<? extends E> source)
  {
    if (XCollections.isSizeStable(source))
    {
      source = source(source);
      if ((index != 0) || ((Object) source != this.delegate))
      {
        final int added = this.delegate.copy(index, source);
        contentsChanged(index, (index + source.size()) - 1);
        return added;
      }
      else
        return 0;
    }
    else
    {
      return super.copy(index, source);
    }
  }



  @Override
  public int copy(final int index, List<? extends E> source, final int fromIndex, final int toIndex)
  {
    source = source(source);
    if ((fromIndex != toIndex) && ((index != 0) || ((Object) source != this.delegate)))
    {
      final int added = this.delegate.copy(index, source, fromIndex, toIndex);
      contentsChanged(index, (index + (toIndex - fromIndex)) - 1);
      return added;
    }
    else
    {
      if ((Object) source == this.delegate)
        source = this;
      return super.copy(index, source, fromIndex, toIndex); // check for valid args
    }
  }



  @Override
  public int copy(final int index, final E[] source, final int fromIndex, final int toIndex)
  {
    final int added = this.delegate.copy(index, source, fromIndex, toIndex);
    if (fromIndex != toIndex)
      contentsChanged(index, (index + (toIndex - fromIndex)) - 1);
    return added;
  }



  @Override
  public int copyNext(final int index, final Iterator<? extends E> source, final int remaining)
  {
    if (remaining > 0)
    {
      final int added = this.delegate.copyNext(index, source, remaining);
      if (remaining > 0)
        contentsChanged(index, (index + remaining) - 1);
      return added;
    }
    else if (source instanceof ListIterator)
    {
      final ListIterator<? extends E> lit = (ListIterator<? extends E>) source;
      final int a = lit.nextIndex();
      final int added = this.delegate.copyNext(index, source, remaining);
      final int b = lit.nextIndex();
      if (a != b)
        contentsChanged(index, index + ((a + b) - 1));
      return added;
    }
    else
      return super.copyNext(index, source, remaining);
  }



  @Override
  public int countUp(final int fromIndex, final int toIndex, final Object e, final int maxCount)
  {
    return this.delegate.countUp(fromIndex, toIndex, e, maxCount);
  }



  @Override
  public int countIdUp(final int fromIndex, final int toIndex, final Object identity, final int maxCount)
  {
    return this.delegate.countUp(fromIndex, toIndex, identity, maxCount);
  }



  @Override
  public int forEach(final int fromIndex, final int toIndex, final BiDi dir, final Closure<? super E> procedure)
  {
    return this.delegate.forEach(fromIndex, toIndex, dir, procedure);
  }



  @Override
  public int freeCapacity()
  {
    return this.delegate.freeCapacity();
  }



  @Override
  public E get(final int index)
  {
    return this.delegate.get(index);
  }



  @Override
  public Class<? super E> getComponentType()
  {
    return this.delegate.getComponentType();
  }



  @Override
  public E getEqual(final int fromIndex, final int toIndex, final Object e)
  {
    return this.delegate.getEqual(fromIndex, toIndex, e);
  }



  @Override
  public int indexOf(final int fromIndex, final int toIndex, final Object e)
  {
    return this.delegate.indexOf(fromIndex, toIndex, e);
  }



  @Override
  public int indexOfId(final int fromIndex, final int toIndex, final Object identity)
  {
    return this.delegate.indexOfId(fromIndex, toIndex, identity);
  }



  @Override
  public int indexOfSequence(final int fromIndex, final int toIndex, final List<?> seq)
  {
    return this.delegate.indexOfSequence(fromIndex, toIndex, source(seq));
  }



  @Override
  public int indexOfIdSequence(final int fromIndex, final int toIndex, final List<?> seq)
  {
    return this.delegate.indexOfIdSequence(fromIndex, toIndex, source(seq));
  }



  @Override
  public boolean isFastSearching()
  {
    return this.delegate.isFastSearching();
  }



  @Override
  public Iterator<E> iterator(final int nextIndex)
  {
    return new IteratorImpl(nextIndex);
  }



  @Override
  public ListIterator<E> listIterator(final int nextIndex)
  {
    return new ListIteratorImpl(nextIndex);
  }



  @Override
  public int lastIndexOf(final int fromIndex, final int toIndex, final Object e)
  {
    return this.delegate.lastIndexOf(fromIndex, toIndex, e);
  }



  @Override
  public int lastIndexOfId(final int fromIndex, final int toIndex, final Object identity)
  {
    return this.delegate.lastIndexOfId(fromIndex, toIndex, identity);
  }



  @Override
  public int lastIndexOfSequence(final int fromIndex, final int toIndex, final List<?> seq)
  {
    return this.delegate.lastIndexOfSequence(fromIndex, toIndex, source(seq));
  }



  @Override
  public int lastIndexOfIdSequence(final int fromIndex, final int toIndex, final List<?> seq)
  {
    return this.delegate.lastIndexOfIdSequence(fromIndex, toIndex, source(seq));
  }



  @Override
  public E remove(final int index)
  {
    final E e = this.delegate.remove(index);
    intervalRemoved(index, index);
    return e;
  }



  @Override
  public int removeFirst(final int fromIndex, final int toIndex, final Object e)
  {
    final int i = this.delegate.removeFirst(fromIndex, toIndex, e);
    if (i >= 0)
      intervalRemoved(i, i);
    return i;
  }



  @Override
  public int removeLast(final int fromIndex, final int toIndex, final Object e)
  {
    final int i = this.delegate.removeLast(fromIndex, toIndex, e);
    if (i >= 0)
      intervalRemoved(i, i);
    return i;
  }



  @Override
  public int removeFirstId(final int fromIndex, final int toIndex, final Object identity)
  {
    final int i = this.delegate.removeFirstId(fromIndex, toIndex, identity);
    if (i >= 0)
      intervalRemoved(i, i);
    return i;
  }



  @Override
  public int removeLastId(final int fromIndex, final int toIndex, final Object identity)
  {
    final int i = this.delegate.removeLastId(fromIndex, toIndex, identity);
    if (i >= 0)
      intervalRemoved(i, i);
    return i;
  }



  @Override
  public void reverse(final int fromIndex, final int toIndex)
  {
    this.delegate.reverse(fromIndex, toIndex);
    if (fromIndex < toIndex - 1)
      contentsChanged(fromIndex, toIndex - 1);
  }



  @Override
  public void rotate(final int fromIndex, final int toIndex, final int distance)
  {
    this.delegate.rotate(fromIndex, toIndex, distance);
    if ((distance != 0) && (fromIndex < toIndex - 1))
      contentsChanged(fromIndex, toIndex - 1);
  }



  @Override
  public E set(final int index, E e)
  {
    e = this.delegate.set(index, e);
    contentsChanged(index, index);
    return e;
  }



  @Override
  public void set(final int fromIndex, final int toIndex, final E e)
  {
    this.delegate.set(fromIndex, toIndex, e);
    if (fromIndex != toIndex)
      contentsChanged(fromIndex, toIndex - 1);
  }



  @Override
  public int size()
  {
    return this.delegate.size();
  }



  @Override
  public void sort(final int fromIndex, final int toIndex, final Comparator<? super E> c, SortAlgorithm algo)
  {
    this.delegate.sort(fromIndex, toIndex, c, algo);
    if (fromIndex < toIndex - 1)
      contentsChanged(fromIndex, toIndex - 1);
  }



  @Override
  public void swap(final int index0, final int index1)
  {
    this.delegate.swap(index0, index1);
    if (index0 != index1)
      contentsChanged(index0, index1);
  }



  @Override
  public void swapRanges(int index0, int index1, final int count)
  {
    this.delegate.swapRanges(index0, index1, count);
    if ((count != 0) && (index0 != index1))
    {
      if (index0 > index1)
      {
        int t = index0;
        index0 = index1;
        index1 = t;
      }
      contentsChanged(index0, (index1 + count) - 1);
    }
  }



  @Override
  public void swapContent(final Collection<E> source)
  {
    if ((source == this) || (source == this.delegate))
      return;
    final int oldSize = this.delegate.size();
    this.delegate.swapContent(source);
    final int newSize = this.delegate.size();
    if (oldSize == newSize)
    {
      if (newSize != 0)
        contentsChanged(0, newSize - 1);
    }
    else if (oldSize < newSize)
    {
      //contentsChanged(0, newSize - 1);
      if (oldSize != 0)
        contentsChanged(0, oldSize - 1);
      intervalAdded(oldSize, newSize - 1);
    }
    else // oldSize > newSize
    {
      if (newSize != 0)
        contentsChanged(0, newSize - 1);
      intervalRemoved(newSize, oldSize - 1);
    }
  }



  @Override
  public void swapRanges(final int index1, List<E> b, final int index2, final int count)
  {
    b = source(b);
    if ((count == 0) || (index1 == index2) && (b == this.delegate))
    {
      CheckArg.swapRanges(count, this.delegate.size(), index1, b.size(), index2);
    }
    else
    {
      this.delegate.swapRanges(index1, b, index2, count);
      contentsChanged(index1, (index1 + count) - 1);
    }
  }



  @Override
  public void toArray(final int fromIndex, final int toIndex, final Object[] dest, final int destIndex)
  {
    this.delegate.toArray(fromIndex, toIndex, dest, destIndex);
  }



  @Override
  public String toString()
  {
    return this.delegate.toString();
  }



  @Override
  public void toString(final StringBuilder sb)
  {
    if (this.delegate instanceof Stringifiable)
      ((Stringifiable) this.delegate).toString(sb);
    else
      sb.append(this.delegate);
  }



  @Override
  public int trimCapacity(final int newCapacity)
  {
    return this.delegate.trimCapacity(newCapacity);
  }






  private final class IteratorImpl extends Object implements Iterator<E>
  {

    private final ListIterator<E> delegate;


    IteratorImpl(int index)
    {
      super();
      this.delegate = DefaultListModelList.this.delegate.listIterator(index);
    }



    @Override
    public final boolean hasNext()
    {
      return this.delegate.hasNext();
    }



    @Override
    public final E next()
    {
      return this.delegate.next();
    }



    @Override
    public final void remove()
    {
      final int index = this.delegate.previousIndex();
      this.delegate.remove();
      DefaultListModelList.this.intervalRemoved(index, index);
    }
  }





  private final class ListIteratorImpl extends Object implements ListIterator<E>
  {

    private final ListIterator<E> delegate;
    private boolean forward;


    ListIteratorImpl(final int index)
    {
      super();
      this.delegate = DefaultListModelList.this.delegate.listIterator(index);
      this.forward = true;
    }



    @Override
    public final void add(final E e)
    {
      final int index = this.forward ? this.delegate.previousIndex() : this.delegate.nextIndex();
      this.delegate.add(e);
      DefaultListModelList.this.intervalAdded(index, index);
    }



    @Override
    public final boolean hasNext()
    {
      return this.delegate.hasNext();
    }



    @Override
    public final boolean hasPrevious()
    {
      return this.delegate.hasPrevious();
    }



    @Override
    public final E next()
    {
      this.forward = true;
      return this.delegate.next();
    }



    @Override
    public final int nextIndex()
    {
      return this.delegate.nextIndex();
    }



    @Override
    public final E previous()
    {
      this.forward = false;
      return this.delegate.previous();
    }



    @Override
    public final int previousIndex()
    {
      return this.delegate.previousIndex();
    }



    @Override
    public final void remove()
    {
      final int index = this.forward ? this.delegate.previousIndex() : this.delegate.nextIndex();
      this.delegate.remove();
      DefaultListModelList.this.intervalRemoved(index, index);
    }



    @Override
    public final void set(final E e)
    {
      this.delegate.set(e);
      final int index = this.forward ? this.delegate.previousIndex() : this.delegate.nextIndex();
      contentsChanged(index, index);
    }
  }


}
