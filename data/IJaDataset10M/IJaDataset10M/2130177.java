package com.j2biz.compote.plugins.news.model;

import net.sf.hibernate.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.j2biz.compote.model.IUserExtension;

/**
 * @author michelson
 * @version $$
 * @since 0.1
 * 
 * 
 */
public class NewsUserExtension implements IUserExtension {

    private static final Log log = LogFactory.getLog(NewsUserExtension.class);

    public NewsUserExtension() {
    }

    public Object getValue(Object key) {
        if (log.isDebugEnabled()) {
            log.debug(">>> Method getValue in NewsUserExtension not implemented yet");
        }
        return null;
    }

    public void beforeSave(Session session, Object object) {
        if (log.isDebugEnabled()) {
            log.debug(">>> Method beforeSave in NewsUserExtension not implemented yet");
        }
    }

    public void afterSave(Session session, Object object) {
        log.info(">>> Method afterSave in NewsUserExtension not implemented yet");
    }

    public void onUpdate(Session session, Object object) {
        log.info(">>> Method onUpdate in NewsUserExtension not implemented yet");
    }

    public void onDelete(Session session, Object object) {
        log.info(">>> Method onDelete in NewsUserExtension not implemented yet");
    }

    public void onLoad(Session session, Object object) {
    }
}
