package beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Ioana C
 */
@Entity
@Table(name = "Employee")
@NamedQueries({ @NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e"), @NamedQuery(name = "Employee.findByPersonID", query = "SELECT e FROM Employee e WHERE e.personID = :personID"), @NamedQuery(name = "Employee.findByFirstName", query = "SELECT e FROM Employee e WHERE e.firstName = :firstName"), @NamedQuery(name = "Employee.findByLastName", query = "SELECT e FROM Employee e WHERE e.lastName = :lastName"), @NamedQuery(name = "Employee.findByEmail", query = "SELECT e FROM Employee e WHERE e.email = :email"), @NamedQuery(name = "Employee.findBySex", query = "SELECT e FROM Employee e WHERE e.sex = :sex"), @NamedQuery(name = "Employee.findByStartDate", query = "SELECT e FROM Employee e WHERE e.startDate = :startDate"), @NamedQuery(name = "Employee.findByEndDate", query = "SELECT e FROM Employee e WHERE e.endDate = :endDate"), @NamedQuery(name = "Employee.findByIsRecrutor", query = "SELECT e FROM Employee e WHERE e.isRecrutor = :isRecrutor"), @NamedQuery(name = "Employee.findByIsResigned", query = "SELECT e FROM Employee e WHERE e.isResigned = :isResigned") })
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "PersonID")
    private Integer personID;

    @Basic(optional = false)
    @Column(name = "FirstName")
    private String firstName;

    @Basic(optional = false)
    @Column(name = "LastName")
    private String lastName;

    @Column(name = "Email")
    private String email;

    @Basic(optional = false)
    @Column(name = "Sex")
    private char sex;

    @Basic(optional = false)
    @Column(name = "StartDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "EndDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Basic(optional = false)
    @Column(name = "isRecrutor")
    private boolean isRecrutor;

    @Basic(optional = false)
    @Column(name = "isResigned")
    private boolean isResigned;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private List<RecTeam> recTeamCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personID")
    private List<EmpFile> empFileCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personID")
    private List<EmpComment> empCommentCollection;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "employee")
    private EmpConfidential empConfidential;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personID")
    private List<JobMove> jobMoveCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personID")
    private List<EmpSalaryHistory> empSalaryHistoryCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private List<EmpLeaves> empLeavesCollection;

    @OneToMany(mappedBy = "departmentSDM")
    private List<Department> departmentCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personID")
    private List<JobChange> jobChangeCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personID")
    private List<EmpUnderperforming> empUnderperformingCollection;

    @JoinColumn(name = "DepartmentID", referencedColumnName = "DepartmentID")
    @ManyToOne(optional = false)
    private Department departmentID;

    @JoinColumn(name = "JobLangID", referencedColumnName = "JobLangID")
    @ManyToOne
    private JobLanguage jobLangID;

    @JoinColumn(name = "JobLevelID", referencedColumnName = "JobLevelID")
    @ManyToOne(optional = false)
    private JobLevel jobLevelID;

    @JoinColumn(name = "JobTypeID", referencedColumnName = "JobTypeID")
    @ManyToOne(optional = false)
    private JobType jobTypeID;

    @JoinColumn(name = "PersonID", referencedColumnName = "PersonID", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Person person;

    @OneToMany(mappedBy = "eRPersonID")
    private List<JobApp> jobAppCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personID")
    private List<JobPromotion> jobPromotionCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personID")
    private List<DiscHistory> discHistoryCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    private List<EmpTraining> empTrainingCollection;

    public Employee() {
    }

    public Employee(Integer personID) {
        this.personID = personID;
    }

    public Employee(Integer personID, String firstName, String lastName, char sex, Date startDate, boolean isRecrutor, boolean isResigned) {
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.startDate = startDate;
        this.isRecrutor = isRecrutor;
        this.isResigned = isResigned;
    }

    public Integer getPersonID() {
        return personID;
    }

    public void setPersonID(Integer personID) {
        this.personID = personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean getIsRecrutor() {
        return isRecrutor;
    }

    public void setIsRecrutor(boolean isRecrutor) {
        this.isRecrutor = isRecrutor;
    }

    public boolean getIsResigned() {
        return isResigned;
    }

    public void setIsResigned(boolean isResigned) {
        this.isResigned = isResigned;
    }

    public List<RecTeam> getRecTeamCollection() {
        return recTeamCollection;
    }

    public void setRecTeamCollection(List<RecTeam> recTeamCollection) {
        this.recTeamCollection = recTeamCollection;
    }

    public List<EmpFile> getEmpFileCollection() {
        return empFileCollection;
    }

    public void setEmpFileCollection(List<EmpFile> empFileCollection) {
        this.empFileCollection = empFileCollection;
    }

    public List<EmpComment> getEmpCommentCollection() {
        return empCommentCollection;
    }

    public void setEmpCommentCollection(List<EmpComment> empCommentCollection) {
        this.empCommentCollection = empCommentCollection;
    }

    public EmpConfidential getEmpConfidential() {
        return empConfidential;
    }

    public void setEmpConfidential(EmpConfidential empConfidential) {
        this.empConfidential = empConfidential;
    }

    public List<JobMove> getJobMoveCollection() {
        return jobMoveCollection;
    }

    public void setJobMoveCollection(List<JobMove> jobMoveCollection) {
        this.jobMoveCollection = jobMoveCollection;
    }

    public List<EmpSalaryHistory> getEmpSalaryHistoryCollection() {
        return empSalaryHistoryCollection;
    }

    public void setEmpSalaryHistoryCollection(List<EmpSalaryHistory> empSalaryHistoryCollection) {
        this.empSalaryHistoryCollection = empSalaryHistoryCollection;
    }

    public List<EmpLeaves> getEmpLeavesCollection() {
        return empLeavesCollection;
    }

    public void setEmpLeavesCollection(List<EmpLeaves> empLeavesCollection) {
        this.empLeavesCollection = empLeavesCollection;
    }

    public List<Department> getDepartmentCollection() {
        return departmentCollection;
    }

    public void setDepartmentCollection(List<Department> departmentCollection) {
        this.departmentCollection = departmentCollection;
    }

    public List<JobChange> getJobChangeCollection() {
        return jobChangeCollection;
    }

    public void setJobChangeCollection(List<JobChange> jobChangeCollection) {
        this.jobChangeCollection = jobChangeCollection;
    }

    public List<EmpUnderperforming> getEmpUnderperformingCollection() {
        return empUnderperformingCollection;
    }

    public void setEmpUnderperformingCollection(List<EmpUnderperforming> empUnderperformingCollection) {
        this.empUnderperformingCollection = empUnderperformingCollection;
    }

    public Department getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(Department departmentID) {
        this.departmentID = departmentID;
    }

    public JobLanguage getJobLangID() {
        return jobLangID;
    }

    public void setJobLangID(JobLanguage jobLangID) {
        this.jobLangID = jobLangID;
    }

    public JobLevel getJobLevelID() {
        return jobLevelID;
    }

    public void setJobLevelID(JobLevel jobLevelID) {
        this.jobLevelID = jobLevelID;
    }

    public JobType getJobTypeID() {
        return jobTypeID;
    }

    public void setJobTypeID(JobType jobTypeID) {
        this.jobTypeID = jobTypeID;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public List<JobApp> getJobAppCollection() {
        return jobAppCollection;
    }

    public void setJobAppCollection(List<JobApp> jobAppCollection) {
        this.jobAppCollection = jobAppCollection;
    }

    public List<JobPromotion> getJobPromotionCollection() {
        return jobPromotionCollection;
    }

    public void setJobPromotionCollection(List<JobPromotion> jobPromotionCollection) {
        this.jobPromotionCollection = jobPromotionCollection;
    }

    public List<DiscHistory> getDiscHistoryCollection() {
        return discHistoryCollection;
    }

    public void setDiscHistoryCollection(List<DiscHistory> discHistoryCollection) {
        this.discHistoryCollection = discHistoryCollection;
    }

    public List<EmpTraining> getEmpTrainingCollection() {
        return empTrainingCollection;
    }

    public void setEmpTrainingCollection(List<EmpTraining> empTrainingCollection) {
        this.empTrainingCollection = empTrainingCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (personID != null ? personID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.personID == null && other.personID != null) || (this.personID != null && !this.personID.equals(other.personID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "beans.Employee[personID=" + personID + "]";
    }
}
