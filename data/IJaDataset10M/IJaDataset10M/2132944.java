package redora.junit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import redora.exceptions.ObjectNotFoundException;
import redora.exceptions.RedoraException;
import redora.test.rdo.model.JUnitMaster;
import redora.test.rdo.service.JUnitMasterService;
import redora.test.rdo.service.ServiceFactory;
import static redora.api.fetch.Scope.Table;
import static redora.junit.AbstractDBTest.*;
import static redora.util.JUnitUtil.assertRedoraPersist;

/**
 * InsertDeleteTest will check if insert and delete works OK.
 * 
 * @author Nanjing RedOrange (www.red-orange.cn)
 */
public class InsertDeleteTest {

    static JUnitMasterService jUnitMasterService;

    @BeforeClass
    public static void makeTables() throws RedoraException {
        makeTestTables();
        jUnitMasterService = ServiceFactory.jUnitMasterService();
    }

    @AfterClass
    public static void dropTables() throws RedoraException {
        dropTestTables();
        jUnitMasterService.close();
    }

    @Test
    public void insert() throws Exception {
        JUnitMaster original = new JUnitMaster();
        original.fillMe();
        assertRedoraPersist(jUnitMasterService.persist(original));
        assertNotNull("Insert failed", jUnitMasterService.findById(original.getId(), Table));
        JUnitMaster duplicate = jUnitMasterService.findById(original.getId(), Table);
        assertEquals("FindById did not retrieve a similar Master object: id", original.getId(), duplicate.getId());
        assertEquals("FindById did not retrieve a similar Master object: notnull", original.getNotnull(), duplicate.getNotnull());
        assertEquals("FindById did not retrieve a similar Master object: iinteger", original.getIinteger(), duplicate.getIinteger());
        assertEquals("FindById did not retrieve a similar Master object: ddate", original.getDdate(), duplicate.getDdate());
        assertEquals("FindById did not retrieve a similar Master object: CreationDate", original.getCreationDate(), duplicate.getCreationDate());
        assertEquals("FindById did not retrieve a similar Master object: bboolean", original.getBboolean(), duplicate.getBboolean());
        assertEquals("FindById did not retrieve a similar Master object: llong", original.getLlong(), duplicate.getLlong());
        assertEquals("FindById did not retrieve a similar Master object: ddouble", original.getDdouble(), duplicate.getDdouble());
        assertEquals("FindById did not retrieve a similar Master object: enumm", original.getEnumm(), duplicate.getEnumm());
    }

    /**
     * Similar to testInsert, but it will invoke a streaming (PreparedStatement)
     * insert by using a large input string.
     */
    @Test
    public void insertStream() throws Exception {
        JUnitMaster original = new JUnitMaster();
        original.fillMe();
        StringBuilder mmm = new StringBuilder("mmm");
        for (int p = 0; p < 30000; p++) {
            mmm.append("mmm");
        }
        original.setLarge(mmm.toString());
        assertRedoraPersist(jUnitMasterService.persist(original));
        assertNotNull("Insert failed", jUnitMasterService.findById(original.getId(), Table));
        JUnitMaster duplicate = jUnitMasterService.findById(original.getId(), Table);
        assertEquals("FindById did not retrieve a similar Master object: id", original.getId(), duplicate.getId());
        assertEquals("FindById did not retrieve a similar Master object: notnull", original.getNotnull(), duplicate.getNotnull());
        assertEquals("FindById did not retrieve a similar Master object: iinteger", original.getIinteger(), duplicate.getIinteger());
        assertEquals("FindById did not retrieve a similar Master object: ddate", original.getDdate(), duplicate.getDdate());
        assertEquals("FindById did not retrieve a similar Master object: CreationDate", original.getCreationDate(), duplicate.getCreationDate());
        assertEquals("FindById did not retrieve a similar Master object: bboolean", original.getBboolean(), duplicate.getBboolean());
        assertEquals("FindById did not retrieve a similar Master object: llong", original.getLlong(), duplicate.getLlong());
        assertEquals("FindById did not retrieve a similar Master object: ddouble", original.getDdouble(), duplicate.getDdouble());
        assertEquals("FindById did not retrieve a similar Master object: enumm", original.getEnumm(), duplicate.getEnumm());
        assertEquals("FindById did not retrieve a similar Master object: large", original.getLarge(), duplicate.getLarge());
    }

    @Test
    public void delete() throws RedoraException {
        JUnitMaster master = new JUnitMaster();
        master.avoidNull();
        assertRedoraPersist(jUnitMasterService.persist(master));
        assertRedoraPersist(jUnitMasterService.delete(master));
        try {
            jUnitMasterService.findById(master.getId(), Table);
            fail("ObjectNotFoundException expected");
        } catch (ObjectNotFoundException e) {
        }
    }
}
