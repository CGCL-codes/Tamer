package org.impalaframework.module.operation;

import static org.easymock.EasyMock.expect;
import org.impalaframework.module.definition.SimpleModuleDefinition;
import org.impalaframework.module.spi.TransitionResultSet;

public class AddModuleOperationTest extends BaseModuleOperationTest {

    protected LockingModuleOperation getOperation() {
        AddModuleOperation operation = new AddModuleOperation();
        operation.setModificationExtractorRegistry(modificationExtractorRegistry);
        operation.setFrameworkLockHolder(frameworkLockHolder);
        operation.setTransitionManager(transitionManager);
        return operation;
    }

    public final void testInvalidArgs() {
        try {
            operation.execute(application, new ModuleOperationInput(null, null, null));
        } catch (IllegalArgumentException e) {
            assertEquals("moduleName is required as it specifies the name of the module to add in org.impalaframework.module.operation.AddModuleOperation", e.getMessage());
        }
    }

    public final void testExecute() {
        SimpleModuleDefinition moduleDefinition = new SimpleModuleDefinition("mymodule");
        expect(moduleStateHolder.cloneRootModuleDefinition()).andReturn(originalDefinition);
        expect(moduleStateHolder.cloneRootModuleDefinition()).andReturn(newDefinition);
        expect(stickyModificationExtractor.getTransitions(application, originalDefinition, newDefinition)).andReturn(transitionSet);
        newDefinition.addChildModuleDefinition(moduleDefinition);
        expect(transitionManager.processTransitions(moduleStateHolder, application, transitionSet)).andReturn(new TransitionResultSet());
        replayMocks();
        assertEquals(ModuleOperationResult.EMPTY, operation.doExecute(application, new ModuleOperationInput(null, moduleDefinition, null)));
        verifyMocks();
    }
}
