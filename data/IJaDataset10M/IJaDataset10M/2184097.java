package drcl.diffserv.tool;

import java.util.HashMap;
import java.util.Iterator;
import drcl.comp.*;
import drcl.comp.contract.GarbageContract;
import drcl.net.Packet;
import drcl.inet.InetPacket;
import drcl.diffserv.DFUtil;
import drcl.diffserv.DFConstants;

/**
  */
public class DFFlowPktCounter extends drcl.comp.Component implements DFConstants {

    HashMap hmCount = null;

    static final String[] PKT_CLASSES = { "EF", "BE", "AF11", "AF12", "AF13", "AF21", "AF22", "AF23", "AF31", "AF32", "AF33" };

    public DFFlowPktCounter() {
        super();
    }

    public DFFlowPktCounter(String id_) {
        super(id_);
    }

    public void reset() {
        super.reset();
        if (hmCount != null) hmCount.clear();
    }

    protected void process(Object data_, Port inPort_) {
        if (data_ instanceof GarbageContract.Message) data_ = ((GarbageContract.Message) data_).getData();
        while (data_ instanceof Packet) {
            if (data_ instanceof InetPacket) {
                if (hmCount == null) hmCount = new HashMap();
                long src_ = ((InetPacket) data_).getSource();
                int dscp_ = DFUtil.getDSCP((InetPacket) data_);
                long[] count_ = (long[]) hmCount.get(new Long(src_));
                if (count_ == null) {
                    count_ = new long[11];
                    hmCount.put(new Long(src_), count_);
                }
                int index_ = dscp_ == EF ? 0 : dscp_ == BE ? 1 : dscp_ >= AF31 ? (dscp_ - AF31) / 2 + 8 : dscp_ >= AF21 ? (dscp_ - AF21) / 2 + 5 : (dscp_ - AF11) / 2 + 2;
                count_[index_]++;
                return;
            } else data_ = ((Packet) data_).getBody();
        }
    }

    public String info() {
        if (hmCount == null || hmCount.size() == 0) return "Nothing been counted.\n";
        StringBuffer sb = new StringBuffer();
        for (Iterator it = hmCount.keySet().iterator(); it.hasNext(); ) {
            Object key_ = it.next();
            long[] count_ = (long[]) hmCount.get(key_);
            long total_ = 0;
            StringBuffer sb2 = new StringBuffer();
            for (int i = 0; i < count_.length; i++) {
                total_ += count_[i];
                if (count_[i] > 0) sb2.append(" " + PKT_CLASSES[i] + "=" + count_[i]);
            }
            if (total_ > 0) sb.append(key_ + ": total=" + total_ + sb2 + "\n");
        }
        return sb.toString();
    }
}
