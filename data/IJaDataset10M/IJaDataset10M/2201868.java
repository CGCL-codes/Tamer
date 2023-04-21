package tr.view.action.recurrence;

import java.awt.Component;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import tr.model.action.Recurrence;

public class NewRecurrenceWizardPanel1 implements WizardDescriptor.Panel {

    public NewRecurrenceWizardPanel1(Recurrence recurrence) {
        this.recurrence = recurrence;
    }

    private final Recurrence recurrence;

    private Component component;

    public Component getComponent() {
        if (component == null) {
            component = new NewRecurrenceVisualPanel1(recurrence);
        }
        return component;
    }

    public HelpCtx getHelp() {
        return new HelpCtx("recurrence");
    }

    public boolean isValid() {
        return true;
    }

    public final void addChangeListener(ChangeListener l) {
    }

    public final void removeChangeListener(ChangeListener l) {
    }

    public void readSettings(Object settings) {
        ((NewRecurrenceVisualPanel1) component).initPanel();
    }

    public void storeSettings(Object settings) {
    }
}
