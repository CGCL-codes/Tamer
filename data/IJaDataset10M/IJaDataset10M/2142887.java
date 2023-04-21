package proper.engine;

import proper.database.ColumnLister;
import proper.database.Connector;
import proper.database.Dropper;
import proper.database.Join;
import proper.database.JoinTreeNode;
import proper.database.Joiner;
import proper.database.RelationDiscoverer;
import proper.database.Table;
import proper.database.TableLister;
import proper.relaggs.RelaggsConfig;
import proper.relaggs.RelaggsStructure;
import proper.relaggs.RelaggsTable;
import proper.relaggs.SqlEngine;
import proper.util.ProperVector;
import proper.util.Set;
import proper.util.TemporaryName;
import java.util.Vector;

/**
* This class is a wrapper around RELAGGS (Propositionalization of a relational
* data model).
*
* @author      FracPete
* @version $Revision: 1.10 $
*/
public class Relaggs extends DatabaseEngine {

    private static final String TMPPREFIX = "_relaggsed_";

    /** 
   * the prefix used for duplicate columns (a number and a "_" is added, i.e.
   * it could look like "dup1_" or "dup12_") 
   */
    public static final String COLUMN_PREFIX = "dup";

    private SqlEngine sql;

    private RelationDiscoverer discoverer;

    private TableLister tabLister;

    private ColumnLister colLister;

    private Dropper dropper;

    private TemporaryName tmpName;

    /**
   * initializes the object
   */
    public Relaggs() {
        super();
        setParameter("database", "relaggs");
        setParameter("table", "tab_loan");
        setParameter("result_table", "_relaggs");
        setParameter("field", "status");
        setParameter("join", "natural");
        setParameter("any_index", "no");
        setParameter("index", "");
        setParameter("exclude_tables", "");
        setParameter("max_depth", "1");
        setParameter("instead_null", "");
        setParameter("tree", "");
        setParameter("properties", "");
    }

    /**
   * drops all temporary tables and also the result table
   */
    private void dropTables(boolean onlyTemporary) {
        Vector tables;
        tables = tabLister.getList(RelaggsConfig.relaggs_tmpprefix + "%");
        tables.addAll(tabLister.getList(TMPPREFIX + "%"));
        if (!onlyTemporary) tables.add(getParameter("result_table"));
        dropper.drop(tables);
    }

    /**
   * adds the given table to the excludes list and returns the list
   */
    private String addToExcludes(String excludes, String table) {
        if (!excludes.equals("")) excludes += ",";
        excludes += table;
        return excludes;
    }

    /**
   * creates the excludes list for the given node
   */
    private String buildExcludes(JoinTreeNode node) {
        String result;
        Vector all;
        Vector others;
        int i;
        result = getStringParameter("exclude_tables");
        all = tabLister.getList();
        others = node.getAllTables();
        all = Set.minus(all, others);
        for (i = 0; i < all.size(); i++) result = addToExcludes(result, all.get(i).toString());
        return result;
    }

    /**
   * does the actual aggregation of the given two tables<br>
   * taken from: RelaggsController.aggregateAttributes(...)
   * @param db          the current database
   * @param main        the central table, with the class
   * @param aggr        the table to aggregate and add to the main table
   * @param join        the columns that the foreign key relation consists of
   * @return            the resulting table
   */
    private RelaggsTable aggregateAttributes(RelaggsTable main, RelaggsTable aggr, Join join) throws IllegalArgumentException {
        String new_table;
        RelaggsStructure struct;
        RelaggsTable res_table;
        new_table = sql.aggregateAttributes(main, aggr, join, sql.getTmpNumber());
        struct = sql.getAttributes(aggr, aggr.getAttribute(join.getLeftColumn()));
        res_table = sql.getTable(new_table);
        res_table.setShortName(sql.getTmpNumber(), (new_table.startsWith(RelaggsConfig.relaggs_tmpprefix)));
        sql.addAggregatedAttributes(res_table, aggr, struct, join);
        res_table = sql.getTable(res_table.getName());
        res_table.setShortName(sql.getTmpNumber(), (new_table.startsWith(RelaggsConfig.relaggs_tmpprefix)));
        sql.incTmpNumber();
        return res_table;
    }

    /**
   * flattens the given node into the given table
   */
    private boolean flatten(JoinTreeNode node, String result_table) {
        Flattener flattener;
        boolean result;
        Join oldJoin;
        if (getVerbose()) println("Flattening: " + node.getJoin().getLeftTable() + " -> " + result_table);
        flattener = new Flattener();
        addListener(flattener);
        flattener.setParameter("driver", getParameter("driver"));
        flattener.setParameter("url", getParameter("url"));
        flattener.setParameter("database", getParameter("database"));
        flattener.setParameter("user", getParameter("user"));
        flattener.setParameter("password", getParameter("password"));
        flattener.setParameter("table", node.getJoin().getLeftTable().toString());
        flattener.setParameter("field", "");
        flattener.setParameter("join", getParameter("join"));
        flattener.setParameter("result_table", result_table);
        flattener.setParameter("exclude_tables", buildExcludes(node));
        flattener.setParameter("max_depth", "-1");
        flattener.setParameter("tree", node.toTreeString(true));
        flattener.setParameter("instead_null", getParameter("instead_null"));
        flattener.setTemporaryName(tmpName);
        flattener.setShowRelations(false);
        if (exists("use_foreign_keys")) flattener.setParameter("use_foreign_keys", getStringParameter("use_foreign_keys"));
        result = flattener.execute();
        oldJoin = node.getJoin();
        node.getJoin().assign(new Join(result_table, oldJoin.getLeftColumn(), oldJoin.getSize(), oldJoin.getRightTable(), oldJoin.getRightColumn()));
        node.removeAllChildren();
        return result;
    }

    /**
   * flattens the DB structure, s.t. RELAGGS can be run
   */
    private boolean flatten(JoinTreeNode root) {
        int i;
        JoinTreeNode node;
        boolean result;
        result = true;
        println("\nRelations for flattening:\n");
        println(root.toString(true));
        println("Left over tables: " + discoverer.leftOverTables(root));
        for (i = 0; i < root.getChildCount(); i++) {
            node = (JoinTreeNode) root.getChildAt(i);
            if (node.getChildCount() > 0) {
                result = flatten(node, tmpName.next());
                if (result) node.removeAllChildren(); else break;
            }
        }
        return result;
    }

    /**
   * performs RELAGGS on the database (was flattened before and excludes list
   * compiled)
   */
    private boolean relaggs(JoinTreeNode root) {
        RelaggsTable table;
        boolean performed;
        String excl;
        Vector all;
        Vector others;
        int i;
        RelaggsTable child;
        String tmp_table;
        Join join;
        Table oldTable;
        RelaggsTable result;
        println("\nRelations for RELAGGS:\n");
        println(root.toString(true));
        println("Left over tables: " + discoverer.leftOverTables(root));
        all = tabLister.getList();
        others = new ProperVector();
        if (root.isJoin()) others.add(new Table(root.getJoin().getLeftTable())); else others.add(new Table(root.getTable().getName()));
        for (i = 0; i < root.getChildCount(); i++) others.add(new Table(root.getChildJoinAt(i).getLeftTable()));
        all = Set.minus(all, others);
        excl = "";
        for (i = 0; i < all.size(); i++) excl = addToExcludes(excl, all.get(i).toString());
        RelaggsConfig.setPropertiesList(getStringParameter("properties"));
        sql.setLeftOuterJoin(Joiner.parseType(getStringParameter("join")) == Joiner.LEFTOUTER_JOIN);
        sql.setUseAnyIndex(valueEquals("any_index", "yes"));
        sql.setUseIndex(getStringParameter("index"));
        sql.setExcludedTables(excl);
        sql.setTargetTable(getStringParameter("table"));
        sql.setTargetField(getStringParameter("field"));
        sql.setTmpNumber(1);
        table = sql.getTable(root.getTable().getName());
        result = (RelaggsTable) table.clone();
        performed = false;
        for (i = 0; i < root.getChildCount(); i++) {
            performed = true;
            join = root.getChildJoinAt(i);
            child = sql.getTable(join.getLeftTable());
            tmp_table = result.getName();
            result = aggregateAttributes(result, child, join);
            oldTable = root.getTable();
            root.getTable().assign(new Table(result.getName(), oldTable.getSize()));
            root.updateChildren();
            if (tmp_table.startsWith(RelaggsConfig.relaggs_tmpprefix)) sql.dropTable(tmp_table);
        }
        if (performed) sql.renameTable(result.getName(), getStringParameter("result_table"));
        return true;
    }

    /**
   * executes the propositionalization via RELAGGS
   */
    public boolean execute() {
        boolean result;
        JoinTreeNode root;
        String targetTable;
        super.execute();
        try {
            if (getStringParameter("driver").equals("")) setParameter("driver", Connector.MYSQL_DRIVER);
            if (getStringParameter("url").equals("")) setParameter("url", Connector.MYSQL_DRIVER);
            sql = new SqlEngine(conn);
            colLister = new ColumnLister(conn);
            colLister.setSort(true);
            tabLister = new TableLister(conn);
            tabLister.setSort(true);
            dropper = new Dropper(conn);
            discoverer = new RelationDiscoverer(conn);
            discoverer.setTable(getStringParameter("table"));
            discoverer.setExcludes(getStringParameter("exclude_tables"));
            discoverer.setMaxDepth(Integer.parseInt(getStringParameter("max_depth")));
            discoverer.setUseForeignKeys(exists("use_foreign_keys"));
            dropTables(false);
            if (!getStringParameter("tree").equals("")) root = JoinTreeNode.parseTreeString(getStringParameter("tree")); else root = discoverer.discover();
            targetTable = root.toString();
            tmpName = new TemporaryName(TMPPREFIX);
            result = flatten(root) && relaggs(root);
            println("\nRecord-Count (" + targetTable + "/" + getStringParameter("result_table") + "): " + exec.getRecordCount(targetTable, getStringParameter("field"), true) + "/" + exec.getRecordCount(getStringParameter("result_table"), getStringParameter("field"), true) + "\n");
            dropTables(true);
            conn.disconnect();
        } catch (Exception e) {
            println(e);
            result = false;
        }
        return result;
    }
}
