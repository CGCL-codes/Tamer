package edu.ba.library.management.user;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Customer {

    protected int id;

    protected String name;

    protected Date birthdate;

    protected String email;

    protected String address;

    protected Set<Process> processes = new HashSet<Process>();

    protected Set<CustomerCard> cards = new HashSet<CustomerCard>();

    protected User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Process> getProcesses() {
        return processes;
    }

    public void setProcesses(Set<Process> processes) {
        this.processes = processes;
    }

    public Set<CustomerCard> getCards() {
        return cards;
    }

    public void setCards(Set<CustomerCard> cards) {
        this.cards = cards;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean hasValidCard() {
        for (CustomerCard cc : getCards()) {
            if (cc.getStatus() != CustomerCardStatus.Lost && cc.getValidUntil() == null) {
                return true;
            }
        }
        return false;
    }
}
