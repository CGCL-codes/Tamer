package ca.petersens.gwt.databinding.example.client;

import static java.util.Collections.sort;
import static java.util.Collections.unmodifiableList;
import ca.petersens.gwt.databinding.client.SourcesPropertyChangeEvents;
import ca.petersens.gwt.databinding.example.client.beans.Person;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public final class PersonModel implements SourcesPropertyChangeEvents {

    private final class NameChangeListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent event) {
            sortPersonList();
        }
    }

    private static final class PersonComparator implements Comparator<Person> {

        public static final Comparator<Person> INSTANCE = new PersonComparator();

        private PersonComparator() {
        }

        public int compare(Person o1, Person o2) {
            int ret = o1.getLastName().compareToIgnoreCase(o2.getLastName());
            if (ret != 0) {
                return ret;
            }
            return o1.getFirstName().compareToIgnoreCase(o2.getFirstName());
        }
    }

    private static PersonModel instance;

    public static PersonModel getInstance() {
        if (instance == null) {
            instance = new PersonModel();
        }
        return instance;
    }

    private Person currentPerson;

    private PropertyChangeListener nameChangeListener = new NameChangeListener();

    private final List<Person> personList;

    private PropertyChangeSupport support;

    @SuppressWarnings("deprecation")
    private PersonModel() {
        personList = new ArrayList<Person>();
        Person ian = new Person("Ian", "Petersen");
        ian.setBirthDate(new Date(81, 4, 25));
        personList.add(ian);
    }

    public void addPerson(Person person) {
        personList.add(person);
        person.addPropertyChangeListener("firstName", nameChangeListener);
        person.addPropertyChangeListener("lastName", nameChangeListener);
        setCurrentPerson(person);
        sortPersonList();
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        if (support == null) {
            support = new PropertyChangeSupport(this);
        }
        support.addPropertyChangeListener(property, listener);
    }

    public Person getCurrentPerson() {
        return currentPerson;
    }

    public List<Person> getPersonList() {
        return unmodifiableList(personList);
    }

    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        if (support == null) {
            return;
        }
        support.removePropertyChangeListener(property, listener);
    }

    public void setCurrentPerson(Person currentPerson) {
        Person oldCurrentPerson = this.currentPerson;
        this.currentPerson = currentPerson;
        firePropertyChange("currentPerson", oldCurrentPerson, this.currentPerson);
    }

    private <T> void firePropertyChange(String property, T oldValue, T newValue) {
        if (support == null) {
            return;
        }
        support.firePropertyChange(property, oldValue, newValue);
    }

    private void sortPersonList() {
        sort(personList, PersonComparator.INSTANCE);
        firePropertyChange("personList", null, getPersonList());
    }
}
