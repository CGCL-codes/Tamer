package ch.gmtech.lab.datasource.db;

public interface SqlDialect {

    String deleteUsing(ColumnRepository aColumnRepository);

    String findUsing(ColumnRepository aColumnRepository);

    String insertUsing(ColumnRepository aColumnRepository);
}
