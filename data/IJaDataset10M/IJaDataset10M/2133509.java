package org.openconcerto.sql.model;

import org.openconcerto.sql.model.graph.Path;
import org.openconcerto.utils.CollectionUtils;
import org.openconcerto.utils.cc.ITransformer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ILM Informatique 10 mai 2004
 */
public final class SQLSelect {

    public static enum ArchiveMode {

        UNARCHIVED, ARCHIVED, BOTH
    }

    public static final ArchiveMode UNARCHIVED = ArchiveMode.UNARCHIVED;

    public static final ArchiveMode ARCHIVED = ArchiveMode.ARCHIVED;

    public static final ArchiveMode BOTH = ArchiveMode.BOTH;

    /**
     * Quote %-escaped parameters. %% : %, %s : quoteString, %i : quote, %f : quote(getFullName()),
     * %n : quote(getName()).
     * 
     * @param pattern a string with %, eg "SELECT * FROM %n where %f like '%%a%%'".
     * @param params the parameters, eg [ /TENSION/, |TENSION.LABEL| ].
     * @return pattern with % replaced, eg SELECT * FROM "TENSION" where "TENSION.LABEL" like '%a%'.
     */
    public static final String quote(final String pattern, Object... params) {
        return SQLBase.quoteStd(pattern, params);
    }

    private final SQLBase base;

    private final List<String> select;

    private final List<SQLField> selectFields;

    private Where where;

    private boolean whereAddToFrom;

    private final List<FieldRef> groupBy;

    private Where having;

    private final List<String> order;

    private final FromClause from;

    private final AliasedTables declaredTables;

    private final Set<String> joinAliases;

    private final List<SQLSelectJoin> joins;

    private boolean generalExcludeUndefined;

    private Map<SQLTable, Boolean> excludeUndefined;

    private final Map<SQLTable, ArchiveMode> archivedPolicy;

    private boolean distinct;

    private boolean waitTrx;

    private final List<String> waitTrxTables;

    private Integer limit;

    public SQLSelect(SQLBase base) {
        this(base, false);
    }

    /**
     * Create a new SQLSelect.
     * 
     * @param base the database of the request.
     * @param plain whether this request should automatically add a where clause for archived and
     *        undefined.
     */
    public SQLSelect(SQLBase base, boolean plain) {
        this.select = new ArrayList<String>();
        this.selectFields = new ArrayList<SQLField>();
        this.where = null;
        this.whereAddToFrom = true;
        this.groupBy = new ArrayList<FieldRef>();
        this.having = null;
        this.order = new ArrayList<String>();
        this.from = new FromClause();
        this.declaredTables = new AliasedTables();
        this.joinAliases = new HashSet<String>();
        this.joins = new ArrayList<SQLSelectJoin>();
        this.base = base;
        this.distinct = false;
        this.excludeUndefined = new HashMap<SQLTable, Boolean>();
        this.archivedPolicy = new HashMap<SQLTable, ArchiveMode>();
        this.waitTrx = false;
        this.waitTrxTables = new ArrayList<String>();
        if (plain) {
            this.generalExcludeUndefined = false;
            this.setArchivedPolicy(BOTH);
        } else {
            this.generalExcludeUndefined = true;
            this.setArchivedPolicy(UNARCHIVED);
        }
        assert this.archivedPolicy.containsKey(null);
    }

    /**
     * Clone un SQLSelect.
     * 
     * @param orig l'instance à cloner.
     */
    public SQLSelect(SQLSelect orig) {
        this.select = new ArrayList<String>(orig.select);
        this.selectFields = new ArrayList<SQLField>(orig.selectFields);
        this.where = orig.where == null ? null : new Where(orig.where);
        this.whereAddToFrom = orig.whereAddToFrom;
        this.groupBy = new ArrayList<FieldRef>(orig.groupBy);
        this.having = orig.having == null ? null : new Where(orig.having);
        this.order = new ArrayList<String>(orig.order);
        this.from = new FromClause(orig.from);
        this.declaredTables = new AliasedTables(orig.declaredTables);
        this.joinAliases = new HashSet<String>(orig.joinAliases);
        this.joins = new ArrayList<SQLSelectJoin>(orig.joins);
        this.base = orig.base;
        this.generalExcludeUndefined = orig.generalExcludeUndefined;
        this.excludeUndefined = new HashMap<SQLTable, Boolean>(orig.excludeUndefined);
        this.archivedPolicy = new HashMap<SQLTable, ArchiveMode>(orig.archivedPolicy);
        this.distinct = orig.distinct;
        this.waitTrx = orig.waitTrx;
        this.waitTrxTables = new ArrayList<String>(orig.waitTrxTables);
    }

    public String asString() {
        final SQLSystem sys = this.base.getServer().getSQLSystem();
        final StringBuffer result = new StringBuffer(512);
        result.append("SELECT ");
        if (this.distinct) result.append("DISTINCT ");
        if (this.getLimit() != null && sys == SQLSystem.MSSQL) {
            result.append("TOP ");
            result.append(this.getLimit());
            result.append(' ');
        }
        result.append(CollectionUtils.join(this.select, ", "));
        result.append("\n " + this.from.getSQL());
        Where archive = this.where;
        final Collection<String> fromAliases = CollectionUtils.substract(this.declaredTables.getAliases(), this.joinAliases);
        for (final String alias : fromAliases) {
            final SQLTable fromTable = this.declaredTables.getTable(alias);
            archive = Where.and(getArchiveWhere(fromTable, alias), archive);
            archive = Where.and(getUndefWhere(fromTable, alias), archive);
        }
        if (archive != null && archive.getClause() != "") {
            result.append("\n WHERE ");
            result.append(archive.getClause());
        }
        if (!this.groupBy.isEmpty()) {
            result.append("\n GROUP BY ");
            result.append(CollectionUtils.join(this.groupBy, ", ", new ITransformer<FieldRef, String>() {

                @Override
                public String transformChecked(FieldRef input) {
                    return input.getFieldRef();
                }
            }));
        }
        if (this.having != null) {
            result.append("\n HAVING ");
            result.append(this.having.getClause());
        }
        if (!this.order.isEmpty()) {
            result.append("\n ORDER BY ");
            result.append(CollectionUtils.join(this.order, ", "));
        }
        if (this.getLimit() != null && sys != SQLSystem.MSSQL) {
            result.append("\nLIMIT ");
            result.append(this.getLimit());
        }
        if (this.waitTrx) {
            if (sys.equals(SQLSystem.POSTGRESQL)) {
                result.append(" FOR SHARE");
                if (this.waitTrxTables.size() > 0) result.append(" OF " + CollectionUtils.join(this.waitTrxTables, ", "));
            } else if (sys.equals(SQLSystem.MYSQL)) result.append(" LOCK IN SHARE MODE");
        }
        return result.toString();
    }

    Where getArchiveWhere(final SQLTable table, final String alias) {
        final Where res;
        final ArchiveMode m = this.archivedPolicy.containsKey(table) ? this.archivedPolicy.get(table) : this.archivedPolicy.get(null);
        assert m != null : "no default policy";
        if (table.isArchivable() && m != BOTH) {
            final Object archiveValue;
            if (table.getArchiveField().getType().getJavaType().equals(Boolean.class)) {
                archiveValue = m == ARCHIVED;
            } else {
                archiveValue = m == ARCHIVED ? 1 : 0;
            }
            res = new Where(this.createRef(alias, table.getArchiveField()), "=", archiveValue);
        } else res = null;
        return res;
    }

    Where getUndefWhere(final SQLTable table, final String alias) {
        final Where res;
        final Boolean exclude = this.excludeUndefined.get(table);
        if (table.isRowable() && (exclude == Boolean.TRUE || (exclude == null && this.generalExcludeUndefined))) {
            res = new Where(this.createRef(alias, table.getKey()), "!=", table.getUndefinedID());
        } else res = null;
        return res;
    }

    public String toString() {
        return this.asString();
    }

    /**
     * Fields names of the SELECT
     * 
     * @return a list of fields names used by the SELECT
     */
    public List<String> getSelect() {
        return this.select;
    }

    /**
     * Fields of the SELECT
     * 
     * @return a list of fields used by the SELECT
     */
    public final List<SQLField> getSelectFields() {
        return this.selectFields;
    }

    public List<String> getOrder() {
        return this.order;
    }

    public Where getWhere() {
        return this.where;
    }

    public final boolean contains(String alias) {
        return this.declaredTables.contains(alias);
    }

    /**
     * Whether this SELECT already references table (eg by a from or a join). For example, if not
     * you can't ORDER BY with a field of that table.
     * 
     * @param table the table to test.
     * @return <code>true</code> if table is already in this.
     */
    public final boolean contains(SQLTable table) {
        return this.contains(table.getName());
    }

    public SQLSelect addGroupBy(FieldRef f) {
        this.groupBy.add(f);
        return this;
    }

    public SQLSelect setHaving(Where w) {
        this.having = w;
        return this;
    }

    /**
     * Ajoute un ORDER BY.
     * 
     * @param t a table alias.
     * @return this.
     * @throws IllegalArgumentException si t n'est pas ordonné.
     * @throws IllegalStateException si t n'est pas dans cette requete.
     * @see SQLTable#isOrdered()
     */
    public SQLSelect addOrder(String t) {
        final SQLTable table = this.getTable(t);
        if (!table.isOrdered()) throw new IllegalArgumentException("table is not ordered.");
        return this.addFieldOrder(this.createRef(t, table.getOrderField()));
    }

    public SQLSelect addFieldOrder(String fieldRef) {
        return this.addFieldOrder(this.createRef(fieldRef));
    }

    public SQLSelect addFieldOrder(FieldRef fieldRef) {
        if (this.base.getServer().getSQLSystem().equals(SQLSystem.DERBY)) this.addSelect(fieldRef);
        return this.addRawOrder(fieldRef.getFieldRef());
    }

    /**
     * Add an ORDER BY that is not an ORDER field.
     * 
     * @param selectItem an item that appears in the select, either a field reference or an alias.
     * @return this.
     */
    public SQLSelect addRawOrder(String selectItem) {
        this.order.add(selectItem);
        return this;
    }

    public SQLSelect clearOrder() {
        this.order.clear();
        return this;
    }

    /**
     * Ajoute un ORDER BY. Ne fais rien si t n'est pas ordonné.
     * 
     * @param t la table.
     * @return this.
     * @throws IllegalStateException si t n'est pas dans cette requete.
     */
    public SQLSelect addOrderSilent(String t) {
        try {
            this.addOrder(t);
        } catch (IllegalArgumentException e) {
        }
        return this;
    }

    public SQLSelect addSelect(String f) {
        return this.addSelect(f, null);
    }

    /**
     * Ajoute un champ au SELECT.
     * 
     * @param f le champ à ajouter.
     * @return this pour pouvoir chaîner.
     */
    public SQLSelect addSelect(FieldRef f) {
        return this.addSelect(f, null);
    }

    /**
     * Permet d'ajouter plusieurs champs.
     * 
     * @param s une collection de FieldRef.
     * @return this pour pouvoir chaîner.
     */
    public SQLSelect addAllSelect(Collection<? extends FieldRef> s) {
        for (final FieldRef element : s) {
            this.addSelect(element);
        }
        return this;
    }

    /**
     * Permet d'ajouter plusieurs champs d'une même table sans avoir à les préfixer.
     * 
     * @param t la table.
     * @param s une collection de nom de champs, eg "NOM".
     * @return this pour pouvoir chaîner.
     */
    public SQLSelect addAllSelect(SQLTable t, Collection<String> s) {
        for (final String fieldName : s) {
            this.addSelect(t.getField(fieldName));
        }
        return this;
    }

    public SQLSelect addSelect(String f, String function) {
        return this.addSelect(this.createRef(f, false), function);
    }

    /**
     * Ajoute une fonction d'un champ au SELECT.
     * 
     * @param f le champ, eg "PERSON.AGE".
     * @param function la fonction, eg "AVG".
     * @return this pour pouvoir chaîner.
     */
    public SQLSelect addSelect(FieldRef f, String function) {
        return this.addSelect(f, function, null);
    }

    public SQLSelect addSelect(FieldRef f, String function, String alias) {
        String s = f.getFieldRef();
        if (function != null) {
            s = function + "(" + s + ")";
        }
        this.from.add(this.declaredTables.add(f));
        this.selectFields.add(f.getField());
        return this.addRawSelect(s, alias);
    }

    /**
     * To add an item that is not a field.
     * 
     * @param expr any legal exp in a SELECT statement (eg a constant, a complex function, etc).
     * @param alias a name for the expression, may be <code>null</code>.
     * @return this.
     */
    public SQLSelect addRawSelect(String expr, String alias) {
        if (alias != null) {
            expr += " as " + SQLBase.quoteIdentifier(alias);
        }
        this.select.add(expr);
        return this;
    }

    /**
     * Ajoute une fonction prenant * comme paramètre.
     * 
     * @param function la fonction, eg "COUNT".
     * @return this pour pouvoir chaîner.
     */
    public SQLSelect addSelectFunctionStar(String function) {
        return this.addRawSelect(function + "(*)", null);
    }

    public SQLSelect addSelectStar(SQLTable table) {
        this.select.add(SQLBase.quoteIdentifier(table.getName()) + ".*");
        this.from.add(this.declaredTables.add(table));
        this.selectFields.addAll(table.getOrderedFields());
        return this;
    }

    public SQLSelect addSelectStar(String table) {
        return this.addSelectStar(this.base.getTable(table));
    }

    public SQLSelect addFrom(SQLTable table) {
        return this.addFrom(table, null);
    }

    /**
     * Explicitely add a table to the from clause. Rarely needed since tables are auto added by
     * addSelect(), setWhere() and addJoin().
     * 
     * @param table the table to add.
     * @param alias table alias, can be <code>null</code>.
     * @return this.
     */
    public SQLSelect addFrom(SQLTable table, String alias) {
        this.from.add(this.declaredTables.add(alias, table));
        return this;
    }

    public Where createWhereJ(String f1, String op, String f2) {
        return new Where(this.base.getFieldChecked(f1), op, this.base.getField(f2));
    }

    public Where createWhereS(String f1, String op, String scalar) {
        return new Where(this.base.getFieldChecked(f1), op, scalar);
    }

    /**
     * Renvoie une clause WHERE. Attention utilise une heuristique pour trouver la bonne méthode.
     * L'algo est si <code>s</code> est un champ de la base, alors on utilise createWhereJ() sinon
     * createWhereS().
     * 
     * @param field un champ de la base.
     * @param op l'opérateur.
     * @param s un nom de champ ou une valeur.
     * @return la clause WHERE correspondante.
     * @see #createWhereJ(String, String, String)
     * @see #createWhereS(String, String, String)
     */
    Where createWhere(String field, String op, String s) {
        boolean isField;
        try {
            isField = this.base.getField(s) != null;
        } catch (IllegalArgumentException e) {
            isField = false;
        }
        if (isField) return this.createWhereJ(field, op, s); else return this.createWhereS(field, op, s);
    }

    Where createWhere(String f1, String op, int scalar) {
        return new Where(this.createRef(f1), op, scalar);
    }

    /**
     * Change la clause where de cette requete.
     * 
     * @param w la nouvelle clause, <code>null</code> pour aucune clause.
     * @return this.
     */
    public SQLSelect setWhere(Where w) {
        this.where = w;
        if (this.whereAddToFrom && w != null) {
            for (final FieldRef f : w.getFields()) {
                this.from.add(this.declaredTables.add(f));
            }
        }
        return this;
    }

    public SQLSelect setWhere(String field, String op, String s) {
        return this.setWhere(this.createWhere(field, op, s));
    }

    public SQLSelect setWhere(String field, String op, int i) {
        return this.setWhere(this.createWhere(field, op, i));
    }

    /**
     * Ajoute le Where passé à celui de ce select.
     * 
     * @param w le Where à ajouter.
     * @return this.
     */
    public SQLSelect andWhere(Where w) {
        return this.setWhere(Where.and(this.getWhere(), w));
    }

    /**
     * Whether the tables used in the where are added to our from. By default this is
     * <code>true</code>, but you might want to set it to <code>false</code> eg with
     * "SELECT * FROM T where ID in (select T_ID from T2)".
     * 
     * @param b <code>true</code> if the tables should be added.
     */
    public final void setWhereAddToFrom(final boolean b) {
        this.whereAddToFrom = b;
    }

    /**
     * Add a join to this SELECT.
     * 
     * @param joinType can be INNER, LEFT or RIGHT.
     * @param fk the full name of a foreign key, eg 'BATIMENT.ID_SITE'.
     * @return the added join.
     */
    public SQLSelectJoin addJoin(String joinType, String fk) {
        return this.addJoin(joinType, this.base.getFieldChecked(fk));
    }

    /**
     * Add a join to this SELECT. Eg if <code>f</code> is |BATIMENT.ID_SITE|, then "join SITE on
     * BATIMENT.ID_SITE = SITE.ID" will be added.
     * 
     * @param joinType can be INNER, LEFT or RIGHT.
     * @param f a foreign key, eg |BATIMENT.ID_SITE|.
     * @return the added join.
     */
    public SQLSelectJoin addJoin(String joinType, SQLField f) {
        return this.addJoin(joinType, f, null);
    }

    public SQLSelectJoin addJoin(String joinType, String fk, String alias) {
        return this.addJoin(joinType, createRef(fk), alias);
    }

    /**
     * Add a join to this SELECT. Eg if <code>f</code> is bat.ID_SITE and <code>alias</code> is "s",
     * then "join SITE s on bat.ID_SITE = s.ID" will be added.
     * 
     * @param joinType can be INNER, LEFT or RIGHT.
     * @param f a foreign key, eg obs.ID_ARTICLE_2.
     * @param alias the alias for joined table, can be <code>null</code>, eg "art2".
     * @return the added join.
     */
    public SQLSelectJoin addJoin(String joinType, FieldRef f, final String alias) {
        final SQLTable foreignTable = this.base.getGraph().getForeignTable(f.getField());
        this.getTable(f.getAlias());
        final AliasedTable aliased = this.declaredTables.add(alias, foreignTable);
        return this.addJoin(new SQLSelectJoin(this, joinType, aliased, f, aliased));
    }

    /**
     * Add a join to this SELECT, inferring the joined table from the where.
     * 
     * @param joinType can be INNER, LEFT or RIGHT.
     * @param w the where joining the new table.
     * @return the added join.
     * @throws IllegalArgumentException if <code>w</code> hasn't exactly one table not yet
     *         {@link #contains(String) contained} in this.
     */
    public SQLSelectJoin addJoin(String joinType, final Where w) {
        final Set<AliasedTable> tables = new HashSet<AliasedTable>();
        for (final FieldRef f : w.getFields()) {
            if (!this.contains(f.getAlias())) {
                tables.add(new AliasedTable(f.getField().getTable(), f.getAlias()));
            }
        }
        if (tables.size() == 0) throw new IllegalArgumentException("No tables to add in " + w);
        if (tables.size() > 1) throw new IllegalArgumentException("More than one table to add (" + tables + ") in " + w);
        final AliasedTable joinedTable = tables.iterator().next();
        return addJoin(joinType, joinedTable.getTable(), joinedTable.getAlias(), w);
    }

    public SQLSelectJoin addJoin(String joinType, SQLTable joinedTable, final Where w) {
        return this.addJoin(joinType, joinedTable, null, w);
    }

    public SQLSelectJoin addJoin(String joinType, SQLTable joinedTable, final String alias, final Where w) {
        final AliasedTable aliased = this.declaredTables.add(alias, joinedTable);
        return this.addJoin(new SQLSelectJoin(this, joinType, aliased, w));
    }

    /**
     * Add a join that goes backward through a foreign key, eg LEFT JOIN "KD_2006"."BATIMENT" "bat"
     * on "s"."ID" = "bat"."ID_SITE".
     * 
     * @param joinType can be INNER, LEFT or RIGHT.
     * @param joinAlias the alias for the joined table, must not exist, eg "bat".
     * @param ff the foreign field, eg |BATIMENT.ID_SITE|.
     * @param foreignTableAlias the alias for the foreign table, must exist, eg "sit" or
     *        <code>null</code> for "SITE".
     * @return the added join.
     */
    public SQLSelectJoin addBackwardJoin(String joinType, final String joinAlias, SQLField ff, final String foreignTableAlias) {
        final SQLTable foreignTable = this.base.getGraph().getForeignTable(ff);
        final AliasedTable aliasedFT = new AliasedTable(foreignTable, foreignTableAlias);
        if (aliasedFT.getTable() != this.getTable(aliasedFT.getAlias())) throw new IllegalArgumentException("wrong alias: " + foreignTableAlias + " is not an alias to the target of " + ff);
        final AliasedTable aliased = this.declaredTables.add(joinAlias, ff.getTable());
        return this.addJoin(new SQLSelectJoin(this, joinType, aliased, new AliasedField(ff, joinAlias), aliasedFT));
    }

    private final SQLSelectJoin addJoin(SQLSelectJoin j) {
        this.from.add(j);
        this.joinAliases.add(j.getAlias());
        this.joins.add(j);
        return j;
    }

    public final List<SQLSelectJoin> getJoins() {
        return Collections.unmodifiableList(this.joins);
    }

    /**
     * Get the join going through <code>ff</code>, regardless of its alias.
     * 
     * @param ff a foreign field, eg |BATIMENT.ID_SITE|.
     * @return the corresponding join or <code>null</code> if none found, eg LEFT JOIN "test"."SITE"
     *         "s" on "bat"."ID_SITE"="s"."ID"
     */
    public final SQLSelectJoin getJoinFromField(SQLField ff) {
        for (final SQLSelectJoin j : this.joins) {
            if (j.hasForeignField() && j.getForeignField().getField().equals(ff)) {
                return j;
            }
        }
        return null;
    }

    /**
     * The first join adding the passed table.
     * 
     * @param t the table to search for, e.g. /LOCAL/.
     * @return the first matching join or <code>null</code> if none found, eg LEFT JOIN
     *         "test"."LOCAL" "l" on "r"."ID_LOCAL"="l"."ID"
     */
    public final SQLSelectJoin findJoinAdding(SQLTable t) {
        for (final SQLSelectJoin j : this.joins) {
            if (j.getJoinedTable().getTable().equals(t)) {
                return j;
            }
        }
        return null;
    }

    /**
     * The join adding the passed table alias.
     * 
     * @param alias a table alias, e.g. "l".
     * @return the matching join or <code>null</code> if none found, eg LEFT JOIN "test"."LOCAL" "l"
     *         on "r"."ID_LOCAL"="l"."ID"
     */
    public final SQLSelectJoin getJoinAdding(final String alias) {
        for (final SQLSelectJoin j : this.joins) {
            if (j.getAlias().equals(alias)) {
                return j;
            }
        }
        return null;
    }

    /**
     * Get the join going through <code>ff</code>, matching its alias.
     * 
     * @param ff a foreign field, eg |BATIMENT.ID_SITE|.
     * @return the corresponding join or <code>null</code> if none found, eg <code>null</code> if
     *         this only contains LEFT JOIN "test"."SITE" "s" on "bat"."ID_SITE"="s"."ID"
     */
    public final SQLSelectJoin getJoin(FieldRef ff) {
        for (final SQLSelectJoin j : this.joins) {
            if (j.hasForeignField() && j.getForeignField().getFieldRef().equals(ff.getFieldRef())) {
                return j;
            }
        }
        return null;
    }

    private SQLSelectJoin getJoin(SQLField ff, String foreignTableAlias) {
        for (final SQLSelectJoin j : this.joins) {
            if (j.hasForeignField() && j.getForeignField().getField().equals(ff) && j.getForeignTable().getAlias().equals(foreignTableAlias)) {
                return j;
            }
        }
        return null;
    }

    /**
     * Assure that there's a path from <code>tableAlias</code> through <code>p</code>, adding the
     * missing joins.
     * 
     * @param tableAlias the table at the start, eg "loc".
     * @param p the path that must be added, eg LOCAL-BATIMENT-SITE.
     * @return the alias of the last table of the path, "sit".
     */
    public String assurePath(String tableAlias, Path p) {
        return this.followPath(tableAlias, p, true);
    }

    public String followPath(String tableAlias, Path p) {
        return this.followPath(tableAlias, p, false);
    }

    /**
     * Return the alias at the end of the passed path.
     * 
     * @param tableAlias the table at the start, eg "loc".
     * @param p the path to follow, eg LOCAL-BATIMENT-SITE.
     * @param create <code>true</code> if missing joins should be created.
     * @return the alias of the last table of the path or <code>null</code>, eg "sit".
     */
    public String followPath(String tableAlias, Path p, final boolean create) {
        final SQLTable firstTable = this.getTable(tableAlias);
        if (!p.getFirst().equals(firstTable) && !p.getLast().equals(firstTable)) throw new IllegalArgumentException("neither ends of " + p + " is " + firstTable); else if (!p.getFirst().equals(firstTable)) return followPath(tableAlias, p.reverse(), create);
        String current = tableAlias;
        for (int i = 0; i < p.length(); i++) {
            final Set<SQLField> step = p.getStepFields(i);
            if (step.size() != 1) throw new IllegalArgumentException(p + " has more than 1 link at index " + i);
            final SQLField ff = step.iterator().next();
            final SQLSelectJoin j;
            final boolean forward = this.getTable(current) == ff.getTable();
            if (forward) {
                j = this.getJoin(new AliasedField(ff, current));
            } else {
                j = this.getJoin(ff, current);
            }
            if (j != null) current = j.getAlias(); else if (create) {
                final String previous = current;
                current = getUniqueAlias("assurePath_" + i);
                if (forward) this.addJoin("LEFT", new AliasedField(ff, previous), current); else this.addBackwardJoin("LEFT", current, ff, previous);
            } else return null;
        }
        return current;
    }

    public boolean isExcludeUndefined() {
        return this.generalExcludeUndefined;
    }

    public void setExcludeUndefined(boolean excludeUndefined) {
        this.generalExcludeUndefined = excludeUndefined;
    }

    public void setExcludeUndefined(boolean exclude, SQLTable table) {
        this.excludeUndefined.put(table, Boolean.valueOf(exclude));
    }

    public void setArchivedPolicy(ArchiveMode policy) {
        this.setArchivedPolicy(null, policy);
    }

    public void setArchivedPolicy(SQLTable t, ArchiveMode policy) {
        this.archivedPolicy.put(t, policy);
    }

    public final void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * Whether this SELECT should wait until all committed transaction are complete. This prevent a
     * SELECT following an UPDATE from seeing rows as they were before. NOTE that this may conflict
     * with other clauses (GROUP BY, DISTINCT, etc.).
     * 
     * @param waitTrx <code>true</code> if this select should wait.
     */
    public void setWaitPreviousWriteTX(boolean waitTrx) {
        this.waitTrx = waitTrx;
    }

    public void addWaitPreviousWriteTXTable(String table) {
        this.setWaitPreviousWriteTX(true);
        this.waitTrxTables.add(SQLBase.quoteIdentifier(table));
    }

    /**
     * Set the maximum number of rows to return.
     * 
     * @param limit the number of rows, <code>null</code> meaning no limit
     * @return this.
     */
    public SQLSelect setLimit(final Integer limit) {
        this.limit = limit;
        return this;
    }

    public final Integer getLimit() {
        return this.limit;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SQLSelect) return this.asString().equals(((SQLSelect) o).asString()); else return false;
    }

    @Override
    public int hashCode() {
        return this.select.hashCode() + this.from.getSQL().hashCode() + (this.where == null ? 0 : this.where.hashCode());
    }

    /**
     * Returns the table designated in this select by name.
     * 
     * @param name a table name or an alias, eg "OBSERVATION" or "art2".
     * @return the table named <code>name</code>.
     * @throws IllegalArgumentException if <code>name</code> is unknown to this select.
     */
    public final SQLTable getTable(String name) {
        if (this.declaredTables.contains(name)) return this.declaredTables.getTable(name); else if (this.base.containsTable(name)) return this.base.getTable(name); else throw new IllegalArgumentException("unknown table/alias: " + name);
    }

    /**
     * Return the alias for the passed table.
     * 
     * @param t a table.
     * @return the alias for <code>t</code>, or <code>null</code> if <code>t</code> is not exactly
     *         once in this.
     */
    public final AliasedTable getAlias(SQLTable t) {
        return this.declaredTables.getAlias(t);
    }

    public final List<AliasedTable> getAliases(SQLTable t) {
        return this.declaredTables.getAliases(t);
    }

    public final AliasedField getAlias(SQLField f) {
        return this.getAlias(f.getTable()).getField(f.getName());
    }

    /**
     * See http://www.postgresql.org/docs/8.2/interactive/sql-syntax-lexical.html#SQL-SYNTAX-
     * IDENTIFIERS
     */
    static final int maxAliasLength = 63;

    /**
     * Return an unused alias in this select.
     * 
     * @param seed the wanted name, eg "tableAlias".
     * @return a unique alias with the maximum possible of <code>seed</code>, eg "tableAl_1234".
     */
    public final String getUniqueAlias(String seed) {
        if (seed.length() > maxAliasLength) seed = seed.substring(0, maxAliasLength);
        if (!this.contains(seed)) {
            return seed;
        } else {
            long time = System.currentTimeMillis();
            for (int i = 0; i < 50; i++) {
                final String res;
                final String cat = seed + "_" + time;
                if (cat.length() > maxAliasLength) res = seed.substring(0, seed.length() - (cat.length() - maxAliasLength)) + "_" + time; else res = cat;
                if (!this.contains(res)) return res; else time += 1;
            }
            return null;
        }
    }

    /**
     * Creates a FieldRef from the passed reference. The table alias is either an existing alias or
     * it must be a table name.
     * 
     * @param f the field reference, eg "obs.ID_TENSION" or "TENSION.ID".
     * @return the corresponding FieldRef.
     */
    public final FieldRef createRef(String f) {
        return this.createRef(f, true);
    }

    /**
     * Creates a FieldRef from the passed reference. Obviously if the alias is not already in this
     * select, it must be a table name. You can also use {@link #createRef(String, SQLField)}.
     * 
     * @param f the field reference, eg "obs.ID_TENSION".
     * @param mustExist if the table name/alias must already exist in this select.
     * @return the corresponding FieldRef.
     */
    private final FieldRef createRef(String f, boolean mustExist) {
        final String[] names = SQLField.parse(f);
        if (names == null) throw new IllegalArgumentException("You must specify a full qualified name (ex TABLE.FIELD_NAME): " + f);
        return createRef(names[0], this.getTable(names[0]).getField(names[1]), mustExist);
    }

    private final FieldRef createRef(String alias, SQLField f) {
        return createRef(alias, f, true);
    }

    /**
     * Creates a FieldRef from the passed alias and field.
     * 
     * @param alias the table alias, eg "obs".
     * @param f the field, eg |OBSERVATION.ID_TENSION|.
     * @param mustExist if the table name/alias must already exist in this select.
     * @return the corresponding FieldRef.
     * @throws IllegalArgumentException if <code>mustExist</code> is <code>true</code> and this does
     *         not contain alias.
     */
    private final FieldRef createRef(String alias, SQLField f, boolean mustExist) {
        if (mustExist && !this.contains(alias)) throw new IllegalArgumentException("unknown alias " + alias);
        return new AliasedField(f, alias);
    }

    /**
     * Return all fields known to this instance. NOTE the fields used in ORDER BY are not returned.
     * 
     * @return all fields known to this instance.
     */
    public final Set<SQLField> getFields() {
        final Set<SQLField> res = new HashSet<SQLField>(this.getSelectFields());
        for (final SQLSelectJoin j : getJoins()) res.addAll(getFields(j.getWhere()));
        res.addAll(getFields(this.getWhere()));
        for (final FieldRef gb : this.groupBy) res.add(gb.getField());
        res.addAll(getFields(this.having));
        return res;
    }

    private static final Set<SQLField> getFields(Where w) {
        if (w != null) {
            final Set<SQLField> res = new HashSet<SQLField>();
            for (final FieldRef v : w.getFields()) res.add(v.getField());
            return res;
        } else return Collections.emptySet();
    }
}
