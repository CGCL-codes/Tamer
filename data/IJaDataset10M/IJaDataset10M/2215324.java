package br.com.visualmidia.persistence.add;

import java.util.ArrayList;
import java.util.List;
import br.com.visualmidia.business.Group;
import br.com.visualmidia.business.Person;
import br.com.visualmidia.business.User;
import br.com.visualmidia.exception.BusinessException;
import br.com.visualmidia.persistence.GDTransaction;
import br.com.visualmidia.persistence.PrevalentSystem;

public class AddGroupToUser extends GDTransaction {

    private static final long serialVersionUID = 4153427412444504426L;

    private String nameGroup;

    private List<String> idUsers;

    public AddGroupToUser(String nameGroup, List<Person> users) {
        this.nameGroup = nameGroup;
        idUsers = new ArrayList<String>();
        for (Person user : users) {
            idUsers.add(user.getId());
        }
    }

    protected void execute(PrevalentSystem system) throws BusinessException {
        Group group = system.getGroups().get(nameGroup);
        for (String idUser : this.idUsers) {
            Person person = system.getPeople().get(idUser);
            User user = (User) person.getPersonType("user");
            if (groupAlreadyExistInUser(group, user)) return;
            user.getGroups().add(group);
        }
    }

    private boolean groupAlreadyExistInUser(Group group, User user) {
        List<Group> groups = user.getGroups();
        for (Group groupOfUser : groups) {
            if (groupOfUser.getNameGroup().equals(group.getNameGroup())) {
                return true;
            }
        }
        return false;
    }
}
