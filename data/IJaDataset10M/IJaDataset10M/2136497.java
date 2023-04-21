package eulergui.parser.n3.impl.parser4j.entity.data;

import net.sf.parser4j.parser.entity.parsenode.data.AbstractParserNodeData;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class BarNameListData extends AbstractParserNodeData {

    private final String barName;

    private final BarNameListData next;

    public BarNameListData(final String barName, final BarNameListData next) {
        super();
        this.barName = barName;
        this.next = next;
    }

    public BarNameListData(final String barName) {
        super();
        this.barName = barName;
        this.next = null;
    }

    public BarNameListData() {
        super();
        this.barName = null;
        this.next = null;
    }

    public String getBarName() {
        return barName;
    }

    public BarNameListData getNext() {
        return next;
    }
}
