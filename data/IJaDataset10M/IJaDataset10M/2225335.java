package org.equanda.tapestry.navigation;

import javolution.lang.TextBuilder;
import java.io.Serializable;

/**
 * Combination of entry id and page title for a navigationable page.
 *
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
public class NavigationLink implements Serializable {

    String entryId;

    String title;

    NavigationLink() {
    }

    NavigationLink(String id, String t) {
        entryId = id;
        title = t;
    }

    public String getEntryId() {
        return entryId;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        TextBuilder text = TextBuilder.newInstance();
        text.append(entryId);
        text.append('-');
        text.append(title);
        return text.toString();
    }
}
