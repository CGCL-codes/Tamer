package jpianotrain.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import jpianotrain.midi.MidiThread;
import jpianotrain.staff.Note;
import jpianotrain.staff.ScaleName;
import jpianotrain.util.ResourceFactory;
import jpianotrain.util.ResourceKeys;
import org.apache.log4j.Logger;

/**
 * A pane rendering different scales and their
 * notes into a table.
 *
 * @author Alexander Methke
 */
public class ScalePane extends JPanel implements ActionListener, ItemListener, MouseListener {

    private static final Logger log = Logger.getLogger(ScalePane.class);

    public ScalePane() {
        this(true);
    }

    public ScalePane(boolean showHelp) {
        createUI(showHelp);
    }

    protected void createUI(boolean showHelp) {
        popup = new JPopupMenu();
        playMidiItem = new JMenuItem("Play Midi");
        playMidiItem.addActionListener(this);
        popup.add(playMidiItem);
        setLayout(new BorderLayout());
        Vector<ScaleName> v = new Vector<ScaleName>(ScaleName.collection());
        scaleList = new JComboBox(v);
        scaleList.setEditable(false);
        scaleList.addItemListener(this);
        scaleList.setSelectedItem(ScaleName.MAJOR);
        add(scaleList, BorderLayout.NORTH);
        scaleTable = createTable(ScaleName.MAJOR);
        add(scaleTable, BorderLayout.CENTER);
    }

    private JTable createTable(ScaleName scaleName) {
        JTable scaleTable = new JTable();
        scaleTable.setDefaultRenderer(Note.class, new VLTableCellRenderer());
        scaleTable.setModel(new ScaleTableModel(scaleName, Note.MAJOR_NAMES));
        scaleTable.setRowSelectionAllowed(false);
        scaleTable.setColumnSelectionAllowed(true);
        scaleTable.setCellEditor(null);
        scaleTable.addMouseListener(this);
        scaleTable.getTableHeader().setReorderingAllowed(false);
        return scaleTable;
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() != ItemEvent.SELECTED) {
            return;
        }
        ScaleName scaleName = (ScaleName) scaleList.getSelectedItem();
        scaleTable.setModel(new ScaleTableModel(scaleName, Note.MAJOR_NAMES));
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3_MASK) {
            log.debug("Context Menu anzeigen");
            popup.show(scaleTable, e.getX(), e.getY());
            selectedColumn = scaleTable.columnAtPoint(e.getPoint());
            scaleTable.clearSelection();
            scaleTable.addColumnSelectionInterval(selectedColumn, selectedColumn);
        }
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == playMidiItem) {
            int rCount = scaleTable.getRowCount();
            Note[] n = new Note[rCount];
            int pit = 0;
            Note nt;
            for (int i = 0; i < rCount; i++) {
                Object o = scaleTable.getValueAt(i, selectedColumn);
                log.debug("Playing: " + o);
                nt = (Note) o;
                if (nt.getPitch() >= pit) {
                    pit = nt.getPitch();
                } else {
                    nt.transpose(12);
                }
                n[i] = nt;
            }
            MidiThread mt = MidiThread.getInstance();
            if (mt == null) {
                ResourceBundle bdl = ResourceBundle.getBundle("vl.vlc");
                JOptionPane.showMessageDialog(this, bdl.getString("msg.midi_not_ready"));
                return;
            }
            try {
                mt.play(n);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex, ResourceFactory.getString(ResourceKeys.TITLE_ERROR), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private int selectedColumn;

    private JPopupMenu popup;

    private JMenuItem playMidiItem;

    private JComboBox scaleList;

    private JTable scaleTable;
}
