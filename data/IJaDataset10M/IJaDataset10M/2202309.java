package net.sf.refactorit.netbeans.common.action;

import net.sf.refactorit.netbeans.common.NbOptionsAction;

/**
 *
 * @author  RISTO A
 */
public class NbOptionsNBAction extends RitActionDelegate {

    protected net.sf.refactorit.commonIDE.IdeAction getRitAction() {
        return new NbOptionsAction();
    }
}
