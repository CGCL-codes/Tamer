package br.ufmg.saotome.arangi.commons.report;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import br.ufmg.saotome.arangi.dto.QueryField;
import br.ufmg.saotome.arangi.model.data.DataSet;
import br.ufmg.saotome.arangi.model.data.Type;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PDFHelper {

    private static Paragraph NEW_LINE = new Paragraph(" ", new Font(Font.HELVETICA, 12));

    private static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    private static NumberFormat NUMBER_FORMAT = NumberFormat.getInstance();

    {
        NUMBER_FORMAT.setMaximumFractionDigits(2);
        NUMBER_FORMAT.setMinimumFractionDigits(2);
    }

    public static void main(String[] args) throws Exception {
        String title = "aaaaaab";
        List<QueryField[]> listFields = new ArrayList<QueryField[]>();
        List<Type> listColumns = new ArrayList<Type>();
        QueryField[] linha1 = new QueryField[3];
        QueryField l1c1 = new QueryField();
        l1c1.setValue("aab");
        l1c1.setType(Type.TYPE_STRING);
        linha1[0] = l1c1;
        QueryField l1c2 = new QueryField();
        l1c2.setValue("12345");
        l1c2.setType(Type.TYPE_INTEGER);
        linha1[1] = l1c2;
        QueryField l1c3 = new QueryField();
        l1c3.setValue(new Date());
        l1c3.setType(Type.TYPE_TIMESTAMP);
        linha1[2] = l1c3;
        listFields.add(linha1);
        QueryField[] linha2 = new QueryField[3];
        QueryField l2c1 = new QueryField();
        l2c1.setValue("bb");
        l2c1.setType(Type.TYPE_STRING);
        linha2[0] = l2c1;
        QueryField l2c2 = new QueryField();
        l2c2.setValue("22");
        l2c2.setType(Type.TYPE_INTEGER);
        linha2[1] = l2c2;
        QueryField l2c3 = new QueryField();
        double aa = 5.2;
        l2c3.setValue(aa);
        l2c3.setType(Type.TYPE_DOUBLE);
        linha2[2] = l2c3;
        listFields.add(linha2);
        Type coluna1 = new Type();
        coluna1.setName("Col 1");
        listColumns.add(coluna1);
        Type coluna2 = new Type();
        coluna2.setName("Col 2");
        listColumns.add(coluna2);
        DataSet dataSet = new DataSet();
        dataSet.setTitle(title);
        dataSet.setColumnHeaders(listColumns);
        dataSet.setData(listFields);
        ByteArrayOutputStream bytes = createPDF(dataSet);
        FileOutputStream fos = new FileOutputStream("teste.pdf");
        fos.write(bytes.toByteArray());
        System.out.println(bytes.toByteArray().length);
    }

    public static ByteArrayOutputStream createPDF(DataSet dataSet) throws Exception {
        String title = dataSet.getTitle();
        List<Type> listColumns = dataSet.getColumnHeaders();
        List<QueryField[]> listFields = dataSet.getData();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
        try {
            PdfWriter.getInstance(document, bytes);
            document.open();
            setTitle(document, title);
            mountTable(document, listColumns, listFields);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        }
        document.close();
        return bytes;
    }

    private static void setTitle(Document document, String title) throws DocumentException {
        Paragraph pTitle = new Paragraph(title, new Font(Font.HELVETICA, 12, Font.BOLD));
        pTitle.setAlignment(Element.ALIGN_CENTER);
        document.add(pTitle);
        document.add(NEW_LINE);
    }

    private static void mountTable(Document document, List<Type> listColumns, List<QueryField[]> listFields) throws DocumentException {
        PdfPTable table = new PdfPTable(listColumns.size());
        setColumns(table, listColumns);
        setBody(table, listFields);
        document.add(table);
    }

    private static void setColumns(PdfPTable table, List<Type> listColumns) {
        for (Iterator<Type> itListColumns = listColumns.iterator(); itListColumns.hasNext(); ) {
            Type column = itListColumns.next();
            PdfPCell cell = new PdfPCell(new Paragraph(column.getName(), new Font(Font.HELVETICA, 10, Font.BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
        }
    }

    private static void setBody(PdfPTable table, List<QueryField[]> listFields) {
        for (Iterator<QueryField[]> itListFields = listFields.iterator(); itListFields.hasNext(); ) {
            QueryField[] fields = itListFields.next();
            for (int i = 0; i < fields.length; i++) {
                String sValue = "";
                if (fields[i] != null) {
                    sValue = valueToString(fields[i].getValue(), fields[i].getType());
                }
                PdfPCell cell = new PdfPCell(new Paragraph(sValue, FontFactory.getFont(FontFactory.HELVETICA, 8)));
                cell.setHorizontalAlignment(defineAlign(fields[i]));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
            }
        }
    }

    private static String valueToString(Object value, int type) {
        String sValue = "";
        if (value != null) {
            if (type == Type.TYPE_DOUBLE) {
                sValue = NUMBER_FORMAT.format(new Double(value.toString()));
            } else if (type == Type.TYPE_TIMESTAMP) {
                Date date = (Date) value;
                sValue = DATE_FORMAT.format(date);
            } else {
                sValue = value.toString();
            }
        }
        return sValue;
    }

    private static int defineAlign(QueryField field) {
        int align = Element.ALIGN_LEFT;
        if (field != null) {
            if (field.getAlign() != null && !field.getAlign().equals("")) {
                align = parseAlignToInt(field.getAlign());
            } else {
                if (field.getType() != Type.TYPE_STRING) {
                    align = Element.ALIGN_RIGHT;
                }
            }
        }
        return align;
    }

    private static int parseAlignToInt(String sAlign) {
        int align = Element.ALIGN_LEFT;
        if (sAlign != null && sAlign.equals("CENTER")) {
            align = Element.ALIGN_CENTER;
        } else if (sAlign != null && sAlign.equals("RIGHT")) {
            align = Element.ALIGN_RIGHT;
        }
        return align;
    }
}
