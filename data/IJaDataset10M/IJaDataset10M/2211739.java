package de.d3web.we.kdom.sectionFinder;

import java.util.List;
import de.d3web.we.kdom.Section;
import de.d3web.we.kdom.Type;

/**
 * @author Jochen
 * 
 * 
 *         The normal trim() operation of Strings also cuts of line breaks. This
 *         SectionFinder only cuts off real space characters
 * 
 * 
 */
public class AllTextFinderTrimSpaces implements SectionFinder {

    @Override
    public List<SectionFinderResult> lookForSections(String text, Section father, Type type) {
        int leadingSpaces = 0;
        while (text.charAt(leadingSpaces) == ' ') {
            leadingSpaces++;
        }
        int postSpacesIndex = text.length() - 1;
        while (text.charAt(postSpacesIndex) == ' ') {
            postSpacesIndex--;
        }
        if (text.substring(leadingSpaces, postSpacesIndex + 1).matches("\\r?\\n")) return null;
        return SectionFinderResult.createSingleItemList(new SectionFinderResult(leadingSpaces, postSpacesIndex + 1));
    }
}
