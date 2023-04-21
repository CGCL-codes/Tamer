package com.j2biz.blogunity.web.decorator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.j2biz.blogunity.pojo.Category;

public class GlobalCategoriesTableDecorator extends AbstractTableDecorator {

    /**
     * Logger for this class
     */
    private static final Log log = LogFactory.getLog(GlobalCategoriesTableDecorator.class);

    public String getActions() {
        Category category = (Category) getCurrentRowObject();
        StringBuffer out = new StringBuffer();
        out.append("<nobr>");
        out.append("<a href=\"");
        out.append(ctx);
        out.append("/editGlobalCategoryForm.secureaction?id=");
        out.append(category.getId());
        out.append("\">");
        out.append(getMessageForKey("EDIT"));
        out.append("</a>");
        out.append("&nbsp;|&nbsp;");
        out.append("<a href=\"");
        out.append(ctx);
        out.append("/deleteGlobalCategoryConfirm.secureaction?id=");
        out.append(category.getId());
        out.append("\">");
        out.append(getMessageForKey("DELETE"));
        out.append("</a>");
        out.append("</nobr>");
        return out.toString();
    }
}
