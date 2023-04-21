package corner.orm.tapestry.jasper;

import java.applet.Applet;
import java.net.URL;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * 打印使用的小程序
 * @author <a href=mailto:xf@bjmaxinfo.com>xiafei</a>
 * @version $Revision: 3678 $
 * @since 2.3.7
 */
public class JRPrinterApplet extends Applet {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3127611999010424523L;

    private URL url = null;

    public void init() {
        String strUrl = getParameter("REPORT_URL");
        if (strUrl != null) {
            try {
                url = new URL(strUrl);
                System.out.println(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else JOptionPane.showMessageDialog(this, "Source URL not specified");
    }

    public void start() {
        if (url != null) {
            try {
                Object obj = JRLoader.loadObject(url);
                JasperPrintManager.printReport((JasperPrint) obj, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
