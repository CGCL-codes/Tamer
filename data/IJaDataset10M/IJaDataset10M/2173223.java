package ch.photoindex.db;

/**
 * Topics table.
 * 
 * @author Lukas Blunschi
 * 
 */
public class TTopics implements Table {

    public static final String TBL_NAME = "topics";

    public static final String F_ID = "id";

    public static final String F_NAME = "name";

    public static final String F_ACTIVE = "active";

    public String getSQLCreate() {
        StringBuffer sql = new StringBuffer();
        sql.append("create table if not exists " + TBL_NAME + "(");
        sql.append(F_ID + "      int not null primary key auto_increment, ");
        sql.append(F_NAME + "    varchar(200) character set utf8 not null, ");
        sql.append(F_ACTIVE + "  boolean not null);");
        return sql.toString();
    }

    public String getSQLDefaultData() {
        StringBuffer sql = new StringBuffer();
        sql.append("insert into " + TBL_NAME + " ");
        sql.append("(" + F_ID + "," + F_NAME + "," + F_ACTIVE + ") ");
        sql.append("values ");
        sql.append("(1,'-',true);");
        return sql.toString();
    }
}
