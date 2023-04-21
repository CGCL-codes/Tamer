package camelinaction;

import org.apache.camel.language.XPath;

/**
 * @version $Revision: 19 $
 */
public class PartnerServiceBean {

    public String toSql(@XPath("partner/@id") int partnerId, @XPath("partner/date/text()") String date, @XPath("partner/code/text()") int statusCode, @XPath("partner/time/text()") long responsTime) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO PARTNER_METRIC (partner_id, time_occurred, status_code, perf_time) VALUES (");
        sb.append("'").append(partnerId).append("', ");
        sb.append("'").append(date).append("', ");
        sb.append("'").append(statusCode).append("', ");
        sb.append("'").append(responsTime).append("') ");
        return sb.toString();
    }
}
