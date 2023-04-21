package org.geogurus.gas.forms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.geogurus.mapserver.objects.Label;

/**
 *
 * @author gnguessan
 */
public class LabelForm extends org.apache.struts.action.ActionForm {

    private Label label;

    private String labelItem;

    private String labelAngleItem;

    private String labelSizeItem;

    private int bitmapSize;

    private int truetypeSize;

    private String labelColor;

    private String labelOutlineColor;

    private Integer labelOutlineWidth;

    private String labelWrap;

    private Integer labelAlign;

    private Integer labelMaxLength;

    private String labelBackgroundColor;

    /**
     *
     */
    public LabelForm() {
        super();
        this.label = new Label();
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        return errors;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public String getLabelItem() {
        return labelItem;
    }

    public void setLabelItem(String labelItem) {
        this.labelItem = labelItem;
    }

    public String getLabelAngleItem() {
        return labelAngleItem;
    }

    public void setLabelAngleItem(String labelAngleItem) {
        this.labelAngleItem = labelAngleItem;
    }

    public String getLabelSizeItem() {
        return labelSizeItem;
    }

    public void setLabelSizeItem(String labelSizeItem) {
        this.labelSizeItem = labelSizeItem;
    }

    public int getBitmapSize() {
        return bitmapSize;
    }

    public void setBitmapSize(int bitmapSize) {
        this.bitmapSize = bitmapSize;
    }

    public int getTruetypeSize() {
        return truetypeSize;
    }

    public void setTruetypeSize(int truetypeSize) {
        this.truetypeSize = truetypeSize;
    }

    public String getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }

    public String getLabelOutlineColor() {
        return labelOutlineColor;
    }

    public Integer getLabelOutlineWidth() {
        return labelOutlineWidth;
    }

    public void setLabelOutlineWidth(Integer labelOutlineWidth) {
        this.labelOutlineWidth = labelOutlineWidth;
    }

    public void setLabelOutlineColor(String labelOutlineColor) {
        this.labelOutlineColor = labelOutlineColor;
    }

    public String getLabelBackgroundColor() {
        return labelBackgroundColor;
    }

    public void setLabelBackgroundColor(String labelBackgroundColor) {
        this.labelBackgroundColor = labelBackgroundColor;
    }

    public void setLabelWrap(String labelWrap) {
        this.labelWrap = labelWrap;
    }

    public String getLabelWrap() {
        return labelWrap;
    }

    public Integer getLabelAlign() {
        return labelAlign;
    }

    public void setLabelAlign(Integer labelAlign) {
        this.labelAlign = labelAlign;
    }

    public Integer getLabelMaxLength() {
        return labelMaxLength;
    }

    public void setLabelMaxLength(Integer labelMaxLength) {
        this.labelMaxLength = labelMaxLength;
    }
}
