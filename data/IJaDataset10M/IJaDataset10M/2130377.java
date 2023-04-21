package com.nexirius.framework.layout;

import com.nexirius.framework.datamodel.DataModel;
import com.nexirius.framework.datamodel.DataModelCommand;
import com.nexirius.framework.dataviewer.DataViewer;
import javax.swing.*;

/**
 * This class is used to define the position and size of a CommandViewer component.
 *
 * @see com.nexirius.framework.dataviewer.CommandViewer
 */
public class ButtonLayoutItem extends DefaultLayoutItem {

    public ButtonLayoutItem() {
        super();
    }

    /**
     * @param field The associated method command name
     * @param x     the x position of the component
     * @param y     the y position of the component
     * @param w     the width of the component (if  0 then the preferred width is used)
     * @param h     the height of the component (if  0 then the preferred height is used)
     */
    public ButtonLayoutItem(String field, int x, int y, int w, int h) {
        super(field, x, y, w, h);
    }

    /**
     * @param field    The associated structure field name
     * @param x        the x position of the component
     * @param y        the y position of the component
     * @param w        the width of the component (if  0 then the preferred width is used)
     * @param h        the height of the component (if  0 then the preferred height is used)
     * @param viewerId Use the specified id as the component name (this name is used with DataViewer.getSubcomponent)
     * @see com.nexirius.framework.dataviewer.DataViewer#getSubcomponent
     */
    public ButtonLayoutItem(String field, int x, int y, int w, int h, String viewerId) {
        this(field, x, y, w, h);
        setViewerId(viewerId);
    }

    /**
     * @param field    The associated structure field name
     * @param x        the x position of the component
     * @param y        the y position of the component
     * @param w        the width of the component (if  0 then the preferred width is used)
     * @param h        the height of the component (if  0 then the preferred height is used)
     * @param viewerId Use the specified id as the component name (this name is used with DataViewer.getSubcomponent)
     * @param prop     Any property changer
     * @see com.nexirius.framework.dataviewer.DataViewer#getSubcomponent
     */
    public ButtonLayoutItem(String field, int x, int y, int w, int h, String viewerId, PropertyEntry prop) {
        super(field, x, y, w, h, viewerId);
        setProperties(prop);
    }

    /**
     * Create and layout the button component
     *
     * @param parent_viewer The parent viewer which displays the newly created component on its panel.
     */
    public void doLayout(DataViewer parent_viewer) {
        try {
            DataModel parent_model = parent_viewer.getDataModel();
            DataModelCommand method = parent_model.getMethod(getModelFieldName());
            DataViewer viewer = parent_viewer.getFactory().createDefaultEditor(method);
            JComponent comp = viewer.getJComponent();
            parent_viewer.add(comp);
            defaultLayout(comp, parent_viewer.getFactory());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
