package Person.newpackage;

/**
 *
 * @author m10frr
 */
public class Person {

    String name;

    public Person(String name) {
        this.name = name;
    }

    public String nameYourself() {
        return name;
    }

    public static void inspect(Person p) {
        p = new Person("mr. X");
    }

    public static void main(String[] args) {
        Person person = new Person("Bill");
        Person.inspect(person);
        int MAX_VALUE = 100;
        for (int i = 0; i < MAX_VALUE; i++) ;
        System.out.println(person.nameYourself());
    }
}
