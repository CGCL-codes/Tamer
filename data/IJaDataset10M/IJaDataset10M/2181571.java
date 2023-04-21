package uk.ac.ebi.intact.dbupdate.prot.actions;

import uk.ac.ebi.intact.dbupdate.prot.event.ProteinEvent;

/**
 * This interface is for classes charged to remap a protein to a uniprot entry
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>06-Jan-2011</pre>
 */
public interface UniprotProteinMapper {

    /**
     * This method will remap a protein to a uniprot entry.
     * @param evt : evt with the protein to remap
     * @return true if the protein has been remapped successfully to a uniprot entry, false otherwise
     */
    public boolean processProteinRemappingFor(ProteinEvent evt);
}
