package com.openclub.objects;

import java.util.Hashtable;
import java.util.List;
import org.javizy.database.DbObject;
import org.javizy.sql.Database;

public class Gaz extends DbObject {

    public static Class<?> classes[] = { Integer.class, String.class, String.class };

    public static String fields[] = { "id", "gazname", "mineqlevel" };

    public static final int ID = 0;

    public static final int NAME = 1;

    public static final int LEVEL = 2;

    public int getFieldsCount() {
        return classes.length;
    }

    public Class<?> getFieldClass(int field) {
        return classes[field];
    }

    public String getFieldSqlName(int field) {
        return fields[field];
    }

    public boolean isPrimary(int field) {
        return field == ID;
    }

    public boolean isAI(int field) {
        return field == ID;
    }

    protected void postLoad(Database db) throws Exception {
    }

    private static Hashtable<Integer, DbObject> allGaz = null;

    public static void loadAll(org.javizy.sql.Database db) throws Exception {
        List<DbObject> gazlist = DbObject.loadAll(db, Gaz.class);
        allGaz = new Hashtable<Integer, DbObject>();
        for (DbObject obj : gazlist) {
            Gaz boat = (Gaz) obj;
            allGaz.put((Integer) boat.get(ID), boat);
        }
    }

    public static Gaz find(Integer i) {
        return (Gaz) allGaz.get(i);
    }

    public static Object[] getList() {
        return allGaz.values().toArray();
    }

    public Gaz() {
    }

    public Gaz(Object boatId) {
        set(ID, boatId);
    }

    public String toString() {
        return (String) get(Gaz.NAME);
    }
}
