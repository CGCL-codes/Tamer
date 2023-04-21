/*
 *
 * Copyright (C) 2001, 2002 David Leuschner, Stefan Heimann
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 *
 */

package org.cantaloop.tools.validation;

import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.Comparator;

/**
 * AbstractValidator.java
 *
 *
 * @created Thu Nov  1 20:46:19 2001
 *
 * @author <a href="mailto:stefan@cantaloop.org">Stefan Heimann</a>
 * @version @version@ ($Revision: 1.4 $)
 */

public abstract class AbstractValidator implements Validator {

  protected Set m_enumeration;
  protected Comparator m_comparator;
  protected boolean m_isEnumSet = false;
  
  protected AbstractValidator() {
    this(NaturalOrderComparator.getInstance());
    m_enumeration = new HashSet();
  }


  protected AbstractValidator(Comparator comparator) {
    this.m_comparator = comparator;
    m_enumeration = new TreeSet(comparator);
  }

  /**
   * Set the enumeration facet. <code>enum</code> does <b>not<b>
   * become the new enumeration facet, instead, the values of
   * <code>enum</code> are copied.
   * Setting <code>enum</code> to <code>null</code> means clearing
   * this facet.
   *
   * @param enum a <code>Set</code> value
   */
  public void setEnumeration(Set enum) {
    m_enumeration.clear();
    m_isEnumSet = false;
    if(enum != null) {
      m_enumeration.addAll(enum);
      m_isEnumSet = true;
    }
  }

  /**
   * Specify if the enumeration facet should be active or not.
   *
   * @param b a <code>boolean</code> value
   */
  public void toggleEnumeration(boolean b) {
    m_isEnumSet = b;
  }

  /**
   * Determine if the enumeration facet is active or not.
   *
   * @return a <code>boolean</code> value
   */
  public boolean isEnumerationEnabled() {
    return m_isEnumSet;
  }
  
  public Set getEnumeration() {
    return m_enumeration;
  }
  
  protected void enumerationOk(Object obj) throws ValidationException {
    if(m_isEnumSet && !m_enumeration.contains(obj)) {
      throw new ValidationException(obj + " is not contained in the enumeration facet.");
    }
  }
}
