package java.awt.print;

/**
 * Fake PrinterJob that just signals no print service.  This is only
 * here so applications can call
 * <code>PrintJob.getPrinterJob().getPrinterJob()</code> and check
 * that it returns <code>null</code> which indicates no actual
 * printing support is available.
 */
class NoPrinterJob extends PrinterJob {

    public int getCopies() {
        return 0;
    }

    public void setCopies(int copies) {
    }

    public String getJobName() {
        return "NoPrinterJob";
    }

    public void setJobName(String job_name) {
    }

    public String getUserName() {
        return "NoUser";
    }

    public void cancel() {
    }

    public boolean isCancelled() {
        return true;
    }

    public PageFormat defaultPage(PageFormat page_format) {
        return page_format;
    }

    public PageFormat pageDialog(PageFormat page_format) {
        return page_format;
    }

    public void print() throws PrinterException {
        throw new PrinterException("No printer");
    }

    public boolean printDialog() {
        return false;
    }

    public void setPageable(Pageable pageable) {
    }

    public void setPrintable(Printable printable) {
    }

    public void setPrintable(Printable printable, PageFormat page_format) {
    }

    public PageFormat validatePage(PageFormat page_format) {
        return page_format;
    }
}
