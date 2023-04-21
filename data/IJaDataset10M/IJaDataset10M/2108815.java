package jdbchelper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * Author: Erdinc YILMAZEL
 * Date: Dec 30, 2008
 * Time: 4:24:25 PM
 */
public abstract class IteratorBatchFeeder<T> implements BatchFeeder {

    Iterator<T> iterator;

    public IteratorBatchFeeder(Iterator<T> i) {
        iterator = i;
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public boolean feedStatement(PreparedStatement stmt) throws SQLException {
        feedStatement(stmt, iterator.next());
        return true;
    }

    public abstract void feedStatement(PreparedStatement stmt, T object) throws SQLException;
}
