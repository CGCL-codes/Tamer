package com.teg.tobe.ui;

import org.zkoss.zul.Treecell;
import com.teg.tobe.ui.datasource.DataSource;
import com.teg.tobe.util.Converter;

public class DataSourceTask extends DefaultComponentTask {

    private static final long serialVersionUID = 1L;

    private String name;

    private String datasetName;

    private String accessName;

    private String accessId;

    private String deleteAccessName;

    private String insertAccessId;

    private String updateAccessId;

    public DataSourceTask() {
        super();
    }

    public void execute() throws Exception {
        DataSource dataSource = new DataSource();
        dataSource.setName(name);
        dataSource.setDatasetName(datasetName);
        result = dataSource;
        executeChildren();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public String getAccessName() {
        return accessName;
    }

    public void setAccessName(String accessName) {
        this.accessName = accessName;
    }

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getDeleteAccessName() {
        return deleteAccessName;
    }

    public void setDeleteAccessName(String deleteAccessName) {
        this.deleteAccessName = deleteAccessName;
    }

    public String getInsertAccessId() {
        return insertAccessId;
    }

    public void setInsertAccessId(String insertAccessId) {
        this.insertAccessId = insertAccessId;
    }

    public String getUpdateAccessId() {
        return updateAccessId;
    }

    public void setUpdateAccessId(String updateAccessId) {
        this.updateAccessId = updateAccessId;
    }

    public String getName() {
        return name;
    }

    public String getDatasetName() {
        return datasetName;
    }

    @Override
    public boolean isTaskAttribute(String attribute) {
        if ("datasetName".equals(attribute)) return true;
        if ("name".equals(attribute)) return true;
        if ("accessName".equals(attribute)) return true;
        if ("accessId".equals(attribute)) return true;
        if ("deleteAccessName".equals(attribute)) return true;
        if ("insertAccessId".equals(attribute)) return true;
        if ("updateAccessId".equals(attribute)) return true;
        return super.isTaskAttribute(attribute);
    }

    @Override
    public void generateAttributes() throws Exception {
        generateAttributes(DataSource.class);
    }

    public Treecell getDesignerTreeComponent() {
        String componentText = "" + Converter.asNotNullString(getTagName()) + " " + Converter.asNotNullString(getAttributes().getValueAsString("name")) + "->" + Converter.asNotNullString(getAttributes().getValueAsString("datasetName"));
        return new Treecell(componentText, "/image/components/" + getTagName() + ".png");
    }
}
