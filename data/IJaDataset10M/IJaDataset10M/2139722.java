package org.tripcom.metadata.test.unit;

import java.net.URI;
import java.util.Set;
import net.jini.core.entry.Entry;
import org.junit.Test;
import org.openrdf.model.ValueFactory;
import org.tripcom.integration.entry.ClientInfo;
import org.tripcom.integration.entry.ManagementOperation;
import org.tripcom.integration.entry.TripleEntry;
import org.tripcom.metadata.handler.Handler;

/**
 * Unit tests for the {@link org.tripcom.metadata.handler.Handler}
 * 
 * @author Joachim Adi Schuetz (adi.schuetz@sti2.at)
 * @version $Id: AbstractHandlerTest.java 956 2008-05-19 11:31:49Z atoz $
 */
public abstract class AbstractHandlerTest extends AbstractIntegrationBusTest {

    URI structuralMetadataSpace;

    Handler handler;

    Set<TripleEntry> data;

    URI space;

    long timestamp;

    ManagementOperation operation;

    ClientInfo client;

    Long opid;

    URI transid;

    Entry entry;

    ValueFactory valFactory;

    /**
	 * override this method with the necessary test set-up functionality.
	 */
    protected void setUp() {
    }

    /**
	 * override this method with the necessary test tear-down functionality.
	 */
    protected void tearDown() {
    }

    /**
	 * tests if the handler object to be tested was initialized properly.
	 */
    @Test
    public void testTrivialTestSetup() {
        assertNotNull(handler);
    }

    /**
	 * tests the reaction of the handler if the entry is a null-value.
	 */
    @Test
    public void testHandlerNull() {
        assertNull(handler.handleRequest(null));
    }
}
