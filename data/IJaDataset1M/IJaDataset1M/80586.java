package de.iritgo.aktera.aktario.akteraconnector;

import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.framework.action.ActionTools;
import de.iritgo.aktario.framework.base.DataObject;
import de.iritgo.aktario.framework.base.action.FrameworkInputStream;
import de.iritgo.aktario.framework.base.action.FrameworkOutputStream;
import de.iritgo.aktario.framework.base.action.FrameworkServerAction;
import java.io.IOException;

/**
 *
 */
public class EditAkteraObjectRequest extends FrameworkServerAction {

    private String model;

    private String jFrameId;

    private String keelObjectUniqueId;

    private String queryPaneId;

    /**
	 * Standard constructor
	 */
    public EditAkteraObjectRequest() {
    }

    /**
	 * Standard constructor
	 */
    public EditAkteraObjectRequest(String model, DataObject dataObject, String jFrameId, String queryPaneId) {
        this.model = model;
        this.keelObjectUniqueId = dataObject.getStringAttribute("keelObjectId");
        this.jFrameId = jFrameId;
        this.queryPaneId = queryPaneId;
    }

    /**
	 * Read the attributes from the given stream.
	 */
    public void readObject(FrameworkInputStream stream) throws IOException, ClassNotFoundException {
        model = stream.readUTF();
        keelObjectUniqueId = stream.readUTF();
        jFrameId = stream.readUTF();
        queryPaneId = stream.readUTF();
    }

    /**
	 * Write the attributes to the given stream.
	 */
    public void writeObject(FrameworkOutputStream stream) throws IOException {
        stream.writeUTF(model);
        stream.writeUTF(keelObjectUniqueId);
        stream.writeUTF(jFrameId);
        stream.writeUTF(queryPaneId);
    }

    /**
	 * Perform the action.
	 */
    public void perform() {
        ConnectorServerManager connectorServerManager = (ConnectorServerManager) Engine.instance().getManagerRegistry().getManager("ConnectorServerManager");
        connectorServerManager.editKeelObject(model, keelObjectUniqueId, userUniqueId);
        ActionTools.sendToClient(userUniqueId, new EditAkteraObjectResponse(model, keelObjectUniqueId, jFrameId, queryPaneId));
    }
}
