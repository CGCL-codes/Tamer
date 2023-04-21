package org.hip.kernel.bom.impl.test;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.bom.GeneralDomainObject;
import org.hip.kernel.bom.QueryResult;
import org.hip.kernel.bom.impl.XMLSerializer;
import org.hip.kernel.exc.VException;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Benno Luthiger
 * Created on Dec 24, 2003
 */
public class NestedJoinTest {

    private static DataHouseKeeper data;

    @BeforeClass
    public static void init() {
        data = DataHouseKeeper.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        data.deleteAllFromGroup();
        data.deleteAllFromParticipant();
    }

    @Test
    public void testNested() throws VException, SQLException {
        DomainObject lGroup = data.getGroupHome().create();
        lGroup.set(TestGroupHomeImpl.KEY_NAME, "test1");
        lGroup.set(TestGroupHomeImpl.KEY_DESCRIPTION, "Test group 1 for testing nested queries.");
        lGroup.insert(true);
        BigDecimal lGroupID1 = data.getGroupHome().getMax(TestGroupHomeImpl.KEY_ID);
        lGroup.setVirgin();
        lGroup.set(TestGroupHomeImpl.KEY_NAME, "test2");
        lGroup.set(TestGroupHomeImpl.KEY_DESCRIPTION, "Test group 2 for testing nested queries.");
        lGroup.insert(true);
        BigDecimal lGroupID2 = data.getGroupHome().getMax(TestGroupHomeImpl.KEY_ID);
        DomainObject lParticipant = data.getParticipantHome().create();
        for (int i = 1; i < 7; i++) {
            lParticipant.set(TestParticipantHomeImpl.KEY_GROUPID, lGroupID1);
            lParticipant.set(TestParticipantHomeImpl.KEY_MEMBERID, new Integer(i));
            lParticipant.insert(true);
            lParticipant.setVirgin();
        }
        for (int i = 1; i < 10; i++) {
            lParticipant.set(TestParticipantHomeImpl.KEY_GROUPID, lGroupID2);
            lParticipant.set(TestParticipantHomeImpl.KEY_MEMBERID, new Integer(10 + i));
            lParticipant.insert(true);
            lParticipant.setVirgin();
        }
        XMLSerializer lSerializer = new XMLSerializer();
        QueryResult lResult = data.getNestedDomainObjectHomeImpl().select();
        int lCount = 0;
        int lRegistered = 0;
        while (lResult.hasMoreElements()) {
            GeneralDomainObject lDomainObject = lResult.nextAsDomainObject();
            lRegistered += ((BigDecimal) lDomainObject.get(TestNestedDomainObjectHomeImpl.KEY_REGISTERED)).intValue();
            lDomainObject.accept(lSerializer);
            lCount++;
        }
        assertEquals("number of rows", 2, lCount);
        assertEquals("number of registered", 15, lRegistered);
        System.out.println(lSerializer.toString());
    }
}
