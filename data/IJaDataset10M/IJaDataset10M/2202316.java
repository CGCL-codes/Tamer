package de.jmulti.proc;

import com.jstatcom.engine.gauss.GaussLoadTypes;
import com.jstatcom.model.JSCData;
import com.jstatcom.model.JSCInt;
import com.jstatcom.model.JSCNArray;
import com.jstatcom.model.JSCNumber;
import com.jstatcom.model.JSCSArray;
import com.jstatcom.model.JSCString;
import com.jstatcom.model.SymbolTable;
import com.jstatcom.util.FArg;
import de.jmulti.tools.IRACIManager;
import de.jmulti.tools.ModelTypes;
import de.jmulti.var.SVARConstants;
import de.jmulti.vecm.SVECConstants;
import de.jmulti.vecm.VECMConstants;

/**
 * This GAUSS command object computes the bootstrapped Efron/Hall percentile
 * confidence intervals for the impulse response analysis according to the
 * parametrization and the specified model. The result of this call is stored to
 * the <code>IRACIManager</code> according to the model type that has been
 * set.
 * 
 * @author <a href="mailto:mk@mk-home.de">Markus Kraetzig</a>
 */
public final class IRABootCIPercCall extends GaussPCall {

    private int periods = 20;

    private int numberOfRep = 100;

    private double coverage = 0.950;

    private boolean isSeed = false;

    private int seed = 0;

    private ModelTypes modelType = ModelTypes.VAR;

    private SymbolTable gst = null;

    /**
     * IRABootCIPercCall constructor comment.
     */
    public IRABootCIPercCall(SymbolTable symbolTable, int periods, int numberOfRep, double coverage, boolean isSeed, int seed, ModelTypes modelType) {
        super();
        this.gst = symbolTable;
        setName("Bootstrap Efron & Hall Percentile CIs");
        this.periods = periods;
        this.numberOfRep = numberOfRep;
        this.coverage = coverage;
        this.isSeed = isSeed;
        this.seed = seed;
        this.modelType = modelType;
    }

    /**
     * Gets an array of descriptive strings for the specified bootstrap CIs. The
     * first element contains the Efron CI identifier, the second element the
     * Hall CI identifier. The strings contain all selected parameters and can
     * therefore be used as keys to identify all bootstrapped CIs for a given
     * model.
     * 
     * @return a 2 x 1 string array with names for the CIs to bootstrap
     */
    private String[] getCINames() {
        StringBuffer efronNameCI = new StringBuffer();
        StringBuffer hallNameCI = new StringBuffer();
        String coverageString = FArg.sprintf("%.0f", new FArg(100 * coverage));
        efronNameCI.append(coverageString + "% Efron Percentile CI (B=" + numberOfRep + " h=" + periods);
        hallNameCI.append(coverageString + "% Hall Percentile CI (B=" + numberOfRep + " h=" + periods);
        if (isSeed) {
            efronNameCI.append(", seed=" + seed);
            hallNameCI.append(", seed=" + seed);
        }
        efronNameCI.append(")");
        hallNameCI.append(")");
        String[] names = new String[2];
        names[0] = efronNameCI.toString();
        names[1] = hallNameCI.toString();
        return names;
    }

    /**
     * @see ProcCall
     */
    protected void runCode() {
        engine().load("var", GaussLoadTypes.LIB);
        engine().load("vec", GaussLoadTypes.LIB);
        if (modelType == ModelTypes.VAR || modelType == ModelTypes.VECM) {
            runVARVECCode();
            return;
        }
        if (modelType == ModelTypes.SVAR) {
            String lastModel = gst.get(SVARConstants.lastModel_Def).getJSCString().string();
            if (lastModel.equalsIgnoreCase(SVARConstants.AB_Model)) runSVARABCode(); else if (lastModel.equalsIgnoreCase(SVARConstants.BQ_Model)) runSVARBQCode();
            return;
        }
        if (modelType == ModelTypes.SVEC) {
            runSVECCode();
            return;
        }
    }

    /**
     * @see ProcCall
     */
    private void runSVARABCode() {
        seed = isSeed ? seed : 0;
        JSCNArray varBuffer = new JSCNArray("varBuffer");
        VAREstimationCall.setVarBuffer(engine(), gst, varBuffer);
        engine().call("vml_vput", new JSCData[] { varBuffer, gst.get(SVARConstants.resMatrixAB_Def).getJSCData(), new JSCString("info_key", "resMat") }, new JSCData[] { varBuffer });
        engine().call("vml_vput", new JSCData[] { varBuffer, gst.get(SVARConstants.startValVector_Def).getJSCData(), new JSCString("info_key", "startVal") }, new JSCData[] { varBuffer });
        engine().call("vml_vput", new JSCData[] { varBuffer, gst.get(SVARConstants.relParamCh_Def).getJSCData(), new JSCString("info_key", "relParCh") }, new JSCData[] { varBuffer });
        engine().call("vml_vput", new JSCData[] { varBuffer, gst.get(SVARConstants.relLikCh_Def).getJSCData(), new JSCString("info_key", "relLikCh") }, new JSCData[] { varBuffer });
        engine().call("vml_vput", new JSCData[] { varBuffer, gst.get(SVARConstants.maxIter_Def).getJSCData(), new JSCString("info_key", "maxIter") }, new JSCData[] { varBuffer });
        engine().call("vml_vput", new JSCData[] { varBuffer, gst.get(SVARConstants.svarABModelType_Def).getJSCData(), new JSCString("info_key", "sabtype") }, new JSCData[] { varBuffer });
        engine().call("vml_vput", new JSCData[] { varBuffer, gst.get(SVARConstants.normConst_Def).getJSCData(), new JSCString("info_key", "normCon") }, new JSCData[] { varBuffer });
        engine().call("vml_vput", new JSCData[] { varBuffer, gst.get(SVARConstants.lastModel_Def).getJSCData(), new JSCString("info_key", "svar") }, new JSCData[] { varBuffer });
        JSCNArray[] retArgs = getRtnArgs();
        engine().call("var__IRA_CI_HEP", new JSCData[] { varBuffer, new JSCInt("numberOfRep", numberOfRep), new JSCInt("seed", seed), new JSCInt("periods", periods), new JSCNumber("coverage", coverage) }, retArgs);
        String[] efron_hall = getCINames();
        IRACIManager manager = IRACIManager.getInstance();
        JSCNArray[] gdEfron = new JSCNArray[] { retArgs[6], retArgs[7] };
        manager.addCI(efron_hall[0], modelType, gdEfron);
        JSCNArray[] gdHall = new JSCNArray[] { retArgs[8], retArgs[9] };
        manager.addCI(efron_hall[1], modelType, gdHall);
        setLocalResults(retArgs, true);
    }

    /**
     * @return
     */
    private JSCNArray[] getRtnArgs() {
        JSCNArray[] retArgs = new JSCNArray[] { new JSCNArray("__rt_vec0"), new JSCNArray("__rt_vec1"), new JSCNArray("__rt_vec2"), new JSCNArray("__rt_vec3"), new JSCNArray("__rt_vec4"), new JSCNArray("__rt_vec5"), new JSCNArray("__rt_vec6"), new JSCNArray("__rt_vec7"), new JSCNArray("__rt_vec8"), new JSCNArray("__rt_vec9"), new JSCNArray("__rt_vec10"), (JSCNArray) IRABootCIStudCall.FE_BOOT.getInstance(), (JSCNArray) IRABootCIStudCall.ORTH_BOOT.getInstance() };
        return retArgs;
    }

    /**
     * @see ProcCall
     */
    private void runSVARBQCode() {
        seed = isSeed ? seed : 0;
        JSCNArray varBuffer = new JSCNArray("varBuffer");
        VAREstimationCall.setVarBuffer(engine(), gst, varBuffer);
        engine().call("vml_vput", new JSCData[] { varBuffer, gst.get(SVARConstants.lastModel_Def).getJSCData(), new JSCString("info_key", "svar") }, new JSCData[] { varBuffer });
        JSCNArray[] retArgs = getRtnArgs();
        engine().call("var__IRA_CI_HEP", new JSCData[] { varBuffer, new JSCInt("numberOfRep", numberOfRep), new JSCInt("seed", seed), new JSCInt("periods", periods), new JSCNumber("coverage", coverage) }, retArgs);
        String[] efron_hall = getCINames();
        IRACIManager manager = IRACIManager.getInstance();
        JSCNArray[] gdEfron = new JSCNArray[] { retArgs[6], retArgs[7] };
        manager.addCI(efron_hall[0], modelType, gdEfron);
        JSCNArray[] gdHall = new JSCNArray[] { retArgs[8], retArgs[9] };
        manager.addCI(efron_hall[1], modelType, gdHall);
        setLocalResults(retArgs, true);
    }

    /**
     * @see ProcCall
     */
    private void runSVECCode() {
        seed = isSeed ? seed : 0;
        int estStrat = gst.get(VECMConstants.estimationStrategy_Def).getJSCInt().intVal();
        JSCNArray vecBuffer = new JSCNArray("vecmBuffer");
        VECMEstimationCall.setVecBuffer(engine(), gst, vecBuffer, estStrat);
        engine().call("vml_vdel", new JSCData[] { vecBuffer, new JSCSArray("toDelete", new String[] { "em", "r_est", "cir", "idx_equa" }) }, new JSCData[] { vecBuffer });
        engine().call("vml_SetCointRelation", new JSCData[] { vecBuffer, gst.get(VECMConstants.beta_Def).getJSCData(), gst.get(VECMConstants.beta_d_Def).getJSCData() }, new JSCData[] { vecBuffer });
        engine().call("vml_vput", new JSCData[] { vecBuffer, gst.get(SVECConstants.resB_Def).getJSCData(), new JSCString("info_key", "resB") }, new JSCData[] { vecBuffer });
        engine().call("vml_vput", new JSCData[] { vecBuffer, gst.get(SVECConstants.resC_Def).getJSCData(), new JSCString("info_key", "resC") }, new JSCData[] { vecBuffer });
        engine().call("vml_vput", new JSCData[] { vecBuffer, gst.get(SVECConstants.startValVector_Def).getJSCData(), new JSCString("info_key", "startVal") }, new JSCData[] { vecBuffer });
        engine().call("vml_vput", new JSCData[] { vecBuffer, gst.get(SVECConstants.relParamCh_Def).getJSCData(), new JSCString("info_key", "relParCh") }, new JSCData[] { vecBuffer });
        engine().call("vml_vput", new JSCData[] { vecBuffer, gst.get(SVECConstants.relLikCh_Def).getJSCData(), new JSCString("info_key", "relLikCh") }, new JSCData[] { vecBuffer });
        engine().call("vml_vput", new JSCData[] { vecBuffer, gst.get(SVECConstants.maxIter_Def).getJSCData(), new JSCString("info_key", "maxIter") }, new JSCData[] { vecBuffer });
        engine().call("vml_vput", new JSCData[] { vecBuffer, gst.get(SVECConstants.normConst_Def).getJSCData(), new JSCString("info_key", "normCon") }, new JSCData[] { vecBuffer });
        engine().call("vml_vput", new JSCData[] { vecBuffer, new JSCString("modelName", "SVEC"), new JSCString("info_key", "svar") }, new JSCData[] { vecBuffer });
        JSCNArray[] retArgs = getRtnArgs();
        engine().call("var__IRA_CI_HEP", new JSCData[] { vecBuffer, new JSCInt("numberOfRep", numberOfRep), new JSCInt("seed", seed), new JSCInt("periods", periods), new JSCNumber("coverage", coverage) }, retArgs);
        String[] efron_hall = getCINames();
        IRACIManager manager = IRACIManager.getInstance();
        JSCNArray[] gdEfron = new JSCNArray[] { retArgs[6], retArgs[7] };
        manager.addCI(efron_hall[0], modelType, gdEfron);
        JSCNArray[] gdHall = new JSCNArray[] { retArgs[8], retArgs[9] };
        manager.addCI(efron_hall[1], modelType, gdHall);
        setLocalResults(retArgs, true);
    }

    /**
     * @see ProcCall
     */
    private void runVARVECCode() {
        seed = isSeed ? seed : 0;
        JSCNArray varBuffer = new JSCNArray("varBuffer");
        if (modelType == ModelTypes.VAR) {
            VAREstimationCall.setVarBuffer(engine(), gst, varBuffer);
        } else {
            int estStrat = gst.get(VECMConstants.estimationStrategy_Def).getJSCInt().intVal();
            VECMEstimationCall.setVecBuffer(engine(), gst, varBuffer, estStrat);
        }
        JSCNArray[] retArgs = getRtnArgs();
        engine().call("var__IRA_CI_HEP", new JSCData[] { varBuffer, new JSCInt("numberOfRep", numberOfRep), new JSCInt("seed", seed), new JSCInt("periods", periods), new JSCNumber("coverage", coverage) }, retArgs);
        String[] efron_hall = getCINames();
        IRACIManager manager = IRACIManager.getInstance();
        JSCNArray[] gdEfron = new JSCNArray[] { retArgs[1], retArgs[2], retArgs[6], retArgs[7] };
        manager.addCI(efron_hall[0], modelType, gdEfron);
        JSCNArray[] gdHall = new JSCNArray[] { retArgs[3], retArgs[4], retArgs[8], retArgs[9] };
        manager.addCI(efron_hall[1], modelType, gdHall);
        setLocalResults(retArgs, false);
    }

    /**
     * @param retArgs
     */
    private void setLocalResults(JSCNArray[] retArgs, boolean isSVARVEC) {
        SymbolTable local = getSymbolTable();
        if (local != null) {
            if (!isSVARVEC) local.get(IRABootCIStudCall.FE_BOOT).setJSCData(retArgs[11]);
            local.get(IRABootCIStudCall.ORTH_BOOT).setJSCData(retArgs[12]);
        }
    }
}
