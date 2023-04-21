package com.spring.rssReader.web;

import com.spring.rssReader.Role;
import java.util.List;

/**
 * @author Ronald Haring
 * Date: 18-jan-2004
 * Time: 21:37:09
 * To change this template use Options | File Templates.
 */
public interface IRoleController {

    List getRoles();

    void deleteRole(long id);

    Role getRole(long id);

    boolean isUniqueRole(Role role);

    void save(Role role);

    Role findRole(String role);
}
