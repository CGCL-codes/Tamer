package javax.media.j3d;

import java.util.*;
import java.security.*;

/**
 * The CompileState holds information used during a compile.  It is
 * passed to each SceneGraphObject (SGO) during the compile.  Each SGO
 * modifies the CompileState as necessary and passes the CompileState
 * to its children (if any).
 *
 * The CompileState currently has two functions: appearance mapping
 * and shape merging.
 *
 * Appearance mapping maintains a list of the unique appearances seen
 * during the compile.  getAppearance() is used to turn multiple,
 * equivalent and static appearances into a single shared appearance.
 *
 * The shape mergings collects shapes that are potentially mergable
 * during a compile.  The shapes are sorted into a Map of Lists of
 * shapes, using the shape's appearance as the key.  After a subtree
 * is traversed, the shapes are merged and added to the Group.
 */
class CompileState {

    HashMap knownAppearances = new HashMap();

    int numAppearances = 0;

    int numShared = 0;

    int numShapes = 0;

    HashMap shapeLists = null;

    int numMergeSets = 0;

    int numMergeShapes = 0;

    boolean compileVerbose = false;

    static final int BOUNDS_READ = 0x00001;

    static final int GEOMETRY_READ = 0x00002;

    boolean keepTG = false;

    boolean needNormalsTransform = false;

    TransformGroupRetained staticTransform = null;

    GroupRetained parentGroup = null;

    ArrayList transformGroupChildrenList = null;

    ArrayList staticTransformObjects = new ArrayList(1);

    int numTransformGroups = 0;

    int numStaticTransformGroups = 0;

    int numMergedTransformGroups = 0;

    int numGroups = 0;

    int numMergedGroups = 0;

    int numShapesWSharedGeom = 0;

    int numShapesWStaticTG = 0;

    int numLinks = 0;

    int numSwitches = 0;

    int numOrderedGroups = 0;

    int numMorphs = 0;

    CompileState() {
        try {
            compileVerbose = Boolean.getBoolean("javax.media.j3d.compileVerbose");
        } catch (AccessControlException e) {
            compileVerbose = false;
        }
        initShapeMerge();
    }

    /**
     * Returns an unique appearance which equals app.  If appearance does not
     * equal any previously found, the appearance will be added to the known
     * appearances and be returned.  If the apperance equals a previously known
     * appearance, then the prevously known apperance will be returned
     */
    AppearanceRetained getAppearance(AppearanceRetained app) {
        AppearanceRetained retval;
        if (app.map == this) {
            if (app.mapAppearance != null) {
                numShared++;
                return app.mapAppearance;
            }
        }
        if ((retval = (AppearanceRetained) knownAppearances.get(app)) != null) {
            numShared++;
        } else {
            knownAppearances.put(app, app);
            numAppearances++;
            numShared++;
            retval = app;
        }
        app.map = this;
        app.mapAppearance = retval;
        return retval;
    }

    private void initShapeMerge() {
        shapeLists = new HashMap();
    }

    void addShape(Shape3DRetained shape) {
        if (parentGroup != null) {
            Vector list;
            if ((list = (Vector) shapeLists.get(shape.appearance)) == null) {
                list = new Vector();
                shapeLists.put(shape.appearance, list);
            }
            GeometryRetained geometry = null;
            int i = 0;
            while (geometry == null && i < shape.geometryList.size()) {
                geometry = (GeometryRetained) shape.geometryList.get(i);
                i++;
            }
            if (shape.parent instanceof GroupRetained && ((GroupRetained) shape.parent).isStaticChildren() && geometry.geoType < GeometryArrayRetained.GEO_TYPE_RASTER) {
                list.add(shape);
            }
        }
    }

    void printStats() {
        System.err.println("numTransformGroups= " + numTransformGroups);
        System.err.println("numStaticTransformGroups= " + numStaticTransformGroups);
        System.err.println("numMergedTransformGroups= " + numMergedTransformGroups);
        System.err.println("numGroups= " + numGroups);
        System.err.println("numMergedGroups= " + numMergedGroups);
        System.err.println("numShapes= " + numShapes);
        System.err.println("numShapesWStaticTG= " + numShapesWStaticTG);
        System.err.println("numMergeShapes= " + numMergeShapes);
        System.err.println("numMergeSets= " + numMergeSets);
        System.err.println("numLinks= " + numLinks);
        System.err.println("numSwitches= " + numSwitches);
        System.err.println("numOrderedGroups= " + numOrderedGroups);
        System.err.println("numMorphs= " + numMorphs);
    }

    void doShapeMerge() {
        if (shapeLists != null) {
            Collection lists = shapeLists.values();
            Iterator listIterator = lists.iterator();
            Shape3DRetained mergeShape;
            GeometryRetained firstGeo;
            int num = 0;
            int compileFlags = 0;
            while (listIterator.hasNext()) {
                Vector curList = (Vector) listIterator.next();
                int numShapes = curList.size();
                Shape3DRetained[] shapes = new Shape3DRetained[numShapes];
                curList.copyInto(shapes);
                Shape3DRetained[] toBeMergedShapes = new Shape3DRetained[numShapes];
                for (int i = 0; i < numShapes; i++) {
                    if (shapes[i] == null) {
                        continue;
                    }
                    firstGeo = null;
                    num = 0;
                    while (firstGeo == null && num < shapes[i].geometryList.size()) {
                        firstGeo = (GeometryRetained) shapes[i].geometryList.get(num);
                        num++;
                    }
                    if (firstGeo != null && firstGeo instanceof GeometryArrayRetained) {
                        int numMerge = 0;
                        mergeShape = shapes[i];
                        GeometryArrayRetained mergeGeo = (GeometryArrayRetained) firstGeo;
                        toBeMergedShapes[numMerge++] = mergeShape;
                        compileFlags = getCompileFlags(mergeShape);
                        for (int j = i + 1; j < numShapes; j++) {
                            if (shapes[j] == null) {
                                continue;
                            }
                            firstGeo = null;
                            num = 0;
                            while (firstGeo == null && num < shapes[j].geometryList.size()) {
                                firstGeo = (GeometryRetained) shapes[j].geometryList.get(num);
                                num++;
                            }
                            if (firstGeo != null && shapes[j].isEquivalent(mergeShape) && firstGeo.isEquivalenceClass(mergeGeo) && ((GeometryArrayRetained) firstGeo).vertexFormat == mergeGeo.vertexFormat) {
                                toBeMergedShapes[numMerge++] = shapes[j];
                                compileFlags |= getCompileFlags(shapes[j]);
                                shapes[j] = null;
                            }
                        }
                        if (numMerge > 1) {
                            GroupRetained group = (GroupRetained) toBeMergedShapes[0].parent;
                            Shape3DRetained s;
                            for (int n = 0; n < numMerge; n++) {
                                s = toBeMergedShapes[n];
                                boolean found = false;
                                int numChilds = group.numChildren();
                                for (int k = 0; (k < numChilds && !found); k++) {
                                    if (group.getChild(k).retained == s) {
                                        found = true;
                                        group.removeChild(k);
                                    }
                                }
                                if (!found) {
                                    System.err.println("ShapeSet.add(): Can't remove " + "shape from parent, can't find shape!");
                                }
                            }
                            mergeShape = new Shape3DCompileRetained(toBeMergedShapes, numMerge, compileFlags);
                            if (J3dDebug.devPhase && J3dDebug.debug) {
                                if (J3dDebug.doDebug(J3dDebug.compileState, J3dDebug.LEVEL_3)) {
                                    System.err.println("Dest is " + parentGroup);
                                    System.err.println("Compile Shape " + mergeShape);
                                    System.err.println(mergeShape.geometryList.size() + " geoemtryList");
                                    for (int j = 0; j < mergeShape.geometryList.size(); j++) {
                                        GeometryRetained geo = ((GeometryRetained) mergeShape.geometryList.get(j));
                                        if (geo != null) System.err.println("\t Geo_type = " + geo.geoType);
                                    }
                                    System.err.println(numMerge + " Shapes were merged ");
                                    for (int j = 0; j < numMerge; j++) {
                                        System.err.println("\t" + toBeMergedShapes[j]);
                                    }
                                }
                            }
                            mergeShape.setSource(toBeMergedShapes[0].source);
                            numMergeSets++;
                            numMergeShapes += numMerge;
                            parentGroup.addChild((Node) mergeShape.source);
                        }
                    }
                }
            }
        }
        shapeLists.clear();
    }

    int getCompileFlags(Shape3DRetained shape) {
        int cflag = 0;
        if (shape.allowIntersect() || shape.source.getCapability(Shape3D.ALLOW_GEOMETRY_READ) || (shape.boundsAutoCompute && shape.source.getCapability(Shape3D.ALLOW_BOUNDS_READ))) cflag |= GEOMETRY_READ;
        return cflag;
    }
}
