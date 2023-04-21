package org.semanticweb.mmm.mr3.actions;

import java.awt.*;
import java.awt.event.*;
import java.lang.ref.*;
import javax.swing.*;
import org.semanticweb.mmm.mr3.ui.*;
import org.semanticweb.mmm.mr3.util.*;

/**
 * @author takeshi morita
 * 
 */
public class ShowVersionInfoAction extends MR3AbstractAction {

    private Frame rootFrame;

    private WeakReference<VersionInfoDialog> versionInfoDialogRef;

    private static final String TITLE = Translator.getString("Component.Help.About.Text");

    private static final ImageIcon ICON = Utilities.getImageIcon(Translator.getString("Component.Help.About.Icon"));

    public ShowVersionInfoAction(Frame frame) {
        super(TITLE, ICON);
        rootFrame = frame;
        versionInfoDialogRef = new WeakReference<VersionInfoDialog>(null);
        setValues();
    }

    private void setValues() {
        putValue(SHORT_DESCRIPTION, TITLE);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("F1"));
    }

    public VersionInfoDialog getVersionInfoDialog() {
        VersionInfoDialog result = versionInfoDialogRef.get();
        if (result == null) {
            result = new VersionInfoDialog(rootFrame, TITLE, ICON);
            versionInfoDialogRef = new WeakReference<VersionInfoDialog>(result);
        }
        return result;
    }

    public void actionPerformed(ActionEvent e) {
        getVersionInfoDialog().setVisible(true);
    }
}
