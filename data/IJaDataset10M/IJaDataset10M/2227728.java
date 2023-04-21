package org.adempierelbr.apps.form;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import org.adempierelbr.util.ExportUtil;
import org.compiere.apps.ADialog;
import org.compiere.apps.form.FormFrame;
import org.compiere.apps.form.FormPanel;
import org.compiere.swing.CPanel;
import org.compiere.util.CLogger;
import org.compiere.util.DB;

/**
 * ConsultaSQL
 *
 * Form to execute SQL Query
 *
 * @author Henrique Santos
 * @contributor Mario Grigioni
 * @version $Id: ConsultaSQL.java, 16/10/2009 09:42:00 mgrigioni
 */
public class VConsultaSQL extends CPanel implements FormPanel, ActionListener, KeyListener, ItemListener {

    private static final long serialVersionUID = 1L;

    private static CLogger log = CLogger.getCLogger(VConsultaSQL.class);

    private int m_WindowNo = 0;

    private CPanel painelPrincipal = new CPanel();

    private CPanel painelSuperior = new CPanel();

    private CPanel painelSuperiorDireito = new CPanel();

    private CPanel painelSuperiorEsquerdo = new CPanel();

    private CPanel painelInferior = new CPanel();

    private GridBagLayout gridBaglayout = new GridBagLayout();

    private BorderLayout borderLayout = new BorderLayout();

    private JTable table = new JTable();

    private JScrollPane scrollPane = new JScrollPane();

    private JTextArea comandoSQL = new JTextArea(10, 75);

    private JTextArea erroSQL = new JTextArea(5, 75);

    private JScrollPane barraRolagem = new JScrollPane();

    private JButton executar = new JButton();

    private JButton exportar = new JButton();

    private JComboBox caixaLista = new JComboBox();

    private Vector<String> colunas;

    private Vector<Vector<Comparable<?>>> linhasTotal;

    public void init(int WindowNo, FormFrame frame) {
        log.info("");
        m_WindowNo = WindowNo;
        try {
            jbInit();
            frame.getContentPane().add(painelPrincipal, BorderLayout.CENTER);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "init", ex);
        }
    }

    void jbInit() throws Exception {
        executar.setText("Executar(F5)");
        executar.setToolTipText("Executa Comando SQL");
        exportar.setText("Exportar");
        exportar.setToolTipText("Exporta Consulta para arquivo CSV");
        painelSuperior.setBorder(BorderFactory.createTitledBorder("COMANDO SQL"));
        painelInferior.setBorder(BorderFactory.createTitledBorder("ERROS"));
        scrollPane.setBorder(BorderFactory.createTitledBorder("RESULTADO"));
        painelPrincipal.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        caixaLista.setPreferredSize(new Dimension(100, 20));
        painelPrincipal.setLayout(borderLayout);
        painelSuperior.setLayout(gridBaglayout);
        painelInferior.setLayout(gridBaglayout);
        painelSuperiorDireito.setLayout(gridBaglayout);
        painelSuperiorEsquerdo.setLayout(gridBaglayout);
        painelPrincipal.add(scrollPane, BorderLayout.CENTER);
        painelPrincipal.add(painelSuperior, BorderLayout.NORTH);
        painelPrincipal.add(painelInferior, BorderLayout.SOUTH);
        painelSuperior.add(painelSuperiorEsquerdo, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        painelSuperior.add(painelSuperiorDireito, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        painelSuperior.add(caixaLista, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        painelSuperiorEsquerdo.add(barraRolagem, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        painelSuperiorDireito.add(executar, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        painelSuperiorDireito.add(exportar, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        painelInferior.add(erroSQL, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        barraRolagem.getViewport().add(comandoSQL, null);
        erroSQL.setEnabled(false);
        painelPrincipal.addKeyListener(this);
        executar.addActionListener(this);
        exportar.addActionListener(this);
        comandoSQL.addKeyListener(this);
        caixaLista.addItemListener(this);
        painelPrincipal.repaint();
    }

    public void select(String query) {
        erroSQL.setText(null);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean erro = false;
        pstmt = DB.prepareStatement(query, null);
        try {
            rs = pstmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            colunas = new Vector<String>();
            linhasTotal = new Vector<Vector<Comparable<?>>>();
            colunas.addElement("Seq.");
            for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
                colunas.addElement(rsmd.getColumnName(i));
            }
            int numeroRegistro = 1;
            while (rs.next()) {
                Vector<Comparable<?>> linhas = new Vector<Comparable<?>>();
                linhas.addElement(numeroRegistro);
                for (int cont = 1; cont <= rsmd.getColumnCount(); cont++) {
                    linhas.addElement(rs.getString(cont));
                }
                linhasTotal.addElement(linhas);
                numeroRegistro++;
            }
            DB.close(rs, pstmt);
        } catch (SQLException e) {
            erroSQL.setText(e.getMessage());
            log.log(Level.WARNING, e.getMessage());
            erro = true;
        }
        boolean jaExiste = false;
        table = new JTable(linhasTotal, colunas);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scrollPane.getViewport().add(table, null);
        scrollPane.repaint();
        String texto = null;
        if (erro == false) {
            if (caixaLista.getItemCount() > 0) {
                for (int a = 0; a < caixaLista.getItemCount(); a++) {
                    String selecao = comandoSQL.getSelectedText();
                    if (selecao != null) {
                        texto = comandoSQL.getSelectedText();
                    } else {
                        texto = comandoSQL.getText();
                    }
                    if (caixaLista.getItemAt(a).equals(texto.toLowerCase())) {
                        jaExiste = true;
                        return;
                    }
                }
            }
            if (jaExiste == false) {
                String selecao = comandoSQL.getSelectedText();
                if (selecao != null) {
                    caixaLista.addItem(comandoSQL.getSelectedText());
                } else {
                    caixaLista.addItem(comandoSQL.getText());
                }
                caixaLista.setPreferredSize(new Dimension(300, 20));
            }
        }
    }

    public void executeUpdate(String query) {
        erroSQL.setText(null);
        boolean jaExiste = false;
        if (query.substring(0, 6).toLowerCase().equals("delete") || query.substring(0, 6).toLowerCase().equals("update")) {
            if (!query.contains("where")) {
                ADialog.info(m_WindowNo, this, "Obrigatório Clausula WHERE");
                return;
            }
        }
        Date data1 = new Date();
        int retornoDB = DB.executeUpdate(query, null);
        Date data2 = new Date();
        table.removeAll();
        table.repaint();
        if (retornoDB == -1) {
            erroSQL.setText(CLogger.retrieveError().toString());
            return;
        }
        String texto = null;
        if (caixaLista.getItemCount() > 0) {
            for (int a = 0; a < caixaLista.getItemCount(); a++) {
                String selecao = comandoSQL.getSelectedText();
                if (selecao != null) {
                    texto = comandoSQL.getSelectedText();
                } else {
                    texto = comandoSQL.getText();
                }
                if (caixaLista.getItemAt(a).equals(texto.toLowerCase())) {
                    jaExiste = true;
                    return;
                }
            }
        }
        if (jaExiste == false) {
            String selecao = comandoSQL.getSelectedText();
            if (selecao != null) {
                caixaLista.addItem(comandoSQL.getSelectedText());
            } else {
                caixaLista.addItem(comandoSQL.getText());
            }
            caixaLista.setPreferredSize(new Dimension(300, 20));
        }
        Object tempo = data2.getTime() - data1.getTime();
        erroSQL.setText("Query returned successfully:" + retornoDB + " rows affected, " + tempo + " ms execution time.");
        table.removeAll();
    }

    public void dispose() {
    }

    /**
	 * AÇÕES DO FORMULARIO
	 *
	 */
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource().equals(executar)) {
            String selecao = comandoSQL.getSelectedText();
            if (selecao != null) {
                String comando = comandoSQL.getSelectedText();
                if (comando.substring(0, 6).toLowerCase().equals("select")) {
                    select(comandoSQL.getSelectedText());
                } else {
                    executeUpdate(comandoSQL.getSelectedText());
                }
            } else {
                String comando = comandoSQL.getText().trim();
                if (comando.substring(0, 6).toLowerCase().equals("select")) {
                    select(comandoSQL.getText().trim());
                } else {
                    executeUpdate(comandoSQL.getText().trim());
                }
            }
        }
        if (arg0.getSource().equals(exportar)) {
            JFileChooser abrir = new JFileChooser();
            abrir.setSelectedFile(new File("export.csv"));
            abrir.showSaveDialog(VConsultaSQL.this);
            String caminho = abrir.getSelectedFile().getPath();
            try {
                ExportUtil.resultSetToCSV(comandoSQL.getText(), caminho, ";", true);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void keyPressed(KeyEvent arg0) {
        int keycode = arg0.getKeyCode();
        if (arg0.getSource().equals(comandoSQL)) {
            if (keycode == KeyEvent.VK_F5) {
                String selecao = comandoSQL.getSelectedText();
                if (selecao != null) {
                    String comando = comandoSQL.getSelectedText();
                    if (comando.substring(0, 6).toLowerCase().equals("select")) {
                        select(comandoSQL.getSelectedText());
                    } else {
                        executeUpdate(comandoSQL.getSelectedText());
                    }
                } else {
                    String comando = comandoSQL.getText().trim();
                    if (comando.substring(0, 6).toLowerCase().equals("select")) {
                        select(comandoSQL.getText().trim());
                    } else {
                        executeUpdate(comandoSQL.getText().trim());
                    }
                }
            }
        }
    }

    public void keyReleased(KeyEvent arg0) {
    }

    public void keyTyped(KeyEvent arg0) {
    }

    public void itemStateChanged(ItemEvent arg0) {
        if (arg0.getSource().equals(caixaLista)) {
            comandoSQL.setText(caixaLista.getSelectedItem().toString());
        }
    }
}
