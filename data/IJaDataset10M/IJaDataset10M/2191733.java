package org.jcvi.glk.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.junit.Test;
import org.jcvi.glk.SangerPositions;
import org.jcvi.glk.encoders.DefaultPositionsEncoder;
import org.jcvi.glk.encoders.IllegalEncodedValueException;
import org.jcvi.glk.encoders.PositionsEncoder;
import static junit.framework.Assert.*;
import static org.easymock.EasyMock.*;

public class TestPositionsUserType extends AbstractTestImmutableUserType {

    PositionsUserType sut = new PositionsUserType();

    PositionsEncoder encoder = new DefaultPositionsEncoder();

    short[] positions = new short[] { 10, 20, 30, 40, 50 };

    @Override
    protected UserType getUserType() {
        return sut;
    }

    @Test
    public void returnedClass() {
        assertEquals(SangerPositions.class, sut.returnedClass());
    }

    @Test
    public void sqlTypes() {
        int[] actualTypes = sut.sqlTypes();
        assertEquals(1, actualTypes.length);
        assertEquals(Hibernate.STRING.sqlType(), actualTypes[0]);
    }

    @Test
    public void getNullShouldReturnNull() throws SQLException {
        ResultSet mockResultSet = createMock(ResultSet.class);
        expect(mockResultSet.getString(NAMES[0])).andReturn(null);
        expect(mockResultSet.wasNull()).andReturn(true);
        replay(mockResultSet);
        assertNull(sut.nullSafeGet(mockResultSet, NAMES, this));
        verify(mockResultSet);
    }

    @Test
    public void get() throws SQLException {
        String encodedPositions = encoder.encode(positions);
        ResultSet mockResultSet = createMock(ResultSet.class);
        expect(mockResultSet.getString(NAMES[0])).andReturn(encodedPositions);
        expect(mockResultSet.wasNull()).andReturn(false);
        replay(mockResultSet);
        SangerPositions actualPositions = (SangerPositions) sut.nullSafeGet(mockResultSet, NAMES, this);
        assertTrue(Arrays.equals(positions, actualPositions.getPositions()));
        verify(mockResultSet);
    }

    @Test
    public void getInvalidPositionsShouldThrowHibernateException() throws SQLException {
        String invalidPositions = "invalidPositions";
        ResultSet mockResultSet = createMock(ResultSet.class);
        expect(mockResultSet.getString(NAMES[0])).andReturn(invalidPositions);
        expect(mockResultSet.wasNull()).andReturn(false);
        replay(mockResultSet);
        try {
            sut.nullSafeGet(mockResultSet, NAMES, this);
            fail("should wrap IllegalEncodedValueException in HibernateException");
        } catch (HibernateException expected) {
            assertEquals("Could not decode encoded positions", expected.getMessage());
            assertTrue(IllegalEncodedValueException.class.isAssignableFrom(expected.getCause().getClass()));
        }
        verify(mockResultSet);
    }

    @Test
    public void setNull() throws SQLException {
        PreparedStatement mockPreparedStatement = createMock(PreparedStatement.class);
        mockPreparedStatement.setNull(index, Hibernate.STRING.sqlType());
        replay(mockPreparedStatement);
        sut.nullSafeSet(mockPreparedStatement, null, index);
        verify(mockPreparedStatement);
    }

    @Test
    public void set() throws SQLException {
        PreparedStatement mockPreparedStatement = createMock(PreparedStatement.class);
        mockPreparedStatement.setString(index, encoder.encode(positions));
        replay(mockPreparedStatement);
        sut.nullSafeSet(mockPreparedStatement, new SangerPositions(positions), index);
        verify(mockPreparedStatement);
    }
}
