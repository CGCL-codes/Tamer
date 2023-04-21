package atp.reporter.test.valera;

import java.io.*;
import atp.reporter.RDriver;
import atp.reporter.data.RArguments;
import atp.reporter.db.*;
import atp.reporter.exception.RParseException;
import atp.reporter.export.RExporter;
import atp.reporter.export.RHTMLClassic;
import atp.reporter.factory.*;
import atp.reporter.items.RReport;
import atp.reporter.items.allocators.cits.RFactoryOfAllocatorCITS;
import atp.reporter.items.parameter.cits.RFactoryOfParameterCITS;
import atp.reporter.product.*;
import atp.xml.*;

public class RParserTest {

    public static RDBManager sqlManager = new RDBManagerStub(RDBType.CITS) {

        public RDriver createDriver() {
            return new RDriver(new RFactoryOfAllocatorCITS(), new RFactoryOfReport(), new RFactoryOfParameterCITS(), this);
        }
    };

    public static void main(String[] arg) {
        try {
            String file = "reports/test8.xml";
            RDriver driver = sqlManager.createDriver();
            Reader reader = new InputStreamReader(new FileInputStream(file), "Cp866");
            Document doc = Parser.parse(reader);
            RReport report = driver.parseReport(doc);
            System.out.println(report.getType() + " " + report.getDescription() + " " + report.getTitle());
            RArguments a = new RArguments();
            long now = atp.util.ATPUtil.getNowDate();
            a.setDatePeriod(now - 1000 * 60 * 60 * 24 * 2, now);
            a.put("CDNG", "1");
            RResult result = report.getResult();
            result.setDataManager(sqlManager, a);
            (new RExporterExcel()).exportToFile(result, "export/valera.xls");
            driver.sqlManager.close();
        } catch (RParseException r) {
            System.out.println(r.toString());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
