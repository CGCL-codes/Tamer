package com.ken.dgking.model;

import com.ken.dgking.common.ValidationManager;

public abstract class Person {

    private String id;

    private String name;

    private String zip;

    private String address;

    private String telephone;

    private String fax;

    private String connectionPerson;

    private String phone;

    private String email;

    private String bank;

    private String account;

    private int available;

    public Person() {
        super();
    }

    public Person(String id, String name, String zip, String address, String telephone, String connectionPerson, String phone, String bank, String account, String email, String fax) {
        super();
        this.id = id;
        this.name = name;
        this.zip = zip;
        this.address = address;
        this.telephone = telephone;
        this.fax = fax;
        this.connectionPerson = connectionPerson;
        System.out.println("Person���캯��connectionPerson->" + connectionPerson);
        System.out.println("Person���캯��phone->" + phone);
        this.phone = phone;
        this.email = email;
        this.bank = bank;
        this.account = account;
    }

    public Person(String id, String name, String zip, String address, String telephone, String fax, String connectionPerson, String phone, String email, String bank, String account, int available) {
        super();
        this.id = id;
        this.name = name;
        this.zip = zip;
        this.address = address;
        this.telephone = telephone;
        this.fax = fax;
        this.connectionPerson = connectionPerson;
        this.phone = phone;
        this.email = email;
        this.bank = bank;
        this.account = account;
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getConnectionPerson() {
        return connectionPerson;
    }

    public void setConnectionPerson(String connectionPerson) {
        this.connectionPerson = connectionPerson;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getAvailable() {
        return available;
    }

    /**
	 * ���ؼ�����
	 * @param columnNumber �к�
	 * @return ��Ӧ�е�ֵ
	 */
    public Object getPersonValue(int columnNumber) {
        switch(columnNumber) {
            case 0:
                return ValidationManager.changeNull(getId());
            case 1:
                return ValidationManager.changeNull(getName());
            case 2:
                return ValidationManager.changeNull(getZip());
            case 3:
                return ValidationManager.changeNull(getAddress());
            case 4:
                return ValidationManager.changeNull(getTelephone());
            case 5:
                return ValidationManager.changeNull(getConnectionPerson());
            case 6:
                return ValidationManager.changeNull(getPhone());
            case 7:
                return ValidationManager.changeNull(getBank());
            case 8:
                return ValidationManager.changeNull(getAccount());
            case 9:
                return ValidationManager.changeNull(getEmail());
            case 10:
                return ValidationManager.changeNull(getFax());
            default:
                return "";
        }
    }
}
