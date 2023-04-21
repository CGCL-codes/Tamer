package phex.xml.sax.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import phex.rules.condition.Condition;
import phex.rules.condition.FileSizeCondition;
import phex.rules.condition.FileSizeCondition.Range;
import phex.xml.sax.PhexXmlSaxWriter;

/**
 * Filters all files matching the expression.
 */
public class DFileSizeCondition implements DCondition {

    public static final String ELEMENT_NAME = "filesize-condition";

    private List<FileSizeCondition.Range> ranges;

    /**
     * @param expression
     * @param case1
     */
    public DFileSizeCondition() {
        ranges = new ArrayList<FileSizeCondition.Range>();
    }

    public List<FileSizeCondition.Range> getRanges() {
        return ranges;
    }

    public void setRanges(List<FileSizeCondition.Range> ranges) {
        this.ranges = ranges;
    }

    public void serialize(PhexXmlSaxWriter writer) throws SAXException {
        writer.startElm(ELEMENT_NAME, null);
        if (ranges != null) {
            AttributesImpl attributes = null;
            Iterator iterator = ranges.iterator();
            while (iterator.hasNext()) {
                FileSizeCondition.Range range = (Range) iterator.next();
                attributes = new AttributesImpl();
                attributes.addAttribute("", "", "min", "CDATA", String.valueOf(range.min));
                attributes.addAttribute("", "", "max", "CDATA", String.valueOf(range.max));
                writer.startElm("range", attributes);
                writer.endElm("range");
            }
        }
        writer.endElm(ELEMENT_NAME);
    }

    public Condition createCondition() {
        FileSizeCondition cond = new FileSizeCondition();
        Iterator iterator = ranges.iterator();
        while (iterator.hasNext()) {
            cond.addRange((Range) iterator.next());
        }
        return cond;
    }
}
