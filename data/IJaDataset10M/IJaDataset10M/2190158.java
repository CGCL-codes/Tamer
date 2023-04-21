package pl.wcislo.sbql4j.javac.test.advanced.model;

public class Employee {

    private String name;

    private Address address;

    private Double salary;

    private String job;

    private Department worksIn;

    public Employee(String name, Address address, Double salary, String job, Department worksIn) {
        this.name = name;
        this.address = address;
        this.salary = salary;
        this.job = job;
        this.worksIn = worksIn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Department getWorksIn() {
        return worksIn;
    }

    public void setWorksIn(Department worksIn) {
        this.worksIn = worksIn;
    }
}
