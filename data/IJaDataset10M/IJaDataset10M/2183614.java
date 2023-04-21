package com.coloradoresearch.gridster.service.admin;

import java.io.Serializable;
import com.coloradoresearch.gridster.Service;
import com.coloradoresearch.gridster.Utils;

/**
 * @author jonford
 *
 */
public class UndeployTool implements Service {

    public Serializable execute(Serializable object) {
        String toolName = (String) object;
        boolean deleted = Utils.deleteDirectory("./tools/" + toolName);
        if (deleted) {
            return "success";
        } else {
            return "failure";
        }
    }
}
