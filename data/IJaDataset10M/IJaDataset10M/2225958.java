package com.entelience.export.jasper.admin;

import java.util.Arrays;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import com.entelience.export.jasper.DateLimitedReportFiller;
import com.entelience.objects.module.Preference;
import com.entelience.soap.soapModule;

public class AdminGlobalPrefsFiller extends DateLimitedReportFiller {

    public JRDataSource fillDateLimitedReport(JasperReport report) throws Exception {
        Integer peopleId = getPeopleId();
        List<Preference> prefs = null;
        soapModule sm = new soapModule(peopleId);
        prefs = getGlobalPrefs(sm);
        return new JRBeanCollectionDataSource(prefs);
    }

    private List<Preference> getGlobalPrefs(soapModule sm) throws Exception {
        return Arrays.asList(sm.listGlobalPreferences());
    }
}
