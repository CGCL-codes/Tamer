package net.sf.dynxform.container.cocoon.generation.preview;

import net.sf.dynxform.container.FlowController;
import net.sf.dynxform.container.cocoon.ConfigurableSourceModule;
import net.sf.dynxform.container.cocoon.generation.CocoonModuleFacade;
import net.sf.dynxform.container.cocoon.generation.load.CocoonFlowControllerImpl;
import net.sf.dynxform.form.data.ModuleType;
import net.sf.dynxform.form.data.module.AbstractModuleProvider;
import net.sf.dynxform.form.data.module.SourceModule;
import org.apache.cocoon.environment.Request;

/**
 * net.sf.dynxform.form.container.cocoon Feb 20, 2004 4:14:38 PM andreyp
 * Copyright (c) dynxform.sf.net. All Rights Reserved
 */
public final class PreviewFormProvider extends AbstractModuleProvider implements CocoonModuleFacade {

    private static PreviewFormProvider factory = null;

    private ConfigurableSourceModule requestProducer = null;

    private ConfigurableSourceModule requestAttrProducer = null;

    private ConfigurableSourceModule sessionProducer = null;

    private ConfigurableSourceModule stateProducer = null;

    private CocoonFlowControllerImpl flowController = null;

    private PreviewFormProvider() {
        requestProducer = new ParametersModule(ModuleType.REQUEST);
        requestAttrProducer = new ParametersModule(ModuleType.REQUEST_ATTR);
        stateProducer = new ParametersModule(ModuleType.STATE);
        sessionProducer = new SessionModule();
        flowController = new CocoonFlowControllerImpl();
    }

    public static synchronized PreviewFormProvider getInstance() {
        if (factory == null) {
            factory = new PreviewFormProvider();
        }
        return factory;
    }

    public final void setup(final Request request) {
        requestProducer.configure(request);
        requestAttrProducer.configure(request);
        sessionProducer.configure(request);
        stateProducer.configure(request);
    }

    public final SourceModule getRequestModule() {
        return requestProducer;
    }

    public final SourceModule getRequestAttrModule() {
        return requestAttrProducer;
    }

    public final SourceModule getSessionModule() {
        return sessionProducer;
    }

    public final FlowController getFlowController() {
        return flowController;
    }

    public final SourceModule getStateModule() {
        return stateProducer;
    }
}
