package org.fantasy.cpp.core.item.input;

import org.fantasy.cpp.core.bean.ParamContext;
import org.fantasy.cpp.core.html.query.QueryHtmlFactory;

/**
 *  简单文本框
 * @author: 王文成
 * @version: 1.0
 * @since 2009-11-11
 */
public class TextSimpleItem extends AbstractInputItem {

    /**
     * 生成HTML
     * 
     * @return
     */
    @Override
    public String getHtml() throws Exception {
        ParamContext param = getParamContext();
        return QueryHtmlFactory.getTextBuilder(request, param).getHtml();
    }
}
