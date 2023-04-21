/*
 * IdentityHashSetTest.java
 *
 * Created on Dec 27, 2011, 7:14:31 PM
 *
 * Copyright (c) Joerg Wassmer
 * This library is free software. You can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 2 or above
 * as published by the Free Software Foundation.
 * For more information please visit <http://jaxlib.sourceforge.net>.
 */
package jaxlib.col;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assume;
import org.junit.Test;



/**
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: IdentityHashSetTest.java 3029 2011-12-29 00:36:48Z joerg_wassmer $
 */
public final class IdentityHashSetTest extends SetTestCase
{


  public IdentityHashSetTest()
  {
    super();
  }



  @Test
  public void testSerialization() throws Exception
  {
    Assume.assumeTrue(isSerializable());

    final Collection tested = createTestedCollection(createTestElements());

    ByteArrayOutputStream bout = new ByteArrayOutputStream(8192);
    ObjectOutputStream    out  = new ObjectOutputStream(bout);
    out.writeObject(tested);
    out.close();
    out = null;

    ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
    bout = null;

    ObjectInputStream in = new ObjectInputStream(bin);
    final Collection deserialized = (Collection) in.readObject();
    in.close();
    in = null;
    bin = null;

    assertEquals(new HashSet(tested), new HashSet(deserialized));
  }



  @Override
  protected <E> Set<E> createExpectedCollection(final Collection<E> elements)
  {
    return new HashSet<>(elements);
  }



  @Override
  protected <E> Set<E> createTestedCollection(final Collection<E> elements)
  {
    final IdentityHashSet<E> set = new IdentityHashSet<>(new HashSet<>(elements));
    //log.info(set.hashtableStatistics());
    return set;
  }



  @Override
  protected boolean implementsStandardHashCode()
  {
    return false;
  }

}


