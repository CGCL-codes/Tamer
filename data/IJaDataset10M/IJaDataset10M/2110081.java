package dormatory;

import java.util.ArrayList;
import java.util.List;

public class DormitoryRepositoryInMemoryImpl implements DormitoryRepository {

    public List<Dormitory> dormitoryList = new ArrayList<Dormitory>();

    public DormitoryRepositoryInMemoryImpl() {
        Person bob = new Person("Bob Stevens", Gender.MALE);
        Person mary = new Person("Mary Johnson", Gender.FEMALE);
        Person larry = new Person("Larry Jones", Gender.MALE);
        Person steve = new Person("Steveie Wonder", Gender.MALE);
        Person luke = new Person("Luke Skywalker", Gender.MALE);
        Person darth = new Person("Darth Vadar", Gender.MALE);
        Person wendell = new Person("Wendell Blankenship", Gender.MALE);
        Person Jessie = new Person("Jessie Hartman", Gender.MALE);
        Person Tom = new Person("Tom Severn", Gender.MALE);
        Person Rick = new Person("Rick Wagner", Gender.MALE);
        Person Joe = new Person("Joe Thorpe", Gender.MALE);
        Person Kim = new Person("Kim Jones", Gender.FEMALE);
        Person Katie = new Person("Katie Smith", Gender.FEMALE);
        Room room1 = new Room("101", 6);
        Room room2 = new Room("102", 8);
        Room room3 = new Room("103", 4);
        Room room4 = new Room("104", 4);
        Room room5 = new Room("105", 2);
        Room room6 = new Room("106", 4);
        Dormitory dorm1 = new Dormitory("Captains Deck");
        dormitoryList.add(dorm1);
        dorm1.addRoom(room1);
        dorm1.addRoom(room2);
        dorm1.addRoom(room3);
        dorm1.addRoom(room4);
        dorm1.addRoom(room5);
        dorm1.addRoom(room6);
        room1.addPerson(Katie);
        room1.addPerson(Kim);
        room1.addPerson(Joe);
        room1.addPerson(Rick);
        room1.addPerson(Tom);
        room1.addPerson(Jessie);
        room1.removePerson(Katie);
        room2.addPerson(bob);
        room2.addPerson(mary);
        room2.addPerson(Katie);
        room3.addPerson(larry);
        room3.addPerson(steve);
        room4.addPerson(luke);
        room4.addPerson(darth);
    }

    public Dormitory getDorm(String name) {
        for (Dormitory dorm : dormitoryList) {
            if (dorm.getName().equals(name)) {
                return dorm;
            }
        }
        return null;
    }

    public List<Dormitory> getDormList() {
        return dormitoryList;
    }

    public void save(Dormitory dorm) {
        dormitoryList.add(dorm);
    }
}
