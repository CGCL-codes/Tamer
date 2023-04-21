package net.sf.appomatox.berechnungen;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import net.sf.appomatox.control.AppoController;
import net.sf.appomatox.control.BerechnungenController;
import net.sf.appomatox.gui.DiagrammController;
import net.sf.appomatox.utils.Utils;
import com.jgoodies.forms.layout.FormLayout;

public abstract class Berechnung extends JPanel {

    protected BerechnungenController m_BerechnungenCtrl = null;

    protected DiagrammController m_DiagrammCtrl = null;

    private AppoController m_AppoCtrl = null;

    protected Berechnung() {
    }

    public String getBeschreibung() {
        return null;
    }

    /**
     * Die Logik zum Berechnen des Ergebnisses. Diese Methode sollte nur
     * innerhalb dieser Klasse aufgerufen werden.
     * @throws InterruptedException Falls die Berechnung (z.B. vom Benutzer) abgebrochen
     * wurde.
     */
    public abstract void berechnen() throws InterruptedException;

    public final void setController(AppoController bc) {
        assert m_AppoCtrl == null : "Methode wurde mehrfach aufgerufen";
        m_BerechnungenCtrl = bc.getBerechungenController();
        m_DiagrammCtrl = bc.getDiagrammController();
        m_AppoCtrl = bc;
    }

    protected final Action getActBerechnen() {
        return new AbstractAction("Berechnen") {

            {
                putValue(Action.SMALL_ICON, Utils.getIcon("/res/32x32/Crystal_Clear_action_forward.png"));
                putValue(Action.SHORT_DESCRIPTION, "Startet die Berechnung");
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                m_BerechnungenCtrl.berechne(Berechnung.this);
            }
        };
    }

    public Action[] getActions() {
        return new Action[] { getActBerechnen() };
    }

    protected FormLayout getStandardLayout() {
        return new FormLayout("right:max(60dlu;p), 4dlu, 100dlu:grow", "p, 10dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu");
    }

    /**
     * Die �bergebene Komponente erh�lt den Fukus, sobald die Berechnung
     * gestartet wurde.
     * @param comp Die Komponente, die den Fokus erhalten soll.
     */
    protected void setFocusComponent(final JComponent comp) {
        comp.addAncestorListener(new AncestorListener() {

            @Override
            public void ancestorAdded(AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
                comp.requestFocusInWindow();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
            }
        });
    }
}
