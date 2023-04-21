package net.sf.bookright.entity;

import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;
import net.sf.bookright.entity.Customer;
import javax.validation.constraints.NotNull;
import javax.persistence.ManyToOne;
import net.sf.bookright.entity.Production;
import net.sf.bookright.entity.Performance;
import java.util.Set;
import net.sf.bookright.entity.ReservationPlace;
import java.util.HashSet;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@RooJavaBean
@RooToString
@RooEntity(finders = { "findReservationsByPerformance" })
public class Reservation {

    @NotNull
    @ManyToOne
    private Customer customer;

    @ManyToOne
    private transient Production production;

    @NotNull
    @ManyToOne
    private Performance performance;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reservation")
    private Set<ReservationPlace> reservationPlace = new HashSet<ReservationPlace>();

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    private Date reservedAtDate;
}
