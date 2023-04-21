package de.evandor.easyc.client.console;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;
import de.evandor.easyc.common.exceptions.EasyCException;
import de.evandor.easyc.common.groups.Group;
import de.evandor.easyc.common.groups.GroupBean;
import de.evandor.easyc.common.groups.IGroupsManager;
import de.evandor.easyc.common.permissions.IPermissionsManager;
import de.evandor.easyc.common.permissions.Permission;
import de.evandor.easyc.common.permissions.PermissionBean;
import de.evandor.easyc.common.roles.Role;

public class GroupsHandler {

    public void handleRequest(String string, List paramsList) throws EasyCException, NumberFormatException {
        String method = string;
        System.out.println(" >>> Calling " + method);
        IGroupsManager manager = (IGroupsManager) Console.getEntityManager(IGroupsManager.ID);
        Group[] entities;
        String[] params;
        if (paramsList != null) params = (String[]) paramsList.toArray(new String[paramsList.size()]); else params = new String[] {};
        if (manager == null) {
            System.out.println(" !!! PermissionManager is null");
        }
        if (method.equals("getall")) {
            entities = manager.getAll(null);
            for (int i = 0; i < entities.length; i++) {
                System.out.println(" " + entities[i].toString());
            }
        } else if (method.equals("add")) {
            if (!Console.checkParams(params, 3, "try permissions.add <parentID> <componentID> <name> <desc>")) return;
            GroupBean entity = new GroupBean();
            try {
                int i = 0;
                entity.setParentId(new Integer(params[i++]));
                manager.add(null, entity);
                System.out.println(" >>> Role added");
            } catch (EasyCException e) {
                System.out.println("SQL Exception: " + e.getMessage());
            }
        } else if (method.equals("del")) {
            if (!Console.checkParams(params, 1, "try roles.del <id>")) return;
            try {
                manager.delete(null, Integer.parseInt(params[0]));
                System.out.println(" >>> Role " + Integer.parseInt(params[0]) + " deleted");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (method.equals("info")) {
            if (!Console.checkParams(params, 1, "try roles.info <id>")) return;
            try {
                Group entity = manager.get(null, new Integer(params[0]));
                BeanInfo beanInfo = Introspector.getBeanInfo(Role.class);
                for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) System.out.println(pd.getDisplayName() + " : " + pd.getPropertyType().getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (method.equals("update")) {
            if (!Console.checkParams(params, 1, "try roles.update <id> <property> <newvalue>")) return;
            try {
                Group entity = manager.get(null, new Integer(params[0]));
                BeanInfo beanInfo = Introspector.getBeanInfo(Role.class);
                String property = params[1];
                for (PropertyDescriptor pd : beanInfo.getPropertyDescriptors()) {
                    if (pd.getDisplayName().toLowerCase().equals(property)) {
                        Method writePropertyMethod = pd.getWriteMethod();
                        Object[] arguments;
                        if (pd.getPropertyType().getName().equals("java.lang.Integer")) {
                            arguments = new Object[] { new Integer(params[2]) };
                        } else {
                            arguments = new Object[] { params[2] };
                        }
                        writePropertyMethod.invoke(entity, arguments);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
