package com.leclercb.taskunifier.gui.commons.transfer;

import java.io.Serializable;
import com.leclercb.commons.api.utils.CheckUtils;

public class NoteSorterTransferData implements Serializable {

    private int[] elementIndexes;

    public NoteSorterTransferData(int elementIndex) {
        this.elementIndexes = new int[] { elementIndex };
    }

    public NoteSorterTransferData(int[] elementIndexes) {
        CheckUtils.isNotNull(elementIndexes);
        this.elementIndexes = elementIndexes;
    }

    public int[] getElementIndexes() {
        return this.elementIndexes;
    }
}
