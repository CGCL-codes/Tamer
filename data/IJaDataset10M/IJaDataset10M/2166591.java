package playground.yu.integration.cadyts.parameterCalibration.withCarCounts.experiment.general.paramCorrection;

import org.matsim.core.config.Config;
import org.matsim.core.controler.Controler;
import org.matsim.core.scoring.ScoringFunctionFactory;
import playground.yu.integration.cadyts.parameterCalibration.withCarCounts.experiment.general.scoring.PlansScoring4PC;
import playground.yu.integration.cadyts.parameterCalibration.withCarCounts.experiment.general.withLegModeASC.CharyparNagelScoringFunctionFactory4PC;

/**
 * @author yu
 * 
 */
public abstract class BseParamCalibrationControler extends Controler {

    protected BseParamCalibrationControlerListener extension;

    protected PlansScoring4PC plansScoring4PC;

    public BseParamCalibrationControler(String[] args) {
        super(args);
    }

    public BseParamCalibrationControler(Config config) {
        super(config);
    }

    public PlansScoring4PC getPlansScoring4PC() {
        return plansScoring4PC;
    }

    protected ScoringFunctionFactory loadScoringFunctionFactory() {
        return new CharyparNagelScoringFunctionFactory4PC(config.planCalcScore());
    }

    @Override
    protected abstract void loadCoreListeners();
}
