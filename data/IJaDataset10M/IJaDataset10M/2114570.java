package com.novasurv.turtle.frontend.swing.nav;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.novasurv.turtle.backend.project.Project;
import com.novasurv.turtle.backend.project.SourceFileContents;
import com.novasurv.turtle.frontend.swing.GradeTabbedPane;
import com.novasurv.turtle.frontend.swing.data.SwingDataManager;

/** @author Jason Dobies */
public class LoadFilesListener implements ActionListener {

    private JFrame owner;

    private GradeTabbedPane gradeTabPane;

    private SwingDataManager dataManager;

    private final Log log = LogFactory.getLog(this.getClass());

    public LoadFilesListener(JFrame owner, GradeTabbedPane gradeTabPane, SwingDataManager dataManager) {
        this.owner = owner;
        this.gradeTabPane = gradeTabPane;
        this.dataManager = dataManager;
    }

    public void actionPerformed(ActionEvent event) {
        Project project = dataManager.getActiveProject();
        if (project == null) {
            return;
        }
        List<SourceFileContents> sourceFileContents;
        try {
            sourceFileContents = dataManager.loadSourceFiles(project);
            if (sourceFileContents != null) {
                gradeTabPane.addSourceFileTabs(sourceFileContents);
            }
        } catch (Exception e) {
            log.error("Error loading source files for project [" + project + "]", e);
            JOptionPane.showMessageDialog(owner, "Could not load source files for project " + project.getStudentId(), "Error Loading Source", JOptionPane.ERROR_MESSAGE);
        }
    }
}
