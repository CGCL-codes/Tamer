package at.ac.tuwien.vitalab.hrcrm.xsl.party;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.view.document.AbstractExcelView;
import at.ac.tuwien.vitalab.hrcrm.adaptor.PartyAdaptor;
import at.ac.tuwien.vitalab.hrcrm.dto.party.PartyType;

public class PartyXslView<T> extends AbstractExcelView {

    private PartyAdaptor<T> partyAdaptor;

    @Override
    @SuppressWarnings("unchecked")
    protected void buildExcelDocument(Map model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<PartyType> partyTypes = (List<PartyType>) model.get("party.xsl");
        if (partyTypes.size() == 0) throw new IllegalArgumentException("Unable to generate pdf file. Dadabase is empty!");
        T object = this.partyAdaptor.partyType2Party(partyTypes.get(0));
        HSSFSheet sheet = workbook.createSheet("Availabvle Adresses");
        int rowNum = 0;
        rowNum = this.generateTableHeader(workbook, object, sheet, rowNum);
        for (int i = 0; i < partyTypes.size(); i++) {
            object = this.partyAdaptor.partyType2Party(partyTypes.get(i));
            Field[] fields = object.getClass().getDeclaredFields();
            for (int j = 0; j < fields.length; j++) {
                Field field = fields[j];
                field.setAccessible(true);
                HSSFRow row = sheet.createRow(rowNum);
                HSSFCell cell = row.createCell((short) j);
                cell.setCellValue(String.valueOf(field.get(object)));
            }
            rowNum++;
        }
    }

    private int generateTableHeader(final HSSFWorkbook workbook, final T object, final HSSFSheet sheet, int rowNum) throws Exception {
        Field[] fields = object.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            HSSFRow row = sheet.createRow(rowNum);
            HSSFCell cell = row.createCell((short) i);
            cell.setCellValue(field.getName().toUpperCase());
            HSSFCellStyle style = workbook.createCellStyle();
            style.setFillBackgroundColor(HSSFColor.AQUA.index);
            style.setFillPattern(HSSFCellStyle.BIG_SPOTS);
            cell.setCellStyle(style);
        }
        return ++rowNum;
    }

    /**
	 * Write access to private field.
	 * @param partyAdaptor
	 *            The partyAdaptor to set.
	 */
    @Required
    public void setPartyAdaptor(final PartyAdaptor<T> partyAdaptor) {
        this.partyAdaptor = partyAdaptor;
    }
}
