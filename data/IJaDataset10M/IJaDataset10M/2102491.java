package uk.ac.ebi.intact.dbupdate.prot.errors;

/**
 * Interface for errors concerning a uniprot entry rather than specific intact proteins
 *
 * @author Marine Dumousseau (marine@ebi.ac.uk)
 * @version $Id$
 * @since <pre>08/08/11</pre>
 */
public interface UniprotUpdateError extends ProteinUpdateError {

    public String getUniprotAc();
}
