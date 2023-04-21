package org.pworks.core.model;

import org.pworks.core.adapter.IDataProcessor;

public class CodeModel {

    private String id;

    private String version;

    private String desc;

    private String template;

    private IDataProcessor dataProcessor;

    private String namePattern;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public IDataProcessor getDataProcessor() {
        return dataProcessor;
    }

    public void setDataProcessor(IDataProcessor dataProcessor) {
        this.dataProcessor = dataProcessor;
    }

    public String getNamePattern() {
        return namePattern;
    }

    public void setNamePattern(String namePattern) {
        this.namePattern = namePattern;
    }

    public String getTemplate() {
        return template;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
