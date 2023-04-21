package ModelLayer;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

/**
 * This class models a person to be used in an adressbook
 * 
 * @author (Claus Thomsen)
 * 
 */
public class Person {

    private String id;

    private String name;

    private String address;

    private String postalCode;

    private String city;

    private String tlf;

    private HashMap<String, Loan> loans;

    public Person(String id, String name, String address, String postalCode, String city, String tlf) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;
        this.tlf = tlf;
        this.loans = new HashMap<String, Loan>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getTlf() {
        return tlf;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setTlf(String tlf) {
        this.tlf = tlf;
    }

    public void addLoan(Loan loan) {
        loans.put(loan.getID(), loan);
    }

    public Loan getLoan(String id) {
        return loans.get(id);
    }

    public ArrayList<Loan> getLoans() {
        ArrayList<Loan> list = new ArrayList<Loan>();
        for (Map.Entry<String, Loan> loan : loans.entrySet()) {
            list.add(loan.getValue());
        }
        return list;
    }
}
