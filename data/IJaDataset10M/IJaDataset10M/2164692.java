package de.iritgo.aktera.permissions.module;

import java.sql.Connection;
import org.apache.avalon.framework.logger.Logger;
import de.iritgo.aktera.model.ModelRequest;
import de.iritgo.aktera.persist.ModuleVersion;
import de.iritgo.aktera.persist.PersistentFactory;
import de.iritgo.aktera.persist.UpdateHandler;

/**
 *
 */
public class ModuleUpdateHandler extends UpdateHandler {

    @Override
    public void updateDatabase(ModelRequest req, Logger logger, Connection connection, PersistentFactory pf, ModuleVersion currentVersion, ModuleVersion newVersion) throws Exception {
        if (currentVersion.lessThan("2.2.1")) {
            createPrimaryKeySequenceFromIdTable("Permission", "permissionId");
            currentVersion.setVersion("2.2.1");
        }
        if (currentVersion.lessThan("2.3.1")) {
            renameIdColumn("permission", "permissionid", "id");
            setReboot();
            currentVersion.setVersion("2.3.1");
        }
        if (currentVersion.lessThan("2.3.2")) {
            dropColumn("Permission", "type");
            currentVersion.setVersion("2.3.2");
        }
    }
}
