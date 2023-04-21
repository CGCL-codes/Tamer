package edu.java.lectures.lec15.comparator;

public class Person implements Comparable<Person> {

    private String fisrtName = null;

    private String secondName = null;

    private String thirdName = null;

    private int age = 0;

    private Gender gender = Gender.MALE;

    private String city = null;

    private int kids = 0;

    public Person() {
        this(null, null, null, 0, Gender.MALE, null, 0);
    }

    public Person(String firstName, String secondName, String thirdName, int age, Gender gender, String city, int kids) {
        this.fisrtName = firstName;
        this.secondName = secondName;
        this.thirdName = thirdName;
        this.age = age;
        this.gender = gender;
        this.city = city;
        this.kids = kids;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Person)) {
            throw new IllegalArgumentException("Incorrect type of the input object.");
        }
        Person person = (Person) obj;
        return (this.getFisrtName().equals(person.getFisrtName())) && (this.getSecondName().equals(person.getSecondName())) && (this.getThirdName().equals(person.getThirdName())) && (this.getGender().equals(person.getGender())) && (this.getCity().equals(person.getCity())) && (this.getAge() == person.getAge()) && (this.getKids() == person.getKids());
    }

    @Override
    public String toString() {
        String BLANK_SPACE = " ";
        String COMMA = ", ";
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        sb.append(this.getFisrtName());
        sb.append(BLANK_SPACE);
        sb.append(this.getSecondName());
        sb.append(BLANK_SPACE);
        sb.append(this.getThirdName());
        sb.append(COMMA);
        sb.append(this.getAge());
        sb.append(BLANK_SPACE);
        sb.append("year(s)");
        sb.append(COMMA);
        sb.append(getGender() != null ? getGender().toString().toLowerCase() : null);
        sb.append(COMMA);
        sb.append(this.getCity());
        sb.append(COMMA);
        sb.append(this.getKids());
        sb.append("kid(s) ]");
        return sb.toString();
    }

    @Override
    public int compareTo(Person person) {
        if (person == null) {
            System.out.println(this.toString());
            throw new IllegalArgumentException("Person object is null pointed.");
        }
        int compareFirstName = this.getFisrtName().compareTo(person.getFisrtName());
        if (compareFirstName != 0) {
            return compareFirstName;
        }
        int compareSecondName = this.getSecondName().compareTo(person.getSecondName());
        if (compareSecondName != 0) {
            return compareSecondName;
        }
        int compareThirdName = this.getThirdName().compareTo(person.getThirdName());
        if (compareThirdName != 0) {
            return compareThirdName;
        }
        int compareAge = this.getAge() - person.getAge();
        if (compareAge != 0) {
            return compareAge;
        }
        int compareGender = this.getGender().compareTo(person.getGender());
        if (compareGender != 0) {
            return compareGender;
        }
        int compareCity = this.getCity().compareTo(person.getCity());
        if (compareCity != 0) {
            return compareCity;
        }
        int compareKids = this.getKids() - person.getKids();
        return compareKids;
    }

    public String getFisrtName() {
        return fisrtName;
    }

    public void setFisrtName(String fisrtName) {
        if (fisrtName == null) {
            throw new IllegalArgumentException("FirstName is null-pointed string");
        }
        this.fisrtName = fisrtName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        if (secondName == null) {
            throw new IllegalArgumentException("SecondName is null-pointed string");
        }
        this.secondName = secondName;
    }

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        if (thirdName == null) {
            throw new IllegalArgumentException("ThirdName is null-pointed string");
        }
        this.thirdName = thirdName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age is negative. It should be positive number");
        }
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        if (gender == null) {
            throw new IllegalArgumentException("Gender is null-pointed object." + "Gender can have only male and femay as value.");
        }
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if (city == null) {
            throw new IllegalArgumentException("City is null-pointed string.");
        }
        this.city = city;
    }

    public int getKids() {
        return kids;
    }

    public void setKids(int kids) {
        if (kids < 0) {
            throw new IllegalArgumentException("Age is negative. It should be positive number");
        }
        this.kids = kids;
    }
}
