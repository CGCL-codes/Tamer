package edu.psu.bd.math.grooms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import org.cgsuite.plugin.EditorPanel;
import org.cgsuite.plugin.GridEditable;
import org.cgsuite.plugin.GridEditorPanel;
import org.cgsuite.ui.AboutDialog;
import org.cgsuite.util.Grid;

/**
 *
 * @author Joe Pleso
 */
public final class GRooms extends JPanel implements Runnable, ActionListener, PropertyChangeListener {

    private static final double VERSION = 0.01;

    public static final String CALCULATE = "Calculate";

    public static final String ABOUT = "About";

    private static final String ABOUT_TITLE = "About GRooms";

    private static final String[] ABOUT_BODY = { "GRooms", "Version " + Double.toString(VERSION), "(c) 2007 Joe Pleso", "joe.pleso@gmail.com", "\n", "Combinatorial Game Suite", "Version 0.7", "(c) 2002--2007 Aaron Siegel", "http://www.cgsuite.org/" };

    public static final String KO_MESSAGE = "A ko was found in the game tree.";

    public GRooms() {
        super(new BorderLayout());
        GridEditorPanel gep = new GridEditorPanel(new Grid(5, 5, Grid.BitsPerEntry.TWO));
        gep.addPropertyChangeListener(EditorPanel.EDIT_STATE_PROPERTY, this);
        add(gep, BorderLayout.CENTER);
        JPanel bigPanel = new JPanel(new GridLayout(0, 1));
        bigPanel.setBorder(BorderFactory.createEtchedBorder());
        JButton b1 = new JButton(CALCULATE);
        b1.setVerticalTextPosition(AbstractButton.CENTER);
        b1.setHorizontalTextPosition(AbstractButton.CENTER);
        b1.setMnemonic(KeyEvent.VK_C);
        b1.setActionCommand(CALCULATE);
        JButton b2 = new JButton(ABOUT);
        b2.setVerticalTextPosition(AbstractButton.CENTER);
        b2.setHorizontalTextPosition(AbstractButton.CENTER);
        b2.setMnemonic(KeyEvent.VK_A);
        b2.setActionCommand(ABOUT);
        b1.addActionListener(this);
        b2.addActionListener(this);
        bigPanel.add(b1);
        bigPanel.add(b2);
        add(bigPanel, BorderLayout.WEST);
    }

    private void about() {
        new AboutDialog((JFrame) this.getTopLevelAncestor(), ABOUT_TITLE, ABOUT_BODY).setVisible(true);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        ((JFrame) this.getTopLevelAncestor()).pack();
    }

    private void calculate() {
        Object o = new JProgressBar();
        ((JProgressBar) o).setIndeterminate(true);
        Object a[] = { "Cancel" };
        JOptionPane jop = new JOptionPane(o, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, (Icon) null, a, (Object) null);
        JDialog dialog = jop.createDialog((JFrame) this.getTopLevelAncestor(), "Building Game Tree...");
        Component[] components = this.getComponents();
        int i = components.length;
        for (i--; i >= 0; i--) {
            if (components[i] instanceof GridEditorPanel) break;
        }
        GoRoom gr = new GoRoom(((GridEditorPanel) components[i]).getGrid());
        GRoomsWorker worker = new GRoomsWorker(gr);
        GRoomsWorkerWaiter gww = new GRoomsWorkerWaiter(dialog, worker);
        jop.addPropertyChangeListener(gww);
        worker.addPropertyChangeListener(gww);
        worker.execute();
        dialog.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command == CALCULATE) calculate();
        if (command == ABOUT) about();
    }

    public void run() {
        createAndShowGUI();
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("GRooms");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GRooms contentPane = new GRooms();
        contentPane.setOpaque(true);
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new GRooms());
    }
}
