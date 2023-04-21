package router.policy;

import packet.flit.header.address.*;
import java.io.Serializable;
import java.util.Vector;

public class CountTable implements Serializable {

    Vector block_count_table;

    public CountTable() {
        block_count_table = new Vector();
    }

    public int getBlockCount(Address addr, int dir) {
        int i;
        CountEntry myEntry;
        for (i = 0; i < block_count_table.size(); i++) {
            myEntry = (CountEntry) block_count_table.elementAt(i);
            if (myEntry.equals(addr)) return myEntry.get_block_count(dir);
        }
        myEntry = new CountEntry(addr.copy());
        block_count_table.add(myEntry);
        return 0;
    }

    public int incBlockCount(Address addr, int dir) {
        int i;
        CountEntry myEntry;
        for (i = 0; i < block_count_table.size(); i++) {
            myEntry = (CountEntry) block_count_table.elementAt(i);
            if (myEntry.equals(addr)) {
                myEntry.inc_block_count(dir);
                return myEntry.get_block_count(dir);
            }
        }
        myEntry = new CountEntry(addr.copy());
        myEntry.inc_block_count(dir);
        block_count_table.add(myEntry);
        return 1;
    }

    public int decBlockCount(Address addr, int dir) {
        int i;
        if (addr == null) {
            System.err.println("CountTable.decBlockCount: addr parameter is null!");
            System.exit(0);
        }
        CountEntry myEntry;
        for (i = 0; i < block_count_table.size(); i++) {
            myEntry = (CountEntry) block_count_table.elementAt(i);
            if (myEntry.equals(addr)) {
                myEntry.dec_block_count(dir);
                return myEntry.get_block_count(dir);
            }
        }
        return 0;
    }
}
