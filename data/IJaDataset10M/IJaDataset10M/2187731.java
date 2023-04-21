package packetsamurai.filter;

import java.util.List;
import packetsamurai.parser.DataStructure;
import javolution.util.FastList;

/**
 * 
 * @author Gilles Duboscq
 *
 */
public class Filter {

    private Condition _cond;

    public Filter(String str) {
        _cond = new Condition(str);
    }

    public Condition getFilterCondition() {
        return _cond;
    }

    public List<DataStructure> filterPacketList(List<DataStructure> packets) {
        List<DataStructure> list = new FastList<DataStructure>();
        for (DataStructure dp : packets) if (this.matches(dp)) list.add(dp);
        return list;
    }

    public boolean matches(DataStructure dp) {
        return this.getFilterCondition().evaluate(dp);
    }
}
