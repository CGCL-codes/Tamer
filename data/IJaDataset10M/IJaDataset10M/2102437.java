package com.kescom.matrix.core.batch;

import com.kescom.matrix.core.db.IIndexND;

public interface IBatchPolicy extends IIndexND {

    boolean allowReady(IBatchJob job);
}
