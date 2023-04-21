package binky.reportrunner.data.sampling;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import binky.reportrunner.data.DatabaseObject;
import binky.reportrunner.data.RunnerDashboardSampler;

@Entity(name = "T_S_DATA")
public class SamplingData extends DatabaseObject<Long> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7337434800918457411L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal value;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public SamplingData() {
    }

    public SamplingData(RunnerDashboardSampler sampler, Long sampleDate, BigDecimal value) {
        this.sampler = sampler;
        this.sampleTime = sampleDate;
        this.value = value;
    }

    @ManyToOne
    private RunnerDashboardSampler sampler;

    private Long sampleTime;

    public RunnerDashboardSampler getSampler() {
        return sampler;
    }

    public void setSampler(RunnerDashboardSampler sampler) {
        this.sampler = sampler;
    }

    public Long getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(Long sampleTime) {
        this.sampleTime = sampleTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((sampleTime == null) ? 0 : sampleTime.hashCode());
        result = prime * result + ((sampler == null) ? 0 : sampler.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SamplingData other = (SamplingData) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (sampleTime == null) {
            if (other.sampleTime != null) return false;
        } else if (!sampleTime.equals(other.sampleTime)) return false;
        if (sampler == null) {
            if (other.sampler != null) return false;
        } else if (!sampler.equals(other.sampler)) return false;
        if (value == null) {
            if (other.value != null) return false;
        } else if (!value.equals(other.value)) return false;
        return true;
    }
}
