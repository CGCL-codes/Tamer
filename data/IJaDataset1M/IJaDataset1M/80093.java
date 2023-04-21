package de.miethxml.hawron.gui.context.viewer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import de.miethxml.toolkit.component.AbstractServiceable;
import de.miethxml.toolkit.component.GuiConfigurable;
import de.miethxml.toolkit.gui.ImagePreviewer;
import de.miethxml.toolkit.gui.LocaleButton;
import de.miethxml.toolkit.gui.StringListView;
import de.miethxml.toolkit.gui.StringListViewImpl;
import de.miethxml.toolkit.setup.ApplicationSetup;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.service.ServiceManager;

/**
 * @author <a href="mailto:simon.mieth@gmx.de">Simon Mieth </a>
 *
 *
 *
 *
 *
 *
 *
 */
public class ExternalViewerSetup extends AbstractServiceable implements Initializable, GuiConfigurable, ActionListener {

    private static String ACTION_SETTINGS_OK = "settings.ok";

    private static String ACTION_SETTINGS_CANCEL = "settings.cancel";

    public static String DEFAUL_ICON = "icons/viewer.gif";

    private JList handles;

    private JList viewers;

    private JTextField name;

    private JTextField icon;

    private JTextField command;

    private JTextField handle;

    private DefaultListModel handlesdata;

    private JPanel setup;

    private ExternalViewerManager em;

    private DefaultListModel viewerdata;

    private ApplicationSetup applicationSetup;

    private StringListView suffices;

    private JFileChooser commandChooser;

    private JDialog dialog;

    private boolean isNew = false;

    private int editviewer = -1;

    /**
     *
     *
     *
     */
    public ExternalViewerSetup() {
        super();
        handlesdata = new DefaultListModel();
        viewerdata = new DefaultListModel();
    }

    public void initialize() {
        setViewers();
    }

    public void disposeComponent(ServiceManager newParam) {
    }

    public String getLabel() {
        return "External Viewer Setup";
    }

    public JComponent getSetupComponent() {
        if (setup == null) {
            setup = createSetupPanel();
        }
        return setup;
    }

    public void setup() {
        em.save();
    }

    public boolean isSetup() {
        return false;
    }

    private JPanel createSetupPanel() {
        FormLayout panellayout = new FormLayout("3dlu,pref,3dlu,pref:grow,3dlu,pref,3dlu,pref:grow,3dlu", "3dlu,p,3dlu,p,2dlu,p,2dlu,p,3dlu:grow,9dlu,p,3dlu,p,2dlu,p,2dlu,p,2dlu,p,2dlu,p,20dlu:grow,3dlu");
        PanelBuilder builder = new PanelBuilder(panellayout);
        CellConstraints cc = new CellConstraints();
        builder.addSeparator("All Viewers", cc.xywh(2, 2, 5, 1));
        viewers = new JList(viewerdata);
        viewers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane sp = new JScrollPane(viewers);
        builder.add(sp, cc.xywh(4, 4, 1, 6));
        JButton button = new JButton("add");
        button.setActionCommand("add.viewer");
        button.addActionListener(this);
        builder.add(button, cc.xy(6, 4));
        button = new JButton("edit");
        button.setActionCommand("edit.viewer");
        button.addActionListener(this);
        builder.add(button, cc.xy(6, 6));
        button = new JButton("remove");
        button.setActionCommand("remove.viewer");
        button.addActionListener(this);
        builder.add(button, cc.xy(6, 8));
        createDialog();
        return builder.getPanel();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("remove.viewer")) {
            em.removeViewer(viewers.getSelectedIndex());
            viewerdata.removeElementAt(viewers.getSelectedIndex());
        } else if (e.getActionCommand().equals("edit.viewer")) {
            isNew = false;
            setViewer(viewers.getSelectedIndex());
            dialog.setVisible(true);
        } else if (e.getActionCommand().equals("add.viewer")) {
            isNew = true;
            setBlankFields();
            dialog.setVisible(true);
        } else if (e.getActionCommand().equals("choose.icon")) {
            JFileChooser fs = new JFileChooser("icons/");
            ImagePreviewer ip = new ImagePreviewer(36, 36);
            fs.setAccessory(ip);
            fs.addPropertyChangeListener(ip);
            if (fs.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                icon.setText(fs.getSelectedFile().getPath());
            }
        } else if (e.getActionCommand().equals("choose.command")) {
            commandChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int value = commandChooser.showDialog(null, "Choose Program");
            if (value == JFileChooser.APPROVE_OPTION) {
                command.setText(commandChooser.getSelectedFile().getAbsolutePath() + "  %s");
            }
        } else if (e.getActionCommand().equals(ACTION_SETTINGS_CANCEL)) {
            dialog.setVisible(false);
        } else if (e.getActionCommand().equals(ACTION_SETTINGS_OK)) {
            if (!isNew) {
                em.removeViewer(editviewer);
            }
            addNewViewer();
            dialog.setVisible(false);
        }
    }

    private void setViewers() {
        for (int i = 0; i < em.getViewerCount(); i++) {
            viewerdata.addElement(em.getViewer(i).getName());
        }
    }

    private void addNewViewer() {
        ExternalViewer viewer = new ExternalViewer();
        viewer.setName(name.getText());
        viewer.setCommand(command.getText());
        viewer.setIcon(icon.getText());
        viewer.setHandles(suffices.getStringList());
        em.addViewer(viewer);
        viewerdata.addElement(name.getText());
        setBlankFields();
    }

    private void setBlankFields() {
        name.setText("");
        command.setText("");
        icon.setText(DEFAUL_ICON);
        ArrayList list = new ArrayList();
        list.add("*");
        suffices.setStringList(list);
    }

    private void setViewer(int index) {
        setBlankFields();
        ExternalViewer viewer = em.getViewer(index);
        name.setText(viewer.getName());
        command.setText(viewer.getCommand());
        icon.setText(viewer.getIconURL());
        suffices.setStringList(viewer.getHandles());
        editviewer = index;
    }

    /**
     * @param applicationSetup
     *            The applicationSetup to set.
     */
    public void setExternalViewerManager(ExternalViewerManager m) {
        this.em = m;
    }

    private void createDialog() {
        FormLayout panellayout = new FormLayout("3dlu,pref,3dlu,pref:grow,3dlu,pref,3dlu,pref:grow,3dlu", "3dlu,p,3dlu,p,2dlu,p,2dlu,p,2dlu,p,2dlu,p,20dlu:grow,3dlu");
        PanelBuilder builder = new PanelBuilder(panellayout);
        CellConstraints cc = new CellConstraints();
        dialog = new JDialog();
        dialog.setTitle("Viewer Settings");
        builder.addLabel("Name", cc.xy(2, 4));
        name = new JTextField(20);
        builder.add(name, cc.xy(4, 4));
        builder.addLabel("Command ", cc.xy(2, 6));
        command = new JTextField(20);
        builder.add(command, cc.xy(4, 6));
        JButton button = new JButton("...");
        button.setActionCommand("choose.command");
        button.addActionListener(this);
        builder.add(button, cc.xy(6, 6));
        commandChooser = new JFileChooser();
        builder.addLabel("%s for file ", cc.xy(8, 6));
        builder.addLabel("Icon", cc.xy(2, 8));
        icon = new JTextField(20);
        builder.add(icon, cc.xy(4, 8));
        button = new JButton("...");
        button.setActionCommand("choose.icon");
        button.addActionListener(this);
        builder.add(button, cc.xy(6, 8));
        builder.addSeparator("Extensions", cc.xywh(2, 10, 7, 1));
        suffices = new StringListViewImpl();
        suffices.init();
        builder.add(suffices.getView(), cc.xywh(2, 12, 5, 2));
        dialog.getContentPane().add(builder.getPanel(), BorderLayout.CENTER);
        JPanel buttonpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        button = new LocaleButton("common.button.cancel");
        button.addActionListener(this);
        button.setActionCommand(ACTION_SETTINGS_CANCEL);
        buttonpanel.add(button);
        button = new LocaleButton("common.button.ok");
        button.addActionListener(this);
        button.setActionCommand(ACTION_SETTINGS_OK);
        buttonpanel.add(button);
        dialog.getContentPane().add(buttonpanel, BorderLayout.SOUTH);
        dialog.pack();
    }
}
