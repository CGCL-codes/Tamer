package com.bkhn.ltnc.vnetsim.ui;

import java.awt.Dimension;
import javax.swing.*;
import java.awt.BorderLayout;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import javax.swing.GroupLayout.Alignment;
import com.bkhn.ltnc.vnetsim.myobjects.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

public class frmCosts extends JFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public JTable tblCost;

    public String[] columnNames;

    public Object[][] data;

    public frmCosts(Graph myGraph) {
        super("Bảng ma trận giá liên kết");
        setIconImage(Toolkit.getDefaultToolkit().getImage(frmCosts.class.getResource("/VnetsimIcons/iconCost.png")));
        setSize(new Dimension(1200, 700));
        JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        int side = myGraph.getNumberOfVertices();
        String[][] data = new String[side][side + 1];
        String[] columnNames = new String[side + 1];
        columnNames[0] = "X";
        int countColumn = 0;
        for (Vertex node : myGraph.getVertices()) {
            columnNames[countColumn + 1] = "V" + node.getId();
            int countRow = 0;
            data[countColumn][countRow] = "V" + node.getId();
            for (Vertex target : myGraph.getVertices()) {
                String text;
                int traffic = 0;
                if (node.equals(target)) text = "-"; else {
                    traffic = myGraph.getEdge(node, target).getCost();
                    text = "" + traffic;
                }
                data[countColumn][countRow + 1] = text;
                countRow++;
            }
            countColumn++;
        }
        tblCost = new JTable(data, columnNames) {

            /**
			 * 
			 */
            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        TableColumn column = null;
        for (int i = 0; i <= side; i++) {
            column = tblCost.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(100);
            } else {
                column.setPreferredWidth(50);
            }
        }
        scrollPane.setViewportView(tblCost);
        JPanel pnlcontrol = new JPanel();
        pnlcontrol.setBorder(new TitledBorder(null, "Control Panel", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        pnlcontrol.setPreferredSize(new Dimension(100, 600));
        getContentPane().add(pnlcontrol, BorderLayout.EAST);
        JButton btnUnlock = new JButton("Unlock");
        btnUnlock.setEnabled(false);
        JButton btnOk = new JButton("OK");
        btnOk.setEnabled(false);
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                frmCosts.this.setVisible(false);
            }
        });
        GroupLayout gl_pnlcontrol = new GroupLayout(pnlcontrol);
        gl_pnlcontrol.setHorizontalGroup(gl_pnlcontrol.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING, gl_pnlcontrol.createSequentialGroup().addContainerGap().addGroup(gl_pnlcontrol.createParallelGroup(Alignment.LEADING).addComponent(btnCancel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE).addComponent(btnUnlock, GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE).addComponent(btnOk, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)).addContainerGap()));
        gl_pnlcontrol.setVerticalGroup(gl_pnlcontrol.createParallelGroup(Alignment.LEADING).addGroup(gl_pnlcontrol.createSequentialGroup().addGap(31).addComponent(btnUnlock, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE).addGap(28).addComponent(btnOk, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE).addGap(29).addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE).addContainerGap(349, Short.MAX_VALUE)));
        pnlcontrol.setLayout(gl_pnlcontrol);
    }
}
