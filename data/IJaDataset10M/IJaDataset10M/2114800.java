package com.dbxml.db.client.tools.command;

import java.io.PrintWriter;
import com.dbxml.db.client.CollectionClient;
import com.dbxml.db.client.tools.CommandLine;
import com.dbxml.db.common.security.AccessManagerClient;
import com.dbxml.util.dbXMLException;

/**
 * RemoveRole (RMROLE) removes a Role from the Database.
 */
public class RemoveRole extends SingletonBase {

    protected String getArgumentName() {
        return "Role ID";
    }

    protected void process(CollectionClient col, String argument) throws dbXMLException {
        PrintWriter pw = cl.getWriter();
        if (cl.isPropertyTrue(CommandLine.CONFIRM)) {
            try {
                String ans = cl.readLine("Delete Role '" + argument + "' (y/N)? ");
                if (!ans.equalsIgnoreCase("yes") && !ans.equalsIgnoreCase("y")) {
                    pw.println("Role deletion canceled");
                    return;
                }
            } catch (Exception e) {
                throw new dbXMLException("Error obtaining answer");
            }
        }
        AccessManagerClient manager = new AccessManagerClient(cl.getClient());
        manager.removeRole(argument);
        pw.println("Role '" + argument + "' removed");
    }
}
