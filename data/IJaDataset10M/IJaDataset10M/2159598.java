package org.jnet.shapespecial;

import org.jnet.shape.Mesh;
import org.jnet.util.Escape;
import org.jnet.util.Logger;
import org.jnet.util.ColorEncoder;
import org.jnet.util.ArrayUtil;
import org.jnet.viewer.JnetConstants;
import org.jnet.jvxl.readers.JvxlReader;
import java.util.BitSet;
import java.util.Hashtable;
import java.util.Vector;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix3f;
import javax.vecmath.Point3f;
import javax.vecmath.Point3i;
import javax.vecmath.Point4f;
import javax.vecmath.Vector3f;
import org.jnet.g3d.Graphics3D;
import org.jnet.jvxl.data.JvxlData;
import org.jnet.jvxl.data.MeshData;
import org.jnet.jvxl.api.MeshDataServer;
import org.jnet.jvxl.readers.SurfaceGenerator;

public class Isosurface extends MeshFileCollection implements MeshDataServer {

    private IsosurfaceMesh[] isomeshes = new IsosurfaceMesh[4];

    private IsosurfaceMesh thisMesh;

    public void allocMesh(String thisID) {
        meshes = isomeshes = (IsosurfaceMesh[]) ArrayUtil.ensureLength(isomeshes, meshCount + 1);
        currentMesh = thisMesh = isomeshes[meshCount++] = new IsosurfaceMesh(thisID, g3d, colix);
        sg.setJvxlData(jvxlData = thisMesh.jvxlData);
    }

    public void initShape() {
        super.initShape();
        myType = "isosurface";
        jvxlData = new JvxlData();
        sg = new SurfaceGenerator(viewer, this, colorEncoder, null, jvxlData);
    }

    private int lighting;

    private boolean iHaveBitSets;

    private int modelIndex;

    private int nodeIndex;

    private int moNumber;

    private short defaultColix;

    private Point3f center;

    private boolean isPhaseColored;

    protected SurfaceGenerator sg;

    private JvxlData jvxlData;

    private ColorEncoder colorEncoder = new ColorEncoder();

    public void setProperty(String propertyName, Object value, BitSet bs) {
        if ("delete" == propertyName) {
            setPropertySuper(propertyName, value, bs);
            if (!explicitID) nLCAO = nUnnamed = 0;
            return;
        }
        if ("remapcolor" == propertyName) {
            if (thisMesh != null) remapColors();
            return;
        }
        if ("thisID" == propertyName) {
            setPropertySuper("thisID", value, null);
            return;
        }
        if ("map" == propertyName) {
            setProperty("squareData", Boolean.FALSE, null);
            return;
        }
        if ("color" == propertyName) {
            if (thisMesh != null) {
                thisMesh.vertexColixes = null;
                thisMesh.isColorSolid = true;
            } else {
                for (int i = meshCount; --i >= 0; ) {
                    isomeshes[i].vertexColixes = null;
                    isomeshes[i].isColorSolid = true;
                }
            }
            setPropertySuper(propertyName, value, bs);
            return;
        }
        if ("fixed" == propertyName) {
            isFixed = ((Boolean) value).booleanValue();
            setModelIndex();
            return;
        }
        if ("modelIndex" == propertyName) {
            modelIndex = ((Integer) value).intValue();
            sg.setModelIndex(modelIndex);
            return;
        }
        if ("lcaoCartoon" == propertyName) {
            Vector3f[] info = (Vector3f[]) value;
            if (!explicitID) {
                setPropertySuper("thisID", null, null);
            }
            if (sg.setParameter("lcaoCartoonCenter", info[2])) return;
            drawLcaoCartoon(info[0], info[1], info[3]);
            return;
        }
        if ("title" == propertyName) {
            if (value instanceof String && "-".equals((String) value)) value = null;
            setPropertySuper(propertyName, value, bs);
            sg.setParameter("title", title, bs);
            return;
        }
        if ("select" == propertyName) {
            if (iHaveBitSets) return;
        }
        if ("ignore" == propertyName) {
            if (iHaveBitSets) return;
        }
        if ("nodeIndex" == propertyName) {
            nodeIndex = ((Integer) value).intValue();
        }
        if ("pocket" == propertyName) {
            Boolean pocket = (Boolean) value;
            lighting = (pocket.booleanValue() ? JnetConstants.FULLYLIT : JnetConstants.FRONTLIT);
        }
        if ("colorRGB" == propertyName) {
            int rgb = ((Integer) value).intValue();
            defaultColix = Graphics3D.getColix(rgb);
        }
        if ("molecularOrbital" == propertyName) {
            moNumber = ((Integer) value).intValue();
        }
        if ("center" == propertyName) {
            center.set((Point3f) value);
        }
        if ("phase" == propertyName) {
            isPhaseColored = true;
        }
        if ("finalize" == propertyName) {
            setScriptInfo();
            setJvxlInfo();
        }
        if (sg.setParameter(propertyName, value, bs)) return;
        if ("init" == propertyName) {
            setPropertySuper("thisID", JnetConstants.PREVIOUS_MESH_ID, null);
            if (!(iHaveBitSets = getScriptBitSets(script = (String) value))) {
                sg.setParameter("select", bs);
            }
            initializeIsosurface();
            sg.setModelIndex(modelIndex);
            return;
        }
        if ("clear" == propertyName) {
            discardTempData(true);
            return;
        }
        setPropertySuper(propertyName, value, bs);
    }

    private void setPropertySuper(String propertyName, Object value, BitSet bs) {
        currentMesh = thisMesh;
        super.setProperty(propertyName, value, bs);
        thisMesh = (IsosurfaceMesh) currentMesh;
        jvxlData = (thisMesh == null ? null : thisMesh.jvxlData);
    }

    public Object getProperty(String property, int index) {
        if (property == "dataRange") return (thisMesh == null ? null : new float[] { thisMesh.jvxlData.mappedDataMin, thisMesh.jvxlData.mappedDataMax, thisMesh.jvxlData.valueMappedToRed, thisMesh.jvxlData.valueMappedToBlue });
        if (property == "moNumber") return new Integer(moNumber);
        if (thisMesh == null) return "no current isosurface";
        if (property == "plane") return jvxlData.jvxlPlane;
        if (property == "jvxlFileData") return JvxlReader.jvxlGetFile(jvxlData, title, "", true, index, thisMesh.getState(myType), shortScript());
        if (property == "jvxlSurfaceData") return JvxlReader.jvxlGetFile(jvxlData, title, "", false, 1, thisMesh.getState(myType), shortScript());
        return super.getProperty(property, index);
    }

    private String shortScript() {
        return (thisMesh.scriptCommand == null ? "" : thisMesh.scriptCommand.substring(0, (thisMesh.scriptCommand + ";").indexOf(";")));
    }

    private boolean getScriptBitSets(String script) {
        if (script == null) return false;
        int i = script.indexOf("# ({");
        if (i < 0) return false;
        int j = script.indexOf("})", i);
        if (j < 0) return false;
        sg.setParameter("select", Escape.unescapeBitset(script.substring(i + 3, j + 1)));
        if ((i = script.indexOf("({", j)) < 0) return false;
        j = script.indexOf("})", i);
        if (j > i) sg.setParameter("ignore", Escape.unescapeBitset(script.substring(i + 1, j + 1)));
        if ((i = script.indexOf("/({", j)) == j + 2) {
            if ((j = script.indexOf("})", i)) < 0) return false;
            viewer.setTrajectory(Escape.unescapeBitset(script.substring(i + 3, j + 1)));
        }
        return true;
    }

    private String fixScript(String script, BitSet bsSelected, BitSet bsIgnore) {
        if (script == null) return null;
        if (script.indexOf("# ({") >= 0) return script;
        if (script.charAt(0) == ' ') return myType + " " + thisMesh.thisID + script;
        if (!sg.getIUseBitSets()) return script;
        BitSet bs = viewer.getBitSetTrajectories();
        return script + "# " + (bsSelected == null ? "({null})" : Escape.escape(bsSelected)) + " " + (bsIgnore == null ? "({null})" : Escape.escape(bsIgnore)) + (bs == null ? "" : "/" + Escape.escape(bs));
    }

    private void initializeIsosurface() {
        lighting = JnetConstants.FRONTLIT;
        modelIndex = viewer.getCurrentModelIndex();
        isFixed = (modelIndex < 0);
        if (modelIndex < 0) modelIndex = 0;
        title = null;
        nodeIndex = -1;
        colix = Graphics3D.ORANGE;
        defaultColix = 0;
        isPhaseColored = false;
        center = new Point3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        linkedMesh = null;
        initState();
    }

    private void initState() {
        associateNormals = true;
        sg.initState();
    }

    private void discardTempData(boolean discardAll) {
        if (!discardAll) return;
        title = null;
        if (thisMesh == null) return;
        thisMesh.surfaceSet = null;
    }

    private int indexColorPositive;

    private int indexColorNegative;

    private short getDefaultColix() {
        if (defaultColix != 0) return defaultColix;
        if (!sg.isCubeData()) return colix;
        int argb;
        if (sg.getCutoff() >= 0) {
            indexColorPositive = (indexColorPositive % JnetConstants.argbsIsosurfacePositive.length);
            argb = JnetConstants.argbsIsosurfacePositive[indexColorPositive++];
        } else {
            indexColorNegative = (indexColorNegative % JnetConstants.argbsIsosurfaceNegative.length);
            argb = JnetConstants.argbsIsosurfaceNegative[indexColorNegative++];
        }
        return Graphics3D.getColix(argb);
    }

    private int nLCAO = 0;

    private void drawLcaoCartoon(Vector3f z, Vector3f x, Vector3f rotAxis) {
        String lcaoCartoon = sg.setLcao();
        float rotRadians = rotAxis.x + rotAxis.y + rotAxis.z;
        defaultColix = Graphics3D.getColix(sg.getColor(1));
        int colorNeg = sg.getColor(-1);
        Vector3f y = new Vector3f();
        boolean isReverse = (lcaoCartoon.length() > 0 && lcaoCartoon.charAt(0) == '-');
        if (isReverse) lcaoCartoon = lcaoCartoon.substring(1);
        int sense = (isReverse ? -1 : 1);
        y.cross(z, x);
        if (rotRadians != 0) {
            AxisAngle4f a = new AxisAngle4f();
            if (rotAxis.x != 0) a.set(x, rotRadians); else if (rotAxis.y != 0) a.set(y, rotRadians); else a.set(z, rotRadians);
            Matrix3f m = new Matrix3f();
            m.set(a);
            m.transform(x);
            m.transform(y);
            m.transform(z);
        }
        if (thisMesh == null && nLCAO == 0) nLCAO = meshCount;
        String id = (thisMesh == null ? "lcao" + (++nLCAO) + "_" + lcaoCartoon : thisMesh.thisID);
        if (thisMesh == null) allocMesh(id);
        if (lcaoCartoon.equals("px")) {
            thisMesh.thisID += "a";
            Mesh meshA = thisMesh;
            createLcaoLobe(x, sense);
            setProperty("thisID", id + "b", null);
            createLcaoLobe(x, -sense);
            thisMesh.colix = Graphics3D.getColix(colorNeg);
            linkedMesh = thisMesh.linkedMesh = meshA;
            return;
        }
        if (lcaoCartoon.equals("py")) {
            thisMesh.thisID += "a";
            Mesh meshA = thisMesh;
            createLcaoLobe(y, sense);
            setProperty("thisID", id + "b", null);
            createLcaoLobe(y, -sense);
            thisMesh.colix = Graphics3D.getColix(colorNeg);
            linkedMesh = thisMesh.linkedMesh = meshA;
            return;
        }
        if (lcaoCartoon.equals("pz")) {
            thisMesh.thisID += "a";
            Mesh meshA = thisMesh;
            createLcaoLobe(z, sense);
            setProperty("thisID", id + "b", null);
            createLcaoLobe(z, -sense);
            thisMesh.colix = Graphics3D.getColix(colorNeg);
            linkedMesh = thisMesh.linkedMesh = meshA;
            return;
        }
        if (lcaoCartoon.equals("pxa")) {
            createLcaoLobe(x, sense);
            return;
        }
        if (lcaoCartoon.equals("pxb")) {
            createLcaoLobe(x, -sense);
            return;
        }
        if (lcaoCartoon.equals("pya")) {
            createLcaoLobe(y, sense);
            return;
        }
        if (lcaoCartoon.equals("pyb")) {
            createLcaoLobe(y, -sense);
            return;
        }
        if (lcaoCartoon.equals("pza")) {
            createLcaoLobe(z, sense);
            return;
        }
        if (lcaoCartoon.equals("pzb")) {
            createLcaoLobe(z, -sense);
            return;
        }
        if (lcaoCartoon.indexOf("sp") == 0 || lcaoCartoon.indexOf("lp") == 0) {
            createLcaoLobe(z, sense);
            return;
        }
        createLcaoLobe(null, 1);
        return;
    }

    private Point4f lcaoDir = new Point4f();

    private void createLcaoLobe(Vector3f lobeAxis, float factor) {
        initState();
        if (Logger.debugging) {
            Logger.debug("creating isosurface " + thisMesh.thisID);
        }
        thisMesh.colix = defaultColix;
        if (lobeAxis == null) {
            setProperty("sphere", new Float(factor / 2f), null);
            return;
        }
        lcaoDir.x = lobeAxis.x * factor;
        lcaoDir.y = lobeAxis.y * factor;
        lcaoDir.z = lobeAxis.z * factor;
        lcaoDir.w = 0.7f;
        setProperty("lobe", lcaoDir, null);
        setScriptInfo();
    }

    public void invalidateTriangles() {
        thisMesh.invalidateTriangles();
    }

    public void fillMeshData(MeshData meshData, int mode) {
        if (meshData == null) {
            if (thisMesh == null) allocMesh(null);
            thisMesh.clear("isosurface", sg.getIAddGridPoints(), thisMesh.showTriangles);
            thisMesh.colix = getDefaultColix();
            if (isPhaseColored || thisMesh.jvxlData.isBicolorMap) thisMesh.isColorSolid = false;
            return;
        }
        switch(mode) {
            case MeshData.MODE_GET_VERTICES:
                meshData.vertices = thisMesh.vertices;
                meshData.vertexValues = thisMesh.vertexValues;
                meshData.vertexCount = thisMesh.vertexCount;
                meshData.vertexIncrement = thisMesh.vertexIncrement;
                meshData.polygonCount = thisMesh.polygonCount;
                meshData.polygonIndexes = thisMesh.polygonIndexes;
                return;
            case MeshData.MODE_GET_COLOR_INDEXES:
                if (thisMesh.vertexColixes == null || thisMesh.vertexCount > thisMesh.vertexColixes.length) thisMesh.vertexColixes = new short[thisMesh.vertexCount];
                meshData.vertexColixes = thisMesh.vertexColixes;
                return;
            case MeshData.MODE_PUT_SETS:
                thisMesh.surfaceSet = meshData.surfaceSet;
                thisMesh.vertexSets = meshData.vertexSets;
                thisMesh.nSets = meshData.nSets;
                return;
        }
    }

    public void notifySurfaceGenerationCompleted() {
        setModelIndex();
        thisMesh.initialize(sg.getPlane() != null ? JnetConstants.FULLYLIT : lighting);
    }

    public void notifySurfaceMappingCompleted() {
        setModelIndex();
        String schemeName = colorEncoder.getColorSchemeName();
        viewer.setPropertyColorScheme(schemeName, false);
        viewer.setCurrentColorRange(jvxlData.valueMappedToRed, jvxlData.valueMappedToBlue);
        thisMesh.isColorSolid = false;
        thisMesh.colorCommand = "color $" + thisMesh.thisID + " " + getUserColorScheme(schemeName) + " range " + (jvxlData.isColorReversed ? jvxlData.valueMappedToBlue + " " + jvxlData.valueMappedToRed : jvxlData.valueMappedToRed + " " + jvxlData.valueMappedToBlue);
    }

    public Point3f[] calculateGeodesicSurface(BitSet bsSelected, float envelopeRadius) {
        return viewer.calculateSurface(bsSelected, envelopeRadius);
    }

    public int getSurfacePointIndex(float cutoff, boolean isCutoffAbsolute, int x, int y, int z, Point3i offset, int vA, int vB, float valueA, float valueB, Point3f pointA, Vector3f edgeVector, boolean isContourType) {
        return 0;
    }

    private boolean associateNormals;

    public int addVertexCopy(Point3f vertexXYZ, float value, int assocVertex) {
        return thisMesh.addVertexCopy(vertexXYZ, value, assocVertex, associateNormals);
    }

    public void addTriangleCheck(int iA, int iB, int iC, int check, boolean isAbsolute) {
        if (isAbsolute && !MeshData.checkCutoff(iA, iB, iC, thisMesh.vertexValues)) return;
        thisMesh.addTriangleCheck(iA, iB, iC, check);
    }

    private void setModelIndex() {
        setModelIndex(nodeIndex, modelIndex);
        thisMesh.ptCenter.set(center);
    }

    private void setScriptInfo() {
        thisMesh.title = sg.getTitle();
        thisMesh.scriptCommand = fixScript(sg.getScript(), sg.getBsSelected(), sg.getBsIgnore());
    }

    private void setJvxlInfo() {
        jvxlData.jvxlDefinitionLine = JvxlReader.jvxlGetDefinitionLine(jvxlData, false);
        jvxlData.jvxlInfoLine = JvxlReader.jvxlGetDefinitionLine(jvxlData, true);
    }

    public Vector getShapeDetail() {
        Vector V = new Vector();
        for (int i = 0; i < meshCount; i++) {
            Hashtable info = new Hashtable();
            IsosurfaceMesh mesh = isomeshes[i];
            if (mesh == null) continue;
            info.put("ID", (mesh.thisID == null ? "<noid>" : mesh.thisID));
            info.put("vertexCount", new Integer(mesh.vertexCount));
            if (mesh.ptCenter.x != Float.MAX_VALUE) info.put("center", mesh.ptCenter);
            if (mesh.jvxlData.jvxlDefinitionLine != null) info.put("jvxlDefinitionLine", mesh.jvxlData.jvxlDefinitionLine);
            info.put("modelIndex", new Integer(mesh.modelIndex));
            if (mesh.title != null) info.put("title", mesh.title);
            V.addElement(info);
        }
        return V;
    }

    protected void remapColors() {
        JvxlData jvxlData = thisMesh.jvxlData;
        float[] vertexValues = thisMesh.vertexValues;
        short[] vertexColixes = thisMesh.vertexColixes;
        if (vertexValues == null || jvxlData.isBicolorMap || jvxlData.vertexCount == 0) return;
        if (vertexColixes == null) vertexColixes = thisMesh.vertexColixes = new short[thisMesh.vertexCount];
        for (int i = thisMesh.vertexCount; --i >= 0; ) {
            vertexColixes[i] = viewer.getColixForPropertyValue(vertexValues[i]);
        }
        float[] range = viewer.getCurrentColorRange();
        jvxlData.valueMappedToRed = Math.min(range[0], range[1]);
        jvxlData.valueMappedToBlue = Math.max(range[0], range[1]);
        jvxlData.isJvxlPrecisionColor = true;
        JvxlReader.jvxlCreateColorData(jvxlData, vertexValues);
        String schemeName = viewer.getPropertyColorScheme();
        thisMesh.colorCommand = "color $" + thisMesh.thisID + " " + getUserColorScheme(schemeName) + " range " + range[0] + " " + range[1];
        thisMesh.isColorSolid = false;
    }

    private String getUserColorScheme(String schemeName) {
        String colors = viewer.getColorSchemeList(schemeName, false);
        return "\"" + (colors.length() == 0 ? schemeName : colors) + "\"";
    }
}
