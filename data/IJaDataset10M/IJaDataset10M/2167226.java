package edu.unibi.agbi.dawismd.entities.biodwh.transpath.molecule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Benjamin Kormeier
 * @version 1.0 15.05.2008
 */
@Entity(name = "tp_molecule_location_neg")
@Table(name = "tp_molecule_location_neg")
public class MoleculeLocationNegative {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long locationId = 0L;

    @ManyToOne
    @JoinColumn(name = "molecule_id")
    private Molecule moleculeId = new Molecule();

    @Column(name = "molecule_experiment", length = 512)
    private String experiment = new String();

    public MoleculeLocationNegative() {
    }

    /**
	 * @param moleculeId
	 * @param experiment
	 */
    public MoleculeLocationNegative(Molecule moleculeId, String experiment) {
        this.moleculeId = moleculeId;
        this.experiment = experiment;
    }

    /**
	 * @return the locationId
	 */
    public Long getLocationId() {
        return locationId;
    }

    /**
	 * @param locationId the locationId to set
	 */
    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    /**
	 * @return the moleculeId
	 */
    public Molecule getMoleculeId() {
        return moleculeId;
    }

    /**
	 * @param moleculeId the moleculeId to set
	 */
    public void setMoleculeId(Molecule moleculeId) {
        this.moleculeId = moleculeId;
    }

    /**
	 * @return the experiment
	 */
    public String getExperiment() {
        return experiment;
    }

    /**
	 * @param experiment the experiment to set
	 */
    public void setExperiment(String experiment) {
        this.experiment = experiment;
    }
}
