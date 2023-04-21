package eduburner.entity.user;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.security.GrantedAuthority;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import eduburner.entity.EntityObject;

@Entity
@Table(name = "role")
public class Role extends EntityObject implements GrantedAuthority {

    private static final long serialVersionUID = -908605749081541265L;

    private String name;

    private String description;

    protected Set<User> users = Sets.newHashSet();

    protected Set<PermissionBase> permissions = Sets.newHashSet();

    @Column(name = "name", nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    public Set<User> getUsers() {
        return users;
    }

    @Override
    @Transient
    public String getAuthority() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    public Set<PermissionBase> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionBase> permissions) {
        this.permissions = permissions;
    }

    @Transient
    public String getUsersString() {
        Iterable<String> iter = Iterables.transform(getUsers(), new Function<User, String>() {

            @Override
            public String apply(User from) {
                return from.getUsername();
            }
        });
        return Joiner.on(",").join(iter);
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Role)) {
            throw new IllegalArgumentException();
        }
        Role role = (Role) o;
        if (role.getName().equals(this)) {
            return 0;
        }
        return 1;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
