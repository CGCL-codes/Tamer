package net.sf.xpontus.actions.impl;

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.DocumentTabContainer;

/**
 * Action to copy some text
 * @version 0.0.1
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class CopyActionImpl extends DefaultDocumentAwareActionImpl {

    private static final long serialVersionUID = 8827427964965795677L;

    public static final String BEAN_ALIAS = "action.copy";

    public CopyActionImpl() {
    }

    public void run() {
        DocumentTabContainer documentTabContainer = DefaultXPontusWindowImpl.getInstance().getDocumentTabContainer();
        documentTabContainer.getCurrentEditor().copy();
    }
}
