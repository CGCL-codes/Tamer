package org.plotnikov.pricemaker;

import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import org.jdesktop.application.Action;
import org.plotnikov.pricemaker.db.DBOperations;
import org.plotnikov.pricemaker.db.SQLiteDBOperationsImpl;
import org.plotnikov.pricemaker.error.DBException;
import org.plotnikov.pricemaker.error.InitException;
import org.plotnikov.pricemaker.error.RootException;
import org.plotnikov.pricemaker.gui.utils.ComboBoxEditedListener;
import org.plotnikov.pricemaker.patterns.observer.PriceList;
import org.plotnikov.pricemaker.patterns.observer.DrawObject;
import org.plotnikov.pricemaker.preferences.OthersDialog;
import org.plotnikov.pricemaker.preferences.ProductionDialog;
import org.plotnikov.pricemaker.utils.DBEntity;
import org.plotnikov.pricemaker.gui.utils.GUIUtils;
import org.plotnikov.pricemaker.utils.ComboIDs;
import org.plotnikov.pricemaker.utils.PrintUtilities;

/**
 *
 * @author alexey
 */
public class MainFrame extends javax.swing.JFrame {

    private static DBOperations db = null;

    private PriceList priceList = null;

    private JPanel priceListPanel = null;

    private PrintDialog printDialog = null;

    private JDialog aboutBox;

    /** Creates new form MainFrame */
    public MainFrame() {
        initComponents();
        datePicker1.setDateFormat(new SimpleDateFormat("dd.MM.yyyy"));
        priceList = new PriceList(System.getProperty("user.dir") + File.separator + "pict.jpg");
        try {
            db = new SQLiteDBOperationsImpl();
            initModels();
            initPriceListDrawObjects();
            priceList.rebuildLayer();
        } catch (RootException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            GUIUtils.showError(ex.getMessage());
            dispose();
            return;
        }
        jTable1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    rebuildMainFrameInfo();
                }
            }
        });
        if (autoCompleteComboBox1.getItemCount() > 0) autoCompleteComboBox1.setSelectedIndex(0);
        if (autoCompleteComboBox2.getItemCount() > 0) autoCompleteComboBox2.setSelectedIndex(0);
        if (autoCompleteComboBox3.getItemCount() > 0) autoCompleteComboBox3.setSelectedIndex(0);
        if (autoCompleteComboBox4.getItemCount() > 0) autoCompleteComboBox4.setSelectedIndex(0);
        if (autoCompleteComboBox5.getItemCount() > 0) autoCompleteComboBox5.setSelectedIndex(0);
        priceListPanel = priceList.getPanel();
        jPanel1.add(priceListPanel);
        jPanel1.setSize(300, 300);
        jTextField5.setText("100");
        jTextField6.setText("00");
        printDialog = new PrintDialog(this, false);
        setVisible(true);
    }

    public static DBOperations getDB() {
        return db;
    }

    private void initModels() throws InitException {
        try {
            autoCompleteComboBox1.setModel(new PricerListModel(db.listOrganisations()));
            autoCompleteComboBox2.setModel(new PricerListModel(db.listTitleProducts()));
            autoCompleteComboBox4.setModel(new PricerListModel(db.listPostavchiki()));
            autoCompleteComboBox5.setModel(new PricerListModel(db.listPricesFor()));
        } catch (DBException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initPriceListDrawObjects() throws InitException {
        priceList.addRefreshObjectToHash(makeRefreshObj("organisationname", 120, 34, 18, "a_AvanteTitulB&W", Font.PLAIN, ""));
        priceList.addRefreshObjectToHash(makeRefreshObj("organisation", 180, 17, 9, "Times New Roman", Font.PLAIN, "Организация"));
        priceList.addRefreshObjectToHash(makeRefreshObj("titleproduct", 47, 52, 18, "Times New Roman", Font.PLAIN, "Наименование товара"));
        priceList.addRefreshObjectToHash(makeRefreshObj("nameproduct", 40, 72, 24, "Times New Roman", Font.BOLD, "Название товара"));
        priceList.addRefreshObjectToHash(makeRefreshObj("otherinfo", 65, 85, 16, "Times New Roman", Font.PLAIN, ""));
        priceList.addRefreshObjectToHash(makeRefreshObj("postavchik", 10, 94, 9, "Times New Roman", Font.PLAIN, "Поставщик"));
        priceList.addRefreshObjectToHash(makeRefreshObj("pricefor", 205, 94, 9, "Times New Roman", Font.PLAIN, "Цена за"));
        priceList.addRefreshObjectToHash(makeRefreshObj("postavchikname", 10, 104, 11, "Times New Roman", Font.PLAIN, "Название поставщика"));
        priceList.addRefreshObjectToHash(makeRefreshObj("price", 205, 107, 16, "Times New Roman", Font.BOLD, "1 кг"));
        priceList.addRefreshObjectToHash(makeRefreshObj("date", 175, 178, 14, "Times New Roman", Font.PLAIN, new SimpleDateFormat("dd.MM.yyyy").format(new Date())));
        priceList.addRefreshObjectToHash(makeRefreshObj("pricecopeek", 165, 142, 36, "Times New Roman", Font.BOLD, "00"));
        priceList.addRefreshObjectToHash(makeRefreshObj("line", 165, 138, 36, "Times New Roman", Font.BOLD, "__"));
        priceList.addRefreshObjectToHash(makeRefreshObj("pricerubl", 55, 165, 72, "Times New Roman", Font.BOLD, "100"));
    }

    private DrawObject makeRefreshObj(final String name, int width, int height, int fontsize, String fontname, int fontstyle, final String drawstring) {
        DrawObject obj = new DrawObject(width, height, drawstring, fontsize, fontname, fontstyle, name);
        return obj;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        datePicker1 = new com.michaelbaranov.microba.calendar.DatePicker();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        autoCompleteComboBox1 = new org.plotnikov.pricemaker.gui.utils.AutoCompleteComboBox();
        autoCompleteComboBox2 = new org.plotnikov.pricemaker.gui.utils.AutoCompleteComboBox();
        autoCompleteComboBox3 = new org.plotnikov.pricemaker.gui.utils.AutoCompleteComboBox();
        autoCompleteComboBox4 = new org.plotnikov.pricemaker.gui.utils.AutoCompleteComboBox();
        autoCompleteComboBox5 = new org.plotnikov.pricemaker.gui.utils.AutoCompleteComboBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.plotnikov.pricemaker.PriceMakerApp.class).getContext().getResourceMap(MainFrame.class);
        setTitle(resourceMap.getString("Form.title"));
        setName("Form");
        jScrollPane1.setName("jScrollPane1");
        jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] {}));
        jTable1.setName("jTable1");
        jScrollPane1.setViewportView(jTable1);
        jTextField1.setText(resourceMap.getString("jTextField1.text"));
        jTextField1.setName("jTextField1");
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.plotnikov.pricemaker.PriceMakerApp.class).getContext().getActionMap(MainFrame.class, this);
        jButton2.setAction(actionMap.get("search"));
        jButton2.setText(resourceMap.getString("jButton2.text"));
        jButton2.setName("jButton2");
        jLabel1.setText(resourceMap.getString("jLabel1.text"));
        jLabel1.setName("jLabel1");
        jLabel2.setText(resourceMap.getString("jLabel2.text"));
        jLabel2.setName("jLabel2");
        jLabel3.setText(resourceMap.getString("jLabel3.text"));
        jLabel3.setName("jLabel3");
        jLabel4.setText(resourceMap.getString("jLabel4.text"));
        jLabel4.setName("jLabel4");
        jLabel5.setText(resourceMap.getString("jLabel5.text"));
        jLabel5.setName("jLabel5");
        jLabel6.setText(resourceMap.getString("jLabel6.text"));
        jLabel6.setName("jLabel6");
        jLabel7.setText(resourceMap.getString("jLabel7.text"));
        jLabel7.setName("jLabel7");
        jLabel8.setText(resourceMap.getString("jLabel8.text"));
        jLabel8.setName("jLabel8");
        jTextField3.setText(resourceMap.getString("jTextField3.text"));
        jTextField3.setName("jTextField3");
        jTextField3.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField3FocusLost(evt);
            }
        });
        jTextField5.setText(resourceMap.getString("jTextField5.text"));
        jTextField5.setName("jTextField5");
        jTextField5.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField5FocusLost(evt);
            }
        });
        jTextField6.setText(resourceMap.getString("jTextField6.text"));
        jTextField6.setName("jTextField6");
        jTextField6.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField6FocusLost(evt);
            }
        });
        jLabel10.setText(resourceMap.getString("jLabel10.text"));
        jLabel10.setName("jLabel10");
        jLabel11.setText(resourceMap.getString("jLabel11.text"));
        jLabel11.setName("jLabel11");
        datePicker1.setName("datePicker1");
        datePicker1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                datePicker1ActionPerformed(evt);
            }
        });
        datePicker1.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                datePicker1FocusLost(evt);
            }
        });
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel1.border.title")));
        jPanel1.setName("jPanel1");
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));
        jButton1.setText(resourceMap.getString("jButton1.text"));
        jButton1.setName("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton3.setAction(actionMap.get("createNewPrice"));
        jButton3.setName("jButton3");
        jButton4.setAction(actionMap.get("editPrice"));
        jButton4.setName("jButton4");
        jButton5.setAction(actionMap.get("deletePrice"));
        jButton5.setName("jButton5");
        jButton6.setAction(actionMap.get("showPrintDialog"));
        jButton6.setName("jButton6");
        jButton7.setAction(actionMap.get("addPriceToPrint"));
        jButton7.setName("jButton7");
        jButton8.setAction(actionMap.get("print"));
        jButton8.setName("jButton8");
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText(resourceMap.getString("jLabel9.text"));
        jLabel9.setName("jLabel9");
        autoCompleteComboBox1.setEditable(true);
        autoCompleteComboBox1.setName("autoCompleteComboBox1");
        autoCompleteComboBox1.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                autoCompleteComboBox1ItemStateChanged(evt);
            }
        });
        autoCompleteComboBox1.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                autoCompleteComboBox1FocusLost(evt);
            }
        });
        autoCompleteComboBox1.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                autoCompleteComboBox1KeyPressed(evt);
            }
        });
        autoCompleteComboBox2.setEditable(true);
        autoCompleteComboBox2.setName("autoCompleteComboBox2");
        autoCompleteComboBox2.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                autoCompleteComboBox2ItemStateChanged(evt);
            }
        });
        autoCompleteComboBox3.setEditable(true);
        autoCompleteComboBox3.setName("autoCompleteComboBox3");
        autoCompleteComboBox3.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                autoCompleteComboBox3ItemStateChanged(evt);
            }
        });
        autoCompleteComboBox4.setEditable(true);
        autoCompleteComboBox4.setName("autoCompleteComboBox4");
        autoCompleteComboBox4.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                autoCompleteComboBox4ItemStateChanged(evt);
            }
        });
        autoCompleteComboBox5.setEditable(true);
        autoCompleteComboBox5.setName("autoCompleteComboBox5");
        autoCompleteComboBox5.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                autoCompleteComboBox5ItemStateChanged(evt);
            }
        });
        jMenuBar1.setName("jMenuBar1");
        jMenu1.setText(resourceMap.getString("jMenu1.text"));
        jMenu1.setName("jMenu1");
        jMenuItem4.setAction(actionMap.get("quit"));
        jMenuItem4.setText(resourceMap.getString("jMenuItem4.text"));
        jMenuItem4.setName("jMenuItem4");
        jMenu1.add(jMenuItem4);
        jMenuBar1.add(jMenu1);
        jMenu2.setText(resourceMap.getString("jMenu2.text"));
        jMenu2.setName("jMenu2");
        jMenuItem1.setAction(actionMap.get("dbPreferences1"));
        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text"));
        jMenuItem1.setName("jMenuItem1");
        jMenu2.add(jMenuItem1);
        jSeparator1.setName("jSeparator1");
        jMenu2.add(jSeparator1);
        jMenuItem3.setAction(actionMap.get("refreshData"));
        jMenuItem3.setText(resourceMap.getString("jMenuItem3.text"));
        jMenuItem3.setName("jMenuItem3");
        jMenu2.add(jMenuItem3);
        jMenuBar1.add(jMenu2);
        jMenu3.setText(resourceMap.getString("jMenu3.text"));
        jMenu3.setName("jMenu3");
        jMenuItem5.setAction(actionMap.get("showAboutDialog"));
        jMenuItem5.setText(resourceMap.getString("jMenuItem5.text"));
        jMenuItem5.setName("jMenuItem5");
        jMenu3.add(jMenuItem5);
        jMenuBar1.add(jMenu3);
        setJMenuBar(jMenuBar1);
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(45, 45, 45).add(jButton7).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jButton6).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 210, Short.MAX_VALUE).add(jButton8).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jButton3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jButton4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 28, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jButton5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 29, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(jLabel2).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(jLabel4).add(jLabel3).add(jLabel5).add(jLabel6).add(jLabel7)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))).add(layout.createSequentialGroup().add(jLabel1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))).add(10, 10, 10)).add(layout.createSequentialGroup().add(jLabel8).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(datePicker1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(jTextField5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE).add(2, 2, 2).add(jLabel11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jTextField6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(2, 2, 2).add(jLabel10)).add(jTextField3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE).add(autoCompleteComboBox1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE).add(autoCompleteComboBox2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE).add(autoCompleteComboBox3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE).add(autoCompleteComboBox4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE).add(autoCompleteComboBox5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))).add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE).add(layout.createSequentialGroup().add(jTextField1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jButton2)).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(jLabel9).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jButton2))).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel1).add(autoCompleteComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel2).add(autoCompleteComboBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel3).add(autoCompleteComboBox3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jTextField3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel4)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel5).add(autoCompleteComboBox4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel6).add(autoCompleteComboBox5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(11, 11, 11).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel10).add(jTextField6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jTextField5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel7).add(jLabel11)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(datePicker1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel8)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE))).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jButton5).add(jButton7).add(jButton8).add(jButton6)).add(jButton4).add(jButton3)).addContainerGap()));
        autoCompleteComboBox1.addComboBoxEditedListener(new ComboBoxEditedListener() {

            public void editValue(String text) {
                int conf = GUIUtils.showConfirmMessage("Вы действительно хотите добавить организацию '" + text + "'");
                if (conf == 0) {
                    try {
                        db.addOrganisation(text);
                        autoCompleteComboBox1.setModel(new PricerListModel(db.listOrganisations()));
                        selectingComboBoxValue(autoCompleteComboBox1, text);
                    } catch (DBException e) {
                        e.printStackTrace();
                        GUIUtils.showError(e.getMessage());
                    }
                }
            }
        });
        autoCompleteComboBox2.addComboBoxEditedListener(new ComboBoxEditedListener() {

            public void editValue(String text) {
                int conf = GUIUtils.showConfirmMessage("Вы действительно хотите добавить наименование '" + text + "'");
                if (conf == 0) {
                    try {
                        db.addTitleProduction(text);
                        autoCompleteComboBox2.setModel(new PricerListModel(db.listTitleProducts()));
                        selectingComboBoxValue(autoCompleteComboBox2, text);
                    } catch (DBException e) {
                        e.printStackTrace();
                        GUIUtils.showError(e.getMessage());
                    }
                }
            }
        });
        autoCompleteComboBox3.addComboBoxEditedListener(new ComboBoxEditedListener() {

            public void editValue(String text) {
                DBEntity ent1 = (DBEntity) autoCompleteComboBox1.getSelectedItem();
                DBEntity ent2 = (DBEntity) autoCompleteComboBox2.getSelectedItem();
                int conf = GUIUtils.showConfirmMessage("Вы действительно хотите добавить товар '" + text + "'\r\n" + "и соотнести его к '" + ent1.getString("name") + "' и '" + ent2.getString("name") + "'");
                if (conf == 0) {
                    try {
                        int id_org = ent1.getInt("id");
                        int id_type = ent2.getInt("id");
                        db.addProduction(id_org, id_type, text);
                        autoCompleteComboBox3.setModel(new PricerListModel(db.listProducts(id_org, id_type)));
                        selectingComboBoxValue(autoCompleteComboBox3, text);
                    } catch (RootException e) {
                        e.printStackTrace();
                        GUIUtils.showError(e.getMessage());
                    }
                }
            }
        });
        autoCompleteComboBox4.addComboBoxEditedListener(new ComboBoxEditedListener() {

            public void editValue(String text) {
                int conf = GUIUtils.showConfirmMessage("Вы действительно хотите добавить поставщика '" + text + "'");
                if (conf == 0) {
                    try {
                        db.addPostavchik(text);
                        autoCompleteComboBox4.setModel(new PricerListModel(db.listPostavchiki()));
                        selectingComboBoxValue(autoCompleteComboBox4, text);
                    } catch (DBException e) {
                        e.printStackTrace();
                        GUIUtils.showError(e.getMessage());
                    }
                }
            }
        });
        autoCompleteComboBox5.addComboBoxEditedListener(new ComboBoxEditedListener() {

            public void editValue(String text) {
                int conf = GUIUtils.showConfirmMessage("Вы действительно хотите добавить Цену за '" + text + "'");
                if (conf == 0) {
                    try {
                        db.addPriceFor(text);
                        autoCompleteComboBox5.setModel(new PricerListModel(db.listPricesFor()));
                        selectingComboBoxValue(autoCompleteComboBox5, text);
                    } catch (DBException e) {
                        e.printStackTrace();
                        GUIUtils.showError(e.getMessage());
                    }
                }
            }
        });
        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        new CoordinateDialog(this, true, priceList);
    }

    private void jTextField3FocusLost(java.awt.event.FocusEvent evt) {
        textFieldStateChangedAction(jTextField3, "otherinfo");
    }

    private void jTextField5FocusLost(java.awt.event.FocusEvent evt) {
        textFieldStateChangedAction(jTextField5, "pricerubl");
    }

    private void jTextField6FocusLost(java.awt.event.FocusEvent evt) {
        textFieldStateChangedAction(jTextField6, "pricecopeek");
    }

    private void datePicker1FocusLost(java.awt.event.FocusEvent evt) {
    }

    private void datePicker1ActionPerformed(java.awt.event.ActionEvent evt) {
        DrawObject obj = priceList.getDrawObjFromHash("date");
        if (obj != null) {
            obj.setDrawString(new SimpleDateFormat("dd.MM.yyyy").format(datePicker1.getDate()));
            priceList.rebuildLayer();
        }
    }

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {
        if (KeyEvent.VK_ENTER == evt.getKeyCode()) {
            jButton2.doClick();
        }
        jTextField1.requestFocus();
    }

    private void autoCompleteComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            Object org = autoCompleteComboBox1.getSelectedItem();
            autoCompleteComboBox1.getEditor().setItem(org);
            Object type = autoCompleteComboBox2.getSelectedItem();
            if (org != null && type != null) {
                try {
                    autoCompleteComboBox3.setModel(new PricerListModel(db.listProducts(((DBEntity) org).getInt("id"), ((DBEntity) type).getInt("id"))));
                } catch (RootException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    GUIUtils.showError(ex.getMessage());
                }
            }
        }
        comboStateChangedAction(evt, "organisationname");
    }

    private void autoCompleteComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            Object org = autoCompleteComboBox1.getSelectedItem();
            Object type = autoCompleteComboBox2.getSelectedItem();
            autoCompleteComboBox2.getEditor().setItem(type);
            if (org != null && type != null) {
                try {
                    autoCompleteComboBox3.setModel(new PricerListModel(db.listProducts(((DBEntity) org).getInt("id"), ((DBEntity) type).getInt("id"))));
                } catch (RootException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    GUIUtils.showError(ex.getMessage());
                }
            }
        }
        comboStateChangedAction(evt, "titleproduct");
    }

    private void autoCompleteComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            autoCompleteComboBox3.getEditor().setItem(autoCompleteComboBox3.getSelectedItem());
        }
        comboStateChangedAction(evt, "nameproduct");
    }

    private void autoCompleteComboBox1FocusLost(java.awt.event.FocusEvent evt) {
    }

    private void autoCompleteComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            autoCompleteComboBox4.getEditor().setItem(autoCompleteComboBox4.getSelectedItem());
        }
        comboStateChangedAction(evt, "postavchikname");
    }

    private void autoCompleteComboBox1KeyPressed(java.awt.event.KeyEvent evt) {
    }

    private void autoCompleteComboBox5ItemStateChanged(java.awt.event.ItemEvent evt) {
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            autoCompleteComboBox5.getEditor().setItem(autoCompleteComboBox5.getSelectedItem());
        }
        comboStateChangedAction(evt, "price");
    }

    private void textFieldStateChangedAction(JTextField tf, String name) {
        DrawObject obj = priceList.getDrawObjFromHash(name);
        if (obj != null) {
            obj.setDrawString(tf.getText());
            priceList.rebuildLayer();
        }
    }

    private void comboStateChangedAction(ItemEvent evt, String name) {
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            DrawObject obj = priceList.getDrawObjFromHash(name);
            if (obj != null) {
                obj.setDrawString(((DBEntity) evt.getItem()).getString("name"));
                priceList.rebuildLayer();
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    @Action
    public void search() {
        try {
            GUIUtils.setBusyCursor(this);
            Vector<DBEntity> data = db.searchPriceLists(jTextField1.getText());
            jTable1.setModel(new PricerListTableModel(data));
            jLabel9.setText("Найдено: " + jTable1.getRowCount());
        } catch (DBException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            jLabel9.setText("Найдено: ?");
            GUIUtils.showError(ex.getMessage());
        } finally {
            GUIUtils.setNormalCursor(this);
        }
    }

    @Action
    public void createNewPrice() {
        try {
            DBEntity e = new DBEntity();
            e.setField("id_org", ((DBEntity) autoCompleteComboBox1.getSelectedItem()).getInt("id"));
            e.setField("id_pt", ((DBEntity) autoCompleteComboBox2.getSelectedItem()).getInt("id"));
            e.setField("id_post", ((DBEntity) autoCompleteComboBox4.getSelectedItem()).getInt("id"));
            e.setField("id_price_for", ((DBEntity) autoCompleteComboBox5.getSelectedItem()).getInt("id"));
            e.setField("id_prod", ((DBEntity) autoCompleteComboBox3.getSelectedItem()).getInt("id"));
            e.setField("rubl", new Integer(jTextField5.getText()));
            e.setField("copeek", jTextField6.getText());
            e.setField("date", datePicker1.getDate());
            e.setField("other", jTextField3.getText());
            e.setField("xml", priceList.makeXML());
            db.addPricer(e);
            GUIUtils.showInfo("Ценник был успешно добавлен.");
        } catch (RootException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            GUIUtils.showError(ex.getMessage());
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            GUIUtils.showError("Рубли должны вводится в числовом формате.");
        } catch (Throwable t) {
            t.printStackTrace();
            GUIUtils.showError("Ошибка, проверьте данные.");
        }
    }

    @Action
    public void editPrice() {
        int[] sel = jTable1.getSelectedRows();
        if (sel.length == 0) {
            GUIUtils.showError("Выберите ценник, который нужно отредактировать.");
            return;
        }
        if (sel.length > 1) {
            GUIUtils.showError("Необходимо выбрать только один ценник для редактирования.");
            return;
        }
        PricerListTableModel model = (PricerListTableModel) jTable1.getModel();
        try {
            DBEntity e = new DBEntity();
            e.setField("id", model.getData().get(sel[0]).getInt("id"));
            e.setField("id_org", ((DBEntity) autoCompleteComboBox1.getSelectedItem()).getInt("id"));
            e.setField("id_pt", ((DBEntity) autoCompleteComboBox2.getSelectedItem()).getInt("id"));
            e.setField("id_post", ((DBEntity) autoCompleteComboBox4.getSelectedItem()).getInt("id"));
            e.setField("id_price_for", ((DBEntity) autoCompleteComboBox5.getSelectedItem()).getInt("id"));
            e.setField("id_prod", ((DBEntity) autoCompleteComboBox3.getSelectedItem()).getInt("id"));
            e.setField("rubl", new Integer(jTextField5.getText()));
            e.setField("copeek", jTextField6.getText());
            e.setField("date", datePicker1.getDate());
            e.setField("other", jTextField3.getText());
            e.setField("xml", priceList.makeXML());
            db.updatePricer(e);
            GUIUtils.showInfo("Ценник был успешно обновлен.");
        } catch (RootException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            GUIUtils.showError(ex.getMessage());
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            GUIUtils.showError("Рубли должны вводится в числовом формате.");
        }
    }

    @Action
    public void deletePrice() {
        int[] sel = jTable1.getSelectedRows();
        if (sel.length > 0) {
            if (GUIUtils.showConfirmMessage("Вы действительно хотите удалить выбранные ценники?\r\n" + "Выбрано записей: " + sel.length) == 0) {
                try {
                    int[] ids = new int[sel.length];
                    for (int i = 0; i < sel.length; i++) {
                        try {
                            PricerListTableModel model = (PricerListTableModel) jTable1.getModel();
                            ids[i] = model.getData().get(sel[i]).getInt("id");
                        } catch (RootException ex) {
                            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    db.deletePricers(ids);
                    GUIUtils.showInfo("Выбранные ценники были успешно удалены.");
                    search();
                } catch (DBException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    GUIUtils.showError(ex.getMessage());
                }
            }
        }
    }

    public void rebuildMainFrameInfo() {
        int sel = jTable1.getSelectedRow();
        if (sel != -1) {
            try {
                PricerListTableModel model = (PricerListTableModel) jTable1.getModel();
                DBEntity e = db.getPricerObj((Integer) model.getData().get(sel).getInt("id"));
                settingJcomboBox(autoCompleteComboBox1, e.getInt("id_organisation"));
                settingJcomboBox(autoCompleteComboBox2, e.getInt("id_production_type"));
                settingJcomboBox(autoCompleteComboBox3, e.getInt("id_production"));
                settingJcomboBox(autoCompleteComboBox4, e.getInt("id_postavchik"));
                settingJcomboBox(autoCompleteComboBox5, e.getInt("id_price_for"));
                jTextField3.setText(e.getString("other_information"));
                jTextField5.setText(e.getString("rubl"));
                jTextField6.setText(e.getString("copeek"));
                try {
                    try {
                        datePicker1.setDate(SQLiteDBOperationsImpl.df.parse(e.getString("price_date")));
                    } catch (ParseException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (PropertyVetoException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                priceList.buildView(e.getString("xml"));
            } catch (RootException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                GUIUtils.showError(ex.getMessage());
            }
        }
    }

    private void settingJcomboBox(JComboBox combo, int id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            Object obj = combo.getItemAt(i);
            if (obj instanceof DBEntity) {
                try {
                    DBEntity item = (DBEntity) obj;
                    if (item.getInt("id") == id) {
                        combo.setSelectedIndex(i);
                        combo.repaint();
                        break;
                    }
                } catch (RootException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void selectingComboBoxValue(JComboBox combo, String text) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            Object obj = combo.getItemAt(i);
            if (obj instanceof DBEntity) {
                DBEntity item = (DBEntity) obj;
                if (item.getString("name").equals(text)) {
                    combo.setSelectedIndex(i);
                    combo.requestFocus();
                    break;
                }
            }
        }
    }

    private class PricerListModel extends AbstractListModel implements ComboBoxModel {

        private Vector<DBEntity> data = null;

        private DBEntity selectedItem = null;

        public PricerListModel(Vector<DBEntity> data) {
            this.data = data;
        }

        public int getSize() {
            if (data != null) {
                return data.size();
            }
            return 0;
        }

        public Object getElementAt(int index) {
            if (data == null) {
                return null;
            }
            return data.get(index);
        }

        public void setSelectedItem(Object anItem) {
            if (anItem instanceof DBEntity) {
                selectedItem = (DBEntity) anItem;
            } else {
                selectedItem = null;
            }
        }

        public Object getSelectedItem() {
            return selectedItem;
        }
    }

    @Action
    public void dbPreferences1() {
        new ProductionDialog(null, true, db);
        refreshData();
    }

    @Action
    public void dbPreferences2() {
        new OthersDialog(null, true, db);
    }

    @Action
    public void refreshData() {
        try {
            String[] values = new String[5];
            Object selected = autoCompleteComboBox1.getSelectedItem();
            values[0] = selected == null ? "unknown" : selected.toString();
            selected = autoCompleteComboBox2.getSelectedItem();
            values[1] = selected == null ? "unknown" : selected.toString();
            selected = autoCompleteComboBox3.getSelectedItem();
            values[2] = selected == null ? "unknown" : selected.toString();
            selected = autoCompleteComboBox4.getSelectedItem();
            values[3] = selected == null ? "unknown" : selected.toString();
            selected = autoCompleteComboBox5.getSelectedItem();
            values[4] = selected == null ? "unknown" : selected.toString();
            initModels();
            if (values[0].equals("unknown")) {
                autoCompleteComboBox1.setSelectedIndex(autoCompleteComboBox1.getItemCount() > 0 ? 0 : -1);
            } else {
                selectingComboBoxValue(autoCompleteComboBox1, values[0]);
            }
            if (values[1].equals("unknown")) {
                autoCompleteComboBox2.setSelectedIndex(autoCompleteComboBox2.getItemCount() > 0 ? 0 : -1);
            } else {
                selectingComboBoxValue(autoCompleteComboBox2, values[1]);
            }
            if (values[2].equals("unknown")) {
                autoCompleteComboBox3.setSelectedIndex(autoCompleteComboBox3.getItemCount() > 0 ? 0 : -1);
            } else {
                selectingComboBoxValue(autoCompleteComboBox3, values[2]);
            }
            if (values[3].equals("unknown")) {
                autoCompleteComboBox4.setSelectedIndex(autoCompleteComboBox4.getItemCount() > 0 ? 0 : -1);
            } else {
                selectingComboBoxValue(autoCompleteComboBox4, values[3]);
            }
            if (values[4].equals("unknown")) {
                autoCompleteComboBox5.setSelectedIndex(autoCompleteComboBox5.getItemCount() > 0 ? 0 : -1);
            } else {
                selectingComboBoxValue(autoCompleteComboBox5, values[4]);
            }
        } catch (InitException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            GUIUtils.showError(ex.getMessage());
        }
    }

    private class PricerListTableModel extends AbstractTableModel {

        private Vector<DBEntity> data = null;

        private String[] colNames = { "Ценник" };

        public PricerListTableModel(Vector<DBEntity> data) {
            this.data = data;
        }

        public int getRowCount() {
            if (data == null) {
                return 0;
            }
            return data.size();
        }

        public int getColumnCount() {
            return colNames.length;
        }

        public Vector<DBEntity> getData() {
            return data;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            DBEntity e = data.get(rowIndex);
            switch(columnIndex) {
                case 0:
                    return e.getString("name");
                default:
                    return "unknown";
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public String getColumnName(int column) {
            return colNames[column];
        }
    }

    @Action
    public void showPrintDialog() {
        if (printDialog.isVisible()) {
        } else {
            printDialog.setVisible(true);
        }
        printDialog.requestFocus();
    }

    @Action
    public void addPriceToPrint() {
        DBEntity ent1 = (DBEntity) autoCompleteComboBox1.getSelectedItem();
        if (ent1 == null) {
            GUIUtils.showError("Организация не выбрана");
            return;
        }
        DBEntity ent2 = (DBEntity) autoCompleteComboBox2.getSelectedItem();
        if (ent2 == null) {
            GUIUtils.showError("Наименование не выбрано");
            return;
        }
        DBEntity ent3 = (DBEntity) autoCompleteComboBox3.getSelectedItem();
        if (ent3 == null) {
            GUIUtils.showError("Название не выбрано");
            return;
        }
        DBEntity ent4 = (DBEntity) autoCompleteComboBox4.getSelectedItem();
        if (ent4 == null) {
            GUIUtils.showError("Поставщик не выбран");
            return;
        }
        DBEntity ent5 = (DBEntity) autoCompleteComboBox5.getSelectedItem();
        if (ent5 == null) {
            GUIUtils.showError("'Цена за' не выбрана");
            return;
        }
        ComboIDs ids;
        try {
            ids = new ComboIDs(ent1.getInt("id"), ent2.getInt("id"), ent3.getInt("id"), ent4.getInt("id"), ent5.getInt("id"), datePicker1.getDate());
        } catch (RootException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            GUIUtils.showError(ex.getMessage());
            return;
        }
        PriceList pl = (PriceList) priceList.clone();
        pl.setComboIDs(ids);
        printDialog.addPrintPanel(pl);
        if (printDialog.isVisible()) {
        } else {
            printDialog.setVisible(true);
        }
        printDialog.requestFocus();
    }

    @Action
    public void print() {
        PrintUtilities.printComponent(priceListPanel, OrientationRequested.PORTRAIT);
    }

    @Action
    public void quit() {
        dispose();
    }

    @Action
    public void showAboutDialog() {
        if (aboutBox == null) {
            aboutBox = new PriceMakerAboutBox(this);
            aboutBox.setLocationRelativeTo(this);
        }
        PriceMakerApp.getApplication().show(aboutBox);
    }

    @Action
    public void select() {
        autoCompleteComboBox1.setSelectedIndex(25);
    }

    private org.plotnikov.pricemaker.gui.utils.AutoCompleteComboBox autoCompleteComboBox1;

    private org.plotnikov.pricemaker.gui.utils.AutoCompleteComboBox autoCompleteComboBox2;

    private org.plotnikov.pricemaker.gui.utils.AutoCompleteComboBox autoCompleteComboBox3;

    private org.plotnikov.pricemaker.gui.utils.AutoCompleteComboBox autoCompleteComboBox4;

    private org.plotnikov.pricemaker.gui.utils.AutoCompleteComboBox autoCompleteComboBox5;

    private com.michaelbaranov.microba.calendar.DatePicker datePicker1;

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JButton jButton3;

    private javax.swing.JButton jButton4;

    private javax.swing.JButton jButton5;

    private javax.swing.JButton jButton6;

    private javax.swing.JButton jButton7;

    private javax.swing.JButton jButton8;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JMenu jMenu1;

    private javax.swing.JMenu jMenu2;

    private javax.swing.JMenu jMenu3;

    private javax.swing.JMenuBar jMenuBar1;

    private javax.swing.JMenuItem jMenuItem1;

    private javax.swing.JMenuItem jMenuItem3;

    private javax.swing.JMenuItem jMenuItem4;

    private javax.swing.JMenuItem jMenuItem5;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JTable jTable1;

    private javax.swing.JTextField jTextField1;

    private javax.swing.JTextField jTextField3;

    private javax.swing.JTextField jTextField5;

    private javax.swing.JTextField jTextField6;
}
