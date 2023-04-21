package lablog.lib.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lablog.lib.db.entity.base.EntityMetadata;

/**
 * Entity to store stimulus definitions. Extends {@link Resource}. 
 * 
 * @assoc n - 1 StimulusType
 * @assoc n - 1 StimulusModality 
 *
 */
@Entity
@Table(name = "stimulus")
@EntityMetadata(name = "Stimulus", odmlmapping = "stimulus")
public class Stimulus extends Resource {

    public static final short CLASSID = 25;

    public static final long serialVersionUID = createSerialUID(DB_VERSION, MODULE, CLASSID);

    private Float duration;

    private Float intentsity;

    private Float startTime;

    private Integer repetitions;

    private StimulusType stimulusType;

    private StimulusModality modality;

    public Stimulus() {
        super();
    }

    public Stimulus(String name, Person owner, Group ownerGroup) {
        super(name, owner, ownerGroup);
    }

    public void setDuration(Float duration) {
        this.duration = duration;
    }

    @Column(name = "duration", precision = 10)
    @EntityMetadata(name = "Duration", order = 6, odmlmapping = "stimulus#duration")
    public Float getDuration() {
        return duration;
    }

    public void setIntentsity(Float intentsity) {
        this.intentsity = intentsity;
    }

    @Column(name = "intensity", precision = 10)
    @EntityMetadata(name = "Intensity", order = 7, odmlmapping = "stimulus#intensity")
    public Float getIntentsity() {
        return intentsity;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    @Column(name = "Repetitions")
    @EntityMetadata(name = "repetitions", order = 8, odmlmapping = "stimulus#repetitions")
    public Integer getRepetitions() {
        return repetitions;
    }

    public void setStimulusType(StimulusType type) {
        this.stimulusType = type;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @EntityMetadata(name = "stimulus_type", order = 2, odmlmapping = "stimulus#type")
    public StimulusType getStimulusType() {
        return stimulusType;
    }

    public void setModality(StimulusModality modality) {
        this.modality = modality;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @EntityMetadata(name = "Modality", order = 3, odmlmapping = "stimulus#modality")
    public StimulusModality getModality() {
        return this.modality;
    }

    public void setStartTime(Float startTime) {
        this.startTime = startTime;
    }

    @Column(name = "start_time")
    @EntityMetadata(name = "Start time", order = 10, odmlmapping = "stimulus#temporalOffset")
    public Float getStartTime() {
        return startTime;
    }
}
