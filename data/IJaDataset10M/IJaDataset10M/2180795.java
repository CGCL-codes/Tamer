package de.spotnik.mail;

import de.spotnik.mail.core.Activator;
import junit.framework.TestCase;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author Jens Rehp�hler
 * @created 23.02.2006
 */
public abstract class AbstractSpotnikTest extends TestCase {

    /** appContext. */
    public static final Activator CONTEXT;

    /** transaction template. */
    private static final TransactionTemplate TT;

    static {
        CONTEXT = Activator.getDefault();
        TT = new TransactionTemplate();
        TT.setTransactionManager((PlatformTransactionManager) AbstractSpotnikTest.CONTEXT.getBean("transactionManager"));
    }

    /**
     * @return the transaction template
     */
    public static TransactionTemplate getTransactionTemplate() {
        return TT;
    }
}
