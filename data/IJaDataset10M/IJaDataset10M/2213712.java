package com.markatta.hund.wicket.pages;

import com.google.inject.Inject;
import com.markatta.hund.model.User;
import com.markatta.hund.service.Database;
import com.markatta.hund.service.UserService;
import com.markatta.hund.wicket.SecurePage;
import com.markatta.hund.wicket.model.AllUsersModel;
import com.markatta.hund.wicket.components.OddEvenListItem;
import java.util.List;
import javax.persistence.EntityManager;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.Model;

/**
 * Page for manipulating application users
 * 
 * @author johan
 */
public class Users extends SecurePage {

    @Inject
    private UserService userService;

    @Inject
    private Database database;

    public class UsersForm extends Form {

        private AllUsersModel listModel = new AllUsersModel(userService);

        private Button submit;

        public UsersForm(String componentName) {
            super(componentName);
            ListView settingsView = new PropertyListView("usersView", listModel) {

                @Override
                protected ListItem newItem(int index) {
                    return new OddEvenListItem(index, getListItemModel(getModel(), index));
                }

                @Override
                protected void populateItem(ListItem item) {
                    item.add(new HiddenField("id"));
                    item.add(new TextField("name"));
                    item.add(new TextField("emailAddress"));
                    User user = (User) item.getModelObject();
                    item.add(new Link("deleteUserLink", new Model(user.getId())) {

                        @Override
                        public void onClick() {
                            deleteUser((Long) getModelObject());
                        }
                    });
                }
            };
            add(settingsView);
            Link addUserLink = new Link("addUserLink") {

                @Override
                public void onClick() {
                    createNewUser();
                    submit.setEnabled(true);
                }
            };
            add(addUserLink);
            submit = new Button("submit");
            add(submit);
        }

        @Override
        protected void onSubmit() {
            EntityManager em = database.createEntityManager();
            em.getTransaction().begin();
            List<User> users = (List<User>) listModel.getObject();
            for (User user : users) {
                if (user.getId() == 0) {
                    em.persist(user);
                } else {
                    em.merge(user);
                }
            }
            em.getTransaction().commit();
        }
    }

    public Users() {
        add(new UsersForm("usersForm"));
    }

    private void createNewUser() {
        userService.createNewUser();
    }

    private void deleteUser(long id) {
        userService.deleteUser(id);
    }
}
