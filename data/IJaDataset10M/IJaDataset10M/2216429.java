package edu.umich.marketplace;

import org.apache.log4j.Logger;
import com.webobjects.eocontrol.EOEditingContext;

public class CleanDatabase extends MarketplaceAction {

    private static final Logger logger = Logger.getLogger(CleanDatabase.class);

    private EOEditingContext _ec = new EOEditingContext();

    public CleanDatabase() {
    }

    public void doCleanupA() {
        logger.trace("--> doCleanupA()");
        _ec.saveChanges();
    }

    public void doCleanupB() {
        logger.trace("--> doCleanupB()");
        _ec.saveChanges();
    }
}
