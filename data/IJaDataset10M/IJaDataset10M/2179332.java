package net.kodveus.kumanifest.report;

import java.net.URL;
import javax.swing.JApplet;
import javax.swing.JOptionPane;
import net.kodveus.kumanifest.utility.LogHelper;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * TODO Bu sinif uygulamaya uygun bir hale getirilmeli
 * 
 * @author emre
 * 
 */
public class PrintReport extends JApplet {

    private static final long serialVersionUID = 1L;

    private URL url = null;

    JasperPrint jasperPrint = null;

    public PrintReport() {
    }

    @Override
    public void init() {
        String strUrl = getParameter("url");
        LogHelper.getLogger().info(strUrl);
        if (strUrl != null) {
            try {
                url = getURL(strUrl);
                printReport();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.toString());
            }
        } else {
            JOptionPane.showMessageDialog(this, "URL Hatasý");
        }
    }

    private void printReport() {
        if (url != null) {
            try {
                jasperPrint = (JasperPrint) JRLoader.loadObject(url);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.toString());
            }
            if (jasperPrint != null) {
                Thread thread = new Thread(new Runnable() {

                    public void run() {
                        try {
                            if (jasperPrint.getPages() != null && jasperPrint.getPages().size() > 0) {
                                JasperPrintManager.printReport(jasperPrint, false);
                            } else {
                                JOptionPane.showMessageDialog(null, "Yeterli Sayýda Sayfa Yok!");
                            }
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, e.toString());
                        }
                    }
                });
                thread.start();
            } else {
                JOptionPane.showMessageDialog(this, "Boþ Rapor");
            }
        } else {
            JOptionPane.showMessageDialog(this, "URL Hatasý");
        }
    }

    private URL getURL(String path) throws Exception {
        URL _url = this.getCodeBase();
        URL newUrl = new URL(_url.getProtocol(), _url.getHost(), _url.getPort(), path);
        return newUrl;
    }
}
