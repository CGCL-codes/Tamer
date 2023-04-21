package net.mystrobe.client;

import net.mystrobe.client.IDataBean;
import net.mystrobe.client.IDataSource;

/**
 * @author TVH Group NV
 */
public class TestPerson implements IDataBean {

    protected String countryCode = "us";

    protected IDataSource dataSource = null;

    protected String firstName = "";

    protected int id = -1;

    protected String language = "en";

    protected String lastName = "";

    public TestPerson() {
    }

    public TestPerson(int id, String firstName, String lastName, String language, String countryCode) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.language = language;
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDataSource(IDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public IDataSource getDataSource() {
        return this.dataSource;
    }

    public String getRowId() {
        return "" + getId();
    }

    public void detach() {
    }

    @Override
    public void setRowid(String rowId) {
    }
}
