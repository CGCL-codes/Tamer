package com.ivis.xprocess.ui.editors;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import com.ivis.xprocess.core.State;
import com.ivis.xprocess.core.StateSet;
import com.ivis.xprocess.ui.datawrappers.workflow.StateWrapper;
import com.ivis.xprocess.ui.refresh.ChangeRecord;
import com.ivis.xprocess.ui.refresh.ChangeEventFactory.ChangeEvent;
import com.ivis.xprocess.ui.util.TestHarness;

public class StateSetEditor extends ElementEditor {

    private String schemaName = "stateset_schema.xml";

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        super.init(site, input);
    }

    @Override
    protected void createPages() {
        super.createPages();
        if (elementWrapper != null) {
            Composite editorPage = getContainer();
            TestHarness.name(editorPage, "Editor:" + elementWrapper.getLabel());
            attachHooks();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(Class adapter) {
        return super.getAdapter(adapter);
    }

    @Override
    public String getSchemaName() {
        return schemaName;
    }

    @Override
    public void refreshEvent(ChangeRecord changeRecord) {
        if (hasBeenExternallyChanged() || (elementWrapper == null)) {
            return;
        }
        super.refreshEvent(changeRecord);
        if (changeRecord.hasChange(ChangeEvent.USERDEFINED_PROPERTY_ADDED) || changeRecord.hasChange(ChangeEvent.USERDEFINED_PROPERTY_REMOVED)) {
            if (changeRecord.getElement() == elementWrapper.getElement().getSchema()) {
                if (canRefresh() && !getContainer().isDisposed()) {
                    recreatePagesAsync();
                } else {
                    setNeedsRefreshAfterEditing(true);
                }
            }
        }
        if (changeRecord.getElementWrapper() instanceof StateWrapper) {
            StateSet stateSet = (StateSet) getElement();
            for (State state : stateSet.getStatesInOrder()) {
                if (state.getUuid().equals(changeRecord.getElementWrapper().getUuid())) {
                    if (canRefresh() && !getContainer().isDisposed()) {
                        recreatePagesAsync();
                    } else {
                        setNeedsRefreshAfterEditing(true);
                    }
                    break;
                }
            }
        }
    }
}
