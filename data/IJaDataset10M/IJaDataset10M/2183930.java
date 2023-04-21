package interpreter;

import java.awt.Image;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author  marcin
 */
public class snake extends javax.swing.JFrame {

    String tekst = "";

    /** Creates new form snake */
    public snake() {
        initComponents();
    }

    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setFocusable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);
        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);
        jMenu1.setText("Menu");
        jMenuItem1.setText("wczytaj");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);
        jMenuItem2.setText("analizuj");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);
        jMenuItem3.setText("koniec");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);
        jMenuBar1.add(jMenu1);
        setJMenuBar(jMenuBar1);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)));
        pack();
    }

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {
        int liczba = tekst.length();
        for (int i = 0; i < liczba; i++) {
            if (tekst.charAt(i) == 'f' && tekst.charAt(i + 1) == 'o' && tekst.charAt(i + 2) == 'r') {
                jTextArea2.setText("dobrze");
            }
        }
    }

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {
        int wynik;
        wynik = JOptionPane.showConfirmDialog(this, "Czy na pewno zako�czy� dzia�anie?", "Pytanie", JOptionPane.YES_NO_OPTION);
        if (wynik == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
        Image img;
        JFileChooser wybor = new javax.swing.JFileChooser();
        int wynik = wybor.showOpenDialog(this);
        File plik = wybor.getSelectedFile();
        System.out.println(plik);
        String g = plik.toString();
        File inputFile = new File(g);
        String s = "";
        try {
            FileReader we = new FileReader(inputFile);
            BufferedReader w = new BufferedReader(we);
            while ((s = w.readLine()) != null) {
                tekst += s + "\n";
            }
            jTextArea1.setText(tekst);
            we.close();
        } catch (Exception e) {
            System.out.println("Blad operacji odczytu");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new snake().setVisible(true);
            }
        });
    }

    private javax.swing.JMenu jMenu1;

    private javax.swing.JMenuBar jMenuBar1;

    private javax.swing.JMenuItem jMenuItem1;

    private javax.swing.JMenuItem jMenuItem2;

    private javax.swing.JMenuItem jMenuItem3;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JTextArea jTextArea1;

    private javax.swing.JTextArea jTextArea2;
}
