package edu.pw.treegrid.server.web.converters;

import edu.pw.treegrid.server.reportmodel.Report;
import edu.pw.treegrid.shared.ReportColumnDescription;
import xalan.smartgwt.record.BeanToRecordConverter;
import xalan.smartgwt.record.Record;

public class ReportConverter implements BeanToRecordConverter<Report> {

    @Override
    public void convert(Record record, Report r) {
        record.addParam(ReportColumnDescription.ID, r.getId());
        record.addParam(ReportColumnDescription.NAME, r.getName());
        record.addParam(ReportColumnDescription.DESCRIPTION, r.getDescription());
        record.addParam(ReportColumnDescription.COUNT, r.isCountVisible());
    }
}
