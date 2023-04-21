package org.arastreju.core.terminology.binding;

import org.arastreju.api.common.Language;
import org.arastreju.api.terminology.FlectedWord;
import org.arastreju.api.terminology.WordDefinition;
import org.arastreju.api.terminology.attributes.Flavor;
import org.arastreju.api.terminology.attributes.WordClass;
import org.arastreju.api.terminology.binding.ImportContext;
import org.arastreju.core.ResourceGate;
import org.jdom.Element;

/**
 * Binding for adverbs.
 *
 * Created: 23.07.2006
 * 
 * @author Oliver Tigges
 */
public class AdverbBinding extends WordDefinitionBinding {

    public static final String ADVERB = "adverb";

    public AdverbBinding(ResourceGate gate) {
        super(gate);
    }

    @Override
    public void importElement(Element element, ImportContext context) {
        WordDefinition def = createDefinition(element, context);
        FlectedWord flected = createFlectedWord(def, def.getNennform());
        store(flected);
    }

    @Override
    public WordDefinition createDefinition(Element element, ImportContext context) {
        String nennform = readNennform(element);
        Flavor flavor = readFlavor(element);
        Language language = readLanguage(element, context);
        WordDefinition def = createWordDefinition(language, WordClass.ADVERB, nennform, nennform);
        def.setFlavor(flavor);
        def.setLanguage(language);
        return def;
    }

    @Override
    public Element exportElement(WordDefinition def) {
        Element element = new Element(ADVERB);
        writeNennform(element, def.getNennform());
        writeLanguage(element, def.getLanguage());
        writeFlavor(element, def.getFlavor());
        return element;
    }
}
