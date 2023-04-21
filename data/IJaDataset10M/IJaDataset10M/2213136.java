package org.ibex.js;

import org.ibex.js.parse.Parser.ScopeInfo;

public class Instr {

    public static JS devl;

    public static Interpreter interpreter;

    public static Debug debug;

    public static Memory memory;

    public static interface Interpreter {

        /**
		 * Called before the execution of every byte code in the intepreter (if debugging
		 * is on). The params provide sufficient information to display the current
		 * execution state.
		 * @param interpreter 
		 */
        public void handle(org.ibex.js.Interpreter interpreter);
    }

    /**
	 * Provides a link for hooking in a debugger. For the implementation see org.vexi.debug.
	 * <br>
	 * Seperated from the implementation mainly so that there is no compile time dependency on the
	 * implementation (which also includes xml-rpc server) for a smaller default build.
	 * 
	 * @author mike
	 *
	 */
    public static interface Debug extends Interpreter {

        /**	Called from the parser. Preserves local variable name information 
		 * 
		 * @param si           - ScopeInfo, maps scope slots to variable names
		 */
        public abstract void putScopeInfo(ScopeInfo si);

        public void threadFinished(Thread t);

        public void threadCurrent(Thread t);

        public void threadScheduled(Thread t);

        public void threadSleep(Thread t);
    }

    public static interface Memory {

        public void register(JS o);

        public void unregister(JS o);
    }
}
