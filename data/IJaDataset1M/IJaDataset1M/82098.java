package org.arastreju.core.terminology.binding;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.arastreju.api.common.Language;
import org.arastreju.api.modelling.flection.DeclinationClassKey;
import org.arastreju.api.terminology.AbbrevationDefinition;
import org.arastreju.api.terminology.FlectedWord;
import org.arastreju.api.terminology.FlectionClass;
import org.arastreju.api.terminology.WordDefinition;
import org.arastreju.api.terminology.attributes.DefinitionOrigin;
import org.arastreju.api.terminology.attributes.Genus;
import org.arastreju.api.terminology.attributes.Numerus;
import org.arastreju.api.terminology.attributes.WordClass;
import org.arastreju.api.terminology.binding.ImportContext;
import org.arastreju.core.ResourceGate;
import org.arastreju.core.model.lexis.AbbrevationDefinitionDBO;
import org.arastreju.core.model.lexis.AffixDBO;
import org.arastreju.core.model.lexis.FlectedWordDBO;
import org.arastreju.core.model.lexis.WordDefinitionDBO;
import org.arastreju.core.store.FlectionClassStore;
import org.arastreju.core.store.WordDefinitionStore;
import org.jdom.Element;
import de.lichtflut.infra.Infra;
import de.lichtflut.infra.data.MultiMap;

/**
 * Binding for WordDefinitions to XML (JDom).
 * 
 * Created: 01.02.2006
 *  
 * @author Oliver Tigges
 */
public abstract class WordDefinitionBinding extends BaseBinding {

    private final FlectionClassStore flectionClassStore;

    private final WordDefinitionStore wordDefinitionStore;

    /**
	 * Constructor ob abstract base class.
	 */
    protected WordDefinitionBinding(final ResourceGate gate) {
        this.flectionClassStore = gate.lookupFlectionClassStore();
        this.wordDefinitionStore = gate.lookupWordDefinitionStore();
    }

    @Override
    public void importElement(Element element, ImportContext context) {
        final WordDefinition def = createDefinition(element, context);
        store(def, context);
    }

    public void deleteElement(Element element, ImportContext context) {
        WordDefinition def = createDefinition(element, context);
        wordDefinitionStore.deleteByKey(def.getKey());
    }

    /**
	 * to be implemented by subclasses
	 * @param context TODO
	 */
    public abstract WordDefinition createDefinition(Element element, ImportContext context);

    @SuppressWarnings("unchecked")
    protected List<AbbrevationDefinition> readAbbrevationDefinitions(Element element) {
        List<AbbrevationDefinition> result = new ArrayList<AbbrevationDefinition>();
        List<Element> abbrevations = element.getChildren(ELEMENT_ABBREVATION);
        for (Element abbrElement : abbrevations) {
            String name = abbrElement.getAttributeValue(ATTRIBUTE_NAME);
            AbbrevationDefinitionDBO def = createAbbrevationDefinition(name);
            result.add(def);
        }
        return result;
    }

    protected void storeAbbrevations(WordDefinition def, Element element) {
        List<AbbrevationDefinition> abbrDefs = readAbbrevationDefinitions(element);
        for (AbbrevationDefinition ad : abbrDefs) {
            ad.setLanguage(def.getLanguage());
            ad.setWordDefinition(def);
            store(ad);
        }
    }

    protected FlectionClass findFlectionClass(Element element, Language language, Genus genus) {
        if (element.getAttribute(ATTRIBUTE_FLECTION_CLASS) != null) {
            String name = element.getAttributeValue(ATTRIBUTE_FLECTION_CLASS);
            return findFlectionClass(language, name, genus);
        }
        return null;
    }

    protected FlectionClass findFlectionClass(Language language, String name, Genus genus) {
        return flectionClassStore.findByKey(new DeclinationClassKey(WordClass.NOUN, language, name, genus));
    }

    protected List<FlectedWord> findFlectedWords(WordDefinition def) {
        return wordDefinitionStore.findFlectedWords(def);
    }

    protected List<WordDefinition> findDerived(WordDefinition def) {
        return wordDefinitionStore.findDerived(def);
    }

    /**
	 * creates transient FlectedWordDBO 
	 * @param wortart
	 * @param nennform
	 * @param name
	 * @return
	 */
    protected FlectedWord createFlectedWord(WordDefinition def, String name) {
        FlectedWord flected = new FlectedWordDBO(def);
        flected.setName(name);
        return flected;
    }

    /**
	 * creates transient FlectedWordDBO 
	 * @param wordclass
	 * @param nennform
	 * @param name
	 * @return
	 */
    protected FlectedWord createFlectedWord(WordClass wordclass, String nennform, String base, String name) {
        WordDefinitionDBO def = createWordDefinition(null, wordclass, nennform, base);
        FlectedWord flected = new FlectedWordDBO(def);
        flected.setName(name);
        return flected;
    }

    /**
	 * creates a new {@link AbbrevationDefinitionDBO}
	 * @param name
	 * @param locale
	 * @return
	 */
    protected AbbrevationDefinitionDBO createAbbrevationDefinition(String name) {
        AbbrevationDefinitionDBO ad = new AbbrevationDefinitionDBO();
        ad.setName(name);
        ad.setOrigin(DefinitionOrigin.LEXICON);
        return ad;
    }

    protected void addAffix(String prefix, Language language) {
        AffixDBO affix = wordDefinitionStore.findByName(prefix, language);
        if (affix == null) {
            affix = new AffixDBO(prefix, language);
            wordDefinitionStore.store(affix);
        }
    }

    protected WordDefinition store(WordDefinition def, ImportContext context) {
        return wordDefinitionStore.sync(def, context.getTerminology(), context.getChangeState());
    }

    protected FlectedWord store(FlectedWord flected) {
        return wordDefinitionStore.store(flected);
    }

    protected AbbrevationDefinition store(AbbrevationDefinition ad) {
        return wordDefinitionStore.store(ad);
    }

    protected MultiMap<Numerus, FlectedWord> groupByNumerus(List<FlectedWord> flected) {
        MultiMap<Numerus, FlectedWord> map = new MultiMap<Numerus, FlectedWord>();
        for (FlectedWord fw : flected) {
            if (fw.getNumerus() != null) {
                map.add(fw.getNumerus(), fw);
            }
        }
        return map;
    }

    protected Long getRevision(final Element element) {
        String revStr = element.getAttributeValue(ATTRIBUTE_REVISION);
        if (revStr == null) {
            return new Long(1);
        } else {
            return Long.parseLong(revStr);
        }
    }

    public static Comparator<FlectedWord> FLECTED_WORD_COMP = new Comparator<FlectedWord>() {

        public int compare(FlectedWord a, FlectedWord b) {
            if (!Infra.equals(a.getCasus(), b.getCasus()) && a.getCasus() != null) {
                return a.getCasus().compareTo(b.getCasus());
            } else if (!Infra.equals(a.getTempus(), b.getTempus()) && a.getTempus() != null) {
                return a.getTempus().compareTo(b.getTempus());
            } else if (!Infra.equals(a.getNumerus(), b.getNumerus()) && a.getNumerus() != null) {
                return a.getNumerus().compareTo(b.getNumerus());
            } else if (!Infra.equals(a.getPerson(), b.getPerson()) && a.getPerson() != null) {
                return a.getPerson().compareTo(b.getPerson());
            }
            return 0;
        }

        ;
    };
}
