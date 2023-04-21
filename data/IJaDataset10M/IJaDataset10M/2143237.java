package abbot.finder.matchers.swt;

import org.eclipse.swt.widgets.Widget;
import abbot.finder.swt.Matcher;
import abbot.util.AWT;
import abbot.util.ExtendedComparator;

/** Provides matching of Widgets by widget name. */
public class NameMatcher extends AbstractMatcher {

    private String name;

    private String foundName;

    public NameMatcher(String name) {
        this.name = name;
    }

    public boolean matches(final Widget w) {
        foundName = null;
        w.getDisplay().syncExec(new Runnable() {

            public void run() {
                foundName = (String) (w.getData("name"));
            }
        });
        if (name == null) return foundName == null;
        return stringsMatch(name, foundName);
    }

    public String toString() {
        return "Name matcher (" + name + ")";
    }
}
