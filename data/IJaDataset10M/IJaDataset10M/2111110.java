package ge.lowlevel;

/**
 * 
 * @author Abel
 *	Interval is an object with one function, run, who is going to be called 
 *	for an interval function
 */
public abstract class Interval {

    private Object concretTimer;

    public Object getConcretTimer() {
        return concretTimer;
    }

    public void setConcretTimer(Object concretTimer) {
        this.concretTimer = concretTimer;
    }

    public Interval() {
    }

    /**
	 * 	This function has to be defined 
	 */
    public abstract void run();
}
