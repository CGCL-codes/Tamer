package org.apache.ibatis.submitted.blobtest;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

public class BlobTest {

    private static SqlSessionFactory sqlSessionFactory;

    @BeforeClass
    public static void initDatabase() throws Exception {
        Connection conn = null;
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            conn = DriverManager.getConnection("jdbc:hsqldb:mem:blobtest", "sa", "");
            Reader reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/blobtest/CreateDB.sql");
            ScriptRunner runner = new ScriptRunner(conn);
            runner.setLogWriter(null);
            runner.setErrorLogWriter(null);
            runner.runScript(reader);
            conn.commit();
            reader.close();
            reader = Resources.getResourceAsReader("org/apache/ibatis/submitted/blobtest/MapperConfig.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            reader.close();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Test
    public void testInsertBlobThenSelectAll() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            BlobMapper blobMapper = sqlSession.getMapper(BlobMapper.class);
            byte[] myblob = new byte[] { 1, 2, 3, 4, 5 };
            BlobRecord blobRecord = new BlobRecord(1, myblob);
            int rows = blobMapper.insert(blobRecord);
            assertEquals(1, rows);
            List<BlobRecord> results = blobMapper.selectAll();
            assertEquals(1, results.size());
            BlobRecord result = results.get(0);
            assertEquals(blobRecord.getId(), result.getId());
            assertTrue(blobsAreEqual(blobRecord.getBlob(), result.getBlob()));
        } finally {
            sqlSession.close();
        }
    }

    public static boolean blobsAreEqual(byte[] blob1, byte[] blob2) {
        if (blob1 == null) {
            return blob2 == null;
        }
        if (blob2 == null) {
            return blob1 == null;
        }
        boolean rc = blob1.length == blob2.length;
        if (rc) {
            for (int i = 0; i < blob1.length; i++) {
                if (blob1[i] != blob2[i]) {
                    rc = false;
                    break;
                }
            }
        }
        return rc;
    }
}
