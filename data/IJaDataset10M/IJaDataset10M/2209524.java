package net.sf.portecle.gui.error;

import static net.sf.portecle.FPortecle.RB;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.ToolTipManager;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import net.sf.portecle.PortecleJDialog;

/**
 * Modal dialog to display a throwable's stack trace. Cause throwable's stack trace will be show recursively
 * also.
 */
public class DThrowableDetail extends PortecleJDialog {

    /** Stores throwable to display */
    private Throwable m_throwable;

    /**
	 * Creates new DThrowableDetail dialog.
	 * 
	 * @param parent Parent window
	 * @param throwable Throwable to display
	 */
    public DThrowableDetail(Window parent, Throwable throwable) {
        super(parent, true);
        m_throwable = throwable;
        initComponents();
    }

    /**
	 * Initialize the dialog's GUI components.
	 */
    private void initComponents() {
        JPanel jpButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton jbOK = getOkButton(true);
        jpButtons.add(jbOK);
        JButton jbCopy = new JButton(RB.getString("DThrowableDetail.jbCopy.text"));
        jbCopy.setMnemonic(RB.getString("DThrowableDetail.jbCopy.mnemonic").charAt(0));
        jbCopy.setToolTipText(RB.getString("DThrowableDetail.jbCopy.tooltip"));
        jbCopy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                copyPressed();
            }
        });
        jpButtons.add(jbCopy);
        JPanel jpThrowable = new JPanel(new BorderLayout());
        jpThrowable.setBorder(new EmptyBorder(5, 5, 5, 5));
        JTree jtrThrowable = new JTree(createThrowableNodes());
        jtrThrowable.setRowHeight(18);
        jtrThrowable.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        ToolTipManager.sharedInstance().registerComponent(jtrThrowable);
        jtrThrowable.setCellRenderer(new ThrowableTreeCellRend());
        JScrollPane jspThrowable = new JScrollPane(jtrThrowable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jspThrowable.setPreferredSize(new Dimension(500, 250));
        jpThrowable.add(jspThrowable, BorderLayout.CENTER);
        getContentPane().add(jpThrowable, BorderLayout.CENTER);
        getContentPane().add(jpButtons, BorderLayout.SOUTH);
        setTitle(RB.getString("DThrowableDetail.Title"));
        getRootPane().setDefaultButton(jbOK);
        initDialog();
        setResizable(true);
        jbOK.requestFocusInWindow();
    }

    /**
	 * Create tree node with information on the throwable and its cause throwables.
	 * 
	 * @return The tree node
	 */
    private DefaultMutableTreeNode createThrowableNodes() {
        DefaultMutableTreeNode topNode = new DefaultMutableTreeNode(RB.getString("DThrowableDetail.RootNode.text"));
        Throwable throwable = m_throwable;
        while (throwable != null) {
            DefaultMutableTreeNode throwableNode = new DefaultMutableTreeNode(throwable);
            topNode.add(throwableNode);
            for (StackTraceElement ste : throwable.getStackTrace()) {
                throwableNode.add(new DefaultMutableTreeNode(ste));
            }
            throwable = throwable.getCause();
        }
        return topNode;
    }

    /**
	 * Copy button pressed - copy throwable stack traces to clipboard.
	 */
    private void copyPressed() {
        StringBuilder strBuff = new StringBuilder();
        Throwable throwable = m_throwable;
        while (throwable != null) {
            strBuff.append(throwable);
            strBuff.append('\n');
            for (StackTraceElement ste : throwable.getStackTrace()) {
                strBuff.append('\t');
                strBuff.append(ste);
                strBuff.append('\n');
            }
            throwable = throwable.getCause();
            if (throwable != null) {
                strBuff.append('\n');
            }
        }
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection copy = new StringSelection(strBuff.toString());
        clipboard.setContents(copy, copy);
    }
}
