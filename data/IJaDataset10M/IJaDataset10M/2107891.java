package de.forsthaus.webui.bbruhns;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkex.zul.Jasperreport;
import de.forsthaus.webui.util.GFCBaseCtrl;

/**
 * @author bbruhns
 * 
 */
public class BbruhnsCtrl extends GFCBaseCtrl implements Serializable {

    protected Jasperreport report;

    public BbruhnsCtrl() {
        super();
    }

    public void onClick$test(Event event) {
        JRDataSource dataSource;
        System.err.println("hallo");
        Map parameters = new HashMap();
        parameters.put("ReportTitle", "Address Report");
        parameters.put("DataFile", "CustomDataSource from java");
        report.setParameters(parameters);
        report.setDatasource(new JREmptyDataSource());
    }
}
