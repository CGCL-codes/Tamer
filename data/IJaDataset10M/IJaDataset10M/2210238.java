package util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Table;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class DomainDataBinder {

    private Node rootNode;

    private NodeList dataItems;

    private NodeList viewItems;

    private XPath xpath;

    private int idCounter;

    private int dataLength;

    /**
	 * �R���X�g���N�^�ɂ�XML�t�@�C�����i�[����Ă���InputStream�� �w�肷��B
	 * 
	 * @param ins
	 */
    public DomainDataBinder(InputStream ins) {
        xpath = XPathFactory.newInstance().newXPath();
        InputSource source = new InputSource(ins);
        try {
            rootNode = (Node) xpath.evaluate("/", source, XPathConstants.NODE);
            dataItems = (NodeList) xpath.evaluate("/bind/data/item", rootNode, XPathConstants.NODESET);
            viewItems = (NodeList) xpath.evaluate("/bind/view/item", rootNode, XPathConstants.NODESET);
            Element dataRoot = (Element) xpath.evaluate("/bind/data", rootNode, XPathConstants.NODE);
            String dataLengthString = dataRoot.getAttribute("length");
            if (dataLengthString != "") {
                dataLength = Integer.parseInt(dataLengthString);
            }
            bindViewWithData();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
	 * 1. /bind/data�v�f�̒���type������/bind/view�v�f�̊eitem�Ɋ��蓖�Ă�B���蓖�Ă̍ہAid�������g�p���� 2.
	 * /bind/view/item�v�f�̒���id="ID"�̂��̂�����΁AID��p�̑��������蓖�Ă�
	 * 
	 */
    private void bindViewWithData() {
        for (int i = 0; i < viewItems.getLength(); i++) {
            Element item = (Element) viewItems.item(i);
            NamedNodeMap attrs = item.getAttributes();
            Node attr = attrs.getNamedItem("id");
            if (attr != null) {
                String idName = attr.getNodeValue();
                try {
                    if (idName.equals("ID")) {
                        item.setAttribute("label", "ID");
                        item.setAttribute("type", "int");
                    } else {
                        Element boundItem = (Element) xpath.evaluate("/bind/data/item[@id=\"" + idName + "\"]", rootNode, XPathConstants.NODE);
                        String typeName = boundItem.getAttribute("type");
                        item.setAttribute("type", typeName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
	 * �h���C���f�[�^��`�t�@�C���Ɋ�Â��āA�C���v�b�g����h���C���f�[�^��ǂݍ���
	 * 
	 * @param dis
	 * @return
	 * @throws IOException
	 */
    public List<Object> load(DataInputStream dis) throws IOException {
        Map<String, Object> tempDatum = new HashMap<String, Object>();
        for (int i = 0; i < dataItems.getLength(); i++) {
            Element item = (Element) dataItems.item(i);
            String type = item.getAttribute("type");
            String id = item.getAttribute("id");
            if (type.equals("String")) {
                tempDatum.put(id, dis.readUTF());
            } else if (type.equals("int")) {
                tempDatum.put(id, new Integer(dis.readInt()));
            } else if (type.equals("byte")) {
                tempDatum.put(id, new Byte(dis.readByte()));
            } else if (type.equals("bool")) {
                tempDatum.put(id, new Boolean(dis.readBoolean()));
            }
        }
        List<Object> domainDatum = new ArrayList<Object>();
        for (int i = 0; i < viewItems.getLength(); i++) {
            Element item = (Element) viewItems.item(i);
            String id = item.getAttribute("id");
            if (id.equals("ID")) {
                domainDatum.add(new Integer(idCounter));
            } else {
                domainDatum.add(tempDatum.get(id));
            }
        }
        return domainDatum;
    }

    /**
	 * �h���C���f�[�^��`�t�@�C���Ɋ�Â��āA�h���C���f�[�^���A�E�g�v�b�g�ɏ�������
	 * 
	 * @param dos
	 * @param domainDatum
	 * @throws IOException
	 */
    public void save(DataOutputStream dos, List<Object> domainDatum) throws IOException {
        Map<String, Object> tempDatum = new HashMap<String, Object>();
        for (int i = 0; i < viewItems.getLength(); i++) {
            Element item = (Element) viewItems.item(i);
            String id = item.getAttribute("id");
            tempDatum.put(id, domainDatum.get(i));
        }
        for (int i = 0; i < dataItems.getLength(); i++) {
            Element item = (Element) dataItems.item(i);
            String id = item.getAttribute("id");
            String type = item.getAttribute("type");
            if (type.equals("String")) {
                dos.writeUTF(((String) tempDatum.get(id)));
            } else if (type.equals("int")) {
                dos.writeInt(((Integer) tempDatum.get(id)).intValue());
            } else if (type.equals("byte")) {
                dos.writeByte(((Byte) tempDatum.get(id)).byteValue());
            } else if (type.equals("bool")) {
                dos.writeBoolean(((Boolean) tempDatum.get(id)).booleanValue());
            }
        }
    }

    /**
	 * �h���C���f�[�^��`�t�@�C����/bind/data@length�v�f�Ɋ�Â���
	 * ��Ŏw�肳�ꂽ�h���C���f�[�^�ɒl��ǂݍ���
	 * @param data
	 * @param dis
	 * @throws IOException 
	 */
    public void loadData(DomainData<List<Object>> data, DataInputStream dis) throws IOException {
        int localDataLength = dataLength;
        if (localDataLength == 0) {
            localDataLength = dis.readByte();
        }
        for (int i = 0; i < localDataLength; i++) {
            setIdCounter(i);
            try {
                data.add(load(dis));
            } catch (IOException e) {
                data.add(getNullData());
            }
        }
    }

    public void saveData(DomainData<List<Object>> data, DataOutputStream dos) throws IOException {
        int localDataLength = data.getList().size();
        if (dataLength == 0) {
            dos.writeByte(localDataLength);
        }
        for (int i = 0; i < localDataLength; i++) {
            save(dos, data.getList().get(i));
        }
    }

    public int getIdCounter() {
        return idCounter;
    }

    public void setIdCounter(int idCounter) {
        this.idCounter = idCounter;
    }

    /**
	 * �h���C���f�[�^��`�t�@�C���Ɋ�Â��āA���̃f�[�^�ł̃f�t�H���g�f�[�^��ԋp����
	 * 
	 * @return �f�t�H���g�f�[�^
	 */
    public List<Object> getNullData() {
        List<Object> result = new ArrayList<Object>();
        for (int i = 0; i < viewItems.getLength(); i++) {
            Element item = (Element) viewItems.item(i);
            String id = item.getAttribute("id");
            String type = item.getAttribute("type");
            if (id.equals("ID")) {
                result.add(new Integer(idCounter));
            } else if (type.equals("String")) {
                result.add("");
            } else if (type.equals("int")) {
                result.add(new Integer(0));
            } else if (type.equals("byte")) {
                result.add(new Byte((byte) 0));
            } else if (type.equals("bool")) {
                result.add(new Boolean(false));
            } else {
                result.add(new Object());
            }
        }
        return result;
    }

    /**
	 * �h���C���f�[�^�Ɋ�Â��āA���x���ꗗ��ԋp
	 * 
	 * @return �s���x���ꗗ(�v���p�e�B�ɂ����p)
	 */
    public String[] getLabels() {
        String[] result = new String[viewItems.getLength()];
        for (int i = 0; i < viewItems.getLength(); i++) {
            Element item = (Element) viewItems.item(i);
            String labelName = item.getAttribute("label");
            if (labelName != null) {
                result[i] = labelName;
            } else {
                result[i] = Integer.toString(i);
            }
        }
        return result;
    }

    /**
	 * �h���C���f�[�^�Ɋ�Â��āA���x����ԋp
	 * @param columnImdex ���x���̍���
	 * @return ���x���B�Y���Ȃ��̏ꍇ�A�󔒂�Ԃ�
	 */
    public String getLabel(int columnIndex) {
        Element item = (Element) viewItems.item(columnIndex);
        String labelName = item.getAttribute("label");
        if (labelName != null) {
            return labelName;
        }
        return "";
    }

    /**
	 * �h���C���f�[�^�Ɋ�Â��āA�e�[�u���̃G�f�B�^��ԋp �������A�Ή����Ă��Ȃ��f�[�^�^�C�v�ɂ�null��Ԃ�
	 * 
	 * @param table
	 * @return
	 */
    public CellEditor[] getCellEditors(Table table) {
        CellEditor[] result = new CellEditor[viewItems.getLength()];
        for (int i = 0; i < viewItems.getLength(); i++) {
            Element item = (Element) viewItems.item(i);
            String editorName = item.getAttribute("editor");
            if (editorName.equals("Text")) {
                result[i] = new TextCellEditor(table);
            } else if (editorName.equals("Check")) {
                result[i] = new CheckboxCellEditor(table);
            } else {
                result[i] = null;
            }
        }
        return result;
    }

    /**
	 * �o�C���h�f�[�^���Q�l�ɂ��āA��ԍ�����\�ɕ\������ׂ���������o�͂���B �o�̓f�[�^��viewItem�v�f��type�����ɂĔ��ʂ���
	 * 
	 * @param element
	 * @param columnIndex
	 * @return ��ԍ��ƃh���C���f�[�^�Ɋ�Â����\�ɕ\�����镶����
	 */
    public String getColumnText(Object element, int columnIndex) {
        Element item = (Element) viewItems.item(columnIndex);
        String typeName = item.getAttribute("type");
        Object resultTemp = ((List<?>) element).get(columnIndex);
        if (typeName.equals("String")) {
            return (String) resultTemp;
        } else if (typeName.equals("int")) {
            return Integer.toString(((Integer) resultTemp).intValue());
        } else if (typeName.equals("byte")) {
            return Byte.toString(((Byte) resultTemp).byteValue());
        } else if (typeName.equals("bool")) {
            return Boolean.toString(((Boolean) resultTemp).booleanValue());
        }
        return null;
    }

    /**
	 * �o�C���h�f�[�^���Q�l�ɂ��āA��ԍ�����摜�\���̂��߂̃q���g��������o�͂���B ���ɉ摜�\�����w�肳��Ă��Ȃ���΁A""��Ԃ�
	 * 
	 * @param element
	 * @param columnIndex
	 * @return �摜�q���g������
	 */
    public String getImageName(Object element, int columnIndex) {
        Element item = (Element) viewItems.item(columnIndex);
        return item.getAttribute("image");
    }

    /**
	 * editor�����������Ă����true�A��������Ȃ���false
	 * 
	 * @param element
	 * @param property
	 * @return
	 */
    public boolean canModify(String property) {
        for (int i = 0; i < viewItems.getLength(); i++) {
            Element target = (Element) viewItems.item(i);
            String labelName = target.getAttribute("label");
            if (labelName.equals(property)) {
                return target.hasAttribute("editor");
            }
        }
        return false;
    }

    /**
	 * �o�C���h�f�[�^���Q�l�ɂ��āA���x��(�v���p�e�B)����G�f�B�^�o�͂���f�[�^��Ԃ�
	 * 
	 * @param datum
	 * @param property
	 * @return
	 */
    public Object getValue(List<Object> datum, String property) {
        try {
            for (int i = 0; i < viewItems.getLength(); i++) {
                Element target = (Element) viewItems.item(i);
                String labelName = target.getAttribute("label");
                if (labelName.equals(property)) {
                    String editorName = target.getAttribute("editor");
                    String typeName = target.getAttribute("type");
                    if (editorName.equals("")) {
                        return null;
                    } else if (typeName.equals("")) {
                        return null;
                    } else if (editorName.equals("Text")) {
                        if (typeName.equals("String")) {
                            return datum.get(i);
                        } else if (typeName.equals("byte")) {
                            return ((Byte) datum.get(i)).toString();
                        } else if (typeName.equals("int")) {
                            return ((Integer) datum.get(i)).toString();
                        } else if (typeName.equals("bool")) {
                            return ((Boolean) datum.get(i)).toString();
                        }
                    } else if (editorName.equals("Check")) {
                        if (typeName.equals("bool")) {
                            return datum.get(i);
                        } else {
                            return new Boolean(false);
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            return "0";
        } catch (NullPointerException e) {
        }
        return "";
    }

    /**
	 * �o�C���h�f�[�^���Q�l�ɁA�G�f�B�^����߂��Ă����I�u�W�F�N�g���h���C���f�[�^�ɔ��f����
	 * 
	 * @param datum
	 * @param property
	 * @param value
	 * @return �h���C���f�[�^���ύX���ꂽ���ǂ���
	 */
    public boolean modify(List<Object> datum, String property, Object value) {
        for (int i = 0; i < viewItems.getLength(); i++) {
            Element target = (Element) viewItems.item(i);
            String labelName = target.getAttribute("label");
            if (labelName.equals(property)) {
                String typeName = target.getAttribute("type");
                if (value instanceof String) {
                    String strValue = (String) value;
                    if (typeName.equals("String")) {
                        datum.set(i, strValue);
                        return true;
                    } else if (typeName.equals("int")) {
                        int input = 0;
                        try {
                            input = Integer.parseInt(strValue);
                        } catch (NumberFormatException e) {
                        }
                        datum.set(i, new Integer(input));
                        return true;
                    } else if (typeName.equals("byte")) {
                        byte input = 0;
                        try {
                            input = Byte.parseByte(strValue);
                        } catch (NumberFormatException e) {
                        }
                        datum.set(i, new Byte(input));
                        return true;
                    } else if (typeName.equals("bool")) {
                        datum.set(i, Boolean.valueOf(strValue.equals("true")));
                        return true;
                    }
                } else if (value instanceof Integer) {
                    int intValue = ((Integer) value).intValue();
                    if (typeName.equals("String")) {
                        datum.set(i, Integer.toString(intValue));
                        return true;
                    } else if (typeName.equals("int")) {
                        datum.set(i, new Integer(intValue));
                        return true;
                    } else if (typeName.equals("byte")) {
                        datum.set(i, new Byte((byte) intValue));
                        return true;
                    } else if (typeName.equals("bool")) {
                        datum.set(i, new Boolean(intValue != 0));
                        return true;
                    }
                } else if (value instanceof Boolean) {
                    Boolean boolValue = (Boolean) value;
                    if (typeName.equals("String")) {
                        datum.set(i, boolValue.toString());
                        return true;
                    } else if (typeName.equals("int")) {
                        if (boolValue.booleanValue()) {
                            datum.set(i, new Integer(1));
                        } else {
                            datum.set(i, new Integer(0));
                        }
                        return true;
                    } else if (typeName.equals("byte")) {
                        if (boolValue.booleanValue()) {
                            datum.set(i, new Byte((byte) 1));
                        } else {
                            datum.set(i, new Byte((byte) 0));
                        }
                        return true;
                    } else if (typeName.equals("bool")) {
                        datum.set(i, boolValue);
                    }
                }
            }
        }
        return false;
    }

    /**
	 * ���̑��̗p�r�̂��߁A�����̕ێ����Ă���XML�f�[�^��XPATH�̌��ʂ�Ԃ�
	 * @param query
	 * @return
	 */
    public String xpath(String query) {
        try {
            return (String) xpath.evaluate(query, rootNode);
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
	 * �R�s�[�y�[�X�g�̂��߂ɕ�������^�u��؂�ɂ��Ē���t����
	 * @param datum
	 * @param index
	 * @param line
	 */
    public void modifyLine(List<Object> datum, int index, String line) {
        String[] cells = line.split("\t");
        for (int i = index; i < viewItems.getLength(); i++) {
            Element target = (Element) viewItems.item(i);
            String labelName = target.getAttribute("label");
            if ((i - index) < cells.length) {
                modify(datum, labelName, cells[i - index]);
            }
        }
    }

    /**
	 * �\�̗񐔂�Ԃ��B�f�[�^���g�ݍ��܂�Ă��Ȃ��ꍇ�A0��Ԃ�
	 * @return �񐔁B�f�[�^���w��̏ꍇ�A0
	 */
    public int getLength() {
        if (viewItems != null) {
            return viewItems.getLength();
        }
        return 0;
    }
}
