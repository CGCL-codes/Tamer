package edu.princeton.wordnet.pojos.reveng;

import org.hibernate.cfg.reveng.DelegatingReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.TableIdentifier;

/**
 * @author bbou
 */
public class Strategy extends DelegatingReverseEngineeringStrategy {

    /**
	 * Constructor
	 * 
	 * @param delegate
	 */
    public Strategy(final ReverseEngineeringStrategy delegate) {
        super(delegate);
    }

    @Override
    public String tableToClassName(final TableIdentifier tableIdentifier) {
        String thisResult = super.tableToClassName(tableIdentifier);
        if (thisResult.endsWith("s")) {
            thisResult = thisResult.substring(0, thisResult.length() - 1);
        }
        System.out.println("table " + tableIdentifier.getName() + "-> class " + thisResult);
        return thisResult;
    }
}
