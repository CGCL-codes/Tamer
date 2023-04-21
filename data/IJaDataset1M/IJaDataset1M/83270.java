package tabela;

import java.awt.*;
import javax.swing.*;
import java.sql.*;
import java.util.HashMap;

public class Tabela extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JButton jButton1 = null;

    private JButton jButton2 = null;

    public Tabela(HashMap<String, String> propriedades) {
        super();
        initialize(propriedades);
    }

    private void initialize(HashMap<String, String> propriedades) {
        this.setSize(600, 400);
        this.setContentPane(getJContentPane(propriedades));
        this.setTitle("Tabela");
        this.setResizable(false);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(200, 200, 200));
    }

    private JPanel getJContentPane(HashMap<String, String> propriedades) {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            JTable jTable1 = new JTable(5, 5);
            jTable1.setVisible(true);
            jTable1.getTableHeader().setReorderingAllowed(false);
            jTable1.getTableHeader().setResizingAllowed(false);
            jTable1.getTableHeader().setBackground(Color.lightGray);
            jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTable1.setGridColor(Color.red);
            jTable1.setShowHorizontalLines(true);
            jTable1.setShowVerticalLines(true);
            jTable1.setEnabled(true);
            JScrollPane scrollPane1 = new JScrollPane(jTable1);
            scrollPane1.setVisible(true);
            scrollPane1.setBounds(20, 20, 300, 300);
            scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            jContentPane.add(scrollPane1, null);
            jContentPane.add(getJButton1());
            jContentPane.add(getJButton2(propriedades));
        }
        return jContentPane;
    }

    private JButton getJButton1() {
        if (jButton1 == null) {
            jButton1 = new JButton();
            jButton1.setVisible(true);
            jButton1.setText("limpar tabela");
            jButton1.setBounds(20, 330, 200, 25);
            jButton1.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent e) {
                    JOptionPane.showMessageDialog(null, "tabela limpa", "Operador", JOptionPane.INFORMATION_MESSAGE);
                }
            });
        }
        return jButton1;
    }

    private JButton getJButton2(HashMap<String, String> propriedades) {
        if (jButton2 == null) {
            jButton2 = new JButton();
            jButton2.setVisible(true);
            jButton2.setText("atualizar tabela");
            jButton2.setBounds(250, 330, 200, 25);
            final String DRIVER = propriedades.get("driver");
            final String URL = propriedades.get("url");
            final String USER = propriedades.get("user");
            final String PW = propriedades.get("pw");
            final String TABLE = propriedades.get("table");
            jButton2.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent e) {
                    String codigo = null;
                    String descricao = null;
                    String tipo = null;
                    try {
                        Class.forName(DRIVER);
                        Connection con = DriverManager.getConnection(URL, USER, PW);
                        Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        ResultSet resul = stmt.executeQuery(" select * from " + TABLE + ";");
                        if (resul.next()) {
                            codigo = resul.getString(1);
                            descricao = resul.getString(2);
                            tipo = resul.getString(3);
                            codigo = codigo.trim();
                            descricao = descricao.trim();
                            tipo = tipo.trim();
                        }
                        con.close();
                        stmt.close();
                    } catch (Exception ee) {
                        JOptionPane.showMessageDialog(null, "Erro !\n" + ee.getMessage(), "Operador", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    JOptionPane.showMessageDialog(null, "tabela atualizada", "Operador", JOptionPane.INFORMATION_MESSAGE);
                }
            });
        }
        return jButton2;
    }
}
