package org.arastreju.core.terminology.binding;

import org.arastreju.api.common.Language;
import org.arastreju.api.terminology.FlectedWord;
import org.arastreju.api.terminology.WordDefinition;
import org.arastreju.api.terminology.attributes.WordClass;
import org.arastreju.api.terminology.binding.ImportContext;
import org.arastreju.core.ResourceGate;
import org.arastreju.core.model.lexis.WordDefinitionDBO;
import org.jdom.Element;

/**
 * Binding for interrogatives.
 *
 * Created: 26.03.2006
 * 
 * @author Oliver Tigges
 */
public class InterrogativeBinding extends WordDefinitionBinding {

    public static final String INTERROGATIVE = "interrogative";

    public InterrogativeBinding(ResourceGate gate) {
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
        Language language = readLanguage(element, context);
        WordDefinitionDBO def = createWordDefinition(language, WordClass.INTERROGATIVE, nennform, nennform);
        def.setLanguage(language);
        return def;
    }

    @Override
    public Element exportElement(WordDefinition def) {
        Element element = new Element(INTERROGATIVE);
        writeLanguage(element, def.getLanguage());
        writeNennform(element, def.getNennform());
        return element;
    }
}
