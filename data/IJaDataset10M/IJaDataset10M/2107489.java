package org.jowidgets.impl.model.item;

import org.jowidgets.api.model.item.ICheckedItemModel;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.common.types.Accelerator;

class CheckedItemModelImpl extends AbstractSelectableItemModel implements ICheckedItemModel {

    protected CheckedItemModelImpl() {
        this(null, null, null, null, null, null, true, false);
    }

    protected CheckedItemModelImpl(final String id, final String text, final String toolTipText, final IImageConstant icon, final Accelerator accelerator, final Character mnemonic, final boolean enabled, final boolean selected) {
        super(id, text, toolTipText, icon, accelerator, mnemonic, enabled, selected);
    }

    @Override
    public ICheckedItemModel createCopy() {
        final CheckedItemModelImpl result = new CheckedItemModelImpl();
        result.setContent(this);
        return result;
    }
}
