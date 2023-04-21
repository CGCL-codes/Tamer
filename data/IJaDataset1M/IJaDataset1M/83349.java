package ch.sahits.codegen.addon.internal.wizards;

/**
 * Container holding the next steps to be taken.
 * This class is immutable and hence thread safe.
 * @author andi
 *
 */
final class PropagationSteps {

    private final ENextPropagationState firstStep;

    private final ENextPropagationState secondStep;

    public PropagationSteps(ENextPropagationState firstStep, ENextPropagationState secondStep) {
        super();
        this.firstStep = firstStep;
        this.secondStep = secondStep;
    }

    /**
	 * @return the firstStep
	 */
    public ENextPropagationState getFirstStep() {
        return firstStep;
    }

    /**
	 * @return the secondStep
	 */
    public ENextPropagationState getSecondStep() {
        return secondStep;
    }
}
