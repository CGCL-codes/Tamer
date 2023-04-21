package org.jnet.viewer;

import javax.vecmath.Point3f;
import javax.vecmath.Matrix3f;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.BitSet;
import java.util.Enumeration;
import org.jnet.g3d.Graphics3D;
import org.jnet.modelset.Edge;
import org.jnet.modelset.ModelSet;
import org.jnet.util.BitSetUtil;
import org.jnet.util.Escape;
import org.jnet.util.Logger;
import org.jnet.util.Parser;
import org.jnet.util.TextFormat;
import java.util.Arrays;

public class StateManager implements Serializable {

    static final long serialVersionUID = 1L;

    public static final int OBJ_BACKGROUND = 0;

    public static final int OBJ_AXIS1 = 1;

    public static final int OBJ_AXIS2 = 2;

    public static final int OBJ_AXIS3 = 3;

    public static final int OBJ_BOUNDBOX = 4;

    public static final int OBJ_UNITCELL = 5;

    public static final int OBJ_FRANK = 6;

    public static final int OBJ_MAX = 7;

    private static final String objectNameList = "background axis1      axis2      axis3      boundbox   unitcell   frank      ";

    static int getObjectIdFromName(String name) {
        if (name == null) return -1;
        int objID = objectNameList.indexOf(name.toLowerCase());
        return (objID < 0 ? objID : objID / 11);
    }

    static String getObjectNameFromId(int objId) {
        if (objId < 0 || objId >= OBJ_MAX) return null;
        return objectNameList.substring(objId * 11, objId * 11 + 11).trim();
    }

    Viewer viewer;

    Hashtable saved = new Hashtable();

    String lastOrientation = "";

    String lastConnections = "";

    String lastSelected = "";

    String lastState = "";

    String lastShape = "";

    String lastCoordinates = "";

    StateManager(Viewer viewer) {
        this.viewer = viewer;
    }

    GlobalSettings getGlobalSettings(GlobalSettings gsOld) {
        GlobalSettings g = new GlobalSettings();
        g.registerAllValues(gsOld);
        return g;
    }

    void clear() {
        viewer.setShowAxes(false);
        viewer.setShowBbcage(false);
        viewer.setShowUnitCell(false);
    }

    void setCrystallographicDefaults() {
        viewer.setAxesModeUnitCell(true);
        viewer.setShowAxes(true);
        viewer.setShowUnitCell(true);
        viewer.setBooleanProperty("perspectiveDepth", false);
    }

    private void setCommonDefaults() {
        viewer.setBooleanProperty("perspectiveDepth", true);
        viewer.setFloatProperty("edgeTolerance", JnetConstants.DEFAULT_EDGE_TOLERANCE);
        viewer.setFloatProperty("minEdgeDistance", JnetConstants.DEFAULT_MIN_EDGE_DISTANCE);
    }

    void setJnetDefaults() {
        setCommonDefaults();
        viewer.setStringProperty("defaultColorScheme", "Jnet");
        viewer.setBooleanProperty("axesOrientationRasmol", false);
        viewer.setBooleanProperty("zeroBasedXyzRasmol", false);
        viewer.setIntProperty("percentVdwNode", JnetConstants.DEFAULT_PERCENT_VDW_NODE);
        viewer.setIntProperty("edgeRadiusMilliAngstroms", JnetConstants.DEFAULT_EDGE_MILLIANGSTROM_RADIUS);
        viewer.setDefaultVdw("Jnet");
    }

    void setRasMolDefaults() {
        setCommonDefaults();
        viewer.setStringProperty("defaultColorScheme", "RasMol");
        viewer.setBooleanProperty("axesOrientationRasmol", true);
        viewer.setBooleanProperty("zeroBasedXyzRasmol", true);
        viewer.setIntProperty("percentVdwNode", 0);
        viewer.setIntProperty("edgeRadiusMilliAngstroms", 1);
        viewer.setDefaultVdw("Rasmol");
    }

    String getStandardLabelFormat() {
        String strLabel;
        int modelCount = viewer.getModelCount();
        if (viewer.getChainCount() > modelCount) strLabel = "[%n]%r:%c.%a"; else if (viewer.getGroupCount() <= modelCount) strLabel = "%e%i"; else strLabel = "[%n]%r.%a";
        if (viewer.getModelCount() > 1) strLabel += "/%M";
        return strLabel;
    }

    String listSavedStates() {
        String names = "";
        Enumeration e = saved.keys();
        while (e.hasMoreElements()) names += "\n" + e.nextElement();
        return names;
    }

    void saveSelection(String saveName, BitSet bsSelected) {
        saveName = lastSelected = "Selected_" + saveName;
        saved.put(saveName, BitSetUtil.copy(bsSelected));
    }

    boolean restoreSelection(String saveName) {
        String name = (saveName.length() > 0 ? "Selected_" + saveName : lastSelected);
        BitSet bsSelected = (BitSet) saved.get(name);
        if (bsSelected == null) {
            viewer.select(new BitSet(), false);
            return false;
        }
        viewer.select(bsSelected, false);
        return true;
    }

    void saveState(String saveName) {
        saveName = lastState = "State_" + saveName;
        saved.put(saveName, viewer.getStateInfo());
    }

    String getSavedState(String saveName) {
        String name = (saveName.length() > 0 ? "State_" + saveName : lastState);
        String script = (String) saved.get(name);
        return (script == null ? "" : script);
    }

    void saveStructure(String saveName) {
        saveName = lastShape = "Shape_" + saveName;
        saved.put(saveName, viewer.getStructureState());
    }

    String getSavedStructure(String saveName) {
        String name = (saveName.length() > 0 ? "Shape_" + saveName : lastShape);
        String script = (String) saved.get(name);
        return (script == null ? "" : script);
    }

    void saveCoordinates(String saveName, BitSet bsSelected) {
        saveName = lastCoordinates = "Coordinates_" + saveName;
        saved.put(saveName, viewer.getCoordinateState(bsSelected));
    }

    String getSavedCoordinates(String saveName) {
        String name = (saveName.length() > 0 ? "Coordinates_" + saveName : lastCoordinates);
        String script = (String) saved.get(name);
        return (script == null ? "" : script);
    }

    Orientation getOrientation() {
        return new Orientation();
    }

    void saveOrientation(String saveName) {
        Orientation o = new Orientation();
        o.saveName = lastOrientation = "Orientation_" + saveName;
        saved.put(o.saveName, o);
    }

    boolean restoreOrientation(String saveName, float timeSeconds, boolean isAll) {
        String name = (saveName.length() > 0 ? "Orientation_" + saveName : lastOrientation);
        Orientation o = (Orientation) saved.get(name);
        if (o == null) return false;
        o.restore(timeSeconds, isAll);
        return true;
    }

    public class Orientation implements Serializable {

        static final long serialVersionUID = 1L;

        String saveName;

        Matrix3f rotationMatrix = new Matrix3f();

        float xTrans, yTrans;

        float zoom, rotationRadius;

        Point3f center = new Point3f();

        Point3f navCenter = new Point3f();

        float xNav = Float.NaN;

        float yNav = Float.NaN;

        float navDepth = Float.NaN;

        boolean windowCenteredFlag;

        boolean navigationMode;

        String moveToText;

        Orientation() {
            viewer.getRotation(rotationMatrix);
            xTrans = viewer.getTranslationXPercent();
            yTrans = viewer.getTranslationYPercent();
            zoom = viewer.getZoomSetting();
            center.set(viewer.getRotationCenter());
            windowCenteredFlag = viewer.isWindowCentered();
            rotationRadius = viewer.getRotationRadius();
            navigationMode = viewer.getNavigationMode();
            moveToText = viewer.getMoveToText(-1);
            if (navigationMode) {
                navCenter = viewer.getNavigationOffset();
                xNav = viewer.getNavigationOffsetPercent('X');
                yNav = viewer.getNavigationOffsetPercent('Y');
                navDepth = viewer.getNavigationDepthPercent();
                navCenter = viewer.getNavigationCenter();
            }
        }

        public String getMoveToText() {
            return moveToText;
        }

        void restore(float timeSeconds, boolean isAll) {
            if (!isAll) {
                viewer.moveTo(timeSeconds, rotationMatrix, null, Float.NaN, Float.NaN, Float.NaN, Float.NaN, null, Float.NaN, Float.NaN, Float.NaN);
                return;
            }
            viewer.setBooleanProperty("windowCentered", windowCenteredFlag);
            viewer.setBooleanProperty("navigationMode", navigationMode);
            viewer.moveTo(timeSeconds, rotationMatrix, center, zoom, xTrans, yTrans, rotationRadius, navCenter, xNav, yNav, navDepth);
        }
    }

    void saveEdges(String saveName) {
        Connections b = new Connections();
        b.saveName = lastConnections = "Edges_" + saveName;
        saved.put(b.saveName, b);
    }

    boolean restoreEdges(String saveName) {
        String name = (saveName.length() > 0 ? "Edges_" + saveName : lastConnections);
        Connections c = (Connections) saved.get(name);
        if (c == null) return false;
        c.restore();
        return true;
    }

    class Connections {

        String saveName;

        int edgeCount;

        Connection[] connections;

        Connections() {
            ModelSet modelSet = viewer.getModelSet();
            if (modelSet == null) return;
            edgeCount = modelSet.getEdgeCount();
            connections = new Connection[edgeCount + 1];
            Edge[] edges = modelSet.getEdges();
            for (int i = edgeCount; --i >= 0; ) {
                Edge b = edges[i];
                connections[i] = new Connection(b.getNodeIndex1(), b.getNodeIndex2(), b.getMad(), b.getColix(), b.getOrder(), b.getShapeVisibilityFlags());
            }
        }

        void restore() {
            ModelSet modelSet = viewer.getModelSet();
            if (modelSet == null) return;
            modelSet.deleteAllEdges();
            for (int i = edgeCount; --i >= 0; ) {
                Connection c = connections[i];
                int nodeCount = modelSet.getNodeCount();
                if (c.nodeIndex1 >= nodeCount || c.nodeIndex2 >= nodeCount) continue;
                Edge b = modelSet.edgeNodes(modelSet.nodes[c.nodeIndex1], modelSet.nodes[c.nodeIndex2], c.order, c.mad, null);
                b.setColix(c.colix);
                b.setShapeVisibilityFlags(c.shapeVisibilityFlags);
            }
            for (int i = edgeCount; --i >= 0; ) modelSet.getEdgeAt(i).setIndex(i);
            viewer.setShapeProperty(JnetConstants.SHAPE_STICKS, "reportAll", null);
        }
    }

    static class Connection {

        int nodeIndex1;

        int nodeIndex2;

        short mad;

        short colix;

        short order;

        int shapeVisibilityFlags;

        Connection(int node1, int node2, short mad, short colix, short order, int shapeVisibilityFlags) {
            nodeIndex1 = node1;
            nodeIndex2 = node2;
            this.mad = mad;
            this.colix = colix;
            this.order = order;
            this.shapeVisibilityFlags = shapeVisibilityFlags;
        }
    }

    static boolean isMeasurementUnit(String units) {
        return Parser.isOneOf(units.toLowerCase(), "angstroms;au;bohr;nanometers;nm;picometers;pm");
    }

    class GlobalSettings implements Serializable {

        static final long serialVersionUID = 1L;

        GlobalSettings() {
        }

        Hashtable listVariables = new Hashtable();

        void clear() {
            Enumeration e = htUserVariables.keys();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                if (key.charAt(0) == '@' || key.startsWith("site_")) htUserVariables.remove(key);
            }
            setParameterValue("_nodepicked", -1);
            setParameterValue("_nodehovered", -1);
            setParameterValue("_pickinfo", "");
            setParameterValue("selectionhalos", false);
            setParameterValue("hidenotselected", false);
            setParameterValue("measurementlabels", measurementLabels = true);
        }

        void setListVariable(String name, Token value) {
            name = name.toLowerCase();
            if (value == null) listVariables.remove(name); else listVariables.put(name, value);
        }

        Object getListVariable(String name, Object value) {
            if (name == null) return value;
            name = name.toLowerCase();
            return (listVariables.containsKey(name) ? listVariables.get(name) : value);
        }

        int ambientPercent = 45;

        int diffusePercent = 84;

        boolean specular = true;

        int specularExponent = 6;

        int specularPercent = 22;

        int specularPower = 40;

        boolean allowEmbeddedScripts = true;

        boolean appendNew = true;

        String appletProxy = "";

        boolean applySymmetryToEdges = false;

        boolean autoEdge = true;

        short edgeRadiusMilliAngstroms = JnetConstants.DEFAULT_EDGE_MILLIANGSTROM_RADIUS;

        float edgeTolerance = JnetConstants.DEFAULT_EDGE_TOLERANCE;

        String defaultLoadScript = "";

        String defaultDirectory = "";

        boolean forceAutoEdge = false;

        char inlineNewlineChar = '|';

        String loadFormat = "http://www.rcsb.org/pdb/files/%FILE.pdb";

        float minEdgeDistance = JnetConstants.DEFAULT_MIN_EDGE_DISTANCE;

        boolean pdbGetHeader = false;

        boolean pdbSequential = false;

        int percentVdwNode = JnetConstants.DEFAULT_PERCENT_VDW_NODE;

        boolean smartAromatic = true;

        boolean zeroBasedXyzRasmol = false;

        /**
     *  these settings are determined when the file is loaded and are
     *  kept even though they might later change. So we list them here
     *  and ALSO let them be defined in the settings. 10.9.98 missed this. 
     *  
     * @return script command
     */
        String getLoadState() {
            StringBuffer str = new StringBuffer();
            appendCmd(str, "set allowEmbeddedScripts false");
            if (allowEmbeddedScripts) setParameterValue("allowEmbeddedScripts", true);
            appendCmd(str, "set autoEdge " + autoEdge);
            appendCmd(str, "set appendNew " + appendNew);
            appendCmd(str, "set appletProxy " + Escape.escape(appletProxy));
            appendCmd(str, "set applySymmetryToEdges " + applySymmetryToEdges);
            if (axesOrientationRasmol) appendCmd(str, "set axesOrientationRasmol true");
            appendCmd(str, "set edgeRadiusMilliAngstroms " + edgeRadiusMilliAngstroms);
            appendCmd(str, "set edgeTolerance " + edgeTolerance);
            appendCmd(str, "set defaultDirectory " + Escape.escape(defaultDirectory));
            appendCmd(str, "set defaultLattice " + Escape.escape(ptDefaultLattice));
            appendCmd(str, "set defaultLoadScript \"\"");
            if (defaultLoadScript.length() > 0) setParameterValue("defaultLoadScript", defaultLoadScript);
            String sMode = viewer.getDefaultVdw(Integer.MIN_VALUE);
            appendCmd(str, "set defaultVDW " + sMode);
            if (sMode.equals("User")) appendCmd(str, viewer.getDefaultVdw(Integer.MAX_VALUE));
            appendCmd(str, "set forceAutoEdge " + forceAutoEdge);
            appendCmd(str, "set loadFormat " + Escape.escape(loadFormat));
            appendCmd(str, "set minEdgeDistance " + minEdgeDistance);
            appendCmd(str, "set pdbSequential " + pdbSequential);
            appendCmd(str, "set pdbGetHeader " + pdbGetHeader);
            appendCmd(str, "set percentVdwNode " + percentVdwNode);
            appendCmd(str, "set smartAromatic " + smartAromatic);
            if (zeroBasedXyzRasmol) appendCmd(str, "set zeroBasedXyzRasmol true");
            return str.toString();
        }

        private final Point3f ptDefaultLattice = new Point3f();

        void setDefaultLattice(Point3f ptLattice) {
            ptDefaultLattice.set(ptLattice);
        }

        Point3f getDefaultLatticePoint() {
            return ptDefaultLattice;
        }

        int[] getDefaultLatticeArray() {
            int[] A = new int[4];
            A[1] = (int) ptDefaultLattice.x;
            A[2] = (int) ptDefaultLattice.y;
            A[3] = (int) ptDefaultLattice.z;
            return A;
        }

        boolean allowRotateSelected = false;

        boolean perspectiveDepth = true;

        int stereoDegrees = 5;

        float visualRange = 5f;

        boolean solventOn = false;

        String defaultAngleLabel = "%VALUE %UNITS";

        String defaultDistanceLabel = "%VALUE %UNITS";

        String defaultTorsionLabel = "%VALUE %UNITS";

        boolean justifyMeasurements = false;

        boolean measureAllModels = false;

        int minimizationSteps = 100;

        boolean minimizationRefresh = true;

        float minimizationCriterion = 0.001f;

        int layoutSteps = 0;

        boolean layoutRefresh = true;

        String layoutType = "spring";

        boolean stretchMode = false;

        boolean antialiasDisplay = false;

        boolean antialiasImages = true;

        boolean antialiasTranslucent = true;

        boolean displayCellParameters = true;

        boolean dotsSelectedOnly = false;

        boolean dotSurface = true;

        int dotDensity = 3;

        boolean dynamicMeasurements = false;

        boolean greyscaleRendering = false;

        boolean isosurfacePropertySmoothing = true;

        boolean showHiddenSelectionHalos = false;

        boolean showMeasurements = true;

        boolean zoomLarge = true;

        boolean zShade = false;

        String backgroundImageFileName;

        boolean edgeModeOr = false;

        boolean hedgesBackbone = false;

        boolean hedgesSolid = false;

        byte modeMultipleEdge = JnetConstants.MULTIEDGE_NOTSMALL;

        boolean showHydrogens = true;

        boolean showMultipleEdges = true;

        boolean ssedgesBackbone = false;

        boolean cartoonRockets = false;

        boolean chainCaseSensitive = false;

        int hermiteLevel = 0;

        boolean highResolutionFlag = false;

        boolean rangeSelected = false;

        boolean rasmolHydrogenSetting = true;

        boolean rasmolHeteroSetting = true;

        int ribbonAspectRatio = 16;

        boolean ribbonBorder = false;

        boolean rocketBarrels = false;

        float sheetSmoothing = 1;

        boolean traceAlpha = true;

        int animationFps = 10;

        boolean autoFps = false;

        boolean axesOrientationRasmol = false;

        int axesMode = JnetConstants.AXES_MODE_BOUNDBOX;

        float axesScale = 2;

        float cameraDepth = 3.0f;

        String dataSeparator = "~~~";

        boolean debugScript = false;

        float defaultDrawArrowScale = 0.5f;

        float defaultTranslucent = 0.5f;

        int delayMaximumMs = 0;

        float dipoleScale = 1.0f;

        boolean disablePopupMenu = false;

        boolean drawPicking = false;

        boolean edgeHovering = true;

        boolean edgePicking = false;

        boolean nodePicking = true;

        String helpPath = JnetConstants.DEFAULT_HELP_PATH;

        boolean fontScaling = false;

        boolean hideNameInPopup = false;

        int hoverDelayMs = 110;

        int animationDelayMs = 100;

        boolean measurementLabels = true;

        boolean messageStyleChime = false;

        int pickingSpinRate = 10;

        String pickLabel = "";

        float pointGroupDistanceTolerance = 0.2f;

        float pointGroupLinearTolerance = 8.0f;

        String propertyColorScheme = "roygb";

        String quaternionFrame = "c";

        float solventProbeRadius = 1.2f;

        int scriptDelay = 0;

        boolean selectAllModels = true;

        boolean statusReporting = true;

        int strandCountForStrands = 5;

        int strandCountForMeshRibbon = 7;

        boolean useNumberLocalization = true;

        float vectorScale = 1f;

        float vibrationPeriod = 1f;

        float vibrationScale = 1f;

        boolean wireframeRotation = false;

        boolean hideNavigationPoint = false;

        boolean navigationMode = false;

        boolean navigationPeriodic = false;

        float navigationSpeed = 5;

        boolean showNavigationPointAlways = false;

        String stereoState = null;

        int[] objColors = new int[OBJ_MAX];

        boolean[] objStateOn = new boolean[OBJ_MAX];

        int[] objMad = new int[OBJ_MAX];

        boolean ellipsoidAxes = false;

        boolean ellipsoidDots = false;

        boolean ellipsoidArcs = false;

        boolean ellipsoidFill = false;

        boolean ellipsoidBall = true;

        int ellipsoidDotCount = 200;

        float ellipsoidAxisDiameter = 0.02f;

        String getWindowState(StringBuffer sfunc) {
            StringBuffer str = new StringBuffer();
            if (sfunc != null) {
                sfunc.append("  initialize;\n  set refreshing false;\n  _setWindowState;\n");
                str.append("\nfunction _setWindowState();\n");
            }
            str.append("# height " + viewer.getScreenHeight() + ";\n# width " + viewer.getScreenWidth() + ";\n");
            appendCmd(str, "stateVersion = " + getParameter("_version"));
            for (int i = 0; i < OBJ_MAX; i++) if (objColors[i] != 0) appendCmd(str, getObjectNameFromId(i) + "Color = \"" + Escape.escapeColor(objColors[i]) + '"');
            if (backgroundImageFileName != null) appendCmd(str, "background IMAGE /*file*/" + Escape.escape(backgroundImageFileName));
            str.append(getSpecularState());
            if (stereoState != null) appendCmd(str, "stereo" + stereoState);
            appendCmd(str, "statusReporting  = " + statusReporting);
            if (sfunc != null) str.append("end function;\n\n");
            return str.toString();
        }

        String getSpecularState() {
            StringBuffer str = new StringBuffer("");
            appendCmd(str, "ambientPercent = " + Graphics3D.getAmbientPercent());
            appendCmd(str, "diffusePercent = " + Graphics3D.getDiffusePercent());
            appendCmd(str, "specular = " + Graphics3D.getSpecular());
            appendCmd(str, "specularPercent = " + Graphics3D.getSpecularPercent());
            appendCmd(str, "specularPower = " + Graphics3D.getSpecularPower());
            appendCmd(str, "specularExponent = " + Graphics3D.getSpecularExponent());
            return str.toString();
        }

        boolean testFlag1 = false;

        boolean testFlag2 = false;

        boolean testFlag3 = false;

        boolean testFlag4 = false;

        private String measureDistanceUnits = "nanometers";

        void setMeasureDistanceUnits(String units) {
            if (units.equalsIgnoreCase("angstroms")) measureDistanceUnits = "angstroms"; else if (units.equalsIgnoreCase("nanometers") || units.equalsIgnoreCase("nm")) measureDistanceUnits = "nanometers"; else if (units.equalsIgnoreCase("picometers") || units.equalsIgnoreCase("pm")) measureDistanceUnits = "picometers"; else if (units.equalsIgnoreCase("bohr") || units.equalsIgnoreCase("au")) measureDistanceUnits = "au";
            setParameterValue("measurementUnits", measureDistanceUnits);
        }

        String getMeasureDistanceUnits() {
            return measureDistanceUnits;
        }

        Hashtable htParameterValues;

        Hashtable htPropertyFlags;

        Hashtable htPropertyFlagsRemoved;

        static final String unreportedProperties = ";ambientpercent;animationfps" + ";stretchmode" + ";antialiasdisplay;antialiasimages;antialiastranslucent;appendnew;axescolor" + ";axesposition;axesmolecular;axesorientationrasmol;axesunitcell;axeswindow;axis1color;axis2color" + ";axis3color;backgroundcolor;backgroundmodel;edgesymmetrynodes;boundboxcolor;cameradepth" + ";debugscript;defaultlatttice;defaults;diffusepercent;exportdrivers" + ";fontscaling;language;loglevel;layoutsteps;layouttype;layoutrefresh;measureStyleChime" + ";minimizationsteps;minimizationrefresh;minimizationcriterion;navigationmode" + ";perspectivedepth;visualrange;perspectivemodel;refreshing;rotationradius" + ";showaxes;showaxis1;showaxis2;showaxis3;showboundbox;showfrank;showunitcell" + ";slabenabled;specular;specularexponent;specularpercent;specularpower;stateversion" + ";statusreporting;stereo;stereostate" + ";unitcellcolor;windowcentered;zerobasedxyzrasmol;zoomenabled;" + ";scriptqueue;scriptreportinglevel;syncscript;syncmouse;currentlocalpath" + ";ambient;edges;colorrasmol;diffuse;frank;hetero;hidenotselected" + ";hoverlabel;hydrogen;languagetranslation;measurementunits;navigationdepth;navigationslab" + ";picking;pickingstyle;propertycolorschemeoverload;radius;rgbblue;rgbgreen;rgbred" + ";scaleangstromsperinch;selectionhalos;showscript;showselections;solvent;strandcount" + ";spinx;spiny;spinz;spinfps;layoutcallback" + ";animframecallback;echocallback;evalcallback;loadstructcallback" + ";measurecallback;messagecallback;minimizationcallback;hovercallback" + ";resizecallback;pickcallback;scriptcallback;synccallback;undo;";

        boolean isJnetVariable(String key) {
            return key.charAt(0) == '_' || htParameterValues.containsKey(key = key.toLowerCase()) || htPropertyFlags.containsKey(key) || unreportedProperties.indexOf(";" + key + ";") >= 0;
        }

        private void resetParameterStringValue(String name, GlobalSettings g) {
            setParameterValue(name, g == null ? "" : (String) g.getParameter(name));
        }

        void setParameterValue(String name, boolean value) {
            name = name.toLowerCase();
            if (htParameterValues.containsKey(name)) return;
            htPropertyFlags.put(name, value ? Boolean.TRUE : Boolean.FALSE);
        }

        void setParameterValue(String name, int value) {
            name = name.toLowerCase();
            if (htPropertyFlags.containsKey(name)) return;
            htParameterValues.put(name, new Integer(value));
        }

        void setParameterValue(String name, float value) {
            name = name.toLowerCase();
            if (Float.isNaN(value)) {
                htParameterValues.remove(name);
                htPropertyFlags.remove(name);
                return;
            }
            if (htPropertyFlags.containsKey(name)) return;
            htParameterValues.put(name, new Float(value));
        }

        void setParameterValue(String name, String value) {
            name = name.toLowerCase();
            if (value == null || htPropertyFlags.containsKey(name)) return;
            htParameterValues.put(name, value);
        }

        void removeJnetParameter(String key) {
            if (htPropertyFlags.containsKey(key)) {
                htPropertyFlags.remove(key);
                if (!htPropertyFlagsRemoved.containsKey(key)) htPropertyFlagsRemoved.put(key, Boolean.FALSE);
                return;
            }
            if (htParameterValues.containsKey(key)) htParameterValues.remove(key);
        }

        Hashtable htCollapsed = new Hashtable();

        Hashtable getCollapseHashtable() {
            return htCollapsed;
        }

        Hashtable htUserVariables = new Hashtable();

        void setUserVariable(String key, Token value) {
            key = key.toLowerCase();
            if (value == null) {
                if (key.equals("all") || key.equals("variables")) {
                    htUserVariables.clear();
                    Logger.info("all user-defined variables deleted");
                } else if (htUserVariables.containsKey(key)) {
                    Logger.info("variable " + key + " deleted");
                    htUserVariables.remove(key);
                }
                return;
            }
            htUserVariables.put(key, value);
        }

        void removeUserVariable(String key) {
            htUserVariables.remove(key);
        }

        Object getUserParameterValue(String key) {
            return htUserVariables.get(key);
        }

        String getParameterEscaped(String name, int nMax) {
            name = name.toLowerCase();
            if (htParameterValues.containsKey(name)) {
                Object v = htParameterValues.get(name);
                String sv = escapeVariable(name, v);
                if (nMax > 0 && sv.length() > nMax) sv = sv.substring(0, nMax) + "\n#...(" + sv.length() + " bytes -- use SHOW " + name + " or MESSAGE @" + name + " to view)";
                return sv;
            }
            if (htPropertyFlags.containsKey(name)) return htPropertyFlags.get(name).toString();
            if (htUserVariables.containsKey(name)) return escapeUserVariable(name);
            if (htPropertyFlagsRemoved.containsKey(name)) return "false";
            return "<not set>";
        }

        private String escapeUserVariable(String name) {
            Token token = (Token) htUserVariables.get(name);
            switch(token.tok) {
                case Token.on:
                    return "true";
                case Token.off:
                    return "false";
                case Token.integer:
                    return "" + token.intValue;
                default:
                    return escapeVariable(name, token.value);
            }
        }

        Object getParameter(String name) {
            name = name.toLowerCase();
            if (name.equals("_memory")) {
                Runtime runtime = Runtime.getRuntime();
                float bTotal = runtime.totalMemory() / 1000000f;
                float bFree = runtime.freeMemory() / 1000000f;
                String value = TextFormat.formatDecimal(bTotal - bFree, 1) + "/" + TextFormat.formatDecimal(bTotal, 1);
                htParameterValues.put("_memory", value);
            }
            if (htParameterValues.containsKey(name)) return htParameterValues.get(name);
            if (htPropertyFlags.containsKey(name)) return htPropertyFlags.get(name);
            if (htPropertyFlagsRemoved.containsKey(name)) return Boolean.FALSE;
            if (htUserVariables.containsKey(name)) {
                return Token.oValue((Token) htUserVariables.get(name));
            }
            return "";
        }

        String getAllSettings(String prefix) {
            StringBuffer commands = new StringBuffer("");
            Enumeration e;
            String key;
            String[] list = new String[htPropertyFlags.size() + htParameterValues.size()];
            int n = 0;
            String _prefix = "_" + prefix;
            e = htPropertyFlags.keys();
            while (e.hasMoreElements()) {
                key = (String) e.nextElement();
                if (prefix == null || key.indexOf(prefix) == 0 || key.indexOf(_prefix) == 0) list[n++] = (key.indexOf("_") == 0 ? key + " = " : "set " + key + " ") + htPropertyFlags.get(key);
            }
            e = htParameterValues.keys();
            while (e.hasMoreElements()) {
                key = (String) e.nextElement();
                if (key.charAt(0) != '@' && (prefix == null || key.indexOf(prefix) == 0 || key.indexOf(_prefix) == 0)) {
                    Object value = htParameterValues.get(key);
                    if (value instanceof String) value = Escape.escape((String) value);
                    list[n++] = (key.indexOf("_") == 0 ? key + " = " : "set " + key + " ") + value;
                }
            }
            Arrays.sort(list, 0, n);
            for (int i = 0; i < n; i++) if (list[i] != null) appendCmd(commands, list[i]);
            commands.append("\n");
            return commands.toString();
        }

        String getState(StringBuffer sfunc) {
            int n = 0;
            String[] list = new String[htPropertyFlags.size() + htParameterValues.size()];
            StringBuffer commands = new StringBuffer();
            if (sfunc != null) {
                sfunc.append("  _setVariableState;\n");
                commands.append("function _setVariableState();\n\n");
            }
            Enumeration e;
            String key;
            e = htPropertyFlags.keys();
            while (e.hasMoreElements()) {
                key = (String) e.nextElement();
                if (doReportProperty(key)) list[n++] = "set " + key + " " + htPropertyFlags.get(key);
            }
            e = htParameterValues.keys();
            while (e.hasMoreElements()) {
                key = (String) e.nextElement();
                String name = key;
                if (key.charAt(0) != '@' && doReportProperty(key)) {
                    Object value = htParameterValues.get(key);
                    if (key.charAt(0) == '=') {
                        key = key.substring(1);
                    } else {
                        if (key.indexOf("default") == 0) key = " set " + key; else key = "set " + key;
                        value = escapeVariable(name, value);
                    }
                    list[n++] = key + " " + value;
                }
            }
            switch(axesMode) {
                case JnetConstants.AXES_MODE_UNITCELL:
                    list[n++] = "set axes unitcell";
                    break;
                case JnetConstants.AXES_MODE_BOUNDBOX:
                    list[n++] = "set axes window";
                    break;
                default:
                    list[n++] = "set axes molecular";
            }
            e = htParameterValues.keys();
            while (e.hasMoreElements()) {
                key = (String) e.nextElement();
                if (key.charAt(0) == '@') list[n++] = key + " " + htParameterValues.get(key);
            }
            Arrays.sort(list, 0, n);
            for (int i = 0; i < n; i++) if (list[i] != null) appendCmd(commands, list[i]);
            commands.append("\n#user-defined variables; \n");
            e = htUserVariables.keys();
            n = 0;
            list = new String[htUserVariables.size()];
            while (e.hasMoreElements()) list[n++] = (key = (String) e.nextElement()) + (key.charAt(0) == '@' ? " " + Token.sValue((Token) htUserVariables.get(key)) : " = " + escapeUserVariable(key));
            Arrays.sort(list, 0, n);
            for (int i = 0; i < n; i++) if (list[i] != null) appendCmd(commands, list[i]);
            if (n == 0) commands.append("# --none--;\n");
            viewer.loadShape(JnetConstants.SHAPE_LABELS);
            commands.append(viewer.getShapeProperty(JnetConstants.SHAPE_LABELS, "defaultState"));
            if (sfunc != null) commands.append("\nend function;\n\n");
            return commands.toString();
        }

        private boolean doReportProperty(String name) {
            return (name.charAt(0) != '_' && unreportedProperties.indexOf(";" + name + ";") < 0);
        }

        private String escapeVariable(String name, Object value) {
            if (!(value instanceof String)) return Escape.escape(value);
            Token var = (Token) getListVariable(name, (Token) null);
            if (var == null) return Escape.escape(value);
            return Escape.escape((String[]) var.value);
        }

        void registerAllValues(GlobalSettings g) {
            htParameterValues = new Hashtable();
            htPropertyFlags = new Hashtable();
            htPropertyFlagsRemoved = new Hashtable();
            if (g != null) {
                debugScript = g.debugScript;
                disablePopupMenu = g.disablePopupMenu;
                messageStyleChime = g.messageStyleChime;
                zShade = g.zShade;
            }
            resetParameterStringValue("animFrameCallback", g);
            resetParameterStringValue("echoCallback", g);
            resetParameterStringValue("evalCallback", g);
            resetParameterStringValue("hoverCallback", g);
            resetParameterStringValue("loadStructCallback", g);
            resetParameterStringValue("measureCallback", g);
            resetParameterStringValue("messageCallback", g);
            resetParameterStringValue("minimizationCallback", g);
            resetParameterStringValue("pickCallback", g);
            resetParameterStringValue("resizeCallback", g);
            resetParameterStringValue("scriptCallback", g);
            resetParameterStringValue("syncCallback", g);
            setParameterValue("hoverLabel", "");
            setParameterValue("rotationRadius", 0);
            setParameterValue("scriptqueue", true);
            setParameterValue("_version", 0);
            setParameterValue("stateversion", 0);
            setParameterValue("allowEmbeddedScripts", allowEmbeddedScripts);
            setParameterValue("allowRotateSelected", allowRotateSelected);
            setParameterValue("ambientPercent", ambientPercent);
            setParameterValue("animationFps", animationFps);
            setParameterValue("antialiasImages", antialiasImages);
            setParameterValue("antialiasDisplay", antialiasDisplay);
            setParameterValue("stretchMode", stretchMode);
            setParameterValue("antialiasTranslucent", antialiasTranslucent);
            setParameterValue("appendNew", appendNew);
            setParameterValue("appletProxy", appletProxy);
            setParameterValue("applySymmetryToEdges", applySymmetryToEdges);
            setParameterValue("nodePicking", nodePicking);
            setParameterValue("autoEdge", autoEdge);
            setParameterValue("autoFps", autoFps);
            setParameterValue("axesMode", axesMode);
            setParameterValue("axesScale", axesScale);
            setParameterValue("axesWindow", true);
            setParameterValue("axesMolecular", false);
            setParameterValue("axesPosition", false);
            setParameterValue("axesUnitcell", false);
            setParameterValue("axesOrientationRasmol", axesOrientationRasmol);
            setParameterValue("backgroundModel", 0);
            setParameterValue("edgeModeOr", edgeModeOr);
            setParameterValue("edgePicking", edgePicking);
            setParameterValue("edgeRadiusMilliAngstroms", edgeRadiusMilliAngstroms);
            setParameterValue("edgeTolerance", edgeTolerance);
            setParameterValue("cameraDepth", cameraDepth);
            setParameterValue("cartoonRockets", cartoonRockets);
            setParameterValue("chainCaseSensitive", chainCaseSensitive);
            setParameterValue("colorRasmol", false);
            setParameterValue("currentLocalPath", "");
            setParameterValue("dataSeparator", dataSeparator);
            setParameterValue("debugScript", debugScript);
            setParameterValue("defaultLattice", "{0 0 0}");
            setParameterValue("defaultAngleLabel", defaultAngleLabel);
            setParameterValue("defaultColorScheme", "Jnet");
            setParameterValue("defaultDrawArrowScale", defaultDrawArrowScale);
            setParameterValue("defaultDirectory", defaultDirectory);
            setParameterValue("defaultDistanceLabel", defaultDistanceLabel);
            setParameterValue("defaultLoadScript", defaultLoadScript);
            setParameterValue("defaults", "Jnet");
            setParameterValue("defaultVDW", "Jnet");
            setParameterValue("defaultTorsionLabel", defaultTorsionLabel);
            setParameterValue("defaultTranslucent", defaultTranslucent);
            setParameterValue("delayMaximumMs", delayMaximumMs);
            setParameterValue("diffusePercent", diffusePercent);
            setParameterValue("dipoleScale", dipoleScale);
            setParameterValue("disablePopupMenu", disablePopupMenu);
            setParameterValue("displayCellParameters", displayCellParameters);
            setParameterValue("dotDensity", dotDensity);
            setParameterValue("dotsSelectedOnly", dotsSelectedOnly);
            setParameterValue("dotSurface", dotSurface);
            setParameterValue("drawHover", false);
            setParameterValue("drawPicking", drawPicking);
            setParameterValue("dynamicMeasurements", dynamicMeasurements);
            setParameterValue("ellipsoidArcs", ellipsoidArcs);
            setParameterValue("ellipsoidAxes", ellipsoidAxes);
            setParameterValue("ellipsoidAxisDiameter", ellipsoidAxisDiameter);
            setParameterValue("ellipsoidBall", ellipsoidBall);
            setParameterValue("ellipsoidDotCount", ellipsoidDotCount);
            setParameterValue("ellipsoidDots", ellipsoidDots);
            setParameterValue("ellipsoidFill", ellipsoidFill);
            setParameterValue("exportDrivers", JnetConstants.EXPORT_DRIVER_LIST);
            setParameterValue("fontScaling", fontScaling);
            setParameterValue("forceAutoEdge", forceAutoEdge);
            setParameterValue("greyscaleRendering", greyscaleRendering);
            setParameterValue("hedgesBackbone", hedgesBackbone);
            setParameterValue("hedgesSolid", hedgesSolid);
            setParameterValue("helpPath", helpPath);
            setParameterValue("hermiteLevel", hermiteLevel);
            setParameterValue("hideNameInPopup", hideNameInPopup);
            setParameterValue("hideNavigationPoint", hideNavigationPoint);
            setParameterValue("hideNotSelected", false);
            setParameterValue("highResolution", highResolutionFlag);
            setParameterValue("historyLevel", 0);
            setParameterValue("hoverDelay", hoverDelayMs / 1000f);
            setParameterValue("animationDelay", animationDelayMs / 1000f);
            setParameterValue("isosurfacePropertySmoothing", isosurfacePropertySmoothing);
            setParameterValue("justifyMeasurements", justifyMeasurements);
            setParameterValue("loadFormat", loadFormat);
            setParameterValue("layoutSteps", layoutSteps);
            setParameterValue("layoutRefresh", layoutRefresh);
            setParameterValue("layoutType", layoutType);
            setParameterValue("measureAllModels", measureAllModels);
            setParameterValue("measurementLabels", measurementLabels = true);
            setParameterValue("measurementUnits", measureDistanceUnits);
            setParameterValue("messageStyleChime", messageStyleChime);
            setParameterValue("minEdgeDistance", minEdgeDistance);
            setParameterValue("minimizationSteps", minimizationSteps);
            setParameterValue("minimizationRefresh", minimizationRefresh);
            setParameterValue("minimizationCriterion", minimizationCriterion);
            setParameterValue("navigationMode", navigationMode);
            setParameterValue("navigationPeriodic", navigationPeriodic);
            setParameterValue("navigationDepth", 0);
            setParameterValue("navigationSlab", 0);
            setParameterValue("navigationSpeed", navigationSpeed);
            setParameterValue("pdbGetHeader", pdbGetHeader);
            setParameterValue("pdbSequential", pdbSequential);
            setParameterValue("perspectiveModel", 11);
            setParameterValue("perspectiveDepth", perspectiveDepth);
            setParameterValue("percentVdwNode", percentVdwNode);
            setParameterValue("picking", "ident");
            setParameterValue("pickingSpinRate", pickingSpinRate);
            setParameterValue("pickingStyle", "toggle");
            setParameterValue("pickLabel", pickLabel);
            setParameterValue("pointGroupLinearTolerance", pointGroupLinearTolerance);
            setParameterValue("pointGroupDistanceTolerance", pointGroupDistanceTolerance);
            setParameterValue("propertyColorScheme", propertyColorScheme);
            setParameterValue("propertyNodeNumberColumnCount", 0);
            setParameterValue("propertyNodeNumberField", 0);
            setParameterValue("propertyDataColumnCount", 0);
            setParameterValue("propertyDataField", 0);
            setParameterValue("quaternionFrame", quaternionFrame);
            setParameterValue("rangeSelected", rangeSelected);
            setParameterValue("refreshing", true);
            setParameterValue("ribbonAspectRatio", ribbonAspectRatio);
            setParameterValue("ribbonBorder", ribbonBorder);
            setParameterValue("rocketBarrels", rocketBarrels);
            setParameterValue("scaleAngstromsPerInch", 0);
            setParameterValue("scriptReportingLevel", 0);
            setParameterValue("selectAllModels", selectAllModels);
            setParameterValue("selectionHalos", false);
            setParameterValue("selectHetero", rasmolHeteroSetting);
            setParameterValue("selectHydrogen", rasmolHydrogenSetting);
            setParameterValue("sheetSmoothing", sheetSmoothing);
            setParameterValue("showaxes", false);
            setParameterValue("showboundbox", false);
            setParameterValue("showfrank", false);
            setParameterValue("showHiddenSelectionHalos", showHiddenSelectionHalos);
            setParameterValue("showHydrogens", showHydrogens);
            setParameterValue("showMeasurements", showMeasurements);
            setParameterValue("showMultipleEdges", showMultipleEdges);
            setParameterValue("showNavigationPointAlways", showNavigationPointAlways);
            setParameterValue("showScript", scriptDelay);
            setParameterValue("showUnitcell", false);
            setParameterValue("slabEnabled", false);
            setParameterValue("smartAromatic", smartAromatic);
            setParameterValue("solventProbe", solventOn);
            setParameterValue("solventProbeRadius", solventProbeRadius);
            setParameterValue("specular", specular);
            setParameterValue("specularExponent", specularExponent);
            setParameterValue("specularPercent", specularPercent);
            setParameterValue("specularPower", specularPower);
            setParameterValue("spinX", 0);
            setParameterValue("spinY", 30);
            setParameterValue("spinZ", 0);
            setParameterValue("spinFps", 30);
            setParameterValue("ssedgesBackbone", ssedgesBackbone);
            setParameterValue("stereoDegrees", stereoDegrees);
            setParameterValue("statusReporting", statusReporting);
            setParameterValue("strandCount", strandCountForStrands);
            setParameterValue("strandCountForStrands", strandCountForStrands);
            setParameterValue("strandCountForMeshRibbon", strandCountForMeshRibbon);
            setParameterValue("syncMouse", false);
            setParameterValue("syncScript", false);
            setParameterValue("testFlag1", testFlag1);
            setParameterValue("testFlag2", testFlag2);
            setParameterValue("testFlag3", testFlag3);
            setParameterValue("testFlag4", testFlag4);
            setParameterValue("traceAlpha", traceAlpha);
            setParameterValue("undo", true);
            setParameterValue("useNumberLocalization", useNumberLocalization);
            setParameterValue("vectorScale", vectorScale);
            setParameterValue("vibrationPeriod", vibrationPeriod);
            setParameterValue("vibrationScale", vibrationScale);
            setParameterValue("visualRange", visualRange);
            setParameterValue("windowCentered", true);
            setParameterValue("wireframeRotation", wireframeRotation);
            setParameterValue("zoomEnabled", true);
            setParameterValue("zoomLarge", zoomLarge);
            setParameterValue("zShade", zShade);
            setParameterValue("zeroBasedXyzRasmol", zeroBasedXyzRasmol);
        }
    }

    public static void setStateInfo(Hashtable ht, int i1, int i2, String key) {
        BitSet bs;
        if (ht.containsKey(key)) {
            bs = (BitSet) ht.get(key);
        } else {
            bs = new BitSet();
            ht.put(key, bs);
        }
        for (int i = i1; i <= i2; i++) bs.set(i);
    }

    public static String getCommands(Hashtable ht) {
        return getCommands(ht, null, -1, "select");
    }

    public static String getCommands(Hashtable htDefine, Hashtable htMore, int nAll) {
        return getCommands(htDefine, htMore, nAll, "select");
    }

    public static String getCommands(Hashtable htDefine, Hashtable htMore, int nAll, String selectCmd) {
        StringBuffer s = new StringBuffer();
        String setPrev = getCommands(htDefine, s, null, nAll, selectCmd);
        if (htMore != null) getCommands(htMore, s, setPrev, nAll, "select");
        return s.toString();
    }

    public static String getCommands(Hashtable ht, StringBuffer s, String setPrev, int nAll, String selectCmd) {
        if (ht == null) return "";
        String strAll = "({0:" + (nAll - 1) + "})";
        Enumeration e = ht.keys();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            String set = Escape.escape((BitSet) ht.get(key));
            if (set.length() < 5) continue;
            set = selectCmd + " " + (set.equals(strAll) && false ? "*" : set);
            if (!set.equals(setPrev)) appendCmd(s, set);
            setPrev = set;
            if (key.indexOf("-") != 0) appendCmd(s, key);
        }
        return setPrev;
    }

    public static void appendCmd(StringBuffer s, String cmd) {
        if (cmd.length() == 0) return;
        s.append("  ").append(cmd).append(";\n");
    }
}
