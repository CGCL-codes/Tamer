package org.statcato.dialogs.data;

import org.statcato.*;
import org.statcato.random.IntegerSample;
import java.util.Vector;

/**
 * A dialog for generating random samples from an integer distribution.
 * 
 * @author  Margaret Yau
 * @version %I%, %G%
 * @see org.statcato.random.IntegerSample
 * @since 1.0
 */
public class IntegerSampleDialog extends StatcatoDialog {

    /** Creates new form IntegerSampleDialog */
    public IntegerSampleDialog(java.awt.Frame parent, boolean modal, Statcato app) {
        super(parent, modal);
        ParentSpreadsheet = app.getSpreadsheet();
        this.app = app;
        initComponents();
        customInitComponents();
        setHelpFile("data-random-integer");
        name = "Random Integer Samples";
        description = "For generating random samples from a range of integer " + "values, each of which having the same probability of being chosen.";
        helpStrings.add("Specify the column name(s) in which samples will " + "be stored, the sample size, and the minimum and maximum " + "values of the range of possible values.");
    }

    public void customInitComponents() {
        getRootPane().setDefaultButton(OKButton);
    }

    private void initComponents() {
        OKButton = new javax.swing.JButton();
        CancelButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        StoreTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        SeedTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        MinTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        nTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        MaxTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Random Integer Samples");
        OKButton.setText("OK");
        OKButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKButtonActionPerformed(evt);
            }
        });
        CancelButton.setText("Cancel");
        CancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelButtonActionPerformed(evt);
            }
        });
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Results"));
        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel3.setText("Store Samples in:");
        jLabel5.setText("<html>\n- Enter valid column names separated by space.<br>\nFor a continuous range of columns, separate using dash (e.g. C1-C30).<br>\n- The random sampling process is repeated for each column.\n</html>\n");
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(StoreTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jLabel5)).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(StoreTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel5).addContainerGap()));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Options"));
        jLabel2.setText("for each column");
        jLabel4.setText("Random Generator Seed (optional):");
        jLabel7.setText("Maximum:");
        jLabel6.setText("Minimum:");
        jLabel1.setText("Number of Samples to Generate:");
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(188, 188, 188).addComponent(nTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jLabel1)).addGap(10, 10, 10).addComponent(jLabel2)).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel7).addComponent(jLabel6)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addComponent(MaxTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE).addGap(4, 4, 4)).addComponent(MinTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jLabel4).addGap(17, 17, 17).addComponent(SeedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { MaxTextField, MinTextField });
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(nTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel6).addComponent(MinTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel7).addComponent(MaxTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(30, 30, 30).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(SeedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))).addGroup(layout.createSequentialGroup().addGap(121, 121, 121).addComponent(OKButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(CancelButton))).addContainerGap()));
        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { CancelButton, OKButton });
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(OKButton).addComponent(CancelButton)).addContainerGap()));
        pack();
    }

    private void OKButtonActionPerformed(java.awt.event.ActionEvent evt) {
        app.compoundEdit = new DialogEdit("integer samples");
        int n = 0, min = 0, max = 0;
        try {
            n = Integer.parseInt(nTextField.getText());
            if (n <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            app.showErrorDialog("Enter a valid sample size.");
            return;
        }
        try {
            min = Integer.parseInt(MinTextField.getText());
        } catch (NumberFormatException e) {
            app.showErrorDialog("Enter a valid minimum integer value.");
            return;
        }
        try {
            max = Integer.parseInt(MaxTextField.getText());
        } catch (NumberFormatException e) {
            app.showErrorDialog("Enter a valid maximum integer value.");
            return;
        }
        if (min > max) {
            int temp = max;
            max = min;
            min = temp;
        }
        int storeColumnNum;
        String StoreColumn = StoreTextField.getText();
        Vector<Integer> nums = ParentSpreadsheet.getColumnNumbersFromString(StoreColumn);
        if (nums == null) {
            app.showErrorDialog("Invalid column(s) for storing results.");
            return;
        }
        if (nums != null) {
            for (int i = 0; i < nums.size(); ++i) {
                storeColumnNum = nums.elementAt(i).intValue();
                IntegerSample sampler = new IntegerSample(min, max);
                try {
                    long seed = Long.parseLong(SeedTextField.getText());
                    sampler.setSeed(seed);
                } catch (NumberFormatException e) {
                }
                Vector<String> results = new Vector<String>();
                for (int j = 0; j < n; ++j) {
                    int sample = sampler.nextSample();
                    results.addElement(sample + "");
                }
                ParentSpreadsheet.setColumn(storeColumnNum, results);
            }
            app.addLogParagraph("Integer Samples [" + min + ", " + max + "]", n + " samples stored in each of " + StoreColumn);
            app.compoundEdit.end();
            app.addCompoundEdit(app.compoundEdit);
            setVisible(false);
        }
    }

    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
    }

    private javax.swing.JButton CancelButton;

    private javax.swing.JTextField MaxTextField;

    private javax.swing.JTextField MinTextField;

    private javax.swing.JButton OKButton;

    private javax.swing.JTextField SeedTextField;

    private javax.swing.JTextField StoreTextField;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JTextField nTextField;
}
