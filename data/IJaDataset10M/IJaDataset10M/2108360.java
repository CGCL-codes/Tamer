package com.bluebrim.base.shared;

/**
 * Class that holds a modifiable name to be displayed to the user.
 *
 * @author Markus Persson 1999-05-17
 */
public class CoPlainName implements CoNameHandler {

    private String m_name;

    public CoPlainName() {
    }

    public CoPlainName(String name) {
        m_name = name;
    }

    public String getKey() {
        return null;
    }

    public String getName() {
        return (m_name != null) ? m_name : CoStringResources.getName(CoConstants.UNTITLED);
    }

    public boolean isRenameable() {
        return true;
    }

    public void setName(String name) {
        m_name = ("".equals(name) ? null : name);
    }
}
