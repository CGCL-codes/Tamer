package com.jacum.cms.rcp.ui.editors.item.stylededitor.actions;

import org.eclipse.jface.text.projection.ProjectionDocument;
import org.eclipse.jface.viewers.ISelectionProvider;
import com.jacum.cms.rcp.ui.Messages;
import com.jacum.cms.rcp.ui.editors.item.stylededitor.painters.AbstractPainter;
import com.jacum.cms.rcp.ui.editors.item.stylededitor.painters.IConstructions;

/**
 * 
 * 
 * @author rich
 */
public class ItalicStyleAction extends EditorAction {

    public static final String TOOL_TIP = Messages.getString("ItalicStyleAction.0");

    public static final String ID = "italic.style.action";

    /**
	 * 
	 * 
	 * @param painter
	 * @param document
	 * @param selectionProvider
	 */
    public ItalicStyleAction(AbstractPainter painter, ProjectionDocument document, ISelectionProvider selectionProvider) {
        super(painter, selectionProvider, "italic");
        setToolTipText(TOOL_TIP);
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    protected String[] getConstructions() {
        return IConstructions.ITALIC_CONSTRUCTIONS;
    }

    /**
	 * 
	 */
    @Override
    protected boolean isSelectionMustBeEmpty() {
        return false;
    }
}
