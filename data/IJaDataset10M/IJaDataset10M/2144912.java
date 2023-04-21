package net.community.chest.eclipselink.test.data;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import net.community.chest.reflect.ClassUtil;
import net.community.chest.util.compare.AbstractComparator;

/**
 * <P>Copyright as per GPLv2</P>
 * @author Lyor G.
 * @since Aug 30, 2010 12:29:28 PM
 */
@Entity
@Table(name = "plugin_data")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "aspect_type", discriminatorType = DiscriminatorType.STRING)
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractPluginData implements Serializable, Cloneable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3042043820566095863L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @XmlTransient
    private Long _id;

    @Version
    @XmlTransient
    private int _version;

    @Column(name = "start_time", nullable = false)
    @XmlElement(name = "startTime", nillable = false, required = true)
    private long _sampleStartTime;

    @Column(name = "end_time", nullable = false)
    @XmlElement(name = "endTime", nillable = false, required = true)
    private long _sampleEndTime;

    @Transient
    @XmlTransient
    private final PluginAspectType _aspectType;

    public final PluginAspectType getAspectType() {
        return _aspectType;
    }

    protected AbstractPluginData(final PluginAspectType aspectType) {
        if (null == (_aspectType = aspectType)) throw new IllegalStateException("No aspect type provided");
        _sampleStartTime = System.currentTimeMillis();
    }

    @SuppressWarnings("unused")
    private AbstractPluginData() {
        this(null);
    }

    public final Long getId() {
        return _id;
    }

    public final void setId(Long id) {
        _id = id;
    }

    public final int getVersion() {
        return _version;
    }

    public long getSampleStartTime() {
        return _sampleStartTime;
    }

    public void setSampleStartTime(long sampleStartTime) {
        _sampleStartTime = sampleStartTime;
    }

    public long getSampleEndTime() {
        return _sampleEndTime;
    }

    public void setSampleEndTime(long sampleEndTime) {
        _sampleEndTime = sampleEndTime;
    }

    public long getSampleDuration() {
        return getSampleEndTime() - getSampleStartTime();
    }

    @Override
    public AbstractPluginData clone() throws CloneNotSupportedException {
        return getClass().cast(super.clone());
    }

    @Override
    public String toString() {
        final DateFormat dtf = DateFormat.getDateTimeInstance();
        return new StringBuilder(128).append(getAspectType()).append(";start=").append(dtf.format(new Date(getSampleStartTime()))).append(";end=").append(dtf.format(new Date(getSampleEndTime()))).append(";duration=").append(getSampleDuration()).toString();
    }

    @Override
    public int hashCode() {
        return (int) (ClassUtil.getObjectHashCode(getAspectType()) + getSampleStartTime() + getSampleEndTime());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AbstractPluginData)) return false;
        if (this == obj) return true;
        final AbstractPluginData od = (AbstractPluginData) obj;
        if ((AbstractComparator.compareComparables(getAspectType(), od.getAspectType()) != 0) || (getSampleStartTime() != od.getSampleStartTime()) || (getSampleEndTime() != od.getSampleEndTime())) return false;
        return true;
    }
}
