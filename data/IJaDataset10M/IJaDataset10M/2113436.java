package TangramBase;

import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import Pentominos.Pentominos;
import TangramCore.Database;
import TangramCore.Skladacka;

public class PentominoSolverFrame extends Pentominos implements DocumentWindow, WindowStateListener {

    static final long serialVersionUID = 24362462L;

    private JMenuItem mniThis = null;

    private int iWindow = -1, lastState;

    TangramFrame frmDatabase;

    Skladacka skl;

    boolean changed = false;

    public static final String solverName = "Pentomino Solver";

    public PentominoSolverFrame(TangramFrame tf, Database d, int rows, int cols) {
        super("Pentomino Solver", rows, cols, tf);
        frmDatabase = tf;
        skl = d.getActSet();
        setTitle();
        addWindowStateListener(this);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                showDocument(false);
            }
        });
    }

    public void restoreState() {
        setExtendedState(lastState);
        toFront();
    }

    private void setMenuItemText() {
        if (mniThis != null) mniThis.setText(sLNumberBracket + iWindow + sRNumberBracket + getTitle());
    }

    public void setMenuItem(int i, JMenuItem mni) {
        iWindow = i;
        mniThis = mni;
        setMenuItemText();
    }

    public void windowStateChanged(WindowEvent ev) {
        if ((ev.getNewState() & JFrame.ICONIFIED) != 0) lastState = ev.getOldState();
    }

    @Override
    public void setVisible(boolean jak) {
        super.setVisible(jak);
        if (jak == false) showDocument(false);
    }

    public void setTitle() {
        setTitle(solverName + sLFigureBracket + panel.nazev + (changed ? sChanged : sUpToDate) + sRFigureBracket);
        setMenuItemText();
    }

    public boolean showDocument(boolean jak) {
        boolean ret = jak ? frmDatabase.windowOpened(this) : frmDatabase.windowClosed(this);
        super.setVisible(jak);
        if (jak) toFront();
        return ret;
    }

    public boolean hasChanged() {
        return false;
    }
}
