package com.msli.rcp.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.eclipse.core.commands.operations.IOperationHistoryListener;
import org.eclipse.core.commands.operations.OperationHistoryEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import com.msli.app.model.ModelProxy;
import com.msli.core.exception.NotYetInitializedException;
import com.msli.core.util.CoreUtils;
import com.msli.rcp.app.MsliApplicationService;

/**
 * Base class for a data model editor, which uses a ModelProxy to mediate its
 * input data model and dirty state.
 * <p>
 * TODO: Includes a command stack for handling undo/redo. 
 * Currently half-baked, and may need an editor context.
 * @author jonb
 */
public abstract class ModelEditor extends EditorPart implements IOperationHistoryListener {

    /**
	 * Creates an instance.
	 * @param name Name of this editor. Never null.
	 */
    public ModelEditor(String name) {
        super();
        CoreUtils.assertNonNullArg(name);
        _name = name;
    }

    /**
	 * Gets the name of this editor.
	 * @return The editor name.  Never null.
	 */
    public String getName() {
        return _name;
    }

    /**
	 * Gets the ModelProxy used to mediate this editor's input data model.
	 * @return Shared exposed proxy.  Never null.
	 * @throws NotYetInitializedException if used before part control created.
	 */
    public abstract ModelProxy<?, ?> getModelProxy();

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
        setPartName(getEditorInput().getName());
    }

    /**
	 * Subclasses must call this superclass method AFTER the model proxy has
	 * been created.
	 * <p>
	 * {@inheritDoc}
	 */
    @Override
    public void createPartControl(final Composite parent) {
        getModelProxy().addPropertyListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                MsliApplicationService.getDisplayAssuredly().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        firePropertyChange(PROP_DIRTY);
                    }
                });
            }
        }, "modelDirty");
    }

    @Override
    public boolean isDirty() {
        return getModelProxy().isModelDirty();
    }

    @Override
    public boolean isSaveAsAllowed() {
        return true;
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        getModelProxy().saveModel();
    }

    @Override
    public void doSaveAs() {
        doSave(null);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public void historyNotification(OperationHistoryEvent event) {
        if (event.getEventType() == OperationHistoryEvent.DONE) {
            getModelProxy().setModelDirty();
        }
    }

    private String _name;
}
