package com.intel.gpe.client2.expert.explorer;

import com.intel.gpe.client2.AsyncClient;
import com.intel.gpe.client2.ListClient;
import com.intel.gpe.client2.SelectionClient;
import com.intel.gpe.client2.adapters.MessageAdapter;
import com.intel.gpe.client2.gridbeans.GridBeanJob;
import com.intel.gpe.client2.providers.FileProvider;
import com.intel.gpe.client2.security.GPESecurityManager;
import com.intel.gpe.gridbeans.NamedGPEFile;

/**
 * @version $Id: ClientAdapter.java,v 1.5 2007/02/15 14:01:36 dizhigul Exp $
 * @author Valery Shorin
 */
public class ClientAdapter extends com.intel.gpe.client2.adapters.ClientAdapter {

    private GridBeanJob gridBeanJob;

    public ClientAdapter(AsyncClient asyncClient, MessageAdapter messageAdapter, SelectionClient selectionClient, ListClient listClient, FileProvider fileProvider, GPESecurityManager securityManager) {
        super(asyncClient, messageAdapter, selectionClient, listClient, null, fileProvider, securityManager);
    }

    public GridBeanJob getGridBeanJob() {
        return gridBeanJob;
    }

    public void setGridBeanJob(GridBeanJob gridBeanJob) {
        this.gridBeanJob = gridBeanJob;
    }

    @Override
    public NamedGPEFile newTemporaryFile(String localFileName) {
        if (gridBeanJob == null) {
            return null;
        }
        return gridBeanJob.newFile(localFileName);
    }
}
