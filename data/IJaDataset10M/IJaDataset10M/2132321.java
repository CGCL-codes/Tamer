package gui.piechart;

import exceptions.NullDataValueException;
import info.clearthought.layout.TableLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import main.OpenPlotTool;
import plot.piechart.PieChartDataSet;
import plot.piechart.PieChartItem;

@SuppressWarnings("serial")
public class PieChartDataDialog extends JDialog implements ActionListener {

    private class ListClickListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent evt) {
            JList list = (JList) evt.getSource();
            if (evt.getClickCount() == 2) {
                int index = list.locationToIndex(evt.getPoint());
                editItem(index);
            }
        }
    }

    public static int ADD_PRESSED = 1;

    private JButton addButton, cancelButton;

    private JButton addItemButton, editItemButton, removeItemButton;

    private JList dataItemList;

    private DefaultListModel dataItemListModel;

    private ArrayList<PieChartItem> dataItems = new ArrayList<PieChartItem>();

    private JTextField nameField;

    private int pressed = 0;

    public PieChartDataDialog(PieChartDataSet currentDataSet) {
        super(OpenPlotTool.getMainFrame(), "Add Data Set", true);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setContentPane(mainPanel);
        JPanel settingsPanel = new JPanel();
        settingsPanel.setBorder(BorderFactory.createTitledBorder("Set Settings: "));
        mainPanel.add(settingsPanel, BorderLayout.PAGE_START);
        double[][] settingsSize = { { 0.25, 0.75 }, { 25 } };
        settingsPanel.setLayout(new TableLayout(settingsSize));
        nameField = new JTextField();
        nameField.setText("pie chart data");
        settingsPanel.add(new JLabel("Name: "), "0, 0");
        settingsPanel.add(nameField, "1, 0");
        JPanel dataPanel = new JPanel(new BorderLayout());
        dataPanel.setBorder(BorderFactory.createTitledBorder("Set Data: "));
        mainPanel.add(dataPanel, BorderLayout.CENTER);
        dataItemListModel = new DefaultListModel();
        dataItemList = new JList(dataItemListModel);
        dataItemList.setCellRenderer(new PieChartItemRender());
        dataItemList.addMouseListener(new ListClickListener());
        JScrollPane dataItemListScroll = new JScrollPane(dataItemList, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        dataPanel.add(dataItemListScroll, BorderLayout.CENTER);
        JPanel sidePanel = new JPanel(new BorderLayout());
        dataPanel.add(sidePanel, BorderLayout.LINE_END);
        JPanel dataButtonPanel = new JPanel(new GridLayout(3, 1));
        sidePanel.add(dataButtonPanel, BorderLayout.PAGE_START);
        addItemButton = new JButton("Add");
        addItemButton.addActionListener(this);
        dataButtonPanel.add(addItemButton);
        editItemButton = new JButton("Edit");
        editItemButton.addActionListener(this);
        dataButtonPanel.add(editItemButton);
        removeItemButton = new JButton("Remove");
        removeItemButton.addActionListener(this);
        dataButtonPanel.add(removeItemButton);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        mainPanel.add(buttonPanel, BorderLayout.PAGE_END);
        addButton = new JButton("Add");
        addButton.addActionListener(this);
        buttonPanel.add(addButton);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(cancelButton);
        if (currentDataSet != null) {
            dataItems = currentDataSet.getDataItems();
            for (PieChartItem dataItem : dataItems) {
                dataItemListModel.addElement(dataItem);
            }
            nameField.setText(currentDataSet.getDataTitle());
            addButton.setText("Update");
            this.setTitle("Edit Data Set");
        }
        setSize(300, 300);
        Point winLocation = new Point(OpenPlotTool.getMainFrame().getLocation().x + 20, OpenPlotTool.getMainFrame().getLocation().y + 20);
        setLocation(winLocation);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addItemButton) {
            addItem();
        } else if (e.getSource() == editItemButton) {
            editItem();
        } else if (e.getSource() == removeItemButton) {
            removeItem();
        } else if (e.getSource() == addButton) {
            pressed = ADD_PRESSED;
            setVisible(false);
        } else if (e.getSource() == cancelButton) {
            setVisible(false);
        }
    }

    private void addItem() {
        PieChartItemDialog dialog = new PieChartItemDialog(null);
        dialog.setVisible(true);
        if (dialog.getPressed() == PieChartItemDialog.ADD_PRESSED) {
            try {
                PieChartItem newItem = dialog.getPieChartItem();
                dataItems.add(newItem);
                dataItemListModel.addElement(newItem);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(OpenPlotTool.getMainFrame(), "Invalid Data Item.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        dialog.dispose();
    }

    private void editItem() {
        int index = dataItemList.getSelectedIndex();
        editItem(index);
    }

    private void editItem(int index) {
        PieChartItem pieItem = (PieChartItem) dataItemList.getSelectedValue();
        if (pieItem != null) {
            PieChartItemDialog dialog = new PieChartItemDialog(pieItem);
            dialog.setVisible(true);
            if (dialog.getPressed() == PieChartItemDialog.ADD_PRESSED) {
                PieChartItem newPieItem = dialog.getPieChartItem();
                dataItems.set(index, newPieItem);
                dataItemListModel.remove(index);
                dataItemListModel.add(index, newPieItem);
            }
            dialog.dispose();
        }
    }

    public PieChartDataSet getDataItems() throws NullDataValueException {
        if (dataItems.size() > 0) {
            PieChartDataSet newData = new PieChartDataSet();
            newData.setItems(dataItems);
            newData.setDataTitle(nameField.getText());
            return newData;
        } else {
            throw new NullDataValueException();
        }
    }

    public int getPressed() {
        return pressed;
    }

    private void removeItem() {
        int index = dataItemList.getSelectedIndex();
        if (index > -1) {
            dataItems.remove(dataItemList.getSelectedValue());
            dataItemListModel.remove(index);
        }
    }
}
