package blue.ui.core.score.soundLayer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import skt.swing.SwingUtil;
import blue.SoundLayer;
import blue.noteProcessor.NoteProcessorChainMap;
import blue.soundObject.PolyObject;

public class LayersPanel extends JComponent implements PropertyChangeListener, ListDataListener {

    private SoundLayerLayout layout = new SoundLayerLayout();

    private PolyObject pObj = null;

    private NoteProcessorChainMap npcMap = null;

    private SelectionModel selection = new SelectionModel();

    public LayersPanel() {
        this.setLayout(layout);
        this.setMinimumSize(new Dimension(0, 0));
        this.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent me) {
                LayersPanel.this.requestFocus();
                if (me.isPopupTrigger()) {
                } else if (SwingUtilities.isLeftMouseButton(me)) {
                    if (me.getClickCount() == 1) {
                        Component c = getComponentAt(me.getPoint());
                        int index = getIndexOfComponent(c);
                        if (index < 0) {
                            return;
                        }
                        if (me.isShiftDown()) {
                            selection.setEnd(index);
                        } else {
                            selection.setAnchor(index);
                        }
                    } else if (me.getClickCount() == 2) {
                        Component c = SwingUtilities.getDeepestComponentAt(LayersPanel.this, me.getX(), me.getY());
                        if (c != null && c instanceof JLabel) {
                            Component panel = getComponentAt(me.getPoint());
                            ((SoundLayerPanel) panel).editName();
                        }
                    }
                }
            }
        });
        selection.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                updateSelection();
            }
        });
        this.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent e) {
            }

            public void focusLost(FocusEvent e) {
                if (!e.isTemporary()) {
                    selection.setAnchor(-1);
                }
            }
        });
        initActions();
    }

    private void initActions() {
        SwingUtil.installActions(this, new Action[] { new ShiftUpAction(), new UpAction(), new ShiftDownAction(), new DownAction() }, WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private int getIndexOfComponent(Component c) {
        Component[] comps = getComponents();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] == c) {
                return i;
            }
        }
        return -1;
    }

    public void setPolyObject(PolyObject pObj) {
        if (this.pObj != null) {
            this.pObj.removePropertyChangeListener(this);
            this.pObj.removeListDataListener(this);
        }
        layout.setPolyObject(pObj);
        this.pObj = pObj;
        if (pObj != null) {
            this.pObj.addPropertyChangeListener(this);
            this.pObj.addListDataListener(this);
        }
        this.populate();
    }

    private void populate() {
        this.checkSize();
        for (Component c : getComponents()) {
            ((SoundLayerPanel) c).cleanup();
        }
        this.removeAll();
        if (pObj == null) {
            return;
        }
        for (int i = 0; i < pObj.getSize(); i++) {
            SoundLayer sLayer = (SoundLayer) pObj.getElementAt(i);
            SoundLayerPanel panel = new SoundLayerPanel();
            panel.setNoteProcessorChainMap(npcMap);
            panel.setAutomatable(pObj.isRoot());
            panel.setSoundLayer(sLayer);
            this.add(panel);
        }
        revalidate();
    }

    public void checkSize() {
        if (pObj == null || getParent() == null) {
            setSize(0, 0);
            return;
        }
        int w = getParent().getWidth();
        int h = pObj.getTotalHeight();
        this.setSize(w, h * pObj.getSize());
    }

    private void updateSelection() {
        int start = selection.getStartIndex();
        int end = selection.getEndIndex();
        Component[] comps = getComponents();
        for (int i = 0; i < comps.length; i++) {
            SoundLayerPanel panel = (SoundLayerPanel) comps[i];
            panel.setSelected(i >= start && i <= end);
        }
    }

    public SelectionModel getSelectionModel() {
        return selection;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == this.pObj) {
            if (evt.getPropertyName().equals("heightIndex")) {
                revalidate();
            }
        }
    }

    public void setNoteProcessorChainMap(NoteProcessorChainMap npcMap) {
        this.npcMap = npcMap;
    }

    public void intervalAdded(ListDataEvent e) {
        int index = e.getIndex0();
        SoundLayer sLayer = (SoundLayer) pObj.getElementAt(index);
        SoundLayerPanel panel = new SoundLayerPanel();
        panel.setNoteProcessorChainMap(npcMap);
        panel.setAutomatable(pObj.isRoot());
        panel.setSoundLayer(sLayer);
        this.add(panel, index);
        checkSize();
    }

    public void intervalRemoved(ListDataEvent e) {
        int start = e.getIndex0();
        int end = e.getIndex1();
        for (int i = end; i >= start; i--) {
            ((SoundLayerPanel) getComponent(i)).cleanup();
            remove(i);
        }
        checkSize();
        selection.setAnchor(-1);
    }

    public void contentsChanged(ListDataEvent e) {
        int start = e.getIndex0();
        int end = e.getIndex1();
        boolean isUp = ((start >= 0) && (end >= 0));
        if (isUp) {
            Component c = getComponent(start);
            SoundLayerPanel panel = (SoundLayerPanel) c;
            remove(start);
            add(c, end);
            int i1 = selection.getStartIndex() - 1;
            int i2 = selection.getEndIndex() - 1;
            selection.setAnchor(i1);
            selection.setEnd(i2);
        } else {
            Component c = getComponent(-start);
            SoundLayerPanel panel = (SoundLayerPanel) c;
            remove(-start);
            add(c, -end);
            int i1 = selection.getStartIndex() + 1;
            int i2 = selection.getEndIndex() + 1;
            selection.setAnchor(i1);
            selection.setEnd(i2);
        }
        revalidate();
    }

    class UpAction extends AbstractAction {

        public UpAction() {
            super("up");
            putValue(Action.SHORT_DESCRIPTION, "Up Action");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0));
        }

        public void actionPerformed(ActionEvent e) {
            int index = selection.getLastIndexSet() - 1;
            index = index < 0 ? 0 : index;
            if (index != selection.getStartIndex()) {
                selection.setAnchor(index);
            }
        }
    }

    class ShiftUpAction extends AbstractAction {

        public ShiftUpAction() {
            super("shift-up");
            putValue(Action.SHORT_DESCRIPTION, "Shift-Up Action");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.SHIFT_DOWN_MASK));
        }

        public void actionPerformed(ActionEvent e) {
            int index = selection.getLastIndexSet() - 1;
            index = index < 0 ? 0 : index;
            selection.setEnd(index);
        }
    }

    class DownAction extends AbstractAction {

        public DownAction() {
            super("down");
            putValue(Action.SHORT_DESCRIPTION, "Down Action");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0));
        }

        public void actionPerformed(ActionEvent e) {
            int index = selection.getLastIndexSet() + 1;
            int length = getComponents().length;
            index = index >= length ? length - 1 : index;
            if (index != selection.getEndIndex()) {
                selection.setAnchor(index);
            }
        }
    }

    class ShiftDownAction extends AbstractAction {

        public ShiftDownAction() {
            super("shift-Down");
            putValue(Action.SHORT_DESCRIPTION, "Shift-Down Action");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.SHIFT_DOWN_MASK));
        }

        public void actionPerformed(ActionEvent e) {
            int index = selection.getLastIndexSet() + 1;
            int length = getComponents().length;
            index = index >= length ? length - 1 : index;
            selection.setEnd(index);
        }
    }
}
