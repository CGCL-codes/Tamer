package org.openymsg.addressBook;

public class YahooAddressBookEntry {

    public static final YahooAddressBookEntry EMPTY = new YahooAddressBookEntry(null, null, null, null, null);

    private String id;

    private String firstName;

    private String lastName;

    private String nickName;

    private String groupName;

    public YahooAddressBookEntry(String id, String firstName, String lastName, String nickName, String groupName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickName = nickName;
        this.groupName = groupName;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNickName() {
        return nickName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        YahooAddressBookEntry other = (YahooAddressBookEntry) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "YahooAddressBookEntry [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", nickName=" + nickName + "]";
    }

    public String getGroupName() {
        return groupName;
    }
}
