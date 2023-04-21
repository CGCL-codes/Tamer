package net.sf.jzeno.echo.viewer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.sf.jzeno.echo.EchoSupport;
import net.sf.jzeno.echo.components.CompoundComponent;
import net.sf.jzeno.echo.databinding.DynaLabel;
import nextapp.echo.EchoConstants;
import nextapp.echo.Font;
import org.apache.log4j.Logger;

/**
 * @author ddhondt
 * 
 * Viewer for Longs. Right aligns the content
 */
public class DateViewer extends CompoundComponent {

    private static final long serialVersionUID = 1L;

    protected static Logger log = Logger.getLogger(DateViewer.class);

    private String formatString = "dd-MMM-yyyy HH:mm";

    private DynaLabel dynaLabel = null;

    private static Map formatStringToFormatter = new HashMap();

    public DateViewer(Class beanClass, String property, String constructionHints) {
        super(beanClass, property, "");
        dynaLabel = new DynaLabel(getClass(), "date", "");
        dynaLabel.setHorizontalAlignment(EchoConstants.RIGHT);
        add(dynaLabel);
        EchoSupport.executeHints(this, constructionHints);
    }

    public String getFormatString() {
        return formatString;
    }

    public void setFormatString(String formatString) {
        this.formatString = formatString;
    }

    public String getDate() {
        String ret = "";
        Object value = getValue();
        if (value != null) {
            if (value instanceof Date) {
                SimpleDateFormat format = null;
                format = (SimpleDateFormat) formatStringToFormatter.get(formatString);
                if (format == null) {
                    format = new SimpleDateFormat(formatString);
                    formatStringToFormatter.put(formatString, format);
                }
                Date utcDate = (Date) value;
                ret = format.format(utcDate);
            } else {
                ret = value.toString();
            }
        }
        return ret;
    }

    public void setFont(Font newValue) {
        if (dynaLabel != null) {
            dynaLabel.setFont(newValue);
        }
    }
}
