package net.community.chest.jfree.jfreechart.chart.renderer;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Locale;
import net.community.chest.BaseTypedValuesContainer;
import net.community.chest.dom.transform.XmlValueInstantiator;
import net.community.chest.text.DecimalFormatValueStringInstantiator;
import net.community.chest.text.SimpleDateFormatValueStringInstantiator;
import net.community.chest.util.locale.LocaleValueInstantiator;
import org.w3c.dom.Element;

/**
 * <P>Copyright GPLv2</P>
 *
 * @param <V> Type being converted
 * @author Lyor G.
 * @since Jun 8, 2009 12:27:51 PM
 */
public abstract class BaseGeneratorConverter<V> extends BaseTypedValuesContainer<V> implements XmlValueInstantiator<V> {

    protected BaseGeneratorConverter(Class<V> objClass) throws IllegalArgumentException {
        super(objClass);
    }

    protected Locale getLocale(Element elem, String attrName) throws Exception {
        final String lcl = ((null == elem) || (null == attrName) || (attrName.length() <= 0)) ? null : elem.getAttribute(attrName);
        if ((null == lcl) || (lcl.length() <= 0)) return null;
        return LocaleValueInstantiator.DEFAULT.newInstance(lcl);
    }

    public Locale getLocale(Element elem) throws Exception {
        return getLocale(elem, "locale");
    }

    protected NumberFormat getNumberFormat(Element elem, String attrName) throws Exception {
        final String fmt = ((null == elem) || (null == attrName) || (attrName.length() <= 0)) ? null : elem.getAttribute(attrName);
        if ((null == fmt) || (fmt.length() <= 0)) return null;
        if ("default".equalsIgnoreCase(fmt)) return NumberFormat.getInstance();
        return DecimalFormatValueStringInstantiator.DEFAULT.newInstance(fmt);
    }

    public NumberFormat getNumberFormat(Element elem) throws Exception {
        return getNumberFormat(elem, "number");
    }

    public NumberFormat getPercentFormat(Element elem) throws Exception {
        return getNumberFormat(elem, "percent");
    }

    protected DateFormat getDateFormat(Element elem, String attrName) throws Exception {
        final String fmt = ((null == elem) || (null == attrName) || (attrName.length() <= 0)) ? null : elem.getAttribute(attrName);
        if ((null == fmt) || (fmt.length() <= 0)) return null;
        if ("default".equalsIgnoreCase(fmt)) return DateFormat.getDateTimeInstance();
        return SimpleDateFormatValueStringInstantiator.DEFAULT.newInstance(fmt);
    }

    public DateFormat getDateFormat(Element elem) throws Exception {
        return getDateFormat(elem, "date");
    }

    public String getLabelFormat(Element elem) throws Exception {
        return (null == elem) ? null : elem.getAttribute("format");
    }
}
