package com.centraview.common;

/**
 * @author 
 */
public abstract class ListElementMember {

    int auth;

    public abstract String getMemberType();

    public abstract String getRequestURL();

    public abstract Object getMemberValue();

    public abstract char getDisplayType();

    public abstract String getGraphicResourceURL();

    public abstract boolean getLinkEnabled();

    public abstract void setLinkEnabled(boolean enabled);

    public abstract int getAuth();

    public abstract void setAuth(int auth);

    public abstract String getDisplayString();

    public abstract String getSortString();

    /**
   * This method search the String with displayString
   * Both string convert to UpperCase and and then
   * Compare with contentEquals method.
   *
   * @param searchString The search String we are searching the
   * ListElementMember's display String.
   *
   * @return Whether or not the searchString was found.
   *
   * @see #getDisplayString()
   */
    public boolean search(String searchString) {
        if (searchString == null) {
            searchString = "";
        }
        if (searchString.length() > 8) {
            searchString = searchString.substring(8);
        }
        String searchValue = searchString.toUpperCase().trim();
        String dispValue = this.getDisplayString();
        if (dispValue == null) {
            dispValue = "";
        }
        dispValue = dispValue.toUpperCase().trim();
        int occuranceCount = dispValue.indexOf(searchValue);
        boolean searchflag = true;
        if (occuranceCount == -1) {
            searchflag = false;
        }
        return searchflag;
    }
}
