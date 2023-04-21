package ast.gui.quotesrc;

import ast.DocumentController;
import ast.common.data.MagazineQuote;
import ast.common.data.Source;
import ast.common.error.ASTError;
import ast.common.error.ConfigHandlerError;
import ast.common.error.DBError;
import ast.common.util.PathUtils;
import ast.gui.DialogController;
import com.sun.star.awt.ActionEvent;
import com.sun.star.awt.XActionListener;
import com.sun.star.awt.XDialog;
import com.sun.star.lang.EventObject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Handler for new and edit magazinequote dialog.
 *
 * @author feh
 * @author Chrissyx
 */
public class MagazineQuoteDialogHandler implements XActionListener {

    /**
     * Reference to the {@link ast.DocumentController}.
     */
    private DocumentController docController;

    /**
     * Dialog file to handle its events.
     */
    private XDialog xDialog;

    /**
     * Shortinfo of the source which will be edited.
     */
    private String shortinfo;

    /**
     * Default constructor.
     *
     * @param aDocController Reference to the {@link ast.DocumentController}
     * @param xDialog Dialog file being worked with
     * @param sShortinfo Brief description of magazinequote to edit or null
     * @throws ConfigHandlerError If needed property retrieval for SourceCon failed
     * @throws DBError If database init of SourceCon failed
     */
    public MagazineQuoteDialogHandler(final DocumentController aDocController, final XDialog xDialog, final String sShortinfo) throws ConfigHandlerError, DBError {
        this.docController = aDocController;
        this.xDialog = xDialog;
        this.shortinfo = sShortinfo;
        if (this.shortinfo != null) {
            final MagazineQuote magazineQuote = this.docController.getSourceController().getMagazineQuoteByShortinfo(this.shortinfo);
            this.fillTXTFields(magazineQuote);
        }
        this.xDialog.setTitle(this.docController.getLanguageController().__("Magazinequote"));
        this.docController.getDialogUtils().registerButtons(new String[][] { new String[] { "Import", this.docController.getLanguageController().__("Import") }, this.docController.getLanguageController().captionOK, this.docController.getLanguageController().captionCancel }, this.xDialog, this);
        this.docController.getDialogUtils().setTextForLBLs(new String[][] { this.docController.getSourceController().captionTitle, this.docController.getSourceController().captionSubtitle, this.docController.getSourceController().captionShortinfo, this.docController.getSourceController().captionQuotetext, this.docController.getSourceController().captionAuthor, this.docController.getSourceController().captionFromPage, this.docController.getSourceController().captionEditor, this.docController.getSourceController().captionToPage, this.docController.getSourceController().captionPublisher, this.docController.getSourceController().captionCityOfPublisher, this.docController.getSourceController().captionEditor, this.docController.getSourceController().captionYearOfPublication, this.docController.getSourceController().captionPublishedIn, this.docController.getSourceController().captionIssue, this.docController.getSourceController().captionVolume }, this.xDialog);
        this.docController.getLogger().fine("MagazineQuoteDialogHandler started");
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
            if (sName.equals("cmdOK")) {
                final String sInfo = this.docController.getDialogUtils().getTextFromTXT(this.xDialog, "Shortinfo");
                if (!sInfo.isEmpty()) {
                    try {
                        final Source src = this.docController.getSourceController().getSourceByShortinfo(sInfo);
                        if (src != null && !src.getShortinfo().equalsIgnoreCase(this.shortinfo)) DocumentController.getStaticMessageBox().showWarningBox(this.docController.getLanguageController().__("Magazinequote"), this.docController.getLanguageController().__("The brief description is already used by another source.")); else {
                            final MagazineQuote newMagazineQuote = this.generateMagazineQuote();
                            if (this.shortinfo == null) {
                                this.docController.getSourceController().addNewSource(newMagazineQuote);
                                DocumentController.getStaticMessageBox().showInfoBox(this.docController.getLanguageController().__("New magazinequote"), this.docController.getLanguageController().__("Magazinequote \"{0}\" added.", newMagazineQuote.getShortinfo()));
                            } else {
                                this.docController.getSourceController().updateSource(this.shortinfo, newMagazineQuote);
                                DocumentController.getStaticMessageBox().showInfoBox(this.docController.getLanguageController().__("Edit magazinequote"), this.docController.getLanguageController().__("Magazinequote \"{0}\" edited.", newMagazineQuote.getShortinfo()));
                            }
                            this.xDialog.endExecute();
                        }
                    } catch (final ASTError e) {
                        e.severe();
                    }
                } else DocumentController.getStaticMessageBox().showWarningBox(this.docController.getLanguageController().__("Magazinequote"), this.docController.getLanguageController().__("Please enter a brief description first."));
            } else if (sName.equals("cmdImport")) {
                try {
                    final MagazineQuote newMagazineQuote = this.generateMagazineQuote();
                    File bibTexFile = new File(this.docController.getPathUtils().getSystemPathFromFileURL(this.docController.getDialogUtils().chooseFileDialog(this.docController.getPathUtils().getPath(PathUtils.WORK), false, new String[][] { new String[] { this.docController.getLanguageController().__("BibTeX file (*.bib)"), "*.bib" } }, 0)[0]));
                    if (this.docController.getSourceController().importUtils.getEntryCountFromBibTex(bibTexFile) == 1) this.docController.getSourceController().importUtils.getSourceDataFromBibFile(bibTexFile, newMagazineQuote); else if (this.docController.getSourceController().importUtils.getEntryCountFromBibTex(bibTexFile) >= 1) {
                        final DialogController dialogController = this.docController.getDialogController();
                        new ChooseBibTexEntryDialogHandler(this.docController, dialogController.loadDialog("dlgChooseBibTexEntry.xdl"), newMagazineQuote, bibTexFile);
                        dialogController.showDialog();
                    } else DocumentController.getStaticMessageBox().showWarningBox(this.docController.getLanguageController().__("BibTeX import"), this.docController.getLanguageController().__("File contains no readable BibTeX entries."));
                    this.fillTXTFields(newMagazineQuote);
                } catch (final ASTError e) {
                    e.severe();
                }
            } else this.xDialog.endExecute();
        } catch (final com.sun.star.uno.Exception e) {
            this.docController.getLogger().severe("Can't handle event! " + e.getMessage());
        }
    }

    /**
     * Fill the TXT fields with the data source.
     *
     * @param aMagazineQuote
     */
    private void fillTXTFields(final MagazineQuote aMagazineQuote) {
        this.docController.getDialogUtils().setTextForTXT(this.xDialog, "Title", aMagazineQuote.getTitle(), false);
        this.docController.getDialogUtils().setTextForTXT(this.xDialog, "Subtitle", aMagazineQuote.getSubtitle(), false);
        this.docController.getDialogUtils().setTextForTXT(this.xDialog, "Shortinfo", aMagazineQuote.getShortinfo(), false);
        this.docController.getDialogUtils().setTextForTXT(this.xDialog, "Quotetext", aMagazineQuote.getQuotetext(), false);
        for (final String curAuthor : aMagazineQuote.getAuthor()) {
            if (!curAuthor.isEmpty()) {
                this.docController.getDialogUtils().setTextForTXT(this.xDialog, "Author", curAuthor + ";", true);
            }
        }
        for (final String curEditor : aMagazineQuote.getEditor()) {
            if (!curEditor.isEmpty()) {
                this.docController.getDialogUtils().setTextForTXT(this.xDialog, "Editor", curEditor + ";", true);
            }
        }
        this.docController.getDialogUtils().setTextForTXT(this.xDialog, "FromPage", aMagazineQuote.getFromPage(), false);
        this.docController.getDialogUtils().setTextForTXT(this.xDialog, "ToPage", aMagazineQuote.getToPage(), false);
        this.docController.getDialogUtils().setTextForTXT(this.xDialog, "Publisher", aMagazineQuote.getPublisher(), false);
        this.docController.getDialogUtils().setTextForTXT(this.xDialog, "CityOfPublisher", aMagazineQuote.getCityOfPublisher(), false);
        this.docController.getDialogUtils().setTextForTXT(this.xDialog, "Edition", aMagazineQuote.getEdition(), false);
        this.docController.getDialogUtils().setValueForNUM(this.xDialog, "YearOfPublication", Double.valueOf(aMagazineQuote.getYearOfPublication()));
        this.docController.getDialogUtils().setTextForTXT(this.xDialog, "PublishedIn", aMagazineQuote.getPublishedIn(), false);
        this.docController.getDialogUtils().setTextForTXT(this.xDialog, "Issue", aMagazineQuote.getIssue(), false);
        this.docController.getDialogUtils().setTextForTXT(this.xDialog, "Volume", aMagazineQuote.getVolume(), false);
    }

    /**
     * Generates a magazine quote from the text elements.
     *
     * @return New generated magazine quote
     */
    private MagazineQuote generateMagazineQuote() {
        final List<String> author = new ArrayList<String>();
        for (final StringTokenizer authors = new StringTokenizer(this.docController.getDialogUtils().getTextFromTXT(this.xDialog, "Author"), ";"); authors.hasMoreTokens(); ) author.add(authors.nextToken());
        final List<String> editor = new ArrayList<String>();
        for (final StringTokenizer editors = new StringTokenizer(this.docController.getDialogUtils().getTextFromTXT(this.xDialog, "Editor"), ";"); editors.hasMoreTokens(); ) editor.add(editors.nextToken());
        return new MagazineQuote(0, author, this.docController.getDialogUtils().getTextFromTXT(this.xDialog, "CityOfPublisher"), this.docController.getDialogUtils().getTextFromTXT(this.xDialog, "Edition"), editor, this.docController.getDialogUtils().getTextFromTXT(this.xDialog, "Issue"), this.docController.getDialogUtils().getTextFromTXT(this.xDialog, "PublishedIn"), this.docController.getDialogUtils().getTextFromTXT(this.xDialog, "Publisher"), this.docController.getDialogUtils().getTextFromTXT(this.xDialog, "Shortinfo"), this.docController.getDialogUtils().getTextFromTXT(this.xDialog, "Title"), this.docController.getDialogUtils().getTextFromTXT(this.xDialog, "FromPage"), this.docController.getDialogUtils().getTextFromTXT(this.xDialog, "ToPage"), this.docController.getDialogUtils().getTextFromTXT(this.xDialog, "Volume"), ((Double) this.docController.getDialogUtils().getValueFromNUM(this.xDialog, "YearOfPublication")).intValue(), this.docController.getDialogUtils().getTextFromTXT(this.xDialog, "Quotetext"), this.docController.getDialogUtils().getTextFromTXT(this.xDialog, "Subtitle"));
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
