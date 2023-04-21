package com.xenoage.zong.player;

import java.util.List;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import com.xenoage.util.CommandLine;
import com.xenoage.util.error.BasicErrorProcessing;
import com.xenoage.util.error.Err;
import com.xenoage.util.io.IO;
import com.xenoage.util.language.Lang;
import com.xenoage.util.language.LanguageInfo;
import com.xenoage.util.logging.ApplicationLog;
import com.xenoage.util.logging.Log;
import com.xenoage.util.logging.LogLevel;
import com.xenoage.zong.Zong;
import com.xenoage.zong.io.midi.out.SynthManager;
import com.xenoage.zong.player.gui.ZongFrameController;
import com.xenoage.zong.player.gui.ZongFrameView;

/**
 * Main class for the Zong! Player desktop application.
 * 
 * @author Andreas Wenger
 */
public class Player {

    private static final String PROJECT_FIRST_NAME = "Player";

    private static final String FILENAME = Zong.FILENAME + "/player/";

    public static void main(String[] args) {
        CommandLine.setArgs(args);
        IO.initApplication(FILENAME);
        Log.initApplicationLog(ApplicationLog.FILENAME_DEFAULT, Zong.getNameAndVersion(PROJECT_FIRST_NAME));
        Err.setProcessing(new BasicErrorProcessing());
        try {
            List<LanguageInfo> languages = LanguageInfo.getAvailableLanguages(Lang.defaultLangPath);
            String langID = LanguageInfo.getDefaultID(languages);
            Lang.loadLanguage(langID);
        } catch (Exception ex) {
            Log.log(LogLevel.Warning, "Could not load language", ex);
            Lang.loadLanguage("en");
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }
        try {
            SynthManager.init(true);
        } catch (MidiUnavailableException ex) {
            JOptionPane.showMessageDialog(null, Lang.get(Voc.Error_Midi), getProjectName(), JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        ZongFrameController ctrl = new ZongFrameController(new ZongFrameView());
        if (args.length > 0) {
            try {
                ctrl.openScore(args[0]);
            } catch (Exception ex) {
            }
        }
        ctrl.getFrameView().getFrame().setVisible(true);
    }

    public static String getProjectName() {
        return Zong.getName(PROJECT_FIRST_NAME);
    }
}
