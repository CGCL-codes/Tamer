package com.jgeppert.struts2.jquery.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.components.Component;
import com.jgeppert.struts2.jquery.components.Autocompleter;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * 
 * @see Autocompleter
 * @author <a href="http://www.jgeppert.com">Johannes Geppert</a>
 * 
 */
public class AutocompleterTag extends AbstractFormListElementTag {

    private static final long serialVersionUID = 4675729906623010236L;

    protected String delay;

    protected String loadMinimumCount;

    protected String selectBox;

    protected String selectBoxIcon;

    protected String onSelectTopics;

    protected String onFocusTopics;

    protected String onSearchTopics;

    protected String maxlength;

    protected String readonly;

    protected String size;

    protected String multiple;

    protected String listLabel;

    protected String forceValidOption;

    protected String autoFocus;

    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new Autocompleter(stack, req, res);
    }

    protected void populateParams() {
        super.populateParams();
        Autocompleter completer = (Autocompleter) component;
        completer.setDelay(delay);
        completer.setLoadMinimumCount(loadMinimumCount);
        completer.setSelectBox(selectBox);
        completer.setSelectBoxIcon(selectBoxIcon);
        completer.setOnSelectTopics(onSelectTopics);
        completer.setOnFocusTopics(onFocusTopics);
        completer.setOnSearchTopics(onSearchTopics);
        completer.setMaxlength(maxlength);
        completer.setReadonly(readonly);
        completer.setSize(size);
        completer.setMultiple(multiple);
        completer.setListLabel(listLabel);
        completer.setForceValidOption(forceValidOption);
        completer.setAutoFocus(autoFocus);
    }

    public void setMaxlength(String maxlength) {
        this.maxlength = maxlength;
    }

    public void setReadonly(String readonly) {
        this.readonly = readonly;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public void setLoadMinimumCount(String loadMinimumCount) {
        this.loadMinimumCount = loadMinimumCount;
    }

    public void setSelectBox(String selectBox) {
        this.selectBox = selectBox;
    }

    public void setOnSelectTopics(String onSelectTopics) {
        this.onSelectTopics = onSelectTopics;
    }

    public void setOnFocusTopics(String onFocusTopics) {
        this.onFocusTopics = onFocusTopics;
    }

    public void setOnSearchTopics(String onSearchTopics) {
        this.onSearchTopics = onSearchTopics;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public void setSelectBoxIcon(String selectBoxIcon) {
        this.selectBoxIcon = selectBoxIcon;
    }

    public void setListLabel(String listLabel) {
        this.listLabel = listLabel;
    }

    public void setForceValidOption(String forceValidOption) {
        this.forceValidOption = forceValidOption;
    }

    public void setAutoFocus(String autoFocus) {
        this.autoFocus = autoFocus;
    }
}
