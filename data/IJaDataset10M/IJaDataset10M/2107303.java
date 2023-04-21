package com.frameworkset.common.poolman.handle;

import com.frameworkset.common.poolman.Record;

/**
 * <p>Title: NullRowHandler.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date 2010-1-22 ����06:13:30
 * @author biaoping.yin
 * @version 1.0
 * @param <T>
 */
public abstract class NullRowHandler<T> extends BaseRowHandler<T> {

    @Override
    public void handleRow(T rowValue, Record origine) throws Exception {
        handleRow(origine);
    }

    public abstract void handleRow(Record origine) throws Exception;
}
