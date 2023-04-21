package net.sf.valjax;

/**
 * Created by IntelliJ IDEA.
 * User: Zach
 * Date: Mar 1, 2007
 * Time: 9:50:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class ValidatorForm {

    private int number;

    private String name;

    private String date;

    private String email;

    private String phone;

    private String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
