package dbs_project.myDB.storagelayer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.primitives.IntIterator;
import org.apache.commons.collections.primitives.adapters.IteratorIntIterator;
import org.apache.mahout.math.list.IntArrayList;
import dbs_project.exceptions.ColumnAlreadyExistsException;
import dbs_project.exceptions.NoSuchColumnException;
import dbs_project.exceptions.NoSuchRowException;
import dbs_project.exceptions.SchemaMismatchException;
import dbs_project.myDB.abstraction.ColumnIDGenerator;
import dbs_project.myDB.abstraction.RowIDGenerator;
import dbs_project.storage.Column;
import dbs_project.storage.ColumnCursor;
import dbs_project.storage.ColumnMetaData;
import dbs_project.storage.Row;
import dbs_project.storage.RowCursor;
import dbs_project.storage.RowMetaData;
import dbs_project.storage.Table;
import dbs_project.storage.TableMetaData;
import dbs_project.storage.Type;
import dbs_project.util.annotation.BufferUtils;

public class myTable implements Table {

    private HashMap<Integer, myColumn> columns;

    private RowIDGenerator rowgenerator;

    private ColumnIDGenerator columngenerator;

    private MyTableMetaData metadata;

    private int maxrowid;

    private int maxcolumnid;

    private IntArrayList orderlist;

    private Set<Integer> tempcoids;

    public myTable(String name, Map<String, Type> schema, int tableID) {
        this.rowgenerator = new RowIDGenerator(-1);
        this.columngenerator = new ColumnIDGenerator(-1);
        int tempid = 0;
        this.columns = new LinkedHashMap<Integer, myColumn>();
        Iterator<String> it = schema.keySet().iterator();
        this.metadata = new MyTableMetaData(name, tableID);
        for (int i = 0; i < schema.size(); i++) {
            String columnname = it.next();
            tempid = columngenerator.newId();
            this.columns.put(tempid, new myColumn(this, columnname, tempid, schema.get(columnname)));
            this.metadata.addcolumn(this.columns.get(tempid).getMetaData());
        }
    }

    public myTable(ByteBuffer buffer) {
        int columncount = buffer.getInt();
        this.metadata = new MyTableMetaData(buffer);
        this.maxrowid = buffer.getInt();
        this.maxcolumnid = buffer.getInt();
        this.rowgenerator = new RowIDGenerator(this.maxrowid);
        this.columngenerator = new ColumnIDGenerator(this.maxcolumnid);
        IntArrayList tupleids = new IntArrayList(this.metadata.getRowCount());
        for (int i = 0; i < this.metadata.getRowCount(); i++) {
            tupleids.set(i, buffer.getInt());
        }
        this.columns = new LinkedHashMap<Integer, myColumn>(columncount);
        myColumn colu;
        myColumnMetaData cometa;
        for (int i = 0; i < columncount; i++) {
            cometa = new myColumnMetaData(this, tupleids, buffer);
            colu = new myColumn(cometa, buffer, maxrowid, this.metadata.getRowCount());
            this.columns.put(cometa.getId(), colu);
            this.metadata.addcolumn(cometa);
        }
    }

    @Override
    public int addColumn(Column column) throws SchemaMismatchException, ColumnAlreadyExistsException {
        if (!this.columns.isEmpty()) {
            checkrowids(column.getMetaData());
        }
        if (columns.containsKey(column.getMetaData().getName())) {
            throw new ColumnAlreadyExistsException();
        } else {
            myColumn temp = new myColumn(column, this, columngenerator.newId());
            columns.put(temp.getMetaData().getId(), temp);
            this.metadata.addcolumn(temp.getMetaData());
            return temp.getMetaData().getId();
        }
    }

    @Override
    public IntIterator addColumns(ColumnCursor columns) throws SchemaMismatchException, ColumnAlreadyExistsException {
        myColumn temp;
        int tempid;
        ArrayList<Integer> tempids = new ArrayList<Integer>();
        while (columns.next()) {
            if (!this.columns.isEmpty()) {
                checkrowids(columns.getMetaData());
            }
            if (this.columns.containsKey(columns.getMetaData().getName())) {
                throw new ColumnAlreadyExistsException();
            } else {
                tempid = columngenerator.newId();
                temp = new myColumn(columns.getMetaData(), this, tempid);
                for (int i = 0; i < this.metadata.getRowCount(); i++) {
                    temp.addtuple(columns.getMetaData().getRowId(i), columns.getObject(i));
                }
                this.columns.put(temp.getMetaData().getId(), temp);
                this.metadata.addcolumn(temp.getMetaData());
                tempids.add(tempid);
            }
        }
        return IteratorIntIterator.wrap(tempids.iterator());
    }

    public int addRow(Object[] obj) throws SchemaMismatchException {
        int id = rowgenerator.newId();
        for (int i = 0; i < obj.length; i++) {
            columns.get(this.metadata.getcolumnlist().get(i).getId()).addtuple(id, obj[i]);
        }
        this.metadata.addtuple(id);
        return id;
    }

    @Override
    public int addRow(Row row) throws SchemaMismatchException {
        int id = rowgenerator.newId();
        this.metadata.addtuple(id);
        int conumber = this.columns.size();
        if (orderlist == null) {
            this.getOrderlist(row);
        }
        for (int i = 0; i < conumber; i++) {
            columns.get(this.metadata.getcolumnlist().get(orderlist.get(i)).getId()).addtuple(id, row.getObject(i));
        }
        return id;
    }

    public void getOrderlist(Row row) {
        if (orderlist == null) {
            int conumber = this.columns.size();
            orderlist = new IntArrayList();
            String coname;
            for (int i = 0; i < conumber; i++) {
                coname = row.getMetaData().getColumnMetaData(i).getName();
                for (int t = 0; t < conumber; t++) {
                    if (this.metadata.getcolumnlist().get(t).getName().matches(coname)) {
                        orderlist.add(t);
                    }
                }
            }
        } else return;
    }

    public IntArrayList getschma(RowMetaData rowmeta) {
        IntArrayList te = new IntArrayList();
        int conumber = this.columns.size();
        for (int i = 0; i < conumber; i++) {
            String coname = rowmeta.getColumnMetaData(i).getName();
            for (int t = 0; t < conumber; t++) {
                if (this.metadata.getcolumnlist().get(t).getName().matches(coname)) {
                    te.add(t);
                }
            }
        }
        return te;
    }

    @Override
    public IntIterator addRows(RowCursor rows) throws SchemaMismatchException {
        int id;
        int conumber = this.columns.size();
        String coname;
        ArrayList<Integer> ids = new ArrayList<Integer>();
        IntArrayList te = new IntArrayList();
        for (int i = 0; i < conumber; i++) {
            coname = rows.getMetaData().getColumnMetaData(i).getName();
            for (int t = 0; t < conumber; t++) {
                if (this.metadata.getcolumnlist().get(t).getName().matches(coname)) {
                    te.add(t);
                }
            }
        }
        while (rows.next()) {
            id = rowgenerator.newId();
            this.metadata.addtuple(id);
            ids.add(id);
            int temp;
            for (int i = 0; i < conumber; i++) {
                this.columns.get(this.metadata.getcolumnlist().get(te.get(i)).getId()).addtuple(id, rows.getObject(i));
            }
        }
        return IteratorIntIterator.wrap(ids.iterator());
    }

    @Override
    public int createColumn(String columnName, Type columnType) throws ColumnAlreadyExistsException {
        for (myColumnMetaData temp : this.metadata.getcolumnlist()) {
            if (temp.getName().matches(columnName)) {
                throw new ColumnAlreadyExistsException();
            }
        }
        int id = columngenerator.newId();
        myColumn newcolumn = new myColumn(this, columnName, id, columnType);
        columns.put(id, newcolumn);
        this.metadata.addcolumn(newcolumn.getMetaData());
        return id;
    }

    @Override
    public void deleteRow(int rowID) throws NoSuchRowException {
        this.metadata.removerow(rowID);
    }

    @Override
    public void deleteRows(IntIterator rowIDs) throws NoSuchRowException {
        int temprowID = 0;
        while (rowIDs.hasNext()) {
            temprowID = rowIDs.next();
            this.metadata.removerow(temprowID);
        }
    }

    @Override
    public void dropColumn(int columnId) throws NoSuchColumnException {
        if (!this.columns.containsKey(columnId)) {
            throw new NoSuchColumnException();
        } else this.columns.remove(columnId);
        this.metadata.removecolumn(columnId);
    }

    public boolean checkColumn(int columnId) {
        if (!this.columns.containsKey(columnId)) {
            return false;
        } else return true;
    }

    @Override
    public void dropColumns(IntIterator columnIds) throws NoSuchColumnException {
        ArrayList<Integer> templish = new ArrayList<Integer>();
        int tempcolumnID;
        while (columnIds.hasNext()) {
            tempcolumnID = columnIds.next();
            templish.add(tempcolumnID);
            if (!this.columns.containsKey(tempcolumnID)) {
                throw new NoSuchColumnException();
            }
        }
        for (int i = 0; i < templish.size(); i++) {
            this.columns.remove(templish.get(i));
            this.metadata.removecolumn(templish.get(i));
        }
    }

    @Override
    public Column getColumn(int columnId) throws NoSuchColumnException {
        if (!this.columns.containsKey(columnId)) {
            throw new NoSuchColumnException();
        } else return this.columns.get(columnId);
    }

    @Override
    public ColumnCursor getColumns(IntIterator columnIds) throws NoSuchColumnException {
        return new myColumnCursor(this, columnIds);
    }

    @Override
    public ColumnCursor getColumns() {
        return new myColumnCursor(this);
    }

    @Override
    public Row getRow(int rowId) throws NoSuchRowException {
        myRowMetaData tempmetadata = new myRowMetaData(this.metadata.getcolumnlist(), rowId);
        List<Object> temp = new ArrayList<Object>(this.metadata.getcolumnnumber());
        for (int i = 0; i < this.metadata.getcolumnnumber(); i++) {
            temp.add(this.columns.get(tempmetadata.getColumMetaDataLst().get(i).getId()).getdatabyrowid(rowId));
        }
        return new myRow(tempmetadata, temp);
    }

    public myRow getMyRow(int rowId) throws NoSuchRowException {
        myRowMetaData tempmetadata = new myRowMetaData(this.metadata.getcolumnlist(), rowId);
        List<Object> temp = new ArrayList<Object>(this.metadata.getcolumnnumber());
        for (int i = 0; i < this.metadata.getcolumnnumber(); i++) {
            temp.add(this.columns.get(tempmetadata.getColumMetaDataLst().get(i).getId()).getdatabyrowid(rowId));
        }
        return new myRow(tempmetadata, temp);
    }

    @Override
    public RowCursor getRows() {
        return new myRowCursor(this, IteratorIntIterator.wrap(this.getrowIDs().toList().iterator()));
    }

    @Override
    public RowCursor getRows(IntIterator rowIds) throws NoSuchRowException {
        return new myRowCursor(this, rowIds);
    }

    @Override
    public TableMetaData getTableMetaData() {
        return this.metadata;
    }

    @Override
    public void renameColumn(int columnId, String newColumnName) throws ColumnAlreadyExistsException, NoSuchColumnException {
        for (myColumnMetaData temp : this.metadata.getcolumnlist()) {
            if (temp.getName().matches(newColumnName)) {
                throw new ColumnAlreadyExistsException();
            }
        }
        if (!this.columns.containsKey(columnId)) {
            throw new NoSuchColumnException();
        }
        this.columns.get(columnId).getMetaData().rename(newColumnName);
        for (int i = 0; i < this.metadata.getcolumnnumber(); i++) {
            if (this.metadata.getcolumnlist().get(i).getId() == columnId) this.metadata.getcolumnlist().get(i).rename(newColumnName);
        }
    }

    @Override
    public void updateColumn(int columnId, Column updateColumn) throws SchemaMismatchException, NoSuchColumnException {
        if (this.columns.isEmpty()) {
            throw new NoSuchColumnException();
        }
        checkrowids(updateColumn.getMetaData());
        if (!this.columns.containsKey(columnId)) {
            throw new NoSuchColumnException();
        }
        int newid = columngenerator.newId();
        this.columns.get(columnId).updatecolumn(updateColumn);
        this.metadata.updatecolumn(updateColumn.getMetaData(), columnId, this);
    }

    @Override
    public void updateColumns(IntIterator columnIDs, ColumnCursor updateColumns) throws SchemaMismatchException, NoSuchColumnException {
        while (columnIDs.hasNext()) {
            int tempid = columnIDs.next();
            if (this.columns.containsKey(tempid)) {
                this.columns.get(tempid).updatecolumn(updateColumns);
                this.metadata.updatecolumn(updateColumns.getMetaData(), tempid, this);
            } else try {
                this.addColumn(updateColumns);
            } catch (ColumnAlreadyExistsException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateRow(int rowID, Row newRow) throws SchemaMismatchException, NoSuchRowException {
        this.getOrderlist(newRow);
        for (int i = 0; i < this.columns.size(); i++) {
            columns.get(this.metadata.getcolumnlist().get(orderlist.get(i)).getId()).updatetuple(rowID, newRow.getObject(i));
        }
    }

    @Override
    public void updateRows(IntIterator rowIDs, RowCursor newRows) throws SchemaMismatchException, NoSuchRowException {
        IntArrayList te = new IntArrayList();
        Iterator<Integer> temp = this.columns.keySet().iterator();
        int tempid;
        while (temp.hasNext()) {
            tempid = temp.next();
            for (int i = 0; i < this.columns.size(); i++) {
                if (this.columns.get(tempid).getMetaData().getName().matches(newRows.getMetaData().getColumnMetaData(i).getName())) {
                    te.add(i);
                }
            }
        }
        while (rowIDs.hasNext()) {
            int t = 0;
            int temprowid = rowIDs.next();
            newRows.next();
            temp = this.columns.keySet().iterator();
            while (temp.hasNext()) {
                tempid = temp.next();
                columns.get(tempid).updatetuple(temprowid, newRows.getObject(te.get(t)));
                t++;
            }
        }
    }

    public String getname() {
        return this.metadata.getName();
    }

    public int[] getcolumnsbyID() {
        int length = columns.size();
        int[] IDs = new int[length];
        for (int i = 0; i < length; i++) {
            IDs[i] = columns.keySet().iterator().next();
        }
        return IDs;
    }

    public HashMap<Integer, myColumn> getColumnIDMap() {
        return this.columns;
    }

    private void checktableschema(RowMetaData row) throws SchemaMismatchException {
        if (this.metadata.getTableSchema().size() != row.getColumnCount()) {
            throw new SchemaMismatchException();
        }
        for (int i = 0; i < this.columns.size(); i++) {
            String temp = row.getColumnMetaData(i).getName();
            if (!this.metadata.getTableSchema().containsKey(temp) || row.getColumnMetaData(i).getType() != this.metadata.getTableSchema().get(temp).getType()) {
                throw new SchemaMismatchException();
            } else return;
        }
    }

    public IntArrayList getrowIDs() {
        return this.metadata.getRowids();
    }

    public HashMap<Integer, myColumn> getalldata() {
        return this.columns;
    }

    public List<myColumnMetaData> getcolumnlist() {
        return this.metadata.getcolumnlist();
    }

    public void checkrowids(ColumnMetaData newColumn) throws SchemaMismatchException {
        if (this.getrowIDs().size() != newColumn.getRowCount()) {
            throw new SchemaMismatchException();
        }
    }

    public MyTableMetaData getMetaData() {
        return this.metadata;
    }

    public IntArrayList getRowidsbycolumnid(int columnid) {
        return this.columns.get(columnid).getMetaData().getrowids();
    }

    public myColumn getMyColumn(int columnId) throws NoSuchColumnException {
        return (myColumn) this.columns.get(columnId);
    }

    public Column getColumn(String columnName) throws NoSuchColumnException {
        int columnId = -1;
        try {
            columnId = this.metadata.getTableSchema().get(columnName).getId();
        } catch (NullPointerException e) {
            throw new NoSuchColumnException();
        }
        if (!this.columns.containsKey(columnId)) {
            throw new NoSuchColumnException();
        } else return this.columns.get(columnId);
    }

    public myColumn getMyColumn(String columnName) throws NoSuchColumnException {
        int columnId = -1;
        try {
            columnId = this.metadata.getTableSchema().get(columnName).getId();
        } catch (NullPointerException e) {
            throw new NoSuchColumnException();
        }
        if (!this.columns.containsKey(columnId)) {
            throw new NoSuchColumnException();
        } else return (myColumn) this.columns.get(columnId);
    }

    public void writeIT(ByteBuffer buffer) {
        this.metadata.writeIT(buffer);
        buffer.putInt(this.rowgenerator.getLastId());
        buffer.putInt(this.columngenerator.getLastId());
        IntArrayList temp = this.getrowIDs();
        for (int i = 0; i < temp.size(); i++) {
            buffer.putInt(temp.get(i));
        }
        Iterator<Integer> it = this.columns.keySet().iterator();
        while (it.hasNext()) {
            this.columns.get(it.next()).WriteIT(buffer);
        }
    }
}
