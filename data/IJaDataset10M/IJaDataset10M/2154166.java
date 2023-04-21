package org.fao.waicent.kids.giews.security;

import org.fao.waicent.kids.server.security.AccessProfileI;

/**
 *     This class defines the  user's project accessibility.
 */
public class GIEWSAccessProfile implements AccessProfileI {

    private String user_name;

    private String role;

    private String project;

    /**
     *   Creates instance of GIEWSAccessProfile and initializes the values for
     *           user_name, role, and project code.
     *
     * @param    String   user's name
     * @param    String   user's role
     * @param    String   code of the project accessible to user
     */
    public GIEWSAccessProfile(String user_name, String role, String project) {
        this.user_name = user_name;
        this.role = role;
        this.project = project;
    }

    /**
     *  This method returns the user's name
     *
     * @return      String
     */
    public String getUserName() {
        return this.user_name;
    }

    /**
     *  This method returns the user's role
     *
     * @return      String
     */
    public String getRole() {
        return this.role;
    }

    /**
     *  This method returns the project code accessible to the user
     *
     * @return      String
     */
    public String getProject() {
        return this.project;
    }
}
