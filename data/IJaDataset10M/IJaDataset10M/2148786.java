package org.limaloa.test;

/**
 * A simple JavaBean, used by <code>AdapterFactoryTest</code> and
 * <code>AdapterBeanDefinitionParserTest</code>.
 * 
 * @author Chris Nappin
 */
public class SourceBean {

    private String firstName;

    private String lastName;

    private int age;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /**
	 * Not essential but nice for debugging.
	 * @return The bean, as a string
	 */
    public String toString() {
        return new StringBuffer(this.getClass().getName()).append("{ firstName: ").append(firstName).append(", lastName: ").append(lastName).append(", age: ").append(age).append(" }").toString();
    }
}
