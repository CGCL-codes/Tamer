package org.chon.cms.menu.model.factory;

import org.chon.cms.menu.model.IMenuItem;
import org.chon.cms.model.content.IContentNode;

public interface MiFactory {

    public static final MiFactory Instance = new MiFactoryImpl();

    public IMenuItem createMenuItem(IContentNode contentNode, IMenuItem parent);
}
