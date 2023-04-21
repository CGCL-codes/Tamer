package fr.soleil.TangoArchiving.ArchivingWatchApi.strategy.control.global;

/**
 * A factory for the IController singleton.
 * @author CLAISSE 
 */
public class ControllerFactory {

    /**
	 * Code for a DummyController implementation 
	 */
    public static final int DUMMY = 1;

    /**
	 * Code for a DoActionAtFullCycleController implementation 
	 */
    public static final int DO_ACTION_AT_FULL_CYCLE = 2;

    /**
	 * Code for a RetryAtFullCycleController implementation 
	 */
    public static final int RETRY_AT_FULL_CYCLE = 3;

    /**
     * Code for a DoActionAtEachStepController implementation 
     */
    public static final int DO_ACTION_AT_EACH_STEP = 4;

    /**
     * Code for a RetryAtEachStepController implementation 
     */
    public static final int RETRY_AT_EACH_STEP = 5;

    /**
     * Code for a RetryEveryThirdStepController implementation 
     */
    public static final int RETRY_EVERY_THIRD_STEP = 6;

    /**
      * Code for a RetryEveryLargeAmountOfStepsController implementation 
      */
    public static final int RETRY_EVERY_LARGE_AMOUNT_OF_STEPS = 7;

    private static IController currentImpl = null;

    /**
      * Instantiates and return the IController singleton
      * @param typeOfImpl The type of implementation
      * @return The required implementation
      */
    public static IController getImpl(int typeOfImpl) {
        switch(typeOfImpl) {
            case DUMMY:
                currentImpl = new DummyController();
                break;
            case DO_ACTION_AT_FULL_CYCLE:
                currentImpl = new DoActionAtFullCycleController();
                break;
            case RETRY_AT_FULL_CYCLE:
                currentImpl = new RetryAtFullCycleController();
                break;
            case DO_ACTION_AT_EACH_STEP:
                currentImpl = new DoActionAtEachStepController();
                break;
            case RETRY_AT_EACH_STEP:
                currentImpl = new RetryAtEachStepController();
                break;
            case RETRY_EVERY_THIRD_STEP:
                currentImpl = new RetryEveryThirdStepController();
                break;
            case RETRY_EVERY_LARGE_AMOUNT_OF_STEPS:
                currentImpl = new RetryEveryLargeAmountOfStepsController();
                break;
            default:
                throw new IllegalArgumentException("Expected either DUMMY_TYPE (1) or BASIC_TYPE (2) or SECOND_CHANCE_TYPE(3), got " + typeOfImpl + " instead.");
        }
        return currentImpl;
    }

    /**
      * Returns the current implementation singleton
      * @return The current implementation singleton
      */
    public static IController getCurrentImpl() {
        return currentImpl;
    }
}
