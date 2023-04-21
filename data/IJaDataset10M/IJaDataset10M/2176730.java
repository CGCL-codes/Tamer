package com.vividsolutions.jump.workbench.ui;

import java.util.Collection;
import com.vividsolutions.jump.workbench.model.Layer;
import com.vividsolutions.jump.workbench.model.LayerManagerProxy;

public interface LayerNamePanel extends LayerManagerProxy {

    public Collection getSelectedCategories();

    public Collection selectedNodes(Class c);

    public Layer[] getSelectedLayers();

    /** 
     * @return e.g. the first selected editable layer, otherwise the first editable layer, otherwise null
     */
    public Layer chooseEditableLayer();

    public void addListener(LayerNamePanelListener listener);

    public void removeListener(LayerNamePanelListener listener);

    /**
     * The parent window is closing.
     */
    public void dispose();
}
