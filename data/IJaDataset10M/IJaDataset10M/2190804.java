package com.volantis.mcs.eclipse.builder.editors.common;

import com.volantis.mcs.eclipse.builder.BuilderPlugin;
import com.volantis.mcs.eclipse.builder.common.ResourceDiagnosticsAdapter;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessException;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessor;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessorFactory;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.operation.Operation;
import com.volantis.mcs.model.descriptor.ModelDescriptor;
import com.volantis.mcs.policies.PolicyModel;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A context containing contextual information shared between parts of an
 * editor.
 */
public abstract class EditorContext {

    /**
     * Logger for this class.
     */
    private static final Logger logger = Logger.getLogger(EditorContext.class);

    private static final PolicyFileAccessorFactory POLICY_FILE_ACCESSOR_FACTORY = PolicyFileAccessorFactory.getDefaultInstance();

    /**
     * The interaction model around the model object being edited.
     */
    private Proxy interactionModel;

    /**
     * A list of dirty state listeners.
     */
    private List dirtyStateListeners = new ArrayList();

    /**
     * Flag to indicate whether the model is dirty (ie. edited and unsaved).
     */
    private boolean dirty = false;

    /**
     * The project which the resource being edited forms part of.
     */
    private IProject project;

    /**
     * The resource being edited.
     */
    private IResource resource;

    public Proxy getInteractionModel() {
        return interactionModel;
    }

    /**
     * Sets the interaction model. Should only be called once, when the initial
     * value for the interaction model is set - future changes should be to the
     * underlying model object, not the interaction layer.
     *
     * @param newModel The interaction model
     * @throws IllegalStateException if a second attempt to set the model is
     *                               made
     */
    protected void setInteractionModel(Proxy newModel) {
        if (interactionModel != null) {
            throw new IllegalStateException("Interaction model already set");
        }
        interactionModel = newModel;
    }

    public void setDirty(boolean newDirty) {
        boolean oldDirty = dirty;
        dirty = newDirty;
        if (oldDirty != newDirty) {
            Iterator it = dirtyStateListeners.iterator();
            while (it.hasNext()) {
                DirtyStateListener dsl = (DirtyStateListener) it.next();
                dsl.dirtyStateChanged(newDirty);
            }
        }
    }

    /**
     * Add a listener for changes in dirty state.
     *
     * @param dsl The listener to add
     */
    public void addDirtyStateListener(DirtyStateListener dsl) {
        dirtyStateListeners.add(dsl);
    }

    /**
     * Remove a listener for changes in dirty state.
     *
     * @param dsl The listener to remove
     */
    public void removeDirtyStateListener(DirtyStateListener dsl) {
        dirtyStateListeners.remove(dsl);
    }

    public boolean isDirty() {
        return dirty;
    }

    public PolicyFileAccessor getPolicyFileAccessor() {
        return POLICY_FILE_ACCESSOR_FACTORY.getPolicyFileAccessor(project);
    }

    /**
     * Executes an operation.
     *
     * @param op The operation to execute
     * @todo later This should add the operation to the undo/redo stack.
     */
    public void executeOperation(Operation op) {
        op.execute();
    }

    /**
     * Loads a specified file resource into the context, if the context has not
     * already been initialised.
     *
     * @param file The file to load
     * @throws PolicyFileAccessException if an error occurs while loading
     */
    public abstract void loadResource(IFile file) throws PolicyFileAccessException;

    public void setResource(IResource newResource) {
        resource = newResource;
    }

    public IResource getResource() {
        return resource;
    }

    /**
     * Retrieves the type of model object edited with this context. This
     * must be a class which corresponds to a repository object with a
     * suitable JiBX mapping.
     *
     * @return a class representing the model type edited by this part
     */
    protected abstract Class getModelType();

    /**
     * Retrieves the interaction layer model descriptor for this part.
     *
     * @return the model descriptor for this part
     */
    protected ModelDescriptor getModelDescriptor() {
        return PolicyModel.MODEL_DESCRIPTOR;
    }

    public IProject getProject() {
        return project;
    }

    protected void setProject(IProject project) {
        this.project = project;
    }

    /**
     * Report all diagnostic errors against the current resource.
     */
    protected void reportErrors() {
        if (resource != null) {
            try {
                if (resource.exists()) {
                    new ResourceDiagnosticsAdapter(resource).setDiagnostics(interactionModel.getDiagnostics());
                }
            } catch (CoreException ce) {
                EclipseCommonPlugin.handleError(BuilderPlugin.getDefault(), ce);
            }
        }
    }

    /**
     * Displays an error dialog with the specified title and message.
     *
     * @param title The title of the error dialog
     * @param message The message of the error dialog
     * @param throwable
     */
    public void showErrorDialog(String title, String message, Throwable throwable) {
        StringBuffer messageBuffer = new StringBuffer();
        if (message != null) {
            messageBuffer.append(message);
        }
        String pluginId = BuilderPlugin.getDefault().getDescriptor().getUniqueIdentifier();
        Status status = new Status(Status.ERROR, pluginId, Status.ERROR, messageBuffer.toString(), throwable);
        Shell shell = Display.getCurrent().getActiveShell();
        ErrorDialog.openError(shell, title, null, status);
    }

    /**
     * Displays a warning dialog with the specified title and message.
     *
     * @param title The title of the dialog
     * @param message The message of the dialog
     */
    public void showWarningDialog(String title, String message) {
        StringBuffer messageBuffer = new StringBuffer();
        if (message != null) {
            messageBuffer.append(message);
        }
        Shell shell = Display.getCurrent().getActiveShell();
        MessageDialog.openWarning(shell, title, message);
    }
}
