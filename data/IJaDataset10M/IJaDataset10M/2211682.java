package net.sourceforge.nconfigurations;

import net.sourceforge.nconfigurations.ImmNodeTreeBuilder.KeyValueAssoc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator implementation to ease the creation of configuration objects
 * using the {@link ImmNodeTreeBuilder#buildTree(String, KeyFactory, java.util.Iterator)}
 * method out of a result set obtained from a SQL query.
 *
 * <p>An instance of this iterator is given a result set over which it will
 * iterate until it hits the end of this set. For each row in the result set it
 * will call the {@link RowMapper#map(java.sql.ResultSet)} method on an given
 * instance of {@link RowMapper}. This row mapper is responsible to extract
 * values from the current row of the supplied result set and convert it into
 * a {@link ImmNodeTreeBuilder.KeyValueAssoc} object.
 * In case the mapper throws a runtime exception the iterator does
 * not make any attempt to recover from it &ndash; typically, this will abort
 * the construction of the configuration object for which this iterator is used.
 * However, the iterator catches {@link java.sql.SQLException}s and retrows them
 * as {@link net.sourceforge.nconfigurations.BuilderRuntimeException}s.
 *
 * <p>Note that {@link ImmNodeTreeBuilder#buildTree(String, KeyFactory, java.util.Iterator)}
 * does not catch the {@link BuilderRuntimeException} which is typically
 * used to throw a checked exception (e.g. {@code ConvertException}) through the
 * the iterator inteface (that does not allow for checked exception).
 *
 * <p>The following example demonstrates the typical usage of this class and the
 * mapper interface:
 * <pre>{@code
 * public class FooConfigurationBuilder implements ConfigurationBuilder {
 *   ...
 *   public Configuration buildConfiguration (String name) throws BuilderException {
 *       try {
 *           ...
 *           ResultSet rset = stmt.executeQuery ();
 *           return ConfigurationTreeBuilder.buildTree (name,
 *               new ResultSetIterator (rset, new RowMapper<KeyValueAssoc> () {
 *                   public ResourceRecord map (ResultSet rset) throws SQLException {
 *                      String key = rset.getString (1);
 *                      String type = rset.getString (2);
 *                      String value = rset.getString (3);
 *                      try {
 *                          ...
 *                          // ~ try to convert the given tripplet to a resource record
 *                          // or return null in case the tripplet should be ignored
 *                          // (removed from the built configuration object)
 *                          ...
 *                          return new KeyValueAssoc (key, convertedValue);
 *                      } catch (ConvertException e) {
 *                          // ~ translate the checked exception to an unchecked one
 *                          throw new BuilderRuntimeException ("Failed to convert value for key: " + key, e);
 *                      }
 *                   }
 *               });
 *       } catch (BuilderRuntimeException e) {
 *           // ~ translate the runtime exception back to a checked one
 *           throw new BuilderException (e.getMessage(), e.getCause());
 *       } catch (...) {
 *           ...
 *       } finally {
 *           ... // ~ release allocated resources
 *       }
 *   }
 * }
 * }</pre>
 *
 * @author Petr Novotník
 * @since 1.0
 */
class ResultSetIterator implements Iterator<KeyValueAssoc> {

    /**
     * A functor interface to map a single row from result set to an object.
     * 
     * @param <E> the type of objects this mapper will be producing 
     */
    static interface RowMapper<E> {

        /**
         * Called repeatedly for every row in a result set to convert the
         * "current" row into an object. It is important to keep in mind
         * that implementations must not move the cursor within the given
         * result set, just retrieve values from the current row.
         *
         * @param rset the result set positioned at the "current" row
         * 
         * @return an object created out of the given row
         *
         * @throws SQLException in case of problems quering the given result set
         */
        E map(final ResultSet rset) throws SQLException;
    }

    /**
     * Creates a new result set iterator specifying the mapper to apply for
     * each row in the result set.
     *
     * @param rset             the result set to be iterated over
     * @param mapper           the mapper to apply to each row in the given
     *                         result set
     *
     * @throws NullPointerException     if any of the parameters is {@code null}
     *
     * @see net.sourceforge.nconfigurations.ResultSetIterator
     */
    ResultSetIterator(final ResultSet rset, final RowMapper<KeyValueAssoc> mapper) {
        if (rset == null || mapper == null) {
            throw new NullPointerException();
        }
        this.rset = rset;
        this.mapper = mapper;
    }

    public boolean hasNext() {
        if (determinedNext) {
            return determinedNextValue;
        } else {
            determinedNext = true;
            try {
                determinedNextValue = rset.next();
            } catch (final SQLException e) {
                throw new BuilderRuntimeException("Failed to determine #hasNext()!", e);
            }
            return determinedNextValue;
        }
    }

    public ImmNodeTreeBuilder.KeyValueAssoc next() {
        if (determinedNext) {
            if (determinedNextValue) {
                determinedNextValue = determinedNext = false;
                try {
                    return mapper.map(rset);
                } catch (final SQLException e) {
                    throw new BuilderRuntimeException("Failed to obtain values from result-set in #next()!", e);
                }
            } else {
                throw new NoSuchElementException();
            }
        } else {
            if (hasNext()) {
                return next();
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    private boolean determinedNextValue;

    private boolean determinedNext;

    private final ResultSet rset;

    private final RowMapper<KeyValueAssoc> mapper;
}
