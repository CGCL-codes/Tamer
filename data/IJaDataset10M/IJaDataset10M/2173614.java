package org.vikamine.app.rcp.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.vikamine.app.Resources;
import org.vikamine.app.rcp.view.SGWorkspace;
import org.vikamine.kernel.subgroup.SG;
import org.vikamine.kernel.subgroup.SGSet;
import org.vikamine.kernel.subgroup.SGSets;
import org.vikamine.swing.subgroup.AllSubgroupPluginController;
import org.vikamine.swing.subgroup.SGStatInfoSettings;
import org.vikamine.swing.subgroup.export.SGStatisticsExporter;
import org.vikamine.swing.util.Utils;

/**
 * The Class SGWorkspaceExportToExcel.
 * 
 * @author Alex Plischke
 */
public class SGWorkspaceExportToExcel extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        FileDialog fd = new FileDialog(Display.getDefault().getActiveShell(), SWT.SAVE);
        fd.setText(Utils.getResourceStringFromDefaultBundle("%vikamine.sgStatInfoView.menu.exportSelectedToExcel"));
        fd.setFilterExtensions(new String[] { "*.xls" });
        String saveAs = fd.open();
        if (saveAs != null) {
            File file = new File(saveAs);
            OutputStream out = null;
            try {
                out = new FileOutputStream(file);
                SGWorkspace view = (SGWorkspace) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("org.vikamine.app.rcp.view.SGWorkspace");
                TreeItem[] items = view.getTreeViewer().getTree().getSelection();
                HashSet<SG> sgs = new HashSet<SG>();
                for (TreeItem item : items) {
                    TreeItem parent = item.getParentItem();
                    if (parent != null) {
                        sgs.add((SG) parent.getData());
                    } else {
                        sgs.add((SG) item.getData());
                    }
                }
                SGStatInfoSettings settings = AllSubgroupPluginController.getInstance().getSubgroupDiscoveryController().getSubgroupExportAsTextStatInfoSettings();
                SGStatisticsExporter statCreator = new SGStatisticsExporter(settings);
                SGSet sgSet = SGSets.createSGSet();
                sgSet.addAll(sgs);
                sgSet.setName(Resources.I18N.getString("vikamine.SGSetTree.actions.userSelectedSGs"));
                List<SGSet> sgList = new ArrayList<SGSet>();
                sgList.add(sgSet);
                statCreator.createXLSFileExport(sgList, out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }
}
