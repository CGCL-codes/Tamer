package com.j2biz.compote.plugins.news.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.j2biz.compote.plugins.news.pojos.NewsMessage;
import com.j2biz.compote.util.AbstractTableDecorator;

/**
 * @author michelson
 * @version $$
 * @since 0.1
 * 
 * 
 */
public class MessageTableDecorator extends AbstractTableDecorator {

    /**
     * Logger for this class
     */
    private static final Log log = LogFactory.getLog(MessageTableDecorator.class);

    /**
     * @return
     */
    public String getActions() {
        if (log.isDebugEnabled()) {
            log.debug("getActions() - start");
        }
        initI18N(getPageContext());
        NewsMessage object = (NewsMessage) getCurrentRowObject();
        Long id = object.getId();
        StringBuffer link = new StringBuffer();
        link.append("<a href=\"ManageMessages.do?method=editForm&id=" + id + "\">" + I18N_EDIT + "</a>");
        link.append(" | ");
        link.append("<a href=\"ManageMessages.do?method=removeConfirm&id=" + id + "\">" + I18N_REMOVE + "</a>");
        String returnString = link.toString();
        if (log.isDebugEnabled()) {
            log.debug("getActions() - end");
        }
        return returnString;
    }
}
