package com.googlecode.simpleret.test.importer;

import java.io.BufferedReader;
import java.io.StringReader;
import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.googlecode.simpleret.Utilities;
import com.googlecode.simpleret.importer.TraceFileReader2StoreVocabulary;
import com.googlecode.simpleret.test.All;
import com.googlecode.simpleret.test.DatabaseTestBase;
import com.googlecode.simpleret.test.TestUtilities;

public class TraceFileReader2StoreVocabularyTest extends DatabaseTestBase {

    private BufferedReader br;

    private IDatabaseTester db;

    @Before
    public void before() throws Exception {
        br = Utilities.getResurceReader("/files/test-trace-2.txt");
        db = TestUtilities.getDatabaseTester(All.CONFIGURATION);
        IDataSet ds = new FlatXmlDataSet(new StringReader("<dataset />"));
        db.setDataSet(ds);
        db.onSetup();
    }

    @After
    public void after() throws Exception {
        br.close();
        db.onTearDown();
    }

    @Test
    public void test() throws Exception {
        TraceFileReader2StoreVocabulary reader = new TraceFileReader2StoreVocabulary(15L);
        reader.startProcessing(br);
        IDataSet actualDS = db.getConnection().createDataSet();
        IDataSet expectedDS = new FlatXmlDataSet(new StringReader("<dataset>" + "<VY WORD=\"com.googlecode.simpleret.example.Alpha.start()\"/>" + "<VY WORD=\"com.googlecode.simpleret.example.Main.main()\"/>" + "</dataset>"));
        Assertion.assertEqualsIgnoreCols(expectedDS, actualDS, "VY", new String[] { "WORD_ID" });
    }
}
