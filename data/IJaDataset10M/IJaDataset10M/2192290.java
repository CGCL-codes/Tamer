package org.formaria.editor.project.pages.events;

import java.util.Vector;
import org.formaria.editor.project.pages.IPagePanel;

/**
 * <p>Copyright: Copyright (c) Formaria Ltd., 1998-2003</p>
 */
public interface ComponentSelectListener {

    /**
   * Called when a component has been selected
   * @param selectedComponents
   */
    public void setSelectedComponents(IPagePanel currentPanel, Vector selectedComponents, boolean contentsChanged);
}
