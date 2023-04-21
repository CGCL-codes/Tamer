package org.hip.vif.bom.impl.test;

import java.math.BigDecimal;
import java.sql.SQLException;
import junit.framework.TestCase;
import org.hip.kernel.bom.BOMException;
import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.bom.DomainObjectHome;
import org.hip.kernel.bom.JoinedDomainObjectHome;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.OrderObject;
import org.hip.kernel.bom.QueryResult;
import org.hip.kernel.bom.SettingException;
import org.hip.kernel.bom.KeyObject.BinaryBooleanOperator;
import org.hip.kernel.bom.impl.DBAdapterSelector;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.kernel.bom.impl.OrderObjectImpl;
import org.hip.kernel.exc.VException;
import org.hip.kernel.sys.VSys;
import org.hip.kernel.util.VInvalidNameException;
import org.hip.kernel.util.VInvalidValueException;

/**
 * ExampleTest.java
 * 
 * Created on 27.09.2002
 * @author Benno Luthiger
 */
public class ExampleTest extends TestCase {

    /**
	 * Constructor for ExampleTest.
	 * @param arg0
	 */
    public ExampleTest(String arg0) {
        super(arg0);
    }

    public void testInsert() {
        try {
            DomainObject lMember = ((DomainObjectHome) VSys.homeManager.getHome("org.hip.vif.bom.impl.MemberHomeImpl")).create();
            lMember.set("UserID", "myUserID");
            lMember.set("Name", "myName");
            lMember.set("Firstname", "myFirstName");
            lMember.set("Street", "myStreet");
            lMember.set("ZIP", "myZIP");
            lMember.set("City", "myCity");
            lMember.set("Tel", "myTel");
            lMember.set("Fax", "myFax");
            lMember.set("Mail", "myMail");
            lMember.set("Sex", new Integer(1));
            lMember.set("Language", "myLanguage");
            lMember.set("Password", "myPassword");
        } catch (BOMException exc) {
        } catch (SettingException exc) {
        }
    }

    public void testUpdate() {
        try {
            KeyObject lKey = new KeyObjectImpl();
            lKey.setValue("UserID", "myUserID");
            DomainObject lMember = ((DomainObjectHome) VSys.homeManager.getHome("org.hip.vif.bom.impl.MemberHomeImpl")).findByKey(lKey);
            lMember.set("Mail", "newMail");
            lMember.update(true);
        } catch (BOMException exc) {
        } catch (SettingException exc) {
        } catch (VInvalidNameException exc) {
        } catch (VInvalidValueException exc) {
        } catch (SQLException exc) {
        }
    }

    public void testDelete() {
        try {
            KeyObject lKey = new KeyObjectImpl();
            lKey.setValue("UserID", "myUserID");
            ((DomainObjectHome) VSys.homeManager.getHome("org.hip.vif.bom.impl.MemberHomeImpl")).findByKey(lKey);
        } catch (BOMException exc) {
        } catch (VInvalidNameException exc) {
        } catch (VInvalidValueException exc) {
        }
    }

    public void testSelect() throws BOMException, SQLException, VException {
        DomainObjectHome lHome = ((DomainObjectHome) VSys.homeManager.getHome("org.hip.vif.bom.impl.MemberHomeImpl"));
        lHome.select();
        KeyObject lKey1 = new KeyObjectImpl();
        lKey1.setValue("Sex", new Integer(1));
        lHome.select(lKey1);
        OrderObject lOrder = new OrderObjectImpl();
        lOrder.setValue("Name", 1);
        lOrder.setValue("Firstname", 2);
        lHome.select(lKey1, lOrder);
        KeyObject lKey2 = new KeyObjectImpl();
        lKey2.setValue("Name", "A%", "LIKE");
        lHome.select(lKey2);
        KeyObject lKey3 = new KeyObjectImpl();
        lKey3.setValue("Name", "A%", "LIKE");
        lKey3.setValue("Name", "a%", "LIKE", BinaryBooleanOperator.OR);
        lHome.select(lKey3);
        KeyObject lKey4 = new KeyObjectImpl();
        lKey4.setValue("Name", "A%", "LIKE");
        lKey4.setValue("Name", "a%", "LIKE", BinaryBooleanOperator.OR);
        KeyObject lKey5 = new KeyObjectImpl();
        lKey5.setValue("Sex", new Integer(1));
        lKey5.setValue(lKey4);
        lHome.select(lKey5);
        KeyObject lKey = new KeyObjectImpl();
        lKey.setValue("Name", "A%", "LIKE", BinaryBooleanOperator.AND, DBAdapterSelector.getInstance().getColumnModifierUCase());
        QueryResult lEntries = lHome.select(lKey);
        assertNotNull(lEntries);
    }

    public void testJoin() throws VException, SQLException {
        JoinedDomainObjectHome lHome = ((JoinedDomainObjectHome) VSys.homeManager.getHome("org.hip.vif.bom.impl.JoinMemberToRoleHome"));
        KeyObject lKey = new KeyObjectImpl();
        lKey.setValue("MemberID", new BigDecimal(217));
        lHome.select(lKey);
    }
}
