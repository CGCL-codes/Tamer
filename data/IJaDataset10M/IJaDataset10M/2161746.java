package com.nexirius.framework.dataviewer;

import com.nexirius.framework.FWLog;
import com.nexirius.framework.dataeditor.StructEditorCreator;
import com.nexirius.framework.datamodel.DataModel;
import com.nexirius.framework.datamodel.DataModelEvent;
import com.nexirius.framework.datamodel.StructModel;
import com.nexirius.framework.layout.DefaultLayoutItem;
import com.nexirius.framework.layout.LayoutEnumeration;
import com.nexirius.framework.layout.StructureLayoutItem;
import javax.swing.*;

/**
 * This viewer is designed to show two members of a StructModel in a Swing JSplitPane
 *
 * @author Marcel Baumann
 */
public class SplitPaneViewer extends DataViewer {

    public static final String s_viewername = "SplitPaneViewer";

    private int orientation;

    private double deviderLocation;

    /**
     * Creates a JSplitPane
     *
     * @param model           The first two children of this model are beeing shown (unless a layout specifies another pair of children
     * @param orientation     JSplitPane.HORIZONTAL_SPLIT or JSplitPane.VERTICAL_SPLIT
     * @param deviderLocation percentage for the position of the devider from 0.0 (left/top) to 1.0 (right/bottom)
     */
    public SplitPaneViewer(StructModel model, int orientation, double deviderLocation) {
        super(model);
        this.orientation = orientation;
        this.deviderLocation = deviderLocation;
    }

    /**
     * Returns the associated struct model
     */
    public StructModel getModel() {
        return (StructModel) getDataModel();
    }

    /**
     * Returns the associated CFJTabbedPane or null (when called before creation)
     */
    public JSplitPane getJSplitPane() {
        return (JSplitPane) getJComponent();
    }

    /**
     * Creates the actual component
     */
    public void create() {
        if (this.factory == null) {
            throw new RuntimeException("Can't create a TabbedViewer with null factory (call setFactory())");
        }
        setJComponent(new JSplitPane(orientation));
        getJSplitPane().setDividerLocation(deviderLocation);
        getJSplitPane().setDividerSize(10);
        getJSplitPane().setOneTouchExpandable(true);
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
     * Removes all the tabs and recreates them (Typically called by structure change events)
     */
    public void update() {
        if (isCreated()) {
            getJSplitPane().setLeftComponent(new JPanel());
            getJSplitPane().setRightComponent(new JPanel());
            if (getLayout() instanceof StructureLayoutItem) {
                StructureLayoutItem l = (StructureLayoutItem) getLayout();
                LayoutEnumeration e = l.getStructureLayoutArray().getEnumeration();
                DefaultLayoutItem leftLayout = (DefaultLayoutItem) e.next();
                DefaultLayoutItem rightLayout = (DefaultLayoutItem) e.next();
                if (leftLayout != null) {
                    try {
                        DataViewer viewer;
                        StructureLayoutItem structureLayout = new StructureLayoutItem();
                        if (isEditor()) {
                            viewer = factory.createViewer(new StructEditorCreator(), getModel());
                        } else {
                            viewer = factory.createViewer(new StructViewerCreator(), getModel());
                        }
                        structureLayout.append(leftLayout);
                        viewer.setLayout(structureLayout);
                        getJSplitPane().setLeftComponent(viewer.getJComponent());
                    } catch (Exception ex1) {
                        ex1.printStackTrace();
                    }
                }
                if (rightLayout != null) {
                    try {
                        DataViewer viewer;
                        StructureLayoutItem structureLayout = new StructureLayoutItem();
                        if (isEditor()) {
                            viewer = factory.createViewer(new StructEditorCreator(), getModel());
                        } else {
                            viewer = factory.createViewer(new StructViewerCreator(), getModel());
                        }
                        structureLayout.append(rightLayout);
                        viewer.setLayout(structureLayout);
                        getJSplitPane().setRightComponent(viewer.getJComponent());
                    } catch (Exception ex1) {
                        ex1.printStackTrace();
                    }
                }
            } else {
                if (getLayout() != null) {
                    FWLog.log("Unsupported layout for " + getClass().getName() + " have " + getLayout().getClass().getName() + " need instanceof StructureLayoutItem");
                }
                DataModel dm = getModel().getChildren().firstItem();
                if (dm != null) {
                    try {
                        getJSplitPane().setLeftComponent(factory.createViewer(dm, isEditor()).getJComponent());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    dm = getModel().getChildren().nextItem();
                    if (dm != null) {
                        try {
                            getJSplitPane().setRightComponent(factory.createViewer(dm, isEditor()).getJComponent());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
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
