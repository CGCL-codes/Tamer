package org.nakedobjects.persistence.hibernate.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.easymock.MockControl;
import org.hibernate.Hibernate;
import org.nakedobjects.application.value.PhoneNumber;

public class PhoneNumberTest extends TypesTestCase {

    public void testNullSafeGetNotNull() throws Exception {
        String value = "abc";
        MockControl control = MockControl.createControl(ResultSet.class);
        ResultSet rs = (ResultSet) control.getMock();
        control.expectAndReturn(rs.getString(names[0]), value);
        control.expectAndReturn(rs.wasNull(), false);
        control.replay();
        PhoneNumberType type = new PhoneNumberType();
        PhoneNumber returned = (PhoneNumber) type.nullSafeGet(rs, names, null);
        assertEquals("phoneNumber", value, returned.toString());
        control.verify();
    }

    public void testNullSafeGetIsNull() throws Exception {
        MockControl control = MockControl.createControl(ResultSet.class);
        ResultSet rs = (ResultSet) control.getMock();
        control.expectAndReturn(rs.getString(names[0]), null);
        control.expectAndReturn(rs.wasNull(), true);
        control.replay();
        PhoneNumberType type = new PhoneNumberType();
        assertNull(type.nullSafeGet(rs, names, null));
        control.verify();
    }

    public void testNullSafeSetNotNull() throws Exception {
        String value = "abc";
        PhoneNumber phoneNumber = new PhoneNumber(value);
        MockControl control = MockControl.createControl(PreparedStatement.class);
        PreparedStatement ps = (PreparedStatement) control.getMock();
        ps.setString(1, value);
        control.replay();
        PhoneNumberType type = new PhoneNumberType();
        type.nullSafeSet(ps, phoneNumber, 1);
        control.verify();
    }

    public void testNullSafeSetIsNull() throws Exception {
        MockControl control = MockControl.createControl(PreparedStatement.class);
        PreparedStatement ps = (PreparedStatement) control.getMock();
        ps.setNull(1, Hibernate.STRING.sqlType());
        control.replay();
        PhoneNumberType type = new PhoneNumberType();
        type.nullSafeSet(ps, null, 1);
        control.verify();
    }

    public void testBasics() {
        PhoneNumberType type = new PhoneNumberType();
        super.basicTest(type);
        assertEquals("returned class", org.nakedobjects.application.value.PhoneNumber.class, type.returnedClass());
    }
}
