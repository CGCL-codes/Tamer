package org.stellarium.ui;

import org.stellarium.*;
import org.stellarium.ui.components.*;
import org.stellarium.ui.dialog.ConfigDialog;
import org.stellarium.ui.dialog.SearchDialog;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class StelUIConf extends StelUI {

    private StelListBox languageSkyLb;

    private StelListBox languageLb;

    private StelListBox skycultureLb;

    private StringList landscapeSl;

    private StelLabel landscapeAuthorLabel;

    private TextLabel landscapeDescriptionLabel;

    protected StelUIConf(StelCore someCore, StelApp someApp) throws StellariumException {
        super(someCore, someApp);
    }

    protected ConfigDialog createConfigWindow(JFrame mainFrame) throws StellariumException {
        ConfigDialog configWin = new ConfigDialog(mainFrame, app);
        configWin.addWindowListener(new WindowAdapter() {

            public void windowClosed(WindowEvent e) {
                configDialogHide();
            }
        });
        configWin.pack();
        return configWin;
    }

    protected void dialogCallback() {
        String lastID = dialogWin.getLastID();
        int lastButton = dialogWin.getLastButton();
        String lastInput = dialogWin.getLastInput();
        int lastType = dialogWin.getLastType();
        if ("observatory name".equals(lastID)) {
            if (lastButton != SGUI.BT_OK || StelUtility.isEmpty(lastInput)) {
                lastInput = SGUI.UNKNOWN_OBSERVATORY;
            }
            configWin.doSaveObserverPosition(lastInput);
            configWin.setCityFromMap();
        } else if (!StelUtility.isEmpty(lastID)) {
            StringBuilder msg = new StringBuilder(lastID);
            msg.append(" returned btn: ");
            if (lastButton == SGUI.BT_OK) msg.append("BT_OK"); else if (lastButton == SGUI.BT_YES) msg.append("BT_YES"); else if (lastButton == SGUI.BT_NO) msg.append("BT_NO"); else if (lastButton == SGUI.BT_CANCEL) msg.append("BT_CANCEL");
            if (lastType == SGUI.STDDLGWIN_MSG) {
                dialogWin.MessageBox("Stellarium", msg.toString(), SGUI.BT_OK, "");
            } else if (lastType == SGUI.STDDLGWIN_INPUT) {
                msg.append(" inp: ").append(lastInput);
                dialogWin.MessageBox("Stellarium", msg.toString(), SGUI.BT_OK, "");
            }
        }
        configWin.setVisible(flagConfig);
    }

    void setSkyLanguage() {
        core.setSkyLanguage(Translator.nativeLanguageNameCodeToIso639_1(languageSkyLb.getCurrent()));
    }

    void setAppLanguage() {
        app.setAppLanguage(Translator.nativeLanguageNameCodeToIso639_1(languageLb.getCurrent()));
    }

    void setSkyCulture() {
        core.setSkyCulture(skycultureLb.getCurrent());
    }

    /**
     * Create Search window widgets
     */
    protected SearchDialog createSearchWindow(JFrame mainFrame) throws StellariumException {
        SearchDialog searchWin = new SearchDialog(mainFrame, app);
        searchWin.addWindowListener(new WindowAdapter() {

            public void windowClosed(WindowEvent e) {
                searchDialogHide();
            }
        });
        searchWin.pack();
        return searchWin;
    }

    void configDialogHide() {
        flagConfig = false;
        waitOnLocation = true;
        btFlagConfig.setSelected(flagConfig);
    }

    void searchDialogHide() {
        flagSearch = false;
        searchWin.setVisible(false);
        btFlagSearch.setSelected(false);
    }
}
