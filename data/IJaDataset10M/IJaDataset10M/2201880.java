package dbgate.ermanagement.support.persistant.inheritancetest;

import dbgate.DBColumnType;
import dbgate.ermanagement.DefaultDBColumn;
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
public class InheritanceTestExtFactory {

    public static Collection<IField> getFieldInfo(Class type) {
        Collection<IField> fields = new ArrayList<IField>();
        if (type == InheritanceTestSuperEntityExt.class) {
            DefaultDBColumn idCol = new DefaultDBColumn("idCol", true, false, DBColumnType.INTEGER);
            idCol.setSubClassCommonColumn(true);
            fields.add(idCol);
            fields.add(new DefaultDBColumn("name", DBColumnType.VARCHAR));
        } else if (type == InheritanceTestSubEntityAExt.class) {
            DefaultDBColumn idCol = new DefaultDBColumn("idCol", true, false, DBColumnType.INTEGER);
            idCol.setSubClassCommonColumn(true);
            fields.add(idCol);
            fields.add(new DefaultDBColumn("nameA", DBColumnType.VARCHAR));
        } else if (type == InheritanceTestSubEntityBExt.class) {
            DefaultDBColumn idCol = new DefaultDBColumn("idCol", true, false, DBColumnType.INTEGER);
            idCol.setSubClassCommonColumn(true);
            fields.add(idCol);
            fields.add(new DefaultDBColumn("nameB", DBColumnType.VARCHAR));
        }
        return fields;
    }

    public static String getTableNames(Class type) {
        String tableName = null;
        if (type == InheritanceTestSuperEntityExt.class) {
            tableName = "inheritance_test_super";
        } else if (type == InheritanceTestSubEntityAExt.class) {
            tableName = "inheritance_test_suba";
        } else if (type == InheritanceTestSubEntityBExt.class) {
            tableName = "inheritance_test_subb";
        }
        return tableName;
    }
}
