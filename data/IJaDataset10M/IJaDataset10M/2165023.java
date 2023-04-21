package org.arastreju.core.modelling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.arastreju.api.ServiceException;
import org.arastreju.api.TransactionControl;
import org.arastreju.api.common.CoreConstants;
import org.arastreju.api.common.Language;
import org.arastreju.api.common.QualifiedName;
import org.arastreju.api.context.Context;
import org.arastreju.api.modelling.Adjective;
import org.arastreju.api.modelling.ModellingConversation;
import org.arastreju.api.modelling.ModellingService;
import org.arastreju.api.modelling.Noun;
import org.arastreju.api.modelling.Verb;
import org.arastreju.api.modelling.flection.DeclinationClass;
import org.arastreju.api.modelling.flection.DeclinationClassKey;
import org.arastreju.api.ontology.ResourceResolver;
import org.arastreju.api.ontology.SemanticNetworkService;
import org.arastreju.api.ontology.apriori.Aras;
import org.arastreju.api.ontology.apriori.RDFS;
import org.arastreju.api.ontology.binding.ModelExtract;
import org.arastreju.api.ontology.model.Association;
import org.arastreju.api.ontology.model.ResourceID;
import org.arastreju.api.ontology.model.sn.ResourceNode;
import org.arastreju.api.ontology.model.sn.views.SNClass;
import org.arastreju.api.ontology.model.sn.views.SNEntity;
import org.arastreju.api.ontology.model.sn.views.SNName;
import org.arastreju.api.ontology.model.sn.views.SNProperty;
import org.arastreju.api.ontology.model.sn.views.SNTerm;
import org.arastreju.api.ontology.model.sn.views.SNText;
import org.arastreju.api.ontology.model.sn.views.SNUri;
import org.arastreju.api.terminology.TerminologyService;
import org.arastreju.api.terminology.WordDefinition;
import org.arastreju.api.terminology.Wortform;
import org.arastreju.api.terminology.attributes.Genus;
import org.arastreju.api.terminology.attributes.WordClass;
import org.arastreju.core.ResourceGate;
import org.arastreju.core.model.lexis.NounDefinitionDBO;
import org.arastreju.core.model.lexis.WordDefinitionDBO;
import org.arastreju.core.modelling.impl.AttachedModellingConversation;
import org.arastreju.core.modelling.impl.DetachedModellingConversation;
import org.arastreju.core.modelling.impl.SemanticModelMerger;
import org.arastreju.core.store.FlectionClassStore;
import org.arastreju.core.store.WordDefinitionStore;
import org.arastreju.core.terminology.flection.FlectionClassProvider;
import org.arastreju.core.terminology.flection.declination.DeclinationClassImpl;

/**
 * Implementation of {@link ModellingService}.
 * 
 * Created: 23.02.2008
 * 
 * @author Oliver Tigges 
 */
public class ModellingServiceImpl implements ModellingService {

    private final ResourceGate gate;

    private final FlectionClassProvider fcProvider;

    private final TerminologyService terminologyService;

    private final WordDefinitionStore wordDefStore;

    private final SemanticNetworkService snService;

    /**
	 * Creates a new instance of the modeling service based on given gate.
	 */
    public ModellingServiceImpl(final ResourceGate gate) {
        this.gate = gate;
        this.fcProvider = new FlectionClassProvider(new FlectionClassStore(gate.getEntityManager()));
        this.terminologyService = gate.lookupTerminologyService();
        this.wordDefStore = gate.lookupWordDefinitionStore();
        this.snService = gate.lookupSemanticNetworkService();
    }

    public ModellingConversation createDetachedConversation() {
        return new DetachedModellingConversation(gate);
    }

    public ModellingConversation createAttachedConversation() {
        return new AttachedModellingConversation(gate);
    }

    /**
	 * Merges the given extract into main semantic model.
	 * @param extract The extract to merge.
	 */
    public void merge(final ModelExtract extract) {
        final TransactionControl tx = gate.requireTx();
        final SemanticModelMerger merger = new SemanticModelMerger(gate.lookupSemanticNetworkService(), gate.lookupNamespaceService());
        try {
            merger.merge(extract, gate.getContext());
        } catch (Exception e) {
            tx.rollback();
            throw new RuntimeException("Exception while trying to merge extract", e);
        }
        tx.commit();
    }

    public ResourceResolver getResolver() {
        return snService;
    }

    public List<QualifiedName> findAutoCompleteSuggestions(final String shortformBegin) {
        return snService.findByShortformStart(shortformBegin, 20);
    }

    public List<QualifiedName> findAutoCompleteSuggestions(final String shortformBegin, ResourceID type) {
        return snService.findByShortformStart(shortformBegin, type, 20);
    }

    /**
	 * Resolves the Semantic Nodes that have the given {@link WordDefinition} as extension.
	 */
    public List<SNClass> findNamedConcepts(final WordDefinition wordDefinition) {
        if (!wordDefinition.isPersistent()) {
            return Collections.emptyList();
        }
        final SNTerm term = snService.findTerm(wordDefinition);
        final SNProperty termProperty = snService.resolve(Aras.HAS_TERM).asProperty();
        final List<ResourceNode> subjects = snService.findSubjects(termProperty, term);
        final List<SNClass> meanings = new ArrayList<SNClass>(subjects.size());
        for (ResourceNode subj : subjects) {
            meanings.add(subj.asClass());
        }
        return meanings;
    }

    /**
	 * adds a word definition as extension to the given classifier.
	 * Associations stays transient.
	 */
    public SNTerm addTerm(final SNClass node, final WordDefinition wordDef) {
        SNTerm snTerm = snService.findTerm(wordDef);
        Association.create(getContext(), node, snTerm, RDFS.LABEL);
        return snTerm;
    }

    public SNName addProperName(SNEntity entity, WordDefinition wordDef, ResourceID predicate) {
        if (!wordDef.isPersistent()) {
            throw new IllegalArgumentException("cannot add tranisent word def: " + wordDef);
        }
        SNName snName = new SNName(wordDef);
        snName.setName(entity.getName() + "=" + wordDef.getNennform());
        Association.create(getContext(), entity, snName, predicate);
        return snName;
    }

    public SNText addLiteralDescription(final ResourceNode node, Language language, String literal) {
        SNText snText = new SNText(literal);
        Association.create(getContext(), node, snText, RDFS.COMMENT);
        return snText;
    }

    public SNUri setMediaDescription(final ResourceNode node, final String uri) {
        final Set<Association> existing = node.getAssociations(Aras.HAS_MEDIA_DESC);
        for (Association assoc : existing) {
            node.revoke(assoc);
        }
        final SNUri snURI = new SNUri(uri);
        Association.create(getContext(), node, snURI, Aras.HAS_MEDIA_DESC);
        return snURI;
    }

    public Noun createNoun(String nennform) {
        NounDefinitionDBO def = new NounDefinitionDBO(CoreConstants.DEFAULT_LANGUAGE, nennform);
        def.setRadical(nennform);
        def.setPluralBase(nennform);
        def.setGenus(Genus.U);
        return new Noun(def);
    }

    public void storeNoun(Noun noun) {
        TransactionControl tx = gate.requireTx();
        try {
            WordDefinition def = noun.getWordDefinition();
            if (noun.hasDeclinationClassKey()) {
                DeclinationClassImpl decl = fcProvider.getDeclinationClass(noun.getDeclinationClassKey());
                def.setFlectionClass(decl.getFlectionClass());
            }
            if (def.isPersistent()) {
                terminologyService.store(def);
            } else {
                terminologyService.addToTerminology(def, noun.getTerminology());
            }
            tx.commit();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public Adjective createAdjective(String nennform) {
        WordDefinition def = new WordDefinitionDBO();
        def.setWordClass(WordClass.ADJECTIVE);
        def.setNennform(nennform);
        def.setRadical(nennform);
        def.setGenus(Genus.U);
        def.setLanguage(CoreConstants.DEFAULT_LANGUAGE);
        return new Adjective(def);
    }

    public void storeAdjective(Adjective adjective) {
        TransactionControl tx = gate.requireTx();
        WordDefinition def = adjective.getWordDefinition();
        if (adjective.hasDeclinationClassKey()) {
            DeclinationClassImpl decl = fcProvider.getDeclinationClass(adjective.getDeclinationClassKey());
            def.setFlectionClass(decl.getFlectionClass());
        }
        wordDefStore.update(def);
        tx.commit();
    }

    public Verb createVerb(String nennform) {
        WordDefinition def = new WordDefinitionDBO();
        def.setWordClass(WordClass.VERB);
        def.setNennform(nennform);
        def.setRadical(nennform);
        def.setLanguage(CoreConstants.DEFAULT_LANGUAGE);
        return new Verb(def);
    }

    public void storeVerb(Verb verb) {
        TransactionControl tx = gate.requireTx();
        WordDefinition def = verb.getWordDefinition();
        wordDefStore.update(def);
        tx.commit();
    }

    public Context getContext() {
        return gate.getContext();
    }

    public Set<DeclinationClassKey> getDeclinationClassKeys(Language language) {
        return fcProvider.getDeclinationClassKeys(WordClass.NOUN, language);
    }

    public Set<DeclinationClass> getDeclinationClasses(Language language) {
        return fcProvider.getDeclinationClasses(WordClass.NOUN, language);
    }

    public List<Wortform> flect(Noun noun) {
        DeclinationClass decl = fcProvider.getDeclinationClass(noun.getDeclinationClassKey());
        return decl.flect(noun.getWordDefinition());
    }

    public List<Wortform> flect(Adjective adjective) {
        return Collections.emptyList();
    }
}
