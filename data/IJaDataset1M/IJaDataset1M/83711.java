package net.diet_rich.jabak.main;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.Timer;
import net.diet_rich.util.PropertyChangeListener;
import net.diet_rich.util.StringUtils;
import net.diet_rich.util.ref.FlaggedRef;

public class GUICommon {

    public static class ComponentDisabler implements PropertyChangeListener<GUIStates> {

        JComponent comp;

        public ComponentDisabler(JComponent component) {
            comp = component;
        }

        public void changed(GUIStates state) {
            comp.setEnabled(state == GUIStates.SETTINGS);
        }
    }

    public static class ComponentDisablerBool implements PropertyChangeListener<Boolean> {

        JComponent comp;

        public ComponentDisablerBool(JComponent component) {
            comp = component;
        }

        public void changed(Boolean state) {
            comp.setEnabled(state);
        }
    }

    /**
	 * TODO doc
	 * @author Georg Dietrich
	 */
    public static class ProblemMarker implements PropertyChangeListener<String> {

        JComponent comp;

        Color original;

        static final Color problem = new Color(255, 192, 160);

        public ProblemMarker(JComponent component) {
            comp = component;
            original = component.getBackground();
        }

        public void changed(String string) {
            if (string == null || string.equals("")) comp.setBackground(original); else comp.setBackground(problem);
        }
    }

    public static class TextFieldUpdater implements PropertyChangeListener<String> {

        JTextField field;

        public TextFieldUpdater(JTextField textField) {
            field = textField;
        }

        public void changed(String newText) {
            if (field.getText().equals(newText)) return;
            field.setText(newText);
        }
    }

    /** the states the GUI can be in */
    public enum GUIStates {

        SETTINGS, RUNNING, ERROR, FINISHED
    }

    /**
	 * display a file chooser and store the file path in the settings
	 * 
	 * @param settings
	 *            the settings to store the chosen path in
	 * @param key
	 *            the key to store the chosen path with
	 * @param buttontext
	 *            the text to display on the approve button
	 * @param mode
	 *            whether to make files, dirs, or both selectable
	 */
    public static void fileChooser(FlaggedRef<String> fileref, String buttontext, Component parent, int mode) {
        String path = fileref.get();
        if (path == null) path = "";
        File file = new File(path);
        if (!file.exists()) path = file.getParent();
        JFileChooser chooser = new JFileChooser(path);
        chooser.setFileSelectionMode(mode);
        chooser.setMultiSelectionEnabled(false);
        int value = chooser.showDialog(parent, buttontext);
        if (value == JFileChooser.APPROVE_OPTION) fileref.stringSet(chooser.getSelectedFile().getPath());
    }

    public static void componentOK(JComponent component) {
        component.setBackground(Color.white);
    }

    public static void componentNotOK(JComponent component) {
        component.setBackground(new Color(255, 192, 160));
    }

    public static void memoryBarInit(final JProgressBar memoryBar) {
        final Runtime runtime = Runtime.getRuntime();
        final long max = runtime.maxMemory();
        Timer timer = new Timer(1000, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                long total = runtime.totalMemory();
                long free = runtime.freeMemory();
                memoryBar.setValue((int) ((total - free) / 1000));
                memoryBar.setMaximum((int) total / 1000);
                memoryBar.setString(StringUtils.readableNumber(total - free) + "/" + StringUtils.readableNumber(max));
            }
        });
        timer.start();
    }
}
