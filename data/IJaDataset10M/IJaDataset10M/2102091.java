package org.colimas.entity;

/**
 * <h3>UserRoleEntity.java</h3>
 *
 * <P>
 * Function:<BR />
 * select records from turm
 * </P>
 * @author zhao lei
 * @version 1.0
 * <br>
 * Modification History:
 * <PRE>
 * SEQ DATE       ORDER DEVELOPER      DESCRIPTION
 * --- ---------- ----- -------------- -----------------------------
 * 001 2006/03/30          tyrone        INIT
 * </PRE>
 */
public class UserRoleEntity implements I_Entity {

    public static final long serialVersionUID = 1;

    private String userid;

    private String keyid;

    private String keyname;

    private String keytype;

    /**
	 *<p>get keyid</p>
	 * @return Returns the keyid.
	 */
    public String getKeyid() {
        return keyid;
    }

    /**
	 * <p>set keyid</p>
	 * @param keyid The keyid to set.
	 */
    public void setKeyid(String keyid) {
        this.keyid = keyid;
    }

    /**
	 *<p>get keyname</p>
	 * @return Returns the keyname.
	 */
    public String getKeyname() {
        return keyname;
    }

    /**
	 * <p>set keyname</p>
	 * @param keyname The keyname to set.
	 */
    public void setKeyname(String keyname) {
        this.keyname = keyname;
    }

    /**
	 *<p>get keytype</p>
	 * @return Returns the keytype.
	 */
    public String getKeytype() {
        return keytype;
    }

    /**
	 * <p>set keytype</p>
	 * @param keytype The keytype to set.
	 */
    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    /**
	 *<p>get userid</p>
	 * @return Returns the userid.
	 */
    public String getUserid() {
        return userid;
    }

    /**
	 * <p>set userid</p>
	 * @param userid The userid to set.
	 */
    public void setUserid(String userid) {
        this.userid = userid;
    }
}
