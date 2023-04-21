package net.nothinginteresting.ylib.xstream;

import net.nothinginteresting.ylib.annotations.YAttribute;
import net.nothinginteresting.ylib.constants.YAlignment;
import net.nothinginteresting.ylib.widgets.YTableColumn;
import net.nothinginteresting.ylib.widgets.YWidget;

public class YTableViewerColumnModel extends YWidgetModel {

    @YAttribute(name = "text", category = AttributeGroup.GENERAL)
    private final String text;

    @YAttribute(name = "alignment", category = AttributeGroup.GENERAL)
    private final YAlignment alignment;

    @YAttribute(name = "movable", category = AttributeGroup.GENERAL)
    private final Boolean movable;

    @YAttribute(name = "resizable", category = AttributeGroup.GENERAL)
    private final Boolean resizable;

    @YAttribute(name = "tooltip", category = AttributeGroup.GENERAL)
    private final String toolTipText;

    @YAttribute(name = "width", category = AttributeGroup.GENERAL)
    private final Integer width;

    public YTableViewerColumnModel(YTableColumn column) {
        super(column);
        text = column.getText();
        alignment = column.getAlignment();
        movable = column.getMoveable();
        resizable = column.getResizable();
        toolTipText = column.getToolTipText();
        width = column.getWidth();
    }

    @Override
    protected void setWidgetAttributes(YWidget widget) {
        super.setWidgetAttributes(widget);
        YTableColumn column = (YTableColumn) widget;
        if (text != null) column.setText(text);
        if (alignment != null) column.setAlignment(alignment);
        if (movable != null) column.setMoveable(movable);
        if (resizable != null) column.setResizable(resizable);
        if (toolTipText != null) column.setToolTipText(toolTipText);
        if (width != null) column.setWidth(width);
    }
}
