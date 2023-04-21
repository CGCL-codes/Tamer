/*
 * Swapped.java
 *
 * Created on February 21, 2007, 1:48 AM
 *
 * Copyright (c) Joerg Wassmer
 * This library is free software. You can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 2 or above
 * as published by the Free Software Foundation.
 * For more information please visit <http://jaxlib.sourceforge.net>.
 */
package jaxlib.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import jaxlib.io.file.Files;
import jaxlib.io.stream.objects.ClassLoaderObjectInputStream;
import jaxlib.logging.Log;
import jaxlib.ref.Ref;
import jaxlib.thread.Threads;
import jaxlib.thread.XThread;


/**
 * @param <T>
 *  the type of referenced object.
 *
 * @author jw
 * @version $Id: Swapped.java 3016 2011-11-28 06:17:26Z joerg_wassmer $
 * @since JaXLib 1.0
 */
@SuppressWarnings("unchecked")
public class Swapped<T> extends Object implements Ref<T>
{

  private ClassLoader classLoader;
  private final boolean deflate;
  private final boolean delete;
  private File file;
  private SoftReference<T> ref;


  public Swapped(final T referent, final ClassLoader classLoader) throws IOException
  {
    this(referent, classLoader, null, true, true);
  }


  public Swapped(
    final T           referent,
          ClassLoader classLoader,
          File        file,
    final boolean     deflate,
    final boolean     delete
  )
  throws IOException
  {
    super();

    if (!(referent instanceof Serializable))
      throw new NotSerializableException(referent.getClass().toString());

    this.classLoader = classLoader;

    this.deflate = deflate;
    this.delete = delete || (file == null);
    if (file == null)
    {
      file = Files.createTempFile();
      file.deleteOnExit();
    }
    this.file = file;

    OutputStream bout = null;
    boolean ok = false;
    try
    {
      bout = new FileOutputStream(file);
      if (deflate)
        bout = new DeflaterOutputStream(bout);
      ObjectOutputStream out = new ObjectOutputStream(bout);
      out.writeObject(referent);
      out.close();
      out = null;
      bout = null;
      ok = true;
    }
    finally
    {
      if (!ok)
      {
        IO.tryClose(bout);
        bout = null;
        if (this.delete)
        {
          try
          {
            file.delete();
          }
          catch (final Exception ex)
          {
            // ignore, throw previous exception
          }
        }
      }
    }

    this.ref = new SoftReference<>(referent);
  }



  private T read()
  {
    final File file = this.file;
    ClassLoader classLoader = this.classLoader;
    if ((file == null) || (classLoader == null))
      return null;

    InputStream bin = null;
    boolean ok = false;
    try
    {
      bin = new FileInputStream(file);
      if (this.deflate)
        bin = new InflaterInputStream(bin);
      ObjectInputStream in = (this.classLoader == null)
        ? new ObjectInputStream(bin)
        : new ClassLoaderObjectInputStream(bin, this.classLoader);
      final T object = (T) in.readObject();
      in.close();
      ok = true;
      return object;
    }
    catch (final ClassNotFoundException ex)
    {
      throw new IllegalStateException(ex);
    }
    catch (final IOException ex)
    {
      return null;
    }
    finally
    {
      if (!ok)
      {
        IO.tryClose(bin);
        bin = null;
        if (this.delete && (this.file != null))
        {
          classLoader = null;
          this.classLoader = null;
          this.file = null;
          try
          {
            file.delete();
          }
          catch (final Exception ex)
          {
            // ignore, throw previous exception
          }
        }
      }
    }
  }



  @Override
  protected void finalize() throws Throwable
  {
    super.finalize();
    final File file = this.file;
    if ((file != null) && this.delete)
    {
      if (file.getClass() == File.class)
      {
        clear0(false);
        Swapped.DeleteThread.enqueue(file);
      }
      else
      {
        clear0(true);
      }
    }
  }



  @Override
  public synchronized void clear()
  {
    clear0(this.delete);
  }


  private void clear0(final boolean delete)
  {
    final File file = this.file;
    if (file != null)
    {
      this.file = null;
      this.classLoader = null;
      SoftReference<T> ref = this.ref;
      if (ref != null)
      {
        this.ref = null;
        ref.clear();
        ref = null;
      }
      if (delete)
        file.delete();
    }
  }



  @Override
  public synchronized T get()
  {
    SoftReference<T> ref = this.ref;
    T referent = (ref == null) ? null : ref.get();
    if (referent == null)
    {
      ref = null;
      this.ref = null;
      referent = read();
      if (referent != null)
        this.ref = new SoftReference<>(referent);
    }
    return referent;
  }



  @Override
  public synchronized T getAndClear()
  {
    final T v = get();
    clear();
    return v;
  }



  public T getClone()
  {
    return read();
  }








  private static final class DeleteThread extends XThread
  {

    private static final long DELAY = TimeUnit.MINUTES.toNanos(1);

    private static final LinkedBlockingQueue<File> queue = new LinkedBlockingQueue<>(65535);
    private static volatile boolean running;


    static void enqueue(final File file)
    {
      try
      {
        if (DeleteThread.queue.offer(file))
        {
          if (!DeleteThread.running)
          {
            synchronized (DeleteThread.queue)
            {
              if (!DeleteThread.running)
              {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {
                  public Object run()
                  {
                    new DeleteThread().start();
                    return null;
                  }
                });
                DeleteThread.running = true;
              }
            }
          }
          return;
        }
      }
      catch (final Exception ex)
      {
        Log.global.severe(ex);
      }
      file.delete();
    }



    DeleteThread()
    {
      super(Threads.getAccessibleTopThreadGroup(), DeleteThread.class.getName());
      setDaemon(true);
      setPriority(Thread.NORM_PRIORITY);
    }



    @Override
    public final void interrupt()
    {
      // ignore
    }


    @Override
    public void run()
    {
      initRun();
      AccessController.doPrivileged(new PrivilegedAction<Object>() {
        public Object run()
        {
          DeleteThread.this.run0();
          return null;
        }
      });
    }


    private void run0()
    {
      while (true)
      {
        File file;
        try
        {
          file = DeleteThread.queue.poll(DeleteThread.DELAY, TimeUnit.NANOSECONDS);
        }
        catch (InterruptedException ex)
        {
          // should not happen
          ex = null;
          Thread.interrupted();
          continue;
        }

        if (file != null)
        {
          try
          {
            file.delete();
          }
          catch (final ThreadDeath ex)
          {
            throw ex;
          }
          catch (final Throwable ex)
          {
            Log.global.warning(ex);
          }
          file = null;
        }
        else if (DeleteThread.queue.isEmpty())
        {
          synchronized (DeleteThread.queue)
          {
            if (DeleteThread.queue.isEmpty())
            {
              DeleteThread.running = false;
              return;
            }
          }
        }
      }
    }
  }
}
