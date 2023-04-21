package org.ochnygosch.jIMAP.base.command.store;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import org.ochnygosch.jIMAP.internal.Argument;
import org.ochnygosch.jIMAP.internal.RawData;

public class FlagListDataItem extends MessageDataItem {

    private Set<String> flags;

    public FlagListDataItem(String name) {
        super(name);
        this.flags = new HashSet<String>();
    }

    public void setFlag(String flag) {
        this.flags.add(flag);
    }

    @Override
    public List<Argument> getArguments() {
        List<Argument> l = super.getArguments();
        String flgs = "(";
        Iterator<String> it = this.flags.iterator();
        boolean first = true;
        while (it.hasNext()) {
            if (first) {
                first = false;
            } else {
                flgs += " ";
            }
            flgs += it.next();
        }
        flgs += ")";
        l.add(new RawData(flgs));
        return l;
    }
}
