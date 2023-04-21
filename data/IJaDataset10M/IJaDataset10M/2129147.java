package edu.co.unal.bioing.jnukak3d.Dicom.ui;

import edu.co.unal.bioing.jnukak3d.Dicom.nkDicomNodeTree;
import edu.co.unal.bioing.jnukak3d.event.nkEvent;
import edu.co.unal.bioing.jnukak3d.event.nkEventListener;
import edu.co.unal.bioing.jnukak3d.event.nkEventGenerator;
import edu.co.unal.bioing.jnukak3d.nkDebug;
import edu.co.unal.bioing.jnukak3d.Dicom.io.nkDicomImport;
import javax.swing.JDialog;
import javax.swing.WindowConstants;
import jwf.Wizard;
import jwf.WizardListener;
import java.util.Vector;
import javax.swing.JFrame;

/**
 *
 * @author Alexander Pinzon Fernandez
 */
public class nkWizardDicomImport extends JDialog implements WizardListener, nkEventGenerator {

    protected static final boolean DEBUG = nkDebug.DEBUG;

    public nkWizardDicomImport(JFrame parent) {
        super(parent);
        Wizard nkw = new Wizard();
        nkw.addWizardListener(this);
        setTitle("jNukak3D: Dicom import");
        this.setContentPane(nkw);
        nkWizardPage1 page1 = new nkWizardPage1();
        nkw.start(page1);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }

    /** Called when the wizard finishes.
     * @param wizard the wizard that finished.
     */
    public void wizardFinished(Wizard wizard) {
        firenkMenuEvent(wizard);
        dispose();
    }

    /** Called when the wizard is cancelled.
     * @param wizard the wizard that was cancelled.
     */
    public void wizardCancelled(Wizard wizard) {
        dispose();
    }

    /** Called when a new panel has been displayed in the wizard.
     * @param wizard the wizard that was updated
     */
    public void wizardPanelChanged(Wizard wizard) {
        if (DEBUG) System.out.println("wizard new panel");
    }

    private Vector prv_nkEvent_listeners;

    public final synchronized void addnkEventListener(nkEventListener pl) {
        if (null == prv_nkEvent_listeners) prv_nkEvent_listeners = new Vector();
        if (null == pl || prv_nkEvent_listeners.contains(pl)) return;
        prv_nkEvent_listeners.addElement(pl);
    }

    public final synchronized void removenkEventListener(nkEventListener pl) {
        if (null != prv_nkEvent_listeners && null != pl) prv_nkEvent_listeners.removeElement(pl);
    }

    public void firenkMenuEvent(Wizard wizard) {
        if (null == prv_nkEvent_listeners) return;
        nkDicomNodeTree selectedNode;
        selectedNode = ((nkDicomNodeTree) (wizard.getWizardContext().getAttribute("nkDicomNodeTreeSelected")));
        nkDicomImport nkio;
        nkio = ((nkDicomImport) (wizard.getWizardContext().getAttribute("nkDicomImport")));
        if (nkio == null || selectedNode == null) return;
        nkEvent e = new nkEvent(this);
        e.setAttribute("nkDicomNodeTreeSelected", selectedNode);
        e.setAttribute("nkDicomImportSelected", nkio);
        for (int i = 0; i < prv_nkEvent_listeners.size(); ++i) ((nkEventListener) prv_nkEvent_listeners.elementAt(i)).nkEventInvoke(e);
    }
}
