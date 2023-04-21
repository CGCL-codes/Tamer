package org.fao.waicent.xmap2D.convers;

import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Vector;
import org.fao.waicent.util.DBFTableReader;
import org.fao.waicent.util.FileResource;
import org.fao.waicent.util.TableAttributes;
import org.fao.waicent.util.TableInfo;
import org.fao.waicent.util.TableInfoImpl;
import org.fao.waicent.util.TableReader;
import org.fao.waicent.util.io.EndianDataInputStream;
import org.fao.waicent.util.io.EndianDataOutputStream;
import xBaseJ.CharField;
import xBaseJ.DBF;
import xBaseJ.Field;
import xBaseJ.xBaseJException;

public class Shapefile implements Serializable {

    public static void main(String arg[]) throws Exception {
        Shapefile _shapefile = new Shapefile("C:/Program Files/ESRI/ArcExplorer/AETutor/Algeria/awscitie.shp", "C:/Program Files/ESRI/ArcExplorer/AETutor/Algeria/awscitie.dbf");
        Shapefile shapefile = new Shapefile(_shapefile.getShapeType(), _shapefile.getBounds(), _shapefile.getShapeRecords(), _shapefile.getTableAttributes());
        _shapefile.writeDBF("c:\\temp\\wbd06075.dbf");
        _shapefile.writeShapefile(new FileOutputStream("c:\\temp\\wbd06075.shp"));
        _shapefile.writeIndex(new FileOutputStream("c:\\temp\\wbd06075.shx"));
        System.exit(0);
    }

    static final int SHAPEFILE_ID = 9994;

    static final int VERSION = 1000;

    public static final int NULL = 0;

    public static final int POINT = 1;

    public static final int ARC = 3;

    public static final int POLYGON = 5;

    public static final int MULTIPOINT = 8;

    public static final int ARC_M = 23;

    public static final int UNDEFINED = -1;

    protected ShapefileHeader mainHeader;

    protected Vector records;

    TableAttributes table_attributes;

    public TableAttributes getTableAttributes() {
        return table_attributes;
    }

    /**     * Creates and initialised a shapefile from disk     * @param filename The filename (including path) of the shapefile     */
    public Shapefile(String filename, String db_name) throws Exception, ShapefileException {
        InputStream in = new FileInputStream(filename);
        EndianDataInputStream sfile = new EndianDataInputStream(in);
        table_attributes = new CodeLabelTableSetShapeImpl(new FileResource(db_name));
        init(sfile, new DBF(db_name));
    }

    public Shapefile(int shapeType, double[] bbox, ShapeRecord[] recs, TableAttributes ta) throws Exception {
        table_attributes = ta;
        mainHeader = new ShapefileHeader(shapeType, bbox, recs);
        records = new Vector();
        for (int i = 0; i < recs.length; i++) {
            records.add(recs[i]);
        }
    }

    private static class CodeLabelTableSetShapeImpl implements TableAttributes {

        FileResource tablename;

        TableReader r = null;

        boolean finished = false;

        TableInfo tableinfo;

        public TableInfo getTableInfo() {
            return tableinfo;
        }

        public TableReader getTableReader() throws IOException {
            return new DBFTableReader(tablename);
        }

        CodeLabelTableSetShapeImpl(FileResource tablename) throws IOException {
            this.tablename = tablename;
            TableReader r = getTableReader();
            try {
                r.readRow();
            } catch (Exception e) {
                throw new IOException("Error opening DBF file");
            }
            String columnNames[] = new String[r.getColumnCount()];
            int columnTypes[] = new int[r.getColumnCount()];
            int primaryKeyColumns[] = new int[r.getColumnCount()];
            for (int j = 0; j < r.getColumnCount(); j++) {
                columnNames[j] = r.getColumn(j);
                columnTypes[j] = TableInfo.COLUMN_TYPE_STRING;
                primaryKeyColumns[j] = j;
            }
            final int DEFAULT_COLUMN_LENGHT = 32;
            TableInfoImpl ti = new TableInfoImpl(tablename.getResource(), columnNames, columnTypes, r.getColumnCount(), primaryKeyColumns);
            for (int j = 0; j < r.getColumnCount(); j++) {
                ti.setColumnLength(j, DEFAULT_COLUMN_LENGHT);
            }
            tableinfo = ti;
            r.close();
        }

        public String getValue(int i) {
            try {
                return r.getValue(i);
            } catch (Exception e) {
                return null;
            }
        }

        public void rewind() {
            try {
                if (r != null) {
                    r.close();
                }
            } catch (Exception e) {
            }
            try {
                r = getTableReader();
                finished = false;
            } catch (IOException e) {
                throw new NullPointerException();
            }
        }

        public boolean readNext() {
            if (finished) {
                return false;
            }
            try {
                if (r.readRow() == -1) {
                    finished = true;
                    return false;
                } else {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }

        protected void finalize() throws Throwable {
            super.finalize();
            if (r != null) {
                r.close();
            }
        }
    }

    public Shapefile(int shapeType, double[] bbox, ShapeRecord[] recs) {
        mainHeader = new ShapefileHeader(shapeType, bbox, recs);
        records = new Vector();
        for (int i = 0; i < recs.length; i++) {
            records.add(recs[i]);
        }
    }

    /**     * Initialises a shapefile from disk.     * Use Shapefile(String) if you don't want to use LEDataInputStream directly (recomened)     * @param file A LEDataInputStream that conects to the shapefile to read     */
    public synchronized void init(EndianDataInputStream file, DBF shapeDB) throws Exception {
        mainHeader = new ShapefileHeader(file);
        if (mainHeader.getVersion() < VERSION) {
            System.err.println("Warning, Shapefile format (" + mainHeader.getVersion() + ") older that supported (" + VERSION + "), attempting to read anyway");
        }
        if (mainHeader.getVersion() > VERSION) {
            System.err.println("Warning, Shapefile format (" + mainHeader.getVersion() + ") newer that supported (" + VERSION + "), attempting to read anyway");
        }
        records = new Vector();
        ShapefileShape body;
        RecordHeader header;
        shapeDB.startTop();
        try {
            for (; ; ) {
                shapeDB.read();
                String stringdata[] = new String[shapeDB.fldcount()];
                for (int i = 0; i < shapeDB.fldcount(); i++) {
                    stringdata[i] = shapeDB.getField(i + 1).get();
                }
                header = new RecordHeader(file);
                int type = mainHeader.getShapeType();
                switch(type) {
                    case (POINT):
                        body = new ShapePoint(file);
                        break;
                    case (ARC):
                        body = new ShapeArc(file);
                        break;
                    case (POLYGON):
                        body = new ShapePolygon(file);
                        break;
                    case (ARC_M):
                        body = new ShapeArcM(file);
                        break;
                    default:
                        throw new ShapeTypeNotSupportedException("Shape type " + getShapeTypeDescription() + " [" + type + "] not suported");
                }
                records.addElement(new ShapeRecord(header, body));
            }
        } catch (EOFException e) {
        } catch (xBaseJException ej) {
            if (shapeDB.recno() != shapeDB.reccount()) {
                throw ej;
            }
        }
        mainHeader.setIndexLength(50 + 4 * records.size());
    }

    /**     * Saves a shapefile to and output stream.     * @param file A LEDataInputStream that conects to the shapefile to read     */
    public synchronized void writeShapefile(OutputStream os) throws IOException {
        EndianDataOutputStream file = null;
        try {
            BufferedOutputStream out = new BufferedOutputStream(os);
            file = new EndianDataOutputStream(out);
        } catch (Exception e) {
            System.err.println(e);
        }
        mainHeader.write(file);
        for (int i = 0; i < records.size(); i++) {
            ShapeRecord item = (ShapeRecord) records.elementAt(i);
            item.header.write(file);
            item.shape.write(file);
        }
        file.flush();
        file.close();
    }

    public void writeDBF(String dbfname) throws Exception {
        DBF shapeDBF = new DBF(dbfname, true);
        TableInfo ti = table_attributes.getTableInfo();
        int col_count = ti.getColumnCount();
        Field fields[] = new Field[col_count];
        for (int i = 0; i < col_count; i++) {
            fields[i] = new CharField(ti.getColumnName(i), ti.getColumnLength(i));
        }
        shapeDBF.addField(fields);
        table_attributes.rewind();
        while (table_attributes.readNext()) {
            for (int i = 0; i < col_count; i++) {
                fields[i].put(table_attributes.getValue(i));
            }
            shapeDBF.write();
        }
        shapeDBF.close();
    }

    public synchronized void writeIndex(OutputStream os) throws IOException {
        EndianDataOutputStream file = null;
        try {
            BufferedOutputStream out = new BufferedOutputStream(os);
            file = new EndianDataOutputStream(out);
        } catch (Exception e) {
            System.err.println(e);
        }
        mainHeader.writeToIndex(file);
        int pos = 50;
        int len = 0;
        for (int i = 0; i < records.size(); i++) {
            ShapeRecord item = (ShapeRecord) records.elementAt(i);
            file.writeInt(pos);
            len = item.header.getContentLength();
            file.writeInt(len);
            pos += len + 4;
        }
        file.flush();
        file.close();
    }

    /**     * Gets the number of records stored in this shapefile     * @return Number of records     */
    public int getRecordCount() {
        return records.size();
    }

    public Vector getRecords() {
        return records;
    }

    /**     * Returns a ShapefileShape     * If index is out of range a null ShapefileShape     * will be returned. (As an alternative     * I could throw an ArrayIndexOutOfBoundsException, comments please...)     * @param index The index of the record from which to extract the shape.     * @return A ShapefileShape from the given index.     */
    public ShapefileShape getShape(int index) {
        ShapeRecord r;
        try {
            r = (ShapeRecord) records.elementAt(index);
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            return null;
        }
        return r.getShape();
    }

    /**     * Returns an array of all the shapes in this shapefile.     * @return An array of all the shapes     */
    public ShapeRecord[] getShapeRecords() {
        ShapeRecord[] recs = new ShapeRecord[records.size()];
        ShapeRecord r;
        for (int i = 0; i < records.size(); i++) {
            r = (ShapeRecord) records.elementAt(i);
            recs[i] = r;
        }
        return recs;
    }

    public ShapefileShape[] getShapes() {
        ShapefileShape[] shapes = new ShapefileShape[records.size()];
        ShapeRecord r;
        for (int i = 0; i < records.size(); i++) {
            r = (ShapeRecord) records.elementAt(i);
            shapes[i] = r.getShape();
        }
        return shapes;
    }

    /**     * Gets the bounding box for the whole shape file.     * @return An array of four doubles in the form {x1,y1,x2,y2}     */
    public double[] getBounds() {
        return mainHeader.getBounds();
    }

    /**     * Gets the type of shape stored in this shapefile.     * @return An int indicating the type     * @see #getShapeTypeDescription()     * @see #getShapeTypeDescription(int type)     */
    public int getShapeType() {
        return mainHeader.getShapeType();
    }

    /**     * Returns a string for the shape type of index.     * @param index An int coresponding to the shape type to be described     * @return A string descibing the shape type     */
    public static String getShapeTypeDescription(int index) {
        switch(index) {
            case (NULL):
                return ("Null");
            case (POINT):
                return ("Points");
            case (ARC):
                return ("Arcs");
            case (ARC_M):
                return ("ArcsM");
            case (POLYGON):
                return ("Polygons");
            case (MULTIPOINT):
                return ("Multipoints");
            default:
                return ("Undefined");
        }
    }

    /**     * Returns a description of the shape type stored in this shape file.     * @return String containing description     */
    public String getShapeTypeDescription() {
        return getShapeTypeDescription(mainHeader.getShapeType());
    }
}

class ShapefileHeader implements Serializable {

    private int fileCode = -1;

    private int fileLength = -1;

    private int indexLength = -1;

    private int version = -1;

    private int shapeType = -1;

    private double[] bounds = new double[4];

    public ShapefileHeader(EndianDataInputStream file) throws Exception {
        fileCode = file.readInt();
        System.out.println("Filecode " + fileCode);
        if (fileCode != Shapefile.SHAPEFILE_ID) {
            System.err.println("WARNING filecode " + fileCode + " not a match for documented shapefile code " + Shapefile.SHAPEFILE_ID);
        }
        for (int i = 0; i < 5; i++) {
            System.out.println("blank " + file.readInt());
        }
        fileLength = file.readInt();
        System.out.println("***************IN FileLength" + fileLength);
        version = file.readIntLittleEndian();
        shapeType = file.readIntLittleEndian();
        System.out.println("IN Type" + shapeType);
        for (int i = 0; i < 4; i++) {
            bounds[i] = file.readDoubleLittleEndian();
        }
        file.skipBytes(32);
    }

    public ShapefileHeader(int shapeType, double[] bbox, ShapeRecord[] shapes) {
        System.out.println("ShapefileHeader constructed with type " + shapeType);
        this.shapeType = shapeType;
        version = Shapefile.VERSION;
        fileCode = Shapefile.SHAPEFILE_ID;
        bounds = bbox;
        fileLength = 0;
        for (int i = 0; i < shapes.length; i++) {
            fileLength += shapes[i].getLength();
            fileLength += 4;
        }
        fileLength += 50;
        indexLength = 50 + (4 * shapes.length);
        System.out.println("********************OUT fileLength = " + fileLength);
    }

    public void setFileLength(int fileLength) {
        this.fileLength = fileLength;
    }

    public void setIndexLength(int indexLength) {
        this.indexLength = indexLength;
    }

    public void setBounds(double[] bbox) {
        bounds = bbox;
    }

    public void write(EndianDataOutputStream file) throws IOException {
        int pos = 0;
        file.writeInt(fileCode);
        pos += 4;
        for (int i = 0; i < 5; i++) {
            file.writeInt(0);
            pos += 4;
        }
        file.writeInt(fileLength);
        pos += 4;
        file.writeIntLittleEndian(version);
        pos += 4;
        file.writeIntLittleEndian(shapeType);
        pos += 4;
        for (int i = 0; i < 4; i++) {
            pos += 8;
            file.writeDoubleLittleEndian(bounds[i]);
        }
        for (int i = 0; i < 4; i++) {
            file.writeDoubleLittleEndian(0.0);
            pos += 8;
        }
        System.out.println("Position " + pos);
    }

    public void writeToIndex(EndianDataOutputStream file) throws IOException {
        int pos = 0;
        file.writeInt(fileCode);
        pos += 4;
        for (int i = 0; i < 5; i++) {
            file.writeInt(0);
            pos += 4;
        }
        file.writeInt(indexLength);
        pos += 4;
        file.writeIntLittleEndian(version);
        pos += 4;
        file.writeIntLittleEndian(shapeType);
        pos += 4;
        for (int i = 0; i < 4; i++) {
            pos += 8;
            file.writeDoubleLittleEndian(bounds[i]);
        }
        for (int i = 0; i < 4; i++) {
            file.writeDoubleLittleEndian(0.0);
            pos += 8;
        }
        System.out.println("Position " + pos);
    }

    public int getShapeType() {
        return shapeType;
    }

    public int getVersion() {
        return version;
    }

    public double[] getBounds() {
        return bounds;
    }

    public String toString() {
        String res = new String("type " + fileCode + " size " + fileLength + " version " + version + " Shape Type " + shapeType);
        return res;
    }
}
