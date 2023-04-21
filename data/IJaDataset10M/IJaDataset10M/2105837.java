package redora.junit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import redora.exceptions.RedoraException;
import redora.test.rdo.model.JUnitChild;
import redora.test.rdo.model.JUnitMaster;
import redora.test.rdo.service.*;
import redora.util.JSONWriter;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import static redora.api.fetch.Scope.Form;
import static redora.junit.AbstractDBTest.*;
import static redora.util.JSONCopyTest.DIFFICULT;
import static redora.util.JSONCopyTest.REF_DIFFICULT;
import static redora.util.JUnitUtil.assertRedoraPersist;

/**
 * JSON is generated directly from JDBC bypassing the object layer. This test
 * will check if all works well....
 * @author Nanjing RedOrange (www.red-orange.cn)
 */
public class JSONTest {

    static JUnitMasterService jUnitMasterService;

    static JUnitMasterServiceJSON jUnitMasterServiceJSON;

    static JUnitChildService jUnitChildService;

    static JUnitChildServiceJSON jUnitChildServiceJSON;

    static String MASTER_FILLED = "{response:{status:0,data:{id:REPLACE_ID," + "_scope:0,sstring:null,finder:null,rregexp:null," + "notnull:\"" + REF_DIFFICULT + "\"," + "lazy:null,ddefault:\"i am here\",ccaption:null,medium:null," + "large:null,ddate:\"" + JUnitMaster.DDATE + "\"," + "ddatetime:\"" + JUnitMaster.DDATETIME + "\"," + "iinteger:8,integerNotNull:923221,llong:12,bboolean:true," + "booleanNotNull:false,ddouble:2.232,enumm:\"Three\",antisamyNotLazy:null," + "antisamyLazySlashdot:null," + "creationDate:\"REPLACE_CD\"," + "updateDate:null}}}";

    static String MASTER_NULL = "{response:{status:0,data:{id:REPLACE_ID," + "_scope:0,sstring:null,finder:null,rregexp:null," + "notnull:\"I am filled\",lazy:null,ddefault:\"i am here\"," + "ccaption:null,medium:null,large:null,ddate:null," + "ddatetime:null,iinteger:null,integerNotNull:923221," + "llong:23,bboolean:null,booleanNotNull:false,ddouble:null," + "enumm:null,antisamyNotLazy:null,antisamyLazySlashdot:null," + "creationDate:\"REPLACE_CD\"," + "updateDate:null}}}";

    static String MASTER_NULL_AND_DEFAULT = MASTER_NULL.replace("iinteger:null", "iinteger:22").replace("bboolean:null", "bboolean:false").replace("enumm:null", "enumm:\"One\"").replace("ddouble:null", "ddouble:2.2");

    static String CHILD = "{response:{status:0,data:{id:REPLACE_ID,_scope:0,name:\"object\",jUnitMasterId:MASTER_ID,sex:null,creationDate:\"REPLACE_CD\",updateDate:null}}}";

    @BeforeClass
    public static void makeTables() throws RedoraException {
        makeTestTables();
        jUnitMasterService = ServiceFactory.jUnitMasterService();
        jUnitMasterServiceJSON = ServiceFactory.jUnitMasterServiceJSON();
        jUnitChildService = ServiceFactory.jUnitChildService();
        jUnitChildServiceJSON = ServiceFactory.jUnitChildServiceJSON();
    }

    @AfterClass
    public static void dropTables() throws RedoraException {
        dropTestTables();
        jUnitMasterService.close();
        jUnitMasterServiceJSON.close();
        jUnitChildService.close();
        jUnitChildServiceJSON.close();
    }

    /**
     * Persist a JUnitMaster with all the attribute types filled with a value.
     * Retrieve the object as JSON stream and copy it to a JUnitMaster object
     * using the copy util. This object should be the same as the object you
     * persisted in the beginning.
     * @throws Exception passing on.
     */
    @Test
    public void attributes() throws Exception {
        JUnitMaster master = new JUnitMaster();
        master.fillMe();
        master.setNotnull(DIFFICULT);
        assertRedoraPersist(jUnitMasterService.persist(master));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JSONWriter p = new JSONWriter(new PrintWriter(baos));
        jUnitMasterServiceJSON.findById(master.getId(), Form, p);
        p.flush();
        assertEquals("Find check Form", MASTER_FILLED.replace("REPLACE_ID", master.getId().toString()).replace("REPLACE_CD", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(master.getCreationDate())), baos.toString());
    }

    @Test
    public void lazy() throws Exception {
        JUnitMaster master = new JUnitMaster();
        master.fillMe();
        master.setMedium("Lazy stuff");
        master.setLazy("Lazy stuff");
        master.setLarge("Lazy stuff");
        master.setAntisamyLazySlashdot("Lazy stuff");
        assertRedoraPersist(jUnitMasterService.persist(master));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JSONWriter p = new JSONWriter(new PrintWriter(baos));
        jUnitMasterServiceJSON.fetchLazy(master.getId(), p);
        p.flush();
        assertEquals("Lazy check", "{response:{status:0,data:{id:ID,_scope:3,lazy:\"Lazy stuff\",medium:\"Lazy stuff\",large:\"Lazy stuff\",antisamyLazySlashdot:\"Lazy stuff\"}}}".replace("ID", master.getId().toString()), baos.toString());
    }

    /**
     * Persist a JUnitMaster with null values for every attribute type. Retrieve
     * it as JSON from the database and check if the persisted object streams to
     * valid JSON.
     * @throws Exception passing on.
     */
    @Test
    public void nullAttributes() throws Exception {
        JUnitMaster master = new JUnitMaster();
        master.avoidNull();
        master.setIinteger(null);
        master.setBboolean(null);
        master.setEnumm(null);
        master.setDdouble(null);
        assertRedoraPersist(jUnitMasterService.persist(master));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JSONWriter p = new JSONWriter(new PrintWriter(baos));
        jUnitMasterServiceJSON.findById(master.getId(), Form, p);
        p.flush();
        assertEquals(MASTER_NULL.replace("REPLACE_ID", master.getId().toString()).replace("REPLACE_CD", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(master.getCreationDate())), baos.toString());
    }

    static String HTML = "<p>ABC</p><p><img src=\"http://img04.taobaocdn.com/imgextra/i4/53702955/T2zg4BXexaXXXXXXXX_!!53702955.jpg\" /></p>\n<p><img src=\"http://img02.taobaocdn.com/imgextra/i2/53702955/T2ghXBXa0aXXXXXXXX_!!53702955.jpg\" /></p>";

    static String REF_HTML = "<p>ABC</p>\\n<p><img src=\\\"http://img04.taobaocdn.com/imgextra/i4/53702955/T2zg4BXexaXXXXXXXX_!!53702955.jpg\\\" /></p>\\n\\n<p><img src=\\\"http://img02.taobaocdn.com/imgextra/i2/53702955/T2ghXBXa0aXXXXXXXX_!!53702955.jpg\\\" /></p>";

    /**
      * Persist a JUnitMaster with filled with HTML. Retrieve
      * it as JSON from the database and check if the persisted object streams to
      * valid JSON.
      * @throws Exception passing on.
      */
    @Test
    public void html() throws Exception {
        JUnitMaster master = new JUnitMaster();
        master.avoidNull();
        master.setAntisamyNotLazy(HTML);
        assertRedoraPersist(jUnitMasterService.persist(master));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JSONWriter p = new JSONWriter(new PrintWriter(baos));
        jUnitMasterServiceJSON.findById(master.getId(), Form, p);
        p.flush();
        assertEquals(MASTER_NULL_AND_DEFAULT.replace("REPLACE_ID", master.getId().toString()).replace("REPLACE_CD", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(master.getCreationDate())).replace("antisamyNotLazy:null", "antisamyNotLazy:\"" + REF_HTML + "\""), baos.toString());
    }

    /**
     * Persist a JUnitMaster and related JUnitChild. Retrieve the child as JSON stream
     * and check if it streams to valid JSON.
     *
     * @throws Exception passing on.
     */
    @Test
    public void object() throws Exception {
        JUnitMaster master = new JUnitMaster();
        master.fillMe();
        assertRedoraPersist(jUnitMasterService.persist(master));
        JUnitChild child = new JUnitChild();
        child.setName("object");
        child.setJUnitMaster(master);
        assertRedoraPersist(jUnitChildService.persist(child));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        JSONWriter p = new JSONWriter(new PrintWriter(baos));
        jUnitChildServiceJSON.findById(child.getId(), Form, p);
        p.flush();
        assertEquals(baos.toString(), CHILD.replace("REPLACE_ID", child.getId().toString()).replace("MASTER_ID", master.getId().toString()).replace("REPLACE_CD", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(child.getCreationDate())));
    }
}
