package com.roha.xmlparsing;

/**
 * User: Ronald
 * Date: 20-mei-2004
 * Time: 13:34:50
 */
public class XMLElementSearch {

    /**
     * searchName is the name of the xmlElement that will be searched for.
     */
    private String searchName = null;

    /**
	 * searchId contains a value that must match to an id or name attribute of an xmlElement. This way you can search
	 * for specific ids. The syntax to search for this is nodename:attributename=someValue
	 */
    private String searchId = null;

    /**
	 * This method will check if there is a slash in the searchName to determine whether we want to search for an xpath
	 * like expression. If there is, then the searchname will look at the fullname and determine if this xpath
	 * expression is correct.
	 *
	 * todo: expand this to allow for * using regular expressions.
	 *
	 * @param xmlElement
	 * @return
	 */
    protected boolean isXpathQueryElementFound(XMLElement xmlElement) {
        if (searchName.indexOf("/") != -1) {
            String name = xmlElement.getFullName();
            if (searchName.startsWith("/")) {
                if (name.equals(searchName)) {
                    return isXPathQueryAttributeFound(xmlElement);
                } else {
                    if ((name + "/").equals((searchName + "/"))) {
                        return isXPathQueryAttributeFound(xmlElement);
                    }
                }
            } else {
                if ((name + "/").indexOf((searchName + "/")) != -1) {
                    return isXPathQueryAttributeFound(xmlElement);
                }
            }
        } else {
            if (xmlElement.getKey().equals(this.searchName)) {
                return isXPathQueryAttributeFound(xmlElement);
            }
        }
        return false;
    }

    /**
	 * This method will check the xmlElement and return true if the searchId isnt set. If the searchId is set then the
	 * attribute of this id will be searched for. If this isnt found, or the value isnt the same as the value in the
	 * searchId then false is returned. Please note that this value should only be called from within
	 * isXPathQueryElementFound, since that is the first step. Otherwise you could add xmlElement only because of an
	 * attribute
	 *
	 * todo maybe it could be usefull to add all kinds of different xmlElement only by a common attribute?
	 *
	 * @param xmlElement
	 * @return
	 */
    private boolean isXPathQueryAttributeFound(XMLElement xmlElement) {
        if (this.searchId == null) {
            return true;
        }
        String key = searchId.substring(0, searchId.indexOf("="));
        String value = searchId.substring(searchId.indexOf("=") + 1);
        String foundValue = (String) xmlElement.getValue(key);
        return foundValue != null && foundValue.equals(value);
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        if (searchName.indexOf(":") != -1) {
            this.searchName = searchName.substring(0, searchName.indexOf(":"));
            this.searchId = searchName.substring(searchName.indexOf(":") + 1);
        } else {
            this.searchName = searchName;
        }
    }
}
