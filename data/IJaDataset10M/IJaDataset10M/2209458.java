package com.gsoft.archive.helper;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.sql.Clob;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.AbstractAttribute;
import com.hzzk.common.exception.BusinessException;
import com.hzzk.common.www.converter.DateConverter;

public class XmlParser {

    /**
     * ˵������xml���תд��ʵ�������
     * 
     * @param xml
     *            xml����ݱ���Ϊ�������е���� <data><row/><row/></data>
     * @param Class
     *            ��������� return Object ���ض���
     */
    public static Object getObjFromXF(String xml, Class beanClass) {
        Object bean = null;
        try {
            bean = beanClass.newInstance();
            bean = getObjFromXF(xml, bean);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bean;
    }

    /**
     * ˵������xml���תд��ʵ�������
     * 
     * @param xml
     *            xml����ݱ���Ϊ�������е���� <data><row/><row/></data>
     * @param entity
     *            ����Ķ��� return Object ���ض���
     */
    public static Object getObjFromXF(String xml, Object entity) {
        try {
            StringBuffer xfomXML = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            xfomXML.append(xml);
            StringReader stringReader = new StringReader(xfomXML.toString());
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(stringReader);
            List rowNodes = document.selectNodes("//data/row");
            for (int i = 0; i < rowNodes.size(); i++) {
                entity = getObjectFromElement((Element) rowNodes.get(i), entity);
            }
        } catch (DocumentException e) {
            throw new BusinessException("����XForm��ݵ�ʵ�����ʧ�ܣ�");
        } catch (NumberFormatException e) {
            throw new BusinessException("���ת������");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return entity;
    }

    /**
     * ˵������ʵ�����תд��xml�����
     * 
     * @param entity
     *            ����Ķ��� return xml xml����ݱ���Ϊ�������е���� <data><row/></data>
     */
    public static String getXFFromObj(Object entity) {
        if (entity == null) return "<data><row/></data>";
        StringBuffer xmlBuffer = new StringBuffer("<data><row");
        try {
            Map pMap = PropertyUtils.describe(entity);
            Iterator iter = pMap.keySet().iterator();
            while (iter.hasNext()) {
                String name = (String) iter.next();
                Object value = PropertyUtils.getSimpleProperty(entity, name);
                if (value == null || name.toLowerCase().equals("class") || name.toLowerCase().equals("attributes")) continue;
                if (value.getClass().equals(Date.class) || value.getClass().equals(java.sql.Timestamp.class)) xmlBuffer.append(" " + name).append("='").append(DateConverter.doConvertToString(value)).append("'"); else xmlBuffer.append(" " + name).append("='").append(value.toString()).append("'");
            }
            xmlBuffer.append("/></data>");
        } catch (Exception e) {
            throw new BusinessException("�������ʧ�ܣ�");
        }
        return xmlBuffer.toString();
    }

    /**
     * ˵������xml���תд������б���
     * 
     * @param Class
     *            ���������
     * @param xml
     *            xml����ݱ���Ϊ�������е���� <data><row/><row/><row/></data> return
     *            List ���صĶ����б�
     */
    public static List getListFromGrid(String xmlStr, Class beanClass) {
        List list = new ArrayList();
        Document document = convertStr2Xml(xmlStr);
        List rowNodes = document.selectNodes("//data/row");
        try {
            if (rowNodes != null) {
                for (int i = 0; i < rowNodes.size(); i++) {
                    Element row = (Element) rowNodes.get(i);
                    if (hasDeleteAttribute(row) == true) {
                        Object bean = beanClass.newInstance();
                        bean = getObjectFromElement(row, bean);
                        list.add(bean);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * ˵������xml�������תд������б���
     * 
     * @param Class
     *            ���������
     * @param xml
     *            xml����ݱ���Ϊ�������е���� <data><row/><row/><row/></data> return
     *            List ���صĶ����б�
     */
    public static List getNewListFromGrid(String xmlStr, Class beanClass) {
        List list = new ArrayList();
        Document document = convertStr2Xml(xmlStr);
        List rowNodes = document.selectNodes("//data/row");
        try {
            if (rowNodes != null) {
                for (int i = 0; i < rowNodes.size(); i++) {
                    Element row = (Element) rowNodes.get(i);
                    if (row.attribute("_new") != null) {
                        Object bean = beanClass.newInstance();
                        bean = getObjectFromElement(row, bean);
                        list.add(bean);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * ˵������xml�޸����תд������б���
     * 
     * @param Class
     *            ���������
     * @param xml
     *            xml����ݱ���Ϊ�������е���� <data><row/><row/><row/></data> return
     *            List ���صĶ����б�
     */
    public static List getOldListFromGrid(String xmlStr, Class beanClass) {
        List list = new ArrayList();
        Document document = convertStr2Xml(xmlStr);
        List rowNodes = document.selectNodes("//data/row");
        try {
            if (rowNodes != null) {
                for (int i = 0; i < rowNodes.size(); i++) {
                    Element row = (Element) rowNodes.get(i);
                    if (row.attribute("_new") == null) {
                        Object bean = beanClass.newInstance();
                        bean = getObjectFromElement(row, bean);
                        list.add(bean);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * ��ȡGrid���豣��ļ�¼
     * @param xmlStr
     * @param beanClass
     * @return
     */
    public static List getToSaveListFromGrid(String xmlStr, Class beanClass) {
        List list = new ArrayList();
        Document document = convertStr2Xml(xmlStr);
        List rowNodes = document.selectNodes("//data/row");
        try {
            if (rowNodes != null) {
                for (int i = 0; i < rowNodes.size(); i++) {
                    Element row = (Element) rowNodes.get(i);
                    if (row.attribute("_new") != null || row.attribute("_modify") != null) {
                        Object bean = beanClass.newInstance();
                        bean = getObjectFromElement(row, bean);
                        list.add(bean);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * ��ȡGrid����Ҫɾ��ļ�¼
     * @param xmlStr
     * @param beanClass
     * @return
     */
    public static List getToDeleteListFromGrid(String xmlStr, Class beanClass) {
        List list = new ArrayList();
        Document document = convertStr2Xml(xmlStr);
        List rowNodes = document.selectNodes("//data/row");
        try {
            if (rowNodes != null) {
                for (int i = 0; i < rowNodes.size(); i++) {
                    Element row = (Element) rowNodes.get(i);
                    if (row.attribute("_delete") != null) {
                        Object bean = beanClass.newInstance();
                        bean = getObjectFromElement(row, bean);
                        list.add(bean);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * ˵������ʵ�����תд��xml�����
     * 
     * @param list
     *            ����Ķ����б� return xml xml����ݱ���Ϊ�������е���� <data><row/></data>
     */
    public static String getGridDataFromList(List list) {
        if (list == null || list.size() == 0) return "<data></data>";
        StringBuffer xmlBuffer = new StringBuffer("<data>");
        Object obj = list.get(0);
        try {
            Map pMap = PropertyUtils.describe(obj);
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                xmlBuffer.append("<row");
                Iterator piter = pMap.keySet().iterator();
                Object object = (Object) iter.next();
                while (piter.hasNext()) {
                    String name = (String) piter.next();
                    if (name.toLowerCase().equals("class") || name.toLowerCase().equals("attributes")) continue;
                    Object value = PropertyUtils.getSimpleProperty(object, name);
                    if (value == null) continue;
                    if (value.getClass().toString().toLowerCase().endsWith("clob")) {
                        Clob clob = (Clob) value;
                        xmlBuffer.append(" " + name).append("=\"").append(XmlString.convert2XML(clob.getSubString(1, (int) clob.length()))).append("\"");
                    } else if (value.getClass().equals(Date.class) || value.getClass().equals(java.sql.Timestamp.class)) xmlBuffer.append(" " + name).append("=\"").append(DateConverter.doConvertToString(value)).append("\""); else xmlBuffer.append(" " + name).append("=\"").append(XmlString.convert2XML(value.toString())).append("\"");
                }
                xmlBuffer.append(" other=\"�嵥�б�\"");
                xmlBuffer.append("/>");
            }
            xmlBuffer.append("</data>");
        } catch (Exception e) {
            throw new BusinessException("�������ʧ�ܣ�");
        }
        return xmlBuffer.toString();
    }

    /**
     * 
     */
    public static String getLevelGridDataFromList(List list) {
        if (list == null || list.size() == 0) return "<data></data>";
        StringBuffer xmlBuffer = new StringBuffer("<data>");
        Object obj = list.get(0);
        List levelList = new ArrayList();
        Map levelMap = new HashMap();
        try {
            String piName = getPIdName(obj);
            if (piName == null) return "<data></data>";
            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                LevelRow levelRow = new LevelRow(iter.next());
                levelMap.put(levelRow.getId(), levelRow);
            }
            iter = levelMap.values().iterator();
            while (iter.hasNext()) {
                LevelRow levelRow = (LevelRow) iter.next();
                if (levelRow.getParentId() == null || levelMap.get(levelRow.getParentId()) == null) levelList.add(levelRow); else {
                    LevelRow pRow = (LevelRow) levelMap.get(levelRow.getParentId());
                    pRow.appendChild(levelRow);
                }
            }
            iter = levelList.iterator();
            while (iter.hasNext()) {
                LevelRow levelRow = (LevelRow) iter.next();
                xmlBuffer.append(levelRow.getXML());
            }
            xmlBuffer.append("</data>");
        } catch (Exception e) {
            throw new BusinessException("�������ʧ�ܣ�");
        }
        return xmlBuffer.toString();
    }

    /**
     * ˵������xml���תд��XML�ĵ�������
     * 
     * @param xmlStr
     *            xml����ݱ���Ϊ�������е���� return Document XML�ĵ�����
     */
    public static Document convertStr2Xml(String xmlStr) {
        StringReader sr = new StringReader(xmlStr);
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader.read(sr);
        } catch (DocumentException e) {
            throw new BusinessException(" ����document�ĵ�ʱ����", e);
        }
        return document;
    }

    /**
     * ˵������XML�ĵ������element����ת����Object
     * 
     * @param element
     *            ML�ĵ������element���� return Object ʵ�����
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private static Object getObjectFromElement(Element element, Object entity) throws Exception {
        if (element == null) return null;
        List attrs = element.attributes();
        Map pMap = PropertyUtils.describe(entity);
        if (attrs != null) for (Iterator ia = attrs.iterator(); ia.hasNext(); ) {
            AbstractAttribute attr = (AbstractAttribute) ia.next();
            if (attr.getValue() == null || attr.getValue() == "" || attr.getName() == null || attr.getName().equals("class")) continue;
            if (attr.getName().indexOf(".") != -1) {
                String name = attr.getName().replace('.', ':');
                String pros[] = name.split(":");
                if (!pMap.containsKey(pros[0])) continue;
                Class tClass = PropertyUtils.getPropertyType(entity, pros[0]);
                Object id = PropertyUtils.getProperty(entity, pros[0]);
                if (id == null) id = tClass.newInstance();
                setStrToObject(id, pros[1], attr.getValue());
                PropertyUtils.setSimpleProperty(entity, pros[0], id);
                continue;
            } else {
                if (!pMap.containsKey(attr.getName())) continue;
            }
            setStrToObject(entity, attr.getName(), attr.getValue());
        }
        return entity;
    }

    public static String getPIdName(Object obj) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map pMap = PropertyUtils.describe(obj);
        Iterator piter = pMap.keySet().iterator();
        String pIdName = null;
        while (piter.hasNext()) {
            String name = (String) piter.next();
            if (name.equalsIgnoreCase("parentGuId")) {
                pIdName = name;
            }
        }
        return pIdName;
    }

    public static void setStrToObject(Object entity, String name, String value) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Class typeClass = PropertyUtils.getPropertyType(entity, name);
        if (typeClass.equals(Integer.class)) PropertyUtils.setSimpleProperty(entity, name, new Integer(value)); else if (typeClass.equals(Long.class)) PropertyUtils.setSimpleProperty(entity, name, new Long(value)); else if (typeClass.equals(String.class)) PropertyUtils.setSimpleProperty(entity, name, value); else if (typeClass.equals(Double.class)) {
            PropertyUtils.setSimpleProperty(entity, name, new Double(value.replaceAll(",", "")));
        } else if (typeClass.equals(Date.class)) {
            PropertyUtils.setSimpleProperty(entity, name, DateConverter.doConvertToDate(value));
        } else PropertyUtils.setSimpleProperty(entity, name, null);
    }

    private static boolean hasDeleteAttribute(Element row) {
        List listAtt = row.attributes();
        for (Iterator ia = listAtt.iterator(); ia.hasNext(); ) {
            AbstractAttribute attr = (AbstractAttribute) ia.next();
            if (attr.getName().equals("_delete")) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * </p>
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
    }
}
