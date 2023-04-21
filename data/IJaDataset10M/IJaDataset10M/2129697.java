package jp.ac.jaist.ceqea.io;

import java.io.*;
import java.util.*;
import jp.ac.jaist.ceqea.CEq_enums.*;
import jp.ac.jaist.ceqea.*;
import jp.ac.jaist.ceqea.D_inSitu.*;
import jp.ac.jaist.ceqea.G_systems.*;

public class CEq_cliResults {

    public static void output(CEq_1_perSpcfctn perS) {
        assert perS.spcfctn.env().outformats() != null;
        if (perS.spcfctn.env().outformats().isEmpty()) return;
        for (CEq_2_perDiagram perD : perS.dgrmProcesses()) {
            CEq_cliResults.writeDgrmResult(perD.dgrm());
            for (CEq_systDgrm sD : perD.systDgrms) CEq_cliResults.writeDgrmResult(sD);
        }
        if (perS.uniformCasc != null) CEq_cliResults.writeDgrmResult(perS.uniformCasc);
        if (perS.uniformSspc != null) CEq_cliResults.writeDgrmResult(perS.uniformSspc);
    }

    public static void writeDgrmResult(CEq_diagram dgrm) {
        CEq_cliResults.writeDgrmResult(dgrm, dgrm.process.spcfctn.env().outformats());
    }

    private static HashSet<String> onlyDot() {
        HashSet<String> outs = new HashSet();
        outs.add("dot");
        return outs;
    }

    private static HashSet<String> onlyDot = onlyDot();

    public static void writeDgrmResult(CEq_diagram dgrm, HashSet<String> outformats) {
        if (dgrm.dot == null) return;
        boolean dotoutd = false;
        String pathName = dgrm.process.spcfctn.pathname();
        if (CEq_system.ceqea_out != null) {
            final File out_dir = new File(CEq_system.ceqea_out);
            if (!out_dir.exists()) out_dir.mkdir();
            if (!out_dir.exists() || !out_dir.canWrite()) CEq_feedback.exit(CEq_feedback.ERR.File_or_generic_error, "\"" + CEq_system.ceqea_out + "\" cannot be used as out dir.");
            try {
                pathName = out_dir.getCanonicalPath() + File.separator;
            } catch (Exception e) {
                CEq_feedback.exit(CEq_feedback.ERR.File_or_generic_error, e);
            }
        }
        String fullName = pathName + dgrm.fnBase;
        for (String format : outformats) {
            File outFile = CEq_cliResults.freshFile(fullName, format);
            if (format.equals("dot")) {
                if (dotoutd) {
                    CEq_feedback.warning(dgrm.fnBase + "N has already been saved as .dot!");
                    continue;
                }
                try {
                    outFile.createNewFile();
                    Files.setContents(outFile, dgrm.dot);
                } catch (Exception e) {
                    CEq_feedback.exit(CEq_feedback.ERR.File_or_generic_error, e);
                }
                dotoutd = true;
            } else {
                try {
                    String[] cmd = { CEq_system.graphviz_dot, "-T" + format, "-o" + outFile.getAbsolutePath() };
                    Process dot = Runtime.getRuntime().exec(cmd);
                    OutputStream dot_stdin = dot.getOutputStream();
                    InputStream dot_stderr = dot.getErrorStream();
                    dot_stdin.write(dgrm.dot.getBytes());
                    dot_stdin.close();
                    if (dot_stderr.read() != -1) {
                        if (!dotoutd) {
                            CEq_feedback.warning(dgrm.fnBase + "N might not have been saved as ." + format + "; trying .dot ...");
                            CEq_cliResults.writeDgrmResult(dgrm, onlyDot);
                            dotoutd = true;
                        } else CEq_feedback.warning(dgrm.fnBase + "N might not have been saved as ." + format + "; not to worry, though: it has already been saved as .dot!");
                    }
                } catch (Exception e) {
                    CEq_feedback.exit(CEq_feedback.ERR.File_or_generic_error, e);
                }
            }
        }
    }

    private static File freshFile(String fullName, String type) {
        String freshName = null;
        File freshFile = null;
        int i = 1;
        do {
            freshName = fullName + i++ + "." + type;
            freshFile = new File(freshName);
        } while (freshFile.exists());
        return freshFile;
    }
}
