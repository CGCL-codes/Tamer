package router.policy;

import java.io.Serializable;
import packet.flit.header.address.*;

public class CountEntry implements Serializable {

    Address dest_address;

    int[] block_count;

    public CountEntry(Address addr) {
        dest_address = addr;
        block_count = new int[5];
        int i;
        for (i = 0; i < 2; i++) block_count[i] = 0;
    }

    public boolean equals(Address addr) {
        if (dest_address.getField(0).getBindArg() != addr.getField(0).getBindArg()) return false;
        if (dest_address.getField(0).getOffsetArg() != addr.getField(0).getOffsetArg()) return false;
        if (dest_address.getField(1).getBindArg() != addr.getField(1).getBindArg()) return false;
        if (dest_address.getField(1).getOffsetArg() != addr.getField(1).getOffsetArg()) return false;
        return true;
    }

    public int get_block_count(int dir) {
        return block_count[dir];
    }

    public void inc_block_count(int dir) {
        block_count[dir]++;
    }

    public void dec_block_count(int dir) {
        block_count[dir]--;
    }
}
