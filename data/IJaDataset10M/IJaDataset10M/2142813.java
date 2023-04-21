package topiaryexplorer;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.jnlp.*;

/**
 * TopiaryMenu is the main menu bar for TopiaryTool
 */
public class ColorByMenu extends JMenu {

    private TopiaryWindow parent;

    private MainFrame frame;

    private ColorPanel colorPanel;

    private int elementType = 0;

    private JMenu colorByOtuMetadataMenu = new JMenu("OTU Metadata");

    private JMenu colorBySampleMetadataMenu = new JMenu("Sample Metadata");

    private ButtonGroup colorByGroup = new ButtonGroup();

    /**
     * Creates a menu for the specified element type.
     */
    public ColorByMenu(MainFrame _frame, TopiaryWindow _parent, ColorPanel _colorPanel, int _elementType) {
        super("Color By");
        frame = _frame;
        parent = _parent;
        colorPanel = _colorPanel;
        elementType = _elementType;
        add(colorByOtuMetadataMenu);
        add(colorBySampleMetadataMenu);
    }

    /**
    * Resets colorby OTU menu when a new otu table is loaded.
    */
    public void resetColorByOtuMenu() {
        colorByOtuMetadataMenu.removeAll();
        ArrayList<String> data = frame.otuMetadata.getColumnNames();
        for (int i = 1; i < data.size(); i++) {
            String value = data.get(i);
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(value);
            colorByGroup.add(item);
            item.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    String value = e.getActionCommand();
                    frame.currTable = frame.otuMetadata;
                    frame.colorPane.setSelectedIndex(elementType);
                    if (elementType == 0) ((TreeWindow) parent).colorBranchesByValue(value); else if (elementType == 1) ((TreeWindow) parent).colorLabelsByValue(value);
                    TableColumn column = colorPanel.getColorKeyTable().getColumnModel().getColumn(0);
                    column.setHeaderValue(value);
                }
            });
            colorByOtuMetadataMenu.add(item);
        }
    }

    /**
    * Resets colorby Sample menu when a new sample metadata table is loaded.
    */
    public void resetColorBySampleMenu() {
        colorBySampleMetadataMenu.removeAll();
        ArrayList<String> data = frame.sampleMetadata.getColumnNames();
        for (int i = 0; i < data.size(); i++) {
            String value = data.get(i);
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(value);
            item.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    String value = e.getActionCommand();
                    frame.currTable = frame.sampleMetadata;
                    frame.colorPane.setSelectedIndex(elementType);
                    if (elementType == 0) ((TreeWindow) parent).colorBranchesByValue(value); else if (elementType == 1) ((TreeWindow) parent).colorLabelsByValue(value);
                    TableColumn column = colorPanel.getColorKeyTable().getColumnModel().getColumn(0);
                    column.setHeaderValue(value);
                }
            });
            colorByGroup.add(item);
            colorBySampleMetadataMenu.add(item);
        }
    }
}
