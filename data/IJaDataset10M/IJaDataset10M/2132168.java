package de.grogra.blocks;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import javax.vecmath.Tuple2f;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3d;
import de.grogra.graph.Instantiator;
import de.grogra.graph.impl.Edge;
import de.grogra.graph.impl.Node;
import de.grogra.imp.View;
import de.grogra.imp3d.objects.GlobalTransformation;
import de.grogra.imp3d.objects.Mark;
import de.grogra.imp3d.objects.NURBSSurface;
import de.grogra.imp3d.objects.Null;
import de.grogra.imp3d.objects.Vertex;
import de.grogra.imp3d.objects.VertexSequence;
import de.grogra.turtle.D;
import de.grogra.turtle.F;
import de.grogra.turtle.M;
import de.grogra.turtle.RH;
import de.grogra.turtle.RL;
import de.grogra.turtle.Rotate;
import de.grogra.turtle.Scale;
import de.grogra.math.Arc;
import de.grogra.math.BSpline;
import de.grogra.math.BSplineCurve;
import de.grogra.math.Id;
import de.grogra.math.ProfileSweep;
import de.grogra.math.RegularPolygon;
import de.grogra.math.SkinnedSurface;
import de.grogra.math.SplineFunction;
import de.grogra.math.TMatrix4d;
import de.grogra.math.Transform3D;
import de.grogra.persistence.PersistenceField;
import de.grogra.persistence.Transaction;
import de.grogra.rgg.Library;
import de.grogra.rgg.model.Instantiation;
import de.grogra.util.EnumerationType;
import de.grogra.vecmath.Matrix34d;
import de.grogra.xl.lang.FloatToFloat;

public class Tree extends NullWithShaderNode implements de.grogra.xl.modules.Instantiator<Instantiation> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4L;

    private static final EnumerationType BRANCHES_TYPE = new EnumerationType("branchesType", Attributes.I18N, 8);

    CustomFunction stemLength = new CustomFunction(20, 0, 200);

    CustomFunction trunkScale = new CustomFunction(1, 0, 2);

    boolean spline = false;

    BSplineCurve trajectory = (BSplineCurve) BlockTools.getObject("/objects/math/curves/TreeCurves/trajectory", new Arc(0, 0.1f, 1));

    FloatToFloat shape = (FloatToFloat) BlockTools.getObject("/objects/math/functions/TreeFunctions/shape", new Id());

    boolean top = true;

    BSplineCurve crookednessCurve = (BSplineCurve) BlockTools.getObject("/objects/math/curves/TreeCurves/crookedness", new Arc(0, 0.1f, 1));

    CustomFunction crookednessAmount = new CustomFunction(2, 0, 10);

    FloatToFloat crookednessIntensity = (FloatToFloat) BlockTools.getObject("/objects/math/functions/TreeFunctions/intensity", new Id());

    BSplineCurve deviateCurve = (BSplineCurve) BlockTools.getObject("/objects/math/curves/TreeCurves/deviateC", new Arc(0, 0.1f, 1));

    FloatToFloat deviate = (FloatToFloat) BlockTools.getObject("/objects/math/functions/TreeFunctions/deviate", new Id());

    FloatToFloat screw = (FloatToFloat) BlockTools.getObject("/objects/math/functions/TreeFunctions/screw", new Id());

    CustomFunction branchesNumber = new CustomFunction(10, 2, 100);

    int arrangement = 0;

    FloatToFloat branchesDistribution = (FloatToFloat) BlockTools.getObject("/objects/math/functions/TreeFunctions/distribution", new Id());

    FloatToFloat branchesGrowthScale = (FloatToFloat) BlockTools.getObject("/objects/math/functions/TreeFunctions/growthscale", new Id());

    FloatToFloat branchesGeometricScale = (FloatToFloat) BlockTools.getObject("/objects/math/functions/TreeFunctions/geomscale", new Id());

    FloatToFloat branchesAngle = (FloatToFloat) BlockTools.getObject("/objects/math/functions/TreeFunctions/angle", new Id());

    FloatToFloat branchesDense = (FloatToFloat) BlockTools.getObject("/objects/math/functions/TreeFunctions/dense", new Id());

    BSplineCurve profile = new RegularPolygon();

    boolean geometry = true;

    TreeLOD lod = new TreeLOD();

    LocationParameterBase locationParameter = new LocationParameterBase();

    boolean initAll = false;

    private Point2f ids = new Point2f(0, 0);

    ;

    private Float densityValue = new Float(0);

    private Tuple2f height = new Point2f(0, 0);

    private Tuple3f nutrientsValues = new Point3f(0, 0, 0);

    private int childId = 0;

    private int branches_NumberI_alt = -1;

    private float[] distribution;

    private static final RL RL90 = new RL(90);

    private static final RL RL_90 = new RL(-90);

    public Tree() {
        super();
        super.setLayer(1);
        initFunctionsToHermit();
    }

    public Tree(float number) {
        super();
        super.setLayer(1);
        branchesNumber.setFunction(number);
        initFunctionsToHermit();
    }

    public Tree(String number) {
        super();
        super.setLayer(1);
        branchesNumber.setFunction(number);
        initFunctionsToHermit();
    }

    private void initAttributes() {
        stemLength = new CustomFunction(20, 0, 200);
        trunkScale = new CustomFunction(1, 0, 1);
        spline = false;
        trajectory = (BSplineCurve) BlockTools.getObject("/objects/math/curves/TreeCurves/trajectory", new Arc(0, 0.1f, 1));
        shape = (FloatToFloat) BlockTools.getObject("/objects/math/functions/TreeFunctions/shape", new Id());
        top = true;
        crookednessCurve = (BSplineCurve) BlockTools.getObject("/objects/math/curves/TreeCurves/crookedness", new Arc(0, 0.1f, 1));
        crookednessAmount = new CustomFunction(2, 0, 10);
        crookednessIntensity = (FloatToFloat) BlockTools.getObject("/objects/math/functions/TreeFunctions/intensity", new Id());
        deviateCurve = (BSplineCurve) BlockTools.getObject("/objects/math/curves/TreeCurves/deviateC", new Arc(0, 0.1f, 1));
        deviate = (FloatToFloat) BlockTools.getObject("/objects/math/functions/TreeFunctions/deviate", new Id());
        screw = (FloatToFloat) BlockTools.getObject("/objects/math/functions/TreeFunctions/screw", new Id());
        branchesNumber = new CustomFunction(10, 2, 100);
        arrangement = 0;
        branchesDistribution = (FloatToFloat) BlockTools.getObject("/objects/math/functions/TreeFunctions/distribution", new Id());
        branchesGrowthScale = (FloatToFloat) BlockTools.getObject("/objects/math/functions/TreeFunctions/growthscale", new Id());
        branchesGeometricScale = (FloatToFloat) BlockTools.getObject("/objects/math/functions/TreeFunctions/geomscale", new Id());
        branchesAngle = (FloatToFloat) BlockTools.getObject("/objects/math/functions/TreeFunctions/angle", new Id());
        branchesDense = (FloatToFloat) BlockTools.getObject("/objects/math/functions/TreeFunctions/dense", new Id());
        profile = new RegularPolygon();
        geometry = true;
        lod = new TreeLOD();
        locationParameter = new LocationParameterBase();
        initFunctionsToHermit();
    }

    private void initFunctionsToHermit() {
        ((SplineFunction) shape).setType(SplineFunction.HERMITE);
        ((SplineFunction) crookednessIntensity).setType(SplineFunction.HERMITE);
        ((SplineFunction) deviate).setType(SplineFunction.HERMITE);
        ((SplineFunction) screw).setType(SplineFunction.HERMITE);
        ((SplineFunction) branchesDistribution).setType(SplineFunction.HERMITE);
        ((SplineFunction) branchesGrowthScale).setType(SplineFunction.HERMITE);
        ((SplineFunction) branchesGeometricScale).setType(SplineFunction.HERMITE);
        ((SplineFunction) branchesAngle).setType(SplineFunction.HERMITE);
        ((SplineFunction) branchesDense).setType(SplineFunction.HERMITE);
        if (profile instanceof RegularPolygon) {
            ((RegularPolygon) profile).setSideCount(9);
        }
    }

    public Instantiator getInstantiator() {
        return de.grogra.rgg.model.Instantiation.INSTANTIATOR;
    }

    public void instantiate(Instantiation state) {
        Instantiation inst = (Instantiation) state;
        Library.setSeed(hashCode());
        ids = (Point2f) inst.getGraphState().getObjectDefault(this, true, Attributes.ID, null);
        densityValue = (Float) inst.getGraphState().getObjectDefault(this, true, Attributes.DENSITY, 1f);
        Matrix34d m = GlobalTransformation.get(this, true, inst.getGraphState(), false);
        Vector3d v = new Vector3d();
        m.get(v);
        height = new Point2f((float) v.z, (Float) inst.getGraphState().getObjectDefault(this, true, Attributes.HEIGHT, 1f));
        nutrientsValues = (Tuple3f) inst.getGraphState().getObjectDefault(this, true, Attributes.LOCATIONPARAMETER, null);
        Tuple3f nutrientsSetValues = locationParameter.setLocationParameter(nutrientsValues);
        lod.set(View.get(inst.getGraphState()), v, trunkScale.evaluateFloat(ids, nutrientsValues, height, densityValue));
        float xi = 0;
        float screw_value = 0;
        float branchAngle = 0;
        Scale branchesGeometricScaleNode = null;
        Rotate rotateS = new Rotate(0, 0, 0);
        final int anzStuetzstellen = 100;
        float[] branches_Distribution_Integral = new float[anzStuetzstellen];
        branches_Distribution_Integral[0] = branchesDistribution.evaluateFloat(0);
        for (int i = 1; i < anzStuetzstellen; i++) {
            branches_Distribution_Integral[i] = branches_Distribution_Integral[i - 1] + branchesDistribution.evaluateFloat(((float) i) / (float) anzStuetzstellen);
        }
        Tuple3f treeValues = (Tuple3f) inst.getGraphState().getObjectDefault(this, true, Attributes.TREEVALUES, null);
        int branches_NumberI = Math.round(((treeValues.y != 0) ? treeValues.y : 1) * lod.branches_NumberToLod(branchesNumber.evaluateFloat(ids, nutrientsValues, height, densityValue)));
        if (treeValues.x > 0.001) {
            inst.instantiate(new Scale(treeValues.x));
        }
        if (branches_NumberI != branches_NumberI_alt || branches_NumberI + 2 < distribution.length) {
            float sw = branches_Distribution_Integral[anzStuetzstellen - 1] / (float) (branches_NumberI + 2);
            int j = 0;
            distribution = new float[branches_NumberI + 3];
            for (int i = 1; i < anzStuetzstellen; i++) {
                if (branches_Distribution_Integral[i] > (j + 1) * sw) {
                    distribution[j] = (float) (i / 100.0);
                    j = j + 1;
                }
            }
            distribution[branches_NumberI + 2] = 1.0f;
            branches_NumberI_alt = branches_NumberI;
        }
        int idx;
        float[] trajPoint = new float[4];
        float[] trajPoint0 = { 0, 0, 0, 1 };
        float[] trajPoint1 = new float[4];
        float[] crookPoint = new float[4];
        float[] deviatePoint = new float[4];
        Vector3d vv2 = new Vector3d(0, 0, 0);
        Vector3d vv2old = new Vector3d(0, 0, 1);
        Point3f crookedness = new Point3f(0, 0, 0);
        Point3f deviateP = new Point3f(0, 0, 0);
        if (lod.isProfile() && geometry) {
            inst.instantiate(new Mark());
            inst.instantiate(new Vertex(shape.evaluateFloat(distribution[0])));
        }
        try {
            for (int i = 0; i < branches_NumberI; i++) {
                childId = i;
                inst.getGraphState().setInstanceAttribute(Attributes.NUTRIENTS_TUPLE3F, nutrientsSetValues);
                inst.getGraphState().setInstanceAttribute(Attributes.NUMBER_INT, new Integer(branches_NumberI));
                xi = (float) i / (float) (branches_NumberI - 1);
                crookPoint = getPoints(inst, crookednessCurve, distribution[i]);
                deviatePoint = getPoints(inst, deviateCurve, distribution[i]);
                crookedness.x = crookednessIntensity.evaluateFloat(distribution[i]) * crookednessAmount.evaluateFloat(ids, nutrientsValues, height, densityValue) * crookPoint[0];
                crookedness.y = crookednessIntensity.evaluateFloat(distribution[i]) * crookednessAmount.evaluateFloat(ids, nutrientsValues, height, densityValue) * crookPoint[1];
                crookedness.z = crookednessIntensity.evaluateFloat(distribution[i]) * crookednessAmount.evaluateFloat(ids, nutrientsValues, height, densityValue) * crookPoint[2];
                deviateP.x = deviate.evaluateFloat(distribution[i]) * deviatePoint[0];
                deviateP.y = deviate.evaluateFloat(distribution[i]) * deviatePoint[1];
                deviateP.z = deviate.evaluateFloat(distribution[i]) * deviatePoint[2];
                Point3f ri = new Point3f(crookedness);
                ri.add(deviateP);
                if (spline) {
                    idx = trajectory.getDimension(inst.getGraphState()) - 1;
                    BSpline.evaluate(trajPoint1, trajectory, distribution[i], inst.getGraphState());
                    if (trajectory.isRational(inst.getGraphState())) {
                        for (int k = idx - 1; k >= 0; k--) {
                            trajPoint1[k] /= trajPoint1[idx];
                            trajPoint[k] = trajPoint1[k] - trajPoint0[k];
                        }
                        trajPoint1[idx] = 0;
                    } else {
                        for (int k = idx; k >= 0; k--) {
                            trajPoint[k] = trajPoint1[k] - trajPoint0[k];
                        }
                    }
                    vv2 = new Vector3d(trajPoint[0], trajPoint[1], trajPoint[2]);
                    trajPoint0[0] = trajPoint1[0];
                    trajPoint0[1] = trajPoint1[1];
                    trajPoint0[2] = trajPoint1[2];
                    trajPoint0[3] = trajPoint1[3];
                    Vector3d vv2n = (Vector3d) vv2.clone();
                    vv2n.normalize();
                    Vector3d vv2oldn = (Vector3d) vv2old.clone();
                    vv2oldn.normalize();
                    Vector3d crossP = new Vector3d();
                    crossP.cross(vv2n, vv2oldn);
                    double angle = Math.acos(vv2n.dot(vv2oldn) / (vv2n.length() * vv2oldn.length()));
                    if (angle != 0) {
                        AxisAngle4d aa4d = new AxisAngle4d();
                        aa4d.set(crossP, angle);
                        Transform3D mat4d = new TMatrix4d();
                        ((TMatrix4d) mat4d).set(aa4d);
                        vv2old = vv2;
                        Null nullNode = new Null();
                        nullNode.setTransform(mat4d);
                        inst.instantiate(nullNode);
                    }
                    float aktLength = (float) vv2.length() * (stemLength.evaluateFloat(ids, nutrientsValues, height, densityValue) - 19);
                    inst.instantiate(new Scale(trunkScale.evaluateFloat(ids, nutrientsValues, height, densityValue)));
                    inst.instantiate(new Rotate(ri.x * (float) (180 / Math.PI), ri.y * (float) (180 / Math.PI), ri.z * (float) (180 / Math.PI)));
                    if (geometry) {
                        if (lod.isProfile()) {
                            inst.instantiate(new M(aktLength));
                            inst.instantiate(new Vertex(shape.evaluateFloat(distribution[i])));
                        } else {
                            inst.instantiate(new D(2 * shape.evaluateFloat(distribution[i])));
                            inst.instantiate(new F(aktLength));
                        }
                    } else {
                        inst.instantiate(new M(aktLength));
                    }
                } else {
                    inst.instantiate(new Scale(trunkScale.evaluateFloat(ids, nutrientsValues, height, densityValue)));
                    inst.instantiate(new Rotate(ri.x * (float) (180 / Math.PI), ri.y * (float) (180 / Math.PI), ri.z * (float) (180 / Math.PI)));
                    if (geometry) {
                        if (lod.isProfile()) {
                            inst.instantiate(new M(stemLength.evaluateFloat(ids, nutrientsValues, height, densityValue) * (distribution[i + 1] - distribution[i])));
                            inst.instantiate(new Vertex(shape.evaluateFloat(distribution[i])));
                        } else {
                            inst.instantiate(new D(2 * shape.evaluateFloat(distribution[i])));
                            inst.instantiate(new F(stemLength.evaluateFloat(ids, nutrientsValues, height, densityValue) * (distribution[i + 1] - distribution[i])));
                        }
                    } else {
                        inst.instantiate(new M(stemLength.evaluateFloat(ids, nutrientsValues, height, densityValue) * (distribution[i + 1] - distribution[i])));
                    }
                }
                branchAngle = 180 * branchesAngle.evaluateFloat(distribution[i]);
                branchesGeometricScaleNode = new Scale(lod.scaleToLod(branchesGeometricScale.evaluateFloat(distribution[i])));
                inst.getGraphState().setInstanceAttribute(Attributes.TREE_TUPLE3F, new Point3f(branchesGrowthScale.evaluateFloat(distribution[i]), branchesDense.evaluateFloat(distribution[i]), 0));
                inst.getGraphState().setInstanceAttribute(Attributes.ID_TUPLE2F, new Point2f(i, ids.x));
                if (arrangement == 0 || arrangement == 1) {
                    screw_value += 180 * screw.evaluateFloat(xi);
                    if (((int) screw_value) / 360 > ((int) screw_value - 180 * screw.evaluateFloat(xi)) / 360) {
                        screw_value += (180 * screw.evaluateFloat(xi)) / 10.0;
                        screw_value -= 360;
                    }
                    inst.producer$push();
                    inst.instantiate(new RH(screw_value));
                    Rotate rot = new Rotate(branchAngle, 0, (arrangement == 0) ? 180 : 90);
                    inst.instantiate(rot);
                    for (Edge e = getFirstEdge(); e != null; e = e.getNext(this)) {
                        Node n = e.getTarget();
                        if ((n != this) && e.testEdgeBits(BlockConst.MULTIPLY)) {
                            inst.instantiate(branchesGeometricScaleNode);
                            setHeightLocal(inst, branchesGeometricScaleNode, height.x);
                            inst.instantiate(n);
                        }
                    }
                    inst.producer$pop(null);
                }
                if (arrangement == 2 || arrangement == 3) {
                    Rotate rot;
                    inst.producer$push();
                    if (i % 2 == 0) {
                        rot = new Rotate(branchAngle, 0, (arrangement == 2) ? 180 : 90);
                    } else {
                        rot = new Rotate(180 - branchAngle, 180, (arrangement == 2) ? 180 : -90);
                    }
                    inst.instantiate(rot);
                    for (Edge e = getFirstEdge(); e != null; e = e.getNext(this)) {
                        Node n = e.getTarget();
                        if ((n != this) && e.testEdgeBits(BlockConst.MULTIPLY)) {
                            inst.instantiate(branchesGeometricScaleNode);
                            setHeightLocal(inst, branchesGeometricScaleNode, height.x);
                            inst.instantiate(n);
                        }
                    }
                    inst.producer$pop(null);
                }
                if (arrangement == 4 || arrangement == 5) {
                    inst.producer$push();
                    Rotate rot = new Rotate(branchAngle, 0, (arrangement == 4) ? 180 : 90);
                    inst.instantiate(rot);
                    for (Edge e = getFirstEdge(); e != null; e = e.getNext(this)) {
                        Node n = e.getTarget();
                        if ((n != this) && e.testEdgeBits(BlockConst.MULTIPLY)) {
                            inst.instantiate(branchesGeometricScaleNode);
                            setHeightLocal(inst, branchesGeometricScaleNode, height.x);
                            inst.instantiate(n);
                        }
                    }
                    inst.producer$pop(null);
                    inst.producer$push();
                    rot = new Rotate(180 - branchAngle, 180, (arrangement == 4) ? 180 : -90);
                    inst.instantiate(rot);
                    for (Edge e = getFirstEdge(); e != null; e = e.getNext(this)) {
                        Node n = e.getTarget();
                        if ((n != this) && e.testEdgeBits(BlockConst.MULTIPLY)) {
                            inst.instantiate(branchesGeometricScaleNode);
                            setHeightLocal(inst, branchesGeometricScaleNode, height.x);
                            inst.instantiate(n);
                        }
                    }
                    inst.producer$pop(null);
                }
                if (arrangement == 6 || arrangement == 7) {
                    inst.producer$push();
                    Rotate rot = new Rotate(branchAngle, 0, (arrangement == 6) ? 180 : 90);
                    inst.instantiate(rot);
                    for (Edge e = getFirstEdge(); e != null; e = e.getNext(this)) {
                        Node n = e.getTarget();
                        if ((n != this) && e.testEdgeBits(BlockConst.MULTIPLY)) {
                            inst.instantiate(branchesGeometricScaleNode);
                            setHeightLocal(inst, branchesGeometricScaleNode, height.x);
                            inst.instantiate(n);
                        }
                    }
                    inst.producer$pop(null);
                }
            }
            if (branches_NumberI > 1) {
                final float di = distribution[branches_NumberI];
                crookPoint = getPoints(inst, crookednessCurve, di);
                deviatePoint = getPoints(inst, deviateCurve, di);
                crookedness.x = crookednessIntensity.evaluateFloat(di) * crookednessAmount.evaluateFloat(ids, nutrientsValues, height, densityValue) * crookPoint[0];
                crookedness.y = crookednessIntensity.evaluateFloat(di) * crookednessAmount.evaluateFloat(ids, nutrientsValues, height, densityValue) * crookPoint[1];
                crookedness.z = crookednessIntensity.evaluateFloat(di) * crookednessAmount.evaluateFloat(ids, nutrientsValues, height, densityValue) * crookPoint[2];
                deviateP.x = deviate.evaluateFloat(di) * deviatePoint[0];
                deviateP.y = deviate.evaluateFloat(di) * deviatePoint[1];
                deviateP.z = deviate.evaluateFloat(di) * deviatePoint[2];
                Point3f ri = new Point3f(crookedness);
                ri.add(deviateP);
                if (spline) {
                    BSpline.evaluate(trajPoint1, trajectory, di, inst.getGraphState());
                    idx = trajectory.getDimension(inst.getGraphState()) - 1;
                    if (trajectory.isRational(inst.getGraphState())) {
                        for (int k = idx - 1; k >= 0; k--) {
                            trajPoint1[k] /= trajPoint1[idx];
                            trajPoint[k] = trajPoint1[k] - trajPoint0[k];
                        }
                        trajPoint1[idx] = 0;
                    } else {
                        for (int k = idx; k >= 0; k--) {
                            trajPoint[k] = trajPoint1[k] - trajPoint0[k];
                        }
                    }
                    vv2 = new Vector3d(trajPoint[0], trajPoint[1], trajPoint[2]);
                    Vector3d vv2n = (Vector3d) vv2.clone();
                    vv2n.normalize();
                    Vector3d vv2oldn = (Vector3d) vv2old.clone();
                    vv2oldn.normalize();
                    Vector3d crossP = new Vector3d();
                    crossP.cross(vv2n, vv2oldn);
                    double angle = Math.acos(vv2n.dot(vv2oldn) / (vv2n.length() * vv2oldn.length()));
                    if (angle != 0) {
                        AxisAngle4d aa4d = new AxisAngle4d();
                        aa4d.set(crossP, angle);
                        Transform3D mat4d = new TMatrix4d();
                        ((TMatrix4d) mat4d).set(aa4d);
                        vv2old = vv2;
                        Null nullNode = new Null();
                        nullNode.setTransform(mat4d);
                        inst.instantiate(nullNode);
                    }
                    inst.instantiate(new Scale(trunkScale.evaluateFloat(ids, nutrientsValues, height, densityValue)));
                    inst.instantiate(new Rotate(ri.x * (float) (180 / Math.PI), ri.y * (float) (180 / Math.PI), ri.z * (float) (180 / Math.PI)));
                    inst.instantiate(new M((float) Math.sqrt(trajPoint[0] * trajPoint[0] + trajPoint[1] * trajPoint[1] + trajPoint[2] * trajPoint[2]) * (stemLength.evaluateFloat(ids, nutrientsValues, height, densityValue) - 19)));
                    if (geometry) {
                        if (top) {
                            inst.instantiate(new Vertex(0));
                        } else {
                            inst.instantiate(new Vertex(shape.evaluateFloat(distribution[branches_NumberI])));
                        }
                    }
                } else {
                    inst.instantiate(new Scale(trunkScale.evaluateFloat(ids, nutrientsValues, height, densityValue)));
                    inst.instantiate(new Rotate(ri.x * (float) (180 / Math.PI), ri.y * (float) (180 / Math.PI), ri.z * (float) (180 / Math.PI)));
                    if (geometry) {
                        if (top) {
                            inst.instantiate(new M(stemLength.evaluateFloat(ids, nutrientsValues, height, densityValue) * (1 - di)));
                            inst.instantiate(new Vertex(0));
                        } else {
                            inst.instantiate(new M(stemLength.evaluateFloat(ids, nutrientsValues, height, densityValue) * (1 - di)));
                            inst.instantiate(new Vertex(shape.evaluateFloat(distribution[branches_NumberI - 1])));
                        }
                    } else {
                        inst.instantiate(new M(stemLength.evaluateFloat(ids, nutrientsValues, height, densityValue) * (1 - di)));
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException exp) {
        }
        if (lod.isProfile() && geometry) {
            inst.instantiate(new NURBSSurface(new SkinnedSurface(new ProfileSweep(profile, new VertexSequence(null)))));
        }
        for (Edge e = getFirstEdge(); e != null; e = e.getNext(this)) {
            Node n = e.getTarget();
            if ((n != this) && e.testEdgeBits(BlockConst.CHILD)) {
                inst.producer$push();
                if (spline) {
                    inst.instantiate(RL90);
                    inst.instantiate(rotateS);
                    inst.instantiate(RL_90);
                }
                inst.instantiate(branchesGeometricScaleNode);
                inst.getGraphState().setInstanceAttribute(Attributes.ID_TUPLE2F, new Point2f(0, ids.x));
                inst.instantiate(n);
                inst.producer$pop(null);
            }
        }
    }

    private float[] getPoints(Instantiation inst, BSplineCurve curve, float at) {
        float[] points = { 0, 0, 0, 0 };
        int idx = curve.getDimension(inst.getGraphState()) - 1;
        BSpline.evaluate(points, curve, at, inst.getGraphState());
        if (curve.isRational(inst.getGraphState())) {
            for (int k = idx - 1; k >= 0; k--) {
                points[k] /= points[idx];
            }
        }
        return points;
    }

    private void setHeightLocal(Instantiation inst, Node n, float height0) {
        Matrix34d m = GlobalTransformation.get(n, true, inst.getGraphState(), false);
        Vector3d heighti = new Vector3d();
        m.get(heighti);
        height.y = (float) Math.abs(heighti.z - height0);
        inst.getGraphState().setInstanceAttribute(Attributes.HEIGHT_FLOAT, new Float(height.y));
    }

    public void fieldModified(PersistenceField field, int[] indices, Transaction t) {
        super.fieldModified(field, indices, t);
        if (!Transaction.isApplying(t)) {
            if (field.overlaps(indices, initAll$FIELD, null)) {
                if (isInitAll()) {
                    initAttributes();
                    setInitAll(false);
                }
            }
        }
    }

    public float getBranchesNumber() {
        return branchesNumber.evaluateZerro();
    }

    public void setBranchesNumber(float value) {
        branchesNumber.setFunction(value);
    }

    public float getStemLength() {
        return stemLength.evaluateZerro();
    }

    public void setStemLength(double value) {
        stemLength.setFunction(value);
    }

    public float getTrunkScale() {
        return trunkScale.evaluateZerro();
    }

    public void setTrunkScale(double value) {
        trunkScale.setFunction(value);
    }

    public float getCrookednessAmount() {
        return crookednessAmount.evaluateZerro();
    }

    public void setCrookednessAmount(double value) {
        crookednessAmount.setFunction(value);
    }

    public void setStemLength(String value) {
        stemLength.setFunction(value);
    }

    public void setTrunkScale(String value) {
        trunkScale.setFunction(value);
    }

    public void setCrookednessAmount(String value) {
        crookednessAmount.setFunction(value);
    }

    public void useLod(boolean value) {
        lod.setUseLOD(value);
    }

    public int getChildId() {
        return childId;
    }

    public int getParentId() {
        return (int) ids.x;
    }

    public int getThisId() {
        return (int) ids.y;
    }

    public float getDensity() {
        return densityValue;
    }

    public float getAbsoluteHeight() {
        return height.x;
    }

    public float getLocalHeight() {
        return height.y;
    }

    public float getN1() {
        return nutrientsValues.x;
    }

    public float getN2() {
        return nutrientsValues.y;
    }

    public float getN3() {
        return nutrientsValues.z;
    }

    public static final NType $TYPE;

    public static final NType.Field stemLength$FIELD;

    public static final NType.Field trunkScale$FIELD;

    public static final NType.Field spline$FIELD;

    public static final NType.Field trajectory$FIELD;

    public static final NType.Field shape$FIELD;

    public static final NType.Field top$FIELD;

    public static final NType.Field crookednessCurve$FIELD;

    public static final NType.Field crookednessAmount$FIELD;

    public static final NType.Field crookednessIntensity$FIELD;

    public static final NType.Field deviateCurve$FIELD;

    public static final NType.Field deviate$FIELD;

    public static final NType.Field screw$FIELD;

    public static final NType.Field branchesNumber$FIELD;

    public static final NType.Field arrangement$FIELD;

    public static final NType.Field branchesDistribution$FIELD;

    public static final NType.Field branchesGrowthScale$FIELD;

    public static final NType.Field branchesGeometricScale$FIELD;

    public static final NType.Field branchesAngle$FIELD;

    public static final NType.Field branchesDense$FIELD;

    public static final NType.Field profile$FIELD;

    public static final NType.Field geometry$FIELD;

    public static final NType.Field lod$FIELD;

    public static final NType.Field locationParameter$FIELD;

    public static final NType.Field initAll$FIELD;

    private static final class _Field extends NType.Field {

        private final int id;

        _Field(String name, int modifiers, de.grogra.reflect.Type type, de.grogra.reflect.Type componentType, int id) {
            super(Tree.$TYPE, name, modifiers, type, componentType);
            this.id = id;
        }

        @Override
        public void setBoolean(Object o, boolean value) {
            switch(id) {
                case 2:
                    ((Tree) o).spline = (boolean) value;
                    return;
                case 5:
                    ((Tree) o).top = (boolean) value;
                    return;
                case 20:
                    ((Tree) o).geometry = (boolean) value;
                    return;
                case 23:
                    ((Tree) o).initAll = (boolean) value;
                    return;
            }
            super.setBoolean(o, value);
        }

        @Override
        public boolean getBoolean(Object o) {
            switch(id) {
                case 2:
                    return ((Tree) o).isSpline();
                case 5:
                    return ((Tree) o).isTop();
                case 20:
                    return ((Tree) o).isGeometry();
                case 23:
                    return ((Tree) o).isInitAll();
            }
            return super.getBoolean(o);
        }

        @Override
        public void setInt(Object o, int value) {
            switch(id) {
                case 13:
                    ((Tree) o).arrangement = (int) value;
                    return;
            }
            super.setInt(o, value);
        }

        @Override
        public int getInt(Object o) {
            switch(id) {
                case 13:
                    return ((Tree) o).getArrangement();
            }
            return super.getInt(o);
        }

        @Override
        protected void setObjectImpl(Object o, Object value) {
            switch(id) {
                case 0:
                    ((Tree) o).stemLength = (CustomFunction) value;
                    return;
                case 1:
                    ((Tree) o).trunkScale = (CustomFunction) value;
                    return;
                case 3:
                    ((Tree) o).trajectory = (BSplineCurve) value;
                    return;
                case 4:
                    ((Tree) o).shape = (FloatToFloat) value;
                    return;
                case 6:
                    ((Tree) o).crookednessCurve = (BSplineCurve) value;
                    return;
                case 7:
                    ((Tree) o).crookednessAmount = (CustomFunction) value;
                    return;
                case 8:
                    ((Tree) o).crookednessIntensity = (FloatToFloat) value;
                    return;
                case 9:
                    ((Tree) o).deviateCurve = (BSplineCurve) value;
                    return;
                case 10:
                    ((Tree) o).deviate = (FloatToFloat) value;
                    return;
                case 11:
                    ((Tree) o).screw = (FloatToFloat) value;
                    return;
                case 12:
                    ((Tree) o).branchesNumber = (CustomFunction) value;
                    return;
                case 14:
                    ((Tree) o).branchesDistribution = (FloatToFloat) value;
                    return;
                case 15:
                    ((Tree) o).branchesGrowthScale = (FloatToFloat) value;
                    return;
                case 16:
                    ((Tree) o).branchesGeometricScale = (FloatToFloat) value;
                    return;
                case 17:
                    ((Tree) o).branchesAngle = (FloatToFloat) value;
                    return;
                case 18:
                    ((Tree) o).branchesDense = (FloatToFloat) value;
                    return;
                case 19:
                    ((Tree) o).profile = (BSplineCurve) value;
                    return;
                case 21:
                    ((Tree) o).lod = (TreeLOD) value;
                    return;
                case 22:
                    ((Tree) o).locationParameter = (LocationParameterBase) value;
                    return;
            }
            super.setObjectImpl(o, value);
        }

        @Override
        public Object getObject(Object o) {
            switch(id) {
                case 0:
                    return ((Tree) o).stemLength;
                case 1:
                    return ((Tree) o).trunkScale;
                case 3:
                    return ((Tree) o).getTrajectory();
                case 4:
                    return ((Tree) o).getShape();
                case 6:
                    return ((Tree) o).getCrookednessCurve();
                case 7:
                    return ((Tree) o).crookednessAmount;
                case 8:
                    return ((Tree) o).getCrookednessIntensity();
                case 9:
                    return ((Tree) o).getDeviateCurve();
                case 10:
                    return ((Tree) o).getDeviate();
                case 11:
                    return ((Tree) o).getScrew();
                case 12:
                    return ((Tree) o).branchesNumber;
                case 14:
                    return ((Tree) o).getBranchesDistribution();
                case 15:
                    return ((Tree) o).getBranchesGrowthScale();
                case 16:
                    return ((Tree) o).getBranchesGeometricScale();
                case 17:
                    return ((Tree) o).getBranchesAngle();
                case 18:
                    return ((Tree) o).getBranchesDense();
                case 19:
                    return ((Tree) o).getProfile();
                case 21:
                    return ((Tree) o).getLod();
                case 22:
                    return ((Tree) o).getLocationParameter();
            }
            return super.getObject(o);
        }
    }

    static {
        $TYPE = new NType(new Tree());
        $TYPE.addManagedField(stemLength$FIELD = new _Field("stemLength", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(CustomFunction.class), null, 0));
        $TYPE.addManagedField(trunkScale$FIELD = new _Field("trunkScale", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(CustomFunction.class), null, 1));
        $TYPE.addManagedField(spline$FIELD = new _Field("spline", 0 | _Field.SCO, de.grogra.reflect.Type.BOOLEAN, null, 2));
        $TYPE.addManagedField(trajectory$FIELD = new _Field("trajectory", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(BSplineCurve.class), null, 3));
        $TYPE.addManagedField(shape$FIELD = new _Field("shape", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(FloatToFloat.class), null, 4));
        $TYPE.addManagedField(top$FIELD = new _Field("top", 0 | _Field.SCO, de.grogra.reflect.Type.BOOLEAN, null, 5));
        $TYPE.addManagedField(crookednessCurve$FIELD = new _Field("crookednessCurve", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(BSplineCurve.class), null, 6));
        $TYPE.addManagedField(crookednessAmount$FIELD = new _Field("crookednessAmount", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(CustomFunction.class), null, 7));
        $TYPE.addManagedField(crookednessIntensity$FIELD = new _Field("crookednessIntensity", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(FloatToFloat.class), null, 8));
        $TYPE.addManagedField(deviateCurve$FIELD = new _Field("deviateCurve", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(BSplineCurve.class), null, 9));
        $TYPE.addManagedField(deviate$FIELD = new _Field("deviate", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(FloatToFloat.class), null, 10));
        $TYPE.addManagedField(screw$FIELD = new _Field("screw", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(FloatToFloat.class), null, 11));
        $TYPE.addManagedField(branchesNumber$FIELD = new _Field("branchesNumber", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(CustomFunction.class), null, 12));
        $TYPE.addManagedField(arrangement$FIELD = new _Field("arrangement", 0 | _Field.SCO, BRANCHES_TYPE, null, 13));
        $TYPE.addManagedField(branchesDistribution$FIELD = new _Field("branchesDistribution", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(FloatToFloat.class), null, 14));
        $TYPE.addManagedField(branchesGrowthScale$FIELD = new _Field("branchesGrowthScale", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(FloatToFloat.class), null, 15));
        $TYPE.addManagedField(branchesGeometricScale$FIELD = new _Field("branchesGeometricScale", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(FloatToFloat.class), null, 16));
        $TYPE.addManagedField(branchesAngle$FIELD = new _Field("branchesAngle", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(FloatToFloat.class), null, 17));
        $TYPE.addManagedField(branchesDense$FIELD = new _Field("branchesDense", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(FloatToFloat.class), null, 18));
        $TYPE.addManagedField(profile$FIELD = new _Field("profile", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(BSplineCurve.class), null, 19));
        $TYPE.addManagedField(geometry$FIELD = new _Field("geometry", 0 | _Field.SCO, de.grogra.reflect.Type.BOOLEAN, null, 20));
        $TYPE.addManagedField(lod$FIELD = new _Field("lod", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(TreeLOD.class), null, 21));
        $TYPE.addManagedField(locationParameter$FIELD = new _Field("locationParameter", 0 | _Field.SCO, de.grogra.reflect.ClassAdapter.wrap(LocationParameterBase.class), null, 22));
        $TYPE.addManagedField(initAll$FIELD = new _Field("initAll", 0 | _Field.SCO, de.grogra.reflect.Type.BOOLEAN, null, 23));
        $TYPE.validate();
    }

    @Override
    protected NType getNTypeImpl() {
        return $TYPE;
    }

    @Override
    protected de.grogra.graph.impl.Node newInstance() {
        return new Tree();
    }

    public boolean isSpline() {
        return spline;
    }

    public void setSpline(boolean value) {
        this.spline = (boolean) value;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean value) {
        this.top = (boolean) value;
    }

    public boolean isGeometry() {
        return geometry;
    }

    public void setGeometry(boolean value) {
        this.geometry = (boolean) value;
    }

    public boolean isInitAll() {
        return initAll;
    }

    public void setInitAll(boolean value) {
        this.initAll = (boolean) value;
    }

    public int getArrangement() {
        return arrangement;
    }

    public void setArrangement(int value) {
        this.arrangement = (int) value;
    }

    public void setStemLength(CustomFunction value) {
        stemLength$FIELD.setObject(this, value);
    }

    public void setTrunkScale(CustomFunction value) {
        trunkScale$FIELD.setObject(this, value);
    }

    public BSplineCurve getTrajectory() {
        return trajectory;
    }

    public void setTrajectory(BSplineCurve value) {
        trajectory$FIELD.setObject(this, value);
    }

    public FloatToFloat getShape() {
        return shape;
    }

    public void setShape(FloatToFloat value) {
        shape$FIELD.setObject(this, value);
    }

    public BSplineCurve getCrookednessCurve() {
        return crookednessCurve;
    }

    public void setCrookednessCurve(BSplineCurve value) {
        crookednessCurve$FIELD.setObject(this, value);
    }

    public void setCrookednessAmount(CustomFunction value) {
        crookednessAmount$FIELD.setObject(this, value);
    }

    public FloatToFloat getCrookednessIntensity() {
        return crookednessIntensity;
    }

    public void setCrookednessIntensity(FloatToFloat value) {
        crookednessIntensity$FIELD.setObject(this, value);
    }

    public BSplineCurve getDeviateCurve() {
        return deviateCurve;
    }

    public void setDeviateCurve(BSplineCurve value) {
        deviateCurve$FIELD.setObject(this, value);
    }

    public FloatToFloat getDeviate() {
        return deviate;
    }

    public void setDeviate(FloatToFloat value) {
        deviate$FIELD.setObject(this, value);
    }

    public FloatToFloat getScrew() {
        return screw;
    }

    public void setScrew(FloatToFloat value) {
        screw$FIELD.setObject(this, value);
    }

    public void setBranchesNumber(CustomFunction value) {
        branchesNumber$FIELD.setObject(this, value);
    }

    public FloatToFloat getBranchesDistribution() {
        return branchesDistribution;
    }

    public void setBranchesDistribution(FloatToFloat value) {
        branchesDistribution$FIELD.setObject(this, value);
    }

    public FloatToFloat getBranchesGrowthScale() {
        return branchesGrowthScale;
    }

    public void setBranchesGrowthScale(FloatToFloat value) {
        branchesGrowthScale$FIELD.setObject(this, value);
    }

    public FloatToFloat getBranchesGeometricScale() {
        return branchesGeometricScale;
    }

    public void setBranchesGeometricScale(FloatToFloat value) {
        branchesGeometricScale$FIELD.setObject(this, value);
    }

    public FloatToFloat getBranchesAngle() {
        return branchesAngle;
    }

    public void setBranchesAngle(FloatToFloat value) {
        branchesAngle$FIELD.setObject(this, value);
    }

    public FloatToFloat getBranchesDense() {
        return branchesDense;
    }

    public void setBranchesDense(FloatToFloat value) {
        branchesDense$FIELD.setObject(this, value);
    }

    public BSplineCurve getProfile() {
        return profile;
    }

    public void setProfile(BSplineCurve value) {
        profile$FIELD.setObject(this, value);
    }

    public TreeLOD getLod() {
        return lod;
    }

    public void setLod(TreeLOD value) {
        lod$FIELD.setObject(this, value);
    }

    public LocationParameterBase getLocationParameter() {
        return locationParameter;
    }

    public void setLocationParameter(LocationParameterBase value) {
        locationParameter$FIELD.setObject(this, value);
    }
}
