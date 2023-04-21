package ba_leipzig_lending_and_service_control_system.view;

import ba_leipzig_lending_and_service_control_system.conroller.ctrlDatabase;
import ba_leipzig_lending_and_service_control_system.conroller.ctrlLayout;
import ba_leipzig_lending_and_service_control_system.conroller.ctrlMain;
import ba_leipzig_lending_and_service_control_system.conroller.ctrlTools;
import ba_leipzig_lending_and_service_control_system.conroller.ctrlXML;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 * GUI-Class to show the admininstrator's section for editing users, deletet
 * and blocked recordsets
 *
 * @author Chris Hagen
 */
public class viewAdmin extends JDialog {

    private final int WIDTH = 600;

    private final int HEIGHT = 500;

    private final viewAdmin viewadmin;

    private JTabbedPane tabpane = null;

    private JTable tabUsers = null;

    private JTable tabBlocked = null;

    private JTable tabDeleted = null;

    private JButton btnNeu = null;

    private JButton btnBea = null;

    private JButton btnRes = null;

    private JButton btnLoe = null;

    private JButton btnAnz = null;

    private JPanel placeHolder = null;

    private JComboBox cboTableBlocked = null;

    private JComboBox cboTableDeleted = null;

    private JPanel panSym = null;

    private JButton btnZur = null;

    /**
     * Gets an instance of viewAdmin
     *
     * @return instance
     */
    public static viewAdmin getInstance() {
        return new viewAdmin();
    }

    /**
     * Creates a new instance of viewAdmin
     */
    private viewAdmin() {
        viewadmin = this;
    }

    /**
     * Creates a new instance of viewAdmin
     *
     * @param parent parent component
     */
    private viewAdmin(JFrame parent) {
        super(parent);
        viewadmin = this;
    }

    /**
     * Creates a new instance of viewAdmin
     *
     * @param parent parent component
     */
    private viewAdmin(JDialog parent) {
        super(parent);
        viewadmin = this;
    }

    /**
     * Shows the dialog for editing tasks
     *
     * @param parent parent component
     */
    public void showViewAdmin(Component parent) {
        try {
            if (parent.getClass().isInstance(JFrame.class)) new viewAdmin((JFrame) parent); else if (parent.getClass().isInstance(JDialog.class)) new viewAdmin((JDialog) parent);
            createGUI();
            this.setModal(true);
            this.setVisible(true);
        } catch (Exception e) {
            ctrlTools.showErrorMessage(this, e);
        }
    }

    /**
     * Creates the GUI.
     */
    private void createGUI() throws Exception {
        ctrlLayout.getDialogLayout(this, WIDTH, HEIGHT, ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "heading"));
        this.setLayout(new BorderLayout());
        panSym = new JPanel();
        panSym.setPreferredSize(new Dimension(10, 35));
        panSym.setLayout(new FlowLayout(FlowLayout.LEADING));
        panSym.setBorder(BorderFactory.createEmptyBorder(-4, 0, 0, 0));
        this.add(panSym, BorderLayout.NORTH);
        tabpane = new JTabbedPane();
        tabpane.setFont(ctrlMain.getFont());
        tabpane.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                if (btnAnz != null) {
                    if (tabpane.getSelectedIndex() == 0) {
                        btnNeu.setVisible(true);
                        btnBea.setVisible(true);
                        btnRes.setVisible(false);
                        btnLoe.setVisible(true);
                        btnAnz.setVisible(true);
                    }
                    if (tabpane.getSelectedIndex() == 1) {
                        btnNeu.setVisible(false);
                        btnBea.setVisible(false);
                        btnRes.setVisible(true);
                        btnLoe.setVisible(false);
                        btnAnz.setVisible(false);
                    }
                    if (tabpane.getSelectedIndex() == 2) {
                        btnNeu.setVisible(false);
                        btnBea.setVisible(false);
                        btnRes.setVisible(true);
                        btnLoe.setVisible(true);
                        btnAnz.setVisible(false);
                    }
                    aktual();
                }
            }
        });
        this.add(tabpane, BorderLayout.CENTER);
        JPanel panUser = new JPanel();
        panUser.setLayout(new BorderLayout());
        tabpane.add(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "lblusers"), panUser);
        tabUsers = new JTable();
        tabUsers.setFont(ctrlMain.getFont());
        tabUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabUsers.setColumnSelectionAllowed(false);
        tabUsers.setRowSelectionAllowed(true);
        tabUsers.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader header = tabUsers.getTableHeader();
        tabUsers.setRowHeight(20);
        header.setFont(ctrlMain.getFont());
        header.setReorderingAllowed(true);
        JScrollPane scrUser = new JScrollPane(tabUsers);
        scrUser.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrUser.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        tabUsers.getParent().setBackground(Color.WHITE);
        scrUser.setPreferredSize(new Dimension(420, 120));
        panUser.add(scrUser, BorderLayout.CENTER);
        JPanel panBlocked = new JPanel();
        panBlocked.setLayout(new BorderLayout());
        tabpane.add(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "lblblocked"), panBlocked);
        JPanel panTopBlocked = new JPanel();
        panTopBlocked.setPreferredSize(new Dimension(200, 40));
        panTopBlocked.setBorder(BorderFactory.createEtchedBorder());
        panTopBlocked.setLayout(null);
        panBlocked.add(panTopBlocked, BorderLayout.NORTH);
        JLabel lblTab = new JLabel(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "lbltable") + ":");
        lblTab.setFont(ctrlMain.getFont());
        lblTab.setBounds(10, 10, 100, 20);
        panTopBlocked.add(lblTab);
        cboTableBlocked = new JComboBox();
        cboTableBlocked.setFont(ctrlMain.getFont());
        cboTableBlocked.addItem(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "cbouser"));
        cboTableBlocked.addItem(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "cbocustomer"));
        cboTableBlocked.addItem(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "cbokey"));
        cboTableBlocked.addItem(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "cboinventory"));
        cboTableBlocked.addItem(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "cboexemplar"));
        cboTableBlocked.addItem(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "cboprocess"));
        cboTableBlocked.setBounds(80, 10, 130, 20);
        cboTableBlocked.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aktual();
            }
        });
        panTopBlocked.add(cboTableBlocked);
        tabBlocked = new JTable();
        tabBlocked.setFont(ctrlMain.getFont());
        tabBlocked.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabBlocked.setColumnSelectionAllowed(false);
        tabBlocked.setRowSelectionAllowed(true);
        tabBlocked.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        header = tabBlocked.getTableHeader();
        tabBlocked.setRowHeight(20);
        header.setFont(ctrlMain.getFont());
        header.setReorderingAllowed(true);
        JScrollPane scrBlocked = new JScrollPane(tabBlocked);
        scrBlocked.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrBlocked.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        tabBlocked.getParent().setBackground(Color.WHITE);
        scrBlocked.setPreferredSize(new Dimension(420, 600));
        panBlocked.add(scrBlocked, BorderLayout.CENTER);
        JPanel panDeleted = new JPanel();
        panDeleted.setLayout(new BorderLayout());
        tabpane.add(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "lbldeleted"), panDeleted);
        JPanel panTopDeleted = new JPanel();
        panTopDeleted.setPreferredSize(new Dimension(200, 40));
        panTopDeleted.setBorder(BorderFactory.createEtchedBorder());
        panTopDeleted.setLayout(null);
        panDeleted.add(panTopDeleted, BorderLayout.NORTH);
        JLabel lblTab2 = new JLabel(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "lbltable") + ":");
        lblTab2.setFont(ctrlMain.getFont());
        lblTab2.setBounds(10, 10, 100, 20);
        panTopDeleted.add(lblTab2);
        cboTableDeleted = new JComboBox();
        cboTableDeleted.setFont(ctrlMain.getFont());
        cboTableDeleted.addItem(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "cbouser"));
        cboTableDeleted.addItem(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "cbocustomer"));
        cboTableDeleted.addItem(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "cbokey"));
        cboTableDeleted.addItem(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "cboinventory"));
        cboTableDeleted.addItem(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "cboexemplar"));
        cboTableDeleted.addItem(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "cboprocess"));
        cboTableDeleted.setBounds(80, 10, 130, 20);
        cboTableDeleted.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aktual();
            }
        });
        panTopDeleted.add(cboTableDeleted);
        tabDeleted = new JTable();
        tabDeleted.setFont(ctrlMain.getFont());
        tabDeleted.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabDeleted.setColumnSelectionAllowed(false);
        tabDeleted.setRowSelectionAllowed(true);
        tabDeleted.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        header = tabDeleted.getTableHeader();
        tabDeleted.setRowHeight(20);
        header.setFont(ctrlMain.getFont());
        header.setReorderingAllowed(true);
        JScrollPane scrDeleted = new JScrollPane(tabDeleted);
        scrDeleted.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrDeleted.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        tabDeleted.getParent().setBackground(Color.WHITE);
        scrDeleted.setPreferredSize(new Dimension(420, 600));
        panDeleted.add(scrDeleted, BorderLayout.CENTER);
        btnNeu = new JButton();
        btnNeu.setPreferredSize(new Dimension(32, 32));
        btnNeu.setIcon((Icon) new ImageIcon(ImageIO.read(ctrlMain.class.getClass().getResourceAsStream("/ba_leipzig_lending_and_service_control_system/resource/images/btnNeu.png"))));
        btnNeu.setRolloverIcon((Icon) new ImageIcon(ImageIO.read(ctrlMain.class.getClass().getResourceAsStream("/ba_leipzig_lending_and_service_control_system/resource/images/btnNeu_A.png"))));
        btnNeu.setRolloverEnabled(true);
        btnNeu.setBorderPainted(false);
        btnNeu.setFocusPainted(false);
        btnNeu.setToolTipText(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "symnew"));
        btnNeu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new viewUserDetail(viewadmin, 'N', "");
                aktual();
            }
        });
        panSym.add(btnNeu);
        btnBea = new JButton();
        btnBea.setPreferredSize(new Dimension(32, 32));
        btnBea.setIcon((Icon) new ImageIcon(ImageIO.read(ctrlMain.class.getClass().getResourceAsStream("/ba_leipzig_lending_and_service_control_system/resource/images/btnBea.png"))));
        btnBea.setRolloverIcon((Icon) new ImageIcon(ImageIO.read(ctrlMain.class.getClass().getResourceAsStream("/ba_leipzig_lending_and_service_control_system/resource/images/btnBea_A.png"))));
        btnBea.setRolloverEnabled(true);
        btnBea.setBorderPainted(false);
        btnBea.setFocusPainted(false);
        btnBea.setToolTipText(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "symedit"));
        btnBea.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    int column = -1;
                    for (int i = 0; i < tabUsers.getModel().getColumnCount(); i++) {
                        if (tabUsers.getModel().getColumnName(i).equals(ctrlXML.getInstance().getLanguageDataValue(viewadmin.getClass().getSimpleName(), "lblid"))) column = i;
                    }
                    int row = tabUsers.getSelectedRow();
                    if (row == -1) {
                        ctrlTools.showInformationMessage(viewadmin, ctrlXML.getInstance().getLanguageDataValue(viewadmin.getClass().getSimpleName(), "norowselected"));
                        return;
                    }
                    String id = (String) tabUsers.getModel().getValueAt(row, column);
                    if (id.trim().toUpperCase().equals(ctrlMain.getUser().trim().toUpperCase())) {
                        ctrlTools.showInformationMessage(viewadmin, ctrlXML.getInstance().getLanguageDataValue(viewadmin.getClass().getSimpleName(), "noselfediting"));
                        return;
                    }
                    String sperre = ctrlDatabase.dlookup(ctrlMain.getConnection(), "LAUXART", "LAUSER", "LAUID='" + id + "'");
                    if (sperre.equals("X")) {
                        ctrlTools.showInformationMessage(viewadmin, ctrlXML.getInstance().getLanguageDataValue(viewadmin.getClass().getSimpleName(), "userediting"));
                        return;
                    }
                    ctrlDatabase.executeQuery(ctrlMain.getConnection(), "UPDATE LASCS.LAUSER SET LAUXART='X', LAUXNTZ='" + ctrlMain.getUser() + "' WHERE LAUID='" + id + "'");
                    new viewUserDetail(viewadmin, 'B', id);
                    aktual();
                } catch (Exception ex) {
                    ctrlTools.showErrorMessage(viewadmin, ex);
                }
            }
        });
        panSym.add(btnBea);
        btnRes = new JButton();
        btnRes.setPreferredSize(new Dimension(32, 32));
        btnRes.setIcon((Icon) new ImageIcon(ImageIO.read(ctrlMain.class.getClass().getResourceAsStream("/ba_leipzig_lending_and_service_control_system/resource/images/btnWieder.png"))));
        btnRes.setRolloverIcon((Icon) new ImageIcon(ImageIO.read(ctrlMain.class.getClass().getResourceAsStream("/ba_leipzig_lending_and_service_control_system/resource/images/btnWieder_A.png"))));
        btnRes.setRolloverEnabled(true);
        btnRes.setBorderPainted(false);
        btnRes.setFocusPainted(false);
        btnRes.setToolTipText(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "symrestore"));
        btnRes.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    if (ctrlTools.showYesNoQuestionMessage(viewadmin, ctrlXML.getInstance().getLanguageDataValue(viewadmin.getClass().getSimpleName(), "reallyrestore")) == JOptionPane.YES_OPTION) {
                        restore();
                        aktual();
                    }
                } catch (Exception ex) {
                    ctrlTools.showErrorMessage(viewadmin, ex);
                }
            }
        });
        panSym.add(btnRes);
        btnLoe = new JButton();
        btnLoe.setPreferredSize(new Dimension(32, 32));
        btnLoe.setIcon((Icon) new ImageIcon(ImageIO.read(ctrlMain.class.getClass().getResourceAsStream("/ba_leipzig_lending_and_service_control_system/resource/images/btnLoe.png"))));
        btnLoe.setRolloverIcon((Icon) new ImageIcon(ImageIO.read(ctrlMain.class.getClass().getResourceAsStream("/ba_leipzig_lending_and_service_control_system/resource/images/btnLoe_A.png"))));
        btnLoe.setRolloverEnabled(true);
        btnLoe.setBorderPainted(false);
        btnLoe.setFocusPainted(false);
        btnLoe.setToolTipText(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "symdelete"));
        btnLoe.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    switch(tabpane.getSelectedIndex()) {
                        case 0:
                            {
                                if (ctrlTools.showYesNoQuestionMessage(viewadmin, ctrlXML.getInstance().getLanguageDataValue(viewadmin.getClass().getSimpleName(), "reallydelete")) == JOptionPane.YES_OPTION) {
                                    int column = -1;
                                    for (int i = 0; i < tabUsers.getModel().getColumnCount(); i++) {
                                        if (tabUsers.getModel().getColumnName(i).equals(ctrlXML.getInstance().getLanguageDataValue(viewadmin.getClass().getSimpleName(), "lblid"))) column = i;
                                    }
                                    int row = tabUsers.getSelectedRow();
                                    if (row == -1) {
                                        ctrlTools.showInformationMessage(viewadmin, ctrlXML.getInstance().getLanguageDataValue(viewadmin.getClass().getSimpleName(), "norowselected"));
                                        return;
                                    }
                                    String id = (String) tabUsers.getModel().getValueAt(row, column);
                                    if (id.trim().toUpperCase().equals(ctrlMain.getUser().trim().toUpperCase())) {
                                        ctrlTools.showInformationMessage(viewadmin, ctrlXML.getInstance().getLanguageDataValue(viewadmin.getClass().getSimpleName(), "noselfediting"));
                                        return;
                                    }
                                    String sperre = ctrlDatabase.dlookup(ctrlMain.getConnection(), "LAUXART", "LAUSER", "LAUID='" + id + "'");
                                    if (sperre.equals("X")) {
                                        ctrlTools.showInformationMessage(viewadmin, ctrlXML.getInstance().getLanguageDataValue(viewadmin.getClass().getSimpleName(), "userediting"));
                                        return;
                                    }
                                    ctrlDatabase.executeQuery(ctrlMain.getConnection(), "UPDATE LASCS.LAUSER SET LAUSTAT='L', LAUXNTZ='" + ctrlMain.getUser() + "' WHERE LAUID='" + id + "'");
                                }
                                break;
                            }
                        case 2:
                            {
                                if (ctrlTools.showYesNoQuestionMessage(viewadmin, ctrlXML.getInstance().getLanguageDataValue(viewadmin.getClass().getSimpleName(), "reallyfinallydelete")) == JOptionPane.YES_OPTION) {
                                    finallyDeleteRecord();
                                    break;
                                }
                            }
                    }
                    aktual();
                } catch (Exception ex) {
                    ctrlTools.showErrorMessage(viewadmin, ex);
                }
            }
        });
        panSym.add(btnLoe);
        btnAnz = new JButton();
        btnAnz.setPreferredSize(new Dimension(32, 32));
        btnAnz.setIcon((Icon) new ImageIcon(ImageIO.read(ctrlMain.class.getClass().getResourceAsStream("/ba_leipzig_lending_and_service_control_system/resource/images/btnAnz.png"))));
        btnAnz.setRolloverIcon((Icon) new ImageIcon(ImageIO.read(ctrlMain.class.getClass().getResourceAsStream("/ba_leipzig_lending_and_service_control_system/resource/images/btnAnz_A.png"))));
        btnAnz.setRolloverEnabled(true);
        btnAnz.setBorderPainted(false);
        btnAnz.setFocusPainted(false);
        btnAnz.setToolTipText(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "symview"));
        btnAnz.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int column = -1;
                for (int i = 0; i < tabUsers.getModel().getColumnCount(); i++) {
                    if (tabUsers.getModel().getColumnName(i).equals(ctrlXML.getInstance().getLanguageDataValue(viewadmin.getClass().getSimpleName(), "lblid"))) column = i;
                }
                int row = tabUsers.getSelectedRow();
                if (row == -1) {
                    ctrlTools.showInformationMessage(viewadmin, ctrlXML.getInstance().getLanguageDataValue(viewadmin.getClass().getSimpleName(), "norowselected"));
                    return;
                }
                String id = (String) tabUsers.getModel().getValueAt(row, column);
                new viewUserDetail(viewadmin, 'A', id);
            }
        });
        panSym.add(btnAnz);
        placeHolder = new JPanel();
        placeHolder.setPreferredSize(new Dimension(20, 20));
        panSym.add(placeHolder);
        JButton btnAktual = new JButton();
        btnAktual.setPreferredSize(new Dimension(32, 32));
        btnAktual.setIcon((Icon) new ImageIcon(ImageIO.read(ctrlMain.class.getClass().getResourceAsStream("/ba_leipzig_lending_and_service_control_system/resource/images/btnAktual.png"))));
        btnAktual.setRolloverIcon((Icon) new ImageIcon(ImageIO.read(ctrlMain.class.getClass().getResourceAsStream("/ba_leipzig_lending_and_service_control_system/resource/images/btnAktual_A.png"))));
        btnAktual.setRolloverEnabled(true);
        btnAktual.setBorderPainted(false);
        btnAktual.setFocusPainted(false);
        btnAktual.setToolTipText(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "symrefresh"));
        btnAktual.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aktual();
            }
        });
        panSym.add(btnAktual);
        JPanel panBottom = new JPanel();
        panBottom.setPreferredSize(new Dimension(20, 40));
        panBottom.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.add(panBottom, BorderLayout.SOUTH);
        btnZur = new JButton(ctrlXML.getInstance().getLanguageDataValue("all", "back"));
        btnZur.setFont(ctrlMain.getFont());
        btnZur.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                viewadmin.dispose();
            }
        });
        btnZur.setPreferredSize(new Dimension(110, 25));
        panBottom.add(btnZur);
        tabpane.setSelectedIndex(1);
        tabpane.setSelectedIndex(0);
        aktual();
    }

    /**
     *  Refreshes the list.
     */
    public void aktual() {
        try {
            switch(tabpane.getSelectedIndex()) {
                case 0:
                    {
                        String query = "LAUSTAT='A' ORDER BY LAUID";
                        ArrayList rs = ctrlDatabase.getResultSet(ctrlMain.getConnection(), "LAUID, CONCAT(CONCAT(LAUNAME,', '), LAUFNAME), LAURIGHT, LAUXZST, " + "LAUXNTZ", "LAUSER", query);
                        Vector cols = new Vector();
                        cols.add(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "lblid"));
                        cols.add(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "lblname"));
                        cols.add(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "lblright"));
                        cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedat"));
                        cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedby"));
                        Vector data = new Vector();
                        for (int i = 0; i < rs.size(); i++) {
                            ArrayList rsset = (ArrayList) rs.get(i);
                            Vector row = new Vector();
                            row.add(rsset.get(0));
                            row.add(rsset.get(1));
                            row.add(ctrlXML.getInstance().getLanguageDataValue(viewadmin.getClass().getSimpleName(), "lblright[@right='" + rsset.get(2) + "']"));
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = df.parse((String) rsset.get(3));
                            df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
                            if (date != null) row.add(df.format(date)); else row.add("");
                            row.add(rsset.get(4));
                            data.add(row);
                        }
                        tabUsers.setModel(new DefaultTableModel(data, cols));
                        setTabColWidth(tabUsers, 0, 100);
                        setTabColWidth(tabUsers, 1, 200);
                        setTabColWidth(tabUsers, 2, 150);
                        setTabColWidth(tabUsers, 3, 150);
                        setTabColWidth(tabUsers, 4, 100);
                        tabUsers.invalidate();
                        if (tabUsers.getRowCount() > 0) tabUsers.changeSelection(0, 0, false, false);
                        final Vector findata = data;
                        final Vector fincols = cols;
                        tabUsers.getTableHeader().addMouseListener(new MouseAdapter() {

                            public void mousePressed(MouseEvent evt) {
                                ctrlTools.getInstance().getTableSortModel(findata, fincols).sortByColumn(tabUsers.columnAtPoint(evt.getPoint()));
                            }
                        });
                        break;
                    }
                case 1:
                    {
                        Vector cols = null;
                        Vector data = null;
                        switch(cboTableBlocked.getSelectedIndex()) {
                            case 0:
                                {
                                    String query = "LAUXART='X' ORDER BY LAUID";
                                    ArrayList rs = ctrlDatabase.getResultSet(ctrlMain.getConnection(), "LAUID, CONCAT(CONCAT(LAUNAME,', '), LAUFNAME), LAURIGHT, LAUXZST, " + "LAUXNTZ", "LAUSER", query);
                                    cols = new Vector();
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "lblid"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "lblname"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "lblright"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedat"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedby"));
                                    data = new Vector();
                                    for (int i = 0; i < rs.size(); i++) {
                                        ArrayList rsset = (ArrayList) rs.get(i);
                                        Vector row = new Vector();
                                        row.add(rsset.get(0));
                                        row.add(rsset.get(1));
                                        row.add(ctrlXML.getInstance().getLanguageDataValue(viewadmin.getClass().getSimpleName(), "lblright[@right='" + rsset.get(2) + "']"));
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date date = df.parse((String) rsset.get(3));
                                        df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
                                        if (date != null) row.add(df.format(date)); else row.add("");
                                        row.add(rsset.get(4));
                                        data.add(row);
                                    }
                                    tabBlocked.setModel(new DefaultTableModel(data, cols));
                                    setTabColWidth(tabBlocked, 0, 100);
                                    setTabColWidth(tabBlocked, 1, 200);
                                    setTabColWidth(tabBlocked, 2, 150);
                                    setTabColWidth(tabBlocked, 3, 150);
                                    setTabColWidth(tabBlocked, 4, 100);
                                    break;
                                }
                            case 1:
                                {
                                    String query = "LACXART='X' ORDER BY LACID";
                                    ArrayList rs = ctrlDatabase.getResultSet(ctrlMain.getConnection(), "LACID, CONCAT(CONCAT(LACNAME,', '), LACFNAME), LACGROUP, " + "LACSTREET, LACHNR, LACHNRADD, LACZIP, LACCITY, LACTEL, " + "LACEMAIL, LACXZST, LACXNTZ", "LACUSTOMER", query);
                                    cols = new Vector();
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCustomer.class.getSimpleName(), "lblid"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCustomer.class.getSimpleName(), "lblname"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCustomer.class.getSimpleName(), "lblgroup"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCustomer.class.getSimpleName(), "lblstreet"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCustomer.class.getSimpleName(), "lblhnr"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCustomer.class.getSimpleName(), "lblzip"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCustomer.class.getSimpleName(), "lblcity"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCustomer.class.getSimpleName(), "lbltel"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCustomer.class.getSimpleName(), "lblemail"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedat"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedby"));
                                    data = new Vector();
                                    for (int i = 0; i < rs.size(); i++) {
                                        ArrayList rsset = (ArrayList) rs.get(i);
                                        Vector row = new Vector();
                                        row.add(rsset.get(0));
                                        row.add(rsset.get(1));
                                        row.add(ctrlDatabase.dlookup(ctrlMain.getConnection(), "LAKDESCRIPTION", "LAKEY", "LAKART='K' AND LAKKEY='" + rsset.get(2).toString().substring(1) + "'"));
                                        row.add(rsset.get(3));
                                        row.add(rsset.get(4).toString() + (rsset.get(5) == null ? "" : rsset.get(5)));
                                        row.add(rsset.get(6));
                                        row.add(rsset.get(7));
                                        row.add(rsset.get(8));
                                        row.add(rsset.get(9));
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date date = df.parse((String) rsset.get(10));
                                        df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
                                        if (date != null) row.add(df.format(date)); else row.add("");
                                        row.add(rsset.get(11));
                                        data.add(row);
                                    }
                                    tabBlocked.setModel(new DefaultTableModel(data, cols));
                                    setTabColWidth(tabBlocked, 0, 100);
                                    setTabColWidth(tabBlocked, 1, 200);
                                    setTabColWidth(tabBlocked, 2, 150);
                                    setTabColWidth(tabBlocked, 3, 150);
                                    setTabColWidth(tabBlocked, 4, 100);
                                    setTabColWidth(tabBlocked, 5, 100);
                                    setTabColWidth(tabBlocked, 6, 150);
                                    setTabColWidth(tabBlocked, 7, 150);
                                    setTabColWidth(tabBlocked, 8, 150);
                                    setTabColWidth(tabBlocked, 9, 150);
                                    setTabColWidth(tabBlocked, 10, 100);
                                    break;
                                }
                            case 2:
                                {
                                    String query = "LAKXART='X' ORDER BY CONCAT(LAKART, LAKKEY)";
                                    ArrayList rs = ctrlDatabase.getResultSet(ctrlMain.getConnection(), "LAKART, LAKKEY, LAKDESCRIPTION, LAKXZST, LAKXNTZ", "LAKEY", query);
                                    cols = new Vector();
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewKeyDetail.class.getSimpleName(), "lblart"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewKeyDetail.class.getSimpleName(), "lblkey"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewKeyDetail.class.getSimpleName(), "lbldescription"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedat"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedby"));
                                    data = new Vector();
                                    for (int i = 0; i < rs.size(); i++) {
                                        ArrayList rsset = (ArrayList) rs.get(i);
                                        Vector row = new Vector();
                                        row.add(rsset.get(0));
                                        row.add(rsset.get(1));
                                        row.add(rsset.get(2));
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date date = df.parse((String) rsset.get(3));
                                        df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
                                        if (date != null) row.add(df.format(date)); else row.add("");
                                        row.add(rsset.get(4));
                                        data.add(row);
                                    }
                                    tabBlocked.setModel(new DefaultTableModel(data, cols));
                                    setTabColWidth(tabBlocked, 0, 130);
                                    setTabColWidth(tabBlocked, 1, 100);
                                    setTabColWidth(tabBlocked, 2, 200);
                                    setTabColWidth(tabBlocked, 3, 150);
                                    setTabColWidth(tabBlocked, 4, 100);
                                    break;
                                }
                            case 3:
                                {
                                    String query = "LAIXART='X' ORDER BY LAIID";
                                    ArrayList rs = ctrlDatabase.getResultSet(ctrlMain.getConnection(), "LAIID, LAIART, LAIRIGHT, LAIDESCRIPTION, LAIXZST, LAIXNTZ", "LAINVENTORY", query);
                                    cols = new Vector();
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewInventory.class.getSimpleName(), "lblid"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewInventory.class.getSimpleName(), "lblgroup"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewInventory.class.getSimpleName(), "lblright"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewInventory.class.getSimpleName(), "lbldescription"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedat"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedby"));
                                    data = new Vector();
                                    for (int i = 0; i < rs.size(); i++) {
                                        ArrayList rsset = (ArrayList) rs.get(i);
                                        Vector row = new Vector();
                                        row.add(rsset.get(0));
                                        row.add(ctrlDatabase.dlookup(ctrlMain.getConnection(), "LAKDESCRIPTION", "LAKEY", "LAKART='" + rsset.get(1).toString().substring(0, rsset.get(1).toString().length() - 1) + "' AND LAKKEY='" + rsset.get(1).toString().substring(rsset.get(1).toString().length() - 1) + "'"));
                                        row.add(ctrlDatabase.dlookup(ctrlMain.getConnection(), "LAKDESCRIPTION", "LAKEY", "LAKART='K' AND LAKKEY='" + rsset.get(2).toString().substring(1) + "'"));
                                        row.add(rsset.get(3));
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date date = df.parse((String) rsset.get(4));
                                        df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
                                        if (date != null) row.add(df.format(date)); else row.add("");
                                        row.add(rsset.get(5));
                                        data.add(row);
                                    }
                                    tabBlocked.setModel(new DefaultTableModel(data, cols));
                                    setTabColWidth(tabBlocked, 0, 100);
                                    setTabColWidth(tabBlocked, 1, 150);
                                    setTabColWidth(tabBlocked, 2, 150);
                                    setTabColWidth(tabBlocked, 3, 200);
                                    setTabColWidth(tabBlocked, 4, 150);
                                    setTabColWidth(tabBlocked, 5, 100);
                                    break;
                                }
                            case 4:
                                {
                                    String query = "LAEXART='X' ORDER BY LAEIID, LAEID";
                                    ArrayList rs = ctrlDatabase.getResultSet(ctrlMain.getConnection(), "LAEIID, LAEID, LAIART, LAEDESCRIPTION, LAEXZST, LAEXNTZ", "LAEXEMPLAR JOIN LAINVENTORY ON LAEIID=LAIID", query);
                                    cols = new Vector();
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewExemplar.class.getSimpleName(), "lbliid"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewExemplar.class.getSimpleName(), "lblid"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewExemplar.class.getSimpleName(), "lblgroup"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewExemplar.class.getSimpleName(), "lbldescription"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedat"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedby"));
                                    data = new Vector();
                                    for (int i = 0; i < rs.size(); i++) {
                                        ArrayList rsset = (ArrayList) rs.get(i);
                                        Vector row = new Vector();
                                        row.add(rsset.get(0));
                                        row.add(rsset.get(1));
                                        row.add(ctrlDatabase.dlookup(ctrlMain.getConnection(), "LAKDESCRIPTION", "LAKEY", "LAKART='" + rsset.get(2).toString().substring(0, rsset.get(2).toString().length() - 1) + "' AND LAKKEY='" + rsset.get(2).toString().substring(rsset.get(2).toString().length() - 1) + "'"));
                                        row.add(rsset.get(3));
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date date = df.parse((String) rsset.get(4));
                                        df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
                                        if (date != null) row.add(df.format(date)); else row.add("");
                                        row.add(rsset.get(5));
                                        data.add(row);
                                    }
                                    tabBlocked.setModel(new DefaultTableModel(data, cols));
                                    setTabColWidth(tabBlocked, 0, 100);
                                    setTabColWidth(tabBlocked, 1, 100);
                                    setTabColWidth(tabBlocked, 2, 150);
                                    setTabColWidth(tabBlocked, 3, 200);
                                    setTabColWidth(tabBlocked, 4, 150);
                                    setTabColWidth(tabBlocked, 5, 100);
                                    break;
                                }
                            case 5:
                                {
                                    String query = "LAPXART='X' ORDER BY LAPIID, LAPEID, LAPID, LAPIDP";
                                    ArrayList rs = ctrlDatabase.getResultSet(ctrlMain.getConnection(), "LAPIID, LAPEID, LAEDESCRIPTION, LAPID, LAPCID, " + "CONCAT(CONCAT(LACNAME,', '),LACFNAME), LAPGROUP, " + "LAPDATESTART, LAPDATEEND, LAPXZST, LAPXNTZ", "LAPROCESS JOIN LASCS.LAEXEMPLAR ON (LAPIID=LAEIID AND LAPEID=LAEID) " + "JOIN LASCS.LACUSTOMER ON (LAPCID=LACID)", query);
                                    cols = new Vector();
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCalendarDetail.class.getSimpleName(), "lbliid"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCalendarDetail.class.getSimpleName(), "lbleid"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCalendarDetail.class.getSimpleName(), "lbldescription"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCalendarDetail.class.getSimpleName(), "lblid"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCalendarDetail.class.getSimpleName(), "lblcid"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCalendarDetail.class.getSimpleName(), "lblname"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCalendarDetail.class.getSimpleName(), "lblgroup"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCalendarDetail.class.getSimpleName(), "lbldatestart"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCalendarDetail.class.getSimpleName(), "lbldateend"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedat"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedby"));
                                    data = new Vector();
                                    ArrayList doneIDs = new ArrayList();
                                    for (int i = 0; i < rs.size(); i++) {
                                        ArrayList rsset = (ArrayList) rs.get(i);
                                        Vector row = new Vector();
                                        if (doneIDs.contains(Integer.parseInt(rsset.get(3).toString()))) continue;
                                        doneIDs.add(Integer.parseInt(rsset.get(3).toString()));
                                        row.add(rsset.get(0));
                                        row.add(rsset.get(1));
                                        row.add(rsset.get(2));
                                        row.add(rsset.get(3));
                                        row.add(rsset.get(4));
                                        row.add(rsset.get(5));
                                        row.add(ctrlDatabase.dlookup(ctrlMain.getConnection(), "LAKDESCRIPTION", "LAKEY", "LAKART='V' AND LAKKEY='" + rsset.get(6).toString().substring(1) + "'"));
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date date = df.parse((String) rsset.get(7));
                                        df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
                                        if (date != null) row.add(df.format(date)); else row.add("");
                                        if (rsset.get(8) != null) {
                                            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            date = df.parse((String) rsset.get(8));
                                            df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
                                            if (date != null) row.add(df.format(date)); else row.add("");
                                        } else row.add("");
                                        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        date = df.parse((String) rsset.get(9));
                                        df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
                                        if (date != null) row.add(df.format(date)); else row.add("");
                                        row.add(rsset.get(10));
                                        data.add(row);
                                    }
                                    tabBlocked.setModel(new DefaultTableModel(data, cols));
                                    setTabColWidth(tabBlocked, 0, 100);
                                    setTabColWidth(tabBlocked, 1, 100);
                                    setTabColWidth(tabBlocked, 2, 200);
                                    setTabColWidth(tabBlocked, 3, 100);
                                    setTabColWidth(tabBlocked, 4, 100);
                                    setTabColWidth(tabBlocked, 5, 200);
                                    setTabColWidth(tabBlocked, 6, 150);
                                    setTabColWidth(tabBlocked, 7, 150);
                                    setTabColWidth(tabBlocked, 8, 150);
                                    setTabColWidth(tabBlocked, 9, 150);
                                    setTabColWidth(tabBlocked, 10, 100);
                                    break;
                                }
                        }
                        tabBlocked.invalidate();
                        if (tabBlocked.getRowCount() > 0) tabBlocked.changeSelection(0, 0, false, false);
                        final Vector findata = data;
                        final Vector fincols = cols;
                        tabBlocked.getTableHeader().addMouseListener(new MouseAdapter() {

                            public void mousePressed(MouseEvent evt) {
                                ctrlTools.getInstance().getTableSortModel(findata, fincols).sortByColumn(tabBlocked.columnAtPoint(evt.getPoint()));
                            }
                        });
                        break;
                    }
                case 2:
                    {
                        Vector cols = null;
                        Vector data = null;
                        switch(cboTableDeleted.getSelectedIndex()) {
                            case 0:
                                {
                                    String query = "LAUSTAT='L' ORDER BY LAUID";
                                    ArrayList rs = ctrlDatabase.getResultSet(ctrlMain.getConnection(), "LAUID, CONCAT(CONCAT(LAUNAME,', '), LAUFNAME), LAURIGHT, LAUXZST, " + "LAUXNTZ", "LAUSER", query);
                                    cols = new Vector();
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "lblid"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "lblname"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(this.getClass().getSimpleName(), "lblright"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedat"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedby"));
                                    data = new Vector();
                                    for (int i = 0; i < rs.size(); i++) {
                                        ArrayList rsset = (ArrayList) rs.get(i);
                                        Vector row = new Vector();
                                        row.add(rsset.get(0));
                                        row.add(rsset.get(1));
                                        row.add(ctrlXML.getInstance().getLanguageDataValue(viewadmin.getClass().getSimpleName(), "lblright[@right='" + rsset.get(2) + "']"));
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date date = df.parse((String) rsset.get(3));
                                        df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
                                        if (date != null) row.add(df.format(date)); else row.add("");
                                        row.add(rsset.get(4));
                                        data.add(row);
                                    }
                                    tabDeleted.setModel(new DefaultTableModel(data, cols));
                                    setTabColWidth(tabDeleted, 0, 100);
                                    setTabColWidth(tabDeleted, 1, 200);
                                    setTabColWidth(tabDeleted, 2, 150);
                                    setTabColWidth(tabDeleted, 3, 150);
                                    setTabColWidth(tabDeleted, 4, 100);
                                    break;
                                }
                            case 1:
                                {
                                    String query = "LACSTAT='L' ORDER BY LACID";
                                    ArrayList rs = ctrlDatabase.getResultSet(ctrlMain.getConnection(), "LACID, CONCAT(CONCAT(LACNAME,', '), LACFNAME), LACGROUP, " + "LACSTREET, LACHNR, LACHNRADD, LACZIP, LACCITY, LACTEL, " + "LACEMAIL, LACXZST, LACXNTZ", "LACUSTOMER", query);
                                    cols = new Vector();
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCustomer.class.getSimpleName(), "lblid"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCustomer.class.getSimpleName(), "lblname"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCustomer.class.getSimpleName(), "lblgroup"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCustomer.class.getSimpleName(), "lblstreet"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCustomer.class.getSimpleName(), "lblhnr"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCustomer.class.getSimpleName(), "lblzip"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCustomer.class.getSimpleName(), "lblcity"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCustomer.class.getSimpleName(), "lbltel"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCustomer.class.getSimpleName(), "lblemail"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedat"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedby"));
                                    data = new Vector();
                                    for (int i = 0; i < rs.size(); i++) {
                                        ArrayList rsset = (ArrayList) rs.get(i);
                                        Vector row = new Vector();
                                        row.add(rsset.get(0));
                                        row.add(rsset.get(1));
                                        row.add(ctrlDatabase.dlookup(ctrlMain.getConnection(), "LAKDESCRIPTION", "LAKEY", "LAKART='K' AND LAKKEY='" + rsset.get(2).toString().substring(1) + "'"));
                                        row.add(rsset.get(3));
                                        row.add(rsset.get(4).toString() + (rsset.get(5) == null ? "" : rsset.get(5)));
                                        row.add(rsset.get(6));
                                        row.add(rsset.get(7));
                                        row.add(rsset.get(8));
                                        row.add(rsset.get(9));
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date date = df.parse((String) rsset.get(10));
                                        df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
                                        if (date != null) row.add(df.format(date)); else row.add("");
                                        row.add(rsset.get(11));
                                        data.add(row);
                                    }
                                    tabDeleted.setModel(new DefaultTableModel(data, cols));
                                    setTabColWidth(tabDeleted, 0, 100);
                                    setTabColWidth(tabDeleted, 1, 200);
                                    setTabColWidth(tabDeleted, 2, 150);
                                    setTabColWidth(tabDeleted, 3, 150);
                                    setTabColWidth(tabDeleted, 4, 100);
                                    setTabColWidth(tabDeleted, 5, 100);
                                    setTabColWidth(tabDeleted, 6, 150);
                                    setTabColWidth(tabDeleted, 7, 150);
                                    setTabColWidth(tabDeleted, 8, 150);
                                    setTabColWidth(tabDeleted, 9, 150);
                                    setTabColWidth(tabDeleted, 10, 100);
                                    break;
                                }
                            case 2:
                                {
                                    String query = "LAKSTAT='L' ORDER BY CONCAT(LAKART, LAKKEY)";
                                    ArrayList rs = ctrlDatabase.getResultSet(ctrlMain.getConnection(), "LAKART, LAKKEY, LAKDESCRIPTION, LAKXZST, LAKXNTZ", "LAKEY", query);
                                    cols = new Vector();
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewKeyDetail.class.getSimpleName(), "lblart"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewKeyDetail.class.getSimpleName(), "lblkey"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewKeyDetail.class.getSimpleName(), "lbldescription"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedat"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedby"));
                                    data = new Vector();
                                    for (int i = 0; i < rs.size(); i++) {
                                        ArrayList rsset = (ArrayList) rs.get(i);
                                        Vector row = new Vector();
                                        row.add(rsset.get(0));
                                        row.add(rsset.get(1));
                                        row.add(rsset.get(2));
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date date = df.parse((String) rsset.get(3));
                                        df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
                                        if (date != null) row.add(df.format(date)); else row.add("");
                                        row.add(rsset.get(4));
                                        data.add(row);
                                    }
                                    tabDeleted.setModel(new DefaultTableModel(data, cols));
                                    setTabColWidth(tabDeleted, 0, 130);
                                    setTabColWidth(tabDeleted, 1, 100);
                                    setTabColWidth(tabDeleted, 2, 200);
                                    setTabColWidth(tabDeleted, 3, 150);
                                    setTabColWidth(tabDeleted, 4, 100);
                                    break;
                                }
                            case 3:
                                {
                                    String query = "LAISTAT='L' ORDER BY LAIID";
                                    ArrayList rs = ctrlDatabase.getResultSet(ctrlMain.getConnection(), "LAIID, LAIART, LAIRIGHT, LAIDESCRIPTION, LAIXZST, LAIXNTZ", "LAINVENTORY", query);
                                    cols = new Vector();
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewInventory.class.getSimpleName(), "lblid"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewInventory.class.getSimpleName(), "lblgroup"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewInventory.class.getSimpleName(), "lblright"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewInventory.class.getSimpleName(), "lbldescription"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedat"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedby"));
                                    data = new Vector();
                                    for (int i = 0; i < rs.size(); i++) {
                                        ArrayList rsset = (ArrayList) rs.get(i);
                                        Vector row = new Vector();
                                        row.add(rsset.get(0));
                                        row.add(ctrlDatabase.dlookup(ctrlMain.getConnection(), "LAKDESCRIPTION", "LAKEY", "LAKART='" + rsset.get(1).toString().substring(0, rsset.get(1).toString().length() - 1) + "' AND LAKKEY='" + rsset.get(1).toString().substring(rsset.get(1).toString().length() - 1) + "'"));
                                        row.add(ctrlDatabase.dlookup(ctrlMain.getConnection(), "LAKDESCRIPTION", "LAKEY", "LAKART='K' AND LAKKEY='" + rsset.get(2).toString().substring(1) + "'"));
                                        row.add(rsset.get(3));
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date date = df.parse((String) rsset.get(4));
                                        df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
                                        if (date != null) row.add(df.format(date)); else row.add("");
                                        row.add(rsset.get(5));
                                        data.add(row);
                                    }
                                    tabDeleted.setModel(new DefaultTableModel(data, cols));
                                    setTabColWidth(tabDeleted, 0, 100);
                                    setTabColWidth(tabDeleted, 1, 150);
                                    setTabColWidth(tabDeleted, 2, 150);
                                    setTabColWidth(tabDeleted, 3, 200);
                                    setTabColWidth(tabDeleted, 4, 150);
                                    setTabColWidth(tabDeleted, 5, 100);
                                    break;
                                }
                            case 4:
                                {
                                    String query = "LAESTAT='L' ORDER BY LAEIID, LAEID";
                                    ArrayList rs = ctrlDatabase.getResultSet(ctrlMain.getConnection(), "LAEIID, LAEID, LAIART, LAEDESCRIPTION, LAEXZST, LAEXNTZ", "LAEXEMPLAR JOIN LAINVENTORY ON LAEIID=LAIID", query);
                                    cols = new Vector();
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewExemplar.class.getSimpleName(), "lbliid"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewExemplar.class.getSimpleName(), "lblid"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewExemplar.class.getSimpleName(), "lblgroup"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewExemplar.class.getSimpleName(), "lbldescription"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedat"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedby"));
                                    data = new Vector();
                                    for (int i = 0; i < rs.size(); i++) {
                                        ArrayList rsset = (ArrayList) rs.get(i);
                                        Vector row = new Vector();
                                        row.add(rsset.get(0));
                                        row.add(rsset.get(1));
                                        row.add(ctrlDatabase.dlookup(ctrlMain.getConnection(), "LAKDESCRIPTION", "LAKEY", "LAKART='" + rsset.get(2).toString().substring(0, rsset.get(2).toString().length() - 1) + "' AND LAKKEY='" + rsset.get(2).toString().substring(rsset.get(2).toString().length() - 1) + "'"));
                                        row.add(rsset.get(3));
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date date = df.parse((String) rsset.get(4));
                                        df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
                                        if (date != null) row.add(df.format(date)); else row.add("");
                                        row.add(rsset.get(5));
                                        data.add(row);
                                    }
                                    tabDeleted.setModel(new DefaultTableModel(data, cols));
                                    setTabColWidth(tabDeleted, 0, 100);
                                    setTabColWidth(tabDeleted, 1, 100);
                                    setTabColWidth(tabDeleted, 2, 150);
                                    setTabColWidth(tabDeleted, 3, 200);
                                    setTabColWidth(tabDeleted, 4, 150);
                                    setTabColWidth(tabDeleted, 5, 100);
                                    break;
                                }
                            case 5:
                                {
                                    String query = "LAPSTAT='L' ORDER BY LAPIID, LAPEID, LAPID, LAPIDP";
                                    ArrayList rs = ctrlDatabase.getResultSet(ctrlMain.getConnection(), "LAPIID, LAPEID, LAEDESCRIPTION, LAPID, LAPCID, " + "CONCAT(CONCAT(LACNAME,', '),LACFNAME), LAPGROUP, " + "LAPDATESTART, LAPDATEEND, LAPXZST, LAPXNTZ", "LAPROCESS JOIN LASCS.LAEXEMPLAR ON (LAPIID=LAEIID AND LAPEID=LAEID) " + "JOIN LASCS.LACUSTOMER ON (LAPCID=LACID)", query);
                                    cols = new Vector();
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCalendarDetail.class.getSimpleName(), "lbliid"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCalendarDetail.class.getSimpleName(), "lbleid"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCalendarDetail.class.getSimpleName(), "lbldescription"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCalendarDetail.class.getSimpleName(), "lblid"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCalendarDetail.class.getSimpleName(), "lblcid"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCalendarDetail.class.getSimpleName(), "lblname"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCalendarDetail.class.getSimpleName(), "lblgroup"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCalendarDetail.class.getSimpleName(), "lbldatestart"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue(viewCalendarDetail.class.getSimpleName(), "lbldateend"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedat"));
                                    cols.add(ctrlXML.getInstance().getLanguageDataValue("all", "changedby"));
                                    data = new Vector();
                                    ArrayList doneIDs = new ArrayList();
                                    for (int i = 0; i < rs.size(); i++) {
                                        ArrayList rsset = (ArrayList) rs.get(i);
                                        Vector row = new Vector();
                                        if (doneIDs.contains(Integer.parseInt(rsset.get(3).toString()))) continue;
                                        doneIDs.add(Integer.parseInt(rsset.get(3).toString()));
                                        row.add(rsset.get(0));
                                        row.add(rsset.get(1));
                                        row.add(rsset.get(2));
                                        row.add(rsset.get(3));
                                        row.add(rsset.get(4));
                                        row.add(rsset.get(5));
                                        row.add(ctrlDatabase.dlookup(ctrlMain.getConnection(), "LAKDESCRIPTION", "LAKEY", "LAKART='V' AND LAKKEY='" + rsset.get(6).toString().substring(1) + "'"));
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date date = df.parse((String) rsset.get(7));
                                        df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
                                        if (date != null) row.add(df.format(date)); else row.add("");
                                        if (rsset.get(8) != null) {
                                            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            date = df.parse((String) rsset.get(8));
                                            df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
                                            if (date != null) row.add(df.format(date)); else row.add("");
                                        } else row.add("");
                                        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        date = df.parse((String) rsset.get(9));
                                        df = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
                                        if (date != null) row.add(df.format(date)); else row.add("");
                                        row.add(rsset.get(10));
                                        data.add(row);
                                    }
                                    tabDeleted.setModel(new DefaultTableModel(data, cols));
                                    setTabColWidth(tabDeleted, 0, 100);
                                    setTabColWidth(tabDeleted, 1, 100);
                                    setTabColWidth(tabDeleted, 2, 200);
                                    setTabColWidth(tabDeleted, 3, 100);
                                    setTabColWidth(tabDeleted, 4, 100);
                                    setTabColWidth(tabDeleted, 5, 200);
                                    setTabColWidth(tabDeleted, 6, 150);
                                    setTabColWidth(tabDeleted, 7, 150);
                                    setTabColWidth(tabDeleted, 8, 150);
                                    setTabColWidth(tabDeleted, 9, 150);
                                    setTabColWidth(tabDeleted, 10, 100);
                                    break;
                                }
                        }
                        tabDeleted.invalidate();
                        if (tabDeleted.getRowCount() > 0) tabDeleted.changeSelection(0, 0, false, false);
                        final Vector findata = data;
                        final Vector fincols = cols;
                        tabDeleted.getTableHeader().addMouseListener(new MouseAdapter() {

                            public void mousePressed(MouseEvent evt) {
                                ctrlTools.getInstance().getTableSortModel(findata, fincols).sortByColumn(tabDeleted.columnAtPoint(evt.getPoint()));
                            }
                        });
                        break;
                    }
            }
        } catch (Exception ex) {
            ctrlTools.showErrorMessage(this, ex);
        }
    }

    /**
     * Sets the width of a TableColumn.
     *
     * @param tab the table
     * @param col column index
     * @param width width
     */
    private void setTabColWidth(JTable tab, int col, int width) {
        TableColumn column = tab.getColumnModel().getColumn(col);
        column.setPreferredWidth(width);
        column.setCellEditor(ctrlTools.getInstance().getColCellEditor());
    }

    /**
     * Restores the specific record
     */
    private void restore() {
        try {
            switch(tabpane.getSelectedIndex()) {
                case 1:
                    {
                        switch(cboTableBlocked.getSelectedIndex()) {
                            case 0:
                                {
                                    String id = getCellValue(tabBlocked, viewAdmin.class, "lblid").toString().trim();
                                    ctrlDatabase.executeQuery(ctrlMain.getConnection(), "UPDATE LASCS.LAUSER " + "SET LAUXART='N', LAUXNTZ='" + ctrlMain.getUser() + "' WHERE LAUID='" + id + "'");
                                    break;
                                }
                            case 1:
                                {
                                    int id = Integer.parseInt(getCellValue(tabBlocked, viewCustomer.class, "lblid").toString());
                                    ctrlDatabase.executeQuery(ctrlMain.getConnection(), "UPDATE LASCS.LACUSTOMER " + "SET LACXART='N', LACXNTZ='" + ctrlMain.getUser() + "' WHERE LACID=" + id);
                                    break;
                                }
                            case 2:
                                {
                                    String art = getCellValue(tabBlocked, viewKeyDetail.class, "lblart").toString().trim();
                                    String key = getCellValue(tabBlocked, viewKeyDetail.class, "lblkey").toString().trim();
                                    ctrlDatabase.executeQuery(ctrlMain.getConnection(), "UPDATE LASCS.LAKEY " + "SET LAKXART='N', LAKXNTZ='" + ctrlMain.getUser() + "' WHERE LAKART='" + art + "' AND LAKKEY='" + key + "'");
                                    break;
                                }
                            case 3:
                                {
                                    int id = Integer.parseInt(getCellValue(tabBlocked, viewInventory.class, "lblid").toString());
                                    ctrlDatabase.executeQuery(ctrlMain.getConnection(), "UPDATE LASCS.LAINVENTORY " + "SET LAIXART='N', LAIXNTZ='" + ctrlMain.getUser() + "' WHERE LAIID=" + id);
                                    break;
                                }
                            case 4:
                                {
                                    int iid = Integer.parseInt(getCellValue(tabBlocked, viewExemplar.class, "lbliid").toString());
                                    int id = Integer.parseInt(getCellValue(tabBlocked, viewExemplar.class, "lblid").toString());
                                    ctrlDatabase.executeQuery(ctrlMain.getConnection(), "UPDATE LASCS.LAEXEMPLAR " + "SET LAEXART='N', LAEXNTZ='" + ctrlMain.getUser() + "' WHERE LAEIID=" + iid + " AND LAEID=" + id);
                                    break;
                                }
                            case 5:
                                {
                                    int iid = Integer.parseInt(getCellValue(tabBlocked, viewCalendarDetail.class, "lbliid").toString());
                                    int eid = Integer.parseInt(getCellValue(tabBlocked, viewCalendarDetail.class, "lbleid").toString());
                                    int id = Integer.parseInt(getCellValue(tabBlocked, viewCalendarDetail.class, "lblid").toString());
                                    ctrlDatabase.executeQuery(ctrlMain.getConnection(), "UPDATE LASCS.LAPROCESS " + "SET LAPXART='N', LAPXNTZ='" + ctrlMain.getUser() + "' WHERE LAPIID=" + iid + " AND LAPEID=" + eid + " AND LAPID=" + id);
                                    break;
                                }
                        }
                        break;
                    }
                case 2:
                    {
                        switch(cboTableDeleted.getSelectedIndex()) {
                            case 0:
                                {
                                    String id = getCellValue(tabDeleted, viewAdmin.class, "lblid").toString().trim();
                                    ctrlDatabase.executeQuery(ctrlMain.getConnection(), "UPDATE LASCS.LAUSER " + "SET LAUSTAT='A', LAUXNTZ='" + ctrlMain.getUser() + "' WHERE LAUID='" + id + "'");
                                    break;
                                }
                            case 1:
                                {
                                    int id = Integer.parseInt(getCellValue(tabDeleted, viewCustomer.class, "lblid").toString());
                                    ctrlDatabase.executeQuery(ctrlMain.getConnection(), "UPDATE LASCS.LACUSTOMER " + "SET LACSTAT='A', LACXNTZ='" + ctrlMain.getUser() + "' WHERE LACID=" + id);
                                    break;
                                }
                            case 2:
                                {
                                    String art = getCellValue(tabDeleted, viewKeyDetail.class, "lblart").toString().trim();
                                    String key = getCellValue(tabDeleted, viewKeyDetail.class, "lblkey").toString().trim();
                                    ctrlDatabase.executeQuery(ctrlMain.getConnection(), "UPDATE LASCS.LAKEY " + "SET LAKSTAT='A', LAKXNTZ='" + ctrlMain.getUser() + "' WHERE LAKART='" + art + "' AND LAKKEY='" + key + "'");
                                    break;
                                }
                            case 3:
                                {
                                    int id = Integer.parseInt(getCellValue(tabDeleted, viewInventory.class, "lblid").toString());
                                    ctrlDatabase.executeQuery(ctrlMain.getConnection(), "UPDATE LASCS.LAINVENTORY " + "SET LAISTAT='A', LAIXNTZ='" + ctrlMain.getUser() + "' WHERE LAIID=" + id);
                                    break;
                                }
                            case 4:
                                {
                                    int iid = Integer.parseInt(getCellValue(tabDeleted, viewExemplar.class, "lbliid").toString());
                                    int id = Integer.parseInt(getCellValue(tabDeleted, viewExemplar.class, "lblid").toString());
                                    ctrlDatabase.executeQuery(ctrlMain.getConnection(), "UPDATE LASCS.LAEXEMPLAR " + "SET LAESTAT='A', LAEXNTZ='" + ctrlMain.getUser() + "' WHERE LAEIID=" + iid + " AND LAEID=" + id);
                                    break;
                                }
                            case 5:
                                {
                                    int iid = Integer.parseInt(getCellValue(tabDeleted, viewCalendarDetail.class, "lbliid").toString());
                                    int eid = Integer.parseInt(getCellValue(tabDeleted, viewCalendarDetail.class, "lbleid").toString());
                                    int id = Integer.parseInt(getCellValue(tabDeleted, viewCalendarDetail.class, "lblid").toString());
                                    ctrlDatabase.executeQuery(ctrlMain.getConnection(), "UPDATE LASCS.LAPROCESS " + "SET LAPSTAT='A', LAPXNTZ='" + ctrlMain.getUser() + "' WHERE LAPIID=" + iid + " AND LAPEID=" + eid + " AND LAPID=" + id);
                                    break;
                                }
                        }
                        break;
                    }
            }
        } catch (Exception e) {
            if (e.getMessage().equals("no row selected")) return;
            ctrlTools.showErrorMessage(viewadmin, e);
        }
    }

    /**
     * Gets the value of the cell named by the column with the name <i>name</i>
     * and the current selected row of the table <i>tab</i>
     *
     * @param tab table
     * @param parent parent class, which includes the columnname in language.xml
     * @param name name of the tag in language.xml including the column name
     * @return cell value
     */
    private Object getCellValue(JTable tab, Class parent, String name) throws Exception {
        int column = -1;
        for (int i = 0; i < tab.getModel().getColumnCount(); i++) {
            if (tab.getModel().getColumnName(i).equals(ctrlXML.getInstance().getLanguageDataValue(parent.getSimpleName(), name))) column = i;
        }
        int row = tab.getSelectedRow();
        if (row == -1) {
            ctrlTools.showInformationMessage(viewadmin, ctrlXML.getInstance().getLanguageDataValue(viewadmin.getClass().getSimpleName(), "norowselected"));
            throw new Exception("no row selected");
        }
        return tab.getModel().getValueAt(row, column);
    }

    /**
     * Finally deletes a record
     */
    private void finallyDeleteRecord() throws Exception {
        switch(cboTableDeleted.getSelectedIndex()) {
            case 0:
                {
                    String id = getCellValue(tabDeleted, viewAdmin.class, "lblid").toString().trim();
                    ctrlDatabase.executeQuery(ctrlMain.getConnection(), "DELETE FROM LASCS.LAUSER " + " WHERE LAUID='" + id + "'");
                    break;
                }
            case 1:
                {
                    int id = Integer.parseInt(getCellValue(tabDeleted, viewCustomer.class, "lblid").toString());
                    if (ctrlDatabase.dcount(ctrlMain.getConnection(), "LAPROCESS", "LAPCID=" + id) > 0) {
                        ctrlTools.showInformationMessage(this, ctrlXML.getInstance().getLanguageDataValue(viewadmin.getClass().getSimpleName(), "existingdependence[@table='LAPROCESS']"));
                        return;
                    }
                    ctrlDatabase.executeQuery(ctrlMain.getConnection(), "DELETE FROM LASCS.LACUSTOMER " + "WHERE LACID=" + id);
                    break;
                }
            case 2:
                {
                    String art = getCellValue(tabDeleted, viewKeyDetail.class, "lblart").toString().trim();
                    String key = getCellValue(tabDeleted, viewKeyDetail.class, "lblkey").toString().trim();
                    if (ctrlDatabase.dcount(ctrlMain.getConnection(), "LAKEY", "LAKART LIKE '" + art + key + "%'") > 0) {
                        ctrlTools.showInformationMessage(this, ctrlXML.getInstance().getLanguageDataValue(viewadmin.getClass().getSimpleName(), "existingdependence[@table='LAKEY']"));
                        return;
                    }
                    if (ctrlDatabase.dcount(ctrlMain.getConnection(), "LACUSTOMER", "LACGROUP='" + art + key + "'") > 0) {
                        ctrlTools.showInformationMessage(this, ctrlXML.getInstance().getLanguageDataValue(viewadmin.getClass().getSimpleName(), "existingdependence[@table='LACUSTOMER']"));
                        return;
                    }
                    if (ctrlDatabase.dcount(ctrlMain.getConnection(), "LAINVENTORY", "LAIART='" + art + key + "'") > 0) {
                        ctrlTools.showInformationMessage(this, ctrlXML.getInstance().getLanguageDataValue(viewadmin.getClass().getSimpleName(), "existingdependence[@table='LAINVENTORY']"));
                        return;
                    }
                    if (ctrlDatabase.dcount(ctrlMain.getConnection(), "LAPROCESS", "LAPGROUP='" + art + key + "'") > 0) {
                        ctrlTools.showInformationMessage(this, ctrlXML.getInstance().getLanguageDataValue(viewadmin.getClass().getSimpleName(), "existingdependence[@table='LAPROCESS']"));
                        return;
                    }
                    ctrlDatabase.executeQuery(ctrlMain.getConnection(), "DELETE FROM LASCS.LAKEY " + "WHERE LAKART='" + art + "' AND LAKKEY='" + key + "'");
                    break;
                }
            case 3:
                {
                    int id = Integer.parseInt(getCellValue(tabDeleted, viewInventory.class, "lblid").toString());
                    if (ctrlDatabase.dcount(ctrlMain.getConnection(), "LAEXEMPLAR", "LAEIID=" + id) > 0) {
                        ctrlTools.showInformationMessage(this, ctrlXML.getInstance().getLanguageDataValue(viewadmin.getClass().getSimpleName(), "existingdependence[@table='LAEXEMPLAR']"));
                        return;
                    }
                    ctrlDatabase.executeQuery(ctrlMain.getConnection(), "DELETE FROM LASCS.LAINVENTORY " + "WHERE LAIID=" + id);
                    break;
                }
            case 4:
                {
                    int iid = Integer.parseInt(getCellValue(tabDeleted, viewExemplar.class, "lbliid").toString());
                    int id = Integer.parseInt(getCellValue(tabDeleted, viewExemplar.class, "lblid").toString());
                    if (ctrlDatabase.dcount(ctrlMain.getConnection(), "LAPROCESS", "LAPIID=" + iid + " AND LAPEID=" + id) > 0) {
                        ctrlTools.showInformationMessage(this, ctrlXML.getInstance().getLanguageDataValue(viewadmin.getClass().getSimpleName(), "existingdependence[@table='LAPROCESS']"));
                        return;
                    }
                    ctrlDatabase.executeQuery(ctrlMain.getConnection(), "DELETE FROM LASCS.LAEXEMPLAR " + "WHERE LAEIID=" + iid + " AND LAEID=" + id);
                    break;
                }
            case 5:
                {
                    int iid = Integer.parseInt(getCellValue(tabDeleted, viewCalendarDetail.class, "lbliid").toString());
                    int eid = Integer.parseInt(getCellValue(tabDeleted, viewCalendarDetail.class, "lbleid").toString());
                    int id = Integer.parseInt(getCellValue(tabDeleted, viewCalendarDetail.class, "lblid").toString());
                    ctrlDatabase.executeQuery(ctrlMain.getConnection(), "DELETE FROM LASCS.LAPROCESS " + "WHERE LAPIID=" + iid + " AND LAPEID=" + eid + " AND LAPID=" + id);
                    break;
                }
        }
    }
}
