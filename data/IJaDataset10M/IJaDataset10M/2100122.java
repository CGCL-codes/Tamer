package diuf.diva.hephaistk.xml.emma;

import java.io.Serializable;
import java.util.Vector;
import diuf.diva.hephaistk.Constants;
import diuf.diva.hephaistk.xml.Attribute;
import diuf.diva.hephaistk.xml.HephaisTKParsingException;
import diuf.diva.hephaistk.xml.XmlGeneric;

public class Info extends Emma_Generic implements Serializable {

    private static final long serialVersionUID = -8157806454530844763L;

    protected final String TYPE = "INFO";

    public static final String NAME = "info";

    private Vector<XmlGeneric> instanceDatas = null;

    private Attribute attId = null;

    public Info() {
        super();
        setName(NAME);
        instanceDatas = new Vector<XmlGeneric>();
    }

    public XmlGeneric getInstanceData(int i) {
        return instanceDatas.get(i);
    }

    public void addInstanceData(XmlGeneric instanceData) {
        this.instanceDatas.add(instanceData);
    }

    public Vector<XmlGeneric> getInstanceDatas() {
        return instanceDatas;
    }

    public int instanceDatasSize() {
        return instanceDatas.size();
    }

    /**
	 * @return Returns the attId.
	 */
    public Attribute getAttId() {
        return attId;
    }

    /**
	 * @param attId The attId to set.
	 */
    public void setAttId(String attId) {
        this.attId = new Attribute("id", attId);
    }

    @Override
    public void addElement(XmlGeneric element) {
        addInstanceData(element);
    }

    @Override
    public void addAttribute(Attribute attribute) throws HephaisTKParsingException {
        super.addAttribute(attribute);
        String attName = attribute.getName();
        if (attName.indexOf(':') != -1) {
            attName = attName.substring(attName.indexOf(':') + 1);
        }
        addAttribute(attName, attribute.getValue());
    }

    @Override
    public void addAttribute(String name, String value) throws HephaisTKParsingException {
        if (name.equals("id")) {
            setAttId(value);
        } else {
            throw new HephaisTKParsingException("emma.info -- unexpected attribute: " + name);
        }
    }

    @Override
    public String toString(int indent) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            ret.append(' ');
        }
        ret.append("<emma:info");
        ret.append(" id=\"" + attId.getValue() + "\"");
        ret.append(">");
        for (int i = 0; i < cData.size(); i++) {
            ret.append(cData.get(i));
        }
        ret.append("\n");
        for (int i = 0; i < instanceDatas.size(); i++) {
            ret.append(instanceDatas.get(i).toString(indent + Constants.INDENT_PREFIX));
        }
        for (int i = 0; i < indent; i++) {
            ret.append(' ');
        }
        ret.append("</emma:info>\n");
        return ret.toString();
    }
}
