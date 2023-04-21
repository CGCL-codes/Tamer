package creiter.gpxTcxWelder.gui;

import creiter.gpxTcxWelder.gui.tools.PreferenceStore;
import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author christian
 */
public abstract class AbstractFileChooser {

    private PreferenceStore preferences = PreferenceStore.get();

    protected abstract SuffixFilter getSuffixFilter();

    protected JFileChooser getJFileChooser(Component parent, String preferencesKey) {
        JFileChooser fileChooser = new JFileChooser();
        if (preferences.getPreference(preferencesKey) != null) {
            fileChooser.setCurrentDirectory(new File(preferences.getPreference(preferencesKey)));
        }
        fileChooser.setAcceptAllFileFilterUsed(true);
        if (getSuffixFilter() != null) {
            fileChooser.addChoosableFileFilter(getSuffixFilter());
        }
        return fileChooser;
    }

    public File selectFile(Component parent, String preferencesKey, boolean isSaveDialog) {
        JFileChooser fileChooser = getJFileChooser(parent, preferencesKey);
        int result = 4711;
        if (isSaveDialog) {
            result = fileChooser.showSaveDialog(parent);
        } else {
            result = fileChooser.showOpenDialog(parent);
        }
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            preferences.storePreference(preferencesKey, file.toString());
            return file;
        }
        return null;
    }
}
