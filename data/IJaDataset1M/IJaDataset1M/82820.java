package ebiNeutrino.core.settings;

import ebiNeutrino.core.EBIMain;
import ebiNeutrinoSDK.gui.component.EBIVisualPanel;
import ebiNeutrinoSDK.gui.component.EBIVisualPanelTemplate;
import javax.swing.*;
import java.awt.*;

public class EBISystemSetting extends EBIVisualPanelTemplate {

    private JSplitPane jSplitPane = null;

    public EBIListSettingName listName = null;

    public EBISystemSettingStart systemStart = null;

    public JPanel cpanel = null;

    public EBIMain ebiMain = null;

    public static int selectedModule = -1;

    public EBISystemSetting(EBIMain main) {
        super();
        ebiMain = main;
        cpanel = new JPanel();
        cpanel.setLayout(new BorderLayout());
        cpanel.setSize(1000, 800);
        listName = new EBIListSettingName(ebiMain, cpanel);
        initialize();
    }

    private void initialize() {
        super.getPanel().setLayout(new BorderLayout());
        super.getPanel().add(getJSplitPane(), java.awt.BorderLayout.CENTER);
    }

    private JSplitPane getJSplitPane() {
        if (jSplitPane == null) {
            jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listName, cpanel);
            jSplitPane.setOneTouchExpandable(true);
            jSplitPane.setBorder(BorderFactory.createEmptyBorder());
            jSplitPane.setDividerLocation(230);
        }
        return jSplitPane;
    }
}
