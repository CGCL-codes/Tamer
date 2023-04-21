package pierre.reports;

import pierre.system.BrowserServiceResources;

public class ReportLayoutFormat {

    public static ReportLayoutFormat SINGLE_PAGE = new ReportLayoutFormat();

    public static ReportLayoutFormat MULTIPLE_PAGES = new ReportLayoutFormat();

    private String identifier;

    public ReportLayoutFormat() {
        identifier = "";
    }

    public ReportLayoutFormat(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
