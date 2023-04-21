package org.gwtoolbox.widget.client.support.setter;

import com.google.gwt.user.client.Element;
import org.gwtoolbox.commons.util.client.UIUtils;

/**
 * @author Uri Boness
 */
public class ElementVisibilitySetter implements ValueSetter<Boolean> {

    private final Element element;

    public ElementVisibilitySetter(Element element) {
        this.element = element;
    }

    public void set(Boolean visible) {
        boolean isVisible = visible != null && visible;
        UIUtils.setVisible(element, isVisible);
    }
}
