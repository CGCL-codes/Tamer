package de.iritgo.aktera.base.database;

import de.iritgo.aktera.base.server.*;
import de.iritgo.aktera.model.Command;
import de.iritgo.aktera.model.ModelException;
import de.iritgo.aktera.model.ModelRequest;
import de.iritgo.aktera.model.ModelResponse;
import de.iritgo.aktera.model.StandardLogEnabledModel;

/**
 * @avalon.component
 * @avalon.service type="de.iritgo.aktera.model.Model"
 * @x-avalon.info name="aktera.database.update-prompt"
 * @model.model name="aktera.database.update-prompt" id="aktera.database.update-prompt" logger="aktera"
 * @model.attribute name="forward" value="aktera.database.update-prompt"
 */
public class PromptUpdateDatabase extends StandardLogEnabledModel {

    /**
	 * Execute the model.
	 *
	 * @param req The model request.
	 * @throws ModelException In case of a business failure.
	 */
    public ModelResponse execute(ModelRequest req) throws ModelException {
        SystemFirewall.disable();
        ModelResponse res = req.createResponse();
        Command cmd = res.createCommand("aktera.database.update");
        cmd.setName("update");
        cmd.setLabel("$updateDatabase");
        res.add(cmd);
        return res;
    }
}
