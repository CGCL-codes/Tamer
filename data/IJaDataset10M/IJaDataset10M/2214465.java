package org.kalypso.nofdpidss.report.worker.builders.measures;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IMeasureWeir;
import org.kalypso.nofdpidss.report.base.ReportModules.REPORT_MODULE;
import org.kalypso.nofdpidss.report.interfaces.IPoolContributor;
import org.kalypso.nofdpidss.report.worker.ReportFolders;
import org.kalypso.nofdpidss.report.worker.builders.IReportBuilder;
import org.kalypso.openofficereportservice.util.schema.StringReplacementType;
import org.kalypso.openofficereportservice.util.schema.TableReplacementType;

public class MeasureWeirDetailsBuilder extends AbstractSubModuleMeasureBuilder implements IReportBuilder {

    private final IMeasureWeir m_measure;

    public MeasureWeirDetailsBuilder(final IPoolContributor contributor, final REPORT_MODULE module, final IMeasureWeir measure, final ReportFolders reportFolders) {
        super(contributor, module, measure, reportFolders);
        m_measure = measure;
    }

    @Override
    protected TableReplacementType[] getTableReplacements() {
        return new TableReplacementType[] {};
    }

    @Override
    protected String getTemplateFileName() {
        if (getLanguage().toLowerCase().startsWith("de")) return "measureWeir_de.odt";
        return "measureWeir.odt";
    }

    @Override
    protected StringReplacementType[] getAdditionalTextReplacements() {
        final List<StringReplacementType> replacements = new ArrayList<StringReplacementType>();
        final String crestHeightHeader = getLabel(m_measure.getFeatureType().getProperty(IMeasureWeir.QN_CREST_HEIGHT));
        final String crestHeight = String.format(Locale.ENGLISH, "%,.02f", m_measure.getCrestHeight());
        replacements.add(getSRT("%CREST_HEIGHT_HEADER%", crestHeightHeader));
        replacements.add(getSRT("%CREST_HEIGHT%", crestHeight));
        final String crestWidthHeader = getLabel(m_measure.getFeatureType().getProperty(IMeasureWeir.QN_CREST_WIDTH));
        final String crestWidth = String.format(Locale.ENGLISH, "%,.02f", m_measure.getCrestWidth());
        replacements.add(getSRT("%CREST_WIDTH_HEADER%", crestWidthHeader));
        replacements.add(getSRT("%CREST_WIDTH%", crestWidth));
        final String dischargeHeader = getLabel(m_measure.getFeatureType().getProperty(IMeasureWeir.QN_DISCHARGE_COEFF));
        final String discharge = String.format(Locale.ENGLISH, "%,.02f", m_measure.getDischargeCoeff());
        replacements.add(getSRT("%DISCHARGE_COEFF_HEADER%", dischargeHeader));
        replacements.add(getSRT("%DISCHARGE_COEFF%", discharge));
        final String fdHeader = getLabel(m_measure.getFeatureType().getProperty(IMeasureWeir.QN_FLOW_DIRECTION));
        final String fd = m_measure.getFlowDirection();
        replacements.add(getSRT("%FLOW_DIRECTION_HEADER%", fdHeader));
        replacements.add(getSRT("%FLOW_DIRECTION%", fd));
        final String lateralHeader = getLabel(m_measure.getFeatureType().getProperty(IMeasureWeir.QN_LATERAL_CONTRACTION));
        final String lateral = String.format(Locale.ENGLISH, "%,.02f", m_measure.getLateralContraction());
        replacements.add(getSRT("%LATERAL_CONTRACTION_HEADER%", lateralHeader));
        replacements.add(getSRT("%LATERAL_CONTRACTION%", lateral));
        return replacements.toArray(new StringReplacementType[] {});
    }

    @Override
    protected String getMeasureImageKey() {
        return "%MEASURE_IMAGE_WEIR%";
    }

    @Override
    protected String getMeasureTypeKey() {
        return "%MEASURE_TYPE_WEIR%";
    }
}
