package org.jymc.jpydebug.swing.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.jymc.jpydebug.*;

/**
 * @author jean-yves
 *
 * JEdit independent Python monitoring frontend This class provides a 'clean
 * sanity' isolation between JEDIT and Techprint Debugger and MUST ALLWAYS BE
 * DEBUGGED outside JEDIT
 *
 */
public class PythonDebugFrontEnd {

    private Container _container;

    private PythonDebugContainer _debugger;

    /** default listening port for debugging solicitor */
    private int _listeningPort = 29000;

    /** full path to pyhon executable location */
    private String _pythonShellPath = null;

    /** if not null JpyDbg to connect to */
    private String _dbgHost = null;

    /** python debugger dameon script name */
    private String _jpydbgScript = "jpydbg.py";

    public PythonDebugFrontEnd(Container container) {
        _container = container;
    }

    public void set_listeningPort(int port) {
        PythonDebugParameters.set_listeningPort(port);
    }

    public int get_listeningPort() {
        return _listeningPort;
    }

    public void set_pythonShellPath(String path) {
        PythonDebugParameters.set_pythonShellPath(path);
    }

    public String get_pythonShellPath() {
        return _pythonShellPath;
    }

    public void set_dbgHost(String dbgHost) {
        PythonDebugParameters.set_dbgHost(dbgHost);
    }

    public String get_dbgHost() {
        return _dbgHost;
    }

    public void set_jpydbgScript(String jpydbgScript) {
        PythonDebugParameters.set_jpydbgScript(jpydbgScript);
    }

    public String get_jpydbgScript() {
        return _jpydbgScript;
    }

    public void activate() {
        _debugger = new PythonDebugContainer(_container);
    }

    public void terminate() {
        try {
            _debugger.terminate();
        } catch (PythonDebugException e) {
            e.printStackTrace();
        }
    }

    private static PythonDebugFrontEnd application = null;

    public static void main(String[] args) {
        JFrame f = new JFrame("test Python Debug Frame");
        application = new PythonDebugFrontEnd(f.getContentPane());
        f.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                application.terminate();
                System.exit(0);
            }
        });
        application.activate();
        f.pack();
        f.setVisible(true);
    }
}
