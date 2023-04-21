package org.arastreju.tools.dictionary;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.arastreju.api.ArastrejuGate;
import org.arastreju.api.ArastrejuGateFactory;
import org.arastreju.api.terminology.WordDefinition;
import org.arastreju.api.terminology.WordDefinitionTemplate;
import org.arastreju.api.terminology.attributes.WordClass;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import de.lichtflut.infra.security.Crypt;

/**
 * Dumps all verbs from dictionary and creates ontology XML snippets.
 * 
 * Sample:
 * 
 * <node name="gehen" stereotype="ACTIVITY_CLASS">
 *   <intension locale="DE" type="VERB" wordDefinition="gehen" />
 * </node>
 * 
 * Created: 04.10.2008
 * 
 * @author Oliver Tigges
 * 
 */
public class ActivityClassDumper {

    private final ArastrejuGate gate;

    public ActivityClassDumper() {
        this.gate = ArastrejuGateFactory.getInstance().login("root", Crypt.md5Hex("root"));
    }

    public void dump() throws IOException {
        List<WordDefinition> verbs = loadVerbs();
        Set<String> existing = loadExistingClasses();
        Element root = new Element("verbs");
        for (WordDefinition def : verbs) {
            if (existing.contains(def.getNennform())) {
                continue;
            }
            Element node = new Element("node");
            node.setAttribute("name", def.getNennform());
            node.setAttribute("stereotype", "ACTIVITY_CLASS");
            Element intension = new Element("intension");
            intension.setAttribute("locale", def.getLanguage().name());
            intension.setAttribute("type", "VERB");
            intension.setAttribute("wordDefinition", def.getNennform());
            node.addContent(intension);
            root.addContent(node);
        }
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        outputter.getFormat().setEncoding("MacRoman");
        outputter.output(root, System.out);
    }

    public List<WordDefinition> loadVerbs() {
        WordDefinitionTemplate template = new WordDefinitionTemplate();
        template.setWordClass(WordClass.VERB);
        List<WordDefinition> defs = gate.lookupTerminologyService().findAllByTemplate(template);
        Collections.sort(defs, new Comparator<WordDefinition>() {

            public int compare(WordDefinition a, WordDefinition b) {
                return a.getNennform().compareTo(b.getNennform());
            }
        });
        return defs;
    }

    public Set<String> loadExistingClasses() {
        Set<String> result = new HashSet<String>();
        return result;
    }

    public static void main(String[] args) throws IOException {
        new ActivityClassDumper().dump();
    }
}
