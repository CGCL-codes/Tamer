package org.jmesa.view.html.event;

import org.jmesa.view.html.component.HtmlRow;

/**
 * @since 2.2
 * @author Jeff Johnston
 */
public class MouseRowEvent extends AbstractRowEvent {

    private String styleClass;

    @Override
    public HtmlRow getRow() {
        return (HtmlRow) super.getRow();
    }

    protected boolean isHighlighter() {
        return getRow().isHighlighter();
    }

    protected String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String execute(Object item, int rowcount) {
        if (isHighlighter()) {
            return "this.className='" + getStyleClass() + "'";
        }
        return "";
    }
}
