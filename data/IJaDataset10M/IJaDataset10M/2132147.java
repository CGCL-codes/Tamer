package com.dukesoftware.viewlon3.gui.view3d.mode;

import java.util.Iterator;
import java.util.List;
import com.dukesoftware.viewlon3.data.common.DataManagerCore;
import com.dukesoftware.viewlon3.data.internal.ClassObject;
import com.dukesoftware.viewlon3.data.internal.SensorObject;

/**
 * 
 * 
 * @see SensorMode3DAbstract
 * 
 * 
 *
 *
 */
public class SensorMode3DRealObject extends SensorMode3DAbstract {

    private static final SensorMode3DRealObject singleton = new SensorMode3DRealObject();

    private SensorMode3DRealObject() {
    }

    public static Mode3D getInstance() {
        return singleton;
    }

    public void setDist(int dist) {
        s_data.setObjSpringConst(dist);
    }

    public void updatePos(int dist) {
        setSensorObjSpringConstForSensorRealLevel();
        simSensorObj(dist);
        simRealObj();
        simClassObj();
    }

    private void simClassObj() {
        for (Iterator<ClassObject> it = class_array.iterator(); it.hasNext(); ) {
            ClassObject cobj = (ClassObject) it.next();
            int i = Integer.parseInt(cobj.getID());
            List<Integer> list = cobj.returnInstanceList();
            if (list.size() > 0) {
                rarray.calcObjsGravity2D(list, p);
                carray.setPositionSpecial(i, p.x, p.y, p.z, 0);
            }
        }
    }

    private void setSensorObjSpringConstForSensorRealLevel() {
        DataManagerCore oarray = d_con.getPointerData();
        for (Iterator<SensorObject> it = oarray.sensorIterator(); it.hasNext(); ) {
            SensorObject sobj = (SensorObject) it.next();
            String id = sobj.getID();
            int i = oarray.searchSensorInternalID(id);
            int pid = oarray.getAttachedObjectIDFromSensorID(id);
            if (pid != NOID) {
                final double r = sarray.get(i).r;
                s_data.setSpringObjAllinaSet(oarray.returnSensorList(pid), (int) (r * r));
            }
        }
    }

    public String toString() {
        return "Sensor-Real Relation";
    }

    protected void switchModel() {
        rarray_model.enebleTransparency(true);
        reciever.switch3DModel(true, true, false, false);
        relationArrowManager.hideAll();
    }
}
