package ebiNeutrinoSDK.model.hibernate;

/**
 * Crmproblemsoltype generated by hbm2java
 */
public class Crmproblemsoltype implements java.io.Serializable {

    private Integer id;

    private String name;

    public Crmproblemsoltype() {
    }

    public Crmproblemsoltype(String name) {
        this.name = name;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
