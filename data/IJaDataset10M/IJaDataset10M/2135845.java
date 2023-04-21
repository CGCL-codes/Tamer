package fr.itris.glips.extension.jwidget.trends.runtime.view.component.tools;

import javax.swing.*;
import fr.itris.glips.extension.jwidget.trends.runtime.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.*;
import fr.itris.glips.extension.jwidget.trends.runtime.view.component.*;
import java.awt.*;
import java.awt.event.*;

/**
 * the class of the pause button tool
 * @author ITRIS, Jordi SUC
 */
public class DisplayCurvesPropertiesTool extends JToggleButton {

    /**
	 * the id
	 */
    public static String id = "DisplayCurvesProperties";

    /**
	 * the listener to the button
	 */
    private ActionListener listener;

    /**
	 * the components handler
	 */
    private ComponentsHandler componentsHandler;

    /**
	 * the constructor of the class
	 * @param toolBar the tool bar component
	 */
    public DisplayCurvesPropertiesTool(ToolBarComponent toolBar) {
        componentsHandler = toolBar.getComponentsHandler();
        build();
    }

    /**
	 * builds the button
	 */
    protected void build() {
        String label = TrendsBundle.bundle.getString(id);
        setToolTipText(label);
        setIcon(TrendsIcons.getIcon(id, false));
        setSelected(componentsHandler.getView().getController().getConfiguration().displayCurvesLegend());
        listener = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                Point location = new Point(getWidth() / 2, getHeight() / 2);
                SwingUtilities.convertPointToScreen(location, DisplayCurvesPropertiesTool.this);
                JComponent destCmp = componentsHandler.getView().getController().getJwidgetRuntime().getPicture().getCanvas();
                SwingUtilities.convertPointFromScreen(location, destCmp);
                componentsHandler.getCurvesPropertiesComponent().setLocation(location);
                componentsHandler.getCurvesPropertiesComponent().setVisible(isSelected(), null);
            }
        };
        addActionListener(listener);
    }

    /**
	 * disposes the object
	 */
    public void dispose() {
        removeActionListener(listener);
        listener = null;
        componentsHandler = null;
    }
}
