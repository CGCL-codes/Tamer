package com.ourlinc.conference.book.impl;

import com.ourlinc.conference.PodiBase;
import com.ourlinc.omni.persistence.Persistence;
import com.ourlinc.omni.persistence.Persister;

/**
 * 依赖对象依赖注入接口
 * @author pengchengji
 */
public interface BookPodi extends PodiBase {

    public <E extends Persistence> Persister<E> getPersister(Class<E> arg0);
}
