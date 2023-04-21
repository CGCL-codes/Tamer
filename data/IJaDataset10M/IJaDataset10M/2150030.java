package net.jadoth.sqlengine.internal;

import static net.jadoth.sqlengine.SQL.Punctuation.NEW_LINE;
import static net.jadoth.sqlengine.SQL.Punctuation.comma_;
import java.util.HashMap;
import java.util.Iterator;
import net.jadoth.sqlengine.SQL;

/**
 * The Class SET.
 * 
 * @author Thomas Muenz
 */
public class SET extends SqlClause<ColumnValueAssignment> {

    /** The values. */
    private final Iterable<ColumnValueAssignment> assignments;

    /**
	 * Instantiates a new sets the.
	 */
    public SET(final Iterable<ColumnValueAssignment> assignments) {
        super();
        this.bodyElementSeperator = comma_ + NEW_LINE;
        this.keyWordSeperator = NEW_LINE;
        this.assignments = assignments;
        this.indentFirstBodyElement = true;
    }

    protected SET(final SET copySource) {
        super(copySource);
        this.assignments = copySource.assignments;
    }

    /**
	 * 
	 */
    public Iterable<ColumnValueAssignment> getAssignments() {
        return this.assignments;
    }

    /**
	 * @return
	 * @see net.jadoth.sqlengine.internal.SqlClause#keyword()
	 */
    @Override
    public String keyword() {
        return SQL.LANG.SET;
    }

    /**
	 * @param singleLineMode
	 * @return
	 * @see net.jadoth.sqlengine.internal.SqlClause#getBodyElementSeperator(boolean)
	 */
    @Override
    public String getBodyElementSeperator(final boolean singleLineMode) {
        return singleLineMode ? comma_ : this.bodyElementSeperator;
    }

    /**
	 * @return
	 */
    @Override
    public Iterator<ColumnValueAssignment> iterator() {
        return this.assignments.iterator();
    }

    /**
	 * @return
	 */
    @Override
    public SET copy() {
        return new SET(this);
    }

    /**
	 * @param alreadyCopied
	 * @return
	 */
    @Override
    protected SET copy(final HashMap<SqlClause<?>, SqlClause<?>> alreadyCopied) {
        return new SET(this);
    }
}
