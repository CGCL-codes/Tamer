package com.gwtaf.ext.core.client.util;

import com.gwtaf.core.client.util.EventInfo;
import com.gwtaf.ext.core.client.store.GroupingStoreEx;
import com.gwtaf.ext.core.client.store.IStoreAdapter;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.ExtElement;
import com.gwtext.client.core.NameValuePair;
import com.gwtext.client.core.UrlParam;
import com.gwtext.client.data.Record;
import com.gwtext.client.dd.DragData;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.grid.ColumnConfig;
import com.gwtext.client.widgets.grid.GridDragData;
import com.gwtext.client.widgets.grid.GridEditor;
import com.gwtext.client.widgets.grid.Renderer;
import com.gwtext.client.widgets.tree.TreeDragData;

public final class GwtExtUtil {

    private static final String GWTAF_FORM_FIELD_RO = "gwtaf-form-field-ro";

    private static boolean menuShadow;

    public static Object getValue(UrlParam baseParam) {
        if (baseParam == null) return null;
        switch(baseParam.getType()) {
            case NameValuePair.STRING:
                return baseParam.getValue();
            case NameValuePair.BOOLEAN:
                return baseParam.getValueAsBoolean();
            case NameValuePair.FLOAT:
                return baseParam.getValueAsFloat();
            case NameValuePair.INT:
                return baseParam.getValueAsInt();
            case NameValuePair.DATE:
                return baseParam.getValueAsDate();
            default:
                return null;
        }
    }

    public static EventInfo createEventInfo(EventObject e) {
        int[] xy = e.getXY();
        return new EventInfo(xy[0], xy[1], e.isShiftKey(), e.isCtrlKey(), e.isAltKey());
    }

    public static Object extractData(DragData dragData) {
        if (dragData instanceof TreeDragData) {
            return ((TreeDragData) dragData).getTreeNode().getUserObject();
        } else if (dragData instanceof GridDragData) {
            GridDragData gridDragData = (GridDragData) dragData;
            Record r = gridDragData.getGrid().getStore().getRecordAt(gridDragData.getRowIndex());
            return r.getAsObject(GroupingStoreEx.DATA);
        }
        return null;
    }

    public static void hideFieldBorder(Field field, boolean hide) {
        if (hide) field.addClass(GWTAF_FORM_FIELD_RO); else field.removeClass(GWTAF_FORM_FIELD_RO);
    }

    public static void hideFieldBorder(ExtElement el, boolean hide) {
        if (hide) el.addClass(GWTAF_FORM_FIELD_RO); else el.removeClass(GWTAF_FORM_FIELD_RO);
    }

    public static boolean isMenuShadow() {
        return menuShadow;
    }

    public static ColumnConfig createColumnConfig(Renderer renderer, final IStoreAdapter<?> storeAdapter, String header, String dataIndex) {
        ColumnConfig columnConfig = createColumnConfig(storeAdapter, header, dataIndex);
        columnConfig.setRenderer(renderer);
        return columnConfig;
    }

    public static ColumnConfig createColumnConfig(final IStoreAdapter<?> storeAdapter, String header, String dataIndex) {
        ColumnConfig columnConfig = new ColumnConfig(header, dataIndex);
        ComboBox comboBox = new ComboBox();
        comboBox.setStore(storeAdapter.getStore());
        comboBox.setDisplayField(storeAdapter.getDisplayField());
        comboBox.setValueField(storeAdapter.getValueField());
        comboBox.setSelectOnFocus(true);
        comboBox.setMode(ComboBox.LOCAL);
        comboBox.setTriggerAction(ComboBox.ALL);
        columnConfig.setEditor(new GridEditor(comboBox));
        return columnConfig;
    }
}
