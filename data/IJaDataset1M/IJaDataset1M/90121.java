package com.schinzer.fin.basic;

public abstract class Bank {

    private long id;

    private String name;

    private String iban;

    public abstract String getBankCode();

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((iban == null) ? 0 : iban.hashCode());
        result = PRIME * result + (int) (id ^ (id >>> 32));
        result = PRIME * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Bank other = (Bank) obj;
        if (iban == null) {
            if (other.iban != null) return false;
        } else if (!iban.equals(other.iban)) return false;
        if (id != other.id) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }
}
