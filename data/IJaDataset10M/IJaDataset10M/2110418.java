package com.javaeye.lonlysky.lforum.entity.forum;

/**
 * 自定义按钮
 * 
 * @author 黄磊
 *
 */
public class CustomEditorButtonInfo {

    private int id;

    private int available;

    private String tag;

    private String icon;

    private String replacement;

    private String example;

    private String explanation;

    private int params;

    private int nest;

    private String paramsdescript;

    private String paramsdefvalue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public int getParams() {
        return params;
    }

    public void setParams(int params) {
        this.params = params;
    }

    public int getNest() {
        return nest;
    }

    public void setNest(int nest) {
        this.nest = nest;
    }

    public String getParamsdescript() {
        return paramsdescript;
    }

    public void setParamsdescript(String paramsdescript) {
        this.paramsdescript = paramsdescript;
    }

    public String getParamsdefvalue() {
        return paramsdefvalue;
    }

    public void setParamsdefvalue(String paramsdefvalue) {
        this.paramsdefvalue = paramsdefvalue;
    }
}
