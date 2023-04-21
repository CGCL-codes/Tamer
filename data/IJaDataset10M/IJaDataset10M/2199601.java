package trabalho.odonto.telas;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.Point;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.UIManager;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JButton;
import com.toedter.calendar.JCalendar;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import trabalho.odonto.util.AtualizadorHorario;
import trabalho.odonto.util.HibernateUtil;
import java.awt.Dimension;
import javax.swing.ImageIcon;

public class TelaPrincipal {

    private JFrame jFrame = null;

    private JPanel jContentPane = null;

    private JMenuBar jJMenuBar = null;

    private JMenu arquivoMenu = null;

    private JMenu doutorMenu = null;

    private JMenu ajudaMenu = null;

    private JMenu pacienteMenu = null;

    private JMenu funcionarioMenu = null;

    private JMenu ortodontiaMenu = null;

    private JMenu despesasMenu = null;

    private JMenu relatorioMenu = null;

    private JMenuItem exitMenuItem = null;

    private JMenuItem aboutMenuItem = null;

    private JMenuItem novoDoutorMenuItem = null;

    private JMenuItem alterarDoutorMenuItem = null;

    private JMenuItem removerDoutorMenuItem = null;

    private JMenuItem consultarDoutorMenuItem = null;

    private JDialog aboutDialog = null;

    private JPanel aboutContentPane = null;

    private JLabel aboutVersionLabel = null;

    private JPanel jPanelPainel2 = null;

    private JLabel jLabelNome = null;

    private JTextField jTextFieldNome = null;

    private JButton jButtonAddLista = null;

    private JCalendar jCalendar = null;

    private DefaultListModel model = new DefaultListModel();

    private JLabel jLabel1 = null;

    String[] tipo = { "Ortodontia" };

    String[] hora = { "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00" };

    private JMenuItem jMenuItemCadastrarPaciente = null;

    private JMenuItem jMenuItemCadastrarFuncionario = null;

    private JButton jButtonDelLista = null;

    /**
	 * This method initializes jFrame
	 * 
	 * @return javax.swing.JFrame
	 */
    private JFrame getJFrame() {
        if (jFrame == null) {
            jFrame = new JFrame();
            jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            jFrame.setJMenuBar(getJJMenuBar());
            jFrame.setSize(1194, 648);
            jFrame.setContentPane(getJContentPane());
            jFrame.setTitle("Odonto Plus (Vers�o Alpha)");
            jFrame.setExtendedState(jFrame.MAXIMIZED_BOTH);
            jFrame.setLocationRelativeTo(null);
            mostrarHora();
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ArrayList<String> doutor = new ArrayList<String>();
                String query = "select * from tb_doutor";
                ps = HibernateUtil.getSession().connection().prepareStatement(query);
                rs = ps.executeQuery();
                while (rs.next()) {
                    jComboBoxDoutor.addItem(rs.getString("nome"));
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro ao carregar Banco de Dados!");
                System.exit(0);
            }
        }
        return jFrame;
    }

    private JMenu getOrtodontiaMenu() {
        if (ortodontiaMenu == null) {
            ortodontiaMenu = new JMenu();
            ortodontiaMenu.setText("Ortodontia");
            ortodontiaMenu.add(getJMenuItemMarcarConsulta());
            ortodontiaMenu.add(getJMenuItemVerificarConsulta());
            ortodontiaMenu.add(getJMenuItemConsultaDia());
        }
        return ortodontiaMenu;
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getJPanelPainel2(), null);
            jContentPane.add(getJCalendar(), null);
            jContentPane.add(jLabelRelogio, null);
            jContentPane.add(getJButtonConsultaDia(), null);
        }
        return jContentPane;
    }

    /**
	 * This method initializes jJMenuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
    private JMenuBar getJJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new JMenuBar();
            jJMenuBar.add(getArquivoMenu());
            jJMenuBar.add(getDoutorMenu());
            jJMenuBar.add(getPacienteMenu());
            jJMenuBar.add(getFuncionarioMenu());
            jJMenuBar.add(getOrtodontiaMenu());
            jJMenuBar.add(getDespesasMenu());
            jJMenuBar.add(getRelatoriosMenu());
            jJMenuBar.add(getAjudaMenu());
        }
        return jJMenuBar;
    }

    /**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
    private JMenu getArquivoMenu() {
        if (arquivoMenu == null) {
            arquivoMenu = new JMenu();
            arquivoMenu.setText("Arquivo");
            arquivoMenu.add(getExitMenuItem());
        }
        return arquivoMenu;
    }

    private JMenu getPacienteMenu() {
        if (pacienteMenu == null) {
            pacienteMenu = new JMenu();
            pacienteMenu.setText("Paciente");
            pacienteMenu.add(getJMenuItemCadastrarPaciente());
        }
        return pacienteMenu;
    }

    private JMenu getDespesasMenu() {
        if (despesasMenu == null) {
            despesasMenu = new JMenu();
            despesasMenu.setText("Despesas");
            despesasMenu.add(getJMenuItemCadastrarDespesa());
        }
        return despesasMenu;
    }

    private JMenu getFuncionarioMenu() {
        if (funcionarioMenu == null) {
            funcionarioMenu = new JMenu();
            funcionarioMenu.setText("Funcion�rio");
            funcionarioMenu.add(getJMenuItemCadastrarFuncionario());
        }
        return funcionarioMenu;
    }

    private JMenu getRelatoriosMenu() {
        if (relatorioMenu == null) {
            relatorioMenu = new JMenu();
            relatorioMenu.setText("Relat�rios");
        }
        return relatorioMenu;
    }

    /**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
    private JMenu getDoutorMenu() {
        if (doutorMenu == null) {
            doutorMenu = new JMenu();
            doutorMenu.setText("Doutor");
            doutorMenu.add(getNovoDoutorMenuItem());
            doutorMenu.add(getConsultarDoutorMenuItem());
        }
        return doutorMenu;
    }

    /**
	 * This method initializes jMenu
	 * 
	 * @return javax.swing.JMenu
	 */
    private JMenu getAjudaMenu() {
        if (ajudaMenu == null) {
            ajudaMenu = new JMenu();
            ajudaMenu.setText("Ajuda");
            ajudaMenu.add(getAboutMenuItem());
        }
        return ajudaMenu;
    }

    /**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getExitMenuItem() {
        if (exitMenuItem == null) {
            exitMenuItem = new JMenuItem();
            exitMenuItem.setText("Sair");
            exitMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    int i = JOptionPane.showConfirmDialog(null, "Deseja sair?", "Sair", JOptionPane.YES_NO_OPTION);
                    if (i == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                }
            });
        }
        return exitMenuItem;
    }

    /**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getAboutMenuItem() {
        if (aboutMenuItem == null) {
            aboutMenuItem = new JMenuItem();
            aboutMenuItem.setText("Sobre");
            aboutMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    JDialog aboutDialog = getAboutDialog();
                    aboutDialog.pack();
                    Point loc = getJFrame().getLocation();
                    loc.translate(20, 20);
                    aboutDialog.setLocation(loc);
                    aboutDialog.setVisible(true);
                }
            });
        }
        return aboutMenuItem;
    }

    /**
	 * This method initializes aboutDialog
	 * 
	 * @return javax.swing.JDialog
	 */
    private JDialog getAboutDialog() {
        if (aboutDialog == null) {
            aboutDialog = new JDialog(getJFrame(), true);
            aboutDialog.setTitle("Sobre");
            aboutDialog.setSize(new Dimension(377, 257));
            aboutDialog.setPreferredSize(new Dimension(340, 240));
            aboutDialog.setContentPane(getAboutContentPane());
        }
        return aboutDialog;
    }

    /**
	 * This method initializes aboutContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getAboutContentPane() {
        if (aboutContentPane == null) {
            jLabelContato = new JLabel();
            jLabelContato.setText("<html>Contatos: <br> Victor Oliveira - (81)87455133 - (81)99015996<br>Edinaldo Machado - (81)97433318</html>");
            jLabelContato.setHorizontalAlignment(SwingConstants.CENTER);
            jLabelContato.setPreferredSize(new Dimension(38, 120));
            jLabelDescricao = new JLabel();
            jLabelDescricao.setText("Software de Gest�o de Cl�nicas Odontol�gicas");
            jLabelDescricao.setPreferredSize(new Dimension(262, 30));
            jLabelDescricao.setHorizontalAlignment(SwingConstants.CENTER);
            aboutContentPane = new JPanel();
            aboutContentPane.setLayout(new BorderLayout());
            aboutContentPane.add(getAboutVersionLabel(), BorderLayout.NORTH);
            aboutContentPane.add(jLabelDescricao, BorderLayout.CENTER);
            aboutContentPane.add(jLabelContato, BorderLayout.SOUTH);
        }
        return aboutContentPane;
    }

    /**
	 * This method initializes aboutVersionLabel
	 * 
	 * @return javax.swing.JLabel
	 */
    private JLabel getAboutVersionLabel() {
        if (aboutVersionLabel == null) {
            aboutVersionLabel = new JLabel();
            aboutVersionLabel.setText("Odonto Plus (Vers�o Alpha)");
            aboutVersionLabel.setPreferredSize(new Dimension(156, 50));
            aboutVersionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return aboutVersionLabel;
    }

    /**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getNovoDoutorMenuItem() {
        if (novoDoutorMenuItem == null) {
            novoDoutorMenuItem = new JMenuItem();
            novoDoutorMenuItem.setText("Cadastrar Doutor");
            novoDoutorMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    CadastrarDoutorTela cdt = new CadastrarDoutorTela();
                    cdt.show();
                }
            });
        }
        return novoDoutorMenuItem;
    }

    /**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getAlterarDoutorMenuItem() {
        if (alterarDoutorMenuItem == null) {
            alterarDoutorMenuItem = new JMenuItem();
            alterarDoutorMenuItem.setText("Alterar");
        }
        return alterarDoutorMenuItem;
    }

    /**
	 * This method initializes jMenuItem
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getRemoverDoutorMenuItem() {
        if (removerDoutorMenuItem == null) {
            removerDoutorMenuItem = new JMenuItem();
            removerDoutorMenuItem.setText("Remover");
        }
        return removerDoutorMenuItem;
    }

    private JMenuItem getConsultarDoutorMenuItem() {
        if (consultarDoutorMenuItem == null) {
            consultarDoutorMenuItem = new JMenuItem();
            consultarDoutorMenuItem.setText("Verificar Doutor");
            consultarDoutorMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    VerificarDoutor vdt = new VerificarDoutor();
                    vdt.show();
                }
            });
        }
        return consultarDoutorMenuItem;
    }

    /**
	 * This method initializes jPanelPainel2
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJPanelPainel2() {
        if (jPanelPainel2 == null) {
            jLabelDr = new JLabel();
            jLabelDr.setBounds(new Rectangle(15, 362, 71, 16));
            jLabelDr.setFont(new Font("Dialog", Font.BOLD, 14));
            jLabelDr.setText("Doutor(a)");
            jLabelRelogio = new JLabel();
            jLabelRelogio.setFont(new Font("Dialog", Font.BOLD, 48));
            jLabelRelogio.setBounds(new Rectangle(1065, 15, 219, 40));
            jLabelRelogio.setText("Relogio");
            jLabel1 = new JLabel();
            jLabel1.setBounds(new Rectangle(33, 33, 23, 16));
            jLabel1.setFont(new Font("Eras Bold ITC", Font.BOLD, 16));
            jLabel1.setForeground(Color.red);
            jLabel1.setIcon(new ImageIcon(getClass().getResource("/img/user_go.png")));
            jLabel1.setText("");
            jLabelNome = new JLabel();
            jLabelNome.setBounds(new Rectangle(15, 393, 46, 16));
            jLabelNome.setFont(new Font("Dialog", Font.BOLD, 14));
            jLabelNome.setText("Nome");
            jPanelPainel2 = new JPanel();
            jPanelPainel2.setLayout(null);
            jPanelPainel2.setBounds(new Rectangle(6, 15, 595, 566));
            jPanelPainel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), "Lista de Pacientes", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), new Color(51, 51, 51)));
            jPanelPainel2.add(jLabelNome, null);
            jPanelPainel2.add(getJTextFieldNome(), null);
            jPanelPainel2.add(getJButtonAddLista(), null);
            jPanelPainel2.add(jLabel1, null);
            jPanelPainel2.add(getJButtonDelLista(), null);
            jPanelPainel2.add(getJScrollPaneListaPaciente(), null);
            jPanelPainel2.add(jLabelDr, null);
            jPanelPainel2.add(getJComboBoxDoutor(), null);
        }
        return jPanelPainel2;
    }

    /**
	 * This method initializes jTextFieldNome
	 * 
	 * @return javax.swing.JTextField
	 */
    private JTextField getJTextFieldNome() {
        if (jTextFieldNome == null) {
            jTextFieldNome = new JTextField();
            jTextFieldNome.setBounds(new Rectangle(89, 390, 204, 27));
            jTextFieldNome.addCaretListener(new javax.swing.event.CaretListener() {

                public void caretUpdate(javax.swing.event.CaretEvent e) {
                    if (jTextFieldNome.getText().length() > 0) {
                        jButtonAddLista.setEnabled(true);
                    } else {
                        jButtonAddLista.setEnabled(false);
                    }
                }
            });
            jTextFieldNome.addKeyListener(new java.awt.event.KeyAdapter() {

                public void keyPressed(java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if (jTextFieldNome.getText().equals("")) {
                        } else {
                            String txt = jTextFieldNome.getText();
                            String relogio = jLabelRelogio.getText();
                            String nomehora = txt + "   |   " + "Entrada: " + relogio + "   |   Doutor(a): " + jComboBoxDoutor.getSelectedItem();
                            jListListaPaciente.setModel(model);
                            model.addElement(nomehora);
                            jTextFieldNome.setText("");
                            jButtonDelLista.setEnabled(false);
                            jListListaPaciente.clearSelection();
                            jTextFieldNome.requestFocus();
                        }
                    }
                }
            });
        }
        return jTextFieldNome;
    }

    private JCalendar getJCalendar() {
        if (jCalendar == null) {
            jCalendar = new JCalendar();
            jCalendar.setBounds(new Rectangle(828, 63, 443, 234));
            jCalendar.setFont(new Font("Dialog", Font.PLAIN, 14));
        }
        return jCalendar;
    }

    /**
	 * This method initializes jButtonAddLista
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getJButtonAddLista() {
        if (jButtonAddLista == null) {
            jButtonAddLista = new JButton();
            jButtonAddLista.setBounds(new Rectangle(320, 360, 108, 25));
            jButtonAddLista.setEnabled(false);
            jButtonAddLista.setIcon(new ImageIcon(getClass().getResource("/img/add.png")));
            jButtonAddLista.setText("Adicionar");
            jButtonAddLista.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    String txt = jTextFieldNome.getText();
                    String relogio = jLabelRelogio.getText();
                    String nomehora = txt + "   |   " + "Entrada: " + relogio + "   |  Doutor(a):  " + jComboBoxDoutor.getSelectedItem();
                    jListListaPaciente.setModel(model);
                    model.addElement(nomehora);
                    jTextFieldNome.setText("");
                    jButtonDelLista.setEnabled(false);
                    jListListaPaciente.clearSelection();
                    jTextFieldNome.requestFocus();
                }
            });
        }
        return jButtonAddLista;
    }

    /**
	 * This method initializes jMenuItemCadastrarPaciente
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getJMenuItemCadastrarPaciente() {
        if (jMenuItemCadastrarPaciente == null) {
            jMenuItemCadastrarPaciente = new JMenuItem();
            jMenuItemCadastrarPaciente.setText("Cadastrar Paciente");
            jMenuItemCadastrarPaciente.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    CadastrarPacienteTela cpt = new CadastrarPacienteTela();
                    cpt.show();
                }
            });
        }
        return jMenuItemCadastrarPaciente;
    }

    /**
	 * This method initializes jMenuItemCadastrarFuncionario
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getJMenuItemCadastrarFuncionario() {
        if (jMenuItemCadastrarFuncionario == null) {
            jMenuItemCadastrarFuncionario = new JMenuItem();
            jMenuItemCadastrarFuncionario.setText("Cadastrar Funcion�rio");
            jMenuItemCadastrarFuncionario.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    CadastrarFuncionarioTela cft = new CadastrarFuncionarioTela();
                    cft.show();
                }
            });
        }
        return jMenuItemCadastrarFuncionario;
    }

    /**
	 * This method initializes jButtonDelLista
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getJButtonDelLista() {
        if (jButtonDelLista == null) {
            jButtonDelLista = new JButton();
            jButtonDelLista.setBounds(new Rectangle(461, 360, 100, 25));
            jButtonDelLista.setEnabled(false);
            jButtonDelLista.setIcon(new ImageIcon(getClass().getResource("/img/delete.png")));
            jButtonDelLista.setText("Excluir");
            jButtonDelLista.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int indice = jListListaPaciente.getSelectedIndex();
                    if (indice > -1) {
                        model.remove(indice);
                        jButtonDelLista.setEnabled(false);
                        jListListaPaciente.clearSelection();
                    }
                }
            });
        }
        return jButtonDelLista;
    }

    private JLabel jLabelRelogio = null;

    private JScrollPane jScrollPaneListaPaciente = null;

    private JList jListListaPaciente = null;

    private JMenuItem jMenuItemMarcarConsulta = null;

    private JMenuItem jMenuItemVerificarConsulta = null;

    private JLabel jLabelDr = null;

    private JComboBox jComboBoxDoutor = null;

    private JLabel jLabelDescricao = null;

    private JLabel jLabelContato = null;

    private JMenuItem jMenuItemCadastrarDespesa = null;

    private JButton jButtonConsultaDia = null;

    private JMenuItem jMenuItemConsultaDia = null;

    public void mostrarHora() {
        AtualizadorHorario ah = new AtualizadorHorario(jLabelRelogio);
        ah.mostrarData(true);
        Thread thHora = ah;
        thHora.start();
    }

    /**
	 * This method initializes jScrollPaneListaPaciente	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJScrollPaneListaPaciente() {
        if (jScrollPaneListaPaciente == null) {
            jScrollPaneListaPaciente = new JScrollPane();
            jScrollPaneListaPaciente.setBounds(new Rectangle(60, 29, 526, 323));
            jScrollPaneListaPaciente.setViewportView(getJListListaPaciente());
        }
        return jScrollPaneListaPaciente;
    }

    /**
	 * This method initializes jTableListaPaciente	
	 * 	
	 * @return javax.swing.JTable	
	 */
    private JList getJListListaPaciente() {
        if (jListListaPaciente == null) {
            jListListaPaciente = new JList();
            jListListaPaciente.setFont(new Font("Dialog", Font.BOLD, 14));
            jListListaPaciente.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

                public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                    if (jListListaPaciente.isFocusable()) {
                        jButtonDelLista.setEnabled(true);
                    } else {
                        jButtonDelLista.setEnabled(false);
                    }
                }
            });
        }
        return jListListaPaciente;
    }

    /**
	 * This method initializes jMenuItemMarcarConsulta	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
    private JMenuItem getJMenuItemMarcarConsulta() {
        if (jMenuItemMarcarConsulta == null) {
            jMenuItemMarcarConsulta = new JMenuItem();
            jMenuItemMarcarConsulta.setText("Marcar Consulta");
            jMenuItemMarcarConsulta.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    CadastrarConsulta cct = new CadastrarConsulta();
                    cct.show();
                }
            });
        }
        return jMenuItemMarcarConsulta;
    }

    /**
	 * This method initializes jMenuItemVerificarConsulta	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
    private JMenuItem getJMenuItemVerificarConsulta() {
        if (jMenuItemVerificarConsulta == null) {
            jMenuItemVerificarConsulta = new JMenuItem();
            jMenuItemVerificarConsulta.setText("Verificar Consulta");
            jMenuItemVerificarConsulta.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    VerificarConsulta vct = new VerificarConsulta();
                    vct.show();
                }
            });
        }
        return jMenuItemVerificarConsulta;
    }

    /**
	 * This method initializes jComboBoxDoutor	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getJComboBoxDoutor() {
        if (jComboBoxDoutor == null) {
            jComboBoxDoutor = new JComboBox();
            jComboBoxDoutor.setBounds(new Rectangle(90, 360, 204, 25));
        }
        return jComboBoxDoutor;
    }

    /**
	 * This method initializes jMenuItemCadastrarDespesa	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
    private JMenuItem getJMenuItemCadastrarDespesa() {
        if (jMenuItemCadastrarDespesa == null) {
            jMenuItemCadastrarDespesa = new JMenuItem();
            jMenuItemCadastrarDespesa.setText("Cadastrar Despesa");
            jMenuItemCadastrarDespesa.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    CadastrarDespesas cdsp = new CadastrarDespesas();
                    cdsp.show();
                }
            });
        }
        return jMenuItemCadastrarDespesa;
    }

    /**
	 * This method initializes jButtonConsultaDia	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonConsultaDia() {
        if (jButtonConsultaDia == null) {
            jButtonConsultaDia = new JButton();
            jButtonConsultaDia.setBounds(new Rectangle(828, 28, 154, 30));
            jButtonConsultaDia.setFont(new Font("Dialog", Font.PLAIN, 12));
            jButtonConsultaDia.setIcon(new ImageIcon(getClass().getResource("/img/calendar_view_day.png")));
            jButtonConsultaDia.setText("Consultas do dia");
            jButtonConsultaDia.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    ConsultaDoDia cdd = new ConsultaDoDia();
                    cdd.show();
                }
            });
        }
        return jButtonConsultaDia;
    }

    /**
	 * This method initializes jMenuItemConsultaDia	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
    private JMenuItem getJMenuItemConsultaDia() {
        if (jMenuItemConsultaDia == null) {
            jMenuItemConsultaDia = new JMenuItem();
            jMenuItemConsultaDia.setText("Consultas do dia");
            jMenuItemConsultaDia.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    ConsultaDoDia cdd = new ConsultaDoDia();
                    cdd.show();
                }
            });
        }
        return jMenuItemConsultaDia;
    }

    /**
	 * Launches this application
	 */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.out.println("Nimbus isn't available");
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                TelaPrincipal application = new TelaPrincipal();
                application.getJFrame().setVisible(true);
            }
        });
    }
}
