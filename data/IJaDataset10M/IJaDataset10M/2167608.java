package org.wikiup.servlet.imp.action;

import org.wikiup.core.inf.Document;
import org.wikiup.database.orm.Entity;
import org.wikiup.servlet.ServletProcessorContext;
import org.wikiup.servlet.inf.ServletAction;
import org.wikiup.servlet.util.ActionUtil;
import org.wikiup.util.DocumentUtil;

public class SelectServletAction implements ServletAction {

    public void doAction(ServletProcessorContext context, Document node) {
        Entity entity = ActionUtil.getEntity(context, node);
        context.setContextEntityProperties(node, entity);
        entity.setDirty(DocumentUtil.getAttributeBooleanValue(node, "dirty", true));
    }
}
