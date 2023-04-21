package net.sf.bookright.entity;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;
import net.sf.bookright.entity.Performance;
import java.util.HashSet;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import net.sf.bookright.entity.TicketCategory;

@RooJavaBean
@RooToString
@RooEntity
public class Production {

    @NotNull
    @Size(max = 10)
    private String code;

    @NotNull
    @Size(max = 50)
    private String name;

    @Size(max = 200)
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "production", fetch = FetchType.EAGER)
    private Set<Performance> performance = new HashSet<Performance>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "production")
    private Set<TicketCategory> ticketCategory = new HashSet<TicketCategory>();
}
