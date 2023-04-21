package weblife.section;

import weblife.object.ObjectUser;
import weblife.ressource.MD5;
import weblife.server.AbstractWeblife;
import weblife.server.RequestInterface;
import weblife.xml.ErrorcodeException;
import weblife.xml.MyParser;

/**
 * Section f�r Methodenaufrufe der User section
 * http://wiki.szene1.co.at/wiki/Weblife1_API:Section_User (20.04.2011)
 * 
 * @author Knoll
 *
 */
public class SectionUser extends AbstractWeblife implements SectionInterface {

    private final String SECTION = "user";

    public static final String METHODE_LOGIN = "login";

    public static final String METHODE_LOGOUT = "logout";

    public static final String METHODE_GETCURRENTSTATUS = "getcurrentstatus";

    public static final String METHODE_GETFRIENDSTATUS = "getfriendsstatus";

    public static final String METHODE_GETUSERPAGEDATA = "getuserpagedata";

    public static final String METHODE_GETUSERPAGEIMAGE = "getuserpageimage";

    public static final String METHODE_UPDATESTATUS = "updatestatus";

    public static final String METHODE_RESETSTATUS = "resetstatus";

    public static final String METHODE_GETUSERID = "getuserid";

    public static final String METHODE_GETUSERNAME = "getusername";

    public static final String METHODE_ADDSTATUSCOMMENT = "addstatuscomment";

    public static final String METHODE_GETSTATUSCOMMENTS = "getstatuscomments";

    private RequestInterface rq;

    /**
	 * 
	 * @param rq @see {@link RequestInterface}
	 */
    public SectionUser(RequestInterface rq) {
        this.rq = rq;
    }

    /**
	 * Logged einen 
	 * 
	 * @param username Usernmane des Users
	 * @param password Passwort des Users
	 * @return {@link ObjectUser}
	 * @throws {@link ErrorcodeException}
	 */
    public ObjectUser Methode_Login(String username, String password) throws ErrorcodeException {
        String passwordmd5 = MD5.getHashString(password);
        String param[] = new String[] { "username", "password" };
        String value[] = new String[] { username, passwordmd5 };
        MyParser xml = rq.Request(this.SECTION, SectionUser.METHODE_LOGIN, param, value);
        while (!xml.getStatus()) ;
        ObjectUser obj = (ObjectUser) xml.getWeblifeObject();
        if (obj.Error()) throw new ErrorcodeException(obj.getErrorcode(), obj.getErrormessage());
        ObjectUser user = (ObjectUser) xml.getWeblifeObject();
        user.setPassword(passwordmd5);
        this.rq.SetUser(user);
        rq.GetUser().setUsername(username);
        System.out.println("Setze Authtoken: " + this.rq.GetUser().getAuthtoken());
        return user;
    }

    /**
	 * Logged einen User vom dem System aus
	 */
    public void Methode_Logout() {
        MyParser xml = rq.Request(this.SECTION, SectionUser.METHODE_LOGOUT, null, null);
        while (!xml.getStatus()) ;
    }

    /**
	 * 
	 * @param username Name des Users
	 * @return {@link ObjectUser}
	 * @throws {@link ErrorcodeException}
	 * @deprecated
	 */
    public ObjectUser Methode_Getcurrentstatus(String username) throws ErrorcodeException {
        String param[] = new String[] { "username" };
        String value[] = new String[] { username };
        MyParser xml = rq.Request(this.SECTION, SectionUser.METHODE_GETCURRENTSTATUS, param, value);
        while (!xml.getStatus()) ;
        ObjectUser obj = (ObjectUser) xml.getWeblifeObject();
        if (obj.Error()) throw new ErrorcodeException(obj.getErrorcode(), obj.getErrormessage());
        return (ObjectUser) xml.getWeblifeObject();
    }

    /**
	 * 
	 * @param statusage Alter der Status�nderungen
	 * @param limit Max Anzahl der Status�nderungen
	 * @return {@link ObjectUser}
	 * @throws {@link ErrorcodeException}
	 * @deprecated
	 */
    public ObjectUser Methode_Getfriendsstatus(String statusage, String limit) throws ErrorcodeException {
        String param[] = new String[] { "statusage", "limit" };
        String value[] = new String[] { statusage, limit };
        MyParser xml = rq.Request(this.SECTION, SectionUser.METHODE_GETFRIENDSTATUS, param, value);
        while (!xml.getStatus()) ;
        ObjectUser obj = (ObjectUser) xml.getWeblifeObject();
        if (obj.Error()) throw new ErrorcodeException(obj.getErrorcode(), obj.getErrormessage());
        return (ObjectUser) xml.getWeblifeObject();
    }

    /**
	 * 
	 * @param statusid Statusid des Status
	 * @param limit Anzahl der anzuzeigenden Kommentaren
	 * @return {@link ObjectUser}
	 * @throws {@link ErrorcodeException}
	 * @deprecated
	 */
    public ObjectUser Methode_Getstatuscomments(String statusid, String limit) throws ErrorcodeException {
        String param[] = new String[] { "statusid", "limit" };
        String value[] = new String[] { statusid, limit };
        MyParser xml = rq.Request(this.SECTION, SectionUser.METHODE_GETSTATUSCOMMENTS, param, value);
        while (!xml.getStatus()) ;
        ObjectUser obj = (ObjectUser) xml.getWeblifeObject();
        if (obj.Error()) throw new ErrorcodeException(obj.getErrorcode(), obj.getErrormessage());
        return (ObjectUser) xml.getWeblifeObject();
    }

    /**
	 * 
	 * @param statusid Statusid des Status
	 * @param comment Nachricht
	 * @return {@link ObjectUser}
	 * @throws {@link ErrorcodeException}
	 * @deprecated
	 */
    public ObjectUser Methode_Addstatuscomment(String statusid, String comment) throws ErrorcodeException {
        String param[] = new String[] { "statusid", "comment" };
        String value[] = new String[] { statusid, comment };
        MyParser xml = rq.Request(this.SECTION, SectionUser.METHODE_ADDSTATUSCOMMENT, param, value);
        while (!xml.getStatus()) ;
        ObjectUser obj = (ObjectUser) xml.getWeblifeObject();
        if (obj.Error()) throw new ErrorcodeException(obj.getErrorcode(), obj.getErrormessage());
        return (ObjectUser) xml.getWeblifeObject();
    }

    /**
	 * 
	 * @param message Text des neuen Status
	 * @return {@link ObjectUser}
	 * @throws {@link ErrorcodeException}
	 * @deprecated
	 */
    public ObjectUser Methode_Updatestatus(String message) throws ErrorcodeException {
        String param[] = new String[] { "message" };
        String value[] = new String[] { message };
        MyParser xml = rq.Request(this.SECTION, SectionUser.METHODE_UPDATESTATUS, param, value);
        while (!xml.getStatus()) ;
        ObjectUser obj = (ObjectUser) xml.getWeblifeObject();
        if (obj.Error()) throw new ErrorcodeException(obj.getErrorcode(), obj.getErrormessage());
        return (ObjectUser) xml.getWeblifeObject();
    }

    /**
	 * 
	 * @return {@link ObjectUser}
	 * @throws {@link ErrorcodeException}
	 * @deprecated
	 */
    public ObjectUser Methode_Resetstatus() throws ErrorcodeException {
        String param[] = new String[] {};
        String value[] = new String[] {};
        MyParser xml = rq.Request(this.SECTION, SectionUser.METHODE_RESETSTATUS, param, value);
        while (!xml.getStatus()) ;
        ObjectUser obj = (ObjectUser) xml.getWeblifeObject();
        if (obj.Error()) throw new ErrorcodeException(obj.getErrorcode(), obj.getErrormessage());
        return (ObjectUser) xml.getWeblifeObject();
    }

    /**
	 * 
	 * @param userid Userid des Users 
	 * @return {@link ObjectUser}
	 * @throws {@link ErrorcodeException}
	 */
    public ObjectUser Methode_Getuserpagedata(String userid) throws ErrorcodeException {
        String param[] = new String[] { "userid" };
        String value[] = new String[] { userid };
        MyParser xml = rq.Request(this.SECTION, SectionUser.METHODE_GETUSERPAGEDATA, param, value);
        while (!xml.getStatus()) ;
        ObjectUser obj = (ObjectUser) xml.getWeblifeObject();
        if (obj.Error()) throw new ErrorcodeException(obj.getErrorcode(), obj.getErrormessage());
        return (ObjectUser) xml.getWeblifeObject();
    }

    /**
	 * 
	 * @param userid
	 * @return {@link ObjectUser}
	 * @throws {@link ErrorcodeException}
	 */
    public ObjectUser Methode_Getuserpageimage(String userid) throws ErrorcodeException {
        String param[] = new String[] { "userid" };
        String value[] = new String[] { userid };
        MyParser xml = rq.Request(this.SECTION, SectionUser.METHODE_GETUSERPAGEIMAGE, param, value);
        while (!xml.getStatus()) ;
        ObjectUser obj = (ObjectUser) xml.getWeblifeObject();
        if (obj.Error()) throw new ErrorcodeException(obj.getErrorcode(), obj.getErrormessage());
        return (ObjectUser) xml.getWeblifeObject();
    }

    /**
	 * 
	 * @param username Username des Users
	 * @return {@link ObjectUser}
	 * @throws {@link ErrorcodeException}
	 */
    public ObjectUser Methode_Getuserid(String username) throws ErrorcodeException {
        String param[] = new String[] { "username" };
        String value[] = new String[] { username };
        MyParser xml = rq.Request(this.SECTION, SectionUser.METHODE_GETUSERID, param, value);
        while (!xml.getStatus()) ;
        ObjectUser obj = (ObjectUser) xml.getWeblifeObject();
        if (obj.Error()) throw new ErrorcodeException(obj.getErrorcode(), obj.getErrormessage());
        return (ObjectUser) xml.getWeblifeObject();
    }

    public ObjectUser Methode_Getusername(String userid) throws ErrorcodeException {
        String param[] = new String[] { "userid" };
        String value[] = new String[] { userid };
        MyParser xml = rq.Request(this.SECTION, SectionUser.METHODE_GETUSERNAME, param, value);
        while (!xml.getStatus()) ;
        ObjectUser obj = (ObjectUser) xml.getWeblifeObject();
        if (obj.Error()) throw new ErrorcodeException(obj.getErrorcode(), obj.getErrormessage());
        return (ObjectUser) xml.getWeblifeObject();
    }
}
