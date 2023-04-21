package net.community.chest.jfree.jcommon.util;

import java.util.NoSuchElementException;
import org.jfree.util.TableOrder;
import net.community.chest.dom.AbstractXmlValueStringInstantiator;
import net.community.chest.lang.StringUtil;

/**
 * <P>Copyright GPLv2</P>
 *
 * @author Lyor G.
 * @since May 26, 2009 12:41:33 PM
 */
public class TableOrderEnumStringInstantiator extends AbstractXmlValueStringInstantiator<TableOrder> {

    public TableOrderEnumStringInstantiator() {
        super(TableOrder.class);
    }

    @Override
    public String convertInstance(TableOrder inst) throws Exception {
        if (null == inst) return null;
        final TableOrderEnum o = TableOrderEnum.fromTableOrder(inst);
        if (null == o) throw new NoSuchElementException("convertInstance(" + inst + ") uknown value");
        return o.toString();
    }

    @Override
    public TableOrder newInstance(String v) throws Exception {
        final String s = StringUtil.getCleanStringValue(v);
        if ((null == s) || (s.length() <= 0)) return null;
        final TableOrderEnum o = TableOrderEnum.fromString(s);
        if (null == o) throw new NoSuchElementException("newInstance(" + s + ") uknown value");
        return o.getTableOrder();
    }

    public static final TableOrderEnumStringInstantiator DEFAULT = new TableOrderEnumStringInstantiator();
}
