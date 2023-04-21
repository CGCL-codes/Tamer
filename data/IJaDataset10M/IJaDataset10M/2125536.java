package playground.yu.parameterSearch.LJ;

import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import playground.yu.scoring.withAttrRecorder.leftTurn.LeftTurnPenaltyControler;
import playground.yu.tests.parameterCalibration.naiveWithoutUC.SimCntLogLikelihoodCtlListener;

public class LJParameterHunter extends LeftTurnPenaltyControler {

    private final SimCntLogLikelihoodCtlListener llhListener;

    public LJParameterHunter(Config cfg) {
        super(cfg);
        llhListener = new SimCntLogLikelihoodCtlListener();
        addControlerListener(llhListener);
        addControlerListener(new LJParaemterSearchListener());
        setOverwriteFiles(true);
        setCreateGraphs(false);
        run();
    }

    public SimCntLogLikelihoodCtlListener getLlhListener() {
        return llhListener;
    }

    public static void main(String[] args) {
        new LJParameterHunter(ConfigUtils.loadConfig(args[0]));
    }
}
