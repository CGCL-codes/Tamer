package com.xy.sframe.component.xml;

import java.util.Stack;
import java.io.*;

/**
 * 
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PrintXml {

    public PrintXml() {
    }

    public void printToFile(String xmlString) {
        String newXmlString = "";
        try {
            newXmlString = ">" + xmlString + "<";
        } catch (java.lang.NullPointerException e) {
            System.out.println("û���ַ�������");
        }
        try {
            String[] seg = split(newXmlString, "><");
            ItemInfo[] itemInfo = new ItemInfo[seg.length];
            for (int i = 0; i < seg.length; i++) {
                if (i > 0) {
                    itemInfo[i] = testItemInfo(seg[i - 1], seg[i], itemInfo[i - 1].getWeight());
                    if (itemInfo[i].getWeight() < itemInfo[i - 1].getWeight()) {
                        itemInfo[i].setParent((itemInfo[i - 1].getParent()).getParent());
                    } else if (itemInfo[i].getWeight() == itemInfo[i - 1].getWeight()) {
                        itemInfo[i].setParent(itemInfo[i - 1].getParent());
                    } else {
                        itemInfo[i].setParent(itemInfo[i - 1]);
                    }
                } else {
                    itemInfo[i] = testItemInfo(seg[i]);
                    itemInfo[i].setParent(new ItemInfo(0, ""));
                }
            }
            for (int i = 0; i < itemInfo.length; i++) {
                String blank = "";
                for (int j = 1; j < itemInfo[i].getWeight(); j++) {
                    blank = blank + "  ";
                }
                System.out.println(blank + "<" + itemInfo[i].getItem() + ">");
            }
        } catch (java.lang.NullPointerException e) {
            System.out.println("PrintToFile�������null");
        }
    }

    public void printToFile(String xmlString, String fileName) {
        StringBuffer sb = new StringBuffer();
        String newXmlString = "";
        try {
            newXmlString = ">" + xmlString + "<";
        } catch (java.lang.NullPointerException e) {
            System.out.println("û���ַ�������");
        }
        try {
            String[] seg = split(newXmlString, "><");
            ItemInfo[] itemInfo = new ItemInfo[seg.length];
            for (int i = 0; i < seg.length; i++) {
                if (i > 0) {
                    itemInfo[i] = testItemInfo(seg[i - 1], seg[i], itemInfo[i - 1].getWeight());
                    if (itemInfo[i].getWeight() < itemInfo[i - 1].getWeight()) {
                        itemInfo[i].setParent((itemInfo[i - 1].getParent()).getParent());
                    } else if (itemInfo[i].getWeight() == itemInfo[i - 1].getWeight()) {
                        itemInfo[i].setParent(itemInfo[i - 1].getParent());
                    } else {
                        itemInfo[i].setParent(itemInfo[i - 1]);
                    }
                } else {
                    itemInfo[i] = testItemInfo(seg[i]);
                    itemInfo[i].setParent(new ItemInfo(0, ""));
                }
            }
            for (int i = 0; i < itemInfo.length; i++) {
                String blank = "";
                for (int j = 1; j < itemInfo[i].getWeight(); j++) {
                    blank = blank + "  ";
                }
                sb.append(blank + "<" + itemInfo[i].getItem() + ">" + "\n");
            }
            OutputStream f1 = new FileOutputStream(fileName);
            byte buf[] = (sb.toString()).getBytes();
            f1.write(buf);
            f1.close();
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Ŀ���ļ�������");
        } catch (java.lang.NullPointerException e) {
            System.out.println("PrintToFile�������null");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isLeaf(String nodeString) {
        boolean flag = false;
        if (nodeString.indexOf(">") > 0) flag = true;
        return flag;
    }

    public ItemInfo testItemInfo(String value) {
        if (value == null) {
            System.out.println("��ǰitemΪnull");
            return null;
        }
        return new ItemInfo(1, value);
    }

    public ItemInfo testItemInfo(String lastValue, String value, int weight) {
        if (lastValue == null) {
            System.out.println("֮ǰ��itemΪnull");
            return null;
        }
        if (value == null) {
            System.out.println("��ǰitemΪnull");
            return null;
        }
        if (weight < 0) {
            System.out.println("Ȩֵ����С��0");
            return null;
        }
        ItemInfo itemInfo = new ItemInfo(0, "");
        try {
            if (isLeaf(value)) {
                if (isLeaf(lastValue)) {
                    itemInfo = new ItemInfo(weight, value);
                } else if (lastValue.startsWith("/")) {
                    itemInfo = new ItemInfo(weight, value);
                } else {
                    itemInfo = new ItemInfo(weight + 1, value);
                }
            } else if (value.startsWith("/")) {
                if (isLeaf(lastValue)) {
                    itemInfo = new ItemInfo(weight - 1, value);
                } else if (lastValue.startsWith("/")) {
                    itemInfo = new ItemInfo(weight - 1, value);
                } else {
                    itemInfo = new ItemInfo(weight, value);
                }
            } else {
                if (isLeaf(lastValue)) {
                    itemInfo = new ItemInfo(weight, value);
                } else if (lastValue.startsWith("/")) {
                    itemInfo = new ItemInfo(weight, value);
                } else {
                    itemInfo = new ItemInfo(weight + 1, value);
                }
            }
        } catch (Exception e) {
            System.out.println("Ӧ�ù���ʱ�������");
        }
        return itemInfo;
    }

    public String[] split(String xmlString, String delim) {
        String tempString = "";
        try {
            tempString = removeCom(xmlString);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            if (tempString.indexOf(delim) == -1) {
                return new String[] { tempString };
            }
        } catch (java.lang.NullPointerException e) {
            System.out.println("delimeterΪnull");
        }
        int segCount = 0;
        try {
            while (tempString.lastIndexOf(delim) != 0) {
                tempString = tempString.substring((tempString.indexOf(delim) + delim.length()), (tempString.lastIndexOf(delim) + delim.length()));
                tempString = tempString.substring(tempString.indexOf(delim), (tempString.lastIndexOf(delim) + delim.length()));
                segCount++;
            }
        } catch (java.lang.NullPointerException e) {
            System.out.println("�ַ����null");
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            System.out.println("�ֶ��в�û�и�delimeter");
        } catch (Exception e) {
            System.out.println("���ض���ʶ��xml�ַ�ֶ�ʱ����");
        }
        String[] seg = new String[segCount];
        try {
            tempString = removeCom(xmlString);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            for (int i = 0; i < segCount; i++) {
                tempString = tempString.substring((tempString.indexOf(delim) + delim.length()), (tempString.lastIndexOf(delim) + delim.length()));
                seg[i] = tempString.substring(0, tempString.indexOf(delim));
            }
        } catch (java.lang.NullPointerException e) {
            System.out.println("split�������null");
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            System.out.println("�ֶ��в�û�и�delimeter");
        } catch (Exception e) {
            System.out.println("���ض���ʶ��xml�ַ�ֶ�ʱ����");
        }
        return seg;
    }

    public String transToTreeXml(String xmlString) {
        StringBuffer sb = new StringBuffer();
        if (null == xmlString) {
            System.out.println("�ַ���Ϊnull");
            return "";
        }
        String newXmlString = ">" + xmlString + "<";
        try {
            String[] seg = split(newXmlString, "><");
            ItemInfo[] itemInfo = new ItemInfo[seg.length];
            for (int i = 0; i < seg.length; i++) {
                if (i > 0) {
                    itemInfo[i] = testItemInfo(seg[i - 1], seg[i], itemInfo[i - 1].getWeight());
                    if (itemInfo[i].getWeight() < itemInfo[i - 1].getWeight()) {
                        itemInfo[i].setParent((itemInfo[i - 1].getParent()).getParent());
                    } else if (itemInfo[i].getWeight() == itemInfo[i - 1].getWeight()) {
                        itemInfo[i].setParent(itemInfo[i - 1].getParent());
                    } else {
                        itemInfo[i].setParent(itemInfo[i - 1]);
                    }
                } else {
                    itemInfo[i] = testItemInfo(seg[i]);
                    itemInfo[i].setParent(new ItemInfo(0, ""));
                }
            }
            for (int i = 0; i < itemInfo.length; i++) {
                if (!((itemInfo[i].getItem()).startsWith("/"))) {
                    if (isLeaf(itemInfo[i].getItem())) {
                        String item = (itemInfo[i].getItem()).substring(0, (itemInfo[i].getItem()).indexOf(">"));
                        String value = (itemInfo[i].getItem()).substring(((itemInfo[i].getItem()).indexOf(">") + 1), (itemInfo[i].getItem()).indexOf("<"));
                        sb.append("<ITEM NAME= '" + item + "' KEY= '" + i + "' EXTRADATA= 'leaf##" + itemInfo[i].getWeight() + "$$" + value + "' ></ITEM>");
                    } else {
                        String item = itemInfo[i].getItem();
                        sb.append("<ITEM NAME= '" + item + "' KEY= '" + i + "' EXTRADATA= 'trunk##" + itemInfo[i].getWeight() + "$$" + "' >");
                    }
                } else {
                    sb.append("</ITEM>");
                }
            }
        } catch (java.lang.StringIndexOutOfBoundsException e) {
            System.out.println("�����ļ��ĸ�ʽ����ȷ");
        } catch (java.lang.NullPointerException e) {
            System.out.println("transToTreeXml�������null");
        }
        return sb.toString();
    }

    public String transToTreeXml(String xmlString, int count) {
        StringBuffer sb = new StringBuffer();
        if (null == xmlString) {
            System.out.println("�ַ���Ϊnull");
            return "";
        }
        if (count <= 0) {
            System.out.println("����Ӧ����0");
            return "";
        }
        String newXmlString = ">" + xmlString + "<";
        try {
            String[] seg = split(newXmlString, "><");
            ItemInfo[] itemInfo = new ItemInfo[seg.length];
            for (int i = 0; i < seg.length; i++) {
                if (i > 0) {
                    itemInfo[i] = testItemInfo(seg[i - 1], seg[i], itemInfo[i - 1].getWeight());
                    if (itemInfo[i].getWeight() < itemInfo[i - 1].getWeight()) {
                        itemInfo[i].setParent((itemInfo[i - 1].getParent()).getParent());
                    } else if (itemInfo[i].getWeight() == itemInfo[i - 1].getWeight()) {
                        itemInfo[i].setParent(itemInfo[i - 1].getParent());
                    } else {
                        itemInfo[i].setParent(itemInfo[i - 1]);
                    }
                } else {
                    itemInfo[i] = testItemInfo(seg[i]);
                    itemInfo[i].setParent(new ItemInfo(0, ""));
                }
            }
            for (int i = 0; i < itemInfo.length; i++) {
                if (!((itemInfo[i].getItem()).startsWith("/"))) {
                    if (isLeaf(itemInfo[i].getItem())) {
                        String item = (itemInfo[i].getItem()).substring(0, (itemInfo[i].getItem()).indexOf(">"));
                        String value = (itemInfo[i].getItem()).substring(((itemInfo[i].getItem()).indexOf(">") + 1), (itemInfo[i].getItem()).indexOf("<"));
                        sb.append("<ITEM NAME= '" + item + "' KEY= '" + (count * 1000 + i) + "' EXTRADATA= 'leaf##" + itemInfo[i].getWeight() + "$$" + value + "' ></ITEM>");
                    } else {
                        String item = itemInfo[i].getItem();
                        sb.append("<ITEM NAME= '" + item + "' KEY= '" + (count * 1000 + i) + "' EXTRADATA= 'trunk##" + itemInfo[i].getWeight() + "$$" + "' >");
                    }
                } else {
                    sb.append("</ITEM>");
                }
            }
        } catch (java.lang.StringIndexOutOfBoundsException e) {
            System.out.println("�����ļ��ĸ�ʽ����ȷ");
        } catch (java.lang.NullPointerException e) {
            System.out.println("transToTreeXml�������null");
        }
        return sb.toString();
    }

    public String transFromTreeXml(String xmlString) {
        Stack st = new Stack();
        StringBuffer sb = new StringBuffer();
        String newXmlString = "";
        try {
            newXmlString = ">" + xmlString + "<";
        } catch (java.lang.NullPointerException e) {
            System.out.println("TransFromTreeXml�������null");
        }
        try {
            String[] seg = split(newXmlString, "><");
            for (int i = 0; i < seg.length; i++) {
                if (!(seg[i].startsWith("/"))) {
                    String s1 = seg[i].substring((seg[i].indexOf("NAME='") + 6), (seg[i].indexOf("' KEY=")));
                    String s2 = seg[i].substring((seg[i].indexOf("$$") + 2), seg[i].lastIndexOf("'"));
                    if ((null == s2) || (s2.equals(""))) {
                        st.push(getTag(s1));
                        sb.append("<" + divAttr(s1) + ">");
                    } else {
                        st.push(getTag(s1));
                        sb.append("<" + divAttr(s1) + ">" + s2);
                    }
                } else {
                    sb.append("</" + (String) st.pop() + ">");
                }
            }
        } catch (java.lang.StringIndexOutOfBoundsException e) {
            System.out.println("�����ϻ�ȡ��xml��ʽ����ȷ");
        } catch (Exception e) {
            System.out.println("���ڴ�ű�ǩ�Ķ�ջ��������");
        }
        return sb.toString();
    }

    private String divAttr(String tagString) {
        String tempStr = "";
        StringBuffer sb = new StringBuffer();
        tempStr = tagString;
        int idx = -1;
        if ((idx = tempStr.indexOf(" ")) == -1) {
            if ((idx = tempStr.indexOf("\t")) == -1) {
                return tempStr;
            }
        }
        sb.append(tempStr.substring(0, idx));
        tempStr = tempStr.substring(idx, tempStr.length());
        tempStr = tempStr.trim();
        int i = 0;
        while ((idx = tempStr.indexOf("\"")) != -1) {
            if ((i % 2) == 0) {
                if (i == 0) {
                    sb.append("  ");
                    sb.append(tempStr.substring(0, idx + 1));
                    tempStr = tempStr.substring(idx + 1, tempStr.length());
                } else {
                    sb.append("\n");
                    sb.append(tempStr.substring(0, idx + 1));
                    tempStr = tempStr.substring(idx + 1, tempStr.length());
                }
            } else {
                sb.append(tempStr.substring(0, idx + 1));
                tempStr = tempStr.substring(idx + 1, tempStr.length());
            }
            i++;
        }
        tempStr = sb.toString();
        return tempStr;
    }

    private String getTag(String tagString) {
        String tempStr = "";
        tempStr = tagString;
        int idx = -1;
        if ((idx = tempStr.indexOf(" ")) == -1) {
            if ((idx = tempStr.indexOf("\t")) == -1) {
                return tempStr;
            }
        }
        tempStr = tempStr.substring(0, idx);
        return tempStr;
    }

    private String removeCom(String xmlString) throws Exception {
        String tempStr1 = "";
        String tempStr2 = "";
        while (xmlString.indexOf("<!--") != -1) {
            if (xmlString.indexOf("-->") == -1) {
                throw new Exception("ע�͵ĸ�ʽ����");
            }
            tempStr1 = xmlString.substring(0, (xmlString.indexOf("<!--")));
            tempStr2 = xmlString.substring((xmlString.indexOf("-->") + 3), xmlString.length());
            xmlString = tempStr1.concat(tempStr2);
        }
        while (xmlString.indexOf("<?") != -1) {
            if (xmlString.indexOf("?>") == -1) {
                throw new Exception("PI�ĸ�ʽ����");
            }
            tempStr1 = xmlString.substring(0, (xmlString.indexOf("<?")));
            tempStr2 = xmlString.substring((xmlString.indexOf("?>") + 2), xmlString.length());
            xmlString = tempStr1.concat(tempStr2);
        }
        return xmlString;
    }

    public int getTotalStr(String xmlString, String str) {
        int count = 0;
        try {
            int i = xmlString.indexOf(str);
            while (i != -1) {
                count++;
                i = xmlString.indexOf(str, (i + str.length()));
            }
        } catch (java.lang.NullPointerException e) {
            System.out.println("�������Ϊnull");
        } catch (Exception e) {
            System.out.println("��ȡ���ַ������ַ��е���Ŀʱ�����쳣");
        }
        return count;
    }
}
