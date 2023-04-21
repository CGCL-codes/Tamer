package it.ge.condam.util;

import java.util.List;
import javax.persistence.NoResultException;
import it.ge.condam.systemuser.Userproperty;
import it.ge.condam.systemuser.service.UserPropertiesServiceUtil;

/**
 * 
 * @author Leone
 *
 */
public class SerializeProperties {

    private static final String SPLIT_CHAR = ";";

    private List<Userproperty> userPropertyList = null;

    public SerializeProperties() {
    }

    public List<Userproperty> getUserPropertyList() {
        return userPropertyList;
    }

    public void setUserPropertyList(List<Userproperty> userPropertyList) {
        this.userPropertyList = userPropertyList;
    }

    public static boolean hasProperty(int userid, String userProperties) {
        boolean result = false;
        Userproperty userproperty = null;
        try {
            userproperty = UserPropertiesServiceUtil.findUserproperties(userid);
        } catch (NoResultException ex) {
            return false;
        }
        if (userproperty != null) {
            String properties = userproperty.getUserproperties();
            String[] propArray = properties.split(SPLIT_CHAR);
            for (int i = 0; i < propArray.length; i++) {
                if (propArray[i].trim().equalsIgnoreCase(userProperties.trim())) {
                    result = true;
                }
            }
        }
        return result;
    }
}
