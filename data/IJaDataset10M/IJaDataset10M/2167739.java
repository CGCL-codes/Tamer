package com.xsky.datagrid.impl;

import java.io.IOException;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.xsky.common.logic.Config;
import com.xsky.common.logic.ReflectTable;
import com.xsky.database.dao.impl.BaseEntityDaoImpl;
import com.xsky.datagrid.facade.IDataGridService;
import com.xsky.table.impl.Table;
import com.xsky.table.impl.TableConfiguration;

public class DataGridService implements IDataGridService {

    public String genGridTemplate(String tableName) {
        try {
            TableConfiguration tcf = TableConfiguration.getInstance();
            Table table = tcf.getTable(tableName);
            return table.getXml().asXML();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String search(String searchItemXml) throws DocumentException {
        StringBuffer resultXml = new StringBuffer();
        Document document = DocumentHelper.parseText(searchItemXml);
        String tableName = document.selectSingleNode("//data/table").getText();
        String start = document.selectSingleNode("//data/page/start").getText();
        String limit = document.selectSingleNode("//data/page/limit").getText();
        String orderBy = document.selectSingleNode("//data/page/orderBy").getText();
        String orderType = document.selectSingleNode("//data/page/orderType").getText();
        String uuid = document.selectSingleNode("//data/uuid").getText();
        Node conditionNode = document.selectSingleNode("//data/condition");
        String condition = "";
        ReflectTable reflectTable = null;
        try {
            reflectTable = ReflectTable.getInstance();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String fullName = (String) reflectTable.reflectMap.get(tableName);
        if (conditionNode != null) {
            condition = conditionNode.getText();
        }
        Class entityClass = null;
        try {
            entityClass = Class.forName(fullName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        BaseEntityDaoImpl baseDao = new BaseEntityDaoImpl();
        int totalNumbers = baseDao.fetchCount(fullName, condition);
        List resultList = baseDao.fetchByPagesWithCondition(fullName, start, limit, condition, orderBy, orderType);
        resultXml.append("<root>");
        resultXml.append("<totalRecords>" + totalNumbers + "</totalRecords>");
        resultXml.append("<data>");
        XStream xs = new XStream();
        xs.alias("item", entityClass);
        for (int i = 0; i < resultList.size(); i++) {
            resultXml.append(xs.toXML(resultList.get(i)));
        }
        resultXml.append("</data>");
        resultXml.append("<result>ok</result>");
        resultXml.append("</root>");
        return resultXml.toString().replace(" 00:00:00.0", "");
    }

    public String add(String dataXml) {
        StringBuffer resultXml = new StringBuffer();
        Document document;
        try {
            document = DocumentHelper.parseText(dataXml);
            String tableName = document.getRootElement().attribute("tableName").getText();
            document.getRootElement().remove(document.getRootElement().attribute("tableName"));
            dataXml = document.asXML();
            ReflectTable reflectTable = null;
            try {
                reflectTable = ReflectTable.getInstance();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            String fullName = (String) reflectTable.reflectMap.get(tableName);
            Class entityClass = null;
            try {
                entityClass = Class.forName(fullName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            BaseEntityDaoImpl baseDao = new BaseEntityDaoImpl();
            XStream xs = new XStream(new DomDriver());
            String defaultFormat = "yyyy-MM-dd HH:mm:ss";
            String[] acceptableFormat = { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" };
            xs.registerConverter(new DateConverter(defaultFormat, acceptableFormat));
            xs.alias("data", entityClass);
            Object object = xs.fromXML(dataXml);
            baseDao.insertEntity(object);
        } catch (DocumentException e2) {
            e2.printStackTrace();
        }
        return "添加成功";
    }

    public String delete(String dataXml) {
        Document document;
        try {
            BaseEntityDaoImpl baseDao = new BaseEntityDaoImpl();
            document = DocumentHelper.parseText(dataXml);
            String tableName = document.getRootElement().attribute("table").getText();
            ReflectTable reflectTable = null;
            try {
                reflectTable = ReflectTable.getInstance();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            String fullName = (String) reflectTable.reflectMap.get(tableName);
            List itemList = document.selectNodes("//data/item");
            for (int i = 0; i < itemList.size(); i++) {
                Element element = (Element) itemList.get(i);
                System.out.println(element.asXML());
                String id = element.selectSingleNode("id").getText();
                if (element.attribute("selected") != null) {
                    String isSelected = element.attribute("selected").getText();
                    if ("true".equals(isSelected)) {
                        baseDao.deleteByKey(fullName, "id", id);
                    } else {
                        continue;
                    }
                } else {
                    id = "";
                    element = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "删除成功";
    }

    public String genConfig(String configName) {
        try {
            Config config = Config.getInstance();
            String configStr = (String) config.configMap.get(configName);
            return configStr;
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
