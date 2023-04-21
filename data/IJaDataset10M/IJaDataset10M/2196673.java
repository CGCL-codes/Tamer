package no.uib.hplims.models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import org.vaadin.appfoundation.persistence.data.AbstractPojo;
import uk.ac.ebi.kraken.interfaces.uniprot.features.Feature;

@Entity
public class BlastInfo extends AbstractPojo {

    private static final long serialVersionUID = -3437355398006106923L;

    private String uniProtAccessionNumber;

    private String proteinName;

    private String proteinShortName;

    private int peptideStart;

    private int peptideEnd;

    private List<Feature> knownModifications = null;

    ;

    public BlastInfo() {
    }

    @Override
    public String toString() {
        return proteinName + ", " + uniProtAccessionNumber + ", " + proteinShortName;
    }

    /**
	 * @return the uniProtAccessionNumber
	 */
    public String getUniProtAccessionNumber() {
        return uniProtAccessionNumber;
    }

    /**
	 * @param uniProtAccessionNumber
	 *            the uniProtAccessionNumber to set
	 */
    public void setUniProtAccessionNumber(String uniProtAccessionNumber) {
        this.uniProtAccessionNumber = uniProtAccessionNumber;
    }

    /**
	 * @return the proteinName
	 */
    public String getProteinName() {
        return proteinName;
    }

    /**
	 * @param proteinName
	 *            the proteinName to set
	 */
    public void setProteinName(String proteinName) {
        this.proteinName = proteinName;
    }

    /**
	 * @return the proteinShortName
	 */
    public String getProteinShortName() {
        return proteinShortName;
    }

    /**
	 * @param proteinShortName
	 *            the proteinShortName to set
	 */
    public void setProteinShortName(String proteinShortName) {
        this.proteinShortName = proteinShortName;
    }

    /**
	 * @return the peptideStart
	 */
    public int getPeptideStart() {
        return peptideStart;
    }

    /**
	 * @param peptideStart
	 *            the peptideStart to set
	 */
    public void setPeptideStart(int peptideStart) {
        this.peptideStart = peptideStart;
    }

    /**
	 * @return the peptideEnd
	 */
    public int getPeptideEnd() {
        return peptideEnd;
    }

    /**
	 * @param peptideEnd
	 *            the peptideEnd to set
	 */
    public void setPeptideEnd(int peptideEnd) {
        this.peptideEnd = peptideEnd;
    }

    /**
	 * @return the knownModifications
	 */
    public List<Feature> getKnownModifications() {
        if (knownModifications == null) {
            knownModifications = new ArrayList<Feature>();
        }
        return knownModifications;
    }

    /**
	 * @param knownModifications the knownModifications to set
	 */
    public void setKnownModifications(List<Feature> knownModifications) {
        this.knownModifications = knownModifications;
    }
}
