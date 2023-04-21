package view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import control.AcessaDados;
import control.ExtraiCodigo;

/**
 * Classe que implementa a Interface Gr�fica de Usu�rio
 * para a funcionalidade de consulta, exclus�o e altera��o de informa��es
 * dos objetos M�dia, Li��o, Cap�tulo e Tutorial do 
 * M�dulo 1 do sistema TuDMaP.<br />
 * Esta implementa��o tem como base o modelo de interface gr�fica
 * 'tudmap-mod01-gui-consultar'.
 * @author Samuel Henrique Nascimento da Silva
 *
 */
public class TelaCEA extends JFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private static JList codTituloList;

    private static JScrollPane codTituloSP;

    private JButton consultarButton, excluirButton, alterarButton, confirmarButton, sairButton, browseButton, cancelarButton;

    private JLabel codTituloLabel, tipoObjetoLabel, tutorialLabel, capituloLabel, licaoLabel, midiaLabel, codigoLabel, tituloLabel, autorLabel, comentarioLabel, descricaoLabel, urlLabel, tipoMidiaLabel, imagemLabel, videoLabel;

    private JPanel alterarPanel;

    private JSeparator separator;

    private static JRadioButton tutorialRB, capituloRB, licaoRB, midiaRB, videoRB, imagemRB;

    private ButtonGroup tipoObjetoBG, tipoMidiaBG;

    private static JTextField codigoTF, tituloTF, autorTF, urlTF;

    private static JTextArea comentarioTA, descricaoTA;

    private JPanel tipoObjetoPanel, tipoMidiaPanel;

    private JScrollPane descricaoSP;

    private Connection con;

    private JFrame parent;

    private DefaultListModel listModel;

    private int codConsultado;

    private static Vector<String> editaveis;

    /**
	*Construtor que adiciona e configura os componentes de interface e recebe como argumentos
	*objeto da classe 'java.sql.Connection' para armazezar as informa��es necess�rias da conex�o 
	*com a base de dados corrente, e um objeto da classe 'javax.swing.JFrame' que representa
	*a janela anterior que chamou esta, para que assim, quando esta for fechada a anterior possa
	*ser restaurada
	*/
    public TelaCEA(Connection con, JFrame parent) {
        super("TuDMaP :: Consultar/Excluir/Alterar");
        setConexao(con);
        setParent(parent);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                setGuiDefault();
                dispose();
                getParent().setVisible(true);
            }
        });
        this.buildGui();
    }

    /**
	 * Atribui valor para o atributo desta classe que armazena inform��es sobre a conex�o com a base de dados
	 * corrente.
	 * @param con Conex�o com a base de dados corrente
	 */
    public void setConexao(Connection con) {
        this.con = con;
    }

    /**
	 * Retorna o valor do atributo 'outcon', que guarda as informa��es da conex�o com a base de dados corrente.
	 */
    public Connection getConexao() {
        return (this.con);
    }

    /**
	 * Atribui um valor para o atributo 'parent' que representa a tela anterior em que a atual foi chamada.
	 * @param parent JFrame
	 */
    public void setParent(JFrame parent) {
        this.parent = parent;
    }

    /**
	 * Retorna o valor do atributo 'parent'
	 */
    @Override
    public JFrame getParent() {
        return (parent);
    }

    public void setListModel() {
        listModel = new DefaultListModel();
    }

    public DefaultListModel getListModel() {
        return listModel;
    }

    /**
	 * Retorna o valor do atributo 'parent'
	 */
    public void buildGui() {
        this.setSize(610, 600);
        this.setResizable(false);
        this.getContentPane().setLayout(null);
        this.setLocationRelativeTo(getParent());
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        getTipoObjetoLabel().setBounds(10, 27, 85, 25);
        this.add(getTipoObjetoLabel(), null);
        getTipoObjetoPanel().setBounds(10, 50, 180, 90);
        this.add(getTipoObjetoPanel(), null);
        getCodTituloLabel().setBounds(200, 31, 100, 15);
        this.add(getCodTituloLabel(), null);
        getCodTituloSP().setBounds(200, 49, 270, 92);
        this.add(getCodTituloSP(), null);
        getSeparator().setBounds(10, 157, 570, 10);
        this.add(getSeparator(), null);
        getCodigoLabel().setBounds(50, 175, 85, 25);
        this.add(getCodigoLabel(), null);
        getCodigoTF().setBounds(100, 175, 100, 25);
        getCodigoTF().setEditable(false);
        this.add(getCodigoTF(), null);
        getTituloLabel().setBounds(58, 210, 85, 25);
        this.add(getTituloLabel(), null);
        getTituloTF().setBounds(100, 210, 340, 25);
        this.add(getTituloTF(), null);
        getTituloTF().setEditable(false);
        getAutorLabel().setBounds(60, 245, 85, 25);
        this.add(getAutorLabel(), null);
        getAutorTF().setEditable(false);
        getAutorTF().setBounds(100, 245, 340, 25);
        this.add(getAutorTF(), null);
        getComentarioLabel().setBounds(25, 285, 85, 25);
        this.add(getComentarioLabel(), null);
        getComentarioTA().setEditable(false);
        getComentarioTA().setBounds(101, 285, 340, 50);
        this.add(getComentarioTA(), null);
        getDescricaoLabel().setBounds(33, 350, 85, 25);
        this.add(getDescricaoLabel(), null);
        getDescricaoTA().setEditable(false);
        getDescricaoSP().setBounds(101, 350, 340, 100);
        this.add(getDescricaoSP(), null);
        getUrlLabel().setBounds(19, 475, 85, 25);
        this.add(getUrlLabel(), null);
        getUrlTF().setEditable(false);
        getUrlTF().setBounds(100, 475, 258, 25);
        this.add(getUrlTF(), null);
        getBrowseButton().setBounds(360, 475, 80, 24);
        this.add(getBrowseButton(), null);
        getTipoMidiaLabel().setBounds(18, 515, 85, 25);
        this.add(getTipoMidiaLabel(), null);
        getImagemRB().setEnabled(false);
        getVideoRB().setEnabled(false);
        getTipoMidiaPanel().setBounds(100, 515, 215, 30);
        this.add(getTipoMidiaPanel(), null);
        getConsultarButton().setBounds(480, 75, 90, 30);
        this.add(getConsultarButton(), null);
        getExcluirButton().setBounds(480, 210, 90, 30);
        this.add(getExcluirButton(), null);
        getAlterarPanel().setBounds(470, 255, 110, 150);
        getAlterarPanel().setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        this.add(getAlterarPanel(), null);
        getAlterarButton().setBounds(10, 10, 90, 30);
        getConfirmarButton().setBounds(10, 60, 91, 30);
        getCancelarButton().setBounds(10, 110, 90, 30);
        getAlterarPanel().add(getAlterarButton(), null);
        getAlterarPanel().add(getConfirmarButton(), null);
        getAlterarPanel().add(getCancelarButton(), null);
        getSairButton().setBounds(480, 420, 90, 30);
        this.add(getSairButton(), null);
        this.setTipoObjetoBG();
        this.setTipoMidiaBG();
    }

    private JLabel getCodTituloLabel() {
        if (codTituloLabel == null) {
            codTituloLabel = new JLabel("C�digo/T�tulo");
        }
        return (codTituloLabel);
    }

    public void setCodTituloList(JList list) {
        codTituloList = list;
    }

    public void setListNull() {
        Vector<String> v = new Vector<String>();
        v.add("");
        getCodTituloList().setListData(v);
    }

    public JList getCodTituloList() {
        if (codTituloList == null) {
            codTituloList = new JList();
        }
        return (codTituloList);
    }

    public void addListaToSP(JList list) {
        getCodTituloSP().getViewport().setView(list);
    }

    public JScrollPane getCodTituloSP() {
        if (codTituloSP == null) {
            codTituloSP = new JScrollPane();
            codTituloSP.getViewport().setView(getCodTituloList());
        }
        return (codTituloSP);
    }

    /**
	 * Implmenta as a��es a serem realizadas quando o usu�rio clica no bot�o 'Consultar'
	 */
    private JButton getConsultarButton() {
        if (consultarButton == null) {
            consultarButton = new JButton("Consultar");
            consultarButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    if (getCodTituloList().isSelectionEmpty()) {
                        JOptionPane.showMessageDialog(null, "Caso exista valores na lista, por favor, selecione algum para consulta", "Valor para consulta n�o selecionado", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        int tpObj = 0;
                        tpObj = retornaTipoObjeto();
                        if (tpObj > 0) {
                            int cod;
                            cod = ExtraiCodigo.extraiCodigo(getCodTituloList().getSelectedValue().toString());
                            AcessaDados.consultaRecurso(getConexao(), tpObj, cod);
                            setCodigoConsultado(cod);
                            getExcluirButton().setEnabled(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Por favor, selecione um tipo de recurso", "Tipo de Recurso n�o selecionado", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            });
        }
        return (consultarButton);
    }

    /**
	 * Implmenta as a��es a serem realizadas quando o usu�rio clica no bot�o 'Excluir'
	 */
    private JButton getExcluirButton() {
        if (excluirButton == null) {
            excluirButton = new JButton("Excluir");
            excluirButton.setEnabled(false);
            excluirButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    if (JOptionPane.showConfirmDialog(null, "Confirmar exlus�o?") == JOptionPane.OK_OPTION) {
                        AcessaDados.excluirRecurso(getConexao(), retornaTipoObjeto(), getCodigoConsultado());
                        recarregaCodTituloList();
                        setTextFieldsDefaul();
                        getTipoMidiaBG().clearSelection();
                    }
                }
            });
        }
        return (excluirButton);
    }

    /**
	 * Implmenta as a��es a serem realizadas quando o usu�rio clica no bot�o 'Alterar'
	 */
    private JButton getAlterarButton() {
        if (alterarButton == null) {
            alterarButton = new JButton("Alterar");
            alterarButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Vector<String> v = new Vector<String>();
                    getTituloTF().setEditable(true);
                    getTituloTF().setBorder(BorderFactory.createLineBorder(Color.gray));
                    getComentarioTA().setBackground(Color.white);
                    getComentarioTA().setEditable(true);
                    getComentarioTA().setBorder(BorderFactory.createLineBorder(Color.gray));
                    getDescricaoTA().setBackground(Color.white);
                    getDescricaoTA().setEditable(true);
                    getDescricaoSP().setBorder(BorderFactory.createLineBorder(Color.gray));
                    v.add(getTituloTF().getText());
                    v.add(getComentarioTA().getText());
                    v.add(getDescricaoTA().getText());
                    v.add(getUrlTF().getText());
                    if (retornaTipoObjeto() == 4) {
                        getUrlTF().setEditable(true);
                        getUrlTF().setBorder(BorderFactory.createLineBorder(Color.gray));
                        getTipoMidiaPanel().setBorder(BorderFactory.createLineBorder(Color.gray));
                        getVideoRB().setEnabled(true);
                        getImagemRB().setEnabled(true);
                        getBrowseButton().setEnabled(true);
                        if (getImagemRB().isSelected() || getVideoRB().isSelected()) {
                            if (getImagemRB().isSelected()) {
                                v.add("img");
                            } else {
                                v.add("vid");
                            }
                        }
                    }
                    setEditaveis(v);
                    getConfirmarButton().setEnabled(true);
                    getCancelarButton().setEnabled(true);
                }
            });
        }
        return (alterarButton);
    }

    /**
	 * Implmenta as a��es a serem realizadas quando o usu�rio clica no bot�o 'Confirmar'
	 */
    private JButton getConfirmarButton() {
        if (confirmarButton == null) {
            confirmarButton = new JButton("Confirmar");
            confirmarButton.setEnabled(false);
            confirmarButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    if (verificaCamposEditaveis(retornaTipoObjeto()) == 0) {
                        if (AcessaDados.alteraRecurso(getConexao(), retornaTipoObjeto(), getCodigoConsultado())) {
                            JOptionPane.showMessageDialog(null, "Altera��o realizada com sucesso", "Alterar recurso", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Ocorreu uma falha ao tentar realizar a alterara��o", "Alterar recurso", JOptionPane.ERROR_MESSAGE);
                        }
                        getTituloTF().setEditable(false);
                        getTituloTF().setBorder(BorderFactory.createLineBorder(Color.lightGray));
                        getComentarioTA().setEditable(false);
                        getComentarioTA().setBackground(null);
                        getComentarioTA().setBorder(BorderFactory.createLineBorder(Color.lightGray));
                        getDescricaoTA().setEditable(false);
                        getDescricaoTA().setBackground(null);
                        getDescricaoSP().setBorder(BorderFactory.createLineBorder(Color.lightGray));
                        if (retornaTipoObjeto() == 4) {
                            getUrlTF().setEditable(false);
                            getUrlTF().setBorder(BorderFactory.createLineBorder(Color.lightGray));
                            getTipoMidiaPanel().setBorder(BorderFactory.createLineBorder(Color.lightGray));
                            getVideoRB().setEnabled(false);
                            getImagemRB().setEnabled(false);
                            getBrowseButton().setEnabled(false);
                        }
                        getConfirmarButton().setEnabled(false);
                        getCancelarButton().setEnabled(false);
                        recarregaCodTituloList();
                        getCodTituloSP().setViewportView(getCodTituloList());
                    } else {
                        JOptionPane.showMessageDialog(null, "Campos com bordas vermelhas s�o obrigat�rios", "Alterar dados de recuros", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });
        }
        return (confirmarButton);
    }

    /**
	 * Implmenta as a��es a serem realizadas quando o usu�rio clica no bot�o 'Cancelar'
	 */
    protected JButton getCancelarButton() {
        if (cancelarButton == null) {
            cancelarButton = new JButton("Cancelar");
            cancelarButton.setEnabled(false);
            cancelarButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    getConfirmarButton().setEnabled(false);
                    getCancelarButton().setEnabled(false);
                    getTituloTF().setEditable(false);
                    getTituloTF().setBorder(BorderFactory.createLineBorder(Color.lightGray));
                    getComentarioTA().setBackground(null);
                    getComentarioTA().setEditable(false);
                    getComentarioTA().setBorder(BorderFactory.createLineBorder(Color.lightGray));
                    getDescricaoTA().setBackground(null);
                    getDescricaoTA().setEditable(false);
                    getDescricaoSP().setBorder(BorderFactory.createLineBorder(Color.lightGray));
                    if (retornaTipoObjeto() == 4) {
                        getUrlTF().setEditable(false);
                        getUrlTF().setBorder(BorderFactory.createLineBorder(Color.lightGray));
                        getTipoMidiaPanel().setBorder(BorderFactory.createLineBorder(Color.lightGray));
                        getVideoRB().setEnabled(false);
                        getImagemRB().setEnabled(false);
                        getBrowseButton().setEnabled(false);
                    }
                    getTituloTF().setText(getEditaveis().get(0));
                    getComentarioTA().setText(getEditaveis().get(1));
                    getDescricaoTA().setText(getEditaveis().get(2));
                    getUrlTF().setText(getEditaveis().get(3));
                    if (getEditaveis().size() > 3) {
                        if (getEditaveis().get(4).equals("img")) {
                            getImagemRB().setSelected(true);
                        } else {
                            getVideoRB().setSelected(true);
                        }
                    }
                }
            });
        }
        return (cancelarButton);
    }

    /**
	 * Implmenta as a��es a serem realizadas quando o usu�rio clica no bot�o 'Sair'
	 */
    private JButton getSairButton() {
        if (sairButton == null) {
            sairButton = new JButton("Sair");
            sairButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    getParent().setVisible(true);
                    setGuiDefault();
                    setVisible(false);
                }
            });
        }
        return (sairButton);
    }

    private JPanel getAlterarPanel() {
        if (alterarPanel == null) {
            alterarPanel = new JPanel();
            alterarPanel.setLayout(null);
        }
        return (alterarPanel);
    }

    private JSeparator getSeparator() {
        if (separator == null) {
            separator = new JSeparator(SwingConstants.HORIZONTAL);
        }
        return (separator);
    }

    /**
	 * Configura os componentes a serem mostrados na tela
	 */
    protected JLabel getTipoObjetoLabel() {
        if (tipoObjetoLabel == null) {
            tipoObjetoLabel = new JLabel("Tipo de Objeto");
        }
        return (tipoObjetoLabel);
    }

    protected JPanel getTipoObjetoPanel() {
        if (tipoObjetoPanel == null) {
            tipoObjetoPanel = new JPanel();
            tipoObjetoPanel.setLayout(null);
            tipoObjetoPanel.setBorder(BorderFactory.createLineBorder(Color.lightGray));
            tipoObjetoPanel.add(getTutorialRB(), null);
            tipoObjetoPanel.add(getTutorialLabel(), null);
            tipoObjetoPanel.add(getCapituloRB(), null);
            tipoObjetoPanel.add(getCapituloLabel(), null);
            tipoObjetoPanel.add(getLicaoRB(), null);
            tipoObjetoPanel.add(getLicaoLabel(), null);
            tipoObjetoPanel.add(getMidiaRB(), null);
            tipoObjetoPanel.add(getMidiaLabel(), null);
        }
        return (tipoObjetoPanel);
    }

    /**
	 * Implmenta as a��es a serem realizadas quando o usu�rio clica no bot�o-radio 'Tutorial'
	 */
    private JRadioButton getTutorialRB() {
        if (tutorialRB == null) {
            tutorialRB = new JRadioButton();
            tutorialRB.setBounds(10, 10, 20, 15);
            tutorialRB.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    getCodTituloList().setListData(AcessaDados.retornaTutorialID(getConexao()));
                    setTextFieldsDefaul();
                }
            });
        }
        return (tutorialRB);
    }

    public JLabel getTutorialLabel() {
        if (tutorialLabel == null) {
            tutorialLabel = new JLabel("Tutorial");
            tutorialLabel.setBounds(35, 10, 85, 15);
        }
        return (tutorialLabel);
    }

    /**
	 * Implmenta as a��es a serem realizadas quando o usu�rio clica no bot�o-radio 'Cap�tulo'
	 */
    public JRadioButton getCapituloRB() {
        if (capituloRB == null) {
            capituloRB = new JRadioButton();
            capituloRB.setBounds(10, 30, 20, 15);
            capituloRB.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    getCodTituloList().setListData(AcessaDados.retornaCapituloID(getConexao()));
                    setTextFieldsDefaul();
                }
            });
        }
        return (capituloRB);
    }

    protected JLabel getCapituloLabel() {
        if (capituloLabel == null) {
            capituloLabel = new JLabel("Cap�tulo");
            capituloLabel.setBounds(35, 30, 85, 15);
        }
        return (capituloLabel);
    }

    /**
	 * Implmenta as a��es a serem realizadas quando o usu�rio clica no bot�o-radio 'Li��o'
	 */
    public JRadioButton getLicaoRB() {
        if (licaoRB == null) {
            licaoRB = new JRadioButton();
            licaoRB.setBounds(10, 50, 20, 15);
            licaoRB.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    getCodTituloList().setListData(AcessaDados.retornaLicaoID(getConexao()));
                    setTextFieldsDefaul();
                }
            });
        }
        return (licaoRB);
    }

    protected JLabel getLicaoLabel() {
        if (licaoLabel == null) {
            licaoLabel = new JLabel("Li��o");
            licaoLabel.setBounds(35, 50, 85, 15);
        }
        return (licaoLabel);
    }

    /**
	 * Implmenta as a��es a serem realizadas quando o usu�rio clica no bot�o-radio 'M�dia'
	 */
    public JRadioButton getMidiaRB() {
        if (midiaRB == null) {
            midiaRB = new JRadioButton();
            midiaRB.setBounds(10, 70, 20, 15);
            midiaRB.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    getCodTituloList().setListData(AcessaDados.retornaMidiaID(getConexao()));
                    setTextFieldsDefaul();
                }
            });
        }
        return (midiaRB);
    }

    protected JLabel getMidiaLabel() {
        if (midiaLabel == null) {
            midiaLabel = new JLabel("M�dia");
            midiaLabel.setBounds(35, 70, 85, 15);
        }
        return (midiaLabel);
    }

    protected JLabel getCodigoLabel() {
        if (codigoLabel == null) {
            codigoLabel = new JLabel("C�digo");
        }
        return (codigoLabel);
    }

    public static JTextField getCodigoTF() {
        if (codigoTF == null) {
            codigoTF = new JTextField();
            codigoTF.setEditable(false);
            codigoTF.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        }
        return (codigoTF);
    }

    protected JLabel getTituloLabel() {
        if (tituloLabel == null) {
            tituloLabel = new JLabel("T�tulo");
        }
        return (tituloLabel);
    }

    public static JTextField getTituloTF() {
        if (tituloTF == null) {
            tituloTF = new JTextField();
            tituloTF.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        }
        return (tituloTF);
    }

    protected JLabel getAutorLabel() {
        if (autorLabel == null) {
            autorLabel = new JLabel("Autor");
        }
        return (autorLabel);
    }

    public static JTextField getAutorTF() {
        if (autorTF == null) {
            autorTF = new JTextField();
            autorTF.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        }
        return (autorTF);
    }

    protected JLabel getComentarioLabel() {
        if (comentarioLabel == null) {
            comentarioLabel = new JLabel("Coment�rio");
        }
        return (comentarioLabel);
    }

    public static JTextArea getComentarioTA() {
        if (comentarioTA == null) {
            comentarioTA = new JTextArea();
            comentarioTA.setBorder(BorderFactory.createLineBorder(Color.lightGray));
            comentarioTA.setLineWrap(true);
            comentarioTA.setBackground(null);
        }
        return (comentarioTA);
    }

    protected JLabel getDescricaoLabel() {
        if (descricaoLabel == null) {
            descricaoLabel = new JLabel("Descri��o");
        }
        return (descricaoLabel);
    }

    public static JTextArea getDescricaoTA() {
        if (descricaoTA == null) {
            descricaoTA = new JTextArea(10, 80);
            descricaoTA.setLineWrap(true);
            descricaoTA.setBackground(null);
        }
        return (descricaoTA);
    }

    protected JScrollPane getDescricaoSP() {
        if (descricaoSP == null) {
            descricaoSP = new JScrollPane(getDescricaoTA());
            descricaoSP.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        }
        return (descricaoSP);
    }

    protected JLabel getUrlLabel() {
        if (urlLabel == null) {
            urlLabel = new JLabel("URL da M�dia");
        }
        return (urlLabel);
    }

    public static JTextField getUrlTF() {
        if (urlTF == null) {
            urlTF = new JTextField();
            urlTF.setBorder(BorderFactory.createLineBorder(Color.lightGray));
        }
        return (urlTF);
    }

    /**
	 * Implmenta as a��es a serem realizadas quando o usu�rio clica no bot�o 'Browse'
	 */
    public JButton getBrowseButton() {
        if (browseButton == null) {
            browseButton = new JButton("Browse");
            browseButton.setEnabled(false);
            browseButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    JFileChooser jfc = new JFileChooser();
                    URI uri = null;
                    URL url = null;
                    int flag = jfc.showOpenDialog(null);
                    if (flag == JFileChooser.APPROVE_OPTION) {
                        try {
                            uri = jfc.getSelectedFile().toURI();
                            url = uri.toURL();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        String midiaURL = url.toString().substring(6);
                        TelaCEA.getUrlTF().setText(midiaURL);
                        System.out.println(midiaURL.length());
                    }
                }
            });
        }
        return (browseButton);
    }

    protected JLabel getTipoMidiaLabel() {
        if (tipoMidiaLabel == null) {
            tipoMidiaLabel = new JLabel("Tipo de M�dia");
        }
        return (tipoMidiaLabel);
    }

    protected JPanel getTipoMidiaPanel() {
        if (tipoMidiaPanel == null) {
            tipoMidiaPanel = new JPanel();
            tipoMidiaPanel.setLayout(null);
            tipoMidiaPanel.setBorder(BorderFactory.createLineBorder(Color.lightGray));
            getImagemRB().setBounds(20, 7, 20, 15);
            tipoMidiaPanel.add(getImagemRB(), null);
            getImagemLabel().setBounds(40, 7, 85, 15);
            tipoMidiaPanel.add(getImagemLabel(), null);
            getVideoRB().setBounds(120, 7, 20, 15);
            tipoMidiaPanel.add(getVideoRB(), null);
            getVideoLabel().setBounds(140, 7, 85, 15);
            tipoMidiaPanel.add(getVideoLabel(), null);
        }
        return (tipoMidiaPanel);
    }

    public static JRadioButton getImagemRB() {
        if (imagemRB == null) {
            imagemRB = new JRadioButton();
        }
        return (imagemRB);
    }

    protected JLabel getImagemLabel() {
        if (imagemLabel == null) {
            imagemLabel = new JLabel("Imagem");
        }
        return (imagemLabel);
    }

    public static JRadioButton getVideoRB() {
        if (videoRB == null) {
            videoRB = new JRadioButton();
        }
        return (videoRB);
    }

    protected JLabel getVideoLabel() {
        if (videoLabel == null) {
            videoLabel = new JLabel("V�deo");
        }
        return (videoLabel);
    }

    protected void setTipoObjetoBG() {
        if (tipoObjetoBG == null) {
            tipoObjetoBG = new ButtonGroup();
            tipoObjetoBG.add(getTutorialRB());
            tipoObjetoBG.add(getCapituloRB());
            tipoObjetoBG.add(getLicaoRB());
            tipoObjetoBG.add(getMidiaRB());
        }
    }

    public ButtonGroup getTipoObjBG() {
        return tipoObjetoBG;
    }

    protected void setTipoMidiaBG() {
        if (tipoMidiaBG == null) {
            tipoMidiaBG = new ButtonGroup();
            tipoMidiaBG.add(getVideoRB());
            tipoMidiaBG.add(getImagemRB());
        }
    }

    public ButtonGroup getTipoMidiaBG() {
        return tipoMidiaBG;
    }

    /**
	 * O m�todo <b>retornaTipoObjeto()</b> verifica qual <i>radio button</i> da Tela de Consulta, Exclus�o e Altera��o (TelaCEA) est� selecionado, e 
	 * retorna um inteiro representando o escolhido.
	 * @return int Tipo de recurso (Tutorial, Capitulo, Licao ou M�dia) selecionado pelo usu�rio a partir dos <i>radios buttons</i> 
	 * da Tela de Cadastro (TelaCEA).
	 */
    public int retornaTipoObjeto() {
        if (getTutorialRB().isSelected()) {
            return (1);
        } else if (getCapituloRB().isSelected()) {
            return (2);
        } else if (getLicaoRB().isSelected()) {
            return (3);
        } else if (getMidiaRB().isSelected()) {
            return (4);
        } else {
            return (0);
        }
    }

    /**
	 * Retorna os campos de textos e bot�es para o estado referente ao momento em que a tela de consulta � aberta
	 */
    public void setTextFieldsDefaul() {
        getCodigoTF().setText("");
        getTituloTF().setText("");
        getTituloTF().setBorder(BorderFactory.createLineBorder(Color.lightGray));
        getTituloTF().setBackground(null);
        getAutorTF().setText("");
        getAutorTF().setBackground(null);
        getComentarioTA().setText("");
        getComentarioTA().setBorder(BorderFactory.createLineBorder(Color.lightGray));
        getComentarioTA().setBackground(null);
        getDescricaoTA().setText("");
        getDescricaoSP().setBorder(BorderFactory.createLineBorder(Color.lightGray));
        getDescricaoTA().setBackground(null);
        getUrlTF().setText("");
        getUrlTF().setBorder(BorderFactory.createLineBorder(Color.lightGray));
        getUrlTF().setBackground(null);
        getExcluirButton().setEnabled(false);
    }

    /**
	 * Retorna os elementos para o estado referete ao momento em que a tela de consulta � aberta
	 */
    public void setGuiDefault() {
        getTipoObjBG().clearSelection();
        setListNull();
        setTextFieldsDefaul();
        getTipoMidiaBG().clearSelection();
    }

    /**
	 * Atribui um valor, passado por par�metro, para a vari�vel que guarda o c�digo do recurso consultado
	 * no momento do acionamento do bot�o 'Consultar'
	 * @param cod
	 */
    private void setCodigoConsultado(final int cod) {
        this.codConsultado = cod;
    }

    /**
	 * Retorna o valor do atributo 'codConsultado'
	 */
    public int getCodigoConsultado() {
        return codConsultado;
    }

    /**
	 * Verfica se os campos de texto e alguns bot�es utilizados no momento da altera��o das informa��es 
	 * de algum recurso est�o corretamente preenchidos pelo usu�rio.
	 * @param tipo Tipo de Recurso
	 */
    public int verificaCamposEditaveis(final int tipo) {
        int flag = 0;
        if (getTituloTF().getText().trim().equals("")) {
            getTituloTF().setBorder(BorderFactory.createLineBorder(Color.red));
            flag += 1;
        } else {
            getTituloTF().setBorder(BorderFactory.createLineBorder(Color.gray));
        }
        if (getComentarioTA().getText().trim().equals("")) {
            getComentarioTA().setBorder(BorderFactory.createLineBorder(Color.red));
            flag += 1;
        } else {
            getComentarioTA().setBorder(BorderFactory.createLineBorder(Color.gray));
        }
        if (getDescricaoTA().getText().trim().equals("")) {
            getDescricaoSP().setBorder(BorderFactory.createLineBorder(Color.red));
            flag += 1;
        } else {
            getDescricaoSP().setBorder(BorderFactory.createLineBorder(Color.gray));
        }
        if (tipo == 4) {
            if (getUrlTF().getText().trim().equals("")) {
                getUrlTF().setBorder(BorderFactory.createLineBorder(Color.red));
                flag += 1;
            } else {
                getUrlTF().setBorder(BorderFactory.createLineBorder(Color.gray));
            }
            if (!getImagemRB().isSelected() && !getVideoRB().isSelected()) {
                getTipoMidiaPanel().setBorder(BorderFactory.createLineBorder(Color.red));
                flag += 1;
            } else {
                getTipoMidiaPanel().setBorder(BorderFactory.createLineBorder(Color.gray));
            }
        }
        return flag;
    }

    /**
	 * Met�do utilizado para capturar os dados logo ap�s o usu�rio clicar no bot�o ALTERAR. Sendo assim
	 * este m�todo retorna um vetor de strings correspondente aos campos editaveis (titulo, comentario, descri��o, e url).
	 * Isto faz com que os dados voltem ao estado do momento em que o bot�o ALTERAR foi acionado, e n�o sejam alterados
	 * quando o usu�rio digitar alguma nova informa��o e, por algum motivo, desiste da altera��o e clica em CANCELAR.
	 */
    public void setEditaveis(Vector<String> v) {
        editaveis = v;
    }

    /**
	 * Retorna o valor do atributo editaveis
	 */
    public Vector<String> getEditaveis() {
        return editaveis;
    }

    /**
	 * Rcarrega a lista de codigo e t�tulo de recursos
	 */
    public void recarregaCodTituloList() {
        int tp = retornaTipoObjeto();
        if (tp == 1) {
            getCodTituloList().setListData(AcessaDados.retornaTutorialID(getConexao()));
        } else if (tp == 2) {
            getCodTituloList().setListData(AcessaDados.retornaCapituloID(getConexao()));
        } else if (tp == 3) {
            getCodTituloList().setListData(AcessaDados.retornaLicaoID(getConexao()));
        } else {
            getCodTituloList().setListData(AcessaDados.retornaMidiaID(getConexao()));
        }
    }
}
