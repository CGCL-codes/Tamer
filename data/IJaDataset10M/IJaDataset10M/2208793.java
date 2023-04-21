package nl.joppla.ejb.entity.document;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import nl.joppla.ejb.entity.schedule.Schedule;

@Entity
@Table(name = "RUNS")
@SequenceGenerator(name = "RunSequenceGenerator", sequenceName = "DOCUMENT.RUNS_SEQ", allocationSize = 1)
@NamedQuery(name = "Run.findAll", query = "select o from Run o")
public class Run implements Serializable {

    /**
	 * Default serial version id for serializable
	 */
    private static final long serialVersionUID = 1401036334107203433L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RunSequenceGenerator")
    @Column(updatable = false)
    private Long Id;

    @Column(name = "RUN_ID", nullable = false, length = 100)
    private String runId;

    @Column(name = "RUN_DATE", nullable = false)
    private Timestamp runDate;

    @Column(name = "RUN_STATUS", nullable = false, length = 1)
    private String status;

    @ManyToOne
    @JoinColumn(name = "SCHEDULE_ID", referencedColumnName = "ID")
    private Schedule schedule;

    @OneToMany(mappedBy = "run")
    private List<GeneratedFile> files;

    public Run() {
    }

    public Long getId() {
        return Id;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunDate(Timestamp runDate) {
        this.runDate = runDate;
    }

    public Timestamp getRunDate() {
        return runDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setFiles(List<GeneratedFile> files) {
        this.files = files;
    }

    public List<GeneratedFile> getFiles() {
        return files;
    }

    public void setId(Long id) {
        this.Id = id;
    }
}
