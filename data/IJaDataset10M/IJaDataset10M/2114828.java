package trabalho.odonto.telas;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import trabalho.odonto.classesbasicas.Consulta;
import trabalho.odonto.exception.ConsultaException;
import trabalho.odonto.fachada.Fachada;
import trabalho.odonto.fachada.IFachada;
import trabalho.odonto.util.HibernateUtil;

public class ConsultaDoDia extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    Date now = new Date();

    DateFormat dataFormatada = DateFormat.getDateInstance();

    String dataAtual = dataFormatada.format(now);

    private JLabel jLabelDataAtual = null;

    private JScrollPane jScrollPaneListaConsulta = null;

    private JTable jTableListaConsulta = null;

    private JLabel jLabelPaciente = null;

    private JLabel jLabelDoutor = null;

    private JLabel jLabelObs = null;

    private JTextField jTextFieldNomePaciente = null;

    private JLabel jLabelDia = null;

    private JTextField jTextFieldDoutor = null;

    private JButton jButtonAlterarConsulta = null;

    private JButton jButtonExcluirConsulta = null;

    private JLabel jLabelDataHoje = null;

    private JButton jButtonSalvarAlteracao = null;

    private JButton jButtonCancelarAlteracao = null;

    private JScrollPane jScrollPaneObsConsulta = null;

    private JTextArea jTextAreaObsConsulta = null;

    DefaultTableModel tabelaConsultaPorData = new DefaultTableModel();

    IFachada fachada = Fachada.getObterInstancia();

    Consulta con = new Consulta();

    private ArrayList<Object> listaConsultaData = new ArrayList();

    PreparedStatement ps = null;

    ResultSet rs = null;

    private JLabel jLabelHoraConsulta = null;

    private JTextField jTextFieldHora = null;

    private JTextField jTextFieldDataConsulta = null;

    private JLabel jLabelID = null;

    private JTextField jTextFieldCod = null;

    private JLabel jLabelmsg = null;

    private JLabel jLabelmsg2 = null;

    private JLabel jLabelmsgat = null;

    private JButton jButtonAtualizarConsultaDia = null;

    /**
	 * This is the default constructor
	 */
    public ConsultaDoDia() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(847, 635);
        this.setResizable(false);
        this.setContentPane(getJContentPane());
        this.setTitle("Consultas do dia");
        PreencherTabelaConsultarData();
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jLabelmsgat = new JLabel();
            jLabelmsgat.setBounds(new Rectangle(701, 364, 56, 16));
            jLabelmsgat.setForeground(Color.red);
            jLabelmsgat.setText("Aten��o!");
            jLabelmsgat.setFont(new Font("Dialog", Font.BOLD, 13));
            jLabelmsgat.setVisible(false);
            jLabelmsg2 = new JLabel();
            jLabelmsg2.setBounds(new Rectangle(653, 406, 147, 16));
            jLabelmsg2.setFont(new Font("Dialog", Font.PLAIN, 12));
            jLabelmsg2.setForeground(Color.red);
            jLabelmsg2.setText("Data, Hora e Observa��o.");
            jLabelmsg2.setVisible(false);
            jLabelmsg = new JLabel();
            jLabelmsg.setBounds(new Rectangle(631, 383, 187, 21));
            jLabelmsg.setFont(new Font("Dialog", Font.PLAIN, 12));
            jLabelmsg.setForeground(Color.red);
            jLabelmsg.setText("Voc� s� pode alterar os campos:");
            jLabelmsg.setVisible(false);
            jLabelID = new JLabel();
            jLabelID.setBounds(new Rectangle(240, 304, 58, 17));
            jLabelID.setFont(new Font("Dialog", Font.BOLD, 14));
            jLabelID.setText("C�digo");
            jLabelHoraConsulta = new JLabel();
            jLabelHoraConsulta.setBounds(new Rectangle(240, 394, 38, 16));
            jLabelHoraConsulta.setFont(new Font("Dialog", Font.BOLD, 14));
            jLabelHoraConsulta.setText("Hora");
            jLabelDataHoje = new JLabel();
            jLabelDataHoje.setBounds(new Rectangle(45, 38, 40, 16));
            jLabelDataHoje.setText("Hoje �");
            jLabelDia = new JLabel();
            jLabelDia.setBounds(new Rectangle(240, 364, 38, 16));
            jLabelDia.setFont(new Font("Dialog", Font.BOLD, 14));
            jLabelDia.setText("Data");
            jLabelObs = new JLabel();
            jLabelObs.setBounds(new Rectangle(240, 421, 93, 18));
            jLabelObs.setFont(new Font("Dialog", Font.BOLD, 14));
            jLabelObs.setText("Observa��o");
            jLabelDoutor = new JLabel();
            jLabelDoutor.setBounds(new Rectangle(240, 528, 67, 16));
            jLabelDoutor.setFont(new Font("Dialog", Font.BOLD, 14));
            jLabelDoutor.setText("Doutor(a)");
            jLabelPaciente = new JLabel();
            jLabelPaciente.setBounds(new Rectangle(240, 334, 67, 16));
            jLabelPaciente.setFont(new Font("Dialog", Font.BOLD, 14));
            jLabelPaciente.setText("Paciente");
            jLabelDataAtual = new JLabel();
            jLabelDataAtual.setBounds(new Rectangle(91, 37, 120, 16));
            jLabelDataAtual.setFont(new Font("Dialog", Font.BOLD, 18));
            jLabelDataAtual.setText(dataAtual);
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(jLabelDataAtual, null);
            jContentPane.add(getJScrollPaneListaConsulta(), null);
            jContentPane.add(jLabelPaciente, null);
            jContentPane.add(jLabelDoutor, null);
            jContentPane.add(jLabelObs, null);
            jContentPane.add(getJTextFieldNomePaciente(), null);
            jContentPane.add(jLabelDia, null);
            jContentPane.add(getJTextFieldDoutor(), null);
            jContentPane.add(getJButtonAlterarConsulta(), null);
            jContentPane.add(getJButtonExcluirConsulta(), null);
            jContentPane.add(jLabelDataHoje, null);
            jContentPane.add(getJButtonSalvarAlteracao(), null);
            jContentPane.add(getJButtonCancelarAlteracao(), null);
            jContentPane.add(getJScrollPaneObsConsulta(), null);
            jContentPane.add(jLabelHoraConsulta, null);
            jContentPane.add(getJTextFieldHora(), null);
            jContentPane.add(getJTextFieldDataConsulta(), null);
            jContentPane.add(jLabelID, null);
            jContentPane.add(getJTextFieldCod(), null);
            jContentPane.add(jLabelmsg, null);
            jContentPane.add(jLabelmsg2, null);
            jContentPane.add(jLabelmsgat, null);
            jContentPane.add(getJButtonAtualizarConsultaDia(), null);
        }
        return jContentPane;
    }

    /**
	 * This method initializes jScrollPaneListaConsulta	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJScrollPaneListaConsulta() {
        if (jScrollPaneListaConsulta == null) {
            jScrollPaneListaConsulta = new JScrollPane();
            jScrollPaneListaConsulta.setBounds(new Rectangle(30, 60, 783, 203));
            jScrollPaneListaConsulta.setViewportView(getJTableListaConsulta());
        }
        return jScrollPaneListaConsulta;
    }

    /**
	 * This method initializes jTableListaConsulta	
	 * 	
	 * @return javax.swing.JTable	
	 */
    private JTable getJTableListaConsulta() {
        if (jTableListaConsulta == null) {
            jTableListaConsulta = new JTable();
            jTableListaConsulta.setEnabled(true);
            jTableListaConsulta.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            jTableListaConsulta.setFont(new Font("Dialog", Font.PLAIN, 14));
            jTableListaConsulta.setModel(tabelaConsultaPorData);
            jTableListaConsulta.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (jTableListaConsulta.isColumnSelected(0) || jTableListaConsulta.isColumnSelected(1) || jTableListaConsulta.isColumnSelected(2) || jTableListaConsulta.isColumnSelected(3) || jTableListaConsulta.isColumnSelected(4) || jTableListaConsulta.isColumnSelected(5)) {
                        jTextFieldCod.setText(jTableListaConsulta.getValueAt(jTableListaConsulta.getSelectedRow(), 0).toString());
                        jTextFieldNomePaciente.setText(jTableListaConsulta.getValueAt(jTableListaConsulta.getSelectedRow(), 1).toString());
                        jTextFieldDataConsulta.setText(jTableListaConsulta.getValueAt(jTableListaConsulta.getSelectedRow(), 2).toString());
                        jTextFieldHora.setText(jTableListaConsulta.getValueAt(jTableListaConsulta.getSelectedRow(), 3).toString());
                        jTextAreaObsConsulta.setText(jTableListaConsulta.getValueAt(jTableListaConsulta.getSelectedRow(), 4).toString());
                        jTextFieldDoutor.setText(jTableListaConsulta.getValueAt(jTableListaConsulta.getSelectedRow(), 5).toString());
                        jButtonAlterarConsulta.setEnabled(true);
                        jButtonExcluirConsulta.setEnabled(true);
                    }
                }
            });
        }
        return jTableListaConsulta;
    }

    /**
	 * This method initializes jTextFieldNomePaciente	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getJTextFieldNomePaciente() {
        if (jTextFieldNomePaciente == null) {
            jTextFieldNomePaciente = new JTextField();
            jTextFieldNomePaciente.setBounds(new Rectangle(314, 330, 287, 27));
            jTextFieldNomePaciente.setEditable(false);
        }
        return jTextFieldNomePaciente;
    }

    /**
	 * This method initializes jTextFieldDoutor	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getJTextFieldDoutor() {
        if (jTextFieldDoutor == null) {
            jTextFieldDoutor = new JTextField();
            jTextFieldDoutor.setBounds(new Rectangle(315, 524, 286, 27));
            jTextFieldDoutor.setEditable(false);
        }
        return jTextFieldDoutor;
    }

    /**
	 * This method initializes jButtonAlterarConsulta	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonAlterarConsulta() {
        if (jButtonAlterarConsulta == null) {
            jButtonAlterarConsulta = new JButton();
            jButtonAlterarConsulta.setBounds(new Rectangle(240, 569, 96, 25));
            jButtonAlterarConsulta.setIcon(new ImageIcon(getClass().getResource("/img/pencil.png")));
            jButtonAlterarConsulta.setEnabled(false);
            jButtonAlterarConsulta.setText("Alterar");
            jButtonAlterarConsulta.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    jTextAreaObsConsulta.setEditable(true);
                    jButtonCancelarAlteracao.setVisible(true);
                    jButtonSalvarAlteracao.setVisible(true);
                    jButtonExcluirConsulta.setVisible(false);
                    jButtonAlterarConsulta.setEnabled(false);
                    jTextFieldHora.setEditable(true);
                    jTextFieldDataConsulta.setEditable(true);
                    jTableListaConsulta.setEnabled(false);
                    jLabelmsgat.setVisible(true);
                    jLabelmsg.setVisible(true);
                    jLabelmsg2.setVisible(true);
                }
            });
        }
        return jButtonAlterarConsulta;
    }

    /**
	 * This method initializes jButtonExcluirConsulta	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonExcluirConsulta() {
        if (jButtonExcluirConsulta == null) {
            jButtonExcluirConsulta = new JButton();
            jButtonExcluirConsulta.setBounds(new Rectangle(345, 569, 96, 25));
            jButtonExcluirConsulta.setIcon(new ImageIcon(getClass().getResource("/img/delete.png")));
            jButtonExcluirConsulta.setEnabled(false);
            jButtonExcluirConsulta.setText("Excluir ");
            jButtonExcluirConsulta.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        int i = JOptionPane.showConfirmDialog(null, "Deseja excluir esta Consulta?", "Deletar Consulta", JOptionPane.YES_NO_OPTION);
                        if (i == JOptionPane.YES_OPTION) {
                            con.setId(Integer.parseInt(jTextFieldCod.getText()));
                            fachada.removerConsulta(con);
                            JOptionPane.showMessageDialog(null, "Consulta exclu�da com Sucesso!");
                        }
                    } catch (ConsultaException e1) {
                        JOptionPane.showMessageDialog(null, "Erro ao acessar o Banco de Dados!");
                        e1.printStackTrace();
                    }
                }
            });
        }
        return jButtonExcluirConsulta;
    }

    /**
	 * This method initializes jButtonSalvarAlteracao	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonSalvarAlteracao() {
        if (jButtonSalvarAlteracao == null) {
            jButtonSalvarAlteracao = new JButton();
            jButtonSalvarAlteracao.setBounds(new Rectangle(454, 569, 149, 25));
            jButtonSalvarAlteracao.setIcon(new ImageIcon(getClass().getResource("/img/accept.png")));
            jButtonSalvarAlteracao.setText("Salvar Altera��o");
            jButtonSalvarAlteracao.setVisible(false);
            jButtonSalvarAlteracao.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        int i = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja alterar?", "Alterar Consulta", JOptionPane.YES_NO_OPTION);
                        if (i == JOptionPane.YES_OPTION) {
                            con.setId(Integer.parseInt(jTextFieldCod.getText()));
                            con.setDoutor(jTextFieldDoutor.getText().toUpperCase());
                            con.setHora(jTextFieldHora.getText().toUpperCase());
                            con.setAnotacao(jTextAreaObsConsulta.getText().toUpperCase());
                            con.setPaciente(jTextFieldNomePaciente.getText().toUpperCase());
                            con.setData(jTextFieldDataConsulta.getText().toUpperCase());
                            fachada.atualizarConsulta(con);
                            jButtonSalvarAlteracao.setVisible(false);
                            jButtonCancelarAlteracao.setVisible(false);
                            jButtonAlterarConsulta.setEnabled(true);
                            jButtonExcluirConsulta.setVisible(true);
                            jTableListaConsulta.setEnabled(true);
                        }
                    } catch (ConsultaException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
        return jButtonSalvarAlteracao;
    }

    /**
	 * This method initializes jButtonCancelarAlteracao	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonCancelarAlteracao() {
        if (jButtonCancelarAlteracao == null) {
            jButtonCancelarAlteracao = new JButton();
            jButtonCancelarAlteracao.setBounds(new Rectangle(437, 287, 164, 25));
            jButtonCancelarAlteracao.setIcon(new ImageIcon(getClass().getResource("/img/cancel.png")));
            jButtonCancelarAlteracao.setText("Cancelar Altera��o");
            jButtonCancelarAlteracao.setVisible(false);
            jButtonCancelarAlteracao.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    jTextAreaObsConsulta.setEditable(false);
                    jButtonSalvarAlteracao.setVisible(false);
                    jButtonAlterarConsulta.setEnabled(true);
                    jButtonExcluirConsulta.setVisible(true);
                    jButtonCancelarAlteracao.setVisible(false);
                    jTextFieldHora.setEditable(false);
                    jTextFieldDataConsulta.setEditable(false);
                    jTableListaConsulta.setEnabled(true);
                    jLabelmsgat.setVisible(false);
                    jLabelmsg.setVisible(false);
                    jLabelmsg2.setVisible(false);
                    if (jTableListaConsulta.isColumnSelected(0) || jTableListaConsulta.isColumnSelected(1) || jTableListaConsulta.isColumnSelected(2) || jTableListaConsulta.isColumnSelected(3) || jTableListaConsulta.isColumnSelected(4)) {
                        jTextFieldCod.setText(jTableListaConsulta.getValueAt(jTableListaConsulta.getSelectedRow(), 0).toString());
                        jTextFieldNomePaciente.setText(jTableListaConsulta.getValueAt(jTableListaConsulta.getSelectedRow(), 1).toString());
                        jTextFieldDataConsulta.setText(jTableListaConsulta.getValueAt(jTableListaConsulta.getSelectedRow(), 2).toString());
                        jTextFieldHora.setText(jTableListaConsulta.getValueAt(jTableListaConsulta.getSelectedRow(), 3).toString());
                        jTextAreaObsConsulta.setText(jTableListaConsulta.getValueAt(jTableListaConsulta.getSelectedRow(), 4).toString());
                        jTextFieldDoutor.setText(jTableListaConsulta.getValueAt(jTableListaConsulta.getSelectedRow(), 5).toString());
                    }
                }
            });
        }
        return jButtonCancelarAlteracao;
    }

    /**
	 * This method initializes jScrollPaneObsConsulta	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJScrollPaneObsConsulta() {
        if (jScrollPaneObsConsulta == null) {
            jScrollPaneObsConsulta = new JScrollPane();
            jScrollPaneObsConsulta.setBounds(new Rectangle(240, 440, 360, 70));
            jScrollPaneObsConsulta.setViewportView(getJTextAreaObsConsulta());
        }
        return jScrollPaneObsConsulta;
    }

    /**
	 * This method initializes jTextAreaObsConsulta	
	 * 	
	 * @return javax.swing.JTable	
	 */
    private JTextArea getJTextAreaObsConsulta() {
        if (jTextAreaObsConsulta == null) {
            jTextAreaObsConsulta = new JTextArea();
            jTextAreaObsConsulta.setEditable(false);
            jTextAreaObsConsulta.setLineWrap(true);
        }
        return jTextAreaObsConsulta;
    }

    public ArrayList consultaPorData() {
        try {
            String query = "select * from tb_consulta where data = ?";
            ps = HibernateUtil.getSession().connection().prepareStatement(query);
            ps.setString(1, jLabelDataAtual.getText());
            rs = ps.executeQuery();
            while (rs.next()) {
                String[] row = { rs.getString("id"), rs.getString("paciente"), rs.getString("data"), rs.getString("hora"), rs.getString("anotacao"), rs.getString("doutor") };
                listaConsultaData.add(row);
            }
            return listaConsultaData;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaConsultaData;
    }

    protected void PreencherTabelaConsultarData() {
        String[] colunas = { "C�digo", "Paciente", "Data", "Hora", "Obs.", "Doutor" };
        String[][] arr = new String[consultaPorData().size()][];
        for (int k = 0; k < arr.length; k++) {
            arr[k] = (String[]) consultaPorData().get(k);
        }
        tabelaConsultaPorData.setDataVector(arr, colunas);
    }

    /**
	 * This method initializes jTextFieldHora	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getJTextFieldHora() {
        if (jTextFieldHora == null) {
            jTextFieldHora = new JTextField();
            jTextFieldHora.setBounds(new Rectangle(315, 391, 91, 27));
            jTextFieldHora.setEditable(false);
        }
        return jTextFieldHora;
    }

    /**
	 * This method initializes jTextFieldDataConsulta	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getJTextFieldDataConsulta() {
        if (jTextFieldDataConsulta == null) {
            jTextFieldDataConsulta = new JTextField();
            jTextFieldDataConsulta.setBounds(new Rectangle(315, 361, 85, 27));
            jTextFieldDataConsulta.setEditable(false);
        }
        return jTextFieldDataConsulta;
    }

    /**
	 * This method initializes jTextFieldCod	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getJTextFieldCod() {
        if (jTextFieldCod == null) {
            jTextFieldCod = new JTextField();
            jTextFieldCod.setBounds(new Rectangle(315, 301, 47, 27));
            jTextFieldCod.setEditable(false);
            jTextFieldCod.setFont(new Font("Dialog", Font.PLAIN, 12));
        }
        return jTextFieldCod;
    }

    /**
	 * This method initializes jButtonAtualizarConsultaDia	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonAtualizarConsultaDia() {
        if (jButtonAtualizarConsultaDia == null) {
            jButtonAtualizarConsultaDia = new JButton();
            jButtonAtualizarConsultaDia.setBounds(new Rectangle(678, 263, 135, 25));
            jButtonAtualizarConsultaDia.setIcon(new ImageIcon(getClass().getResource("/img/arrow_refresh.png")));
            jButtonAtualizarConsultaDia.setText("Atualizar lista");
            jButtonAtualizarConsultaDia.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    PreencherTabelaConsultarData();
                }
            });
        }
        return jButtonAtualizarConsultaDia;
    }
}
