package org.mandarax.reference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.mandarax.kernel.*;
import org.mandarax.util.StringUtils;
import org.mandarax.util.logging.LogCategories;

/**
 * Reference implementation of Robinson's unification algorithm.
 * @author <A HREF="mailto:a.kozlenkov@city.ac.uk">Alex Kozlenkov</A>
 * @author <A href="http://www-ist.massey.ac.nz/JBDietrich" target="_top">Jens Dietrich</A>
 * @version 3.4 <7 March 05>
 * @since 1.2
 * [Prova] Most methods made inheritable as the class is overridden
 * by ws.prova.reference.ProvaRobinsonUnificationAlgorithm
 * @author <A HREF="mailto:a.kozlenkov@city.ac.uk">Alex Kozlenkov</A>
 * @version 3.4 <7 March 05>
 */
public class RobinsonsUnificationAlgorithm implements ExtendedUnificationAlgorithm, LogCategories {

    private SemanticEvaluationPolicy semanticEvaluationPolicy = new DefaultSemanticEvaluationPolicy();

    /**
	 * Constructor.
	 */
    public RobinsonsUnificationAlgorithm() {
        super();
    }

    /**
	 * Apply a replacement to an array of terms.
	 * @return an array containing the results of applying the replacement
	 * @param t an array of terms
	 * @param r a replacement
	 */
    protected Term[] apply(Term[] t, Replacement r) {
        Term[] tn = new Term[t.length];
        for (int i = 0; i < t.length; i++) {
            tn[i] = t[i].apply(r);
        }
        return tn;
    }

    /**
	 * Get an array of all subterms of an array of terms.
	 * @return an array containing all subterms of all terms of the inpute array
	 * @param terms an array of terms
	 */
    protected Term[] getAllSubterms(Term[] terms) {
        int i, l = 0;
        Term[] st = null;
        for (i = 0; i < terms.length; i++) {
            l = l + terms[i].getAllSubterms().length;
        }
        Term[] collected = new Term[l];
        int j = 0;
        for (i = 0; i < terms.length; i++) {
            st = terms[i].getAllSubterms();
            for (int k = 0; k < st.length; k++) {
                collected[j] = st[k];
                j = j + 1;
            }
        }
        return collected;
    }

    /**
	 * Unify the two arrays of terms. Return <code>Unification.noUnificationPossible</code> if unifiction fails.
	 * @see org.mandarax.kernel.Unification#noUnificationPossible
	 * @return the computed unification
	 * @param t1 the first array of terms
	 * @param t2 the second array of terms
	 * @param session a session
	 */
    public Unification unify(Term[] t1, Term[] t2, Session session) {
        List substitutions = new ArrayList();
        boolean logOn = LOG_IE_UNIFICATION.isDebugEnabled();
        if (logOn) {
            LOG_IE_UNIFICATION.debug("Trying to unify: " + StringUtils.toString(t1) + " and " + StringUtils.toString(t2));
        }
        return unify(t1, t2, substitutions, 0, session, logOn);
    }

    public Unification unify(Term[] t1, Term[] t2, boolean isRule) {
        return null;
    }

    /**
	 * Unify two arrays of terms.
	 * @return org.mandarax.kernel.Unification
	 * @param t1 the first array of terms
	 * @param t2 the second array of terms
	 * @param subst the list of replavements
	 * @param start start investigation at this index
	 * @param session a session
	 * @param logOn if true then we log
	 */
    protected org.mandarax.kernel.Unification unify(Term[] t1, Term[] t2, List subst, int start, Session session, boolean logOn) {
        Term[] subTerms1 = getAllSubterms(t1);
        Term[] subTerms2 = getAllSubterms(t2);
        int maxIdx = Math.min(subTerms1.length, subTerms2.length);
        Term subTerm1, subTerm2, original, replace;
        for (int i = start; i < maxIdx; i++) {
            original = null;
            replace = null;
            subTerm1 = subTerms1[i];
            subTerm2 = subTerms2[i];
            if (!subTerm1.sameAs(subTerm2)) {
                if (subTerm1.isVariable()) {
                    original = subTerm1;
                    replace = subTerm2;
                } else if (subTerm2.isVariable()) {
                    original = subTerm2;
                    replace = subTerm1;
                }
                if (original == null) {
                    if (semanticEvaluationPolicy != null && semanticEvaluationPolicy.evaluateAndCompare(subTerm1, subTerm2, session, logOn)) continue; else return Unification.noUnificationPossible;
                }
                Replacement r = new Replacement(original, replace);
                subst.add(r);
                return unify(apply(t1, r), apply(t2, r), subst, i + 1, session, logOn);
            }
        }
        if (logOn) {
            LOG_IE_UNIFICATION.debug("Unified: " + StringUtils.toString(t1) + " and " + StringUtils.toString(t2));
            LOG_IE_UNIFICATION.debug("Substitutions:");
            for (Iterator it = subst.iterator(); it.hasNext(); ) {
                LOG_IE_UNIFICATION.debug(it.next().toString());
            }
        }
        return new Unification(subst, t1, t2);
    }

    /**
	 * Returns the semantic evaluation policy.
	 * @return the semantic evaluation policy
	 */
    public SemanticEvaluationPolicy getSemanticEvaluationPolicy() {
        return semanticEvaluationPolicy;
    }

    /**
	 * Sets the semantic evaluation policy.
	 * @param semanticEvaluationPolicy The semantic evaluation policy to set
	 */
    public void setSemanticEvaluationPolicy(SemanticEvaluationPolicy semanticEvaluationPolicy) {
        this.semanticEvaluationPolicy = semanticEvaluationPolicy;
    }
}
