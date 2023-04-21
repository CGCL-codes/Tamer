package org.zkoss.bind.impl;

import java.io.Serializable;
import org.zkoss.bind.annotation.Immutable;
import org.zkoss.zk.ui.util.ForEachStatus;

@Immutable
abstract class AbstractForEachStatus implements ForEachStatus, Serializable {

    private static final long serialVersionUID = 1L;

    public ForEachStatus getPrevious() {
        return null;
    }

    public Integer getBegin() {
        return 0;
    }
}
