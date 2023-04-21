package com.servengine.ecommerce;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.transaction.UserTransaction;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.servengine.ecommerce.ejb.EcommerceManagerLocal;
import com.servengine.ecommerce.ejb.Invoice;
import com.servengine.ecommerce.ejb.PurchaseOrder;
import com.servengine.ecommerce.ejb.PurchaseOrderItem;
import com.servengine.jsf.ServengineViewSupport;
import com.servengine.portal.Portal;
import com.servengine.portal.PortalManagerLocal;

@Named
@SessionScoped
public class InvoicesAdminView extends ServengineViewSupport {

    private static final long serialVersionUID = 1616872093417955384L;

    Logger logger = java.util.logging.Logger.getLogger(InvoicesAdminView.class.getName());

    private Invoice expurgandaInvoice;

    @EJB
    private transient EcommerceManagerLocal ecommerceManager;

    @EJB
    private transient PortalManagerLocal portalManager;

    private List<Invoice> invoices;

    private Invoice[] selectedInvoices;

    private Date datePaid;

    public static SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");

    public float getNetTotal() {
        float netTotal = 0;
        if (invoices != null) for (Invoice invoice : invoices) netTotal += invoice.getNetTotal();
        return netTotal;
    }

    public float getGrossTotal() {
        float netTotal = 0;
        if (invoices != null) for (Invoice invoice : invoices) netTotal += invoice.getTotal();
        return netTotal;
    }

    public void setSelectedAsPaid() {
        if (datePaid == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(getText("dataSaved")));
            return;
        }
        for (Invoice invoice : selectedInvoices) {
            invoice.setDatePaid(datePaid);
            ecommerceManager.merge(invoice);
        }
    }

    public String findAll() {
        invoices = ecommerceManager.getInvoices(getUserSession().getPortal());
        return "invoices";
    }

    public String findUnpaid() {
        invoices = ecommerceManager.getUnpaidInvoices(getUserSession().getPortal());
        return "invoices";
    }

    public List<Invoice> getInvoices() {
        if (invoices == null) invoices = ecommerceManager.getInvoices(getUserSession().getPortal());
        return invoices;
    }

    public InvoiceDataModel getInvoicesModel() {
        return new InvoiceDataModel(getInvoices());
    }

    public Invoice getExpurgandaInvoice() {
        return expurgandaInvoice;
    }

    public void setExpurgandaInvoice(Invoice invoiceExpurganda) {
        this.expurgandaInvoice = invoiceExpurganda;
    }

    public String removeInvoice() {
        ecommerceManager.removeInvoice(expurgandaInvoice);
        return "invoices";
    }

    public Invoice[] getSelectedInvoices() {
        return selectedInvoices;
    }

    public void setSelectedInvoices(Invoice[] selectedInvoices) {
        this.selectedInvoices = selectedInvoices;
    }

    public StreamedContent getAsPDF() throws IOException {
        Portal portal = getUserSession().getPortal();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Locale portallocale = new Locale("es", "ES");
        NumberFormat currencyformat = NumberFormat.getCurrencyInstance(portallocale);
        NumberFormat percentformat = NumberFormat.getPercentInstance(portallocale);
        String pdfFileName = selectedInvoices.length == 1 ? selectedInvoices[0].getId() : "invoices";
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, output);
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            for (Invoice invoice : selectedInvoices) {
                document.newPage();
                float[] widths = { 0.16f, 0.37f, 0.07f, 0.15f, 0.15f };
                PdfPTable items = new PdfPTable(widths);
                items.setWidthPercentage(100);
                Font mainfont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
                Font mainfontbold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                Font smallfont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
                PdfPCell cell = new PdfPCell(new Phrase("Referencia", mainfont));
                cell.setBackgroundColor(new BaseColor(153, 204, 255));
                cell.setPadding(3f);
                cell.setBorder(Rectangle.NO_BORDER);
                items.addCell(cell);
                cell = new PdfPCell(new Phrase("Descripción", mainfont));
                cell.setPadding(3f);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(new BaseColor(153, 204, 255));
                items.addCell(cell);
                cell = new PdfPCell(new Phrase("Cant.", mainfont));
                cell.setPadding(3f);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(new BaseColor(153, 204, 255));
                items.addCell(cell);
                cell = new PdfPCell(new Phrase("Precio", mainfont));
                cell.setPadding(3f);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(new BaseColor(153, 204, 255));
                items.addCell(cell);
                cell = new PdfPCell(new Phrase("Importe", mainfont));
                cell.setPadding(3f);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setBackgroundColor(new BaseColor(153, 204, 255));
                items.addCell(cell);
                for (PurchaseOrderItem item : invoice.getOrder().getItems()) {
                    cell = new PdfPCell(new Phrase("" + item.getCode() == null ? "" : item.getCode(), mainfont));
                    cell.setPadding(3f);
                    cell.setBorder(Rectangle.NO_BORDER);
                    items.addCell(cell);
                    cell = new PdfPCell(new Phrase(item.getName(), mainfont));
                    cell.setPadding(3f);
                    cell.setBorder(Rectangle.NO_BORDER);
                    items.addCell(cell);
                    cell = new PdfPCell(new Phrase(item.getQuantity() + "", mainfont));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setPadding(3f);
                    cell.setBorder(Rectangle.NO_BORDER);
                    items.addCell(cell);
                    cell = new PdfPCell(new Phrase(currencyformat.format(item.getPrice()), mainfont));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setPadding(3f);
                    cell.setBorder(Rectangle.NO_BORDER);
                    items.addCell(cell);
                    cell = new PdfPCell(new Phrase(currencyformat.format(item.getTotal()), mainfont));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    cell.setPadding(3f);
                    cell.setBorder(Rectangle.NO_BORDER);
                    items.addCell(cell);
                }
                Image logo = null;
                UserTransaction ut = null;
                try {
                    ut = getUserTransaction();
                    logo = Image.getInstance(portalManager.getLogo(portal).getData());
                    ut.commit();
                    logo.scaleToFit(60, 80);
                    logo.setAbsolutePosition(80, 720);
                    document.add(logo);
                } catch (Throwable e) {
                    logger.log(Level.SEVERE, e.getMessage(), e);
                    try {
                        ut.rollback();
                    } catch (Exception e2) {
                        logger.log(Level.SEVERE, e2.getMessage(), e2);
                    }
                }
                BaseFont basefont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
                BaseFont basefontbold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
                cb.beginText();
                cb.setFontAndSize(basefontbold, 12);
                cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "" + portal.getProperty(PurchaseOrder.INVOICENAME_PROPERTYNAME), 230, 773, 0);
                cb.setFontAndSize(basefont, 10);
                cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "" + portal.getProperty(Invoice.INVOICEVATNUMBER_PROPERTYNAME), 230, 750, 0);
                cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "" + portal.getProperty(PurchaseOrder.INVOICEADDRESS_PROPERTYNAME), 230, 740, 0);
                cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "" + portal.getProperty(PurchaseOrder.INVOICECITY_PROPERTYNAME), 230, 730, 0);
                cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "" + portal.getProperty(PurchaseOrder.INVOICE_AREA_CODE_PROPERTYNAME) + " " + portal.getProperty(PurchaseOrder.INVOICESTATE_PROPERTYNAME), 230, 720, 0);
                cb.setFontAndSize(basefont, 7);
                String companyregisterdata = portal.getProperty(Invoice.INVOICE_COMPANY_REGISTRY_DATA_PROPERTYNAME);
                cb.showTextAligned(PdfContentByte.ALIGN_CENTER, companyregisterdata == null ? "" : companyregisterdata, 290, 45, 0);
                if (invoice.getPaymentDetails() != null) {
                    String paymentDetails = "Forma de pago: " + invoice.getPaymentDetails();
                    cb.setFontAndSize(basefont, 10);
                    cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, paymentDetails, 500, 90, 0);
                }
                cb.showTextAligned(PdfContentByte.ALIGN_LEFT, invoice.getComment() == null ? "" : "Comentarios:", 80, 315, 0);
                cb.endText();
                Phrase comentarios = new Phrase(invoice.getComment() == null ? "" : invoice.getComment(), smallfont);
                ColumnText ct = new ColumnText(cb);
                ct.setSimpleColumn(comentarios, 80, 300, 450, 170, 10, Element.ALIGN_LEFT);
                ct.go();
                Font boldfontbig = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
                PdfPTable invoicedata = new PdfPTable(2);
                invoicedata.setExtendLastRow(true);
                cell = new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setPadding(3f);
                cell.setBackgroundColor(new BaseColor(153, 204, 255));
                cell.setPaddingTop(5f);
                invoicedata.addCell(cell);
                cell = new PdfPCell(new Phrase("Factura", boldfontbig));
                cell.setBackgroundColor(new BaseColor(153, 204, 255));
                cell.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
                cell.setPadding(3f);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setPaddingTop(5f);
                invoicedata.addCell(cell);
                cell = new PdfPCell(new Phrase("Número:", mainfont));
                cell.setBackgroundColor(new BaseColor(153, 204, 255));
                cell.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
                cell.setPadding(3f);
                cell.setBorder(Rectangle.NO_BORDER);
                invoicedata.addCell(cell);
                cell = new PdfPCell(new Phrase("" + invoice.getId(), mainfontbold));
                cell.setBackgroundColor(new BaseColor(153, 204, 255));
                cell.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
                cell.setPadding(3f);
                cell.setBorder(Rectangle.NO_BORDER);
                invoicedata.addCell(cell);
                cell = new PdfPCell(new Phrase("NIF/CIF:", mainfont));
                cell.setBackgroundColor(new BaseColor(153, 204, 255));
                cell.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
                cell.setPadding(3f);
                cell.setBorder(Rectangle.NO_BORDER);
                invoicedata.addCell(cell);
                cell = new PdfPCell(new Phrase(invoice.getVatNumber(), mainfontbold));
                cell.setBackgroundColor(new BaseColor(153, 204, 255));
                cell.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
                cell.setPadding(3f);
                cell.setBorder(Rectangle.NO_BORDER);
                invoicedata.addCell(cell);
                cell = new PdfPCell(new Phrase("Fecha:", mainfont));
                cell.setBackgroundColor(new BaseColor(153, 204, 255));
                cell.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
                cell.setPadding(3f);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setPaddingBottom(10f);
                invoicedata.addCell(cell);
                cell = new PdfPCell(new Phrase(dateformat.format(invoice.getDate()), mainfontbold));
                cell.setBackgroundColor(new BaseColor(153, 204, 255));
                cell.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
                cell.setPadding(3f);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setPaddingBottom(10f);
                invoicedata.addCell(cell);
                cell = new PdfPCell(new Phrase("", mainfontbold));
                cell.setPadding(3f);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(2);
                invoicedata.addCell(cell);
                cell = new PdfPCell(new Phrase(invoice.getName() == null ? "" : invoice.getName(), mainfontbold));
                cell.setPadding(3f);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(2);
                invoicedata.addCell(cell);
                cell = new PdfPCell(new Phrase(invoice.getStreetAddress() == null ? "" : invoice.getStreetAddress(), mainfont));
                cell.setPadding(3f);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(2);
                invoicedata.addCell(cell);
                cell = new PdfPCell(new Phrase("" + invoice.getCity(), mainfont));
                cell.setPadding(3f);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(2);
                invoicedata.addCell(cell);
                cell = new PdfPCell(new Phrase("" + invoice.getAreaCode() + " " + invoice.getState(), mainfont));
                cell.setPadding(3f);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(2);
                invoicedata.addCell(cell);
                cell = new PdfPCell(new Phrase("" + invoice.getCountry() == null ? "" : invoice.getCountry(), mainfont));
                cell.setPadding(3f);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setColspan(2);
                cell.setPaddingBottom(10f);
                invoicedata.addCell(cell);
                float[] widths4 = { 0.7f, 0.4f };
                PdfPTable totalstable = new PdfPTable(widths4);
                totalstable.setExtendLastRow(true);
                if (invoice.getDiscount() > 0) {
                    cell = new PdfPCell(new Phrase("Descuento:", mainfontbold));
                    cell.setPadding(3f);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
                    totalstable.addCell(cell);
                    cell = new PdfPCell(new Phrase(percentformat.format(invoice.getDiscount()), mainfontbold));
                    cell.setPadding(3f);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
                    totalstable.addCell(cell);
                }
                cell = new PdfPCell(new Phrase("Base imponible:", mainfontbold));
                cell.setBackgroundColor(new BaseColor(153, 204, 255));
                cell.setPadding(3f);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
                totalstable.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyformat.format(invoice.getNetTotal()), mainfontbold));
                cell.setBackgroundColor(new BaseColor(153, 204, 255));
                cell.setPadding(3f);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
                totalstable.addCell(cell);
                cell = new PdfPCell(new Phrase("IVA total:", mainfontbold));
                cell.setBackgroundColor(new BaseColor(153, 204, 255));
                cell.setPadding(3f);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
                totalstable.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyformat.format(invoice.getVat()), mainfontbold));
                cell.setBackgroundColor(new BaseColor(153, 204, 255));
                cell.setPadding(3f);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
                totalstable.addCell(cell);
                cell = new PdfPCell(new Phrase("Total:", mainfontbold));
                cell.setBackgroundColor(new BaseColor(153, 204, 255));
                cell.setPadding(3f);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
                cell.setPaddingBottom(10f);
                totalstable.addCell(cell);
                cell = new PdfPCell(new Phrase(currencyformat.format(invoice.getTotal()), mainfontbold));
                cell.setBackgroundColor(new BaseColor(153, 204, 255));
                cell.setPadding(3f);
                cell.setBorder(Rectangle.NO_BORDER);
                cell.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
                cell.setPaddingBottom(10f);
                totalstable.addCell(cell);
                items.setTotalWidth(500);
                items.writeSelectedRows(0, -1, 45, 550, cb);
                invoicedata.setTotalWidth(250);
                invoicedata.writeSelectedRows(0, -1, 315, 795, cb);
                totalstable.setTotalWidth(200);
                totalstable.writeSelectedRows(0, -1, 310, 215, cb);
            }
            document.close();
        } catch (DocumentException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new IllegalStateException(e.getMessage());
        }
        output.close();
        InputStream stream = new ByteArrayInputStream(output.toByteArray());
        return new DefaultStreamedContent(stream, "application/pdf", pdfFileName + ".pdf");
    }

    public Date getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(Date datePaid) {
        this.datePaid = datePaid;
    }
}
