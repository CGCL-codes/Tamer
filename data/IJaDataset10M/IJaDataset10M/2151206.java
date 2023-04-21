package org.jcvi.glk.hibernate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.HibernateException;
import org.jcvi.glk.SangerTrash;
import org.junit.Test;
import static junit.framework.Assert.*;
import static org.easymock.EasyMock.*;

public abstract class TestAbstractTrashUserTypeNotTrash {

    protected abstract AbstractTrashUserType getTrashUserType();

    protected abstract void expectSetNotTrash(PreparedStatement mockPreparedStatement, int index) throws SQLException;

    private static final String COLUMN_NAME = "firstName";

    private static final String[] NAMES = { COLUMN_NAME };

    @Test
    public void getNotTrash() throws HibernateException, SQLException {
        ResultSet mockResultSet = createMock(ResultSet.class);
        Character notTrashCode = getTrashUserType().getNotTrashCode();
        String trashCodeAsString = notTrashCode == null ? null : notTrashCode.toString();
        expect(mockResultSet.getString(COLUMN_NAME)).andReturn(trashCodeAsString);
        replay(mockResultSet);
        assertEquals(SangerTrash.NONE, getTrashUserType().nullSafeGet(mockResultSet, NAMES, this));
        verify(mockResultSet);
    }

    @Test
    public void setNotTrash() throws HibernateException, SQLException {
        int index = 7;
        PreparedStatement mockPreparedStatement = createMock(PreparedStatement.class);
        expectSetNotTrash(mockPreparedStatement, index);
        replay(mockPreparedStatement);
        getTrashUserType().nullSafeSet(mockPreparedStatement, SangerTrash.NONE, index);
        verify(mockPreparedStatement);
    }

    @Test
    public void setNullTrash() throws HibernateException, SQLException {
        int index = 7;
        PreparedStatement mockPreparedStatement = createMock(PreparedStatement.class);
        expectSetNotTrash(mockPreparedStatement, index);
        replay(mockPreparedStatement);
        getTrashUserType().nullSafeSet(mockPreparedStatement, null, index);
        verify(mockPreparedStatement);
    }
}
