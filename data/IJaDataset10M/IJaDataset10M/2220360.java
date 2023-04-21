package com.certesystems.swingforms.forms.states;

import javax.swing.JOptionPane;
import org.apache.cayenne.ObjectContext;
import com.certesystems.swingforms.Enlace;
import com.certesystems.swingforms.forms.Form;
import com.certesystems.swingforms.tools.Messages;
import com.certesystems.swingforms.tools.SwingHelper;

public class FormStateDelete extends FormState {

    public String getDescripcion() {
        return Messages.getString("FormStateDelete.deleteMode");
    }

    public void toState(Form oForm) throws Exception {
        int result = JOptionPane.showConfirmDialog(oForm, Messages.getString("FormStateDelete.confirmation"), Messages.getString("FormStateDelete.confirmationTitle"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            oForm.getGrid().preDelete();
            ObjectContext oOC = oForm.getGrid().getRegister().getObjectContext();
            oOC.deleteObject(oForm.getGrid().getRegister());
            oForm.getGrid().postDelete();
            oOC.commitChanges();
            oForm.getGrid().postDeleteCommit();
            for (Enlace enlace : oForm.getGrid().getEnlaces()) {
                enlace.asyncRefresh();
            }
        } else {
            oForm.setState(new FormStateSelect());
            oForm.setTextMessage(Messages.getString("FormStateDelete.cancelled"));
        }
        SwingHelper.closeParentFrame(oForm);
    }

    public void loadBar(Form form) {
    }
}
