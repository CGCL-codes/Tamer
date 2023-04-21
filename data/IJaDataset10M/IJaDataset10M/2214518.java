package panda.planner.impl;

import java.util.Map;
import panda.index.Index;
import panda.metainfo.IndexToken;
import panda.planner.UpdatePlanner;
import panda.query.scan.Scanner;
import panda.query.scan.UpdatableScanner;
import panda.query.struct.Attribute;
import panda.query.struct.Constant;
import panda.query.struct.SemAttribute;
import panda.query.tree.SelectNode;
import panda.query.tree.TableNode;
import panda.record.Schema;
import panda.record.TableToken;
import panda.record.TupleToken;
import panda.server.Panda;
import panda.syntax.ColumnDef;
import panda.syntax.CreateDefinition;
import panda.syntax.CreateIndexStm;
import panda.syntax.CreateTableStm;
import panda.syntax.DeleteStm;
import panda.syntax.DropIndexStm;
import panda.syntax.DropTableStm;
import panda.syntax.UpdateStm;
import panda.syntax.ValuesInsertStm;
import panda.transaction.Transaction;

public class NormalUpdatePlanner implements UpdatePlanner {

    @Override
    public int executeCreateIndex(CreateIndexStm stm, Transaction tx) {
        Panda.getMetaInfoManager().createIndex(stm.indexName, stm.columnName.tableName, stm.columnName.columnName, tx);
        TableNode tt = new TableNode(stm.columnName.tableName, tx);
        Map<String, IndexToken> indices = Panda.getMetaInfoManager().getAllIndices(stm.columnName.tableName, tx);
        Index idx = indices.get(stm.columnName.columnName).open();
        Attribute attr = tt.getSchema().getAttributeByName(stm.columnName.columnName);
        UpdatableScanner s = (UpdatableScanner) tt.open();
        while (s.next()) idx.insert(s.getValue(attr), s.getCursor());
        s.close();
        idx.close();
        return 0;
    }

    @Override
    public int executeDropIndex(DropIndexStm stm, Transaction tx) {
        Panda.getMetaInfoManager().deleteIndex(stm.indexName, tx);
        return 0;
    }

    @Override
    public int executeCreateTable(CreateTableStm stm, Transaction tx) {
        Schema sch = new Schema();
        for (CreateDefinition c : stm.DefList) if (c instanceof ColumnDef) sch.addAttribute(new SemAttribute(((ColumnDef) c).column), ((ColumnDef) c).dataType.type, ((ColumnDef) c).dataType.len);
        Panda.getMetaInfoManager().createTable(stm.table_name, sch, tx);
        return 0;
    }

    @Override
    public int executeDropTable(DropTableStm stm, Transaction tx) {
        for (String tbl : stm.droped_tablenames) Panda.getMetaInfoManager().deleteTable(tbl, tx);
        return 0;
    }

    @Override
    public int executeInsertValues(ValuesInsertStm stm, Transaction tx) {
        IndexToken it;
        TableNode tn = new TableNode(stm.tableName, tx);
        TableToken table = tn.getTableToken();
        UpdatableScanner upScan = (UpdatableScanner) tn.open();
        Schema upSch = table.getSchema();
        Map<String, IndexToken> indices = Panda.getMetaInfoManager().getAllIndices(stm.tableName, tx);
        upScan.insert();
        TupleToken tuple = upScan.getCursor();
        for (Attribute a : upSch.getAllAttributes()) {
            Constant val = stm.values.get(upSch.IDof(a.getAttributeName()) - 1).evaluate(null, tx);
            upScan.setValue(a, val);
            if (indices != null && (it = indices.get(a.getAttributeName())) != null) {
                Index idx = it.open();
                idx.insert(val, tuple);
                idx.close();
            }
        }
        upScan.close();
        return 1;
    }

    @Override
    public int executeDelete(DeleteStm stm, Transaction tx) {
        SelectNode sn = new SelectNode(new TableNode(stm.token, tx), stm.whereCondition, tx);
        Map<String, IndexToken> indices = Panda.getMetaInfoManager().getAllIndices(stm.tableName, tx);
        UpdatableScanner s = (UpdatableScanner) sn.open();
        int cnt = 0;
        while (s.next()) {
            TupleToken tuple = s.getCursor();
            if (indices != null) for (String attr : indices.keySet()) {
                Constant val = s.getValue(sn.getSchema().getAttributeByName(attr));
                Index idx = indices.get(attr).open();
                idx.delete(val, tuple);
                idx.close();
            }
            s.delete();
            cnt++;
        }
        s.close();
        return cnt;
    }

    @Override
    public int executeModify(UpdateStm stm, Transaction tx) {
        int colCnt = stm.setValueList.size();
        Constant[] newValue = new Constant[colCnt];
        Constant[] oldValue = new Constant[colCnt];
        Attribute[] attr = new Attribute[colCnt];
        Index[] idx = new Index[colCnt];
        SelectNode sn = new SelectNode(new TableNode(stm.token, tx), stm.whereCondition, tx);
        Map<String, IndexToken> indices = Panda.getMetaInfoManager().getAllIndices(stm.tableName, tx);
        for (int i = 0; i < colCnt; i++) {
            attr[i] = sn.getSchema().getAttributeByName(stm.setValueList.get(i).columnName.columnName);
            if (indices != null && indices.get(stm.setValueList.get(i).columnName.columnName) != null) idx[i] = indices.get(stm.setValueList.get(i).columnName.columnName).open(); else idx[i] = null;
        }
        UpdatableScanner s = (UpdatableScanner) sn.open();
        int cnt = 0;
        while (s.next()) {
            for (int i = 0; i < colCnt; i++) {
                System.out.println(s.getValue(attr[i]).getContentValue() + "->" + stm.setValueList.get(i).value.evaluate(s, tx).getContentValue());
                newValue[i] = stm.setValueList.get(i).value.evaluate(s, tx);
                oldValue[i] = s.getValue(attr[i]);
            }
            for (int i = 0; i < colCnt; i++) {
                TupleToken tuple = s.getCursor();
                s.setValue(attr[i], newValue[i]);
                if (idx[i] != null) {
                    idx[i].delete(oldValue[i], tuple);
                    idx[i].insert(newValue[i], tuple);
                }
            }
            cnt++;
        }
        for (int i = 0; i < colCnt; i++) if (idx[i] != null) idx[i].close();
        s.close();
        return cnt;
    }
}
