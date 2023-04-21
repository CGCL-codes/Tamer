package net.community.chest.swing;

import java.util.NoSuchElementException;
import net.community.chest.dom.AbstractXmlValueStringInstantiator;
import net.community.chest.lang.StringUtil;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Feb 8, 2009 9:17:17 AM
 */
public class HAlignmentValueStringInstantiator extends AbstractXmlValueStringInstantiator<Integer> {

    public HAlignmentValueStringInstantiator() {
        super(Integer.class);
    }

    @Override
    public String convertInstance(Integer inst) throws Exception {
        if (null == inst) return null;
        final HAlignmentValue st = HAlignmentValue.fromAlignmentValue(inst.intValue());
        if (null == st) throw new NoSuchElementException("convertInstance(" + inst + ") unknown value");
        return st.toString();
    }

    @Override
    public Integer newInstance(String v) throws Exception {
        final String s = StringUtil.getCleanStringValue(v);
        if ((null == s) || (s.length() <= 0)) return null;
        final HAlignmentValue st = HAlignmentValue.fromString(s);
        if (null == st) throw new NoSuchElementException("newInstance(" + s + ") unknown value");
        return Integer.valueOf(st.getAlignmentValue());
    }

    public static final HAlignmentValueStringInstantiator DEFAULT = new HAlignmentValueStringInstantiator();
}
