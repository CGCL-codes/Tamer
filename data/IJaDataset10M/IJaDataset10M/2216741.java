package playground.yu.integration.cadyts.parameterCalibration.withCarCounts.generalNormal.paramCorrection;

import org.matsim.core.config.Config;
import org.matsim.core.controler.Controler;
import org.matsim.core.scoring.ScoringFunctionFactory;
import playground.yu.integration.cadyts.parameterCalibration.withCarCounts.parametersCorrection.BseParamCalibrationControlerListener;
import playground.yu.integration.cadyts.parameterCalibration.withCarCounts.scoring.PlansScoring4PC_I;

/**
 * @author yu
 * 
 */
public abstract class BseParamCalibrationControler extends Controler {

    protected BseParamCalibrationControlerListener extension;

    protected PlansScoring4PC_I plansScoring4PC;

    public BseParamCalibrationControler(Config config) {
        super(config);
    }

    public BseParamCalibrationControler(String[] args) {
        super(args);
    }

    public PlansScoring4PC_I getPlansScoring4PC() {
        return plansScoring4PC;
    }

    @Override
    protected abstract void loadCoreListeners();

    @Override
    protected abstract ScoringFunctionFactory loadScoringFunctionFactory();
}
