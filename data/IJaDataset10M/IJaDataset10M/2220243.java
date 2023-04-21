package com.manydesigns.portofino.base;

import com.manydesigns.portofino.util.Defs;
import com.manydesigns.portofino.util.Escape;
import com.manydesigns.portofino.util.Util;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Paolo Predonzani - paolo.predonzani@manydesigns.com
 * @author Angelo Lupo      - angelo.lupo@manydesigns.com
 */
public class MDBlobAttribute extends MDAttribute {

    public static final String copyright = "Copyright (c) 2005-2009, ManyDesigns srl";

    private boolean inline;

    private Integer maxDimension;

    private boolean preview;

    private Integer width;

    private Integer height;

    private MDClass ownerCls;

    private final String schema;

    private final String cvsWorkingDirectory;

    private final String cvsModule;

    /**
     * Creates a new instance of MDBlobAttribute
     */
    public MDBlobAttribute(MDClass ownerCls, int id, String name, String prettyName, Integer order, String groupName, boolean inName, boolean inDetails, boolean inSummary, boolean required, String description, boolean inline, Integer maxDimension, boolean preview, Integer widht, Integer height, MDThreadLocals threadLocals, Locale locale, String schema, String cvsWorkingDirectory, String cvsModule) throws Exception {
        super(ownerCls, id, name, prettyName, order, groupName, inName, inSummary, inDetails, required, description, threadLocals, locale);
        this.inline = inline;
        this.maxDimension = maxDimension;
        this.preview = preview;
        this.width = widht;
        this.height = height;
        this.ownerCls = ownerCls;
        this.schema = schema;
        this.cvsWorkingDirectory = cvsWorkingDirectory;
        this.cvsModule = cvsModule;
    }

    public int getPhysicalJdbcType() {
        return java.sql.Types.INTEGER;
    }

    public void visit(MDConfigVisitor visitor) {
        visitor.doBlobAttributePre(this);
        visitCommonAttribute(visitor);
        visitor.doBlobAttributePost();
    }

    public String formatValue(MDBlob value) {
        if (value == null) {
            return null;
        } else {
            return value.getName();
        }
    }

    public void setAttributeCastValue(MDObject obj, Object value) throws Exception {
        if (value == null) {
            obj.setBlobAttribute(this, null);
        } else if (value instanceof MDBlob) {
            obj.setBlobAttribute(this, (MDBlob) value);
        } else {
            throw new Exception(Util.getLocalizedString(Defs.MDLIBI18N, locale, "Incompatible_type"));
        }
    }

    public boolean isInline() {
        return inline;
    }

    public Integer getMaxDim() {
        return maxDimension;
    }

    public boolean isPreview() {
        return preview;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public int getMaxBlobId() throws Exception {
        Connection conn = threadLocals.getCurrentTransaction().getConnection();
        int oid = 0;
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            String query = MessageFormat.format("SELECT MAX(\"{0}\") AS \"maxid\" FROM \"{1}\".\"{2}\"", Escape.dbSchemaEscape(getName()), schema, Escape.dbSchemaEscape(ownerCls.getName()));
            st = conn.prepareStatement(query);
            st.clearParameters();
            rs = st.executeQuery();
            if (rs.next()) {
                oid = rs.getInt("maxid");
            } else oid = 1;
        } finally {
            try {
                rs.close();
            } catch (Exception e) {
            }
            try {
                st.close();
            } catch (Exception e) {
            }
        }
        int oidFS = getMaxBlobIdfromFileSystem();
        if (oid < oidFS) return oidFS;
        return oid;
    }

    private int getMaxBlobIdfromFileSystem() throws Exception {
        int maxId = 0;
        String mDCvsWorkingDirectory = ownerCls.getConfig().getMDCvsWorkingDirectory();
        String mDCvsModule = ownerCls.getConfig().getMDCvsModule();
        String pathBlobDir = mDCvsWorkingDirectory + File.separatorChar + mDCvsModule + File.separatorChar + Defs.BLOB_DIRECTORY;
        File blobDir = new File(pathBlobDir);
        String[] children = blobDir.list();
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                String filename = children[i];
                try {
                    int currentId = new Integer(filename);
                    if (currentId > maxId) maxId = currentId;
                } catch (NumberFormatException e) {
                } catch (Exception e) {
                    throw e;
                }
            }
        }
        return maxId;
    }

    public void writeBlob(String blobName, InputStream inputStream) throws Exception {
        FileOutputStream fileOutStream = null;
        File savedFile = null;
        try {
            String pathBlob = cvsWorkingDirectory + File.separatorChar + cvsModule + File.separatorChar + Defs.BLOB_DIRECTORY + File.separatorChar;
            new File(pathBlob).mkdirs();
            savedFile = new File(pathBlob + blobName);
            if (savedFile.exists()) {
                return;
            }
            fileOutStream = new FileOutputStream(savedFile);
            int c;
            while ((c = inputStream.read()) != -1) {
                fileOutStream.write(c);
            }
        } finally {
            if (inputStream != null) try {
                inputStream.close();
            } catch (Exception e) {
            }
            if (fileOutStream != null) try {
                fileOutStream.close();
            } catch (Exception e) {
            }
        }
        if (maxDimension != null && savedFile.length() > (long) (1024 * maxDimension.longValue())) {
            savedFile.delete();
            String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, locale, "Size_larger_than_the_permitted"), getName(), maxDimension);
            throw new Exception(msg);
        }
    }

    public static File[] getBlobListThumb(String pathBlob, final String blobName) {
        File pp = new File(pathBlob);
        File listaBlob[] = pp.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String n) {
                String f = new File(n).getName();
                if (f.startsWith(blobName + Defs.IGNORE_DOT_TH)) return true; else return false;
            }
        });
        return listaBlob;
    }

    public static void deleteOrphanBlobs(MDConfig config) throws Exception {
        Transaction tx = config.getCurrentTransaction();
        String pathBlob = getPathBlob(config);
        File listaBlob[] = getBlobList(pathBlob);
        if (listaBlob == null) return;
        List<String> filesList = new ArrayList<String>();
        for (File aListaBlob : listaBlob) {
            filesList.add(aListaBlob.getName());
        }
        for (MDAttribute attr : config.getAllAttributes()) {
            if (!(attr instanceof MDBlobAttribute)) continue;
            MDBlobAttribute blobAttr = (MDBlobAttribute) attr;
            String sql = "SELECT \"" + Escape.dbSchemaEscape(blobAttr.getName()) + "\" AS \"name\" FROM \"" + config.getSchema1() + "\".\"" + Escape.dbSchemaEscape(blobAttr.getOwnerClass().getName()) + "\" c";
            PreparedStatement st = null;
            ResultSet rs = null;
            try {
                st = tx.prepareStatement(sql);
                rs = st.executeQuery();
                while (rs.next()) {
                    String numb = rs.getString("name");
                    if (filesList != null && filesList.contains(numb)) {
                        filesList.remove(numb);
                    }
                }
            } finally {
                try {
                    rs.close();
                } catch (Exception e) {
                }
                try {
                    st.close();
                } catch (Exception e) {
                }
            }
        }
        for (String nameFile : filesList) {
            File delete = new File(pathBlob + nameFile);
            delete.delete();
            File[] blobThumb = getBlobListThumb(pathBlob, nameFile);
            for (File aBlobThumb : blobThumb) {
                aBlobThumb.delete();
            }
        }
    }

    public static String getPathBlob(MDConfig config) {
        return config.getMDCvsWorkingDirectory() + File.separatorChar + config.getMDCvsModule() + File.separatorChar + Defs.BLOB_DIRECTORY + File.separatorChar;
    }

    public static File[] getBlobList(String pathBlob) {
        File pp = new File(pathBlob);
        File listaBlob[] = pp.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String n) {
                String f = new File(n).getName();
                if (f.indexOf(Defs.IGNORE_DOT_TH) > 0 || f.equals(Defs.IGNORE_DIRECTORY_CVS)) return false; else return true;
            }
        });
        return listaBlob;
    }
}
