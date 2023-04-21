package com.antlersoft.odb;

import java.io.Serializable;

/**
 * <p>Title: odb</p>
 * <p>Description: Simple Object Database</p>
 * <p>Copyright (c) 2000-2005  Michael A. MacDonald<p>
 * ----- - - -- - - --
 * <p>
 *     This package is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 * <p>
 *     This package is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * <p>
 *     You should have received a copy of the GNU General Public License
 *     along with the package (see gpl.txt); if not, see www.gnu.org
 * <p>
 * ----- - - -- - - --
 * <p>Company: </p>
 * @author Michael MacDonald
 * @version 1.0
 */
public class ObjectRefKey implements Comparable, Serializable {

    private Comparable m_object_key;

    public int compareTo(Object parm1) {
        ObjectRefKey to_compare = (ObjectRefKey) parm1;
        if (m_object_key == null) {
            if (to_compare.m_object_key == null) return 0;
            return -1;
        }
        if (to_compare.m_object_key == null) return 1;
        return m_object_key.compareTo(to_compare.m_object_key);
    }

    public ObjectRefKey(ObjectRef ref) {
        if (ref != null && ref.impl != null) m_object_key = (Comparable) ref.impl.objectKey; else m_object_key = null;
    }

    public ObjectRefKey(Persistent p) {
        m_object_key = (Comparable) p._getPersistentImpl().objectKey;
    }
}
