package br.ita.doacoes.view.voluntarios;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.swingBean.actions.ApplicationAction;
import org.swingBean.descriptor.BeanTableModel;
import org.swingBean.descriptor.TableFieldDescriptor;
import org.swingBean.descriptor.XMLDescriptorFactory;
import org.swingBean.gui.JBeanTable;
import br.ita.doacoes.core.templates.GenericDAOJPA;
import br.ita.doacoes.domain.cadastrodoacoes.Pessoa;
import br.ita.doacoes.domain.cadastrodoacoes.PessoaFisica;
import br.ita.doacoes.domain.voluntarios.TelefonePessoa;
import br.ita.doacoes.domain.voluntarios.Voluntario;
import br.ita.doacoes.view.controledoacoes.TipoItemCombo;
import br.ita.doacoes.view.templates.CadastroSimples;
import br.ita.doacoes.view.templates.ConsultaDetalheMestreDetalhe;
import br.ita.doacoes.view.templates.ConsultaDetalheSimples;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class ConsultaVoluntario implements ActionListener {

    private JLabel jlabelInf = new JLabel("Informa��o");

    private JLabel jlabelLogica = new JLabel("L�gica");

    private JLabel jlabelRestricao = new JLabel("Campo de Restri��o");

    private JButton jbuttonFiltrar = new JButton();

    private JButton jbuttonDetalhar = new JButton();

    private JButton jbuttonLimpar = new JButton();

    private JComboBox jcombobox1;

    private JTextField jtextfield = new JTextField();

    private JComboBox jcombobox2 = new JComboBox(new DefaultComboBoxModel(new String[] { "Iniciado", "Contendo" }));

    private JComboBox jcombobox3 = new JComboBox(new DefaultComboBoxModel(new String[] { "Contendo" }));

    private TableFieldDescriptor tableDescriptor;

    private BeanTableModel tableModel;

    private JBeanTable table;

    private JComboBox jcomboDisponibilidade;

    private JComboBox jcomboHabilidade;

    private Object procura;

    private Map<String, String> mapss = new HashMap<String, String>();

    public static void main(String[] args) {
        ConsultaVoluntario consulta = new ConsultaVoluntario();
        consulta.init();
    }

    public void init() {
        tableDescriptor = XMLDescriptorFactory.getTableFieldDescriptor(Voluntario.class, "consultaVoluntarioTable.xml", "consultaTable");
        tableModel = new BeanTableModel<Voluntario>(tableDescriptor);
        table = new JBeanTable(tableModel);
        jcombobox1 = new JComboBox(new DefaultComboBoxModel(new String[] { "Nome", "Disponibilidade", "Habilidades" }));
        jcomboDisponibilidade = new JComboBox(new DisponibilidadeCombo());
        jcomboHabilidade = new JComboBox(new HabilidadeCombo());
        mapss.put("Nome", "pessoa.nome");
        mapss.put("Disponibilidade", "disponibilidade");
        mapss.put("Habilidades", "habilidades");
        JFrame frame = new JFrame("Consulta de Volunt�rios");
        frame.setSize(700, 400);
        frame.setLocation(100, 100);
        JScrollPane scrollPane = new JScrollPane(table);
        tableModel.orderByProperty("pessoa.nome");
        tableModel.setBeanList(getList());
        table.enableQuickEditing();
        table.addDoubleClickAction(new ApplicationAction() {

            public void execute() {
                detalhaPesquisa();
            }
        });
        JPanel jpanel1 = new JPanel();
        FormLayout formlayout1 = new FormLayout("2dlu, 180px, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu, pref, 2dlu", "2dlu, pref, 2dlu, pref, 2dlu");
        jpanel1.setBorder(BorderFactory.createTitledBorder("Par�metros da pesquisa"));
        CellConstraints cc = new CellConstraints();
        jpanel1.setLayout(formlayout1);
        jbuttonFiltrar.setText("Filtrar");
        jbuttonDetalhar.setText("Detalhar");
        jbuttonLimpar.setText("Limpar Pesquisa");
        jpanel1.add(jlabelInf, cc.xy(2, 2));
        jpanel1.add(jlabelLogica, cc.xy(4, 2));
        jpanel1.add(jlabelRestricao, cc.xy(6, 2));
        jpanel1.add(jtextfield, cc.xy(2, 4));
        jpanel1.add(jcomboDisponibilidade, cc.xy(2, 4));
        jpanel1.add(jcomboHabilidade, cc.xy(2, 4));
        jcomboDisponibilidade.setVisible(false);
        jcomboHabilidade.setVisible(false);
        jcombobox1.addActionListener(this);
        jpanel1.add(jcombobox2, cc.xy(4, 4));
        jpanel1.add(jcombobox3, cc.xy(4, 4));
        jcombobox3.setVisible(false);
        jpanel1.add(jcombobox1, cc.xy(6, 4));
        jpanel1.add(jbuttonFiltrar, cc.xy(8, 4));
        jpanel1.add(jbuttonDetalhar, cc.xy(10, 4));
        jpanel1.add(jbuttonLimpar, cc.xy(12, 4));
        jbuttonFiltrar.addActionListener(this);
        jbuttonDetalhar.addActionListener(this);
        jbuttonLimpar.addActionListener(this);
        frame.setLayout(new BorderLayout());
        frame.add(jpanel1, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
    }

    public List<Pessoa> getList() {
        List<Pessoa> list;
        GenericDAOJPA dao = new GenericDAOJPA(Voluntario.class);
        list = dao.getList();
        return list;
    }

    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == jbuttonFiltrar) {
            String field = mapss.get(jcombobox1.getSelectedItem().toString());
            String valor = null;
            if (jcombobox1.getSelectedItem().toString() == "Nome") valor = jtextfield.getText();
            if (jcombobox1.getSelectedItem().toString() == "Disponibilidade") valor = jcomboDisponibilidade.getSelectedItem().toString();
            if (jcombobox1.getSelectedItem().toString() == "Habilidades") valor = jcomboHabilidade.getSelectedItem().toString();
            System.out.println(field + " " + valor);
            if (jcombobox1.getSelectedItem().toString() == "Nome") {
                if (jcombobox2.getSelectedItem().equals("Iniciado")) tableModel.filterStartedBy(field, valor); else tableModel.filterContains(field, valor);
            } else tableModel.filterContains(field, valor);
        }
        if (event.getSource() == jbuttonDetalhar) {
            detalhaPesquisa();
        }
        if (event.getSource() == jbuttonLimpar) {
            tableModel.cleanFilter();
            jtextfield.setText("");
        }
        if (event.getSource() == jcombobox1) {
            jtextfield.setVisible(false);
            jcomboDisponibilidade.setVisible(false);
            jcomboHabilidade.setVisible(false);
            jcombobox3.setVisible(false);
            jcombobox2.setVisible(false);
            if (jcombobox1.getSelectedItem().toString() == "Nome") {
                jtextfield.setVisible(true);
                jcombobox2.setVisible(true);
            }
            if (jcombobox1.getSelectedItem().toString() == "Disponibilidade") {
                jcomboDisponibilidade.setVisible(true);
                jcombobox3.setVisible(true);
            }
            if (jcombobox1.getSelectedItem().toString() == "Habilidades") {
                jcomboHabilidade.setVisible(true);
                jcombobox3.setVisible(true);
            }
        }
    }

    private void detalhaPesquisa() {
        if (table.getSelectedRow() < 0) return;
        JFrame frame = new JFrame("Consulta de Volunt�rios");
        ConsultaDetalheSimples consulta = new ConsultaDetalheSimples(frame);
        consulta.setClasse(Voluntario.class);
        consulta.setXmlForm("consultaVoluntarioDetalheForm.xml");
        consulta.setFormName("formName");
        consulta.setDao(new GenericDAOJPA(Voluntario.class));
        Object vonluntario = tableModel.getBeanAt(table.getSelectedRow());
        consulta.init(vonluntario);
        frame.setSize(400, 380);
        frame.getContentPane().add(consulta);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
    }
}
