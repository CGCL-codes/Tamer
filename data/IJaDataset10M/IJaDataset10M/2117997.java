package de.knowwe.kdom.sectionFinder;

import java.util.List;
import de.knowwe.core.kdom.Type;
import de.knowwe.core.kdom.parsing.Section;
import de.knowwe.core.kdom.sectionFinder.MultiSectionFinder;
import de.knowwe.core.kdom.sectionFinder.SectionFinder;
import de.knowwe.core.kdom.sectionFinder.SectionFinderResult;

/**
 * This SectionFinder is created with an array of Strings. It looks for unquoted
 * occurrences of these Strings and creates Sections from it.
 * 
 * @author Jochen
 * 
 */
public class OneOfStringEnumUnquotedFinder implements SectionFinder {

    private final MultiSectionFinder msf;

    public OneOfStringEnumUnquotedFinder(String[] values) {
        msf = new MultiSectionFinder();
        for (int i = 0; i < values.length; i++) {
            msf.addSectionFinder(new UnquotedExpressionFinder(values[i]));
        }
    }

    @Override
    public List<SectionFinderResult> lookForSections(String text, Section<?> father, Type type) {
        return msf.lookForSections(text, father, type);
    }
}
