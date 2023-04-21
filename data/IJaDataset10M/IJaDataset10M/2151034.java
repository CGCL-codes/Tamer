package com.dbxml.db.client.tools.command;

import java.io.PrintWriter;
import com.dbxml.db.client.CollectionClient;
import com.dbxml.db.common.security.AccessManagerClient;
import com.dbxml.util.dbXMLException;

/**
 * AddRole (ADDROLE) adds a new Role to the Database.
 */
public class AddRole extends SingletonBase {

    protected String getArgumentName() {
        return "Role ID";
    }

    protected void process(CollectionClient col, String argument) throws dbXMLException {
        AccessManagerClient manager = new AccessManagerClient(cl.getClient());
        manager.addRole(argument);
        PrintWriter pw = cl.getWriter();
        pw.println("Role '" + argument + "' added");
    }
}
