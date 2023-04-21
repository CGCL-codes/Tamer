package org.castafiore.shoppingmall.util.list;

import org.castafiore.ui.Container;
import org.castafiore.wfs.types.File;

public interface ListItem<T extends File> extends Container {

    public boolean isChecked();

    public T getItem();

    public void setItem(T file);
}
