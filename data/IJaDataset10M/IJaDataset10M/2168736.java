package idv.takeshi.test.hbase;

import static org.junit.Assert.assertTrue;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.MiniHBaseCluster;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.util.GenericOptionsParser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class HBaseMiniClusterTest extends AbstractHBaseMiniClusterTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        AbstractHBaseMiniClusterTest.setUpBeforeClass();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        AbstractHBaseMiniClusterTest.tearDownAfterClass();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testImportCompanyInfo() throws Exception {
        final String TABLE_NAME = "COMPANY_INFO";
        final String[] FAMILIES = { "DEPARTMENT", "EMPLOYEE" };
        final String[] QUALIFIERS = { "DEP_ID", "DEP_NAME", "EMP_ID", "EMP_NAME", "DEP_ID" };
        final String INPUT_FILE = "companyInfo.esv";
        final String INPUT_FILE_PATH = "idv/takeshi/test/hbase/" + INPUT_FILE;
        String[] args = new String[] { "-Dimporttsv.columns" + "=HBASE_ROW_KEY,DEPARTMENT:DEP_ID,DEPARTMENT:DEP_NAME,EMPLOYEE:EMP_ID,EMPLOYEE:EMP_NAME,EMPLOYEE:DEP_ID", "-Dimporttsv.separator=|", TABLE_NAME, INPUT_FILE };
        MiniHBaseCluster cluster = TEST_UTIL.getHbaseCluster();
        GenericOptionsParser opts = new GenericOptionsParser(cluster.getConfiguration(), args);
        Configuration conf = opts.getConfiguration();
        args = opts.getRemainingArgs();
        try {
            final byte[][] FAMS = transfer2BytesArray(FAMILIES);
            final byte[] TAB = Bytes.toBytes(TABLE_NAME);
            final byte[][] QS = transfer2BytesArray(QUALIFIERS);
            createTable(conf, TAB, FAMS);
            importLocalFile2Table(conf, args, INPUT_FILE, INPUT_FILE_PATH);
            HTable table = new HTable(new Configuration(conf), TAB);
            boolean verified = false;
            long pause = conf.getLong("hbase.client.pause", 5 * 1000);
            int numRetries = conf.getInt("hbase.client.retries.number", 5);
            for (int i = 0; i < numRetries; i++) {
                try {
                    Scan scan = new Scan();
                    ResultScanner resScanner = table.getScanner(scan);
                    for (Result res : resScanner) {
                        verified = true;
                        System.out.println(Bytes.toString(res.getRow()));
                        System.out.println(Bytes.toString(res.getValue(FAMS[0], QS[0])));
                        System.out.println(Bytes.toString(res.getValue(FAMS[0], QS[1])));
                        System.out.println(Bytes.toString(res.getValue(FAMS[1], QS[2])));
                        System.out.println(Bytes.toString(res.getValue(FAMS[1], QS[3])));
                        System.out.println(Bytes.toString(res.getValue(FAMS[1], QS[4])));
                    }
                    break;
                } catch (NullPointerException e) {
                }
                try {
                    Thread.sleep(pause);
                } catch (InterruptedException e) {
                }
            }
            assertTrue(verified);
        } finally {
            cluster.shutdown();
        }
    }
}
