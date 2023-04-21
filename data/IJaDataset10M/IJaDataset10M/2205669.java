package org.wsmostudio.repository.ordi;

import java.io.File;
import java.io.IOException;
import java.util.*;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.wsmo.datastore.WsmoRepository;
import org.wsmo.factory.Factory;
import org.wsmostudio.runtime.*;
import org.wsmostudio.runtime.extension.Configurator;
import com.ontotext.ordi.trree.TRREEAdapter;

public class ORDIRepositoryConfigurator implements Configurator {

    @SuppressWarnings("unchecked")
    public Map getConfigurationData(WsmoRepository instance, boolean forceConfig) {
        if (forceConfig == true) {
            MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Not Configurable Repository", "The selected repository '" + instance.getDescription() + "' can not be configured!");
            return null;
        }
        HashMap confData = new HashMap();
        confData.put(Factory.WSMO_FACTORY, WSMORuntime.getRuntime().getWsmoFactory());
        confData.put(Factory.LE_FACTORY, WSMORuntime.getRuntime().getLogExprFactory());
        confData.put(Factory.DATA_FACTORY, WSMORuntime.getRuntime().getDataFactory());
        String workspacePath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString() + "/.metadata/";
        confData.put(TRREEAdapter.STORAGE_DIRECTORY, workspacePath);
        File entitypoolBin = new File(workspacePath + "/entitypool.bin");
        try {
            entitypoolBin.createNewFile();
        } catch (IOException e) {
            LogManager.logError(e);
        }
        return confData;
    }
}
