package net.community.chest.jfree.jfreechart.chart.renderer.area;

import java.util.NoSuchElementException;
import org.jfree.chart.renderer.AreaRendererEndType;
import net.community.chest.dom.AbstractXmlValueStringInstantiator;
import net.community.chest.lang.StringUtil;

/**
 * <P>Copyright GPLv2</P>
 *
 * @author Lyor G.
 * @since Jun 8, 2009 1:56:55 PM
 */
public class AreaRendererEndTypeValueStringInstantiator extends AbstractXmlValueStringInstantiator<AreaRendererEndType> {

    public AreaRendererEndTypeValueStringInstantiator() {
        super(AreaRendererEndType.class);
    }

    @Override
    public String convertInstance(AreaRendererEndType inst) throws Exception {
        if (null == inst) return null;
        final AreaEndType a = AreaEndType.fromType(inst);
        if (null == a) throw new NoSuchElementException("convertInstance(" + inst + ") unknown value");
        return a.toString();
    }

    @Override
    public AreaRendererEndType newInstance(String v) throws Exception {
        final String s = StringUtil.getCleanStringValue(v);
        if ((null == s) || (s.length() <= 0)) return null;
        final AreaEndType a = AreaEndType.fromString(s);
        if (null == a) throw new NoSuchElementException("newInstance(" + s + ") unknown value");
        return a.getType();
    }

    public static final AreaRendererEndTypeValueStringInstantiator DEFAULT = new AreaRendererEndTypeValueStringInstantiator();
}
