package org.sylfra.idea.plugins.revu.externalizing.impl;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.sylfra.idea.plugins.revu.model.User;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author <a href="mailto:syllant@gmail.com">Sylvain FRANCOIS</a>
 * @version $Id: UserConverter.java 29 2010-04-21 20:51:59Z syllant $
 */
class UserConverter extends AbstractConverter {

    public boolean canConvert(Class type) {
        return User.class.equals(type);
    }

    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        User user = (User) source;
        writer.addAttribute("displayName", user.getDisplayName());
        writer.addAttribute("login", user.getLogin());
        if (user.getPassword() != null) {
            writer.addAttribute("password", user.getPassword());
        }
        SortedSet<User.Role> roles = new TreeSet<User.Role>(user.getRoles());
        writer.addAttribute("roles", ConverterUtils.toString(roles, ",", true));
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        String displayName = reader.getAttribute("displayName");
        String login = reader.getAttribute("login");
        String password = reader.getAttribute("password");
        String roles = reader.getAttribute("roles");
        User user = new User();
        user.setDisplayName(displayName);
        user.setLogin(login);
        user.setPassword(password);
        if (roles.length() == 0) {
            roles = User.Role.AUTHOR.toString();
        }
        String[] roleNames = roles.split(",");
        Set<User.Role> roleSet = new HashSet<User.Role>();
        for (String roleName : roleNames) {
            try {
                roleSet.add(User.Role.valueOf(roleName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                logger.warn("No user role for this value: <" + roleName + "> !");
            }
        }
        user.setRoles(roleSet);
        return user;
    }
}
