package accountcustomer;

import java.util.*;

class Customer {

    private String name;

    private String address;

    private List<Account> accounts;

    /**
   * @param n navnet p� kunden
   * @param a adressen p� kunden
   */
    public Customer(String n, String a) {
        name = n;
        address = a;
        accounts = new ArrayList<Account>();
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String a) {
        address = a;
    }

    /**
   * tilf�jer en konto til kunden
   * @param a kontoen, som skal tilf�jes
   */
    public void add(Account a) {
        accounts.add(a);
    }

    /**
   * fjerner en konto til kunden
   * @param a kontoen, som skal fjernes
   */
    public void remove(Account a) {
        accounts.remove(a);
    }

    public String toString() {
        return name + " " + address;
    }
}
