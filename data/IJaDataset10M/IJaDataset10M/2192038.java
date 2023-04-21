package com.jtri.struts;

/**
 * A form with a set of buttons that call other forms.
 * @author atorres
 */
public interface ICallerForm {

    public ToolBarManager getLoveButtons();

    public void setLoveButtons(ToolBarManager manager);
}
