package com.gorillalogic.dal;

import com.gorillalogic.dal.common.PathStrategyFactory;
import java.util.Hashtable;

/**
 * <code>PathStrategy</code> is argument to and defines the format of the string
 * returned by <code>Table.path()</code>. <code>PathStrategy</code>s are
 * aggregations of flags describing how paths should be constructed. They are
 * accessed in one of several available base forms, and can be modified
 * incrementally as in the following:
 *
 *   PathStrategy strategy = PathStrategy.pkg;
 *   PathStrategy strategy = PathStrategy.pkg.parenthesize(true);
 *   PathStrategy strategy = PathStrategy.pkg.parenthesize(true).readable(false);
 *
 * PathStrategies are pre-created and efficient to access.
 *
 * There are important 2nd-level properties of paths that can be achieved
 * in a variety of ways and are not therefore provided as direct operations
 * on <code>PathStrategy</code>. Callers should invoke the correct set of
 * operations to ensure that a given strategy performs as desired for these
 * properties.
 *
 * One 2nd-level property is that of <b>idempotency</b>. A strategy is 
 * idempotent iff:
 * 
 *    assert Table.factory.gclToTable(table.path(strategy),CONTEXT).equals(table)
 *
 * (assuming no side effect operations change the interpretation of the resulting
 * path). Idempotency is <i>always</i> relative to the supplied CONTEXT which is 
 * another <code>Table</code>. If CONTEXT is null it is taken as the current system.
 *
 * The related concept of <b>key</b> achieves idempotency based on unique values of
 * specially declared fields. 'Keyness' is an aspect of a particular path while
 * idempotency is an aspect of a PathStrategy. Idempotency in PathStrategies is
 * orthogonal to the presence or absence of keys. Keys achieve the effect of
 * idempotency independently of which PathStrategy is used.
 *
 * Another 2nd-level property is that <b>parsable</b>. A strategy is parseable 
 * iff:
 *
 *    Table t= Table.factory.gclToTable(table.path(strategy),CONTEXT);
 *
 * Does not generate an exception. Non-parsable paths exist for readability only. 
 * A path which is not parsable is obviously never idempotent.
 *
 * @author <a href="mailto:Brendan@Gosh">U-GOSH\Brendan</a>
 * @version 1.0
 */
public interface PathStrategy {

    /**
	 * Provides a readable summarization of all settings in this strategy.
	 *
	 * @return a summary <code>String</code>
	 */
    String describe();

    /**
	 * Provides a Java expression that will replicate the current PathStrategy.
	 *
	 * @return a Java expression
	 */
    String inJava();

    /**
	 * Return a single-row <code>Table</code> encapsulating the values
	 * in this strategy
	 *
	 * @return a <code>Table</code> of settings
	 */
    Table pathTable();

    /**
	 * A readable path is intended for human consumption. A non-readable
	 * path is terse, takes up less space, and may be faster to parse
	 * (especially if it is also not persistent).
	 *
	 * Readability is orthogonal to idempotency and parsability.
	 *
	 * @return true iff readable flag is set in this <code>PathStrategy</code>
	 */
    boolean readable();

    /**
	 * Return a PathStrategy with the specified readable setting.
	 *
	 * @param readableElseTerse sets readable flag in result (default=true)
	 * @return a <code>PathStrategy</code> reflecting the updated value
	 */
    PathStrategy readable(boolean readableElseTerse);

    /**
	 * Parentheses in a path demarcate all operators so that the parsing
	 * structure is explicit. This can be aid to understanding how complex 
	 * expressions are parsed.
	 *
	 * Parentheses are orthogonal to idempotency and parsability.
	 *
	 * @return true iff parenthesize flag is set in this <code>PathStrategy</code>
	 */
    boolean parenthesize();

    /**
	 * Return a PathStrategy with the specified parenthesize setting.
	 *
	 * @param parenthesizeElseNormal sets parenthesize flag in result (default=false)
	 * @return a <code>PathStrategy</code> reflecting the updated value
	 */
    PathStrategy parenthesize(boolean parenthesizeElseNormal);

    /**
	 * Sub-expressions in square brackets may be elided (i.e. take the form
	 * of "...") for readability.
	 *
	 * An elided strategy is neither parsable nor idempotent.
	 *
	 * @return true iff query flag is set in this <code>PathStrategy</code>
	 */
    boolean query();

    /**
	 * Return a PathStrategy with the specified query setting.
	 *
	 * @param exposeElseElide sets elide flag in result (default=true)
	 * @return a <code>PathStrategy</code> reflecting the updated value
	 */
    PathStrategy query(boolean exposeElseElide);

    /**
	 * Registered paths always start from a table that is registered.
	 * From a given Origin, the path will be expanded as required in
	 * order to meet this criteria. If the first component of a path
	 * is itself registered, that the length of the path is not affected.
	 *
	 * A registed strategy is idempotent relative to the currently
	 * running process, but is transient and should not be used across
	 * process spaces. Use persistent for the latter.
	 *
	 * If persistent is false on this strategy (a common setting), the 
	 * path will start with a table index, using the GCL '!' operator, 
	 * e.g. "!n" where n is the table index.
	 *
	 * @return true iff registered flag is set in this <code>PathStrategy</code>
	 */
    boolean registered();

    /**
	 * Return a PathStrategy with the specified registered setting.
	 *
	 * @param registeredElseNot sets registered flag in result (default=false)
	 * @return a <code>PathStrategy</code> reflecting the updated value
	 */
    PathStrategy registered(boolean registeredElseNot);

    /**
	 * Persistent paths always start from a table that is persistent.
	 * From a given Origin, the path will be expanded as required in
	 * order to meet this criteria. If the first component of a path
	 * is itself persistent, that the length of the path is not affected.
	 *
	 * Persistent paths do not use references to rowIds or internal
	 * system tables that are transient. As a result, persistent paths 
	 * may be used across process spaces.
	 *
	 * A persistent strategy is idempotent relative to the currently
	 * system. 
	 *
	 * @return true iff persitent flag is set in this <code>PathStrategy</code>
	 */
    boolean persistent();

    /**
	 * Return a PathStrategy with the specified persistent setting.
	 *
	 * @param persistentElseTransient sets persistent flag in result (default=false)
	 * @return a <code>PathStrategy</code> reflecting the updated value
	 */
    PathStrategy persistent(boolean persistentElseTransient);

    /**
	 * The origin of a strategy determines where the path will be unique
	 * relative to to. For example, if the origin is "world" (see list of
	 * defined origins below), then the path will be unique within its
	 * world but <i>will not</i> include its world -- i.e. it will start
	 * with a '/'.
	 *
	 * The origin in turn is the assumed the CONTEXT for idempotency, i.e.
	 * the second arg to Table.factory.gclToTable.
	 *
	 * @return The {@link #Origin} of this <code>PathStrategy</code>
	 */
    Origin origin();

    /**
	 * Increase or decrease the relative path detail as specified
	 * in the origin.
	 *
	 * @param moreElseLess iff true, the origin is farther away (more 
	 * detail), iff false, the origin is 'closer' (less detail)
	 * @return a <code>PathStrategy</code> reflecting the updated value
	 */
    PathStrategy origin(boolean moreElseLess);

    /**
	 * Explicitly set the origin.
	 *
	 * @param origin the strategy <code>Origin</code>
	 * @return a <code>PathStrategy</code> reflecting the updated value
	 */
    PathStrategy origin(Origin origin);

    /**
	 * Origin defines the context within which the path is unique.
	 *
	 */
    public interface Origin {

        String describe();

        public static final Origin name = PathStrategyFactory.nameOrigin();

        public static final Origin rowOwner = PathStrategyFactory.rowOwnerOrigin();

        public static final Origin pkg = PathStrategyFactory.pkgOrigin();

        public static final Origin world = PathStrategyFactory.worldOrigin();

        public static final Origin universe = PathStrategyFactory.universeOrigin();

        public static final Origin primordial = PathStrategyFactory.primordialOrigin();
    }

    /**
	 * Base strategies define various origins, each providing successively larger
	 * scopes and hence longer paths. Each of these has default values for all the
	 * other settings.
	 * 
	 * <code>name</code> strategy includes only a table name.
	 *
	 */
    public static final PathStrategy name = PathStrategyFactory.make(Origin.name);

    /**
	 * <code>rowOwner</code> strategy starts from the most immediate single row
	 * owning a link table which is also part of the path. This is useful in situations
	 * where the link table itself must be preserved so that operations can apply to it,
	 * but the rest of the path is not needed. It can also reduce the likelihood, but
	 * not exclude entirely, the deletion problem involving circular paths.
	 *
	 */
    public static final PathStrategy rowOwner = PathStrategyFactory.make(Origin.rowOwner);

    /**
	 * <code>pkg</code> strategy starts from the owning package.
	 *
	 */
    public static final PathStrategy pkg = PathStrategyFactory.make(Origin.pkg);

    /**
	 * <code>world</code> strategy starts from the owning world.
	 *
	 */
    public static final PathStrategy world = PathStrategyFactory.make(Origin.world);

    /**
	 * <code>universe</code> strategy starts from the owning universe.
	 *
	 */
    public static final PathStrategy universe = PathStrategyFactory.make(Origin.universe);

    /**
	 * <code>primordial</code> strategies starts the primordial meta model.
	 *
	 */
    public static final PathStrategy primordial = PathStrategyFactory.make(Origin.primordial);

    /**
	 * Other common strategies are also defined. 
	 *
	 * <code>defaultStrategy</code> is used by <code>Table</code>s when no strategy is 
	 * explicitly specified. It is simply a synonym for <code>world</code>.
	 *
	 */
    public static final PathStrategy defaultStrategy = world;

    /**
	 * <code>encoded</code> is a non-readable form preserving the full context relative
	 * the table's world.
	 *
	 */
    public static final PathStrategy encoded = PathStrategy.pkg.registered(true).readable(false);

    /**
	 * <code>parsed</code> provides displays world scope with parentheses.
	 *
	 */
    public static final PathStrategy parsed = PathStrategy.world.readable(true).parenthesize(true);

    /**
	 * <code>linked</code> excludes the full context of encoded, but preserves the 
	 * outermost link table.
	 *
	 */
    public static final PathStrategy linked = PathStrategy.rowOwner.registered(true).readable(false);

    /**
	 * Map of standard strategies.
	 *
	 */
    public static final Hashtable standard = new Initializer().add("name", name).add("rowOwner", rowOwner).add("pkg", pkg).add("world", world).add("universe", universe).add("primordial", primordial).add("encoded", encoded).add("parsed", parsed).add("linked", linked).end();

    public static class Initializer {

        Hashtable hashTable = new Hashtable();

        Initializer add(String nm, PathStrategy ps) {
            hashTable.put(nm, ps);
            return this;
        }

        Hashtable end() {
            return hashTable;
        }
    }
}
