package action;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PdfToSwfConversionAction extends Action {

    /*************************************************************************/
    private int m_pageNumber;

    /*************************************************************************/
    public PdfToSwfConversionAction(String receivedMessage) {
        int inputIndex = receivedMessage.indexOf(";", 0);
        int outputIndex = receivedMessage.indexOf(";", inputIndex + 1);
        this.m_input = receivedMessage.substring(inputIndex + 1, outputIndex);
        this.m_output = receivedMessage.substring(outputIndex + 1);
    }

    /*************************************************************************/
    public void run() {
        int returnValue = -1;
        try {
            System.out.println(">> Conversion starts");
            m_pageNumber = this.returnPdfPagesNumber(this.m_input);
            if (m_pageNumber != -1) {
                returnValue = this.convertPdfToSwfWithCommandLine("SWFTools/pdf2swf -T9 -s insertstop " + this.m_input + " -o " + this.m_output);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println(">> Conversion stops, return value = " + returnValue);
        if (returnValue != 0) this.dispatchMessageToClient("Erreur", "La conversion a �chou� !"); else {
            this.dispatchMessageToClient("info", "Conversion finie !");
        }
    }

    protected int returnPdfPagesNumber(String inputFilePath) {
        Process process = null;
        int pageNumber = -1;
        try {
            process = Runtime.getRuntime().exec("SWFTools/pdf2swf.exe -I " + inputFilePath);
            String string;
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while (!isProcessDone(process)) {
                while ((string = stdInput.readLine()) != null) {
                    if (string.toLowerCase().contains("page=")) {
                        int pageNumberStartIndex = string.toLowerCase().indexOf("page=");
                        pageNumberStartIndex = string.toLowerCase().indexOf("=", pageNumberStartIndex);
                        int pageNumberEndIndex = string.toLowerCase().indexOf(" ", pageNumberStartIndex + 1);
                        pageNumber = Integer.parseInt(string.substring(pageNumberStartIndex + 1, pageNumberEndIndex));
                    }
                }
            }
            stdInput.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return pageNumber;
    }

    protected int convertPdfToSwfWithCommandLine(String commandLine) {
        int pageNumber = -1;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(commandLine);
            String string;
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while (!isProcessDone(process)) {
                while ((string = stdInput.readLine()) != null) {
                    if (string.toLowerCase().contains("page")) {
                        int pageNumberStartIndex = string.toLowerCase().indexOf("page");
                        pageNumberStartIndex = string.toLowerCase().indexOf(" ", pageNumberStartIndex);
                        int pageNumberEndIndex = string.toLowerCase().indexOf(" ", pageNumberStartIndex + 1);
                        pageNumber = Integer.parseInt(string.substring(pageNumberStartIndex + 1, pageNumberEndIndex));
                        this.dispatchMessageToClient("ConversionPercent", String.valueOf((100 * pageNumber / this.m_pageNumber)));
                    }
                }
            }
            stdInput.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        if (process != null) return process.exitValue(); else return -1;
    }
}
