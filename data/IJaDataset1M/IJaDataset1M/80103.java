package cz.expenses.storage;

import cz.expenses.commons.data.Id;
import cz.expenses.commons.data.Person;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "people")
public class PersonSave extends AbstractValueSave {

    @Element
    private String name;

    public PersonSave() {
        super();
    }

    public PersonSave(final String name) {
        super();
        this.name = name;
    }

    public PersonSave(final String id, final String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public PersonSave(final Person p) {
        this(p.getId().getText(), p.getName());
    }

    public Person getValue() {
        return new Person(new Id(getId()), getName());
    }
}
