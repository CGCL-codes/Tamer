package br.ufpa.spider.mplan.logic.support;

import br.ufpa.spider.mplan.model.Organization;
import br.ufpa.spider.mplan.model.Alocation;
import br.ufpa.spider.mplan.model.Profile;
import br.ufpa.spider.mplan.model.User;
import br.ufpa.spider.mplan.persistence.AlocationDAO;
import br.ufpa.spider.mplan.persistence.OrganizationDAO;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.UiException;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.ext.AfterCompose;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author Kaio Valente
 */
public class DefinitionAlocationController extends Window implements AfterCompose {

    private Window window;

    private Textbox name;

    private Textbox userName;

    private Textbox password;

    private Combobox organizationCombobox;

    private Combobox userCombobox;

    private Listbox profileSearchListBox;

    private Listbox inSearchListBox;

    private User currentUser;

    private Div mainDiv;

    public Session session;

    private Boolean update = false;

    private Alocation alocation = null;

    private List<Organization> organization = null;

    private static final String SUCCESS_INSERT = "Alocação realizada com sucesso!";

    private static final String SUCCESS_UPDATE = "Registro atualizado com sucesso!";

    private static final String ERROR_BD = "Houve um erro ao acessar o banco de dados!";

    private static final String TITLE = "Spider Mplan";

    public void afterCompose() {
        Components.wireVariables(this, this);
        Components.addForwards(this, this);
        profileSearchListBox.addEventListener("onDrop", new ListboxDropListener());
        session = window.getDesktop().getSession();
        currentUser = (User) session.getAttribute("user");
        update = (Boolean) session.getAttribute("update");
        onLoad();
    }

    public void onLoad() {
        if (update == null) {
            organization = OrganizationDAO.getAll();
            organizationCombobox.getItems().clear();
            for (int i = 0; i < organization.size(); i++) {
                Comboitem item = new Comboitem();
                item = organizationCombobox.appendItem(organization.get(i).getId() + " - " + organization.get(i).getName());
                item.setValue(organization.get(i));
            }
        } else {
            alocation = (Alocation) session.getAttribute("alocation");
            Comboitem comboitem = organizationCombobox.appendItem(alocation.getUser().getOrganization().getId() + "-" + alocation.getUser().getOrganization().getName());
            organizationCombobox.setSelectedItem(comboitem);
            organizationCombobox.setDisabled(true);
            Comboitem comboitemuser = userCombobox.appendItem(alocation.getUser().getId() + "-" + alocation.getUser().getName());
            userCombobox.setSelectedItem(comboitemuser);
            userCombobox.setDisabled(true);
            Listitem listitem;
            Listcell listcell;
            for (Profile profile : alocation.getListOfProfiles()) {
                listitem = new Listitem();
                listitem.setDraggable("true");
                listitem.setDroppable("true");
                listitem.setParent(profileSearchListBox);
                listitem.setValue(profile);
                listcell = new Listcell(Long.toString(profile.getId()));
                listcell.setParent(listitem);
                listcell = new Listcell(profile.getName());
                listcell.setParent(listitem);
                listitem.setCtrlKeys("#del");
                listitem.addEventListener("onCtrlKey", new DeleteListitemListener());
            }
        }
        Listitem listitem;
        Listcell listcell;
        for (Profile prorg : AlocationDAO.getAllProfiles()) {
            listitem = new Listitem();
            listitem.setDraggable("true");
            listitem.setDroppable("true");
            listitem.setParent(inSearchListBox);
            listitem.setValue(prorg);
            listcell = new Listcell(Long.toString(prorg.getId()));
            listcell.setParent(listitem);
            listcell = new Listcell(prorg.getName());
            listcell.setParent(listitem);
        }
        session.removeAttribute("update");
        session.removeAttribute("alocation");
    }

    public class ListitemDropListener implements EventListener {

        public void onEvent(Event event) throws Exception {
            DropEvent dropEvent = (DropEvent) event;
            Listitem dragged = (Listitem) dropEvent.getDragged();
            Listitem target = (Listitem) dropEvent.getTarget();
            Listitem listitem;
            Profile user0, user1;
            user0 = (Profile) dragged.getValue();
            for (Object object : profileSearchListBox.getItems()) {
                listitem = (Listitem) object;
                user1 = (Profile) listitem.getValue();
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
            Profile user0, user1;
            user0 = (Profile) listitem0.getValue();
            for (Object object : profileSearchListBox.getItems()) {
                user1 = (Profile) ((Listitem) object).getValue();
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

    public void onSelect$organizationCombobox(Event event) {
        Organization org = (Organization) organizationCombobox.getSelectedItem().getValue();
        int a = (int) org.getId();
        userCombobox.setSelectedItem(null);
        userCombobox.getItems().clear();
        List lisfOfEntities = null;
        List<User> project = new ArrayList<User>();
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("spider_mplan");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        lisfOfEntities = em.createQuery("select a from user a where a.organization = " + a).getResultList();
        em.getTransaction().commit();
        em.close();
        emf.close();
        for (int i = 0; i < lisfOfEntities.size(); ++i) {
            project.add((User) lisfOfEntities.get(i));
        }
        for (int i = 0; i < project.size(); ++i) {
            Comboitem item = new Comboitem();
            item = userCombobox.appendItem(project.get(i).getId() + " - " + project.get(i).getName());
            item.setValue(project.get(i));
        }
    }

    public class DeleteListitemListener implements EventListener {

        public void onEvent(Event event) throws UiException {
            event.getTarget().detach();
        }
    }

    public void onClick$save(Event event) throws InterruptedException {
        if (update == null) {
            insert();
        } else {
            update(alocation);
        }
    }

    private void insert() throws InterruptedException {
        if (organizationCombobox.getSelectedItem() == null) {
            Messagebox.show("Uma organização deve ser selecionada", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        if (userCombobox.getSelectedItem() == null) {
            Messagebox.show("Um usuário deve ser selecionado", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        Alocation aloc = new Alocation();
        List<Profile> p = new ArrayList<Profile>();
        Profile pro;
        for (Object object : profileSearchListBox.getItems()) {
            pro = (Profile) ((Listitem) object).getValue();
            p.add((Profile) pro);
        }
        if (p.size() == 0) {
            try {
                Messagebox.show("Ao menos um perfil deve ser alocado.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        aloc.setListOfProfiles(p);
        aloc.setUser((User) userCombobox.getSelectedItem().getValue());
        if (AlocationDAO.searchAlocationDuplicate(aloc.getUser().getName()) == 1) {
            Messagebox.show("Este usuário já está registrado", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        int pressedButton = 0;
        try {
            pressedButton = Messagebox.show("O usuario e a organizacao nao poderao ser editados em outro momento. Deseja Continuar?", "Spider-MPlan", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (pressedButton == Messagebox.NO) {
            return;
        }
        try {
            AlocationDAO.createAlocation(aloc);
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
            Messagebox.show("Alocação realizada com sucesso.", "Spider-MPlan", Messagebox.OK, Messagebox.INFORMATION);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        redirect();
    }

    private void update(Alocation alocation) throws UiException, InterruptedException {
        List<Profile> p = new ArrayList<Profile>();
        Profile pro;
        for (Object object : profileSearchListBox.getItems()) {
            pro = (Profile) ((Listitem) object).getValue();
            p.add((Profile) pro);
        }
        if (p.size() == 0) {
            try {
                Messagebox.show("Uma perfil deve ser definido.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        alocation.setListOfProfiles(p);
        try {
            AlocationDAO.updateAlocation(alocation);
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
        redirect();
    }

    public void redirect() {
        onClick$cancel();
    }

    public void onClick$cancel() {
        Component parent = window.getParent();
        window.detach();
        Window wdw = (Window) Executions.createComponents("alocation/alocation.zul", parent, null);
        mainDiv.setHeight("350px");
        window = wdw;
    }
}
