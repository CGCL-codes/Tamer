package Interfaz;

import java.util.Vector;
import ingredientsClient.Consumer;
import Modelo.Busqueda;
import Modelo.Util;
import Modelo.FileXML;
import Modelo.Ingredient;
import java.io.File;

/**
 *
 * @author  MiNiNo
 */
public class WizardIngredientGui extends javax.swing.JFrame {

    private Vector tags = new Vector();

    private Busqueda resultados;

    public WizardIngredientGui() {
        initComponents();
        jTabbedPane1.setEnabledAt(1, false);
    }

    private void initComponents() {
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jTxtTag = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListTags = new javax.swing.JList();
        jBtnNext = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListResult = new javax.swing.JList();
        jBtnNext2 = new javax.swing.JButton();
        jBtnNew = new javax.swing.JButton();
        jBtnView = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jBtnBack = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Wizard to Create a New Ingredient");
        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/logo01.jpg")));
        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Welcome to Fresita Wizard!");
        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Please enter some related tags about your Ingredient...");
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Enter Tags:");
        jTxtTag.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTxtTagKeyPressed(evt);
            }
        });
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("List of Tags:");
        jListTags.setEnabled(false);
        jScrollPane1.setViewportView(jListTags);
        jBtnNext.setText("Next >");
        jBtnNext.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnNextActionPerformed(evt);
            }
        });
        jButton1.setText("Remove Last");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jButton1).addComponent(jLabel1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE).addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE).addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE).addComponent(jLabel5).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jTxtTag, javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE).addComponent(jBtnNext)).addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE).addComponent(jButton1)).addGroup(jPanel1Layout.createSequentialGroup().addGap(11, 11, 11).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup().addGap(6, 6, 6).addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTxtTag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)).addComponent(jBtnNext)))).addContainerGap()));
        jTabbedPane1.addTab("Step 1", jPanel1);
        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Please select if there is any existing Ingredient that");
        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("will be useful for your purpose.");
        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Or select New to Create it from the bigginig.");
        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12));
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Results:");
        jListResult.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(jListResult);
        jBtnNext2.setText("Next >");
        jBtnNext2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnNext2ActionPerformed(evt);
            }
        });
        jBtnNew.setText("New");
        jBtnNew.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnNewActionPerformed(evt);
            }
        });
        jBtnView.setText("Quick View From Selected");
        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jBtnBack.setText("< Back");
        jBtnBack.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnBackActionPerformed(evt);
            }
        });
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/logo01.jpg")));
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jBtnBack).addComponent(jBtnView)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE).addComponent(jLabel10).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE).addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE).addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE).addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE))).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(jBtnNew).addGap(25, 25, 25).addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBtnNext2))).addContainerGap()).addComponent(jLabel6));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jBtnView, javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(jLabel7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel8).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel9).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel10).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jBtnNext2).addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jBtnNew).addComponent(jBtnBack))).addContainerGap()).addComponent(jLabel6));
        jTabbedPane1.addTab("Step 2", jPanel2);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE));
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 528) / 2, (screenSize.height - 414) / 2, 528, 414);
    }

    private void jBtnNext2ActionPerformed(java.awt.event.ActionEvent evt) {
        if (!jListResult.isSelectionEmpty()) {
            int id = Integer.parseInt((String) resultados.getIds().get(jListResult.getSelectedIndex()));
            Consumer c = new Consumer();
            FileXML fxml = FileXML.fromXml(c.getFile(id));
            Ingredient ing = Ingredient.fromXml(fxml.getXml());
            IngredientsGui ingredient = new IngredientsGui(ing);
            ingredient.setVisible(true);
            dispose();
        } else {
            Util.errorDialog("No Ingredient", "There is not an Ingredient Selected.");
        }
    }

    private void jBtnNewActionPerformed(java.awt.event.ActionEvent evt) {
        IngredientsGui ingredient = new IngredientsGui();
        ingredient.setVisible(true);
        dispose();
    }

    private void jBtnBackActionPerformed(java.awt.event.ActionEvent evt) {
        jTabbedPane1.setEnabledAt(0, true);
        jTabbedPane1.setEnabledAt(1, false);
        jTabbedPane1.setSelectedIndex(0);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        tags.removeElementAt(tags.size() - 1);
        jListTags.setListData(tags);
        jListTags.setSelectionInterval(0, tags.size() - 1);
    }

    private void jBtnNextActionPerformed(java.awt.event.ActionEvent evt) {
        jTabbedPane1.setEnabledAt(1, true);
        jTabbedPane1.setEnabledAt(0, false);
        jTabbedPane1.setSelectedIndex(1);
        if (tags.size() == 0) {
            jBtnNext2.setEnabled(false);
            jBtnView.setEnabled(false);
        } else {
            Consumer c = new Consumer();
            this.resultados = c.busqueda(tags);
            if (resultados != null) {
                try {
                    jListResult.setListData(resultados.getTitulos());
                } catch (Exception e) {
                    jListResult.setListData(new Vector());
                }
            } else {
                Util.informDialog("No results", "No search results founded for provided tags ");
            }
        }
    }

    private void jTxtTagKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyChar() == '\n') {
            tags.addElement(jTxtTag.getText().toLowerCase());
            jListTags.setListData(tags);
            jTxtTag.setText("");
            jTxtTag.requestFocus();
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new WizardIngredientGui().setVisible(true);
            }
        });
    }

    private javax.swing.JButton jBtnBack;

    private javax.swing.JButton jBtnNew;

    private javax.swing.JButton jBtnNext;

    private javax.swing.JButton jBtnNext2;

    private javax.swing.JButton jBtnView;

    private javax.swing.JButton jButton1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JList jListResult;

    private javax.swing.JList jListTags;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JSeparator jSeparator2;

    private javax.swing.JSeparator jSeparator3;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JTextField jTxtTag;
}
