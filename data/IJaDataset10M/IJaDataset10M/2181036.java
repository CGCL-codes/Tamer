package org.dynalang.debug.rhino;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.WeakHashMap;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.debug.DebugFrame;
import org.mozilla.javascript.debug.DebuggableScript;
import org.mozilla.javascript.debug.Debugger;

/**
 * Implementation of a Rhino {@link Debugger} interface that provides hooks 
 * into Rhino script executions. Objects of this class are immutable, it is 
 * sufficient to have only one and share it across all Rhino contexts using 
 * {@link Context#setDebugger(Debugger, Object)}.
 * @author Attila Szegedi
 * @version $Id: RhinoDebugger.java,v 1.3 2009/02/23 13:42:51 szegedia Exp $
 */
public class RhinoDebugger implements Debugger {

    public DebugFrame getFrame(Context cx, DebuggableScript scriptOrFn) {
        return new DebuggedRhinoFrame(scriptOrFn, (DebuggedRhinoExecution) cx.getDebuggerContextData());
    }

    public void handleCompilationDone(Context ctx, DebuggableScript scriptOrFn, String source) {
    }
}
