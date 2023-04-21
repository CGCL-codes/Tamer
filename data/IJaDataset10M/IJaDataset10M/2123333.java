package com.sqlmagic.tinysql;

import java.util.*;
import java.lang.*;
import java.io.*;
import java.sql.Types;

/**
dBase read/write access <br>
@author Brian Jepson <bjepson@home.com>
@author Marcel Ruff <ruff@swand.lake.de> Added write access to dBase and JDK 2 support
@author Thomas Morgner <mgs@sherito.org> Changed ColumnName to 11 bytes and strip name
 after first occurence of 0x00.
 Types are now handled as java.sql.Types, not as character flag
*/
public class dbfFile extends tinySQL {

    public static String dataDir;

    public static boolean debug = false;

    private Vector tableList = new Vector();

    static {
        try {
            dataDir = System.getProperty("user.home") + File.separator + ".tinySQL";
        } catch (Exception e) {
            System.err.println("tinySQL: unable to get user.home property, " + "reverting to current working directory.");
            dataDir = "." + File.separator + ".tinySQL";
        }
    }

    /**
   *
   * Constructs a new dbfFile object
   *
   */
    public dbfFile() {
        super();
        if (debug) System.out.println("Set datadir=" + dataDir);
    }

    /**
   *
   * Constructs a new dbfFile object
   *
   * @param d directory with which to override the default data directory
   *
   */
    public dbfFile(String d) {
        super();
        dataDir = d;
        if (debug) System.out.println("Set datadir=" + dataDir);
    }

    /**
   *
   * Creates a table given the name and a vector of
   * column definition (tsColumn) arrays.
   *
   * @param tableName the name of the table
   * @param v a Vector containing arrays of column definitions.
   * @see tinySQL#CreateTable
   *
   */
    void setDataDir(String d) {
        dataDir = d;
    }

    void CreateTable(String tableName, Vector v) throws IOException, tinySQLException {
        int numCols = v.size();
        int recordLength = 1;
        for (int i = 0; i < numCols; i++) {
            tsColumn coldef = ((tsColumn) v.elementAt(i));
            recordLength += coldef.size;
        }
        DBFHeader dbfHeader = new DBFHeader(numCols, recordLength);
        RandomAccessFile ftbl = dbfHeader.create(dataDir, tableName);
        for (int i = 0; i < v.size(); i++) {
            tsColumn coldef = ((tsColumn) v.elementAt(i));
            Utils.log("CREATING COL=" + coldef.name);
            writeColdef(ftbl, coldef);
        }
        ftbl.write((byte) 0x0d);
        ftbl.close();
    }

    /**
   * Creates new Columns in tableName, given a vector of
   * column definition (tsColumn) arrays.<br>
   * It is necessary to copy the whole file to do this task.
   *
   * ALTER TABLE table [ * ] ADD [ COLUMN ] column type
   *
   * @param tableName the name of the table
   * @param v a Vector containing arrays of column definitions.
   * @see tinySQL#AlterTableAddCol
   */
    void AlterTableAddCol(String tableName, Vector v) throws IOException, tinySQLException {
        String fullpath = dataDir + File.separator + tableName + dbfFileTable.dbfExtension;
        String tmppath = dataDir + File.separator + tableName + "_tmp_tmp" + dbfFileTable.dbfExtension;
        if (Utils.renameFile(fullpath, tmppath) == false) throw new tinySQLException("ALTER TABLE ADD COL error in renaming " + fullpath);
        try {
            RandomAccessFile ftbl_tmp = new RandomAccessFile(tmppath, "r");
            DBFHeader dbfHeader_tmp = new DBFHeader(ftbl_tmp);
            Vector coldef_list = new Vector(dbfHeader_tmp.numFields + v.size());
            int locn = 0;
            for (int i = 1; i <= dbfHeader_tmp.numFields; i++) {
                tsColumn coldef = readColdef(ftbl_tmp, tableName, i, locn);
                locn += coldef.size;
                coldef_list.addElement(coldef);
            }
            for (int jj = 0; jj < v.size(); jj++) coldef_list.addElement(v.elementAt(jj));
            CreateTable(tableName, coldef_list);
            RandomAccessFile ftbl = new RandomAccessFile(fullpath, "rw");
            ftbl.seek(ftbl.length());
            int numRec = 0;
            for (int iRec = 1; iRec <= dbfHeader_tmp.numRecords; iRec++) {
                String str = GetRecord(ftbl_tmp, dbfHeader_tmp, iRec);
                if (str == null) continue;
                ftbl.write(str.getBytes(Utils.encode));
                numRec++;
                for (int iCol = 0; iCol < v.size(); iCol++) {
                    tsColumn coldef = (tsColumn) v.elementAt(iCol);
                    String value = Utils.forceToSize(coldef.defaultVal, coldef.size, " ");
                    byte[] b = value.getBytes(Utils.encode);
                    ftbl.write(b);
                }
            }
            ftbl_tmp.close();
            DBFHeader.writeNumRecords(ftbl, numRec);
            ftbl.close();
            Utils.delFile(tmppath);
        } catch (Exception e) {
            throw new tinySQLException(e.getMessage());
        }
    }

    /**
   * Retrieve a record (=row)
   * @param dbfHeader dBase meta info
   * @param recordNumber starts with 1
   * @return the String with the complete record
   *         or null if the record is marked as deleted
   * @see tinySQLTable#GetCol
   */
    public String GetRecord(RandomAccessFile ff, DBFHeader dbfHeader, int recordNumber) throws tinySQLException {
        if (recordNumber < 1) throw new tinySQLException("Internal error - current record number < 1");
        try {
            ff.seek(dbfHeader.headerLength + (recordNumber - 1) * dbfHeader.recordLength);
            byte[] b = new byte[dbfHeader.recordLength];
            ff.readFully(b);
            String record = new String(b, Utils.encode);
            if (dbfFileTable.isDeleted(record)) return null;
            return record;
        } catch (Exception e) {
            throw new tinySQLException(e.getMessage());
        }
    }

    /**
   *
   * Deletes Columns from tableName, given a vector of
   * column definition (tsColumn) arrays.<br>
   *
   * ALTER TABLE table DROP [ COLUMN ] column { RESTRICT | CASCADE }
   *
   * @param tableName the name of the table
   * @param v a Vector containing arrays of column definitions.
   * @see tinySQL#AlterTableDropCol
   *
   */
    void AlterTableDropCol(String tableName, Vector v) throws IOException, tinySQLException {
        String fullpath = dataDir + File.separator + tableName + dbfFileTable.dbfExtension;
        String tmppath = dataDir + File.separator + tableName + "-tmp" + dbfFileTable.dbfExtension;
        if (Utils.renameFile(fullpath, tmppath) == false) throw new tinySQLException("ALTER TABLE DROP COL error in renaming " + fullpath);
        try {
            RandomAccessFile ftbl_tmp = new RandomAccessFile(tmppath, "r");
            DBFHeader dbfHeader_tmp = new DBFHeader(ftbl_tmp);
            Vector coldef_list = new Vector(dbfHeader_tmp.numFields - v.size());
            int locn = 0;
            nextCol: for (int i = 1; i <= dbfHeader_tmp.numFields; i++) {
                tsColumn coldef = readColdef(ftbl_tmp, tableName, i, locn);
                for (int jj = 0; jj < v.size(); jj++) {
                    String colName = (String) v.elementAt(jj);
                    if (coldef.name.equals(colName)) {
                        Utils.log("Dropping " + colName);
                        continue nextCol;
                    }
                }
                locn += coldef.size;
                coldef_list.addElement(coldef);
            }
            CreateTable(tableName, coldef_list);
            RandomAccessFile ftbl = new RandomAccessFile(fullpath, "rw");
            ftbl.seek(ftbl.length());
            int numRec = 0;
            for (int iRec = 1; iRec <= dbfHeader_tmp.numRecords; iRec++) {
                if (dbfFileTable.isDeleted(ftbl_tmp, dbfHeader_tmp, iRec) == true) continue;
                numRec++;
                ftbl.write(dbfFileTable.RECORD_IS_NOT_DELETED);
                String column = dbfFileTable._GetCol(ftbl_tmp, dbfHeader_tmp, iRec);
                for (int iCol = 0; iCol < coldef_list.size(); iCol++) {
                    tsColumn coldef = (tsColumn) coldef_list.elementAt(iCol);
                    String value = dbfFileTable.getColumn(coldef, column);
                    value = Utils.forceToSize(value, coldef.size, " ");
                    byte[] b = value.getBytes(Utils.encode);
                    ftbl.write(b);
                }
            }
            ftbl_tmp.close();
            File f = new File(tmppath);
            if (f.exists()) f.delete();
            DBFHeader.writeNumRecords(ftbl, numRec);
            ftbl.close();
        } catch (Exception e) {
            throw new tinySQLException(e.getMessage());
        }
    }

    void AlterTableRenameCol(String tableName, String oldColname, String newColname) throws IOException, tinySQLException {
        String fullpath = dataDir + File.separator + tableName + dbfFileTable.dbfExtension;
        try {
            RandomAccessFile ftbl = new RandomAccessFile(fullpath, "rw");
            DBFHeader dbfHeader = new DBFHeader(ftbl);
            int locn = 0;
            for (int iCol = 1; iCol <= dbfHeader.numFields; iCol++) {
                tsColumn coldef = readColdef(ftbl, tableName, iCol, locn);
                if (coldef.name.equals(oldColname)) {
                    Utils.log("Replacing column name '" + oldColname + "' with '" + newColname + "'");
                    ftbl.seek((iCol - 1) * 32 + 32);
                    ftbl.write(Utils.forceToSize(newColname, dbfFileTable.FIELD_TYPE_INDEX - dbfFileTable.FIELD_NAME_INDEX, (byte) 0));
                    ftbl.close();
                    return;
                }
            }
            ftbl.close();
            throw new tinySQLException("Renaming of column name '" + oldColname + "' to '" + newColname + "' failed, no column '" + oldColname + "' found");
        } catch (Exception e) {
            throw new tinySQLException(e.getMessage());
        }
    }

    /**
   *
   * Return a tinySQLTable object, given a table name.
   *
   * @param tableName
   * @see tinySQL#getTable
   *
   */
    tinySQLTable getTable(String tableName) throws tinySQLException {
        int i, tableIndex;
        tinySQLTable nextTable;
        tableIndex = Integer.MIN_VALUE;
        if (debug) System.out.println("Trying to create table object for " + tableName);
        for (i = 0; i < tableList.size(); i++) {
            nextTable = (tinySQLTable) tableList.elementAt(i);
            if (nextTable.table.equals(tableName)) {
                if (nextTable.isOpen()) {
                    if (debug) System.out.println("Found in cache " + nextTable.toString());
                    return nextTable;
                }
                tableIndex = i;
                break;
            }
        }
        if (tableIndex == Integer.MIN_VALUE) {
            tableList.addElement(new dbfFileTable(dataDir, tableName));
            nextTable = (tinySQLTable) tableList.lastElement();
            if (debug) System.out.println("Add to cache " + nextTable.toString());
            return (tinySQLTable) tableList.lastElement();
        } else {
            tableList.setElementAt(new dbfFileTable(dataDir, tableName), tableIndex);
            nextTable = (tinySQLTable) tableList.elementAt(tableIndex);
            if (debug) System.out.println("Update in cache " + nextTable.toString());
            return (tinySQLTable) tableList.elementAt(tableIndex);
        }
    }

    /**
   *
   * The DBF File class provides read-only access to DBF
   * files, so this baby should throw an exception.
   *
   * @param fname table name
   * @see tinySQL#DropTable
   *
   */
    void DropTable(String fname) throws tinySQLException {
        DBFHeader.dropTable(dataDir, fname);
    }

    /**
  Reading a column definition from file<br>
  @param ff file handle (correctly positioned)
  @param iCol index starts with 1
  @param locn offset to the current column
  @return struct with column info
  */
    static tsColumn readColdef(RandomAccessFile ff, String tableName, int iCol, int locn) throws tinySQLException {
        try {
            ff.seek((iCol - 1) * 32 + 32);
            byte[] b = new byte[11];
            ff.readFully(b);
            boolean clear = false;
            int i = 0;
            while ((i < 11) && (b[i] != 0)) {
                i++;
            }
            while (i < 11) {
                b[i] = 0;
                i++;
            }
            String colName = (new String(b, Utils.encode)).trim();
            byte c[] = new byte[1];
            c[0] = ff.readByte();
            String ftyp = new String(c, Utils.encode);
            ff.skipBytes(4);
            short flen = Utils.fixByte(ff.readByte());
            short fdec = Utils.fixByte(ff.readByte());
            if (ftyp.equals("N") & fdec == 0) ftyp = "I";
            tsColumn column = new tsColumn(colName);
            column.type = typeToSQLType(ftyp);
            column.size = flen;
            column.decimalPlaces = fdec;
            column.position = locn + 1;
            column.table = tableName;
            return column;
        } catch (Exception e) {
            throw new tinySQLException(e.getMessage());
        }
    }

    /**
  Writing a column definition to file<br>
  NOTE: the file pointer (seek()) must be at the correct position
  @param ff file handle (correctly positioned)
  @param coldef struct with column info
  */
    void writeColdef(RandomAccessFile ff, tsColumn coldef) throws tinySQLException {
        try {
            ff.write(Utils.forceToSize(coldef.name, dbfFileTable.FIELD_TYPE_INDEX - dbfFileTable.FIELD_NAME_INDEX, (byte) 0));
            String type = null;
            if (coldef.type == Types.CHAR || coldef.type == Types.VARCHAR || coldef.type == Types.LONGVARCHAR) type = "C"; else if (coldef.type == Types.NUMERIC || coldef.type == Types.INTEGER || coldef.type == Types.TINYINT || coldef.type == Types.SMALLINT || coldef.type == Types.BIGINT || coldef.type == Types.FLOAT || coldef.type == Types.DOUBLE || coldef.type == Types.REAL) type = "N"; else if (coldef.type == Types.BIT) type = "L"; else if (coldef.type == Types.DATE) type = "D"; else type = "M";
            ff.write(Utils.forceToSize(type, 1, (byte) 0));
            ff.write(Utils.forceToSize(null, 4, (byte) 0));
            ff.write(coldef.size);
            ff.write(coldef.decimalPlaces);
            ff.write(Utils.forceToSize(null, DBFHeader.BULK_SIZE - dbfFileTable.FIELD_RESERVED_INDEX, (byte) 0));
        } catch (Exception e) {
            throw new tinySQLException(e.getMessage());
        }
    }

    /**
  'C' Char (max 254 bytes)
  'N' '-.0123456789' (max 19 bytes)
  'L' 'YyNnTtFf?' (1 byte)
  'M' 10 digit .DBT block number
  'D' 8 digit YYYYMMDD
  *
  * Uses java.sql.Types as key
  */
    static String typeToLiteral(int type) {
        if (type == Types.CHAR) return "CHAR";
        if (type == Types.VARCHAR) return "VARCHAR";
        if (type == Types.FLOAT) return "FLOAT";
        if (type == Types.NUMERIC) return "NUMERIC";
        if (type == Types.INTEGER) return "INT";
        if (type == Types.BIT) return "BIT";
        if (type == Types.BINARY) return "BINARY";
        if (type == Types.DATE) return "DATE";
        return "CHAR";
    }

    /**
  'C' Char (max 254 bytes)
  'N' '-.0123456789' (max 19 bytes)
  'L' 'YyNnTtFf?' (1 byte)
  'M' 10 digit .DBT block number
  'D' 8 digit YYYYMMDD
  */
    static int typeToSQLType(String type) {
        if (type.equals("C")) return java.sql.Types.CHAR;
        if (type.equals("N")) return java.sql.Types.FLOAT;
        if (type.equals("I")) return java.sql.Types.INTEGER;
        if (type.equals("L")) return java.sql.Types.CHAR;
        if (type.equals("M")) return java.sql.Types.INTEGER;
        if (type.equals("D")) return java.sql.Types.DATE;
        return java.sql.Types.CHAR;
    }
}
