package eu.keep.uphec.mainwindow.menubar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import eu.keep.kernel.CoreEngineModel;
import eu.keep.uphec.mainwindow.language.LanguageSettingsDialog;

public class ViewLanguageSettingsListener implements ActionListener {

    private JFrame mainWindowFrame;

    private CoreEngineModel model;

    public ViewLanguageSettingsListener(JFrame mainWindowFrame, CoreEngineModel model) {
        this.mainWindowFrame = mainWindowFrame;
        this.model = model;
    }

    public void actionPerformed(ActionEvent arg0) {
        new LanguageSettingsDialog(mainWindowFrame, model);
    }
}
