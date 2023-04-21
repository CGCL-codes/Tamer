package GShape.Core.Objects;

import java.util.Vector;
import GShape.Core.GCore;
import GShape.Core.Expression.GExpressionItem;
import GShape.Core.Objects.GMaterial.DisplayMode;
import GShape.Core.Objects.Primitives.*;
import processing.core.PApplet;

public class GCollection implements iGObject {

    private java.util.Vector<iGObject> children;

    private boolean isVisible = false;

    GCore core;

    public GCollection(GCore core, String name, java.util.Vector<iGObject> children) {
        core.Add(name, this);
        this.children = children;
        this.core = core;
    }

    public void Draw(PApplet handler, DisplayMode mode) {
    }

    @Override
    public Vector<iGObject> GetChildren() {
        return children;
    }

    @Override
    public boolean Delete() {
        boolean ok = true;
        for (iGObject child : children) {
            if (core.Remove(child) == false) ok = false;
        }
        core.Remove(this);
        return ok;
    }

    @Override
    public Object GetPrimitive(Class type) {
        if (children != null) return children.get(0).GetPrimitive(type); else return null;
    }

    @Override
    public void SetMaterial(GMaterial mat) {
    }

    public static float getBiggestGObjectNumber(java.util.Vector<iGObject> children) throws Exception {
        float biggestNumber = 0;
        for (iGObject item : children) {
            if (item.GetPrimitive(rect.class) != null) {
                rect o = (rect) item.GetPrimitive(rect.class);
                if (o.area > biggestNumber) biggestNumber = o.area;
            } else if (item.GetPrimitive(vrect.class) != null) {
                vrect o = (vrect) item.GetPrimitive(vrect.class);
                if (o.area > biggestNumber) biggestNumber = o.area;
            } else if (item.GetPrimitive(cube.class) != null) {
                cube o = (cube) item.GetPrimitive(cube.class);
                if (o.volume > biggestNumber) biggestNumber = o.volume;
            } else {
                throw new Exception("Error in GCollection.getBiggestGObjectNumber: unrecognised primitive");
            }
        }
        return biggestNumber;
    }

    public static float getSmallestGObjectNumber(java.util.Vector<iGObject> children) throws Exception {
        float smallestNumber = 0;
        boolean first = true;
        for (iGObject item : children) {
            if (item.GetPrimitive(rect.class) != null) {
                rect o = (rect) item.GetPrimitive(rect.class);
                if (o.area < smallestNumber || first == true) smallestNumber = o.area;
            } else if (item.GetPrimitive(vrect.class) != null) {
                vrect o = (vrect) item.GetPrimitive(vrect.class);
                if (o.area < smallestNumber || first == true) smallestNumber = o.area;
            } else if (item.GetPrimitive(cube.class) != null) {
                cube o = (cube) item.GetPrimitive(cube.class);
                if (o.volume < smallestNumber || first == true) smallestNumber = o.volume;
            } else {
                throw new Exception("Error in GCollection.getBiggestGObjectNumber: unrecognised primitive");
            }
            first = false;
        }
        return smallestNumber;
    }

    @Override
    public void Rename(String Name) {
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void setInvisible() {
        for (iGObject item : children) {
            item.setInvisible();
        }
    }

    @Override
    public void setVisible() {
        for (iGObject item : children) {
            item.setVisible();
        }
    }
}
