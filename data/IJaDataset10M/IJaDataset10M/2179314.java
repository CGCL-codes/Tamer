package br.ufpa.spider.mplan.logic.project;

import br.ufpa.spider.mplan.model.ApprovalProject;
import br.ufpa.spider.mplan.model.CollectionProcedureProject;
import br.ufpa.spider.mplan.model.MeasureProject;
import br.ufpa.spider.mplan.model.Organization;
import br.ufpa.spider.mplan.model.Project;
import br.ufpa.spider.mplan.model.User;
import br.ufpa.spider.mplan.persistence.ApprovalProjectDAO;
import br.ufpa.spider.mplan.persistence.CollectionProcedureProjectDAO;
import br.ufpa.spider.mplan.persistence.MeasureProjectDAO;
import br.ufpa.spider.mplan.persistence.ProjectDAO;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityExistsException;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author Kaio Valente
 */
public class DefinitionCollectionProcedureProjectController extends Window implements AfterCompose {

    private Window window;

    private Combobox measureCombobox;

    private Listbox typeListBox;

    private Textbox version;

    private Textbox moment;

    private Textbox frequency;

    private Textbox periodCollection;

    private Textbox instruction;

    private Textbox usedtool;

    private Listbox responsible;

    private Listbox responsiblePreservation;

    private Textbox storage;

    private Textbox toolVerification;

    private Textbox nameCheckList;

    private User currentUser;

    public Session session;

    private Boolean update = false;

    private Organization userOrganization;

    private Project userProject;

    private CollectionProcedureProject cpp;

    private Div mainDiv;

    private static final String SUCCESS_INSERT = "Organização registrada com sucesso!";

    private static final String SUCCESS_UPDATE = "Organização atualizada com sucesso!";

    private static final String ERROR_BD = "Houve um erro ao acessar o banco de dados!";

    private static final String TITLE = "Spider Mplan";

    public void afterCompose() {
        Components.wireVariables(this, this);
        Components.addForwards(this, this);
        session = window.getDesktop().getSession();
        currentUser = (User) session.getAttribute("user");
        update = (Boolean) session.getAttribute("update");
        userOrganization = (Organization) session.getAttribute("userOrganization");
        userProject = (Project) session.getAttribute("userProject");
        onLoad();
    }

    public void onLoad() {
        if (update == null) {
            measureCombobox.getItems().clear();
            List<MeasureProject> measure = new ArrayList<MeasureProject>();
            measure = MeasureProjectDAO.getAllMeasureByProject(userProject.getId());
            for (int i = 0; i < measure.size(); ++i) {
                Comboitem item = new Comboitem();
                item = measureCombobox.appendItem(measure.get(i).getId() + " - " + measure.get(i).getName());
                item.setValue(measure.get(i));
            }
        } else {
            cpp = (CollectionProcedureProject) session.getAttribute("collectionProcedureProject");
            Comboitem item = measureCombobox.appendItem(cpp.getMeasureproject().getId() + "-" + cpp.getMeasureproject().getName());
            measureCombobox.setSelectedItem(item);
            List<ApprovalProject> ap = ApprovalProjectDAO.getAllApprovalByMeasure(cpp.getMeasureproject().getId());
            version.setText(ap.get(ap.size() - 1).getVersion());
            responsible.setSelectedIndex(cpp.getResponsible());
            moment.setText(cpp.getMoment());
            frequency.setText(cpp.getFrequency());
            periodCollection.setText(cpp.getPeriod_collection());
            instruction.setText(cpp.getInstruction());
            System.out.println(">>> type: " + cpp.getType());
            typeListBox.setSelectedIndex(cpp.getType());
            typeListBox.setDisabled(true);
            usedtool.setText(cpp.getUsedTool());
            usedtool.setReadonly(true);
            storage.setText(cpp.getStorage());
            responsiblePreservation.setSelectedIndex(cpp.getResponsiblePreservation());
            toolVerification.setText(cpp.getToolVerification());
            nameCheckList.setText(cpp.getNameCheckList());
        }
        session.removeAttribute("update");
        session.removeAttribute("collectionProcedureProject");
    }

    public void onClick$save(Event event) throws InterruptedException {
        if (update == null) {
            insert();
        } else {
            session.setAttribute("update", true);
            update(cpp);
        }
    }

    public void onSelect$typeListBox(Event event) {
        if (update == null) {
            if (typeListBox.getSelectedIndex() == 1) {
                usedtool.setReadonly(true);
            } else {
                usedtool.setText("");
                usedtool.setReadonly(false);
            }
        } else {
            if (typeListBox.getSelectedIndex() == 1) {
                usedtool.setReadonly(true);
            } else {
                usedtool.setText(cpp.getUsedTool());
                usedtool.setReadonly(false);
            }
        }
    }

    public void onSelect$measureCombobox(Event event) {
        MeasureProject measure = (MeasureProject) measureCombobox.getSelectedItem().getValue();
        List<ApprovalProject> ap = ApprovalProjectDAO.getAllApprovalByMeasure(measure.getId());
        try {
            if (CollectionProcedureProjectDAO.searchCollectionDuplicate(measure.getName()) == 1) {
                Messagebox.show("O procedimento para esta medida já está registrado.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
                measureCombobox.setSelectedItem(null);
                version.setText("");
            } else if (ap.size() == 0) {
                Messagebox.show("Esta medida está definida, mas ainda não foi aprovada.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
                measureCombobox.setSelectedItem(null);
                version.setText("");
            } else if (ap.get(ap.size() - 1).getStatus().compareTo("0") == 0) {
                Messagebox.show("Esta medida está na condição de não aprovada.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
                measureCombobox.setSelectedItem(null);
                version.setText("");
            } else {
                version.setText(ap.get(ap.size() - 1).getVersion());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insert() throws UiException, InterruptedException {
        CollectionProcedureProject cp = new CollectionProcedureProject();
        MeasureProject measure = (MeasureProject) measureCombobox.getSelectedItem().getValue();
        if (frequency.getText().compareTo("") == 0) {
            Messagebox.show("Todos os campos devem ser preenchidos para a definição de uma medida.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        if (toolVerification.getText().compareTo("") == 0) {
            Messagebox.show("Todos os campos devem ser preenchidos para a definição de uma medida.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        if (nameCheckList.getText().compareTo("") == 0) {
            Messagebox.show("Todos os campos devem ser preenchidos para a definição de uma medida.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        if (storage.getText().compareTo("") == 0) {
            Messagebox.show("Todos os campos devem ser preenchidos para a definição de uma medida.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        if (nameCheckList.getText().compareTo("") == 0) {
            Messagebox.show("Todos os campos devem ser preenchidos para a definição de uma medida.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        if (instruction.getText().compareTo("") == 0) {
            Messagebox.show("Todos os campos devem ser preenchidos para a definição de uma medida.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        if (periodCollection.getText().compareTo("") == 0) {
            Messagebox.show("Todos os campos devem ser preenchidos para a definição de uma medida.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        if (responsible.getSelectedItem().getIndex() == 0) {
            Messagebox.show("Todos os campos devem ser preenchidos para a definição de uma medida.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        if (responsiblePreservation.getSelectedItem().getIndex() == 0) {
            Messagebox.show("Todos os campos devem ser preenchidos para a definição de uma medida.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        if (typeListBox.getSelectedItem().getIndex() == 0) {
            Messagebox.show("Todos os campos devem ser preenchidos para a definição de uma medida.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        cp.setFrequency(frequency.getText());
        cp.setNameCheckList(nameCheckList.getText());
        cp.setToolVerification(toolVerification.getText());
        cp.setStorage(storage.getText());
        cp.setProject(ProjectDAO.getProjectById(userProject.getId()));
        cp.setMeasureproject((MeasureProject) measureCombobox.getSelectedItem().getValue());
        cp.setInstruction(instruction.getText());
        cp.setUsedTool(usedtool.getText());
        cp.setMoment(moment.getText());
        cp.setVersion(version.getText());
        cp.setPeriod_collection(periodCollection.getText());
        cp.setResponsible(responsible.getSelectedItem().getIndex());
        cp.setType(typeListBox.getSelectedItem().getIndex());
        cp.setResponsiblePreservation(responsiblePreservation.getSelectedItem().getIndex());
        cp.setType(typeListBox.getSelectedItem().getIndex());
        try {
            CollectionProcedureProjectDAO.addCollectionProcedure(cp);
        } catch (EntityExistsException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Messagebox.show("Um erro ocorreu ao acessar o banco de dados", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return;
        }
        try {
            Messagebox.show("Procedimento de coleta definido com sucesso.", "Spider-MPlan", Messagebox.OK, Messagebox.INFORMATION);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onClick$cancel();
    }

    private void update(CollectionProcedureProject cp) throws UiException, InterruptedException {
        if (frequency.getText().compareTo("") == 0) {
            Messagebox.show("Todos os campos devem ser preenchidos para a definição de uma medida.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        if (toolVerification.getText().compareTo("") == 0) {
            Messagebox.show("Todos os campos devem ser preenchidos para a definição de uma medida.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        if (nameCheckList.getText().compareTo("") == 0) {
            Messagebox.show("Todos os campos devem ser preenchidos para a definição de uma medida.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        if (storage.getText().compareTo("") == 0) {
            Messagebox.show("Todos os campos devem ser preenchidos para a definição de uma medida.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        if (instruction.getText().compareTo("") == 0) {
            Messagebox.show("Todos os campos devem ser preenchidos para a definição de uma medida.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        if (periodCollection.getText().compareTo("") == 0) {
            Messagebox.show("Todos os campos devem ser preenchidos para a definição de uma medida.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        cp.setFrequency(frequency.getText());
        cp.setNameCheckList(nameCheckList.getText());
        cp.setToolVerification(toolVerification.getText());
        cp.setStorage(storage.getText());
        cp.setProject(ProjectDAO.getProjectById(userProject.getId()));
        cp.setMeasureproject((MeasureProject) measureCombobox.getSelectedItem().getValue());
        cp.setInstruction(instruction.getText());
        cp.setUsedTool(usedtool.getText());
        cp.setMoment(moment.getText());
        cp.setVersion(version.getText());
        cp.setPeriod_collection(periodCollection.getText());
        cp.setResponsible(responsible.getSelectedItem().getIndex());
        cp.setResponsiblePreservation(responsiblePreservation.getSelectedItem().getIndex());
        try {
            CollectionProcedureProjectDAO.updateCollectionProcedure(cp);
        } catch (EntityExistsException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Messagebox.show("Um erro ocorreu ao acessar o banco de dados", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return;
        }
        try {
            Messagebox.show("Procedimento de coleta alterado com sucesso.", "Spider-MPlan", Messagebox.OK, Messagebox.INFORMATION);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onClick$cancel();
    }

    public void onClick$cancel() {
        Component parent = window.getParent();
        window.detach();
        Window wdw = (Window) Executions.createComponents("collectionProcedureProject/collectionProcedureProject.zul", parent, null);
        mainDiv.setHeight("350px");
        window = wdw;
    }
}
