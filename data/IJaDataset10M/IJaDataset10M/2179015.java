package il.ac.biu.cs.grossmm.api.cpp;

/**
 * State of the subscription. This state consist of the subsciption status
 * (success or failure) and the remaining duration, in seconds.
 */
public class State {

    private final boolean isSuccess;

    private final int duration;

    /**
	 * Creates a nde state object.
	 * 
	 * @param status
	 *            status of the subscription
	 * @param duration
	 *            duration of the subscription
	 */
    public State(final boolean isSuccess, final int duration) {
        super();
        this.isSuccess = isSuccess;
        this.duration = duration;
    }

    /**
	 * Gets duration of the subscription
	 * 
	 * @return remainig duration of the subscription
	 */
    public int getDuration() {
        return duration;
    }

    /**
	 * Queries whether subscription status is a success
	 * 
	 * @return true if subscription status is a success, otherwise returns false
	 */
    public boolean isSuccess() {
        return isSuccess;
    }

    @Override
    public String toString() {
        if (isSuccess) return "Success-" + duration; else return "Failure";
    }
}
