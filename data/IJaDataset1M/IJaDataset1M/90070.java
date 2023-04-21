package org.shestkoff.nimium.model.dao.linking;

import java.lang.reflect.InvocationTargetException;

/**
 * Project: Smart Advertising
 * Created by: Yakoushin Andrey
 * Date: 24.08.2009
 * Time: 18:48:24
 * <p/>
 * Copyright (c) 1999-2009 Magenta Corporation Ltd. All Rights Reserved.
 * Magenta Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * @author ELectronic ENgine
 */
public interface Getter<E, R> {

    R get(E e) throws InvocationTargetException, IllegalAccessException;
}
