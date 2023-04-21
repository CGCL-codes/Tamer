package mosinstaller.event;

import java.awt.AWTEventMulticaster;
import java.util.EventListener;

/**
 * @author PLAGNIOL-VILLARD Jean-Christophe
 * @version 1.0
 */
public class ProgressWizardEventMulticaster extends AWTEventMulticaster implements ProgressWizardListener {

    protected ProgressWizardEventMulticaster(EventListener a, EventListener b) {
        super(a, b);
    }

    public static ProgressWizardListener add(ProgressWizardListener a, ProgressWizardListener b) {
        return (ProgressWizardListener) addInternal(a, b);
    }

    public static ProgressWizardListener remove(ProgressWizardListener l, ProgressWizardListener oldl) {
        return (ProgressWizardListener) removeInternal(l, oldl);
    }

    public void Ended(ProgressWizardEvent e) {
        if (a != null) ((ProgressWizardAdapter) a).Ended(e);
        if (b != null) ((ProgressWizardAdapter) b).Ended(e);
    }

    public void Reseted(ProgressWizardEvent e) {
        if (a != null) ((ProgressWizardAdapter) a).Reseted(e);
        if (b != null) ((ProgressWizardAdapter) b).Reseted(e);
    }

    public void Started(ProgressWizardEvent e) {
        if (a != null) ((ProgressWizardAdapter) a).Started(e);
        if (b != null) ((ProgressWizardAdapter) b).Started(e);
    }

    public void Incremented(ProgressWizardEvent e) {
        if (a != null) ((ProgressWizardAdapter) a).Incremented(e);
        if (b != null) ((ProgressWizardAdapter) b).Incremented(e);
    }

    public void Decremented(ProgressWizardEvent e) {
        if (a != null) ((ProgressWizardAdapter) a).Decremented(e);
        if (b != null) ((ProgressWizardAdapter) b).Decremented(e);
    }

    protected static EventListener addInternal(EventListener a, EventListener b) {
        if (a == null) return b;
        if (b == null) return a;
        return new ProgressWizardEventMulticaster(a, b);
    }

    protected EventListener remove(EventListener oldl) {
        if (oldl == a) return b;
        if (oldl == b) return a;
        EventListener a2 = removeInternal(a, oldl);
        EventListener b2 = removeInternal(b, oldl);
        if (a2 == a && b2 == b) return this;
        return addInternal(a2, b2);
    }

    public void valueChanged(ProgressWizardEvent e) {
        if (a != null) ((ProgressWizardAdapter) a).valueChanged(e);
        if (b != null) ((ProgressWizardAdapter) b).valueChanged(e);
    }
}
