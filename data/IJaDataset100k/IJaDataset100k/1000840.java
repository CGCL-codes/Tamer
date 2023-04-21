package edu.nctu.csie.jichang.database.command;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import edu.nctu.csie.jichang.database.dbinfo.DataBaseInfo;
import edu.nctu.csie.jichang.database.dbinfo.LoginInfo;
import edu.nctu.csie.jichang.database.model.builder.ISQLBuilder;
import edu.nctu.csie.jichang.database.model.cell.DBDatabase;
import edu.nctu.csie.jichang.database.model.connection.AbstractDBConnection;
import edu.nctu.csie.jichang.database.model.connection.IDBConnection;
import edu.nctu.csie.jichang.database.parser.IParser;
import edu.nctu.csie.jichang.database.parser.ParseDDL;
import edu.nctu.csie.jichang.database.util.Configuration;
import edu.nctu.csie.jichang.database.util.FileUtil;
import edu.nctu.csie.jichang.database.util.StringUtil;

public class ScriptRunTest {

    private static final Logger LOG = LoggerFactory.getLogger(ScriptRunTest.class);

    private static ApplicationContext context = null;

    private static LoginInfo baseLoginInfo = null;

    private static LoginInfo currentLoginInfo = null;

    private static boolean MSSQL = true;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        context = new ClassPathXmlApplicationContext("dbtoolstest.xml");
        DataBaseInfo tDataBaseInfo = context.getBean("datasoure", DataBaseInfo.class);
        List<LoginInfo> tLoginInfos = tDataBaseInfo.getLoginInfos();
        baseLoginInfo = tLoginInfos.get(0);
        currentLoginInfo = tLoginInfos.get(1);
        MSSQL = baseLoginInfo.getDatabaseInfo().getType() == DataBaseInfo.TYPE_SQLSERVER;
    }

    @Test
    @Ignore
    public void testGenerateScript() throws Exception {
        Configuration.getInstance().setIgnoreAlterReference(false);
        LOG.info("Start testGenerateScript ");
        String tPath = "src/test/resources/mssql/case2/case2_14.sql";
        compareFile(currentLoginInfo, baseLoginInfo, null, new File(tPath));
    }

    @Test
    public void testCrossCompare() throws Exception {
        Configuration.getInstance().setIgnoreAlterReference(false);
        LOG.info("testCrossCompare");
        String tPath = null;
        if (MSSQL) {
            tPath = "src/test/resources/mssql/";
        } else {
            tPath = "src/test/resources/postgresql/";
        }
        File tFile = new File(tPath);
        for (File f : tFile.listFiles()) {
            if (f.isDirectory()) {
                File[] tFiles = f.listFiles();
                if (tFiles.length > 0) {
                    for (File c : tFiles) {
                        if (c.getName().toUpperCase().endsWith(".SQL")) {
                            compareFile(currentLoginInfo, baseLoginInfo, null, c);
                            for (File b : tFiles) {
                                if (b.getName().toUpperCase().endsWith(".SQL")) {
                                    compareFile(currentLoginInfo, baseLoginInfo, c, b);
                                } else {
                                    if (StringUtil.isNotEmptyOrSpace(b.getAbsolutePath().trim())) {
                                        LOG.error("File Error = " + b.getAbsolutePath());
                                    }
                                }
                            }
                        } else {
                            if (StringUtil.isNotEmptyOrSpace(c.getAbsolutePath().trim())) {
                                LOG.error("File Error = " + c.getAbsolutePath());
                            }
                        }
                    }
                }
            }
        }
    }

    private void compareFile(LoginInfo tCurrentLoginInfo, LoginInfo tBaseLoginInfo, File c, File b) {
        try {
            LOG.info("Start compare file " + (c != null ? c.getName() : "null") + " vs " + (b != null ? b.getName() : "null"));
            ISQLBuilder tBuilder = createDatabase(tCurrentLoginInfo, c);
            createDatabase(tBaseLoginInfo, b);
            String tSQL = getDiffSQL(tCurrentLoginInfo, tBaseLoginInfo, tBuilder);
            if (c == null) {
                FileUtil.writeStringToFile(tSQL, new File(b.getAbsoluteFile() + ".bak"));
            }
            if (b != c) {
                IDBConnection tCurrentConn = AbstractDBConnection.getInstance(tCurrentLoginInfo);
                tCurrentConn.executeBatchUpdate(tSQL);
                tCurrentConn.close();
                tSQL = getDiffSQL(tCurrentLoginInfo, tBaseLoginInfo, tBuilder);
            }
            LOG.info(tSQL);
            Assert.assertTrue("變更失敗", tSQL.indexOf("Without Change") > 0);
        } catch (Exception e) {
            LOG.error("", e);
            Assert.fail();
        }
    }

    private ISQLBuilder createDatabase(LoginInfo pLoginInfo, File pFile) throws IOException {
        IDBConnection tCurrentConn = AbstractDBConnection.getInstance(pLoginInfo);
        tCurrentConn.dropAndCreateDatabase();
        if (pFile != null) {
            tCurrentConn.executeBatchUpdate(FileUtil.readFileToString(pFile));
        }
        tCurrentConn.close();
        return tCurrentConn.getBuilder();
    }

    private String getDiffSQL(LoginInfo pCurrentLoginInfo, LoginInfo pBaseLoginInfo, ISQLBuilder pBuilder) throws Exception {
        IParser tParser = new ParseDDL();
        DBDatabase tCurrent = tParser.doParse(StringUtil.getListContent(pCurrentLoginInfo));
        tCurrent.setDatabaseName("ienc");
        DBDatabase tBase = tParser.doParse(StringUtil.getListContent(pBaseLoginInfo));
        tBase.setDatabaseName("base");
        ISQLCommand tCmd = new DiffStructureCommand();
        tCmd.setOperand(pBuilder, tCurrent, tBase);
        return tCmd.execute();
    }
}
