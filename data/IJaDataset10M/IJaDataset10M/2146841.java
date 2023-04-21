package com.jot.search;

import java.util.ArrayList;
import java.util.List;
import com.jot.system.pjson.Guid;

public class And extends SortedGuidStream {

    SortedGuidStream lhs, rhs;

    Guid head = null;

    public And(SortedGuidStream rhs, SortedGuidStream lhs) {
        this.rhs = rhs;
        this.lhs = lhs;
    }

    void sync() {
        if (head != null) return;
        while (true) {
            if (!lhs.hasNext()) return;
            if (!rhs.hasNext()) return;
            Guid r = rhs.head();
            Guid l = lhs.head();
            if (r.compareTo(l) < 0) rhs.popWhileLess(l); else lhs.popWhileLess(r);
            if (!lhs.hasNext()) return;
            if (!rhs.hasNext()) return;
            if (rhs.head().equals(lhs.head())) {
                head = rhs.head();
                rhs.next();
                lhs.next();
                return;
            } else {
            }
        }
    }

    @Override
    public boolean hasNext() {
        sync();
        if (head != null) return true;
        return false;
    }

    @Override
    public Guid head() {
        if (head != null) return head;
        sync();
        return head;
    }

    @Override
    public Guid next() {
        sync();
        Guid tmp = head;
        head = null;
        return tmp;
    }

    public static CollectionGuidStream fromStrs(String[] strs) {
        List<Guid> list = new ArrayList<Guid>();
        for (String string : strs) {
            list.add(new Guid(string));
        }
        return new CollectionGuidStream(list);
    }

    public static void main(String[] args) {
        CollectionGuidStream one = fromStrs(new String[] { "aa", "bb", "cc" });
        CollectionGuidStream two = fromStrs(new String[] { "ax", "bb", "cx" });
        CollectionGuidStream three = fromStrs(new String[] { "bb", "dd", "ee" });
        CollectionGuidStream four = fromStrs(new String[] { "aa", "bb", "ee", "ff", "gg" });
        And jj = new And(new And(one, two), new And(three, four));
        jj.print();
        System.out.println();
        one.reset();
        two.reset();
        three.reset();
        four.reset();
        And kk = new And(one, two);
        kk.print();
        System.out.println();
        one.reset();
        two.reset();
        three.reset();
        four.reset();
        And gg = new And(three, four);
        gg.print();
        System.out.println();
    }
}
