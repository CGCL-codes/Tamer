package com.entelience.export.jasper.admin;

import java.util.Arrays;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import com.entelience.export.jasper.DateLimitedReportFiller;
import com.entelience.objects.directory.Expertise;
import com.entelience.soap.soapPreferences;

public class AdminExpertisesFiller extends DateLimitedReportFiller {

    public JRDataSource fillDateLimitedReport(JasperReport report) throws Exception {
        Integer peopleId = getPeopleId();
        soapPreferences sp = new soapPreferences(peopleId);
        List<Expertise> xps = getMyExpertises(sp);
        setParameter("nbExpertises", xps.size());
        return new JRBeanCollectionDataSource(xps);
    }

    private List<Expertise> getMyExpertises(soapPreferences sp) throws Exception {
        return Arrays.asList(sp.getMyExpertises(true, true));
    }
}
