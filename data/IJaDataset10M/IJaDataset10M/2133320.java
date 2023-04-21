package listo.client.actions;

import com.google.inject.Inject;
import listo.client.ImageProvider;
import listo.client.MainForm;
import listo.client.Preferences;
import listo.client.Lang;
import listo.utils.MiscUtils;
import listo.utils.logging.Log;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import org.apache.commons.lang.WordUtils;

/**
 * Closes the application.
 */
public class PreferencesAction extends BaseAction {

    private final String filename;

    private final MainForm mainForm;

    @Inject
    public PreferencesAction(Log log, Lang lang, Preferences preferences, MainForm mainForm, ImageProvider imageProvider) {
        super(log, lang, "preferences");
        this.filename = preferences.getFilename();
        this.mainForm = mainForm;
        putValue(Action.SMALL_ICON, imageProvider.getIcon("16x16/tools.png"));
    }

    public void perform(ActionEvent e) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.EDIT)) {
            JOptionPane.showMessageDialog(mainForm, MiscUtils.wordWrap(String.format("Up to now lis.to does not yet provide its own " + "preference dialog. However, the application preferences are kept in a simple " + "text file (currently '%s') that you can edit. You have to restart lis.to to make " + "your changes take effect.", filename), 80));
            try {
                Desktop.getDesktop().edit(new File(filename));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(mainForm, MiscUtils.wordWrap(String.format("lis.to could not open the default text editor " + "for the file '%s'. Please open the file manually (and restart lis.to)!", filename), 80), "Error opening preferences file", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(mainForm, MiscUtils.wordWrap(String.format("lis.to can not open the default text editor " + "on this system. Please edit the file %s manually (and restart lis.to)!", filename), 80));
        }
    }
}
