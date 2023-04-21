package javax.print;

import java.io.InputStream;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaName;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.Sides;
import junit.framework.TestCase;

public class LookupDefaultPrintServiceTest extends TestCase {

    public void testLookupDefaultPrintService() throws Exception {
        System.out.println("======= START LookupDefaultPrintServiceTest ======");
        DocFlavor psFlavor = DocFlavor.INPUT_STREAM.GIF;
        PrintService service;
        InputStream fis;
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        HashDocAttributeSet daset = new HashDocAttributeSet();
        DocPrintJob pj;
        Doc doc;
        aset.add(new Copies(2));
        aset.add(MediaSizeName.ISO_A4);
        daset.add(MediaName.ISO_A4_WHITE);
        daset.add(Sides.TWO_SIDED_LONG_EDGE);
        service = PrintServiceLookup.lookupDefaultPrintService();
        if (service != null) {
            if (service.isDocFlavorSupported(psFlavor)) {
                if (service.getUnsupportedAttributes(psFlavor, aset) == null) {
                    fis = this.getClass().getResourceAsStream("/Resources/GIF.gif");
                    doc = new SimpleDoc(fis, psFlavor, daset);
                    pj = service.createPrintJob();
                    pj.print(doc, aset);
                    System.out.println(fis.toString() + " printed on " + service.getName());
                }
            } else {
                System.out.println("flavor is not supported");
            }
        } else {
            System.out.println("service not found");
        }
        System.out.println("======= END LookupDefaultPrintServiceTest =======");
    }
}
