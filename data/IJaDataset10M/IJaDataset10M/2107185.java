package erreAga.view.funcionario;

import java.util.List;
import erreAga.dao.DaoException;
import erreAga.eb.Funcionario;
import erreAga.eb.HoraExtra;
import erreAga.service.ErreAgaService;
import erreAga.service.impl.ErreAgaServiceImpl;
import erreAga.ws.cliente.ClienteWebService;
import erreAga.ws.cliente.impl.ClienteWebServiceImpl;

/**
 *
 * @author Danilo Carlos
 */
public class ListagemHoraExtra extends javax.swing.JFrame {

    /**
   *
   */
    private static final long serialVersionUID = -3318124659147836858L;

    /** Creates new form ListagemFuncionarios */
    public ListagemHoraExtra() {
        initComponents();
        listar();
    }

    private void initComponents() {
        labelListagemFuncionarios = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        textAreaListagemFuncionarios = new javax.swing.JTextArea();
        buttonFechar = new javax.swing.JButton();
        setResizable(false);
        labelListagemFuncionarios.setText("Listagem de Horas Extras");
        textAreaListagemFuncionarios.setColumns(20);
        textAreaListagemFuncionarios.setRows(5);
        jScrollPane1.setViewportView(textAreaListagemFuncionarios);
        buttonFechar.setText("Fechar");
        buttonFechar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonFecharActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE).addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE).addComponent(labelListagemFuncionarios, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(buttonFechar, javax.swing.GroupLayout.Alignment.TRAILING)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(labelListagemFuncionarios).addGap(5, 5, 5).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(17, 17, 17).addComponent(buttonFechar).addContainerGap(19, Short.MAX_VALUE)));
        pack();
    }

    private void listar() {
        ErreAgaService erreAgaService = new ErreAgaServiceImpl();
        ClienteWebService clienteWebService = new ClienteWebServiceImpl();
        try {
            List<Funcionario> func = erreAgaService.listFuncionario();
            for (Funcionario funcionario : func) {
                this.textAreaListagemFuncionarios.append(funcionario.getNome() + "\t" + funcionario.getMatricula() + "\t" + funcionario.getSalarioBase() + "\n");
                if (funcionario.getHorasExtras().size() > 0) {
                    for (HoraExtra horaExtra : erreAgaService.listHoraExtra(funcionario.getMatricula())) {
                        this.textAreaListagemFuncionarios.append("Dias chegados as 10h: " + horaExtra.getQuantidadeDiasChegadaAs10h() + "\nDias chegados as 08h:" + horaExtra.getQuantidadeDiasChegadaAs8h() + "\nQtde. de folgas no mês: " + horaExtra.getQuantidadeFolgasMes() + "\nQtde de minutos após as 18h: " + horaExtra.getQuantidadeMinutosApos18h() + "\nQtde de sábados chegados as 10h: " + horaExtra.getQuantidadeSabadosChegadaAs10h() + "\nQtde de sábados chegados as 08h: " + horaExtra.getQuantidadeSabadosChegadaAs8h() + "\n");
                        this.textAreaListagemFuncionarios.append("Valor da hora extrsa: " + clienteWebService.calculaHoraExtras(horaExtra.getQuantidadeDiasChegadaAs8h(), horaExtra.getQuantidadeDiasChegadaAs10h(), horaExtra.getQuantidadeSabadosChegadaAs8h(), horaExtra.getQuantidadeSabadosChegadaAs10h(), horaExtra.getQuantidadeFolgasMes(), horaExtra.getQuantidadeMinutosApos18h()) + "\n-----------------------------------\n");
                    }
                }
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    private void buttonFecharActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
    }

    private javax.swing.JButton buttonFechar;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JLabel labelListagemFuncionarios;

    private javax.swing.JTextArea textAreaListagemFuncionarios;
}
