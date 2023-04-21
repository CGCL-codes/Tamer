package uk.ac.ebi.intact.util.uniprotExport.parameters.drlineparameters;

/**
 * The DR parameters should provide the uniprot ac and a number of interactions
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>31/01/11</pre>
 */
public interface DRParameters {

    /**
     * The uniprot ac
     * @return
     */
    public String getUniprotAc();

    /**
     * The number of distinct interactors (isoforms/feature chains are merged with uniprot master)and self interaction are not counted
     * @return
     */
    public int getNumberOfInteractions();
}
