package frost.messaging.frost.boards;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import frost.*;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
@SuppressWarnings("serial")
public class BoardUpdateInformationFrame extends javax.swing.JFrame implements BoardUpdateThreadListener, TreeSelectionListener {

    private JComboBox cbBoards;

    private JLabel lBoards;

    private JTextArea taContent;

    private JLabel lDates;

    private JComboBox cbDates;

    private static boolean isShowing = false;

    private final TofTree tofTree;

    private JCheckBox cbSyncWithBoardTree;

    private JTextArea taSummary;

    private JPanel buttonPanel;

    private JButton Bclose;

    private JTabbedPane tabbedPane;

    private final TofTreeModel tofTreeModel;

    public BoardUpdateInformationFrame(final JFrame parentFrame, final TofTree tofTree) {
        super();
        this.tofTree = tofTree;
        this.tofTreeModel = (TofTreeModel) tofTree.getModel();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        initGUI();
        setTitle("Board update informations");
        setLocationRelativeTo(parentFrame);
    }

    private void initGUI() {
        try {
            getContentPane().setLayout(new BorderLayout());
            final GridBagLayout boardUpdateInfoPanelLayout = new GridBagLayout();
            final JPanel boardUpdateInfoPanel = new JPanel(boardUpdateInfoPanelLayout);
            {
                final ComboBoxModel cbBoardsModel = new DefaultComboBoxModel();
                cbBoards = new JComboBox();
                boardUpdateInfoPanel.add(cbBoards, new GridBagConstraints(1, 0, 1, 1, 0.4, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
                cbBoards.setModel(cbBoardsModel);
                cbBoards.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent evt) {
                        cbBoardsActionPerformed(evt);
                    }
                });
            }
            {
                final ComboBoxModel cbDatesModel = new DefaultComboBoxModel();
                cbDates = new JComboBox();
                boardUpdateInfoPanel.add(cbDates, new GridBagConstraints(3, 0, 1, 1, 0.4, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
                cbDates.setModel(cbDatesModel);
                cbDates.addActionListener(new ActionListener() {

                    public void actionPerformed(final ActionEvent evt) {
                        cbDatesActionPerformed(evt);
                    }
                });
            }
            {
                lBoards = new JLabel();
                boardUpdateInfoPanel.add(lBoards, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
                lBoards.setText("Board");
            }
            {
                lDates = new JLabel();
                boardUpdateInfoPanel.add(lDates, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
                lDates.setText("Date");
            }
            {
                taContent = new JTextArea();
                boardUpdateInfoPanel.add(taContent, new GridBagConstraints(0, 1, 4, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
                taContent.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
                taContent.setEditable(false);
            }
            final GridBagLayout summaryPanelLayout = new GridBagLayout();
            final JPanel summaryPanel = new JPanel(summaryPanelLayout);
            final GridBagLayout summaryPanelLayout1 = new GridBagLayout();
            summaryPanel.setLayout(summaryPanelLayout1);
            {
                tabbedPane = new JTabbedPane();
                tabbedPane.addTab("By board", boardUpdateInfoPanel);
                tabbedPane.addTab("Summary", summaryPanel);
                {
                    taSummary = new JTextArea();
                    summaryPanel.add(taSummary, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
                    taSummary.setText(" ");
                    taSummary.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, null, null, null, null), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                    taSummary.setEditable(false);
                }
                tabbedPane.addChangeListener(new ChangeListener() {

                    public void stateChanged(final ChangeEvent e) {
                        maybeUpdateSummaryTextArea();
                    }
                });
                getContentPane().add(tabbedPane, BorderLayout.CENTER);
            }
            {
                buttonPanel = new JPanel();
                final BorderLayout buttonPanelLayout = new BorderLayout();
                getContentPane().add(buttonPanel, BorderLayout.SOUTH);
                buttonPanel.setLayout(buttonPanelLayout);
                buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                {
                    Bclose = new JButton();
                    buttonPanel.add(Bclose, BorderLayout.EAST);
                    Bclose.setText("Close");
                    Bclose.addActionListener(new ActionListener() {

                        public void actionPerformed(final ActionEvent evt) {
                            BcloseActionPerformed(evt);
                        }
                    });
                }
                {
                    cbSyncWithBoardTree = new JCheckBox();
                    buttonPanel.add(cbSyncWithBoardTree, BorderLayout.WEST);
                    cbSyncWithBoardTree.setText("Sync with board tree");
                    cbSyncWithBoardTree.addItemListener(new ItemListener() {

                        public void itemStateChanged(final ItemEvent e) {
                            maybeSyncBoards();
                        }
                    });
                }
            }
            pack();
            setSize(400, 470);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void clearTaContent() {
        taContent.setText("No informations available");
    }

    private void cbBoardsActionPerformed(final ActionEvent evt) {
        final JComboBox cb = (JComboBox) evt.getSource();
        final Board selectedBoard = (Board) cb.getSelectedItem();
        if (selectedBoard == null) {
            clearTaContent();
            return;
        }
        cbDates.removeAllItems();
        final List<BoardUpdateInformation> l = selectedBoard.getBoardUpdateInformationList();
        for (final BoardUpdateInformation bui : l) {
            cbDates.addItem(bui);
        }
        if (cbDates.getModel().getSize() > 0) {
            cbDates.setSelectedIndex(0);
        } else {
            clearTaContent();
        }
    }

    private void cbDatesActionPerformed(final ActionEvent evt) {
        final JComboBox cb = (JComboBox) evt.getSource();
        final BoardUpdateInformation selectedItem = (BoardUpdateInformation) cb.getSelectedItem();
        if (selectedItem == null) {
            clearTaContent();
            return;
        }
        taContent.setText(selectedItem.getInfoString());
    }

    /**
     * Reload dialog after restart.
     */
    private void loadGuiData() {
        final Vector<Board> items = new Vector<Board>();
        for (final Board b : tofTreeModel.getAllBoards()) {
            if (b.hasBoardUpdateInformations()) {
                items.add(b);
            }
        }
        final ComboBoxModel cbBoardsModel = new DefaultComboBoxModel(items);
        cbBoards.setModel(cbBoardsModel);
        if (cbBoards.getItemCount() > 0) {
            cbBoards.setSelectedIndex(0);
        }
    }

    public void startDialog() {
        tofTree.getRunningBoardUpdateThreads().addBoardUpdateThreadListener(this);
        MainFrame.getInstance().getFrostMessageTab().getTofTree().addTreeSelectionListener(this);
        setDialogShowing(true);
        loadGuiData();
        setVisible(true);
    }

    private void closeDialog() {
        MainFrame.getInstance().getFrostMessageTab().getTofTree().removeTreeSelectionListener(this);
        tofTree.getRunningBoardUpdateThreads().removeBoardUpdateThreadListener(this);
        setDialogShowing(false);
        dispose();
    }

    private void BcloseActionPerformed(final ActionEvent evt) {
        closeDialog();
    }

    @Override
    protected void processWindowEvent(final WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            closeDialog();
        }
        super.processWindowEvent(e);
    }

    public static boolean isDialogShowing() {
        return isShowing;
    }

    public static void setDialogShowing(final boolean val) {
        isShowing = val;
    }

    /**
     * Is called if a Thread is finished.
     */
    public void boardUpdateThreadFinished(final BoardUpdateThread thread) {
    }

    /**
    * Is called if a Thread is started.
    *
    * @see frost.messaging.frost.boards.BoardUpdateThreadListener#boardUpdateThreadStarted(frost.messaging.frost.boards.BoardUpdateThread)
    */
    public void boardUpdateThreadStarted(final BoardUpdateThread thread) {
    }

    public void boardUpdateInformationChanged(final BoardUpdateThread thread, final BoardUpdateInformation bui) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                updateGui(thread, bui);
            }
        });
    }

    private void updateGui(final BoardUpdateThread thread, final BoardUpdateInformation bui) {
        maybeUpdateSummaryTextArea();
        final Board selectedBoard = (Board) cbBoards.getSelectedItem();
        {
            boolean contained = false;
            for (int x = 0; x < cbBoards.getItemCount(); x++) {
                if (cbBoards.getItemAt(x) == thread.getTargetBoard()) {
                    contained = true;
                    break;
                }
            }
            if (!contained) {
                cbBoards.addItem(thread.getTargetBoard());
                return;
            }
        }
        if (selectedBoard == null) {
            return;
        }
        if (selectedBoard != thread.getTargetBoard()) {
            return;
        }
        {
            boolean contained = false;
            for (int x = 0; x < cbDates.getItemCount(); x++) {
                if (cbDates.getItemAt(x) == bui) {
                    contained = true;
                    break;
                }
            }
            if (!contained) {
                cbDates.addItem(bui);
                return;
            }
        }
        final BoardUpdateInformation selectedBui = (BoardUpdateInformation) cbDates.getSelectedItem();
        if (selectedBui == null) {
            return;
        }
        if (selectedBui != bui) {
            return;
        }
        taContent.setText(bui.getInfoString());
    }

    private void maybeUpdateSummaryTextArea() {
        if (tabbedPane.getSelectedIndex() == 1) {
            taSummary.setText(BoardUpdateInformation.getSummaryInfoString(tofTreeModel.getAllBoards()));
        }
    }

    /**
    * Implement TreeSelectionListener.
    */
    public void valueChanged(final TreeSelectionEvent e) {
        maybeSyncBoards();
    }

    /**
    * Maybe change board selection if board tree changes.
    */
    private void maybeSyncBoards() {
        if (cbSyncWithBoardTree.isSelected()) {
            final Board selectedBoard = tofTree.getSelectedBoard();
            if (selectedBoard == null) {
                return;
            }
            if (selectedBoard == cbBoards.getSelectedItem()) {
                return;
            }
            cbBoards.setSelectedItem(selectedBoard);
        }
    }
}
