package org.apache.poi.hssf.usermodel;

import org.apache.poi.hssf.record.HeaderRecord;
import org.apache.poi.hssf.record.aggregates.PageSettingsBlock;
import org.apache.poi.ss.usermodel.Header;

/**
 * Class to read and manipulate the header.
 * <P>
 * The header works by having a left, center, and right side.  The total cannot
 * be more that 255 bytes long.  One uses this class by getting the HSSFHeader
 * from HSSFSheet and then getting or setting the left, center, and right side.
 * For special things (such as page numbers and date), one can use a the methods
 * that return the characters used to represent these.  One can also change the
 * fonts by using similar methods.
 * <P>
 *
 * @author Shawn Laubach (slaubach at apache dot org)
 */
public final class HSSFHeader extends HeaderFooter implements Header {

    private final PageSettingsBlock _psb;

    protected HSSFHeader(PageSettingsBlock psb) {
        _psb = psb;
    }

    protected String getRawText() {
        HeaderRecord hf = _psb.getHeader();
        if (hf == null) {
            return "";
        }
        return hf.getText();
    }

    @Override
    protected void setHeaderFooterText(String text) {
        HeaderRecord hfr = _psb.getHeader();
        if (hfr == null) {
            hfr = new HeaderRecord(text);
            _psb.setHeader(hfr);
        } else {
            hfr.setText(text);
        }
    }
}
