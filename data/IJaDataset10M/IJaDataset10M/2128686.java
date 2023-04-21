package ro.gateway.aida.locations;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import ro.gateway.aida.utils.HttpUtils;

/**
 * <p>Title: Romanian AIDA</p> <p>Description: </p> <p>Copyright: Copyright (comparator) 2003</p> <p>Company:
 * ro-gateway</p>
 *
 * @author Mihai Popoaei, mihai_popoaei@yahoo.com, smike@intellisource.ro
 * @version 1.0-* @version $Id: EditLocationForm.java,v 1.1 2004/10/24 23:37:41 mihaipostelnicu Exp $
 */
public class EditLocationForm extends ActionForm {

    public String name;

    public int type;

    public String geo_code;

    public double latitude;

    public double longitude;

    public String id;

    public String cregion_code;

    public String action;

    public String otype;

    public int cregion_parent_id = -1;

    public int getCregion_parent_id() {
        return cregion_parent_id;
    }

    public void setCregion_parent_id(int cregion_parent_id) {
        this.cregion_parent_id = cregion_parent_id;
    }

    public String getCregion_code() {
        return cregion_code;
    }

    public void setCregion_code(String cregion_code) {
        this.cregion_code = cregion_code;
    }

    public String getOtype() {
        return otype;
    }

    public void setOtype(String otype) {
        this.otype = otype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGeo_code() {
        return geo_code;
    }

    public void setGeo_code(String geo_code) {
        this.geo_code = geo_code;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void clear() {
        this.longitude = 0;
        this.latitude = 0;
        this.geo_code = null;
        this.action = null;
        this.otype = null;
        this.name = null;
        this.type = -1;
        this.id = null;
    }

    public void populate(HttpServletRequest request) {
        this.longitude = HttpUtils.getDouble(request, "longitude", 0);
        this.latitude = HttpUtils.getDouble(request, "latitude", 0);
        this.geo_code = HttpUtils.getValidTrimedString(request, "geo_code", null);
        this.action = HttpUtils.getValidTrimedString(request, "action", null);
        this.otype = HttpUtils.getValidTrimedString(request, "otype", null);
        this.name = HttpUtils.getValidTrimedString(request, "name", null);
        this.id = HttpUtils.getValidTrimedString(request, "id", null);
        if (this.id == null && "country".equals(this.otype)) {
            this.id = HttpUtils.getValidTrimedString(request, "iso3", null);
        }
        this.type = HttpUtils.getInt(request, "type", -1);
        this.cregion_code = HttpUtils.getValidTrimedString(request, "cregion_code", null);
    }
}
