package org.aitools.synonyms.substitutions;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;
import org.aitools.synonyms.utils.XMLTagReader;

public class SubstitutionReader extends XMLTagReader<Substitution> {

    public SubstitutionReader(String fileName) throws IOException {
        super(fileName);
    }

    public Set<Substitution> getSubstitutionSet() {
        return getElements();
    }

    public static final Set<Substitution> getSubstitutionSet(String fileName) throws IOException {
        SubstitutionReader substitutionReader = new SubstitutionReader(fileName);
        return substitutionReader.getSubstitutionSet();
    }

    protected final String getTagName() {
        return "substitute";
    }

    protected final Substitution parseTag(XMLEvent startEvent, XMLEvent contentEvent) {
        String find = "";
        String replace = "";
        @SuppressWarnings("unchecked") Iterator<Attribute> attributes = startEvent.asStartElement().getAttributes();
        while (attributes.hasNext()) {
            Attribute attribute = attributes.next();
            if ("find".equalsIgnoreCase(attribute.getName().getLocalPart())) {
                find = attribute.getValue();
            } else if ("replace".equalsIgnoreCase(attribute.getName().getLocalPart())) {
                replace = attribute.getValue();
            }
        }
        return new Substitution(find, replace);
    }
}
