package org.gaea.demo.Employee;

/**
 * 
 * @author bbahi
 */
public class Person {

    protected String name;

    protected Phone numbr;

    public Person() {
        this.name = "";
        this.numbr = null;
    }

    public Person(String name) {
        this();
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(Phone number) {
        this.numbr = number;
    }

    public String getName() {
        return this.name;
    }

    public Phone getPhone() {
        return this.numbr;
    }

    public void showPerson() {
        System.out.println("\tPerson: " + this.getName());
        if (this.numbr != null) this.numbr.showPhone();
    }

    /**
     * @return Returns the number.
     */
    public Phone getNumber() {
        return numbr;
    }

    /**
     * @param number The number to set.
     */
    public void setNumber(Phone number) {
        this.numbr = number;
    }
}
