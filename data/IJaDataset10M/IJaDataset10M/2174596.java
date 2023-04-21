package com.iver.cit.jdwglib.dwg.objects;

import com.iver.cit.jdwglib.dwg.DwgHandleReference;
import com.iver.cit.jdwglib.dwg.DwgObject;

/**
 * @author alzabord
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DwgSortEntStable extends DwgObject {

    private DwgHandleReference[] handles;

    private DwgHandleReference[] objHandles;

    private DwgHandleReference parentHdl;

    /**
	 * @param index
	 */
    public DwgSortEntStable(int index) {
        super(index);
    }

    /**
	 * @param handles
	 */
    public void setSortedHandles(DwgHandleReference[] handles) {
        this.handles = handles;
    }

    /**
	 * @param handles2
	 */
    public void setObjHandles(DwgHandleReference[] handles2) {
        this.objHandles = handles2;
    }

    /**
	 * @param handle
	 */
    public void setParentHandle(DwgHandleReference handle) {
        this.parentHdl = handle;
    }

    public Object clone() {
        DwgSortEntStable obj = new DwgSortEntStable(index);
        this.fill(obj);
        return obj;
    }

    protected void fill(DwgObject obj) {
        super.fill(obj);
        DwgSortEntStable myObj = (DwgSortEntStable) obj;
        myObj.setObjHandles(objHandles);
        myObj.setParentHandle(parentHdl);
        myObj.setSortedHandles(handles);
    }
}
