package com.xy.sframe.component.xml;

import java.util.HashMap;

/**
 * 
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Element implements java.io.Serializable {

    /**
   * ����ֵ����Ҷ�ӽ��ֵΪNULL
   */
    Object value = null;

    /**
   * ����·��������
   */
    private String name;

    /**
   * ȫ·������
   */
    private String fullPathName;

    private Attribute attributes;

    private Item parent;

    public Element() {
    }

    public Element(String n, Object p) {
        name = n;
        parent = (Item) p;
    }

    public String getName() {
        return name;
    }

    public Item getParent() {
        return parent;
    }

    public boolean equalName(String nodeName) {
        return name == null ? false : name.equals(nodeName);
    }

    public void setFullPathName(String s) {
        fullPathName = s;
    }

    public String getFullPathName() {
        return fullPathName;
    }

    public boolean equalFullPathName(String s) {
        return s == null ? false : s.equals(fullPathName);
    }

    public Element getItem(String full_path_name) {
        return null;
    }

    public boolean accept(Visitor visitor) {
        return true;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object o) {
        value = o;
    }

    /**
   * ���������б�
   * @param h �����б�
   * @param names �������б�
   */
    public void setAttributes(HashMap h, String[] names) {
        this.attributes = new Attribute(names, h);
    }

    public void setAttributes(Attribute attr) {
        this.attributes = attr;
    }

    /**
   * ȡ������
   * @return ����ֵ
   */
    public Attribute getAttributes() {
        return attributes;
    }

    /**
   * ȡ���������б�
   * @return �������б�
   */
    public String[] getAttributesName() {
        return attributes.getNames();
    }

    /**
   * ȡ������ֵ
   * @param name ������
   * @return ����ֵ
   */
    public String getAttributeValue(String name) {
        return attributes.getAttributeValue(name);
    }

    /**
   * ȡ�����Ը���
   * @return ���Ը���
   */
    public int getAttributeCount() {
        if (attributes == null) {
            return 0;
        }
        return attributes.getAttributeCount();
    }

    /**
   * ȡ��ĳ��λ�õ�������
   * @param idx  λ�ú�
   * @return  ������
   */
    public String getAttributeName(int idx) {
        return attributes.getAttributeName(idx);
    }

    /**
   * ȡ��ĳ��˳��ŵ����Ե�ֵ
   * @param idx  λ��
   * @return  ����ֵ
   */
    public String getAttributeValue(int idx) {
        return attributes.getAttributeValue(idx);
    }
}
