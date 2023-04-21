package org.agilercp.ui.dialog;

import org.agilercp.ui.DefaultPresenter;
import org.agilercp.ui.IView;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Heiko Seeberger
 * @param <T>
 *            The type of the MVP-view. Must be an {@link IDialog}.
 * @param <U>
 *            The type of initialization data for a dialog.
 * @param <V>
 *            The type of the return data for the handler for returning form a
 *            dialog.
 */
public class DefaultDialogPresenter<T extends IDialog, U, V> extends DefaultPresenter<T> implements IDialogPresenter<T, U, V> {

    /**
     * The handler for returning form a dialog.
     */
    protected IDialogReturnHandler<V> dialogReturnHandler;

    /**
     * The initialization data for a dialog.
     */
    protected U initData;

    /**
     * @param view
     *            The MVP-view.
     * @see DefaultPresenter#DefaultPresenter(IView)
     */
    public DefaultDialogPresenter(final T view) {
        super(view);
    }

    /**
     * Stores the given initialization data for a dialog and the handler for
     * returning form a dialog in protected fields for later usage. Then sets
     * the parent shell and opens the MVP-view by calling {@link IDialog#open()}.
     * 
     * @param initData
     *            The initialization data for a dialog.
     * @param dialogReturnHandler
     *            The handler for returning form a dialog.
     * @see org.agilercp.ui.dialog.IDialogPresenter#openDialog(Object,
     *      IDialogReturnHandler, Shell)
     */
    public final int openDialog(final U initData, final IDialogReturnHandler<V> dialogReturnHandler, final Shell parentShell) {
        this.initData = initData;
        this.dialogReturnHandler = dialogReturnHandler;
        getView().setParentShell(parentShell);
        return getView().open();
    }
}
