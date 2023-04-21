package jaxlib.persistence;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import jaxlib.array.ObjectArrays;
import jaxlib.lang.Chars;
import jaxlib.lang.Objects;
import jaxlib.text.SimpleIntegerFormat;
import jaxlib.util.CheckArg;
import jaxlib.util.Strings;

/**
 * An {@code EjbQl} statement and parameters.
 * The common use of this class is to create a query outside of a session for later execution.
 *
 * @param <R>
 *  the type of query result rows.
 *
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: DetachedQuery.java 3036 2012-01-10 02:24:08Z joerg_wassmer $
 */
@SuppressWarnings("unchecked")
public class DetachedQuery<R> extends Object implements Serializable {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    private static String shiftPositionalParameters(final DetachedQuery query, final int delta) {
        final String s = query.getStatement();
        if ((query.getParameterCount() == 0) || (delta == 0)) return s;
        final int hi = s.length() - 1;
        final StringBuilder sb = new StringBuilder(hi + 1 + query.getParameterCount());
        long found = 0;
        for (int i = 0; i <= hi; i++) {
            final char c = s.charAt(i);
            if (c == '\'') {
                final int k = s.indexOf('\'', i + 1);
                if (k < 0) {
                    throw CheckArg.exception("query syntax error, missing closing quote \"'\" for opening at index %s:\n%s", i, s);
                }
                sb.append(s, i, k + 1);
                i = k;
            } else if ((c == '?') && (i < hi) && Chars.Ascii.isDigit(s.charAt(i + 1)) && ((i == 0) || !Character.isLetterOrDigit(s.charAt(i - 1)))) {
                int k = i + 1;
                while ((k < hi) && Chars.Ascii.isDigit(s.charAt(k + 1))) k++;
                final int n = SimpleIntegerFormat.DECIMAL.parseInt(s, i + 1, k + 1);
                if (n == 0) {
                    throw CheckArg.exception("query syntax error at %s, positional parameter can not be \"?0\":\n%s", i, query);
                }
                found |= (1L << n - 1);
                sb.append('?').append(n + delta);
                i = k;
            } else {
                sb.append(c);
            }
        }
        for (int i = query.getParameterCount(); --i >= 0; ) {
            if ((found & (1L << i)) == 0) {
                throw CheckArg.exception("query syntax error, missing positional parameter %s in statement:\n%s", i + 1, query);
            }
            found &= ~(1L << i);
        }
        if (found != 0) {
            throw new IllegalArgumentException("query syntax error, statement contains more positional parameters than the parameter list:\n" + query);
        }
        return sb.toString();
    }

    private static final QueryCustomizer[] NO_HINT = new QueryCustomizer[0];

    public static final DetachedQuery[] EMPTY_ARRAY = new DetachedQuery[0];

    private static Query applyHints(Query query, QueryCustomizer[] hints) {
        if (hints != null) {
            for (final QueryCustomizer hint : hints) {
                if (hint != null) {
                    query = hint.applyTo(query);
                    if (query == null) throw new RuntimeException("QueryCustomizer.applyTo(query) returned null: " + hint);
                }
            }
        }
        return query;
    }

    private final QueryCustomizer[] hints;

    @CheckForNull
    private final LockModeType lockMode;

    private final int maxResults;

    private final Object[] parameters;

    private final String[] parameterNames;

    private final String statement;

    private transient int hashCode;

    private transient QueryKind kind;

    public DetachedQuery(final String statement) {
        super();
        CheckArg.notBlank(statement, "statement");
        this.hints = null;
        this.lockMode = null;
        this.maxResults = Integer.MAX_VALUE;
        this.statement = statement;
        this.parameters = null;
        this.parameterNames = null;
    }

    public DetachedQuery(final String statement, final Object... parameters) {
        super();
        CheckArg.notBlank(statement, "statement");
        this.hints = null;
        this.lockMode = null;
        this.maxResults = Integer.MAX_VALUE;
        this.statement = statement;
        this.parameters = ((parameters == null) || (parameters.length == 0)) ? null : parameters;
        this.parameterNames = null;
    }

    public DetachedQuery(final QueryCustomizer defaultHint, final String statement) {
        CheckArg.notBlank(statement, "statement");
        this.hints = (defaultHint == null) ? null : new QueryCustomizer[] { defaultHint };
        this.lockMode = null;
        this.maxResults = Integer.MAX_VALUE;
        this.statement = statement;
        this.parameters = null;
        this.parameterNames = null;
    }

    public DetachedQuery(final QueryCustomizer defaultHint, final String statement, final Object... parameters) {
        this((defaultHint == null) ? null : new QueryCustomizer[] { defaultHint }, Integer.MAX_VALUE, (String[]) null, statement, parameters);
    }

    public DetachedQuery(final QueryCustomizer[] defaultHints, final String statement, final Object... parameters) {
        this(defaultHints, Integer.MAX_VALUE, (String[]) null, statement, parameters);
    }

    public DetachedQuery(final int maxResults, final String statement, final Object... parameters) {
        this((QueryCustomizer[]) null, maxResults, (String[]) null, statement, parameters);
    }

    public DetachedQuery(final QueryCustomizer defaultHint, final int maxResults, final String statement, final Object... parameters) {
        this((defaultHint == null) ? null : new QueryCustomizer[] { defaultHint }, maxResults, (String[]) null, statement, parameters);
    }

    public DetachedQuery(final QueryCustomizer[] defaultHints, final int maxResults, final String statement, final Object... parameters) {
        this(defaultHints, maxResults, (String[]) null, statement, parameters);
    }

    public DetachedQuery(final String[] parameterNames, final int maxResults, final String statement, final Object[] parameters) {
        this((QueryCustomizer[]) null, maxResults, parameterNames, statement, parameters);
    }

    public DetachedQuery(final QueryCustomizer[] defaultHints, final int maxResults, final String[] parameterNames, final String statement, final Object[] parameters) {
        super();
        CheckArg.notBlank(statement, "statement");
        CheckArg.ge(maxResults, -1, "maxResults");
        if (parameterNames != null) {
            CheckArg.equals(parameterNames.length, parameters.length, "parameterNames.length", "parameters.length");
        }
        this.hints = defaultHints;
        this.lockMode = null;
        this.maxResults = (maxResults < 0) ? Integer.MAX_VALUE : maxResults;
        this.statement = statement;
        this.parameters = ((parameters == null) || (parameters.length == 0)) ? null : parameters;
        this.parameterNames = ((parameterNames == null) || (parameterNames.length == 0)) ? null : parameterNames;
    }

    public DetachedQuery(final QuerySet querySet, final String queryName, final Object... parameters) {
        this(true, 0, querySet, queryName, parameters);
    }

    public DetachedQuery(final int maxResults, final QuerySet querySet, final String queryName, final Object... parameters) {
        this(false, maxResults, querySet, queryName, parameters);
    }

    private DetachedQuery(final boolean useMetaMaxResults, final int maxResults, final QuerySet querySet, final String queryName, final Object... parameters) {
        super();
        CheckArg.notNull(querySet, "querySet");
        CheckArg.notNull(queryName, "queryName");
        final QueryDef.Meta meta = querySet.getMeta(queryName);
        this.lockMode = meta.getLockMode();
        this.maxResults = useMetaMaxResults ? meta.getMaxResults() : (maxResults < 0) ? Integer.MAX_VALUE : maxResults;
        this.parameters = ((parameters == null) || (parameters.length == 0)) ? null : parameters;
        this.parameterNames = null;
        this.statement = meta.getStatement();
        int hintCount = 0;
        final QueryFlag f = QueryFlag.of(meta.isLazy(), meta.isCacheResult(), meta.isReadOnly());
        if (f != null) hintCount++;
        if (meta.getCacheMode() != EntityCacheMode.READ_WRITE) hintCount++;
        if (hintCount == 0) this.hints = null; else {
            this.hints = new QueryCustomizer[3];
            int i = 0;
            if (f != null) this.hints[i++] = f;
            if (meta.getCacheMode() != EntityCacheMode.READ_WRITE) this.hints[i++] = meta.getCacheMode();
        }
    }

    /**
   * Copy constructor, usually used only by subclasses.
   *
   * @since JaXLib 1.0
   */
    public DetachedQuery(@Nonnull final DetachedQuery<? extends R> src) {
        super();
        this.hashCode = src.hashCode;
        this.hints = src.hints;
        this.lockMode = src.lockMode;
        this.kind = src.kind;
        this.maxResults = src.maxResults;
        this.parameters = src.parameters;
        this.parameterNames = src.parameterNames;
        this.statement = src.statement;
    }

    /**
   * <b>Warning:</b> Currently this method may return buggy results if the specified query contains inline string
   * literals containing "?" followed by a number. This method is not yet supporting named parameters.
   *
   * @since JaXLib 1.0
   */
    public DetachedQuery<R> appendSubQuery(@Nonnull final DetachedQuery<?> subQuery) {
        return insertSubQuery(this.statement.length(), subQuery);
    }

    public final EntityQuery<R> attach(final EntityManager em) {
        return attach(em, (QueryCustomizer[]) null);
    }

    public final EntityQuery<R> attach(final EntityManager em, final QueryCustomizer hint) {
        return attach(em, new QueryCustomizer[] { hint });
    }

    public EntityQuery<R> attach(final EntityManager em, QueryCustomizer... hints) {
        final Query query = createQuery(em, 0, -1, hints);
        hints = null;
        if (query instanceof EntityQuery) return (EntityQuery) query;
        return new AttachedQuery<R>(this.statement, query);
    }

    public final Query createQuery(final EntityManager em) {
        return createQuery(em, 0, -1, (QueryCustomizer[]) null);
    }

    public final Query createQuery(final EntityManager em, final QueryCustomizer hint) {
        return createQuery(em, 0, -1, new QueryCustomizer[] { hint });
    }

    public final Query createQuery(final EntityManager em, final int maxResults, final QueryCustomizer hint) {
        return createQuery(em, 0, maxResults, new QueryCustomizer[] { hint });
    }

    public final Query createQuery(final EntityManager em, final int maxResults, final QueryCustomizer hint0, final QueryCustomizer hint1) {
        return createQuery(em, 0, maxResults, new QueryCustomizer[] { hint0, hint1 });
    }

    public Query createQuery(final EntityManager em, final int firstResult, int maxResults, final QueryCustomizer... hints) {
        final Query query = em.createQuery(this.statement);
        final int parameterCount = getParameterCount();
        for (int i = 1; i <= parameterCount; i++) {
            final String parameterName = getParameterName(i);
            final Object v = getParameter(i);
            try {
                if (parameterName == null) query.setParameter(i, v); else query.setParameter(parameterName, v);
            } catch (final IllegalArgumentException ex) {
                throw new IllegalArgumentException("Unable to create query, failed to set parameter:\n" + "\n  parameter index = " + i + "\n  parameter name  = " + parameterName + "\n  parameter value = " + v + "\n-------------------------------------------------------------------------------\n" + this.statement + "\n-------------------------------------------------------------------------------", ex);
            }
        }
        if (firstResult > 0) query.setFirstResult(firstResult);
        if ((maxResults < 0) || (maxResults > this.maxResults)) maxResults = this.maxResults;
        if ((maxResults >= 0) && (maxResults < Integer.MAX_VALUE)) query.setMaxResults(maxResults);
        if (this.lockMode != null) query.setLockMode(this.lockMode);
        return applyHints(applyHints(query, this.hints), hints);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof DetachedQuery)) return false;
        final DetachedQuery<?> b = (DetachedQuery) o;
        return (this.maxResults == b.maxResults) && this.statement.equals(b.statement) && Arrays.equals(this.parameters, b.parameters) && Arrays.equals(this.hints, b.hints) && Arrays.equals(this.parameterNames, b.parameterNames);
    }

    public final QueryCustomizer[] getHints() {
        return (this.hints == null) ? NO_HINT : this.hints.clone();
    }

    public final QueryKind getKind() {
        if (this.kind == null) this.kind = QueryKind.getKind(this.statement);
        return this.kind;
    }

    public final int getMaxResults() {
        return this.maxResults;
    }

    public final Object getParameter(final int index) {
        if (this.parameters != null) return this.parameters[index - 1]; else throw new ArrayIndexOutOfBoundsException();
    }

    public final int getParameterCount() {
        return (this.parameters == null) ? 0 : this.parameters.length;
    }

    public final String getParameterName(final int index) {
        return (this.parameterNames == null) ? null : this.parameterNames[index];
    }

    public final Object[] getParameters() {
        return (this.parameters == null) ? Objects.EMPTY_ARRAY : this.parameters;
    }

    public final String getStatement() {
        return this.statement;
    }

    @Override
    public int hashCode() {
        int h = this.hashCode;
        if (h == 0) {
            h = 7;
            h = 83 * h + Arrays.hashCode(this.hints);
            h = 83 * h + this.maxResults;
            h = 83 * h + Arrays.hashCode(this.parameters);
            h = 83 * h + Arrays.hashCode(this.parameterNames);
            h = 83 * h + this.statement.hashCode();
            this.hashCode = h;
        }
        return h;
    }

    /**
   * <b>Warning:</b> Currently this method may return buggy results if the specified query contains inline string
   * literals containing "?" followed by a number. This method is not yet supporting named parameters.
   *
   * @since JaXLib 1.0
   */
    public DetachedQuery<R> insertSubQuery(final int index, @Nonnull final DetachedQuery<?> subQuery) {
        CheckArg.notNull(subQuery, "subQuery");
        CheckArg.between(index, 0, this.statement.length(), "index");
        CheckArg.isNull(subQuery.parameterNames, "subQuery.parameterNames");
        return new DetachedQuery<R>(this.hints, this.maxResults, this.parameterNames, Strings.concat(this.statement.substring(0, index), shiftPositionalParameters(subQuery, getParameterCount()), this.statement.substring(index)), ObjectArrays.concatOrKeep(this.parameters, subQuery.parameters));
    }

    /**
   * Replace each occurence of the specified token in the statement of this query by the argument query.
   * The parameter list of the argument query is appended at most once to the parameters list of this query.
   * <p>
   * <b>Warning:</b> Currently this method may return buggy results if the specified query contains inline string
   * literals containing "?" followed by a number. This method is not yet supporting named parameters.
   * </p>
   *
   * @since JaXLib 1.0
   */
    public DetachedQuery<R> replaceTokenBySubQuery(@Nonnull final String token, @Nonnull final DetachedQuery<?> subQuery) {
        CheckArg.notNull(token, "token");
        CheckArg.notNull(subQuery, "subQuery");
        CheckArg.isNull(subQuery.parameterNames, "subQuery.parameterNames");
        if (this.statement.indexOf(token) < 0) return this;
        return new DetachedQuery<R>(this.hints, this.maxResults, this.parameterNames, this.statement.replace(token, shiftPositionalParameters(subQuery, getParameterCount())), ObjectArrays.concatOrKeep(this.parameters, subQuery.parameters));
    }

    public final List<R> select(final EntityManager em) {
        return select(em, 0, -1, (QueryCustomizer[]) null);
    }

    public final List<R> select(final EntityManager em, final QueryCustomizer hint) {
        return select(em, 0, -1, new QueryCustomizer[] { hint });
    }

    public final List<R> select(final EntityManager em, final QueryCustomizer hint0, final QueryCustomizer hint1) {
        return select(em, 0, -1, hint0, hint1);
    }

    public final List<R> select(final EntityManager em, final int maxResults) {
        return select(em, 0, maxResults, (QueryCustomizer[]) null);
    }

    public final List<R> select(final EntityManager em, final int maxResults, final QueryCustomizer hint) {
        return select(em, 0, maxResults, new QueryCustomizer[] { hint });
    }

    public final List<R> select(final EntityManager em, final int maxResults, final QueryCustomizer hint0, final QueryCustomizer hint1) {
        return select(em, 0, maxResults, new QueryCustomizer[] { hint0, hint1 });
    }

    public final List<R> select(final EntityManager em, final int firstIndex, final int maxResults, final QueryCustomizer hint0, final QueryCustomizer hint1) {
        return select(em, firstIndex, maxResults, new QueryCustomizer[] { hint0, hint1 });
    }

    public List<R> select(final EntityManager em, final int firstIndex, final int maxResults, QueryCustomizer... hints) {
        final Query query = createQuery(em, firstIndex, maxResults, hints);
        hints = null;
        return query.getResultList();
    }

    public final List<R> selectAndRemove(final EntityManager em) {
        return selectAndRemove(em, 0, -1, (QueryCustomizer[]) null);
    }

    public final List<R> selectAndRemove(final EntityManager em, final QueryCustomizer hint) {
        return selectAndRemove(em, 0, -1, new QueryCustomizer[] { hint });
    }

    public final List<R> selectAndRemove(final EntityManager em, final QueryCustomizer hint0, final QueryCustomizer hint1) {
        return selectAndRemove(em, 0, -1, hint0, hint1);
    }

    public final List<R> selectAndRemove(final EntityManager em, final int maxResults) {
        return selectAndRemove(em, 0, maxResults, (QueryCustomizer[]) null);
    }

    public final List<R> selectAndRemove(final EntityManager em, final int maxResults, final QueryCustomizer hint) {
        return selectAndRemove(em, 0, maxResults, new QueryCustomizer[] { hint });
    }

    public final List<R> selectAndRemove(final EntityManager em, final int maxResults, final QueryCustomizer hint0, final QueryCustomizer hint1) {
        return selectAndRemove(em, 0, maxResults, new QueryCustomizer[] { hint0, hint1 });
    }

    public final List<R> selectAndRemove(final EntityManager em, final int firstIndex, final int maxResults, final QueryCustomizer hint0, final QueryCustomizer hint1) {
        return selectAndRemove(em, firstIndex, maxResults, new QueryCustomizer[] { hint0, hint1 });
    }

    public List<R> selectAndRemove(final EntityManager em, final int firstIndex, final int maxResults, QueryCustomizer... hints) {
        final List<R> list = select(em, firstIndex, maxResults, hints);
        hints = null;
        for (final Object e : list) em.remove(e);
        return list;
    }

    public final R selectAndRemoveSingle(final EntityManager em) {
        return selectAndRemoveSingle(em, (QueryCustomizer[]) null);
    }

    public final R selectAndRemoveSingle(final EntityManager em, final QueryCustomizer hint) {
        return selectAndRemoveSingle(em, new QueryCustomizer[] { hint });
    }

    public final R selectAndRemoveSingle(final EntityManager em, final QueryCustomizer hint0, final QueryCustomizer hint1) {
        return selectAndRemoveSingle(em, new QueryCustomizer[] { hint0, hint1 });
    }

    public R selectAndRemoveSingle(final EntityManager em, QueryCustomizer... hints) {
        final R e = selectSingle(em, hints);
        hints = null;
        em.remove(e);
        return e;
    }

    public final R selectAndRemoveTop(final EntityManager em) {
        return selectAndRemoveTop(em, (QueryCustomizer[]) null);
    }

    public final R selectAndRemoveTop(final EntityManager em, final QueryCustomizer hint) {
        return selectAndRemoveTop(em, new QueryCustomizer[] { hint });
    }

    public final R selectAndRemoveTop(final EntityManager em, final QueryCustomizer hint0, final QueryCustomizer hint1) {
        return selectAndRemoveTop(em, new QueryCustomizer[] { hint0, hint1 });
    }

    public R selectAndRemoveTop(final EntityManager em, QueryCustomizer... hints) {
        final R e = selectTop(em, hints);
        hints = null;
        if (e != null) em.remove(e);
        return e;
    }

    public final R selectSingle(final EntityManager em) {
        return selectSingle(em, (QueryCustomizer[]) null);
    }

    public final R selectSingle(final EntityManager em, final QueryCustomizer hint) {
        return selectSingle(em, new QueryCustomizer[] { hint });
    }

    public final R selectSingle(final EntityManager em, final QueryCustomizer hint0, final QueryCustomizer hint1) {
        return selectSingle(em, new QueryCustomizer[] { hint0, hint1 });
    }

    public R selectSingle(final EntityManager em, QueryCustomizer... hints) {
        final Query query = createQuery(em, 0, -1, hints);
        hints = null;
        return (R) query.getSingleResult();
    }

    public final R selectTop(final EntityManager em) {
        return selectTop(em, (QueryCustomizer[]) null);
    }

    public final R selectTop(final EntityManager em, final QueryCustomizer hint) {
        return selectTop(em, new QueryCustomizer[] { hint });
    }

    public final R selectTop(final EntityManager em, final QueryCustomizer hint0, final QueryCustomizer hint1) {
        return selectTop(em, new QueryCustomizer[] { hint0, hint1 });
    }

    public R selectTop(final EntityManager em, QueryCustomizer... hints) {
        final Query query = createQuery(em, 0, 1, hints);
        hints = null;
        final List<R> list = query.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    public final R selectUnique(final EntityManager em) {
        return selectUnique(em, (QueryCustomizer[]) null);
    }

    public final R selectUnique(final EntityManager em, final QueryCustomizer hint) {
        return selectUnique(em, new QueryCustomizer[] { hint });
    }

    public final R selectUnique(final EntityManager em, final QueryCustomizer hint0, final QueryCustomizer hint1) {
        return selectUnique(em, new QueryCustomizer[] { hint0, hint1 });
    }

    public R selectUnique(final EntityManager em, QueryCustomizer... hints) {
        int maxResults = getMaxResults();
        if ((maxResults < 0) || (maxResults > 2)) maxResults = 2;
        final Query query = createQuery(em, 0, maxResults, hints);
        hints = null;
        final List<R> list = query.getResultList();
        final int size = list.size();
        if (size == 0) return null; else if (size == 1) return list.get(0); else throw new NonUniqueResultException();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(this.statement.length() + 64 + (getParameterCount() * 5));
        sb.append(getClass().getName()).append('@').append(super.hashCode()).append(':').append('\n');
        for (int i = 50; --i >= 0; ) sb.append('-');
        sb.append('\n').append(this.statement).append('\n');
        for (int i = 50; --i >= 0; ) sb.append('-');
        sb.append('\n').append("maxResults = ").append(this.maxResults);
        sb.append('\n').append("hints      = ").append((this.hints == null) ? "{}" : Arrays.toString(this.hints));
        sb.append('\n').append("parameters = ");
        if (this.parameters == null) sb.append("{}"); else {
            for (int i = 0; i < this.parameters.length; i++) {
                sb.append('\n').append("  ").append('[');
                if (i < 100) {
                    sb.append(' ');
                    if (i < 10) sb.append(' ');
                }
                sb.append(i + 1).append("]: \"").append(this.parameters[i]).append('"');
            }
        }
        return sb.toString();
    }

    public final int update(final EntityManager em) {
        return update(em, (QueryCustomizer[]) null);
    }

    public final int update(final EntityManager em, final QueryCustomizer hint) {
        return update(em, new QueryCustomizer[] { hint });
    }

    public final int update(final EntityManager em, final QueryCustomizer hint0, final QueryCustomizer hint1) {
        return update(em, new QueryCustomizer[] { hint0, hint1 });
    }

    public int update(final EntityManager em, QueryCustomizer... hints) {
        final Query query = createQuery(em, 0, -1, hints);
        hints = null;
        return query.executeUpdate();
    }
}
