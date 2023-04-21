package uk.ac.lkl.migen.mockup.shapebuilder.ui.plotter;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.apache.log4j.Logger;
import uk.ac.lkl.migen.mockup.shapebuilder.ui.ShapePlotter;

public class GridSizeEditor extends ShapePlotterComponent {

    private JTextField gridSizeField;

    static Logger logger = Logger.getLogger(GridSizeEditor.class);

    public GridSizeEditor(ShapePlotter shapePlotter) {
        super(shapePlotter);
        setLayout(new GridLayout(2, 1));
        add(new JLabel("Grid Size"));
        gridSizeField = new JTextField(5);
        add(gridSizeField);
        addActionListener();
        updateTextField();
    }

    private void addActionListener() {
        gridSizeField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                processActionPerformed();
            }
        });
    }

    protected void processStateChanged() {
        updateTextField();
    }

    private void updateTextField() {
        int gridSize = getShapePlotter().getGridSize();
        gridSizeField.setText(Integer.toString(gridSize));
    }

    private void processActionPerformed() {
        try {
            String gridSizeString = gridSizeField.getText();
            int gridSize = Integer.parseInt(gridSizeString);
            getShapePlotter().setGridSize(gridSize);
            logger.info("Grid size changed to: " + gridSize + ".");
        } catch (NumberFormatException e) {
        }
        updateTextField();
    }
}
