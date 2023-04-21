package vmp.gate.tbl.utils;

import gate.Annotation;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Valentina Munoz Porras
 * @version 1.0 22 de abril de 2008
 */
public class PredicatesIterator implements Iterable<Predicate>, Iterator<Predicate> {

    private List<Template> templates;

    private Map<Predicate, Set<Rule>> rules;

    private List<Annotation> vicinity;

    private Iterator<Template> templatesIt;

    private Iterator<Predicate> predicatesIt;

    private boolean applicableNow;

    private Template currentTemplate;

    /** Creates a new instance of PredicatesIterator */
    public PredicatesIterator() {
    }

    public PredicatesIterator(List<Template> tpls, Map<Predicate, Set<Rule>> rls) {
        templates = tpls;
        rules = rls;
        applicableNow = true;
    }

    public PredicatesIterator(List<Template> tpls, Map<Predicate, Set<Rule>> rls, boolean an) {
        templates = tpls;
        rules = rls;
        applicableNow = an;
    }

    public void setVicinity(List<Annotation> v) {
        vicinity = v;
    }

    public Iterator<Predicate> iterator() {
        templatesIt = templates.iterator();
        setRequiredPredicates();
        return this;
    }

    public boolean hasNext() {
        while (!predicatesIt.hasNext()) {
            if (templatesIt.hasNext()) {
                setRequiredPredicates();
            } else {
                return false;
            }
        }
        return true;
    }

    public Predicate next() {
        return predicatesIt.next();
    }

    public void remove() {
    }

    public void setApplicableNow(boolean an) {
        applicableNow = an;
    }

    public Template getCurrentTemplate() {
        return currentTemplate;
    }

    private void setRequiredPredicates() {
        currentTemplate = templatesIt.next();
        if (applicableNow) {
            predicatesIt = currentTemplate.getPredicateTemplate().createPredicates(vicinity).iterator();
        } else {
            predicatesIt = currentTemplate.getPredicateTemplate().createPastApplicablePredicates(vicinity).iterator();
        }
    }

    void freeMemory() {
        templates = null;
        rules = null;
        vicinity.clear();
        vicinity = null;
        templatesIt = null;
        predicatesIt = null;
        currentTemplate = null;
    }
}
