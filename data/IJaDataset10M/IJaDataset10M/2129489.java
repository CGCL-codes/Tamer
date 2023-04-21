package com.softwoehr.pigiron.webobj.topview.functions;

import com.softwoehr.pigiron.webobj.topview.*;
import com.softwoehr.pigiron.webobj.topview.functions.FunctionProxy;

/**
 * Proxy function class to bridge JSON to PigIron
 *
 */
public class VMRMConfigurationQuery extends FunctionProxy {

    /**
     * Create an instance of the function proxy with requestor and response instanced.
     * It will consume the requestor in execution and return the response suitably modified.
     * @param requestor the requestor spawning the instance execution
     * @param response the response to be modified and returned in the execution
     */
    public VMRMConfigurationQuery(Requestor requestor, Response response) throws org.json.JSONException {
        super(requestor, response);
    }

    /**
     * Execute the PigIron VSMAPI call we have set up in this instance.
     *
     * @return                             the response from the call
     * @exception  org.json.JSONException  on JSON err
     *
     * The PigIron/VSMAPI parameters fed to the instancing within execute() are as follows:
     *   --  hostname  VSMAPI Host DNS name
     *   --  port port VSMAPI Host is listening on
     *   --  userid userid executing the function
     *   --  password the password
     *   --  target_identifier the target of the VSMAPI function
     *   --  configuration_file_name instances {@code String}
     *   --  configuration_file_type instances {@code String}
     *   --  configuration_dir_name instances {@code String}
     */
    public Response execute() throws org.json.JSONException {
        com.softwoehr.pigiron.functions.VMRMConfigurationQuery pigfunc = new com.softwoehr.pigiron.functions.VMRMConfigurationQuery(getHostSpecifier(), host.getPortNumber(), user.getUid(), user.getPassword(), getTargetIdentifier(), getInputArgumentString("configuration_file_name"), getInputArgumentString("configuration_file_type"), getInputArgumentString("configuration_dir_name"));
        execute(pigfunc, requestor, response);
        return response;
    }
}
