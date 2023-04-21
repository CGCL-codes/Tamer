package genj.edit.actions;

import genj.gedcom.Gedcom;
import genj.gedcom.GedcomListener;
import genj.gedcom.GedcomListenerAdapter;
import genj.util.swing.DialogHelper;
import javax.swing.Action;
import javax.swing.JComponent;

/**
 * Editing dialog for a gedcom context that auto-dismisses on edit
 */
public class GedcomDialog extends DialogHelper.Dialog {

    private Gedcom gedcom;

    private GedcomListener listener = new GedcomListenerAdapter() {

        public void gedcomWriteLockAcquired(Gedcom gedcom) {
            cancel();
        }
    };

    public GedcomDialog(Gedcom gedcom, String title, int messageType, final JComponent content, Action[] actions, Object source) {
        super(title, messageType, content, actions, source);
        this.gedcom = gedcom;
    }

    @Override
    public int show() {
        try {
            gedcom.addGedcomListener(listener);
            return super.show();
        } finally {
            gedcom.removeGedcomListener(listener);
        }
    }
}
