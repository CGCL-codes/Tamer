package org.jfunfx.jsconstruction.components;

import org.jfunfx.jsconstruction.JFunFXContainer;
import org.jfunfx.jsconstruction.ObjectLocator;

/**
 * date 21.07.2009
 *
 * @author dvponomarev
 * @version 1.0
 */
public class RadioButton extends Button {

    public RadioButton(JFunFXContainer jFunFXContainer, ObjectLocator objectLocator, long delay) {
        super(jFunFXContainer, objectLocator, delay);
    }

    public RadioButton(JFunFXContainer jFunFXContainer, ObjectLocator objectLocator) {
        super(jFunFXContainer, objectLocator);
    }

    public Boolean isSelected() {
        return super.isSelected();
    }
}
