package de.miethxml.hawron.gui.context.bookmark;

import javax.swing.JComponent;
import de.miethxml.hawron.gui.context.ContextViewComponent;

/**
 * @author <a href="mailto:simon.mieth@gmx.de">Simon Mieth </a>
 *
 *
 *
 *
 *
 *
 *
 */
public interface BookmarkView {

    public JComponent getBookmarkView();

    public void setBaseURL(String baseURL);

    public void setConfigLocation(String location);

    public void setContextViewComponent(ContextViewComponent model);

    public void initialize();
}
