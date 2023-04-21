package org.kalypso.nofdpidss.report.worker.builders.evaluation.valuebenefit;

import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.xmlbeans.XmlException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.kalypso.contribs.eclipse.core.resources.ResourceUtilities;
import org.kalypso.contribs.eclipse.core.runtime.StatusUtilities;
import org.kalypso.nofdpidss.core.NofdpCorePlugin;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.ICriterionDefinition;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IValueBenefitFunction;
import org.kalypso.nofdpidss.core.view.parts.IValueBenefitMenuPart;
import org.kalypso.nofdpidss.evaluation.value.benefit.widgets.VBFunctionSectionBuilder;
import org.kalypso.nofdpidss.report.base.ReportModules.REPORT_MODULE;
import org.kalypso.nofdpidss.report.interfaces.IPoolContributor;
import org.kalypso.nofdpidss.report.worker.ReportFolders;
import org.kalypso.nofdpidss.report.worker.builders.SubModuleReportBuilder;
import org.kalypso.observation.IObservation;
import org.kalypso.observation.result.IRecord;
import org.kalypso.observation.result.TupleResult;
import org.kalypso.ogc.gml.om.ObservationFeatureFactory;
import org.kalypso.openofficereportservice.util.schema.ImageReplacementType;
import org.kalypso.openofficereportservice.util.schema.StringReplacementType;
import org.kalypso.openofficereportservice.util.schema.TableCellType;
import org.kalypso.openofficereportservice.util.schema.TableHeaderType;
import org.kalypso.openofficereportservice.util.schema.TableReplacementType;
import org.kalypso.openofficereportservice.util.schema.TableRowType;
import de.openali.odysseus.chart.factory.config.ChartConfigurationLoader;
import de.openali.odysseus.chart.factory.config.ChartExtensionLoader;
import de.openali.odysseus.chart.factory.config.ChartFactory;
import de.openali.odysseus.chart.framework.model.impl.ChartModel;
import de.openali.odysseus.chart.framework.util.ChartUtilities;
import de.openali.odysseus.chart.framework.util.img.ChartImageFactory;
import de.openali.odysseus.chart.framework.view.impl.ChartComposite;

/**
 * @author Dirk Kuch
 */
public class SMRBuilderEvaluationValueBenefitCriterion extends SubModuleReportBuilder {

    private final ICriterionDefinition m_definition;

    public SMRBuilderEvaluationValueBenefitCriterion(final IPoolContributor contributor, final ReportFolders folders, final ICriterionDefinition definition) {
        super(contributor, REPORT_MODULE.eEvaluationValueBenefitAnalysis, null, folders);
        m_definition = definition;
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.report.builders.AbstractReportBuilder#getImageRelacements()
   */
    @Override
    protected ImageReplacementType[] getImageRelacements() {
        final List<ImageReplacementType> replacements = new ArrayList<ImageReplacementType>();
        final Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
        final IProject project = NofdpCorePlugin.getProjectManager().getActiveProject();
        final IValueBenefitMenuPart part = (IValueBenefitMenuPart) NofdpCorePlugin.getWindowManager().getMenuPart();
        final int width = 500;
        final int height = 300;
        try {
            part.setChartFeature("vbFunctionMember", m_definition.getFunction());
            part.setChartFeature("crDefFeature", m_definition);
            part.setChartFeature("criterionDefinition", m_definition);
            final IFile kodValueBenefitFunction = project.getFile(VBFunctionSectionBuilder.VB_FUNCTION_DEF);
            final URL contextValueBenefitFunction = ResourceUtilities.createURL(kodValueBenefitFunction);
            ChartConfigurationLoader ccl = null;
            try {
                ccl = new ChartConfigurationLoader(contextValueBenefitFunction);
                final ChartModel chartModel = new ChartModel();
                ChartFactory.configureChartModel(chartModel, ccl, ccl.getChartIds()[0], ChartExtensionLoader.getInstance(), null);
                final ChartComposite cc = new ChartComposite(shell, SWT.NONE, chartModel, new RGB(255, 255, 255));
                cc.setSize(new Point(width, height));
                chartModel.setAutoscale(false);
                ChartUtilities.maximize(chartModel);
                final ImageData id = ChartImageFactory.createChartImage(cc, shell.getDisplay(), width, height);
                final ImageLoader il = new ImageLoader();
                il.data = new ImageData[] { id };
                final IFile iFile = getFolders().getTempImageFolder().getFile(m_definition.getId() + "_func.png");
                if (iFile.exists()) iFile.delete(true, new NullProgressMonitor());
                final FileOutputStream os = new FileOutputStream(iFile.getLocation().toFile());
                il.save(os, SWT.IMAGE_PNG);
                os.close();
                replacements.add(getIRT("%VB_FUNCTION_DIAGRAM%", iFile.getLocationURI()));
            } catch (final XmlException e1) {
                NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e1));
            }
            final IFile kodValueBenefitOverview = project.getFile(VBFunctionSectionBuilder.VARIANT_RESULT_HISTOGRAM_DEF);
            final URL contextValueBenefitOverview = ResourceUtilities.createURL(kodValueBenefitOverview);
            ccl = null;
            try {
                ccl = new ChartConfigurationLoader(contextValueBenefitOverview);
                final ChartModel chartModel = new ChartModel();
                ChartFactory.configureChartModel(chartModel, ccl, ccl.getChartIds()[0], ChartExtensionLoader.getInstance(), null);
                final ChartComposite cc = new ChartComposite(shell, SWT.NONE, chartModel, new RGB(255, 255, 255));
                cc.setSize(new Point(width, height));
                chartModel.setAutoscale(false);
                ChartUtilities.maximize(chartModel);
                final ImageData id = ChartImageFactory.createChartImage(cc, shell.getDisplay(), width, height);
                final ImageLoader il = new ImageLoader();
                il.data = new ImageData[] { id };
                final IFile iFile = getFolders().getTempImageFolder().getFile(m_definition.getId() + "_over.png");
                if (iFile.exists()) iFile.delete(true, new NullProgressMonitor());
                final FileOutputStream os = new FileOutputStream(iFile.getLocation().toFile());
                il.save(os, SWT.IMAGE_PNG);
                os.close();
                replacements.add(getIRT("%VB_VARIANT_OVERVIEW%", iFile.getLocationURI()));
            } catch (final XmlException e1) {
                NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e1));
            }
        } catch (final Exception e) {
            NofdpCorePlugin.getDefault().getLog().log(StatusUtilities.statusFromThrowable(e));
        }
        return replacements.toArray(new ImageReplacementType[] {});
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.report.builders.SubModuleReportBuilder#getTableReplacements()
   */
    @Override
    protected TableReplacementType[] getTableReplacements() {
        final ArrayList<TableReplacementType> replacements = new ArrayList<TableReplacementType>();
        final TableReplacementType trt = new TableReplacementType();
        trt.setName("%VB_FUNCTION_TABLE%");
        trt.setTableHeader(new TableHeaderType());
        replacements.add(trt);
        final IValueBenefitFunction function = m_definition.getFunction();
        final IObservation<TupleResult> observation = ObservationFeatureFactory.toObservation(function);
        final TupleResult result = observation.getResult();
        final List<TableRowType> rows = trt.getTableRow();
        for (final IRecord record : result) {
            final Double value = (Double) record.getValue(0);
            final Double rating = (Double) record.getValue(1);
            final TableRowType row = new TableRowType();
            final List<TableCellType> cells = row.getTableCell();
            cells.add(getCell(String.format(Locale.ENGLISH, "%,.02f", value), 1));
            cells.add(getCell(String.format(Locale.ENGLISH, "%,.02f", rating), 2));
            rows.add(row);
        }
        return replacements.toArray(new TableReplacementType[] {});
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.report.builders.SubModuleReportBuilder#getTemplateFileName()
   */
    @Override
    protected String getTemplateFileName() {
        if (getLanguage().toLowerCase().startsWith("de")) return "vbCriterion_de.odt";
        return "vbCriterion.odt";
    }

    /**
   * @see org.kalypso.nofdpidss.ui.view.wizard.report.builders.SubModuleReportBuilder#getTextReplacements()
   */
    @Override
    protected StringReplacementType[] getTextReplacements() {
        final List<StringReplacementType> replacements = new ArrayList<StringReplacementType>();
        replacements.add(getSRT("%CRITERION%", m_definition.getName()));
        return replacements.toArray(new StringReplacementType[] {});
    }
}
