package org.vramework.jdbc.map;

import java.sql.ResultSet;
import java.util.List;

/**
 * Translates {@link ResultSet} rows to objects an vv. <br />
 * The algorithm of looking up and generating mappings is described in {@link IMapping}.
 * 
 * @author thomas.mahringer
 * @param <T>
 *          The translated type.
 * @see IMapping
 */
public interface IRowTranslator<T> {

    /**
   * Translates the current row of the {@link ResultSet} to an object of our type.
   * 
   * @param rs
   * @param singleTarget
   *          See {@link #rowToObject(ResultSet, Object, Object, IClassRegistry)}.
   * @param classRegistry 
   * @return The translated object.
   */
    T rowToObject(ResultSet rs, Object singleTarget, IClassRegistry classRegistry);

    /**
   * Translates the current row of the {@link ResultSet} into the passed target object.
   * 
   * @param rs
   * @param target
   * @param singleTargetToSet
   *          JdbcMap loads 1:n relationships very efficiently: It sets the backlink on the "n" side during the
   *          row-translation. <br />
   *          To do so, it generates a translator for each single target association. If <code>singleTargetToSet</code>
   *          is set, the translator will call the according setter for each translated object, e.g. a
   *          "Contract.setCustomer(Customer). <br />
   *          This means: Neither reflection is needed for setting the "n" side, nor an iteration over all loaded
   *          objects. <br />
   *          For a detailed description of translators per single target association, see also
   *          {@link Operation#getRowTranslators()}.
   * @param classRegistry 
   * @return The same object passed in as target (return value == target). It is now filled with new values from the
   *         {@link ResultSet}.
   * @see Operation#getRowTranslators()
   */
    T rowToObject(ResultSet rs, T target, Object singleTargetToSet, IClassRegistry classRegistry);

    /**
   * Translates an object's properties to a list of list of bind values. Each list of bind values will be added to the
   * respective SQL statement. (Remember that an operation of a mapping can have have more than one SQL statement).
   * 
   * @param object
   * @return The translated properties.
   */
    List<List<Object>> objectToBind(T object);
}
