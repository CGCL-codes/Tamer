package org.slasoi.infrastructure.monitoring.reporting;

import com.lowagie.text.*;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYBarRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;
import org.slasoi.infrastructure.monitoring.InfrastructureMonitoringAgent;
import org.slasoi.infrastructure.monitoring.jpa.entities.*;
import org.slasoi.infrastructure.monitoring.jpa.enums.MetricTypeEnum;
import org.slasoi.infrastructure.monitoring.jpa.enums.ViolationSeverity;
import org.slasoi.infrastructure.monitoring.jpa.enums.ViolationType;
import org.slasoi.infrastructure.monitoring.jpa.managers.MetricManager;
import org.slasoi.infrastructure.monitoring.jpa.managers.MetricValueHistoryManager;
import org.slasoi.infrastructure.monitoring.jpa.managers.ViolationManager;
import org.slasoi.infrastructure.monitoring.pubsub.handlers.MonitoringDataRequestHandler;
import org.slasoi.infrastructure.monitoring.utils.DateUtils;
import java.awt.*;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class ServiceSummaryReport extends ReportTemplate {

    Service service;

    Date fromDate;

    Date toDate;

    Document document;

    public ServiceSummaryReport(Service service) {
        this.service = service;
    }

    public ServiceSummaryReport(Service service, Date fromDate, Date toDate) {
        this.service = service;
        if (fromDate == null) {
            fromDate = service.getStartDate();
        } else if (fromDate.getTime() < service.getStartDate().getTime()) {
            fromDate = service.getStartDate();
        }
        if (toDate == null) {
            toDate = InfrastructureMonitoringAgent.getInstance().getMonitoringEngine().getMetricsCollectedDate();
        } else {
            Calendar toDateCalendar = Calendar.getInstance();
            toDateCalendar.setTime(toDate);
            Calendar nowCalendar = Calendar.getInstance();
            nowCalendar.setTime(new Date());
            if (toDateCalendar.get(Calendar.YEAR) == nowCalendar.get(Calendar.YEAR) && toDateCalendar.get(Calendar.DAY_OF_YEAR) == nowCalendar.get(Calendar.DAY_OF_YEAR)) {
                toDate = InfrastructureMonitoringAgent.getInstance().getMonitoringEngine().getMetricsCollectedDate();
            } else {
                toDateCalendar.set(Calendar.HOUR_OF_DAY, 23);
                toDateCalendar.set(Calendar.MINUTE, 59);
                toDateCalendar.set(Calendar.SECOND, 59);
                toDate = toDateCalendar.getTime();
            }
        }
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    public ByteArrayOutputStream generate() throws Exception {
        document = new Document();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, os);
        pdfWriter.setStrictImageSequence(true);
        document.open();
        addTitlePage();
        addSummaryPage();
        addQoSTermsHistoryPage();
        addResourcesUsagePage();
        addViolationsChapter();
        document.close();
        return os;
    }

    private void addViolationsChapter() throws Exception {
        Chapter chapter = new ChapterAutoNumber(new Paragraph("Violations", chapterFont));
        Section section1 = chapter.addSection(new Paragraph("Service SLA Compliance History", sectionFont));
        Metric metric = MetricManager.getInstance().findServiceMetric(MetricTypeEnum.SERVICE_SLA_COMPLIANCE, service);
        PdfPTable slaComplianceTable = createMetricHistoryTable(metric, null);
        if (slaComplianceTable == null) {
            section1.add(new Paragraph("No SLA violations or warnings occurred.", normalFont));
        } else {
            section1.add(slaComplianceTable);
        }
        Section section2 = chapter.addSection(new Paragraph("Service Violations and Warnings Count Diagram", sectionFont));
        byte[][] charts = createViolationsHistoryCharts();
        Image violationsImage = Image.getInstance(charts[0]);
        section2.add(violationsImage);
        Image warningsImage = Image.getInstance(charts[1]);
        section2.add(warningsImage);
        Section section3 = chapter.addSection(new Paragraph("Service Violations and Warnings History", sectionFont));
        Paragraph legend = new Paragraph("", normalFont);
        legend.add(new Chunk("Legend", normalBoldFont));
        legend.setSpacingBefore(10);
        legend.add(Chunk.NEWLINE);
        legend.add("Type:");
        legend.add(Chunk.NEWLINE);
        legend.add("A - Acceptable violation (happens outside business hours as defined by 'Service Availability Restrictions' term)");
        legend.add(Chunk.NEWLINE);
        legend.add("P - Punishable violation (happens during business hours)");
        legend.add(Chunk.NEWLINE);
        legend.add("Source:");
        legend.add(Chunk.NEWLINE);
        HashMap<String, String> vmAliases = createVmAliasMap();
        TreeMap<String, String> vmAliasesTree = new TreeMap<String, String>();
        for (String vmName : vmAliases.keySet()) {
            vmAliasesTree.put(vmAliases.get(vmName), vmName);
        }
        for (Map.Entry<String, String> entry : vmAliasesTree.entrySet()) {
            legend.add(String.format("%s - %s", entry.getKey(), entry.getValue()));
            legend.add(Chunk.NEWLINE);
        }
        section3.add(legend);
        Section subsection1 = section3.addSection(new Paragraph("Service Violations", subsectionFont));
        PdfPTable violationsHistoryTable = addViolationsHistoryTable(ViolationType.VIOLATION, vmAliases);
        subsection1.add(violationsHistoryTable);
        Section subsection2 = section3.addSection(new Paragraph("Service Warnings", subsectionFont));
        PdfPTable warningHistoryTable = addViolationsHistoryTable(ViolationType.WARNING, vmAliases);
        subsection2.add(warningHistoryTable);
        document.add(chapter);
    }

    private void addTitlePage() throws DocumentException {
        Paragraph p1 = new Paragraph();
        p1.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(p1, 15);
        String subtitle = String.format("Infrastructure Service '%s'", service.getServiceName());
        p1.add(new Paragraph(subtitle, titlePageSubtitleFont));
        addEmptyLine(p1, 2);
        String title = "Service Summary Report";
        p1.add(new Paragraph(title, titlePageTitleFont));
        document.add(p1);
        Paragraph p2 = new Paragraph();
        p2.setAlignment(Element.ALIGN_LEFT);
        p2.setIndentationLeft(100);
        addEmptyLine(p2, 10);
        p2.add(new Paragraph("Generated on: " + new Date()));
        if (fromDate != null && toDate != null) {
            addEmptyLine(p2, 1);
            p2.add(new Paragraph("Covers the time period:"));
            p2.add(new Paragraph("from: " + fromDate));
            p2.add(new Paragraph("to: " + toDate));
        }
        document.add(p2);
        document.newPage();
        document.addTitle(title);
        document.addAuthor(InfrastructureMonitoringAgent.APPLICATION_NAME);
    }

    private void addSummaryPage() throws Exception {
        Chapter chapter = new ChapterAutoNumber(new Paragraph("Summary", chapterFont));
        PdfPTable summaryTable = createSummaryTable();
        chapter.add(summaryTable);
        Section section2 = chapter.addSection(new Paragraph("Service SLA", sectionFont));
        Section subsection = section2.addSection(new Paragraph("Service QoS Terms", subsectionFont));
        PdfPTable srvQosTermsTable = createSlaQoSTermsTable(service.getMetricList());
        subsection.add(srvQosTermsTable);
        for (Vm vm : service.getVmList()) {
            String title = String.format("VM '%s' QoS Terms", vm.getName());
            subsection = section2.addSection(new Paragraph(title, subsectionFont));
            PdfPTable vmQosTermsTable = createSlaQoSTermsTable(vm.getMetricList());
            subsection.add(vmQosTermsTable);
        }
        document.add(chapter);
    }

    private void addQoSTermsHistoryPage() throws Exception {
        Chapter chapter = new ChapterAutoNumber(new Paragraph("QoS Terms History", chapterFont));
        Metric metric;
        byte[] imageBytes;
        Image image;
        PdfPTable pdfTable;
        Section section = chapter.addSection(new Paragraph("Service Availability", sectionFont));
        Section subsection = section.addSection(new Paragraph("Service Availability Chart per Reporting Periods", subsectionFont));
        metric = MetricManager.getInstance().findServiceMetric(MetricTypeEnum.SERVICE_AVAILABILITY, service);
        imageBytes = createServiceAvailabilityChart(metric);
        image = Image.getInstance(imageBytes);
        subsection.add(image);
        subsection = section.addSection(new Paragraph("Availability Status History of the Service", subsectionFont));
        metric = MetricManager.getInstance().findServiceMetric(MetricTypeEnum.SERVICE_AVAILABILITY_STATUS, service);
        HashMap<String, String> valueMapping = new HashMap<String, String>();
        valueMapping.put("true", "up");
        valueMapping.put("false", "down");
        pdfTable = createMetricHistoryTable(metric, valueMapping);
        subsection.add(pdfTable);
        for (Vm vm : service.getVmList()) {
            subsection = section.addSection(new Paragraph("Availability Status History of the VM " + vm.getName(), subsectionFont));
            metric = MetricManager.getInstance().findVmMetric(MetricTypeEnum.VM_AVAILABILITY_STATUS, vm);
            pdfTable = createMetricHistoryTable(metric, valueMapping);
            subsection.add(pdfTable);
        }
        section = chapter.addSection(new Paragraph("Service Mean Time To Failure (MTTF)", sectionFont));
        metric = service.getMetric(MetricTypeEnum.SERVICE_MTTF);
        addMttxChart(metric, section);
        section = chapter.addSection(new Paragraph("Service Mean Time To Violation (MTTV)", sectionFont));
        metric = service.getMetric(MetricTypeEnum.SERVICE_MTTV);
        addMttxChart(metric, section);
        section = chapter.addSection(new Paragraph("Service Mean Time To Recovery (MTTR)", sectionFont));
        metric = service.getMetric(MetricTypeEnum.SERVICE_MTTR);
        addMttxChart(metric, section);
        document.add(chapter);
    }

    private void addResourcesUsagePage() throws Exception {
        Chapter chapter = new ChapterAutoNumber(new Paragraph("Resources Usage", chapterFont));
        byte[] imageBytes;
        Image image;
        imageBytes = createCpuUsageChart();
        image = Image.getInstance(imageBytes);
        chapter.add(image);
        imageBytes = createMemoryUsageChart();
        image = Image.getInstance(imageBytes);
        chapter.add(image);
        document.add(chapter);
    }

    private PdfPTable createSummaryTable() {
        PdfPTable table = new PdfPTable(2);
        table.setSpacingBefore(20);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(new Paragraph("Service name", tableFont));
        table.addCell(new Paragraph(service.getServiceName(), tableFont));
        table.addCell(new Paragraph("Creation date", tableFont));
        table.addCell(new Paragraph(DateUtils.formatForDisplay(service.getStartDate()), tableFont));
        table.addCell(new Paragraph("Number of VM", tableFont));
        table.addCell(new Paragraph(Integer.toString(service.getVmList().size()), tableFont));
        table.addCell(new Paragraph("Total running time", tableFont));
        long runningTime = new Date().getTime() - service.getStartDate().getTime();
        table.addCell(new Paragraph(DateUtils.getHumanFriendlyTimeInterval(runningTime), tableFont));
        HashMap<String, String> hashMap = MonitoringDataRequestHandler.computeUpDowntime(service);
        table.addCell(new Paragraph("Total downtime", tableFont));
        table.addCell(new Paragraph(hashMap.get("DOWNTIME"), tableFont));
        table.addCell(new Paragraph("Total monitored QoS terms", tableFont));
        int qosTermsCount = getTotalMonitoredQoSTerms();
        table.addCell(new Paragraph(Integer.toString(qosTermsCount), tableFont));
        table.addCell(new Paragraph("Total number of QoS terms violations", tableFont));
        int violationsCount = ViolationManager.getInstance().getServiceViolations(service, ViolationType.VIOLATION, ViolationSeverity.PUNISHABLE).size();
        table.addCell(new Paragraph(Integer.toString(violationsCount), tableFont));
        table.addCell(new Paragraph("Total time service SLA was violated", tableFont));
        long inViolationTime = ViolationManager.getInstance().getServiceTotalInViolationTime(service);
        table.addCell(new Paragraph(DateUtils.getHumanFriendlyTimeInterval(inViolationTime), tableFont));
        return table;
    }

    private PdfPTable createMetricHistoryTable(Metric metric, HashMap<String, String> valueMapping) throws Exception {
        PdfPTable table = new PdfPTable(4);
        table.setSpacingBefore(20);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        String[] headers = { "Num", "Start Time", "End Time", "Value" };
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Paragraph(header, tableFont));
            cell.setBackgroundColor(tableHeaderColor);
            table.addCell(cell);
        }
        float[] columnWidths = { 0.25f, 1f, 1f, 1f };
        table.setWidths(columnWidths);
        table.setHeaderRows(1);
        List<MetricValueHistory> history = MetricValueHistoryManager.getInstance().getMvhValues(metric, fromDate, toDate, true);
        int num = 1;
        for (MetricValueHistory mvh : history) {
            table.addCell(new Phrase(Integer.toString(num), tableFont));
            String startTime = DateUtils.formatForDisplay(mvh.getStartTime());
            table.addCell(new Phrase(startTime, tableFont));
            String endTime = DateUtils.formatForDisplay(mvh.getEndTime());
            table.addCell(new Phrase(endTime, tableFont));
            String value;
            if (mvh.getValue() != null) {
                if (valueMapping != null && valueMapping.containsKey(mvh.getValue())) {
                    value = valueMapping.get(mvh.getValue());
                } else {
                    value = mvh.getValue();
                }
            } else {
                value = "No data available.";
            }
            table.addCell(new Phrase(value, tableFont));
            num++;
        }
        if (num == 0) {
            return null;
        } else {
            return table;
        }
    }

    private byte[][] createViolationsHistoryCharts() throws Exception {
        byte[][] charts = new byte[2][];
        HashMap<Date, ViolationManager.ViolationsCount> history = ViolationManager.getInstance().getServiceViolationsFrequency(service, fromDate, toDate);
        List<Date> periods = new ArrayList<Date>();
        for (Date date : history.keySet()) {
            periods.add(date);
        }
        Collections.sort(periods);
        TimeTableXYDataset violationsDataset = new TimeTableXYDataset();
        TimeTableXYDataset warningsDataset = new TimeTableXYDataset();
        for (Date periodStart : periods) {
            ViolationManager.ViolationsCount historyViolCount = history.get(periodStart);
            String periodTitle = service.getReportingPeriod().getPeriodTitle(periodStart);
            int punishable = historyViolCount.getCount(ViolationType.VIOLATION, ViolationSeverity.PUNISHABLE);
            int acceptable = historyViolCount.getCount(ViolationType.VIOLATION, ViolationSeverity.ACCEPTABLE);
            violationsDataset.add(new Day(periodStart), punishable, "Punishable");
            violationsDataset.add(new Day(periodStart), acceptable, "Acceptable");
            punishable = historyViolCount.getCount(ViolationType.WARNING, ViolationSeverity.PUNISHABLE);
            acceptable = historyViolCount.getCount(ViolationType.WARNING, ViolationSeverity.ACCEPTABLE);
            warningsDataset.add(new Day(periodStart), punishable, "Punishable");
            warningsDataset.add(new Day(periodStart), acceptable, "Acceptable");
        }
        charts[0] = createViolationsCountChart(violationsDataset, "Service Violations History", "Violations Count", Color.RED, new Color(66, 126, 192));
        charts[1] = createViolationsCountChart(warningsDataset, "Service Warnings History", "Warnings Count", Color.YELLOW, new Color(66, 126, 192));
        return charts;
    }

    private byte[] createViolationsCountChart(TimeTableXYDataset dataset, String title, String yLabel, Paint series1Color, Paint series2Color) throws IOException {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(title, "Time", yLabel, dataset, true, false, false);
        chart.setBackgroundPaint(Color.white);
        XYPlot xyPlot = (XYPlot) chart.getPlot();
        xyPlot.setBackgroundPaint(chartBackgroundPaint);
        xyPlot.setDomainGridlinePaint(Color.gray);
        xyPlot.setRangeGridlinePaint(Color.gray);
        StackedXYBarRenderer barRenderer = new StackedXYBarRenderer(getBarRendererMargin(dataset.getItemCount(0)));
        xyPlot.setRenderer(0, barRenderer);
        xyPlot.setRenderer(1, barRenderer);
        barRenderer.setSeriesPaint(0, series1Color);
        barRenderer.setSeriesPaint(1, series2Color);
        NumberAxis rangeAxis = (NumberAxis) xyPlot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ChartUtilities.writeChartAsPNG(baos, chart, chartWidth, chartHeight);
        return baos.toByteArray();
    }

    private PdfPTable addViolationsHistoryTable(ViolationType violationType, HashMap<String, String> vmAliases) throws Exception {
        PdfPTable table = new PdfPTable(9);
        String[] headers = { "#", "QoS Term", "Source", "Actual Value", (violationType == ViolationType.VIOLATION) ? "Violation Condition" : "Warning Condition", "Start Time", "End Time", "Type", (violationType == ViolationType.VIOLATION) ? "Violation ID" : "Warning ID" };
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Paragraph(header, tableFont));
            cell.setBackgroundColor(tableHeaderColor);
            table.addCell(cell);
        }
        float[] columnWidths = { 0.25f, 1f, 0.5f, 1f, 1f, 0.75f, 0.75f, 0.4f, 0.5f };
        table.setWidths(columnWidths);
        List<Violation> violations = ViolationManager.getInstance().getServiceViolations(service);
        if (violations != null) {
            int i = 1;
            for (Violation violation : violations) {
                if (violation.getType() != violationType) {
                    continue;
                }
                table.addCell(new Phrase(Integer.toString(i), tableFont));
                Metric metric = violation.getMetric();
                String qosTerm = metric.getMetricType().getTitle();
                table.addCell(new Phrase(qosTerm, tableFont));
                String source;
                if (metric.getService() != null) {
                    source = "Service";
                } else {
                    source = vmAliases.get(metric.getVm().getName());
                }
                table.addCell(new Phrase(source, tableFont));
                String actualValue = metric.displayValue(violation.getMetricValue());
                table.addCell(new Phrase(actualValue, tableFont));
                String violationCond = (violationType == ViolationType.VIOLATION) ? metric.getViolationCondition() : metric.getWarningCondition();
                table.addCell(new Phrase(violationCond, tableFont));
                String startTime = DateUtils.formatForDisplay(violation.getStartTime());
                table.addCell(new Phrase(startTime, tableFont));
                String endTime = (violation.getEndTime() != null) ? DateUtils.formatForDisplay(violation.getEndTime()) : "Remaining";
                table.addCell(new Phrase(endTime, tableFont));
                String type = (violation.getSeverity() == ViolationSeverity.PUNISHABLE) ? "P" : "A";
                table.addCell(new Phrase(type, tableFont));
                String violationId = violation.getViolationId().toString();
                table.addCell(new Phrase(violationId, tableFont));
                i++;
            }
        } else {
            return null;
        }
        table.setHeaderRows(1);
        table.setSpacingBefore(20);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setWidthPercentage(100);
        return table;
    }

    private void addMttxChart(Metric metric, Section section) throws Exception {
        String metricTypeTitle = metric.getMetricType().getTitle();
        String unit = metric.getMetricType().getMetricUnit();
        double threshold = Double.parseDouble(metric.getViolationThreshold());
        boolean addNote = false;
        List<MetricValueHistory> history = MetricValueHistoryManager.getInstance().getMvhValues(metric, fromDate, toDate, false);
        TimeSeries timeSeries = new TimeSeries(metricTypeTitle);
        for (MetricValueHistory mvh : history) {
            if (mvh.getValue() == null) {
                continue;
            }
            double value = Double.parseDouble(mvh.getValue());
            if (Double.isInfinite(value) || Double.isNaN(value)) {
                value = Double.MIN_VALUE;
                addNote = true;
            }
            timeSeries.add(new Day(mvh.getStartTime()), value);
        }
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(timeSeries);
        String yLabel = String.format("%s [%s]", metric.getMetricType().getName(), unit);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(metricTypeTitle, "Time", yLabel, dataset, false, false, false);
        chart.setBackgroundPaint(Color.white);
        XYPlot xyPlot = (XYPlot) chart.getPlot();
        xyPlot.setBackgroundPaint(chartBackgroundPaint);
        xyPlot.setDomainGridlinePaint(Color.gray);
        xyPlot.setRangeGridlinePaint(Color.gray);
        XYBarRenderer barRenderer = new XYBarRenderer(getBarRendererMargin(timeSeries.getItemCount()));
        xyPlot.setRenderer(0, barRenderer);
        barRenderer.setSeriesPaint(0, chartBarsColor);
        XYItemLabelGenerator labelGenerator = new XYItemLabelGenerator() {

            @Override
            public String generateLabel(XYDataset xyDataset, int series, int item) {
                double value = xyDataset.getYValue(series, item);
                if (value == Double.MIN_VALUE) {
                    return "*";
                } else {
                    return "";
                }
            }
        };
        barRenderer.setBaseItemLabelGenerator(labelGenerator);
        barRenderer.setBaseItemLabelsVisible(true);
        barRenderer.setBaseItemLabelPaint(Color.BLACK);
        barRenderer.setBaseItemLabelFont(new Font(null, Font.PLAIN, 16));
        xyPlot.setRangeCrosshairValue(threshold);
        xyPlot.setRangeCrosshairVisible(true);
        xyPlot.setRangeCrosshairPaint(Color.RED);
        xyPlot.setRangeCrosshairStroke(new BasicStroke(2));
        ValueAxis valueAxis = xyPlot.getRangeAxis();
        if (valueAxis.getUpperBound() < threshold) {
            double span = threshold - valueAxis.getLowerBound();
            double upperBound = threshold + span * 0.05;
            valueAxis.setUpperBound(upperBound);
            double minLowerBound = valueAxis.getLowerBound() - span * 0.1;
            if (minLowerBound < 0) {
                minLowerBound = 0;
            }
            if (valueAxis.getLowerBound() > minLowerBound) {
                valueAxis.setLowerBound(minLowerBound);
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ChartUtilities.writeChartAsPNG(baos, chart, chartWidth, chartHeight);
        Image image = Image.getInstance(baos.toByteArray());
        section.add(image);
        if (addNote) {
            String note = null;
            switch(MetricTypeEnum.convert(metric.getMetricType())) {
                case SERVICE_MTTF:
                    note = "* No service failure occurred in that period.";
                    break;
                case SERVICE_MTTV:
                    note = "* No SLA violation occurred in that period.";
                    break;
                case SERVICE_MTTR:
                    note = "* No service failure & recovery occurred in that period.";
                    break;
            }
            Paragraph noteP = new Paragraph(note, normalFont);
            section.add(noteP);
        }
    }

    private byte[] createServiceAvailabilityChart(Metric metric) throws Exception {
        String metricTypeTitle = metric.getMetricType().getTitle();
        String unit = metric.getMetricType().getMetricUnit();
        List<MetricValueHistory> history = MetricValueHistoryManager.getInstance().getMvhValues(metric, fromDate, toDate, false);
        TimeSeries timeSeries = new TimeSeries("Service");
        int previousMvhDay = -1;
        double availability = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        HashMap<String, String> availMap = new HashMap<String, String>();
        for (int i = 0; i < history.size(); i++) {
            MetricValueHistory mvh = history.get(i);
            if (mvh.getValue() == null) {
                continue;
            }
            availMap.put(sdf.format(mvh.getStartTime()), mvh.getValue());
        }
        String[] periods = availMap.keySet().toArray(new String[availMap.size()]);
        double minAvailValue = Double.MAX_VALUE;
        Arrays.sort(periods);
        for (String period : periods) {
            Date date = sdf.parse(period);
            double value = Double.parseDouble(availMap.get(period));
            if (value < minAvailValue) {
                minAvailValue = value;
            }
            timeSeries.add(new Day(date), value);
        }
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(timeSeries);
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Service Availability", "Time", "Availability [%]", dataset, false, false, false);
        chart.setBackgroundPaint(Color.white);
        XYPlot xyPlot = (XYPlot) chart.getPlot();
        xyPlot.setBackgroundPaint(chartBackgroundPaint);
        xyPlot.setDomainGridlinePaint(Color.gray);
        xyPlot.setRangeGridlinePaint(Color.gray);
        XYBarRenderer barRenderer = new XYBarRenderer(getBarRendererMargin(timeSeries.getItemCount()));
        xyPlot.setRenderer(0, barRenderer);
        barRenderer.setSeriesPaint(0, new Color(17, 96, 178));
        double threshold = Double.parseDouble(metric.getViolationThreshold());
        xyPlot.setRangeCrosshairValue(threshold);
        xyPlot.setRangeCrosshairVisible(true);
        xyPlot.setRangeCrosshairPaint(Color.RED);
        xyPlot.setRangeCrosshairStroke(new BasicStroke(2));
        ValueAxis valueAxis = xyPlot.getRangeAxis();
        valueAxis.setUpperBound(100);
        if (minAvailValue < threshold) {
            double lowerBound = ((int) (minAvailValue - 5) / 10) * 10;
            if (lowerBound < 0) {
                lowerBound = 0;
            }
            valueAxis.setLowerBound(lowerBound);
        } else {
            double lowerBound = threshold - (100 - threshold) * 0.1;
            valueAxis.setLowerBound(lowerBound);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ChartUtilities.writeChartAsPNG(baos, chart, chartWidth, chartHeight);
        return baos.toByteArray();
    }

    private byte[] createCpuUsageChart() throws IOException {
        if (service.getVmList() == null || service.getVmList().size() == 0) {
            return null;
        }
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        for (Vm vm : service.getVmList()) {
            TimeSeries s = new TimeSeries("VM " + vm.getName());
            Metric metric = MetricManager.getInstance().findVmMetric(MetricTypeEnum.VM_CPU_SPEED_USED, vm);
            List<MetricValue> history = MetricValueHistoryManager.getInstance().getMetricHistory(metric, fromDate, toDate, null);
            for (MetricValue mvh : history) {
                if (mvh.getValue() != null) {
                    double value = Double.parseDouble(mvh.getValue());
                    s.add(new Minute(mvh.getTime()), value);
                }
            }
            dataset.addSeries(s);
        }
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Host CPU Usage", "Time", "CPU Speed [GHz]", dataset, true, false, false);
        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(chartBackgroundPaint);
        plot.setDomainGridlinePaint(Color.gray);
        plot.setRangeGridlinePaint(Color.gray);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ChartUtilities.writeChartAsPNG(baos, chart, chartWidth, chartHeight);
        return baos.toByteArray();
    }

    private byte[] createMemoryUsageChart() throws IOException {
        if (service.getVmList() == null || service.getVmList().size() == 0) {
            return null;
        }
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        for (Vm vm : service.getVmList()) {
            TimeSeries s = new TimeSeries("VM " + vm.getName());
            Metric metric = MetricManager.getInstance().findVmMetric(MetricTypeEnum.VM_MEMORY_SIZE_USED, vm);
            List<MetricValue> history = MetricValueHistoryManager.getInstance().getMetricHistory(metric, fromDate, toDate, null);
            for (MetricValue mvh : history) {
                if (mvh.getValue() != null) {
                    double value = Double.parseDouble(mvh.getValue());
                    s.add(new Minute(mvh.getTime()), value);
                }
            }
            dataset.addSeries(s);
        }
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Host Memory Usage", "Time", "Memory [MB]", dataset, true, false, false);
        chart.setBackgroundPaint(Color.white);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(chartBackgroundPaint);
        plot.setDomainGridlinePaint(Color.gray);
        plot.setRangeGridlinePaint(Color.gray);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ChartUtilities.writeChartAsPNG(baos, chart, chartWidth, chartHeight);
        return baos.toByteArray();
    }

    private PdfPTable createSlaQoSTermsTable(List<Metric> metrics) throws Exception {
        PdfPTable table = new PdfPTable(4);
        table.setSpacingBefore(20);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        String[] headers = { "#", "QoS Term", "Violation Condition", "Warning Condition" };
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Paragraph(header, tableFont));
            cell.setBackgroundColor(tableHeaderColor);
            table.addCell(cell);
        }
        float[] columnWidths = { 0.25f, 1f, 1f, 1f };
        table.setWidths(columnWidths);
        table.setHeaderRows(1);
        int i = 1;
        for (Metric metric : metrics) {
            MetricType metricType = metric.getMetricType();
            if (!(metricType.getType() == MetricType.Type.QOS_TERM || metricType.getType() == MetricType.Type.CONFIG)) {
                continue;
            }
            table.addCell(new Phrase(Integer.toString(i), tableFont));
            String qosTerm = metricType.getTitle();
            table.addCell(new Phrase(qosTerm, tableFont));
            String violationCond = null;
            if (metricType.getType() == MetricType.Type.QOS_TERM) {
                violationCond = metric.getViolationCondition();
            } else if (metricType.getType() == MetricType.Type.CONFIG) {
                violationCond = metric.getConfigValue() + "\n(not monitorable)";
            }
            table.addCell(new Phrase(violationCond, tableFont));
            String warningCond = metric.getWarningCondition();
            table.addCell(new Phrase(warningCond, tableFont));
            i++;
        }
        return table;
    }

    private int getTotalMonitoredQoSTerms() {
        int num = 0;
        for (Metric metric : service.getMetricList()) {
            if (metric.getMetricType().getType() == MetricType.Type.QOS_TERM) {
                num++;
            }
        }
        for (Vm vm : service.getVmList()) {
            for (Metric metric : vm.getMetricList()) {
                if (metric.getMetricType().getType() == MetricType.Type.QOS_TERM) {
                    num++;
                }
            }
        }
        return num;
    }

    private HashMap<String, String> createVmAliasMap() {
        HashMap<String, String> vmAliases = new HashMap<String, String>();
        for (int i = 1; i <= service.getVmList().size(); i++) {
            Vm vm = service.getVmList().get(i - 1);
            vmAliases.put(vm.getName(), "VM" + i);
        }
        return vmAliases;
    }

    private double getBarRendererMargin(int numberOfBars) {
        if (numberOfBars < 2) {
            return 0.6;
        } else {
            return 0.2;
        }
    }
}
