package de.bugbusters.cdoptimizer.gui;

import de.bugbusters.binpacking.model.Bin;
import de.bugbusters.binpacking.model.BinPackingResult;
import de.bugbusters.binpacking.model.Element;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Sven Kiesewetter
 */
public class CDContentPanel extends JPanel implements ActionListener, PropertyChangeListener {

    private static final String COMBOBOX_ENTRY_OVERSIZED_ELEMENTS = "Oversized Elements";

    private FileListModel fileListModel = new FileListModel();

    private BinPackingResultComboBoxModel comboBoxModel = new BinPackingResultComboBoxModel();

    private JList fileList;

    private TitledBorder border;

    private JComboBox comboBox;

    private BinPackingResult binPackingResult;

    public CDContentPanel() {
        border = BorderFactory.createTitledBorder("Content of");
        setBorder(border);
        setLayout(new BorderLayout());
        comboBox = new JComboBox(comboBoxModel);
        comboBox.setRenderer(new BinRenderer());
        comboBox.addActionListener(this);
        add(comboBox, BorderLayout.NORTH);
        fileList = new JList(fileListModel);
        fileList.setCellRenderer(new FileListCellRenderer());
        fileList.setVisibleRowCount(5);
        fileList.setBorder(BorderFactory.createLoweredBevelBorder());
        add(new JScrollPane(fileList), BorderLayout.CENTER);
    }

    public void updateFillList(Set elements) {
        fileListModel.clear();
        List elems = new ArrayList(elements);
        Collections.sort(elems, new ElementIdComparator());
        Iterator it = elems.iterator();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            fileListModel.addFile(((de.bugbusters.cdoptimizer.binpacking.File) element).getFile());
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (Mainframe.PROPERTY_BIN_PACKING_RESULT.equals(evt.getPropertyName())) {
            binPackingResult = (BinPackingResult) evt.getNewValue();
            comboBoxModel.setResult(binPackingResult);
            comboBox.setSelectedIndex(0);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (binPackingResult != null && e.getSource() == comboBox) {
            Object selectedItem = comboBox.getSelectedItem();
            if (selectedItem instanceof Bin) {
                updateFillList(((Bin) selectedItem).getElements());
            } else {
                updateFillList(binPackingResult.getOverSizedElements());
            }
        }
    }

    private static class BinPackingResultComboBoxModel extends DefaultComboBoxModel {

        private BinPackingResult result;

        private List bins;

        public synchronized BinPackingResult getResult() {
            return result;
        }

        public synchronized void setResult(BinPackingResult result) {
            this.result = result;
            bins = new ArrayList(result.getBins());
            Collections.sort(bins, new BinIdComparator());
            fireContentsChanged(this, 0, bins.size() + 1);
        }

        public int getSize() {
            if (bins == null) return 0;
            return bins.size() + 1;
        }

        public Object getElementAt(int index) {
            if (index > bins.size() - 1) {
                return COMBOBOX_ENTRY_OVERSIZED_ELEMENTS;
            } else {
                return bins.get(index);
            }
        }
    }

    private static class BinRenderer extends JLabel implements ListCellRenderer {

        private static final Border hlBorder = UIManager.getBorder("List.focusCellHighlightBorder");

        private static final Border normalBorder = UIManager.getBorder("List.focusCellBorder");

        private static final Color selBgColor = UIManager.getColor("List.selectionBackground");

        private static final Color selFgColor = UIManager.getColor("List.selectionForeground");

        private static final Color bgColor = UIManager.getColor("List.background");

        private static final Color fgColor = UIManager.getColor("List.foreground");

        private static final Font font = UIManager.getFont("List.font");

        public BinRenderer() {
            setFont(font);
            setHorizontalAlignment(SwingConstants.LEFT);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Bin) {
                setText("CD " + ((Bin) value).getId());
            } else if (value != null) {
                setText(String.valueOf(value));
            } else {
                setText("<noting selected>");
            }
            if (isSelected) {
                super.setForeground(selFgColor);
                super.setBackground(selBgColor);
            } else {
                super.setForeground(fgColor);
                super.setBackground(bgColor);
            }
            if (cellHasFocus) {
                super.setBorder(hlBorder);
            } else {
                super.setBorder(normalBorder);
            }
            return this;
        }
    }
}
