package org.netxilia.functions;

import java.util.Iterator;
import org.netxilia.api.display.Styles;
import org.netxilia.api.value.IGenericValue;
import org.netxilia.api.value.RichValue;
import org.netxilia.spi.formula.Function;
import org.netxilia.spi.formula.Functions;

/**
 * These are different functions working with rich values
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
@Functions
public class RichValueFunctions {

    public String STYLE(RichValue rv) {
        return rv.getStyles() != null ? rv.getStyles().toString() : "";
    }

    /**
	 * don't know how to store RichValues so recalculate them each time the method is like STYLEDECODE(value, v1,
	 * style1, v2, style2, ... defaultStyle);
	 * 
	 * @param value
	 * @param valueAndStyle
	 * @return
	 */
    @Function(cacheable = false)
    public IGenericValue STYLEDECODE(IGenericValue value, Iterator<IGenericValue> valueAndStyle) {
        String style = decode(value, valueAndStyle, true);
        if (style == null) {
            return value;
        }
        return new RichValue(value, Styles.styles(style));
    }

    @Function(cacheable = false)
    public IGenericValue STYLEINTERVALS(IGenericValue value, Iterator<IGenericValue> valueAndStyle) {
        String style = decode(value, valueAndStyle, false);
        if (style == null) {
            return value;
        }
        return new RichValue(value, Styles.styles(style));
    }

    public String DISPLAY(RichValue rv) {
        return rv.getDisplay() != null ? rv.getDisplay() : "";
    }

    @Function(cacheable = false)
    public IGenericValue DISPLAYDECODE(IGenericValue value, Iterator<IGenericValue> valueAndDisplay) {
        String display = decode(value, valueAndDisplay, true);
        if (display == null) {
            return value;
        }
        return new RichValue(value, display);
    }

    @Function(cacheable = false)
    public IGenericValue DISPLAYINTERVALS(IGenericValue value, Iterator<IGenericValue> valueAndDisplay) {
        String display = decode(value, valueAndDisplay, false);
        if (display == null) {
            return value;
        }
        return new RichValue(value, display);
    }

    private String decode(IGenericValue value, Iterator<IGenericValue> decodeInfo, boolean strictEqual) {
        String code = null;
        IGenericValue testValue = null;
        while (decodeInfo.hasNext()) {
            IGenericValue elem = decodeInfo.next();
            if (decodeInfo.hasNext()) {
                testValue = elem;
                code = decodeInfo.next().getStringValue();
            } else {
                code = elem.getStringValue();
                break;
            }
            int cmp = testValue.compareTo(value);
            if (cmp == 0 && strictEqual || cmp >= 0 && !strictEqual) {
                break;
            }
        }
        return code;
    }
}
