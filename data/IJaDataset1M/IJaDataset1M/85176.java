package bean;

/**
 * UserSnapDest entity.
 * 
 * @author MyEclipse Persistence Tools
 */
public class UserSnapDest implements java.io.Serializable {

    private UserSnapDestId id;

    /** default constructor */
    public UserSnapDest() {
    }

    /** full constructor */
    public UserSnapDest(UserSnapDestId id) {
        this.id = id;
    }

    public UserSnapDestId getId() {
        return this.id;
    }

    public void setId(UserSnapDestId id) {
        this.id = id;
    }
}
