package br.ita.doacoes.view.cadastrofamilia;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.swingBean.actions.ApplicationAction;
import org.swingBean.gui.JActButton;
import br.ita.doacoes.core.cadastrofamilia.dao.GenericDAOJPA;
import br.ita.doacoes.domain.cadastrofamilia.FamiliaNecessidades;
import br.ita.doacoes.domain.cadastrofamilia.FamiliaNecessidadesPK;
import br.ita.doacoes.view.cadastrofamilia.refresher.Refresher;

public class NecessidadesPanel extends CadastroSimples {

    public NecessidadesPanel() {
        this.setClasse(FamiliaNecessidades.class);
        this.setXmlForm("necessidadesForm.xml");
        this.setXmlTable("necessidadesTable.xml");
        this.setFormName("necessidadesForm");
        this.setTableName("necessidadesTable");
        this.setDao(new GenericDAOJPA(FamiliaNecessidades.class, "Cadastro necessidades"));
        this.init();
        FamiliaSingleton f = FamiliaSingleton.getInstance();
        f.addModel(new ModelToFilter(this.model, "familia.idFamilia"));
    }

    protected void createButtons() {
        botaoInsere = new JActButton("Inserir", new ApplicationAction() {

            public void execute() {
                GenericDAOJPA dao = new GenericDAOJPA(FamiliaNecessidades.class, "Cadastro Familia");
                FamiliaNecessidades c = null;
                try {
                    c = new FamiliaNecessidades();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                panelFormulario.populateBean(c);
                FamiliaSingleton f = FamiliaSingleton.getInstance();
                c.setFamilia(f.getFamilia());
                FamiliaNecessidadesPK pk = new FamiliaNecessidadesPK();
                pk.setIdFamilia(f.getFamilia().getIdFamilia());
                pk.setIdItemTipo(c.getTipoItem().getId());
                c.setFamiliaNecessidadesPK(pk);
                dao.insert(c);
                model.setBeanList(dao.getList());
                panelFormulario.cleanForm();
                Refresher.refresh();
            }
        });
        botaoExclui = new JActButton("Exclui", new ApplicationAction() {

            GenericDAOJPA dao = new GenericDAOJPA(FamiliaNecessidades.class, "Cadastro Familia");

            public void execute() {
                Object o = model.getBeanAt(table.getSelectedRow());
                dao.delete(o);
                model.setBeanList(dao.getList());
                Refresher.refresh();
            }
        });
        botaoUpdate = new JActButton("Atualiza", new ApplicationAction() {

            public void execute() {
                GenericDAOJPA dao = new GenericDAOJPA(FamiliaNecessidades.class, "Cadastro Familia");
                Object c = currentOnForm;
                panelFormulario.populateBean(c);
                dao.update(c);
                panelFormulario.cleanForm();
                currentOnForm = null;
                model.setBeanList(dao.getList());
                Refresher.refresh();
            }
        });
        table.addDoubleClickAction(new ApplicationAction() {

            public void execute() {
                GenericDAOJPA dao = new GenericDAOJPA(FamiliaNecessidades.class, "Cadastro Familia");
                FamiliaSingleton f = FamiliaSingleton.getInstance();
                FamiliaNecessidades o = (FamiliaNecessidades) model.getBeanAt(table.getSelectedRow());
                FamiliaNecessidadesPK pk = new FamiliaNecessidadesPK();
                pk.setIdFamilia(f.getFamilia().getIdFamilia());
                pk.setIdItemTipo(o.getTipoItem().getId());
                o.setFamiliaNecessidadesPK(pk);
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
