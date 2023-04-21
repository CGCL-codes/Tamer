package org.fb4j.events.impl;

import java.util.Date;
import net.sf.json.JSONObject;
import org.fb4j.events.Event;
import org.fb4j.impl.AbstractJsonFacebookObject;
import org.fb4j.impl.AssertInitialized;
import org.fb4j.impl.ConversionUtils;

/**
 * @author Mino Togna
 * 
 */
public class EventImpl extends AbstractJsonFacebookObject implements Event {

    private Long id;

    private Long creatorId;

    private String description;

    private Date endTime;

    private String eventType;

    private String eventSubType;

    private String host;

    private String location;

    private String name;

    private Long networkId;

    private String pic;

    private String picBig;

    private String picSmall;

    private Date startTime;

    private String tagline;

    private Date updateTime;

    private EventLocation venue;

    private Boolean hideGuestList;

    private String privacy;

    private String category;

    private String email;

    private Long pageId;

    private String phone;

    private String subCategory;

    private Long hostId;

    @Override
    protected void processJsonObject(JSONObject jsonObject) {
        id = jsonObject.optLong("eid");
        creatorId = jsonObject.optLong("creator");
        description = jsonObject.optString("description");
        endTime = ConversionUtils.phpToJavaDate(jsonObject.optLong("end_time"));
        startTime = ConversionUtils.phpToJavaDate(jsonObject.optLong("start_time"));
        eventType = jsonObject.optString("event_type");
        eventSubType = jsonObject.optString("event_subtype");
        host = jsonObject.optString("host");
        location = jsonObject.optString("location");
        name = jsonObject.optString("name");
        networkId = jsonObject.optLong("nid");
        pic = jsonObject.optString("pic");
        picBig = jsonObject.optString("pic_big");
        picSmall = jsonObject.optString("pic_small");
        tagline = jsonObject.optString("tagline");
        updateTime = ConversionUtils.phpToJavaDate(jsonObject.optLong("update_time"));
        JSONObject object = jsonObject.optJSONObject("venue");
        if (object != null && !object.isNullObject()) {
            venue = new EventLocationImpl();
            ((EventLocationImpl) venue).processJsonObject(object);
        }
        privacy = jsonObject.optString("privacy");
        hideGuestList = jsonObject.optBoolean("hide_guest_list");
    }

    @AssertInitialized("creator")
    public Long getCreatorId() {
        return creatorId;
    }

    @AssertInitialized("description")
    public String getDescription() {
        return description;
    }

    @AssertInitialized("end_time")
    public Date getEndTime() {
        return endTime;
    }

    @AssertInitialized("event_subtype")
    public String getEventSubtype() {
        return eventSubType;
    }

    @AssertInitialized("event_type")
    public String getEventType() {
        return eventType;
    }

    @AssertInitialized("host")
    public String getHost() {
        return host;
    }

    @AssertInitialized("eid")
    public Long getId() {
        return id;
    }

    @AssertInitialized("location")
    public String getLocation() {
        return location;
    }

    @AssertInitialized("name")
    public String getName() {
        return name;
    }

    @AssertInitialized("nid")
    public Long getNetworkId() {
        return networkId;
    }

    @AssertInitialized("pic")
    public String getPic() {
        return pic;
    }

    @AssertInitialized("pic_big")
    public String getPicBig() {
        return picBig;
    }

    @AssertInitialized("pic_small")
    public String getPicSmall() {
        return picSmall;
    }

    @AssertInitialized("start_time")
    public Date getStartTime() {
        return startTime;
    }

    @AssertInitialized("tagline")
    public String getTagline() {
        return tagline;
    }

    @AssertInitialized("update_time")
    public Date getUpdateTime() {
        return updateTime;
    }

    @AssertInitialized("venue")
    public EventLocation getVenue() {
        return venue;
    }

    @AssertInitialized("privacy")
    public String getPrivacy() {
        return privacy;
    }

    public Boolean hideGuestList() {
        return hideGuestList;
    }

    public String getCategory() {
        return category;
    }

    public String getEmail() {
        return email;
    }

    public Long getPageId() {
        return pageId;
    }

    public String getPhone() {
        return phone;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPageId(long pageId) {
        this.pageId = pageId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public void setVenue(EventLocation venue) {
        this.venue = venue;
    }

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(long hostId) {
        this.hostId = hostId;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("(id=" + id);
        buffer.append(", creator id=" + creatorId);
        buffer.append(", description=" + description);
        buffer.append(", end time=" + endTime);
        buffer.append(", event type=" + eventType);
        buffer.append(", event subType=" + eventSubType);
        buffer.append(", host=" + host);
        buffer.append(", location=" + location);
        buffer.append(", name=" + name);
        buffer.append(", network id=" + networkId);
        buffer.append(", pic=" + pic);
        buffer.append(", pic small=" + picSmall);
        buffer.append(", pic big=" + picBig);
        buffer.append(", start time=" + startTime);
        buffer.append(", tagline=" + tagline);
        buffer.append(", update time=" + updateTime);
        buffer.append(", privacy=" + privacy);
        buffer.append(", hide guest list=" + hideGuestList);
        buffer.append(", venue=" + venue);
        buffer.append(")");
        return buffer.toString();
    }
}
