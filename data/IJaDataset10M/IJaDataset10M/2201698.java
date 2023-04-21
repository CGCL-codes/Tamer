package net.sf.drawbridge.exec.output;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CsvResultWriter implements ResultWriter {

    public void writeResults(List<Map<String, Object>> results, PrintStream out) throws Exception {
        if (results != null) {
            Set<String> cols = results.get(0).keySet();
            writeHeader(cols, out);
            for (Map<String, Object> row : results) {
                boolean first = true;
                for (String col : cols) {
                    if (!first) {
                        out.print(",");
                    }
                    out.print(format(row.get(col)));
                    first = false;
                }
                out.print("\n");
            }
        }
    }

    private void writeHeader(Set<String> cols, PrintStream out) {
        boolean first = true;
        for (String col : cols) {
            if (!first) {
                out.print(",");
            }
            out.print(col);
            first = false;
        }
        out.print("\n");
    }

    private String format(Object object) {
        if (object == null) {
            return "";
        } else {
            String text = object.toString();
            if (text.indexOf(',') != -1) {
                text = '"' + text + '"';
            }
            return text;
        }
    }
}
