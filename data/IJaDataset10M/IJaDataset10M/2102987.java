package jlokg.categories;

/**
 * 
 * 
 * 
 * 
 * @hibernate.class table = "catsitegeo"
 */
public class CatSiteGeo implements IGenericCategory {

    private String Name;

    /**
  *   @hibernate.property
  */
    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
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
  *     column="code"
  *   @hibernate.column
  *     name="code"
  *     not-null="false"
  */
    public String getCode() {
        return Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public CatSiteGeo() {
        Name = "";
        Comment = "";
        LastUser = "";
        Code = "";
    }

    public String toString() {
        return Name;
    }
}
