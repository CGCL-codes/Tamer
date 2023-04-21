package com.wrupple.muba.catalogs.client.activity.process.state.impl;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Provider;
import com.wrupple.muba.catalogs.client.activity.process.state.CatalogProcessDescriptorLoadingState;
import com.wrupple.muba.catalogs.domain.CatalogProcessDescriptor;
import com.wrupple.muba.common.client.application.DataCallback;
import com.wrupple.muba.common.shared.Process;
import com.wrupple.muba.common.shared.StateTransition;

public abstract class AbstractCatalogProcessDescriptorLoadingState<I, O> implements CatalogProcessDescriptorLoadingState {

    class CatalogCallback extends DataCallback<O> {

        @Override
        public void execute() {
            setValue(parameter, result);
            onDone.setResultAndFinish(parameter);
        }
    }

    protected Context context;

    protected Provider<? extends Process<I, O>> provider;

    protected CatalogProcessDescriptor parameter;

    protected StateTransition<CatalogProcessDescriptor> onDone;

    protected EventBus bus;

    public AbstractCatalogProcessDescriptorLoadingState(Provider<? extends Process<I, O>> provider) {
        super();
        this.provider = provider;
    }

    @Override
    public void start(CatalogProcessDescriptor parameter, StateTransition<CatalogProcessDescriptor> onDone, EventBus bus) {
        this.parameter = parameter;
        this.onDone = onDone;
        this.bus = bus;
        if (testCurrentState(parameter)) {
            Process<I, O> readCatalogProcess = provider.get();
            I input = generateWrappedProcessInput();
            StateTransition<O> callback = new CatalogCallback();
            context.getProcessManager().processSwitch(readCatalogProcess, "ReadVegetateBrowsingProcessDataImpl", input, callback, bus);
        } else {
            onDone.setResultAndFinish(parameter);
        }
    }

    protected abstract boolean testCurrentState(CatalogProcessDescriptor parameter);

    protected abstract I generateWrappedProcessInput();

    protected abstract void setValue(CatalogProcessDescriptor parameter, O result);

    @Override
    public void setContext(com.wrupple.muba.common.shared.State.Context context) {
        this.context = context;
    }
}
