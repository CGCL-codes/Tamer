package org.webguitoolkit.ui.util.export;

import java.awt.Color;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.webguitoolkit.ui.base.DataBag;
import org.webguitoolkit.ui.controls.table.IColumnRenderer;
import org.webguitoolkit.ui.controls.table.Table;
import org.webguitoolkit.ui.controls.table.TableColumn;
import org.webguitoolkit.ui.controls.table.renderer.CollectionToStringRenderer;
import org.webguitoolkit.ui.controls.table.renderer.ImageColumnRenderer;
import org.webguitoolkit.ui.controls.util.TextService;
import org.webguitoolkit.ui.controls.util.conversion.IConverter;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

/**
 * converts ISGUIControl Table to HSSF Excel Sheet Assumption: the Default Table Model is in use
 *
 * @author Thorsten Springhart & Lars Br��ler
 *
 */
public class TableExport implements Serializable {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(TableExport.class);

    private HSSFCellStyle excelheadstyle;

    private HSSFCellStyle excelContentstyle;

    private HSSFCellStyle excelDateStyle;

    /**
	 *
	 */
    public TableExport() {
        super();
    }

    /**
	 * @param table
	 * @param sheet
	 * @param footer
	 * @param header
	 * @param headline
	 * @return
	 */
    public HSSFSheet excelExport(Table table, HSSFSheet sheet) {
        HSSFRow row;
        HSSFCell cell;
        TableExportOptions exportOptions = table.getExportOptions();
        List columns = table.getColumns();
        int colNr = columns.size();
        int rownr = 0;
        if (StringUtils.isNotBlank(exportOptions.getHeadline())) {
            row = sheet.createRow(rownr);
            if (getExcelheadstyle() != null) {
                getExcelheadstyle().setWrapText(true);
            }
            double divider = 2;
            double putcol = Math.floor(colNr / divider) - 1;
            short shortCol = (short) putcol;
            cell = row.createCell(shortCol);
            if (getExcelheadstyle() != null) {
                cell.setCellStyle(getExcelheadstyle());
            }
            cell.setCellValue(new HSSFRichTextString(exportOptions.getHeadline()));
            rownr++;
            row = sheet.createRow(rownr);
            cell = row.createCell((short) 0);
            cell.setCellValue(new HSSFRichTextString(""));
            rownr++;
        }
        if (StringUtils.isNotBlank(exportOptions.getTableHeadline())) {
            row = sheet.createRow(rownr);
            getExcelheadstyle().setWrapText(true);
            cell = row.createCell((short) 0);
            cell.setCellValue(new HSSFRichTextString(exportOptions.getTableHeadline()));
            if (getExcelContentstyle() != null) {
                cell.setCellStyle(getExcelContentstyle());
            }
            rownr++;
        }
        row = sheet.createRow(rownr);
        for (int i = 0; i < columns.size(); i++) {
            cell = row.createCell((short) i);
            String tableTitle = TextService.getString(((TableColumn) columns.get(i)).getTitle());
            if (tableTitle.contains("<br/>") || tableTitle.contains("<br>")) {
                tableTitle.replaceAll("<br\\/>", "\\n");
                tableTitle.replaceAll("<br>", "\\n");
            }
            cell.setCellValue(new HSSFRichTextString(tableTitle));
            if (getExcelheadstyle() != null) {
                cell.setCellStyle(getExcelheadstyle());
            }
        }
        rownr++;
        List tabledata = table.getDefaultModel().getFilteredList();
        for (Iterator it = tabledata.iterator(); it.hasNext(); ) {
            row = sheet.createRow(rownr);
            DataBag dbag = (DataBag) it.next();
            for (int i = 0; i < columns.size(); i++) {
                logger.debug("property: " + ((TableColumn) columns.get(i)).getProperty());
                IColumnRenderer renderer = ((TableColumn) columns.get(i)).getRenderer();
                IConverter converter = ((TableColumn) columns.get(i)).getConverter();
                Object obj = null;
                if (renderer != null && renderer instanceof ImageColumnRenderer) {
                    obj = dbag.get(((TableColumn) columns.get(i)).getProperty() + ".title");
                } else if (renderer != null && renderer instanceof CollectionToStringRenderer) {
                    String objString = "";
                    try {
                        ArrayList<String> listOfStrings = (ArrayList<String>) dbag.get(((TableColumn) columns.get(i)).getProperty());
                        for (String string : listOfStrings) {
                            objString += string + ", ";
                        }
                    } catch (ClassCastException e) {
                    }
                    objString = StringUtils.removeEnd(objString, ", ");
                    obj = objString;
                } else {
                    obj = dbag.get(((TableColumn) columns.get(i)).getProperty());
                }
                if (obj != null) {
                    cell = row.createCell((short) i);
                    try {
                        if (obj instanceof String) {
                            String tableData = obj.toString();
                            if (converter != null) {
                                tableData = (String) converter.convert(String.class, obj);
                            }
                            if (tableData.contains("<br/>") || tableData.contains("<br>")) {
                                tableData.replaceAll("<br\\/>", "\\/");
                                tableData.replaceAll("<br>", "\\/");
                            }
                            tableData = StringUtils.replace(tableData, "&nbsp;", " ");
                            String imgOpenTag = "<img";
                            String imgCloseTag = ">";
                            while (StringUtils.contains(tableData, imgOpenTag) && StringUtils.contains(tableData, imgCloseTag)) {
                                tableData = StringUtils.remove(tableData, imgOpenTag + StringUtils.substringBetween(tableData, imgOpenTag, imgCloseTag) + imgCloseTag);
                            }
                            cell.setCellValue(new HSSFRichTextString(tableData));
                            if (getExcelContentstyle() != null) {
                                cell.setCellStyle(getExcelContentstyle());
                            }
                        } else if (obj instanceof Number) {
                            Double number = null;
                            if (obj instanceof Integer) {
                                number = new Double(((Integer) obj).doubleValue());
                            } else if (obj instanceof Long) {
                                number = new Double(((Long) obj).doubleValue());
                            } else if (obj instanceof Short) {
                                number = new Double(((Short) obj).doubleValue());
                            } else if (obj instanceof Float) {
                                number = new Double(((Float) obj).doubleValue());
                            } else if (obj instanceof Double) {
                                number = (Double) obj;
                            } else if (obj instanceof BigDecimal) {
                                number = ((BigDecimal) obj).doubleValue();
                            }
                            if (number != null) cell.setCellValue(number.doubleValue());
                        } else if (obj instanceof Date || obj instanceof java.sql.Date || obj instanceof Timestamp) {
                            if (converter != null) {
                                String tobj = (String) converter.convert(String.class, obj);
                                cell.setCellValue(tobj);
                            } else {
                                cell.setCellValue((Date) obj);
                            }
                            if (getExcelDateStyle() != null) {
                                cell.setCellStyle(getExcelDateStyle());
                            }
                        } else {
                            String cellData = obj.toString();
                            if (converter != null) {
                                cellData = (String) converter.convert(String.class, obj);
                            }
                            if (cellData == null) {
                                cellData = "";
                            }
                            cell.setCellValue(new HSSFRichTextString(cellData));
                            if (getExcelContentstyle() != null) {
                                cell.setCellStyle(getExcelContentstyle());
                            }
                        }
                    } catch (Exception e) {
                        logger.error("row = " + rownr + ", column = " + i + ", error " + e.getMessage(), e);
                        cell.setCellValue(new HSSFRichTextString(""));
                    }
                }
            }
            rownr++;
        }
        if (StringUtils.isNotBlank(exportOptions.getTableFooter())) {
            row = sheet.createRow(rownr);
            cell = row.createCell((short) 0);
            cell.setCellValue(new HSSFRichTextString(""));
            rownr++;
            row = sheet.createRow(rownr);
            getExcelheadstyle().setWrapText(true);
            cell = row.createCell((short) 0);
            cell.setCellValue(new HSSFRichTextString(exportOptions.getTableFooter()));
            if (getExcelContentstyle() != null) {
                cell.setCellStyle(getExcelContentstyle());
            }
        }
        return sheet;
    }

    /**
	 * @param table
	 * @param sw
	 * @return
	 * @throws XMLStreamException
	 */
    public XMLStreamWriter xmlExport(Table table, XMLStreamWriter sw) throws XMLStreamException {
        logger.debug("export table " + table.getTitle() + " to xml");
        List columns = table.getColumns();
        List tabledata = table.getDefaultModel().getFilteredList();
        int rownr = 1;
        for (Iterator it = tabledata.iterator(); it.hasNext(); ) {
            sw.writeStartElement("row");
            sw.writeAttribute("rownumber", String.valueOf(rownr));
            DataBag dbag = (DataBag) it.next();
            for (int i = 0; i < columns.size(); i++) {
                Object obj = dbag.get(((TableColumn) columns.get(i)).getProperty());
                if (obj != null) {
                    sw.writeStartElement(((TableColumn) columns.get(i)).getTitle().replaceAll(" ", "_"));
                    sw.writeCharacters(obj.toString());
                    sw.writeEndElement();
                }
            }
            sw.writeEndElement();
            rownr++;
        }
        return sw;
    }

    /**
	 * @param table
	 * @param footer
	 * @param header
	 * @return
	 */
    public PdfPTable pdfExport(Table table) {
        TableExportOptions exportOptions = table.getExportOptions();
        Font headfont = new Font(Font.UNDEFINED, Font.DEFAULTSIZE, Font.BOLD);
        Font bodyfont = new Font(exportOptions.getPdfFontSize(), exportOptions.getPdfFontSize(), Font.NORMAL);
        if (exportOptions.getPdfFontColour() != null) {
            Color fontColor = exportOptions.getPdfFontColour();
            bodyfont.setColor(fontColor);
            headfont.setColor(fontColor);
        }
        List columns = table.getColumns();
        PdfPTable resulttable = new PdfPTable(columns.size());
        resulttable.setWidthPercentage(100f);
        if (StringUtils.isNotEmpty(exportOptions.getTableHeadline())) {
            PdfPCell cell = new PdfPCell(new Paragraph(exportOptions.getTableHeadline()));
            cell.setColspan(columns.size());
            cell.setBackgroundColor(Color.lightGray);
            resulttable.addCell(cell);
        }
        for (int i = 0; i < columns.size(); i++) {
            String cellData = TextService.getString(((TableColumn) columns.get(i)).getTitle());
            if (cellData.contains("<br/>") || cellData.contains("<br>")) {
                cellData = cellData.replaceAll("<br\\/>", "\\/");
                cellData = cellData.replaceAll("<br>", "\\/");
            }
            PdfPCell cell = new PdfPCell(new Paragraph((cellData), headfont));
            cell.setBackgroundColor(Color.lightGray);
            resulttable.addCell(cell);
        }
        List tabledata = table.getDefaultModel().getFilteredList();
        if (tabledata.isEmpty()) {
            for (int i = 0; i < columns.size(); i++) {
                resulttable.addCell(new PdfPCell((new Phrase(""))));
            }
        }
        for (Iterator it = tabledata.iterator(); it.hasNext(); ) {
            DataBag dbag = (DataBag) it.next();
            PdfPCell cell;
            for (int i = 0; i < columns.size(); i++) {
                logger.debug("property: " + ((TableColumn) columns.get(i)).getProperty());
                IColumnRenderer renderer = ((TableColumn) columns.get(i)).getRenderer();
                Converter converter = ((TableColumn) columns.get(i)).getConverter();
                Object obj = null;
                if (renderer != null && renderer instanceof ImageColumnRenderer) {
                    obj = dbag.get(((TableColumn) columns.get(i)).getProperty() + ".title");
                } else if (renderer != null && renderer instanceof CollectionToStringRenderer) {
                    String objString = "";
                    try {
                        ArrayList<String> listOfStrings = (ArrayList<String>) dbag.get(((TableColumn) columns.get(i)).getProperty());
                        for (String string : listOfStrings) {
                            objString += string + ", ";
                        }
                    } catch (ClassCastException e) {
                    }
                    objString = StringUtils.removeEnd(objString, ", ");
                    obj = objString;
                }
                if (obj == null) obj = dbag.get(((TableColumn) columns.get(i)).getProperty());
                String cellData = "";
                if (obj != null) {
                    cellData = obj.toString();
                    if (converter != null) {
                        if (obj instanceof Timestamp) {
                            cellData = (String) converter.convert(String.class, obj);
                        } else if (obj instanceof Date) {
                            cellData = (String) converter.convert(String.class, obj);
                        } else if (obj instanceof java.sql.Date) {
                            cellData = (String) converter.convert(String.class, obj);
                        } else if (obj instanceof String) {
                            cellData = obj.toString();
                        } else {
                            cellData = obj.toString();
                        }
                    }
                    if (cellData.contains("<br/>") || cellData.contains("<br>")) {
                        cellData = cellData.replace("<br\\/>", "\\/");
                        cellData = cellData.replace("<br>", "\\/");
                    }
                    cell = new PdfPCell(new Paragraph((cellData), bodyfont));
                } else {
                    cell = new PdfPCell(new Phrase(""));
                }
                resulttable.addCell(cell);
            }
        }
        if (StringUtils.isNotEmpty(exportOptions.getTableFooter())) {
            PdfPCell cell = new PdfPCell(new Paragraph(exportOptions.getTableFooter()));
            cell.setColspan(columns.size());
            cell.setBackgroundColor(Color.lightGray);
            resulttable.addCell(cell);
        }
        resulttable.setHeaderRows(1);
        return resulttable;
    }

    public HSSFCellStyle getExcelheadstyle() {
        return excelheadstyle;
    }

    public void setExcelheadstyle(HSSFCellStyle headstyle) {
        this.excelheadstyle = headstyle;
    }

    public HSSFCellStyle getExcelContentstyle() {
        return excelContentstyle;
    }

    public void setExcelContentstyle(HSSFCellStyle contentstyle) {
        this.excelContentstyle = contentstyle;
    }

    public void setExcelDateStyle(HSSFCellStyle excelDateStyle) {
        this.excelDateStyle = excelDateStyle;
    }

    public HSSFCellStyle getExcelDateStyle() {
        return excelDateStyle;
    }
}
