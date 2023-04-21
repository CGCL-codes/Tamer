package org.paccman.ui;

/**
 *
 * @author  joao
 */
public class ExceptionFrame extends javax.swing.JFrame {

    /** Creates new form ExceptionFrame */
    public ExceptionFrame() {
        initComponents();
    }

    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        exceptionStackTraceTxt = new javax.swing.JTextArea();
        closeBtn = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        exceptionStackTraceTxt.setColumns(20);
        exceptionStackTraceTxt.setRows(5);
        jScrollPane1.setViewportView(exceptionStackTraceTxt);
        closeBtn.setText("Close");
        closeBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeBtnActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE).addComponent(closeBtn, javax.swing.GroupLayout.Alignment.TRAILING)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(closeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        pack();
    }

    public static void showException(Exception e) {
        ExceptionFrame ef = new ExceptionFrame();
        ef.setException(e);
        ef.setAlwaysOnTop(true);
        ef.setVisible(true);
    }

    private void setException(Exception e) {
        StringBuffer sb = new StringBuffer();
        sb.append("Exception caught\n");
        for (StackTraceElement s : e.getStackTrace()) {
            sb.append("\t");
            sb.append(s);
            sb.append("\n");
        }
        exceptionStackTraceTxt.setText(sb.toString());
    }

    private void closeBtnActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
    }

    public javax.swing.JButton closeBtn;

    public javax.swing.JTextArea exceptionStackTraceTxt;

    public javax.swing.JScrollPane jScrollPane1;
}
