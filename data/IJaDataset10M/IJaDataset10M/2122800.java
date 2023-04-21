package example.action;

import java.util.List;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.Preparable;
import example.service.dao.PersonService;
import example.service.dto.Person;

/**
 * Action que se encarga de gestionar las personas
 * 
 * La interfaz preparable hace que struts llame al m�todo prepare en caso de que
 * PrepareInterceptor se aplique a este action (por defecto se aplica).
 * @author icasado
 * 
 */
public class PersonAction implements Preparable {

    private PersonService service;

    private List<Person> persons;

    private Person person;

    private Integer id;

    /**
     * Constructor
     * Spring inyecta el PersonService autom�ticamente al instanciar la clase
     * @param service
     */
    public PersonAction(PersonService service) {
        this.service = service;
    }

    /**
     * Rellena la lista de personas 
     * @return String
     */
    public String execute() {
        this.persons = this.service.findAll();
        return Action.SUCCESS;
    }

    /**
     * Obtiene la persona indicada por id
     * @throws Exception 
     */
    public void prepare() throws Exception {
        if (this.id != null) this.person = this.service.find(this.id.intValue());
    }

    /**
     * Guarda una persona
     * @return String
     */
    public String save() {
        this.service.save(this.person);
        this.person = new Person();
        return execute();
    }

    /**
     * Elimina una persona
     * @return String
     */
    public String remove() {
        this.service.remove(this.id.intValue());
        return execute();
    }

    /**
     * Devuelve la lista de personas
     * @return List<Person>
     */
    public List<Person> getPersons() {
        return this.persons;
    }

    /**
     * Devuelve el id de una persona
     * @return Integer
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Establece el id de una persona
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Devuelve una persona
     * @return Person
     */
    public Person getPerson() {
        return this.person;
    }

    /**
     * Establece una persona
     * @param person
     */
    public void setPerson(Person person) {
        this.person = person;
    }
}
