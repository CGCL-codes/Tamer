package imtek.optsuite.acquisition.routine;

import org.eclipse.jface.wizard.IWizard;
import org.nightlabs.base.wizard.IWizardHop;

/**
 * @author Alexander Bieber <fleque@users.sourceforge.net>
 *
 */
public abstract class SimpleMeasurementRoutineStepFactory extends AbstractMeasurementRoutineStepFactory {

    /**
	 * 
	 */
    public SimpleMeasurementRoutineStepFactory() {
        super();
    }

    /**
	 * @see imtek.optsuite.acquisition.routine.MeasurementRoutineStepFactory#needsConfiguration()
	 */
    public boolean needsConfiguration() {
        return false;
    }

    /**
	 * @see imtek.optsuite.acquisition.routine.MeasurementRoutineStepFactory#getConfigurationHop()
	 */
    public IWizardHop getConfigurationHop() {
        return null;
    }

    /**
	 * @see imtek.optsuite.acquisition.routine.MeasurementRoutineStepFactory#discardConfigurationHop()
	 */
    public void discardConfigurationHop() {
    }

    /**
	 * @see imtek.optsuite.acquisition.routine.MeasurementRoutineStepFactory#createRoutineStepFromConfigurationWizard(imtek.optsuite.acquisition.routine.MeasurementRoutine, org.eclipse.jface.wizard.IWizard, org.nightlabs.base.wizard.IWizardHop)
	 */
    public MeasurementRoutineStep createRoutineStepFromConfigurationWizard(MeasurementRoutine routine, IWizard wizard, IWizardHop wizardHop) {
        return createMeasurementRoutineStep(routine);
    }
}
