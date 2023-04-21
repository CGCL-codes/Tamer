package se.krka.kahlua.vm;

public interface JavaFunction {

    /**
	 * General contract<br>
	 * <br>
	 *  Input:<br>
	 *  callFrame = the frame that contains all the arguments, and where all the results should be put.<br> 
	 *  nArgs = number of function arguments<br>
	 *  callFrame.get(i) = an argument (0 <= i < nArgs)<br> 
	 *  
	 * @param callFrame - the current callframe for the function 
	 * @param nArguments - number of function arguments 
	 * @return N - number of return values. The N top objects on the stack are considered the return values 
	 */
    public abstract int call(LuaCallFrame callFrame, int nArguments);
}
