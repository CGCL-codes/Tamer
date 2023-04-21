package civquest.ruleset;

import java.util.Enumeration;
import java.util.Hashtable;
import java.net.URL;

/**
 * A Ruleset object normally maps to one *.ruleset file, from which
 * it builds its data collection by way of a Parser object invoked by
 * a Registry instance. Basically.
 */
public class Ruleset {

    private URL url;

    private String name;

    private Hashtable sections;

    public Ruleset(String name, URL url) {
        this.name = name;
        this.url = url;
        sections = new Hashtable(1);
    }

    public URL getURL() {
        return url;
    }

    public void addSection(Section section) {
        sections.put(section.getName(), section);
    }

    public Section getSection(String name) {
        return (Section) sections.get(name);
    }

    public Enumeration getSectionNames() {
        return sections.keys();
    }
}
