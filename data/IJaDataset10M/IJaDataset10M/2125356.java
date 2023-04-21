package br.ita.doacoes.view.cadastrofamilia;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.swingBean.actions.ApplicationAction;
import org.swingBean.gui.JActButton;
import br.ita.doacoes.core.cadastrofamilia.dao.GenericDAOJPA;
import br.ita.doacoes.domain.cadastrofamilia.TelefoneFamilia;
import br.ita.doacoes.view.cadastrofamilia.refresher.Refresher;

public class TelefoneFamiliaPanel extends CadastroSimples {

    public TelefoneFamiliaPanel() {
        this.setClasse(TelefoneFamilia.class);
        this.setXmlForm("telefoneForm.xml");
        this.setXmlTable("telefoneTable.xml");
        this.setFormName("telefoneForm");
        this.setTableName("telefoneTable");
        this.setDao(new GenericDAOJPA(TelefoneFamilia.class, "Cadastro telefone"));
        this.init();
        FamiliaSingleton f = FamiliaSingleton.getInstance();
        f.addModel(new ModelToFilter(this.model, "idFamilia.idFamilia"));
    }

    protected void createButtons() {
        botaoInsere = new JActButton("Inserir", new ApplicationAction() {

            public void execute() {
                GenericDAOJPA dao = new GenericDAOJPA(TelefoneFamilia.class, "Cadastro Familia");
                TelefoneFamilia c = null;
                try {
                    c = new TelefoneFamilia();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                panelFormulario.populateBean(c);
                FamiliaSingleton f = FamiliaSingleton.getInstance();
                c.setIdFamilia(f.getFamilia());
                dao.insert(c);
                model.setBeanList(dao.getList());
                panelFormulario.cleanForm();
                Refresher.refresh();
            }
        });
        botaoExclui = new JActButton("Exclui", new ApplicationAction() {

            GenericDAOJPA dao = new GenericDAOJPA(TelefoneFamilia.class, "Cadastro Familia");

            public void execute() {
                Object o = model.getBeanAt(table.getSelectedRow());
                dao.delete(o);
                model.setBeanList(dao.getList());
                Refresher.refresh();
            }
        });
        botaoUpdate = new JActButton("Atualiza", new ApplicationAction() {

            public void execute() {
                GenericDAOJPA dao = new GenericDAOJPA(TelefoneFamilia.class, "Cadastro Familia");
                Object c = currentOnForm;
                panelFormulario.populateBean(c);
                dao.update(c);
                panelFormulario.cleanForm();
                currentOnForm = null;
                model.setBeanList(dao.getList());
                panelFormulario.cleanForm();
                Refresher.refresh();
            }
        });
        table.addDoubleClickAction(new ApplicationAction() {

            public void execute() {
                GenericDAOJPA dao = new GenericDAOJPA(TelefoneFamilia.class, "Cadastro Familia");
                Object o = model.getBeanAt(table.getSelectedRow());
                o = dao.getById(o);
                panelFormulario.setBean(o);
                currentOnForm = o;
            }
        });
        botaoLimpa = new JActButton("Limpar Formul�rio", new ApplicationAction() {

            public void execute() {
                panelFormulario.cleanForm();
                currentOnForm = null;
            }
        });
    }

    @Override
    protected void putComponentsInPanel() {
        JPanel newform = new JPanel();
        FamiliaLabelPanel jfamiliapanel = new FamiliaLabelPanel();
        newform.setLayout(new BorderLayout());
        newform.add(jfamiliapanel, BorderLayout.NORTH);
        newform.add(panelFormulario, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        add(newform, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(panelButton, BorderLayout.SOUTH);
    }
}
