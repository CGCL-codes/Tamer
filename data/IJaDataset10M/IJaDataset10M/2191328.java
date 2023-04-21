package de.itemis.gmf.tools.contribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import de.itemis.gmf.tools.Activator;
import de.itemis.gmf.tools.popup.actions.GMFButtonContributionFactory;
import de.itemis.gmf.tools.preferences.GmfModel;
import de.itemis.gmf.tools.preferences.PreferenceUtil;

public class GMFToolsHandler extends AbstractHandler {

    public static final String GENERATE_LATEST_COMMAND_ID = "de.itemis.gmf.tools.commands.generateLatest";

    public static final String GENERATE_PREDEFINED_COMMAND_ID = "de.itemis.gmf.tools.commands.generatePredefined";

    public static final String GMF_TOOLS_CONFIG_PARAMETER_TYPE_ID = "de.itemis.gmf.tools.commands.parameter.type.gmfConfig";

    public static final String GMF_TOOLS_CONFIG_PARAMETER_ID = "de.itemis.gmf.tools.commands.parameter.gmfConfig";

    public static final String GMF_TOOLS_CONFIG_PARAMETER_NAME = "de.itemis.gmf.tools.commands.parameter.gmfConfig";

    public static final String GMF_TOOLS_CONFIG_SET_PARAMETER_NAME = "de.itemis.gmf.tools.commands.parameter.gmfConfigSet";

    private static List<GmfModel> lastGmfModel;

    private IWorkbenchWindow window;

    private List<GmfModel> gmfModel;

    public GMFToolsHandler() {
    }

    public Object execute(ExecutionEvent event) throws ExecutionException {
        window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
        try {
            gmfModel = getGmfModel(event);
            lastGmfModel = gmfModel;
            if (gmfModel != null) {
                for (GmfModel model : gmfModel) {
                    new ProgressMonitorDialog(window.getShell()).run(false, true, new GMFToolsGeneration(model, Collections.<String, String>emptyMap()));
                }
            } else {
                MessageDialog.openInformation(window.getShell(), "No GMF file set selected", "Go to Preferences -> GMF Tools and create a set of files for the editor first.");
            }
        } catch (Exception e) {
            reportError(e);
        }
        return null;
    }

    private void reportError(Exception e) {
        MessageDialog.openError(window.getShell(), "Error", e.getMessage());
        Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error executing GMF action", e));
    }

    private List<GmfModel> getGmfModel(ExecutionEvent event) throws ExecutionException {
        List<GmfModel> result = new ArrayList<GmfModel>();
        if (event != null) {
            String parameter = event.getParameter(GMF_TOOLS_CONFIG_PARAMETER_NAME);
            if (parameter != null) {
                result.add((GmfModel) new GmfModel.Factory().deserialize(parameter));
                return result;
            }
            parameter = event.getParameter(GMF_TOOLS_CONFIG_SET_PARAMETER_NAME);
            if (parameter != null) {
                String[] models = parameter.split(GMFButtonContributionFactory.GMF_MODELS_SEPARATOR);
                for (String model : models) {
                    result.add((GmfModel) new GmfModel.Factory().deserialize(model));
                }
                return result;
            }
        }
        ISelection selection = window.getSelectionService().getSelection();
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection structuredSelection = (IStructuredSelection) selection;
            if (!structuredSelection.isEmpty()) {
                Object selectedElement = structuredSelection.getFirstElement();
                if (selectedElement instanceof IFile) {
                    for (GmfModel gmfModels : PreferenceUtil.getGmfModels()) {
                        if (gmfModels.hasFile((IFile) selectedElement)) {
                            if (lastGmfModel == null) {
                                lastGmfModel = result;
                            } else {
                                lastGmfModel.clear();
                            }
                            lastGmfModel.add(gmfModels);
                        }
                    }
                }
            }
        }
        return lastGmfModel;
    }
}
