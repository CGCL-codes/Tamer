package net.sourceforge.jhelpdev;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import net.sourceforge.jhelpdev.action.CreateMapAction;

/**
 * GUI component that holds the TOC editor.
 * @author <a href="mailto:mk@mk-home.de">Markus Kraetzig</a>
 */
public final class TOCEditorPanel extends JPanel {

    /**
	 * The JHTree for TOC.
	 */
    private static final JHTree tocTree = new JHTree();

    private JScrollPane ivjJScrollPane1 = null;

    private JPanel ivjJPanel1 = null;

    private JButton ivjAddButton = null;

    private JButton ivjDeleteButton = null;

    IvjEventHandler ivjEventHandler = new IvjEventHandler();

    private JButton ivjEditButton = null;

    private JButton ivjMapGenButton = null;

    class IvjEventHandler implements java.awt.event.ActionListener {

        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == TOCEditorPanel.this.getAddButton()) connEtoC1();
            if (e.getSource() == TOCEditorPanel.this.getDeleteButton()) connEtoC2();
            if (e.getSource() == TOCEditorPanel.this.getEditButton()) connEtoC4();
            if (e.getSource() == TOCEditorPanel.this.getMapGenButton()) connEtoC6();
        }

        ;
    }

    ;

    /**
	 * TOCEditorPanel constructor comment.
	 */
    public TOCEditorPanel() {
        super();
        initialize();
    }

    /**
	 * TOCEditorPanel constructor comment.
	 * @param layout java.awt.LayoutManager
	 */
    public TOCEditorPanel(java.awt.LayoutManager layout) {
        super(layout);
    }

    /**
	 * TOCEditorPanel constructor comment.
	 * @param layout java.awt.LayoutManager
	 * @param isDoubleBuffered boolean
	 */
    public TOCEditorPanel(java.awt.LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }

    /**
	 * TOCEditorPanel constructor comment.
	 * @param isDoubleBuffered boolean
	 */
    public TOCEditorPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    /**
	 * Comment
	 */
    private void addButton_ActionEvents() {
        getTOCTree().addNewItem();
        return;
    }

    private void connEtoC1() {
        try {
            this.addButton_ActionEvents();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    private void connEtoC2() {
        try {
            this.deleteButton_ActionEvents();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    private void connEtoC4() {
        try {
            this.editButton_ActionEvents();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    private void connEtoC6() {
        try {
            this.mapGenButton_ActionEvents();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
	 * Comment
	 */
    private void deleteButton_ActionEvents() {
        getTOCTree().deleteSelectedItems();
    }

    /**
	 * Comment
	 */
    private void editButton_ActionEvents() {
        getTOCTree().editSelectedItem();
    }

    private javax.swing.JButton getAddButton() {
        if (ivjAddButton == null) {
            try {
                ivjAddButton = new javax.swing.JButton();
                ivjAddButton.setName("AddButton");
                ivjAddButton.setToolTipText("Add new TOC item.");
                ivjAddButton.setText("Add");
                ivjAddButton.setBounds(5, 10, 110, 25);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjAddButton;
    }

    private javax.swing.JButton getDeleteButton() {
        if (ivjDeleteButton == null) {
            try {
                ivjDeleteButton = new javax.swing.JButton();
                ivjDeleteButton.setName("DeleteButton");
                ivjDeleteButton.setToolTipText("Delete selected items.");
                ivjDeleteButton.setText("Delete");
                ivjDeleteButton.setBounds(5, 50, 110, 25);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjDeleteButton;
    }

    private javax.swing.JButton getEditButton() {
        if (ivjEditButton == null) {
            try {
                ivjEditButton = new javax.swing.JButton();
                ivjEditButton.setName("EditButton");
                ivjEditButton.setToolTipText("Edit first selected TOC item.");
                ivjEditButton.setText("Edit");
                ivjEditButton.setBounds(5, 90, 110, 25);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjEditButton;
    }

    private javax.swing.JPanel getJPanel1() {
        if (ivjJPanel1 == null) {
            try {
                ivjJPanel1 = new javax.swing.JPanel();
                ivjJPanel1.setName("JPanel1");
                ivjJPanel1.setPreferredSize(new java.awt.Dimension(120, 0));
                ivjJPanel1.setLayout(null);
                getJPanel1().add(getAddButton(), getAddButton().getName());
                getJPanel1().add(getDeleteButton(), getDeleteButton().getName());
                getJPanel1().add(getEditButton(), getEditButton().getName());
                getJPanel1().add(getMapGenButton(), getMapGenButton().getName());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJPanel1;
    }

    private javax.swing.JScrollPane getJScrollPane1() {
        if (ivjJScrollPane1 == null) {
            try {
                ivjJScrollPane1 = new javax.swing.JScrollPane();
                ivjJScrollPane1.setName("JScrollPane1");
                ivjJScrollPane1.setViewportView(getTOCTree());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJScrollPane1;
    }

    private javax.swing.JButton getMapGenButton() {
        if (ivjMapGenButton == null) {
            try {
                ivjMapGenButton = new javax.swing.JButton();
                ivjMapGenButton.setName("MapGenButton");
                ivjMapGenButton.setToolTipText("Automatically update TOC with directories and files\nthat are not already in it.");
                ivjMapGenButton.setText("Generate TOC");
                ivjMapGenButton.setBounds(5, 130, 110, 25);
                ivjMapGenButton.setMargin(new java.awt.Insets(2, 10, 2, 10));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjMapGenButton;
    }

    /**
	 * returns a reference to the JHTree used for TOC.
	 * 
	 * @return de.mk_home.jhelpdev.JHTree
	 */
    public static final JHTree getTOCTree() {
        return tocTree;
    }

    /**
	 * Called whenever the part throws an exception.
	 * @param exception java.lang.Throwable
	 */
    private void handleException(java.lang.Throwable exception) {
        System.out.println("--------- UNCAUGHT EXCEPTION ---------");
        exception.printStackTrace(System.out);
    }

    private void initConnections() throws java.lang.Exception {
        getAddButton().addActionListener(ivjEventHandler);
        getDeleteButton().addActionListener(ivjEventHandler);
        getEditButton().addActionListener(ivjEventHandler);
        getMapGenButton().addActionListener(ivjEventHandler);
    }

    private void initialize() {
        try {
            setName("TOCEditorPanel");
            setBorder(new BevelBorder(BevelBorder.LOWERED));
            setLayout(new java.awt.BorderLayout());
            setSize(489, 360);
            add(getJScrollPane1(), "Center");
            add(getJPanel1(), "West");
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
	 * main entrypoint - starts the part when it is run as an application
	 * @param args java.lang.String[]
	 */
    public static void main(java.lang.String[] args) {
        try {
            JFrame frame = new javax.swing.JFrame();
            TOCEditorPanel aTOCEditorPanel;
            aTOCEditorPanel = new TOCEditorPanel();
            frame.setContentPane(aTOCEditorPanel);
            frame.setSize(aTOCEditorPanel.getSize());
            frame.addWindowListener(new java.awt.event.WindowAdapter() {

                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }

                ;
            });
            frame.setVisible(true);
            java.awt.Insets insets = frame.getInsets();
            frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);
            frame.setVisible(true);
        } catch (Throwable exception) {
            System.err.println("Exception occurred in main() of javax.swing.JPanel");
            exception.printStackTrace(System.out);
        }
    }

    /**
	 * Updates the Table of Contents with new Map entries without wiping the TOC
	 */
    private void mapGenButton_ActionEvents() {
        int ret = JOptionPane.showConfirmDialog(JHelpDevFrame.getAJHelpDevToolFrame(), "Really generate TOC?\n(Undo is possible with import TOC from file.)", "Question", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (ret != JOptionPane.YES_OPTION) return;
        getTOCTree().mergeTreeContents(CreateMapAction.getGeneratedRoot());
    }
}
