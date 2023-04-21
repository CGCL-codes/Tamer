package com.wgo.precise.client.ui.model.facade;

import org.eclipse.swt.graphics.Image;

/**
 * 
 * Implementet by all object inn a tree to get image and text.
 * 
 * @author Petter L. H. Eide
 * @version $Id: ILabelProviderItem.java,v 1.2 2006-01-30 21:52:32 petterei Exp $
 * 
 * Changelog:
 * 
 * $Log: ILabelProviderItem.java,v $
 * Revision 1.2  2006-01-30 21:52:32  petterei
 * implemented wrapper factory and lazy wrapper-instantiation of parents
 * implemented a "system" for each top-node type
 * added top-categories for all top-nodes
 * implemented some interfaces for the wrappers, and started using these
 *
 * Revision 1.1  2006-01-23 19:58:37  petterei
 * initial commit for client, fitting the alternative session implementation
 *
 * Revision 1.3  2005-12-07 07:25:35  petterei
 * gui & logic for relationships
 *
 * Revision 1.2  2005-12-05 04:16:40  petterei
 * *** empty log message ***
 *
 * Revision 1.1  2005/09/27 17:04:29  petterei
 * inittial commit
 *
 * Revision 1.2  2005/08/23 06:41:14  peide3
 * *** empty log message ***
 *
 * Revision 1.1  2005/07/31 22:55:09  petterei
 * initial commit
 *
 * Revision 1.1  2005/07/20 06:11:45  peide3
 * Interfaces needed by different views
 *
 *
 */
public interface ILabelProviderItem {

    /**
     * Returns the image for the label of the given element.  The image
     * is owned by the label provider and must not be disposed directly.
     * Instead, dispose the label provider when no longer needed.
     *
     * @return the image used to label the element, or <code>null</code>
     *   if there is no image for the given object
     */
    public Image getImage();

    /**
     * Returns the text for the label of the given element.
     *
     * @return the text string used to label the element, or <code>null</code>
     *   if there is no text label for the given object
     */
    public String getText();

    public String getSimpleName();

    public String getToolTipText();
}
