package siena.base.test;

import siena.Column;
import siena.Generator;
import siena.Id;
import siena.Max;
import siena.Table;

@Table("people")
public class Person {

    @Id(Generator.UUID)
    @Max(36)
    public String id;

    @Column("first_name")
    @Max(100)
    public String firstName;

    @Column("last_name")
    @Max(100)
    public String lastName;

    @Max(100)
    public String city;

    public int n;

    public Person() {
    }

    public Person(String firstName, String lastName, String city, int n) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.n = n;
    }

    public String toString() {
        return "id: " + id + ", firstName: " + firstName + ", lastName: " + lastName + ", city: " + city;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((city == null) ? 0 : city.hashCode());
        result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + n;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Person other = (Person) obj;
        if (city == null) {
            if (other.city != null) return false;
        } else if (!city.equals(other.city)) return false;
        if (firstName == null) {
            if (other.firstName != null) return false;
        } else if (!firstName.equals(other.firstName)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (lastName == null) {
            if (other.lastName != null) return false;
        } else if (!lastName.equals(other.lastName)) return false;
        if (n != other.n) return false;
        return true;
    }
}
