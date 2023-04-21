package com.leohart.buildwhisperer;

/**
 * @author Leo Hart
 */
public class PartTimeDecorator extends PoweredBuildStatusIndicatorDecorator {

    BuildStatusIndicatorTurnOffCriteria turnOffCriteria;

    /**
	 * @param indicator
	 */
    public PartTimeDecorator(PoweredBuildStatusIndicator indicator, BuildStatusIndicatorTurnOffCriteria turnOffCriteria) {
        super(indicator);
        this.turnOffCriteria = turnOffCriteria;
    }

    public void indicate(BuildStatus status) {
        if (!this.wasTurnedOff()) {
            this.indicator.indicate(status);
        }
    }

    /**
	 * @see com.leohart.buildwhisperer.BuildStatusIndicator#indicate(com.leohart.buildwhisperer.BuildStatus[])
	 */
    public void indicate(BuildStatus[] statuses) throws BuildStatusIndicatorException {
        if (!this.wasTurnedOff()) {
            this.indicator.indicate(statuses);
        }
    }

    private boolean wasTurnedOff() {
        if (this.turnOffCriteria.shouldTurnOff()) {
            this.turnOff();
            return true;
        }
        return false;
    }

    /**
	 * @see com.leohart.buildwhisperer.PoweredBuildStatusIndicator#turnOff()
	 */
    public void turnOff() {
        this.indicator.turnOff();
    }
}
