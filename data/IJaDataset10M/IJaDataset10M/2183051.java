package at.langegger.xlwrap.spreadsheet.opendoc;

import java.text.DateFormat;
import java.text.NumberFormat;
import org.jopendocument.dom.spreadsheet.CellStyle;
import at.langegger.xlwrap.spreadsheet.FormatAnnotation;

/**
 * @author dorgon
 *
 */
public class OpenDocumentFormat implements FormatAnnotation {

    private final CellStyle style;

    /**
	 * @param style
	 */
    public OpenDocumentFormat(CellStyle style) {
        this.style = style;
    }

    @Override
    public DateFormat getDateFormat() {
        return null;
    }

    @Override
    public NumberFormat getNumberFormat() {
        return null;
    }

    @Override
    public boolean isDateFormat() {
        return false;
    }

    @Override
    public boolean isNumberFormat() {
        return false;
    }

    /**
	 * @return the style
	 */
    public CellStyle getStyle() {
        return style;
    }
}
