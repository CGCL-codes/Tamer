package br.com.visualmidia.business;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representa todas as pessoas do sistema como funcion�rio, usu�rio e estudante (Emplyee, User e Student), 
 * cont�m informa��es que s�o comuns a todos eles.
 * 
 * @author  Lucas
 * 
 * Alterado e documentado
 * @author	Marcelo Costa Curta
 * @since	10/10/2007
 * @version	1.0
 */
public class Person implements Serializable, Comparable, ITableCompletationItem {

    private String id;

    private String name;

    private String street;

    private String streetNumber;

    private String streetComplement;

    private String neighborhood;

    private String cep;

    private String city;

    private String state;

    private String cpf;

    private String rg;

    private String cellPhone;

    private String phone;

    private String messagePhone;

    private String contact;

    private String birthDate;

    private String gender;

    private String email;

    private String nationality;

    private String civilState;

    private Map<String, Object> personType = new HashMap<String, Object>();

    public static final long serialVersionUID = 9871234918237100L;

    public Person(String id, String name, String street, String streetNumber, String streetComplement, String neighborhood, String cep, String city, String state, String cpf, String rg, String cellPhone, String phone, String messagePhone, String contact, String birthDate, String gender, String email, String nationality, String civilState) {
        this.id = id;
        this.name = name;
        this.street = street;
        this.streetNumber = streetNumber;
        this.streetComplement = streetComplement;
        this.neighborhood = neighborhood;
        this.cep = cep;
        this.city = city;
        this.state = state;
        this.cpf = cpf;
        this.rg = rg;
        this.cellPhone = cellPhone;
        this.phone = phone;
        this.messagePhone = messagePhone;
        this.contact = contact;
        this.birthDate = birthDate;
        this.gender = gender;
        this.email = email;
        this.nationality = nationality;
        this.civilState = civilState;
    }

    public Person(String id, String name) {
        this(id, name, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessagePhone() {
        return messagePhone;
    }

    public void setMessagePhone(String messagePhone) {
        this.messagePhone = messagePhone;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetComplement() {
        return streetComplement;
    }

    public void setStreetComplement(String streetComplement) {
        this.streetComplement = streetComplement;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public Object getPersonType(String key) {
        return personType.get(key);
    }

    public boolean isPersonType(String personType) {
        return this.personType.containsKey(personType);
    }

    public boolean isPersonType(List<String> filters) {
        int cont = 0;
        for (int i = 0; i < filters.size(); i++) if (isPersonType(filters.get(i))) {
            cont++;
        }
        return cont == filters.size();
    }

    public void setPersonType(String key, Object value) {
        personType.put(key, value);
    }

    public String getCivilState() {
        return civilState;
    }

    public void setCivilState(String civilState) {
        this.civilState = civilState;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void removePersonType(String key) {
        personType.remove(key);
    }

    public String getCompletationName() {
        return getName();
    }

    public int compareTo(Object arg0) {
        Person person = null;
        if (arg0 instanceof Person) {
            person = (Person) arg0;
            return getName().compareTo(person.getName());
        }
        return -1;
    }

    public String getCompletationId() {
        return getId();
    }
}
