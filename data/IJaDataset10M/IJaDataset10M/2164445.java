package com.sdi.pws.gui.dialog.generate;

import com.sdi.pws.generator.Generator;
import com.sdi.pws.generator.GeneratorImpl;
import com.sdi.pws.gui.compo.generator.change.ChangeViewGenerator;
import com.sdi.pws.gui.compo.generator.view.JGeneratorProps;
import com.sdi.pws.gui.GuiUtil;
import com.sdi.pws.preferences.Preferences;
import com.sdi.pws.util.PreferencesUtil;
import javax.swing.*;

public class Generate {

    public static String generateNewPassword(JComponent aApp, Preferences aGlobalPrefs) {
        final Generator lCustomGen = PreferencesUtil.getDefaultGenerator(aGlobalPrefs);
        final ChangeViewGenerator lGenModel = new ChangeViewGenerator(lCustomGen);
        final JGeneratorProps lGenProps = new JGeneratorProps(lGenModel);
        while (true) {
            final int lUserResponse = JOptionPane.showOptionDialog(aApp, lGenProps, GuiUtil.getText("generate.title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (lUserResponse == JOptionPane.OK_OPTION) {
                String[] lCandidates = new String[10];
                for (int i = 0; i < 10; i++) {
                    lCandidates[i] = lCustomGen.generate();
                }
                String lSelectedPassword = (String) JOptionPane.showInputDialog(aApp, GuiUtil.getText("generate.proposal"), GuiUtil.getText("generate.proposal.title"), JOptionPane.QUESTION_MESSAGE, null, lCandidates, null);
                if (lSelectedPassword != null) return lSelectedPassword;
            } else return null;
        }
    }
}
