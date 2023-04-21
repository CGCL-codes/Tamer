package net.sourceforge.seqware.common.model.lists;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.seqware.common.model.SequencerRun;

/**
 *
 * @author mtaschuk
 */
public class SequencerRunList {

    protected List<SequencerRun> tList;

    public SequencerRunList() {
        tList = new ArrayList<SequencerRun>();
    }

    public List<SequencerRun> getList() {
        return tList;
    }

    public void setList(List<SequencerRun> list) {
        this.tList = list;
    }

    public void add(SequencerRun ex) {
        tList.add(ex);
    }
}
