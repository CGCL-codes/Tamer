package net.sf.avocado_cad.example.toolsketch;

import net.sf.avocado_cad.eclipse.ui.AvoGlobal;
import net.sf.avocado_cad.model.api.adt.IParamSet;
import net.sf.avocado_cad.model.api.adt.ParamType;
import ui.tools.ToolModelSketch;
import backend.adt.Param;
import backend.adt.ParamSet;
import backend.adt.Point2D;
import backend.model.Sketch;
import backend.model.sketch.Prim2DArc;
import backend.model.sketch.Prim2DLine;
import backend.model.sketch.Prim2DList;

public class ToolSketchExampleModel implements ToolModelSketch {

    public ToolSketchExampleModel() {
    }

    public Prim2DList buildPrim2DList(IParamSet paramSet) {
        try {
            Point2D ptC = (Point2D) paramSet.getParam("c").getDataPoint2D();
            double size = paramSet.getParam("s").getDataDouble();
            int triangles = paramSet.getParam("t").getDataInteger();
            if (triangles >= 2) {
                Prim2DList primList = new Prim2DList();
                Point2D arm = new Point2D(size, 0.0);
                double degPerTri = 360.0 / (double) (2 * triangles);
                for (int i = 0; i < triangles; i++) {
                    primList.add(new Prim2DLine(ptC, arm.getNewRotatedPt(2 * i * degPerTri).addPt(ptC)));
                    primList.add(new Prim2DLine(arm.getNewRotatedPt((2 * i + 1) * degPerTri).addPt(ptC), arm.getNewRotatedPt(2 * i * degPerTri).addPt(ptC)));
                    primList.add(new Prim2DLine(ptC, arm.getNewRotatedPt((2 * i + 1) * degPerTri).addPt(ptC)));
                }
                primList.add(new Prim2DArc(ptC, 1.2 * size, 0.0, 360.0));
                primList.add(new Prim2DArc(ptC, 1.25 * size, 0.0, 360.0));
                return primList;
            }
        } catch (Exception ex) {
            System.out.println(ex.getClass());
        }
        return null;
    }

    public boolean paramSetIsValid(IParamSet paramSet) {
        boolean isValid = (paramSet != null && "Example".equals(paramSet.getLabel()) && paramSet.hasParam("c", ParamType.Point2D) && paramSet.hasParam("s", ParamType.Double) && paramSet.hasParam("t", ParamType.Integer));
        return isValid;
    }

    public void updateDerivedParams(ParamSet paramSet) {
    }

    public void finalize(ParamSet paramSet) {
        Sketch sketch = (Sketch) AvoGlobal.project.getActiveSketch();
        if (sketch != null) {
            sketch.deselectAllFeat2D();
        }
    }

    public ParamSet constructNewParamSet() {
        ParamSet pSet = new ParamSet("Example");
        pSet.addParam("c", new Param("Center", new Point2D(0.0, 0.0)));
        pSet.addParam("s", new Param("Size", 4.0));
        pSet.addParam("t", new Param("Triangles", 9));
        AvoGlobal.paramSetToolModels.put(pSet, new ToolSketchExampleModel());
        return pSet;
    }

    public boolean isWorthKeeping(IParamSet paramSet) {
        return true;
    }
}
