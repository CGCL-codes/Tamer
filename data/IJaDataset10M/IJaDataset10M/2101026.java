package org.springframework.richclient.command.support;

import javax.swing.JFrame;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationWindow;
import org.springframework.richclient.application.config.ApplicationWindowAware;
import org.springframework.richclient.command.ActionCommand;

/**
 * A skeleton implementation of an action command that needs to be aware of the
 * {@link ApplicationWindow} in which it resides. 
 * 
 * @author Keith Donald
 */
public abstract class ApplicationWindowAwareCommand extends ActionCommand implements ApplicationWindowAware {

    private ApplicationWindow window;

    /**
     * Creates a new uninitialized {@code ApplicationWindowAwareCommand}.
     *
     */
    protected ApplicationWindowAwareCommand() {
    }

    /**
     * Creates a new {@code ApplicationWindowAwareCommand} with the given command identifier.
     *
     * @param commandId The identifier of this command instance. This should be unique amongst
     * all comands within the application.
     */
    protected ApplicationWindowAwareCommand(String commandId) {
        super(commandId);
    }

    /**
     * {@inheritDoc}
     */
    public void setApplicationWindow(ApplicationWindow window) {
        this.window = window;
    }

    /**
     * Returns the application window that this component was created within.
     * @return The application window, or null if this property has not yet been initialized.
     */
    protected ApplicationWindow getApplicationWindow() {
        return window;
    }

    /**
     * Returns the {@link JFrame} of the application window that this command belongs to.
     *
     * @return The control component of the application window, never null.
     */
    protected JFrame getParentWindowControl() {
        final ApplicationWindow applicationWindow = getApplicationWindow();
        if (applicationWindow == null) {
            return Application.instance().getActiveWindow().getControl();
        }
        return applicationWindow.getControl();
    }
}
