package plcopen.inf.model;

import plcopen.inf.type.DateTime;

/**
 * Project를 구성하는 ContentHeader를 정의한다. 
 * @author swkim
 *
 */
public interface IContentHeader extends IPLCElement {

    public static final String ID_AUTHOR = "author";

    public static final String ID_COMMENT = "Comment";

    public static final String ID_CONTENTHEADER = "contentHeader";

    public static final String ID_COORINFO = "coordinateInfo";

    public static final String ID_DATE = "modificationDateTime";

    public static final String ID_LANGUAGE = "language";

    public static final String ID_ORGANIZATION = "organization";

    public static final String ID_VERSION = "version";

    public static final String ID_NAME = "name";

    public String getAuthor();

    public String getComment();

    public ICoordinationInfo getCoordinationInfo();

    public DateTime getDate();

    public String getLanguage();

    public String getName();

    public String getOrganization();

    public String getVersion();

    public void setAuthor(String author);

    public void setComment(String comment);

    public void setCoordinationInfo(ICoordinationInfo info);

    public void setDate(DateTime date);

    public void setLanguage(String language);

    public void setName(String name);

    public void setOrganization(String organization);

    public void setVersion(String version);
}
