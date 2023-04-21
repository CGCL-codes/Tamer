package procsim;

import java.util.*;

public class XOR extends LogicalElement {

    @Override
    public XOR add(Signal sig) {
        super.add(sig);
        return this;
    }

    @Override
    public XOR tick() {
        res = 0;
        Iterator iter = list.iterator();
        while (iter.hasNext()) res = res ^ ((Signal) iter.next()).get();
        if (result != null) result.set(res);
        invertIfNeeded();
        return this;
    }
}
