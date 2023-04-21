package org.fao.waicent.attributes;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xpath.XPathAPI;
import org.fao.waicent.util.FileResource;
import org.fao.waicent.util.Log;
import org.fao.waicent.util.TableReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DataLegendExtent extends KeyedExtent {

    public DefinitionInterface getDefaultDefinition() {
        if (global_home != null && !global_home.trim().equals("")) {
            return new DataLegendDefinition(global_home, 1);
        } else {
            return new DataLegendDefinition();
        }
    }

    public DataLegendExtent(int key_size) {
        super(key_size);
    }

    String global_home = null;

    public DataLegendExtent(int key_size, String home) {
        super(key_size);
        this.global_home = home;
    }

    public DataLegendExtent(Element element) throws Exception {
        super((Element) XPathAPI.selectSingleNode(element, "KeyedExtent"));
    }

    public DataLegendExtent(Element element, DataInputStream in, int iSize) throws Exception {
        super((Element) XPathAPI.selectSingleNode(element, "KeyedExtent"), in, iSize);
    }

    public DataLegendExtent(DataInputStream in, Document doc) throws IOException {
        super(in, doc, doc.createElement("DataLegendExtent"));
    }

    public DataLegendExtent(DataInputStream in) throws IOException {
        super(in);
    }

    public DataLegendDefinition getDataLegendDefinition(Key key) {
        return getDataLegendDefinition(key, false);
    }

    public DataLegendDefinition getDataLegendDefinition(Key key, boolean strict) {
        DataLegendDefinition legend = (DataLegendDefinition) getSpecificDefinition(key, strict);
        return legend;
    }

    public DataLegendExtent(ExtentManager extents, FileResource filename, Log log) {
        super(extents.size());
        loadTableReader(extents, filename, log);
    }

    protected void loadTableReader(ExtentManager extents, FileResource filename, Log log) {
        boolean verbose = false;
        TableReader r = null;
        try {
            r = TableReader.createTableReader(filename);
            if (r != null && r.readRow() != -1) {
                while (!r.atEnd()) {
                    addDefinition(new DataLegendDefinition(r));
                }
            }
        } catch (IOException e) {
            log.logError(getClass().getName() + e, filename.getResource() + ":" + r.getRowNumber());
        } finally {
            if (r != null) {
                r.close();
            }
        }
    }

    /**     * jvg: called from EditDatasetLegend.jsp     **/
    public Element getKeyedExtentDOM() {
        Document metadata_document = new DocumentImpl();
        Element root_element = metadata_document.createElement("DataLegendExtent");
        Element keyed_element = metadata_document.createElement("KeyedExtent");
        for (int i = 0; i < size(); i++) {
            KeyedExtentEntry entry = at(i);
            Element entry_element = metadata_document.createElement("KeyedExtentEntry");
            entry_element.setAttribute("id", Integer.toString(i));
            entry_element.setAttribute("index", Integer.toString(entry.index));
            for (int j = 0; j < entry.key.size(); j++) {
                Element key_element = metadata_document.createElement("Key");
                key_element.setAttribute("id", Integer.toString(j));
                key_element.setAttribute("value", Integer.toString(entry.key.at(j)));
                entry_element.appendChild(key_element);
            }
            keyed_element.appendChild(entry_element);
        }
        for (int i = 0; i < getDefinitionSize(); i++) {
            DataLegendDefinition def = getDataLegendDefinition(i);
            Element definition_element = metadata_document.createElement("Definition");
            definition_element.setAttribute("index", Integer.toString(i));
            definition_element.setAttribute("code", def.getCode());
            definition_element.setAttribute("style", def.getStyle() + "");
            definition_element.setAttribute("size", def.size() + "");
            definition_element.setAttribute("round", Boolean.toString(def.isRound()) + "");
            definition_element.setAttribute("isDisplay", def.isDisplay() ? "true" : "false");
            def.getDataLegend(definition_element);
            keyed_element.appendChild(definition_element);
        }
        root_element.appendChild(keyed_element);
        metadata_document.appendChild(root_element);
        return root_element;
    }

    public DataLegendDefinition getDataLegendDefinition(int i) {
        return (DataLegendDefinition) (getDefinition(i));
    }

    public DataLegendExtent(DataInputStream in, Integer version) throws IOException {
        super(in, version);
    }

    public DataLegendDefinition getDataLegendDefinitionByCode(String code) {
        try {
            for (int i = 0; i < defn_extent.size(); i++) {
                if (((DataLegendDefinition) defn_extent.elementAt(i)).getCode().equals(code)) {
                    System.out.println((DataLegendDefinition) defn_extent.elementAt(i));
                    return ((DataLegendDefinition) defn_extent.elementAt(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void toXML(Document doc) throws Exception {
        super.toXML(doc, doc.createElement("DataLegendExtent"));
    }

    public void changeLanguage(String language) {
        for (int i = 0; i < getDefinitionSize(); i++) {
            getDataLegendDefinition(i).changeLanguage(language);
        }
    }

    public boolean ifExists(String name) {
        for (int i = 0; i < getDefinitionSize(); i++) {
            DataLegendDefinition def = getDataLegendDefinition(i);
            String code = def.getCode();
            if (code.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public int getIndex(DataLegendDefinition dld) {
        for (int i = 0; i < getDefinitionSize(); i++) {
            DataLegendDefinition def = getDataLegendDefinition(i);
            if (def == dld) {
                return i;
            }
        }
        return -1;
    }

    public void setKeyedExtent(Map parameters) {
        String KEY_PARAMETER = "key";
        Key n_key = null;
        Iterator iter = parameters.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            String val = ((String[]) parameters.get(key))[0];
            int begin_index = key.indexOf(KEY_PARAMETER);
            if (begin_index != -1) {
                int keyed_extent_id = Integer.parseInt(key.substring(0, begin_index));
                int key_id = Integer.parseInt(key.substring(begin_index + KEY_PARAMETER.length()));
                int key_val = Integer.parseInt(val);
                if (keyed_extent_id == -1) {
                    if (n_key == null) {
                        n_key = new Key(this.key_size);
                    }
                    n_key.set(key_val, key_id - 1);
                } else {
                    at(keyed_extent_id - 1).getKey().set(key_val, key_id - 1);
                }
            }
        }
    }
}
