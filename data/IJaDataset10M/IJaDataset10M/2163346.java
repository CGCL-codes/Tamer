package asfabdesk.apresentacao;

import asfabdesk.dominio.Convenio;
import asfabdesk.dominio.Data;
import asfabdesk.dominio.TipoServico;
import asfabdesk.presistencia.DAO;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Mathias
 */
public class TelaConvenios extends javax.swing.JInternalFrame {

    private List<Convenio> convenios = null;

    /** Creates new form TelaConvenios */
    public TelaConvenios() throws SQLException {
        initComponents();
        configurarTela();
    }

    public void configurarTela() throws SQLException {
        configurarComboServico();
    }

    public void abrirIFrame(JInternalFrame janela) {
        for (JInternalFrame j : TelaPrincipal.desktop.getAllFrames()) {
            if (janela.getClass() == j.getClass()) {
                j.moveToFront();
                return;
            }
        }
        TelaPrincipal.desktop.add(janela);
        janela.setVisible(true);
    }

    public void configurarComboServico() throws SQLException {
        comboServico.addItem("");
        comboServico.setSelectedItem("");
        java.util.List servicos = (new DAO()).listar(TipoServico.class);
        for (int i = 0; i < servicos.size(); i++) {
            String descricao = ((TipoServico) servicos.get(i)).getDescricao();
            comboServico.addItem(descricao);
        }
    }

    private class ModeloConvenio extends DefaultTableModel {

        private List<Convenio> lista;

        private String[] campos = new String[] { "ID", "Convênio", "Empresa", "Término", "Pgt", "Valor R$", "Ativo" };

        public ModeloConvenio(List<Convenio> list) {
            lista = list;
        }

        public ModeloConvenio() {
        }

        public Convenio getConvenio(int index) {
            return lista.get(index);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        @Override
        public Object getValueAt(int row, int column) {
            Convenio convenio = lista.get(row);
            switch(column) {
                case 0:
                    return convenio.getId();
                case 1:
                    return convenio.getNome();
                case 2:
                    return convenio.getEmpresa().getNomeFantasia();
                case 3:
                    return new Data().ConverteDataDateStringBra(convenio.getDataTermino());
                case 4:
                    return getPagamento(convenio.getFormaPgt());
                case 5:
                    return convenio.getValor();
                case 6:
                    return convenio.isStatus();
                default:
                    return null;
            }
        }

        @Override
        public int getRowCount() {
            return lista == null ? 0 : lista.size();
        }

        @Override
        public int getColumnCount() {
            return campos.length;
        }

        @Override
        public String getColumnName(int column) {
            return campos[column];
        }

        @Override
        public Class<?> getColumnClass(int column) {
            if (column == 6) {
                return Boolean.class;
            }
            return super.getColumnClass(column);
        }
    }

    public String getPagamento(int pgt) {
        String pagamento = "";
        if (pgt == 0) {
            pagamento = "NA";
        } else if (pgt == 1) {
            pagamento = "Desconto em Folha";
        } else if (pgt == 2) {
            pagamento = "ASFAB";
        } else if (pgt == 3) {
            pagamento = "Empresa";
        }
        return pagamento;
    }

    public void buscaPorFiltroUnico() throws SQLException {
        if (comboFiltro.getSelectedIndex() == 0) {
            convenios = new DAO().listar(Convenio.class, "ConvenioPorNome", "%" + txtBusca.getText().toUpperCase() + "%");
        } else if (comboFiltro.getSelectedIndex() == 1) {
            convenios = new DAO().listar(Convenio.class, "ConveniosPorNomeEmpresa", "%" + txtBusca.getText().toUpperCase() + "%");
        } else if (comboFiltro.getSelectedIndex() == 2) {
            convenios = new DAO().listar(Convenio.class, "ConveniosPorRazaoSocial", "%" + txtBusca.getText().toUpperCase() + "%");
        } else if (comboFiltro.getSelectedIndex() == 3) {
            convenios = new DAO().listar(Convenio.class, "ConveniosPorCNPJ", "%" + txtBusca.getText() + "%");
        } else if (comboFiltro.getSelectedIndex() == 4) {
            convenios = new DAO().listar(Convenio.class, "ConveniosPorDescricao", "%" + txtBusca.getText() + "%");
        } else if (comboFiltro.getSelectedIndex() == 5) {
            try {
                Calendar inicio = new Data().ConverteDataStringParaCalendar(txtBusca.getText());
                convenios = new DAO().listar(Convenio.class, "ConveniosPorDataInicio", inicio);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Ops, data inválida. Tente novamente", "Data inválida", 2);
            }
        } else if (comboFiltro.getSelectedIndex() == 6) {
            try {
                Calendar termino = new Data().ConverteDataStringParaCalendar(txtBusca.getText());
                convenios = new DAO().listar(Convenio.class, "ConveniosPorDataTermino", termino);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Ops, data inválida. Tente novamente", "Data inválida", 2);
            }
        }
        tabelaResultado.setModel(new ModeloConvenio(convenios));
    }

    public void buscaPorFiltroComposto() throws SQLException {
        if (comboServico.getSelectedIndex() == 0 && comboTipoDePagamento.getSelectedIndex() == 4 && comboStatus.getSelectedIndex() == 0) {
            listarTodosAtivo();
        } else if (comboServico.getSelectedIndex() > 0 && comboTipoDePagamento.getSelectedIndex() == 4 && comboStatus.getSelectedIndex() == 0) {
            listarPorTipoServicoAtivo();
        } else if (comboServico.getSelectedIndex() > 0 && comboTipoDePagamento.getSelectedIndex() < 4 && comboStatus.getSelectedIndex() == 0) {
            listarPorServicoTipoPagamentoAtivo();
        } else if (comboServico.getSelectedIndex() == 0 && comboTipoDePagamento.getSelectedIndex() < 4 && comboStatus.getSelectedIndex() == 0) {
            listarPorTipoPagamentoAtivo();
        } else if (comboServico.getSelectedIndex() == 0 && comboTipoDePagamento.getSelectedIndex() == 4 && comboStatus.getSelectedIndex() == 1) {
            listarTodosInativo();
        } else if (comboServico.getSelectedIndex() > 0 && comboTipoDePagamento.getSelectedIndex() == 4 && comboStatus.getSelectedIndex() == 1) {
            listarPorTipoServicoInativo();
        } else if (comboServico.getSelectedIndex() > 0 && comboTipoDePagamento.getSelectedIndex() < 4 && comboStatus.getSelectedIndex() == 1) {
            listarPorServicoTipoPagamentoInativo();
        } else if (comboServico.getSelectedIndex() == 0 && comboTipoDePagamento.getSelectedIndex() < 4 && comboStatus.getSelectedIndex() == 1) {
            listarPorTipoPagamentoInativo();
        } else if (comboServico.getSelectedIndex() == 0 && comboTipoDePagamento.getSelectedIndex() == 4 && comboStatus.getSelectedIndex() == 2) {
            listarTodos();
        } else if (comboServico.getSelectedIndex() > 0 && comboTipoDePagamento.getSelectedIndex() == 4 && comboStatus.getSelectedIndex() == 2) {
            listarPorTipoServico();
        } else if (comboServico.getSelectedIndex() > 0 && comboTipoDePagamento.getSelectedIndex() < 4 && comboStatus.getSelectedIndex() == 2) {
            listarPorServicoTipoPagamento();
        } else if (comboServico.getSelectedIndex() == 0 && comboTipoDePagamento.getSelectedIndex() < 4 && comboStatus.getSelectedIndex() == 2) {
            listarPorTipoPagamento();
        }
    }

    public void listarTodos() {
        convenios = new DAO().listar(Convenio.class);
        tabelaResultado.setModel(new ModeloConvenio(convenios));
    }

    public void listarPorTipoServico() {
        TipoServico servico = new DAO().localizar(TipoServico.class, "TipoServicoPorNome", comboServico.getSelectedItem().toString());
        convenios = new DAO().listar(Convenio.class, "ConveniosPorServico", servico);
        tabelaResultado.setModel(new ModeloConvenio(convenios));
    }

    public void listarPorServicoTipoPagamento() {
        TipoServico servico = new DAO().localizar(TipoServico.class, "TipoServicoPorNome", comboServico.getSelectedItem().toString());
        int pagamento = comboTipoDePagamento.getSelectedIndex();
        convenios = new DAO().listar(Convenio.class, "ConveniosPorServicoTipoPagamento", servico, pagamento);
        tabelaResultado.setModel(new ModeloConvenio(convenios));
    }

    public void listarPorTipoPagamento() {
        int pagamento = comboTipoDePagamento.getSelectedIndex();
        convenios = new DAO().listar(Convenio.class, "ConveniosPorTipoPagamento", pagamento);
        tabelaResultado.setModel(new ModeloConvenio(convenios));
    }

    public void listarTodosAtivo() {
        convenios = new DAO().listar(Convenio.class, "ConveniosPorStatus", true);
        tabelaResultado.setModel(new ModeloConvenio(convenios));
    }

    public void listarPorTipoServicoAtivo() {
        TipoServico servico = new DAO().localizar(TipoServico.class, "TipoServicoPorNome", comboServico.getSelectedItem().toString());
        convenios = new DAO().listar(Convenio.class, "ConveniosPorServicoStatus", servico, true);
        tabelaResultado.setModel(new ModeloConvenio(convenios));
    }

    public void listarPorServicoTipoPagamentoAtivo() {
        TipoServico servico = new DAO().localizar(TipoServico.class, "TipoServicoPorNome", comboServico.getSelectedItem().toString());
        int pagamento = comboTipoDePagamento.getSelectedIndex();
        convenios = new DAO().listar(Convenio.class, "ConveniosPorServicoTipoPagamentoStatus", servico, pagamento, true);
        tabelaResultado.setModel(new ModeloConvenio(convenios));
    }

    public void listarPorTipoPagamentoAtivo() {
        int pagamento = comboTipoDePagamento.getSelectedIndex();
        convenios = new DAO().listar(Convenio.class, "ConveniosPorTipoPagamentoStatus", pagamento, true);
        tabelaResultado.setModel(new ModeloConvenio(convenios));
    }

    public void listarTodosInativo() {
        convenios = new DAO().listar(Convenio.class, "ConveniosPorStatus", false);
        tabelaResultado.setModel(new ModeloConvenio(convenios));
    }

    public void listarPorTipoServicoInativo() {
        TipoServico servico = new DAO().localizar(TipoServico.class, "TipoServicoPorNome", comboServico.getSelectedItem().toString());
        convenios = new DAO().listar(Convenio.class, "ConveniosPorServicoStatus", servico, false);
        tabelaResultado.setModel(new ModeloConvenio(convenios));
    }

    public void listarPorServicoTipoPagamentoInativo() {
        TipoServico servico = new DAO().localizar(TipoServico.class, "TipoServicoPorNome", comboServico.getSelectedItem().toString());
        int pagamento = comboTipoDePagamento.getSelectedIndex();
        convenios = new DAO().listar(Convenio.class, "ConveniosPorServicoTipoPagamentoStatus", servico, pagamento, false);
        tabelaResultado.setModel(new ModeloConvenio(convenios));
    }

    public void listarPorTipoPagamentoInativo() {
        int pagamento = comboTipoDePagamento.getSelectedIndex();
        convenios = new DAO().listar(Convenio.class, "ConveniosPorTipoPagamentoStatus", pagamento, false);
        tabelaResultado.setModel(new ModeloConvenio(convenios));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        painelPrincipal = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        comboFiltro = new javax.swing.JComboBox();
        txtBusca = new javax.swing.JTextField();
        btnLocalizar = new javax.swing.JButton();
        lblPesquisa = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        comboServico = new javax.swing.JComboBox();
        comboTipoDePagamento = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnFiltrar = new javax.swing.JButton();
        comboStatus = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaResultado = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        bfInferior = new javax.swing.JToolBar();
        btnImprimir = new javax.swing.JButton();
        btnNovo = new javax.swing.JButton();
        setClosable(true);
        setIconifiable(true);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(asfabdesk.apresentacao.AsfabDeskApp.class).getContext().getResourceMap(TelaConvenios.class);
        setTitle(resourceMap.getString("Form.title"));
        setFrameIcon(resourceMap.getIcon("Form.frameIcon"));
        setName("Form");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        painelPrincipal.setBackground(resourceMap.getColor("painelPrincipal.background"));
        painelPrincipal.setName("painelPrincipal");
        painelPrincipal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel5.setBackground(resourceMap.getColor("jPanel5.background"));
        jPanel5.setName("jPanel5");
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        comboFiltro.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Nome do Convênio", "Nome da Empresa", "Razão Social", "CNPJ", "Descrição Convênio", "Data Início", "Data Término" }));
        comboFiltro.setName("comboFiltro");
        jPanel5.add(comboFiltro, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));
        txtBusca.setName("txtBusca");
        jPanel5.add(txtBusca, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 280, -1));
        btnLocalizar.setForeground(resourceMap.getColor("btnLocalizar.foreground"));
        btnLocalizar.setText(resourceMap.getString("btnLocalizar.text"));
        btnLocalizar.setName("btnLocalizar");
        btnLocalizar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLocalizarActionPerformed(evt);
            }
        });
        jPanel5.add(btnLocalizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 10, -1, 20));
        lblPesquisa.setName("lblPesquisa");
        jPanel5.add(lblPesquisa, new org.netbeans.lib.awtextra.AbsoluteConstraints(699, 42, -1, -1));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(resourceMap.getIcon("jLabel5.icon"));
        jLabel5.setName("jLabel5");
        jPanel5.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 530, 20));
        painelPrincipal.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 530, 50));
        jPanel3.setBackground(resourceMap.getColor("jPanel3.background"));
        jPanel3.setName("jPanel3");
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        comboServico.setName("comboServico");
        jPanel3.add(comboServico, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 31, 158, -1));
        comboTipoDePagamento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sem pagamento", "Desconto em folha", "ASFAB", "Empresa", "" }));
        comboTipoDePagamento.setSelectedIndex(4);
        comboTipoDePagamento.setName("comboTipoDePagamento");
        jPanel3.add(comboTipoDePagamento, new org.netbeans.lib.awtextra.AbsoluteConstraints(174, 31, 157, -1));
        jLabel3.setFont(resourceMap.getFont("jLabel7.font"));
        jLabel3.setText(resourceMap.getString("jLabel3.text"));
        jLabel3.setName("jLabel3");
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, -1));
        jLabel4.setFont(resourceMap.getFont("jLabel7.font"));
        jLabel4.setText(resourceMap.getString("jLabel4.text"));
        jLabel4.setName("jLabel4");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(174, 11, -1, -1));
        btnFiltrar.setForeground(resourceMap.getColor("btnFiltrar.foreground"));
        btnFiltrar.setText(resourceMap.getString("btnFiltrar.text"));
        btnFiltrar.setName("btnFiltrar");
        btnFiltrar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarActionPerformed(evt);
            }
        });
        jPanel3.add(btnFiltrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 30, 70, 20));
        comboStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ativo", "Inativo", "Ambos" }));
        comboStatus.setName("comboStatus");
        jPanel3.add(comboStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(341, 30, 79, -1));
        jLabel7.setFont(resourceMap.getFont("jLabel7.font"));
        jLabel7.setText(resourceMap.getString("jLabel7.text"));
        jLabel7.setName("jLabel7");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(341, 11, -1, -1));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setIcon(resourceMap.getIcon("jLabel1.icon"));
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel1.setName("jLabel1");
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 0, -1, -1));
        painelPrincipal.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 530, 60));
        getContentPane().add(painelPrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 540, 135));
        jPanel1.setBackground(resourceMap.getColor("jPanel1.background"));
        jPanel1.setName("jPanel1");
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jScrollPane1.setName("jScrollPane1");
        tabelaResultado.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "ID", "Convênio", "Empresa", "Término", "Valor R$", "Pgt", "Ativo" }) {

            Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class };

            boolean[] canEdit = new boolean[] { false, false, false, false, false, false, false };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        tabelaResultado.setName("tabelaResultado");
        tabelaResultado.setSelectionBackground(resourceMap.getColor("tabelaResultado.selectionBackground"));
        tabelaResultado.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tabelaResultado);
        tabelaResultado.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("tabelaResultado.columnModel.title0"));
        tabelaResultado.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("tabelaResultado.columnModel.title1"));
        tabelaResultado.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("tabelaResultado.columnModel.title2"));
        tabelaResultado.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("tabelaResultado.columnModel.title3"));
        tabelaResultado.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("tabelaResultado.columnModel.title5"));
        tabelaResultado.getColumnModel().getColumn(5).setHeaderValue(resourceMap.getString("tabelaResultado.columnModel.title6"));
        tabelaResultado.getColumnModel().getColumn(6).setHeaderValue(resourceMap.getString("tabelaResultado.columnModel.title4"));
        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 38, 520, 200));
        jLabel2.setFont(resourceMap.getFont("jLabel2.font"));
        jLabel2.setForeground(resourceMap.getColor("jLabel2.foreground"));
        jLabel2.setText(resourceMap.getString("jLabel2.text"));
        jLabel2.setName("jLabel2");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, -1));
        bfInferior.setFloatable(false);
        bfInferior.setName("bfInferior");
        bfInferior.setOpaque(false);
        btnImprimir.setForeground(resourceMap.getColor("btnImprimir.foreground"));
        btnImprimir.setIcon(resourceMap.getIcon("btnImprimir.icon"));
        btnImprimir.setText(resourceMap.getString("btnImprimir.text"));
        btnImprimir.setEnabled(false);
        btnImprimir.setFocusable(false);
        btnImprimir.setMaximumSize(new java.awt.Dimension(80, 33));
        btnImprimir.setMinimumSize(new java.awt.Dimension(80, 33));
        btnImprimir.setName("btnImprimir");
        btnImprimir.setOpaque(false);
        btnImprimir.setPreferredSize(new java.awt.Dimension(67, 33));
        bfInferior.add(btnImprimir);
        btnNovo.setForeground(resourceMap.getColor("btnNovo.foreground"));
        btnNovo.setIcon(resourceMap.getIcon("btnNovo.icon"));
        btnNovo.setText(resourceMap.getString("btnNovo.text"));
        btnNovo.setFocusable(false);
        btnNovo.setName("btnNovo");
        btnNovo.setOpaque(false);
        btnNovo.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoActionPerformed(evt);
            }
        });
        bfInferior.add(btnNovo);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(bfInferior, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE).addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(bfInferior, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(painelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(painelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private void btnLocalizarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            buscaPorFiltroUnico();
        } catch (SQLException ex) {
            Logger.getLogger(TelaConvenios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            buscaPorFiltroComposto();
        } catch (SQLException ex) {
            Logger.getLogger(TelaConvenios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            abrirIFrame(new TelaConvenio());
        } catch (SQLException ex) {
            Logger.getLogger(TelaConvenios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private javax.swing.JToolBar bfInferior;

    private javax.swing.JButton btnFiltrar;

    private javax.swing.JButton btnImprimir;

    private javax.swing.JButton btnLocalizar;

    private javax.swing.JButton btnNovo;

    private javax.swing.JComboBox comboFiltro;

    private javax.swing.JComboBox comboServico;

    private javax.swing.JComboBox comboStatus;

    private javax.swing.JComboBox comboTipoDePagamento;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JLabel lblPesquisa;

    private javax.swing.JPanel painelPrincipal;

    private javax.swing.JTable tabelaResultado;

    private javax.swing.JTextField txtBusca;
}
