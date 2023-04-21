package ast.gui.quotesrc;

import ast.DocumentController;
import ast.common.error.ASTError;
import ast.gui.DialogController;
import com.sun.star.awt.ActionEvent;
import com.sun.star.awt.XActionListener;
import com.sun.star.awt.XDialog;
import com.sun.star.lang.EventObject;

/**
 * Handler for new source dialog.
 *
 * @author feh
 * @author Chrissyx
 */
public class NewSourceDialogHandler implements XActionListener {

    /**
     * Reference to the {@link ast.DocumentController}.
     */
    private DocumentController docController;

    /**
     * Dialog file to handle its events.
     */
    private XDialog xDialog;

    /**
     * Default constructor.
     *
     * @param aDocController Reference to the {@link ast.DocumentController}
     * @param xDialog Dialog file being worked with
     */
    public NewSourceDialogHandler(final DocumentController aDocController, final XDialog xDialog) {
        this.docController = aDocController;
        this.xDialog = xDialog;
        this.xDialog.setTitle(this.docController.getLanguageController().__("Choose sourcestyle"));
        this.docController.getDialogUtils().registerButtons(new String[][] { new String[] { "Quote", this.docController.getLanguageController().__("Quote") }, new String[] { "WebQuote", this.docController.getLanguageController().__("Webquote") }, new String[] { "MagazineQuote", this.docController.getLanguageController().__("Magazinequote") }, new String[] { "Table", this.docController.getLanguageController().__("Table") }, new String[] { "Image", this.docController.getLanguageController().__("Image") }, this.docController.getLanguageController().captionCancel }, this.xDialog, this);
        this.docController.getDialogUtils().setTextForLBL(this.xDialog, "Name", this.docController.getLanguageController().__("Please choose a source type."));
        this.docController.getLogger().fine("NewSourceDialogHandler started");
    }

    /**
     * {@inheritDoc}
     *
     * @see com.sun.star.awt.XActionListener#actionPerformed(com.sun.star.awt.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent aActionEvent) {
        try {
            final String sName = this.docController.getDialogUtils().getAction(aActionEvent);
            final DialogController dialogController = this.docController.getDialogController();
            if (sName.equals("cmdQuote")) {
                new QuoteDialogHandler(this.docController, dialogController.loadDialog("dlgQuote.xdl"), null);
            } else if (sName.equals("cmdWebQuote")) {
                new WebQuoteDialogHandler(this.docController, dialogController.loadDialog("dlgWebQuote.xdl"), null);
            } else if (sName.equals("cmdMagazineQuote")) {
                new MagazineQuoteDialogHandler(this.docController, dialogController.loadDialog("dlgMagazineQuote.xdl"), null);
            } else if (sName.equals("cmdTable")) {
                new TableDialogHandler(this.docController, dialogController.loadDialog("dlgTable.xdl"), null);
            } else if (sName.equals("cmdImage")) {
                new ImageDialogHandler(this.docController, dialogController.loadDialog("dlgImage.xdl"), null);
            } else {
                this.xDialog.endExecute();
                return;
            }
            dialogController.showDialog();
            this.xDialog.endExecute();
        } catch (final ASTError e) {
            e.severe();
        } catch (final com.sun.star.uno.Exception e) {
            this.docController.getLogger().severe("Can't handle event! " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see com.sun.star.awt.XActionListener#disposing(com.sun.star.lang.EventObject)
     */
    @Override
    public void disposing(final EventObject aEventObject) {
        this.xDialog = null;
    }
}
