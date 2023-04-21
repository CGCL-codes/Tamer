package org.arastreju.core.terminology.binding;

import org.arastreju.api.common.Language;
import org.arastreju.api.terminology.WordDefinition;
import org.arastreju.api.terminology.attributes.Flavor;
import org.arastreju.api.terminology.attributes.Genus;
import org.arastreju.api.terminology.attributes.WordClass;
import org.arastreju.api.terminology.binding.ImportContext;
import org.arastreju.core.ResourceGate;
import org.arastreju.core.model.lexis.WordDefinitionDBO;
import org.jdom.Element;

/**
 * Binding for numerals (named numbers).
 * 
 * Created: 02.01.2008 
 *
 * @author Oliver Tigges
 */
public class NumeralBinding extends WordDefinitionBinding {

    public static final String NUMERAL = "numeral";

    public NumeralBinding(ResourceGate gate) {
        super(gate);
    }

    protected boolean canHandle(Element element) {
        return NUMERAL.equalsIgnoreCase(element.getName());
    }

    @Override
    public WordDefinition createDefinition(Element element, ImportContext context) {
        String nennform = readNennform(element);
        Flavor flavor = readFlavor(element);
        Language language = readLanguage(element, context);
        Genus genus = readGenus(element);
        WordDefinitionDBO def = createWordDefinition(language, WordClass.NUMERAL, nennform, nennform);
        def.setFlavor(flavor);
        def.setGenus(genus);
        return def;
    }

    @Override
    public Element exportElement(WordDefinition def) {
        Element element = new Element(NUMERAL);
        writeNennform(element, def.getNennform());
        writeLanguage(element, def.getLanguage());
        writeFlavor(element, def.getFlavor());
        writeGenus(element, def.getGenus());
        return element;
    }
}
