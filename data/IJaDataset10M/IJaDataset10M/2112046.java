package com.hyper9.vmm.client;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The view for displaying a VM.
 * 
 * @author akutz
 * 
 */
public class VMView extends ViewImpl implements ReceivesUpdates {

    private ServerObjectVM vm;

    private ServerObjectVMNetwork[] vmNetworks;

    final VMPanel vmPanel;

    final VMNetPanel netPanel;

    /**
     * Instantiates a new ListView object.
     * 
     * @param vm The VM this view presents.
     * @param vmNetworks The networks available to the VM.
     */
    public VMView(ServerObjectVM vm, ServerObjectVMNetwork[] vmNetworks) {
        super();
        getWidget().getElement().setId("vmView");
        this.vm = vm;
        this.vmPanel = new VMPanel(this, this.vm);
        add(this.vmPanel);
        if ((vm.getRequestedPropertiesMask() & ServerObjectVM.NETWORK) > 0) {
            this.vmNetworks = vmNetworks;
            this.netPanel = new VMNetPanel(this, this.vm, this.vmNetworks);
            add(this.netPanel);
        } else {
            this.netPanel = null;
        }
    }

    @Override
    public void onPushOn() {
        super.onPushOn();
        Vmm.setVmName(this.vm.getName());
        if ((vm.getRequestedPropertiesMask() & ServerObjectVM.CPU) > 0 || (vm.getRequestedPropertiesMask() & ServerObjectVM.CPU_MAX) > 0 || (vm.getRequestedPropertiesMask() & ServerObjectVM.MEM) > 0 || (vm.getRequestedPropertiesMask() & ServerObjectVM.MEM_MAX) > 0 || (vm.getRequestedPropertiesMask() & ServerObjectVM.POWER_STATE) > 0 || (vm.getRequestedPropertiesMask() & ServerObjectVM.NETWORK) > 0) {
            UpdateManager.startChecking();
            UpdateManager.registerForUpdates(this, this.vm);
            addVMtoWatchedObjects();
        }
    }

    /**
     * Adds the VM to the watched objects list on the server.
     */
    private void addVMtoWatchedObjects() {
        Vmm.getSS().addWatchedObjects(AccountPanel.getAuthCookie(), new ServerObject[] { this.vm }, new AsyncCallback<ServerObject[]>() {

            public void onFailure(Throwable caught) {
                AccountPanel.setError("while adding vm to watched objects", caught);
            }

            public void onSuccess(ServerObject[] result) {
                Log.debug("pushed on vm view");
            }
        });
    }

    private void unregisterForUpdates(final String successMsg) {
        if ((vm.getRequestedPropertiesMask() & ServerObjectVM.CPU) > 0 || (vm.getRequestedPropertiesMask() & ServerObjectVM.CPU_MAX) > 0 || (vm.getRequestedPropertiesMask() & ServerObjectVM.MEM) > 0 || (vm.getRequestedPropertiesMask() & ServerObjectVM.MEM_MAX) > 0 || (vm.getRequestedPropertiesMask() & ServerObjectVM.POWER_STATE) > 0 || (vm.getRequestedPropertiesMask() & ServerObjectVM.NETWORK) > 0) {
            UpdateManager.stopChecking();
            UpdateManager.unregisterForUpdates(this.vm);
            Vmm.getSS().removeWatchedObjects(AccountPanel.getAuthCookie(), new ServerObject[] { this.vm }, new AsyncCallback<ServerObject[]>() {

                public void onFailure(Throwable caught) {
                    AccountPanel.setError("while removing vm from watched objects", caught);
                }

                public void onSuccess(ServerObject[] result) {
                    Log.debug(successMsg);
                }
            });
        }
    }

    @Override
    public void onPop() {
        super.onPop();
        unregisterForUpdates("popped off vm view");
    }

    @Override
    public void onPushOff() {
        super.onPushOff();
        Vmm.setVmName(null);
        unregisterForUpdates("pushed off vm view");
    }

    /**
     * Halts the processing of user input.
     */
    void haltUserInput() {
    }

    /**
     * Resumes the processing of user input.
     */
    void resumeUserInput() {
    }

    public void onUpdate(ServerObject updatedServerObject) {
        if (updatedServerObject.getClass() == ServerObjectVM.class) {
            Log.debug("forwarding vm update");
            this.vmPanel.onUpdate(updatedServerObject);
            if (this.netPanel != null) {
                this.netPanel.onUpdate(updatedServerObject);
            }
        } else if (updatedServerObject.getClass() == ServerObjectTask_SetNetwork.class) {
            Log.debug("forwarding set network task update");
            this.netPanel.onUpdate(updatedServerObject);
        } else if (updatedServerObject.getClass() == ServerObjectTask_VMPowerOp.class) {
            Log.debug("forwarding power op task update");
            this.vmPanel.onUpdate(updatedServerObject);
        }
    }
}
