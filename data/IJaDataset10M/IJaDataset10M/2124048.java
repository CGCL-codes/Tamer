package com.googlecode.jerato.common.data;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.googlecode.jerato.core.store.StoreParameters;
import com.googlecode.jerato.core.store.StoreTransaction;
import com.googlecode.jerato.core.store.StoreTransfer;
import com.googlecode.jerato.library.SystemException;
import com.googlecode.jerato.library.SystemService;
import com.googlecode.jerato.library.store.StoreFunctionImpl;
import com.googlecode.jerato.library.store.StoreService;
import com.googlecode.jerato.library.store.StoreTransferImpl;

public class TableListSelect extends StoreFunctionImpl {

    public void execute(StoreTransfer trans, StoreParameters input, StoreParameters output) {
        StoreTransaction transaction = trans.getTransaction();
        List list = new ArrayList();
        try {
            DatabaseMetaData metaData = transaction.getConnection().getMetaData();
            ResultSet resultSet = null;
            try {
                resultSet = metaData.getTables(null, null, null, null);
                ResultSetMetaData meta = resultSet.getMetaData();
                final int colCount = meta.getColumnCount();
                String[] colNameList = new String[colCount];
                for (int loop = 0; loop < colCount; loop++) {
                    colNameList[loop] = meta.getColumnName(loop + 1);
                }
                prepareResultSet(resultSet, colNameList);
                while (resultSet.next()) {
                    HashMap map = new HashMap();
                    processResultSet(map, colNameList, colCount, resultSet);
                    list.add(map);
                }
                output.setResultList(list);
            } finally {
                resultSet.close();
            }
        } catch (SQLException se) {
            throw new SystemException("Table metadata get failed.", se);
        }
    }

    public static void main(String[] args) {
        SystemService.staticInitialize();
        StoreTransferImpl trans = new StoreTransferImpl();
        try {
            List list = StoreService.getInstance().select(trans, TableListSelect.class, null);
            for (Iterator itr = list.iterator(); itr.hasNext(); ) {
                Map map = (Map) itr.next();
                String tableName = (String) map.get("table_name");
                String tableCatalog = (String) map.get("table_cat");
                System.out.println("tableCatalog=" + tableCatalog);
                System.out.println("tableName=" + tableName);
                System.out.println("map=" + map);
            }
        } finally {
            trans.destroy();
            SystemService.staticFinalize();
        }
    }
}
