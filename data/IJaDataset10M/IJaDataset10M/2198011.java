package com.daffodilwoods.daffodildb.server.sql99.dql.tableexpression.groupbyclause;

import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.utils.*;
import com.daffodilwoods.database.resource.*;
import com.daffodilwoods.database.sqlinitiator.*;
import java.util.*;

/**
 * For documentation of this interface and methods, please refer to
 * documantation of groupingset.
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public interface groupingspecification extends com.daffodilwoods.daffodildb.utils.parser.StatementExecuter {

    ColumnDetails[] getColumnDetails() throws DException;

    void getColumnsIncluded(ArrayList aList) throws DException;

    void getTablesIncluded(ArrayList aList) throws DException;

    _Reference[] getReferences(TableDetails[] tableDetailsArray) throws DException;
}
