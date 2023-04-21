package org.nextframework.view.template;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.persistence.Id;
import org.nextframework.bean.BeanDescriptor;
import org.nextframework.bean.PropertyDescriptor;
import org.nextframework.util.Util;
import org.nextframework.view.DataGridTag;
import org.nextframework.view.OutputTag;

public class NextPropertyTagFastRenderer implements PropertyTagFastRenderer {

    class LabelAndValue {

        String label;

        Object value;

        boolean id;

        Type type;
    }

    public boolean render(PropertyTag tag) throws Exception {
        boolean rendered = false;
        if (tag.getRenderAs().equals(PropertyTag.COLUMN)) {
            rendered = renderColumn(tag);
        }
        return rendered;
    }

    protected boolean renderColumn(PropertyTag tag) throws IOException, Exception {
        LabelAndValue labelAndValue;
        DataGridTag dataGrid = tag.findParent2(DataGridTag.class, true);
        dataGrid.setHasColumns(true);
        dataGrid.setRenderHeader(true);
        switch(dataGrid.getCurrentStatus()) {
            case HEADER:
                String label;
                labelAndValue = getLabelAndValue(tag);
                if (Util.strings.isEmpty(tag.getLabel())) {
                    label = tag.getHeaderForLabel(labelAndValue.label);
                } else {
                    label = tag.getHeaderForLabel(tag.getLabel());
                }
                if (labelAndValue.id) {
                    tag.getOut().println("<th style=\"width: 1%; padding-right: 3px;" + tag.getHeaderStyle() + "\" class=\"" + tag.getHeaderStyleClass() + "\">" + label + "</th>");
                } else {
                    tag.getOut().println("<th  style=\"" + tag.getHeaderStyle() + "\" class=\"" + tag.getHeaderStyleClass() + "\">" + label + "</th>");
                }
                return true;
            case BODY:
                if (tag.getMode().equals(PropertyTag.OUTPUT)) {
                    String style = Util.strings.toString(tag.getDynamicAttributesMap().get("style"));
                    String styleclass = Util.strings.toString(tag.getDynamicAttributesMap().get("styleclass"));
                    labelAndValue = getLabelAndValue(tag);
                    tag.getOut().println("<td style=\"" + tag.getBodyStyle() + "\" class=\"" + tag.getBodyStyleClass() + "\" align=\"" + tag.getColumnAlignForType(labelAndValue.type) + "\">");
                    OutputTag outputTag = new OutputTag();
                    outputTag.setJspContext(tag.getPageContext());
                    outputTag.setPattern(tag.getPattern() != null ? tag.getPattern() : "");
                    outputTag.setStyle(style);
                    outputTag.setStyleClass(styleclass);
                    if (tag.getTrueFalseNullLabels() != null) {
                        outputTag.setTrueFalseNullLabels(tag.getTrueFalseNullLabels());
                    }
                    outputTag.setEscapeHTML(true);
                    outputTag.setValue(labelAndValue.value);
                    outputTag.doComponent();
                    tag.getOut().println("</td>");
                    return true;
                } else {
                    return false;
                }
        }
        return false;
    }

    private String getStyles(String style, String styleclass) {
        String styles = "";
        if (Util.strings.isEmpty(styles)) {
            styles = " style=\"" + style + "\"";
        }
        if (Util.strings.isEmpty(styleclass)) {
            styles = " class=\"" + styleclass + "\"";
        }
        return styles;
    }

    protected LabelAndValue getLabelAndValue(PropertyTag tag) {
        BeanDescriptor<?> beanDescriptor = org.nextframework.view.PropertyTag.getBeanDescriptor(tag);
        String fullNestedName = org.nextframework.view.PropertyTag.montarFullNestedName(tag, tag.getName());
        PropertyDescriptor propertyDescriptor = org.nextframework.view.PropertyTag.getPropertyDescriptor(fullNestedName, beanDescriptor);
        Object value = org.nextframework.view.PropertyTag.getPropertyValue(beanDescriptor, propertyDescriptor, null);
        String label = org.nextframework.view.PropertyTag.getLabel(beanDescriptor, propertyDescriptor);
        LabelAndValue labelAndValue = new LabelAndValue();
        for (Annotation ann : propertyDescriptor.getAnnotations()) {
            if (ann.annotationType().equals(Id.class)) {
                labelAndValue.id = true;
            }
        }
        labelAndValue.label = label;
        labelAndValue.value = value;
        labelAndValue.type = propertyDescriptor.getType();
        return labelAndValue;
    }
}
