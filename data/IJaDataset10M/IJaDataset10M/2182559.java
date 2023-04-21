package net.sf.mzmine.modules.peaklistmethods.gapfilling.peakfinder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import net.sf.mzmine.data.ParameterSet;
import net.sf.mzmine.data.PeakList;
import net.sf.mzmine.data.RawDataFile;
import net.sf.mzmine.desktop.Desktop;
import net.sf.mzmine.desktop.MZmineMenu;
import net.sf.mzmine.main.MZmineCore;
import net.sf.mzmine.modules.batchmode.BatchStep;
import net.sf.mzmine.modules.batchmode.BatchStepCategory;
import net.sf.mzmine.taskcontrol.Task;
import net.sf.mzmine.util.GUIUtils;
import net.sf.mzmine.util.dialogs.ExitCode;
import net.sf.mzmine.util.dialogs.ParameterSetupDialog;

public class PeakFinder implements BatchStep, ActionListener {

    final String helpID = GUIUtils.generateHelpID(this);

    public static final String MODULE_NAME = "Peak finder";

    private PeakFinderParameters parameters;

    private Desktop desktop;

    /**
     * @see net.sf.mzmine.main.MZmineModule#initModule(net.sf.mzmine.main.MZmineCore)
     */
    public void initModule() {
        this.desktop = MZmineCore.getDesktop();
        parameters = new PeakFinderParameters();
        desktop.addMenuItem(MZmineMenu.GAPFILLING, MODULE_NAME, "Secondary peak detection, trying to find a missing peak", KeyEvent.VK_G, false, this, null);
    }

    public String toString() {
        return MODULE_NAME;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        PeakList[] selectedPeakLists = desktop.getSelectedPeakLists();
        if (selectedPeakLists.length < 1) {
            desktop.displayErrorMessage("Please select peak lists for gap-filling");
            return;
        }
        ExitCode exitCode = setupParameters(parameters);
        if (exitCode != ExitCode.OK) return;
        runModule(null, selectedPeakLists, parameters.clone());
    }

    /**
     * @see net.sf.mzmine.modules.BatchStep#setupParameters(net.sf.mzmine.data.ParameterSet)
     */
    public ExitCode setupParameters(ParameterSet currentParameters) {
        ParameterSetupDialog dialog = new ParameterSetupDialog("Please set parameter values for " + toString(), (PeakFinderParameters) currentParameters, helpID);
        dialog.setVisible(true);
        return dialog.getExitCode();
    }

    /**
     * @see net.sf.mzmine.main.MZmineModule#getParameterSet()
     */
    public ParameterSet getParameterSet() {
        return parameters;
    }

    public void setParameters(ParameterSet parameters) {
        this.parameters = (PeakFinderParameters) parameters;
    }

    /**
     * @see net.sf.mzmine.modules.BatchStep#runModule(net.sf.mzmine.data.RawDataFile[],
     *      net.sf.mzmine.data.PeakList[], net.sf.mzmine.data.ParameterSet,
     *      net.sf.mzmine.taskcontrol.Task[]Listener)
     */
    public Task[] runModule(RawDataFile[] dataFiles, PeakList[] peakLists, ParameterSet parameters) {
        if (peakLists == null || peakLists.length < 1) {
            throw new IllegalArgumentException("Gap-filling requires a peak list");
        }
        Task tasks[] = new PeakFinderTask[peakLists.length];
        for (int i = 0; i < peakLists.length; i++) {
            tasks[i] = new PeakFinderTask(peakLists[i], (PeakFinderParameters) parameters);
        }
        MZmineCore.getTaskController().addTasks(tasks);
        return tasks;
    }

    public BatchStepCategory getBatchStepCategory() {
        return BatchStepCategory.GAPFILLING;
    }
}
