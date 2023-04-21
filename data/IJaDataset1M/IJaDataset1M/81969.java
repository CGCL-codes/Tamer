package net.sf.cplab.experiment.course;

/**
 * @author James Tse
 * 
 */
public class StaircaseMethod extends MethodOfLimits {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7518302622066035866L;

    public StaircaseMethod() {
        super();
        setStaircaseMode(true);
        setTitle("Staircase Method");
    }

    public static final void main(String[] args) {
        StaircaseMethod method = new StaircaseMethod();
        method.launch();
    }
}
