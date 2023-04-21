package org.slasoi.businessManager.productSLA.productDiscovery;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.slasoi.gslam.core.negotiation.SLATemplateRegistry.Listener;
import org.slasoi.slamodel.primitives.UUID;
import org.slasoi.slamodel.sla.SLATemplate;
import org.slasoi.slamodel.vocab.units;

class SystemLoggerListener implements Listener {

    public void message(String msg, Calendar timestamp) {
        System.out.println(x_CAL_TO_STR(timestamp) + msg);
    }

    public void templateRegistered(UUID uuid, Calendar timestamp) {
        System.out.println(x_CAL_TO_STR(timestamp) + "registered SLAT : " + uuid.getValue());
    }

    public void registerTemplateFailed(SLATemplate slat, java.lang.Exception e, Calendar timestamp) {
        System.err.println(x_CAL_TO_STR(timestamp) + "register SLAT failed : " + e.getMessage());
    }

    private String x_CAL_TO_STR(Calendar timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(units.$date_format);
        return "REG @" + sdf.format(timestamp.getTime()) + "\n    >> ";
    }
}
