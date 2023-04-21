package gu.client.model;

import java.util.List;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Shipper implements BaseObject {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    protected Long id;

    public Shipper() {
    }

    public Shipper(String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8, String s9, String s10, String s11) {
        this.sShipId = s1;
        this.sName = s2;
        this.sCity = s3;
        this.sProv = s4;
        this.sPostalCode = s5;
        this.sLatitude = s6;
        this.sLongtitude = s7;
        this.address1 = s8;
        this.address2 = s9;
        this.contact = s10;
        this.phone = s11;
    }

    @Persistent
    private String address1;

    public String get_address1() {
        return address1;
    }

    public void set_address1(String address1) {
        this.address1 = address1;
    }

    @Persistent
    private String address2;

    public String get_address2() {
        return address2;
    }

    public void set_address2(String address2) {
        this.address2 = address2;
    }

    @Persistent
    private String contact;

    public String get_contact() {
        return contact;
    }

    public void set_contact(String contact) {
        this.contact = contact;
    }

    @Persistent
    private String phone;

    public String get_phone() {
        return phone;
    }

    public void set_phone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        if (id == null) return null;
        return id.toString();
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Persistent
    private String sName;

    public String getName() {
        return sName;
    }

    public void setName(String sName) {
        this.sName = sName;
    }

    @Persistent
    private String sCity;

    public String getCity() {
        return sCity;
    }

    public void setCity(String sCity) {
        this.sCity = sCity;
    }

    @Persistent
    private String sProv;

    public String getProv() {
        return sProv;
    }

    public void setProv(String sProv) {
        this.sProv = sProv;
    }

    @Persistent
    private String sPostalCode;

    public String getPostalCode() {
        return sPostalCode;
    }

    public void setPostalCode(String sPostalCode) {
        this.sPostalCode = sPostalCode;
    }

    @Persistent
    private String sLatitude;

    public String getLatitude() {
        return sLatitude;
    }

    public void setLatitude(String sLatitude) {
        this.sLatitude = sLatitude;
    }

    @Persistent
    private String sLongtitude;

    public String getLongtitude() {
        return sLongtitude;
    }

    public void setLongtitude(String sLongtitude) {
        this.sLongtitude = sLongtitude;
    }

    @Persistent
    private String sShipId;

    public String get_ship_id() {
        return sShipId;
    }

    public void set_ship_id(String sShipId) {
        this.sShipId = sShipId;
    }
}
