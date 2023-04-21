package de.ibis.permoto.start;

import java.util.Locale;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.log4j.Logger;
import de.ibis.permoto.gui.FrameManager;
import de.ibis.permoto.gui.loganalyzer.LogAnalyzer;
import de.ibis.permoto.util.PermotoPreferences;

/**
 * Starts the LogAnalyzer GUI.
 * @author Oliver Huehn
 * @author Andreas Schamberger
 */
public class LogAnalyzerGUIStart {

    public static void main(final String[] args) {
        final Logger logger = Logger.getLogger(LogAnalyzerGUIStart.class);
        try {
            UIManager.setLookAndFeel(new com.jgoodies.looks.windows.WindowsLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            logger.warn("This operating system does not support the chosen LookAndFeel profile!");
        }
        PermotoPreferences.setup();
        Locale.setDefault(Locale.ENGLISH);
        FrameManager.addPerMoToWindow(new LogAnalyzer());
    }
}
