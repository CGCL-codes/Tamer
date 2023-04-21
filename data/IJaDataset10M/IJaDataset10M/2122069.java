package org.njo.webapp.templates.config;

import java.util.LinkedHashMap;
import java.util.Map;

public class PatternConfig {

    private String id;

    private String charset;

    private String extendsid;

    private Map<String, TemplateConfig> templateConfigs = new LinkedHashMap<String, TemplateConfig>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, TemplateConfig> getTemplateConfigs() {
        return templateConfigs;
    }

    void setTemplateConfigs(Map<String, TemplateConfig> templates) {
        this.templateConfigs = templates;
    }

    public void addTemplateConfig(TemplateConfig templateConfig) {
        templateConfigs.put(templateConfig.getId(), templateConfig);
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getExtends() {
        return extendsid;
    }

    public void setExtends(String extendsid) {
        this.extendsid = extendsid;
    }
}
