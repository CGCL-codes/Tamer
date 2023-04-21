package questions.forms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PRStream;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class FillDynamicXfa {

    public static final String RESOURCE_PDF = "resources/questions/forms/dynamic.pdf";

    public static final String RESOURCE_DATA = "resources/questions/forms/datasets.xml";

    public static final String RESULT = "results/questions/forms/filled_xfa.pdf";

    public static void main(String[] args) {
        try {
            PdfReader reader = new PdfReader(RESOURCE_PDF);
            File file = new File(RESOURCE_DATA);
            byte[] data = new byte[(int) file.length()];
            FileInputStream is = new FileInputStream(file);
            int offset = 0;
            int numRead = 0;
            int datalength = data.length;
            while (offset < datalength && (numRead = is.read(data, offset, datalength - offset)) >= 0) {
                offset += numRead;
            }
            PdfDictionary root = reader.getCatalog();
            PdfDictionary acroform = root.getAsDict(PdfName.ACROFORM);
            PdfArray xfa = acroform.getAsArray(PdfName.XFA);
            for (int i = 0; i < xfa.size(); i += 2) {
                if ("datasets".equals(xfa.getAsString(i).toString())) {
                    PRStream s = (PRStream) xfa.getAsStream(i + 1);
                    s.setData(data);
                }
            }
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(RESULT));
            stamper.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
