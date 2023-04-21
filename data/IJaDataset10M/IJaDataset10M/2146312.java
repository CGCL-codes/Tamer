package org.dasein.cloud.metacdn.platform;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.dasein.cloud.CloudException;
import org.dasein.cloud.InternalException;
import org.dasein.cloud.metacdn.MetaCDN;
import org.dasein.cloud.platform.CDNSupport;
import org.dasein.cloud.platform.Distribution;
import org.dasein.util.CalendarWrapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>Several things to note about the MetaCDN API:
 * <ul>
 *   <li>Location: <a href="http://www.metacdn.com/apidocs">http://www.metacdn.com/apidocs</a></li>
 *   <li>The API operates on content items, not whole buckets or lists of content.  Therefore, the 
 *   current implementation assumes that an {@link Distribution} maps to a MetaCDN content.
 * </ul>
 * 
 * TODO REFACTOR!!!!!!!!!!!!!!!!!!!!
 */
public class CDN implements CDNSupport {

    private static final Logger logger = Logger.getLogger(CDN.class);

    private MetaCDN cloud;

    CDN(MetaCDN cloud) {
        this.cloud = cloud;
    }

    @Override
    public String create(String origin, String name, boolean active, String... aliases) throws InternalException, CloudException {
        DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss zzz");
        StringBuilder sb = new StringBuilder("{\"originSource\": [{ \"originUrl\" :\"");
        sb.append(origin);
        sb.append("\", \"description\":\"");
        sb.append(name);
        sb.append("\"}], \"locations\":[USA_WEST, USA_CENTRAL, USA_EAST, ASIA_SOUTH_EAST, ASIA_EAST, EU_WEST,EU_CENTRAL, EU_NORTH, AUS_EAST],");
        sb.append("\"hostUntil\" : \"");
        CalendarWrapper cal = new CalendarWrapper().getMidnight();
        cal.calendar.add(Calendar.MONTH, 1);
        sb.append(df.format(cal.getDate()));
        sb.append("\",\"duplicates\" : true}");
        MetaCDNMethod method = new MetaCDNMethod(cloud, MetaCDNAction.CREATE_CONTENT, null, "application/json", sb.toString());
        try {
            method.invoke();
        } catch (MetaCDNException e) {
            logger.error(e.getSummary());
            throw new CloudException(e);
        }
        return null;
    }

    @Override
    public void delete(String distributionId) throws InternalException, CloudException {
        MetaCDNMethod method = new MetaCDNMethod(cloud, MetaCDNAction.DELETE_CONTENT, null, null, null);
        try {
            method.invoke(distributionId);
        } catch (MetaCDNException e) {
            logger.error(e.getSummary());
            throw new CloudException(e);
        }
    }

    @Override
    public Distribution getDistribution(String distributionId) throws InternalException, CloudException {
        MetaCDNMethod method = new MetaCDNMethod(cloud, MetaCDNAction.GET_CONTENT, null, null, null);
        JSONObject response;
        try {
            response = new JSONObject(method.invoke(distributionId));
            return toDistribution(response);
        } catch (MetaCDNException e) {
            logger.error(e.getSummary());
            throw new CloudException(e);
        } catch (JSONException e) {
            logger.error(e.getMessage());
            throw new CloudException(e);
        }
    }

    @Override
    public String getProviderTermForDistribution(Locale locale) {
        return "Content";
    }

    @Override
    public boolean isSubscribed() throws InternalException, CloudException {
        MetaCDNMethod method = new MetaCDNMethod(cloud, MetaCDNAction.LIST_CONTENT, null, null, null);
        try {
            method.invoke();
            return true;
        } catch (MetaCDNException e) {
            if (e.getStatus() == HttpServletResponse.SC_UNAUTHORIZED || e.getStatus() == HttpServletResponse.SC_FORBIDDEN) {
                return false;
            }
            logger.warn(e.getSummary());
            if (logger.isDebugEnabled()) {
                e.printStackTrace();
            }
            throw new CloudException(e);
        }
    }

    @Override
    public Collection<Distribution> list() throws InternalException, CloudException {
        ArrayList<Distribution> list = new ArrayList<Distribution>();
        MetaCDNMethod method = new MetaCDNMethod(cloud, MetaCDNAction.LIST_CONTENT, null, null, null);
        try {
            JSONArray result = new JSONArray(method.invoke());
            for (int i = 0; i < result.length(); i++) {
                list.add(toDistribution(result.getJSONObject(i)));
            }
            return list;
        } catch (MetaCDNException e) {
            logger.error(e.getSummary());
            throw new CloudException(e);
        } catch (JSONException e) {
            logger.error(e.getMessage());
            throw new CloudException(e);
        }
    }

    @Override
    public void update(String distributionId, String name, boolean active, String... aliases) throws InternalException, CloudException {
        logger.warn("NOT IMPLEMENTED as updating a content doesn't make sense at this time");
    }

    private Distribution toDistribution(JSONObject obj) throws JSONException {
        Distribution distribution = new Distribution();
        distribution.setActive(obj.getString("contentStatus").equals("ACTIVE"));
        distribution.setAliases(new String[] { obj.getString("metaCdnUrl") });
        distribution.setDeployed(obj.getString("replicationStatus").equals("COMPLETED"));
        distribution.setDnsName(null);
        distribution.setLocation(obj.getString("originUrl"));
        distribution.setName(obj.getString("fileName"));
        distribution.setProviderDistributionId(obj.getString("keyName"));
        distribution.setProviderOwnerId(obj.getString("contentUser"));
        return distribution;
    }
}
