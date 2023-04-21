package br.ufpa.spider.mplan.logic.project;

import br.ufpa.spider.mplan.model.ApprovalProject;
import br.ufpa.spider.mplan.model.MeasureProject;
import br.ufpa.spider.mplan.model.Organization;
import br.ufpa.spider.mplan.model.Project;
import br.ufpa.spider.mplan.model.User;
import br.ufpa.spider.mplan.persistence.ApprovalProjectDAO;
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
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Combobox;

/**
 *
 * @author Kaio Valente
 */
public class ApprovalProjectViewController extends Window implements AfterCompose {

    private Window window;

    private Textbox keyWord;

    private Separator lastSeparator;

    private Listbox listbox;

    private Button cancelButton;

    private User currentUser;

    private Organization userOrganization;

    private Project userProject;

    private Div mainDiv;

    private Combobox organizationCombobox;

    public Session session;

    private MeasureProject measureProject;

    public void afterCompose() {
        Components.wireVariables(this, this);
        Components.addForwards(this, this);
        session = window.getDesktop().getSession();
        currentUser = (User) session.getAttribute("user");
        userOrganization = (Organization) session.getAttribute("userOrganization");
        userProject = (Project) session.getAttribute("userProject");
        onLoad();
    }

    public class ViewListener implements EventListener {

        public void onEvent(Event event) throws UiException {
            try {
                onClick$viewButton();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void onLoad() throws UiException {
        measureProject = (MeasureProject) session.getAttribute("measureProject");
        List<ApprovalProject> listOfApproval = ApprovalProjectDAO.searchHistory(keyWord.getText(), measureProject.getId());
        Listitem listitem;
        Listcell listcell;
        for (ApprovalProject hm : listOfApproval) {
            listitem = new Listitem();
            listitem.addEventListener("onDoubleClick", new ViewListener());
            listitem.setDraggable("true");
            listitem.setDroppable("true");
            listitem.setParent(listbox);
            listitem.setValue(hm.getId());
            listcell = new Listcell(Long.toString(hm.getId()));
            listcell.setParent(listitem);
            listcell = new Listcell("" + hm.getDate_definition());
            listcell.setParent(listitem);
            listcell = new Listcell(hm.getAuthor());
            listcell.setParent(listitem);
            listcell = new Listcell(hm.getVersion());
            listcell.setParent(listitem);
            if (hm.getStatus().trim().equals("1")) {
                listcell = new Listcell("Aprovado");
                listcell.setParent(listitem);
            } else if (hm.getStatus().trim().equals("0")) {
                listcell = new Listcell("Não Aprovado");
                listcell.setParent(listitem);
            }
            listcell = new Listcell(hm.getNote());
            listcell.setParent(listitem);
        }
    }

    public void onClick$searchButton() {
        onLoad();
    }

    public void onClick$cancelButton() throws InterruptedException {
        session.removeAttribute("measureProject");
        redirectDefinition();
    }

    public void redirectDefinition() throws InterruptedException {
        Div parent = (Div) window.getParent();
        window.detach();
        Window wdw = (Window) Executions.createComponents("measureProject/measureProject.zul", parent, null);
        mainDiv.setHeight("950px");
        window = wdw;
    }

    public void onClick$viewButton() throws InterruptedException {
        Long id = (Long) listbox.getSelectedItem().getValue();
        ApprovalProject app = ApprovalProjectDAO.getApprovalById(id);
        session.setAttribute("approvalProject", app);
        Component parent = window.getParent();
        window.detach();
        Window wdw = (Window) Executions.createComponents("measureProject/approvalProjectInformation.zul", parent, null);
        mainDiv.setHeight("350px");
        window = wdw;
    }
}
