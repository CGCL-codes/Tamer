package org.chon.cms.menu.model.types;

import org.chon.cms.menu.model.BaseMenuItem;
import org.chon.cms.menu.model.IMenuItem;
import org.chon.cms.menu.model.factory.MiFactory;
import org.chon.cms.model.content.IContentNode;

public class MItemLocalLink extends BaseMenuItem {

    public MItemLocalLink(IContentNode node, IMenuItem parent, MiFactory factory) {
        super(node, parent, factory);
    }

    @Override
    public String getLink() {
        return node.prop("link");
    }

    @Override
    public boolean isInternalLink() {
        return true;
    }
}
