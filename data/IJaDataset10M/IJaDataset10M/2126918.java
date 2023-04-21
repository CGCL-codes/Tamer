package pcgen.gui2.util;

import pcgen.core.facade.UIDelegate;
import pcgen.core.utils.MessageType;
import pcgen.core.utils.MessageWrapper;
import pcgen.system.LanguageBundle;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.util.Observable;
import java.util.Observer;

public class ShowMessageGuiObserver implements Observer {

    private final UIDelegate uiDelegate;

    public ShowMessageGuiObserver(UIDelegate uiDelegate) {
        this.uiDelegate = uiDelegate;
    }

    public void update(Observable o, Object arg) {
        if (arg instanceof MessageWrapper) {
            showMessageDialog((MessageWrapper) arg);
        }
    }

    public void showMessageDialog(MessageWrapper messageWrapper) {
        MessageType mt = messageWrapper.getMessageType();
        String title = messageWrapper.getTitle();
        if (title == null) {
            title = LanguageBundle.getString("in_Prefs_pcgen");
        }
        String message = String.valueOf(messageWrapper.getMessage());
        if (mt.equals(MessageType.WARNING)) {
            uiDelegate.showWarningMessage(title, message);
        } else if (mt.equals(MessageType.ERROR)) {
            uiDelegate.showErrorMessage(title, message);
        } else {
            uiDelegate.showInfoMessage(title, message);
        }
    }
}
