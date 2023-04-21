package com.google.code.jqwicket.ui.selectable;

import com.google.code.jqwicket.IJQUIWidget;
import com.google.code.jqwicket.api.IJQFunction;
import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * @author mkalina
 * 
 */
public interface ISelectable extends IJQUIWidget<SelectableOptions> {

    static final CharSequence JQ_COMPONENT_NAME = "selectable";

    /**
	 * Refresh the position and size of each selectee element. This method can
	 * be used to manually recalculate the position and size of each selectee
	 * element. Very useful if autoRefresh is set to false.
	 */
    IJQFunction refresh();

    /**
	 * Refresh the position and size of each selectee element. This method can
	 * be used to manually recalculate the position and size of each selectee
	 * element. Very useful if autoRefresh is set to false.
	 * 
	 * @param ajaxRequestTarget
	 */
    void refresh(AjaxRequestTarget ajaxRequestTarget);
}
