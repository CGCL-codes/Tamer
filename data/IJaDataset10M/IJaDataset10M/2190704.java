package com.jacum.cms.rcp.ui.editors.item.stylededitor.painters;

import java.text.MessageFormat;
import org.eclipse.jface.text.projection.ProjectionDocument;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.graphics.Color;
import com.jacum.cms.rcp.ui.Messages;
import com.jacum.cms.rcp.ui.RCPImageRegistry;
import com.jacum.cms.rcp.ui.editors.item.stylededitor.ColorConstants;

/**
 * ItemLinkPainter find item link constructions like: <#item href='/static'>item text </a>
 * Painter draw item link icon to mark item link 
 * and set style of the nested text to underlined and blue color.
 * Target item path shows as text hover  
 * 
 * @author rich
 */
public class ItemLinkPainter extends AbstractTriplePainter {

    public static final String CATEGORY = "itemlink";

    private static final String HOVER_TEMPLATE = Messages.getString("ItemLinkPainter.0");

    /**
	 * Constructs new ItemLinkPainter
	 * 
	 * @param document projection document
	 * @param viewer source viewer 
	 */
    public ItemLinkPainter(ProjectionDocument document, ISourceViewer viewer) {
        super(document, viewer, CATEGORY);
    }

    @Override
    public String getImageId() {
        return RCPImageRegistry.ITEM_LINK_ICON;
    }

    @Override
    public String[] getConstructions() {
        return IConstructions.ITEM_LINK_CONSTRUCTIONS;
    }

    /**
	 * Item links highlights with blue color
	 * Method return blue color
	 * 
	 * @return blue color
	 */
    @Override
    protected Color getColor() {
        return ColorConstants.BLUE;
    }

    /** 
	 * Item links marks with underline
	 * Method return true
	 * 
	 * @return true
	 */
    @Override
    protected boolean isUnderline() {
        return true;
    }

    @Override
    protected String getHoverText(String dataText) {
        return MessageFormat.format(HOVER_TEMPLATE, new Object[] { dataText });
    }
}
