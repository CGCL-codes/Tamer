package cn.sduo.app.vo.security;

import java.util.Collection;
import java.util.LinkedHashSet;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

public class RoleDTO implements java.io.Serializable {

    private Long id;

    private String name;

    private Collection<AuthorityDTO> auths = new LinkedHashSet<AuthorityDTO>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotEmpty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<AuthorityDTO> getAuths() {
        return auths;
    }

    public void setAuths(Collection<AuthorityDTO> auths) {
        this.auths = auths;
    }
}
