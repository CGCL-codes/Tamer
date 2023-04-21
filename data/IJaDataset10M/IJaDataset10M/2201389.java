package org.horus.miniframewrk.mock;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import org.horus.miniframewrk.DataAccessObject;
import org.horus.miniframewrk.Pagination;

public class MockDAO<T> implements DataAccessObject<T, BigDecimal> {

    private ArrayList<T> objects;

    public MockDAO(String name) {
        objects = new ArrayList<T>();
    }

    public List<T> list() throws SQLException {
        return objects;
    }

    public void insert(T object) throws SQLException {
        objects.add(object);
    }

    public void update(T object) throws SQLException {
    }

    public T get(BigDecimal key) throws SQLException {
        return objects.get(key.intValue());
    }

    public void delete(BigDecimal key) throws SQLException {
        objects.remove(key.intValue());
    }

    public BigDecimal nextId() throws SQLException {
        return new BigDecimal(objects.size());
    }

    public List<T> list(Pagination page) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
