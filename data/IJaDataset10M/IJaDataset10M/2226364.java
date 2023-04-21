package kr.ac.ssu.imc.durubi.report.designer.components;

/**
 * <p>Title: Report Designer</p>
 * <p>Description: ����Ʈ �����̳�</p>
 * <p>Copyright: Copyright (c) 2000</p>
 * <p>Company: ���Ǵ��б�, �ý��ۼ���Ʈ���� ������, DSS ��</p>
 * @author ������
 * @version 0.1
 */
public class DRUDSColumnData {

    String cName;

    String cType;

    String cCondition;

    String cVariable;

    public DRUDSColumnData() {
    }

    public DRUDSColumnData(String name, String type, String condition, String var) {
        this.cName = name;
        this.cType = type;
    }

    public void setCName(String name) {
        this.cName = name;
    }

    public String getCName() {
        return this.cName;
    }

    public void setCType(String type) {
        this.cType = type;
    }

    public String getCType() {
        return this.cType;
    }

    /** ���� ��ü�� ������ ��� XML ��Ҹ� �����Ѵ�.
   *  @param oDocument XML ��ť��Ʈ ��ü.
   *  @return XML ������Ʈ ��ü.
   */
    public org.w3c.dom.Element createElementNode(org.w3c.dom.Document tDocument) {
        org.w3c.dom.Element tElement = tDocument.createElement("rdUDSColumnData");
        tElement.setAttribute("name", getCName());
        tElement.setAttribute("type", getCType());
        return tElement;
    }
}
