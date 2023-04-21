package oxygen.markup.macros;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import oxygen.markup.MarkupMacroParameters;
import oxygen.markup.MarkupRenderContext;
import oxygen.util.StringUtils;

/**
 * Takes substring in CSV format, and writes out a 
 * table.
 * If first line is not blank, write that out as a header
 * @author ugorji
 */
public class CSVProcessor extends GenericMarkupMacro {

    public void doExecute(Writer writer, MarkupRenderContext rc, MarkupMacroParameters params) throws Exception {
        PrintWriter pw = new PrintWriter(writer);
        BufferedReader br = new BufferedReader(new StringReader(params.getContent()));
        String line = null;
        boolean firstline = true;
        List list = new ArrayList();
        pw.println("<table>");
        while ((line = br.readLine()) != null) {
            list.clear();
            StringUtils.csvSplit(line, list);
            String[] args = (String[]) list.toArray(new String[0]);
            addRow(args, pw, firstline);
            firstline = false;
        }
        pw.println("</table>");
    }

    private void addRow(String[] args, PrintWriter pw, boolean firstline) {
        if (args.length == 0) {
            return;
        }
        pw.println("<tr>");
        for (int i = 0; i < args.length; i++) {
            addCell(args[i], pw, firstline);
        }
        pw.println("</tr>");
    }

    private void addCell(String arg, PrintWriter pw, boolean firstline) {
        if (firstline) {
            pw.println("<th>" + arg + "</th>");
        } else {
            pw.println("<td>" + arg + "</td>");
        }
    }
}
