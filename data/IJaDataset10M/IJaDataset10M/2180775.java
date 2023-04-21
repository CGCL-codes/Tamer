package playground.dgrether.daganzosignal;

import org.apache.log4j.Logger;
import org.matsim.core.basic.signalsystems.BasicSignalGroupDefinition;
import org.matsim.core.basic.signalsystemsconfig.BasicAdaptiveSignalSystemControlInfo;
import org.matsim.signalsystems.control.AdaptiveSignalSystemControlerImpl;

/**
 * @author dgrether
 *
 */
public class AdaptiveController extends AdaptiveSignalSystemControlerImpl {

    private static final Logger log = Logger.getLogger(AdaptiveController.class);

    /**
	 * @param controlInfo
	 */
    public AdaptiveController(BasicAdaptiveSignalSystemControlInfo controlInfo) {
        super(controlInfo);
    }

    /**
	 * @see org.matsim.signalsystems.control.SignalSystemControler#givenSignalGroupIsGreen(org.matsim.core.basic.signalsystems.BasicSignalGroupDefinition)
	 */
    public boolean givenSignalGroupIsGreen(BasicSignalGroupDefinition signalGroup) {
        log.info("isGreen?");
        return true;
    }
}
