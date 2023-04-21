package com.crossdb.sql;

/**

This is the starting point to using the crossdb classes.
 <p>
 You create a factory by:
<p>
<code>
Class factory_class = Class.forName("com.spaceprogram.sql.mysql.MySQLFactory"); // put the implementation string into forName()
com.crossdb.sql.SQLFactory factory = (com.crossdb.sql.SQLFactory)(factory_class.newInstance());
</code>
<p>
Then you use one of the get methods to get the type of query you want to use.

/**
 * <p>Title: crossdb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Space Program Inc.</p>
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 */
public interface SQLFactory {

    InsertQuery getInsertQuery();

    SelectQuery getSelectQuery();

    UpdateQuery getUpdateQuery();

    DeleteQuery getDeleteQuery();

    AlterTableQuery getAlterTableQuery();

    CreateTableQuery getCreateTableQuery();

    /**
     * this will return an implementation specific WhereClause and should be used instead of
     * using WhereClause class directly.
     * 
     * @return
     */
    IWhereClause getWhereClause();

    /**
     * Setting the sequence suffix here will change the default suffix (which is TABLENAME_seq) to the
     * newly passed in suffix.  So whenever you getInsertQuery(), the insertquery will be set ot the appropriate
     * @param suffix
     */
    void setSequenceSuffix(String suffix);
}
