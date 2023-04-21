package edu.unibi.agbi.biodwh.entity.go;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "go_instance_data")
@Table(name = "go_instance_data")
public class InstanceData implements java.io.Serializable {

    private static final long serialVersionUID = 8536186233270498442L;

    private Integer id = 0;

    private String releaseName;

    private String releaseType;

    private String releaseNotes;

    public InstanceData() {
    }

    public InstanceData(String releaseName, String releaseType, String releaseNotes) {
        this.releaseName = releaseName;
        this.releaseType = releaseType;
        this.releaseNotes = releaseNotes;
    }

    /**
	 * @return the id
	 */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "release_name")
    public String getReleaseName() {
        return this.releaseName;
    }

    public void setReleaseName(String releaseName) {
        this.releaseName = releaseName;
    }

    @Column(name = "release_type")
    public String getReleaseType() {
        return this.releaseType;
    }

    public void setReleaseType(String releaseType) {
        this.releaseType = releaseType;
    }

    @Column(name = "release_notes")
    public String getReleaseNotes() {
        return this.releaseNotes;
    }

    public void setReleaseNotes(String releaseNotes) {
        this.releaseNotes = releaseNotes;
    }
}
