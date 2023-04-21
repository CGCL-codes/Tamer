package org.one.stone.soup.grfxML;

import org.one.stone.soup.mjdb.data.field.ArrayField;
import org.one.stone.soup.mjdb.data.field.DataLinker;
import org.one.stone.soup.mjdb.data.field.Field;
import org.one.stone.soup.mjdb.data.field.LockException;
import org.one.stone.soup.xml.XmlElement;

/**
	*
	* @author Nik Cross
*/
public class DataAnchors extends ArrayField implements ITag {

    public static String DEFAULT_ID = "_Anchors";

    private GrfxMLEngine engine;

    public DataAnchors(GrfxMLEngine engine) {
        this.engine = engine;
        try {
            setId(DEFAULT_ID, this);
        } catch (LockException le) {
        }
    }

    public void add(DataAnchor data, Object source) throws LockException {
        _add(data, source);
    }

    public void buildFromgrfxML(XmlElement xml) {
        String name = xml.getAttributeValueByName(Token.ID);
        if (name != null) {
            try {
                setId(name, this);
            } catch (LockException le) {
            }
        }
        XmlElement element;
        for (int loop = 0; loop < xml.getElementCount(); loop++) {
            element = xml.getElementByIndex(loop);
            if (element != null) {
                try {
                    DataAnchor newItem = (DataAnchor) GrfxMLProcessor.buildInstance(element, engine);
                    add(newItem, this);
                } catch (ClassNotFoundException ce) {
                    GrfxMLException.warning(GrfxMLException.WARNING_UNRECOGNISED_TAG_CODE, new Object[] { element.getName(), Token.ANCHORS, "" + element.getStartLineNo() });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void copyFromgrfxML(XmlElement xml) {
        XmlElement element;
        int loop;
        for (loop = 0; loop < xml.getElementCount(); loop++) {
            element = xml.getElementByIndex(loop);
            if (element != null) {
                if (loop < size()) {
                    get(loop).copyFromgrfxML(element);
                } else {
                    try {
                        Class clazz = Class.forName(NameHelper.toClassName(element.getName()));
                        DataAnchor newItem = (DataAnchor) clazz.newInstance();
                        clazz.getMethod(NameHelper.BUILD_METHOD, NameHelper.BUILD_SIG).invoke(newItem, new Object[] { element });
                        add(newItem, this);
                    } catch (ClassNotFoundException ce) {
                        GrfxMLException.warning(GrfxMLException.WARNING_UNRECOGNISED_TAG_CODE, new Object[] { element.getName(), Token.ANCHORS, "" + element.getStartLineNo() });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        while (loop < size()) {
            try {
                remove(size() - 1, this);
            } catch (LockException le) {
            }
        }
    }

    public DataAnchor get(int index) {
        return (DataAnchor) _get(index);
    }

    public DataAnchors getClone() {
        DataAnchors newAnchors = new DataAnchors(engine);
        for (int loop = 0; loop < size(); loop++) {
            try {
                newAnchors.add(((DataAnchor) get(loop)).getClone(), this);
            } catch (LockException le) {
            }
        }
        return newAnchors;
    }

    public DataAnchors getClone(String path) {
        DataAnchors newAnchors = new DataAnchors(engine);
        GrfxMLStoreProcessor.setCloneName(path, this, newAnchors);
        for (int loop = 0; loop < size(); loop++) {
            try {
                newAnchors.add(((DataAnchor) get(loop)).getClone(path), this);
            } catch (LockException le) {
            }
        }
        return newAnchors;
    }

    public void insert(DataAnchor data, int index, Object source) throws LockException {
        _insert(data, index, source);
    }

    public void register(DataLinker store) {
        super.register(store);
    }

    public void remove(int index, Object source) throws LockException {
        _remove(index, source);
    }

    public void remove(DataAnchor data, Object source) throws LockException {
        _remove(data, source);
    }

    public void replace(Field oldObj, Field newObj) {
        replace(oldObj, newObj, false);
    }

    public void replace(Field oldObj, Field newObj, boolean recursive) {
        for (int loop = 0; loop < size(); loop++) {
            if (get(loop) == oldObj) {
                try {
                    set((DataAnchor) newObj, loop, this);
                } catch (LockException le) {
                }
            }
            get(loop).replace(oldObj, newObj, recursive);
        }
    }

    public void resetChanged() {
        super.resetChanged();
    }

    public void set(DataAnchor data, int index, Object source) throws LockException {
        _set(data, index, source);
    }

    public String togrfxML(String name) {
        String xml = "<" + name;
        if (!NameHelper.isSystemName(getId())) {
            xml += " id=\"" + getId() + "\"";
        }
        xml += ">";
        for (int loop = 0; loop < size(); loop++) {
            try {
                xml += get(loop).togrfxML(NameHelper.systemToTagName((String) get(loop).getClass().getField("DEFAULT_ID").get(get(loop))));
            } catch (Exception e) {
            }
        }
        xml += "</" + name + ">\n";
        return xml;
    }

    public String togrfxML(String name, TagModifier modifier) {
        return togrfxML(name);
    }

    public String toString() {
        if (GrfxMLEngine.DEBUG) return (togrfxML(DEFAULT_ID.substring(1)));
        String xml = "<" + DEFAULT_ID.substring(1) + "/>";
        return xml;
    }
}
