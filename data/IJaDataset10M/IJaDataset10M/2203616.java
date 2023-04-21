package com.adserversoft.flexfuse.server.api;

import com.adserversoft.flexfuse.server.api.ui.AdPlaceBookings;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import net.sf.gilead.pojo.java5.LightEntity;

/**
 * Author: Vitaly Sazanovich
 * Email: Vitaly.Sazanovich@gmail.com
 */
public class AdPlace extends LightEntity implements Serializable {

    private Integer id;

    private String uid;

    private String adPlaceName;

    private AdFormat adFormat;

    private String pricesXml;

    private Site site;

    private SortedSet<Rate> prices = new TreeSet<Rate>();

    private Set<BookedHour> bookedHours = new HashSet<BookedHour>();

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPricesXml() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(baos));
        encoder.writeObject(prices);
        encoder.close();
        pricesXml = new String(baos.toByteArray());
        return pricesXml;
    }

    public void setPricesXml(String pricesXml) {
        XMLDecoder xmlDecoder = new XMLDecoder(new BufferedInputStream(new ByteArrayInputStream(pricesXml.getBytes())));
        prices = (SortedSet<Rate>) xmlDecoder.readObject();
        this.pricesXml = pricesXml;
    }

    public Set<BookedHour> getBookedHours() {
        return bookedHours;
    }

    public void setBookedHours(Set<BookedHour> bookedHours) {
        this.bookedHours = bookedHours;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public AdFormat getAdFormat() {
        return adFormat;
    }

    public void setAdFormat(AdFormat adFormat) {
        this.adFormat = adFormat;
    }

    public SortedSet<Rate> getPrices() {
        return prices;
    }

    public void setPrices(SortedSet<Rate> prices) {
        this.prices = prices;
    }

    public String getAdPlaceName() {
        return adPlaceName;
    }

    public void setAdPlaceName(String adPlaceName) {
        this.adPlaceName = adPlaceName;
    }
}
