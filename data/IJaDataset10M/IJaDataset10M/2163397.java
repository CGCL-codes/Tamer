package org.fantasy.cpp.core.item.tree;

import org.fantasy.cpp.core.bean.ParamContext;
import org.fantasy.cpp.core.html.query.QueryHtmlFactory;

/**
 * 树
 * 
 * @author: 王文成
 * @version: 1.0
 * @since 2009-11-11
 */
public class TreeItem extends AbstractTreeItem {

    @Override
    public String getHtml() throws Exception {
        ParamContext param = getParamContext();
        return QueryHtmlFactory.getTreeBuilder(request, param).getHtml();
    }
}
