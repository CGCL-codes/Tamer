package org.tripcom.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.HashSet;
import java.util.Set;
import net.jini.core.entry.Entry;
import net.jini.core.lease.Lease;
import org.junit.Test;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.URIImpl;
import org.tripcom.integration.entry.CommitTransaction;
import org.tripcom.integration.entry.CreateTransaction;
import org.tripcom.integration.entry.DataResultExternal;
import org.tripcom.integration.entry.DistMetaMEntry;
import org.tripcom.integration.entry.EndTransaction;
import org.tripcom.integration.entry.EndTransactionResult;
import org.tripcom.integration.entry.ErrorResultExternal;
import org.tripcom.integration.entry.ExceptionEntry;
import org.tripcom.integration.entry.GetTransaction;
import org.tripcom.integration.entry.InternalDataTSAdapterEntry;
import org.tripcom.integration.entry.ManagementDataResultExternal;
import org.tripcom.integration.entry.MetaMQueryEntry;
import org.tripcom.integration.entry.MetaMResultEntry;
import org.tripcom.integration.entry.MgmtMetaMEntry;
import org.tripcom.integration.entry.OutDMEntry;
import org.tripcom.integration.entry.OutMetaMEntry;
import org.tripcom.integration.entry.OutOperationExternal;
import org.tripcom.integration.entry.RdDMEntry;
import org.tripcom.integration.entry.RdMetaMEntry;
import org.tripcom.integration.entry.RdOperationExternal;
import org.tripcom.integration.entry.RdQPEntry;
import org.tripcom.integration.entry.RdSCEntry;
import org.tripcom.integration.entry.RdTSAdapterEntry;
import org.tripcom.integration.entry.RdWithoutSpaceDMEntry;
import org.tripcom.integration.entry.ReadType;
import org.tripcom.integration.entry.Result;
import org.tripcom.integration.entry.RollbackTransaction;
import org.tripcom.integration.entry.SMTSAdapterQueryEntry;
import org.tripcom.integration.entry.SMTSAdapterResultEntry;
import org.tripcom.integration.entry.SecurityInfo;
import org.tripcom.integration.entry.SetCookieEntry;
import org.tripcom.integration.entry.TMRequestEntry;
import org.tripcom.integration.entry.TSAdapterEntry;
import org.tripcom.integration.entry.Template;
import org.tripcom.integration.entry.TransactionEnded;
import org.tripcom.integration.entry.TransactionEntry;
import org.tripcom.integration.entry.TripleEntry;
import org.tripcom.security.util.Util;

/**
 * This class contains a few (very basic) security manager component tests.
 *
 * @author Michael Lafite
 */
public class SimpleComponentTest extends AbstractComponentTest {

    /**
	 * Temporary test: assure connection to space works (has nothing to do with
	 * SM code).
	 *
	 * @throws Exception if any error occurs.
	 */
    @Test
    public void testConnectionToJavaSpace() throws Exception {
        OutDMEntry entry = new OutDMEntry();
        entry.timestamp = 12345L;
        getSpace().write(entry, null, Lease.FOREVER);
        OutDMEntry entry2 = (OutDMEntry) getSpace().take(entry, null, DEFAULT_TIMEOUT);
        assertNotNull("Entry is null but shouldn't be!");
        assertEquals(entry.timestamp + " should be equal to " + entry2.timestamp, entry.timestamp, entry2.timestamp);
    }

    /**
	 * Writes an empty (--&gt; no field values specified)
	 * {@code OutOperationExternal} entry into the bus. The SM is expected to
	 * take the entry and write an {@link OutDMEntry} entry.
	 * TODO: shouldn't it be an {@link ErrorResultExternal}?
	 *
	 * @throws Exception if any error occurs.
	 */
    @Test
    public void testEmptyOut() throws Exception {
        write(new OutOperationExternal(), new OutDMEntry(), null, true);
    }

    /**
	 * Writes an empty {@code RdOperationExternal} to the system bus.
	 * The SM is expected to return a {@code RdDMEntry}.
	 * TODO: shouldn't it be an {@link ErrorResultExternal}?
	 *
	 * @throws Exception if any error occurs.
	 */
    @Test
    public void testEmptyRead() throws Exception {
        write(new RdOperationExternal(), new RdDMEntry(), null, true);
    }

    /**
	 * Writes an {@code OutOpserationExternal} entry with no values specified
	 * except for an invalid certificate (but valid format) into the bus. The SM
	 * is expected to take the entry and write a {@link ErrorResultExternal}
	 * entry.
	 *
	 * @throws Exception if any error occurs.
	 */
    @Test
    public void testEmptyOutWithInvalidCertificate() throws Exception {
        OutOperationExternal out = new OutOperationExternal();
        out.securityInfo = new SecurityInfo("-----BEGIN CERTIFICATE-----\n" + "INVALIDCERTIFICATESTRINGWITHVALIDFORMATGSIb3DQEBBAUAMHsxCzAJBgNV\n" + "BAYTAkFVMRMwEQYDVQQIEwpTb21lLVN0YXRlMQ8wDQYDVQQHEwZWaWVubmExEDAO\n" + "BgNVBAoTB2V4YW1wbGUxEzARBgNVBAMTCmhhbnMgbWF5ZXIxHzAdBgkqhkiG9w0B\n" + "CQEWEGhhbnNAZXhhbXBsZS5jb20wHhcNMDgwMTMxMTIwMzI0WhcNMTgwMTI4MTIw\n" + "MzI0WjB7MQswCQYDVQQGEwJBVTETMBEGA1UECBMKU29tZS1TdGF0ZTEPMA0GA1UE\n" + "BxMGVmllbm5hMRAwDgYDVQQKEwdleGFtcGxlMRMwEQYDVQQDEwpoYW5zIG1heWVy\n" + "MR8wHQYJKoZIhvcNAQkBFhBoYW5zQGV4YW1wbGUuY29tMIGfMA0GCSqGSIb3DQEB\n" + "AQUAA4GNADCBiQKBgQC/qbjN8d3fh0dJsUOUSw3DSlJUpzG9iqdtdzTiiegaxHVn\n" + "iVEcZJIutKIavapvDSkewClcD+L5dWc09aW7SKj/xKI3UNE5KRd3NGvn7w81/6jN\n" + "NEYAhpgzvt8D7/Tr91CHrCHYwGJiwRo3BqFESOnK8Knjvkchkc8V1viWYehrsQID\n" + "AQABo4HgMIHdMB0GA1UdDgQWBBQM7MykqQa36JvWc83trIevR/NIfTCBrQYDVR0j\n" + "BIGlMIGigBQM7MykqQa36JvWc83trIevR/NIfaF/pH0wezELMAkGA1UEBhMCQVUx\n" + "EzARBgNVBAgTClNvbWUtU3RhdGUxDzANBgNVBAcTBlZpZW5uYTEQMA4GA1UEChMH\n" + "ZXhhbXBsZTETMBEGA1UEAxMKaGFucyBtYXllcjEfMB0GCSqGSIb3DQEJARYQaGFu\n" + "c0BleGFtcGxlLmNvbYIJAJErup9ICWQfMAwGA1UdEwQFMAMBAf8wDQYJKoZIhvcN\n" + "AQEEBQADgYEApYfmxYE0HSJH+qEo/chXoZfc/8V6WV33W5azWALeuJeguTmTJjN+\n" + "mAukFJcZAT/87Y5m3oP37EAgxEmy2Q3NmYK5T92qYVAEnOKHwrTbl5sra6QvqCNc\n" + "JEgyXQGVR/KGdIFm709aQZNz+0ZuImXunksZlq+/997Qj+KMhyKzIr4=\n" + "-----END CERTIFICATE-----");
        write(out, new ErrorResultExternal(), null, true);
    }

    /**
	 * Writes an {@code OutOperationExternal} entry with no values specified
	 * except for an invalid certificate (invalid format) into the bus. The SM
	 * is expected to take the entry and write a {@link ErrorResultExternal}
	 * entry.
	 *
	 * @throws Exception if any error occurs.
	 */
    @Test
    public void testEmptyOutWithCertificateThatHasInvalidFormat() throws Exception {
        OutOperationExternal out = new OutOperationExternal();
        out.securityInfo = new SecurityInfo("INVALID CERTIFICATE");
        write(out, new ErrorResultExternal(), null, true);
    }

    /**
	 * Writes a valid {@code OutOperationExternal} entry that contains a triple
	 * to the system bus. The SM is expected to return a {@code OutDMEntry}.
	 * TODO: no rules specified for SOME_SPACE. should this work?
	 *
	 * @throws Exception if any error occurs.
	 */
    @Test
    public void testSimpleOut() throws Exception {
        OutOperationExternal outOperationExternal = new OutOperationExternal();
        Set<TripleEntry> triples = new HashSet<TripleEntry>();
        triples.add(new TripleEntry(new URIImpl("http://www.w3.org/People/EM/contact#me"), new URIImpl("http://www.w3.org/2000/10/swap/pim/contact#fullName"), new LiteralImpl("John Smith")));
        outOperationExternal.data = triples;
        outOperationExternal.space = Util.toURI(SOME_SPACE);
        outOperationExternal.transactionID = null;
        outOperationExternal.operationID = 1L;
        outOperationExternal.timestamp = System.currentTimeMillis();
        outOperationExternal.securityInfo = new SecurityInfo(CERTIFICATE_HANS);
        write(outOperationExternal, new OutDMEntry(), null, true);
    }

    /**
	 * Writes a valid {@code RdOperationExternal} entry to the system bus.
	 * The SM is expected to return a {@code RdDMEntry}.
	 *
	 * @throws Exception if any error occurs.
	 */
    @Test
    public void testSimpleRead() throws Exception {
        RdOperationExternal read = new RdOperationExternal();
        read.query = new Template("SELECT * FROM ?subject ?predicate ?object.");
        read.kind = ReadType.READ;
        read.space = Util.toURI(SOME_SPACE);
        read.securityInfo = new SecurityInfo(CERTIFICATE_HANS);
        write(read, new RdDMEntry(), null, true);
    }

    /**
	 * Writes a {@code RdOperationExternal} entry with a certificate that has an invalid format to the system bus.
	 * The SM is expected to return an {@code ErrorResultExternal}.
	 *
	 * @throws Exception if any error occurs.
	 */
    @Test
    public void testSimpleReadWithCertifcateThatHasInvalidFormat() throws Exception {
        RdOperationExternal read = new RdOperationExternal();
        read.query = new Template("SELECT * FROM ?subject ?predicate ?object.");
        read.kind = ReadType.READ;
        read.space = Util.toURI(SOME_SPACE);
        read.securityInfo = new SecurityInfo(CERTIFICATE_HANS);
        write(read, new RdDMEntry(), null, true);
    }

    /**
	 * Writes several entries that should be ignored by the SM (--&gt; the SM
	 * must not take these entries) to the system bus. The test fails if the SM
	 * takes any of these entries.
	 *
	 * @throws Exception if any error occurs.
	 */
    @Test
    public void testEntriesNotTaken() throws Exception {
        Set<Entry> entries = new HashSet<Entry>();
        entries.add(new CommitTransaction());
        entries.add(new CreateTransaction());
        entries.add(new DataResultExternal());
        entries.add(new DistMetaMEntry());
        entries.add(new EndTransaction());
        entries.add(new EndTransactionResult(1L, true));
        entries.add(new ErrorResultExternal());
        entries.add(new ExceptionEntry());
        entries.add(new GetTransaction());
        entries.add(new InternalDataTSAdapterEntry());
        entries.add(new ManagementDataResultExternal());
        entries.add(new MetaMQueryEntry());
        entries.add(new MetaMResultEntry());
        entries.add(new MgmtMetaMEntry());
        entries.add(new OutDMEntry());
        entries.add(new OutMetaMEntry());
        entries.add(new RdDMEntry());
        entries.add(new RdMetaMEntry());
        entries.add(new RdQPEntry());
        entries.add(new RdSCEntry());
        entries.add(new RdTSAdapterEntry());
        entries.add(new RdWithoutSpaceDMEntry());
        entries.add(new Result());
        entries.add(new RollbackTransaction());
        entries.add(new SetCookieEntry());
        entries.add(new SMTSAdapterResultEntry());
        entries.add(new SMTSAdapterQueryEntry());
        entries.add(new TMRequestEntry());
        entries.add(new TransactionEnded());
        entries.add(new TransactionEntry());
        entries.add(new TSAdapterEntry());
        for (Entry entry : entries) {
            getSpace().write(entry, null, Lease.FOREVER);
        }
        for (Entry entry : entries) {
            Entry takenEntry = getSpace().take(entry, null, DEFAULT_TIMEOUT);
            assertNotNull("Couldn't take " + entry.getClass().getSimpleName() + "!", takenEntry);
        }
    }
}
