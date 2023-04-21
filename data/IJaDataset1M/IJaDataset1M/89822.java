package edu.unibi.agbi.dawismd.entities.biodwh.transpath.molecule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * @author Benjamin Kormeier
 * @version 1.0 15.05.2008
 */
@Entity(name = "tp_molecule_superfamalies")
@Table(name = "tp_molecule_superfamalies")
@IdClass(MoleculeSuperfamiliesId.class)
public class MoleculeSuperfamilies {

    @Id
    private Molecule moleculeId = new Molecule();

    @Id
    private String superfamilyId = new String();

    @Column(name = "molecule_superfamily")
    private String superfamaly = new String();

    public MoleculeSuperfamilies() {
    }

    /**
	 * @param moleculeId
	 * @param superfamilyId
	 * @param superfamaly
	 */
    public MoleculeSuperfamilies(Molecule moleculeId, String superfamilyId, String superfamaly) {
        super();
        this.moleculeId = moleculeId;
        this.superfamilyId = superfamilyId;
        this.superfamaly = superfamaly;
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
	 * @return the superfamilyId
	 */
    public String getSuperfamilyId() {
        return superfamilyId;
    }

    /**
	 * @param superfamilyId the superfamilyId to set
	 */
    public void setSuperfamilyId(String superfamilyId) {
        this.superfamilyId = superfamilyId;
    }

    /**
	 * @return the superfamaly
	 */
    public String getSuperfamaly() {
        return superfamaly;
    }

    /**
	 * @param superfamaly the superfamaly to set
	 */
    public void setSuperfamaly(String superfamaly) {
        this.superfamaly = superfamaly;
    }
}
