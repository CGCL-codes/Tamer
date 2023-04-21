package gruntspud.project;

import gruntspud.Constants;
import gruntspud.GruntspudContext;
import gruntspud.GruntspudUtil;
import gruntspud.editor.MiniTextEditor;
import gruntspud.filter.CVSFileFilterModel;
import gruntspud.ui.AbstractTab;
import gruntspud.ui.FileNameTextField;
import gruntspud.ui.UIUtil;
import gruntspud.ui.XTextField;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Mini text editor
 * 
 * @author Brett Smiht @created 26 May 2002
 */
public class ProjectEditorPane extends AbstractTab {

    private XTextField name;

    private MiniTextEditor description;

    private FileNameTextField home;

    private JComboBox defaultFilter;

    private Project project;

    private GruntspudContext context;

    private CVSFileFilterModel filterModel;

    /**
	 * Constructor
	 * 
	 * @param host Description of the Parameter
	 */
    public ProjectEditorPane(GruntspudContext context, Project project) {
        super("Project", UIUtil.getCachedIcon(Constants.ICON_TOOL_LARGE_PROJECTS));
        this.context = context;
        setTabToolTipText("Project details.");
        setLayout(new BorderLayout());
        setTabMnemonic('p');
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.weightx = 0.0;
        UIUtil.jGridBagAdd(p, new JLabel("Name"), gbc, GridBagConstraints.RELATIVE);
        gbc.weightx = 1.0;
        UIUtil.jGridBagAdd(p, name = new XTextField(15), gbc, GridBagConstraints.REMAINDER);
        gbc.weightx = 0.0;
        UIUtil.jGridBagAdd(p, new JLabel("Home"), gbc, GridBagConstraints.RELATIVE);
        gbc.weightx = 1.0;
        JFileChooser chooser = new JFileChooser(context.getViewManager().getHome());
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Choose project home directory");
        chooser.setApproveButtonText("Select");
        chooser.setApproveButtonMnemonic('s');
        chooser.setApproveButtonToolTipText("Use the selected directory as the home for the project");
        UIUtil.jGridBagAdd(p, home = new FileNameTextField(null, "", 15, true, false, chooser, false), gbc, GridBagConstraints.REMAINDER);
        description = new MiniTextEditor(context, false, false, false, Constants.PROJECT_DESCRIPTION_EDITOR_WORD_WRAP, true, MiniTextEditor.PLAIN_EDITOR);
        description.setBorder(BorderFactory.createTitledBorder("Description"));
        setLayout(new BorderLayout());
        add(p, BorderLayout.NORTH);
        add(description, BorderLayout.CENTER);
        setProject(project);
    }

    /**
	 * DOCUMENT ME!
	 */
    public void tabSelected() {
    }

    /**
	 * DOCUMENT ME!
	 */
    public void applyTab() {
        if (project != null) {
            project.setName(name.getText());
            project.setDescription(description.getText());
            project.setHome(new File(home.getText()));
        }
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
    public boolean validateTab() {
        try {
            if (name.getText().equals("")) {
                throw new IOException("You must give the project a unique name");
            }
            if (home.getText().equals("")) {
                throw new IOException("You must specify a directory for the home of the project.");
            }
            File f = new File(home.getText());
            if (!f.exists()) {
                int opt = JOptionPane.showConfirmDialog(this, "The home directory does not exist. Do you wish to create it?", "Create home directory", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (opt == JOptionPane.NO_OPTION) return false;
                if (!f.mkdirs()) {
                    throw new IOException("Home directory could not be created.");
                }
            }
            String n = name.getText();
            int i = context.getProjectListModel().indexOf(n);
            if (i != -1) {
                Project p = context.getProjectListModel().getProjectAt(i);
                if (p != project) {
                    throw new Exception("A project named " + n + " already exists.");
                }
            }
        } catch (Exception e) {
            GruntspudUtil.showErrorMessage(this, "Error", e);
            return false;
        }
        return true;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param profile DOCUMENT ME!
	 */
    public void setProject(Project project) {
        this.project = project;
        name.setText(project.getName());
        description.setText(project.getDescription() == null ? "" : project.getDescription());
        home.setText(project.getHome() == null ? "" : project.getHome().getAbsolutePath());
    }
}
