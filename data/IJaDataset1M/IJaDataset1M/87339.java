package nl.hajari.wha.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.tostring.RooToString;

/**
 * 
 * @author <a href="mailto:saeid3@gmail.com">Saeid Moradi</a>
 * 
 */
@Entity
@RooJavaBean
@RooToString
@RooEntity(finders = { "findBizLogsByTimesheet" })
public class BizLog {

    @Column(name = "log_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @Column(name = "details", nullable = false)
    private String details;

    @Column(name = "username", nullable = true)
    private String username;

    @ManyToOne(targetEntity = User.class, optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @ManyToOne(targetEntity = Employee.class, optional = true)
    @JoinColumn(name = "employee_id", nullable = true)
    private Employee employee;

    @ManyToOne(targetEntity = Timesheet.class, optional = true)
    @JoinColumn(name = "timesheet_id", nullable = true)
    private Timesheet timesheet;
}
