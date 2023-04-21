package org.teambeans.sodbeans.windows.api;

import javax.swing.SwingUtilities;
import org.openide.util.Lookup;
import org.sodbeans.compiler.api.CompilerEvent;
import org.sodbeans.compiler.api.CompilerListener;
import org.teambeans.sodbeans.api.hop.WindowService;
import org.teambeans.sodbeans.windows.winLocalVariableTopComponent;
import org.teambeans.sodbeans.windows.winOutputTopComponent;

/**
 *
 * @author Andrew Haywood
 */
public class WindowServiceImpl extends WindowService {

    org.sodbeans.compiler.api.Compiler compiler = Lookup.getDefault().lookup(org.sodbeans.compiler.api.Compiler.class);

    public WindowServiceImpl() {
        compiler.addListener(new CompilerListener() {

            public void actionPerformed(final CompilerEvent event) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        if (event.isBuildEvent()) {
                            int errors = compiler.getCompilerErrorManager().getNumberOfSyntaxErrors();
                            if (errors > 0) compilerErrorWindowStart(); else if (errors == 0) {
                                compilerErrorWindowStop();
                            }
                        }
                        if (event.isDebuggerStopEvent()) {
                            localVariableWindowStop();
                            compilerErrorWindowStop();
                        } else if (event.isDebuggerStartEvent()) {
                            compilerErrorWindowStart();
                            localVariableWindowStart();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void outputWindowPrint(String message) {
        winOutputTopComponent win = winOutputTopComponent.findInstance();
        if (win != null) win.Print(message);
    }

    @Override
    public void outputWindowPrintLn(String message) {
        winOutputTopComponent win = winOutputTopComponent.findInstance();
        if (win != null) win.Print(message + "\n");
    }

    @Override
    public void compilerErrorWindowStart() {
        winOutputTopComponent window = winOutputTopComponent.findInstance();
        window.setVisible(true);
        window.startOutputWindow();
        window.updateOutput();
        window.requestVisible();
    }

    @Override
    public void compilerErrorWindowUpdate() {
        winOutputTopComponent win = winOutputTopComponent.findInstance();
        win.updateOutput();
    }

    @Override
    public void compilerErrorWindowStop() {
        winOutputTopComponent win = winOutputTopComponent.findInstance();
        win.stopOutputWindow();
    }

    @Override
    public void localVariableWindowStart() {
        winLocalVariableTopComponent win = winLocalVariableTopComponent.findInstance();
        win.setVisible(true);
        win.startLocalVariableWindow();
        win.updateLocalVariables();
        win.open();
        win.requestVisible();
    }

    @Override
    public void localVariableWindowUpdate() {
        winLocalVariableTopComponent win = winLocalVariableTopComponent.findInstance();
        win.updateLocalVariables();
    }

    @Override
    public void localVariableWindowStop() {
        winLocalVariableTopComponent win = winLocalVariableTopComponent.findInstance();
        win.stopLocalVariableWindow();
        win.close();
    }
}
