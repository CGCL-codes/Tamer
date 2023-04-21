package com.antilia.web.field.impl;

import org.apache.wicket.Component;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.util.string.Strings;
import com.antilia.web.resources.DefaultStyle;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class AntiliaDatePicker extends DatePicker {

    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public AntiliaDatePicker() {
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference(DefaultStyle.CALENDAR_CSS);
    }

    @Override
    public void onRendered(Component component) {
        Response response = component.getResponse();
        response.write("\n<span class=\"yui-skin-ant\">&nbsp;<span style=\"");
        if (renderOnLoad()) {
            response.write("display:block;");
        } else {
            response.write("display:none;");
            response.write("position:absolute;");
        }
        response.write("z-index: 99999;\" id=\"");
        response.write(getEscapedComponentMarkupId());
        response.write("Dp\"></span>");
        response.write("<span style=\"\">");
        response.write("<img style=\"");
        response.write(getIconStyle());
        response.write("\" id=\"");
        response.write(getIconId());
        response.write("\" src=\"");
        CharSequence iconUrl = getIconUrl();
        response.write(Strings.escapeMarkup(iconUrl != null ? iconUrl.toString() : ""));
        response.write("\" alt=\"\"/>");
        response.write("</span>");
        if (renderOnLoad()) {
            response.write("<br style=\"clear:left;\"/>");
        }
        response.write("</span>");
    }

    @Override
    protected CharSequence getIconUrl() {
        return RequestCycle.get().urlFor(DefaultStyle.IMG_DATEPICKER);
    }

    @Override
    protected String getIconStyle() {
        return "cursor: pointer; none; height: 14px; width: 16px; vertical-align: middle";
    }
}
