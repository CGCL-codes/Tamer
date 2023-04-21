package action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import app.Resource;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import ui.FilterSelectPanel;
import ui.SettingPanel;

@SuppressWarnings("serial")
public class SettingAction extends AbstractAction {

    public SettingAction() {
        putValue(Action.NAME, Resource.get("option"));
        putValue(Action.ACTION_COMMAND_KEY, "Option");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Setting:performed:" + e);
        doAction();
    }

    public void doAction() {
        SettingPanel settingPanel = new SettingPanel();
        FilterSelectPanel filterSelectPanel = new FilterSelectPanel();
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(Resource.get("global"), settingPanel);
        tabbedPane.addTab(Resource.get("selectFilter"), filterSelectPanel);
        String[] options = { Resource.get("close") };
        int retval = JOptionPane.showOptionDialog(null, tabbedPane, "Setting", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        System.out.println("SettingAction: retval:" + retval);
    }
}
