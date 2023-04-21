package halo.dao.partition;

public class TableCnf {

    private String className;

    private DbPartitionHelper dbPartitionHelper;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setDbPartitionHelper(DbPartitionHelper dbPartitionHelper) {
        this.dbPartitionHelper = dbPartitionHelper;
    }

    public DbPartitionHelper getDbPartitionHelper() {
        return dbPartitionHelper;
    }
}
