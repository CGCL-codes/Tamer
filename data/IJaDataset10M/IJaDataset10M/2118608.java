package reconhecedorNumeros;

import javax.swing.JOptionPane;
import negocio.GerenciadorRedeNeural;

/**
 *
 * @author  André
 */
public class ConfiguracaoDialogo extends javax.swing.JDialog {

    /** Creates new form ConfiguracaoDialogo */
    public ConfiguracaoDialogo(java.awt.Frame parent, ReconhecedorNumerosView reconhecedor, boolean modal) {
        super(parent, modal);
        initComponents();
        this.gerenciadorRedeNeural = reconhecedor.getGerenciadorRedeNeural();
        inicializaValores();
    }

    private void inicializaValores() {
        taxaAprendizado.setText(String.valueOf(gerenciadorRedeNeural.getTaxaDeAprendizado()));
        erroMinimo.setText(String.valueOf(gerenciadorRedeNeural.getErroMinimo()));
        numeroMaximoCiclos.setText(String.valueOf(gerenciadorRedeNeural.getNumeroMaximoCiclos()));
        nomeCamadaEntrada.setText(gerenciadorRedeNeural.getNomeCamadaEntrada());
        numeroNeuroniosCamadaEntrada.setText(String.valueOf(gerenciadorRedeNeural.getNumeroNeuroniosCamadaEntrada()));
        nomeCamadaIntermediaria.setText(gerenciadorRedeNeural.getNomeCamadaIntermediaria());
        numeroNeuroniosCamadaIntermediaria.setText(String.valueOf(gerenciadorRedeNeural.getNumeroNeuroniosCamadaIntermediaria()));
        pesosCamadaIntermediaria.setText(gerenciadorRedeNeural.getPesosCamadaIntermediaria());
        nomeCamadaSaida.setText(gerenciadorRedeNeural.getNomeCamadaSaida());
        numeroNeuroniosCamadaSaida.setText(String.valueOf(gerenciadorRedeNeural.getNumeroNeuroniosCamadaSaida()));
        pesosCamadaSaida.setText(gerenciadorRedeNeural.getPesosCamadaSaida());
    }

    private void initComponents() {
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        taxaAprendizado = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        erroMinimo = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        numeroMaximoCiclos = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        nomeCamadaEntrada = new javax.swing.JTextField();
        numeroNeuroniosCamadaEntrada = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        nomeCamadaIntermediaria = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        numeroNeuroniosCamadaIntermediaria = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        pesosCamadaIntermediaria = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        nomeCamadaSaida = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        numeroNeuroniosCamadaSaida = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        pesosCamadaSaida = new javax.swing.JTextArea();
        okBotao = new javax.swing.JButton();
        cancelaBotao = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(reconhecedorNumeros.ReconhecedorNumeros.class).getContext().getResourceMap(ConfiguracaoDialogo.class);
        setTitle(resourceMap.getString("Form.title"));
        setName("Form");
        jTabbedPane1.setName("jTabbedPane1");
        jPanel4.setName("jPanel4");
        jLabel3.setText(resourceMap.getString("jLabel3.text"));
        jLabel3.setName("jLabel3");
        taxaAprendizado.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        taxaAprendizado.setText(resourceMap.getString("taxaAprendizado.text"));
        taxaAprendizado.setName("taxaAprendizado");
        jLabel10.setText(resourceMap.getString("jLabel10.text"));
        jLabel10.setName("jLabel10");
        erroMinimo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        erroMinimo.setText(resourceMap.getString("erroMinimo.text"));
        erroMinimo.setName("erroMinimo");
        jLabel11.setText(resourceMap.getString("jLabel11.text"));
        jLabel11.setName("jLabel11");
        numeroMaximoCiclos.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        numeroMaximoCiclos.setText(resourceMap.getString("numeroMaximoCiclos.text"));
        numeroMaximoCiclos.setName("numeroMaximoCiclos");
        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel10).addComponent(jLabel3).addComponent(jLabel11)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(numeroMaximoCiclos).addComponent(erroMinimo).addComponent(taxaAprendizado, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)).addContainerGap(158, Short.MAX_VALUE)));
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addGap(20, 20, 20).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(taxaAprendizado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel10).addComponent(erroMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel11).addComponent(numeroMaximoCiclos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(73, Short.MAX_VALUE)));
        jTabbedPane1.addTab(resourceMap.getString("jPanel4.TabConstraints.tabTitle"), jPanel4);
        jPanel1.setName("jPanel1");
        jLabel2.setText(resourceMap.getString("jLabel2.text"));
        jLabel2.setName("jLabel2");
        jLabel1.setText(resourceMap.getString("jLabel1.text"));
        jLabel1.setName("jLabel1");
        nomeCamadaEntrada.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        nomeCamadaEntrada.setText(resourceMap.getString("nomeCamadaEntrada.text"));
        nomeCamadaEntrada.setMaximumSize(new java.awt.Dimension(6, 20));
        nomeCamadaEntrada.setName("nomeCamadaEntrada");
        numeroNeuroniosCamadaEntrada.setEditable(false);
        numeroNeuroniosCamadaEntrada.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        numeroNeuroniosCamadaEntrada.setText(resourceMap.getString("numeroNeuroniosCamadaEntrada.text"));
        numeroNeuroniosCamadaEntrada.setName("numeroNeuroniosCamadaEntrada");
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel2).addComponent(jLabel1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(numeroNeuroniosCamadaEntrada).addComponent(nomeCamadaEntrada, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)).addContainerGap(176, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(22, 22, 22).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(nomeCamadaEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(numeroNeuroniosCamadaEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(97, Short.MAX_VALUE)));
        jTabbedPane1.addTab(resourceMap.getString("jPanel1.TabConstraints.tabTitle"), jPanel1);
        jPanel2.setName("jPanel2");
        jLabel4.setText(resourceMap.getString("jLabel4.text"));
        jLabel4.setName("jLabel4");
        nomeCamadaIntermediaria.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        nomeCamadaIntermediaria.setText(resourceMap.getString("nomeCamadaIntermediaria.text"));
        nomeCamadaIntermediaria.setMaximumSize(new java.awt.Dimension(6, 20));
        nomeCamadaIntermediaria.setName("nomeCamadaIntermediaria");
        jLabel5.setText(resourceMap.getString("jLabel5.text"));
        jLabel5.setName("jLabel5");
        numeroNeuroniosCamadaIntermediaria.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        numeroNeuroniosCamadaIntermediaria.setText(resourceMap.getString("numeroNeuroniosCamadaIntermediaria.text"));
        numeroNeuroniosCamadaIntermediaria.setName("numeroNeuroniosCamadaIntermediaria");
        jLabel8.setText(resourceMap.getString("jLabel8.text"));
        jLabel8.setName("jLabel8");
        jScrollPane1.setName("jScrollPane1");
        pesosCamadaIntermediaria.setColumns(20);
        pesosCamadaIntermediaria.setEditable(false);
        pesosCamadaIntermediaria.setLineWrap(true);
        pesosCamadaIntermediaria.setRows(5);
        pesosCamadaIntermediaria.setName("pesosCamadaIntermediaria");
        jScrollPane1.setViewportView(pesosCamadaIntermediaria);
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(numeroNeuroniosCamadaIntermediaria)).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jLabel4).addGap(80, 80, 80).addComponent(nomeCamadaIntermediaria, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(176, Short.MAX_VALUE)).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jLabel8).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(24, 24, 24)))));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addContainerGap(21, Short.MAX_VALUE).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel4).addComponent(nomeCamadaIntermediaria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel5).addComponent(numeroNeuroniosCamadaIntermediaria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel8).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(16, 16, 16)));
        jTabbedPane1.addTab(resourceMap.getString("jPanel2.TabConstraints.tabTitle"), jPanel2);
        jPanel3.setName("jPanel3");
        jLabel6.setText(resourceMap.getString("jLabel6.text"));
        jLabel6.setName("jLabel6");
        nomeCamadaSaida.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        nomeCamadaSaida.setText(resourceMap.getString("nomeCamadaSaida.text"));
        nomeCamadaSaida.setName("nomeCamadaSaida");
        jLabel7.setText(resourceMap.getString("jLabel7.text"));
        jLabel7.setName("jLabel7");
        numeroNeuroniosCamadaSaida.setEditable(false);
        numeroNeuroniosCamadaSaida.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        numeroNeuroniosCamadaSaida.setText(resourceMap.getString("numeroNeuroniosCamadaSaida.text"));
        numeroNeuroniosCamadaSaida.setName("numeroNeuroniosCamadaSaida");
        jLabel9.setText(resourceMap.getString("jLabel9.text"));
        jLabel9.setName("jLabel9");
        jScrollPane2.setName("jScrollPane2");
        pesosCamadaSaida.setColumns(20);
        pesosCamadaSaida.setEditable(false);
        pesosCamadaSaida.setLineWrap(true);
        pesosCamadaSaida.setRows(5);
        pesosCamadaSaida.setName("pesosCamadaSaida");
        jScrollPane2.setViewportView(pesosCamadaSaida);
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel7).addComponent(jLabel9).addComponent(jLabel6)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(nomeCamadaSaida).addComponent(numeroNeuroniosCamadaSaida, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(23, Short.MAX_VALUE)));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGap(19, 19, 19).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(nomeCamadaSaida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel6)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel7).addComponent(numeroNeuroniosCamadaSaida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel9).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(18, Short.MAX_VALUE)));
        jTabbedPane1.addTab(resourceMap.getString("jPanel3.TabConstraints.tabTitle"), jPanel3);
        okBotao.setText(resourceMap.getString("okBotao.text"));
        okBotao.setName("okBotao");
        okBotao.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okBotaoActionPerformed(evt);
            }
        });
        cancelaBotao.setText(resourceMap.getString("cancelaBotao.text"));
        cancelaBotao.setName("cancelaBotao");
        cancelaBotao.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelaBotaoActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 278, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(okBotao).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cancelaBotao)).addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cancelaBotao).addComponent(okBotao)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private void okBotaoActionPerformed(java.awt.event.ActionEvent evt) {
        gerenciadorRedeNeural.setTaxaDeAprendizado(Double.parseDouble(taxaAprendizado.getText()));
        gerenciadorRedeNeural.setErroMinimo(Double.parseDouble(erroMinimo.getText()));
        gerenciadorRedeNeural.setNumeroMaximoCiclos(Integer.parseInt(numeroMaximoCiclos.getText()));
        gerenciadorRedeNeural.setNomeCamadaEntrada(nomeCamadaEntrada.getText());
        gerenciadorRedeNeural.setNomeCamadaIntermediaria(nomeCamadaIntermediaria.getText());
        gerenciadorRedeNeural.setNomeCamadaSaida(nomeCamadaSaida.getText());
        int numeroNeuroniosCI = Integer.parseInt(numeroNeuroniosCamadaIntermediaria.getText());
        if (gerenciadorRedeNeural.getNumeroNeuroniosCamadaIntermediaria() != numeroNeuroniosCI) {
            String mensagem = gerenciadorRedeNeural.setNumeroNeuroniosCamadaIntermediaria(numeroNeuroniosCI);
            JOptionPane.showMessageDialog(null, mensagem);
        }
        this.dispose();
    }

    private void cancelaBotaoActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    private javax.swing.JButton cancelaBotao;

    private javax.swing.JTextField erroMinimo;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JTextField nomeCamadaEntrada;

    private javax.swing.JTextField nomeCamadaIntermediaria;

    private javax.swing.JTextField nomeCamadaSaida;

    private javax.swing.JTextField numeroMaximoCiclos;

    private javax.swing.JTextField numeroNeuroniosCamadaEntrada;

    private javax.swing.JTextField numeroNeuroniosCamadaIntermediaria;

    private javax.swing.JTextField numeroNeuroniosCamadaSaida;

    private javax.swing.JButton okBotao;

    private javax.swing.JTextArea pesosCamadaIntermediaria;

    private javax.swing.JTextArea pesosCamadaSaida;

    private javax.swing.JTextField taxaAprendizado;

    private GerenciadorRedeNeural gerenciadorRedeNeural;
}
