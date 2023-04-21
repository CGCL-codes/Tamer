package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import control.AcessaDados;
import control.ExtraiCodigo;

public class TelaComporCapitulo extends JFrame {

    /**
	 * The serial version id has to do with java serialization
	 */
    private static final long serialVersionUID = -6851806024770436906L;

    /**
	 * R�tulos para identificar respectivamente a lista de cap�tulos cadastrados, a lista (em combo-box) de tutoriais cadastrados e a lista de cap�tulos adicionados.
	 */
    private JLabel licLabel, capLabel, infoLabel;

    /**
	 * Combo-box onde s�o mostrados os identificadores (no formato '#cod - #titulo') dos tutoriais cadastrados, para que o usu�rio possa selecionar 1(um).
	 */
    private static JComboBox capitulos;

    /**
	 * Listas onde s�o armazenados e mostrados os capitulos cadastrados na base de dados e os cap�utlos adicionados a determinado tutorial, respectivamente.
	 */
    private static JList licCadastradosList, licAdicionadosList;

    /**
	 * Paines com barras de rolagem para que as listas de capiutlos cadastrados e adicionados possam ser mostradas caso excedam o espa�o pr�-determinado.
	 */
    private static JScrollPane licCadastradosSP, licAdicionadosSP;

    /**
	 * Bot�es para, respectivamente, adicionar um capiutlo cadastrado � um tutorial selecionado, remover um cap�utlo adicionado � um tutorial
	 * e remover todos os cap�utlos adicionados � um tutorial.
	 */
    private static JButton addButton, removeButton, removeAllButton, okButton;

    /**
	 * Guarda as informa��es da conex�o corrente com a base de dados
	 */
    private static Connection outCon;

    /**
	 * Refer�ncia para a tela anterior.
	 */
    private JFrame parent;

    /**
	 * Construtor da clase TelaComporTutorial. Recebe a conex�o corrente com a base de dados e o objeto JFrame representado a tela anterior a sua abertura, 
	 * para que a aplica��o possa voltar a ela quando esta for fechada.
	 */
    public TelaComporCapitulo(Connection con, JFrame parent) {
        super("Compor Cap�tulo");
        setParent(parent);
        setOutCon(con);
        this.buildGui();
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                setTelaComporCapituloDefault();
                dispose();
            }
        });
    }

    /**
	 * Inicializa o tamanho da tela e define as posi��es para os componentes de interface gr�fica,  
	 */
    public void buildGui() {
        this.setSize(640, 400);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.getContentPane().setLayout(null);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        getCapLabel().setBounds(370, 5, 85, 20);
        this.add(getCapLabel(), null);
        getCapitulos().setBounds(370, 25, 250, 20);
        this.add(getCapitulos(), null);
        getLicLabel().setBounds(10, 60, 150, 20);
        this.add(getLicLabel(), null);
        getLicCadastradosSP().setBounds(10, 90, 250, 220);
        this.add(getLicCadastradosSP(), null);
        getAddButton().setBounds(270, 105, 90, 35);
        this.add(getAddButton(), null);
        getRemoveButton().setBounds(270, 155, 90, 35);
        this.add(getRemoveButton(), null);
        getRemoveAllButton().setBounds(270, 255, 90, 35);
        this.add(getRemoveAllButton(), null);
        getInfoLabel().setBounds(370, 60, 150, 25);
        this.add(getInfoLabel(), null);
        getLicAdicionadosSP().setBounds(370, 90, 250, 220);
        this.add(getLicAdicionadosSP(), null);
        getOkButton().setBounds(10, 325, 80, 30);
        this.add(getOkButton(), null);
        this.carregarLicCadastradasList();
        this.validate();
    }

    /**
	 * Inicializa o campo 'parent' com um valor javax.swing.JFrame passado por par�metro
	 * @param parent
	 */
    public void setParent(JFrame parent) {
        this.parent = parent;
    }

    /**
	 * Retorna o valor do campo 'parent'
	 * @return parent
	 */
    @Override
    public JFrame getParent() {
        return this.parent;
    }

    /**
	 * Inicializa o campo 'outCon' com um valor java.sql.Conncetion passada por par�metro
	 * @param con
	 */
    public void setOutCon(Connection con) {
        outCon = con;
    }

    /**
	 * Retorna o valor do campo 'outCon'
	 * @return
	 */
    public Connection getOutCon() {
        return outCon;
    }

    /**
	 * Retorna o r�tulo que indica a lista de cap�tulos cadastrados
	 * @return capLabel
	 */
    public JLabel getLicLabel() {
        if (licLabel == null) {
            licLabel = new JLabel("Li��es cadastradas");
        }
        return licLabel;
    }

    /**
	 * Retorna o r�tulo que indica a combo-box de tutoriais cadastrados
	 * @return tutLabel
	 */
    public JLabel getCapLabel() {
        if (capLabel == null) {
            capLabel = new JLabel("Cap�tulo");
        }
        return capLabel;
    }

    /**
	 * Retorna o r�tulo que indica a lista de capiutlos adicionados a um tutorial.
	 * @return infoLabel
	 */
    public JLabel getInfoLabel() {
        if (infoLabel == null) {
            infoLabel = new JLabel("<html>Li��es adicionadas ao cap�tulo selecionado</html>");
        }
        return infoLabel;
    }

    /**
	 * Retorna a combo-box que armazena os identificados dos tutoriais cadastrados
	 * @return tutoriais
	 */
    public JComboBox getCapitulos() {
        if (capitulos == null) {
            capitulos = new JComboBox();
            Vector<String> v = new Vector<String>();
            v = AcessaDados.retornaCapituloID(getOutCon());
            v.insertElementAt("[ Selecione um cap�tulo ]", 0);
            capitulos.setModel(new DefaultComboBoxModel(v));
            capitulos.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    if (!capitulos.getSelectedItem().toString().equals("[ Selecione um cap�tulo ]")) {
                        int codSelecionado = ExtraiCodigo.extraiCodigo(capitulos.getSelectedItem().toString());
                        getLicAdicionadosList().setListData(AcessaDados.retornaLicoesDeCapLic(codSelecionado, getOutCon()));
                    } else {
                        Vector<String> v = new Vector<String>();
                        v.add("");
                        getLicAdicionadosList().setListData(v);
                    }
                }
            });
        }
        return capitulos;
    }

    /**
	 * Retorna a lista de cap�utlos cadastrados na base de dados
	 * @return capCadastradosList
	 */
    public JList getLicCadastradosList() {
        if (licCadastradosList == null) {
            licCadastradosList = new JList();
            licCadastradosList.setListData(AcessaDados.retornaLicaoID(getOutCon()));
        }
        return licCadastradosList;
    }

    /**
	 * Retorna a lista de cap�utlos adicionados � determinado tutorial.
	 * @return capAdicionadosList
	 */
    public JList getLicAdicionadosList() {
        if (licAdicionadosList == null) {
            licAdicionadosList = new JList();
        }
        return licAdicionadosList;
    }

    /**
	 * Retorna o painel com barra de rolagens contendo a lista dos cap�tulos cadastrados
	 * @return capCadastradosSP
	 */
    public JScrollPane getLicCadastradosSP() {
        if (licCadastradosSP == null) {
            licCadastradosSP = new JScrollPane(getLicCadastradosList());
        }
        return licCadastradosSP;
    }

    /**
	 * Retorna o painel com barra de roalgem contendo a lista dos cap�utlos adicionados � um tutorial
	 * @return capAdicionadosSP
	 */
    public JScrollPane getLicAdicionadosSP() {
        if (licAdicionadosSP == null) {
            licAdicionadosSP = new JScrollPane(getLicAdicionadosList());
        }
        return licAdicionadosSP;
    }

    /**
	 * Retorna o bot�o para adicionar um cap�utlo selecionado na lista de cap�utlos cadastrados � um tutorial (lista de cap�tulos adicionados)
	 * @return addButton
	 */
    public JButton getAddButton() {
        if (addButton == null) {
            addButton = new JButton("Adicionar");
            addButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    if (!getCapitulos().getSelectedItem().toString().equals("[ Selecione um cap�tulo ]")) {
                        if (!getLicCadastradosList().isSelectionEmpty()) {
                            int codCapitulo = ExtraiCodigo.extraiCodigo(getCapitulos().getSelectedItem().toString());
                            int codLicao = ExtraiCodigo.extraiCodigo(getLicCadastradosList().getSelectedValue().toString());
                            Vector<Integer> v = new Vector<Integer>();
                            v.addElement(codLicao);
                            if (AcessaDados.adicionaLicaoEmCapLic(codCapitulo, v, getOutCon())) {
                                JOptionPane.showMessageDialog(null, "A Li��o foi adicionada ao Cap�tulo com sucesso");
                                getLicAdicionadosList().setListData(AcessaDados.retornaLicoesDeCapLic(codCapitulo, getOutCon()));
                            } else {
                                JOptionPane.showMessageDialog(null, "Erro. A Li��o n�o foi adicionada ao Cap�tulo");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Caso existam li��es cadastradas, por favor, selecione uma", "Li��o n�o selecionada", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Caso existam cap�tulos, por favor, selecione um", "Cap�tulo n�o selecionado", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });
        }
        return addButton;
    }

    /**
	 * Retorna o bot�o que remove um cap�utlo selecionado da lista de cap�tulos adicionados
	 * @return removeButton
	 */
    public JButton getRemoveButton() {
        if (removeButton == null) {
            removeButton = new JButton("Remover");
            removeButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    if (!getCapitulos().getSelectedItem().toString().equals("[ Selecione um cap�tulo ]")) {
                        if (!getLicAdicionadosList().isSelectionEmpty()) {
                            int codCapitulo = ExtraiCodigo.extraiCodigo(getCapitulos().getSelectedItem().toString());
                            int codLicao = ExtraiCodigo.extraiCodigo(getLicAdicionadosList().getSelectedValue().toString());
                            Vector<Integer> v = new Vector<Integer>();
                            v.addElement(codLicao);
                            if (AcessaDados.excluirLicaoDeCapLic(codCapitulo, v, getOutCon())) {
                                JOptionPane.showMessageDialog(null, "A Li��o foi exclu�da do Cap�tulo com sucesso");
                                getLicAdicionadosList().setListData(AcessaDados.retornaLicoesDeCapLic(codCapitulo, getOutCon()));
                            } else {
                                JOptionPane.showMessageDialog(null, "Erro. A Li��o n�o foi exclu�da do Cap�tulo");
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Caso existam li��es adicionadas, por favor, selecione uma", "Li��o n�o selecionado", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Caso existam cap�tulos, por favor, selecione um", "Cap�tulo n�o selecionado", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });
        }
        return removeButton;
    }

    /**
	 * Retorna o bot�o que remove todos os itens da lista de cap�utlos adicionados
	 * @return removeAllButton
	 */
    public JButton getRemoveAllButton() {
        if (removeAllButton == null) {
            removeAllButton = new JButton("<html>Remover<br />Todos</html>");
            removeAllButton.setHorizontalAlignment(SwingConstants.CENTER);
            removeAllButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent arg0) {
                    if (!getCapitulos().getSelectedItem().toString().equals("[ Selecione um cap�tulo ]")) {
                        if (getLicAdicionadosList().getModel().getSize() > 0) {
                            int codCap = ExtraiCodigo.extraiCodigo(getCapitulos().getSelectedItem().toString());
                            Vector<Integer> v = new Vector<Integer>();
                            for (int i = 0; i < getLicAdicionadosList().getModel().getSize(); i++) {
                                int codLic = ExtraiCodigo.extraiCodigo(getLicAdicionadosList().getModel().getElementAt(i).toString());
                                v.addElement(codLic);
                            }
                            if (AcessaDados.excluirLicaoDeCapLic(codCap, v, getOutCon())) {
                                setTelaComporCapituloDefault();
                                JOptionPane.showMessageDialog(null, "Li��es removidas com sucesso do cap�tulo", "Remover li��es", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Erro ao tentar remover li��es do cap�tulo", "Remover li��es", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Lista de li��es adicionadas vazia.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Caso existam cap�tulos, por favor, selecione um", "Cap�tulo n�o selecionado", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });
        }
        return removeAllButton;
    }

    /**
	 * Retorna o bot�o que, quando pressionado, retorna para Tela Principal
	 * @return
	 */
    public JButton getOkButton() {
        if (okButton == null) {
            okButton = new JButton("OK");
        }
        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                setTelaComporCapituloDefault();
                dispose();
            }
        });
        return okButton;
    }

    public void setTelaComporCapituloDefault() {
        getCapitulos().setSelectedIndex(0);
        Vector<String> v = new Vector<String>();
        v.add("");
        getLicCadastradosList().clearSelection();
        getLicAdicionadosList().setListData(v);
    }

    /**
	 * Carrega as informa��es (cod. e titulo) das Li��es cadastradas na base de dados na lista de li��es
	 */
    public void carregarLicCadastradasList() {
        getLicCadastradosList().setListData(AcessaDados.retornaLicaoID(getOutCon()));
        getLicCadastradosSP().setViewportView(getLicCadastradosList());
    }
}
