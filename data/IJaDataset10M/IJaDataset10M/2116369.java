package net.sf.util.persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.sf.component.config.ConfigHelper;
import net.sf.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * XML的实现，不支持对象之间的关联
 * 仅支持LocalProperty
 */
public class EntryDataManager implements IDataManager {

    private static String store = ConfigHelper.getDataHome() + "data/";

    static {
        File f = new File(store);
        if (!f.exists()) f.mkdirs();
    }

    private Class dataClass;

    private String entityName;

    private String entityListName;

    private String dataFile;

    private Set fields;

    public EntryDataManager(Class dataClass) throws DataException {
        try {
            if (!(dataClass.newInstance() instanceof IEntry)) throw new DataException("仅支持IEntry类型实体");
            String[] s = ((IEntry) dataClass.newInstance()).getFieldNames();
            fields = new HashSet(Arrays.asList(s));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.dataClass = dataClass;
        this.entityName = dataClass.getSimpleName().toLowerCase();
        this.entityListName = entityName + "list";
        this.dataFile = store + entityName + ".xml";
    }

    public List<IEntry> readList() throws DataException {
        if (!fileExists()) return new ArrayList<IEntry>(0);
        ArrayList al = new ArrayList();
        try {
            Document doc = getDocument();
            Node object = doc.getFirstChild().getFirstChild();
            while (object != null) {
                if (object.getNodeType() == Document.ELEMENT_NODE) al.add(populate(object));
                object = object.getNextSibling();
            }
        } catch (Exception e) {
            throw new DataException(e);
        }
        return al;
    }

    public List<IEntry> readList(Serializable id) throws DataException {
        this.dataFile = store + entityName + id + ".xml";
        return readList();
    }

    public Serializable[] createList(List list, Serializable id) throws DataException {
        this.dataFile = store + entityName + id + ".xml";
        return createList(list);
    }

    public List<IEntry> readList(String propertyName, Object propertyValue) throws DataException {
        List<IEntry> list = readList();
        if (StringUtil.isNull(propertyName) || StringUtil.isNull(propertyValue)) return list;
        propertyName = propertyName.trim();
        if (propertyValue instanceof String) propertyValue = ((String) propertyValue).trim();
        List<IEntry> al = new ArrayList<IEntry>();
        for (IEntry o : list) {
            try {
                if (propertyValue.equals(o.getProperties().get(propertyName))) al.add(o);
            } catch (Exception e) {
                System.out.println("读列表时取值出错");
            }
        }
        list.clear();
        list = null;
        return al;
    }

    public Serializable[] createList(List list) throws DataException {
        if (list.isEmpty()) return null;
        try {
            Writer fw = new OutputStreamWriter(new FileOutputStream(dataFile, false), "UTF-8");
            fw.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            fw.write("<" + entityListName + " ry=\"" + ConfigHelper.getRy() + "\" bm=\"" + ConfigHelper.getBm() + "\">\n");
            for (Object o : list) fw.write("\t" + ((IEntry) o).toXML() + "\n");
            fw.write("</" + entityListName + ">");
            fw.flush();
            fw.close();
        } catch (IOException e) {
            throw new DataException("生成存储文件出错");
        }
        return null;
    }

    public IData readData(Serializable id) throws DataException {
        throw new DataException("未实现此方法");
    }

    public Serializable createData(IData data) throws DataException {
        throw new DataException("未实现此方法");
    }

    public void updateData(IData data) throws DataException {
        throw new DataException("未实现此方法");
    }

    public void updateList(List list) throws DataException {
        throw new DataException("未实现此方法");
    }

    public void deleteData(IData data) throws DataException {
        throw new DataException("未实现此方法");
    }

    public void deleteList(List list) throws DataException {
        throw new DataException("未实现此方法");
    }

    private Document getDocument() throws DataException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
            return db.parse(new File(dataFile));
        } catch (Exception e) {
            throw new DataException(e);
        }
    }

    private boolean fileExists() {
        return new File(dataFile).exists();
    }

    private IData populate(Node node) throws InstantiationException, IllegalAccessException {
        IEntry data = (IEntry) dataClass.newInstance();
        NamedNodeMap nnm = node.getAttributes();
        for (int i = 0; i < nnm.getLength(); i++) {
            if (fields.contains(nnm.item(i).getNodeName())) {
                data.setProperty(nnm.item(i).getNodeName(), nnm.item(i).getNodeValue());
            }
        }
        return (IData) data;
    }
}
