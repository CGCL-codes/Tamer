package org.mss.db.hibernateutil.model;

import java.util.Iterator;
import java.util.LinkedHashMap;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * MK 06.01.2008 Table Model will be stored in JAXB
 * 
 * implements equals and hashCode Methods
 */
@XmlRootElement(namespace = "http://www.mss.com/")
public class TableModel {

    private String tablename;

    private LinkedHashMap<String, TableFieldModel> fieldlist = new LinkedHashMap<String, TableFieldModel>();

    public Object[] getTableFields() {
        showFields();
        return fieldlist.values().toArray();
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public void setFieldlist(LinkedHashMap<String, TableFieldModel> fieldlist) {
        this.fieldlist = fieldlist;
    }

    public LinkedHashMap<String, TableFieldModel> getFieldlist() {
        return fieldlist;
    }

    public void addField(TableFieldModel field) {
        fieldlist.put(field.getStrFieldName(), field);
    }

    public void deleteField(String fieldname) {
        fieldlist.remove(fieldname);
        showFields();
    }

    private void showFields() {
        Iterator<TableFieldModel> iterator = fieldlist.values().iterator();
        while (iterator.hasNext()) {
            TableFieldModel model = iterator.next();
        }
    }

    /**
	 * MK 06.01.2009
	 * returns the sum of the hashCodes within the Map
	 */
    public int hashCode() {
        return fieldlist.hashCode();
    }
}
