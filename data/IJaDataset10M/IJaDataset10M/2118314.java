package com.wrupple.muba.catalogs.client.widgets.fields.cells.templates;

import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;

public interface Button extends SafeHtmlTemplates {

    @Template("<input type='button' value='{0}' />")
    SafeHtml button(String message);
}
