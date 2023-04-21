package tester;

/**
 * Wrapper class for the arguments that we will be passing to the
 * tester in checkEffect methods.  This prevents us from requiring
 * students to use arrays.  
 * @author Weston Jossey
 * @since December 12 2008
 * @version 2.0
 *
 */
public class Arguments {

    private Object[] args;

    /**
	 * Takes in any number of arguments, including null.  The arguments
	 * are ordered based on their input, so order is important.    
	 * @param args Arguments to wrap
	 */
    public Arguments(Object... args) {
        this.args = args;
    }

    /**
	 * Returns an array of all the arguments.
	 * @return
	 */
    public Object[] getArgs() {
        return args;
    }

    public Class<?>[] getArgumentClasses() {
        Class<?>[] classArray = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            classArray[i] = args[i].getClass();
        }
        return classArray;
    }
}
