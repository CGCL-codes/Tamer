package org.kalypso.nofdpidss.flood.risk.commands;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.progress.UIJob;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.nofdp.idss.schema.schemata.gml.GmlConstants;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.gml.model.floodrisk.IFloodRiskUserDefinedTemplate;
import org.kalypso.nofdpidss.core.base.gml.model.geodata.IGeodataCategory;
import org.kalypso.nofdpidss.core.common.utils.modules.ShapeData;
import org.kalypso.nofdpidss.flood.risk.i18n.Messages;
import org.kalypso.nofdpidss.geodata.wizard.gds.add.AbstractByPass;
import org.kalypso.nofdpidss.geodata.wizard.gds.add.commmon.PageCommonGeoDataSettings;
import org.kalypso.nofdpidss.geodata.wizard.gds.add.commmon.PageSelectGeodataFile;
import org.kalypsodeegree.model.feature.Feature;

public class FloodRiskImportGeodataBypass extends AbstractByPass {

    protected final IFloodRiskUserDefinedTemplate m_template;

    protected final IGeodataCategory m_category;

    protected IFile m_shapeFile;

    public FloodRiskImportGeodataBypass(final IFloodRiskUserDefinedTemplate template, final IGeodataCategory category) {
        m_template = template;
        m_category = category;
    }

    protected IFloodRiskUserDefinedTemplate getTemplate() {
        return m_template;
    }

    public void byPass(final WizardPage page) {
        if (page instanceof PageSelectGeodataFile) {
            selectGeodataFile((PageSelectGeodataFile) page);
        } else if (page instanceof PageCommonGeoDataSettings) {
            try {
                updateCommonSettings((PageCommonGeoDataSettings) page);
            } catch (final CoreException e) {
                NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
            }
        }
    }

    private void updateCommonSettings(final PageCommonGeoDataSettings page) throws CoreException {
        new UIJob("") {

            @Override
            public IStatus runInUIThread(final IProgressMonitor monitor) {
                page.setTBName(Messages.FloodRiskImportGeodataBypass_1);
                page.setTBDescription(Messages.FloodRiskImportGeodataBypass_2);
                page.setTBLongDescription(Messages.FloodRiskImportGeodataBypass_3);
                return Status.OK_STATUS;
            }
        }.schedule();
    }

    private void selectGeodataFile(final PageSelectGeodataFile page) {
        new UIJob(Messages.FloodRiskImportGeodataBypass_0) {

            @Override
            public IStatus runInUIThread(final IProgressMonitor monitor) {
                page.setFileLocation(m_shapeFile.getLocation().toOSString());
                return Status.OK_STATUS;
            }
        }.schedule();
    }

    public void doAdditionalImportWork() {
    }

    public void setDefaultShapeDataColumnSelection(final ComboViewer viewer, final Feature feature, final ShapeData[] mappingData) {
        final UIJob job = new UIJob("") {

            @Override
            public IStatus runInUIThread(IProgressMonitor monitor) {
                if (GmlConstants.QN_GEODATA_SET_INTERNAL_ATTR_TYPE.equals(feature.getFeatureType().getQName())) {
                    final Object objAttrName = feature.getProperty(GmlConstants.QN_GEODATA_SET_INTERNAL_ATTR_NAME);
                    if ("riskzone".equals(objAttrName)) for (final ShapeData shapeData : mappingData) {
                        String name = shapeData.getName().toUpperCase();
                        if ("RISKVALUE".equals(name)) {
                            viewer.setSelection(new StructuredSelection(shapeData));
                            viewer.getCombo().setEnabled(false);
                            return Status.OK_STATUS;
                        }
                    }
                }
                return Status.OK_STATUS;
            }
        };
        job.schedule();
    }

    public void setShapeFile(final IFile shapeFile) {
        m_shapeFile = shapeFile;
    }

    /**
   * @see org.kalypso.nofdpidss.core.common.utils.modules.IByPassImportData#isCrsPanelActive()
   */
    public boolean isCrsPanelActive() {
        return false;
    }
}
