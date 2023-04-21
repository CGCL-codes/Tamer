package org.goet.kits;

import org.goet.gui.*;
import org.goet.datamodel.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;

public class ImageEditor extends AbstractEditor {

    protected JScrollPane scroller = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    protected JScrollPane termScroller = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    protected RegionEditor editor = new RegionEditor(this);

    protected JToolBar toolbar = new JToolBar();

    protected JToolBar optionBar = new JToolBar();

    protected ButtonGroup buttonGroup = new ButtonGroup();

    protected JList termList = new JList();

    protected JLabel imageLabel = new JLabel("Image url");

    protected JTextField imageField = new JTextField();

    protected Node image;

    public ImageEditor() {
        termScroller.setViewportView(termList);
        termList.setCellRenderer(EditorKit.termRenderer);
        termList.registerKeyboardAction(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Node term = (Node) termList.getSelectedValue();
                Node image = editor.getImageNode();
                controller.applyHistoryItem(new TermDeleteHistoryItem(image, term), false);
                signalUpdate();
            }
        }, KeyStroke.getKeyStroke("DELETE"), JComponent.WHEN_FOCUSED);
        JPanel termPanel = new JPanel();
        termPanel.setLayout(new BoxLayout(termPanel, BoxLayout.Y_AXIS));
        termPanel.add(new JLabel("Terms associated with image"));
        termPanel.add(termScroller);
        termPanel.setPreferredSize(new Dimension(200, 400));
        requestFocus();
        setLayout(new BorderLayout());
        scroller.setViewportView(editor);
        JPanel fileBox = new JPanel();
        fileBox.setLayout(new BoxLayout(fileBox, BoxLayout.Y_AXIS));
        fileBox.add(imageLabel);
        fileBox.add(imageField);
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BorderLayout());
        imagePanel.add(scroller, "Center");
        imagePanel.add(fileBox, "South");
        add(imagePanel, "Center");
        add(toolbar, "North");
        add(optionBar, "South");
        add(termPanel, "East");
        editor.setOptionBar(optionBar);
        JToggleButton defaultButton = createToolButton("Pick", new SelectTool());
        toolbar.add(defaultButton);
        toolbar.add(createToolButton("Zoom", new ZoomTool()));
        toolbar.add(createToolButton("Pen", new PenTool()));
        toolbar.add(createToolButton("Text", new TextTool()));
        imageField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateImageURL();
            }
        });
        defaultButton.doClick();
    }

    protected void updateImageURL() {
        HistoryItem item = new TextEditHistoryItem(new NodePropertyValue(image, SCHEMA.imageLoc, image.getFirstValue(SCHEMA.imageLoc)), BasicTypes.STRING_TYPE.getObject(imageField.getText()));
        controller.applyHistoryItem(item, false);
        editor.reloadImage();
        editor.reload();
    }

    protected void signalUpdate() {
        termList.setListData(new Vector(editor.getImageNode().getPropertyValues(SCHEMA.hasTerm)));
        termList.repaint();
    }

    protected JToggleButton createToolButton(String text, final Tool tool) {
        JToggleButton out = new JToggleButton(text);
        out.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                editor.setTool(tool);
            }
        });
        buttonGroup.add(out);
        return out;
    }

    public void load(NodePropertyValue npv) {
        image = npv.getNodeValue();
        editor.load(image);
        imageField.setText(NodeUtil.getStringValue(image, SCHEMA.imageLoc));
        signalUpdate();
        revalidate();
    }

    public HistoryItem getHistoryItem() {
        return null;
    }
}
