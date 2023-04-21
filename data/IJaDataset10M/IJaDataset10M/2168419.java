package se.biobanksregistersyd.boakeity.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Entity bean for storing sample collections.
 */
@Entity
public class SampleCollection implements java.io.Serializable {

    /** The id of the sample collection. This is a code that uniquely identifies
     *  the sample collection within the biobank (and not only within the
     *  biobank division!).
     */
    private String sampleCollectionId;

    /** The name of the sample collection. */
    private String name;

    /** The biobank this sample collection is associated with.
     *  See note on field 'biobankDivision'. */
    private Biobank biobank;

    /** The biobank division this sample collection is associated with.
     *  Note that the is potential risk for inconsistency since a sample
     *  collection is both directly associated with a biobank and indirectly
     *  through a biobankdivision. These relations should always match. */
    private BiobankDivision biobankDivision;

    /**
     * Getter for field 'sampleCollectionId'.
     * 
     * @return a <code>String</code> representing the id of the sample
     *         collection.
     * @see #setSampleCollectionId
     */
    @Id
    public String getSampleCollectionId() {
        return sampleCollectionId;
    }

    /**
     * Setter for field 'sampleCollectionId'.
     * 
     * @param sampleCollectionIdIn  a <code>String</code> specifying the id
     *        of the sample collection.
     * @see #getSampleCollectionId
     */
    public void setSampleCollectionId(final String sampleCollectionIdIn) {
        sampleCollectionId = sampleCollectionIdIn;
    }

    /**
     * Getter for field 'name'.
     * 
     * @return a <code>String</code> representing the name of the sample
     *         collection.
     * @see #setName
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for field 'name'.
     * 
     * @param nameIn  a <code>String</code> specifying the name of the sample
     *                collection.
     * @see #getName
     */
    public void setName(final String nameIn) {
        name = nameIn;
    }

    /**
     * Getter for relation 'samplecollection-has-one-biobank'.
     * 
     * @return a <code>Biobank</code> which is associated with
     *         this <code>SampleCollection</code>.
     * @see #setBiobank
     */
    @ManyToOne
    public Biobank getBiobank() {
        return biobank;
    }

    /**
     * Setter for relation 'samplecollection-has-one-biobank'.
     * 
     * @param biobankIn  a <code>Biobank</code> which is to be
     *                   associated with this <code>SampleCollection</code>.
     * @see #getBiobank
     */
    public void setBiobank(final Biobank biobankIn) {
        biobank = biobankIn;
    }

    /**
     * Getter for relation 'samplecollection-has-one-biobankdivision'.
     * 
     * @return a <code>BiobankDivision</code> which is associated with
     *         this <code>SampleCollection</code>.
     * @see #setBiobankDivision
     */
    @ManyToOne
    public BiobankDivision getBiobankDivision() {
        return biobankDivision;
    }

    /**
     * Setter for relation 'samplecollection-has-one-biobankdivision'.
     * 
     * @param biobankDivisionIn  a <code>BiobankDivision</code> which is
     *                           to be associated with this
     *                           <code>SampleCollection</code>.
     * @see #getBiobank
     */
    public void setBiobankDivision(final BiobankDivision biobankDivisionIn) {
        biobankDivision = biobankDivisionIn;
    }
}
