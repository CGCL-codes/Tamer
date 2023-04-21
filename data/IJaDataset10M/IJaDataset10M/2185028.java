package ro.gateway.aida.obj;

import java.sql.Date;

/**
 * @author Mihai Postelnicu
 * Bookmark
 *  
 * */
public class Bookmark {

    protected String description;

    protected long id;

    protected long userId;

    protected int type;

    protected Date insDate;

    protected long objectId;

    protected String Url;

    public static final int PROJECT_BM = 1;

    public static final int ORG_BM = 2;

    public static final int PERSON_BM = 3;

    public static final int PROJECT_SEARCH_BM = 4;

    /**
	 * @return
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @return
	 */
    public long getId() {
        return id;
    }

    /**
	 * @return
	 */
    public int getType() {
        return type;
    }

    /**
	 * @return
	 */
    public long getUserId() {
        return userId;
    }

    /**
	 * @param string
	 */
    public void setDescription(String string) {
        description = string;
    }

    /**
	 * @param l
	 */
    public void setId(long l) {
        id = l;
    }

    /**
	 * @param i
	 */
    public void setType(int i) {
        type = i;
    }

    /**
	 * @param l
	 */
    public void setUserId(long l) {
        userId = l;
    }

    /**
	 * @return
	 */
    public Date getInsDate() {
        return insDate;
    }

    /**
	 * @param date
	 */
    public void setInsDate(Date date) {
        insDate = date;
    }

    /**
	 * @return
	 */
    public long getObjectId() {
        return objectId;
    }

    /**
	 * @param i
	 */
    public void setObjectId(long i) {
        objectId = i;
    }

    /**
	 * @return
	 */
    public String getUrl() {
        switch(type) {
            case PROJECT_BM:
                return "/activity/view.jsp?id=" + getObjectId();
            default:
                return null;
        }
    }
}
