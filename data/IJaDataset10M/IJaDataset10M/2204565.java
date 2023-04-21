package listo.client.actions;

import com.google.inject.Inject;
import com.google.inject.Provider;
import listo.client.dialogs.AboutDialog;
import listo.client.Lang;
import listo.utils.logging.Log;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class AboutAction extends BaseAction {

    private final Provider<AboutDialog> aboutDialogProvider;

    @Inject
    public AboutAction(Log log, Lang lang, Provider<AboutDialog> aboutDialogProvider) {
        super(log, lang, "about");
        this.aboutDialogProvider = aboutDialogProvider;
    }

    public void perform(ActionEvent e) {
        AboutDialog dialog = aboutDialogProvider.get();
        dialog.run();
    }
}
