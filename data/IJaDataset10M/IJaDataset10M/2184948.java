package tk.solaapps.ohtune.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "jobs")
public class Job {

    public static final String STATUS_PROCESSING = "进行中";

    public static final String STATUS_APPROVING = "审核中";

    public static final String STATUS_DONE = "完成";

    public static final String STATUS_PAUSED = "暂停";

    public static final String STATUS_CANCELED = "取消";

    public static final String COLUMN_JOBTYPE = "job_type";

    public static final String COLUMN_STATUS = "status";

    private Long id;

    private Order orders;

    private UserAC userac;

    private JobType job_type;

    private Integer total;

    private Integer finished;

    private String status;

    private Date complete_date;

    private Date start_date;

    private Date deadline;

    private String finish_remark;

    private Integer remaining;

    private UserAC assigned_to;

    private Integer total_rejected;

    private Long previous_jobid;

    @Id
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToOne
    @JoinColumn(name = "orders")
    public Order getOrders() {
        return orders;
    }

    public void setOrders(Order orders) {
        this.orders = orders;
    }

    @OneToOne
    @JoinColumn(name = "user")
    public UserAC getUserac() {
        return userac;
    }

    public void setUserac(UserAC userac) {
        this.userac = userac;
    }

    @OneToOne
    @JoinColumn(name = "job_type")
    public JobType getJob_type() {
        return job_type;
    }

    public void setJob_type(JobType job_type) {
        this.job_type = job_type;
    }

    @Column(name = "total")
    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Column(name = "finished")
    public Integer getFinished() {
        return finished;
    }

    public void setFinished(Integer finished) {
        this.finished = finished;
    }

    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "complete_date")
    public Date getComplete_date() {
        return complete_date;
    }

    public void setComplete_date(Date complete_date) {
        this.complete_date = complete_date;
    }

    @Column(name = "start_date")
    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    @Column(name = "deadline")
    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    @Column(name = "finish_remark")
    public String getFinish_remark() {
        return finish_remark;
    }

    public void setFinish_remark(String finish_remark) {
        this.finish_remark = finish_remark;
    }

    @Column(name = "remaining")
    public Integer getRemaining() {
        return remaining;
    }

    public void setRemaining(Integer remaining) {
        this.remaining = remaining;
    }

    @OneToOne
    @JoinColumn(name = "assigned_to")
    public UserAC getAssigned_to() {
        return assigned_to;
    }

    public void setAssigned_to(UserAC assigned_to) {
        this.assigned_to = assigned_to;
    }

    @Column(name = "total_rejected")
    public Integer getTotal_rejected() {
        return total_rejected;
    }

    public void setTotal_rejected(Integer total_rejected) {
        this.total_rejected = total_rejected;
    }

    @Column(name = "previous_jobid")
    public Long getPrevious_jobid() {
        return previous_jobid;
    }

    public void setPrevious_jobid(Long previous_jobid) {
        this.previous_jobid = previous_jobid;
    }
}
