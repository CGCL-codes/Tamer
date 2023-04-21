package casdadm.view.entrevistaRenda;

import casdadm.core.DAO;
import casdadm.core.GenericJPADAO;
import casdadm.domain.entrevistaRenda.BemMovel;
import casdadm.domain.entrevistaRenda.EntrevistadoRenda;
import casdadm.domain.entrevistaRenda.Familia;
import casdadm.domain.common.Pessoa;
import casdadm.domain.entrevistaRenda.Imovel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.swingBean.actions.ApplicationAction;
import org.swingBean.descriptor.BeanTableModel;
import org.swingBean.descriptor.GenericFieldDescriptor;
import org.swingBean.descriptor.TableFieldDescriptor;
import org.swingBean.descriptor.XMLDescriptorFactory;
import org.swingBean.gui.JActButton;
import org.swingBean.gui.JBeanPanel;
import org.swingBean.gui.JBeanTable;

public class ConsultaEntrevistadoPanel extends JPanel {

    private Integer id;

    private String nome;

    private Pessoa pessoaAtual = new Pessoa();

    private JBeanPanel<Pessoa> panel1;

    protected BeanTableModel model;

    protected BeanTableModel model2;

    protected JBeanTable table;

    protected JBeanTable table2;

    protected BeanTableModel<Pessoa> model1;

    protected JBeanTable table1;

    protected JActButton botaoFiltra;

    protected JActButton botaoLimpa;

    protected JTextField m_jtextfield1 = new JTextField();

    protected JTextField m_jtextfield2 = new JTextField();

    protected JPanel panelButton;

    private String xmlTable;

    private String tableName;

    private Class classe;

    private GenericJPADAO dao;

    private Class classe1;

    private GenericJPADAO dao1;

    private String xmlTable2;

    private String tableName2;

    private Class classe2;

    private GenericJPADAO dao2;

    private String xmlTable3;

    private String tableName3;

    private Class classe3;

    protected BeanTableModel model3;

    protected JBeanTable table3;

    private GenericJPADAO dao3;

    private String xmlTable4;

    private String tableName4;

    private Class classe4;

    protected BeanTableModel model4;

    protected JBeanTable table4;

    private GenericJPADAO dao4;

    private String xmlTable5;

    private String tableName5;

    private Class classe5;

    protected BeanTableModel model5;

    protected JBeanTable table5;

    private GenericJPADAO dao5;

    private String xmlTable6;

    private String tableName6;

    private Class classe6;

    protected BeanTableModel model6;

    protected JBeanTable table6;

    private GenericJPADAO dao6;

    private String xmlTable7;

    private String tableName7;

    private Class classe7;

    protected BeanTableModel model7;

    protected JBeanTable table7;

    private GenericJPADAO dao7;

    private String xmlTable8;

    private String tableName8;

    private Class classe8;

    protected BeanTableModel model8;

    protected JBeanTable table8;

    private GenericJPADAO dao8;

    protected Object currentOnForm;

    public void init() {
        initComponents();
        createButtons();
        createButtonPanel();
        putComponentsInPanel();
        initFromDatabase();
    }

    protected void initFromDatabase() {
    }

    protected void putComponentsInPanel() {
        setLayout(new BorderLayout());
        JPanel mestrePanel = new JPanel();
        FormLayout formlayout = new FormLayout("FILL:500DLU,FILL:DEFAULT:NONE", "CENTER:40DLU,CENTER:13DLU,CENTER:30DLU,CENTER:13DLU,CENTER:30DLU,CENTER:13DLU,CENTER:30DLU,CENTER:13DLU,CENTER:30DLU,CENTER:13DLU,CENTER:30DLU,CENTER:13DLU,CENTER:30DLU,CENTER:13DLU,CENTER:30DLU,CENTER:13DLU,CENTER:30DLU");
        CellConstraints cc = new CellConstraints();
        mestrePanel.setLayout(formlayout);
        mestrePanel.add(panel1, cc.xy(1, 1));
        mestrePanel.add(panelButton, cc.xy(2, 1));
        mestrePanel.add(new JLabel("Bem Movel"), cc.xy(1, 12));
        mestrePanel.add(new JScrollPane(table), cc.xy(1, 13));
        mestrePanel.add(new JLabel("Dados Entrevistado Renda"), cc.xy(1, 2));
        mestrePanel.add(new JScrollPane(table2), cc.xy(1, 3));
        mestrePanel.add(new JLabel("Dados Familia                                                                                                                                                                                                                                                               Renda Liquida da Familia:"), cc.xy(1, 4));
        mestrePanel.add(m_jtextfield2, cc.xy(2, 4));
        mestrePanel.add(new JScrollPane(table3), cc.xy(1, 5));
        mestrePanel.add(new JLabel("Curso"), cc.xy(1, 10));
        mestrePanel.add(new JScrollPane(table4), cc.xy(1, 11));
        mestrePanel.add(new JLabel("Despesa                                                                                                                                                                                                                                                                                    Total de Despesas: "), cc.xy(1, 8));
        mestrePanel.add(m_jtextfield1, cc.xy(2, 8));
        mestrePanel.add(new JScrollPane(table5), cc.xy(1, 9));
        mestrePanel.add(new JLabel("Parente"), cc.xy(1, 6));
        mestrePanel.add(new JScrollPane(table6), cc.xy(1, 7));
        mestrePanel.add(new JLabel("Imovel"), cc.xy(1, 14));
        mestrePanel.add(new JScrollPane(table7), cc.xy(1, 15));
        mestrePanel.add(new JLabel("Endereco do Imovel"), cc.xy(1, 16));
        mestrePanel.add(new JScrollPane(table8), cc.xy(1, 17));
        add(mestrePanel);
    }

    protected void initComponents() {
        TableFieldDescriptor tabledescriptor = XMLDescriptorFactory.getTableFieldDescriptor(classe, xmlTable, tableName);
        TableFieldDescriptor tabledescriptor2 = XMLDescriptorFactory.getTableFieldDescriptor(classe2, xmlTable2, tableName2);
        TableFieldDescriptor tabledescriptor3 = XMLDescriptorFactory.getTableFieldDescriptor(classe3, xmlTable3, tableName3);
        TableFieldDescriptor tabledescriptor4 = XMLDescriptorFactory.getTableFieldDescriptor(classe4, xmlTable4, tableName4);
        TableFieldDescriptor tabledescriptor5 = XMLDescriptorFactory.getTableFieldDescriptor(classe5, xmlTable5, tableName5);
        TableFieldDescriptor tabledescriptor6 = XMLDescriptorFactory.getTableFieldDescriptor(classe6, xmlTable6, tableName6);
        TableFieldDescriptor tabledescriptor7 = XMLDescriptorFactory.getTableFieldDescriptor(classe7, xmlTable7, tableName7);
        TableFieldDescriptor tabledescriptor8 = XMLDescriptorFactory.getTableFieldDescriptor(classe8, xmlTable8, tableName8);
        GenericFieldDescriptor descriptor = XMLDescriptorFactory.getFieldDescriptor(Pessoa.class, "casdadm\\view\\entrevistaRenda\\inicialForm.xml", "inicialForm");
        panel1 = new JBeanPanel<Pessoa>(Pessoa.class, descriptor);
        TableFieldDescriptor tableDescriptor = XMLDescriptorFactory.getTableFieldDescriptor(Pessoa.class, "casdadm\\view\\entrevistaRenda\\inicialTable.xml", "inicialTable");
        model1 = new BeanTableModel<Pessoa>(tableDescriptor);
        table1 = new JBeanTable(model1);
        model = new BeanTableModel(tabledescriptor);
        model2 = new BeanTableModel(tabledescriptor2);
        model3 = new BeanTableModel(tabledescriptor3);
        model4 = new BeanTableModel(tabledescriptor4);
        model5 = new BeanTableModel(tabledescriptor5);
        model6 = new BeanTableModel(tabledescriptor6);
        model7 = new BeanTableModel(tabledescriptor7);
        model8 = new BeanTableModel(tabledescriptor8);
        table = new JBeanTable(model);
        table2 = new JBeanTable(model2);
        table3 = new JBeanTable(model3);
        table4 = new JBeanTable(model4);
        table5 = new JBeanTable(model5);
        table6 = new JBeanTable(model6);
        table7 = new JBeanTable(model7);
        table8 = new JBeanTable(model8);
        model.setBeanList(dao.getList());
        model1.setBeanList(dao1.getList());
        model2.setBeanList(dao2.getList());
        model3.setBeanList(dao3.getList());
        model4.setBeanList(dao4.getList());
        model5.setBeanList(dao5.getList());
        model6.setBeanList(dao6.getList());
        model7.setBeanList(dao7.getList());
        model8.setBeanList(dao8.getList());
    }

    private BigDecimal b = new BigDecimal(1000.00);

    protected void createButtons() {
        botaoFiltra = new JActButton("Filtrar", new ApplicationAction() {

            public void execute() {
                model.setBeanList(dao.getList());
                model1.setBeanList(dao1.getList());
                model2.setBeanList(dao2.getList());
                model3.setBeanList(dao3.getList());
                model4.setBeanList(dao4.getList());
                model5.setBeanList(dao5.getList());
                model6.setBeanList(dao6.getList());
                model7.setBeanList(dao7.getList());
                model8.setBeanList(dao8.getList());
                m_jtextfield1.setText(" ");
                m_jtextfield2.setText(" ");
                panel1.populateBean(pessoaAtual);
                nome = (String) panel1.getPropertyValue("pessoa");
                id = (Integer) model1.getValueAt(model1.getIndexStartedBy("nome", nome), "id");
                model2.filterContains("id", id.toString());
                model.setBeanList(dao.getObjectFromQuery("select i from BemMovel i where i.entrevistadorenda.id=" + id));
                model3.setBeanList(dao3.getObjectFromQuery("select i from Familia i where i.entrevistadorenda.id=" + id));
                model4.setBeanList(dao4.getObjectFromQuery("select i from Curso i where i.entrevistadorenda.id=" + id));
                model5.setBeanList(dao5.getObjectFromQuery("select i from Despesa i where i.entrevistadorenda.id=" + id));
                model6.setBeanList(dao6.getObjectFromQuery("select i from Parente i where i.entrevistadorenda.id=" + id));
                model7.setBeanList(dao7.getObjectFromQuery("select i from Imovel i where i.entrevistadorenda.id=" + id));
                List<Imovel> l = model7.getFilteredList();
                int a = 1;
                String consulta = new String();
                for (Object o : l) {
                    Imovel iAux = (Imovel) o;
                    if (a == 1) {
                        consulta = "i.id=" + iAux.getEndereco().getId();
                    } else {
                        consulta = consulta + " or i.id=" + iAux.getEndereco().getId();
                    }
                    a = a + 1;
                }
                model8.setBeanList(dao8.getObjectFromQuery("select i from Endereco i where " + consulta));
                List total_despesa = dao5.getObjectFromQuery("select sum(i.valor) from Despesa i where i.entrevistadorenda.id=" + id);
                List renda_liquida = dao6.getObjectFromQuery("select sum(i.renda) from Parente i where i.entrevistadorenda.id=" + id);
                if (!total_despesa.isEmpty() || total_despesa != null) {
                    for (Object o : total_despesa) {
                        if (o != null) m_jtextfield1.setText(Double.valueOf((Double) o).toString());
                    }
                }
                if (!renda_liquida.isEmpty()) {
                    for (Object ob : renda_liquida) {
                        m_jtextfield2.setText((String) ob);
                    }
                }
            }
        });
    }

    protected void createButtonPanel() {
        panelButton = new JPanel();
        setLayout(new BorderLayout());
        panelButton.add(botaoFiltra);
    }

    public Class getClasse1() {
        return classe1;
    }

    public void setClasse1(Class classe1) {
        this.classe1 = classe1;
    }

    public GenericJPADAO getDao1() {
        return dao1;
    }

    public void setDao1(GenericJPADAO dao1) {
        this.dao1 = dao1;
    }

    public GenericJPADAO getDao() {
        return dao;
    }

    public void setDao(GenericJPADAO dao) {
        this.dao = dao;
    }

    public Class getClasse() {
        return classe;
    }

    public void setClasse(Class classe) {
        this.classe = classe;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getXmlTable() {
        return xmlTable;
    }

    public void setXmlTable(String xmlTable) {
        this.xmlTable = xmlTable;
    }

    public Class getClasse2() {
        return classe2;
    }

    public void setClasse2(Class classe2) {
        this.classe2 = classe2;
    }

    public GenericJPADAO getDao2() {
        return dao2;
    }

    public void setDao2(GenericJPADAO dao2) {
        this.dao2 = dao2;
    }

    public String getTableName2() {
        return tableName2;
    }

    public void setTableName2(String tableName2) {
        this.tableName2 = tableName2;
    }

    public String getXmlTable2() {
        return xmlTable2;
    }

    public void setXmlTable2(String xmlTable2) {
        this.xmlTable2 = xmlTable2;
    }

    public Class getClasse3() {
        return classe3;
    }

    public void setClasse3(Class classe3) {
        this.classe3 = classe3;
    }

    public GenericJPADAO getDao3() {
        return dao3;
    }

    public void setDao3(GenericJPADAO dao3) {
        this.dao3 = dao3;
    }

    public String getTableName3() {
        return tableName3;
    }

    public void setTableName3(String tableName3) {
        this.tableName3 = tableName3;
    }

    public String getXmlTable3() {
        return xmlTable3;
    }

    public void setXmlTable3(String xmlTable3) {
        this.xmlTable3 = xmlTable3;
    }

    public Class getClasse4() {
        return classe4;
    }

    public void setClasse4(Class classe4) {
        this.classe4 = classe4;
    }

    public GenericJPADAO getDao4() {
        return dao4;
    }

    public void setDao4(GenericJPADAO dao4) {
        this.dao4 = dao4;
    }

    public String getTableName4() {
        return tableName4;
    }

    public void setTableName4(String tableName4) {
        this.tableName4 = tableName4;
    }

    public String getXmlTable4() {
        return xmlTable4;
    }

    public void setXmlTable4(String xmlTable4) {
        this.xmlTable4 = xmlTable4;
    }

    public Class getClasse5() {
        return classe5;
    }

    public void setClasse5(Class classe5) {
        this.classe5 = classe5;
    }

    public GenericJPADAO getDao5() {
        return dao5;
    }

    public void setDao5(GenericJPADAO dao5) {
        this.dao5 = dao5;
    }

    public JBeanTable getTable5() {
        return table5;
    }

    public void setTable5(JBeanTable table5) {
        this.table5 = table5;
    }

    public String getTableName5() {
        return tableName5;
    }

    public void setTableName5(String tableName5) {
        this.tableName5 = tableName5;
    }

    public String getXmlTable5() {
        return xmlTable5;
    }

    public void setXmlTable5(String xmlTable5) {
        this.xmlTable5 = xmlTable5;
    }

    public Class getClasse6() {
        return classe6;
    }

    public void setClasse6(Class classe6) {
        this.classe6 = classe6;
    }

    public Class getClasse7() {
        return classe7;
    }

    public void setClasse7(Class classe7) {
        this.classe7 = classe7;
    }

    public GenericJPADAO getDao6() {
        return dao6;
    }

    public void setDao6(GenericJPADAO dao6) {
        this.dao6 = dao6;
    }

    public GenericJPADAO getDao7() {
        return dao7;
    }

    public void setDao7(GenericJPADAO dao7) {
        this.dao7 = dao7;
    }

    public String getTableName6() {
        return tableName6;
    }

    public void setTableName6(String tableName6) {
        this.tableName6 = tableName6;
    }

    public String getTableName7() {
        return tableName7;
    }

    public void setTableName7(String tableName7) {
        this.tableName7 = tableName7;
    }

    public String getXmlTable6() {
        return xmlTable6;
    }

    public void setXmlTable6(String xmlTable6) {
        this.xmlTable6 = xmlTable6;
    }

    public String getXmlTable7() {
        return xmlTable7;
    }

    public void setXmlTable7(String xmlTable7) {
        this.xmlTable7 = xmlTable7;
    }

    public Class getClasse8() {
        return classe8;
    }

    public void setClasse8(Class classe8) {
        this.classe8 = classe8;
    }

    public GenericJPADAO getDao8() {
        return dao8;
    }

    public void setDao8(GenericJPADAO dao8) {
        this.dao8 = dao8;
    }

    public String getTableName8() {
        return tableName8;
    }

    public void setTableName8(String tableName8) {
        this.tableName8 = tableName8;
    }

    public String getXmlTable8() {
        return xmlTable8;
    }

    public void setXmlTable8(String xmlTable8) {
        this.xmlTable8 = xmlTable8;
    }
}
