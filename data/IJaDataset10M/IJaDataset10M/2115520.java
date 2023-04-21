package com.breachwalls.mogen.xml.bean;

import java.io.Serializable;
import com.breachwalls.mogen.utils.CloneUtils;

/**
 * モデルクラスのプロパティ.
 * @author todoken
 * @version $Revision: 31 $
 */
public class ModelClassProperty implements Serializable {

    /**
     * モデルプロパティの説明.
     */
    private String description;

    /**
     * モデルプロパティの名称.
     */
    private String name;

    /**
     * モデルプロパティの値.
     */
    private String value;

    /**
     * モデルプロパティのタイプ.
     */
    private String type;

    /**
     * モデルプロパティのスペック.
     */
    private String[] specs;

    /**
     * モデルプロパティの説明を取得する.
     * @return モデルプロパティの説明
     */
    public String getDescription() {
        return description;
    }

    /**
     * モデルプロパティの説明を設定する.
     * @param newDescription モデルプロパティの説明
     */
    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    /**
     * モデルプロパティの名称を取得する.
     * @return モデルプロパティの名称
     */
    public String getName() {
        return name;
    }

    /**
     * モデルプロパティの名称を設定する.
     * @param newName モデルプロパティの名称
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * モデルプロパティの値を取得する.
     * @return モデルプロパティの値
     */
    public String getValue() {
        return value;
    }

    /**
     * モデルプロパティの値を設定する.
     * @param newValue モデルプロパティの値
     */
    public void setValue(String newValue) {
        this.value = newValue;
    }

    /**
     * モデルプロパティのタイプを取得する.
     * @return モデルプロパティのタイプ
     */
    public String getType() {
        return type;
    }

    /**
     * モデルプロパティのタイプを設定する.
     * @param newType モデルプロパティのタイプ
     */
    public void setType(String newType) {
        this.type = newType;
    }

    /**
     * モデルプロパティのスペックを取得する.
     * @return モデルプロパティのスペック
     */
    public String[] getSpecs() {
        return CloneUtils.clone(specs);
    }

    /**
     * モデルプロパティのスペックを設定する.
     * @param newSpecs モデルプロパティのスペック
     */
    public void setSpecs(String[] newSpecs) {
        this.specs = CloneUtils.clone(newSpecs);
    }
}
