package cn.myapps.mobile.element;

import cn.myapps.ui.SelectField;
import cn.myapps.ui.SelectOption;

public class MbSelectField extends SelectField implements IMbField {

    public MbSelectField(boolean enable) {
        super(enable);
        refresh = false;
        tabIndex = -1;
        fieldName = "";
        value = "";
        setLayout(LAYOUT_2 | LAYOUT_NEWLINE_AFTER);
    }

    public int getElementType() {
        return IMbElement.TYPE_MbSelectField;
    }

    public void addOption(SelectOption opt) {
        super.addOption(opt);
        this.value = getString();
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getOldValue() {
        return value;
    }

    public void setOldValue(String value) {
        this.value = value;
    }

    private String value;

    private String fieldName;

    private boolean refresh;

    private int tabIndex;
}
