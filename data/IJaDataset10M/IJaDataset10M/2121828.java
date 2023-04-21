package org.sodbeans.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.util.Lookup;

/**
 *
 * @author Andreas Stefik
 */
public class SodbeansStepBackOverAction implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        org.sodbeans.compiler.api.Compiler compiler = Lookup.getDefault().lookup(org.sodbeans.compiler.api.Compiler.class);
        if (!compiler.isDebuggerActive()) return;
        compiler.stepBackOver();
    }
}
