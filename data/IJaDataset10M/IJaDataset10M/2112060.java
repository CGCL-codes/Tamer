package edu.ucdavis.genomics.metabolomics.binbase.gui.swt.query.rule;

import org.hibernate.criterion.Criterion;

/**
 * @author wohlgemuth
 */
public interface Rule {

    /**
	 * gibt eine beschreibung zur?ck
	 * 
	 * @return
	 * @uml.property name="description" multiplicity="(0 1)"
	 */
    String getDescription();

    /**
	 * gibt die expression zur?ck
	 * 
	 * @return
	 * @uml.property name="expression"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
    Criterion getExpression();

    /**
	 * gibt den namen der propertie zur?ck
	 * 
	 * @return
	 * @uml.property name="property"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
    Getter getProperty();

    /**
	 * gibt an ob diese regel aktive ist
	 * 
	 * @return
	 */
    boolean isRuleActive();

    /**
	 * gibt eine m?gliche subrule oder null zur?ck
	 * 
	 * @return
	 * @uml.property name="subRule"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
    Rule getSubRule();

    /**
	 * ob die expression objekte folgender klasse unterst?zt
	 * 
	 * @param c
	 * @return
	 */
    boolean isSupported(Class<?> c);

    /**
	 * werden subrules gestattet
	 * 
	 * @return
	 */
    boolean allowSubrules();

    /**
	 * @author wohlgemuth
	 * @version Apr 26, 2006
	 * @param g
	 * @return
	 */
    public String getPropertyName();
}
