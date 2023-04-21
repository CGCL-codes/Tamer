package net.ontopia.topicmaps.classify;

import java.util.*;
import net.ontopia.utils.*;
import gnu.trove.TObjectIntHashMap;
import gnu.trove.TObjectIntIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PUBLIC: Represents a concept which occurs in the classified
 * content.  A term can have many variants, all of which can be found
 * from this object. It also has a score, indicating the importance of
 * the term within the content.
 */
public class Term {

    static Logger log = LoggerFactory.getLogger(Term.class.getName());

    protected String stem;

    protected double score = 1.0d;

    protected int totalOccurrences;

    protected TObjectIntHashMap<Variant> variants = new TObjectIntHashMap<Variant>();

    Term(String stem) {
        this.stem = stem;
    }

    /**
   * PUBLIC: Returns the stem common to all variants of the term.
   * Often, the stem does not actually occur in the content.
   */
    public String getStem() {
        return stem;
    }

    /**
   * PUBLIC: Returns the term's score, a number in the range 0-1,
   * indicating its importance within the content.   
   */
    public double getScore() {
        return score;
    }

    /**
   * PUBLIC: Returns all variant spellings of this term within the
   * content.
   */
    public Variant[] getVariants() {
        return variants.keys(new Variant[] {});
    }

    /**
   * PUBLIC: Returns all variant spellings of this term within the
   * content, with the most important first.
   */
    public Variant[] getVariantsByRank() {
        Variant[] ranked = getVariants();
        Arrays.sort(ranked, new VariantComparator());
        return ranked;
    }

    /**
   * PUBLIC: Returns the number of times the term occurred within the
   * classified content.
   */
    public int getOccurrences() {
        return totalOccurrences;
    }

    /**
   * PUBLIC: Returns the preferred variant of the term. This is a form
   * of the term which actually occurred in the classified content.
   */
    public String getPreferredName() {
        if (variants.isEmpty()) return getStem();
        Variant maxKey = null;
        int maxValue = -1;
        TObjectIntIterator<Variant> iter = variants.iterator();
        while (iter.hasNext()) {
            iter.advance();
            int thisValue = iter.value();
            Variant thisKey = iter.key();
            if ((thisValue > maxValue) || ((thisValue == maxValue) && (thisKey.getValue().compareTo(maxKey.getValue()) < 0))) {
                maxValue = thisValue;
                maxKey = thisKey;
            }
        }
        return maxKey.getValue();
    }

    protected double getScore(Variant v) {
        return (1.0d * getOccurrences(v)) / totalOccurrences;
    }

    protected int getOccurrences(Variant variant) {
        return variants.get(variant);
    }

    protected void setScore(double score, String reason) {
        if (score <= 0.0d) throw new RuntimeException("Score is not nillable: " + score + " term: " + this);
        log.debug(">" + stem + "< =" + score + ", " + reason);
        this.score = score;
    }

    protected void addScore(double ascore, String reason) {
        this.score += ascore;
        log.debug(">" + stem + "< +" + ascore + "=" + score + ", " + reason);
    }

    protected void multiplyScore(double factor, String reason) {
        this.score = score * factor;
        log.debug(">" + stem + "< *" + factor + "=" + score + ", " + reason);
    }

    protected void divideScore(double factor, String reason) {
        this.score = score / factor;
        log.debug(">" + stem + "< /" + factor + "=" + score + ", " + reason);
    }

    protected void addVariant(Variant variant) {
        addVariant(variant, 1);
    }

    protected void addVariant(Variant variant, int occurrences) {
        if (variants.get(variant) > 0) variants.increment(variant); else variants.put(variant, occurrences);
        totalOccurrences += occurrences;
    }

    protected void merge(Term other) {
        if (other == this) return;
        this.score = this.score + other.score;
        this.totalOccurrences = this.totalOccurrences + other.totalOccurrences;
        TObjectIntIterator<Variant> iter = other.variants.iterator();
        while (iter.hasNext()) {
            iter.advance();
            Variant key = iter.key();
            int value = iter.value();
            if (this.variants.containsKey(key)) this.variants.adjustValue(key, value); else this.variants.put(key, value);
            key.replaceTerm(this);
        }
    }

    public String toString() {
        return '\'' + getStem() + "\'" + getScore() + ":" + (variants.isEmpty() ? "" : Arrays.asList(variants.keys()).toString());
    }

    protected static Comparator SCORE_COMPARATOR = new Comparator() {

        public int compare(Object o1, Object o2) {
            Term t1 = (Term) o1;
            Term t2 = (Term) o2;
            return ObjectUtils.compare(t2.getScore(), t1.getScore());
        }
    };

    private class VariantComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            Variant v1 = (Variant) o1;
            Variant v2 = (Variant) o2;
            int c = ObjectUtils.compare(getOccurrences(v2), getOccurrences(v1));
            if (c != 0) return c;
            return v1.getValue().compareTo(v2.getValue());
        }
    }

    ;
}
