package corner.orm.tapestry.jasper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.testng.Assert;
import corner.orm.tapestry.jasper.ciq.Custom;
import corner.orm.tapestry.jasper.exporter.CornerPdfExporter;

/**
 * @author <a href=mailto:xf@bjmaxinfo.com>xiafei</a>
 * @version $Revision: 4121 $
 * @since 0.8.3
 */
public class CornerExporterTest extends Assert {

    static Class entity = Custom.class;

    static String simpleName = Custom.class.getSimpleName();

    public void testPath() {
        System.out.println(System.getProperty("user.dir"));
        System.out.println(System.getProperty("java.class.path"));
    }

    public void testMakePdf() throws JRException {
        JRPdfExporter exporter = new CornerPdfExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, getJasperPrint());
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, path + "test.pdf");
        exporter.exportReport();
    }

    /**
	 * 初始化
	 * @return
	 * @throws JRException
	 */
    public JasperPrint getJasperPrint() throws JRException {
        if (jasperPrint != null) {
            return jasperPrint;
        }
        path = System.getProperty("user.dir") + "/target/jasper/";
        File nF = new File(path);
        nF.mkdirs();
        List bowlerInfo = getCollection();
        HashMap parameters = new HashMap();
        parameters.put("port", "aaaaaa");
        parameters.put("test", "aaaaaa");
        parameters.put("a", "大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？说高高山上有条藤，条条藤上挂铜铃，风吹藤动铜铃动，风停藤停铜铃停大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？说高高山上有条藤，条条藤上挂铜铃，风吹藤动铜铃动，风停藤停铜铃停大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？说高高山上有条藤，条条藤上挂铜铃，风吹藤动铜铃动，风停藤停铜铃停大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？说高高山上有条藤，条条藤上挂铜铃，风吹藤动铜铃动，风停藤停铜铃停大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？说高高山上有条藤，条条藤上挂铜铃，风吹藤动铜铃动，风停藤停铜铃停大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？说高高山上有条藤，条条藤上挂铜铃，风吹藤动铜铃动，风停藤停铜铃停大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？说高高山上有条藤，条条藤上挂铜铃，风吹藤动铜铃动，风停藤停铜铃停大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？说高高山上有条藤，条条藤上挂铜铃，风吹藤动铜铃动，风停藤停铜铃停大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？说高高山上有条藤，条条藤上挂铜铃，风吹藤动铜铃动，风停藤停铜铃停大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？说高高山上有条藤，条条藤上挂铜铃，风吹藤动铜铃动，风停藤停铜铃停大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？说高高山上有条藤，条条藤上挂铜铃，风吹藤动铜铃动，风停藤停铜铃停大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？说高高山上有条藤，条条藤上挂铜铃，风吹藤动铜铃动，风停藤停铜铃停大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？说高高山上有条藤，条条藤上挂铜铃，风吹藤动铜铃动，风停藤停铜铃停大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？说高高山上有条藤，条条藤上挂铜铃，风吹藤动铜铃动，风停藤停铜铃停大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？说高高山上有条藤，条条藤上挂铜铃，风吹藤动铜铃动，风停藤停铜铃停大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？说高高山上有条藤，条条藤上挂铜铃，风吹藤动铜铃动，风停藤停铜铃停大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？说高高山上有条藤，条条藤上挂铜铃，风吹藤动铜铃动，风停藤停铜铃停大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？说高高山上有条藤，条条藤上挂铜铃，风吹藤动铜铃动，风停藤停铜铃停大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？说高高山上有条藤，条条藤上挂铜铃，风吹藤动铜铃动，风停藤停铜铃停大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？大家好，今天你做了什么？说高高山上有条藤，条条藤上挂铜铃，风吹藤动铜铃动，风停藤停铜铃停");
        File file = new File(System.getProperty("user.dir") + "/src/test/resources/jasper/test.jasper");
        InputStream is;
        try {
            is = new FileInputStream(file);
            return jasperPrint = JasperFillManager.fillReport(is, parameters, new JRBeanCollectionDataSource(bowlerInfo));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List getCollection() {
        List<Object> rs = new ArrayList<Object>();
        Custom c = new Custom();
        c.setPort("beijing");
        c.setTest("测试测试测试");
        rs.add(c);
        return rs;
    }

    String path = null;

    JasperPrint jasperPrint = null;

    HashMap parameters = null;
}
