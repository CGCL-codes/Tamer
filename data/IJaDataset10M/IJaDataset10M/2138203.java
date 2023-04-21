package tudresden.ocl20.benchmark.testdata.b1;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import tudresden.ocl20.pivot.pivotmodel.impl.PrimitiveTypeImpl;

public class Person {

    public String name;

    public String civstat;

    public String gender;

    public boolean alive;

    public Person husband;

    public Person wife;

    private static List<Person> persons = new ArrayList<Person>();

    /**
	 * Instantiates a new person.
	 * 
	 * @param name 
	 * @param civstat 
	 * @param gender 
	 * @param alive 
	 */
    public Person(String name, String civstat, String gender, boolean alive) {
        this();
        this.name = name;
        this.civstat = civstat;
        this.gender = gender;
        this.alive = alive;
    }

    /**
	 * Instantiates a new person.
	 */
    public Person() {
        persons.add(this);
    }

    /**
	 * Spouse.
	 * 
	 * @return the person
	 */
    public Person spouse() {
        if (this.gender.equals("female")) {
            return this.husband;
        } else {
            return this.wife;
        }
    }

    /**
	 * Marry.
	 * 
	 * @param p 
	 */
    public void marry(Person p) {
        this.civstat = "married";
        p.civstat = "married";
        assert (!p.gender.equals(this.gender));
        this.setSpouse(p);
        p.setSpouse(this);
    }

    /**
	 * Divorce.
	 * 
	 * @param p 
	 */
    public void divorce(Person p) {
        assert (this.civstat.equals("married"));
        assert (p.civstat.equals("married"));
        assert (p.name.equals(this.spouse().name));
        this.civstat = "divorced";
        p.civstat = "divorced";
        this.husband = null;
        this.wife = null;
        p.wife = null;
        p.husband = null;
    }

    /**
	 * Sets the spouse.
	 * 
	 * @param p the new spouse
	 */
    public void setSpouse(Person p) {
        if (this.gender == "female") {
            assert (p == null || p.gender == "male");
            this.husband = p;
        } else {
            assert (p == null || p.gender == "female");
            this.wife = p;
        }
    }

    /**
	 * Removes the spouse.
	 */
    public void removeSpouse() {
        this.setSpouse(null);
    }

    /**
	 * Die.
	 */
    public void die() {
        this.spouse().removeSpouse();
        this.removeSpouse();
        this.alive = false;
    }

    /**
	 * Implemented by OCL-Aspect.
	 * 
	 * @param aName 
	 * 
	 * @return 
	 */
    public static Person getByName(PrimitiveTypeImpl aName) {
        for (Person p : persons) {
            if (p.name == aName.toString()) {
                return p;
            }
        }
        return null;
    }

    public String toString() {
        return "Person: " + this.name;
    }

    /**
 * Prints the.
 */
    public void print() {
        try {
            String buf = "";
            Field[] fields = this.getClass().getDeclaredFields();
            buf += this.getClass().getName() + "(\n";
            for (Field field : fields) {
                buf += "\t" + field.getName() + ": " + field.get(this) + "\n";
            }
            buf += ")";
            System.out.println(buf);
        } catch (IllegalAccessException e) {
            System.out.println("cannot print due to protected visiblity issues");
        }
    }
}
