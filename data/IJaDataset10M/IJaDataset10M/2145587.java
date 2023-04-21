package org.apache.struts2.showcase.person;

import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.views.freemarker.FreemarkerResult;
import com.opensymphony.xwork2.ActionSupport;

/**
 */
@ParentPackage("person")
public class NewPersonAction extends ActionSupport {

    private static final long serialVersionUID = 200410824352645515L;

    PersonManager personManager;

    Person person;

    public void setPersonManager(PersonManager personManager) {
        this.personManager = personManager;
    }

    public String execute() {
        personManager.createPerson(person);
        return SUCCESS;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
