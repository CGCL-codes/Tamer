package com.nhncorp.usf.core;

/**
 * USF 운영을 위한 Configuration 정의.
 *
 * @author Web Platform Development Team.
 */
public class UsfConfiguration {

    /**
     * freemarker template 경로.
     */
    private String templates;

    /**
	 * script file 경로
	 */
    private String scripts;

    /**
     * 액션 extension.
     */
    private String suffix;

    /**
     * script property file 이름.
     */
    private String scriptProperty;

    /**
     * 생성자.
     */
    public UsfConfiguration(String templates, String suffix) {
        this.templates = templates;
        this.suffix = suffix;
    }

    /**
     * Instantiates a new usf configuration.
     *
     * @param templates the templates
     * @param suffix    the suffix
     * @param scripts   the scripts
     */
    public UsfConfiguration(String templates, String suffix, String scripts) {
        this.templates = templates;
        this.suffix = suffix;
        this.scripts = scripts;
    }

    /**
     * Gets the templates.
     *
     * @return the templates
     */
    public String getTemplates() {
        return templates;
    }

    /**
     * Gets the suffix.
     *
     * @return the suffix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Sets the suffix.
     *
     * @param suffix the new suffix
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * Gets the scripts.
     *
     * @return the scripts
     */
    public String getScripts() {
        return scripts;
    }

    /**
     * Sets the scripts.
     *
     * @param scripts the new scripts
     */
    public void setScripts(String scripts) {
        this.scripts = scripts;
    }

    /**
     * Gets the script property.
     *
     * @return the script property
     */
    public String getScriptProperty() {
        return scriptProperty;
    }

    /**
     * Sets the script property.
     *
     * @param scriptProperty the new script property
     */
    public void setScriptProperty(String scriptProperty) {
        this.scriptProperty = scriptProperty;
    }
}
