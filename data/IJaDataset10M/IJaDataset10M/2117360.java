package org.in4ama.documentengine.print;

public class AcrobatPrinter extends Printer {

    private String cmd = "\"{apppath}\" /t \"{pdffile}\" \"{printer}\"";

    @Override
    public void printPDFs(PDFPrintJob[] printJobs, String printServiceName) throws Exception {
        CommandPrinter cmdPrinter = new CommandPrinter(cmd);
        cmdPrinter.setPrintServiceName(printServiceName, this);
        String acroPath = projConfigurationManager.getProperty("print", "pdfapppath");
        cmdPrinter.setPDFAppPath(acroPath);
        cmdPrinter.printPDFs(printJobs);
    }
}
