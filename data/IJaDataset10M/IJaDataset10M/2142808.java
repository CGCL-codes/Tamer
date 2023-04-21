package br.com.manish.ahy.menu;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import br.com.manish.ahy.kernel.BaseEntity;

@Entity
public class Menu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String path;

    private String status;

    private Integer position;

    @ManyToOne
    private Menu superMenu;

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Menu)) {
            return false;
        }
        Menu castOther = (Menu) other;
        return new EqualsBuilder().append(superMenu, castOther.superMenu).append(name, castOther.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(superMenu).append(name).toHashCode();
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Menu getSuperMenu() {
        return superMenu;
    }

    public void setSuperMenu(Menu superMenu) {
        this.superMenu = superMenu;
    }
}
