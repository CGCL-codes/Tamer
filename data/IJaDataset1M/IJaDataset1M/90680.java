package daam.ui.gwt.client.component;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class ExternalLink extends Widget {

    Element anchorElement;

    public ExternalLink() {
        setElement(anchorElement = DOM.createAnchor());
    }

    public void setTarget(String target) {
        anchorElement.setAttribute("target", target);
    }

    public void setHref(String href) {
        anchorElement.setAttribute("href", href);
    }

    public void setText(String text) {
        DOM.setInnerText(anchorElement, text);
    }
}
