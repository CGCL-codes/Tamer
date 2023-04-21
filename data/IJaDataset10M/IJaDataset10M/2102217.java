package tcl.lang;

import java.util.*;

/**
 * This class implements a frame in the call stack.
 *
 * This class can be overridden to define new variable scoping rules for
 * the Tcl interpreter.
 */
public class CallFrame {

    /**
     * The interpreter associated with this call frame.
     */
    Interp interp;

    /**
     * The Namespace this CallFrame is executing in.
     * Used to resolve commands and global variables.
     */
    Namespace ns;

    /**
     * If true, the frame was pushed to execute a Tcl procedure
     * and may have local vars. If false, the frame was pushed to execute
     * a namespace command and var references are treated as references
     * to namespace vars; varTable is ignored.
     */
    boolean isProcCallFrame;

    /**
     * Stores the arguments of the procedure associated with this CallFrame.
     * Is null for global level.
     */
    TclObject[] objv;

    /**
     * Value of interp.frame when this procedure was invoked
     * (i.e. next in stack of all active procedures).
     */
    CallFrame caller;

    /**
     * Value of interp.varFrame when this procedure was invoked
     * (i.e. determines variable scoping within caller; same as
     * caller unless an "uplevel" command or something equivalent
     * was active in the caller).
     */
    CallFrame callerVar;

    /**
     * Level of recursion. = 0 for the global level.
     */
    int level;

    /**
     * Stores the variables of this CallFrame.
     */
    HashMap varTable;

    /**
     * Array of local variables in a compiled proc frame.
     * These include locals set in the proc, globals
     * or other variable brought into the proc scope,
     * and compiler generated aliases to globals.
     * This array is always null for an interpreted proc.
     * A compiled proc implementation known which variable
     * is associated with  each slot at compile time,
     * so it is able to avoid a hashtable lookup each time
     * the variable is accessed. Both scalar variables and
     * array variables could appear in this array.
     */
    Var[] compiledLocals;

    String[] compiledLocalsNames;

    /**
     * Creates a CallFrame for the global variables.
     * @param interp current interpreter.
     */
    CallFrame(Interp i) {
        interp = i;
        ns = i.globalNs;
        varTable = null;
        compiledLocals = null;
        compiledLocalsNames = null;
        caller = null;
        callerVar = null;
        objv = null;
        level = 0;
        isProcCallFrame = true;
    }

    /**
     * Creates a CallFrame. It changes the following variables:
     *
     * <ul>
     *     <li> this.caller
     *	   <li> this.callerVar
     *	   <li> interp.frame
     *	   <li> interp.varFrame
     * </ul>
     * @param i current interpreter.
     * @param proc the procedure to invoke in this call frame.
     * @param objv the arguments to the procedure.
     * @exception TclException if error occurs in parameter bindings.
     */
    CallFrame(Interp i, Procedure proc, TclObject[] objv) throws TclException {
        this(i);
        try {
            chain(proc, objv);
        } catch (TclException e) {
            dispose();
            throw e;
        }
    }

    /**
     * Chain this frame into the call frame stack and binds the parameters
     * values to the formal parameters of the procedure.
     *
     * @param proc the procedure.
     * @param proc argv the parameter values.
     * @exception TclException if wrong number of arguments.
     */
    void chain(Procedure proc, TclObject[] objv) throws TclException {
        this.ns = proc.wcmd.ns;
        this.objv = objv;
        level = (interp.varFrame == null) ? 1 : (interp.varFrame.level + 1);
        caller = interp.frame;
        callerVar = interp.varFrame;
        interp.frame = this;
        interp.varFrame = this;
        int numArgs = proc.argList.length;
        if ((!proc.isVarArgs) && (objv.length - 1 > numArgs)) {
            wrongNumProcArgs(objv[0], proc);
        }
        int i, j;
        for (i = 0, j = 1; i < numArgs; i++, j++) {
            TclObject varName = proc.argList[i][0];
            TclObject value = null;
            if ((i == (numArgs - 1)) && proc.isVarArgs) {
                value = TclList.newInstance();
                value.preserve();
                for (int k = j; k < objv.length; k++) {
                    TclList.append(interp, value, objv[k]);
                }
                interp.setVar(varName, value, 0);
                value.release();
            } else {
                if (j < objv.length) {
                    value = objv[j];
                } else if (proc.argList[i][1] != null) {
                    value = proc.argList[i][1];
                } else {
                    wrongNumProcArgs(objv[0], proc);
                }
                interp.setVar(varName, value, 0);
            }
        }
    }

    private String wrongNumProcArgs(TclObject name, Procedure proc) throws TclException {
        int i;
        StringBuffer sbuf = new StringBuffer(200);
        sbuf.append("wrong # args: should be \"");
        sbuf.append(name.toString());
        for (i = 0; i < proc.argList.length; i++) {
            TclObject arg = proc.argList[i][0];
            TclObject def = proc.argList[i][1];
            sbuf.append(" ");
            if (def != null) sbuf.append("?");
            sbuf.append(arg.toString());
            if (def != null) sbuf.append("?");
        }
        sbuf.append("\"");
        throw new TclException(interp, sbuf.toString());
    }

    /**
     * @param name the name of the variable.
     *
     * @return true if a variable exists and is defined inside this
     *     CallFrame, false otherwise
     */
    static boolean exists(Interp interp, String name) {
        try {
            Var[] result = Var.lookupVar(interp, name, null, 0, "lookup", false, false);
            if (result == null) {
                return false;
            }
            if (result[0].isVarUndefined()) {
                return false;
            }
            return true;
        } catch (TclException e) {
            throw new TclRuntimeError("unexpected TclException: " + e);
        }
    }

    ArrayList getVarNames() {
        ArrayList alist = new ArrayList();
        if (varTable == null) {
            return alist;
        }
        for (Iterator iter = varTable.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            Var v = (Var) entry.getValue();
            if (!v.isVarUndefined()) {
                alist.add(v.hashKey);
            }
        }
        return alist;
    }

    /**
     * @return a List of the names of the (defined) local variables
     *     in this CallFrame (excluding upvar's)
     */
    ArrayList getLocalVarNames() {
        ArrayList alist = new ArrayList();
        if (varTable == null) {
            return alist;
        }
        for (Iterator iter = varTable.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            Var v = (Var) entry.getValue();
            if (!v.isVarUndefined() && !v.isVarLink()) {
                alist.add(v.hashKey);
            }
        }
        return alist;
    }

    /**
     * Tcl_GetFrame -> getFrame
     *
     *	Given a description of a procedure frame, such as the first
     *	argument to an "uplevel" or "upvar" command, locate the
     *	call frame for the appropriate level of procedure.
     *
     *	The return value is 1 if string was either a number or a number
     *  preceded by "#" and it specified a valid frame. 0 is returned
     *  if string isn't one of the two things above (in this case,
     *  the lookup acts as if string were "1"). The frameArr[0] reference
     *  will be filled by the reference of the desired frame (unless an
     *  error occurs, in which case it isn't modified).
     *
     * @param string a string that specifies the level.

     * @exception TclException if s is a valid level specifier but
     *     refers to a bad level that doesn't exist.
     */
    static int getFrame(Interp interp, String string, CallFrame[] frameArr) throws TclException {
        int curLevel, level, result;
        CallFrame frame;
        result = 1;
        curLevel = (interp.varFrame == null) ? 0 : interp.varFrame.level;
        if ((string.length() > 0) && (string.charAt(0) == '#')) {
            level = Util.getInt(interp, string.substring(1));
            if (level < 0) {
                throw new TclException(interp, "bad level \"" + string + "\"");
            }
        } else if ((string.length() > 0) && Character.isDigit(string.charAt(0))) {
            level = Util.getInt(interp, string);
            level = curLevel - level;
        } else {
            level = curLevel - 1;
            result = 0;
        }
        if (level == 0) {
            frame = null;
        } else {
            for (frame = interp.varFrame; frame != null; frame = frame.callerVar) {
                if (frame.level == level) {
                    break;
                }
            }
            if (frame == null) {
                throw new TclException(interp, "bad level \"" + string + "\"");
            }
        }
        frameArr[0] = frame;
        return result;
    }

    /**
     * This method is called when this CallFrame is no longer needed.
     * Removes the reference of this object from the interpreter so
     * that this object can be garbage collected.
     * <p>
     * For this procedure to work correctly, it must not be possible
     * for any of the variable in the table to be accessed from Tcl
     * commands (e.g. from trace procedures).
     */
    protected void dispose() {
        interp.frame = caller;
        interp.varFrame = callerVar;
        caller = null;
        callerVar = null;
        if (varTable != null) {
            Var.deleteVars(interp, varTable);
            varTable = null;
        }
        if (compiledLocals != null) {
            Var.deleteVars(interp, compiledLocals);
            compiledLocals = null;
            compiledLocalsNames = null;
        }
    }
}
