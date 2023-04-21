package edu.ucdavis.genomics.metabolomics.binbase.gui.swt.query.rule;

import org.eclipse.swt.widgets.Composite;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;

/**
 * @author wohlgemuth
 * 
 */
public class GreaterRule extends AbstractTextRule {

    /**
	 * @param parent
	 * @param style
	 * @param propertieyName
	 */
    public GreaterRule(Composite parent, Getter propertieyName) {
        super(parent, propertieyName);
        this.setLabel(">=");
    }

    /**
	 * @see edu.ucdavis.genomics.metabolomics.binbase.gui.swt.query.rule.AbstractRule#getExpression()
	 */
    public Criterion getExpression() {
        return Expression.ge(getPropertyName(), convert(this.getProperty()));
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
    public boolean allowSubrules() {
        return false;
    }
}
