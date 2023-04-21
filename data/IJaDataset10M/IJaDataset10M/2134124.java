package net.sf.zcatalog.xml;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import net.sf.zcatalog.db.*;
import java.io.*;
import java.util.*;
import net.sf.zcatalog.xml.jaxb.*;
import javax.xml.bind.*;
import net.sf.zcatalog.ui.Icons;

/**
 * A MIME type in a ZCatObject hierarchy as understood by ZCatalog .
 * @author Alessandro Zigliani
 * @version 0.9
 * @since ZCatalog 0.9
 * @todo bisogna uniformare alle altre
 */
public final class XMLMimeType implements ZCatObject {

    private net.sf.zcatalog.ui.Icon icon;

    private static XMLMimeType FOLDER;

    /**
     * This is the default type for a folder;
     */
    private static XMLMimeType OCTET_STREAM;

    /**
     * This is the default type for a file;
     */
    public static XMLMimeType OCTET_STREAM() {
        if (OCTET_STREAM == null) {
            MimeType t = JAXBGlobals.OBJ_FACTORY.createMimeType();
            t.setPrimaryType("application");
            t.setSubType("octet-stream");
            t.setZCatPrimaryType(ZCatPrimaryType.UNSUPPORTED);
            OCTET_STREAM = new XMLMimeType(t);
        }
        return OCTET_STREAM;
    }

    /**
     * This is the default type for a folder;
     */
    public static XMLMimeType FOLDER() {
        if (FOLDER == null) {
            MimeType t = new MimeType();
            t.setPrimaryType("folder");
            t.setSubType("plain");
            t.setZCatPrimaryType(ZCatPrimaryType.FOLDER);
            FOLDER = new XMLMimeType(t);
        }
        return FOLDER;
    }

    private static List<XMLMimeType> KNOWN_TYPES = null;

    private static List<XMLMimeType> KNOWN_TYPES() {
        if (KNOWN_TYPES == null) {
            loadDefaultMimeTypes();
        }
        return KNOWN_TYPES;
    }

    private net.sf.zcatalog.xml.jaxb.MimeType jaxbMimeType;

    private static void loadDefaultMimeTypes() {
        MimeTypeList types;
        Unmarshaller unmarshaller;
        XMLMimeType t;
        Iterator<net.sf.zcatalog.xml.jaxb.MimeType> i;
        List<net.sf.zcatalog.xml.jaxb.MimeType> list;
        ArrayList<XMLMimeType> rTypes = new ArrayList<XMLMimeType>(10);
        try {
            unmarshaller = JAXBGlobals.CONTEXT.createUnmarshaller();
            types = (MimeTypeList) unmarshaller.unmarshal(new StringReader(XMLResources.load("defaultMimeTypes.xml")));
            list = types.getMimeType();
            i = list.iterator();
            while (i.hasNext()) {
                rTypes.add(new XMLMimeType(i.next()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        KNOWN_TYPES = rTypes;
    }

    public XMLMimeType(net.sf.zcatalog.xml.jaxb.MimeType t) {
        jaxbMimeType = t;
    }

    private static TreeMap<String, XMLMimeType> FROM_EXT() {
        if (FROM_EXT == null) mapKnownTypes();
        return FROM_EXT;
    }

    private static TreeMap<String, XMLMimeType> FROM_EXT;

    private static TreeMap<String, XMLMimeType> FROM_GFL_NAME() {
        if (FROM_GFL_NAME == null) mapKnownTypes();
        return FROM_GFL_NAME;
    }

    private static TreeMap<String, XMLMimeType> FROM_GFL_NAME;

    private static void mapKnownTypes() {
        String proto[] = new String[0];
        XMLMimeType[] list = getKnownTypes();
        XMLMimeType cur;
        String extensions[];
        String gflName;
        FROM_EXT = new TreeMap<String, XMLMimeType>();
        FROM_GFL_NAME = new TreeMap<String, XMLMimeType>();
        for (int i = 0; i < list.length; i++) {
            cur = list[i];
            extensions = cur.getExtensions().toArray(proto);
            gflName = cur.getGFLName();
            for (int j = 0; j < extensions.length; j++) {
                FROM_EXT.put(extensions[j].toLowerCase(), cur);
                if (gflName != null) FROM_GFL_NAME.put(gflName, cur);
            }
        }
    }

    public static XMLMimeType getMimeType(File f) {
        int i = 0;
        String name = null, ext = null;
        XMLMimeType r = null;
        XMLMimeType rtemp;
        if (f.isDirectory()) {
            r = XMLMimeType.FOLDER();
        } else {
            name = f.getName();
            i = name.length() - 1;
            while ((i = name.lastIndexOf('.', i)) > 0) {
                ext = name.substring(i + 1).toLowerCase();
                rtemp = FROM_EXT().get(ext);
                if (rtemp != null) {
                    r = rtemp;
                }
                i--;
            }
        }
        return r == null ? XMLMimeType.OCTET_STREAM() : r;
    }

    public String getBaseType() {
        return jaxbMimeType.getPrimaryType() + "/" + jaxbMimeType.getSubType();
    }

    public static String getBaseType(File f) {
        return getMimeType(f).getBaseType();
    }

    public static String getPrimaryType(File f) {
        return getMimeType(f).getPrimaryType();
    }

    public static ZCatPrimaryType getZCatPrimaryType(File f) {
        return getMimeType(f).getZCatPrimaryType();
    }

    public ZCatPrimaryType getZCatPrimaryType() {
        ZCatPrimaryType t = jaxbMimeType.getZCatPrimaryType();
        return t == null ? ZCatPrimaryType.UNSUPPORTED : t;
    }

    public List<String> getExtensions() {
        return new ArrayList<String>(jaxbMimeType.getExtensions());
    }

    /**
     * @return
     */
    @Override
    public String getName() {
        return getBaseType();
    }

    public String getPrimaryType() {
        return jaxbMimeType.getPrimaryType();
    }

    public String getSubType() {
        return jaxbMimeType.getSubType();
    }

    public static XMLMimeType[] getKnownTypes() {
        return KNOWN_TYPES().toArray(new XMLMimeType[0]);
    }

    public String getGFLName() {
        return jaxbMimeType.getGflName();
    }

    public static XMLMimeType getFromGFLName(String name) {
        return FROM_GFL_NAME().get(name);
    }

    public static XMLMimeType getByExt(String ext) {
        return FROM_EXT().get(ext);
    }

    @Override
    public ImageIcon getIcon(IconSize size) {
        net.sf.zcatalog.ui.Icon i;
        if (icon == null) {
            i = Icons.getBundledIcon("art/file");
        } else i = icon;
        return i.getIcon(size);
    }

    @Override
    public ZCatDb getDb() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getChildrenCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getIndexOfChild(ZCatObject o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ZCatObject getChild(int n) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ZCatObject getParent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getXML() {
        try {
            Marshaller m = JAXBGlobals.CONTEXT.createMarshaller();
            StringWriter w = new StringWriter();
            m.marshal(JAXBGlobals.OBJ_FACTORY.createMime(jaxbMimeType), w);
            return w.toString();
        } catch (JAXBException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void adopt(ZCatObject o) {
        assert false : "Cannot adopt!";
    }

    @Override
    public boolean canAdopt(ZCatObject o) {
        return false;
    }

    @Override
    public boolean isBrowseable() {
        return false;
    }

    @Override
    public String toString() {
        return getBaseType();
    }
}
