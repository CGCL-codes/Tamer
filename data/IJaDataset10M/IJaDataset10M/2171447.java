package org.broadleafcommerce.profile.email.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.profile.core.domain.Customer;
import org.broadleafcommerce.profile.email.dao.EmailReportingDao;
import org.springframework.stereotype.Service;

/**
 * @author jfischer
 */
@Service("blEmailTrackingManager")
public class EmailTrackingManagerImpl implements EmailTrackingManager {

    private static final Log LOG = LogFactory.getLog(EmailTrackingManagerImpl.class);

    @Resource(name = "blEmailReportingDao")
    protected EmailReportingDao emailReportingDao;

    public Long createTrackedEmail(String emailAddress, String type, String extraValue) {
        return emailReportingDao.createTracking(emailAddress, type, extraValue);
    }

    public void recordClick(Long emailId, Map<String, String> parameterMap, Customer customer, Map<String, String> extraValues) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("recordClick() => Click detected for Email[" + emailId + "]");
        }
        Iterator<String> keys = parameterMap.keySet().iterator();
        ArrayList<String> queryParms = new ArrayList<String>();
        while (keys.hasNext()) {
            String p = keys.next();
            if (!p.equals("email_id")) {
                queryParms.add(p);
            }
        }
        String newQuery = null;
        if (!queryParms.isEmpty()) {
            String[] p = queryParms.toArray(new String[queryParms.size()]);
            Arrays.sort(p);
            StringBuffer newQueryParms = new StringBuffer();
            for (int cnt = 0; cnt < p.length; cnt++) {
                newQueryParms.append(p[cnt]);
                newQueryParms.append("=");
                newQueryParms.append(parameterMap.get(p[cnt]));
                if (cnt != p.length - 1) {
                    newQueryParms.append("&");
                }
            }
            newQuery = newQueryParms.toString();
        }
        emailReportingDao.recordClick(emailId, customer, extraValues.get("requestUri"), newQuery);
    }

    public void recordOpen(Long emailId, Map<String, String> extraValues) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Recording open for email id: " + emailId);
        }
        emailReportingDao.recordOpen(emailId, extraValues.get("userAgent"));
    }
}
