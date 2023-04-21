package net.sourceforge.nattable.extension.copypaste;

import net.sourceforge.nattable.data.DataUpdateHelper;

public interface IPasteExecutionSupport<T> {

    public void doPaste(DataUpdateHelper<T> helper);
}
