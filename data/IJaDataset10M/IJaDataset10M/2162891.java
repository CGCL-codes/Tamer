package org.tripcom.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.net.URI;
import java.util.Random;
import net.jini.core.lease.Lease;
import org.junit.Test;
import org.tripcom.integration.entry.CommitTransaction;
import org.tripcom.integration.entry.CreateTransaction;
import org.tripcom.integration.entry.RollbackTransaction;
import org.tripcom.integration.entry.TransactionEntry;

/**
 * Test cases for the TransactionManager.
 */
public class RequestTest extends AbstractTest {

    /**
	 * Writes a CreateTransaction and waits for a TransactionEntry.
	 * 
	 * @throws Exception
	 *             if an error occurs.
	 */
    @Test
    public void testCreateTransaction() throws Exception {
        CreateTransaction request = new CreateTransaction(new Random().nextLong());
        this.getJS().write(request, null, Lease.FOREVER);
        TransactionEntry result = (TransactionEntry) this.getJS().take(new TransactionEntry(), null, DEFAULT_TIMEOUT);
        assertNotNull(result);
        assertEquals(request.operationID, result.operationID);
        assertNotNull(result.transactionID);
    }

    /**
	 * Writes a CommitTransaction request and waits for a CommitTransaction
	 * entry where the "checked" boolean is set to <code>true</code>.
	 * 
	 * @throws Exception
	 *             if an error occurs.
	 */
    @Test
    public void testCommitTransaction() throws Exception {
        CommitTransaction request = new CommitTransaction(new Random().nextLong(), new URI("tsc://localhost/TX/test"));
        this.getJS().write(request, null, Lease.FOREVER);
        CommitTransaction template = new CommitTransaction();
        template.checked = true;
        CommitTransaction result = (CommitTransaction) this.getJS().take(template, null, DEFAULT_TIMEOUT);
        assertNotNull(result);
        assertEquals(request.operationID, result.operationID);
        assertEquals(request.transactionID, result.transactionID);
    }

    /**
	 * Writes a RollbackTransaction request and waits for a RollbackTransaction
	 * entry where the "checked" boolean is set to <code>true</code>.
	 * 
	 * @throws Exception
	 *             if an error occurs.
	 */
    @Test
    public void testRollbackTransaction() throws Exception {
        RollbackTransaction request = new RollbackTransaction(new Random().nextLong(), new URI("tsc://localhost/TX/test"));
        this.getJS().write(request, null, Lease.FOREVER);
        RollbackTransaction template = new RollbackTransaction();
        template.checked = new Boolean(true);
        RollbackTransaction result = (RollbackTransaction) this.getJS().take(template, null, DEFAULT_TIMEOUT);
        assertNotNull(result);
        assertEquals(request.operationID, result.operationID);
        assertEquals(request.transactionID, result.transactionID);
    }
}
