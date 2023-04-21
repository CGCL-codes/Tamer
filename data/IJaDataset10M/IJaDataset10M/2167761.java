package net.sf.mzmine.modules.projectmethods.projectsave;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.sf.mzmine.main.MZmineCore;
import net.sf.mzmine.parameters.Parameter;
import net.sf.mzmine.parameters.impl.SimpleParameterSet;
import net.sf.mzmine.parameters.parametertypes.FileNameParameter;
import net.sf.mzmine.util.ExitCode;

public class ProjectSaveAsParameters extends SimpleParameterSet {

    private static final FileFilter filters[] = new FileFilter[] { new FileNameExtensionFilter("MZmine projects", "mzmine") };

    public static final FileNameParameter projectFile = new FileNameParameter("Project file", "File name of project to be saved");

    public ProjectSaveAsParameters() {
        super(new Parameter[] { projectFile });
    }

    public ExitCode showSetupDialog() {
        JFileChooser chooser = new JFileChooser();
        for (FileFilter filter : filters) chooser.setFileFilter(filter);
        File currentFile = getParameter(projectFile).getValue();
        if (currentFile != null) {
            File currentDir = currentFile.getParentFile();
            if ((currentDir != null) && (currentDir.exists())) chooser.setCurrentDirectory(currentDir);
        }
        chooser.setMultiSelectionEnabled(false);
        int returnVal = chooser.showSaveDialog(MZmineCore.getDesktop().getMainFrame());
        if (returnVal != JFileChooser.APPROVE_OPTION) return ExitCode.CANCEL;
        File selectedFile = chooser.getSelectedFile();
        if (!selectedFile.getName().endsWith(".mzmine")) {
            selectedFile = new File(selectedFile.getPath() + ".mzmine");
        }
        if (selectedFile.exists()) {
            int selectedValue = JOptionPane.showConfirmDialog(MZmineCore.getDesktop().getMainFrame(), selectedFile.getName() + " already exists, overwrite ?", "Question...", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (selectedValue != JOptionPane.YES_OPTION) return ExitCode.CANCEL;
        }
        getParameter(projectFile).setValue(selectedFile);
        return ExitCode.OK;
    }
}
