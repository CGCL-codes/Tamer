package br.com.visualmidia.persistence;

import br.com.visualmidia.business.Person;
import br.com.visualmidia.business.User;
import br.com.visualmidia.exception.BusinessException;

public class RemoveGroupToUser extends GDTransaction {

    private static final long serialVersionUID = 3559876297969959766L;

    private String personName;

    private String groupName;

    public RemoveGroupToUser(String personName, String groupName) {
        this.personName = personName;
        this.groupName = groupName;
    }

    @Override
    protected void execute(PrevalentSystem system) throws BusinessException {
        for (Person person : system.people.values()) {
            if (person.getName().equals(personName)) {
                User usuario = (User) person.getPersonType("user");
                usuario.removeGroup(groupName);
            }
        }
    }
}
