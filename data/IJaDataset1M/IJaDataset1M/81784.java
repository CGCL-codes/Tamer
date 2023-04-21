package support.pdfHandle.bean;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import domain.specification.Parameter;
import domain.specification.Sheet;

public class PdfHandleBean {

    @SuppressWarnings("unchecked")
    public void buildSheetPdf(Document document, Map model) {
        try {
            String aburl = (String) model.get("aburl");
            Sheet sheet = (Sheet) model.get("sheet");
            String freqValue = "";
            String esrValue = "";
            document.addAuthor("author:" + replaceStr(sheet.getCreateUser().getUsername()));
            document.addSubject("subject");
            Image headerImage = Image.getInstance(aburl + File.separator + "pdfHead.PNG");
            headerImage.scaleAbsoluteWidth(500);
            headerImage.scaleAbsoluteHeight(55);
            HeaderFooter header = new HeaderFooter(new Phrase(new Chunk(headerImage, 0, -10)), false);
            header.setBorder(0);
            header.setAlignment(Paragraph.ALIGN_CENTER);
            document.setHeader(header);
            Image footerImage = Image.getInstance(aburl + File.separator + "pdfFooter.PNG");
            footerImage.scaleAbsoluteWidth(300);
            footerImage.scaleAbsoluteHeight(34);
            HeaderFooter footer = new HeaderFooter(new Phrase(new Chunk(footerImage, 0, 0)), false);
            footer.setBorder(0);
            footer.setAlignment(Paragraph.ALIGN_CENTER);
            document.setFooter(footer);
            document.open();
            document.newPage();
            Map parameterMap = (Map) model.get("parameter" + sheet.getId());
            Map specificationMap = (Map) model.get("specification" + sheet.getId());
            float[] widths = { 0.4f, 0.15f, 0.15f, 0.15f, 0.15f };
            Table table = new Table(5);
            table.setWidth(82f);
            table.setWidths(widths);
            table.setBorderWidth(1);
            table.setBorderColor(Color.BLACK);
            table.setPadding(3);
            table.addCell(new Paragraph("Parameter", getFont(11, Font.NORMAL)));
            table.addCell(new Paragraph("Symbol", getFont(11, Font.NORMAL)));
            table.addCell(new Paragraph("Condition", getFont(11, Font.NORMAL)));
            table.addCell(new Paragraph("Specification", getFont(11, Font.NORMAL)));
            table.addCell(new Paragraph("Unit", getFont(11, Font.NORMAL)));
            table.endHeaders();
            Iterator parameterIterator = parameterMap.keySet().iterator();
            while (parameterIterator.hasNext()) {
                String key = (String) parameterIterator.next();
                Parameter parameter = (Parameter) parameterMap.get(key);
                String specificationName = (String) specificationMap.get(key);
                if (specificationName == null) {
                    specificationName = "";
                }
                if (parameter.getParameterType() == 1) {
                    freqValue = freqValue + " " + specificationName + " " + parameter.getUnit();
                }
                if (parameter.getParameterType() == 2) {
                    esrValue = esrValue + " " + specificationName;
                }
                table.addCell(new Paragraph(parameter.getParameterName(), getFont(11, Font.NORMAL)));
                if (StringUtils.isNotEmpty(parameter.getOther())) {
                    Cell cell = new Cell(new Paragraph(replaceStr(parameter.getOther()), getFont(11, Font.NORMAL)));
                    cell.setColspan(4);
                    table.addCell(cell);
                } else {
                    table.addCell(new Paragraph(replaceStr(parameter.getSymbol()), getFont(11, Font.NORMAL)));
                    table.addCell(new Paragraph(replaceStr(parameter.getCond()), getFont(11, Font.NORMAL)));
                    table.addCell(new Paragraph(replaceStr(specificationName), getFont(11, Font.NORMAL)));
                    table.addCell(new Paragraph(replaceStr(parameter.getUnit()), getFont(11, Font.NORMAL)));
                }
            }
            Paragraph appSheet = new Paragraph("APPROVAL SHEET", getFont(16, Font.BOLD));
            appSheet.setAlignment(Paragraph.ALIGN_CENTER);
            String dateStr = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH).format(sheet.getSheetCreatedate());
            Paragraph date = new Paragraph("DATE : " + dateStr, getFont(11, Font.NORMAL));
            date.setIndentationRight(54f);
            date.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(appSheet);
            document.add(date);
            document.add(new Paragraph(" "));
            Paragraph customerParagraph = new Paragraph("CUSTOMER : " + replaceStr(sheet.getCustomer().getUserName()), getFont(11, Font.NORMAL));
            customerParagraph.setIndentationLeft(54f);
            document.add(customerParagraph);
            document.add(new Paragraph(" "));
            Paragraph productName = new Paragraph("PRODUCT NAME : " + replaceStr(sheet.getProductName()), getFont(11, Font.NORMAL));
            productName.setIndentationLeft(54f);
            document.add(productName);
            document.add(new Paragraph(" "));
            Paragraph specificationVersion = new Paragraph("SPECIFICATION VERSION : " + replaceStr(sheet.getSpecificationVersion()), getFont(11, Font.NORMAL));
            specificationVersion.setIndentationLeft(54f);
            document.add(specificationVersion);
            document.add(new Paragraph(" "));
            Paragraph ittiPartNo = new Paragraph("ITTI PART NO.                                                                                       CUSTOMER PART NO. ", getFont(11, Font.NORMAL));
            ittiPartNo.setIndentationLeft(54f);
            document.add(ittiPartNo);
            document.add(new Paragraph("                                                                                                                                       " + sheet.getCustomerPartNo()));
            Paragraph ittip = new Paragraph("1.   " + replaceStr(sheet.getIttiPartNo()), getFont(11, Font.NORMAL));
            ittip.setIndentationLeft(54f);
            document.add(ittip);
            String dimentsions = replaceStr(sheet.getDimensions());
            if (dimentsions == null) dimentsions = "";
            String productType = replaceStr(sheet.getProductType());
            if (productType == null) productType = "";
            Paragraph otherInfo = new Paragraph("     " + freqValue + " / " + dimentsions + " / " + productType, getFont(11, Font.NORMAL));
            otherInfo.setIndentationLeft(54f);
            document.add(otherInfo);
            Paragraph otherInfo2 = new Paragraph("     " + esrValue, getFont(11, Font.NORMAL));
            otherInfo2.setIndentationLeft(54f);
            document.add(otherInfo2);
            Image image = Image.getInstance(aburl + File.separator + "rosh.PNG");
            image.scaleAbsoluteWidth(100);
            image.scaleAbsoluteHeight(40);
            image.setIndentationLeft(65);
            document.add(image);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            Image seal = Image.getInstance(aburl + File.separator + "seal.PNG");
            seal.scaleAbsoluteWidth(300);
            seal.scaleAbsoluteHeight(64);
            seal.setIndentationLeft(140);
            document.add(seal);
            Paragraph by = new Paragraph("PREPARED BY:________________                     CONFIRMED BY:_______________", getFont(11, Font.NORMAL));
            by.setIndentationLeft(54f);
            document.add(by);
            document.add(new Paragraph(" "));
            Paragraph customerApp = new Paragraph("CUSTOMER APPROVAL:", getFont(11, Font.NORMAL));
            customerApp.setIndentationLeft(54f);
            document.add(customerApp);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            Paragraph checkAppoverBy = new Paragraph("CHECKED BY:________________                         APPROVED BY:_______________", getFont(11, Font.NORMAL));
            checkAppoverBy.setIndentationLeft(54f);
            document.add(checkAppoverBy);
            document.newPage();
            Paragraph specification = new Paragraph("SPECIFICATION", getFont(16, Font.BOLD));
            specification.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(specification);
            Paragraph customer = new Paragraph("CUSTOMER : " + replaceStr(sheet.getCustomer().getUserName()), getFont(11, Font.NORMAL));
            customer.setIndentationLeft(50f);
            Paragraph pkg = new Paragraph("PACKAGE : " + replaceStr(sheet.getProductPackage()) + "  " + sheet.getDimensions(), getFont(11, Font.NORMAL));
            pkg.setIndentationLeft(50f);
            Paragraph partNo = new Paragraph("ITTI PART NO. : " + replaceStr(sheet.getIttiPartNo()), getFont(11, Font.NORMAL));
            partNo.setIndentationLeft(50f);
            Paragraph customerPartNo = new Paragraph("CUSTOMER PART NO. : " + replaceStr(sheet.getCustomerPartNo()), getFont(11, Font.NORMAL));
            customerPartNo.setIndentationLeft(50f);
            Paragraph characteristics = new Paragraph("ELECTRICAL CHARACTERISTICS : ", getFont(11, Font.NORMAL));
            characteristics.setIndentationLeft(50f);
            document.add(customer);
            document.add(pkg);
            document.add(partNo);
            document.add(customerPartNo);
            document.add(characteristics);
            document.add(table);
            Paragraph sdate = new Paragraph("Date of specification : " + dateStr, getFont(11, Font.NORMAL));
            sdate.setIndentationLeft(50f);
            document.add(sdate);
        } catch (Exception e) {
            System.out.println("error" + e.getMessage());
        }
        document.close();
    }

    public String replaceStr(String str) {
        return str.replaceAll("º", "°");
    }

    public Font getFont(int size, int style) throws DocumentException, IOException {
        BaseFont bfChinese = BaseFont.createFont("ARIAL.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        Font font = new Font(bfChinese, size, style, new Color(0, 0, 0));
        return font;
    }
}
