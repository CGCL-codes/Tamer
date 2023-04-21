package dbgate.ermanagement.support.patch.patchempty;

import dbgate.DBColumnType;
import dbgate.ermanagement.DefaultDBColumn;
import dbgate.ermanagement.IField;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Adipa
 * Date: Sep 11, 2010
 * Time: 9:51:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class LeafEntitySubA extends LeafEntity {

    private String someTextA;

    public String getSomeTextA() {
        return someTextA;
    }

    public void setSomeTextA(String someTextA) {
        this.someTextA = someTextA;
    }

    public Map<Class, String> getTableNames() {
        Map<Class, String> map = super.getTableNames();
        map.put(LeafEntitySubA.class, "leaf_entity_a");
        return map;
    }

    public Map<Class, Collection<IField>> getFieldInfo() {
        Map<Class, Collection<IField>> map = super.getFieldInfo();
        ArrayList<IField> dbColumns = new ArrayList<IField>();
        dbColumns.add(new DefaultDBColumn("someTextA", DBColumnType.VARCHAR));
        map.put(LeafEntitySubA.class, dbColumns);
        return map;
    }
}
