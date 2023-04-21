package org.labminder.ant.tasks;

import com.vmware.labmanager.*;
import org.apache.tools.ant.BuildException;
import org.labminder.ant.VMBase;
import javax.xml.ws.WebServiceException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is task used to clone a configuration.
 *
 * @ant.task category="LabMinder"
 */
public class LibraryCloneToWorkspace extends VMBase {

    private String sourceConfig;

    private String destinationConfig;

    private ArrayList<Virtualmachine> virtualmachines = new ArrayList<Virtualmachine>();

    /**
     * Name of the configuration that will be cloned.
     *
     * @ant.required
     */
    public void setSourceConfig(String source) {
        this.sourceConfig = source;
    }

    /**
     * Name of the configuration that will be created as a result of the clone.
     *
     * @ant.required
     */
    public void setDestinationConfig(String destination) {
        destinationConfig = destination;
    }

    /**
     * Factory method for creating nested 'message's.
     */
    public Virtualmachine createVirtualmachine() {
        Virtualmachine vm = new Virtualmachine();
        virtualmachines.add(vm);
        return vm;
    }

    /**
     * A nested 'message'.
     */
    public static class Virtualmachine {

        public Virtualmachine() {
        }

        String datastore;

        public String getDatastore() {
            return datastore;
        }

        public void setDatastore(String datastore) {
            this.datastore = datastore;
        }

        /**
         * Virtualmachine Name.
         */
        String name;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    protected void soapInvoker(LabManagerX0020SOAPX0020InterfaceSoap proxy) {
        if (virtualmachines.size() == 0) {
            throw new BuildException("At least one virtualmachine element must be specified for this call");
        }
        HashMap<String, Virtualmachine> vmap = new HashMap<String, Virtualmachine>();
        for (Virtualmachine vm : virtualmachines) {
            vmap.put(vm.name, vm);
        }
        ArrayOfVMCopyData avmcd = new ArrayOfVMCopyData();
        manditoryAttribute(sourceConfig, "sourceConfig");
        manditoryAttribute(destinationConfig, "destinationConfig");
        Configuration sConfig = proxy.getSingleConfigurationByName(sourceConfig);
        Configuration dConfig = null;
        try {
            dConfig = proxy.getSingleConfigurationByName(destinationConfig);
        } catch (WebServiceException wse) {
        }
        ArrayOfMachine machines = proxy.listMachines(sConfig.getId());
        for (Machine m : machines.getMachines()) {
            Virtualmachine vm = vmap.get(m.getName());
            if (vm != null) {
                VMCopyData vmcd = new VMCopyData();
                vmcd.setMachine(m);
                if (vm.getDatastore() != null) {
                    vmcd.setStorageServerName(vm.getDatastore());
                } else {
                    vmcd.setStorageServerName(m.getDatastoreNameResidesOn());
                }
                avmcd.getVMCopyDatas().add(vmcd);
            }
        }
        if (avmcd.getVMCopyDatas().size() != virtualmachines.size()) {
            throw new BuildException("VM Copy Data did not match what was found");
        }
        boolean isNewConfiguration;
        int existingConfigId = 0;
        if (dConfig == null) {
            isNewConfiguration = true;
        } else {
            isNewConfiguration = false;
            existingConfigId = dConfig.getId();
        }
        log("Starting to clone configuration " + sourceConfig);
        proxy.libraryCloneToWorkspace(sConfig.getId(), getWorkSpaceID(), isNewConfiguration, destinationConfig, "Ant Automation Comment", avmcd, existingConfigId, false, 0);
        log(sourceConfig + " has been successfully cloned as " + destinationConfig);
        dConfig = proxy.getSingleConfigurationByName(destinationConfig);
        dConfig.setIsPublic(true);
        configurationInfo(destinationConfig);
    }
}
