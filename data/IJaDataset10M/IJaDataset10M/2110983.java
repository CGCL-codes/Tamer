package pk.edu.lums.phpcodesniffernb;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Wrapper class to run PHP Code Sniffer
 * @author Midhat
 */
public class CodeSnifferWrapper {

    private String phpDirPath;

    private String codeStandard;

    private String srcDir;

    private String outPath;

    public Process doCodeSniffer() throws IOException {
        final String xslDescriptor = "<?xml-stylesheet type=\"text/xsl\" href=\"Style-Report.xsl\"?>";
        String phpcsPath = phpDirPath + File.separator + "phpcs.bat";
        String[] cmd = { phpcsPath, "--report=\"checkstyle\"", "--standard=\"" + codeStandard + "\"", srcDir };
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(cmd);
        Process p = pb.start();
        PrintWriter writer = new PrintWriter(outPath);
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String str = br.readLine();
        System.out.println(str);
        if (!str.contains("<?xml")) {
            writer.flush();
            writer.close();
            new File(outPath).delete();
            throw new IOException();
        }
        writer.println(xslDescriptor);
        while (str != null) {
            writer.println(str);
            str = br.readLine();
        }
        writer.flush();
        writer.close();
        if (new File(outPath).length() == xslDescriptor.length()) {
            new File(outPath).delete();
            throw new IOException();
        }
        return p;
    }

    public String getPhpDirPath() {
        return phpDirPath;
    }

    public void setPhpDirPath(String phpDirPath) {
        this.phpDirPath = phpDirPath;
    }

    public String getCodeStandard() {
        return codeStandard;
    }

    public void setCodeStandard(String codeStandard) {
        this.codeStandard = codeStandard;
    }

    public String getSrcDir() {
        return srcDir;
    }

    public void setSrcDir(String srcDir) {
        this.srcDir = srcDir;
    }

    public String getOutPath() {
        return outPath;
    }

    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }
}
