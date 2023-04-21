package de.knowwe.core.kdom.sectionFinder;

import java.util.List;
import de.knowwe.core.kdom.Type;
import de.knowwe.core.kdom.parsing.Section;

/**
 * 
 * @author Jochen
 * @created 21.07.2010
 */
public interface SectionFinder {

    /**
	 * 
	 * Allocates text parts for the type owning this sectionfinder. The
	 * resulting SectionFinderResult list contains indices of substrings of the
	 * passed text. These specified substrings will be allocated to this type.
	 * Method will be called multiple times with various article fragments
	 * depending on previous allocations of preceding types. If no interesting
	 * section is found in a passed fragment, return 'null' or an empty list;
	 * 
	 * @param text Text fragment of the wiki article source
	 * @param father TODO
	 * @param type TODO
	 * @return List of SectionFinderResults with informations about what part of
	 *         the next belongs to the ObjectType calling the SectionFinder
	 */
    public List<SectionFinderResult> lookForSections(String text, Section<?> father, Type type);
}
