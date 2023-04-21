package org.wtc.eclipse.platform.shellhandlers;

/**
 * Start listening for the Overwrite Files Dialog and click "OK" if it is ever shown. The
 * caller should call stop listening for this dialog
 *
 * @since 3.8.0
 */
public class ConfirmOverwriteFilesDialogShellHandler extends AbstractOKToCloseShellHandler {

    /**
     * Create an instance.
     * @since 3.8.0
     */
    public ConfirmOverwriteFilesDialogShellHandler() {
        super("Overwrite Files?", true);
    }
}
