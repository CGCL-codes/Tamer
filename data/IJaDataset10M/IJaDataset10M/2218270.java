package br.ufpa.spider.mplan.logic.organization;

import br.ufpa.spider.mplan.logic.support.*;
import br.ufpa.spider.mplan.model.InformationNecessity;
import br.ufpa.spider.mplan.model.MeasurementGoal;
import br.ufpa.spider.mplan.model.Organization;
import br.ufpa.spider.mplan.model.Project;
import br.ufpa.spider.mplan.model.User;
import br.ufpa.spider.mplan.persistence.InformationNecessityDAO;
import br.ufpa.spider.mplan.persistence.MeasurementGoalDAO;
import br.ufpa.spider.mplan.persistence.OrganizationDAO;
import br.ufpa.spider.mplan.persistence.ProjectDAO;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Combobox;

/**
 *
 * @author Kaio Valente
 */
public class MeasurementGoalController extends Window implements AfterCompose {

    private Window window;

    private Textbox keyWord;

    private Separator lastSeparator;

    private Listbox listbox;

    private Button newButton;

    private Button updateButton;

    private Button deleteButton;

    private Button cancelButton;

    private User currentUser;

    private Organization userOrganization;

    private Project userProject;

    private Div mainDiv;

    private Combobox organizationCombobox;

    public Session session;

    public void afterCompose() {
        Components.wireVariables(this, this);
        Components.addForwards(this, this);
        session = window.getDesktop().getSession();
        currentUser = (User) session.getAttribute("user");
        userOrganization = (Organization) session.getAttribute("userOrganization");
        userProject = (Project) session.getAttribute("userProject");
        onLoad();
    }

    public class UpdateInformationNecessityListener implements EventListener {

        public void onEvent(Event event) throws UiException {
            try {
                onClick$updateButton();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void onLoad() throws UiException {
        listbox.getItems().clear();
        List<MeasurementGoal> listOfOrganization = MeasurementGoalDAO.searchMeasurementGoal(keyWord.getText(), userOrganization.getId());
        Listitem listitem;
        Listcell listcell;
        for (MeasurementGoal in : listOfOrganization) {
            listitem = new Listitem();
            listitem.addEventListener("onDoubleClick", new UpdateInformationNecessityListener());
            listitem.setParent(listbox);
            listitem.setValue(in.getId());
            listcell = new Listcell(Long.toString(in.getId()));
            listcell.setParent(listitem);
            listcell = new Listcell(in.getGoal());
            listcell.setParent(listitem);
        }
        if (listbox.getItemCount() == 0) {
            updateButton.setDisabled(true);
            deleteButton.setDisabled(true);
        } else {
            updateButton.setDisabled(false);
            deleteButton.setDisabled(false);
            listbox.setSelectedIndex(0);
        }
    }

    public void onClick$newButton() throws InterruptedException {
        redirectDefinition();
    }

    public void onClick$updateButton() throws InterruptedException {
        session.setAttribute("update", true);
        Long id = (Long) listbox.getSelectedItem().getValue();
        MeasurementGoal measurementGoal = MeasurementGoalDAO.getMeasurementGoalById(id);
        session.setAttribute("measurementGoal", measurementGoal);
        redirectDefinition();
    }

    public void onClick$deleteButton() throws InterruptedException {
        Long id = (Long) listbox.getSelectedItem().getValue();
        MeasurementGoal measurementGoal = MeasurementGoalDAO.getMeasurementGoalById(id);
        delete(measurementGoal);
    }

    public void onClick$searchButton() {
        onLoad();
    }

    public void onClick$cancelButton() {
        Component parent = window.getParent();
        window.detach();
        Window wdw = (Window) Executions.createComponents("main.zul", parent, null);
        window = wdw;
    }

    public void redirectDefinition() throws InterruptedException {
        Div parent = (Div) window.getParent();
        window.detach();
        Window wdw = (Window) Executions.createComponents("measurementGoal/measurementGoaldefinition.zul", parent, null);
        mainDiv.setHeight("990px");
        window = wdw;
    }

    public void delete(MeasurementGoal measurementGoal) throws UiException, InterruptedException {
        int pressedButton = 0;
        try {
            pressedButton = Messagebox.show("Deseja excluir o objetivo de medição?", "Spider-MPlan", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (pressedButton == Messagebox.YES) {
            try {
                MeasurementGoalDAO.deleteMeasurementGoal(measurementGoal);
                onLoad();
            } catch (Exception ex) {
                Messagebox.show("Não é possível deletar esta questão. Ele está atrelado a outras atividades do processo de medição.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
                ex.printStackTrace();
            }
        }
    }
}
