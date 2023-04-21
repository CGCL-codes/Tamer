package eu.fbk.hlt.edits.rules;

import eu.fbk.hlt.common.Configurable;
import eu.fbk.hlt.common.EDITSException;
import eu.fbk.hlt.common.module.Definition;
import eu.fbk.hlt.common.module.Implementation;
import eu.fbk.hlt.common.module.ModuleInfo;
import eu.fbk.hlt.edits.EDITS;
import eu.fbk.hlt.edits.etaf.AnnotatedText;
import eu.fbk.hlt.edits.etaf.EntailmentCorpus;
import eu.fbk.hlt.edits.etaf.Node;
import eu.fbk.hlt.edits.etaf.RelationRule;
import eu.fbk.hlt.edits.etaf.RelationRules;
import eu.fbk.hlt.edits.etaf.Truth;
import eu.fbk.hlt.edits.etaf.Word;

/**
 * @author Milen Kouylekov
 */
public abstract class RulesRepository extends Configurable {

    public static final String MEMORY_RULES_REPOSITORY = "memory";

    public static final String MEMORY_RULES_REPOSITORY_CLASS = "eu.fbk.hlt.edits.rules.index.MemoryIndex";

    public abstract boolean containsStringRules();

    public abstract boolean containsWordRules();

    public abstract void index(EntailmentCorpus corpus) throws EDITSException;

    /**
	 * @param corpus
	 * @throws EDITSException
	 */
    public abstract void index(RelationRules corpus) throws EDITSException;

    @Override
    public ModuleInfo info() {
        ModuleInfo desc = super.info();
        desc.setDescription("Generic rule index");
        desc.options().addAll(definition().options());
        desc.subModules().addAll(definition().subModules());
        return desc;
    }

    /**
	 * @param t
	 * @param h
	 * @return
	 */
    public abstract RelationRule search(AnnotatedText t, AnnotatedText h);

    /**
	 * @param t
	 * @param h
	 * @param out
	 */
    public abstract void search(AnnotatedText t, AnnotatedText h, RelationRule out) throws EDITSException;

    /**
	 * @param t
	 * @param h
	 * @return
	 */
    public abstract Double searchScore(AnnotatedText t, AnnotatedText h);

    public abstract Double searchScore(Node t, Node h);

    public abstract Double searchScore(String t, String h);

    public abstract Double searchScore(Word t, Word h);

    /**
	 * @param t
	 * @param h
	 * @return
	 */
    public abstract Truth searchValue(AnnotatedText t, AnnotatedText h, double[] confidence) throws EDITSException;

    @Override
    public String type() {
        return EDITS.RULES_REPOSITORY;
    }

    /**
	 * @return
	 */
    public static Definition definition() {
        Definition def = new Definition();
        def.setTitle("Rules Repository");
        def.setDescription(EDITS.system().description(EDITS.RULES_REPOSITORY));
        def.setType(EDITS.RULES_REPOSITORY);
        Implementation i = new Implementation();
        i.setAlias(MEMORY_RULES_REPOSITORY);
        i.setClassName(MEMORY_RULES_REPOSITORY_CLASS);
        i.setDefault(true);
        def.implementations().add(i);
        return def;
    }
}
