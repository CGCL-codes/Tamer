package uk.ac.ebi.intact.sanity.check.model;

/**
 * TODO comment it.
 *
 * @author Catherine Leroy (cleroy@ebi.ac.uk)
 * @version $Id: SequenceChunkBean.java,v 1.1 2005/07/28 16:13:29 catherineleroy Exp $
 */
public class SequenceChunkBean extends IntactBean {

    private String sequence_chunk;

    private int sequence_index;

    public SequenceChunkBean() {
    }

    public String getSequence_chunk() {
        return sequence_chunk;
    }

    public void setSequence_chunk(String sequence_chunk) {
        this.sequence_chunk = sequence_chunk;
    }

    public int getSequence_index() {
        return sequence_index;
    }

    public void setSequence_index(int sequence_index) {
        this.sequence_index = sequence_index;
    }
}
