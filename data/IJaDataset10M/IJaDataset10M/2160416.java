package landscape;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import javax.media.opengl.GLAutoDrawable;
import models.GroundObject;
import models.O3dFile;
import models.VisibleObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import util.Vector3d;
import fileutils.FileUtil;

public class GroundEntity extends SingleEntity {

    float width, squareWidth;

    int resolution;

    protected static List<GroundEntity> grounds = Collections.synchronizedList(new LinkedList<GroundEntity>());

    GroundEntity(Entity parent, String name, Vector3d pos, float rot, O3dFile f) {
        super(parent, name, pos, rot, f);
        height = 0;
    }

    GroundEntity(Entity parent, Element e) {
        super(parent, e);
    }

    public void translate(Vector3d v) {
        position = position.add(v);
        getRoot().setAllModified(false, true);
        setModified(true, false);
    }

    public void rotate(float r) {
        rotation += r;
        getRoot().setAllModified(false, true);
        setModified(true, false);
    }

    protected Vector<Vector<Float>> getYs() {
        try {
            return ((GroundObject) (getPrototype())).yValues;
        } catch (Exception e) {
            return null;
        }
    }

    public static float getHeight(float x, float z) {
        try {
            float ret = -Float.MAX_VALUE;
            for (int i = 0; i < grounds.size(); i++) ret = Math.max(ret, getHeight(i, x, z));
            return (ret == -Float.MAX_VALUE ? 0 : ret);
        } catch (Exception e) {
            return 0;
        }
    }

    public static float getHeight(int index, float x, float z) {
        try {
            return grounds.get(index).height(x, z);
        } catch (Exception e) {
            return 0;
        }
    }

    protected float height(float x, float z) {
        if (getPrototype() == null || !(getPrototype() instanceof GroundObject)) return 0;
        if (x >= getMaxValues().x || x <= getMinValues().x || z >= getMaxValues().z || z <= getMinValues().z) return 0;
        width = getPrototype().maxValues.x - getPrototype().minValues.x;
        resolution = getYs().size();
        squareWidth = width / resolution;
        Vector3d pos = new Vector3d(x, 0, z);
        pos = pos.subtract(position);
        pos = pos.rotate(new Vector3d(0, rotation, 0));
        pos = pos.add(new Vector3d((width / 2f), 0, (width / 2f)));
        int qsIndex = (int) pos.z / (int) squareWidth;
        int arrayIndex = (int) pos.x / (int) squareWidth;
        Vector<Vector<Float>> ys = getYs();
        if (ys == null || ys.isEmpty()) return 0;
        Vector3d[] edges = { new Vector3d(arrayIndex * squareWidth, ys.get(qsIndex).get(2 * arrayIndex) + position.y, qsIndex * squareWidth), new Vector3d(arrayIndex * squareWidth, ys.get(qsIndex).get(2 * arrayIndex + 1) + position.y, (qsIndex + 1) * squareWidth), new Vector3d((arrayIndex + 1) * squareWidth, ys.get(qsIndex).get(2 * arrayIndex + 2) + position.y, qsIndex * squareWidth), new Vector3d((arrayIndex + 1) * squareWidth, ys.get(qsIndex).get(2 * arrayIndex + 3) + position.y, (qsIndex + 1) * squareWidth) };
        Vector3d buff = edges[3].subtract(pos);
        buff.y = 0;
        float d1 = buff.value();
        buff = edges[0].subtract(pos);
        buff.y = 0;
        float d2 = buff.value();
        boolean lowerRight = d2 > d1;
        Vector3d[] triangle = { edges[2], edges[1], lowerRight ? edges[3] : edges[0] };
        float weight1 = (pos.x - triangle[1].x) / (triangle[0].x - triangle[1].x);
        Vector3d intersect1 = triangle[1].add(triangle[0].subtract(triangle[1]).scale(weight1));
        float weight2 = Math.abs(triangle[2].x - pos.x) / squareWidth;
        Vector3d intersect2 = triangle[2].add(triangle[lowerRight ? 1 : 0].subtract(triangle[2]).scale(weight2));
        float weight3 = (pos.z - intersect2.z) / (intersect1.z - intersect2.z);
        Vector3d intersect = intersect2.add(intersect1.subtract(intersect2).scale(weight3));
        return intersect.y;
    }

    public void draw(GLAutoDrawable gld) {
        VisibleObject prototype = null;
        prototype = getPrototype();
        if (prototype == null) return;
        gld.getGL().glPushMatrix();
        gld.getGL().glTranslatef(position.x, position.y, position.z);
        gld.getGL().glRotated(rotation, 0, 1, 0);
        for (int dl : prototype.displayLists.get(0)) {
            gld.getGL().glCallList(dl);
        }
        gld.getGL().glPopMatrix();
    }

    public void loadFinished(VisibleObject o) {
        o3dFile = o.o3dFile;
        if (grounds.indexOf(this) == -1) {
            grounds.add(this);
        }
        getRoot().setAllModified(true, true);
    }

    public Element entityToElement(Document doc) {
        Element ret = doc.createElement("Ground");
        ret.setAttribute("name", name);
        ret.setAttribute("position", position.toNumeralString());
        ret.setAttribute("rotation", rotation + "");
        ret.setAttribute("o3dfile", FileUtil.makeAppRelative(o3dFile).getPath());
        return ret;
    }

    protected void recalcHeights() {
        height = 0;
    }
}
