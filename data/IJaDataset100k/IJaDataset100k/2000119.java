package com.jlz.beans.def;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.jlz.managers.def.Columns;
import com.jlz.managers.def.Valuetypes;
import com.julewa.db.DB;
import com.julewa.db.Entity;

/*************************************
 * 字段表
 * 重点在于根据type的不同,其性质将不同 
 *************************************/
@DB.USECACHE
@Component
@Scope(Entity.SCOPE)
public class ColumnBean extends Entity<Long> implements Comparable<ColumnBean> {

    @DB.COLUMN
    private long parentId = -1;

    @DB.COLUMN(min = 0)
    private int idx = 0;

    @DB.COLUMN(min = 2, max = 64)
    @DB.AUTOTRIM
    private String code;

    @DB.COLUMN(min = 2, max = 128)
    @DB.AUTOTRIM
    private String label;

    @DB.COLUMN
    private int valuetypeId = Valuetypes.TYPE_ID_NODE;

    @DB.COLUMN
    private int arraySize = 1;

    @DB.COLUMN(min = 0, max = 100)
    private int visibleType = Columns.VISIBLE.DISPLAY.getValue();

    @DB.COLUMN(min = 0, max = 100)
    private int editType = Columns.EDITABLE.ENABLE.getValue();

    @DB.COLUMN(min = 5, max = 800)
    private int displaySize = 200;

    @DB.COLUMN(min = 0, max = 1024)
    private String defaultValue = "";

    @Autowired
    private Valuetypes valuetypes = null;

    @Autowired
    private Columns columns = null;

    @DB.DEPENDON("valuetypeId")
    private ValuetypeBean valuetype = null;

    @DB.DEPENDON("parentId")
    private ColumnBean parent = null;

    @DB.DEPENDON("arraySize")
    private List<ColumnBean> children = null;

    @DB.DEPENDON("code")
    private String fullCode = null;

    public String getFullCode() {
        if (fullCode != null) return fullCode;
        if (this.parentId < 1) {
            this.fullCode = "";
        } else {
            this.fullCode = this.getParentColumn().getFullCode() + "." + this.code;
        }
        return this.fullCode;
    }

    public List<ColumnBean> getChildren() {
        if (children == null) {
            children = columns.getColumnsOf(this);
        }
        return children;
    }

    public ColumnBean createChild() {
        ColumnBean col = columns.create();
        col.parentId = this.getID();
        col.idx = this.getChildren().size();
        return col;
    }

    public boolean appendChild(ColumnBean column) {
        if (!columns.insert(column)) return false;
        if (this.children != null) {
            this.children.add(column);
            Collections.sort(children);
        }
        return true;
    }

    public boolean updateChild(ColumnBean column) {
        if (!columns.update(column)) return false;
        if (this.children != null) {
            Collections.sort(children);
        }
        return true;
    }

    public boolean removeChild(ColumnBean column) {
        if (!columns.remove(column)) return false;
        if (this.children != null) {
            this.children.remove(column);
            Collections.sort(children);
        }
        return true;
    }

    public boolean isPrimary() {
        return this.arraySize == 1;
    }

    public boolean isComplex() {
        return this.valuetypeId > Valuetypes.TYPE_ID_NODE;
    }

    public boolean isInArray() {
        if (this.getParentColumn() == null) return false;
        if (this.parent.isInArray()) {
            return true;
        }
        return this.parent.arraySize != 0;
    }

    public boolean isArray() {
        if (this.isInArray()) return false;
        return this.arraySize != 1;
    }

    public boolean isInfinite() {
        return this.arraySize < 1;
    }

    private Columns.EDITABLE editable = null;

    public Columns.EDITABLE getEditable() {
        if (editable == null) {
            editable = Columns.EDITABLE.of(editType);
        }
        return editable;
    }

    private Columns.VISIBLE visible = null;

    public Columns.VISIBLE getVisible() {
        if (visible == null) {
            visible = Columns.VISIBLE.of(visibleType);
        }
        return visible;
    }

    public ValuetypeBean getValuetype() {
        if (valuetype == null && valuetypeId > 0) {
            valuetype = valuetypes.get(valuetypeId);
        }
        return valuetype;
    }

    public ColumnBean getParentColumn() {
        if (parent == null) {
            if (parentId < 0) return null;
            parent = columns.get(parentId);
        }
        return parent;
    }

    @Override
    public int compareTo(ColumnBean bean) {
        return this.idx - bean.idx;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getValuetypeId() {
        return valuetypeId;
    }

    public void setValuetypeId(int valuetypeId) {
        this.valuetypeId = valuetypeId;
    }

    public int getArraySize() {
        return arraySize;
    }

    public void setArraySize(int arraySize) {
        this.arraySize = arraySize;
    }

    public int getVisibleType() {
        return visibleType;
    }

    public void setVisibleType(int visibleType) {
        this.visibleType = visibleType;
    }

    public int getEditType() {
        return editType;
    }

    public void setEditType(int editType) {
        this.editType = editType;
    }

    public int getDisplaySize() {
        return displaySize;
    }

    public void setDisplaySize(int displaySize) {
        this.displaySize = displaySize;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
