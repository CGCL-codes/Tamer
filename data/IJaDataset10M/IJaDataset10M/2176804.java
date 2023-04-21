package fitlibrary.runner;

import java.io.PrintWriter;
import fitlibrary.table.Tables;

public class AbstractRunner {

    protected void outputHtml(PrintWriter output, Tables tables) {
        StringBuilder sb = new StringBuilder();
        tables.toHtml(sb);
        output.print(sb.toString());
    }

    protected void stackTrace(PrintWriter output, Exception e) {
        e.printStackTrace();
        output.print("<pre>\n");
        e.printStackTrace(output);
        output.print("</pre>\n");
    }
}
