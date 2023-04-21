package org.joy.index.db.entry;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lamfeeling
 */
public class CompactHitBinding extends TupleBinding {

    @Override
    public Object entryToObject(TupleInput in) {
        try {
            int length = in.readInt();
            byte[] b = new byte[length];
            in.read(b);
            return new CompactHit(b);
        } catch (IOException ex) {
            Logger.getLogger(CompactHitBinding.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void objectToEntry(Object obj, TupleOutput out) {
        try {
            CompactHit cp = (CompactHit) obj;
            out.writeInt(cp.getData().length);
            out.write(cp.getData());
        } catch (IOException ex) {
            Logger.getLogger(CompactHitBinding.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
