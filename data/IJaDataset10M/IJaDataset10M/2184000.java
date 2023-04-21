package org.gocha.data.zip;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.gocha.data.DataSet;
import org.gocha.data.Table;
import org.gocha.data.xml.XMLDataReader;
import org.gocha.data.xml.XMLDataWriter;
import org.gocha.data.xml.XMLSchemaStorage;
import org.gocha.text.TextUtil;
import org.gocha.xml.XMLUtil;
import org.w3c.dom.Document;

/**
 * Сохраняет данные DataSet в ZIP файл
 * @author gocha
 */
public class ZipStorage {

    public ZipStorage() {
    }

    protected String metaFileName() {
        return "meta.xml";
    }

    protected String schemaFileName() {
        return "schema.xml";
    }

    protected String generateTableFileName(int index, Table table) {
        return "" + index + "-table.xml";
    }

    public DataSet read(File input) throws Exception {
        if (input == null) {
            throw new IllegalArgumentException("input == null");
        }
        FileInputStream fIn = new FileInputStream(input);
        try {
            return read(fIn);
        } catch (Exception ex) {
            throw ex;
        } finally {
            fIn.close();
        }
    }

    public DataSet read(InputStream input) throws Exception {
        if (input == null) {
            throw new IllegalArgumentException("input == null");
        }
        Map<String, byte[]> mapUnpack = new HashMap<String, byte[]>();
        ZipInputStream zi = new ZipInputStream(input);
        while (true) {
            ZipEntry ze = zi.getNextEntry();
            if (ze == null) break;
            byte[] data = null;
            byte[] buff = new byte[1024];
            ByteArrayOutputStream memOut = new ByteArrayOutputStream();
            while (true) {
                int readed = zi.read(buff);
                if (readed < 0) break;
                memOut.write(buff, 0, readed);
            }
            data = memOut.toByteArray();
            mapUnpack.put(ze.getName(), data);
            zi.closeEntry();
        }
        if (!mapUnpack.containsKey(metaFileName())) return null;
        byte[] bsMeta = mapUnpack.get(metaFileName());
        String sMeta = new String(bsMeta, TextUtil.UTF8());
        if (sMeta == null) return null;
        MetaFile mf = MetaFile.parseXMLString(sMeta);
        if (mf == null) return null;
        if (mf.getSchemaFile() == null) return null;
        if (!mapUnpack.containsKey(mf.getSchemaFile())) return null;
        String sxmlSchema = new String(mapUnpack.get(mf.getSchemaFile()), TextUtil.UTF8());
        if (sxmlSchema == null) return null;
        Document xmlSchema = XMLUtil.parseXML(sxmlSchema);
        if (xmlSchema == null) return null;
        XMLSchemaStorage xSchema = new XMLSchemaStorage();
        DataSet ds = xSchema.restore(xmlSchema);
        if (ds == null) return null;
        Map<String, String> mapTable2File = mf.getMapTable2File();
        if (mapTable2File == null) return ds;
        XMLDataReader xReader = new XMLDataReader();
        for (Table table : ds.getTables()) {
            String tableName = table.getTableName();
            String tableFile = null;
            if (!mapTable2File.containsKey(tableName)) continue;
            tableFile = mapTable2File.get(tableName);
            if (tableFile == null) continue;
            if (!mapUnpack.containsKey(tableFile)) continue;
            byte[] tableDataBytes = mapUnpack.get(tableFile);
            String tableDataText = new String(tableDataBytes, TextUtil.UTF8());
            if (tableDataText == null) continue;
            xReader.readTable(table, tableDataText);
        }
        return ds;
    }

    public void write(File file, DataSet set) throws Exception {
        if (file == null) {
            throw new IllegalArgumentException("file == null");
        }
        if (set == null) {
            throw new IllegalArgumentException("set == null");
        }
        FileOutputStream fStream = null;
        try {
            fStream = new FileOutputStream(file);
            write(fStream, set);
        } finally {
            if (fStream != null) {
                fStream.close();
            }
        }
    }

    protected MetaFile createMetaFile() {
        return new MetaFile();
    }

    public void write(OutputStream output, DataSet set) throws Exception {
        if (output == null) {
            throw new IllegalArgumentException("output == null");
        }
        if (set == null) {
            throw new IllegalArgumentException("set == null");
        }
        ZipOutputStream zipout = new ZipOutputStream(output);
        MetaFile metafile = createMetaFile();
        writeSchema(set, zipout, metafile);
        writeTables(set, zipout, metafile);
        writeMeta(metafile, zipout);
        zipout.flush();
        zipout.close();
    }

    protected void writeMeta(MetaFile metafile, ZipOutputStream zipout) throws IOException {
        String smeta = metafile.toXMLString();
        byte[] bmeta = smeta != null ? smeta.getBytes(TextUtil.UTF8()) : null;
        if (bmeta != null) {
            ZipEntry zeFile = new ZipEntry(metaFileName());
            zipout.putNextEntry(zeFile);
            zipout.write(bmeta);
            zipout.closeEntry();
        }
    }

    protected void writeSchema(DataSet set, ZipOutputStream zipout, MetaFile metafile) throws Exception, IOException {
        XMLSchemaStorage xSchema = new XMLSchemaStorage();
        Document xmldocSchema = xSchema.store(set);
        String xmlSchema = xmldocSchema == null ? null : XMLUtil.toXMLString(xmldocSchema);
        if (xmlSchema == null) {
            throw new Exception("Не возможно сохранить XML схему");
        }
        ZipEntry zeSchema = new ZipEntry(schemaFileName());
        zipout.putNextEntry(zeSchema);
        byte[] bSchema = xmlSchema.getBytes(TextUtil.UTF8());
        zipout.write(bSchema, 0, bSchema.length);
        zipout.closeEntry();
        metafile.setSchemaFile(schemaFileName());
    }

    protected void writeTables(DataSet set, ZipOutputStream zipout, MetaFile metafile) throws IOException {
        XMLDataWriter xWriter = new XMLDataWriter();
        int index = -1;
        for (Table tbl : set.getTables()) {
            index++;
            String fileName = generateTableFileName(index, tbl);
            ZipEntry zeFile = new ZipEntry(fileName);
            zipout.putNextEntry(zeFile);
            xWriter.writeTable(zipout, "UTF8", tbl);
            zipout.closeEntry();
            metafile.getMapTable2File().put(tbl.getTableName(), fileName);
        }
    }
}
