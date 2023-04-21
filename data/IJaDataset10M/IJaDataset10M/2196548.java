package org.middleheaven.ui.testui;

import org.middleheaven.ui.Displayable;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.rendering.AbstractRenderKit;
import org.middleheaven.ui.rendering.UIRender;
import org.middleheaven.ui.rendering.UIUnitConverter;

public class TestRenderKit extends AbstractRenderKit {

    private static final long serialVersionUID = 925791236014175947L;

    final TestUIRender render = new TestUIRender();

    final UIUnitConverter converter = new UIUnitConverter() {

        @Override
        protected double[] getDialogBaseUnits(Displayable layoutable) {
            return new double[] { 100.0, 100.0 };
        }
    };

    @Override
    protected boolean isRendered(UIComponent component) {
        return component.isRendered();
    }

    @Override
    public UIUnitConverter getUnitConverted() {
        return converter;
    }

    public <T extends UIComponent> UIRender getRender(Class<T> componentType, String familly) {
        return render;
    }

    @Override
    public void show(UIComponent component) {
        component.setVisible(true);
    }

    @Override
    public void dispose(UIComponent splash) {
    }
}
