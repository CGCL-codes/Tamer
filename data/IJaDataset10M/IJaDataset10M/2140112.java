package jlokg.parameters;

/**
 * 
 *
 * @hibernate.class
 *     table="rubrik"
 *
 */
public class LKGRubrik {

    private String Account;

    /**
  *   @hibernate.property
  */
    public String getAccount() {
        return Account;
    }

    public void setAccount(String Account) {
        this.Account = Account;
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

    private String Label;

    /**
  *   @hibernate.property
  *     column="Lbl"
  *   @hibernate.column
  *     name="Lbl"
  */
    public String getLabel() {
        return Label;
    }

    public void setLabel(String Label) {
        this.Label = Label;
    }

    private int Nbr;

    /**
  *   @hibernate.property
  */
    public int getNbr() {
        return Nbr;
    }

    public void setNbr(int Nbr) {
        this.Nbr = Nbr;
    }

    private double Honorary;

    /**
  *   @hibernate.property
  */
    public double getHonorary() {
        return Honorary;
    }

    public void setHonorary(double Honorary) {
        this.Honorary = Honorary;
    }

    private boolean Conserve;

    /**
  *   @hibernate.property
  */
    public boolean getConserve() {
        return Conserve;
    }

    public void setConserve(boolean Conserve) {
        this.Conserve = Conserve;
    }

    private boolean Systematic;

    /**
  *   @hibernate.property
  */
    public boolean getSystematic() {
        return Systematic;
    }

    public void setSystematic(boolean Systematic) {
        this.Systematic = Systematic;
    }

    private boolean Augmentable;

    /**
  *   @hibernate.property
  */
    public boolean getAugmentable() {
        return Augmentable;
    }

    public void setAugmentable(boolean Augmentable) {
        this.Augmentable = Augmentable;
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

    public LKGRubrik() {
        Account = "";
        Comment = "";
        Label = "";
        LastUser = "";
        Code = "";
    }

    public String toString() {
        return String.valueOf(Nbr) + " - " + Label + "-" + Account;
    }
}
