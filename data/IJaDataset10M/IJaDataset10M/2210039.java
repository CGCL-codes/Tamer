package org.databene.mad4db.compare;

import java.util.ArrayList;
import java.util.List;
import org.databene.jdbacl.DBUtil;
import org.databene.jdbacl.model.DBIndex;
import org.databene.jdbacl.model.DBPackage;
import org.databene.jdbacl.model.DBSequence;
import org.databene.jdbacl.model.DBTable;
import org.databene.jdbacl.model.DBTrigger;
import org.databene.jdbacl.model.Database;
import org.databene.jdbacl.model.SequenceHolder;
import org.databene.mad4db.ComparisonConfig;
import org.databene.mad4db.cmd.SchemaChange;

/**
 * Compares all objects in two database schemas.<br/><br/>
 * Created: 20.05.2011 16:52:43
 * @since 0.1
 * @author Volker Bergmann
 */
public class SchemaComparator {

    private ComparisonConfig config;

    public SchemaComparator(ComparisonConfig config) {
        this.config = config;
    }

    public SchemaChange compare(Database db1, Database db2) {
        SchemaChange change = new SchemaChange(config);
        compareTables(db1, db2, change);
        if (!config.isIgnoringSequences()) compareSequences(db1, db2, change);
        compareTriggers(db1, db2, change);
        comparePackages(db1, db2, change);
        return change;
    }

    protected void compareTables(Database db1, Database db2, SchemaChange schemaChange) {
        List<DBTable> tables1 = DBUtil.dependencyOrderedTables(db1);
        List<DBTable> tables2 = DBUtil.dependencyOrderedTables(db2);
        UnorderedListComparator<DBTable> comparator = new TableListComparator(config);
        comparator.compareLists(tables1, tables2, schemaChange);
    }

    protected List<DBIndex> getIndexes(Database db) {
        List<DBIndex> indexes = new ArrayList<DBIndex>();
        for (DBTable table : db.getTables()) indexes.addAll(table.getIndexes());
        return indexes;
    }

    protected void compareSequences(SequenceHolder holder1, SequenceHolder holder2, SchemaChange schemaChange) {
        UnorderedListComparator<DBSequence> comparator = new SequenceListComparator(config);
        comparator.compareLists(holder1.getSequences(true), holder2.getSequences(true), schemaChange);
    }

    protected void compareTriggers(Database db1, Database db2, SchemaChange schemaChange) {
        UnorderedListComparator<DBTrigger> comparator = new TriggerListComparator(config);
        comparator.compareLists(db1.getTriggers(), db2.getTriggers(), schemaChange);
    }

    protected void comparePackages(Database db1, Database db2, SchemaChange schemaChange) {
        List<DBPackage> packages1 = db1.getPackages();
        List<DBPackage> packages2 = db2.getPackages();
        UnorderedListComparator<DBPackage> comparator = new PackageListComparator(config);
        comparator.compareLists(packages1, packages2, schemaChange);
    }
}
