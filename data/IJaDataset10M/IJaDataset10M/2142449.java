package jlokg.lodger;

import fr.nhb.IHasJAddress;
import fr.nhb.JAddress;
import fr.nhb.JAddressFacade;

/**
 * 
 * 
 * 
 * @hibernate.class table = "garant"
 */
public class LKGGarant implements IHasJAddress {

    private static String knhTypeAddress = "garant";

    private JAddress address;

    private String lodger;

    /**
  *   @hibernate.property
  */
    public String getLodger() {
        return lodger;
    }

    public void setLodger(String lodger) {
        this.lodger = lodger;
    }

    private String name;

    /**
  *   @hibernate.property
  */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String profession;

    /**
  *   @hibernate.property
  */
    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    private String phone;

    /**
  *   @hibernate.property
  */
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String Comment;

    /**
  *   @hibernate.property
  */
    public String getComment() {
        return Comment;
    }

    public void setComment(String Comment) {
        this.Comment = Comment;
    }

    private int civilite;

    /**
  *   @hibernate.property
  */
    public int getCivilite() {
        return civilite;
    }

    public void setCivilite(int civilite) {
        this.civilite = civilite;
    }

    private String LastUser;

    /**
  *   @hibernate.property
  */
    public String getLastUser() {
        return LastUser;
    }

    public void setLastUser(String LastUser) {
        this.LastUser = LastUser;
    }

    private java.util.Date DateModification;

    /**
  *   @hibernate.property
  */
    public java.util.Date getDateModification() {
        return DateModification;
    }

    public void setDateModification(java.util.Date DateModification) {
        this.DateModification = DateModification;
    }

    private java.util.Date DateCreation;

    /**
  *   @hibernate.property
  */
    public java.util.Date getDateCreation() {
        return DateCreation;
    }

    public void setDateCreation(java.util.Date DateCreation) {
        this.DateCreation = DateCreation;
    }

    private String Code;

    /**
  *   @hibernate.id
  *     generator-class="assigned"
  */
    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public LKGGarant() {
        lodger = "";
        name = "";
        profession = "";
        phone = "";
        Comment = "";
        LastUser = "";
        Code = "";
    }

    public String getString() {
        return name;
    }

    public JAddress getAddress() {
        if (address == null) {
            JAddressFacade facade = new JAddressFacade();
            address = facade.getAddress(this.knhTypeAddress, this.Code);
        }
        return address;
    }
}
