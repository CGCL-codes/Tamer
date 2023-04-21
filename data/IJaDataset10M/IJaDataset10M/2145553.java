package com.jeroensteenbeeke.hyperion.data;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Lolcat extends BaseDomainObject {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Lolcat parent;

    @OneToMany(mappedBy = "parent")
    private List<Lolcat> children;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lolcat getParent() {
        return parent;
    }

    public void setParent(Lolcat parent) {
        this.parent = parent;
    }

    public List<Lolcat> getChildren() {
        return children;
    }

    public void setChildren(List<Lolcat> children) {
        this.children = children;
    }
}
