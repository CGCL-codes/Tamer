package br.ufpa.spider.mplan.logic.support;

import br.ufpa.spider.mplan.model.Alocation;
import br.ufpa.spider.mplan.model.AlocationProject;
import br.ufpa.spider.mplan.model.Feature;
import br.ufpa.spider.mplan.model.Profile;
import br.ufpa.spider.mplan.model.User;
import br.ufpa.spider.mplan.persistence.AlocationDAO;
import br.ufpa.spider.mplan.persistence.AlocationProjectDAO;
import br.ufpa.spider.mplan.persistence.GenericDAO;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityExistsException;
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
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

/**
 *
 * @author Kaio Valente
 */
public class HumanResourceController extends Window implements AfterCompose {

    private Window window;

    private Textbox keyWord;

    private Separator lastSeparator;

    private Listbox featureSearchListBox;

    private Listbox selectedfeatureListBox;

    private Button newButton;

    private Button updateButton;

    private Button deleteButton;

    private Button cancelButton;

    private User currentUser;

    private Div contentDiv;

    private Div mainDiv;

    public Session session;

    private List<Profile> listOfProfile;

    private Combobox profileCombobox;

    private List<Feature> featureAll = new ArrayList<Feature>();

    private Profile profile;

    public void afterCompose() {
        Components.wireVariables(this, this);
        Components.addForwards(this, this);
        session = window.getDesktop().getSession();
        currentUser = (User) session.getAttribute("user");
        selectedfeatureListBox.addEventListener("onDrop", new ListboxDropListener());
        onLoad();
    }

    public void onLoad() throws UiException {
        listOfProfile = GenericDAO.getAll("profile");
        profileCombobox.getItems().clear();
        for (int i = 0; i < listOfProfile.size(); i++) {
            Comboitem item = new Comboitem();
            item = profileCombobox.appendItem(listOfProfile.get(i).getId() + " - " + listOfProfile.get(i).getName());
            item.setValue(listOfProfile.get(i));
        }
    }

    public void onSelect$profileCombobox(Event event) {
        profile = (Profile) profileCombobox.getSelectedItem().getValue();
        featureAll = GenericDAO.getAll("features");
        featureSearchListBox.getItems().clear();
        Listitem listitem;
        Listcell listcell;
        for (Feature feature : featureAll) {
            listitem = new Listitem();
            listitem.setDraggable("true");
            listitem.setDroppable("true");
            listitem.setParent(featureSearchListBox);
            listitem.setValue(feature);
            listcell = new Listcell(Long.toString(feature.getId()));
            listcell.setParent(listitem);
            listcell = new Listcell(feature.getName());
            listcell.setParent(listitem);
        }
        selectedfeatureListBox.getItems().clear();
        Listitem listitem1;
        Listcell listcell1;
        for (Feature feature1 : profile.getListOfFeatures()) {
            listitem1 = new Listitem();
            listitem1.setDraggable("true");
            listitem1.setDroppable("true");
            listitem1.setCtrlKeys("#del");
            listitem1.addEventListener("onCtrlKey", new DeleteListitemListener());
            listitem1.addEventListener("onDrop", new ListitemDropListener());
            listitem1.setValue(feature1);
            listitem1.setParent(selectedfeatureListBox);
            listcell1 = new Listcell();
            listcell1.setLabel(Long.toString(feature1.getId()));
            listcell1.setParent(listitem1);
            listcell1 = new Listcell();
            listcell1.setLabel(feature1.getName());
            listcell1.setParent(listitem1);
        }
    }

    public class ListitemDropListener implements EventListener {

        public void onEvent(Event event) throws Exception {
            DropEvent dropEvent = (DropEvent) event;
            Listitem dragged = (Listitem) dropEvent.getDragged();
            Listitem target = (Listitem) dropEvent.getTarget();
            Listitem listitem;
            Feature user0, user1;
            user0 = (Feature) dragged.getValue();
            for (Object object : selectedfeatureListBox.getItems()) {
                listitem = (Listitem) object;
                user1 = (Feature) listitem.getValue();
                if (user0.getId() == user1.getId()) {
                    return;
                }
            }
            dragged.setDraggable("true");
            dragged.setDroppable("true");
            dragged.setCtrlKeys("#del");
            dragged.addEventListener("onCtrlKey", new DeleteListitemListener());
            dragged.addEventListener("onDrop", new ListitemDropListener());
            if (dragged.getParent() != target.getParent()) {
                target.getParent().insertBefore((Component) dragged.clone(), target);
            } else {
                target.getParent().insertBefore(dragged, target);
            }
        }
    }

    public class ListboxDropListener implements EventListener {

        public void onEvent(Event event) throws Exception {
            DropEvent dropEvent = (DropEvent) event;
            Listitem listitem0 = (Listitem) dropEvent.getDragged();
            if (listitem0.getParent() == dropEvent.getTarget()) {
                dropEvent.getTarget().appendChild(listitem0);
                return;
            }
            Feature user0, user1;
            user0 = (Feature) listitem0.getValue();
            for (Object object : selectedfeatureListBox.getItems()) {
                user1 = (Feature) ((Listitem) object).getValue();
                if (user0.getId() == user1.getId()) {
                    return;
                }
            }
            Listitem listitem1 = (Listitem) listitem0.clone();
            listitem1.setParent(dropEvent.getTarget());
            listitem1.setDraggable("true");
            listitem1.setDroppable("true");
            listitem1.setCtrlKeys("#del");
            listitem1.addEventListener("onCtrlKey", new DeleteListitemListener());
            listitem1.addEventListener("onDrop", new ListitemDropListener());
        }
    }

    public class DeleteListitemListener implements EventListener {

        public void onEvent(Event event) throws UiException {
            event.getTarget().detach();
        }
    }

    public void onClick$newButton() throws InterruptedException {
        List<Feature> p = new ArrayList<Feature>();
        Feature pro;
        for (Object object : selectedfeatureListBox.getItems()) {
            pro = (Feature) ((Listitem) object).getValue();
            p.add((Feature) pro);
        }
        if (p.size() == 0) {
            try {
                Messagebox.show("Uma funcionalidade deve ser definida.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        profile.setListOfFeatures(p);
        try {
            AlocationDAO.updateHumanResources(profile);
        } catch (EntityExistsException e) {
            e.printStackTrace();
            return;
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
            Messagebox.show("Alocação atualizada com sucesso.", "Spider-MPlan", Messagebox.OK, Messagebox.INFORMATION);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onLoad();
    }

    public void onClick$cancelButton() {
        Component parent = window.getParent();
        window.detach();
        Window wdw = (Window) Executions.createComponents("main.zul", parent, null);
        mainDiv.setHeight("350px");
        window = wdw;
    }
}
