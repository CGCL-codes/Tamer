/*
 * HashKeySequence.java
 *
 * Created on July 6, 2007, 12:35 AM
 *
 * Copyright (c) Joerg Wassmer
 * This library is free software. You can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 2 or above
 * as published by the Free Software Foundation.
 * For more information please visit <http://jaxlib.sourceforge.net>.
 */

package jaxlib.col;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import jaxlib.array.ObjectArrays;
import jaxlib.jaxlib_private.CheckArg;
import jaxlib.lang.Objects;
import jaxlib.util.CheckBounds;



/**
 * An unmodifiable list which keeps its hashcode once computed.
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: HashKeySequence.java 3051 2012-02-13 01:37:48Z joerg_wassmer $
 */
public class HashKeySequence<E> extends ObjectArray<E>
{


  /**
   * @since JaXLib 1.0
   */
  private static final long serialVersionUID = 1L;


  public static final HashKeySequence EMPTY_LIST = new HashKeySequence.Empty();



  /**
   * Create a new {@code HashKeySequence} which contains the specified element appended to the specified
   * {@code HashKeySequence}.
   * The returned list is read-only.
   *
   * @since JaXLib 1.0
   */
  @Nonnull
  public static <E> HashKeySequence<E> add(final ObjectArray<E> src, @Nullable final E e)
  {
    return new HashKeySequence<>(ObjectArrays.add(src.array, e));
  }



  /**
   * Create a new {@code HashKeySequence} which contains the specified element appended to the specified
   * collection.
   * The returned list is read-only.
   * <p>
   * This method must not be used with collections that may get modified concurrently.
   * </p>
   *
   * @since JaXLib 1.0
   */
  @Nonnull
  public static <E> HashKeySequence<E> add(final Collection<E> src, @Nullable final E e)
  {
    if (src instanceof ObjectArray)
      return add((ObjectArray<E>) src, e);

    final int size = src.size();
    final E[] a    = src.toArray((E[]) new Object[size + 1]);
    a[size] = e;
    return new HashKeySequence<>(a);
  }



  /**
   * Create a new {@code HashKeySequence} which contains the specified element inserted into the specified
   * {@code ObjectArray}.
   * The returned list is read-only.
   *
   * @since JaXLib 1.0
   */
  @Nonnull
  public static <E> HashKeySequence<E> add(final ObjectArray<E> src, final int index, @Nullable final E e)
  {
    return new HashKeySequence<>(ObjectArrays.add(src.array, index, e));
  }



  /**
   * Create a new {@code HashKeySequence} which contains the specified element inserted into the specified collection.
   * The returned list is read-only.
   * <p>
   * This method must not be used with collections that may get modified concurrently.
   * </p>
   *
   * @since JaXLib 1.0
   */
  @Nonnull
  public static <E> HashKeySequence<E> add(final Collection<E> src, final int index, @Nullable final E e)
  {
    if (src instanceof ObjectArray)
      return add((ObjectArray<E>) src, index, e);

    final int size = src.size();
    CheckArg.rangeForAdding(size, index);

    final E[] a = src.toArray((E[]) new Object[size + 1]);
    System.arraycopy(a, index, a, index + 1, size - index);
    a[index] = e;
    return new HashKeySequence<>(a);
  }



  /**
   * Create a new {@code ObjectArray} which contains all elements except the element at the specified index.
   * The returned list is read-only.
   *
   * @since JaXLib 1.0
   */
  @Nonnull
  public static <E> HashKeySequence<E> cut(final ObjectArray<E> src, final int index)
  {
    if (src.array.length != 1)
      return new HashKeySequence<>(ObjectArrays.cut(src.array, index));

    CheckBounds.range(src.array.length, index);
    if (src.array.getClass().getComponentType() == Object.class)
      return HashKeySequence.EMPTY_LIST;
    else
      return new HashKeySequence<>(ObjectArrays.newInstance(src.array, 0));
  }



  /**
   * Create a new {@code HashKeySequence} which contains all elements except the element at the specified index.
   * The returned list is read-only.
   * <p>
   * This method must not be used with collections that may get modified concurrently.
   * </p>
   *
   * @since JaXLib 1.0
   */
  @Nonnull
  public static <E> HashKeySequence<E> cut(final Collection<E> src, final int index)
  {
    return (src instanceof ObjectArray)
      ? cut((ObjectArray<E>) src, index)
      : cut0(src, index);
  }



  private static <E> HashKeySequence<E> cut0(final Collection<E> src, final int index)
  {
    final E[] a = cutToArray(src, index);
    return (a == Objects.EMPTY_ARRAY) ? HashKeySequence.EMPTY_LIST : new HashKeySequence<>(a);
  }



  @Nonnull
  public static <E> HashKeySequence<E> emptyList()
  {
    return HashKeySequence.EMPTY_LIST;
  }



  @Nonnull
  public static <E> HashKeySequence<E> emptyList(@Nullable final Class<E> componentType)
  {
    if ((componentType == Object.class) || (componentType == null))
      return emptyList();
    else
      return new HashKeySequence<>(ObjectArrays.newInstance(componentType, 0));
  }



  /**
   * Creates a new read-only <tt>ObjectArray</tt> which delegates to the specified array.
   *
   * @throws NullPointerException
   *  if <code>a == null</code>.
   *
   * @since JaXLib 1.0
   */
  @Nonnull
  public static <E> HashKeySequence<E> readOnly(final E... a)
  {
    return (a.length == 0)
      ? HashKeySequence.EMPTY_LIST
      : new HashKeySequence<>(a);
  }



  @Nonnull
  public static <E> HashKeySequence<E> set(final ObjectArray<E> src, final int index, @Nullable final E e)
  {
    if (src instanceof HashKeySequence)
      return set((HashKeySequence<E>) src, index, e);

    final E[] a = src.array.clone();
    a[index] = e;
    return new HashKeySequence<>(a);
  }



  @Nonnull
  public static <E> HashKeySequence<E> set(final HashKeySequence<E> src, final int index, @Nullable final E e)
  {
    if (src.array[index] == e)
      return src;

    final E[] a = src.array.clone();
    a[index] = e;
    return new HashKeySequence<>(a);
  }



  /**
   * @since JaXLib 1.0
   */
  @Nonnull
  public static <E> HashKeySequence<E> toReadOnly(
    @Nullable final  Class<? super E>     componentType,
              final Iterable<? extends E> src
  )
  {
    if (src instanceof HashKeySequence)
      return toReadOnly0(componentType, (HashKeySequence<? extends E>) src);
    else
      return toReadOnly1(componentType, src);
  }



  /**
   * @since JaXLib 1.0
   */
  @Nonnull
  private static <E> HashKeySequence<E> toReadOnly0(
    @Nullable final Class<? super E>              componentType,
              final HashKeySequence<? extends E>  src
  )
  {
    final Object[] a = src.array;
    if (a.length == 0)
      return HashKeySequence.EMPTY_LIST;
    if (
         src.isReadOnly()
      && ((componentType == null) || componentType.isAssignableFrom(a.getClass().getComponentType()))
    )
      return (HashKeySequence<E>) src;
    return new HashKeySequence<>(ObjectArrays.copy((Class<E>) componentType, a));
  }



  /**
   * @since JaXLib 1.0
   */
  @Nonnull
  private static <E> HashKeySequence<E> toReadOnly1(
    @Nullable final  Class<? super E>     componentType,
              final Iterable<? extends E> src
  )
  {
    final E[] a = (E[]) Iterables.toArrayOrNull(src, componentType);
    if (a == null)
      return HashKeySequence.EMPTY_LIST;
    return new HashKeySequence<>(a);
  }




  /**
   * @since JaXLib 1.0
   */
  public HashKeySequence(final E... keySequence)
  {
    super(keySequence);
  }



  /**
   * @since JaXLib 1.0
   */
  public HashKeySequence(@Nullable final Class<E> componentType, final Iterable<? extends E> src)
  {
    super(componentType, src);
  }



  @Override
  public boolean equals(final Object o)
  {
    if (o == this)
      return true;
    if (!(o instanceof HashKeySequence))
      return super.equals(o);

    final HashKeySequence<?> b = (HashKeySequence) o;
    return (hashCode() == b.hashCode()) && Arrays.equals(this.array, b.array);
  }



  @Override
  public final int hashCode()
  {
    int h = super.modCount; // used for nothing else
    if (h == 0)
      this.modCount = h = super.hashCode();
    return h;
  }





  private static final class Empty extends HashKeySequence implements Externalizable
  {

    /**
     * @since JaXLib 1.0
     */
    private static final long serialVersionUID = 1L;


    /**
     * Public constructor because of Externalizable interface.
     */
    public Empty()
    {
      super(Objects.EMPTY_ARRAY);
    }



    /**
     * @serialData
     * @since JaXLib 1.0
     */
    private Object readResolve()
    {
      return HashKeySequence.EMPTY_LIST;
    }



    @Override
    public final void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException
    {
      // nop
    }



    @Override
    public final void writeExternal(final ObjectOutput out) throws IOException
    {
      // nop
    }
  }

}
