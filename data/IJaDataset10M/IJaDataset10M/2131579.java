package gui.musicas;

import exceptions.DataException;
import fachada.Fachada;
import gui.colecaodiscos.GerarColecaoDiscosDialog;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import util.GlobalPlayer;
import util.Util;
import classesbasicas.Assunto;
import classesbasicas.Cantor;
import classesbasicas.Colecao;
import classesbasicas.Musica;
import classesbasicas.Qualidade;
import classesbasicas.Tipo;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class ProcurarMusicasPanel extends JPanel {

    {
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final long serialVersionUID = 1L;

    private JPanel dadosDaProcuraPanel = null;

    private JLabel nomeLabel = null;

    private JTextField nomeTextField = null;

    private JPanel tabelaPanel = null;

    private JScrollPane tabelaScrollPane = null;

    private JLabel colecaoBuscaLabel;

    private JTextField anoTextField;

    private JLabel anoLabel;

    private JComboBox colecaoBuscaComboBox;

    private JButton excluirColecaoButton;

    private JButton removerMusicasDaColecaoButton;

    private JButton novaColecaoButton;

    private JButton adicionarAColecaoButton;

    private JComboBox colecoesComboBox;

    private JTable musicasTable = null;

    private JPanel botoesPanel = null;

    private JButton editarButton = null;

    private JButton excluirButton = null;

    private JButton playButton = null;

    private JButton adicionarListaReproducaoButton = null;

    private List<Musica> musicas = null;

    private Vector<String> nomesCampos = null;

    private Vector<Vector<String>> musicasDados = null;

    private DefaultTableModel musicasTableModel = null;

    private JLabel cantorLabel = null;

    private JTextField cantorTextField = null;

    private JPanel controlesPanel = null;

    private MusicasInternalFrame pai = null;

    private JLabel ritmoLabel = null;

    private JLabel assuntoLabel = null;

    private JComboBox ritmoComboBox = null;

    private JComboBox assuntoComboBox = null;

    private JLabel observacaoLabel = null;

    private JTextField observacaoTextField = null;

    private JLabel letraLabel = null;

    private JTextField letraTextField = null;

    private JLabel qualidadeLabel = null;

    private JComboBox qualidadeComboBox = null;

    private JLabel numeroRegistrosLabel = null;

    private JTextField numeroRegistrosTextField = null;

    private JPanel controlesInPanel = null;

    private JButton gerarColecaoButton = null;

    private List<Colecao> colecoes = null;

    /**
	 * This is the default constructor
	 */
    public ProcurarMusicasPanel() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        letraLabel = new JLabel();
        letraLabel.setText("Letra");
        GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
        gridBagConstraints14.gridx = 0;
        gridBagConstraints14.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints14.ipadx = 0;
        gridBagConstraints14.ipady = 22;
        gridBagConstraints14.gridheight = 1;
        gridBagConstraints14.gridwidth = 1;
        gridBagConstraints14.gridy = 4;
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.gridx = 1;
        gridBagConstraints7.insets = new Insets(0, 16, 46, 0);
        gridBagConstraints7.fill = GridBagConstraints.BOTH;
        gridBagConstraints7.gridy = 3;
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints6.insets = new Insets(0, 0, 4, 0);
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.fill = GridBagConstraints.BOTH;
        gridBagConstraints4.ipadx = 0;
        gridBagConstraints4.gridy = 3;
        this.setLayout(new GridBagLayout());
        this.setSize(780, 550);
        this.add(getDadosDaProcuraPanel(), gridBagConstraints6);
        this.add(getTabelaPanel(), gridBagConstraints4);
        this.add(getBotoesPanel(), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 14, 0, 0), 0, 0));
        this.add(getControlesPanel(), gridBagConstraints14);
    }

    /**
	 * This method initializes dadosDaProcuraPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getDadosDaProcuraPanel() {
        if (dadosDaProcuraPanel == null) {
            GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
            gridBagConstraints33.fill = GridBagConstraints.BOTH;
            gridBagConstraints33.gridy = 5;
            gridBagConstraints33.weightx = 1.0;
            gridBagConstraints33.gridx = 0;
            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.gridx = 0;
            gridBagConstraints21.anchor = GridBagConstraints.SOUTHWEST;
            gridBagConstraints21.gridy = 4;
            qualidadeLabel = new JLabel();
            qualidadeLabel.setText("Qualidade");
            GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
            gridBagConstraints61.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints61.gridy = 5;
            gridBagConstraints61.weightx = 1.0;
            gridBagConstraints61.gridwidth = 1;
            gridBagConstraints61.insets = new Insets(0, 16, 0, 0);
            gridBagConstraints61.anchor = GridBagConstraints.NORTHWEST;
            gridBagConstraints61.gridx = 1;
            GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
            gridBagConstraints51.anchor = GridBagConstraints.SOUTHWEST;
            gridBagConstraints51.gridy = 4;
            gridBagConstraints51.insets = new Insets(0, 16, 0, 0);
            gridBagConstraints51.gridx = 1;
            GridBagConstraints gridBagConstraints42 = new GridBagConstraints();
            gridBagConstraints42.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints42.gridy = 3;
            gridBagConstraints42.weightx = 1.0;
            gridBagConstraints42.anchor = GridBagConstraints.NORTHWEST;
            gridBagConstraints42.insets = new Insets(0, 12, 0, 0);
            gridBagConstraints42.gridx = 2;
            GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
            gridBagConstraints32.gridx = 2;
            gridBagConstraints32.anchor = GridBagConstraints.SOUTHWEST;
            gridBagConstraints32.insets = new Insets(0, 12, 0, 0);
            gridBagConstraints32.gridy = 2;
            observacaoLabel = new JLabel();
            observacaoLabel.setText("Observa��o");
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.gridy = 3;
            gridBagConstraints2.weightx = 1.0;
            gridBagConstraints2.insets = new Insets(0, 16, 0, 0);
            gridBagConstraints2.gridx = 1;
            GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
            gridBagConstraints15.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints15.gridy = 3;
            gridBagConstraints15.weightx = 1.0;
            gridBagConstraints15.anchor = GridBagConstraints.NORTHWEST;
            gridBagConstraints15.gridx = 0;
            GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
            gridBagConstraints31.gridx = 1;
            gridBagConstraints31.anchor = GridBagConstraints.SOUTHWEST;
            gridBagConstraints31.insets = new Insets(0, 16, 0, 0);
            gridBagConstraints31.gridy = 2;
            assuntoLabel = new JLabel();
            assuntoLabel.setText("Assunto");
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.gridx = 0;
            gridBagConstraints12.anchor = GridBagConstraints.SOUTHWEST;
            gridBagConstraints12.fill = GridBagConstraints.NONE;
            gridBagConstraints12.gridy = 2;
            ritmoLabel = new JLabel();
            ritmoLabel.setText("Ritmo");
            GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
            gridBagConstraints41.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints41.gridy = 1;
            gridBagConstraints41.weightx = 1.0;
            gridBagConstraints41.anchor = GridBagConstraints.NORTHWEST;
            gridBagConstraints41.insets = new Insets(0, 12, 0, 0);
            gridBagConstraints41.gridx = 2;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 2;
            gridBagConstraints3.anchor = GridBagConstraints.SOUTHWEST;
            gridBagConstraints3.insets = new Insets(0, 12, 0, 0);
            gridBagConstraints3.gridy = 0;
            cantorLabel = new JLabel();
            cantorLabel.setText("Cantor");
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.anchor = GridBagConstraints.SOUTHWEST;
            gridBagConstraints1.gridwidth = 2;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.gridx = 0;
            nomeLabel = new JLabel();
            nomeLabel.setText("Nome");
            dadosDaProcuraPanel = new JPanel();
            dadosDaProcuraPanel.setLayout(new GridBagLayout());
            dadosDaProcuraPanel.add(getLetraTextField(), new GridBagConstraints(2, 5, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 12, 0, 0), 0, 0));
            dadosDaProcuraPanel.add(letraLabel, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 0), 0, 0));
            dadosDaProcuraPanel.add(nomeLabel, gridBagConstraints1);
            dadosDaProcuraPanel.add(getNomeTextField(), gridBagConstraints);
            dadosDaProcuraPanel.add(cantorLabel, gridBagConstraints3);
            dadosDaProcuraPanel.add(getCantorTextField(), gridBagConstraints41);
            dadosDaProcuraPanel.add(ritmoLabel, gridBagConstraints12);
            dadosDaProcuraPanel.add(assuntoLabel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 0), 0, 0));
            dadosDaProcuraPanel.add(getRitmoComboBox(), gridBagConstraints15);
            atualizarRitmos();
            dadosDaProcuraPanel.add(getAssuntoComboBox(), new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 12, 0, 0), 0, 0));
            atualizarAssuntos();
            dadosDaProcuraPanel.add(observacaoLabel, gridBagConstraints32);
            dadosDaProcuraPanel.add(getObservacaoTextField(), gridBagConstraints42);
            dadosDaProcuraPanel.add(qualidadeLabel, gridBagConstraints21);
            dadosDaProcuraPanel.add(getQualidadeComboBox(), new GridBagConstraints(0, 5, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 120), 0, 0));
            dadosDaProcuraPanel.add(getColecaoBuscaLabel(), new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 0), 0, 0));
            dadosDaProcuraPanel.add(getColecaoBuscaComboBox(), new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 12, 0, 0), 0, 0));
            dadosDaProcuraPanel.add(getAnoLabel(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(0, 157, 0, 0), 0, 0));
            dadosDaProcuraPanel.add(getAnoTextField(), new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 156, 0, 0), 0, 0));
            atualizarQualidades();
        }
        return dadosDaProcuraPanel;
    }

    /**
	 * This method initializes nomeTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getNomeTextField() {
        if (nomeTextField == null) {
            nomeTextField = new JTextField();
            nomeTextField.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    procurarMusicas();
                }
            });
            nomeTextField.setColumns(10);
        }
        return nomeTextField;
    }

    /**
	 * This method initializes tabelaPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getTabelaPanel() {
        if (tabelaPanel == null) {
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.ipadx = 580;
            gridBagConstraints5.gridwidth = 4;
            gridBagConstraints5.insets = new Insets(0, 0, 0, 0);
            gridBagConstraints5.ipady = 312;
            tabelaPanel = new JPanel();
            tabelaPanel.setLayout(new GridBagLayout());
            tabelaPanel.add(getTabelaScrollPane(), gridBagConstraints5);
        }
        return tabelaPanel;
    }

    /**
	 * This method initializes tabelaScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getTabelaScrollPane() {
        if (tabelaScrollPane == null) {
            tabelaScrollPane = new JScrollPane();
            tabelaScrollPane.setViewportView(getMusicasTable());
        }
        return tabelaScrollPane;
    }

    /**
	 * This method initializes musicasTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
    private JTable getMusicasTable() {
        if (musicasTable == null) {
            musicasTable = new JTable();
            musicasTable.setModel(getMusicasTableModel());
            musicasTable.addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        showDialog(e);
                    }
                }

                private void showDialog(MouseEvent e) {
                    if (musicas != null) {
                        int row = musicasTable.rowAtPoint(e.getPoint());
                        if (row >= 0 && musicas.get(row) != null) {
                            visualizarMusica(musicas.get(row));
                        }
                    }
                }
            });
        }
        musicasTable.getColumn(musicasTable.getColumnName(0)).setPreferredWidth(130);
        musicasTable.getColumn(musicasTable.getColumnName(1)).setPreferredWidth(90);
        musicasTable.getColumn(musicasTable.getColumnName(2)).setPreferredWidth(30);
        musicasTable.getColumn(musicasTable.getColumnName(3)).setPreferredWidth(15);
        return musicasTable;
    }

    /**
	 * This method initializes botoesPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getBotoesPanel() {
        if (botoesPanel == null) {
            GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
            gridBagConstraints17.gridx = 0;
            gridBagConstraints17.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints17.insets = new Insets(0, 0, 20, 0);
            gridBagConstraints17.gridy = 2;
            GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
            gridBagConstraints16.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints16.gridy = 1;
            gridBagConstraints16.weightx = 1.0;
            gridBagConstraints16.insets = new Insets(0, 0, 25, 0);
            gridBagConstraints16.gridx = 0;
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.gridx = 0;
            gridBagConstraints13.anchor = GridBagConstraints.CENTER;
            gridBagConstraints13.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints13.gridy = 0;
            numeroRegistrosLabel = new JLabel();
            numeroRegistrosLabel.setText("N� de Registros");
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 0;
            gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints11.gridy = 4;
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.gridx = 0;
            gridBagConstraints10.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints10.gridy = 3;
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints9.insets = new Insets(20, 0, 0, 0);
            gridBagConstraints9.gridy = 5;
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 0;
            gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints8.insets = new Insets(20, 0, 0, 0);
            gridBagConstraints8.gridy = 6;
            botoesPanel = new JPanel();
            botoesPanel.setLayout(new GridBagLayout());
            botoesPanel.add(getEditarButton(), new GridBagConstraints(-1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(14, 8, 0, 8), 0, 0));
            botoesPanel.add(getExcluirButton(), new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 8, 0, 8), 0, 0));
            botoesPanel.add(getPlayButton(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(14, 8, 0, 8), 0, 0));
            botoesPanel.add(getAdicionarListaReproducaoButton(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 8, 0, 8), 0, 0));
            botoesPanel.add(numeroRegistrosLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 8, 0, 8), 0, 0));
            botoesPanel.add(getNumeroRegistrosTextField(), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 8, 0, 8), 0, 0));
            botoesPanel.add(getGerarColecaoButton(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(20, 8, 0, 8), 0, 0));
            botoesPanel.add(getColecoesComboBox(), new GridBagConstraints(0, 13, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
            botoesPanel.add(getAdicionarAColecaoButton(), new GridBagConstraints(0, 14, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
            botoesPanel.add(getNovaColecaoButton(), new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(20, 0, 0, 0), 0, 0));
            botoesPanel.add(getRemoverMusicasDaColecaoButton(), new GridBagConstraints(0, 15, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
            botoesPanel.add(getExcluirColecaoButton(), new GridBagConstraints(0, 12, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        }
        return botoesPanel;
    }

    /**
	 * This method initializes editarButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getEditarButton() {
        if (editarButton == null) {
            editarButton = new JButton();
            editarButton.setFocusable(false);
            editarButton.setText("Editar");
            editarButton.setIcon(new ImageIcon(getClass().getResource("/figuras/icones/edit16x16.png")));
            editarButton.setToolTipText("Edita a M�sica Selecionada");
            editarButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int indice = getMusicasTable().getSelectedRow();
                    if (pai != null && indice >= 0) {
                        pai.editarMusica(musicas.get(indice));
                    }
                }
            });
        }
        return editarButton;
    }

    /**
	 * This method initializes excluirButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getExcluirButton() {
        if (excluirButton == null) {
            excluirButton = new JButton();
            excluirButton.setFocusable(false);
            excluirButton.setText("Excluir");
            excluirButton.setIcon(new ImageIcon(getClass().getResource("/figuras/icones/delete2.png")));
            excluirButton.setToolTipText("Excluir a M�sica Selecionada");
            excluirButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int indices[] = getMusicasTable().getSelectedRows();
                    if (indices == null || indices.length <= 0) return;
                    int confirm = JOptionPane.showConfirmDialog(ProcurarMusicasPanel.this, "Tem certeza de que deseja excluir as " + indices.length + " M�sicas Selecionadas?\nESSA OPERA��O N�O PODE SER DESFEITA.", "Confirmar Exclus�o", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.OK_OPTION) {
                        for (int indice : indices) {
                            Musica m = musicas.get(indice);
                            if (indice >= 0) {
                                String nomeArquivo = Util.getDiretorioBase() + File.separator + m.getDiretorio() + File.separator + m.getNomeArquivo();
                                File arquivo = new File(nomeArquivo);
                                if (arquivo.delete() || !arquivo.exists()) {
                                    try {
                                        Fachada.excluirMusica(m);
                                    } catch (DataException e1) {
                                        e1.printStackTrace();
                                        JOptionPane.showMessageDialog(ProcurarMusicasPanel.this, "N�o foi poss�vel remover a m�sica do BD.", "Erro.", JOptionPane.ERROR_MESSAGE);
                                        pai.musicaFoiApagada();
                                        return;
                                    }
                                }
                            }
                        }
                        JOptionPane.showMessageDialog(ProcurarMusicasPanel.this, "M�sicas exclu�das com Sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        pai.musicaFoiApagada();
                    }
                }
            });
        }
        return excluirButton;
    }

    /**
	 * This method initializes playButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getPlayButton() {
        if (playButton == null) {
            playButton = new JButton();
            playButton.setFocusable(false);
            playButton.setText("Play");
            playButton.setIcon(new ImageIcon(getClass().getResource("/figuras/icones/media_play_green.png")));
            playButton.setToolTipText("Toca as M�sicas Selecionadas no Player");
            playButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    play();
                }
            });
        }
        return playButton;
    }

    /**
	 * This method initializes adicionarListaReproducaoButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getAdicionarListaReproducaoButton() {
        if (adicionarListaReproducaoButton == null) {
            adicionarListaReproducaoButton = new JButton();
            adicionarListaReproducaoButton.setFocusable(false);
            adicionarListaReproducaoButton.setText("Adic.");
            adicionarListaReproducaoButton.setIcon(new ImageIcon(getClass().getResource("/figuras/media/document_add.png")));
            adicionarListaReproducaoButton.setToolTipText("Adiciona as M�sicas Selecionadas � Lista de Reprodu��o");
            adicionarListaReproducaoButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    adicionarAListaReproducao();
                }
            });
        }
        return adicionarListaReproducaoButton;
    }

    private DefaultTableModel getMusicasTableModel() {
        if (musicasTableModel == null) {
            musicasTableModel = new DefaultTableModel() {

                private static final long serialVersionUID = 2014828586714349050L;

                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            musicasTableModel.setDataVector(getMusicasDados(), getNomesCampos());
            getNumeroRegistrosTextField().setText("" + musicasDados.size());
        }
        return musicasTableModel;
    }

    /**
	 * This method initializes cantorTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getCantorTextField() {
        if (cantorTextField == null) {
            cantorTextField = new JTextField();
            cantorTextField.setColumns(10);
            cantorTextField.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    procurarMusicas();
                }
            });
        }
        return cantorTextField;
    }

    /**
	 * This method initializes controlesPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getControlesPanel() {
        if (controlesPanel == null) {
            controlesPanel = new JPanel();
            controlesPanel.setLayout(null);
            controlesPanel.setPreferredSize(new Dimension(246, 22));
            controlesPanel.add(getControlesInPanel(), null);
        }
        return controlesPanel;
    }

    public void procurarMusicas() {
        getMusicasTableModel().setDataVector(getMusicasDados(), getNomesCampos());
        getMusicasTable();
        getNumeroRegistrosTextField().setText("" + musicasDados.size());
    }

    public void setPai(MusicasInternalFrame pai) {
        this.pai = pai;
    }

    private void play() {
        int[] indices = getMusicasTable().getSelectedRows();
        if (indices != null && indices.length > 0) {
            List<Musica> ms = new ArrayList<Musica>();
            for (int i : indices) {
                ms.add(musicas.get(i));
            }
            player.GlobalPlayer.getGlobalPlayer().open(ms);
        }
    }

    private void adicionarAListaReproducao() {
        int[] indices = getMusicasTable().getSelectedRows();
        if (indices != null && indices.length > 0) {
            List<Musica> ms = new ArrayList<Musica>();
            for (int i : indices) {
                ms.add(musicas.get(i));
            }
            player.GlobalPlayer.getGlobalPlayer().adicionarMusicasAoPlayer(ms);
        }
    }

    private List<Musica> getMusicas() {
        String nome = getNomeTextField().getText();
        String nomeCantor = getCantorTextField().getText();
        String ritmo = (String) getRitmoComboBox().getSelectedItem();
        String assunto = (String) getAssuntoComboBox().getSelectedItem();
        String observacao = getObservacaoTextField().getText();
        String qualidade = (String) getQualidadeComboBox().getSelectedItem();
        String letra = getLetraTextField().getText();
        Colecao colecao = null;
        int ano = 0;
        if (getColecaoBuscaComboBox().getSelectedIndex() > 0) {
            colecao = colecoes.get(getColecaoBuscaComboBox().getSelectedIndex() - 1);
        }
        String anoString = getAnoTextField().getText().trim();
        if (anoString.length() > 0) {
            if (anoString.matches("[0-9][0-9][0-9][0-9]")) {
                ano = Integer.parseInt(anoString);
            } else {
                JOptionPane.showMessageDialog(this, "O Campo Ano est� inv�lido. Por favor, preencha o Campo com 4 d�gitos num�ricos ou deixe-o em Branco.", "Campo Ano inv�lido", JOptionPane.ERROR_MESSAGE);
                ano = 0;
                getAnoTextField().setText("");
            }
        }
        musicas = new ArrayList<Musica>();
        try {
            musicas = Fachada.listarMusicasPorDiversos(nome, nomeCantor, ritmo, assunto, observacao, qualidade, letra, colecao, ano);
        } catch (DataException e) {
            e.printStackTrace();
        }
        return musicas;
    }

    private Vector<Vector<String>> getMusicasDados() {
        musicasDados = new Vector<Vector<String>>();
        for (Musica m : getMusicas()) {
            Vector<String> temp = new Vector<String>();
            temp.add(m.getNome());
            if (m.getCantores() != null && m.getCantores().size() > 0) {
                String cantores = "";
                for (Cantor c : m.getCantores()) {
                    cantores += c.getNome();
                    if (m.getCantores().indexOf(c) < m.getCantores().size() - 1) {
                        cantores += ", ";
                    }
                    temp.add(cantores);
                }
            } else {
                temp.add("");
            }
            if (m.getTipo() != null) {
                temp.add(m.getTipo().getTipo());
            } else {
                temp.add("");
            }
            if (m.getDuracao() > 0) {
                DecimalFormat df = new DecimalFormat("00");
                temp.add(m.getDuracao() / 60 + ":" + df.format(m.getDuracao() % 60));
            } else {
                temp.add("");
            }
            musicasDados.add(temp);
        }
        return musicasDados;
    }

    private Vector<String> getNomesCampos() {
        if (nomesCampos == null) {
            nomesCampos = new Vector<String>();
            nomesCampos.add("Nome");
            nomesCampos.add("Cantores");
            nomesCampos.add("Ritmo");
            nomesCampos.add("Dura��o");
        }
        return nomesCampos;
    }

    public void finalizar() {
        adicionarAListaReproducao();
    }

    /**
	 * This method initializes ritmoComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getRitmoComboBox() {
        if (ritmoComboBox == null) {
            ritmoComboBox = new JComboBox();
            ritmoComboBox.addKeyListener(new KeyAdapter() {

                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        procurarMusicas();
                        e.consume();
                    }
                }
            });
        }
        return ritmoComboBox;
    }

    /**
	 * This method initializes assuntoComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getAssuntoComboBox() {
        if (assuntoComboBox == null) {
            assuntoComboBox = new JComboBox();
            assuntoComboBox.addKeyListener(new KeyAdapter() {

                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        procurarMusicas();
                        e.consume();
                    }
                }
            });
        }
        return assuntoComboBox;
    }

    /**
	 * This method initializes observacaoTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getObservacaoTextField() {
        if (observacaoTextField == null) {
            observacaoTextField = new JTextField();
            observacaoTextField.setColumns(10);
            observacaoTextField.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    procurarMusicas();
                }
            });
        }
        return observacaoTextField;
    }

    /**
	 * This method initializes letraTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getLetraTextField() {
        if (letraTextField == null) {
            letraTextField = new JTextField();
            letraTextField.setColumns(10);
            letraTextField.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    procurarMusicas();
                }
            });
        }
        return letraTextField;
    }

    /**
	 * This method initializes qualidadeComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getQualidadeComboBox() {
        if (qualidadeComboBox == null) {
            qualidadeComboBox = new JComboBox();
            qualidadeComboBox.addKeyListener(new KeyAdapter() {

                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        procurarMusicas();
                        e.consume();
                    }
                }
            });
        }
        return qualidadeComboBox;
    }

    public void atualizarRitmos() {
        try {
            List<Tipo> tipos = Fachada.listarTipos();
            String s = (String) getRitmoComboBox().getSelectedItem();
            getRitmoComboBox().removeAllItems();
            getRitmoComboBox().addItem("");
            for (Tipo t : tipos) {
                getRitmoComboBox().addItem(t.getTipo());
            }
            if (s != null && !s.equals("")) {
                getRitmoComboBox().setSelectedItem(s);
            }
        } catch (DataException e) {
            e.printStackTrace();
        }
    }

    public void atualizarAssuntos() {
        try {
            List<Assunto> assuntos = Fachada.listarAssuntos();
            String s = (String) getAssuntoComboBox().getSelectedItem();
            getAssuntoComboBox().removeAllItems();
            getAssuntoComboBox().addItem("");
            for (Assunto a : assuntos) {
                getAssuntoComboBox().addItem(a.getAssunto());
            }
            if (s != null && !s.equals("")) {
                getAssuntoComboBox().setSelectedItem(s);
            }
        } catch (DataException e) {
            e.printStackTrace();
        }
    }

    public void atualizarQualidades() {
        try {
            List<Qualidade> qualidades = Fachada.listarQualidades();
            String s = (String) getQualidadeComboBox().getSelectedItem();
            getQualidadeComboBox().removeAllItems();
            getQualidadeComboBox().addItem("");
            for (Qualidade q : qualidades) {
                getQualidadeComboBox().addItem(q.getQualidade());
            }
            if (s != null && !s.equals("")) {
                getQualidadeComboBox().setSelectedItem(s);
            }
        } catch (DataException e) {
            e.printStackTrace();
        }
    }

    private void visualizarMusica(Musica m) {
        VisualizarMusicaDialog dialogo = new VisualizarMusicaDialog((Frame) pai.getParent().getParent().getParent().getParent().getParent(), m);
        dialogo.setResizable(false);
        dialogo.setLocationRelativeTo((Frame) pai.getParent().getParent().getParent().getParent().getParent());
        dialogo.setModal(true);
        dialogo.setVisible(true);
    }

    /**
	 * This method initializes numeroRegistrosTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getNumeroRegistrosTextField() {
        if (numeroRegistrosTextField == null) {
            numeroRegistrosTextField = new JTextField();
            numeroRegistrosTextField.setEditable(false);
            numeroRegistrosTextField.setHorizontalAlignment(JTextField.RIGHT);
        }
        return numeroRegistrosTextField;
    }

    /**
	 * This method initializes controlesInPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getControlesInPanel() {
        if (controlesInPanel == null) {
            controlesInPanel = new JPanel();
            controlesInPanel.setLayout(new BorderLayout());
            controlesInPanel.setBounds(new Rectangle(0, 0, 602, 21));
        }
        return controlesInPanel;
    }

    /**
	 * This method initializes gerarColecaoButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getGerarColecaoButton() {
        if (gerarColecaoButton == null) {
            gerarColecaoButton = new JButton();
            gerarColecaoButton.setText("Gerar Cole��o");
            gerarColecaoButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    gerarColecao();
                }
            });
            gerarColecaoButton.setFocusable(false);
            gerarColecaoButton.setToolTipText("Gera uma Cole��o com as M�sicas ao Lado");
        }
        return gerarColecaoButton;
    }

    private void gerarColecao() {
        if (musicas != null && musicas.size() != 0) {
            GerarColecaoDiscosDialog dialogo = new GerarColecaoDiscosDialog(musicas, pai.getFrameOwner());
            dialogo.setResizable(false);
            dialogo.setLocationRelativeTo(pai.getFrameOwner());
            dialogo.setModal(true);
            dialogo.setVisible(true);
            System.out.println("largura: " + dialogo.getSize().width);
            System.out.println("altura: " + dialogo.getSize().height);
        }
    }

    private JComboBox getColecoesComboBox() {
        if (colecoesComboBox == null) {
            colecoesComboBox = new JComboBox();
            atualizarColecoes();
        }
        return colecoesComboBox;
    }

    private JButton getAdicionarAColecaoButton() {
        if (adicionarAColecaoButton == null) {
            adicionarAColecaoButton = new JButton();
            adicionarAColecaoButton.setText("Adicionar M�sicas");
            adicionarAColecaoButton.setFocusable(false);
            adicionarAColecaoButton.setToolTipText("Adicionar as M�sicas Selecionadas � Cole��o");
            adicionarAColecaoButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    adicionarMusicasSelecionadasAColecao();
                }
            });
        }
        return adicionarAColecaoButton;
    }

    private JButton getNovaColecaoButton() {
        if (novaColecaoButton == null) {
            novaColecaoButton = new JButton();
            novaColecaoButton.setText("Nova Cole��o");
            novaColecaoButton.setFocusable(false);
            novaColecaoButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    cadastrarColecao();
                }
            });
        }
        return novaColecaoButton;
    }

    private void cadastrarColecao() {
        String nome = "Nome";
        JTextField nomeTextField = new JTextField();
        String descricao = "Descri��o";
        JTextField descricaoTextField = new JTextField();
        Object[] itens = { nome, nomeTextField, descricao, descricaoTextField };
        if (JOptionPane.showConfirmDialog(this, itens, "Nova Cole��o", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            if (nomeTextField.getText() != null && nomeTextField.getText().length() > 0) {
                Colecao c = new Colecao();
                c.setNome(nomeTextField.getText());
                if (descricaoTextField.getText() != null && descricaoTextField.getText().length() > 0) c.setDescricao(descricaoTextField.getText());
                try {
                    Fachada.cadastrarColecao(c);
                    atualizarColecoes();
                    JOptionPane.showMessageDialog(this, "Cole��o Cadastrada com Sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (DataException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Houve um Erro ao Cadastrar a Cole��o.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void atualizarColecoes() {
        try {
            this.colecoes = Fachada.listarColecoes();
        } catch (DataException e) {
            JOptionPane.showMessageDialog(this, "Houve um Erro ao Listar as Cole��es.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        int numeroItens = 1;
        if (this.colecoes != null) numeroItens += this.colecoes.size();
        String[] itens = new String[numeroItens];
        itens[0] = "";
        if (this.colecoes != null) {
            for (int i = 0; i < this.colecoes.size(); i++) {
                itens[i + 1] = this.colecoes.get(i).getNome();
            }
        }
        ComboBoxModel colecoesComboBoxModel = new DefaultComboBoxModel(itens);
        getColecoesComboBox().setModel(colecoesComboBoxModel);
        ComboBoxModel colecaoBuscaComboBoxModel = new DefaultComboBoxModel(itens);
        getColecaoBuscaComboBox().setModel(colecaoBuscaComboBoxModel);
    }

    private void adicionarMusicasSelecionadasAColecao() {
        List<Musica> musicasAdicionar = new ArrayList<Musica>();
        int[] indices = getMusicasTable().getSelectedRows();
        if (indices.length <= 0 || colecoesComboBox.getSelectedIndex() == 0) return;
        for (int i : indices) {
            musicasAdicionar.add(musicas.get(i));
        }
        try {
            Fachada.adicionarMusicasAColecao(colecoes.get(colecoesComboBox.getSelectedIndex() - 1), musicasAdicionar);
            procurarMusicas();
            JOptionPane.showMessageDialog(this, "M�sicas Adicionadas com Sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (DataException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Houve um Erro ao Adicionar as M�sicas.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton getRemoverMusicasDaColecaoButton() {
        if (removerMusicasDaColecaoButton == null) {
            removerMusicasDaColecaoButton = new JButton();
            removerMusicasDaColecaoButton.setText("Remover M�sicas");
            removerMusicasDaColecaoButton.setFocusable(false);
            removerMusicasDaColecaoButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    removerMusicasDaColecao();
                }
            });
        }
        return removerMusicasDaColecaoButton;
    }

    private JButton getExcluirColecaoButton() {
        if (excluirColecaoButton == null) {
            excluirColecaoButton = new JButton();
            excluirColecaoButton.setText("Excluir Cole��o");
            excluirColecaoButton.setFocusable(false);
            excluirColecaoButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    excluirColecao();
                }
            });
        }
        return excluirColecaoButton;
    }

    private void excluirColecao() {
        if (getColecoesComboBox().getSelectedIndex() >= 1) {
            if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this, "Tem Certeza de que deseja excluir a Cole��o\n(Essa opera��o n�o pode ser desfeita)?", "Tem Certeza?", JOptionPane.YES_NO_OPTION)) {
                try {
                    Fachada.excluirColecao(colecoes.get(getColecoesComboBox().getSelectedIndex() - 1));
                    atualizarColecoes();
                    procurarMusicas();
                    JOptionPane.showMessageDialog(this, "Cole��o exclu�da com Sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (DataException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Houve um Erro ao excluir a Cole��o.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else return;
        }
    }

    private void removerMusicasDaColecao() {
        List<Musica> musicasRemover = new ArrayList<Musica>();
        int[] indices = getMusicasTable().getSelectedRows();
        if (indices.length <= 0 || colecoesComboBox.getSelectedIndex() == 0) return;
        if (JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(this, "Tem Certeza de que deseja remover as M�sicas da Cole��o Selecionada (" + colecoes.get(colecoesComboBox.getSelectedIndex() - 1).getNome() + ")?", "Tem Certeza?", JOptionPane.YES_NO_OPTION)) {
            return;
        }
        for (int i : indices) {
            musicasRemover.add(musicas.get(i));
        }
        try {
            Fachada.removerMusicasDaColecao(colecoes.get(colecoesComboBox.getSelectedIndex() - 1), musicasRemover);
            procurarMusicas();
            JOptionPane.showMessageDialog(this, "M�sicas Removidas com Sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (DataException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Houve um Erro ao Remover as M�sicas.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel getColecaoBuscaLabel() {
        if (colecaoBuscaLabel == null) {
            colecaoBuscaLabel = new JLabel();
            colecaoBuscaLabel.setText("Cole��o");
        }
        return colecaoBuscaLabel;
    }

    private JComboBox getColecaoBuscaComboBox() {
        if (colecaoBuscaComboBox == null) {
            colecaoBuscaComboBox = new JComboBox();
            colecaoBuscaComboBox.addKeyListener(new KeyAdapter() {

                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        procurarMusicas();
                        e.consume();
                    }
                }
            });
            atualizarColecoes();
        }
        return colecaoBuscaComboBox;
    }

    private JLabel getAnoLabel() {
        if (anoLabel == null) {
            anoLabel = new JLabel();
            anoLabel.setText("Ano");
        }
        return anoLabel;
    }

    private JTextField getAnoTextField() {
        if (anoTextField == null) {
            anoTextField = new JTextField();
            anoTextField.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    procurarMusicas();
                }
            });
        }
        return anoTextField;
    }
}
