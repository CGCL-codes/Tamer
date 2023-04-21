/*
 * LinkedExpiringBlockingStack.java
 *
 * Created on March 12, 2006, 2:14 PM
 *
 * Copyright (c) Joerg Wassmer
 * This library is free software. You can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 2 or above
 * as published by the Free Software Foundation.
 * For more information please visit <http://jaxlib.sourceforge.net>.
 */

package jaxlib.col.concurrent;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.AbstractQueuedSynchronizer.ConditionObject;

import javax.annotation.Nullable;

import jaxlib.closure.Filter;
import jaxlib.lang.Objects;
import jaxlib.thread.DefaultThreadFactory;
import jaxlib.thread.ExecutionServices;
import jaxlib.thread.lock.UnfairReentrantLock;
import jaxlib.util.CheckArg;



/**
 * A blocking {@code Last-In-First-Out} queue where expired elements are removed automatically.
 * <p>
 * The {@link #poll()} operation returns the last recently added element. A scheduler removes
 * elements which are staying longer in the queue than the configured {@link #setTimeout(long,TimeUnit)
 * timeout}. The scheduler always removes the eldest element first.
 * </p><p>
 * Subclasses may apply cleanup operations on removed elements by overwriting the
 * {@link #freeRemoved(Object,boolean)} method.
 * </p><p>
 * Instances of {@link LinkedExpiringBlockingStack} are thread-safe.
 * This class guarantees atomic behaviour for all operations implemented by the
 * {@code LinkedExpiringBlockingStack} class itself. For inherited methods there is no guarentee about
 * atomic behaviour.
 * </p><p>
 * Iterators are reflecting the state of the queue at some point at or since the creation of the
 * iterator. They do not throw {@link java.util.ConcurrentModificationException}. However, each iterator
 * instance is designed to be used by only one thread at a time.
 * </p>
 *
 * @author <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @version $Id: LinkedExpiringBlockingStack.java 3025 2011-12-12 18:16:47Z joerg_wassmer $
 * @since JaXLib 1.0
 */
@SuppressWarnings("unchecked")
public class LinkedExpiringBlockingStack<E> extends AbstractBlockingXQueue<E>
implements BlockingXQueue<E>, Serializable
{


  /**
   * @since JaXLib 1.0
   */
  private static final long serialVersionUID = 1L;



  /**
   * @serial
   * @see LinkedExpiringBlockingStack.SerializedStack
   * @since JaXLib 1.0
   */
  final LinkedExpiringBlockingStack<E>.Stack stack;



  /**
   * Creates a new queue with the specified keep-alive time and capacity.
   * The queue will use a single-thread {@link ScheduledExecutionService} implementation which is
   * {@link Serializable serializable}.
   *
   * @param keepAliveTime
   *  the initial {@link #setKeepAliveTime(long,TimeUnit) keep-alive time}.
   * @param timeUnit
   *  the {@code keepAliveTime} unit.
   * @param capacity
   *  the initial {@link #setCapacity(int) capacity} of the new queue.
   *
   * @throws IllegalArgumentException
   *  if {@code (keepAliveTime < 0) || (capacity < 0)}.
   * @throws NullPointerException
   *  if {@code timeUnit == null}.
   *
   * @since JaXLib 1.0
   */
  public LinkedExpiringBlockingStack(final long keepAliveTime, final TimeUnit timeUnit, final int capacity)
  {
    this(keepAliveTime, timeUnit, null, capacity, 0);
  }



  /**
   * Creates a new queue with the specified keep-alive time, capacity and core capacity.
   * The queue will use a single-thread {@link ScheduledExecutionService} implementation which is
   * {@link Serializable serializable}.
   *
   * @param keepAliveTime
   *  the initial {@link #setKeepAliveTime(long,TimeUnit) keep-alive time}.
   * @param timeUnit
   *  the {@code keepAliveTime} unit.
   * @param capacity
   *  the initial {@link #setCapacity(int) capacity} of the new queue.
   * @param coreCapacity
   *  the initial {@link #setCoreCapacity(int) core capacity} of the new queue.
   *
   * @throws IllegalArgumentException
   *  if {@code (keepAliveTime < 0) || (capacity < 0) || (coreCapacity < 0)}.
   * @throws NullPointerException
   *  if {@code timeUnit == null}.
   *
   * @since JaXLib 1.0
   */
  public LinkedExpiringBlockingStack(
    final long      keepAliveTime,
    final TimeUnit  timeUnit,
    final int       capacity,
    final int       coreCapacity
  )
  {
    this(keepAliveTime, timeUnit, null, capacity, coreCapacity);
  }



  /**
   * Creates a new queue with the specified keep-alive time, scheduler and capacity.
   *
   * @param keepAliveTime
   *  the initial {@link #setKeepAliveTime(long,TimeUnit) keep-alive time}.
   * @param timeUnit
   *  the {@code keepAliveTime} unit.
   * @param timeoutExecutor
   *  the executor to use to remove expired elements; {@code null} to use the default implementation.
   * @param capacity
   *  the initial {@link #setCapacity(int) capacity} of the new queue.
   *
   * @throws IllegalArgumentException
   *  if {@code (keepAliveTime < 0) || (capacity < 0)}.
   * @throws NullPointerException
   *  if {@code timeUnit == null}.
   *
   * @since JaXLib 1.0
   */
  public LinkedExpiringBlockingStack(
              final long                      keepAliveTime,
              final TimeUnit                  timeUnit,
    @Nullable final ScheduledExecutorService  timeoutExecutor,
              final int                       capacity
  )
  {
    this(keepAliveTime, timeUnit, timeoutExecutor, capacity, 0);
  }



  /**
   * Creates a new queue with the specified keep-alive time, scheduler, capacity and core capacity.
   *
   * @param keepAliveTime
   *  the initial {@link #setKeepAliveTime(long,TimeUnit) keep-alive time}.
   * @param timeUnit
   *  the {@code keepAliveTime} unit.
   *
   * @throws IllegalArgumentException
   *  if {@code (keepAliveTime < 0) || (capacity < 0) || (coreCapacity < 0)}.
   * @throws NullPointerException
   *  if {@code timeUnit == null}.
   * @param timeoutExecutor
   *  the executor to use to remove expired elements; {@code null} to use the default implementation.
   * @param capacity
   *  the initial {@link #setCapacity(int) capacity} of the new queue.
   * @param coreCapacity
   *  the initial {@link #setCoreCapacity(int) core capacity} of the new queue.
   *
   * @since JaXLib 1.0
   */
  public LinkedExpiringBlockingStack(
                    long                      keepAliveTime,
              final TimeUnit                  timeUnit,
    @Nullable final ScheduledExecutorService  timeoutExecutor,
              final int                       capacity,
                    int                       coreCapacity
  )
  {
    super();

    CheckArg.notNegative(keepAliveTime, "keepAliveTime");
    keepAliveTime = timeUnit.toNanos(keepAliveTime);

    coreCapacity = Math.min(coreCapacity, capacity);
    this.stack = new Stack(capacity, coreCapacity, timeoutExecutor, keepAliveTime);
  }



  final ScheduledExecutorService createDefaultExecutor()
  {
    final ThreadFactory threadFactory = new DefaultThreadFactory(
      true,
      Thread.NORM_PRIORITY,
      Objects.toIdentityString(this) + "-"
    );

    final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
      1, threadFactory, new ThreadPoolExecutor.DiscardOldestPolicy()
    );

    executor.setKeepAliveTime(1, TimeUnit.MINUTES);
    executor.setMaximumPoolSize(1);
    executor.allowCoreThreadTimeOut(true);

    return executor;
  }


  private LinkedExpiringBlockingStack<E>.Stack createStack(
              final int                       capacity,
              final int                       coreCapacity,
    @Nullable final ScheduledExecutorService  executor,
              final long                      timeout
  )
  {
    return new Stack(capacity, coreCapacity, executor, timeout);
  }



  private void expireOlds(final Stack stack, int maxCount)
  {
    final long deadline = System.nanoTime();
    while (maxCount-- > 0)
    {
      Object o = stack.timeoutTask.pollOld(stack, deadline);
      if ((o == null) || (o instanceof Node))
      {
        return;
      }
      else
      {
        freeRemoved((E) o, true);
        o = null;
      }
    }
  }



  final boolean skipFreeRemoved()
  {
    try
    {
      return getClass().getDeclaredMethod("freeRemoved", Object.class, Boolean.TYPE).getDeclaringClass()
          == LinkedExpiringBlockingStack.class;
    }
    catch (final NoSuchMethodException ex)
    {
      throw new AssertionError(ex);
    }
    catch (final SecurityException ex)
    {
      // ignore
      return false;
    }
  }



  @Override
  protected void finalize() throws Throwable
  {
    super.finalize();

    this.stack.clear();

    final TimeoutTask<E> timeoutTask = this.stack.timeoutTask;

    timeoutTask.queueRef.clear();
    timeoutTask.cancel();
    if (timeoutTask.ownsExecutor && !timeoutTask.executor.isShutdown())
      AccessController.doPrivileged(ExecutionServices.privilegedShutdownNow(timeoutTask.executor));
  }




  /**
   * Apply clean up after removal of the specified element, but only if the element is not considered to be
   * in use anymore.
   * <p>
   * This method is being called by the background timeout thread, the {@link #clear()} method
   * and by all other operations which remove an element, except {@link #poll()}, {@link #take()} and
   * {@link #drain(Collection)} operations.
   * </p><p>
   * Generally this method should never throw any exception. If this method terminates abnormally, then
   * the timeout task will be paused until the next successful {@link add(Object) add} operation.
   * </p><p>
   * This method may be called concurrently by timeout and by user threads. Ideally it should return fast to
   * keep the timeout task running smoothly. The queue is not being locked by calling threads.
   * </p><p>
   * The default implementation of this method does nothing.
   * </p>
   *
   * @param e
   *  the removed non-{@code null} element.
   * @param timedOut
   *  if {@code true} then the element has been removed because of a timeout. Otherwise the element has
   *  been removed by a {@code remove} operation as explained above.
   *
   * @since JaXLib 1.0
   */
  protected void freeRemoved(final E e, final boolean timedOut)
  {
    // nop
  }



  protected ScheduledExecutorService getExecutor()
  {
    return this.stack.timeoutTask.executor;
  }



  @Override
  public boolean addIfAbsent(E e)
  {
    if (e == null)
      throw new NullPointerException("element");

    final Stack stack = this.stack;
    final TimeoutTask timeoutTask = stack.timeoutTask;
    final long timestamp = System.nanoTime();
    final int size;
    final boolean schedule;

    stack.lock.lock();
    try
    {
      for (LinkedBlockingXDeque.Node<E> p = stack.first; p != null; p = p.next)
      {
        final E item = p.item;
        if ((e == item) || e.equals(item))
          return false;
      }

      size = stack.link(e, timestamp);
      e = null;
      schedule = (size > 0)
              && (size > timeoutTask.coreCapacity)
              && timeoutTask.scheduled.compareAndSet(false, true);
    }
    finally
    {
      stack.lock.unlock();
    }

    if (schedule)
    {
      timeoutTask.schedule(timestamp);
      return true;
    }
    else if (size > 0)
    {
      return true;
    }
    else
    {
      throw new IllegalStateException("Deque full");
    }
  }


  @Override
  public final int capacity()
  {
    return this.stack.capacityView;
  }


  /**
   * Removes all elements from this queue and calls {@code freeRemoved(element, false)} for each.
   *
   * @see #freeRemoved(Object,boolean)
   *
   * @since JaXLib 1.0
   */
  @Override
  public void clear()
  {
    this.stack.clear();
  }



  @Override
  public boolean contains(final Object e)
  {
    return this.stack.contains(e);
  }



  @Override
  public int drainTo(final Collection<? super E> dest)
  {
    CheckArg.notIdentical(this, dest, "this", "dest");
    return this.stack.drainTo(dest);
  }



  @Override
  public int drainTo(final Collection<? super E> dest, final int maxElements)
  {
    CheckArg.notIdentical(this, dest, "this", "dest");
    return this.stack.drainTo(dest, maxElements);
  }



  /**
   * Get the core capacity of this queue.
   *
   * @see #setCoreCapacity(int)
   *
   * @since JaXLib 1.0
   */
  public final int getCoreCapacity()
  {
    return this.stack.timeoutTask.coreCapacity;
  }


  /**
   * Get the keep-alive time of this queue.
   *
   * @throws NullPointerException
   *  if {@code timeUnit == null}.
   *
   * @see #setKeepAliveTime(long,TimeUnit)
   *
   * @since JaXLib 1.0
   */
  public final long getKeepAliveTime(final TimeUnit timeUnit)
  {
    return timeUnit.convert(this.stack.timeoutTask.keepAliveTime, TimeUnit.NANOSECONDS);
  }


  @Override
  public E internalize(E e)
  {
    if (e == null)
      throw new NullPointerException("element");

    final Stack stack = this.stack;
    final TimeoutTask timeoutTask = stack.timeoutTask;
    final long timestamp = System.nanoTime();
    final int size;
    final boolean schedule;

    stack.lock.lock();
    try
    {
      for (LinkedBlockingXDeque.Node<E> p = stack.first; p != null; p = p.next)
      {
        final E item = p.item;
        if ((e == item) || e.equals(item))
          return item;
      }

      size = stack.link(e, timestamp);
      schedule = (size > 0)
              && (size > timeoutTask.coreCapacity)
              && timeoutTask.scheduled.compareAndSet(false, true);
    }
    finally
    {
      stack.lock.unlock();
    }

    if (schedule)
    {
      timeoutTask.schedule(timestamp);
      return e;
    }
    else if (size > 0)
    {
      return e;
    }
    else
    {
      throw new IllegalStateException("Deque full");
    }
  }


  @Override
  public final boolean isEmpty()
  {
    return this.stack.isEmpty();
  }


  @Override
  public Iterator<E> iterator()
  {
    return this.stack.iterator();
  }


  @Override
  public final boolean offer(E e)
  {
    if (e == null)
      throw new NullPointerException("element");

    final Stack stack               = this.stack;
    final UnfairReentrantLock lock  = stack.lock;
    final TimeoutTask timeoutTask   = stack.timeoutTask;
    final int coreCapacity          = timeoutTask.coreCapacity;
    final long timestamp            = System.nanoTime();
    final int size;
    final boolean schedule;

    lock.lock();
    try
    {
      size = stack.link(e, timestamp);
      e = null;
      schedule = (size > coreCapacity) && timeoutTask.scheduled.compareAndSet(false, true);
    }
    finally
    {
      lock.unlock();
    }

    if (schedule)
      timeoutTask.schedule(timestamp);

    return size > 0;
  }



  @Override
  public final boolean offer(E e, long timeout, final TimeUnit timeUnit)
  throws InterruptedException
  {
    if (e == null)
      throw new NullPointerException("element");

    timeout = timeUnit.toNanos(timeout);
    final Stack stack = this.stack;
    final TimeoutTask timeoutTask = stack.timeoutTask;
    final long timestamp = System.nanoTime();
    boolean schedule = false;

    stack.lock.lockInterruptibly();
    try
    {
      while (true)
      {
        final int size = stack.link(e, timestamp);
        if (size > 0)
        {
          e = null;
          schedule = (size > timeoutTask.coreCapacity) && timeoutTask.scheduled.compareAndSet(false, true);
          break;
        }
        else if (timeout <= 0)
        {
          break;
        }
        else
        {
          timeout = stack.notFull.awaitNanos(timeout);
          continue;
        }
      }
    }
    finally
    {
      stack.lock.unlock();
    }

    if (schedule)
      timeoutTask.schedule(timestamp);

    return e == null;
  }



  @Override
  public final E peek()
  {
    return this.stack.peekFirst();
  }



  @Override
  public final E poll()
  {
    return this.stack.pollFirst();
  }



  @Override
  public final E poll(final long timeout, final TimeUnit timeUnit) throws InterruptedException
  {
    return this.stack.pollFirst(timeout, timeUnit);
  }



  @Override
  public final E poll(final Filter<? super E> condition, final boolean iF)
  {
    return this.stack.pollFirst(condition, iF);
  }



  @Override
  public final E poll(
    final long              timeout,
    final TimeUnit          timeUnit,
    final Filter<? super E> condition,
    final boolean           iF
  ) throws InterruptedException
  {
    return this.stack.pollFirst(timeout, timeUnit, condition, iF);
  }



  @Override
  public final void put(E e) throws InterruptedException
  {
    if (e == null)
      throw new NullPointerException("element");

    final Stack stack = this.stack;
    final TimeoutTask timeoutTask = stack.timeoutTask;
    long timeout = timeoutTask.keepAliveTime;
    final long timestamp = System.nanoTime();

    boolean schedule = false;

    stack.lock.lockInterruptibly();
    try
    {
      while (true)
      {
        final int size = stack.link(e, timestamp);
        if (size > 0)
        {
          e = null;
          schedule = (size > timeoutTask.coreCapacity) && timeoutTask.scheduled.compareAndSet(false, true);
          break;
        }
        else if (timeout > 0)
        {
          timeout = stack.notFull.awaitNanos(timeout);
          continue;
        }
        else if (stack.size <= timeoutTask.coreCapacity)
        {
          stack.notFull.await();
          continue;
        }
        else
        {
          break;
        }
      }
    }
    finally
    {
      stack.lock.unlock();
    }

    if (schedule)
      timeoutTask.schedule(timestamp);
    else if (e != null)
      freeRemoved(e, true);
  }



  @Override
  public final int remainingCapacity()
  {
    return this.stack.remainingCapacity();
  }



  public final int remainingCoreCapacity()
  {
    final Stack stack = this.stack;
    final TimeoutTask timeoutTask = stack.timeoutTask;
    final int coreCapacity;
    final int size;

    stack.lock.lock();
    try
    {
      coreCapacity = timeoutTask.coreCapacity;
      size = stack.size;
    }
    finally
    {
      stack.lock.unlock();
    }

    return Math.max(0, coreCapacity - size);
  }



  @Override
  public final boolean remove(final Object e)
  {
    return removeEqual(e) != null;
  }



  @Override
  public E removeEqual(final Object e)
  {
    return this.stack.removeEqual(e);
  }



  @Override
  public final boolean removeId(final Object e)
  {
    return this.stack.removeId(e);
  }



  /**
   * Set the maximum size of this queue.
   * <p>
   * Note this class ignores calls to {@link #trimCapacity(int)}. Because of the different usecases the
   * {@code setCapacity} method is the only method which allows to modify the capacity.
   * </p><p>
   * Subclasses may overwrite this method to throw an {@link UnsupportedOperationException}.
   * </p>
   *
   * @throws IllegalArgumentException
   *  if {@code newCapacity < 0}.
   *
   * @see #setCoreCapacity(int)
   *
   * @since JaXLib 1.0
   */
  public void setCapacity(final int newCapacity)
  {
    this.stack.setCapacity(newCapacity);
  }



  /**
   * Set the core capacity of this queue.
   * The core capacity is the count of elements the {@link #setKeepAliveTime(long,TimeUnit) keepAliveTime}
   * is ignored for. Whenever the size of this queue is below the core capacity no elements will expire.
   *
   * @throws IllegalArgumentException
   *  if {@code coreCapacity < 0}.
   *
   * @see #setCapacity(int)
   *
   * @since JaXLib 1.0
   */
  public void setCoreCapacity(final int coreCapacity)
  {
    CheckArg.notNegative(coreCapacity, "coreCapacity");

    final Stack stack = this.stack;
    final TimeoutTask timeoutTask = stack.timeoutTask;

    int expire = 0;
    boolean schedule = false;

    stack.lock.lock();
    try
    {
      final int oldCoreCapacity = timeoutTask.coreCapacity;
      if (coreCapacity != oldCoreCapacity)
      {
        timeoutTask.coreCapacity = coreCapacity;
        if (coreCapacity > stack.capacity)
        {
          stack.setCapacity(coreCapacity);
        }
        else
        {
          expire = stack.size - coreCapacity;
          schedule = (expire > 0) && timeoutTask.scheduled.compareAndSet(false, true);
        }
      }
    }
    finally
    {
      stack.lock.unlock();
    }

    if (schedule)
      timeoutTask.schedule(0);

    if (expire > 0)
      expireOlds(stack, expire);
  }



  /**
   * Set the maximum duration elements should stay in this queue.
   * <p>
   * Note this call may temporary pause the timeout task. To avoid state elements you should not modify
   * the timeout too often.
   * </p>
   *
   * @throws IllegalArgumentException
   *  if {@code timeout < 0}.
   * @throws NullPointerException
   *  if {@code timeUnit == null}.
   *
   * @since JaXLib 1.0
   */
  public void setKeepAliveTime(long timeout, final TimeUnit timeUnit)
  {
    CheckArg.notNegative(timeout, "timeout");

    timeout = timeUnit.toNanos(timeout);
    final Stack stack = this.stack;
    final TimeoutTask timeoutTask = stack.timeoutTask;

    int expire = 0;
    boolean schedule = false;

    stack.lock.lock();
    try
    {
      final long oldTimeout = timeoutTask.keepAliveTime;
      if (timeout != oldTimeout)
      {
        timeoutTask.keepAliveTime = timeout;
        if (timeout < oldTimeout)
        {
          expire = stack.size - timeoutTask.coreCapacity;
          timeoutTask.cancel();
          schedule = (expire > 0) && timeoutTask.scheduled.compareAndSet(false, true);
        }
      }
    }
    finally
    {
      stack.lock.unlock();
    }

    if (schedule)
      timeoutTask.schedule(0);

    if (expire > 0)
      expireOlds(stack, expire);
  }



  @Override
  public final int size()
  {
    return this.stack.size();
  }



  @Override
  public final E take() throws InterruptedException
  {
    return this.stack.takeFirst();
  }



  @Override
  public final Object[] toArray()
  {
    return this.stack.toArray();
  }



  @Override
  public final <T> T[] toArray(final T[] a)
  {
    return this.stack.toArray(a);
  }



  @Override
  public final int toArray(final Object[] dest, final int destIndex)
  {
    return this.stack.toArray(dest, destIndex);
  }





  private static final class Node<E> extends LinkedBlockingXDeque.Node<E>
  {

    final long timestamp;

    Node(
      final E                             x,
      final LinkedBlockingXDeque.Node<E>  p,
      final LinkedBlockingXDeque.Node<E>  n,
      final long                          timestamp
    )
    {
      super(x, p, n);
      this.timestamp = timestamp;
    }
  }





  /**
   * Serialized representation to stay independent of LinkedBlockingXDeque.
   *
   * @since JaXLib 1.0
   */
  private static final class SerializedStack extends Object implements Serializable
  {

    /**
     * @since JaXLib 1.0
     */
    private static final long serialVersionUID = 1L;


    private transient LinkedExpiringBlockingStack.Stack stack;


    SerializedStack(final LinkedExpiringBlockingStack.Stack stack)
    {
      super();
      this.stack = stack;
    }


    /**
     * @serialData
     * @since JaXLib 1.0
     */
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException
    {
      in.defaultReadObject();

      LinkedExpiringBlockingStack<Object> queue   = (LinkedExpiringBlockingStack) in.readObject();
      long keepAliveTime                          = in.readLong();
      ScheduledExecutorService executor           = (ScheduledExecutorService) in.readObject();
      int capacity                                = in.readInt();
      int coreCapacity                            = in.readInt();

      final LinkedExpiringBlockingStack.Stack stack = queue.createStack(
        capacity, coreCapacity, executor, keepAliveTime
      );

      for (Object e; (e = in.readObject()) != null;)
        queue.offer(e);

      this.stack = stack;
    }


    /**
     * @serialData
     * @since JaXLib 1.0
     */
    private Object readResolve() throws ObjectStreamException
    {
      final LinkedExpiringBlockingStack.Stack stack = this.stack;
      if (stack != null)
      {
        this.stack = null;
        return stack;
      }
      else
      {
        throw new InvalidObjectException(null);
      }
    }


    /**
     * @serialData
     * @since JaXLib 1.0
     */
    private void writeObject(final ObjectOutputStream out) throws IOException
    {
      LinkedExpiringBlockingStack.Stack stack = this.stack;
      this.stack = null;

      out.defaultWriteObject();

      out.writeObject(stack.getExpiringStack());
      out.writeLong(stack.timeoutTask.keepAliveTime);

      if (stack.timeoutTask.ownsExecutor)
        out.writeObject(null);
      else
        out.writeObject(stack.timeoutTask.executor);

      int c = stack.capacity();
      out.writeInt(c);
      out.writeInt(stack.timeoutTask.coreCapacity);

      for (Iterator<?> it = stack.iterator(); (c-- > 0) && (it.hasNext());)
        out.writeObject(it.next());

      stack = null;

      out.writeObject(null);
    }
  }




  private final class Stack extends LinkedBlockingXDeque<E>
  {

    /**
     * @since JaXLib 1.0
     */
    private static final long serialVersionUID = 1L;


    final transient boolean skipFreeRemoved;
    final transient TimeoutTask<E> timeoutTask;


    Stack(
      final int                       capacity,
      final int                       coreCapacity,
      final ScheduledExecutorService  executor,
      final long                      keepAliveTime
    )
    {
      super(capacity);

      this.skipFreeRemoved = LinkedExpiringBlockingStack.this.skipFreeRemoved();
      this.timeoutTask = new TimeoutTask<>(coreCapacity, LinkedExpiringBlockingStack.this, executor, keepAliveTime);
    }



    @Override
    public final void clear()
    {
      if (this.skipFreeRemoved)
      {
        super.clear();
      }
      else
      {
        final ArrayList<E> list = new ArrayList<>();
        super.drainTo(list);
        for (int i = list.size(); --i >= 0;)
          LinkedExpiringBlockingStack.this.freeRemoved(list.set(i, null), false);
      }
    }



    @Override
    public final Iterator<E> iterator()
    {
      return this.skipFreeRemoved ? super.iterator() : new Stack.IteratorImpl(true);
    }



    @Override
    public final E removeEqual(Object e)
    {
      e = super.removeEqual(e);
      if ((e != null) && !this.skipFreeRemoved)
        LinkedExpiringBlockingStack.this.freeRemoved((E) e, false);
      return (E) e;
    }



    @Override
    public final boolean removeId(final Object e)
    {
      if (!super.removeId(e))
        return false;
      if ((e != null) && !this.skipFreeRemoved)
        LinkedExpiringBlockingStack.this.freeRemoved((E) e, false);
      return true;
    }



    @Override
    public final void setCapacity(final int newCapacity)
    {
      CheckArg.notNegative(newCapacity, "newCapacity");

      if (this.skipFreeRemoved)
      {
        super.setCapacity(newCapacity);
      }
      else
      {
        ArrayList<E> removed = null;

        this.lock.lock();
        try
        {
          final int oldCapacity = this.capacity;
          if (newCapacity != oldCapacity)
          {
            int size = this.size;
            if (size > newCapacity)
            {
              removed = new ArrayList<>(size - newCapacity);

              while (size > newCapacity)
              {
                removed.add(unlinkLast());
                size--;
              }
            }

            this.capacity = newCapacity;
            this.capacityView = newCapacity;
          }
        }
        finally
        {
          this.lock.unlock();
        }

        if (removed != null)
        {
          for (int i = 0, hi = removed.size(); i < hi; i++)
            LinkedExpiringBlockingStack.this.freeRemoved(removed.set(i, null), false);
        }
      }
    }



    final LinkedExpiringBlockingStack<E> getExpiringStack()
    {
      return LinkedExpiringBlockingStack.this;
    }



    final int link(final E e, final long timestamp)
    {
      final int size = this.size;
      if (size >= this.capacity)
        return 0;

      this.size = size + 1;
      final LinkedBlockingXDeque.Node<E>        f = this.first;
      final LinkedExpiringBlockingStack.Node<E> x = new LinkedExpiringBlockingStack.Node<>(e, null, f, timestamp);
      this.first = x;
      if (this.last == null)
        this.last = x;
      else
        f.prev = x;

      this.notEmpty.signal();
      return size + 1;
    }



    /**
     * @serialData
     * @since JaXLib 1.0
     */
    private Object writeReplace()
    {
      return new SerializedStack(this);
    }





    final class IteratorImpl extends LinkedBlockingXDeque<E>.IteratorImpl
    {

      IteratorImpl(final boolean forward)
      {
        super(forward);
      }


      @Override
      final void removeNode(final Node<E> e)
      {
        this.lock.lock();
        try
        {
          for (Node<E> p = Stack.this.first; p != null; p = p.next)
          {
            if (p == e)
            {
              Stack.this.unlink(p);
              LinkedExpiringBlockingStack.this.freeRemoved(p.item, false);
              return;
            }
          }
        }
        finally
        {
          this.lock.unlock();
        }
      }

    }

  }





  private static final class TimeoutTask<E> extends Object implements Runnable
  {

    volatile int coreCapacity;

    final ScheduledExecutorService executor;
    volatile Future<?> future;
    final boolean ownsExecutor;
    final WeakReference<LinkedExpiringBlockingStack<E>> queueRef;
    final AtomicBoolean scheduled;

    volatile long keepAliveTime;

    final AtomicBoolean running;


    TimeoutTask(
                final int                             coreCapacity,
                final LinkedExpiringBlockingStack<E>  queue,
      @Nullable       ScheduledExecutorService        executor,
                final long                            keepAliveTime
    )
    {
      super();

      CheckArg.notNegative(coreCapacity, "coreCapacity");
      CheckArg.notNegative(keepAliveTime, "keepAliveTime");

      if (executor == null)
      {
        executor = queue.createDefaultExecutor();
        this.ownsExecutor = true;
      }
      else
      {
        this.ownsExecutor = false;
      }

      this.coreCapacity       = coreCapacity;
      this.executor           = executor;
      this.keepAliveTime      = keepAliveTime;
      this.queueRef           = new WeakReference<>(queue);
      this.running            = new AtomicBoolean();
      this.scheduled          = new AtomicBoolean();
    }



    private Object pollOld(final LinkedExpiringBlockingStack<E>.Stack stack, final long deadline)
    {
      final UnfairReentrantLock lock  = stack.lock;
      final ConditionObject notFull   = stack.notFull;
      final AtomicBoolean scheduled   = this.scheduled;
      final int coreCapacity          = this.coreCapacity;
      final long keepAliveTime        = this.keepAliveTime;
      Object result = null;

      lock.lock();
      try
      {
        final int size = stack.size;
        if (size > coreCapacity)
        {
          final LinkedBlockingXDeque.Node<E> l = stack.last;
          if (l != null)
          {
            if ((((Node<E>) l).timestamp + keepAliveTime > deadline))
            {
              result = l;
            }
            else
            {
              final LinkedBlockingXDeque.Node<E> p = l.prev;
              stack.last = p;
              if (p == null)
                stack.first = null;
              else
                p.next = null;

              stack.size = size - 1;
              notFull.signal();
              return l.item;
            }
          }
        }

        scheduled.set(false);
      }
      finally
      {
        lock.unlock();
      }

      return result;
    }



    final void cancel()
    {
      final Future<?> future = this.future;
      if (future != null)
      {
        this.future = null;
        future.cancel(false);
      }
      this.scheduled.set(false);
    }



    final void schedule(final long timestamp)
    {
      boolean ok = false;
      try
      {
        Future<?> future = this.future;
        if (future != null)
        {
          this.future = null;
          future.cancel(false);
          future = null;
        }

        if (timestamp == 0)
        {
          this.future = this.executor.submit(this);
        }
        else
        {
          final long delay = this.keepAliveTime - (System.nanoTime() - timestamp);
          if (delay <= 0)
            this.future = this.executor.submit(this);
          else
            this.future = this.executor.schedule(this, delay, TimeUnit.NANOSECONDS);
        }

        ok = true;
      }
      finally
      {
        if (!ok)
          this.scheduled.set(false);
      }
    }



    @Override
    public final void run()
    {
      if (this.running.compareAndSet(false, true))
      {
        try
        {
          run0();
        }
        finally
        {
          this.running.set(false);
        }
      }
    }



    private void run0()
    {
      LinkedExpiringBlockingStack<E> queue = this.queueRef.get();
      if (queue == null)
        cancel();
      else
      {
        final LinkedExpiringBlockingStack<E>.Stack stack = queue.stack;
        if (stack.skipFreeRemoved)
          queue = null;

        final Future<?> future = this.future;
        if (future != null)
        {
          while (future == this.future)
          {
            Object o = pollOld(stack, System.nanoTime());
            if (o == null)
            {
              return;
            }
            else if (o instanceof Node)
            {
              this.running.set(false);
              if (future == this.future)
              {
                if (this.scheduled.compareAndSet(false, true))
                  schedule(((Node) o).timestamp);
              }
              return;
            }
            else if (queue == null)
            {
              o = null;
              continue;
            }
            else
            {
              boolean ok = false;
              try
              {
                queue.freeRemoved((E) o, true);
                ok = true;
              }
              finally
              {
                o = null;
                if (!ok)
                  this.scheduled.set(false);
              }
            }
          }
        }
      }
    }
  }
}
