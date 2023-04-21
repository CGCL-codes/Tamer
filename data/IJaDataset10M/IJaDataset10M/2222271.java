package com.hitao.codegen.util;

import java.util.List;

/**
 * The goal of the IParent interface is to provide a common set of facilities
 * that support the processing of tree structures or any structure managing
 * parent/child relationships.<br>
 *
 * @author zhangjun.ht
 * @created 2010-11-10
 * @version $Id: IParent.java 12 2011-02-20 10:50:23Z guest $
 */
public interface IParent<E> {

    /**
   * Return the list of children currently associated with this parent.
   * May be null or empty.
   *
   * @return the list of children associated with this parent.
   */
    List<E> getChildren();
}
