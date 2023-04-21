package com.ecmdeveloper.plugin.java.wizard;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * 
 * @author Ricardo Belfor
 *
 */
public class NewEventActionClassTypePage extends NewClassTypePage {

    private static final String OBJECT_CHANGE_EVENT_IMPORT = "com.filenet.api.events.ObjectChangeEvent";

    private static final String ID_IMPORT = "com.filenet.api.util.Id";

    private static final String ON_EVENT_METHOD = "public void onEvent(ObjectChangeEvent event, Id subscriptionId) throws EngineRuntimeException" + METHOD_STUB;

    private static final String EVENT_ACTION_HANDLER_INTERFACE_NAME = "com.filenet.api.engine.EventActionHandler";

    public NewEventActionClassTypePage() {
        super(true, "New Event Action");
        setTitle("New Event Action Class");
        setDescription("Create a new Event Action class.");
    }

    public void init(IStructuredSelection selection) {
        super.init(selection);
        addSuperInterface(EVENT_ACTION_HANDLER_INTERFACE_NAME);
    }

    @Override
    protected void createTypeMembers(IType newType, ImportsManager imports, IProgressMonitor monitor) throws CoreException {
        if (createStubsButton.getSelection()) {
            String eventMethod = ON_EVENT_METHOD;
            newType.createMethod(eventMethod, null, false, monitor);
            imports.addImport(OBJECT_CHANGE_EVENT_IMPORT);
            imports.addImport(ENGINE_RUNTIME_EXCEPTION_IMPORT);
            imports.addImport(ID_IMPORT);
        }
        imports.addImport(EVENT_ACTION_HANDLER_INTERFACE_NAME);
    }
}
