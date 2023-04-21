package net.community.chest.awt.dom.converter;

import java.awt.Cursor;
import java.util.NoSuchElementException;
import net.community.chest.awt.CursorType;
import net.community.chest.dom.AbstractXmlValueStringInstantiator;
import net.community.chest.lang.StringUtil;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * @param <C> The instantiated {@link Cursor} type
 * @author Lyor G.
 * @since Mar 20, 2008 11:14:46 AM
 */
public class CursorValueInstantiator<C extends Cursor> extends AbstractXmlValueStringInstantiator<C> {

    public CursorValueInstantiator(Class<C> objClass) throws IllegalArgumentException {
        super(objClass);
    }

    @Override
    public String convertInstance(final C inst) throws Exception {
        if (null == inst) return null;
        final CursorType t = CursorType.fromCursor(inst);
        if (null == t) throw new NoSuchElementException(getArgumentsExceptionLocation("convertInstance", inst) + " no matching " + CursorType.class.getSimpleName() + " value");
        return t.toString();
    }

    @Override
    public C newInstance(final String v) throws Exception {
        final String s = StringUtil.getCleanStringValue(v);
        if ((null == s) || (s.length() <= 0)) return null;
        final CursorType t = CursorType.fromString(s);
        if (null == t) throw new NoSuchElementException(getArgumentsExceptionLocation("newInstance", s) + " no matching " + CursorType.class.getSimpleName() + " value");
        return getValuesClass().cast(t.getCursor());
    }

    public static final CursorValueInstantiator<Cursor> DEFAULT = new CursorValueInstantiator<Cursor>(Cursor.class);
}
