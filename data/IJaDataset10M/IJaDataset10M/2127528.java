package free.order.security.model;

import java.util.HashSet;
import java.util.Set;

/**
 * @author cac
 */
public class Role {

    private Integer id;

    private String name;

    private String descn;

    private Set users = new HashSet(0);

    private Set rescs = new HashSet(0);

    private String authorize;

    /**
	 * default constructor
	 */
    public Role() {
    }

    /**
	 * minimal constructor
	 */
    public Role(String name) {
        this.name = name;
    }

    /**
	 * full constructor
	 */
    public Role(String name, String descn, Set users, Set rescs) {
        this.name = name;
        this.descn = descn;
        this.users = users;
        this.rescs = rescs;
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

    public String getDescn() {
        return this.descn;
    }

    public void setDescn(String descn) {
        this.descn = descn;
    }

    public Set getUsers() {
        return this.users;
    }

    public void setUsers(Set users) {
        this.users = users;
    }

    public Set getRescs() {
        return rescs;
    }

    public void setRescs(Set rescs) {
        this.rescs = rescs;
    }

    public String getAuthorize() {
        return authorize;
    }

    public void setAuthorize(String authorize) {
        this.authorize = authorize;
    }
}
