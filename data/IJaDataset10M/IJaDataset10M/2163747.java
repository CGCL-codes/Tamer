package org.jcvi.glk.elvira.report.sample.summary;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.jcvi.commonx.auth.tigr.DefaultProjectDbAuthorizer;
import org.jcvi.glk.Extent;
import org.jcvi.glk.ExtentAttributeType;
import org.jcvi.glk.ExtentType;
import org.jcvi.glk.ctm.CTMElviraGLKSessionBuilder;
import org.jcvi.glk.ctm.GLKCTMHelper;
import org.jcvi.glk.ctm.HibernateCTMHelper;
import org.jcvi.glk.ctm.Reference;
import org.jcvi.glk.elvira.ExtentTypeName;
import org.jcvi.glk.elvira.report.ReportData;
import org.jcvi.glk.elvira.report.sample.AbstractElviraSampleReporter;
import org.jcvi.glk.elvira.report.sample.TSVSampleReportWriter;
import org.jcvi.glk.elvira.report.sample.ExcelSampleReportWriter;
import org.jcvi.glk.elvira.report.sample.SampleReportWriter;
import org.jcvi.glk.elvira.report.sample.internal.InternalSampleReport;
import org.jcvi.glk.helpers.GLKHelper;
import org.jcvi.glk.helpers.HibernateGLKHelper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ElviraSampleSummaryReport extends AbstractElviraSampleReporter {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd");

    private static final Map<String, String> ATTRIBUTE_NAME_TRANSLATION_MAP;

    static {
        ATTRIBUTE_NAME_TRANSLATION_MAP = new HashMap<String, String>();
        ATTRIBUTE_NAME_TRANSLATION_MAP.put("blinded_number", "Blinded Number");
        ATTRIBUTE_NAME_TRANSLATION_MAP.put("species_code", "Organism Name");
        ATTRIBUTE_NAME_TRANSLATION_MAP.put("country", "Country");
        ATTRIBUTE_NAME_TRANSLATION_MAP.put("district", "District");
        ATTRIBUTE_NAME_TRANSLATION_MAP.put("collection_date", "Collection Date");
        ATTRIBUTE_NAME_TRANSLATION_MAP.put("host", "Host");
        ATTRIBUTE_NAME_TRANSLATION_MAP.put("subtype", "Subtype");
        ATTRIBUTE_NAME_TRANSLATION_MAP.put("passage_history", "Passage History");
        ATTRIBUTE_NAME_TRANSLATION_MAP.put("sample_provider", "Sample Provider");
        ATTRIBUTE_NAME_TRANSLATION_MAP.put("age", "Age");
        ATTRIBUTE_NAME_TRANSLATION_MAP.put("gender", "gender");
        ATTRIBUTE_NAME_TRANSLATION_MAP.put("project_id", "ProjectID");
        ATTRIBUTE_NAME_TRANSLATION_MAP.put("CEIRS_sample_id", "SampleID");
    }

    private final List<String> attributesToInclude;

    private final Map<String, ExtentAttributeType> typeByName;

    private final ExtentType lotType;

    private final ExtentType collectionType;

    private final ExtentAttributeType deprecatedType;

    public ElviraSampleSummaryReport(GLKCTMHelper helper, List<String> attributesToInclude, List<SampleReportWriter> writers) {
        super(helper, writers);
        this.attributesToInclude = attributesToInclude;
        typeByName = new HashMap<String, ExtentAttributeType>();
        GLKHelper glkHelper = helper.getGlkHelper();
        lotType = glkHelper.getExtentType(ExtentTypeName.LOT);
        collectionType = glkHelper.getExtentType(ExtentTypeName.COLLECTION);
        deprecatedType = glkHelper.getExtentAttributeType("deprecated");
        for (String attributeName : attributesToInclude) {
            ExtentAttributeType attrType = glkHelper.getExtentAttributeType(attributeName);
            if (attrType != null) {
                typeByName.put(attributeName, attrType);
            }
        }
    }

    protected boolean deprecated(Extent extent) {
        return extent.hasAttribute(deprecatedType);
    }

    @Override
    protected List<String> generateHeader() {
        List<String> header = new ArrayList<String>();
        for (String attributeName : attributesToInclude) {
            header.add(getTranslatedName(attributeName));
        }
        header.add("ExtentID");
        header.add("Ref_ID");
        header.add("Status");
        header.add("Collection");
        header.add("Lot");
        return header;
    }

    @Override
    protected List<ReportData> generateReportDataFor(Extent sample) {
        if (sample == null) {
            return null;
        }
        Extent collection = sample.getFirstParentWhoseTypeIs(collectionType);
        if (collection == null) {
            System.err.printf("%s has no collection%n", sample.getReference());
            return null;
        }
        if (!deprecated(collection)) {
            List<ReportData> reportData = new ArrayList<ReportData>();
            for (String attributeName : attributesToInclude) {
                final String data = this.generatePrintValueFor(sample, typeByName.get(attributeName));
                reportData.add(ReportData.buildReportData(data, isDate(attributeName)));
            }
            reportData.add(ReportData.buildReportData(sample.getId().toString()));
            reportData.add(ReportData.buildReportData(sample.getReference()));
            reportData.add(getCtmStatusFor(sample));
            reportData.add(ReportData.buildReportData(collection.getReference()));
            Extent lot = sample.getFirstParentWhoseTypeIs(lotType);
            if (lot != null) {
                reportData.add(ReportData.buildReportData(lot.getReference()));
            }
            return reportData;
        }
        return null;
    }

    private ReportData getCtmStatusFor(Extent sample) {
        final Reference ctmReference = getHelper().getCtmHelper().getReferenceFrom(sample);
        if (ctmReference == null) {
            return getBlankCell();
        }
        return ReportData.buildReportData(ctmReference.getStatus().getName());
    }

    /**
     * 
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        String server = args[0];
        String projects = args[1];
        String outFilePrefix = args[2];
        String datePrefix = DATE_FORMAT.print(new DateTime());
        String prefix = outFilePrefix + "." + datePrefix;
        List<String> attrs = Arrays.asList(args[3].split(","));
        for (String project : Arrays.asList(projects.split(","))) {
            System.out.println(project);
            Session session = new CTMElviraGLKSessionBuilder(new DefaultProjectDbAuthorizer.Builder(project).server(server).build()).build();
            GLKCTMHelper helper = new GLKCTMHelper(new HibernateGLKHelper(session), new HibernateCTMHelper(session));
            final ElviraSampleSummaryReport sampleReporter = new ElviraSampleSummaryReport(helper, attrs, Arrays.<SampleReportWriter>asList(new ExcelSampleReportWriter(new File(prefix + ".xls")), new TSVSampleReportWriter(new File(prefix + ".csv"), true)));
            sampleReporter.open();
            sampleReporter.nextSection(project);
            InternalSampleReport.writeInternalSampleReport(helper.getGlkHelper(), sampleReporter);
            sampleReporter.close();
        }
        Runtime.getRuntime().exec("/usr/bin/cp " + prefix + ".csv" + " " + outFilePrefix + ".csv");
        Runtime.getRuntime().exec("/usr/bin/cp " + prefix + ".xls" + " " + outFilePrefix + ".xls");
    }

    @Override
    protected Map<String, String> getAttributeTranslationMap() {
        return ATTRIBUTE_NAME_TRANSLATION_MAP;
    }
}
