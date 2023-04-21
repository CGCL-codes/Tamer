package com.intel.gpe.client2.portalclient.portlets.actions;

import java.io.IOException;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.xml.namespace.QName;
import com.intel.gpe.client2.common.filesets.RemoteFileSetElement;
import com.intel.gpe.client2.portalclient.GridBeanJob;
import com.intel.gpe.client2.portalclient.portlets.ActionHandler;
import com.intel.gpe.client2.portalclient.portlets.InputParametersPanel;
import com.intel.gpe.client2.portalclient.portlets.PortalClientPortlet;
import com.intel.gpe.client2.portalclient.portlets.StringConstants;
import com.intel.gpe.client2.portalclient.portlets.imports.FileSetImport;
import com.intel.gpe.client2.portalclient.portlets.imports.ImportTypes;
import com.intel.gpe.client2.portalclient.portlets.stages.GridBeanStage2;
import com.intel.gpe.client2.portalclient.portlets.tree.FileChooserNode;
import com.intel.gpe.client2.portalclient.portlets.tree.RemoteFileTreeModel;
import com.intel.gpe.gridbeans.web.webcontrols2.tree.Node;

/**
 * GridBean file imports page: add exclude mask to the fileset
 * 
 * @author Alexander Lukichev
 * @version $Id: ExcludeMaskAction.java,v 1.2 2007/02/27 11:52:21 vashorin Exp $
 *
 */
public class ExcludeMaskAction extends ActionHandler {

    public static final String NAME = "excludeMask";

    public ExcludeMaskAction(PortalClientPortlet portlet) {
        super(portlet);
    }

    @Override
    public void handle(ActionRequest request, ActionResponse response) throws PortletException, IOException {
        response.setRenderParameter(StringConstants.STAGE, GridBeanStage2.NAME);
        GridBeanJob gridBeanJob = (GridBeanJob) getPortlet().getSessionObject(request, StringConstants.GRIDBEAN_JOB);
        if (gridBeanJob != null) {
            String selectedImportStr = (String) getPortlet().getSessionObject(request, StringConstants.SELECTED_IMPORT);
            String selectedImportType = (String) getPortlet().getSessionObject(request, StringConstants.SELECTED_IMPORT_TYPE);
            if (selectedImportStr != null && selectedImportType != null) {
                String[] parts = selectedImportStr.split("\\$");
                QName selectedImport = QName.valueOf(parts[0]);
                InputParametersPanel inputs = (InputParametersPanel) getPortlet().getSessionObject(request, StringConstants.GRIDBEAN_JOB_INPUTS);
                String mask = (String) request.getParameter("mask");
                if (selectedImportType.equals(ImportTypes.FILE_SET)) {
                    FileSetImport fileSetImport = (FileSetImport) inputs.getInputParameter(selectedImport);
                    if (fileSetImport == null) {
                        fileSetImport = new FileSetImport();
                        inputs.setInputParameter(selectedImport, fileSetImport);
                    }
                    RemoteFileTreeModel model = (RemoteFileTreeModel) getPortlet().getSessionObject(request, GridBeanStage2.REMOTE_FILE_TREE.toString());
                    Node node = model.getSelectedNode();
                    if (node instanceof FileChooserNode) {
                        if (((FileChooserNode) node).getFile() != null && ((FileChooserNode) node).getFile().isDirectory()) {
                            RemoteFileSetElement directoryEntry = new RemoteFileSetElement(((FileChooserNode) node).getTargetSystem(), ((FileChooserNode) node).getStorage().toString(), ((FileChooserNode) node).getFile().getPath());
                            if (mask != null && mask.length() > 0) {
                                directoryEntry.getExcludes().add(mask);
                            }
                            fileSetImport.addEntry(directoryEntry);
                        }
                    }
                } else if (selectedImportType.equals(ImportTypes.FILESET_ENTRY)) {
                    FileSetImport fileSetImport = (FileSetImport) inputs.getInputParameter(selectedImport);
                    int idx = Integer.parseInt(parts[1]);
                    RemoteFileSetElement directoryEntry = (RemoteFileSetElement) fileSetImport.getEntries().get(idx);
                    if (mask != null && mask.length() > 0) {
                        directoryEntry.getExcludes().add(mask);
                    }
                }
            }
        }
    }
}
