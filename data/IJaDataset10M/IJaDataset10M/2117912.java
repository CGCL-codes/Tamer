package sk.tuke.ess.editor.simulation.simeditbase.objectmodel.schema;

import sk.tuke.ess.editor.simulation.simeditbase.objectmodel.znacka.ZnackaSchema;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: zladovan
 * Date: 13.2.2012
 * Time: 10:31
 * To change this template use File | Settings | File Templates.
 */
public interface SchemaActionsListener extends Serializable {

    public void onZnackaAdd(ZnackaSchema znacka, String subSchemaNazov);

    public void onZnackaRemove(ZnackaSchema znacka, String subSchemaNazov);

    public void onConnect(ConnectionEvent event);

    public void onDisconnect(ConnectionEvent event);
}
