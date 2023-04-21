package com.dukesoftware.viewlon3.gui.view3d.mode;

import java.util.Iterator;
import java.util.List;
import javax.vecmath.Point3d;
import com.dukesoftware.utils.math.Sphere;
import com.dukesoftware.viewlon3.data.internal.ClassObject;

/**
 * 
 * 
 * @see RealMode3DAbstract
 * 
 * 
 *
 *
 */
public class RealMode3DClassSet extends RealMode3DAbstract {

    private static RealMode3DClassSet singleton = new RealMode3DClassSet();

    private RealMode3DClassSet() {
    }

    public static Mode3D getInstance() {
        return singleton;
    }

    public void setDist(int dist) {
        setRealObjSpringConstForRealClassLevel();
    }

    public boolean setFoucus(Point3d centerp, int mapx, int mapy) {
        int select_num = class_array.select;
        if (select_num != NOID) {
            Sphere obj = carray.get(select_num);
            centerp.x += mapx / 2.0 - obj.x;
            centerp.y += mapy / 2.0 - obj.y;
            return true;
        }
        return false;
    }

    public void updatePos(int dist) {
        r_data.setObjSpringConst(dist);
        setRealObjSpringConstForRealClassLevel();
        simRealObj(dist);
        simClassObj();
    }

    private void setRealObjSpringConstForRealClassLevel() {
        for (Iterator<ClassObject> it = class_array.iterator(); it.hasNext(); ) {
            List<Integer> list = it.next().returnInstanceList();
            if (list.size() > 0) {
                int r = (int) (rarray.getMaxR(list));
                r_data.setSpringObjAllinaSet(list, r * r / 2);
            }
        }
    }

    protected void simClassObj() {
        for (Iterator<ClassObject> it = class_array.iterator(); it.hasNext(); ) {
            ClassObject cobj = it.next();
            int i = Integer.parseInt(cobj.getID());
            List<Integer> list = cobj.returnInstanceList();
            if (list.size() > 0) {
                rarray.calcObjsGravity3D(list, p);
                double ir = rarray.getMaxObjsDistance3D(list, p) + rarray.getMaxR(list) + 5;
                carray.setPositionSpecial(i, p.x, p.y, p.z, ir);
                Sphere obj = carray.get(i);
                carray_model.setPosAndScale(i, changeValFor3D(obj.x), changeValFor3D(obj.y), changeValFor3D(obj.z), changeValFor3D(obj.r));
            }
        }
    }

    public String toString() {
        return "Class Set";
    }

    protected void switchModel() {
        carray_model.enebleTransparency(true);
        rarray_model.enebleTransparency(false);
        reciever.switch3DModel(false, true, false, true);
        relationArrowManager.hideAll();
    }
}
