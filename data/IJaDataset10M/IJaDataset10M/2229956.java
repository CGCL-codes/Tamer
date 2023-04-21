package net.mjrz.fm.ui.panels;

import java.awt.GridBagLayout;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.event.*;
import org.apache.log4j.Logger;
import java.awt.Dimension;
import java.awt.Insets;
import java.text.NumberFormat;
import net.mjrz.fm.entity.*;
import net.mjrz.fm.entity.beans.Transaction;
import net.mjrz.fm.services.*;
import net.mjrz.fm.utils.Messages;
import net.mjrz.fm.ui.panels.AccountsTreePanel.PopupMenuHandler;

public class TxDetailsPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JLabel jLabel1 = null;

    private JTextArea notesTa = null;

    private FManEntityManager em = null;

    private static Logger logger = Logger.getLogger(TxDetailsPanel.class.getName());

    /**
	 * This is the default constructor
	 */
    public TxDetailsPanel() {
        super();
        initialize();
        em = new FManEntityManager();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setLayout(new BorderLayout(2, 2));
        jLabel1 = new JLabel();
        this.add(jLabel1, BorderLayout.NORTH);
        this.add(getNotesTa(), BorderLayout.CENTER);
    }

    /**
	 * This method initializes notesTa
	 * 
	 * @return javax.swing.JTextArea
	 */
    private JScrollPane getNotesTa() {
        if (notesTa == null) {
            notesTa = new JTextArea();
            notesTa.setEditable(false);
            notesTa.setLineWrap(true);
            notesTa.setWrapStyleWord(true);
        }
        MouseListener[] mlist = notesTa.getMouseListeners();
        for (int i = 0; i < mlist.length; i++) {
            if (mlist[i].getClass().getName().equals("com.pagosoft.plaf.TextComponentPopupHandler")) {
                notesTa.removeMouseListener(mlist[i]);
                break;
            }
        }
        notesTa.addMouseListener(new NotesMouseListener());
        return new JScrollPane(notesTa);
    }

    class NotesMouseListener implements MouseListener {

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            mouseReleased(e);
        }

        public void mouseReleased(MouseEvent e) {
            if (!e.isPopupTrigger()) return;
            showPopup(e.getPoint());
        }
    }

    private void showPopup(Point p) {
        JPopupMenu popup;
        popup = new JPopupMenu();
        JMenuItem mItem = new JMenuItem("Copy");
        mItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                notesTa.copy();
            }
        });
        String txt = notesTa.getSelectedText();
        if (txt == null || txt.length() == 0) {
            mItem.setEnabled(false);
        }
        popup.add(mItem);
        mItem = new JMenuItem("Select All");
        mItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                notesTa.requestFocusInWindow();
                notesTa.selectAll();
            }
        });
        popup.add(mItem);
        popup.pack();
        popup.show(notesTa, p.x, p.y);
    }

    public void updateTx(String tx) {
        try {
            Transaction t = em.getTransaction(SessionManager.getSessionUserId(), Long.parseLong(tx));
            if (t == null) return;
            if (t.getActivityBy() != null) jLabel1.setText(t.getActivityBy());
            notesTa.setText(t.getTxNotes());
        } catch (NumberFormatException e) {
            logger.error(e);
        } catch (Exception e) {
            logger.error(net.mjrz.fm.utils.MiscUtils.stackTrace2String(e));
        }
    }
}
