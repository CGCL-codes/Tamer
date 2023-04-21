package org.kalypso.nofdpidss.hydraulic.computation.commands;

import javax.xml.namespace.QName;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.IGeodataCategories;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.IGeodataCategory;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.IGeodataModel;
import org.kalypso.nofdpidss.core.base.gml.model.hydraulic.result.ICalculationCase;
import org.kalypso.nofdpidss.core.base.gml.pool.PoolGeoData;
import org.kalypso.nofdpidss.core.base.gml.pool.MyBasePool.POOL_TYPE;
import org.kalypso.nofdpidss.geodata.wizard.gds.add.WizardAddGeodata;
import org.kalypso.nofdpidss.hydraulic.computation.i18n.Messages;
import org.kalypso.nofdpidss.hydraulic.computation.processing.handler.HydraulicCalculationCaseHandler;

public class SaveInunationAreaHandler extends SaveComputedDataset {

    @Override
    public Object execute(final ExecutionEvent arg0) throws ExecutionException {
        try {
            final ICalculationCase caculationCase = getCalculationCase();
            if (caculationCase == null) return Status.CANCEL_STATUS;
            final PoolGeoData pool = (PoolGeoData) NofdpCorePlugin.getProjectManager().getPool(POOL_TYPE.eGeodata);
            final IGeodataModel model = pool.getModel();
            final IGeodataCategory[] categories = model.getCategories().getCategories(new QName[] { IGeodataCategories.QN_SUBCATEGORY_INUNDATION_AREA });
            if (categories.length != 1) throw new ExecutionException(Messages.SaveInunationAreaHandler_0);
            final HydraulicCalculationCaseHandler hcc = new HydraulicCalculationCaseHandler(caculationCase);
            final IFolder iFolder = hcc.getFloodZoneFolder();
            final IFile iFile = iFolder.getFile("inundationArea.shp");
            if (!iFile.exists()) throw new ExecutionException(Messages.SaveInunationAreaHandler_2);
            final HydraulicComputationImportGeodataBypass bypass = new HydraulicComputationImportGeodataBypass(categories[0], caculationCase, iFile);
            final WizardAddGeodata wizard = new WizardAddGeodata(NofdpCorePlugin.getProjectManager().getActiveProject(), pool.getModel(), categories[0], bypass, null);
            wizard.init(PlatformUI.getWorkbench(), null);
            final WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), wizard);
            dialog.open();
        } catch (final CoreException e) {
            NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
            return Status.CANCEL_STATUS;
        }
        return Status.OK_STATUS;
    }
}
