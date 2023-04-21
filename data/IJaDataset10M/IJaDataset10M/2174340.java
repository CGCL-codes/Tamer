package de.d3web.we.kdom.sectionFinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import de.d3web.we.kdom.KnowWEArticle;
import de.d3web.we.kdom.Section;
import de.d3web.we.kdom.Type;
import de.d3web.we.kdom.report.KDOMError;
import de.d3web.we.kdom.report.KDOMReportMessage;
import de.d3web.we.kdom.subtreeHandler.SubtreeHandler;

public class StringEnumChecker<T extends Type> extends SubtreeHandler<T> {

    private final String[] values;

    private final KDOMError error;

    private final int startOffset;

    private final int endOffset;

    public StringEnumChecker(String[] values, KDOMError error, int startOffset, int endoffset) {
        this.values = Arrays.copyOf(values, values.length);
        this.error = error;
        this.startOffset = startOffset;
        this.endOffset = endoffset;
    }

    public StringEnumChecker(String[] values, KDOMError error) {
        this(values, error, 0, 0);
    }

    @Override
    public Collection<KDOMReportMessage> create(KnowWEArticle article, Section s) {
        String sectionContent = s.getOriginalText();
        sectionContent = sectionContent.substring(startOffset);
        sectionContent = sectionContent.substring(0, sectionContent.length() - endOffset);
        String checkContent = sectionContent.trim();
        boolean found = false;
        for (String string : values) {
            if (checkContent.equalsIgnoreCase(string)) {
                found = true;
            }
        }
        List<KDOMReportMessage> msgs = new ArrayList<KDOMReportMessage>();
        if (!found) {
            msgs.add(error);
        }
        return msgs;
    }
}
