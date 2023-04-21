package net.sf.dozer.util.mapping.vo.cumulative;

/**
 * @author Dmitry Buzdin
 */
public class AuthorPrime {

    private Long id;

    private String name;

    private Long salary;

    public AuthorPrime() {
    }

    public AuthorPrime(Long id, String name, Long salary) {
        super();
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }
}
