package org.jymc.jpydebug.jedit;

import sidekick.*;
import org.gjt.sp.jedit.*;
import org.gjt.sp.util.*;
import errorlist.*;
import org.jymc.jpydebug.*;
import java.util.*;

public class JPYPythonParser extends SideKickParser {

    private static final String _PYTHON_ = "JPyDebug";

    private static Hashtable _syntaxTrees = new Hashtable();

    private static PythonTreeNodeEventListener _listener = null;

    private static DefaultErrorSource _defaultErrorSource = null;

    public static PythonSyntaxTreeNode getPythonSyntaxTree(Buffer buffer) {
        return (PythonSyntaxTreeNode) _syntaxTrees.get(buffer);
    }

    public static synchronized void addPythonTreeNodeEventListener(PythonTreeNodeEventListener lstnr) {
        _listener = lstnr;
    }

    public static synchronized void removePythonTreeNodeEventListener(PythonTreeNodeEventListener lstnr) {
        if (_listener == lstnr) _listener = null;
    }

    public static JpyDbgErrorSource get_defaultErrorSource() {
        return new JEditDefaultErrorSource(_defaultErrorSource);
    }

    public JPYPythonParser() {
        super(_PYTHON_);
    }

    /**
   * check for local launch of Python Inspector stuff
   * @param pythonLoc
   * @param inspectorPyLoc
   * @param buffer
   */
    private PythonParsedData localPythonLaunch(Buffer buffer) throws PythonDebugException {
        String inspectorFName = FtpBuffers.checkBufferPath(buffer);
        PythonSyntaxTreeNode parsedNode = PythonInspector.launchInspector(inspectorFName, false);
        PylintInspector.launchPyLint(inspectorFName);
        if (parsedNode != null) {
            _syntaxTrees.put(buffer, parsedNode);
            if (_listener != null) _listener.newTreeNodeEvent(new PythonTreeNodeEvent(buffer, parsedNode));
            return new PythonParsedData(buffer, parsedNode);
        }
        return null;
    }

    /**
   *  Parse A python source providing either a Python tree 
   *  or a Syntax Error 
   */
    public SideKickParsedData parse(Buffer buffer, DefaultErrorSource errorSource) {
        Log.log(Log.DEBUG, this, "entering parser");
        _defaultErrorSource = errorSource;
        String pPath = PythonDebugParameters.get_currentShellPath();
        if (pPath == null) {
            PythonJeditPanel.loadProperties();
        }
        try {
            SideKickParsedData parsed = localPythonLaunch(buffer);
            return parsed;
        } catch (PythonDebugException e) {
            errorSource.addError(DefaultErrorSource.ERROR, buffer.getPath(), 0, 0, 0, e.getMessage());
        }
        return null;
    }
}
