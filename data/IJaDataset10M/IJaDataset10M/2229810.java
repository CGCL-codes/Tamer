package de.d3web.we.kdom.sectionFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import de.d3web.we.kdom.Section;
import de.d3web.we.kdom.Type;

public class RegexSectionFinder implements SectionFinder {

    private final Pattern pattern;

    private final int group;

    public RegexSectionFinder(String p) {
        this(p, 0);
    }

    public RegexSectionFinder(String p, int patternmod) {
        this(p, patternmod, 0);
    }

    /**
	 * creates sections that reflect the content of the group <code>group</code>
	 * .
	 */
    public RegexSectionFinder(String p, int patternmod, int group) {
        this(Pattern.compile(p, patternmod), group);
    }

    public RegexSectionFinder(Pattern pattern) {
        this(pattern, 0);
    }

    public RegexSectionFinder(Pattern pattern, int group) {
        this.pattern = pattern;
        this.group = group;
    }

    @Override
    public List<SectionFinderResult> lookForSections(String text, Section<?> father, Type type) {
        ArrayList<SectionFinderResult> result = new ArrayList<SectionFinderResult>();
        Matcher m = pattern.matcher(text);
        int index = 0;
        while (m.find(index)) {
            if (m.group(group) != null) {
                result.add(createSectionFinderResult(m));
            }
            int next = m.end(group);
            if (next <= index) break;
            if (next >= text.length()) break;
            index = next;
        }
        return result;
    }

    protected SectionFinderResult createSectionFinderResult(Matcher m) {
        return new SectionFinderResult(m.start(group), m.end(group));
    }
}
