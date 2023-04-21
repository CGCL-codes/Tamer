package org.grapheditor.properties;

import java.awt.Component;
import javax.swing.JPanel;
import javax.swing.JColorChooser;
import org.grapheditor.editor.GraphEditorPane;

/**
 * This is a property change module wich implements the PropertiesModule
 * interface. It is used to change the graph properties for the backround color.
 * 
 * @author kjellw
 * 
 */
public class GraphBackgroundProperties extends JPanel implements PropertiesModule {

    private JColorChooser jColorChooser = null;

    private GraphEditorPane graphPane;

    /**
	 * This method initializes
	 * 
	 */
    public GraphBackgroundProperties(GraphEditorPane graphPane) {
        super();
        this.graphPane = graphPane;
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 */
    private void initialize() {
        this.add(getJColorChooser(), null);
    }

    public String getPropertiesDescription() {
        return "Change the settings for the background color of the graph";
    }

    public Component getPropertiesEditor() {
        return this;
    }

    public String getPropertiesName() {
        return "Background properties";
    }

    public void save() {
        graphPane.setBackground(jColorChooser.getColor());
        if (!graphPane.getGraphProperties().getBackgroundColor().equals(jColorChooser.getColor())) {
            graphPane.getGraphProperties().setBackgroundColor(jColorChooser.getColor());
        }
    }

    public String toString() {
        return getPropertiesName();
    }

    /**
	 * This method initializes jColorChooser
	 * 
	 * @return javax.swing.JColorChooser
	 */
    private JColorChooser getJColorChooser() {
        if (jColorChooser == null) {
            jColorChooser = new JColorChooser();
            jColorChooser.setColor(graphPane.getGraphProperties().getBackgroundColor());
        }
        return jColorChooser;
    }
}
