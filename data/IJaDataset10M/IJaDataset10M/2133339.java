package es.eucm.eadventure.editor.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.accessibility.AccessibleContext;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import es.eucm.eadventure.common.auxiliar.ReleaseFolders;
import es.eucm.eadventure.common.gui.TC;
import es.eucm.eadventure.editor.auxiliar.filefilters.FolderFileFilter;

public class ProjectFolderChooser extends JFileChooser {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JTextField projectName = null;

    private static File getDefaultSelectedFile() {
        String defaultName = TC.get("Operation.NewFileTitle");
        File parentDir = getProjectsFolder();
        int i = 0;
        while (new File(parentDir, defaultName).exists()) {
            i++;
            defaultName = TC.get("Operation.NewFileTitle") + " (" + i + ")";
        }
        return new File(defaultName);
    }

    private static File getProjectsFolder() {
        File parentDir = ReleaseFolders.projectsFolder();
        if (!parentDir.exists()) parentDir.mkdirs();
        return parentDir;
    }

    public ProjectFolderChooser(boolean checkName, boolean checkDescriptor) {
        super(getProjectsFolder());
        super.setDialogTitle(TC.get("Operation.NewProjectTitle"));
        super.setMultiSelectionEnabled(false);
        super.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        for (javax.swing.filechooser.FileFilter filter : super.getChoosableFileFilters()) {
            super.removeChoosableFileFilter(filter);
        }
        FolderFileFilter filter = new FolderFileFilter(checkName, checkDescriptor, this);
        super.addChoosableFileFilter(filter);
        super.setFileFilter(filter);
        super.setFileHidingEnabled(true);
        super.setSelectedFile(getDefaultSelectedFile());
        super.setAcceptAllFileFilterUsed(false);
        preparePanel();
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            this.setPreferredSize(new Dimension(700, 600));
        }
    }

    private void preparePanel() {
        String title = TC.get("Operation.NewProjectTitle");
        putClientProperty(AccessibleContext.ACCESSIBLE_DESCRIPTION_PROPERTY, title);
        JPanel mainPanel = new JPanel();
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());
        JTextArea info = new JTextArea();
        info.setColumns(10);
        info.setWrapStyleWord(true);
        info.setFont(new Font(Font.SERIF, Font.PLAIN, 12));
        info.setEditable(false);
        info.setBackground(infoPanel.getBackground());
        info.setText(TC.get("Operation.NewProjectMessage", FolderFileFilter.getAllowedChars()));
        infoPanel.add(info, BorderLayout.NORTH);
        String os = System.getProperty("os.name");
        if (os.contains("MAC") || os.contains("mac") || os.contains("Mac")) {
            projectName = new JTextField(30);
            projectName.setText(ProjectFolderChooser.getDefaultSelectedFile().getName());
            JPanel tempName = new JPanel();
            tempName.add(new JLabel(TC.get("Operation.NewProjectName")));
            tempName.add(projectName);
            JButton create = new JButton(TC.get("Operation.CreateNewProject"));
            create.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    if (projectName.getText() != null) {
                        String name = projectName.getText();
                        if (!name.endsWith(".eap")) name = name + ".eap";
                        File file = new File(ProjectFolderChooser.this.getCurrentDirectory().getAbsolutePath() + File.separatorChar + name);
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                                ProjectFolderChooser.this.updateUI();
                                ProjectFolderChooser.this.setSelectedFile(file);
                                ProjectFolderChooser.this.approveSelection();
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            });
            tempName.add(create);
            infoPanel.add(tempName, BorderLayout.SOUTH);
        }
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(createOpenFilePanel(), BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        add(mainPanel);
    }

    private JPanel createOpenFilePanel() {
        JPanel panelOpen = new JPanel();
        LayoutManager layout = getLayout();
        if (layout instanceof BorderLayout) {
            panelOpen.setLayout(new BorderLayout());
            BorderLayout currentLayout = (BorderLayout) getLayout();
            for (Component comp : getComponents()) {
                panelOpen.add(comp, currentLayout.getConstraints(comp));
            }
        } else if (layout instanceof BoxLayout) {
            BoxLayout currentLayout = (BoxLayout) getLayout();
            panelOpen.setLayout(new BoxLayout(panelOpen, currentLayout.getAxis()));
            for (Component comp : getComponents()) {
                panelOpen.add(comp);
            }
        }
        return panelOpen;
    }
}
