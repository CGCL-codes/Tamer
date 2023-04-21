package org.translationcomponent.service.document.xml.tagfilter;

import java.util.Set;
import javax.xml.stream.events.StartElement;
import org.translationcomponent.api.impl.utils.StringHelper;

/**
 * Specifies all tags that need to be ignored.
 * 
 * @author ROB
 * 
 */
public class TagWithNoTranslation implements TranslateStartElementFilter {

    private Set<String> tags;

    public boolean accept(final StartElement o) {
        try {
            String name = o.getName().getLocalPart();
            return !tags.contains(name);
        } catch (ClassCastException e) {
            return true;
        }
    }

    public void setTags(String[] tags) {
        this.tags = StringHelper.toImmutableSet(tags, true);
    }
}
