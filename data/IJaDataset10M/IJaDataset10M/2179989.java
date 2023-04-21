package com.dukesoftware.viewlon3.data.node;

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.List;
import javax.vecmath.Point3d;
import com.dukesoftware.utils.math.MathUtils;
import com.dukesoftware.utils.math.SimpleVector3d;
import com.dukesoftware.utils.math.Sphere;

/**
 * 
 * 
 * @see LogicalObjectArray
 * 
 * FIXME 委譲関係がおかしい！
 * 
 * 
 *
 *
 */
public class LogicalObjecSpring2D<T extends LogicalObjectArray> {

    private static final long serialVersionUID = 1L;

    public static final int DEFAULT_DIST = 300;

    protected final Point3d centerp;

    public static final int TESTCONST = 6;

    protected int[][] obj_spring_const;

    protected final T obj_array;

    protected final LogicalObject obj_set = new LogicalObject();

    public static final int ZERO = 0;

    public final int OBJ_NUM;

    protected final Point3d p = new Point3d();

    public LogicalObjecSpring2D(Point3d centerp, T obj_array) {
        this.obj_array = obj_array;
        OBJ_NUM = obj_array.getObjNum();
        this.centerp = centerp;
        initObjSpringConst();
    }

    public void setVisibleObjset(boolean visible) {
        obj_set.setVisible(visible);
    }

    public boolean checkVisibleObjset() {
        return obj_set.checkVisible();
    }

    public void setLocationObjset(double x, double y, double z, double r) {
        obj_set.set(x, y, z, r);
    }

    public double getRObjset() {
        return obj_set.r;
    }

    public double getXObjset() {
        return obj_set.x;
    }

    public double getYObjset() {
        return obj_set.y;
    }

    public final T getObjArray() {
        return obj_array;
    }

    /**
	 *  要素間のバネ定数を初期化します。
	 */
    private final void initObjSpringConst() {
        obj_spring_const = new int[OBJ_NUM][OBJ_NUM];
        for (int i = 0; i < OBJ_NUM; i++) {
            for (int j = i + 1; j < OBJ_NUM; j++) {
                obj_spring_const[i][j] = obj_spring_const[j][i] = DEFAULT_DIST;
            }
        }
    }

    /**
	 * 特定の要素(i->j)間でばねの値を変更します。
	 * @param i
	 * @param j
	 * @param value
	 */
    public final void setObjSpringConst(int i, int j, int value) {
        obj_spring_const[i][j] = value;
    }

    /**
	 * 
	 * @param value
	 */
    public final void setObjSpringConst(int value) {
        for (int i = 0; i < OBJ_NUM; i++) {
            for (int j = i + 1; j < OBJ_NUM; j++) {
                obj_spring_const[i][j] = obj_spring_const[j][i] = value;
            }
        }
    }

    public void setPositionSame(Iterator<Integer> it, double x, double y, double z, double r) {
        while (it.hasNext()) {
            obj_array.get(it.next().intValue()).set(x, y, z, r);
        }
    }

    public void addVectorObj(int i, int j, SimpleVector3d fi) {
        if (i != j) {
            Sphere objI = obj_array.get(i);
            Sphere objJ = obj_array.get(j);
            double xi = objI.x;
            double yi = objI.y;
            double xj = objJ.x;
            double yj = objJ.y;
            double delta = (Point2D.distance(xi, yi, xj, yj) - obj_spring_const[i][j]) / (2 * TESTCONST);
            double x = xi - xj;
            double y = yi - yj;
            if (delta > 0) fi.add(-x, -y, ZERO, Math.abs(delta)); else fi.add(x, y, ZERO, Math.abs(delta));
        }
    }

    /**
	 * Objectを中心に引き寄せる力をセットします。
	 * @param i オブジェクトの番号
	 * @param fi 力がセットされるベクトル
	 */
    public void addCenterAttractiveForce(int i, SimpleVector3d fi) {
        Sphere objI = obj_array.get(i);
        double x = objI.x - centerp.x;
        double y = objI.y - centerp.y;
        fi.add(-x, -y, ZERO, MathUtils.hypot(x, y) / TESTCONST);
    }

    public final void setSpringObjAllinaSet(List<Integer> list, int new_const) {
        for (Iterator<Integer> it = list.iterator(); it.hasNext(); ) {
            int i = it.next();
            for (Iterator<Integer> it2 = list.iterator(); it2.hasNext(); ) {
                int j = it2.next().intValue();
                obj_spring_const[i][j] = obj_spring_const[j][i] = new_const;
            }
        }
    }

    public void setLocationAndchangeSpringConst(List<Integer> list, double delta, double dist, double r) {
        setVisibleObjset(true);
        if (list.size() <= 0) {
            setVisibleObjset(false);
            return;
        }
        setSpringObjAllinaSet(list, (int) dist);
        obj_array.calcObjsGravity2D(list, p);
        setLocationObjset(p.x, p.y, ZERO, obj_array.getMaxObjsDistance3D(list, p) + r + delta);
    }
}
