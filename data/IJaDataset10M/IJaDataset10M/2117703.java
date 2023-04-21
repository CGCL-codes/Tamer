package org.zkoss.gwt.client.zul.grid;

import org.zkoss.gwt.client.zul.Widget;
import java.util.*;
import com.google.gwt.core.client.JavaScriptObject;

public class Grid extends org.zkoss.gwt.client.zul.mesh.MeshWidget {

    protected native JavaScriptObject create();

    public native org.zkoss.gwt.client.zk.Widget getCell(int row, int col);

    public native String getOddRowSclass();

    public native void setOddRowSclass(String scls);

    public native org.zkoss.gwt.client.zul.grid.Columns getHeadWidgetClass();
}
