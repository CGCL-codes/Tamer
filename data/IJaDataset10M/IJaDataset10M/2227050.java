package de.grogra.ray2.metropolis.strategy;

import java.util.ArrayList;
import de.grogra.ray.physics.Environment;
import de.grogra.ray.physics.Light;
import de.grogra.ray.physics.Spectrum3d;
import de.grogra.ray2.Scene;
import de.grogra.ray2.antialiasing.MetropolisAntiAliasing;
import de.grogra.ray2.metropolis.strategy.LensSubpathStrat.StratificationCondition;
import de.grogra.ray2.tracing.MetropolisProcessor;
import de.grogra.ray2.tracing.modular.CombinedPathValues;
import de.grogra.ray2.tracing.modular.ComplementTracer;
import de.grogra.ray2.tracing.modular.ConditionObject;
import de.grogra.ray2.tracing.modular.LineTracer;
import de.grogra.ray2.tracing.modular.PathValues;
import de.grogra.vecmath.geom.Intersection;
import de.grogra.vecmath.geom.Line;

public class LensPerturbationStrat extends MutationStrategy {

    static final int NO_DIFFUSE_FOUND = 101;

    static int countOfLensEdgeMutations;

    static int countOfFirstLightRayMutations;

    static int countOfMiddlePathMutations;

    static int countOfLensEdgeMutationsError = 0;

    static int countOfFirstLightRayMutationsError = 0;

    static int countOfMiddlePathMutationsError = 0;

    static int count_NoError;

    static int count_notImplemented = 0;

    static int count_notVisible = 0;

    static int count_ZeroBSDF = 0;

    static int count_noChanges = 0;

    static int count_notValidPath = 0;

    static int count_weakSpec = 0;

    static int count_notCanvasHit = 0;

    static int count_CalculationError = 0;

    static int count_totalError = 0;

    static int count_tracing = 0;

    static int count_no_diffuse = 0;

    public int kd, ka, l, m, l_, m_, real_m_, real_l_;

    public float acceptance;

    public LensPerturbationStrat(MetropolisProcessor metroProc) {
        super(metroProc);
        eyePath = new PathValues();
        eyePath.initialize(eyePathDepth + lightPathDepth);
        lightPath = new PathValues();
        lightPath.initialize(eyePathDepth + lightPathDepth);
    }

    public void initialize(double[] values) {
    }

    public void resetAll() {
        countOfLensEdgeMutations = 0;
        countOfFirstLightRayMutations = 0;
        countOfMiddlePathMutations = 0;
        count_NoError = 0;
        countOfLensEdgeMutationsError = 0;
        countOfFirstLightRayMutationsError = 0;
        countOfMiddlePathMutationsError = 0;
        count_notImplemented = 0;
        count_notVisible = 0;
        count_ZeroBSDF = 0;
        count_noChanges = 0;
        count_notValidPath = 0;
        count_weakSpec = 0;
        count_notCanvasHit = 0;
        count_CalculationError = 0;
        count_totalError = 0;
        count_tracing = 0;
        count_no_diffuse = 0;
    }

    public float mutatePath(CombinedPathValues actualPath, CombinedPathValues mutatedPath) {
        super.mutatePath(actualPath, mutatedPath);
        kd = ka = l = m = l_ = m_ = real_m_ = real_l_ = -1;
        int d = searchForDoubleDiffuse();
        if (d < 0) {
            return registerError(NO_DIFFUSE_FOUND);
        } else {
        }
        l = d;
        m = srcPath.pathLength - 1;
        l_ = 0;
        m_ = m - l - 1;
        srcPath.splitInto2Paths(l, lightPath, eyePath);
        eyePath.pathResultList = srcPath.pathResultList;
        eyePath.creatorID = srcPath.sensorID;
        lightPath.creatorID = srcPath.lightID;
        eyePath.pathLength = 1;
        condition = new StratificationCondition(eyePath, tracer);
        tracer.condition = condition;
        int eyePathLengthBefore = eyePath.pathLength;
        traceEyeSubPath(0, m_);
        real_l_ = 0;
        real_m_ = eyePath.pathLength - eyePathLengthBefore;
        if (m_ != real_m_) return registerError(TRACINGGOAL_WASNT_REACHED);
        int j = 0;
        for (int i = srcPath.pathLength - 1; i > l; i--) {
            if (srcPath.isSpecular(i) != eyePath.isSpecular(j)) return registerError(TRACINGGOAL_WASNT_REACHED);
            j++;
        }
        Environment lastLightEnv = lightPath.envList.get(lightPath.pathLength - 1);
        Environment lastEyeEnv = eyePath.envList.get(eyePath.pathLength - 1);
        Intersection lastLightInt = lightPath.intersecList.get(lightPath.pathLength - 1);
        if (eyePath.pathLength == 1) {
            if (!isVisble(lastEyeEnv, lastLightEnv, null)) {
                return registerError(NOT_VISIBLE);
            }
        } else if (!isVisble(lastLightEnv, lastEyeEnv, lastLightInt)) {
            return registerError(NOT_VISIBLE);
        }
        complTracer.complement2Paths(lightPath, lightPath.pathLength - 1, eyePath, eyePath.pathLength - 1, false, this.mutatedPath);
        this.mutatedPath.pathResultList = eyePath.pathResultList;
        if (!hitsCanvas(mutatedPath)) {
            abbortCode = CANVAS_WASNT_HIT;
            return -1;
        }
        if (!mutatedPath.isValid()) {
            return this.registerError(NO_VALID_COMBINEDPATH);
        }
        acceptance = calculateAcceptanceProbality(l, m, real_l_, real_m_);
        if (abbortCode == SUCCESSFUL_MUTATIONRUN) {
            count_NoError++;
            if (m == actualPath.pathLength - 1) countOfLensEdgeMutations++;
            if (l == 0) countOfFirstLightRayMutations++;
            if ((m != actualPath.pathLength - 1) && (l != 0)) countOfMiddlePathMutations++;
        }
        return acceptance;
    }

    public Line generateNewStartLine(boolean isLightRay) {
        Line newLine = null;
        if (isLightRay) {
            tmpRayList.clear();
            tmpRayList.setSize(1);
            int lId = lightPath.creatorID;
            Scene scene = tracingMediator.getProcessor().scene;
            tmpEnv.localToGlobal.set(scene.getLightTransformation(lId));
            tmpEnv.globalToLocal.set(scene.getInverseLightTransformation(lId));
            Light light = scene.getLights()[lightPath.creatorID];
            light.generateRandomOrigins(tmpEnv, tmpRayList, rnd);
            light.generateRandomRays(tmpEnv, null, tmpRayList.rays[0].spectrum, tmpRayList, false, rnd);
            newLine = tmpRayList.rays[0].convert2Line();
        } else {
            newLine = tracingMediator.getMetropolisAntialiser().getPerturbedLensEdge(eyePath.rayListBE.get(0));
        }
        return newLine;
    }

    @Override
    public int registerError(int error) {
        switch(error) {
            case NOT_IMPLEMENTED_YET:
                {
                    count_notImplemented++;
                    break;
                }
            case NOT_VISIBLE:
                {
                    count_notVisible++;
                    break;
                }
            case SUCCESSFUL_MUTATIONRUN:
                {
                    count_NoError++;
                    break;
                }
            case BSDF_WAS_ZERO:
                {
                    count_ZeroBSDF++;
                    break;
                }
            case NO_CHANGES_HAPPENED:
                {
                    count_noChanges++;
                    break;
                }
            case NO_VALID_COMBINEDPATH:
                {
                    count_notValidPath++;
                    break;
                }
            case PATH_SPECTRA_WERE_TOO_WEAK:
                {
                    count_weakSpec++;
                    break;
                }
            case CANVAS_WASNT_HIT:
                {
                    count_notCanvasHit++;
                    break;
                }
            case TRACINGGOAL_WASNT_REACHED:
                {
                    count_tracing++;
                    break;
                }
            case NO_DIFFUSE_FOUND:
                {
                    count_no_diffuse++;
                    break;
                }
            default:
                {
                    count_CalculationError++;
                    break;
                }
        }
        if (error != SUCCESSFUL_MUTATIONRUN) {
            count_totalError++;
            if (m >= srcPath.pathLength - 1) countOfLensEdgeMutationsError++;
            if (l <= 0) countOfFirstLightRayMutationsError++;
            if ((m < srcPath.pathLength - 1) && (l > 0)) countOfMiddlePathMutationsError++;
        }
        return super.registerError(error);
    }

    public ArrayList<String> getStatistics() {
        ArrayList<String> stat = new ArrayList<String>();
        stat.add("total Count of successfull strategy runs: " + (count_NoError) + "\n");
        stat.add(" * successfull lens edge mutation runs:" + countOfLensEdgeMutations + "(" + ((float) countOfLensEdgeMutations / count_NoError) + ")\n");
        stat.add(" * successfull first light ray mutation runs:" + countOfFirstLightRayMutations + "(" + ((float) countOfFirstLightRayMutations / count_NoError) + ")\n");
        stat.add(" * successfull path centered mutation runs:" + countOfMiddlePathMutations + "(" + ((float) countOfMiddlePathMutations / count_NoError) + ")\n\n");
        stat.add("total count of failure: " + (count_totalError) + "\n");
        stat.add(" * error on lens edge mutation:" + countOfLensEdgeMutationsError + "(" + ((float) countOfLensEdgeMutationsError / count_totalError) + ")\n");
        stat.add(" * error on first light ray mutation:" + countOfFirstLightRayMutationsError + "(" + ((float) countOfFirstLightRayMutationsError / count_totalError) + ")\n");
        stat.add(" * error on path centered mutation:" + countOfMiddlePathMutationsError + "(" + ((float) countOfMiddlePathMutationsError / count_totalError) + ")\n\n");
        stat.add(" * count_no_diffuse: " + count_no_diffuse + " (" + ((float) count_no_diffuse / count_totalError) + ")\n");
        stat.add(" * count_notImplemented: " + count_notImplemented + " (" + ((float) count_notImplemented / count_totalError) + ")\n");
        stat.add(" * count_notVisible: " + count_notVisible + " (" + ((float) count_notVisible / count_totalError) + ")\n");
        stat.add(" * count_ZeroBSDF: " + count_ZeroBSDF + " (" + ((float) count_ZeroBSDF / count_totalError) + ")\n");
        stat.add(" * count_noChanges: " + count_noChanges + " (" + ((float) count_noChanges / count_totalError) + ")\n");
        stat.add(" * count_notValidPath: " + count_notValidPath + " (" + ((float) count_notValidPath / count_totalError) + ")\n");
        stat.add(" * count_weakSpec: " + count_weakSpec + " (" + ((float) count_weakSpec / count_totalError) + "\n");
        stat.add(" * count_notCanvasHit: " + count_notCanvasHit + " (" + ((float) count_notCanvasHit / count_totalError) + ")\n");
        stat.add(" * count_CalculationError: " + count_CalculationError + " (" + ((float) count_CalculationError / count_totalError) + ")\n\n");
        stat.add(" * count_tracingError: " + count_tracing + " (" + ((float) count_tracing / count_totalError) + ")\n\n");
        return stat;
    }

    int searchForDoubleDiffuse() {
        boolean lastWasDiffuse = false;
        for (int i = srcPath.pathLength - 2; i >= 0; i--) {
            if (!srcPath.isSpecular(i) && lastWasDiffuse) return i;
            if (!srcPath.isSpecular(i)) lastWasDiffuse = true;
        }
        return -1;
    }

    class StratificationCondition extends ConditionObject {

        public StratificationCondition(PathValues path, LineTracer lineTracer) {
            super(path, lineTracer);
        }

        public boolean stopOnCondition() {
            int last = path.pathLength - 1;
            if (last == 0) return false;
            return (!path.isSpecular(last));
        }
    }
}
