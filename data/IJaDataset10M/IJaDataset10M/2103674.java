package net.sf.zcatalog.ui.wiz;

import java.awt.Container;
import javax.swing.JButton;
import zizzi.wizard.Wizard;
import zizzi.wizard.WizardState;
import net.sf.zcatalog.fs.*;

/**
 *
 * @author Alessandro Zigliani
 */
public class OW08DataExtState extends WizardState {

    private WizardState nextState;

    private Thread thread;

    private OW08DataExtPane page;

    protected OW08DataExtState(Wizard wiz) {
        super(wiz);
        page = new OW08DataExtPane();
        thread = new Thread() {

            private Traverser trav;

            @Override
            public void run() {
                trav = (Traverser) wizard.getAttribute("TRAVERSER");
                next();
            }
        };
        thread.setPriority(Thread.MIN_PRIORITY);
        page = new OW08DataExtPane();
        nextState = new OW09CollectionState(wizard);
    }

    @Override
    protected void next() {
        wizard.setState(nextState);
    }

    @Override
    protected void prev() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void finish() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void stop() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected Container getPage() {
        thread.start();
        return page;
    }

    @Override
    protected void getBtnStates(JButton prev, JButton next, JButton finish, JButton cancel, JButton stop) {
        prev.setVisible(false);
        prev.setEnabled(false);
        next.setVisible(true);
        next.setEnabled(false);
        finish.setVisible(false);
        finish.setEnabled(false);
        cancel.setVisible(true);
        cancel.setEnabled(true);
        stop.setVisible(true);
        stop.setEnabled(true);
    }
}
