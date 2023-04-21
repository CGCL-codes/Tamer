package imtek.optsuite.acquisition.io.actions;

import java.io.File;
import imtek.optsuite.acquisition.io.routinestep.ExportRoutineStep;
import imtek.optsuite.acquisition.routine.MeasurementRoutine;
import imtek.optsuite.acquisition.routine.actions.RoutineStepWorkbenchAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.nightlabs.base.util.RCPUtil;

/**
 * @author Alexander Bieber <fleque@users.sourceforge.net>
 */
public class ExportDataAction extends RoutineStepWorkbenchAction {

    private ExportRoutineStep exportRoutineStep;

    protected void run(MeasurementRoutine workbenchRoutine) {
        FileDialog fileDialog = getFileDialog();
        String fileName = fileDialog.open();
        if (fileName == null || "".equals(fileName)) return;
        ExportRoutineStep step = getExportRoutineStep();
        File exportFile = new File(fileName);
        if (exportFile.exists()) {
        }
        step.setExportDir(exportFile.getParentFile().getPath());
        step.setExportFilePattern(exportFile.getName());
        step.setAutoSelectFilter(true);
        workbenchRoutine.addRoutineStep(step);
        step.setExportLastStoredData(true);
        step.run(null);
    }

    protected ExportRoutineStep getExportRoutineStep() {
        if (exportRoutineStep == null) exportRoutineStep = new ExportRoutineStep();
        return exportRoutineStep;
    }

    protected FileDialog getFileDialog() {
        return new FileDialog(RCPUtil.getActiveWorkbenchShell(), SWT.SAVE);
    }
}
