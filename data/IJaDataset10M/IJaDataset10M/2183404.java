package si.ibloc.cms.model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.Length;
import org.jboss.seam.annotations.Name;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@Entity
@Name("user")
@Indexed
public class User implements Serializable {

    private static final long serialVersionUID = -4259990899534978612L;

    @Id
    @GeneratedValue
    private Integer id;

    private String username;

    private String password;

    @ManyToMany
    private Set<Role> roles;

    private String name;

    @OneToMany(mappedBy = "author")
    private Set<Content> contents;

    @DocumentId
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Length(min = 4)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Length(min = 4)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Field(index = Index.TOKENIZED)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Content> getContents() {
        return contents;
    }

    public void setContents(Set<Content> contents) {
        this.contents = contents;
    }
}
