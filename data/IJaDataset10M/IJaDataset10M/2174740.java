package org.eclipse.ui.internal.decorators;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.WorkbenchPlugin;

/**
 * The RunnableDecoratorDefinition is the definition for 
 * decorators that have an ILabelDecorator class to instantiate.
 */
class FullDecoratorDefinition extends DecoratorDefinition {

    ILabelDecorator decorator;

    /**
     * Create a new instance of the receiver with the
     * supplied values.
     */
    FullDecoratorDefinition(String identifier, IConfigurationElement element) {
        super(identifier, element);
    }

    /**
     * Gets the decorator and creates it if it does
     * not exist yet. Throws a CoreException if there is a problem
     * creating the decorator.
     * This method should not be called unless a check for
     * enabled to be true is done first.
     * @return Returns a ILabelDecorator
     */
    protected ILabelDecorator internalGetDecorator() throws CoreException {
        if (labelProviderCreationFailed) {
            return null;
        }
        final CoreException[] exceptions = new CoreException[1];
        if (decorator == null) {
            Platform.run(new SafeRunnable(NLS.bind(WorkbenchMessages.DecoratorManager_ErrorActivatingDecorator, getName())) {

                public void run() {
                    try {
                        decorator = (ILabelDecorator) WorkbenchPlugin.createExtension(definingElement, DecoratorDefinition.ATT_CLASS);
                        decorator.addListener(WorkbenchPlugin.getDefault().getDecoratorManager());
                    } catch (CoreException exception) {
                        exceptions[0] = exception;
                    }
                }
            });
        } else {
            return decorator;
        }
        if (decorator == null) {
            this.labelProviderCreationFailed = true;
            setEnabled(false);
        }
        if (exceptions[0] != null) {
            throw exceptions[0];
        }
        return decorator;
    }

    protected void refreshDecorator() {
        if (!this.enabled && decorator != null) {
            IBaseLabelProvider cached = decorator;
            decorator = null;
            disposeCachedDecorator(cached);
        }
    }

    /**
     * Decorate the image provided for the element type.
     * This method should not be called unless a check for
     * isEnabled() has been done first.
     * Return null if there is no image or if an error occurs.
     */
    Image decorateImage(Image image, Object element) {
        try {
            ILabelDecorator currentDecorator = internalGetDecorator();
            if (currentDecorator != null) {
                return currentDecorator.decorateImage(image, element);
            }
        } catch (CoreException exception) {
            handleCoreException(exception);
        }
        return null;
    }

    /**
     * Decorate the text provided for the element type.
     * This method should not be called unless a check for
     * isEnabled() has been done first.
     * Return null if there is no text or if there is an exception.
     */
    String decorateText(String text, Object element) {
        try {
            ILabelDecorator currentDecorator = internalGetDecorator();
            if (currentDecorator != null) {
                return currentDecorator.decorateText(text, element);
            }
        } catch (CoreException exception) {
            handleCoreException(exception);
        }
        return null;
    }

    /**
     * Returns the decorator, or <code>null</code> if not enabled.
     * 
     * @return the decorator, or <code>null</code> if not enabled
     */
    public ILabelDecorator getDecorator() {
        return decorator;
    }

    protected IBaseLabelProvider internalGetLabelProvider() throws CoreException {
        return internalGetDecorator();
    }

    public boolean isFull() {
        return true;
    }
}
