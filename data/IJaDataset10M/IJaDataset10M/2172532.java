package org.yourscrum.domain;

import javax.persistence.Entity;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

@Entity
@RooEntity
@RooJavaBean
@RooToString
public class Project {

    @NotNull
    @Size(min = 1, max = 50)
    private String title;

    @NotNull
    @Size(min = 1, max = 255)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Set<Theme> themes = new HashSet<Theme>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private Set<Team> teams = new HashSet<Team>();
}
