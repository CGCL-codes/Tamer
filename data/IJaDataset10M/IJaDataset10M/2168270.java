package net.community.chest.jfree.jfreechart.axis.value;

import java.lang.reflect.Method;
import java.util.Collection;
import net.community.chest.dom.DOMUtils;
import net.community.chest.lang.StringUtil;
import org.jfree.chart.axis.SymbolAxis;
import org.w3c.dom.Element;

/**
 * <P>Copyright GPLv2</P>
 *
 * @param <A> The reflected {@link SymbolAxis} type
 * @author Lyor G.
 * @since May 25, 2009 9:53:12 AM
 */
public class SymbolAxisReflectiveProxy<A extends SymbolAxis> extends NumberAxisReflectiveProxy<A> {

    protected SymbolAxisReflectiveProxy(Class<A> objClass, boolean registerAsDefault) throws IllegalArgumentException, IllegalStateException {
        super(objClass, registerAsDefault);
    }

    public SymbolAxisReflectiveProxy(Class<A> objClass) throws IllegalArgumentException {
        this(objClass, false);
    }

    /**
	 * Virtual attribute used in constructor
	 */
    public static final String SYMBOLS_VIRTATTR = "symbols";

    @Override
    protected A updateObjectAttribute(A src, String name, String value, Method setter) throws Exception {
        if (SYMBOLS_VIRTATTR.equalsIgnoreCase(name)) return src;
        return super.updateObjectAttribute(src, name, value, setter);
    }

    public static final SymbolAxisReflectiveProxy<SymbolAxis> SYMBOL = new SymbolAxisReflectiveProxy<SymbolAxis>(SymbolAxis.class, true) {

        @Override
        public SymbolAxis createInstance(Element elem) throws Exception {
            final String n = elem.getAttribute(NAME_ATTR), s = elem.getAttribute(SYMBOLS_VIRTATTR);
            final Collection<String> sl = StringUtil.splitString(s, ',');
            final int numSymbols = (null == sl) ? 0 : sl.size();
            final String[] sv = (numSymbols <= 0) ? null : sl.toArray(new String[numSymbols]);
            if ((null == sv) || (sv.length <= 0)) throw new IllegalStateException("createInstance(" + DOMUtils.toString(elem) + ") no '" + SYMBOLS_VIRTATTR + "' specification");
            return new SymbolAxis(n, sv);
        }
    };
}
