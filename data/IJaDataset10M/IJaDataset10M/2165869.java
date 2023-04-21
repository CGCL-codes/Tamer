package apollo.dataadapter.chado.jdbc;

import apollo.datamodel.SequenceI;
import apollo.datamodel.seq.AbstractLazySequence;

class ChadoLazySequence extends AbstractLazySequence {

    private transient JdbcChadoAdapter adapter;

    /** should this work off feature_id or uniquename - probably uniquename if we want it
      to work with game as well */
    ChadoLazySequence(String uniquename, int length, JdbcChadoAdapter adap) {
        super(uniquename, null);
        setLength(length);
        adapter = adap;
    }

    private String getUniqueName() {
        return super.getName();
    }

    protected String getResiduesFromSourceImpl(int low, int high) {
        SequenceI seq = adapter.getResiduesSubstring(getUniqueName(), low, high);
        return seq.getResidues();
    }

    /** this actually isnt used in apollo - take out of SeqI? */
    public SequenceI getSubSequence(int start, int end) {
        return null;
    }
}
