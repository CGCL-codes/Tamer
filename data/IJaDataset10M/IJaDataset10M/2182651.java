package org.expasy.jpl.core.ms.spectrum.annot;

import java.text.ParseException;
import java.util.Comparator;
import org.expasy.jpl.commons.base.builder.StringBasedFactory;
import org.expasy.jpl.commons.collection.sorter.ChainedComparator;
import org.expasy.jpl.commons.collection.sorter.ChainedStringCodedComparator;
import org.expasy.jpl.commons.collection.sorter.StringCodedComparatorFactory;
import org.expasy.jpl.core.ms.spectrum.annot.comp.FragmentChargeComparator;
import org.expasy.jpl.core.ms.spectrum.annot.comp.FragmentModifComparator;
import org.expasy.jpl.core.ms.spectrum.annot.comp.FragmentTypeComparator;

/**
 * This comparator compare {@code FragmentAnnotation} by priority level given
 * internal class {@code FragmentRuleMatcher} knowing a set of conditions on
 * annotation dods and the given annotation.
 * 
 * <pre>
 * Example: 
 * 
 * FragmentAnnotationComparator comparator =
 * 	new FragmentAnnotationComparator();
 * 		
 * // comp name:rule
 * String priorityString = "ion:y>b >> charge:ascent >> loss:ascent";
 * 		
 * comparator.setPriorityRule(priorityString);
 * 		
 * List<FragmentAnnotation> annots = new ArrayList<FragmentAnnotation>();
 * 		
 * annots.add(FragmentAnnotationImpl.valueOf("y24^3"));
 * annots.add(FragmentAnnotationImpl.valueOf("y16-35^2"));
 * annots.add(FragmentAnnotationImpl.valueOf("b8-18"));
 * 		
 * // [y24^3, y16-35^2, b8-18]
 * Collections.sort(annots, comparator);
 * // [b8-18, y16-35^2, y24^3]
 * 
 * </pre>
 * 
 * @author nikitin
 * 
 * @version 1.0
 * 
 */
public class FragmentAnnotationComparator implements Comparator<FragmentAnnotation> {

    private static StringCodedComparatorFactory<FragmentAnnotation> FACTORY;

    private String priorityStringRule;

    private ChainedComparator<FragmentAnnotation> comparators;

    private ChainedStringCodedComparator<FragmentAnnotation> comparators2;

    static {
        FACTORY = StringCodedComparatorFactory.newInstance();
        FACTORY.register(FragmentTypeComparator.COMPARATOR_ID, FragmentTypeComparator.newInstance());
        FACTORY.register(FragmentChargeComparator.COMPARATOR_ID, FragmentChargeComparator.newInstance());
        FACTORY.register(FragmentModifComparator.COMPARATOR_ID, FragmentModifComparator.newInstance());
    }

    private FragmentAnnotationComparator() {
        setDefaultPriorityRule();
    }

    private FragmentAnnotationComparator(String rules) throws ParseException {
        setPriorityRule(rules);
    }

    @SuppressWarnings("unchecked")
    private FragmentAnnotationComparator(ChainedComparator<FragmentAnnotation> comps) {
        comparators = ChainedComparator.newInstance(comps);
    }

    public static FragmentAnnotationComparator newInstance() {
        return new FragmentAnnotationComparator();
    }

    public static FragmentAnnotationComparator valueOf(String rules) throws ParseException {
        return new FragmentAnnotationComparator(rules);
    }

    public static FragmentAnnotationComparator valueOf(ChainedComparator<FragmentAnnotation> comps) throws ParseException {
        return new FragmentAnnotationComparator(comps);
    }

    public static void register(String id, StringBasedFactory<Comparator<FragmentAnnotation>> factory) {
        FACTORY.register(id, factory);
    }

    /**
	 * Set the priority for multi annotations in same sites.
	 * 
	 * @param rule the priority rule.
	 * @throws ParseException
	 */
    public final void setPriorityRule(String rule) throws ParseException {
        this.priorityStringRule = rule;
        try {
            this.comparators2 = ChainedStringCodedComparator.newInstance(rule, FACTORY);
        } catch (ParseException e) {
            throw new ParseException(rule + ": Bad priority rule syntax", -1);
        }
    }

    /**
	 * Set the default priority rule.
	 */
    public final void setDefaultPriorityRule() {
        try {
            setPriorityRule(FragmentAnnotations.DEFAULT_PRIORITY_RULE);
        } catch (ParseException e) {
            throw new IllegalStateException(FragmentAnnotations.DEFAULT_PRIORITY_RULE + ": invalid default priority rule" + e);
        }
    }

    /**
	 * @return the priority rule.
	 */
    public final String getPriorityRule() {
        return priorityStringRule;
    }

    public int compare(FragmentAnnotation f1, FragmentAnnotation f2) {
        if (comparators != null) {
            return comparators.compare(f1, f2);
        } else if (comparators2 != null) {
            return comparators2.compare(f1, f2);
        }
        throw new IllegalStateException("no defined comparators");
    }
}
