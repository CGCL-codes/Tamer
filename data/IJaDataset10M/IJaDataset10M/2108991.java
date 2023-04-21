package net.sourceforge.seqware.queryengine.backend.io.berkeleydb.tuplebinders;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import net.sourceforge.seqware.queryengine.backend.model.ContigPosition;

/**
 * @author boconnor
 *
 */
public class ContigPositionTB extends TupleBinding {

    @Override
    public void objectToEntry(Object object, TupleOutput to) {
        ContigPosition ag = (ContigPosition) object;
        to.writeString(ag.getContig());
        to.writeInt(ag.getStartPosition());
        to.writeInt(ag.getStopPosition());
    }

    @Override
    public Object entryToObject(TupleInput ti) {
        String contig = ti.readString();
        int startPosition = ti.readInt();
        int stopPosition = ti.readInt();
        ContigPosition ag = new ContigPosition();
        ag.setContig(contig);
        ag.setStartPosition(startPosition);
        ag.setStopPosition(stopPosition);
        return (ag);
    }
}
