package com.submersion.jspshop.propertytag;

import javax.servlet.jsp.*;
import javax.servlet.http.*;
import javax.servlet.jsp.tagext.*;
import java.security.*;
import javax.ejb.*;
import com.submersion.jspshop.rae.*;

/** Creates a new ClassProperty and returns it
 * 
 * @author Jeff Davey (jeffdavey@submersion.com)
 * @see com.submersion.jspshop.propertytag.CreateTEI
 * @version $Revision: 1.2 $
 * @created: September 26, 2001  
 * @changed: $Date: 2001/10/13 19:39:14 $
 * @changedBy: $Author: jeffdavey $
*/
public class CreateTag extends TagSupport {

    String objectID;

    String value;

    String propertyID;

    String propertyName;

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setPropertyID(String propertyID) {
        this.propertyID = propertyID;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public int doStartTag() throws JspTagException {
        HttpSession session = pageContext.getSession();
        Long userID = (Long) session.getAttribute("jspShop.userID");
        if (userID == null) {
            throw new JspTagException("You are not logged in.");
        }
        if (propertyID != null) {
            createProperty(objectID, value, propertyID, userID);
        } else {
            createPropertyName(objectID, value, propertyName, userID);
        }
        return EVAL_BODY_INCLUDE;
    }

    private void createPropertyName(String objectID, String value, String propertyName, Long userID) throws JspTagException {
        rObject object = new rObject(new Long(objectID), userID);
        ClassProperty classProperty = new ClassProperty(propertyName, object.getClassName());
        String newValue = value;
        if (classProperty.getName().equals("Password")) {
            try {
                MessageDigest crypt = MessageDigest.getInstance("MD5");
                crypt.update(value.getBytes());
                byte digest[] = crypt.digest();
                StringBuffer hexString = new StringBuffer();
                for (int i = 0; i < digest.length; i++) {
                    hexString.append(hexDigit(digest[i]));
                }
                newValue = hexString.toString();
                crypt.reset();
            } catch (NoSuchAlgorithmException e) {
                System.err.println("jspShop: Could not get instance of MD5 algorithm. Please fix this!" + e.getMessage());
                e.printStackTrace();
                throw new JspTagException("Error crypting password!: " + e.getMessage());
            }
        }
        Properties properties = new Properties(new Long(objectID), userID);
        try {
            Property property = properties.create(classProperty.getID(), newValue);
            pageContext.setAttribute(getId(), property);
        } catch (CreateException e) {
            throw new JspTagException("Could not create PropertyValue, CreateException: " + e.getMessage());
        }
    }

    private void createProperty(String objectID, String value, String propertyID, Long userID) throws JspTagException {
        ClassProperty classProperty = new ClassProperty(new Long(propertyID));
        String newValue = value;
        if (classProperty.getName().equals("Password")) {
            try {
                MessageDigest crypt = MessageDigest.getInstance("MD5");
                crypt.update(value.getBytes());
                byte digest[] = crypt.digest();
                StringBuffer hexString = new StringBuffer();
                for (int i = 0; i < digest.length; i++) {
                    hexString.append(hexDigit(digest[i]));
                }
                newValue = hexString.toString();
                crypt.reset();
            } catch (NoSuchAlgorithmException e) {
                System.err.println("jspShop: Could not get instance of MD5 algorithm. Please fix this!" + e.getMessage());
                e.printStackTrace();
                throw new JspTagException("Error crypting password!: " + e.getMessage());
            }
        }
        Properties properties = new Properties(new Long(objectID), userID);
        try {
            Property property = properties.create(new Long(propertyID), newValue);
            pageContext.setAttribute(getId(), property);
        } catch (CreateException e) {
            throw new JspTagException("Could not create PropertyValue, CreateException: " + e.getMessage());
        }
    }

    private String hexDigit(byte x) {
        StringBuffer sb = new StringBuffer();
        char c;
        c = (char) ((x >> 4) & 0xf);
        if (c > 9) {
            c = (char) ((c - 10) + 'a');
        } else {
            c = (char) (c + '0');
        }
        sb.append(c);
        c = (char) (x & 0xf);
        if (c > 9) {
            c = (char) ((c - 10) + 'a');
        } else {
            c = (char) (c + '0');
        }
        sb.append(c);
        return sb.toString();
    }
}
