package br.ufpa.spider.mplan.logic.support;

import br.ufpa.spider.mplan.model.User;
import br.ufpa.spider.mplan.persistence.UserDAO;
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
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.ext.AfterCompose;

/**
 *
 * @author Kaio Valente
 */
public class UserController extends Window implements AfterCompose {

    private Window window;

    private Textbox keyWord;

    private Separator lastSeparator;

    private Listbox listbox;

    private Button newButton;

    private Button updateButton;

    private Button deleteButton;

    private Button cancelButton;

    private User currentUser;

    private Div contentDiv;

    public Session session;

    public void afterCompose() {
        Components.wireVariables(this, this);
        Components.addForwards(this, this);
        session = window.getDesktop().getSession();
        currentUser = (User) session.getAttribute("user");
        onLoad();
    }

    public class UpdateUserListener implements EventListener {

        public void onEvent(Event event) throws UiException {
            try {
                onClick$updateButton();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void onLoad() throws UiException {
        List<User> listOfUser = UserDAO.searchUser(keyWord.getText());
        Listhead listhead = listbox.getListhead();
        listbox.detach();
        listbox = new Listbox();
        listhead.setParent(listbox);
        listbox.setMultiple(false);
        listbox.setMold("paging");
        listbox.setPageSize(11);
        listbox.setWidth("830px");
        listbox.setHeight("100px");
        contentDiv.insertBefore(listbox, lastSeparator);
        Listitem listitem;
        Listcell listcell;
        for (User user : listOfUser) {
            listitem = new Listitem();
            listitem.addEventListener("onDoubleClick", new UpdateUserListener());
            listitem.setParent(listbox);
            listitem.setValue(user.getId());
            listcell = new Listcell(Long.toString(user.getId()));
            listcell.setParent(listitem);
            listcell = new Listcell(user.getName());
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
        User user = UserDAO.getUserById(id);
        session.setAttribute("regUser", user);
        redirectDefinition();
    }

    public void onClick$deleteButton() throws InterruptedException {
        Long id = (Long) listbox.getSelectedItem().getValue();
        User user = UserDAO.getUserById(id);
        delete(user);
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
        Component parent = window.getParent();
        window.detach();
        Window wdw = (Window) Executions.createComponents("users/usersdefinition.zul", parent, null);
        window = wdw;
    }

    public void delete(User user) throws InterruptedException {
        int pressedButton = 0;
        long id_exclnot = (Long) listbox.getSelectedItem().getValue();
        System.out.println(">>> currentUser id: " + currentUser.getId() + " id_exclnot: " + id_exclnot);
        if (currentUser.getId() == id_exclnot) {
            Messagebox.show("Este usuário está logado e não pode ser deletado", "Spider-MPlan", Messagebox.OK, Messagebox.EXCLAMATION);
            onLoad();
        } else {
            try {
                pressedButton = Messagebox.show("Deseja excluir o usuário?", "Spider-MPlan", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (pressedButton == Messagebox.YES) {
                try {
                    UserDAO.deleteUser(user);
                    onLoad();
                } catch (Exception ex) {
                    Messagebox.show("Não é possível deletar este usuário. Ele está atrelado a outras atividades do processo de medição.", "Spider-MPlan", Messagebox.OK, Messagebox.ERROR);
                    ex.printStackTrace();
                }
            }
        }
    }
}
