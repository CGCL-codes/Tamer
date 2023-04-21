package org.jowidgets.impl.model.item;

import org.jowidgets.api.model.item.IActionItemModel;
import org.jowidgets.api.model.item.IActionItemModelBuilder;

public class ActionItemModelBuilder extends AbstractActionItemModelBuilder<IActionItemModelBuilder, IActionItemModel> implements IActionItemModelBuilder {

    @Override
    public IActionItemModel build() {
        return new ActionItemModelImpl(getId(), getText(), getToolTipText(), getIcon(), getAccelerator(), getMnemonic(), isEnabled(), getAction());
    }
}
