package mekhangar.design.gui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Iterator;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import mekhangar.design.Equipment;
import mekhangar.design.gui.models.EquipmentChoiceModel;
import mekhangar.rules.EquipmentData;

public class EquipmentChoicePanel extends JPanel implements ChangeListener {

    private EquipmentEditorPanel parent;

    private EquipmentChoiceModel model;

    private JScrollPane scrollPane;

    private ScrollableEquipmentListPanel listPanel;

    public EquipmentChoicePanel(EquipmentEditorPanel parent, EquipmentChoiceModel model) {
        this.parent = parent;
        this.model = model;
        model.addChangeListener(this);
        initListPanel();
        scrollPane = new JScrollPane(listPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane);
    }

    private void initListPanel() {
        listPanel = new ScrollableEquipmentListPanel();
        listPanel.initList();
    }

    public void stateChanged(ChangeEvent e) {
        listPanel.updateList();
        selectionChanged();
    }

    public void clearSelection() {
        listPanel.clearSelection();
    }

    private void selectionChanged() {
        if (parent != null) {
            parent.selectionChanged(this);
        }
    }

    public Equipment getSelectedItem() {
        return listPanel.getSelectedItem();
    }

    private class ScrollableEquipmentListPanel extends JPanel implements Scrollable, SelectionListener {

        private Dimension dim;

        private EquipmentItemPanel selected;

        private EquipmentItemPanel last;

        public ScrollableEquipmentListPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            EquipmentItemPanel panel = new EquipmentItemPanel();
            dim = panel.getPreferredSize();
            selected = null;
        }

        public void initList() {
            fillList();
        }

        public void updateList() {
            clearList();
            fillList();
        }

        private void clearList() {
            last = selected;
            clearSelection();
            removeAll();
        }

        private void fillList() {
            EquipmentData data = null;
            if (last != null) {
                data = last.getEquipment().getData();
            }
            for (Iterator it = model.getChoice().iterator(); it.hasNext(); ) {
                Equipment e = (Equipment) it.next();
                EquipmentItemPanel panel = new EquipmentItemPanel(e);
                panel.setSelectionListener(this);
                add(panel);
                if (data != null && data.getName().equals(e.getName()) && data.getTechBase() == e.getTechBase()) {
                    panel.setSelected(true);
                    data = null;
                }
            }
        }

        public Dimension getPreferredScrollableViewportSize() {
            return new Dimension(dim.width, dim.height * 25);
        }

        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return dim.height;
        }

        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return dim.height * 23;
        }

        public boolean getScrollableTracksViewportWidth() {
            return false;
        }

        public boolean getScrollableTracksViewportHeight() {
            return false;
        }

        public Equipment getSelectedItem() {
            if (selected != null) {
                return selected.getEquipment();
            }
            return null;
        }

        public void clearSelection() {
            if (selected != null) {
                selected.setSelected(false);
            }
            selected = null;
        }

        public void itemSelected(EquipmentItemPanel source) {
            if (selected != null && selected != source) {
                selected.setSelected(false);
            }
            selected = source;
            selectionChanged();
        }
    }
}
