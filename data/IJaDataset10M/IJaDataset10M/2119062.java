package co.fxl.gui.style.gplus;

import co.fxl.gui.api.IHorizontalPanel;
import co.fxl.gui.api.ILabel;
import co.fxl.gui.api.IPanel;
import co.fxl.gui.style.api.IStyle.IOptionMenu;

public class GPlusOptionMenu implements IOptionMenu {

    @Override
    public ILabel addCommand(IPanel<?> panel, String text) {
        ILabel l = panel.add().label().text(text).hyperlink();
        return l;
    }

    @Override
    public IOptionMenu label(ILabel label) {
        return this;
    }

    @Override
    public IOptionMenu searchButton(IHorizontalPanel buttonPanel) {
        buttonPanel.color().rgb(77, 144, 255).gradient().vertical().rgb(71, 135, 237);
        return this;
    }
}
