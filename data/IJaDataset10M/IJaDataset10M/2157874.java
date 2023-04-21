package com.nexirius.framework.dataviewer;

import com.nexirius.framework.datamodel.DataModel;
import com.nexirius.framework.datamodel.DataModelEvent;
import com.nexirius.framework.datamodel.DataModelVector;
import com.nexirius.framework.datamodel.StructModel;
import javax.swing.*;
import java.awt.*;

/**
 * This viewer is designed to show StructModel data in a Panel with Card Layout
 *
 * @author Marcel Baumann
 */
public class CardViewer extends DataViewer {

    public static final String s_viewername = "CardViewer";

    public CardViewer(StructModel model) {
        super(model);
    }

    /**
     * Returns the associated struct model
     */
    public StructModel getModel() {
        return (StructModel) getDataModel();
    }

    /**
     * Returns the associated JPanel or null (when called before creation)
     */
    public JPanel getJPanel() {
        return (JPanel) getJComponent();
    }

    private CardLayout getCardLayout() {
        return (CardLayout) getJPanel().getLayout();
    }

    /**
     * Creates the actual component
     */
    public void create() {
        if (this.factory == null) {
            throw new RuntimeException("Can't create a TabbedViewer with null factory (call setFactory())");
        }
        setJComponent(new JPanel());
        getJPanel().setLayout(new CardLayout());
        update();
    }

    /**
     * Not used
     */
    public void dataModelChangeValue(DataModelEvent event) {
    }

    /**
     * Calls update method
     *
     * @see #update
     */
    public void dataModelChangeStructure(DataModelEvent event) {
        update();
    }

    /**
     * The tab which displays the child which transmitted the grab focus event is beeing
     * selected (assotiated tab appears)
     */
    public void dataModelGrabFocus(DataModelEvent event) {
        if (isCreated() && event.fromChild()) {
            DataModel child = null;
            DataModelVector transmitter = event.getTransmitterVector();
            if (transmitter.size() > 1) {
                child = transmitter.getItem(1);
            } else {
                return;
            }
            getCardLayout().show(getJPanel(), child.getFieldName());
        }
    }

    /**
     * Creates a new card which is based on the given data model (called by the update method
     * for every child of the associated structure)
     *
     * @see #update
     */
    public void createCard(DataModel dm) {
        try {
            JComponent component = this.factory.createDefaultEditor(dm).getJComponent();
            getJPanel().add(dm.getFieldName(), component);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Removes all the tabs and recreates them (Typically called by structure change events)
     */
    public void update() {
        if (isCreated()) {
            getJPanel().removeAll();
            if (getLayout() == null) {
                for (DataModel dm = getModel().getChildren().firstItem(); dm != null; dm = getModel().getChildren().nextItem()) {
                    createCard(dm);
                }
            } else {
                getLayout().doLayout(this);
            }
        }
    }

    /**
     * Only for debugging
     */
    public String getViewerName() {
        return s_viewername;
    }
}
