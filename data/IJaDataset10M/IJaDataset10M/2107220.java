package net.sourceforge.olduvai.lrac.genericdataservice;

import java.awt.Color;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * Optional time-saving class which provides some routines for dealing with 
 * Horrible Spread Sheet Format (HSSF) and some basic fonts.  
 * 
 * Implementers are welcome to simply implement ExcelExportInterface directly
 * rather than using this abstract class if it does not provide useful functionality
 * for their purposes.  
 * 
 * @author peter
 *
 */
public abstract class AbstractExcelExport implements ExcelExportInterface {

    protected static final int ONECHARWIDTH = 256;

    HSSFFont boldFont;

    HSSFFont boldUnderlineFont;

    HSSFFont underLineFont;

    HSSFFont titleFont;

    HSSFCellStyle dateStyle;

    HSSFWorkbook wb;

    final HSSFRichTextString hustr(String str) {
        HSSFRichTextString boldedString = new HSSFRichTextString(str);
        boldedString.applyFont(underLineFont);
        return boldedString;
    }

    final HSSFRichTextString hubstr(String str) {
        HSSFRichTextString boldedString = new HSSFRichTextString(str);
        boldedString.applyFont(boldUnderlineFont);
        return boldedString;
    }

    final HSSFRichTextString hbstr(String str) {
        HSSFRichTextString boldedString = new HSSFRichTextString(str);
        boldedString.applyFont(boldFont);
        return boldedString;
    }

    /**
	 * Wrapper to make making rich text strings faster
	 * @param str
	 * @return
	 */
    static final HSSFRichTextString hstr(String str) {
        return new HSSFRichTextString(str);
    }

    /**
	 * Workaround for stupidity of not accepting Java Color objects.  Who are these clowns? 
	 * @param col
	 * @return
	 */
    static final short colorToHSSFShort(Color col) {
        if (col.equals(Color.white)) {
            return HSSFColor.WHITE.index;
        } else if (col.equals(Color.red)) {
            return HSSFColor.RED.index;
        } else if (col.equals(Color.black)) {
            return HSSFColor.BLACK.index;
        } else if (col.equals(Color.orange)) {
            return HSSFColor.ORANGE.index;
        } else if (col.equals(Color.yellow)) {
            return HSSFColor.YELLOW.index;
        } else if (col.equals(Color.blue)) {
            return HSSFColor.BLUE.index;
        } else if (col.equals(Color.green)) {
            return HSSFColor.GREEN.index;
        } else if (col.equals(Color.lightGray)) {
            return HSSFColor.GREY_40_PERCENT.index;
        }
        return HSSFColor.BLACK.index;
    }

    /**
	 * Initializes the workbook wb and all of the fonts.  
	 */
    public AbstractExcelExport() {
        wb = new HSSFWorkbook();
        titleFont = wb.createFont();
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        boldFont = wb.createFont();
        boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        boldUnderlineFont = wb.createFont();
        boldUnderlineFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        boldUnderlineFont.setUnderline(HSSFFont.U_SINGLE);
        underLineFont = wb.createFont();
        underLineFont.setUnderline(HSSFFont.U_SINGLE);
        dateStyle = wb.createCellStyle();
        dateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
    }

    /**
	 * Retrieve the instantiated workbook
	 * @return
	 */
    public HSSFWorkbook getWorkbook() {
        return wb;
    }
}
