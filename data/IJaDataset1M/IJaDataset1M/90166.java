package de.miethxml.hawron.gui.process;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import de.miethxml.hawron.cocoon.SitemapWrapper;
import de.miethxml.hawron.net.PublishTarget;
import de.miethxml.hawron.project.ProcessURI;
import de.miethxml.hawron.project.Project;
import de.miethxml.hawron.project.ProjectConfigListener;
import de.miethxml.hawron.project.PublishDestination;
import de.miethxml.hawron.project.Task;
import de.miethxml.hawron.xml.SAXSitemapBuilder;
import de.miethxml.toolkit.conf.LocaleImpl;
import de.miethxml.toolkit.gui.LocaleButton;
import de.miethxml.toolkit.gui.LocaleLabel;
import de.miethxml.toolkit.gui.LocaleSeparator;
import de.miethxml.toolkit.gui.help.HelpAction;
import de.miethxml.toolkit.ui.GradientLabel;
import org.apache.avalon.framework.service.ServiceManager;

/**
 * @author <a href="mailto:simon.mieth@gmx.de">Simon Mieth </a>
 *
 *
 *
 */
public class TaskDialogImpl implements TaskDialog, ProjectConfigListener {

    private JDialog dialog;

    private boolean initialized = false;

    private Task task;

    private Project project;

    private boolean newTask;

    private JTabbedPane tp;

    private JFileChooser fc;

    private Controller controller;

    private LocaleButton changePublishButton;

    private LocaleButton changeURIButton;

    private JTextField name;

    private JTextField ID;

    private JTextField buildDir;

    private JTextArea description;

    private JPanel publishPanel;

    private JList uris;

    private JList sitemap;

    private JTextField uriEdit;

    private JTextField srcPrefix;

    private JCheckBox followLinks;

    private JCheckBox confirmExtension;

    private JCheckBox partial;

    private JCheckBox clean;

    private JComboBox type;

    private JList publish;

    private JTextField publishTitle;

    private JTextField publishSource;

    private JTextField publishDestination;

    private JComboBox publishTarget;

    private final String[] typeData = new String[] { "append", "insert", "replace" };

    public final String ACTION_CHOOSE_DESTDIR = "choose.destdir";

    public final String ACTION_CANCEL = "cancel";

    public final String ACTION_OK = "ok";

    public final String ACTION_URI_ADD = "uri.add";

    public final String ACTION_URI_EDIT = "uri.edit";

    public final String ACTION_URI_REMOVE = "uri.remove";

    public final String ACTION_URI_CHANGE = "uri.change";

    public final String ACTION_PUBLISHING_ADD = "publish.add";

    public final String ACTION_PUBLISHING_EDIT = "publish.edit";

    public final String ACTION_PUBLISHING_REMOVE = "publish.remove";

    public final String ACTION_PUBLISHING_CHANGE = "publish.change";

    public final String ACTION_PUBLISHING_CHOOSE_SOURCE = "publish.choose.source";

    private boolean change = false;

    private PublishDestination oldPublish;

    public TaskDialogImpl() {
    }

    public void initialize() {
        dialog = new JDialog();
        dialog.setTitle("Task Settings");
        FormLayout layout;
        PanelBuilder builder;
        CellConstraints cc;
        LocaleLabel label;
        LocaleButton button;
        controller = new Controller(dialog);
        tp = new JTabbedPane();
        tp.setBorder(BorderFactory.createEmptyBorder());
        dialog.getContentPane().setLayout(new BorderLayout());
        fc = new JFileChooser();
        layout = new FormLayout("3dlu,right:pref,2dlu,pref:grow,2dlu,pref,3dlu", "3dlu,p,2dlu,p,2dlu,p,9dlu,p,2dlu,fill:p:grow,3dlu");
        builder = new PanelBuilder(layout);
        cc = new CellConstraints();
        label = new LocaleLabel("view.task.settings.label.name");
        builder.add(label, cc.xy(2, 2));
        name = new JTextField(20);
        builder.add(name, cc.xy(4, 2));
        label = new LocaleLabel("view.task.settings.label.id");
        label.setVisible(false);
        builder.add(label, cc.xy(2, 4));
        ID = new JTextField(20);
        ID.setVisible(false);
        builder.add(ID, cc.xy(4, 4));
        label = new LocaleLabel("view.task.settings.label.destdir");
        builder.add(label, cc.xy(2, 6));
        buildDir = new JTextField(20);
        builder.add(buildDir, cc.xy(4, 6));
        button = new LocaleButton("common.button.choose");
        button.setActionCommand(ACTION_CHOOSE_DESTDIR);
        button.addActionListener(controller);
        builder.add(button, cc.xy(6, 6));
        builder.add(new LocaleSeparator("view.task.settings.separator.description"), cc.xywh(2, 8, 5, 1));
        description = new JTextArea(5, 20);
        JScrollPane sp = new JScrollPane(description);
        builder.add(sp, cc.xy(4, 10));
        tp.addTab("Main", builder.getPanel());
        layout = new FormLayout("3dlu,right:pref,2dlu,pref,2dlu,fill:pref:grow,2dlu,pref,3dlu", "3dlu,p,3dlu,p,2dlu,p,2dlu,p,2dlu,p,9dlu,p,3dlu,p,2dlu,p,fill:3dlu:grow,9dlu,p,3dlu,p,2dlu,p,fill:3dlu:grow,2dlu,p,2dlu,p,2dlu,p,3dlu");
        builder = new PanelBuilder(layout);
        cc = new CellConstraints();
        builder.add(new LocaleSeparator("view.task.settings.separator.general"), cc.xywh(2, 2, 7, 1));
        followLinks = new JCheckBox();
        builder.add(followLinks, cc.xy(4, 4, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        builder.add(new LocaleLabel("view.task.settings.label.followlinks"), cc.xy(6, 4));
        confirmExtension = new JCheckBox();
        builder.add(confirmExtension, cc.xy(4, 6, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        builder.add(new LocaleLabel("view.task.settings.label.confirmextension"), cc.xy(6, 6));
        partial = new JCheckBox();
        builder.add(partial, cc.xy(4, 8, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        builder.add(new LocaleLabel("view.task.settings.label.partial"), cc.xy(6, 8));
        clean = new JCheckBox();
        builder.add(clean, cc.xy(4, 10, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        builder.add(new LocaleLabel("view.task.settings.label.clean"), cc.xy(6, 10));
        builder.add(new LocaleSeparator("view.task.settings.separator.uris"), cc.xywh(2, 12, 7, 1));
        uris = new JList();
        sp = new JScrollPane(uris);
        builder.add(sp, cc.xywh(4, 14, 3, 4));
        button = new LocaleButton("common.button.edit");
        button.setActionCommand(ACTION_URI_EDIT);
        button.addActionListener(controller);
        builder.add(button, cc.xy(8, 14));
        button = new LocaleButton("common.button.remove");
        button.setActionCommand(ACTION_URI_REMOVE);
        button.addActionListener(controller);
        builder.add(button, cc.xy(8, 16));
        builder.add(new LocaleSeparator("view.task.settings.separator.urisettings"), cc.xywh(2, 19, 7, 1));
        builder.add(new LocaleLabel("view.task.settings.label.sitemapuris"), cc.xy(2, 21));
        sitemap = new JList();
        sp = new JScrollPane(sitemap);
        builder.add(sp, cc.xywh(4, 21, 3, 4));
        button = new LocaleButton("view.task.settings.button.adduri");
        button.setActionCommand(ACTION_URI_ADD);
        button.addActionListener(controller);
        builder.add(button, cc.xy(8, 21));
        changeURIButton = new LocaleButton("view.task.settings.button.changeuri");
        changeURIButton.setActionCommand(ACTION_URI_CHANGE);
        changeURIButton.addActionListener(controller);
        changeURIButton.setEnabled(false);
        builder.add(changeURIButton, cc.xy(8, 23));
        builder.add(new LocaleLabel("view.task.settings.label.uri"), cc.xy(2, 26));
        uriEdit = new JTextField(20);
        builder.add(uriEdit, cc.xywh(4, 26, 3, 1));
        builder.add(new LocaleLabel("view.task.settings.label.srcprefix"), cc.xy(2, 28, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        srcPrefix = new JTextField(20);
        builder.add(srcPrefix, cc.xywh(4, 28, 3, 1));
        builder.add(new LocaleLabel("view.task.settings.label.type"), cc.xy(2, 30, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        type = new JComboBox(typeData);
        type.setEditable(false);
        builder.add(type, cc.xywh(4, 30, 3, 1));
        tp.addTab("Processing", builder.getPanel());
        layout = new FormLayout("3dlu,right:pref,2dlu,pref,2dlu,fill:pref:grow,2dlu,pref,3dlu", "3dlu,p,3dlu,p,2dlu,p,fill:3dlu:grow,9dlu,p,3dlu,p,2dlu,p,2dlu,p,2dlu,p,3dlu");
        builder = new PanelBuilder(layout);
        cc = new CellConstraints();
        builder.add(new LocaleSeparator("view.task.settings.separator.publishingdestinations"), cc.xywh(2, 2, 7, 1));
        publish = new JList();
        sp = new JScrollPane(publish);
        builder.add(sp, cc.xywh(4, 4, 1, 4));
        button = new LocaleButton("common.button.edit");
        button.setActionCommand(ACTION_PUBLISHING_EDIT);
        button.addActionListener(controller);
        builder.add(button, cc.xy(8, 4));
        button = new LocaleButton("common.button.remove");
        button.setActionCommand(ACTION_PUBLISHING_REMOVE);
        button.addActionListener(controller);
        builder.add(button, cc.xy(8, 6));
        builder.add(new LocaleSeparator("view.task.settings.separator.publishingoptions"), cc.xywh(2, 9, 7, 1));
        builder.add(new LocaleLabel("view.task.settings.label.publishname"), cc.xy(2, 11));
        publishTitle = new JTextField(20);
        builder.add(publishTitle, cc.xy(4, 11));
        button = new LocaleButton("view.task.settings.button.addpublish");
        button.setActionCommand(ACTION_PUBLISHING_ADD);
        button.addActionListener(controller);
        builder.add(button, cc.xy(8, 11));
        builder.add(new LocaleLabel("view.task.settings.label.publishsource"), cc.xy(2, 13));
        publishSource = new JTextField(20);
        builder.add(publishSource, cc.xy(4, 13));
        button = new LocaleButton("common.button.choose");
        button.setActionCommand(ACTION_PUBLISHING_CHOOSE_SOURCE);
        button.addActionListener(controller);
        builder.add(button, cc.xy(6, 13));
        changePublishButton = new LocaleButton("view.task.settings.button.changepublish");
        changePublishButton.setActionCommand(ACTION_PUBLISHING_CHANGE);
        changePublishButton.addActionListener(controller);
        changePublishButton.setEnabled(false);
        builder.add(changePublishButton, cc.xy(8, 13));
        builder.add(new LocaleLabel("view.task.settings.label.publishdestination"), cc.xy(2, 15));
        publishDestination = new JTextField(20);
        builder.add(publishDestination, cc.xy(4, 15));
        builder.add(new LocaleLabel("view.task.settings.label.publishtarget"), cc.xy(2, 17));
        publishTarget = new JComboBox();
        publishTarget.setEditable(false);
        builder.add(publishTarget, cc.xy(4, 17));
        publishPanel = builder.getPanel();
        tp.addTab("Pubishing", publishPanel);
        JButton[] buttons = new JButton[2];
        buttons[0] = new LocaleButton("common.button.cancel");
        buttons[0].setActionCommand(ACTION_CANCEL);
        buttons[0].addActionListener(controller);
        buttons[1] = new LocaleButton("common.button.ok");
        buttons[1].setActionCommand(ACTION_OK);
        buttons[1].addActionListener(controller);
        button = new LocaleButton("common.button.help");
        button.addActionListener(new HelpAction("processing.htm"));
        ButtonBarBuilder bbuilder = new ButtonBarBuilder();
        bbuilder.addGridded(button);
        bbuilder.addRelatedGap();
        bbuilder.addGlue();
        bbuilder.addGriddedButtons(buttons);
        GradientLabel titleLabel = new GradientLabel("Task Settings");
        titleLabel.setFontHeight(28);
        titleLabel.setStartColor(Color.GRAY);
        titleLabel.setEndColor(Color.WHITE);
        titleLabel.setTextColor(Color.BLACK);
        dialog.getContentPane().add(titleLabel, BorderLayout.NORTH);
        tp.setBorder(BorderFactory.createEmptyBorder());
        dialog.getContentPane().add(tp, BorderLayout.CENTER);
        JPanel panel = bbuilder.getPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        dialog.getContentPane().add(panel, BorderLayout.SOUTH);
        dialog.pack();
        initialized = true;
    }

    public void setTask(Task task) {
        controller.setTask(task);
    }

    public void setVisible(boolean state) {
        if (state) {
            publishTarget.removeAllItems();
            if (project.getPublishTargetCount() > 0) {
                enableComponents(publishPanel, true);
                Iterator i = project.getPublishTargets().iterator();
                while (i.hasNext()) {
                    PublishTarget pt = (PublishTarget) i.next();
                    publishTarget.addItem(pt);
                }
                publishTarget.setSelectedIndex(-1);
            } else {
                enableComponents(publishPanel, false);
            }
        }
        dialog.setVisible(state);
    }

    private void enableComponents(JComponent comp, boolean b) {
        comp.setEnabled(b);
        int count = comp.getComponentCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                Component c = comp.getComponent(i);
                c.setEnabled(b);
            }
        }
    }

    public void disposeComponent(ServiceManager newParam) {
    }

    public void newTask() {
        controller.newTask();
    }

    public void setProject(Project project) {
        if (this.project == null) {
            project.addProjectConfigListener(this);
        }
        this.project = project;
        sitemap.setModel(createSitemapList());
    }

    public void configChanged(Project project) {
        setProject(project);
    }

    private ListModel createSitemapList() {
        SAXSitemapBuilder sax = new SAXSitemapBuilder();
        SitemapWrapper sitemapmodel = sax.parseURI(project.getCocoonBeanConfiguration().getContextDir() + File.separator + "sitemap.xmap");
        return sitemapmodel;
    }

    private class Controller implements ActionListener, ListSelectionListener {

        private JDialog dialog;

        private Task task;

        private Task old;

        private ProcessURI olduri;

        private PublishDestination oldPublish;

        public Controller(JDialog dialog) {
            this.dialog = dialog;
        }

        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            if (cmd.equals(ACTION_CANCEL)) {
                setVisible(false);
            } else if (cmd.equals(ACTION_OK)) {
                Vector validateMsg = validateInput();
                if (validateMsg.size() > 0) {
                    JList list = new JList(validateMsg);
                    list.setOpaque(false);
                    list.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    int option = JOptionPane.showConfirmDialog(dialog, list, LocaleImpl.getInstance().getString("view.task.validate.message"), JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        return;
                    }
                }
                updateTask();
                if (newTask) {
                    project.addTask(task);
                } else {
                    project.replaceTask(old, task);
                }
                task = null;
                setVisible(false);
            } else if (cmd.equals(ACTION_CHOOSE_DESTDIR)) {
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int option = fc.showDialog(null, LocaleImpl.getInstance().getString("view.task.settings.dialog.destination"));
                if (option == JFileChooser.APPROVE_OPTION) {
                    buildDir.setText(fc.getSelectedFile().getAbsolutePath());
                }
            } else if (cmd.equals(ACTION_URI_ADD)) {
                ProcessURI uri = new ProcessURI();
                uri.setSrcPrefix(srcPrefix.getText());
                uri.setType((String) type.getSelectedItem());
                uri.setUri(uriEdit.getText());
                task.addProcessURI(uri);
                setUriDefault();
                changeURIButton.setEnabled(false);
            } else if (cmd.equals(ACTION_URI_CHANGE)) {
                ProcessURI uri = new ProcessURI();
                uri.setSrcPrefix(srcPrefix.getText());
                uri.setType((String) type.getSelectedItem());
                uri.setUri(uriEdit.getText());
                uri.setTask(olduri.getTask());
                task.replaceProcessURI(olduri, uri);
                setUriDefault();
                changeURIButton.setEnabled(false);
            } else if (cmd.equals(ACTION_URI_EDIT)) {
                int index = uris.getSelectedIndex();
                if (index >= 0) {
                    changeURIButton.setEnabled(true);
                    olduri = (ProcessURI) task.getProcessURI().get(index);
                    srcPrefix.setText(olduri.getSrcPrefix());
                    type.setSelectedItem(olduri.getType());
                    uriEdit.setText(olduri.getUri());
                }
            } else if (cmd.equals(ACTION_URI_REMOVE)) {
                int index = uris.getSelectedIndex();
                if (index >= 0) {
                    ProcessURI uri = task.removeProcessURI(index);
                }
            } else if (cmd.equals(ACTION_PUBLISHING_ADD)) {
                if (publishTarget.getSelectedIndex() > -1) {
                    changePublishButton.setEnabled(false);
                    PublishDestination pd = new PublishDestination();
                    pd.setTitle(publishTitle.getText());
                    pd.setDestination(publishDestination.getText());
                    pd.setSource(publishSource.getText());
                    PublishTarget pt = (PublishTarget) publishTarget.getSelectedItem();
                    pd.setTargetID(pt.getID());
                    task.addPublishDestination(pd);
                    changePublishButton.setEnabled(false);
                    setDefaultPublishOptions();
                } else {
                    JOptionPane.showMessageDialog(dialog, LocaleImpl.getInstance().getString("view.task.validate.no_publishroot_selected"), "", JOptionPane.ERROR_MESSAGE);
                }
            } else if (cmd.equals(ACTION_PUBLISHING_CHOOSE_SOURCE)) {
                String path = "";
                if (task.getBuildDir().length() > 0) {
                    path = task.getBuildDir();
                } else {
                    path = project.getCocoonBeanConfiguration().getDestDir();
                }
                fc.setSelectedFile(new File(path));
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int option = fc.showDialog(null, LocaleImpl.getInstance().getString("view.task.settings.dialog.publish"));
                if (option == JFileChooser.APPROVE_OPTION) {
                    publishSource.setText(fc.getSelectedFile().getAbsolutePath());
                }
            } else if (cmd.equals(ACTION_PUBLISHING_EDIT)) {
                int index = publish.getSelectedIndex();
                if (index >= 0) {
                    PublishDestination pd = (PublishDestination) task.getPublishDestinations().get(index);
                    publishTitle.setText(pd.getTitle());
                    publishSource.setText(pd.getSource());
                    publishDestination.setText(pd.getDestination());
                    PublishTarget pt = project.getPublishTarget(pd.getTargetID());
                    publishTarget.setSelectedItem(pt);
                    oldPublish = pd;
                    changePublishButton.setEnabled(true);
                }
            } else if (cmd.equals(ACTION_PUBLISHING_REMOVE)) {
                int index = publish.getSelectedIndex();
                if (index >= 0) {
                    task.removePublishDestination((PublishDestination) task.getPublishDestinations().get(index));
                }
            } else if (cmd.equals(ACTION_PUBLISHING_CHANGE)) {
                PublishDestination pd = new PublishDestination();
                pd.setTitle(publishTitle.getText());
                pd.setDestination(publishDestination.getText());
                pd.setSource(publishSource.getText());
                if (publishTarget.getSelectedIndex() > -1) {
                    PublishTarget pt = (PublishTarget) publishTarget.getSelectedItem();
                    pd.setTargetID(pt.getID());
                }
                task.replacePublishDestination(oldPublish, pd);
                changePublishButton.setEnabled(false);
                setDefaultPublishOptions();
            }
        }

        public void setDefaultValues() {
            name.setText("");
            description.setText("");
            buildDir.setText("");
            uriEdit.setText("");
            followLinks.setEnabled(true);
            confirmExtension.setEnabled(false);
            partial.setEnabled(false);
            clean.setEnabled(false);
            sitemap.removeAll();
            uris.removeAll();
            publish.removeAll();
            setUriDefault();
            setDefaultPublishOptions();
        }

        public void valueChanged(ListSelectionEvent e) {
            int index = e.getFirstIndex();
            if (index >= 0) {
                String sitemapURI = (String) sitemap.getSelectedValue();
                uriEdit.setText(sitemapURI);
            } else {
                uriEdit.setText("");
            }
        }

        private void updateTask() {
            task.setTitle(name.getText());
            task.setDescription(description.getText());
            task.setBuildDir(buildDir.getText());
            task.setDiffBuild(partial.isSelected());
            task.setCleanBuild(clean.isSelected());
            task.setFollowLinks(followLinks.isSelected());
            task.setConfirmExtensions(confirmExtension.isSelected());
            Iterator i = task.getProcessURI().iterator();
            while (i.hasNext()) {
                ProcessURI uri = (ProcessURI) i.next();
                uri.setDest(buildDir.getText());
            }
        }

        public void setTask(Task task) {
            newTask = false;
            this.task = (Task) task.clone();
            this.old = task;
            setElements(this.task);
        }

        private void setElements(Task task) {
            setUriDefault();
            setDefaultPublishOptions();
            name.setText(task.getTitle());
            buildDir.setText(task.getBuildDir());
            description.setText(task.getDescription());
            followLinks.setSelected(task.isFollowLinks());
            confirmExtension.setSelected(task.isConfirmExtensions());
            clean.setSelected(task.isCleanBuild());
            partial.setSelected(task.isDiffBuild());
            uris.setModel(task.getProcessListModel());
            publish.setModel(task.getPublishListModel());
            tp.setSelectedIndex(0);
        }

        public void newTask() {
            newTask = true;
            this.task = new Task();
            this.task.setBeanConfiguration(project.getCocoonBeanConfiguration());
            setElements(task);
        }

        private void setUriDefault() {
            srcPrefix.setText("");
            type.setSelectedItem(typeData[0]);
            uriEdit.setText("");
            changeURIButton.setEnabled(false);
        }

        private void setDefaultPublishOptions() {
            publishTitle.setText("");
            publishDestination.setText("");
            publishSource.setText("");
            publishTarget.setSelectedIndex(-1);
            changePublishButton.setEnabled(false);
        }

        private Vector validateInput() {
            Vector msg = new Vector();
            if (name.getText().equals("")) {
                msg.add(LocaleImpl.getInstance().getString("view.task.validate.error.no_title"));
            }
            return msg;
        }
    }
}
