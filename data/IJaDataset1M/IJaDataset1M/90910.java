package org.zkoss.gwt.client.zul.sel;

import org.zkoss.gwt.client.zul.Widget;
import java.util.*;
import com.google.gwt.core.client.JavaScriptObject;

public class SelectWidget extends org.zkoss.gwt.client.zul.mesh.MeshWidget {

    protected native JavaScriptObject create();

    public native void setRows(int rows);

    public native int getRows();

    public native void setCheckmark(boolean checkmark);

    public native boolean isCheckmark();

    public native void setMultiple(boolean multiple);

    public native boolean isMultiple();

    public native void setSelectedIndex(int selectedIndex);

    public native int getSelectedIndex();

    public native void setName(String name);

    public native String getName();

    public native void setSelectedItem(ItemWidget item);

    public native ItemWidget getSelectedItem();

    public native Widget[] getSelectedItems();

    public native int indexOfItem(ItemWidget item);

    public native void selectItem(ItemWidget item);

    public native void clearSelection();
}
