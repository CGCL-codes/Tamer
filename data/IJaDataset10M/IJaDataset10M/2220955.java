package no.ugland.utransprod.util.excel;

public class ExcelReportSettingSales extends ExcelReportSetting {

    public static final String PROPERTY_SALESMAN = "salesman";

    private Boolean isForSalesman;

    public ExcelReportSettingSales(ExcelReportEnum excelReportEnum) {
        super(excelReportEnum);
    }

    public Boolean getSalesman() {
        return isForSalesman;
    }

    public void setSalesman(Boolean salesman) {
        isForSalesman = salesman;
        excelReportEnum = isForSalesman ? ExcelReportEnum.SALES_REPORT_SALESMAN : ExcelReportEnum.SALES_REPORT;
    }
}
