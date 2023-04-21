package net.bull.javamelody;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.naming.NamingException;
import org.junit.Before;
import org.junit.Test;

public class TestPdfOtherReport {

    private static final String TEST_APP = "test app";

    /** Check. */
    @Before
    public void setUp() {
        Utils.initialize();
    }

    /** Test.
	 * @throws IOException e */
    @Test
    public void testWriteHeapHistogram() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final InputStream input = getClass().getResourceAsStream("/heaphisto.txt");
        try {
            final PdfOtherReport pdfOtherReport = new PdfOtherReport(TEST_APP, output);
            final HeapHistogram heapHistogram = new HeapHistogram(input, false);
            pdfOtherReport.writeHeapHistogram(heapHistogram);
            pdfOtherReport.close();
        } finally {
            input.close();
        }
        assertNotEmptyAndClear(output);
        final InputStream input2 = getClass().getResourceAsStream("/heaphisto_jrockit.txt");
        try {
            final PdfOtherReport pdfOtherReport = new PdfOtherReport(TEST_APP, output);
            final HeapHistogram heapHistogram = new HeapHistogram(input2, true);
            pdfOtherReport.writeHeapHistogram(heapHistogram);
            pdfOtherReport.close();
        } finally {
            input2.close();
        }
        assertNotEmptyAndClear(output);
    }

    /** Test.
	 * @throws IOException e */
    @Test
    public void testWriteSessionInformations() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final List<SessionInformations> sessions = new ArrayList<SessionInformations>();
        sessions.add(new SessionInformations(new SessionTestImpl(true), false));
        sessions.add(new SessionInformations(new SessionTestImpl(false), false));
        final SessionTestImpl serializableButNotSession = new SessionTestImpl(true);
        serializableButNotSession.setAttribute("serializable but not", Collections.singleton(new Object()));
        sessions.add(new SessionInformations(serializableButNotSession, false));
        PdfOtherReport pdfOtherReport = new PdfOtherReport(TEST_APP, output);
        pdfOtherReport.writeSessionInformations(Collections.<SessionInformations>emptyList());
        assertNotEmptyAndClear(output);
        pdfOtherReport = new PdfOtherReport(TEST_APP, output);
        pdfOtherReport.writeSessionInformations(sessions);
        assertNotEmptyAndClear(output);
        pdfOtherReport = new PdfOtherReport(TEST_APP, output);
        pdfOtherReport.writeSessionInformations(Collections.singletonList(new SessionInformations(new SessionTestImpl(false), false)));
        assertNotEmptyAndClear(output);
        final SessionTestImpl sessionPays = new SessionTestImpl(true);
        sessionPays.setCountry("nimporte.quoi");
        pdfOtherReport = new PdfOtherReport(TEST_APP, output);
        pdfOtherReport.writeSessionInformations(Collections.singletonList(new SessionInformations(sessionPays, false)));
        assertNotEmptyAndClear(output);
        sessionPays.setCountry(null);
        assertNull("countryDisplay null", new SessionInformations(sessionPays, false).getCountryDisplay());
        pdfOtherReport = new PdfOtherReport(TEST_APP, output);
        pdfOtherReport.writeSessionInformations(Collections.singletonList(new SessionInformations(sessionPays, false)));
        assertNotEmptyAndClear(output);
    }

    /** Test.
	 * @throws IOException e */
    @Test
    public void testWriteProcessInformations() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        PdfOtherReport pdfOtherReport = new PdfOtherReport(TEST_APP, output);
        pdfOtherReport.writeProcessInformations(ProcessInformations.buildProcessInformations(getClass().getResourceAsStream("/tasklist.txt"), true));
        assertNotEmptyAndClear(output);
        pdfOtherReport = new PdfOtherReport(TEST_APP, output);
        pdfOtherReport.writeProcessInformations(ProcessInformations.buildProcessInformations(getClass().getResourceAsStream("/ps.txt"), false));
        assertNotEmptyAndClear(output);
        pdfOtherReport = new PdfOtherReport(TEST_APP, output);
        pdfOtherReport.writeProcessInformations(Collections.singletonMap("localhost", ProcessInformations.buildProcessInformations(getClass().getResourceAsStream("/ps.txt"), false)));
        assertNotEmptyAndClear(output);
    }

    /** Test.
	 * @throws IOException e
	 * @throws NamingException e
	 * @throws SQLException e */
    @Test
    public void testWriteDatabaseInformations() throws IOException, SQLException, NamingException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        TestDatabaseInformations.initJdbcDriverParameters();
        PdfOtherReport pdfOtherReport = new PdfOtherReport(TEST_APP, output);
        pdfOtherReport.writeDatabaseInformations(new DatabaseInformations(0));
        assertNotEmptyAndClear(output);
        pdfOtherReport = new PdfOtherReport(TEST_APP, output);
        pdfOtherReport.writeDatabaseInformations(new DatabaseInformations(3));
        assertNotEmptyAndClear(output);
    }

    /** Test.
	 * @throws IOException e */
    @Test
    public void testWriteMBeans() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final PdfOtherReport pdfOtherReport = new PdfOtherReport(TEST_APP, output);
        pdfOtherReport.writeMBeans();
        assertNotEmptyAndClear(output);
    }

    /** Test.
	 * @throws IOException e */
    @Test
    public void testWriteRuntimeDependencies() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final Counter sqlCounter = new Counter("sql", null);
        final Counter counter = new Counter("services", null, sqlCounter);
        counter.bindContextIncludingCpu("BeanA.test");
        counter.bindContextIncludingCpu("BeanA.test2");
        counter.bindContextIncludingCpu("BeanB.test");
        counter.addRequestForCurrentContext(false);
        counter.bindContextIncludingCpu("BeanB.test2");
        counter.addRequestForCurrentContext(false);
        counter.addRequestForCurrentContext(false);
        counter.addRequestForCurrentContext(false);
        counter.bindContextIncludingCpu("test");
        counter.bindContextIncludingCpu("BeanA.test");
        counter.addRequestForCurrentContext(false);
        counter.addRequestForCurrentContext(false);
        counter.bindContextIncludingCpu("test2");
        sqlCounter.bindContextIncludingCpu("sql");
        sqlCounter.addRequestForCurrentContext(false);
        counter.addRequestForCurrentContext(false);
        final PdfOtherReport pdfOtherReport = new PdfOtherReport(TEST_APP, output);
        pdfOtherReport.writeRuntimeDependencies(counter, Period.TOUT.getRange());
        assertNotEmptyAndClear(output);
    }

    /** Test.
	 * @throws IOException e */
    @Test
    public void testWriteCounterSummaryPerClass() throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final PdfOtherReport pdfOtherReport = new PdfOtherReport(TEST_APP, output);
        final Counter counter = new Counter("services", null);
        final Collector collector = new Collector(TEST_APP, Arrays.asList(counter));
        pdfOtherReport.writeCounterSummaryPerClass(collector, counter, null, Period.TOUT.getRange());
        assertNotEmptyAndClear(output);
    }

    private void assertNotEmptyAndClear(ByteArrayOutputStream output) {
        assertTrue("rapport vide", output.size() > 0);
        output.reset();
    }
}
