package rascal;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Cycix
 */
@Entity
@Table(name = "jobs", catalog = "rscdb", schema = "")
@NamedQueries({ @NamedQuery(name = "Jobs.findAll", query = "SELECT j FROM Jobs j"), @NamedQuery(name = "Jobs.findByIdjobs", query = "SELECT j FROM Jobs j WHERE j.idjobs = :idjobs"), @NamedQuery(name = "Jobs.findByReport", query = "SELECT j FROM Jobs j WHERE j.report = :report"), @NamedQuery(name = "Jobs.findByScript", query = "SELECT j FROM Jobs j WHERE j.script = :script") })
public class Jobs implements Serializable {

    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idjobs")
    private Integer idjobs;

    @Column(name = "report")
    private Integer report;

    @Basic(optional = false)
    @Column(name = "script")
    private int script;

    @JoinColumn(name = "sample", referencedColumnName = "idsamples")
    @ManyToOne
    private Samples sample;

    public Jobs() {
    }

    public Jobs(Integer idjobs) {
        this.idjobs = idjobs;
    }

    public Jobs(Integer idjobs, int script) {
        this.idjobs = idjobs;
        this.script = script;
    }

    public Integer getIdjobs() {
        return idjobs;
    }

    public void setIdjobs(Integer idjobs) {
        Integer oldIdjobs = this.idjobs;
        this.idjobs = idjobs;
        changeSupport.firePropertyChange("idjobs", oldIdjobs, idjobs);
    }

    public Integer getReport() {
        return report;
    }

    public void setReport(Integer report) {
        Integer oldReport = this.report;
        this.report = report;
        changeSupport.firePropertyChange("report", oldReport, report);
    }

    public int getScript() {
        return script;
    }

    public void setScript(int script) {
        int oldScript = this.script;
        this.script = script;
        changeSupport.firePropertyChange("script", oldScript, script);
    }

    public Samples getSample() {
        return sample;
    }

    public void setSample(Samples sample) {
        Samples oldSample = this.sample;
        this.sample = sample;
        changeSupport.firePropertyChange("sample", oldSample, sample);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idjobs != null ? idjobs.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Jobs)) {
            return false;
        }
        Jobs other = (Jobs) object;
        if ((this.idjobs == null && other.idjobs != null) || (this.idjobs != null && !this.idjobs.equals(other.idjobs))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "rascal.Jobs[idjobs=" + idjobs + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
}
