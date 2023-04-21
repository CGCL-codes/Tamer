package dominio.ui;

import dominio.entity.Casoestudo;
import dominio.util.BinModeloLista;
import dominio.util.DominioUtil;
import dominio.util.ModeloLista;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author pbarros
 */
public class CasosEstudo extends javax.swing.JInternalFrame {

    private ModeloLista modeloListNecessarios = null;

    private ModeloLista modeloListPossivel = null;

    private ModeloLista listModelNecessario = null;

    private ModeloLista listModelPossivel = null;

    /** Creates new form CasosEstudo */
    public CasosEstudo() {
        displayResultNomeCaso(consultaHQL(QUERY_Casoestudo, null, null));
        BinModeloLista classeLista = new BinModeloLista();
        classeLista.setDescricao("Caso não carregado");
        classeLista.setId(0);
        listModelNecessario = new ModeloLista(classeLista);
        listModelPossivel = new ModeloLista(classeLista);
        initComponents();
    }

    String QUERY_PropEvidNece = "select nosc.proprEvidDiagNodosCaso, " + " nosc.posNecesNodoCaso, nodo.nomeNodos , nosc.prioridadeNodosCaso, " + " nosc.idNodosCaso " + " from Nodos as nodo " + " inner join nodo.nodoscasos as nosc " + " inner join nosc.casoestudo as caes " + " where caes.nomeCaso=:nome ";

    String QUERY_Casoestudo = "from Casoestudo c ";

    String QUERY_Casoestudo2 = "from Casoestudo c where c.nomeCaso=:nome";

    String UPDATE_DiagnosticosCaso = "UPDATE Nodoscaso n set n.posNecesNodoCaso=:possivelNecessario " + " where n.idNodosCaso=idnodo " + " and ";

    private void displayResultNomeCaso(List resultList) {
        listModel = new DefaultListModel();
        for (Object c : resultList) {
            Casoestudo caso = (Casoestudo) c;
            listModel.addElement(caso.getNomeCaso());
        }
    }

    @SuppressWarnings("empty-statement")
    private void carregaDadosGerais(String nomeCaso) {
        String[] paramHQL = { "nome" };
        String[] valParamHQL = { nomeCaso };
        Casoestudo caso = new Casoestudo();
        caso = (Casoestudo) consultaUniqueResultHQL(QUERY_Casoestudo2, paramHQL, valParamHQL, caso);
        jTextFieldNomeCaso.setText(caso.getNomeCaso());
        jTextAreaDescricao.setText(caso.getDescricaoCaso());
        displayResultNodosCaso(consultaHQL(QUERY_PropEvidNece, paramHQL, valParamHQL));
    }

    private Object consultaUniqueResultHQL(String stringHql, String[] parametro, String[] valorParametro, Object classe) {
        try {
            Session session = DominioUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query q = session.createQuery(stringHql);
            if (!(parametro == null)) {
                for (int i = 0; i < parametro.length; i++) {
                    q.setParameter(parametro[i], valorParametro[i]);
                }
            }
            classe = (Casoestudo) q.uniqueResult();
            session.getTransaction().commit();
        } catch (HibernateException he) {
            he.printStackTrace();
        }
        return classe;
    }

    private List consultaHQL(String strHql, String[] parametro, String[] valorParametro) {
        String QUERY = strHql;
        try {
            Session session = DominioUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query q = session.createQuery(QUERY);
            if (!(parametro == null)) {
                for (int i = 0; i < parametro.length; i++) {
                    q.setParameter(parametro[i], valorParametro[i]);
                }
            }
            List resultList = q.list();
            session.getTransaction().commit();
            return resultList;
        } catch (HibernateException he) {
            he.printStackTrace();
            return null;
        }
    }

    private int atualisaDados(String strHql, String[] parametro, String[] valorParametro) {
        String hqlUpdate = strHql;
        int linhasAtualisadas = 0;
        String QUERY = strHql;
        Object classe = new Object();
        try {
            Session session = DominioUtil.getSessionFactory().openSession();
            session.beginTransaction();
            Query q = session.createQuery(hqlUpdate);
            if (!(parametro == null)) {
                for (int i = 0; i < parametro.length; i++) {
                    q.setParameter(parametro[i], valorParametro[i]);
                }
            }
            linhasAtualisadas = q.executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException he) {
            he.printStackTrace();
        }
        return linhasAtualisadas;
    }

    private void displayResultNodosCaso(List resultList) {
        DefaultTableCellRenderer esquerda = new DefaultTableCellRenderer();
        DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
        DefaultTableCellRenderer direita = new DefaultTableCellRenderer();
        esquerda.setHorizontalAlignment(SwingConstants.LEFT);
        esquerda.setHorizontalAlignment(SwingConstants.LEFT);
        centro.setHorizontalAlignment(SwingConstants.CENTER);
        direita.setHorizontalAlignment(SwingConstants.RIGHT);
        String resultProprEvidDiagNodosCaso = null;
        String resultPosNecesNodoCaso = null;
        String resultNomeNodo = null;
        String resultprioridadeNodosCaso = null;
        Integer resultIdNodosCaso = 0;
        modeloTabela = (DefaultTableModel) jTablePriorNodo.getModel();
        jTablePriorNodo.getColumnModel().getColumn(0).setCellRenderer(esquerda);
        jTablePriorNodo.getColumnModel().getColumn(1).setCellRenderer(centro);
        modeloTabela.setNumRows(0);
        BinModeloLista classeListaPos = new BinModeloLista();
        BinModeloLista classeListaNec = new BinModeloLista();
        System.out.println(listModelNecessario.getSize());
        for (int i = 0; i < resultList.size(); i++) {
            Object[] result = (Object[]) resultList.get(i);
            resultProprEvidDiagNodosCaso = (String) result[0];
            resultPosNecesNodoCaso = (String) result[1];
            resultNomeNodo = (String) result[2];
            resultprioridadeNodosCaso = (String) result[3];
            resultIdNodosCaso = (Integer) result[4];
            if (resultProprEvidDiagNodosCaso.equals("D")) {
                if (resultPosNecesNodoCaso.equals("N")) {
                    classeListaNec.setDescricao(resultNomeNodo);
                    classeListaNec.setId(resultIdNodosCaso);
                    listModelNecessario.add(classeListaNec);
                } else {
                    classeListaPos.setDescricao(resultNomeNodo);
                    classeListaPos.setId(resultIdNodosCaso);
                    listModelPossivel.add(classeListaPos);
                }
            } else {
                modeloTabela.addRow(new String[] { (String) resultNomeNodo, (String) resultprioridadeNodosCaso });
            }
        }
        repaint();
    }

    private DefaultTableModel modeloTabela;

    private DefaultTableModel modeloColuna;

    private DefaultListModel listModel;

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldNomeCaso = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaDescricao = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList33 = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTablePriorNodo = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jPanel5 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        setClosable(true);
        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jLabel1.setText("Nome:");
        jTextAreaDescricao.setColumns(20);
        jTextAreaDescricao.setRows(5);
        jScrollPane2.setViewportView(jTextAreaDescricao);
        jLabel2.setText("Descrição:");
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jTextFieldNomeCaso, javax.swing.GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE).addComponent(jLabel1).addComponent(jLabel2))).addContainerGap(126, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(19, 19, 19).addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextFieldNomeCaso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(4, 4, 4).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(57, Short.MAX_VALUE)));
        jTabbedPane1.addTab("Casos Estudo", jPanel1);
        jLabel3.setText("Escolha quais dos nodos diagnosticos disponiveis aparecerão nesta rede");
        jButton2.setText(">>");
        jButton2.setPreferredSize(new java.awt.Dimension(79, 23));
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddDiagnostico(evt);
            }
        });
        jButton3.setText(">Todos>");
        jButton4.setText("<Todos<");
        jButton5.setText("<<");
        jButton5.setPreferredSize(new java.awt.Dimension(79, 23));
        jList2.setModel(new javax.swing.AbstractListModel() {

            String[] strings = { "ClasseII", "ClasseIII", "ClasseIV", "Congestão Pulmonar", "DAC", "DM com Insulina", "IC", " " };

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        jScrollPane3.setViewportView(jList2);
        jList33.setModel(new javax.swing.AbstractListModel() {

            String[] strings = { "ClasseI" };

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        jScrollPane4.setViewportView(jList33);
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(51, 51, 51).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addComponent(jScrollPane3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jButton3).addComponent(jButton4).addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jLabel3)).addGap(116, 116, 116)));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGap(21, 21, 21).addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(146, Short.MAX_VALUE)));
        jTabbedPane1.addTab("Diagnósticos", jPanel2);
        jTablePriorNodo.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "NOME", "PRIORIDADE" }) {

            Class[] types = new Class[] { java.lang.String.class, java.lang.String.class };

            boolean[] canEdit = new boolean[] { false, false };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane5.setViewportView(jTablePriorNodo);
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup().addContainerGap(49, Short.MAX_VALUE).addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGap(72, 72, 72).addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(81, Short.MAX_VALUE)));
        jTabbedPane1.addTab("Prioridade Nodo", jPanel3);
        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 513, Short.MAX_VALUE));
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 319, Short.MAX_VALUE));
        jTabbedPane1.addTab("Nodos Bogus", jPanel4);
        jTabbedPane1.addTab("Arquivos", jTabbedPane2);
        jTabbedPane1.addTab("Imagens", jTabbedPane3);
        jTabbedPane1.addTab("Casos Reais", jTabbedPane4);
        jList1.setModel(listModel);
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList1ValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);
        jPanel5.setBackground(new java.awt.Color(204, 204, 204));
        jButton1.setText("jButton1");
        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(115, Short.MAX_VALUE)));
        jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addComponent(jButton1).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE))).addContainerGap()));
        pack();
    }

    private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (evt.getValueIsAdjusting() == false) {
            if (jList1.getSelectedIndex() != -1) {
                carregaDadosGerais(jList1.getSelectedValue().toString());
                jTabbedPane1.setSelectedIndex(0);
            }
        }
    }

    private void jButtonAddDiagnostico(java.awt.event.ActionEvent evt) {
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JButton jButton3;

    private javax.swing.JButton jButton4;

    private javax.swing.JButton jButton5;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JList jList1;

    private javax.swing.JList jList2;

    private javax.swing.JList jList33;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JScrollPane jScrollPane4;

    private javax.swing.JScrollPane jScrollPane5;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JTabbedPane jTabbedPane2;

    private javax.swing.JTabbedPane jTabbedPane3;

    private javax.swing.JTabbedPane jTabbedPane4;

    private javax.swing.JTable jTablePriorNodo;

    private javax.swing.JTextArea jTextAreaDescricao;

    private javax.swing.JTextField jTextFieldNomeCaso;
}
