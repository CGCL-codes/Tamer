package com.leclercb.taskunifier.gui.commons.transfer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import com.leclercb.commons.api.utils.CheckUtils;

public class TaskSearcherTransferable implements Transferable {

    public static final DataFlavor TASK_SEARCHER_FLAVOR = new DataFlavor(TaskSearcherTransferData.class, "TASK_SEARCHER_FLAVOR");

    public static final DataFlavor[] FLAVORS = { TASK_SEARCHER_FLAVOR };

    private static final List<DataFlavor> FLAVOR_LIST = Arrays.asList(FLAVORS);

    private TaskSearcherTransferData data;

    public TaskSearcherTransferable(TaskSearcherTransferData data) {
        CheckUtils.isNotNull(data);
        this.data = data;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return FLAVORS;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return FLAVOR_LIST.contains(flavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return this.data;
    }
}
