package uk.ac.ebi.intact.dataexchange.psimi.solr;

/**
 * Contains an ID and the count.
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: InteractorIdCount.java 12736 2009-02-24 09:34:32Z baranda $
 */
public class InteractorIdCount {

    private String ac;

    private long count;

    public InteractorIdCount(String ac, long count) {
        this.ac = ac;
        this.count = count;
    }

    public String getAc() {
        return ac;
    }

    public long getCount() {
        return count;
    }
}
