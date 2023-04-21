package com.leclercb.taskunifier.api.models;

import java.util.Calendar;
import com.leclercb.commons.api.utils.CheckUtils;
import com.leclercb.taskunifier.api.models.beans.LocationBean;
import com.leclercb.taskunifier.api.models.beans.ModelBean;

public class Location extends AbstractModel {

    public static final String PROP_DESCRIPTION = "description";

    public static final String PROP_LATITUDE = "latitude";

    public static final String PROP_LONGITUDE = "longitude";

    private String description;

    private double latitude;

    private double longitude;

    protected Location(LocationBean bean, boolean loadReferenceIds) {
        this(bean.getModelId(), bean.getTitle());
        this.loadBean(bean, loadReferenceIds);
    }

    protected Location(String title) {
        this(new ModelId(), title);
    }

    protected Location(ModelId modelId, String title) {
        super(modelId, title);
        this.setDescription(null);
        this.setLatitude(0);
        this.setLongitude(0);
        this.getFactory().register(this);
    }

    @Override
    public Location clone(ModelId modelId) {
        Location location = this.getFactory().create(modelId, this.getTitle());
        location.setDescription(this.getDescription());
        location.setLatitude(this.getLatitude());
        location.setLongitude(this.getLongitude());
        location.setOrder(this.getOrder());
        location.addProperties(this.getProperties());
        location.setModelStatus(this.getModelStatus());
        location.setModelCreationDate(Calendar.getInstance());
        location.setModelUpdateDate(Calendar.getInstance());
        return location;
    }

    @Override
    public LocationFactory<Location, LocationBean> getFactory() {
        return LocationFactory.getInstance();
    }

    @Override
    public ModelType getModelType() {
        return ModelType.LOCATION;
    }

    @Override
    public void loadBean(ModelBean b, boolean loadReferenceIds) {
        CheckUtils.isNotNull(b);
        CheckUtils.isInstanceOf(b, LocationBean.class);
        LocationBean bean = (LocationBean) b;
        this.setDescription(bean.getDescription());
        this.setLatitude(bean.getLatitude());
        this.setLongitude(bean.getLongitude());
        super.loadBean(bean, loadReferenceIds);
    }

    @Override
    public LocationBean toBean() {
        LocationBean bean = (LocationBean) super.toBean();
        bean.setDescription(this.getDescription());
        bean.setLatitude(this.getLatitude());
        bean.setLongitude(this.getLongitude());
        return bean;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        if (!this.checkBeforeSet(this.getDescription(), description)) return;
        String oldDescription = this.description;
        this.description = description;
        this.updateProperty(PROP_DESCRIPTION, oldDescription, description);
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        if (!this.checkBeforeSet(this.getLatitude(), latitude)) return;
        double oldLatitude = this.latitude;
        this.latitude = latitude;
        this.updateProperty(PROP_LATITUDE, oldLatitude, latitude);
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        if (!this.checkBeforeSet(this.getLongitude(), longitude)) return;
        double oldLongitude = this.longitude;
        this.longitude = longitude;
        this.updateProperty(PROP_LONGITUDE, oldLongitude, longitude);
    }

    @Override
    public String toDetailedString() {
        StringBuffer buffer = new StringBuffer(super.toDetailedString());
        if (this.getDescription() != null) buffer.append("Description: " + this.getDescription() + "\n");
        buffer.append("Latitude: " + this.getLatitude() + "\n");
        buffer.append("Longitude: " + this.getLongitude() + "\n");
        return buffer.toString();
    }
}
