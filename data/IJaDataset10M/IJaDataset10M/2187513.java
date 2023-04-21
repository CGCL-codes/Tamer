package part3.chapter09;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PushbuttonField;
import com.itextpdf.text.pdf.XfdfReader;

public class XFDFServlet extends HttpServlet {

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/pdf");
        try {
            InputStream is = getServletContext().getResourceAsStream("/subscribe.pdf");
            PdfReader reader = new PdfReader(is, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, baos);
            PushbuttonField button = new PushbuttonField(stamper.getWriter(), new Rectangle(90, 660, 140, 690), "submit");
            button.setText("POST");
            button.setBackgroundColor(new GrayColor(0.7f));
            button.setVisibility(PushbuttonField.VISIBLE_BUT_DOES_NOT_PRINT);
            PdfFormField submit = button.getField();
            submit.setAction(PdfAction.createSubmitForm("/book/xfdf", null, PdfAction.SUBMIT_XFDF));
            stamper.addAnnotation(submit, 1);
            stamper.close();
            OutputStream os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
        } catch (DocumentException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/pdf");
        try {
            XfdfReader xfdf = new XfdfReader(request.getInputStream());
            InputStream is = getServletContext().getResourceAsStream("/subscribe.pdf");
            PdfReader reader = new PdfReader(is, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, baos);
            AcroFields fields = stamper.getAcroFields();
            fields.setFields(xfdf);
            stamper.close();
            OutputStream os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
        } catch (DocumentException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = 7582490560292953774L;
}
