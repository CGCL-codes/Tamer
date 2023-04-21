package corina.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import corina.core.App;
import corina.logging.CorinaLog;
import corina.util.TextClipboard;

public class LogViewer extends JPanel {

    public LogViewer() {
        super(new BorderLayout());
        JPanel caption = new JPanel();
        caption.setLayout(new BoxLayout(caption, BoxLayout.X_AXIS));
        caption.setBorder(new EmptyBorder(6, 6, 6, 6));
        caption.add(Box.createHorizontalGlue());
        tailIsOn = Boolean.valueOf(App.prefs.getPref("log-viewer.tail")).booleanValue();
        tail = new JCheckBox("Tail", tailIsOn);
        tail.addActionListener(new ActionHandler());
        caption.add(tail);
        caption.add(Box.createHorizontalStrut(12));
        copy = new JButton("Copy");
        copy.addActionListener(new ActionHandler());
        caption.add(copy);
        ListModel model = CorinaLog.getLogListModel();
        model.addListDataListener(new ListHandler());
        list = new LogList(model);
        list.setFixedCellHeight(list.getFontMetrics(list.getFont()).getHeight());
        add(BorderLayout.NORTH, caption);
        JScrollPane scroller = new JScrollPane(list);
        Dimension dim = scroller.getPreferredSize();
        dim.width = Math.min(600, dim.width);
        scroller.setPreferredSize(dim);
        add(BorderLayout.CENTER, scroller);
    }

    public void addNotify() {
        super.addNotify();
        if (tailIsOn) {
            int index = list.getModel().getSize() - 1;
            list.ensureIndexIsVisible(index);
        }
    }

    public void focusOnDefaultComponent() {
        list.requestFocus();
    }

    private JList list;

    private JButton copy;

    private JCheckBox tail;

    private boolean tailIsOn;

    class ActionHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Object src = e.getSource();
            if (src == tail) {
                tailIsOn = !tailIsOn;
                if (tailIsOn) {
                    int index = list.getModel().getSize();
                    if (index != 0) {
                        list.ensureIndexIsVisible(index - 1);
                    }
                }
            } else if (src == copy) {
                StringBuffer buf = new StringBuffer();
                Object[] selected = list.getSelectedValues();
                if (selected != null && selected.length != 0) {
                    for (int i = 0; i < selected.length; i++) {
                        buf.append(selected[i]);
                        buf.append('\n');
                    }
                } else {
                    ListModel model = list.getModel();
                    for (int i = 0; i < model.getSize(); i++) {
                        buf.append(model.getElementAt(i));
                        buf.append('\n');
                    }
                }
                TextClipboard.copy(buf.toString());
            }
        }
    }

    class ListHandler implements ListDataListener {

        public void intervalAdded(ListDataEvent e) {
            contentsChanged(e);
        }

        public void intervalRemoved(ListDataEvent e) {
            contentsChanged(e);
        }

        public void contentsChanged(ListDataEvent e) {
            if (tailIsOn) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        int index = list.getModel().getSize() - 1;
                        list.ensureIndexIsVisible(index);
                    }
                });
            }
        }
    }

    class LogList extends JList {

        LogList(ListModel model) {
            super(model);
            setVisibleRowCount(24);
            getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            setAutoscrolls(true);
        }

        public void processMouseEvent(MouseEvent evt) {
            if (evt.getID() == MouseEvent.MOUSE_PRESSED) {
                startIndex = list.locationToIndex(evt.getPoint());
            }
            super.processMouseEvent(evt);
        }

        public void processMouseMotionEvent(MouseEvent evt) {
            if (evt.getID() == MouseEvent.MOUSE_DRAGGED) {
                int row = list.locationToIndex(evt.getPoint());
                if (row != -1) {
                    if (startIndex == -1) {
                        list.setSelectionInterval(row, row);
                        startIndex = row;
                    } else list.setSelectionInterval(startIndex, row);
                    list.ensureIndexIsVisible(row);
                    evt.consume();
                }
            } else super.processMouseMotionEvent(evt);
        }

        private int startIndex;
    }
}
