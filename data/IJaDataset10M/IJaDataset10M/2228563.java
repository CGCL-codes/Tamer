package test.model;

/**
 * 测试模型类
 * 
 * @author Sun
 * @version Person.java 2010-7-24 上午11:20:47
 */
public class Person {

    private String firstName;

    private String lastName;

    private PhoneNumber phone;

    private PhoneNumber fax;

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * @return the phone
     */
    public PhoneNumber getPhone() {
        return this.phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(PhoneNumber phone) {
        this.phone = phone;
    }

    /**
     * @return the fax
     */
    public PhoneNumber getFax() {
        return this.fax;
    }

    /**
     * @param fax the fax to set
     */
    public void setFax(PhoneNumber fax) {
        this.fax = fax;
    }
}
