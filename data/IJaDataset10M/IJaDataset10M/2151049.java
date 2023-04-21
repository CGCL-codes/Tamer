package de.d3web.we.flow.type;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import de.d3web.core.inference.condition.Condition;
import de.d3web.diaFlux.inference.FlowchartProcessedCondition;
import de.d3web.we.kdom.condition.D3webCondition;
import de.knowwe.core.kdom.KnowWEArticle;
import de.knowwe.core.kdom.parsing.Section;
import de.knowwe.core.kdom.sectionFinder.RegexSectionFinder;

/**
 * 
 * @author Reinhard Hatko
 * @created 15.11.2010
 */
public class FlowchartProcessedConditionType extends D3webCondition<FlowchartProcessedConditionType> {

    public static final int FLOWCHART_GROUP = 1;

    public static final String REGEX = "PROCESSED\\[([^\\]]*)\\]";

    public static final Pattern PATTERN = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);

    @Override
    protected void init() {
        setSectionFinder(new RegexSectionFinder(PATTERN));
        FlowchartReference reference = new FlowchartReference();
        reference.setSectionFinder(new RegexSectionFinder(PATTERN, FLOWCHART_GROUP));
        addChildType(reference);
    }

    @Override
    protected Condition createCondition(KnowWEArticle article, Section<FlowchartProcessedConditionType> section) {
        Matcher matcher = PATTERN.matcher(section.getOriginalText());
        if (!matcher.matches()) {
            return null;
        } else {
            String flowName = matcher.group(1);
            return new FlowchartProcessedCondition(flowName);
        }
    }
}
