package tlmanalyser.report;

import java.io.Writer;
import java.util.Observer;
import tlmanalyser.parser.event.ProblemParseEvent;

public interface ReportGenerator extends Observer {

    void getXml(Writer out) throws Exception;

    void panicModeFinish(Writer out, ProblemParseEvent pb) throws Exception;
}
