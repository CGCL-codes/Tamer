package br.ufpa.spider.mplan.logic.support;

import br.ufpa.spider.mplan.model.Organization;
import br.ufpa.spider.mplan.model.AlocationProject;
import br.ufpa.spider.mplan.model.Profile;
import br.ufpa.spider.mplan.model.Project;
import br.ufpa.spider.mplan.model.User;
import br.ufpa.spider.mplan.persistence.AlocationProjectDAO;
import br.ufpa.spider.mplan.persistence.OrganizationDAO;
import br.ufpa.spider.mplan.persistence.ProjectDAO;
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
public class DefinitionAlocationProjectController extends Window implements AfterCompose {

    private Window window;

    private Textbox name;

    private Textbox userName;

    private Textbox password;

    private Combobox organizationCombobox;

    private Combobox projectCombobox;

    private Combobox userCombobox;

    private Listbox profileSearchListBox;

    private Listbox inSearchListBox;

    private User currentUser;

    private Div mainDiv;

    public Session session;

    private Boolean update = false;

    private AlocationProject alocationProject = null;

    private List<Organization> organization = null;

    private List<Project> projects = null;

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
            alocationProject = (AlocationProject) session.getAttribute("alocationProject");
            Comboitem comboitem = organizationCombobox.appendItem(alocationProject.getUser().getOrganization().getId() + "-" + alocationProject.getUser().getOrganization().getName());
            organizationCombobox.setSelectedItem(comboitem);
            organizationCombobox.setDisabled(true);
            Comboitem comboitemproject = projectCombobox.appendItem(alocationProject.getProject().getId() + "-" + alocationProject.getProject().getName());
            projectCombobox.setSelectedItem(comboitemproject);
            projectCombobox.setDisabled(true);
            Comboitem comboitemuser = userCombobox.appendItem(alocationProject.getUser().getId() + "-" + alocationProject.getUser().getName());
            userCombobox.setSelectedItem(comboitemuser);
            userCombobox.setDisabled(true);
            Listitem listitem;
            Listcell listcell;
            for (Profile profile : alocationProject.getListOfProfiles()) {
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
        for (Profile prorg : AlocationProjectDAO.getAllProfiles()) {
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
        projectCombobox.setSelectedItem(null);
        projectCombobox.getItems().clear();
        List<Project> projects = new ArrayList<Project>();
        projects = ProjectDAO.getProjectByOrganization(org);
        for (int i = 0; i < projects.size(); ++i) {
            Comboitem item = new Comboitem();
            item = projectCombobox.appendItem(projects.get(i).getId() + " - " + projects.get(i).getName());
            item.setValue(projects.get(i));
        }
        userCombobox.setSelectedItem(null);
        userCombobox.getItems().clear();
        List lisfOfEntitiesUser = null;
        List<User> users = new ArrayList<User>();
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("spider_mplan");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        lisfOfEntitiesUser = em.createQuery("select a from user a where a.organization = " + a).getResultList();
        em.getTransaction().commit();
        em.close();
        emf.close();
        for (int i = 0; i < lisfOfEntitiesUser.size(); ++i) {
            users.add((User) lisfOfEntitiesUser.get(i));
        }
        for (int i = 0; i < users.size(); ++i) {
            Comboitem item = new Comboitem();
            item = userCombobox.appendItem(users.get(i).getId() + " - " + users.get(i).getName());
            item.setValue(users.get(i));
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
            update(alocationProject);
        }
    }

    private void insert() throws InterruptedException {
        if (organizationCombobox.getSelectedItem() == null) {
            Messagebox.show("Uma organização deve ser selecionada", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        if (projectCombobox.getSelectedItem() == null) {
            Messagebox.show("Um projeto deve ser selecionado", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        if (userCombobox.getSelectedItem() == null) {
            Messagebox.show("Um usuário deve ser selecionado", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        AlocationProject aloc = new AlocationProject();
        List<Profile> p = new ArrayList<Profile>();
        Profile pro;
        for (Object object : profileSearchListBox.getItems()) {
            pro = (Profile) ((Listitem) object).getValue();
            p.add((Profile) pro);
        }
        Project proaloc = (Project) projectCombobox.getSelectedItem().getValue();
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
        aloc.setProject(proaloc);
        if (AlocationProjectDAO.searchAlocationProjectDuplicate(aloc.getUser().getId(), aloc.getProject().getId()).size() > 0) {
            Messagebox.show("Este usuário já está registrado para este projeto!", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        int pressedButton = 0;
        try {
            pressedButton = Messagebox.show("O usuario e o projeto nao poderao ser editados em outro momento. Deseja Continuar?", "Spider-MPlan", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (pressedButton == Messagebox.NO) {
            return;
        }
        try {
            AlocationProjectDAO.createUser(aloc);
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

    private void update(AlocationProject alocationProject) throws UiException, InterruptedException {
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
        alocationProject.setListOfProfiles(p);
        try {
            AlocationProjectDAO.updateAlocation(alocationProject);
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
        Window wdw = (Window) Executions.createComponents("alocationProject/alocationProject.zul", parent, null);
        mainDiv.setHeight("350px");
        window = wdw;
    }
}
