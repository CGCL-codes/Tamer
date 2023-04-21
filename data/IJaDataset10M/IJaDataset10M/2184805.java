package com.asoft.common.viewframe.model;

import java.io.Serializable;
import java.util.Set;
import com.asoft.common.viewframe.define.ColumnType;

/**
 * <p>Title: 栏目 </p>
 * <p>Description: 栏目 Column </p>
 * <p>Copyright: Copyright (c) 2004-2006</p>
 * <p>Company: asoft</p>
 * @ $Author: amon.lei $
 * @ $Date: 2007-2-20 $
 * @ $Revision: 1.0 $
 * @ created in 2007-2-20
 *
 */
public class Column extends View implements Serializable {

    private String name;

    private String imageUrl;

    private String url;

    private String remark;

    private int type;

    private Area area;

    private Column superColumn;

    private Set subColumns;

    /**
         * 构造函数
         */
    public Column() {
    }

    public Column(Area area, String code, String name, String url, String pri) {
        this.area = area;
        this.code = code;
        this.name = name;
        this.imageUrl = "";
        this.url = url;
        this.pri = pri;
        this.remark = "";
        this.type = ColumnType.ONE_LEVEL;
    }

    public Column(Column superColumn, String code, String name, String imageUrl, String url, String pri) {
        this.superColumn = superColumn;
        this.code = code;
        this.name = name;
        this.imageUrl = imageUrl;
        this.url = url;
        this.pri = pri;
        this.remark = "";
        this.type = ColumnType.TWO_LEVEL;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
         * @return 返回 subColumns。
         */
    public Set getSubColumns() {
        return subColumns;
    }

    /**
         * @param subColumns 要设置的 subColumns。
         */
    public void setSubColumns(Set subColumns) {
        this.subColumns = subColumns;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return ColumnType.NAMES[this.type];
    }

    /**
         * @return 返回 area。
         */
    public Area getArea() {
        return area;
    }

    /**
         * @param area 要设置的 area。
         */
    public void setArea(Area area) {
        this.area = area;
    }

    /**
         * @return 返回 superColumn。
         */
    public Column getSuperColumn() {
        return superColumn;
    }

    /**
         * @param superColumn 要设置的 superColumn。
         */
    public void setSuperColumn(Column superColumn) {
        this.superColumn = superColumn;
    }

    /** 采用框架提供的操作日志必须填写的项目 */
    public void setModeldetail() {
        this.setModelFieldDetail("id", this.getId());
        if (this.superColumn != null) {
            this.setModelFieldDetail("父栏目", this.superColumn.getName());
        }
        if (this.area != null) {
            this.setModelFieldDetail("分区", this.area.getName());
        }
        this.setModelFieldDetail("代码", this.code);
        this.setModelFieldDetail("名称", this.name);
        this.setModelFieldDetail("图片url", this.imageUrl);
        this.setModelFieldDetail("url", this.url);
        this.setModelFieldDetail("类型", this.getTypeName());
        this.setModelFieldDetail("优先级", this.getPri());
    }
}
