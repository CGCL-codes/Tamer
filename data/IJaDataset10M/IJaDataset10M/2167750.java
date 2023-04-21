package fi.vtt.noen.testgen.observations.formatter;

import fi.vtt.noen.testgen.observations.data.ProgramRun;
import fi.vtt.noen.testgen.observations.data.Event;
import fi.vtt.noen.testgen.observations.data.EventAttribute;
import fi.vtt.noen.testgen.observations.data.ArrayAttribute;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Collection;

/**
 * @author Teemu Kanstr�n
 */
public class NoenFormatter extends BasicFormatter {

    private static int scenarioId = 0;

    public NoenFormatter(OutputStream out) {
        super(out);
    }

    public NoenFormatter(String fileName) throws IOException {
        super(fileName);
    }

    public String header() {
        OutputBuffer buf = new OutputBuffer();
        buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        buf.append("<Observations>");
        buf.append("<Source program=\"ToBeImplemented\"/>");
        return buf.toString();
    }

    public String footer() {
        OutputBuffer out = new OutputBuffer();
        out.append("</Observations>");
        return out.toString();
    }

    public String observations(ProgramRun test) {
        OutputBuffer buf = new OutputBuffer();
        buf.append("<ExecutionScenario id=\"" + scenarioId + "\" description=\"" + test.getName() + "\">");
        scenarioId++;
        for (Iterator<Event> ei = test.iterator(); ei.hasNext(); ) {
            Event event = ei.next();
            if (event.isExit()) {
                continue;
            }
            buf.append("<Event name=\"" + event.getName() + "\">");
            Iterator<EventAttribute> ai = event.attributes();
            while (ai.hasNext()) {
                EventAttribute attribute = ai.next();
                if (attribute instanceof ArrayAttribute) {
                    ArrayAttribute array = (ArrayAttribute) attribute;
                    Collection<String> attributes = array.getAttributes();
                    int n = 0;
                    for (String next : attributes) {
                        buf.append("<Attribute type=\"array\">" + next + "</Attribute>");
                        n++;
                    }
                    if (n == 0) {
                        buf.append("<Attribute type=\"array\"/>");
                    }
                } else {
                    buf.append("<Attribute name=\"" + attribute.getName() + "\">" + attribute.getValue() + "</Attribute>");
                }
            }
            buf.append("</Event>");
        }
        buf.append("</ExecutionScenario>");
        return buf.toString();
    }

    public String fileNameExtension() {
        return "xml";
    }
}
