package edu.rice.cs.cunit.record;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Window for recorder.
 *
 * @author Mathias Ricken
 */
public class RecordView implements IRecordView {

    /**
     * Main frame.
     */
    private JFrame _frame;

    /**
     * Information window.
     */
    RecordInfoView _infoView;

    /**
     * List view for the threads.
     */
    private JList _listView;

    /**
     * Text pane for the state trace.
     */
    private JTextPane _textPane;

    /**
     * Virtual machine adapter.
     */
    private IMonitorAdapter _monitorAdapter;

    /**
     * Initialization done?
     */
    private boolean _initDone = false;

    /**
     * Array with thread information.
     */
    private IThreadInfo[] _threadEntries;

    /**
     * Button to detect deadlocks.
     */
    private JButton _deadLockBu;

    /**
     * Creates a new window.
     * @param vma virtual machine adapter
     * @param exitOnClose true if application should exit on window close
     * @param showSyncButton true if the sync button should be shown
     */
    public RecordView(IMonitorAdapter vma, final boolean exitOnClose, boolean showSyncButton) {
        _monitorAdapter = vma;
        _frame = new JFrame("Record");
        _frame.addWindowListener(new WindowListener() {

            public void windowOpened(WindowEvent e) {
            }

            public void windowClosing(WindowEvent e) {
                _monitorAdapter.shutDown();
                _frame.setVisible(false);
                if (exitOnClose) {
                    System.exit(0);
                }
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }
        });
        _frame.setPreferredSize(new Dimension(700, 600));
        _listView = new JList();
        _listView.setBorder(LineBorder.createBlackLineBorder());
        _listView.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                IThreadInfo ti = (IThreadInfo) _listView.getSelectedValue();
                if (ti != null) {
                    _textPane.setText(ti.toStringVerbose());
                }
            }
        });
        _textPane = new JTextPane();
        _textPane.setBorder(LineBorder.createBlackLineBorder());
        _textPane.setContentType("text/html");
        _textPane.setEditable(false);
        _textPane.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    _monitorAdapter.openSource(e.getDescription());
                }
            }
        });
        JScrollPane lsp = new JScrollPane(_listView);
        JScrollPane tsp = new JScrollPane(_textPane);
        JSplitPane center = new JSplitPane(JSplitPane.VERTICAL_SPLIT, lsp, tsp);
        center.setDividerLocation(0.5);
        JPanel south = new JPanel();
        _deadLockBu = new JButton("Detect Deadlock");
        _deadLockBu.setEnabled(false);
        _deadLockBu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                detectDeadlock();
            }
        });
        south.add(_deadLockBu);
        JButton infoBu = new JButton("Info");
        infoBu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                _infoView.setVisible(true);
            }
        });
        south.add(infoBu);
        if (showSyncButton) {
            JButton syncBu = new JButton("Sync");
            syncBu.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    _monitorAdapter.processSynchronizationPoints("user");
                    updateList(_monitorAdapter.getThreadList());
                }
            });
            south.add(syncBu);
        }
        JTable historyTable = new JTable(_monitorAdapter.getTransferHistoryModel()) {

            public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
                Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    jc.setToolTipText(getValueAt(rowIndex, vColIndex).toString());
                }
                return c;
            }
        };
        historyTable.setPreferredScrollableViewportSize(new Dimension(300, 70));
        TableColumn column;
        for (int i = 0; i < historyTable.getColumnCount(); i++) {
            column = historyTable.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(30);
            } else if (i != 3) {
                column.setPreferredWidth(50);
            } else {
                column.setPreferredWidth(70);
            }
        }
        JScrollPane historyScrollPane = new JScrollPane(historyTable);
        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(new BorderLayout());
        historyPanel.add(new JLabel("Transfer History"), BorderLayout.NORTH);
        historyPanel.add(historyScrollPane, BorderLayout.CENTER);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(center, BorderLayout.CENTER);
        topPanel.add(south, BorderLayout.SOUTH);
        _frame.getContentPane().setLayout(new BorderLayout());
        _frame.getContentPane().add(topPanel, BorderLayout.CENTER);
        _frame.getContentPane().add(historyPanel, BorderLayout.EAST);
        _frame.pack();
        _frame.setVisible(true);
        _infoView = new RecordInfoView(vma);
        _initDone = true;
    }

    /**
     * Sets the project name in the window title.
     * @param project project name
     */
    public void setProjectInWindowTitle(final String project) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (project.equals("")) {
                    _frame.setTitle("Record");
                } else {
                    _frame.setTitle("Record - " + project);
                }
            }
        });
    }

    /**
     * Selects the thread with the specified unique id.
     * @param id unique id
     */
    public void selectThreadWithId(long id) {
        for (final IThreadInfo ti : _threadEntries) {
            if (ti.getThreadID() == id) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        _listView.setSelectedValue(ti, true);
                        _textPane.setText(ti.toStringVerbose());
                    }
                });
                break;
            }
        }
    }

    /**
     * Obtains an updated list of threads from the VM.
     * @param threads threads to display
     */
    public void updateList(final IThreadInfo[] threads) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (!_initDone) {
                    return;
                }
                _deadLockBu.setEnabled(true);
                _threadEntries = threads;
                int sel = _listView.getSelectedIndex();
                _listView.setListData(_threadEntries);
                _listView.setSelectedIndex(sel);
            }
        });
    }

    /**
     * Detects deadlock.
     */
    public void detectDeadlock() {
        DeadlockFrame dlFrame = new DeadlockFrame(this, _monitorAdapter.getCycles());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Point p = new Point(_frame.getLocation().x, _frame.getLocation().y + (int) _frame.getSize().getHeight());
        int fromBottom = (int) (screenSize.getHeight() - _frame.getSize().getHeight());
        if (fromBottom < p.y) {
            p.setLocation(p.x, fromBottom);
        }
        dlFrame.setLocation(p);
        dlFrame.setVisible(true);
    }
}
