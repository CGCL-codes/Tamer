package dbgate.ermanagement.support.persistant.treetest;

import dbgate.DBColumnType;
import dbgate.ermanagement.DBRelationColumnMapping;
import dbgate.ermanagement.DefaultDBColumn;
import dbgate.ermanagement.DefaultDBRelation;
import dbgate.ermanagement.IField;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Adipa
 * Date: Nov 26, 2010
 * Time: 6:32:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class TreeTestExtFactory {

    public static Collection<IField> getFieldInfo(Class type) {
        Collection<IField> fields = new ArrayList<IField>();
        if (type == TreeTestRootEntityExt.class) {
            fields.add(new DefaultDBColumn("idCol", true, false, DBColumnType.INTEGER));
            fields.add(new DefaultDBColumn("name", DBColumnType.VARCHAR));
            fields.add(new DefaultDBRelation("one2ManyEntities", "fk_root2one2manyent", TreeTestOne2ManyEntityExt.class, new DBRelationColumnMapping[] { new DBRelationColumnMapping("idCol", "idCol") }));
            fields.add(new DefaultDBRelation("one2OneEntity", "fk_root2one2oneent", TreeTestOne2OneEntityExt.class, new DBRelationColumnMapping[] { new DBRelationColumnMapping("idCol", "idCol") }));
        } else if (type == TreeTestOne2ManyEntityExt.class) {
            fields.add(new DefaultDBColumn("idCol", true, false, DBColumnType.INTEGER));
            fields.add(new DefaultDBColumn("indexNo", true, false, DBColumnType.INTEGER));
            fields.add(new DefaultDBColumn("name", DBColumnType.VARCHAR));
        } else if (type == TreeTestOne2OneEntityExt.class) {
            fields.add(new DefaultDBColumn("idCol", true, false, DBColumnType.INTEGER));
            fields.add(new DefaultDBColumn("name", DBColumnType.VARCHAR));
        }
        return fields;
    }

    public static String getTableNames(Class type) {
        String tableName = null;
        if (type == TreeTestRootEntityExt.class) {
            tableName = "tree_test_root";
        } else if (type == TreeTestOne2ManyEntityExt.class) {
            tableName = "tree_test_one2many";
        } else if (type == TreeTestOne2OneEntityExt.class) {
            tableName = "tree_test_one2one";
        }
        return tableName;
    }
}
